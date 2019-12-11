package com.yishuifengxiao.common.crawler.extractor.content.strategy;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.impl.ReplaceStrategy;

/**
 * 替换系统占位符
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class SystemStrategy extends ReplaceStrategy {

	@Override
	public String extract(String input, String param1, String param2) {
		return super.extract(input, CrawlerConstant.SEPARATOR, null != param1 ? param1 : "");
	}

}
