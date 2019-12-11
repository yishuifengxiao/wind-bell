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

import com.yishuifengxiao.common.crawler.cache.InMemoryRequestCache;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.domain.model.ContentExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.downloader.impl.SimpleDownloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.listener.CrawlerListener;
import com.yishuifengxiao.common.crawler.listener.SimpleCrawlerListener;
import com.yishuifengxiao.common.crawler.monitor.SimpleStatuObserver;
import com.yishuifengxiao.common.crawler.monitor.StatuObserver;
import com.yishuifengxiao.common.crawler.pipeline.Pipeline;
import com.yishuifengxiao.common.crawler.pipeline.SimplePipeline;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;
import com.yishuifengxiao.common.crawler.scheduler.SchedulerDecorator;
import com.yishuifengxiao.common.crawler.scheduler.impl.SimpleScheduler;
import com.yishuifengxiao.common.crawler.simulator.SimpleSimulator;

/**
 * 爬虫对象
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public class Crawler implements Task, StatuObserver {
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
	protected Statu statu;
	/**
	 * 爬虫的定义
	 */
	private CrawlerRule crawlerRule;
	/**
	 * 爬虫的网页下载器，负责下载网页内容
	 */
	private Downloader downloader;
	/**
	 * 调度器，负责存取将要抓取的请求
	 */
	private Scheduler scheduler;
	/**
	 * 爬虫处理器，负责解析下载后的网页内容
	 */
	private CrawlerProcessor processor;
	/**
	 * 链接提取器，负责从内容中解析处理符合要求的链接
	 */
	private LinkExtract linkExtract;
	/**
	 * 内容解析器，负责从内容中解析出需要提取的内容
	 */
	private ContentExtract contentExtract;
	/**
	 * 内容输出
	 */
	private Pipeline pipeline;
	/**
	 * 请求缓存器，负责缓存所有需要抓取的网页的URL(包括历史记录)和已经爬取的url集合
	 */
	private RequestCache requestCache;
	/**
	 * 爬虫监听器
	 */
	private CrawlerListener crawlerListener;
	/**
	 * 运行的线程池
	 */
	private ThreadPoolExecutor threadPool;
	/**
	 * 爬虫状态观察者
	 */
	private StatuObserver statuObserver;

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
		log.info("The crawler instance {} has been manually stopped", this.name);
		this.statuChange();
	};

	/**
	 * 暂停运行<br/>
	 * <b>停止运行后不会清空爬虫示例</b>
	 */
	public void pause() {
		statu = Statu.PAUSE;
		// 注册一个爬虫实例
		register(this);
		log.info("The crawler instance {} has been manually suspended", this.name);
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
		Assert.notNull(crawlerRule, "爬虫规则配置不能为空");
		this.crawlerRule = crawlerRule;
		this.name = StringUtils.isNotBlank(crawlerRule.getName()) ? crawlerRule.getName()
				: UUID.randomUUID().toString();
		// 初始化数据
		this.initData();

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
	public final static SimulatorData testContent(String url, SiteRule siteRule,
			ContentExtractRule contentExtractRule) {
		return new SimpleSimulator().extract(url, siteRule, contentExtractRule);
	}

	/**
	 * 测试链接提取规则
	 * 
	 * @param siteRule 站点规则
	 * @param linkRule 链接提取规则
	 * @return
	 */
	public final static SimulatorData testLink(SiteRule siteRule, LinkRule linkRule) {
		return new SimpleSimulator().link(siteRule, linkRule);
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
			this.statuObserver.update(this, this.statu);
		}
	}

	/**
	 * 初始化组件<br/>
	 * 注意组件的初始化顺序有先后要求
	 */
	private void initComponents() {

		if (this.pipeline == null) {
			this.pipeline = new SimplePipeline();
		}

		if (this.downloader == null) {
			this.downloader = new SimpleDownloader(this.crawlerRule.getSite());
		}
		if (this.requestCache == null) {
			this.requestCache = new InMemoryRequestCache();
		}

		// 资源调度器
		this.scheduler = new SchedulerDecorator(this.requestCache,
				this.scheduler == null ? new SimpleScheduler() : this.scheduler);

		// 注入起始链接
		this.scheduler.push(
				StringUtils.splitByWholeSeparatorPreserveAllTokens(this.crawlerRule.getLink().getStartUrl(), ","));

		if (this.processor == null) {
			this.processor = new CrawlerProcessor(this, this, this.downloader, this.scheduler, this.threadPool,
					this.linkExtract, this.contentExtract, this.pipeline);
		}
		if (this.statuObserver == null) {
			// 添加一个爬虫状态观察者
			this.statuObserver = new SimpleStatuObserver();
		}

		if (this.crawlerListener == null) {
			this.crawlerListener = new SimpleCrawlerListener();
		}
	}

	/**
	 * 数据初始化
	 */
	private void initData() {
		if (this.crawlerRule.getThreadNum() == null || this.crawlerRule.getThreadNum() <= 0) {
			this.crawlerRule.setThreadNum(SiteConstant.DEFAULT_THREAD_NUM);
		}
		if (this.crawlerRule.getInterval() == null || this.crawlerRule.getInterval() <= 0) {
			this.crawlerRule.setInterval(SiteConstant.REQUEST_INTERVAL_TIME);
		}
		if (this.crawlerRule.getWaitTime() == null || this.crawlerRule.getWaitTime() <= 0) {
			this.crawlerRule.setWaitTime(SiteConstant.WAIT_TIME_FOR_CLOSE);
		}

		if (this.crawlerRule.getSite() == null) {
			this.crawlerRule.setSite(new SiteRule());
		}
		if (this.crawlerRule.getLink() == null) {
			throw new IllegalArgumentException("链接提取配置数据不正确");
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

		if (this.crawlerRule.getSite().getInterceptCount() == null) {
			this.crawlerRule.getSite().setInterceptCount(SiteConstant.INTERCEPT_RETRY_COUNT);
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
		Assert.notNull(crawlerRule, "配置规则不能为空");
		this.crawlerRule = crawlerRule;
		return this;
	}

	public Downloader getDownloader() {
		return downloader;
	}

	public Crawler setDownloader(Downloader downloader) {
		Assert.notNull(downloader, "下载器不能为空");
		this.downloader = downloader;
		return this;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	@Override
	public CrawlerListener getCrawlerListener() {
		return crawlerListener;
	}

	public Crawler setCrawlerListener(CrawlerListener crawlerListener) {
		Assert.notNull(crawlerListener, "事件监听器不能为空");
		this.crawlerListener = crawlerListener;
		return this;
	}

	public LinkExtract getLinkExtract() {
		return linkExtract;
	}

	public Crawler setLinkExtract(LinkExtract linkExtract) {
		Assert.notNull(linkExtract, "链接解析器不能为空");
		this.linkExtract = linkExtract;
		return this;
	}

	public ContentExtract getContentExtract() {
		return contentExtract;
	}

	public Crawler setContentExtract(ContentExtract contentExtract) {
		Assert.notNull(contentExtract, "内容解析器不能为空");
		this.contentExtract = contentExtract;
		return this;
	}

	public Pipeline getPipeline() {
		return pipeline;
	}

	public Crawler setPipeline(Pipeline pipeline) {
		Assert.notNull(pipeline, "信息输出器不能为空");
		this.pipeline = pipeline;
		return this;
	}

	public ThreadPoolExecutor getThreadPool() {
		return this.threadPool != null ? this.threadPool : this.processor.getThreadPool();
	}

	public Crawler setThreadPool(ThreadPoolExecutor threadPool) {
		Assert.notNull(threadPool, "ThreadPoolExecutor不能为空");
		this.threadPool = threadPool;
		return this;
	}

	public RequestCache getRequestCache() {
		return requestCache;
	}

	public Crawler setRequestCache(RequestCache requestCache) {
		Assert.notNull(requestCache, "资源缓存器不能为空");
		this.requestCache = requestCache;
		return this;
	}

	public StatuObserver getStatuObserver() {
		return statuObserver;
	}

	public Crawler setStatuObserver(StatuObserver statuObserver) {
		Assert.notNull(statuObserver, "状态观察者不能为空");
		this.statuObserver = statuObserver;
		return this;
	}

	public Crawler setScheduler(Scheduler scheduler) {
		Assert.notNull(scheduler, "资源调度器不能为空");
		this.scheduler = scheduler;
		return this;
	}

	/**
	 * 已成功提取信息的页面的数据总数
	 * 
	 * @return
	 */
	public long getAllTaskCount() {
		return this.processor.getAllTaskCount();
	}

	/**
	 * 获取已经爬取的网页的数量的值
	 * 
	 * @return
	 */
	public long getExtractedTaskCount() {
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
		StringBuilder builder = new StringBuilder();
		builder.append("Crawler [name=").append(name).append(",scheduler=").append(scheduler.getName()).append("]");
		return builder.toString();
	}

	@Override
	public void update(Task task, Statu statu) {
		if (Statu.STOP == statu) {
			// 移除一个爬虫实例
			remove(this.name);
		}
		this.statu = statu;
		this.statuChange();
	}

}
