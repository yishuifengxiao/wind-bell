package com.yishuifengxiao.common.crawler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.eunm.Pattern;
import com.yishuifengxiao.common.crawler.domain.eunm.Type;
import com.yishuifengxiao.common.crawler.domain.model.PageRule;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractFieldRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.HeaderRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.MatcherRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.tool.utils.LinkUtils;


/**
 * 风铃虫规则构建器
 * 
 * @author yishui
 * @version 1.0.0
 */
public class CrawlerBuilder {

	/**
	 * 每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀<br/>
	 * 默认时间为10000毫秒(10秒)
	 */
	private Long interval = SiteConstant.REQUEST_INTERVAL_TIME;
	/**
	 * 超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束
	 */
	private Long waitTime = SiteConstant.WAIT_TIME_FOR_CLOSE;

	/**
	 * 风铃虫解析时线程数<br/>
	 * 默认线程数为1
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
	 * 最大的请求深度，此值为0或负数时表示不进行深度限制，默认不进行深度限制
	 */
	private long maxDepth = SiteConstant.MAX_REQUEST_DEPTH;

	/**
	 * <pre>
	 * 
	 * 确定连接建立之前的超时时间（以毫秒为单位）。
	 * 
	 * 默认的连接超时时间，30000毫秒(30秒)
	 * 
	 * </pre>
	 */
	private int connectTimeout = SiteConstant.CONNECTION_TIME_OUT;

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
	 * 确定用于HTTP状态管理的cookie规范的名称
	 */
	private String cookieSpec;
	/**
	 * 确定是否应拒绝相对重定向。 默认为false
	 */
	private boolean relativeRedirectsAllowed = false;
	/**
	 * 确定是否应允许循环重定向，默认为false
	 */
	private boolean circularRedirectsAllowed = false;
	/**
	 * 确定客户端是否应规范请求中的URI。默认为true
	 */
	private boolean normalizeUri = true;
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
	private Set<MatcherRule> linkRules = new HashSet<>();

	/**
	 * 内容页地址规则
	 */
	private MatcherRule contentPageRule;

	/**
	 * 内容匹配类型
	 */
	private Type matcherType;

	/**
	 * 内容匹配参数
	 */
	private String matcherPattern;

	/**
	 * 期待匹配值
	 */
	private String matcherTarget;

	/**
	 * 匹配模式，true表示必须包含期待匹配参数，false标识不能包含期待匹配参数
	 */
	private Boolean matcherMode = true;

	/**
	 * 是否大小写敏感，即进行匹配时是否为大小写敏感，默认为 false
	 */
	private Boolean matcherCaseSensitive = false;

	/**
	 * 是否为模糊匹配，默认为true
	 */
	private Boolean matcherFuzzy = true;

	/**
	 * 内容提取规则
	 */
	private Map<String, ExtractRule> extractRules = new HashMap<>();

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
		if (crawlerRule.getRules() == null) {
			crawlerRule.setRules(new ArrayList<>());
		}

