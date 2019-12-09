package com.yishuifengxiao.common.crawler.link;

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

import lombok.extern.slf4j.Slf4j;

/**
 * 链接解析器器适配器 <br/>
 * 功能如下：<br/>
 * 1. 主要负责从原始网页文本中解析出所有的链接<br/>
 * 2. 将链接转换成网络地址形式
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
@Slf4j
public class LinkExtractProxy implements LinkExtract {

	private Strategy strategy = StrategyFactory.get(Rule.XPATH);

	@Override
	public synchronized void extract(Page page) {

		// 先提取出所有的链接
		List<String> urls = this.extractAllLinks(page.getRawTxt());

		// 将超链接放入目标里备用
		page.setLinks(urls);
	}

	/**
	 * 提取出所有的超链接
	 *
	 * @param rawtxt 原始文本
	 * @return
	 */
	private List<String> extractAllLinks(String rawtxt) {
		//@formatter:off 
		try {
			String extract = strategy.extract(rawtxt, RuleConstant.XPATH_ALL_LINK, null);
			
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
