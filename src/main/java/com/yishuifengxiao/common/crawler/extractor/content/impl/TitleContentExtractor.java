package com.yishuifengxiao.common.crawler.extractor.content.impl;

import com.yishuifengxiao.common.crawler.domain.constant.NestConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

/**
 * title提取器<br/>
 * 提取网页中head 区域中的title信息
 * 
 * @author yishui
 * @version 1.0.0
 */
public class TitleContentExtractor implements ContentExtractor {

	private final static String XPATH_STR = "//title/text()";

	@Override
	public Object extract(Page page) {

		String extract = StrategyFactory.get(Rule.XPATH).extract(page.getRawTxt(), XPATH_STR, "");

		return extract;
	}

	@Override
	public String getName() {
		return NestConstant.TITLE;
	}
}
