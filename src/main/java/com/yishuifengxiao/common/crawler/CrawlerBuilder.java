package com.yishuifengxiao.common.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.model.ContentItem;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.HeaderRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 风铃虫规则构建器
 * 
 * @author yishui
 * @date 2019年12月13日
 * @version 1.0.0
 */
public class CrawlerBuilder {

	/**
	 * 每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀
	 */
	private Long interval = SiteConstant.REQUEST_INTERVAL_TIME;
	/**
	 * 超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束
	 */
	private Long waitTime = SiteConstant.WAIT_TIME_FOR_CLOSE;

	/**
	 * 风铃虫解析时线程数
	 */
	private Integer threadNum = SiteConstant.DEFAULT_THREAD_NUM;

	/**
	 * 浏览器标志，默认值为空，表示系统从众多内置标识符中随机选择一个
	 */
	private String userAgent;

	/**
	 * 用于指明当前流量的来源参考页面，默认为空，表示系统设置为当前请求的网页值
	 */
	private String referrer;

	/**
	 * 网页缓存策略，默认为 max-age=0
	 */
	private String cacheControl = SiteConstant.CACHE_CONTROL;
	/**
	 * 请求的cookie，默认为空
	 */
	private String cookieValue;
	/**
	 * 返回要遵循的最大重定向数。 重定向次数的限制旨在防止无限循环 <br/>
	 * <b>默认为50</b>
	 */
	private int maxRedirects = SiteConstant.MAX_REDIRECTS;

	/**
	 * 失败标志， 下载内容里包含此值时表示被服务器拦截，使用正则表达式，如果为空则不进行此校验
	 */
	private String failureMark;

	/**
	 * 拦截次数阀域值，连续多次在下载内容中获取到失败标识时的重试此次，超过此次数会关闭该风铃虫实例，默认为5
	 */
	private Integer interceptCount = SiteConstant.INTERCEPT_RETRY_COUNT;

	/**
	 * 请求失败时重新执行此请求的次数,默认为3
	 */
	private int retryCount = SiteConstant.RETRY_COUNT;

	/**
	 * <pre>
	 * 
	 * 确定连接建立之前的超时时间（以毫秒为单位）。
	 * 
	 * 超时值零被解释为无限超时，负值被解释为未定义（如果适用，则为系统默认值）。
	 * <b>默认为-1</b>
	 * </pre>
	 */
	private int connectTimeout = -1;

	/**
	 * 确定是否应自动处理重定向。 <br/>
	 * <b>默认为true</b>
	 */
	private boolean redirectsEnabled = true;

	/**
	 * <pre>
	 * 确定是否请求目标服务器压缩内容。
	 * <b>默认为true</b>
	 * </pre>
	 */
	private boolean contentCompressionEnabled = true;
	/**
	 * 请求头参数
	 */
	private List<HeaderRule> headers = new ArrayList<>();

	/**
	 * 起始链接，多个起始链接之间用半角逗号隔开
	 */
	private String startUrl;
	/**
	 * 链接提取规则
	 */
	private Set<String> linkRules = new HashSet<>();

	/**
	 * 内容页规则，多个规则之间用半角逗号隔开
	 */
	private String extractUrl;
	/**
	 * 内容提取项
	 */
	private Map<String, ContentItem> extractItems = new HashMap<>();

	/**
	 * 创建一个默认风铃虫规则构建器
	 * 
	 * @return 风铃虫规则构建器
	 */
	public static CrawlerBuilder create() {
		return new CrawlerBuilder();
	}

	/**
	 * 根据已有规则 创建一个默认风铃虫规则构建器
	 * 
	 * @param crawlerRule 风铃虫规则
	 * @return 风铃虫规则构建器
	 */
	public static CrawlerBuilder create(CrawlerRule crawlerRule) {
		Assert.notNull(crawlerRule, "配置规则不能为空");

		if (crawlerRule.getSite() == null) {
			crawlerRule.setSite(new SiteRule());
		}

		if (crawlerRule.getContent() == null) {
			crawlerRule.setContent(new ContentRule());
		}

		if (crawlerRule.getLink() == null) {
			throw new IllegalArgumentException("起始链接不能为空");
		}

		CrawlerBuilder crawlerBuilder = new CrawlerBuilder();
		crawlerBuilder.link(crawlerRule.getLink()).site(crawlerRule.getSite()).content(crawlerRule.getContent())
				.interval(crawlerRule.getInterval()).waitTime(crawlerRule.getWaitTime())
				.threadNum(crawlerRule.getThreadNum());
		return crawlerBuilder;
	}

