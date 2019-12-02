package com.yishuifengxiao.common.crawler.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.yishuifengxiao.common.crawler.Crawler;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.downloader.impl.SimpleDownloader;
import com.yishuifengxiao.common.crawler.extractor.ExtractorFactory;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;

/**
 * 简单的模拟提取器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleSimulator implements Simulator {
	private ExtractorFactory extractorFactory = new ExtractorFactory();

	private Crawler crawler;

	@Override
	public SimulatorData extract(String url, SiteRule siteRule, ContentExtractRule contentExtractRule) {

		SimulatorData simulatorData = check(url, siteRule, contentExtractRule);
		if (simulatorData != null) {
			return simulatorData;
		}
		try {
			Downloader downloader = new SimpleDownloader(siteRule);

			Page page = downloader.down(url);
			if (page.getCode() != HttpStatus.SC_OK) {
				simulatorData = new SimulatorData(false, page.getRawTxt());
			} else {
				ContentExtractor contentExtractor = extractorFactory.getContentExtractor(contentExtractRule);
				Object data = contentExtractor.extract(page.getRawTxt());
				simulatorData = new SimulatorData(true, data);
			}

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, e.getMessage());
		}
		return simulatorData;
	}

	/**
	 * 数据校验
	 * 
	 * @param url
	 * @param contentExtractRule
	 * @return
	 */
	private SimulatorData check(String url, SiteRule siteRule, ContentExtractRule contentExtractRule) {
		if (StringUtils.isBlank(url)) {
			return new SimulatorData(false, "测试网址不能为空");
		}
		if (contentExtractRule == null) {
			return new SimulatorData(false, "提取规则不能为空");
		}
		if (siteRule == null) {
			siteRule = new SiteRule().setHeaders(new ArrayList<>());
		}
		if (StringUtils.isBlank(contentExtractRule.getName()) || contentExtractRule.getRules() == null
				|| contentExtractRule.getRules().isEmpty()) {
			return new SimulatorData(false, "请配置正确的提取规则");
		}
		List<FieldExtractRule> rules = contentExtractRule.getRules().parallelStream().filter(t -> t.getRule() != null)
				.collect(Collectors.toList());
		if (rules == null || rules.isEmpty()) {
			return new SimulatorData(false, "请至少配置一个正确的提取规则");
		}
		contentExtractRule.setRules(rules);
		return null;
	}

	public Crawler getCrawler() {
		return crawler;
	}

	public void setCrawler(Crawler crawler) {
		this.crawler = crawler;
	}

	public SimpleSimulator(Crawler crawler) {
		this.crawler = crawler;
	}

	public SimpleSimulator() {

	}

}
