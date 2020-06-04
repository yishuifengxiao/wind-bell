package com.yishuifengxiao.common.crawler.simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.ContentExtractDecorator;
import com.yishuifengxiao.common.crawler.content.matcher.ContentMatcher;
import com.yishuifengxiao.common.crawler.content.matcher.SimpleContentMatcher;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractFieldRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.MatcherRule;
import com.yishuifengxiao.common.crawler.domain.model.PageRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.downloader.impl.SimpleDownloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.link.LinkExtractDecorator;
import com.yishuifengxiao.common.crawler.macther.MatcherFactory;
import com.yishuifengxiao.common.crawler.scheduler.request.RequestCreater;
import com.yishuifengxiao.common.crawler.scheduler.request.SimpleRequestCreater;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;
import com.yishuifengxiao.common.tool.exception.CustomException;

/**
 * 简单的模拟提取器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleSimulator implements Simulator {

	/**
	 * 匹配器工厂
	 */
	private MatcherFactory matcherFactory = new MatcherFactory();
	/**
	 * 内容匹配器
	 */
	protected ContentMatcher contentMatcher = new SimpleContentMatcher();
	/**
	 * 请求生成器
	 */
	private RequestCreater requestCreater = new SimpleRequestCreater();

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
			LinkExtract linkExtract = new LinkExtractDecorator(null);
			// 解析链接
			linkExtract.extract(linkRule, page);

			simulatorData = new SimulatorData(true, "链接解析成功", page.getLinks());

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, "链接解析失败", e.getMessage());
		}
		return simulatorData;
	}

	@Override
	public SimulatorData match(String url, SiteRule siteRule, ContentRule content, Downloader downloader) {
		SimulatorData simulatorData = null;
		try {
			Page page = this.download(siteRule, url, downloader);
			if (null == page || null == page.getRawTxt()) {
				return new SimulatorData(false, "下载结果为空", null);
			}
			if (null == content) {
				simulatorData = new SimulatorData(true, "匹配通过", "匹配规则为空时直接通过");
			} else {
				if (!matcherFactory.getMatcher(content.getContentPageRule()).match(url)) {
					simulatorData = new SimulatorData(true, "匹配不通过", "目标地址不符合内容页地址规则");
				} else {
					boolean match = contentMatcher.match(content.getPageRule(), page.getRawTxt());
					simulatorData = new SimulatorData(match, match ? "匹配通过" : "匹配失败", match ? "匹配通过" : "匹配失败");
				}

			}

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, "匹配过程出现异常", e.getMessage());
		}
		return simulatorData;
	}

	@Override
	public SimulatorData extract(String url, SiteRule siteRule, ExtractRule contentExtractRule, Downloader downloader) {

		SimulatorData simulatorData = null;
		try {
			this.check(contentExtractRule);

			Page page = this.download(siteRule, url, downloader);
			// 内容解析器
			ContentExtract contentExtract = new ContentExtractDecorator(null);
			// 解析内容
			contentExtract.extract(new ContentRule(new MatcherRule(), new PageRule()),
					Arrays.asList(contentExtractRule), page);
			// 解析出来的内容
			Object data = page.getData(contentExtractRule.getCode());
			simulatorData = new SimulatorData(true, "提取成功", data);

		} catch (Exception e) {
			simulatorData = new SimulatorData(false, "提取失败", e.getMessage());
		}
		return simulatorData;
	}

	/**
	 * 下载网页
	 * 
	 * @param siteRule   站点规则
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
		Request request = new Request(url, url);
		request = requestCreater.create(siteRule, request);
		Page page = downloader.down(request);
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
	 * @throws CustomException
	 */
	private void check(ExtractRule contentItem) throws CustomException {

		if (contentItem == null) {
			throw new CustomException("提取规则不能为空");
		}

		if (StringUtils.isBlank(contentItem.getName()) || contentItem.getRules() == null
				|| contentItem.getRules().isEmpty()) {
			throw new CustomException("请配置正确的内容提取规则");
		}
		List<ExtractFieldRule> rules = contentItem.getRules().stream().filter(t -> t.getRule() != null)
				.collect(Collectors.toList());
		if (rules == null || rules.isEmpty()) {
			throw new CustomException("请至少配置一个正确的属性提取规则");
		}
	}

}
