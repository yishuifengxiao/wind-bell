package com.yishuifengxiao.common.crawler.extractor.content;

import java.util.List;

import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.ExtractFieldRule;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

/**
 * 简单内容提取器
 * 
 * @author yishui
 * @version 1.0.0
 */
public class SimpleContentExtractor extends AbstractContentExtractor {

	/**
	 * 提取数据
	 * 
	 * @param page 下载后的网页对象
	 * @return 提取后的数据
	 */
	@Override
	public Object extract(Page page, List<ExtractFieldRule> fieldExtractRules) {
		//获取
		String input = page.getRawTxt();
		// 获取到所有的处理规则
		for (ExtractFieldRule extractRule : fieldExtractRules) {
			Strategy strategy = StrategyFactory.get(extractRule.getRule());
			if (strategy == null) {
				continue;
			}
			input = strategy.extract(input, extractRule.getParam1(), extractRule.getParam2());
		}

		return input;
	}

	public SimpleContentExtractor(ExtractRule contentRule) {
		super(contentRule);
	}

}