	/**
	 * 获取每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀
	 * 
	 * @return 每次请求的间隔时间，单位为毫秒
	 */
	public long interval() {
		return this.interval;
	}

	/**
	 * 设置每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀
	 * 
	 * @param intervalInSeconds 每次请求的间隔时间，单位为毫秒，必须不小于0
	 * @return
	 */
	public CrawlerBuilder interval(long intervalInSeconds) {
		if (intervalInSeconds < 0) {
			intervalInSeconds = SiteConstant.REQUEST_INTERVAL_TIME;
		}
		this.interval = intervalInSeconds;
		return this;
	}

	/**
	 * 获取超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束
	 * 
	 * @return 超时等待时间，单位为毫秒
	 */
	public long waitTime() {
		return this.waitTime;
	}

	/**
	 * 设置超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束
	 * 
	 * @param waitTimeInSeconds 超时等待时间，单位为毫秒，必须大于0
	 * @return
	 */
	public CrawlerBuilder waitTime(long waitTimeInSeconds) {
		if (waitTimeInSeconds <= 0) {
			waitTimeInSeconds = SiteConstant.WAIT_TIME_FOR_CLOSE;
		}
		this.waitTime = waitTimeInSeconds;
		return this;
	}

	/**
	 * 风铃虫解析时线程数
	 * 
	 * @return
	 */
	public int threadNum() {
		return this.threadNum;
	}

	/**
	 * 设置 风铃虫解析时线程数
	 * 
	 * @param threadNum 风铃虫解析时线程数，必须大于0
	 * @return
	 */
	public CrawlerBuilder threadNum(int threadNum) {
		if (threadNum <= 0) {
			threadNum = SiteConstant.DEFAULT_THREAD_NUM;
		}
		this.threadNum = threadNum;
		return this;
	}

	/**
	 * 获取站点配置规则数据
	 * 
	 * @return 站点配置规则数据
	 */
	public SiteRule site() {
		return new SiteRule().setUserAgent(this.userAgent).setReferrer(this.referrer).setCacheControl(this.cacheControl)
				.setCookieValue(this.cookieValue).setFailureMark(this.failureMark).setRetryCount(this.retryCount)
				.setRedirectsEnabled(this.redirectsEnabled).setConnectTimeout(this.connectTimeout)
				.setContentCompressionEnabled(this.contentCompressionEnabled).setMaxRedirects(this.maxRedirects)
				.setInterceptCount(this.interceptCount).setHeaders(this.headers);
	}

	/**
	 * 设置站点配置规则数据
	 * 
	 * @param site 站点配置规则数据
	 * @return
	 */
	public CrawlerBuilder site(SiteRule site) {
		Assert.notNull(site, "站点配置规则数据不能为空");
		this.userAgent(site.getUserAgent());
		this.referrer(site.getReferrer());
		this.cacheControl(site.getCacheControl());
		this.cookieValue(site.getCookieValue());
		this.failureMark(site.getFailureMark());
		this.interceptCount(site.getInterceptCount());
		this.setHeaders(site.getHeaders());
		this.retryCount(site.getRetryCount());
		this.connectTimeout(site.getConnectTimeout());
		this.redirectsEnabled(site.isRedirectsEnabled());
		this.maxRedirects(site.getMaxRedirects());
		this.contentCompressionEnabled(site.isContentCompressionEnabled());
		return this;
	}

	/**
	 * 获取所有的请求头参数
	 * 
	 * @return 所有的请求头参数
	 */
	public List<HeaderRule> headers() {
		return this.headers;
	}

	/**
	 * 增加一组请求头参数
	 * 
	 * @param headerRule 请求头参数
	 * @return
	 */
	public CrawlerBuilder addHeader(HeaderRule headerRule) {
		Assert.notNull(headerRule, "请求头参数对不能为空");
		this.headers.add(headerRule);
		return this;
	}

