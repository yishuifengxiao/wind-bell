package com.yishuifengxiao.common.crawler.domain.constant;

/**
 * 风铃虫常量类
 * 
 * @author yishui
 * @version 1.0.0
 */
public final class CrawlerConstant {

	/**
	 * 等待下载的URL的集合
	 */
	public final static String WAIT_DOWN = "WAIT_DOWN_";

	/**
	 * 所有请求的集合
	 */
	public final static String REQUEST_HOSTORY = "REQUEST_HOSTORY_";

	/**
	 * 提取时出现多条数据拼接的标识符
	 */
	public final static String SEPARATOR = "[@<yishui>@]";
	/**
	 * 默认的请求成功时的响应码， 200
	 */
	public final static int SC_OK = 200;
	
	/**
	 * 默认的请求异常时的响应码，500
	 */
	public final static int SC_INTERNAL_SERVER_ERROR = 500;
}
