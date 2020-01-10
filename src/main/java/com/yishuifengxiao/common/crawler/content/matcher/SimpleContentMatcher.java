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
	private MatcherRule contentDetect;

	@Override
	public boolean match(String input) {
		if (StringUtils.isBlank(input)) {
			return false;
		}
		if (null == contentDetect || null == contentDetect.getType()) {
			return true;
		}
		String extract = extract(input, contentDetect.getType(), contentDetect.getPattern());
		boolean match = StringUtils.isNotBlank(extract);

		if (contentDetect.getFuzzy()) {
			// 模糊匹配
			if (contentDetect.getCaseSensitive()) {
				// 大小写敏感
				match = StringUtils.contains(extract, contentDetect.getTarget());
			} else {
				match = StringUtils.containsIgnoreCase(extract, contentDetect.getTarget());
			}
		} else {
			// 精准匹配
			if (contentDetect.getCaseSensitive()) {
				// 大小写敏感
				match = StringUtils.equals(extract, contentDetect.getTarget());
			} else {
				match = StringUtils.equalsIgnoreCase(extract, contentDetect.getTarget());
			}
		}

		return match == contentDetect.getMode();
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

	public SimpleContentMatcher(MatcherRule contentDetect) {
		this.contentDetect = contentDetect;
	}

}
