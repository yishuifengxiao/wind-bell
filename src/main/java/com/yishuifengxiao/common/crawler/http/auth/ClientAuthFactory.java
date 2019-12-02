package com.yishuifengxiao.common.crawler.http.auth;

import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

/**
 * 用于使用HttpClient对需要用户身份验证的目标站点执行HTTP请求
 * 
 * @author yishui
 * @date 2019年11月25日
 * @version 1.0.0
 */
public class ClientAuthFactory {

	/**
	 * 生成一个认证信息，用于生成HttpClient时使用
	 * 
	 * @return
	 */
	public CredentialsProvider create() {
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
//		credsProvider.setCredentials(new AuthScope("httpbin.org", 80),
//				new UsernamePasswordCredentials("user", "passwd"));
		return credsProvider;
	}
}
