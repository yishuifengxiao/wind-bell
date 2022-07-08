package com.yishuifengxiao.common.crawler;

import java.util.Arrays;
import java.util.Objects;
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

import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.pool.SimpleThreadFactory;

/**
 * 风铃虫处理器<br/>
 * 负责调用线程池使用多线程进行风铃虫任务处理
 * 
 * @author yishui
 * @version 1.0.0
 */
public class CrawlerProcessor extends Thread {

	private final static Logger log = LoggerFactory.getLogger(CrawlerProcessor.class);

	private final ReentrantLock newUrlLock = new ReentrantLock();

	private final Condition newUrlCondition = newUrlLock.newCondition();
	/**
	 * 状态监视器
	 */
	private final Monitor monitor = new Monitor();
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
	 * 当前任务
	 */
	final Crawler crawler;

	/**
	 * 风铃虫的状态：运行中、停止、暂停
	 */
	private Statu statu = Statu.STOP;

	/**
	 * 执行任务的线程池
	 */
	private ThreadPoolExecutor threadPool;

	public CrawlerProcessor(Crawler crawler, ThreadPoolExecutor threadPool) {
		this.crawler = crawler;
		this.threadPool = threadPool;
		this.setName(crawler.getUuid());
		this.init();
	}

	/**
	 * 初始化组件信息
	 */
	private void init() {

		// 生成线程池
		if (this.threadPool == null) {
			this.threadPool = new ThreadPoolExecutor(this.crawler.getCrawlerRule().getThreadNum(),
					this.crawler.getCrawlerRule().getThreadNum() * 2, 300L, TimeUnit.SECONDS,
					new LinkedBlockingDeque<Runnable>(), new SimpleThreadFactory(this.crawler.getUuid()),
					new ThreadPoolExecutor.CallerRunsPolicy());
		}
		// 注入起始链接
		Arrays.asList(StringUtils
				.splitByWholeSeparatorPreserveAllTokens(this.crawler.getCrawlerRule().getLink().getStartUrl(), ","))
				.stream().filter(Objects::nonNull).map(t -> new Request(t, t))
				.forEach(t -> this.crawler.scheduler.push(this.crawler, t));

	}

