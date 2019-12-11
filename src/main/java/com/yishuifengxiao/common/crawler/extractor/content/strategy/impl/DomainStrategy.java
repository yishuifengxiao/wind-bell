package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;

/**
 * 域名提取策略
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class DomainStrategy extends RegexStrategy {

	@Override
	public String extract(String input, String param1, String param2) {
		return super.extract(input, RuleConstant.REGEX_DOMAIN, param2);
	}

}
