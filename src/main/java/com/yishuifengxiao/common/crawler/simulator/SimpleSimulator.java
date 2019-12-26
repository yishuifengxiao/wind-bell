package com.yishuifengxiao.common.crawler.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.yishuifengxiao.common.crawler.builder.ExtractBuilder;
import com.yishuifengxiao.common.crawler.builder.impl.SimpleExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentItem;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.downloader.impl.SimpleDownloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;

/**
 * 简单的模拟提取器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleSimulator implements Simulator {
	private ExtractBuilder extractBuilder = new SimpleExtractBuilder();

	@Override
	public SimulatorData extract(String url, SiteRule siteRule, ContentItem contentExtractRule, Downloader downloader) {

		SimulatorData simulatorData = null;
		try {
			ContentRule content = check(url, contentExtractRule);

			Page page = this.download(siteRule, url, downloader);
			// 内容解析器
			ContentExtract contentExtract = extractBuilder.createContentExtract(content, null);
			// 解析内容
			contentExtract.extract(page);
			Object data = page.getResultItem(contentExtractRule.getFiledName());
			simulatorData = new SimulatorData(true, data);

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, e.getMessage());
		}
		return simulatorData;
	}

	@Override
	public SimulatorData link(SiteRule siteRule, LinkRule linkRule, Downloader downloader) {

		SimulatorData simulatorData = null;
		try {
			check(linkRule);

			Page page = this.download(siteRule, linkRule.getStartUrl(), downloader);

			// 内容解析器
			LinkExtract linkExtract = extractBuilder.createLinkExtract(linkRule, null);
			// 解析链接
			linkExtract.extract(page);

			simulatorData = new SimulatorData(true, page.getLinks());

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, e.getMessage());
		}
		return simulatorData;
	}

	/**
	 * 下载网页
	 * 
	 * @param siteRule
	 * @param url
	 * @param downloader
	 * @return
	 * @throws Exception
	 */
	private Page download(SiteRule siteRule, String url, Downloader downloader) throws Exception {
		siteRule = siteRule == null ? new SiteRule().setHeaders(new ArrayList<>()) : siteRule;
		downloader = null != downloader ? downloader : new SimpleDownloader();
		Page page = downloader.down(siteRule, url);
		if (null == page) {
			throw new Exception("下载失败");
		}
		if (HttpStatus.SC_OK != page.getCode()) {
			throw new Exception(page.getRawTxt());
		}
		return page;
	}

	private void check(LinkRule linkRule) throws Exception {
		if (linkRule == null) {
			throw new Exception("链接提取规则不能为空");
		}
		if (linkRule.getStartUrl() == null || "".equals(linkRule.getStartUrl())) {
			throw new Exception("起始链接链接提取规则不能为空");
		}
		if (null == linkRule.getRules() || linkRule.getRules().size() == 0) {
			throw new Exception("请至少配置一个链接提取规则");
		}
	}

	/**
	 * 数据校验
	 * 
	 * @param url
	 * @param contentExtractRule
	 * @return
	 * @throws Exception
	 */
	private ContentRule check(String url, ContentItem contentExtractRule) throws Exception {

		if (StringUtils.isBlank(url)) {
			throw new Exception("测试网址不能为空");
		}

		if (contentExtractRule == null) {
			throw new Exception("提取规则不能为空");
		}

		if (StringUtils.isBlank(contentExtractRule.getName()) || contentExtractRule.getRules() == null
				|| contentExtractRule.getRules().isEmpty()) {
			throw new Exception("请配置正确的提取规则");
		}
		List<FieldExtractRule> rules = contentExtractRule.getRules().parallelStream().filter(t -> t.getRule() != null)
				.collect(Collectors.toList());
		if (rules == null || rules.isEmpty()) {
			throw new Exception("请至少配置一个正确的提取规则");
		}
		contentExtractRule.setRules(rules);

		ContentRule content = new ContentRule().setContents(Arrays.asList(contentExtractRule));
		return content;
	}

}
