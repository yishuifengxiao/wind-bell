package com.yishuifengxiao.common.crawler.content;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.content.impl.SimpleContentExtract;
import com.yishuifengxiao.common.crawler.content.matcher.ContentMatcher;
import com.yishuifengxiao.common.crawler.content.matcher.SimpleContentMatcher;
import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.extractor.ExtractorFactory;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.CharsetContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.DescpContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.KeywordContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.TitleContentExtractor;
import com.yishuifengxiao.common.crawler.macther.MatcherFactory;
import com.yishuifengxiao.common.crawler.macther.PathMatcher;
import com.yishuifengxiao.common.crawler.utils.LocalCrawler;
import com.yishuifengxiao.common.tool.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

/**
 * 内容解析器装饰器<br/>
 * 进行内容解析前的前置操作<br/>
 * 功能如下：<br/>
 * 1. 决定是否对该网页进行内容提取<br/>
 * 2. 调用真正的内容解析器进行内容解析
 * 
 * @author yishui
 * @version 1.0.0
 */
@Slf4j
public class ContentExtractDecorator implements ContentExtract {
	/**
	 * 提取器生成工厂
	 */
	private final ExtractorFactory factory = new ExtractorFactory();

	/**
	 * 内容匹配器
	 */
	protected ContentMatcher contentMatcher = new SimpleContentMatcher();
	/**
	 * 匹配器工厂
	 */
	private MatcherFactory matcherFactory = new MatcherFactory();

	/**
	 * 用户自定义的内容解析器
	 */
	protected ContentExtract contentExtract;

	@Override
	public synchronized void extract(final ContentRule contentRule, final List<ExtractRule> rules, final Page page)
			throws CustomException {
		if (null == page) {
			return;
		}

		if (CrawlerConstant.SC_OK != page.getCode()) {
			page.setSkip(true);
			log.debug(
					"【id:{} , name:{} 】 The actual address of the page corresponding to request {} is 【 {} 】,and it has a response code of {} and will not extract data from it",
					LocalCrawler.get() != null ? LocalCrawler.get().getUuid() : "test",
					LocalCrawler.get() != null ? LocalCrawler.get().getName() : "test", page.getRequest().getUrl(),
					page.getCode());
			return;
		}

		// 根据请求的页面的地址判断是否符合内容页规则
		PathMatcher pathMatcher = this.matcherFactory.getMatcher(contentRule.getContentPageRule());

		boolean match = pathMatcher
				.match(null != page.getRedirectUrl() ? page.getRedirectUrl() : page.getRequest().getUrl());

		if (match) {
			// 根据请求的页面的内容判断是否符合内容页规则
			match = this.contentMatcher.match(contentRule.getPageRule(), page.getRawTxt());
		}

		log.debug(
				"【id:{} , name:{} 】 The actual address of the page corresponding to request {} is 【 {} 】, and the matching result of whether the content page address is satisfied is {}",
				LocalCrawler.get() != null ? LocalCrawler.get().getUuid() : "test",
				LocalCrawler.get() != null ? LocalCrawler.get().getName() : "test", page.getRequest().getUrl(),
				page.getRedirectUrl(), match);

		page.setSkip(!match);

		if (match) {
			this.extract(rules, contentRule, page);
		}

		log.debug("【id:{} , name:{} 】 The actual address of request {} is 【{}】, and the extracted data is {}",
				LocalCrawler.get() != null ? LocalCrawler.get().getUuid() : "test",
				LocalCrawler.get() != null ? LocalCrawler.get().getName() : "test", page.getRequest().getUrl(),
				page.getRedirectUrl(), page.getData());

	}

	/**
	 * 进行内容解析操作
	 * 
	 * @param rules       内容提取规则
	 * @param contentRule 内容解析规则
	 * @param page        网页对象
	 * @throws CustomException 解析和提取时可能出现的异常
	 */
	private void extract(final List<ExtractRule> rules, final ContentRule contentRule, final Page page)
			throws CustomException {
		// 构造出默认的内容解析器
		ContentExtract simpleContentExtract = new SimpleContentExtract(this.createContentExtractors(rules));
		// 内容解析与提取
		simpleContentExtract.extract(contentRule, rules, page);
		// 调用用户自定义内容解析器
		if (null != this.contentExtract) {
			synchronized (ContentExtractDecorator.class) {
				this.contentExtract.extract(contentRule, rules, page);
			}
		}
	}

	/**
	 * 生成所有的内容提取器
	 * 
	 * @param rules 内容提取规则
	 * @return 所有的内容提取器
	 */
	private List<ContentExtractor> createContentExtractors(final List<ExtractRule> rules) {
		// 根据内容解析规则获取到所有的内容抽取器
		List<ContentExtractor> contentExtractors = this.buildContentExtractor(rules);

		// 添加系统内置的抽取器
		contentExtractors.addAll(Arrays.asList(new DescpContentExtractor(), new KeywordContentExtractor(),
				new TitleContentExtractor(), new CharsetContentExtractor()));
		// 构建一个内容解析装饰器
		return contentExtractors;
	}

	/**
	 * 根据内容解析规则构建所有的内容提取器
	 * 
	 * @param extractRules 所有的提取规则
	 * @return 内容提取器集合
	 */
	private List<ContentExtractor> buildContentExtractor(List<ExtractRule> extractRules) {

		List<ContentExtractor> contentExtractors = new ArrayList<>();

		extractRules.stream().map(factory::getContentExtractor).forEach(contentExtractors::add);

		return contentExtractors.stream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	public ContentExtractDecorator(ContentExtract contentExtract) {
		this.contentExtract = contentExtract;

	}

}
