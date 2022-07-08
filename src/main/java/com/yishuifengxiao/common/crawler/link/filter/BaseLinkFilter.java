package com.yishuifengxiao.common.crawler.link.filter;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.link.filter.impl.NothingLinkFilter;

/**
 * 抽象链接过滤器<br/>
 * 将提取的链接地址转换成网络地址形式
 * 
 * @author yishui
 * @version 1.0.0
 */
public abstract class BaseLinkFilter {
	/**
	 * 下一个处理器
	 */
	private BaseLinkFilter next;

	/**
	 * 将提取的链接地址转换成网络地址形式
	 * 
	 * @param path 当前正在解析的网页的地址
	 * @param url  当前网页中提取出来的需要处理的网页地址
	 * @return 网络地址形式的链接地址
	 */
	public String doFilter(String path, String url) {
		if (!StringUtils.isNoneBlank(path, url)) {
			return null;
		}
		return this.doFilter(null != next ? next : new NothingLinkFilter(null), path, url);
	}

	/**
	 * 将提取的链接地址转换成网络地址形式
	 * 
	 * @param next 下一个过滤器
	 * @param path 当前正在解析的网页的地址
	 * @param url  当前网页中提取出来的需要处理的网页地址
	 * @return 网络地址形式的链接地址
	 */
	protected abstract String doFilter(BaseLinkFilter next, String path, String url);

	public BaseLinkFilter(BaseLinkFilter next) {
		this.next = next;
	}

}