	@Override
	public void run() {

		// 更新运行状态
		this.update(Statu.RUNNING);

		log.debug("【id:{} , name:{} 】  The instance is started successfully. Start to grab data",
				this.crawler.getUuid(), this.crawler.getName());

		// 运行状态监控检查
		this.monitor.setDaemon(true);
		this.monitor.start();

		// 开始任务
		while (true) {

			// 先休眠一段时间
			long sleepSeconds = this.sleep();

			if (this.getStatu() == Statu.STOP) {
				// 风铃虫处于停止状态
				break;
			}

			if (this.checkStat()) {
				// 爬取任务是否已经完成
				this.kill();
				break;
			}

			// 如果风铃虫不是停止状态就默认其为运行状态
			Request request = this.crawler.scheduler.poll(this.crawler);
			log.debug("【id:{} , name:{} 】  The newly acquired request task is {}", this.crawler.getUuid(),
					this.crawler.getName(), request);
			if (null == request) {
				this.crawler.crawlerListener.onNullRquest(this.crawler);
				// 再次等待一段时间
				this.waitNewUrl(sleepSeconds);
				this.stat.addAndGet(sleepSeconds * 2);

			} else {
				// 每次新的请求时，需要记得将阀值归零
				this.stat.set(0);
				// 处理新的资源
				this.threadPool.execute(new CrawlerWorker(request, this.crawler.downloader, this));
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

		if (this.interceptCount.get() > this.crawler.getCrawlerRule().getSite().getInterceptCount()) {
			// 连续多次检测到失败的标志，表示一倍服务器封锁
			log.info(
					"【id:{} , name:{} 】 The crawler instance has been blocked by the target server, and the crawler instance is automatically tentatively run.",
					this.crawler.getUuid(), this.crawler.getName());
			this.interceptCount.set(0);
			this.crawler.crawlerListener.exitOnBlock(this.crawler);
			return true;
		}
		if (this.stat.get() > this.crawler.getCrawlerRule().getWaitTime()) {
			// 连续请求时间，超过指定的阀值还没有获取到新的请求，表明风铃虫的爬取任务已经完成
			log.info("The crawler instance {} has not received a new task for a long time and will automatically stop",
					this.crawler.getUuid());
			this.stat.set(0);
			this.crawler.crawlerListener.exitOnFinish(this.crawler);
			return true;
		}

		return false;
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
	 * 唤醒所有等待的线程
	 */
	void signalNewUrl() {
		newUrlLock.lock();
		try {
			newUrlCondition.signalAll();
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
	 * 休眠一段时间并返回休眠的时间
	 * 
	 * @return 休眠的时间,单位毫秒
	 */
	private long sleep() {
		if (this.crawler.getCrawlerRule().getInterval() <= 0) {
			return 0;
		}
		// 休眠时间为毫秒
		// 休眠时间为 sleepTime 的0倍到两倍 之间的一个随机值
		// 用来模仿自然请求，防止封杀
		long randomSleepTime = RandomUtils.nextLong(0, this.crawler.getCrawlerRule().getInterval() * 2);
		try {
			Thread.sleep(randomSleepTime);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return randomSleepTime;
	}

	/**
	 * 设置实例的状态
	 * 
	 * @param statu 实例的状态
	 */
	private synchronized void update(Statu statu) {
		this.statu = statu;
		// 通知状态发生变化了
		this.crawler.statuObserver.update(this.crawler, statu);
	}

	/**
	 * 停止实例的运行
	 */
	synchronized void kill() {
		this.statu = Statu.STOP;
		// 如果风铃虫被手动停止就中断线程
		this.threadPool.shutdown();
	}

	/**
	 * 获取实例的状态
	 * 
	 * @return 实例的状态
	 */
	protected synchronized Statu getStatu() {
		return this.statu;
	}

	/**
	 * 获取到风铃虫线程池里最大活跃的线程数
	 * 
	 * @return 风铃虫线程池里最大活跃的线程数
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

		if (this.getStatu() == Statu.RUNNING) {
			return true;
		}

		return !this.threadPool.isTerminated();
	}

	/**
	 * 获取线程池
	 * 
	 * @return 线程池实例
	 */
	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	/**
	 * 连续拦截的次数加一，然后返回增加后的数据
	 * 
	 * @return 增加后的数据
	 */
	synchronized long incrementAndGet() {
		return interceptCount.incrementAndGet();
	}

	/**
	 * 清空拦截次数
	 */
	synchronized void clearInterceptCount() {
		interceptCount.set(0);
	}

	/**
	 * 增加解析任务完成的数量
	 */
	synchronized void incrementTaskCount() {
		taskCount.incrementAndGet();
	}

	/**
	 * 增加失败任务的数量
	 */
	synchronized void incrementFailCount() {
		failCount.incrementAndGet();
	}

	private class Monitor extends Thread {

		@Override
		public void run() {

			while (true) {

				try {
					Thread.sleep(1000 * 30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				/*
				 * <pre> 两种情况下认为风铃虫任务已经完成 <br/> 1. 任务处理器中线程池中的活跃线程数为0且任务管理器线程处于非活跃状态 <br/> 2.
				 * 风铃虫的状态为停止状态 </pre>
				 */
				if ((!CrawlerProcessor.this.isActive())) {

					log.info("【id:{} , name:{} 】 The running state of the instance changes to {}",
							CrawlerProcessor.this.crawler.getUuid(), CrawlerProcessor.this.crawler.getName(),
							Statu.STOP);
					CrawlerProcessor.this.update(Statu.STOP);
					break;
				}

			}

		}

	}

}
