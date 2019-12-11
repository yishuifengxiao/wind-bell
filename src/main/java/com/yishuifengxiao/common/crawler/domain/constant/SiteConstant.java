package com.yishuifengxiao.common.crawler.domain.constant;

/**
 * 站点规则常量类
 * 
 * @author yishui
 * @date 2019年11月27日
 * @version 1.0.0
 */
public final class SiteConstant {

	/**
	 * 网页的缓存是由HTTP消息头中的“Cache-Control”来控制的，常见的取值有private、no-cache、max-age、must-revalidate等，默认为private。其作用根据不同的重新浏览方式分为以下几种情况。
	 */
	public final static String CACHE_CONTROL = "max-age=0";

	/**
	 * 平均每次请求的间隔时间，单位为秒
	 */
	public final static int REQUEST_INTERVAL_TIME = 10;
	
	/**
	 * 默认使用的线程数,默认值为 2
	 */
	public final static int DEFAULT_THREAD_NUM = 2;
	/**
	 * 连续多长时间没有新的求表明任务已经完成，单位为秒
	 */
	public final static int WAIT_TIME_FOR_CLOSE = 300;
	/**
	 * 连续多次在下载内容中获取到失败标识时的重试此次，默认为5
	 */
	public final static int INTERCEPT_RETRY_COUNT = 5;

	/**
	 * 请求失败时重新执行此请求的次数
	 */
	public final static int RETRY_COUNT = 3;
	/**
	 * 浏览器标志的请求头名字
	 */
	public final static String USER_AGENT = "user-agent";

	/**
	 * Https请求
	 */
	public static final String HTTPS = "https";
	
	/**
	 * Http请求
	 */
	public static final String HTTP = "http";
	
	/**
	 * 浏览器标识符集合
	 */
	public static final String[] USER_AGENT_ARRAY = new String[] { UserAgentConstant.USER_AGENT_GOOGLE_VERSION_78_0,
			UserAgentConstant.USER_AGENT_GOOGLE_VERSION_75_0, UserAgentConstant.USER_AGENT_FIREFOX_VERSION_70_0,
			UserAgentConstant.USER_AGENT_FIREFOX_VERSION_MAC, UserAgentConstant.USER_AGENT_IE_VERSION_11_476,
			UserAgentConstant.USER_AGENT_IE_VERSION_9_0, UserAgentConstant.USER_AGENT_EDAG_VERSION_11_476,
			UserAgentConstant.USER_AGENT_SAFARI_VERSION_MAC, UserAgentConstant.USER_AGENT_SAFARI_VERSION_WINDOWS,
			UserAgentConstant.USER_AGENT_OPERA_VERSION_WINDOWS, UserAgentConstant.USER_AGENT_OPERA_VERSION_MAC,
			UserAgentConstant.USER_AGENT_WORLD_VERSION_WINDOWS, UserAgentConstant.USER_AGENT_360_VERSION_WINDOWS,
			UserAgentConstant.USER_AGENT_LBBROWSER_VERSION_WINDOWS, UserAgentConstant.USER_AGENT_AVANT_VERSION_WINDOWS,
			UserAgentConstant.USER_AGENT_GREEN_VERSION_WINDOWS, UserAgentConstant.USER_AGENT_QQTT_VERSION_WINDOWS,
			UserAgentConstant.USER_AGENT_QQ_VERSION_WINDOWS, UserAgentConstant.USER_AGENT_SOUGOU_VERSION_WINDOWS,
			UserAgentConstant.USER_AGENT_AOYOU_VERSION_WINDOWS, UserAgentConstant.USER_AGENT_UC_VERSION_WINDOWS };

}
