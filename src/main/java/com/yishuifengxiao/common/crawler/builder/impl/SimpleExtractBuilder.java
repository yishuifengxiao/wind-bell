package com.yishuifengxiao.common.crawler.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.builder.ExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.decorator.SimpleContentExtractDecorator;
import com.yishuifengxiao.common.crawler.content.impl.SimpleContentExtract;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.extractor.ExtractorFactory;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.DescpContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.KeywordContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.TitleContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.link.LinkConverterChain;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.link.LinkExtractDecorator;
import com.yishuifengxiao.common.crawler.link.LinkExtractProxy;
import com.yishuifengxiao.common.crawler.link.converter.LinkConverter;
import com.yishuifengxiao.common.crawler.link.converter.impl.SimpleLinkConverter;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 简单的解析器构造者
 * 
 * @author yishui
 * @date 2019年12月10日
 * @version 1.0.0
 */
public class SimpleExtractBuilder implements ExtractBuilder {
	/**
	 * 提取器生成工厂
	 */
	private ExtractorFactory factory = new ExtractorFactory();
	/**
	 * 链接转换器
	 */
	private LinkConverterChain strategyChain = new LinkConverterChain();

	/**
	 * 真实的链接解析器，负责从网页里提取出所有的原始的超链接
	 */
	private LinkExtractProxy linkExtractProxy = new LinkExtractProxy();

	@Override
	public LinkExtract createLinkExtract(LinkRule link, LinkExtract linkExtract) {

		// 获取所有的链接提取器
		List<LinkExtractor> linkExtractors = this.buildLinkExtractor(link);
		// 链接转换器
		LinkConverter linkConverter = new SimpleLinkConverter(strategyChain, this.getTopLevelDomain(link));
		// 生成链接解析器
		return new LinkExtractDecorator(linkExtractProxy, linkExtract, linkConverter, linkExtractors);

	}

	@Override
	public ContentExtract createContentExtract(ContentRule content, ContentExtract contentExtract) {
		// 获取到所有的内容抽取器
		List<ContentExtractor> contentExtractors = this.buildAllContentExtractor(content);
		// 构建一个内容解析器
		ContentExtract simpleContentExtract = contentExtract != null ? contentExtract
				: new SimpleContentExtract(contentExtractors);
		// 构建一个内容解析装饰器
		return new SimpleContentExtractDecorator(content.getExtractUrl(), simpleContentExtract);
	}

	/**
	 * 获取当前爬虫爬取的一级域名<br/>
	 * 形式如 yishuifengxiao.com
	 * 
	 * @return 一级域名
	 */
	private String getTopLevelDomain(LinkRule link) {
		if (null == link) {
			throw new IllegalArgumentException("链接提取规则不能为空");
		}
		if (null == link.getStartUrl() || "".equals(link.getStartUrl())) {
			throw new IllegalArgumentException("起始链接不能为空");
		}
		String domain = LinkUtils.extractTopLevelDomain(link.getStartUrl());
		return domain;
	}

	/**
	 * 根据链接解析规则构建所有的链接提取器
	 * 
	 * @param link 链接解析规则
	 * @return
	 */
	private List<LinkExtractor> buildLinkExtractor(LinkRule link) {
		//@formatter:off 
		List<LinkExtractor> linkExtractors = new ArrayList<>();
		if (link != null && link.getRules() != null) {
			// 获取到所有的提取规则
			linkExtractors = link.getRules().parallelStream()
					.filter(t -> StringUtils.isNotBlank(t))
					.map(t -> factory.getLinkExtractor(t))
					.collect(Collectors.toList());
		}
		//@formatter:on  
		return linkExtractors;
	}

	/**
	 * 根据内容提取规则构建所有的内容解析器
	 * 
	 * @param content 内容提取规则
	 * @return
	 */
	private List<ContentExtractor> buildContentExtractor(ContentRule content) {
		//@formatter:off 
		List<ContentExtractor> contentExtractors = new ArrayList<>();
		if (content != null && content.getContents() != null) {

			contentExtractors = content.getContents().parallelStream()
					.filter(t -> t != null)
					.map(t -> factory.getContentExtractor(t))
					.collect(Collectors.toList());
		}
		//@formatter:on  
		return contentExtractors;
	}

	/**
	 * 构建系统通用的解析器
	 * 
	 * @return
	 */
	private List<ContentExtractor> buildCommonExtractor() {
		return Arrays.asList(new DescpContentExtractor(), new KeywordContentExtractor(), new TitleContentExtractor());
	}

	/**
	 * 根据内容解析规则构建出系统中所有需要使用到的内容提取器
	 * 
	 * @param content 内容提取规则
	 * @return
	 */
	private List<ContentExtractor> buildAllContentExtractor(ContentRule content) {
		List<ContentExtractor> linkExtractors = this.buildContentExtractor(content);
		linkExtractors.addAll(this.buildCommonExtractor());
		return linkExtractors;
	}

}
