package com.yishuifengxiao.common.crawler.http.config;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie2;

import com.yishuifengxiao.common.crawler.domain.model.SiteRule;

/**
 * 请求配置，用于配置 Httpclient 和httprequest 的链接信息
 * 
 * @author yishui
 * @date 2019年11月25日
 * @version 1.0.0
 */
public class HttpConfigBuilder {

	private SiteRule siteRule;

	public RequestConfig buildConfig() {
		//@formatter:off 
		// Create global request configuration
		RequestConfig.Builder builder = RequestConfig.custom();
				       
		 builder
		        //确定是否应自动处理身份验证，默认为true
		        .setAuthenticationEnabled(true)
		        //确定是否应允许循环重定向，默认为false
		        .setCircularRedirectsAllowed(siteRule.isCircularRedirectsAllowed())
		        //返回从连接管理器请求连接时使用的超时（以毫秒为单位），默认为-1
		        .setConnectionRequestTimeout(-1)
		        //确定连接建立之前的超时时间（以毫秒为单位），默认为-1
		        .setConnectTimeout(siteRule.getConnectTimeout())
		        //是否请求目标服务器压缩内容。默认为true
		        .setContentCompressionEnabled(siteRule.isContentCompressionEnabled())
		        //确定用于HTTP状态管理的cookie规范的名称
		        .setCookieSpec(siteRule.getCookieSpec())
		        //确定是否为实体封装方法启用了“期望：100-连续握手，默认为false
		        .setExpectContinueEnabled(false)
		        //最大重定向数，默认为50
		        .setMaxRedirects(siteRule.getMaxRedirects())
		        //确定客户端是否应规范请求中的URI。默认为true
		       // .setNormalizeUri(siteRule.isNormalizeUri())
		        //确定是否应自动处理重定向，默认为true
		        .setRedirectsEnabled(siteRule.isRedirectsEnabled())
		        //确定是否应拒绝相对重定向。 默认为false
		        .setRelativeRedirectsAllowed(siteRule.isRelativeRedirectsAllowed())
		        //定义套接字超时，毫秒为单位，默认为-1
		        .setSocketTimeout(-1)
				//.setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
				//.setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC)).build()
				;
		
		//@formatter:on  
		return builder.build();
	}

	/**
	 * 构建出cookie信息
	 * 
	 * @return
	 */
	public CookieStore buildCookie() {
		CookieStore cookieStore = new BasicCookieStore();
		if (siteRule.getCookiValues() != null) {
			siteRule.getCookiValues().forEach((k, v) -> {
				Cookie cookie = new BasicClientCookie2(k, v);
				cookieStore.addCookie(cookie);
			});
		}
		return cookieStore;
	}

	/**
	 * 构造出请求头数据
	 * 
	 * @return Map<String, String> 键：请求头的名字，值：请求头的值
	 */
	public Map<String, String> buildHeaders() {
		Map<String, String> map = new WeakHashMap<String, String>();
		if (siteRule.getHeaders() != null) {
			siteRule.getHeaders().parallelStream().filter(t -> t != null)
					.filter(t -> StringUtils.isNoneBlank(t.getHeaderName(), t.getHeaderValue())).forEach(t -> {
						map.put(t.getHeaderName(), t.getHeaderValue());
					});
		}
		return map;
	}

	public SiteRule getSiteRule() {
		return siteRule;
	}

	public void setSiteRule(SiteRule siteRule) {
		this.siteRule = siteRule;
	}

	public HttpConfigBuilder(SiteRule siteRule) {
		this.siteRule = siteRule;
	}

}
