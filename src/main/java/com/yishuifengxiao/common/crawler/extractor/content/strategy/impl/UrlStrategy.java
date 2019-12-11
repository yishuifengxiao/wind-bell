package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;

/**
 * url提取策略
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class UrlStrategy extends RegexStrategy {

	@Override
	public String extract(String input, String param1, String param2) {
		return super.extract(input, RuleConstant.REGEX_PROTOCOL_AND_HOST, param2);
	}

}