		CrawlerBuilder crawlerBuilder = new CrawlerBuilder();
		crawlerBuilder.link(crawlerRule.getLink()).site(crawlerRule.getSite()).content(crawlerRule.getContent())
				.setExtractRules(crawlerRule.getRules()).interval(crawlerRule.getInterval())
				.waitTime(crawlerRule.getWaitTime()).threadNum(crawlerRule.getThreadNum());
		return crawlerBuilder;
	}

	/**
	 * 获取每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀<br/>
	 * 默认时间为10000毫秒(10秒)
	 * 
	 * @return 每次请求的间隔时间，单位为毫秒
	 */
	public long interval() {
		return this.interval;
	}

	/**
	 * 设置每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀,<br/>
	 * 默认时间为10000毫秒(10秒)
	 * 
	 * @param intervalInSeconds 每次请求的间隔时间，单位为毫秒，必须不小于0
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder interval(long intervalInSeconds) {
		this.interval = intervalInSeconds;
		return this;
	}

	/**
	 * 获取超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束<br/>
	 * 默认为300000毫秒(300秒)
	 * 
	 * @return 超时等待时间，单位为毫秒
	 */
	public long waitTime() {
		return this.waitTime;
	}

	/**
	 * 设置超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束<br/>
	 * 默认为300000毫秒(300秒)
	 * 
	 * @param waitTimeInSeconds 超时等待时间，单位为毫秒，必须大于0
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder waitTime(long waitTimeInSeconds) {
		this.waitTime = waitTimeInSeconds;
		return this;
	}

	/**
	 * 风铃虫解析时线程数<br/>
	 * 默认线程数为1 <br/>
	 * 
	 * @return 解析时线程数
	 */
	public int threadNum() {
		return this.threadNum;
	}

	/**
	 * 设置 风铃虫解析时线程数 默认线程数为1
	 * 
	 * @param threadNum 风铃虫解析时线程数，必须大于0
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder threadNum(int threadNum) {
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
				.setCookieValue(this.cookieValue).setMaxDepth(this.maxDepth).setFailureMark(this.failureMark)
				.setInterceptCount(this.interceptCount).setRetryCount(this.retryCount)
				.setRedirectsEnabled(this.redirectsEnabled).setRelativeRedirectsAllowed(this.relativeRedirectsAllowed)
				.setConnectTimeout(this.connectTimeout).setContentCompressionEnabled(this.contentCompressionEnabled)
				.setMaxRedirects(this.maxRedirects).setHeaders(this.headers)
				.setCircularRedirectsAllowed(this.circularRedirectsAllowed).setCookieSpec(this.cookieSpec)
				.setNormalizeUri(this.normalizeUri);
	}

	/**
	 * 设置站点配置规则数据
	 * 
	 * @param site 站点配置规则数据
	 * @return 规则构建器实例
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
		this.maxDepth(site.getMaxDepth());
		this.circularRedirectsAllowed(site.isCircularRedirectsAllowed());
		this.cookieSpec(site.getCookieSpec());
		this.normalizeUri(site.isNormalizeUri());
		this.relativeRedirectsAllowed(site.isRelativeRedirectsAllowed());
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
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder userAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	/**
	 * 获取请求来源页<br/>
	 * 此值为空时表示由内核智能处理
	 * 
	 * @return 请求来源页
	 */
	public String referrer() {
		return this.referrer;
	}

	/**
	 * 设置请求来源页<br/>
	 * 
	 * @param referrer 请求来源页，此值为空时表示由内核智能处理
	 * @return 规则构建器实例
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
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder cookieValue(String cookieValue) {
		this.cookieValue = cookieValue;

		return this;
	}

	/**
	 * 获取网页缓存策略<br/>
	 * 默认为 max-age=0
	 * 
	 * @return 网页缓存策略
	 */
	public String cacheControl() {
		return this.cacheControl;
	}

	/**
	 * 设置网页缓存策略<br/>
	 * 默认为 max-age=0
	 * 
	 * @param cacheControl 网页缓存策略，默认为 max-age=0
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder cacheControl(String cacheControl) {
		this.cacheControl = cacheControl;

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
	 * @return 规则构建器实例
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
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addHeaders(List<HeaderRule> list) {
		this.headers.addAll(null != list ? list : new ArrayList<>());
		return this;
	}

	/**
	 * 清空原始值后再设置请求头参数
	 * 
	 * @param list 请求头参数
	 * @return  规则构建器实例
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
	 * @return  规则构建器实例
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
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder interceptCount(int interceptCount) {
		this.interceptCount = interceptCount;
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
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder retryCount(int retryCount) {
		this.retryCount = interceptCount;
		return this;
	}

	/**
	 * 获取最大的请求深度
	 * 
	 * @return 最大的请求深度
	 */
	public long maxDepth() {
		return maxDepth;
	}

	/**
	 * 设置最大的请求深度
	 * 
	 * @param maxDepth 最大的请求深度，此值为0或负数时表示不进行深度限制
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder maxDepth(long maxDepth) {
		this.maxDepth = maxDepth;
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
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder connectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout < 1 ? SiteConstant.CONNECTION_TIME_OUT : connectTimeout;
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
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder redirectsEnabled(boolean redirectsEnabled) {
		this.redirectsEnabled = redirectsEnabled;
		return this;
	}

	/**
	 * 获取确定用于HTTP状态管理的cookie规范的名称
	 * 
	 * @return 确定用于HTTP状态管理的cookie规范的名称
	 * 
	 */
	public String cookieSpec() {
		return this.cookieSpec;
	}

	/**
	 * 设置 确定用于HTTP状态管理的cookie规范的名称
	 * 
	 * @param cookieSpec 确定用于HTTP状态管理的cookie规范的名称
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder cookieSpec(String cookieSpec) {
		this.cookieSpec = cookieSpec;
		return this;
	}

	/**
	 * 获取确定是否应拒绝相对重定向
	 * 
	 * @return 确定是否应拒绝相对重定向
	 */
	public boolean relativeRedirectsAllowed() {
		return this.relativeRedirectsAllowed;
	}

	/**
	 * 设置确定是否应拒绝相对重定向
	 * 
	 * @param relativeRedirectsAllowed 确定是否应拒绝相对重定向
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder relativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
		this.relativeRedirectsAllowed = relativeRedirectsAllowed;
		return this;
	}

	/**
	 * 获取是否应允许循环重定向
	 * 
	 * @return 是否应允许循环重定向
	 */
	public boolean circularRedirectsAllowed() {
		return this.circularRedirectsAllowed;
	}

	/**
	 * 设置是否应允许循环重定向
	 * 
	 * @param circularRedirectsAllowed 是否应允许循环重定向
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder circularRedirectsAllowed(boolean circularRedirectsAllowed) {
		this.circularRedirectsAllowed = circularRedirectsAllowed;
		return this;
	}

	/**
	 * 确定客户端是否应规范请求中的URI
	 * 
	 * @return 客户端是否应规范请求中的URI
	 */
	public boolean normalizeUri() {
		return this.normalizeUri;
	}

	/**
	 * 设置客户端是否应规范请求中的URI
	 * 
	 * @param normalizeUri 客户端是否应规范请求中的URI
	 * @return  规则构建器实例
	 */
	public CrawlerBuilder normalizeUri(boolean normalizeUri) {
		this.normalizeUri = normalizeUri;
		return this;
	}

	/**
	 * 获取要遵循的最大重定向数
	 * 
	 * @return 要遵循的最大重定向数
	 */
	public int maxRedirects() {
		return this.maxRedirects;
	}

	/**
	 * 设置要遵循的最大重定向数
	 * 
	 * @param maxRedirects 要遵循的最大重定向数
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder maxRedirects(int maxRedirects) {
		this.maxRedirects = maxRedirects;
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
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder contentCompressionEnabled(boolean contentCompressionEnabled) {
		this.contentCompressionEnabled = contentCompressionEnabled;
		return this;
	}

	/**
	 * 获取链接解析规则
	 * 
	 * @return 链接解析规则
	 */
	public LinkRule link() {
		return new LinkRule().setStartUrl(this.startUrl).setRules(this.linkRules);
	}

	/**
	 * 设置链接解析规则
	 * 
	 * @param link 链接解析规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder link(LinkRule link) {
		Assert.notNull(link, "链接解析规则不能为空");
		this.startUrl(link.getStartUrl());
		this.setLinkRules(link.getRules());
		return this;
	}

	/**
	 * 获取链接提取规则
	 * 
	 * @return 链接提取规则
	 */
	public Set<MatcherRule> linkRules() {
		return this.linkRules;
	}

	/**
	 * 清空原始链接提取规则后设置链接提取规则
	 * 
	 * @param linkRules 链接提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder setLinkRules(Set<MatcherRule> linkRules) {
		this.linkRules = null != linkRules ? linkRules : new HashSet<>();
		return this;
	}

	/**
	 * 增加链接提取规则
	 * 
	 * @param linkRules 链接提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addLinkRules(Set<MatcherRule> linkRules) {
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
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addLinkRule(MatcherRule linkRule) {
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
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder startUrl(String startUrl) {
		Assert.notNull(startUrl, "起始链接不能为空");
		this.startUrl = startUrl;
		return this;
	}

	/**
	 * 获取内容页地址规则
	 * 
	 * @return 内容页地址规则
	 */
	public ContentRule content() {
		return new ContentRule().setContentPageRule(this.contentPageRule).setPageRule(this.pageRule());
	}

	/**
	 * 设置内容解析规则
	 * 
	 * @param content 内容解析规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder content(ContentRule content) {
		Assert.notNull(content, "内容提取规则不能为空");
		this.contentPageRule(content.getContentPageRule());
		this.pageRule(content.getPageRule());
		return this;
	}

	/**
	 * 获取内容匹配规则
	 * 
	 * @return 内容匹配规则
	 */
	public PageRule pageRule() {
		return new PageRule(this.matcherType, this.matcherPattern, this.matcherTarget, this.matcherMode,
				this.matcherCaseSensitive, this.matcherFuzzy);
	}

	/**
	 * 设置内容匹配规则
	 * 
	 * @param pageRule 内容匹配规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder pageRule(PageRule pageRule) {
		pageRule = null == pageRule ? new PageRule() : pageRule;
		this.matcherType(pageRule.getType());
		this.matcherPattern(pageRule.getPattern());
		this.matcherTarget(pageRule.getTarget());
		this.matcherMode(pageRule.getMode());
		this.matcherCaseSensitive(pageRule.getCaseSensitive());
		this.matcherFuzzy(pageRule.getFuzzy());
		return this;
	}

	/**
	 * 获取内容页地址规则<br/>
	 * 多个规则之间用半角逗号隔开
	 * 
	 * @return 内容页地址规则
	 */
	public MatcherRule contentPageRule() {
		return this.contentPageRule;
	}

	/**
	 * 设置内容页地址规则<br/>
	 * 多个规则之间用半角逗号隔开
	 * 
	 * @param contentPageRule 链接过滤规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder contentPageRule(MatcherRule contentPageRule) {
		this.contentPageRule = contentPageRule;
		return this;
	}

	/**
	 * 获取内容匹配类型
	 * 
	 * @return 匹配类型
	 */
	public Type matcherType() {
		return this.matcherType;
	}

	/**
	 * 设置内容匹配类型
	 * 
	 * @param matcherType
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder matcherType(Type matcherType) {
		this.matcherType = matcherType;
		return this;
	}

	/**
	 * 获取 内容匹配参数
	 * 
	 * @return 内容匹配参数
	 */
	public String matcherPattern() {
		return this.matcherPattern;
	}

	/**
	 * 设置内容匹配参数
	 * 
	 * @param matcherPattern 内容匹配参数
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder matcherPattern(String matcherPattern) {
		this.matcherPattern = matcherPattern;
		return this;
	}

	/**
	 * 获取期待匹配值
	 * 
	 * @return 期待匹配值
	 */
	public String matcherTarget() {
		return this.matcherTarget;
	}

	/**
	 * 设置 期待匹配值
	 * 
	 * @param matcherTarget 期待匹配值
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder matcherTarget(String matcherTarget) {
		this.matcherTarget = matcherTarget;
		return this;
	}

	/**
	 * 获取匹配模式
	 * 
	 * @return true表示必须包含期待匹配参数，false标识不能包含期待匹配参数
	 */
	public Boolean matcherMode() {
		return this.matcherMode;
	}

	/**
	 * 设置匹配模式
	 * 
	 * @param matcherMode true表示必须包含期待匹配参数，false标识不能包含期待匹配参数
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder matcherMode(Boolean matcherMode) {
		this.matcherMode = matcherMode;
		return this;
	}

	/**
	 * 获取匹配时是否大小写敏感
	 * 
	 * @return 是否大小写敏感，即进行匹配时是否为大小写敏感，默认为 false
	 */
	public Boolean matcherCaseSensitive() {
		return this.matcherCaseSensitive;
	}

	/**
	 * 设置匹配时是否大小写敏感
	 * 
	 * @param matcherCaseSensitive 是否大小写敏感，true表示敏感
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder matcherCaseSensitive(Boolean matcherCaseSensitive) {
		this.matcherCaseSensitive = matcherCaseSensitive;
		return this;
	}

	/**
	 * 匹配时是否为模糊匹配
	 * 
	 * @return true表示为模糊匹配
	 */
	public Boolean matcherFuzzy() {
		return this.matcherFuzzy;
	}

	/**
	 * 设置匹配时是否为模糊匹配
	 * 
	 * @param matcherFuzzy true表示为模糊匹配
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder matcherFuzzy(Boolean matcherFuzzy) {
		this.matcherFuzzy = matcherFuzzy;
		return this;
	}

	/**
	 * 获取所有的内容提取规则
	 * 
	 * @return 所有的内容提取规则
	 */
	public List<ExtractRule> extractRules() {
		return this.extractRules.values().stream().collect(Collectors.toList());
	}

	/**
	 * 增加内容提取规则
	 * 
	 * @param list 内容提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addExtractRules(List<ExtractRule> list) {
		if (null != list) {
			list.stream().filter(Objects::nonNull).filter(t -> StringUtils.isNotBlank(t.getCode()))
					.forEach(this::addExtractRule);
		}

		return this;
	}

	/**
	 * 设置 内容提取规则<br/>
	 * 会清空原始的内容提取规则
	 * 
	 * @param list 内容提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder setExtractRules(List<ExtractRule> list) {
		this.extractRules.clear();
		this.addExtractRules(list);
		return this;
	}

	/**
	 * 根据内容提取规则的编码获取 内容提取规则
	 * 
	 * @param key 内容提取规则的编码获取
	 * @return 内容提取规则
	 */
	public ExtractRule extractRule(String key) {
		Assert.notNull(key, "内容提取规则的编码不能为空");
		return this.extractRules.get(key);
	}

	/**
	 * 增加内容提取规则
	 * 
	 * @param extractRule 内容提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addExtractRule(ExtractRule extractRule) {
		Assert.notNull(extractRule, "内容提取规则不能为空");
		Assert.notNull(extractRule.getCode(), "内容提取规则的编码不能为空");
		this.extractRules.put(extractRule.getCode(), extractRule);
		return this;
	}

	/**
	 * 根据内容提取规则的编码获取该内容提取规则的提取规则
	 * 
	 * @param key 内容提取规则的编码
	 * @return 该内容提取规则的提取规则
	 */
	public List<ExtractFieldRule> fieldExtractRule(String key) {
		Assert.notNull(key, "内容提取规则的编码不能为空");
		ExtractRule extractRule = this.extractRule(key);
		if (null != extractRule) {
			return extractRule.getRules();
		}
		return null;
	}

	/**
	 * 根据内容提取规则的编码增加该内容提取规则的提取规则
	 * 
	 * @param key  内容提取规则的编码
	 * @param list 该内容提取规则的提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addFieldExtractRules(String key, List<ExtractFieldRule> list) {
		Assert.notNull(list, "内容提取规则的提取规则不能为空");
		ExtractRule extractRule = this.extractRule(key);
		if (null != extractRule) {
			List<ExtractFieldRule> rules = extractRule.getRules();
			rules.addAll(list);
			extractRule.setRules(rules);
			this.addExtractRule(extractRule);
		}
		return this;
	}

	/**
	 * 根据内容提取规则的编码增加该内容提取规则的提取规则
	 * 
	 * @param key              内容提取规则的编码
	 * @param fieldExtractRule 该内容提取规则的提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder addExtractRule(String key, ExtractFieldRule fieldExtractRule) {
		Assert.notNull(fieldExtractRule, "内容提取规则的提取规则不能为空");

		ExtractRule extractRule = this.extractRule(key);
		if (null != extractRule) {
			List<ExtractFieldRule> rules = extractRule.getRules();
			rules.add(fieldExtractRule);
			extractRule.setRules(rules);
			this.addExtractRule(extractRule);
		}
		return this;
	}

	/**
	 * 根据内容提取规则的编码设置该内容提取规则的提取规则
	 * 
	 * @param key  内容提取规则的编码
	 * @param list 该内容提取规则的提取规则
	 * @return 规则构建器实例
	 */
	public CrawlerBuilder setExtractRules(String key, List<ExtractFieldRule> list) {
		ExtractRule extractRule = this.extractRule(key);
		if (null != extractRule) {
			extractRule.setRules(list);
			this.addExtractRule(extractRule);
		}
		return this;
	}

	/**
	 * 构建一个风铃虫规则
	 * 
	 * @return 风铃虫规则
	 */
	public CrawlerRule build() {
		Assert.notNull(this.startUrl, "起始链接不能为空");
		// 检查数据，并对数据进行合法性处理
		this.validate();

		// 检查请求头规则
		if (null == this.headers) {
			this.headers = new ArrayList<>();
		}
		// 检查请求头规则
		this.headers = this.headers.stream().filter(Objects::nonNull)
				.filter(t -> StringUtils.isNotBlank(t.getHeaderName())).collect(Collectors.toList());

		// 检查链接提取规则
		if (null == this.linkRules) {
			this.linkRules = new HashSet<>();
		}

		this.linkRules = this.linkRules.stream().filter(t -> null != t).filter(t -> t.getPattern() != null)
				.collect(Collectors.toSet());

		if (this.linkRules.size() == 0) {
			this.addLinkRule(new MatcherRule(Pattern.KEYWORD, LinkUtils.keyword(this.startUrl)));
		}

		// 检查内容页地址规则
		if (null == this.contentPageRule) {
			this.contentPageRule = new MatcherRule(Pattern.KEYWORD, LinkUtils.keyword(this.startUrl));
		}

		// 检查提取项数据
		List<ExtractRule> extractRules = this.extractRules().stream().filter(Objects::nonNull)
				.filter(t -> StringUtils.isNotBlank(t.getCode()))
				.filter(t -> null != t.getRules() && t.getRules().size() > 0).collect(Collectors.toList());

		this.setExtractRules(extractRules);

		CrawlerRule crawlerRule = new CrawlerRule(this.interval, this.waitTime, this.threadNum, this.site(),
				this.link(), this.content(), extractRules);
		return crawlerRule;
	}

	/**
	 * 检查数据，并对数据进行合法性处理
	 */
	private void validate() {
		if (null == this.interval || this.interval < 0) {
			this.interval = SiteConstant.REQUEST_INTERVAL_TIME;
		}

		if (null == this.waitTime || this.waitTime <= 0) {
			this.waitTime = SiteConstant.WAIT_TIME_FOR_CLOSE;
		}
		if (null == this.threadNum || this.threadNum <= 0) {
			this.threadNum = SiteConstant.DEFAULT_THREAD_NUM;
		}

		if (this.maxRedirects <= 0) {
			this.maxRedirects = SiteConstant.MAX_REDIRECTS;
		}

		if (this.interceptCount <= 0) {
			this.interceptCount = SiteConstant.INTERCEPT_RETRY_COUNT;
		}

		if (this.retryCount <= 0) {
			this.retryCount = SiteConstant.RETRY_COUNT;
		}

		if (this.maxDepth <= 0) {
			this.maxDepth = SiteConstant.MAX_REQUEST_DEPTH;
		}

		if (this.connectTimeout <= 0) {
			this.connectTimeout = SiteConstant.CONNECTION_TIME_OUT;
		}
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
