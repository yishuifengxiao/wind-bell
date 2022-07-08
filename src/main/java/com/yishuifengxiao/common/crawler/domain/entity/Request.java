package com.yishuifengxiao.common.crawler.domain.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 当前请求对象
 * 
 * @author qingteng
 * @version 1.0.0
 */
@ApiModel(value = "当前请求的请求信息")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Request implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2594453434210798576L;
	/**
	 * 请求的目标地址
	 */
	private String url;
	/**
	 * 请求的方法，默认为GET请求
	 */
	private String method;
	/**
	 * 请求的来源地址(默认为请求的目标地址被提取出来的页面的地址)
	 */
	private String referrer;
	/**
	 * 浏览器标志
	 */
	private String userAgent;

	/**
	 * 确定连接建立之前的超时时间（以毫秒为单位）。
	 */
	private int connectTimeout = SiteConstant.CONNECTION_TIME_OUT;
	/**
	 * 请求的优先级，数组越大优先级越高
	 */
	private long priority;
	/**
	 * 请求的深度，起始请求的深度为0
	 */
	private long depth=SiteConstant.DEFAULT_REQUEST_DEPTH;
	/**
	 * 需要额外携带的参数信息
	 */
	private Map<String, Object> extras;

	/**
	 * 当前请求时的携带的cookie信息
	 */
	private Map<String, String> cookies = new HashMap<String, String>();
	/**
	 * 当前请求时的请求头
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	/**
	 * 获取请求的目标地址
	 * 
	 * @return 请求的目标地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置请求的目标地址
	 * 
	 * @param url 请求的目标地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取请求的方法
	 * 
	 * @return 请求的方法
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 设置 请求的方法
	 * 
	 * @param method 请求的方法
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * 获取请求的来源地址
	 * 
	 * @return 请求的来源地址(默认为请求的目标地址被提取出来的页面的地址)
	 */
	public String getReferrer() {
		return referrer;
	}

	/**
	 * 设置请求的来源地址
	 * 
	 * @param referrer 请求的来源地址
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	/**
	 * 获取浏览器标志
	 * 
	 * @return 浏览器标志
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 设置浏览器标志
	 * 
	 * @param userAgent 浏览器标志
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	/**
	 * 获取确定连接建立之前的超时时间
	 * 
	 * @return 确定连接建立之前的超时时间
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * 设置确定连接建立之前的超时时间
	 * 
	 * @param connectTimeout 确定连接建立之前的超时时间
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * 获取请求的优先级
	 * 
	 * @return 请求的优先级，数组越大优先级越高
	 */
	public long getPriority() {
		return priority;
	}

	/**
	 * 设置请求的优先级
	 * 
	 * @param priority 请求的优先级，数组越大优先级越高
	 */
	public void setPriority(long priority) {
		this.priority = priority;
	}

	/**
	 * 获取请求时需要携带的额外的参数
	 * 
	 * @return 请求时需要携带的额外的参数
	 */
	public Map<String, Object> getExtras() {
		return extras;
	}

	/**
	 * 设置请求时需要携带的额外的参数
	 * 
	 * @param extras 请求时需要携带的额外的参数
	 */
	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

	/**
	 * 获取请求时的携带的cookie信息
	 * 
	 * @return 请求时的携带的cookie信息
	 */
	public Map<String, String> getCookies() {
		return cookies;
	}

	/**
	 * 设置请求时的携带的cookie信息
	 * 
	 * @param cookies 请求时的携带的cookie信息
	 */
	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	/**
	 * 获取当前请求时的请求头
	 * 
	 * @return 当前请求时的请求头
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}

	/**
	 * 设置当前请求时的请求头
	 * 
	 * @param headers 当前请求时的请求头
	 */
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	@Override
	public String toString() {
		return "Request [url=" + url + ", method=" + method + ", referrer=" + referrer + ", priority=" + priority
				+ ", extras=" + extras + ", cookies=" + cookies + ", headers=" + headers + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((method == null) ? 0 : method.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Request other = (Request) obj;
		if (method == null) {
			if (other.method != null) {
				return false;
			}
		} else if (!method.equals(other.method)) {
			return false;
		}
		if (url == null) {
			if (other.url != null) {
				return false;
			}
		} else if (!url.equals(other.url)) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param url      请求目标
	 * @param referrer 请求的来源页
	 */
	public Request(String url, String referrer) {
		this.url = url;
		this.referrer = referrer;
	}
    /**
     * 
     * @param url  请求目标
     * @param referrer 请求的来源页
     * @param depth 当前请求的深度
     */
	public Request(String url, String referrer, long depth) {
		this.url = url;
		this.referrer = referrer;
		this.depth = depth;
	}

}
