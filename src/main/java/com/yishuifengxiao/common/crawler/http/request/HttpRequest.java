package com.yishuifengxiao.common.crawler.http.request;

import org.apache.http.client.methods.HttpRequestBase;

/**
 * 请求生成器
 * 
 * @author yishui
 * @date 2019年11月27日
 * @version 1.0.0
 */
public interface HttpRequest {
	/**
	 * 根据URL生成请求数据
	 * 
	 * @param url
	 * @return
	 */
	HttpRequestBase create(String url);

}
