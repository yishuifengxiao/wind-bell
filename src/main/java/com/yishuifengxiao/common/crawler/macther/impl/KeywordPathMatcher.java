package com.yishuifengxiao.common.crawler.macther.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.macther.PathMatcher;

/**
 * 关键词匹配器<br/>
 * 被匹配的目标里必须包含指定的关键词
 * 
 * @author qingteng
 * @date 2020年5月23日
 * @version 1.0.0
 */
public class KeywordPathMatcher implements PathMatcher {

	/**
	 * 匹配表达式
	 */
	private String expression;

	@Override
	public boolean match(String url) {
		if (StringUtils.isBlank(this.expression)) {
			return true;
		}
		String[] keywords = StringUtils.split(this.expression, ",");
		for (String keyword : keywords) {
			if (StringUtils.isBlank(keyword)) {
				continue;
			}
			if (!StringUtils.containsIgnoreCase(url, keyword)) {
				return false;
			}
		}
		return true;
	}

	public KeywordPathMatcher(String expression) {

		this.expression = expression;
	}

}
