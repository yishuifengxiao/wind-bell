package com.yishuifengxiao.common.crawler.http.request;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import com.yishuifengxiao.common.crawler.http.config.HttpConfigBuilder;

/**
 * 默认的请求生成器，生成GET类型的请求
 * 
 * @author yishui
 * @date 2019年11月27日
 * @version 1.0.0
 */
public class HttpGetRequest extends BaseHttpRequest {

	public HttpGetRequest(HttpConfigBuilder httpConfigBuilder) {
		super(httpConfigBuilder);
	}

	@Override
	protected HttpRequestBase createRequest(String url) {

		return new HttpGet(url);
	}

}
