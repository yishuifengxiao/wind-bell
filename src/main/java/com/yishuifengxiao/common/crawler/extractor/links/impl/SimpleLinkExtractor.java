package com.yishuifengxiao.common.crawler.extractor.links.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;

import lombok.extern.slf4j.Slf4j;

/**
 * 简单实现的链接提取器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@Slf4j
public class SimpleLinkExtractor implements LinkExtractor {
	private final Strategy strategy = StrategyFactory.get(Rule.XPATH);

	@Override
	public List<String> extract(Page page) {
		//@formatter:off 
		try {
			String extract = strategy.extract(page.getRawTxt(), RuleConstant.XPATH_ALL_LINK, null);
			
			if (StringUtils.isNotBlank(extract)) {
				
				String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(extract,
						CrawlerConstant.SEPARATOR);
				
				if (tokens != null && tokens.length > 0) {
					return Arrays.asList(tokens);
				}
			}
		} catch (Exception e) {
			log.info("提取所有的超链接失败，失败的原因为 {}", e.getMessage());
		}
		//@formatter:on  
		return new ArrayList<>();
	}
}
