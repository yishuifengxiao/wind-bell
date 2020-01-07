package com.yishuifengxiao.common.crawler.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.builder.ExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.decorator.SimpleContentExtractDecorator;
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
import com.yishuifengxiao.common.crawler.link.filter.impl.HashLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.HttpLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.IllegalLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.RelativeLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.ShortLinkFilter;

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

	/**
	 * 构建一个链接解析器
	 * 
	 * @param link        链接解析规则
	 * @param linkExtract 自定义链接解析器，允许为空
	 * @return 链接解析器
	 */
	@Override
	public LinkExtract createLinkExtract(LinkRule link, LinkExtract linkExtract) {

		// 根据链接解析规则获取所有的链接提取器
		List<LinkExtractor> linkExtractors = link.getRules().parallelStream().map(factory::getLinkExtractor)
				.collect(Collectors.toList());

		// 生成链接解析器
		return new LinkExtractDecorator(linkExtractProxy, linkExtract, this.linkFilter, linkExtractors);

	}

	/**
	 * 构造一个内容解析器
	 * 
	 * @param content        内容解析规则
	 * @param contentExtract 自定义内容解析器，允许为空
	 * @return 内容解析器
	 */
	@Override
	public ContentExtract createContentExtract(ContentRule content, ContentExtract contentExtract) {
		// 根据内容解析规则获取到所有的内容抽取器
		List<ContentExtractor> contentExtractors = this.buildContentExtractor(content);
		// 添加系统内置的抽取器
		contentExtractors.addAll(this.buildCommonExtractor());
		// 构建一个内容解析装饰器
		return new SimpleContentExtractDecorator(content.getExtractUrl(), contentExtract, contentExtractors);
	}

	/**
	 * 根据内容解析规则构建所有的内容提取器
	 * 
	 * @param content 内容解析规则
	 * @return 内容提取器集合
	 */
	private List<ContentExtractor> buildContentExtractor(ContentRule content) {

		List<ContentExtractor> contentExtractors = new ArrayList<>();

		content.getContents().parallelStream().map(factory::getContentExtractor).forEach(contentExtractors::add);

		return contentExtractors.parallelStream().filter(Objects::nonNull).collect(Collectors.toList());
	}

	/**
	 * 构建系统内置通用的提取器
	 * 
	 * @return 系统内置通用的提取器集合
	 */
	private List<ContentExtractor> buildCommonExtractor() {
		return Arrays.asList(new DescpContentExtractor(), new KeywordContentExtractor(), new TitleContentExtractor(),
				new CharsetContentExtractor());
	}

	/**
	 * 构建链接过滤器链
	 * 
	 * @return 链接过滤器链
	 */
	private BaseLinkFilter createLinkFilter() {
		RelativeLinkFilter relativeLinkFilter = new RelativeLinkFilter(null);
		HashLinkFilter hashLinkFilter = new HashLinkFilter(relativeLinkFilter);
		AbsoluteLinkFilter absoluteLinkFilter = new AbsoluteLinkFilter(hashLinkFilter);
		HttpLinkFilter httpLinkFilter = new HttpLinkFilter(absoluteLinkFilter);
		ShortLinkFilter shortLinkFilter = new ShortLinkFilter(httpLinkFilter);
		IllegalLinkFilter illegalLinkFilter = new IllegalLinkFilter(shortLinkFilter);
		return illegalLinkFilter;
	}

}
