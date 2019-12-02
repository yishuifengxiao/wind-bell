package com.yishuifengxiao.common.crawler.http.request;

import org.apache.http.client.methods.HttpRequestBase;

import com.yishuifengxiao.common.crawler.http.config.HttpConfigBuilder;

/**
 * 请求生成器抽象实现类
 * 
 * @author yishui
 * @date 2019年11月27日
 * @version 1.0.0
 */
public abstract class BaseHttpRequest implements HttpRequest {

	protected HttpConfigBuilder httpConfigBuilder;

	@Override
	public HttpRequestBase create(String url) {
		//@formatter:off 
		//创建一个新的请求
		HttpRequestBase request = createRequest(url);

		
		//添加上配置信息
		request.setConfig(httpConfigBuilder.buildConfig());
		
		//添加上请求头
		httpConfigBuilder.buildHeaders().forEach((k,v)->{
			request.setHeader(k, v);
		});

		return request;
		
		//@formatter:on  
	}
    /**
     * 根据url创建出请求对象
     * @param url
     * @return
     */
	protected abstract HttpRequestBase createRequest(String url);

	public BaseHttpRequest(HttpConfigBuilder httpConfigBuilder) {
		this.httpConfigBuilder = httpConfigBuilder;
	}

	public HttpConfigBuilder getHttpConfigBuilder() {
		return httpConfigBuilder;
	}

	public void setHttpConfigBuilder(HttpConfigBuilder httpConfigBuilder) {
		this.httpConfigBuilder = httpConfigBuilder;
	}

}
