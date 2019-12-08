package com.yishuifengxiao.common.crawler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;

import com.yishuifengxiao.common.crawler.builder.ExtractProducer;
import com.yishuifengxiao.common.crawler.cache.InMemoryRequestCache;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.domain.model.ContentExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.downloader.impl.SimpleDownloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.listener.CrawlerListener;
import com.yishuifengxiao.common.crawler.monitor.SimpleStatuObserver;
import com.yishuifengxiao.common.crawler.monitor.StatuObserver;
import com.yishuifengxiao.common.crawler.pipeline.Pipeline;
import com.yishuifengxiao.common.crawler.pipeline.SimplePipeline;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;
import com.yishuifengxiao.common.crawler.scheduler.SimpleScheduler;
import com.yishuifengxiao.common.crawler.simulator.SimpleSimulator;
import com.yishuifengxiao.common.crawler.task.InMemoryTaskManager;
import com.yishuifengxiao.common.crawler.task.TaskManager;

/**
 * 爬虫对象
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public class Crawler implements Task {
	private final static Logger log = LoggerFactory.getLogger(Crawler.class);
	/**
	 * 存储所有已经创建的爬虫实例
	 */
	private final static Map<String, Crawler> WORKER = new WeakHashMap<>();

	/**
	 * 爬虫的名字
	 */
	private String name;
	/**
	 * 爬虫的启动时间
	 */
	private LocalDateTime startTime;

	/**
	 * 爬虫的状态：运行中、停止、暂停
	 */
	private Statu statu;
	/**
	 * 爬虫的定义
	 */
	protected CrawlerRule crawlerRule;
	/**
	 * 爬虫的网页下载器，负责下载网页内容
	 */
	protected Downloader downloader;
	/**
	 * 调度器，负责存取将要抓取的请求
	 */
	protected Scheduler scheduler;
	/**
	 * 爬虫处理器，负责解析下载后的网页内容
	 */
	private CrawlerProcessor processor;
	/**
	 * 链接提取器，负责从内容中解析处理符合要求的链接
	 */
	protected LinkExtract linkExtract;
	/**
	 * 内容解析器，负责从内容中解析出需要提取的内容
	 */
	protected ContentExtract contentExtract;
	/**
	 * 解析工具
	 */
	protected ExtractProducer producer;
	/**
	 * 解析内容输出
	 */
	protected Pipeline pipeline;
	/**
	 * 请求缓存器，负责缓存所有需要抓取的网页的URL(包括历史记录)和已经爬取的url集合
	 */
	protected RequestCache requestCache;
	/**
	 * 任务管理器，负责进行任务管理
	 */
	protected TaskManager taskScheduler;
	/**
	 * 爬虫监听器
	 */
	protected CrawlerListener crawlerListener;
	/**
	 * 运行的线程池
	 */
	protected ThreadPoolExecutor threadPool;
	/**
	 * 爬虫状态观察者
	 */
	protected StatuObserver statuObserver;

	/**
	 * 启动一个一个爬虫实例
	 */
	public void start() {
		// 组件初始化
		initComponents();
		if (statu != Statu.RUNNING) {
			this.statu = Statu.RUNNING;
			this.startTime = LocalDateTime.now();
			this.processor.start();
			// 注册一个爬虫实例
			register(this);
			// 运行状态监控检查
			new Monitor().start();
			this.statuChange();
		}

	};

	/**
	 * 停止运行<br/>
	 * <b>停止运行后会清空爬虫示例</b>
	 */
	public void stop() {
		statu = Statu.STOP;
		// 移除一个爬虫实例
		remove(this.name);

	};

	/**
	 * 暂停运行<br/>
	 * <b>停止运行后不会清空爬虫示例</b>
	 */
	public void pause() {
		statu = Statu.PAUSE;
		// 注册一个爬虫实例
		register(this);
		this.statuChange();
	}

	/**
	 * 创建一个默认的爬虫实例
	 * 
	 * @return
	 */
	public static Crawler create(@Validated CrawlerRule crawlerRule) {
		return new Crawler(crawlerRule);
	}

	/**
	 * 根据名字获取爬虫的状态
	 * 
	 * @param name
	 * @return
	 */
	public static Statu statu(String name) {
		Crawler crawler = WORKER.get(name);
		if (crawler != null) {
			return crawler.statu;
		}
		return null;
	}

	public Crawler(CrawlerRule crawlerRule) {
		Assert.notNull(crawlerRule, "配置规则不能为空");
		this.crawlerRule = crawlerRule;
		this.name = StringUtils.isNotBlank(crawlerRule.getName()) ? crawlerRule.getName()
				: UUID.randomUUID().toString();
		// 初始化数据
		initData();

	}

	private class Monitor extends Thread {

		@Override
		public void run() {

			while (true) {
				/*
				 * <pre> 两种情况下认为爬虫任务已经完成 <br/> 1. 任务处理器中线程池中的活跃线程数为0且任务管理器线程处于非活跃状态 <br/> 2.
				 * 爬虫的状态为停止状态 </pre>
				 */

				if ((!Crawler.this.processor.isActive()) || Crawler.this.statu == Statu.STOP) {
					Crawler.this.statuChange();
					log.info("爬虫 {} 的状态变为 {}", Crawler.this.name, Crawler.this.statu);
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

	/**
	 * 注册一个爬虫实例
	 * 
	 * @param crawler
	 */
	private synchronized void register(Crawler crawler) {
		WORKER.put(crawler.name, crawler);
	}

	/**
	 * 移除一个爬虫实例
	 * 
	 * @param crawler
	 */
	private synchronized void remove(String name) {
		Assert.notNull(name, "移除爬虫实例时参数不能为空");
		Iterator<String> it = WORKER.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (StringUtils.equalsIgnoreCase(key, name)) {
				it.remove();
			}
		}
	}

	/**
	 * 根据爬虫名字获取一个运行实例
	 * 
	 * @param name
	 * @return
	 */
	public static Crawler get(String name) {
		Assert.notNull(name, "获取爬虫实例时参数不能为空");
		return WORKER.get(name);
	}

	/**
	 * 测试内容提取规则
	 * 
	 * @param url                测试网页的地址
	 * @param siteRule           站点规则
	 * @param contentExtractRule 内容提取规则
	 * @return
	 */
	public final static SimulatorData test(String url, SiteRule siteRule, ContentExtractRule contentExtractRule) {
		return new SimpleSimulator().extract(url, siteRule, contentExtractRule);
	}

	/**
	 * 获取到所有正在运行的实例
	 * 
	 * @return
	 */
	public static List<Crawler> getAllWorker() {
		return WORKER.keySet().parallelStream().map(t -> WORKER.get(t)).filter(t -> t != null)
				.collect(Collectors.toList());
	}

	/**
	 * 判断某个爬虫名字是否已经存在
	 * 
	 * @param name
	 * @return
	 */
	public static boolean extis(String name) {
		Assert.isNull(name, "判断爬虫是否存在时参数不能为空");
		return WORKER.containsKey(name);
	}

	/**
	 * 爬虫是否正在运行状态
	 * 
	 * @return
	 */
	public boolean isRun() {
		return this.statu == Statu.RUNNING;
	}

	/**
	 * 通知观察着状态已经发生变化
	 */
	private void statuChange() {
		if (this.statuObserver != null) {
			this.statuObserver.update(this);
		}
	}

	/**
	 * 初始化组件<br/>
	 * 注意组件的初始化顺序有先后要求
	 */
	private void initComponents() {
		Assert.notNull(this.crawlerRule, "爬虫定义规则不能为空");

		if (this.pipeline == null) {
			this.pipeline = new SimplePipeline();
		}

		if (this.downloader == null) {
			this.downloader = new SimpleDownloader(this.crawlerRule.getSite());
		}
		if (this.requestCache == null) {
			this.requestCache = new InMemoryRequestCache();
		}

		if (this.taskScheduler == null) {
			this.taskScheduler = new InMemoryTaskManager();
		}

		if (this.scheduler == null) {
			this.scheduler = new SimpleScheduler(this.requestCache, this.pipeline, this.taskScheduler);
		}

		this.producer = new ExtractProducer(this.crawlerRule, this.linkExtract, this.contentExtract, this.scheduler);

		// 注入起始链接
		this.scheduler.push(
				StringUtils.splitByWholeSeparatorPreserveAllTokens(this.crawlerRule.getLink().getStartUrl(), ","));

		if (this.processor == null) {
			this.processor = new CrawlerProcessor(this, this.downloader, this.scheduler, this.producer,
					this.threadPool);
		}
		if (this.statuObserver == null) {
			// 添加一个爬虫状态观察者
			this.statuObserver = new SimpleStatuObserver();
		}
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		if (this.crawlerRule == null) {
			throw new IllegalArgumentException("配置数据不能为空");
		}
		if (this.crawlerRule.getSite() == null) {
			this.crawlerRule.setSite(new SiteRule());
		}
		if (this.crawlerRule.getLink() == null) {
			throw new IllegalArgumentException("配置数据不正确");
		}
		if (StringUtils.isBlank(this.crawlerRule.getLink().getStartUrl())) {
			throw new IllegalArgumentException("起始链接不能为空");
		}
		if (this.crawlerRule.getLink().getRules() == null) {
			this.crawlerRule.getLink().setRules(Arrays.asList(RuleConstant.REGEX_MATCH_ALL));
		}
		if (this.crawlerRule.getSite().getHeaders() == null) {
			this.crawlerRule.getSite().setHeaders(new ArrayList<>());
		}

		if (this.crawlerRule.getContent() == null) {
			this.crawlerRule.setContent(new ContentRule());
		}
		if (this.crawlerRule.getContent().getExtractUrl() == null) {
			this.crawlerRule.getContent().setExtractUrl(RuleConstant.REGEX_MATCH_ALL);
		}

	}

	@Override
	public CrawlerRule getCrawlerRule() {
		return this.crawlerRule;
	}

	public Crawler setCrawlerRule(CrawlerRule crawlerRule) {
		this.crawlerRule = crawlerRule;
		return this;
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public Crawler setDownloader(Downloader downloader) {
		this.downloader = downloader;
		return this;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public Crawler setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		return this;
	}

	@Override
	public CrawlerListener getCrawlerListener() {
		return crawlerListener;
	}

	public Crawler setCrawlerListener(CrawlerListener crawlerListener) {
		this.crawlerListener = crawlerListener;
		return this;
	}

	public LinkExtract getLinkExtract() {
		return linkExtract;
	}

	public Crawler setLinkExtract(LinkExtract linkExtract) {
		this.linkExtract = linkExtract;
		return this;
	}

	public ContentExtract getContentExtract() {
		return contentExtract;
	}

	public Crawler setContentExtract(ContentExtract contentExtract) {
		this.contentExtract = contentExtract;
		return this;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public Crawler setPipeline(Pipeline pipeline) {
		this.pipeline = pipeline;
		return this;
	}

	public ThreadPoolExecutor getThreadPool() {
		return this.threadPool != null ? this.threadPool : this.processor.getThreadPool();
	}

	public Crawler setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
		return this;
	}

	public RequestCache getRequestCache() {
		return requestCache;
	}

	public Crawler setRequestCache(RequestCache requestCache) {
		this.requestCache = requestCache;
		return this;
	}

	public StatuObserver getStatuObserver() {
		return statuObserver;
	}

	public Crawler setStatuObserver(StatuObserver statuObserver) {
		this.statuObserver = statuObserver;
		return this;
	}

	public TaskManager getTaskScheduler() {
		return taskScheduler;
	}

	public Crawler setTaskScheduler(TaskManager taskScheduler) {
		this.taskScheduler = taskScheduler;
		return this;
	}

	/**
	 * 获取抓取的链接总数量
	 * 
	 * @return
	 */
	public int getAllTaskCount() {
		return this.processor.getAllTaskCount();
	}

	/**
	 * 获取已经爬取的网页的数量的值
	 * 
	 * @return
	 */
	public int getExtractedTaskCount() {
		return this.processor.getExtractedTaskCount();
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	@Override
	public Statu getStatu() {
		return this.statu;
	}

	@Override
	public String toString() {
		return "Crawler [name=" + name + "]";
	}

}
