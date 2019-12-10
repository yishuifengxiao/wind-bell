package com.yishuifengxiao.common.crawler;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishuifengxiao.common.crawler.builder.ExtractProducer;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.pool.SimpleThreadFactory;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;
import com.yishuifengxiao.common.crawler.utils.LocalCrawler;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 爬虫处理器<br/>
 * 负责调用线程池使用多线程进行爬虫任务处理
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class CrawlerProcessor extends Thread {

	private final static Logger log = LoggerFactory.getLogger(CrawlerProcessor.class);
	/**
	 * 未获得新的请求任务的连续总时间
	 */
	protected AtomicInteger stat = new AtomicInteger(0);
	/**
	 * 已成功提取信息的页面的数据
	 */
	private AtomicLong pageCount = new AtomicLong(0);
	/**
	 * 已经爬取的页面的数量
	 */
	private AtomicLong extractedCount = new AtomicLong(0);

	private ReentrantLock newUrlLock = new ReentrantLock();

	private Condition newUrlCondition = newUrlLock.newCondition();

	/**
	 * 当前任务
	 */
	private Task task;

	/**
	 * 爬虫的网页下载器，负责下载网页内容
	 */
	private Downloader downloader;

	/**
	 * 调度器，负责存取将要抓取的请求
	 */
	private Scheduler scheduler;

	/**
	 * 解析工具
	 */
	private ExtractProducer producer;
	/**
	 * 执行任务的线程池
	 */
	private ThreadPoolExecutor threadPool;

	/**
	 * 链接提取器，负责从内容中解析处理符合要求的链接
	 */
	private LinkExtract linkExtract;
	/**
	 * 内容解析器，负责从内容中解析出需要提取的内容
	 */
	private ContentExtract contentExtract;

	public CrawlerProcessor(Task task, Downloader downloader, Scheduler scheduler, ThreadPoolExecutor threadPool,
			LinkExtract linkExtract, ContentExtract contentExtract) {

		this.task = task;
		this.downloader = downloader;
		this.scheduler = scheduler;
		this.threadPool = threadPool;
		this.linkExtract = linkExtract;
		this.contentExtract = contentExtract;
		this.init();
	}

	/**
	 * 初始化组件信息
	 */
	private void init() {
		this.producer = new ExtractProducer(this.task.getCrawlerRule(), this.linkExtract, this.contentExtract,
				this.scheduler);
		if (this.threadPool == null) {
			this.threadPool = new ThreadPoolExecutor(this.task.getCrawlerRule().getThreadNum(),
					this.task.getCrawlerRule().getThreadNum() * 2, 1000L, TimeUnit.SECONDS,
					new LinkedBlockingDeque<Runnable>(), new SimpleThreadFactory(this.task.getName()),
					new ThreadPoolExecutor.CallerRunsPolicy());
		}

	}

	@Override
	public void run() {
		log.debug("Started crawling {} , preparing to crawl data", this.task.getName());
		while (true) {

			// 先休眠一段时间
			int sleepSeconds = this.sleep();

			if (this.task.getStatu() == Statu.STOP) {
				// 爬虫处于停止状态
				// 如果爬虫被手动停止就中断线程
				break;
			}

			if (this.task.getStatu() != Statu.PAUSE) {
				// 如果爬虫不是暂停状态就默认其为运行状态
				String url = this.scheduler.poll();
				log.debug("new processing request is {} ", url);
				if (StringUtils.isBlank(url)) {
					// 再次等待一段时间
					this.waitNewUrl(sleepSeconds);
					if (this.stat.getAndSet(sleepSeconds * 2) > this.task.getCrawlerRule().getWaitTime()) {
						// 连续请求时间，超过指定的阀值还没有获取到新的请求，表明爬虫的爬取任务已经完成
						this.stat.set(0);
						break;
					}
				} else {
					// 每次新的请求时，需要记得将阀值归零
					this.stat.set(0);
					// 处理新的资源
					this.work(url);
				}
			}

		}

		// 清理回收资源
		clear();
	}

	/**
	 * 正式的进行资源处理
	 * 
	 * @param request
	 */
	private void work(final String url) {
		Page page = null;
		try {

			// 处理请求
			// 下载下载后的page信息里包含request信息
			page = this.downloader.down(url);
			if (page == null) {
				throw new ServiceException(
						new StringBuffer("Web page (").append(url).append(" ) download results are empty").toString());
			}
			// 补全URL信息
			page.setUrl(url);
			processRequest(page);

		} catch (Exception e) {
			if (this.task.getCrawlerListener() != null) {
				this.task.getCrawlerListener().onError(page, e);
			}

			log.error("process request " + url + " error", e);
		} finally {
			signalNewUrl();
		}

	}

	/**
	 * 处理新资源
	 * 
	 * @param request
	 */
	private void processRequest(final Page page) {

		this.threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					LocalCrawler.put(task);
					// 解析数据
					producer.extract(page);
					// 增加抓取的链接总数量
					pageCount.addAndGet(page.getLinks().size());
					// 增加已爬取网页的数据
					extractedCount.incrementAndGet();
					// 爬取成功消息
					if (task.getCrawlerListener() != null) {
						task.getCrawlerListener().onSuccess(page);
					}
				} catch (Exception e) {
					if (task.getCrawlerListener() != null) {
						task.getCrawlerListener().onError(page, e);
					}
					log.error("process request " + page + " error", e);
				} finally {
					LocalCrawler.clear();
					signalNewUrl();
				}

			}
		});

	}

	/**
	 * 等待新的处理资源
	 */
	private void waitNewUrl(int sleepSeconds) {
		newUrlLock.lock();
		try {
			if (this.threadPool.getActiveCount() == 0) {
				return;
			}
			newUrlCondition.await(sleepSeconds, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			newUrlLock.unlock();
		}
	}

	/**
	 * 清理并回收资源
	 */
	private void clear() {

		this.threadPool.purge();
		this.threadPool.shutdownNow();
		System.gc();
	};

	/**
	 * 唤醒所有等待的线程
	 */
	private void signalNewUrl() {
		newUrlLock.lock();
		try {
			newUrlCondition.signalAll();
		} finally {
			newUrlLock.unlock();
		}
	}

	/**
	 * 休眠一段时间
	 */
	private int sleep() {
		// 休眠时间为秒
		// 休眠时间为 sleepTime 的0倍到两倍 之间的一个随机值
		// 用来模仿自然请求，防止封杀
		int randomSleepTime = RandomUtils.nextInt(0, this.task.getCrawlerRule().getInterval() * 2);
		log.debug(
				"The crawler sleeps for {} seconds and simulates a manual request to prevent frequent requests from blocking.",
				randomSleepTime);
		try {
			Thread.sleep(1000 * randomSleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return randomSleepTime;
	}

	/**
	 * 抓取的链接总数量
	 * 
	 * @return
	 */
	protected long getAllTaskCount() {
		return pageCount.get();
	}

	/**
	 * 获取已经爬取的网页的数量的值
	 * 
	 * @return
	 */
	protected long getExtractedTaskCount() {
		return extractedCount.get();
	}

	/**
	 * 获取到爬虫线程池里最大活跃的线程数
	 * 
	 * @return
	 */
	protected int getActiveCount() {
		return this.threadPool == null ? 0 : this.threadPool.getActiveCount();
	}

	/**
	 * 判断此处理器是否处于活跃状态
	 * 
	 * @return 当处理器线程为活着且线程池里的活跃线程数大于0时才认为任务没有处理完
	 */
	protected boolean isActive() {
		return this.isAlive() && !this.threadPool.isShutdown() && !this.threadPool.isTerminated();
	}

	/**
	 * 获取线程池
	 * 
	 * @return
	 */
	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

}
