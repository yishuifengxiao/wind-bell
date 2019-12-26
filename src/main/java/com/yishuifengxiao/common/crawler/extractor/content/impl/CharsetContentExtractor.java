package com.yishuifengxiao.common.crawler.extractor.content.impl;

import com.yishuifengxiao.common.crawler.domain.constant.NestConstant;
import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

/**
 * charset提取器<br/>
 * 提取网页中meta 区域中的keywords信息
 * 
 * @author yishui
 * @date 2019年12月26日
 * @version 1.0.0
 */
public class CharsetContentExtractor implements ContentExtractor {

	private final static String XPATH_STR = "//meta[@charset]/@charset";

	@Override
	public String getName() {

		return NestConstant.CHARSET;
	}

	@Override
	public Object extract(String rawText) {
		String extract = StrategyFactory.get(Rule.XPATH).extract(rawText, XPATH_STR, "");

		return extract;
	}

}
