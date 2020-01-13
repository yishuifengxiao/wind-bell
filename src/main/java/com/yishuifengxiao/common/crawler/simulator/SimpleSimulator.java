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
import com.yishuifengxiao.common.crawler.content.matcher.SimpleContentMatcher;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentItem;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.FieldExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.MatcherRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.downloader.impl.SimpleDownloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 简单的模拟提取器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleSimulator implements Simulator {
	private final ExtractBuilder extractBuilder = new SimpleExtractBuilder();

	@Override
	public SimulatorData down(String url, SiteRule siteRule, Downloader downloader) {
		SimulatorData simulatorData = null;
		try {
			Page page = this.download(siteRule, url, downloader);
			simulatorData = (null == page || null == page.getRawTxt()) ? new SimulatorData(false, "下载的结果为空", null)
					: new SimulatorData(true, "下载成功", page.getRawTxt());
		} catch (Exception e) {
			simulatorData = new SimulatorData(false, "下载失败", e.getMessage());
		}
		return simulatorData;
	}

	@Override
	public SimulatorData link(String url, SiteRule siteRule, LinkRule linkRule, Downloader downloader) {

		SimulatorData simulatorData = null;
		try {
			check(linkRule);

			Page page = this.download(siteRule, url, downloader);

			// 内容解析器
			LinkExtract linkExtract = extractBuilder.createLinkExtract(linkRule, null);
			// 解析链接
			linkExtract.extract(page);

			simulatorData = new SimulatorData(true, "链接解析成功", page.getLinks());

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, "链接解析失败", e.getMessage());
		}
		return simulatorData;
	}

	@Override
	public SimulatorData match(String url, SiteRule siteRule, MatcherRule matcherRule, Downloader downloader) {
		SimulatorData simulatorData = null;
		try {
			Page page = this.download(siteRule, url, downloader);
			if (null == page || null == page.getRawTxt()) {
				return new SimulatorData(false, "下载结果为空", null);
			}
			if (null == matcherRule || matcherRule.getType() == null) {
				simulatorData = new SimulatorData(true, "匹配通过", "匹配规则为空时直接通过");
			} else {
				boolean match = new SimpleContentMatcher(matcherRule).match(page.getRawTxt());
				simulatorData = new SimulatorData(match, match ? "匹配通过" : "匹配失败", null);
			}

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, "匹配过程出现异常", e.getMessage());
		}
		return simulatorData;
	}

	@Override
	public SimulatorData extract(String url, SiteRule siteRule, ContentItem contentExtractRule, Downloader downloader) {

		SimulatorData simulatorData = null;
		try {
			ContentRule content = check(contentExtractRule);

			Page page = this.download(siteRule, url, downloader);
			// 内容解析器
			ContentExtract contentExtract = extractBuilder.createContentExtract(content, null);
			// 解析内容
			contentExtract.extract(page);
			// 解析出来的内容
			Object data = page.getResultItem(contentExtractRule.getFiledName());
			simulatorData = new SimulatorData(true, "提取成功", data);

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, e.getMessage(), null);
		}
		return simulatorData;
	}

	/**
	 * 下载网页
	 * 
	 * @param siteRule   站点规则信息
	 * @param url        目标URL
	 * @param downloader 下载器
	 * @return 网页对象
	 * @throws Exception
	 */
	private Page download(SiteRule siteRule, String url, Downloader downloader) throws Exception {
		if (StringUtils.isBlank(url)) {
			throw new Exception("测试网址不能为空");
		}
		if (!LinkUtils.matchHttpRequest(url)) {
			throw new Exception("请输入正确的测试地址");
		}
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

	/**
	 * 校验链接解析规则
	 * 
	 * @param linkRule 链接解析规则
	 * @throws Exception
	 */
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
	 * 校验内容提取规则
	 * 
	 * @param contentItem 内容提取规则
	 * @return
	 * @throws Exception
	 */
	private ContentRule check(ContentItem contentItem) throws Exception {

		if (contentItem == null) {
			throw new Exception("提取规则不能为空");
		}

		if (StringUtils.isBlank(contentItem.getName()) || contentItem.getRules() == null
				|| contentItem.getRules().isEmpty()) {
			throw new Exception("请配置正确的内容提取规则");
		}
		List<FieldExtractRule> rules = contentItem.getRules().parallelStream().filter(t -> t.getRule() != null)
				.collect(Collectors.toList());
		if (rules == null || rules.isEmpty()) {
			throw new Exception("请至少配置一个正确的属性提取规则");
		}
		contentItem.setRules(rules);

		ContentRule content = new ContentRule().setContents(Arrays.asList(contentItem));
		return content;
	}

}
