package com.yishuifengxiao.common.crawler.simulator;

import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;

/**
 * 提取测试器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface Simulator {
	/**
	 * 提取测试
	 * 
	 * @param url                测试目标地址
	 * @param siteRule           站点规则
	 * @param contentExtractRule 内容提取规则
	 * @return
	 */
	SimulatorData extract(String url, SiteRule siteRule, ContentExtractRule contentExtractRule);

}