	/**
	 * 增加一组请求头参数
	 * 
	 * @param list 请求头参数
	 * @return
	 */
	public CrawlerBuilder addHeaders(List<HeaderRule> list) {
		this.headers.addAll(null != list ? list : new ArrayList<>());
		return this;
	}

	/**
	 * 清空原始值后再设置请求头参数
	 * 
	 * @param list 请求头参数
	 * @return
	 */
	public CrawlerBuilder setHeaders(List<HeaderRule> list) {
		this.headers = null != list ? list : new ArrayList<>();
		return this;
	}

	/**
	 * 获取失败标志<br/>
	 * 下载内容里包含此值时表示被服务器拦截，使用正则表达式，如果为空则不进行此校验
	 * 
	 * @return 失败标志
	 */
	public String failureMark() {
		return this.failureMark;
	}

	/**
	 * 设置失败标志<br/>
	 * 下载内容里包含此值时表示被服务器拦截，使用正则表达式，如果为空则不进行此校验
	 * 
	 * @param failureMark 失败标志，此值为空时表示不开启此功能
	 * @return
	 */
	public CrawlerBuilder failureMark(String failureMark) {
		this.failureMark = failureMark;
		return this;
	}

	/**
	 * 获取拦截次数阀域值<br/>
	 * 连续多次在下载内容中获取到失败标识时的重试此次，超过此次数会关闭该风铃虫实例，默认为5
	 * 
	 * @return 拦截次数阀域值
	 */
	public int interceptCount() {
		return this.interceptCount;

	}

	/**
	 * 设置拦截次数阀域值<br/>
	 * 连续多次在下载内容中获取到失败标识时的重试此次，超过此次数会关闭该风铃虫实例，默认为5
	 * 
	 * @param interceptCount 拦截次数阀域值，此值小于1时表示不开启此功能
	 * @return
	 */
	public CrawlerBuilder interceptCount(int interceptCount) {
		this.interceptCount = interceptCount < 0 ? 0 : interceptCount;
		return this;
	}

	/**
	 * 获取请求失败时的重试次数
	 * 
	 * @return 请求失败时的重试次数
	 */
	public int retryCount() {
		return this.retryCount;
	}

	/**
	 * 设置请求失败时的重试次数
	 * 
	 * @param retryCount 请求失败时的重试次数,连续多次在下载内容中获取到失败标识时的重试此次，默认为5
	 * @return
	 */
	public CrawlerBuilder retryCount(int retryCount) {
		this.retryCount = interceptCount < 0 ? SiteConstant.INTERCEPT_RETRY_COUNT : interceptCount;
		return this;
	}

	/**
	 * 获取确定连接建立之前的超时时间（以毫秒为单位）
	 * 
	 * @return 确定连接建立之前的超时时间（以毫秒为单位）,非正数时表示不开启此功能
	 */
	public int connectTimeout() {
		return this.connectTimeout;
	}

