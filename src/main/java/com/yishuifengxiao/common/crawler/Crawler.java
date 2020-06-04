package com.yishuifengxiao.common.crawler;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.cache.InMemoryRequestCache;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
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
import com.yishuifengxiao.common.crawler.scheduler.remover.DuplicateRemover;
import com.yishuifengxiao.common.crawler.scheduler.remover.SimpleDuplicateRemover;
import com.yishuifengxiao.common.crawler.simulator.SimpleSimulator;

/**
 * 风铃虫
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public class Crawler implements Task, StatuObserver {
	private final static Logger log = LoggerFactory.getLogger(Crawler.class);
	/**
	 * 该实例的唯一ID
	 */
	private final String uuid = UUID.randomUUID().toString();
	/**
	 * 该实例的名字
	 */
	private String name;

	/**
	 * 风铃虫的启动时间
	 */
	private LocalDateTime startTime;

	/**
	 * 风铃虫的状态：运行中、停止、暂停
	 */
	protected Statu statu;
	/**
	 * 风铃虫的定义
	 */
	private CrawlerRule crawlerRule;
	/**
	 * 请求去重器
	 */
	private DuplicateRemover duplicateRemover;
	/**
	 * 风铃虫处理器，负责解析下载后的网页内容
	 */
	private CrawlerProcessor processor;

	/**
	 * 运行的线程池
	 */
	private ThreadPoolExecutor threadPool;
	/**
	 * 风铃虫的网页下载器，负责下载网页内容
	 */
	Downloader downloader;
	/**
	 * 调度器，负责存取将要抓取的请求
	 */
	Scheduler scheduler;

	/**
	 * 链接提取器，负责从内容中解析处理符合要求的链接
	 */
	LinkExtract linkExtract;
	/**
	 * 内容解析器，负责从内容中解析出需要提取的内容
	 */
	ContentExtract contentExtract;
	/**
	 * 内容输出
	 */
	Pipeline pipeline;
	/**
	 * 请求缓存器，负责缓存所有需要抓取的网页的URL(包括历史记录)和已经爬取的url集合
	 */
	RequestCache requestCache;
	/**
	 * 风铃虫监听器
	 */
	CrawlerListener crawlerListener;

	/**
	 * 风铃虫状态观察者
	 */
	StatuObserver statuObserver;

	/**
	 * 异步启动一个一个风铃虫实例
	 */
	@Override
	public void start() {
		// 组件初始化
		this.initComponents();
		if (statu != Statu.RUNNING) {
			this.statu = Statu.RUNNING;
			this.startTime = LocalDateTime.now();
			this.processor.start();
			this.statuChange();
		}

	};

	/**
	 * 同步启动一个一个风铃虫实例
	 */

	public void run() {
		// 组件初始化
		this.start();
		while (this.statu == Statu.RUNNING) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	};

	/**
	 * 停止运行<br/>
	 * 
	 */
	@Override
	public void stop() {
		statu = Statu.STOP;
		log.info("【id:{} , name:{} 】   The crawler instance  has been manually stopped", this.getUuid(),
				this.getName());
		this.statuChange();
	};

	/**
	 * 清空数据
	 */
	public void clear() {
		if (null != this.requestCache) {
			this.requestCache.remove(this);
		}
		if (null != this.scheduler) {
			this.scheduler.clear(this);
		}
	}

	/**
	 * 创建一个默认的风铃虫实例
	 * 
	 * @param crawlerRule 规定定义
	 * @return 风铃虫实例
	 */
	public static Crawler create(CrawlerRule crawlerRule) {
		// 初始化数据
		Crawler crawler = new Crawler();
		crawler.crawlerRule = CrawlerBuilder.create(crawlerRule).build();
		return crawler;
	}

	private Crawler() {

	}

	/**
	 * 构建函数<br/>
	 * <b> 注意此构造方法不会校验规则定义</b>
	 * 
	 * @param crawlerRule
	 */
	protected Crawler(CrawlerRule crawlerRule) {
		this.crawlerRule = crawlerRule;
	}

	/**
	 * 测试网页下载器<br/>
	 * 使用默认下载器
	 * 
	 * @param url      测试网页的地址
	 * @param siteRule 站点规则
	 * @return 测试结果
	 */
	public final static SimulatorData testDown(String url, SiteRule siteRule) {
		return new SimpleSimulator().down(url, siteRule, null);
	}

	/**
	 * 测试网页下载器<br/>
	 * 使用自定义下载器
	 * 
	 * @param url        测试网页的地址
	 * @param siteRule   站点规则
	 * @param downloader 网页下载器
	 * @return 测试结果
	 */
	public final static SimulatorData testDown(String url, SiteRule siteRule, Downloader downloader) {
		return new SimpleSimulator().down(url, siteRule, downloader);
	}

	/**
	 * 测试内容提取规则<br/>
	 * 使用默认下载器
	 * 
	 * @param url                测试网页的地址
	 * @param siteRule           站点规则
	 * @param contentExtractRule 内容提取规则
	 * @return 测试结果
	 */
	public final static SimulatorData testContent(String url, SiteRule siteRule, ExtractRule contentExtractRule) {
		return new SimpleSimulator().extract(url, siteRule, contentExtractRule, null);
	}

	/**
	 * 测试内容提取规则<br/>
	 * 使用自定义下载器
	 * 
	 * @param url                测试网页的地址
	 * @param siteRule           站点规则
	 * @param contentExtractRule 内容提取规则
	 * @param downloader         网页下载器
	 * @return 测试结果
	 */
	public final static SimulatorData testContent(String url, SiteRule siteRule, ExtractRule contentExtractRule,
			Downloader downloader) {
		return new SimpleSimulator().extract(url, siteRule, contentExtractRule, downloader);
	}

	/**
	 * 内容匹配测试
	 * 
	 * @param url      测试目标地址
	 * @param siteRule 站点规则
	 * @param content  内容解析规则
	 * @return 测试结果
	 */
	public final static SimulatorData testMatcher(String url, SiteRule siteRule, ContentRule content) {
		return new SimpleSimulator().match(url, siteRule, content, null);
	}

	/**
	 * 内容匹配测试
	 * 
	 * @param url        测试目标地址
	 * @param siteRule   站点规则
	 * @param content    内容解析规则
	 * @param downloader 下载器
	 * @return 测试结果
	 */
	public final static SimulatorData testMatcher(String url, SiteRule siteRule, ContentRule content,
			Downloader downloader) {
		return new SimpleSimulator().match(url, siteRule, content, downloader);
	}

	/**
	 * 测试链接提取规则 <br/>
	 * 使用默认下载器
	 * 
	 * @param url      测试目标地址
	 * @param siteRule 站点规则
	 * @param linkRule 链接提取规则
	 * @return 测试结果
	 */
	public final static SimulatorData testLink(String url, SiteRule siteRule, LinkRule linkRule) {
		return new SimpleSimulator().link(url, siteRule, linkRule, null);
	}

	/**
	 * 测试链接提取规则<br/>
	 * 使用自定义下载器
	 * 
	 * @param url        测试目标地址
	 * @param siteRule   站点规则
	 * @param linkRule   链接提取规则
	 * @param downloader 网页下载器
	 * @return 测试结果
	 */
	public final static SimulatorData testLink(String url, SiteRule siteRule, LinkRule linkRule,
			Downloader downloader) {
		return new SimpleSimulator().link(url, siteRule, linkRule, downloader);
	}

	/**
	 * 风铃虫是否正在运行状态
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
			this.downloader = new SimpleDownloader();
		}
		if (this.requestCache == null) {
			this.requestCache = new InMemoryRequestCache();
		}

		if (this.duplicateRemover == null) {
			this.duplicateRemover = new SimpleDuplicateRemover();
		}

		// 资源调度器
		this.scheduler = new SchedulerDecorator(this.requestCache,
				this.scheduler == null ? new SimpleScheduler() : this.scheduler, this.duplicateRemover);

		if (this.crawlerListener == null) {
			this.crawlerListener = new SimpleCrawlerListener();
		}

		if (this.statuObserver == null) {
			// 添加一个风铃虫状态观察者
			this.statuObserver = new SimpleStatuObserver();
		}

		if (this.processor == null) {
			this.processor = new CrawlerProcessor(this, this.threadPool);
		}

	}

	/**
	 * 获取风铃虫定义规则
	 */
	@Override
	public CrawlerRule getCrawlerRule() {
		return this.crawlerRule;
	}

	/**
	 * 获取网页下载器
	 * 
	 * @return 网页下载器
	 */
	public Downloader getDownloader() {
		return downloader;
	}

	/**
	 * 设置网页下载器
	 * 
	 * @param downloader 网页下载器
	 * @return
	 */
	public Crawler setDownloader(Downloader downloader) {
		Assert.notNull(downloader, "下载器不能为空");
		this.downloader = downloader;
		return this;
	}

	/**
	 * 获取资源调度器
	 * 
	 * @return
	 */
	public Scheduler getScheduler() {
		return this.scheduler;
	}

	/**
	 * 获取事件监听器
	 */

	public CrawlerListener getCrawlerListener() {
		return this.crawlerListener;
	}

	/**
	 * 设置事件监听器
	 * 
	 * @param crawlerListener 事件监听器
	 * @return
	 */
	public Crawler setCrawlerListener(CrawlerListener crawlerListener) {
		Assert.notNull(crawlerListener, "事件监听器不能为空");
		this.crawlerListener = crawlerListener;
		return this;
	}

	/**
	 * 设置链接解析器
	 * 
	 * @return
	 */
	public LinkExtract getLinkExtract() {
		return linkExtract;
	}

	/**
	 * 设置链接解析器
	 * 
	 * @param linkExtract 链接解析器
	 * @return
	 */
	public Crawler setLinkExtract(LinkExtract linkExtract) {
		Assert.notNull(linkExtract, "链接解析器不能为空");
		this.linkExtract = linkExtract;
		return this;
	}

	/**
	 * 获取内容解析器
	 * 
	 * @return
	 */
	public ContentExtract getContentExtract() {
		return this.contentExtract;
	}

	/**
	 * 设置内容解析器
	 * 
	 * @param contentExtract
	 * @return
	 */
	public Crawler setContentExtract(ContentExtract contentExtract) {
		Assert.notNull(contentExtract, "内容解析器不能为空");
		this.contentExtract = contentExtract;
		return this;
	}

	/**
	 * 获取信息输出器
	 * 
	 * @return
	 */
	public Pipeline getPipeline() {
		return this.pipeline;
	}

	/**
	 * 设置信息输出器
	 * 
	 * @param pipeline
	 * @return
	 */
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

	/**
	 * 设置资源缓存器
	 * 
	 * @return
	 */
	public RequestCache getRequestCache() {
		return requestCache;
	}

	/**
	 * 设置资源缓存器
	 * 
	 * @param requestCache 资源缓存器
	 * @return
	 */
	public Crawler setRequestCache(RequestCache requestCache) {
		Assert.notNull(requestCache, "资源缓存器不能为空");
		this.requestCache = requestCache;
		return this;
	}

	/**
	 * 获取状态监听器
	 * 
	 * @return
	 */
	public StatuObserver getStatuObserver() {
		return this.statuObserver;
	}

	/**
	 * 设置状态监听器
	 * 
	 * @param statuObserver 状态监听器
	 * @return
	 */
	public Crawler setStatuObserver(StatuObserver statuObserver) {
		Assert.notNull(statuObserver, "状态观察者不能为空");
		this.statuObserver = statuObserver;
		return this;
	}

	/**
	 * 设置资源调度器
	 * 
	 * @param scheduler 资源调度器
	 * @return
	 */
	public Crawler setScheduler(Scheduler scheduler) {
		Assert.notNull(scheduler, "资源调度器不能为空");
		this.scheduler = scheduler;
		return this;
	}

	/**
	 * 获取所有的任务总数<br/>
	 * 注意此数量是在变化的，且应该在任务启动后调用
	 * 
	 * @return
	 */
	@Override
	public long getAllTaskCount() {
		return this.requestCache.getCount(this);
	}

	/**
	 * 获取本实例已经解析成功的网页的数量<br/>
	 * 注意此数量是在变化的，且应该在任务启动后调用
	 * 
	 * @return
	 */
	@Override
	public long getExtractedTaskCount() {
		return this.processor.taskCount.get();
	}

	/**
	 * 获取本实例已经解析失败的网页的数量<br/>
	 * 注意此数量是在变化的，且应该在任务启动后调用
	 * 
	 * @return
	 */
	@Override
	public long getFailTaskCount() {
		return this.processor.failCount.get();
	}

	/**
	 * 获取风铃虫实例的名字
	 * 
	 * @return 风铃虫实例的名字
	 */
	@Override
	public String getName() {
		if (StringUtils.isBlank(this.name)) {
			return this.uuid;
		}

		return this.name;
	}

	/**
	 * 设置风铃虫实例的名字
	 * 
	 * @param name 风铃虫实例的名字
	 * @return 风铃虫实例
	 */
	public Crawler setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * 该实例唯一的标识符
	 */
	@Override
	public String getUuid() {
		return this.uuid;
	}

	/**
	 * 获取风铃虫实例的启动时间
	 */
	@Override
	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	/**
	 * 获取风铃虫的状态
	 */
	@Override
	public Statu getStatu() {
		return this.statu;
	}

	@Override
	public void update(Task task, Statu statu) {
		this.statu = statu;
		this.statuChange();
		// 实例停止运行时关闭下载器，释放资源
		if (Statu.STOP == statu) {
			this.downloader.close();
		}
	}

	/**
	 * 获取请求去重器
	 * 
	 * @return 请求去重器
	 */
	public DuplicateRemover getDuplicateRemover() {
		return duplicateRemover;
	}

	/**
	 * 设置请求去重器
	 * 
	 * @param duplicateRemover 请求去重器
	 * @return
	 */
	public Crawler setDuplicateRemover(DuplicateRemover duplicateRemover) {
		this.duplicateRemover = duplicateRemover;
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Crawler [uuid=");
		builder.append(uuid);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
