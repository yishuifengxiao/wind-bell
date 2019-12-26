package com.yishuifengxiao.common.crawler.downloader.impl;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于JSOUP实现的网页下载器<br/>
 * 特性如下:<br/>
 * 1. 支持下载https网站<br/>
 * 2. 支持设置cookie<br/>
 * 3. 支持设置user-agent<br/>
 * 
 * @author yishui
 * @date 2019年12月2日
 * @version 1.0.0
 */
@Slf4j
public class SimpleDownloader implements Downloader {

	private Map<String, String> cookies = null;

	@Override
	public Page down(final SiteRule siteRule, final String url) throws ServiceException {
		Page page = new Page(url);
		Response response = null;
		try {
			response = execute(siteRule, url);
			log.debug("Download page successfully ,the page is {}", url);
			//设置真实的请求地址
			page.setRedirectUrl(response.url().toString());
			page.setCode(response.statusCode());
			page.setRawTxt(response.body());
		} catch (IOException e) {
			log.info("An error occurred while downloading the page {}, the problem is {}", url, e.getMessage());
			page.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			page.setRawTxt(e.getMessage());
		}
		return page;
	}

	/**
	 * 执行下载请求
	 * 
	 * @param siteRule
	 * @param url
	 * @return
	 * @throws IOException
	 */
	private Response execute(SiteRule siteRule, String url) throws IOException {
		Connection connection = this.connection(url, "get",
				siteRule.getConnectTimeout() < 0 ? 0 : siteRule.getConnectTimeout(), siteRule.getAutoUserAgent(),
				StringUtils.isNotBlank(siteRule.getReferrer()) ? siteRule.getReferrer() : url, siteRule.getAllHeaders(),
				this.cookies);
		Response response = connection.execute();
		// 替换cookie信息
		this.cookies = response.cookies();
		return response;
	}

	/**
	 * 根据参数构建出请求连接
	 * 
	 * @param url       请求的目标URL
	 * @param method    请求类型
	 * @param timeOut   连接超时时间
	 * @param userAgent 浏览器标识
	 * @param referrer  流量来源页
	 * @param headers   请求头
	 * @param cookies
	 * @return 请求连接
	 */
	private Connection connection(String url, String method, int timeOut, String userAgent, String referrer,
			Map<String, String> headers, Map<String, String> cookies) {

		Assert.notNull(url, "请求的url不能为空");

		// 如果是Https请求
		if (url.startsWith(SiteConstant.HTTPS)) {
			getTrust();
		}

		Connection connection = Jsoup.connect(url);
		connection.method(getMethod(method));
		connection.timeout(timeOut);
		connection.ignoreHttpErrors(true);
		connection.ignoreContentType(true);
		connection.maxBodySize(0);
		if (null != headers) {
			headers.forEach((k, v) -> {
				connection.header(k, v);
			});

			// 浏览器标识
			connection.userAgent(userAgent);
			// 用于指明当前流量的来源参考页面
			connection.referrer(referrer);
		}
		if (null != cookies && !cookies.isEmpty()) {
			connection.cookies(cookies);
		}
		return connection;

	};

	/**
	 * 获取请求类型
	 * 
	 * @param methodStr
	 * @return
	 */
	private Method getMethod(String methodStr) {
		if (StringUtils.isBlank(methodStr)) {
			return Method.GET;
		}
		Method method = Method.GET;
		switch (methodStr.trim().toLowerCase()) {
		case "post":
			method = Method.POST;
			break;
		case "delete":
			method = Method.DELETE;
			break;
		case "put":
			method = Method.PUT;
			break;
		default:
			break;
		}
		return method;
	}

	/**
	 * 获取服务器信任
	 */
	private void getTrust() {
		try {
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, new X509TrustManager[] { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} }, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void close() {

	}

}
