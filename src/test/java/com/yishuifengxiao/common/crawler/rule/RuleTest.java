package com.yishuifengxiao.common.crawler.rule;

import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

public class RuleTest {
	
	public void regex() {
		FieldExtractRule fieldExtractRule=new FieldExtractRule();
		fieldExtractRule.setParam1(null);
		fieldExtractRule.setParam2(null);
		fieldExtractRule.setRule(Rule.ALL);
		fieldExtractRule.setSort(1);
	}

}
