package com.yishuifengxiao.common.crawler.http;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.http.auth.ClientAuthFactory;
import com.yishuifengxiao.common.crawler.http.config.HttpConfigBuilder;
import com.yishuifengxiao.common.crawler.http.context.ContextFactory;
import com.yishuifengxiao.common.crawler.http.request.HttpGetRequest;
/**
 * http请求客户端
 * @author yishui
 * @date 2019年12月2日
 * @version 1.0.0
 */
public class HttpClient {

	private HttpConfigBuilder httpConfigBuilder;

	private ClientAuthFactory clientAuthFactory = new ClientAuthFactory();

	public CloseableHttpResponse execute(String url) throws Exception {

		CloseableHttpClient closeableHttpClient = createClient();

		HttpClientContext context = new ContextFactory(clientAuthFactory.create()).create();
		// 设置cookie信息
		CookieStore cookieStore = httpConfigBuilder.buildCookie();
		context.setCookieStore(cookieStore);

		HttpRequestBase htttRequest = buildRequest(url);
		return closeableHttpClient.execute(htttRequest, context);
	}

	/**
	 * 将提取的链接转换为请求对象
	 */
	public HttpRequestBase buildRequest(String url) {

		// 生成请求
		HttpGetRequest httpGetRequest = new HttpGetRequest(httpConfigBuilder);

		return httpGetRequest.create(url);
	}

	public CloseableHttpClient createClient() throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException, CertificateException, IOException {
		//@formatter:off 
	    // SSL context for secure connections can be created either based on
        // system or application specific properties.
        SSLContext sslcontext = SSLContext.getInstance("TLS");
        
        sslcontext.init(null, new X509TrustManager[] { new X509TrustManager() {
        	 @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        	 @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
        	 @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        } }, new SecureRandom());

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
               null,
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		
		// Create an HttpClient with the given custom dependencies and configuration.
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		
		
		
		

		httpClientBuilder
			.setDefaultRequestConfig(httpConfigBuilder.buildConfig())
			.setDefaultCredentialsProvider(clientAuthFactory.create())
			.setDefaultCookieStore(getCookieStore())
			.setSSLSocketFactory(sslsf)
		;
		
		
		//@formatter:on  
		return httpClientBuilder.build();
	}

	private CookieStore getCookieStore() {
		return new BasicCookieStore();
	}

	public HttpClient(SiteRule siteRule) {
		this.httpConfigBuilder = new HttpConfigBuilder(siteRule);
	}

}
