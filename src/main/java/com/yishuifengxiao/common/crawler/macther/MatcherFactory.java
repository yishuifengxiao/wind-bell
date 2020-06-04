package com.yishuifengxiao.common.crawler.macther;

import com.yishuifengxiao.common.crawler.domain.model.MatcherRule;
import com.yishuifengxiao.common.crawler.macther.impl.ExcludePathMatcher;
import com.yishuifengxiao.common.crawler.macther.impl.KeywordPathMatcher;
import com.yishuifengxiao.common.crawler.macther.impl.RegexPathMatcher;
import com.yishuifengxiao.common.crawler.macther.impl.SimplePathMatcher;

/**
 * 匹配器工厂
 * 
 * @author qingteng
 * @date 2020年5月23日
 * @version 1.0.0
 */
public class MatcherFactory {
	/**
	 * 默认的匹配器
	 */
	private PathMatcher pathMatcher = new SimplePathMatcher();

	/**
	 * 根据链接匹配规则生成对应的匹配器
	 * 
	 * @param pattern 链接匹配规则
	 * @return 匹配器
	 */
	public PathMatcher getMatcher(MatcherRule matcherRule) {

		if (matcherRule == null || matcherRule.getPattern() == null) {
			return pathMatcher;
		}

		switch (matcherRule.getPattern()) {
		case KEYWORD:
			pathMatcher = new KeywordPathMatcher(matcherRule.getExpression());
			break;
		case REGEX:
			pathMatcher = new RegexPathMatcher(matcherRule.getExpression());
			break;
		case NONE:
			pathMatcher = new SimplePathMatcher();
			break;
		case EXCLUDE:
			pathMatcher = new ExcludePathMatcher(matcherRule.getExpression());
			break;
		default:
			break;
		}
		return pathMatcher;
	}
}
