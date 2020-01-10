package com.yishuifengxiao.common.crawler.content.matcher;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.domain.eunm.Type;
import com.yishuifengxiao.common.crawler.domain.model.MatcherRule;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

/**
 * 默认实现的内容匹配器<br/>
 * 
 * @author yishui
 * @date 2020年01月09日
 * @version 1.0.0
 */
public class SimpleContentMatcher implements ContentMatcher {

	/**
	 * 内容匹配规则
	 */
	private MatcherRule matcherRule;

	@Override
	public boolean match(String input) {
		if (StringUtils.isBlank(input)) {
			return false;
		}
		if (null == matcherRule || null == matcherRule.getType()) {
			return true;
		}
		String extract = extract(input, matcherRule.getType(), matcherRule.getPattern());
		boolean match = StringUtils.isBlank(extract);
		if (!match) {
			if (matcherRule.getFuzzy()) {
				// 模糊匹配
				match = matcherRule.getCaseSensitive() ? StringUtils.contains(extract, matcherRule.getTarget())
						: StringUtils.containsIgnoreCase(extract, matcherRule.getTarget());

			} else {
				// 精准匹配
				match = matcherRule.getCaseSensitive() ? StringUtils.equals(extract, matcherRule.getTarget())
						: StringUtils.equalsIgnoreCase(extract, matcherRule.getTarget());

			}
		}

		return match == matcherRule.getMode();
	}

	/**
	 * 从网页内容里提取出对应的数据
	 * 
	 * @param input   内容信息
	 * @param type    内容匹配类型
	 * @param pattern 内容匹配参数
	 * @return 提取出的数据
	 */
	private String extract(String input, Type type, String pattern) {

		Strategy strategy = StrategyFactory.get(Rule.valueOf(type.toString()));

		return strategy.extract(input, pattern, null);
	}

	public SimpleContentMatcher(MatcherRule matcherRule) {
		this.matcherRule = matcherRule;
	}

}
