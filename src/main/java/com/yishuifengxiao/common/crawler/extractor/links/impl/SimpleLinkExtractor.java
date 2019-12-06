package com.yishuifengxiao.common.crawler.extractor.links.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.utils.RegexFactory;

/**
 * 简单实现的链接提取器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
public class SimpleLinkExtractor implements LinkExtractor {

	private String regex;

	@Override
	public List<String> extract(List<String> links) {
		if (StringUtils.equalsIgnoreCase(regex, RuleConstant.ANT_MATCH_ALL)) {
			return links.parallelStream().filter(t -> StringUtils.isNotBlank(t)).collect(Collectors.toList());
		}
		// 放入下一步的链接
		links = links.parallelStream().filter(t -> RegexFactory.match(regex, t)).collect(Collectors.toList());
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
