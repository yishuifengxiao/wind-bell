package com.yishuifengxiao.common.crawler.macther.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.macther.PathMatcher;

/**
 * 排除匹配器<br/>
 * 被匹配的目标里不能包含包含指定的关键词
 * 
 * @author qingteng
 * @version 1.0.0
 */
public class ExcludePathMatcher implements PathMatcher {

	/**
	 * 匹配表达式
	 */
	private String expression;

	@Override
	public boolean match(String url) {
		if (StringUtils.isBlank(this.expression)) {
			return false;
		}
		String[] keywords = StringUtils.split(this.expression, ",");
		for (String keyword : keywords) {
			if (StringUtils.isBlank(keyword)) {
				continue;
			}
			if (StringUtils.containsIgnoreCase(url, keyword)) {
				return false;
			}
		}
		return true;
	}

	public ExcludePathMatcher(String expression) {
		this.expression = expression;
	}

}