	/**
	 * 设置确定连接建立之前的超时时间（以毫秒为单位）
	 * 
	 * @param connectTimeout 确定连接建立之前的超时时间（以毫秒为单位）,非正数时表示不开启此功能
	 * @return
	 */
	public CrawlerBuilder connectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout < 0 ? -1 : connectTimeout;
		return this;
	}

	/**
	 * 获取是否应自动处理重定向
	 * 
	 * @return 是否应自动处理重定向
	 */
	public boolean redirectsEnabled() {
		return this.redirectsEnabled;
	}

	/**
	 * 设置是否应自动处理重定向
	 * 
	 * @param redirectsEnabled 是否应自动处理重定向，默认为true
	 * @return
	 */
	public CrawlerBuilder redirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
		return this;
	}

	/**
	 * 获取要遵循的最大重定向数
	 * 
	 * @return
	 */
	public int maxRedirects() {
		return this.maxRedirects;
	}

	/**
	 * 设置要遵循的最大重定向数
	 * 
	 * @param maxRedirects 要遵循的最大重定向数
	 * @return
	 */
	public CrawlerBuilder maxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects <= 0 ? SiteConstant.MAX_REDIRECTS : maxRedirects;
		return this;
	}

	/**
	 * 是否是否请求目标服务器压缩内容
	 * 
	 * @return 是否请求目标服务器压缩内容
	 */
	public boolean contentCompressionEnabled() {
		return this.contentCompressionEnabled;
	}

	/**
	 * 设置是否请求目标服务器压缩内容
	 * 
	 * @param contentCompressionEnabled 是否请求目标服务器压缩内容
	 * @return
	 */
	public CrawlerBuilder contentCompressionEnabled(boolean contentCompressionEnabled) {
		this.contentCompressionEnabled = contentCompressionEnabled;
		return this;
	}

	/**
	 * 获取链接处理规则
	 * 
	 * @return 链接处理规则
	 */
	public LinkRule link() {
		return new LinkRule().setStartUrl(this.startUrl).setRules(this.linkRules);
	}

	/**
	 * 设置链接处理规则
	 * 
	 * @param link 链接处理规则
	 * @return
	 */
	public CrawlerBuilder link(LinkRule link) {
		Assert.notNull(link, "链接提取规则不能为空");
		this.startUrl(link.getStartUrl());
		this.setLinkRules(link.getRules());
		return this;
	}

	/**
	 * 获取链接提取规则
	 * 
	 * @return 链接提取规则
	 */
	public Set<String> linkRules() {
		return this.linkRules;
	}

	/**
	 * 清空原始链接提取规则后设置链接提取规则
	 * 
	 * @param linkRules 链接提取规则
	 * @return
	 */
	public CrawlerBuilder setLinkRules(Set<String> linkRules) {
		this.linkRules = null != linkRules ? linkRules : new HashSet<>();
		return this;
	}

	/**
	 * 增加链接提取规则
	 * 
	 * @param linkRules 链接提取规则
	 * @return
	 */
	public CrawlerBuilder addLinkRules(Set<String> linkRules) {
		if (null == linkRules) {
			linkRules = new HashSet<>();
		}
		this.linkRules.addAll(linkRules);
		return this;
	}

	/**
	 * 增加链接提取规则
	 * 
	 * @param linkRule 链接提取规则
	 * @return
	 */
	public CrawlerBuilder addLinkRule(String linkRule) {
		Assert.notNull(linkRule, "链接提取规则不能为空");
		this.linkRules.add(linkRule);
		return this;
	}

	/**
	 * 获取起始链接地址<br/>
	 * 多个起始链接之间用半角逗号隔开
	 * 
	 * @return 起始链接地址
	 */
	public String startUrl() {
		return this.startUrl;
	}

	/**
	 * 设置 起始链接地址<br/>
	 * 多个起始链接之间用半角逗号隔开
	 * 
	 * @param startUrl 起始链接地址
	 * @return
	 */
	public CrawlerBuilder startUrl(String startUrl) {
		Assert.notNull(startUrl, "起始链接不能为空");
		this.startUrl = startUrl;
		return this;
	}

	/**
	 * 获取内容处理规则
	 * 
	 * @return 设置内容处理规则
	 */
	public ContentRule content() {
		return new ContentRule().setExtractUrl(this.extractUrl).setContents(this.extractItems());
	}

	/**
	 * 设置内容处理规则
	 * 
	 * @param content 内容处理规则
	 * @return
	 */
	public CrawlerBuilder content(ContentRule content) {
		Assert.notNull(content, "内容提取规则不能为空");
		this.extractUrl(content.getExtractUrl());
		this.setExtractItems(content.getContents());
		return this;
	}

	/**
	 * 获取内容页规则<br/>
	 * 多个规则之间用半角逗号隔开
	 * 
	 * @return 内容页规则
	 */
	public String extractUrl() {
		return this.extractUrl;
	}

	/**
	 * 设置内容页规则<br/>
	 * 多个规则之间用半角逗号隔开
	 * 
	 * @param extractUrl 内容页规则 ，多个规则之间用半角逗号隔开
	 * @return
	 */
	public CrawlerBuilder extractUrl(String extractUrl) {
		this.extractUrl = extractUrl;
		return this;
	}

	/**
	 * 获取浏览器标识 ，此值为空时表示每次请求都会随机从内置浏览器标识中选择一个
	 * 
	 * @return 浏览器标识
	 */
	public String userAgent() {
		return this.userAgent;
	}

	/**
	 * 设置浏览器标识
	 * 
	 * @param userAgent 浏览器标识，此值为空时表示每次请求都会随机从内置浏览器标识中选择一个
	 * @return
	 */
	public CrawlerBuilder userAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	/**
	 * 获取请求来源页<br/>
	 * 此值为空时表示由内核智能处理
	 * 
	 * @return
	 */
	public String referrer() {
		return this.referrer;
	}

	/**
	 * 设置请求来源页<br/>
	 * 
	 * @param referrer 请求来源页，此值为空时表示由内核智能处理
	 * @return
	 */
	public CrawlerBuilder referrer(String referrer) {
		this.referrer = referrer;
		return this;
	}

	/**
	 * 获取请求时携带cookie信息 <br/>
	 * 此值为空时表示由内核智能处理
	 * 
	 * @return 请求时携带cookie信息
	 */
	public String cookieValue() {
		return this.cookieValue;
	}

	/**
	 * 设置请求时携带cookie信息
	 * 
	 * @param cookieValue 请求时携带cookie信息，此值为空时表示由内核智能处理
	 * @return
	 */
	public CrawlerBuilder cookieValue(String cookieValue) {
		this.cookieValue = cookieValue;

		return this;
	}

	/**
	 * 获取网页缓存策略<br/>
	 * 默认为 max-age=0
	 * 
	 * @return
	 */
	public String cacheControl() {
		return this.cacheControl;
	}

	/**
	 * 设置网页缓存策略<br/>
	 * 默认为 max-age=0
	 * 
	 * @param cacheControl 网页缓存策略，默认为 max-age=0
	 * @return
	 */
	public CrawlerBuilder cacheControl(String cacheControl) {
		this.cacheControl = cacheControl;

		return this;
	}

	/**
	 * 获取所有的内容提取项
	 * 
	 * @return 所有的内容提取项
	 */
	public List<ContentItem> extractItems() {
		return this.extractItems.values().stream().collect(Collectors.toList());
	}

	/**
	 * 增加内容提取项
	 * 
	 * @param list 内容提取项
	 * @return
	 */
	public CrawlerBuilder addExtractItems(List<ContentItem> list) {
		if (null != list) {
			list.parallelStream().filter(t -> null != t && StringUtils.isNotBlank(t.getFiledName()))
					.forEach(this::addExtractItem);
		}

		return this;
	}

	/**
	 * 设置 内容提取项
	 * 
	 * @param list 内容提取项
	 * @return
	 */
	public CrawlerBuilder setExtractItems(List<ContentItem> list) {
		if (null != list) {
			this.extractItems.clear();
			this.addExtractItems(list);
		}
		return this;
	}

	/**
	 * 根据内容提取项的代码获取 内容提取项
	 * 
	 * @param key 内容提取项的代码获取
	 * @return 内容提取项
	 */
	public ContentItem extractItem(String key) {
		Assert.notNull(key, "内容提取项的代码不能为空");
		return this.extractItems.get(key);
	}

	/**
	 * 增加内容提取项
	 * 
	 * @param contentItem 内容提取项
	 * @return
	 */
	public CrawlerBuilder addExtractItem(ContentItem contentItem) {
		Assert.notNull(contentItem, "内容提取项不能为空");
		Assert.notNull(contentItem.getFiledName(), "内容提取项的代码不能为空");
		this.extractItems.put(contentItem.getFiledName(), contentItem);
		return this;
	}

	/**
	 * 根据内容提取项的代码获取该内容提取项的提取规则
	 * 
	 * @param contentItemName 内容提取项的代码
	 * @return 该内容提取项的提取规则
	 */
	public List<FieldExtractRule> extractRule(String key) {
		Assert.notNull(key, "内容提取项的代码不能为空");
		ContentItem contentItem = this.extractItem(key);
		if (null != contentItem) {
			return contentItem.getRules();
		}
		return null;
	}

	/**
	 * 根据内容提取项的代码增加该内容提取项的提取规则
	 * 
	 * @param key  内容提取项的代码
	 * @param list 该内容提取项的提取规则
	 * @return
	 */
	public CrawlerBuilder addExtractRules(String key, List<FieldExtractRule> list) {
		Assert.notNull(list, "内容提取项的提取规则不能为空");
		ContentItem contentItem = this.extractItem(key);
		if (null != contentItem) {
			List<FieldExtractRule> rules = contentItem.getRules();
			rules.addAll(list);
			contentItem.setRules(rules);
			this.addExtractItem(contentItem);
		}
		return this;
	}

	/**
	 * 根据内容提取项的代码增加该内容提取项的提取规则
	 * 
	 * @param key              内容提取项的代码
	 * @param fieldExtractRule 该内容提取项的提取规则
	 * @return
	 */
	public CrawlerBuilder addExtractRule(String key, FieldExtractRule fieldExtractRule) {
		Assert.notNull(fieldExtractRule, "内容提取项的提取规则不能为空");

		ContentItem contentItem = this.extractItem(key);
		if (null != contentItem) {
			List<FieldExtractRule> rules = contentItem.getRules();
			rules.add(fieldExtractRule);
			contentItem.setRules(rules);
			this.addExtractItem(contentItem);
		}
		return this;
	}

	/**
	 * 根据内容提取项的代码设置该内容提取项的提取规则
	 * 
	 * @param key  内容提取项的代码
	 * @param list 该内容提取项的提取规则
	 * @return
	 */
	public CrawlerBuilder setExtractRules(String key, List<FieldExtractRule> list) {
		ContentItem contentItem = this.extractItem(key);
		if (null != contentItem) {
			contentItem.setRules(list);
			this.addExtractItem(contentItem);
		}
		return this;
	}

	/**
	 * 处理链接提取表达式<br/>
	 * 
	 * 将形如 /** 的表达式转换为 包含域名关键字的通配符形式
	 * 
	 * @param startUrl 起始链接
	 * @param pattern  链接提取表达式
	 * @return
	 */
	private String pattern(String startUrl, String pattern) {
		if (StringUtils.equalsIgnoreCase(pattern, RuleConstant.ANT_MATCH_ALL)) {
			String shortDomain = LinkUtils.extractShortDomain(startUrl);
			pattern = new StringBuffer(".+").append(shortDomain).append(".+").toString();
		}
		return pattern;
	}

	/**
	 * 构建一个风铃虫规则
	 * 
	 * @return
	 */
	public CrawlerRule build() {

		if (null == this.interval || this.interval < 0) {
			this.interval = SiteConstant.REQUEST_INTERVAL_TIME;
		}

		if (null == this.waitTime || this.waitTime <= 0) {
			this.waitTime = SiteConstant.WAIT_TIME_FOR_CLOSE;
		}
		if (null == this.threadNum || this.threadNum <= 0) {
			this.threadNum = SiteConstant.DEFAULT_THREAD_NUM;
		}

		// 检查请求头规则
		if (null == this.headers) {
			this.headers = new ArrayList<>();
		}

		// 检查连接提取规则
		if (null == this.linkRules) {
			this.linkRules = new HashSet<>();
		}

		if (this.linkRules.size() == 0) {
			this.addLinkRule(RuleConstant.ANT_MATCH_ALL);
		} else {
			this.linkRules = this.linkRules.parallelStream().filter(t -> null != t).map(t -> pattern(startUrl, t))
					.collect(Collectors.toSet());
		}

		// 检查内容页规则
		if (StringUtils.isBlank(this.extractUrl)) {
			this.extractUrl = RuleConstant.ANT_MATCH_ALL;
		}

		// 检查提取项数据
		List<ContentItem> contentItems = this.extractItems().parallelStream().filter(t -> null != t)
				.filter(t -> StringUtils.isNotBlank(t.getFiledName()))
				.filter(t -> null != t.getRules() && t.getRules().size() > 0).collect(Collectors.toList());

		this.setExtractItems(contentItems);

		CrawlerRule crawlerRule = new CrawlerRule(this.interval, this.waitTime, this.threadNum, this.site(),
				this.link(), this.content());
		return crawlerRule;
	}

	/**
	 * 创建一个风铃虫简单实例
	 * 
	 * @return 风铃虫简单实例
	 */
	public Crawler creatCrawler() {
		return new Crawler(this.build());
	}

	private CrawlerBuilder() {
	}
}
