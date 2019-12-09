package com.yishuifengxiao.common.crawler.link.converter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.link.LinkConverterChain;
import com.yishuifengxiao.common.crawler.link.converter.LinkConverter;

/**
 * 默认的链接转换器
 * 
 * @author yishui
 * @date 2019年12月9日
 * @version 1.0.0
 */
public class SimpleLinkConverter implements LinkConverter {

	private LinkConverterChain linkConverterChain;

	/**
	 * 一级域名
	 */
	private String topLevelDomain;

	@Override
	public List<String> format(String path, List<String> urls) {
		if (urls == null) {
			return new ArrayList<>();
		}
		Set<String> links = this.formatLink(path, urls);
		return this.containsTopLevelDomain(links);
	}

	/**
	 * 对链接进行转换，将所有的链接都转换成网络地址
	 *
	 * @param path 当前正在爬取的网页的地址
	 * @param urls 未转换完成的链接
	 * @return 转换后的链接
	 */
	private Set<String> formatLink(String path, List<String> urls) {
		//@formatter:off 
		return urls.parallelStream()
				.filter(t -> StringUtils.isNotBlank(t))
				.map(t -> linkConverterChain.handle(path, t))
				.filter(t->t!=null)
				.collect(Collectors.toSet());
		//@formatter:on  
	}

	/**
	 * 提取的链接必须在爬虫实例对应的一级域名之内，防止默认情况下抓取到友情链接
	 * 
	 * @param urls
	 * @return
	 */
	private List<String> containsTopLevelDomain(Set<String> urls) {
		if (urls != null) {
			// 提取的链接必须在域名之内
			List<String> links = urls.parallelStream().filter(t -> StringUtils.isNotBlank(t))
					.filter(t -> StringUtils.containsIgnoreCase(t, topLevelDomain)).collect(Collectors.toList());
			if (links != null) {
				return new ArrayList<>(links);
			}
		}

		return new ArrayList<>();
	}

	public SimpleLinkConverter(LinkConverterChain linkConverterChain, String topLevelDomain) {
		this.linkConverterChain = linkConverterChain;
		this.topLevelDomain = topLevelDomain;
	}

}
