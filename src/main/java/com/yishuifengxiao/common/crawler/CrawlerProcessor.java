package com.yishuifengxiao.common.crawler;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishuifengxiao.common.crawler.builder.ExtractBuilder;
import com.yishuifengxiao.common.crawler.builder.impl.SimpleExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.ResultData;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.listener.CrawlerListener;
import com.yishuifengxiao.common.crawler.monitor.StatuObserver;
import com.yishuifengxiao.common.crawler.pipeline.Pipeline;
import com.yishuifengxiao.common.crawler.pool.SimpleThreadFactory;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;
import com.yishuifengxiao.common.crawler.utils.LocalCrawler;
import com.yishuifengxiao.common.crawler.utils.RegexFactory;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 风铃虫处理器<br/>
 * 负责调用线程池使用多线程进行风铃虫任务处理
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class CrawlerProcessor extends Thread {

	private final static Logger log = LoggerFactory.getLogger(CrawlerProcessor.class);
	/**
	 * 状态监视器
	 */
	private final Monitor monitor = new Monitor();
	/**
	 * 解析器生成器
	 */
	private final ExtractBuilder extractBuilder = new SimpleExtractBuilder();
	/**
	 * 未获得新的请求任务的连续总时间
	 */
	private final AtomicLong stat = new AtomicLong(0);
	/**
	 * 本实例解析成功的任务数据
	 */
	protected final AtomicLong taskCount = new AtomicLong(0);
	/**
	 * 本实例解析失败的任务数据
	 */
	protected final AtomicLong failCount = new AtomicLong(0);
	/**
	 * 被服务器连续拦截的次数
	 */
	private final AtomicLong interceptCount = new AtomicLong(0);
	/**
	 * 状态观察者
	 */
	private final StatuObserver statuObserver;

	private final ReentrantLock newUrlLock = new ReentrantLock();

	private final Condition newUrlCondition = newUrlLock.newCondition();

	/**
	 * 当前任务
	 */
	private final Task task;

	/**
	 * 风铃虫的网页下载器，负责下载网页内容
	 */
	private final Downloader downloader;

	/**
	 * 调度器，负责存取将要抓取的请求
	 */
	private final Scheduler scheduler;

	/**
	 * 事件监听器
	 */
	private final CrawlerListener crawlerListener;

	/**
	 * 内容输出
	 */
	private final Pipeline pipeline;

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

	public CrawlerProcessor(Task task, StatuObserver statuObserver, Downloader downloader, Scheduler scheduler,
			ThreadPoolExecutor threadPool, LinkExtract linkExtract, ContentExtract contentExtract, Pipeline pipeline,
			CrawlerListener crawlerListener) {
		this.statuObserver = statuObserver;
		this.task = task;
		this.downloader = downloader;
		this.scheduler = scheduler;
		this.threadPool = threadPool;
		this.linkExtract = linkExtract;
		this.contentExtract = contentExtract;
		this.pipeline = pipeline;
		this.crawlerListener = crawlerListener;
		this.setName(task.getName());
		this.init();
	}

	/**
	 * 初始化组件信息
	 */
	private void init() {
		// 生成链接解析器
		this.linkExtract = extractBuilder.createLinkExtract(this.task.getCrawlerRule().getLink(), this.linkExtract);
		// 生成内容解析器
		this.contentExtract = extractBuilder.createContentExtract(this.task.getCrawlerRule().getContent(),
				this.contentExtract);
		// 生成线程池
		if (this.threadPool == null) {
			this.threadPool = new ThreadPoolExecutor(this.task.getCrawlerRule().getThreadNum(),
					this.task.getCrawlerRule().getThreadNum() * 2, 300L, TimeUnit.SECONDS,
					new LinkedBlockingDeque<Runnable>(), new SimpleThreadFactory(this.task.getName()),
					new ThreadPoolExecutor.CallerRunsPolicy());
		}

		// 注入起始链接
		this.scheduler.push(StringUtils
				.splitByWholeSeparatorPreserveAllTokens(this.task.getCrawlerRule().getLink().getStartUrl(), ","));

	}

	@Override
	public void run() {
		log.debug("Started crawling {} , preparing to crawl data", this.task.getName());
		// 运行状态监控检查
		this.monitor.start();
		// 开始任务
		while (true) {

			if (this.checkStat()) {
				// 检测到任务已经完成
				break;
			}

			// 先休眠一段时间
			long sleepSeconds = this.sleep();

			if (this.task.getStatu() == Statu.STOP) {
				// 风铃虫处于停止状态
				// 如果风铃虫被手动停止就中断线程
				break;
			}

			// 如果风铃虫不是停止状态就默认其为运行状态
			String url = this.scheduler.poll();
			log.debug("The new processing request for the crawler instance {} is {}", this.task.getName(), url);
			if (StringUtils.isBlank(url)) {
				this.crawlerListener.onNullRquest(this.task);
				// 再次等待一段时间
				this.waitNewUrl(sleepSeconds);
				this.stat.addAndGet(sleepSeconds * 2);

			} else {
				// 每次新的请求时，需要记得将阀值归零
				this.stat.set(0);
				// 处理新的资源
				this.work(url);
			}

		}

		// 清理回收资源
		clear();
	}

	/**
	 * 检查爬取任务是否已经完成
	 * 
	 * @return 完成返回为true,否则为false
	 */
	private boolean checkStat() {

		if (this.interceptCount.get() > this.task.getCrawlerRule().getSite().getInterceptCount()) {
			// 连续多次检测到失败的标志，表示一倍服务器封锁
			log.info(
					"The crawler instance {} has been blocked by the target server, and the crawler instance is automatically tentatively run.",
					this.task.getName());
			this.interceptCount.set(0);
			this.crawlerListener.exitOnBlock(this.task);
			return true;
		}
		if (this.stat.get() > this.task.getCrawlerRule().getWaitTime()) {
			// 连续请求时间，超过指定的阀值还没有获取到新的请求，表明风铃虫的爬取任务已经完成
			log.info("The crawler instance {} has not received a new task for a long time and will automatically stop",
					this.task.getName());
			this.stat.set(0);
			this.crawlerListener.exitOnFinish(this.task);
			return true;
		}

		return false;
	}

	/**
	 * 根据资源调度器的调度资源进行任务处理
	 * 
	 * @param url 资源调度器的调度资源
	 */
	private void work(final String url) {

		this.threadPool.execute(() -> {
			Page page = null;
			try {

				// 处理请求
				// 下载下载后的page信息里包含request信息
				page = downloader.down(task.getCrawlerRule().getSite(), url);
				if (page == null) {
					throw new ServiceException(new StringBuffer("Web page (").append(url)
							.append(" ) download results are empty").toString());
				}

				// 补全URL信息
				page.setUrl(url);

				// 下载成功
				crawlerListener.onDownSuccess(task, page.getUrl(), page.getRedirectUrl(), page.getCode(),
						page.getRawTxt());

				// 服务器封杀检查
				intercepCheck(page);
				// 资源处理过程真正的资源
				processRequest(page);

			} catch (Exception e) {

				// 下载失败
				crawlerListener.onDownError(task, page, e);

				log.info("process request " + url + " error", e);
			} finally {
				signalNewUrl();
			}
		}

		);

	}

	/**
	 * 服务器封杀检查
	 * 
	 * @param page 下载的网页
	 */
	private void intercepCheck(Page page) {
		if (task.getCrawlerRule().getSite().statCheck()) {
			// 开启失败校验时才启用
			boolean match = RegexFactory.find(task.getCrawlerRule().getSite().getFailureMark(), page.getRawTxt());
			if (match) {
				interceptCount.incrementAndGet();
				log.debug("The crawler instance {} was detected by the server for the {} time", task.getName(),
						interceptCount.get());

			} else {
				// 重置
				interceptCount.set(0);
			}
		}

	}

	/**
	 * 资源处理
	 * 
	 * @param page 下载后的网页资源
	 */
	private void processRequest(final Page page) {
		try {
			LocalCrawler.put(task);
			// 进行真正的网页内容解析操作
			// 解析链接数据
			linkExtract.extract(page);
			// 存储链接
			page.getLinks().parallelStream().forEach(scheduler::push);
			// 抽取内容
			contentExtract.extract(page);
			// 输出数据
			output(pipeline, page);
			// 解析成功任务数
			taskCount.incrementAndGet();
			// 解析成功消息
			crawlerListener.onExtractSuccess(task, page);
		} catch (Exception e) {
			// 解析失败的任务数量
			failCount.incrementAndGet();
			// 解析失败
			crawlerListener.onExtractError(task, page, e);
			log.info("process request " + page + " error", e);
		} finally {
			LocalCrawler.clear();
			signalNewUrl();
		}
	}

	/**
	 * 输出解析后的数据
	 * 
	 * @param pipeline 数据输出器
	 * @param page     解析后的数据信息
	 * @throws ServiceException
	 */
	private void output(final Pipeline pipeline, final Page page) throws ServiceException {

		// 输出数据
		if (!page.isSkip()) {
			// 输出数据
			ResultData resultData = new ResultData().setData(page.getAllResultItem()).setUrl(page.getUrl())
					.setRedirectUrl(page.getRedirectUrl()).setTask(task);
			pipeline.recieve(resultData);
		}
	}

	/**
	 * 等待新的处理资源
	 * 
	 * @param sleepSeconds 单位毫秒
	 */
	private void waitNewUrl(long sleepSeconds) {
		newUrlLock.lock();
		try {
			if (this.threadPool.getActiveCount() == 0) {
				return;
			}
			newUrlCondition.await(sleepSeconds, TimeUnit.MILLISECONDS);
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
	 * 休眠一段时间并返回休眠的时间
	 * 
	 * @return 休眠的时间,单位毫秒
	 */
	private long sleep() {
		if (this.task.getCrawlerRule().getInterval() == 0) {
			return 0;
		}
		// 休眠时间为毫秒
		// 休眠时间为 sleepTime 的0倍到两倍 之间的一个随机值
		// 用来模仿自然请求，防止封杀
		long randomSleepTime = RandomUtils.nextLong(0, this.task.getCrawlerRule().getInterval() * 2);
		try {
			Thread.sleep(randomSleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return randomSleepTime;
	}

	/**
	 * 获取到风铃虫线程池里最大活跃的线程数
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

	private class Monitor extends Thread {

		@Override
		public void run() {

			while (true) {
				/*
				 * <pre> 两种情况下认为风铃虫任务已经完成 <br/> 1. 任务处理器中线程池中的活跃线程数为0且任务管理器线程处于非活跃状态 <br/> 2.
				 * 风铃虫的状态为停止状态 </pre>
				 */
				if ((!CrawlerProcessor.this.isActive())) {
					CrawlerProcessor.this.statuObserver.update(CrawlerProcessor.this.task, Statu.STOP);
					log.info("风铃虫 {} 的状态变为 {}", CrawlerProcessor.this.task.getName(), Statu.STOP);
					break;
				}

				try {
					Thread.sleep(1000 * 60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

}
