package com.yishuifengxiao.common.crawler.http.context;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.HttpClientContext;

/**
 * 自定义客户端上下文
 * 
 * @author yishui
 * @date 2019年11月25日
 * @version 1.0.0
 */
public class ContextFactory {
	private CredentialsProvider credentialsProvider;

	public HttpClientContext create() {
		//@formatter:off 
	      // Create a local instance of cookie store

		// Execution context can be customized locally.
		HttpClientContext context = HttpClientContext.create();
		// Contextual attributes set the local context level will take
		// precedence over those set at the client level.
		context.setCredentialsProvider(credentialsProvider);
		//@formatter:on  
		return context;
	}

	public CredentialsProvider getCredentialsProvider() {
		return credentialsProvider;
	}

	public void setCredentialsProvider(CredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
	}

	public ContextFactory(CredentialsProvider credentialsProvider) {
		this.credentialsProvider = credentialsProvider;
	}

}
