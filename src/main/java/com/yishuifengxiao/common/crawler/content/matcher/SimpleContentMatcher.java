package com.yishuifengxiao.common.crawler.content.matcher;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.domain.eunm.Type;
import com.yishuifengxiao.common.crawler.domain.model.PageRule;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

/**
 * 默认实现的内容匹配器<br/>
 * 
 * @author yishui
 * @version 1.0.0
 */
public class SimpleContentMatcher implements ContentMatcher {

	@Override
	public boolean match(PageRule pageRule, String input) {
		if (StringUtils.isBlank(input)) {
			return false;
		}
		if (null == pageRule || null == pageRule.getType() || pageRule.getType() == Type.NONE) {
			return true;
		}

		String extract = extract(input, pageRule.getType(), pageRule.getPattern());
		if (StringUtils.isBlank(extract)) {
			return false;
		}

		boolean match = StringUtils.isBlank(pageRule.getTarget());
		if (!match) {

			if (pageRule.getFuzzy()) {
				// 模糊匹配
				match = pageRule.getCaseSensitive() ? StringUtils.contains(extract, pageRule.getTarget())
						: StringUtils.containsIgnoreCase(extract, pageRule.getTarget());

			} else {
				// 精准匹配
				match = pageRule.getCaseSensitive() ? StringUtils.equals(extract, pageRule.getTarget())
						: StringUtils.equalsIgnoreCase(extract, pageRule.getTarget());

			}

		}

		return match == pageRule.getMode();
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

}
