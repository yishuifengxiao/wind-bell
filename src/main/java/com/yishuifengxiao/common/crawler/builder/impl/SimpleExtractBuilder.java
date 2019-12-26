package com.yishuifengxiao.common.crawler.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.builder.ExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.decorator.SimpleContentExtractDecorator;
import com.yishuifengxiao.common.crawler.content.impl.SimpleContentExtract;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.extractor.ExtractorFactory;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.CharsetContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.DescpContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.KeywordContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.TitleContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.link.LinkExtractDecorator;
import com.yishuifengxiao.common.crawler.link.LinkExtractProxy;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.AbsoluteLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.HttpLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.IllegalLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.NotLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.RelativeLinkFilter;

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
	 * 链接过滤器
	 */
	private BaseLinkFilter linkFilter = this.createLinkFilter();

	/**
	 * 真实的链接解析器，负责从网页里提取出所有的原始的超链接
	 */
	private LinkExtractProxy linkExtractProxy = new LinkExtractProxy();

	@Override
	public LinkExtract createLinkExtract(LinkRule link, LinkExtract linkExtract) {

		// 获取所有的链接提取器
		// 获取到所有的提取规则
		List<LinkExtractor> linkExtractors = link.getRules().parallelStream().map(t -> factory.getLinkExtractor(t))
				.collect(Collectors.toList());

		// 生成链接解析器
		return new LinkExtractDecorator(linkExtractProxy, linkExtract, this.linkFilter, linkExtractors);

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
	 * 根据内容提取规则构建所有的内容解析器
	 * 
	 * @param content 内容提取规则
	 * @return
	 */
	private List<ContentExtractor> buildContentExtractor(ContentRule content) {
		//@formatter:off 
		List<ContentExtractor> contentExtractors = new ArrayList<>();

		contentExtractors = content.getContents().parallelStream()
				.map(t -> factory.getContentExtractor(t))
				.collect(Collectors.toList());
	
		//@formatter:on  
		return contentExtractors;
	}

	/**
	 * 构建系统通用的解析器
	 * 
	 * @return
	 */
	private List<ContentExtractor> buildCommonExtractor() {
		return Arrays.asList(
				new DescpContentExtractor(), 
				new KeywordContentExtractor(), 
				new TitleContentExtractor(),
				new CharsetContentExtractor()
				);
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

	/**
	 * 构建链接过滤器
	 * 
	 * @return 链接过滤器
	 */
	private BaseLinkFilter createLinkFilter() {
		RelativeLinkFilter relativeLinkFilter = new RelativeLinkFilter(null);
		AbsoluteLinkFilter absoluteLinkFilter = new AbsoluteLinkFilter(relativeLinkFilter);
		HttpLinkFilter httpLinkFilter = new HttpLinkFilter(absoluteLinkFilter);
		NotLinkFilter notLinkFilter = new NotLinkFilter(httpLinkFilter);
		IllegalLinkFilter illegalLinkFilter = new IllegalLinkFilter(notLinkFilter);
		return illegalLinkFilter;
	}

}
