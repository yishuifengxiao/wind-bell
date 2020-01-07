package com.yishuifengxiao.common.crawler.extractor.links.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.macther.PathMatcher;
import com.yishuifengxiao.common.crawler.macther.impl.SimplePathMatcher;

/**
 * 简单实现的链接提取器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
public class SimpleLinkExtractor implements LinkExtractor {

	/**
	 * 路径匹配工具
	 */
	private PathMatcher pathMatcher = new SimplePathMatcher();
	/**
	 * 路径的正则表达式
	 */
	private String regex;

	/**
	 * 提取链接
	 * 
	 * @param links 原始的链接数据
	 * @return 经过处理后的链接数据
	 */
	@Override
	public List<String> extract(List<String> links) {

		// 放入下一步的链接
		links = links.parallelStream().filter(t -> pathMatcher.match(regex, t)).collect(Collectors.toList());
		return links;
	}

	public SimpleLinkExtractor() {
	}

	public SimpleLinkExtractor(String regex) {
		this.regex = regex;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
