package com.yishuifengxiao.common.crawler.link.filter.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.link.LinkConverterChain;
import com.yishuifengxiao.common.crawler.link.filter.LinkFilter;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 默认的链接过滤器
 * 
 * @author yishui
 * @date 2019年12月9日
 * @version 1.0.0
 */
public class SimpleLinkFilter implements LinkFilter {

	private LinkConverterChain linkConverterChain;

	private List<String> keywords;

	@Override
	public List<String> filter(String path, List<String> urls) {
		if (urls == null) {
			return new ArrayList<>();
		}
		Set<String> links = this.filter(urls);
		return this.formatLink(path, links);
	}

	/**
	 * 对链接进行转换，将所有的链接都转换成网络地址
	 *
	 * @param path 当前正在爬取的网页的地址
	 * @param urls 未转换完成的链接
	 * @return 转换后的链接
	 */
	private List<String> formatLink(String path, Set<String> urls) {
		//@formatter:off 
		return urls.parallelStream()
				.map(t -> linkConverterChain.handle(path, t))
				.filter(t -> StringUtils.isNotBlank(t))
				.collect(Collectors.toList());
		//@formatter:on  
	}

	/**
	 * 提取的链接必须在爬虫实例对应的一级域名之内，防止默认情况下抓取到友情链接
	 * 
	 * @param urls
	 * @return
	 */
	private Set<String> filter(List<String> urls) {
		if (urls != null) {
			// 提取的链接必须在域名之内
			Set<String> links = urls.parallelStream().filter(t -> StringUtils.isNotBlank(t))
					.filter(t -> containsIgnoreCase(t)).collect(Collectors.toSet());
			return links;
		}

		return new HashSet<>();
	}

	/**
	 * 判断url里是否包含关键字
	 * 
	 * @param url
	 * @return
	 */
	private boolean containsIgnoreCase(String url) {
		// 提取出域名
		url = LinkUtils.extractDomain(url);
		for (String keyword : keywords) {
			if (StringUtils.containsIgnoreCase(url, keyword)) {
				return true;
			}
		}
		return false;
	}

	public SimpleLinkFilter(LinkConverterChain linkConverterChain, List<String> keywords) {
		this.linkConverterChain = linkConverterChain;
		this.keywords = keywords;
	}

}
