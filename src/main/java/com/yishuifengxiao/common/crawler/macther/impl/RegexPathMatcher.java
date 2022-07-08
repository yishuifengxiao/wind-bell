package com.yishuifengxiao.common.crawler.macther.impl;

import java.util.regex.Pattern;

import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.macther.PathMatcher;
import com.yishuifengxiao.common.tool.utils.RegexUtil;

/**
 * 正则匹配器<br/>
 * 被匹配的内容必须符合指定的正则表达式
 * 
 * @author qingteng
 * @version 1.0.0
 */
public class RegexPathMatcher implements PathMatcher {

	/**
	 * 匹配表达式
	 */
	private Pattern pattern;

	@Override
	public boolean match(String url) {
		return this.pattern.matcher(url).matches();
	}

	public RegexPathMatcher(String expression) {
		Assert.notNull(expression, "匹配表达式不能为空");
		this.pattern = RegexUtil.pattern(expression);
	}

}
