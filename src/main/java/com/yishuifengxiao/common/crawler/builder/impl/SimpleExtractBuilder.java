package com.yishuifengxiao.common.crawler.builder.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.builder.ExtractorBuilder;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.extractor.ExtractorFactory;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.DescpContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.KeywordContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.impl.TitleContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;

/**
 * 简单实现的提取器构建者
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public class SimpleExtractBuilder implements ExtractorBuilder {

	private ExtractorFactory factory = new ExtractorFactory();

	@Override
	public List<LinkExtractor> buildLinkExtractor(LinkRule link) {
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

	@Override
	public List<ContentExtractor> buildContentExtractor(ContentRule content) {
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

	@Override
	public List<ContentExtractor> buildCommonExtractor() {
		return Arrays.asList(new DescpContentExtractor(), new KeywordContentExtractor(), new TitleContentExtractor());
	}

	@Override
	public List<ContentExtractor> buildAllContentExtractor(ContentRule content) {
		List<ContentExtractor> linkExtractors = this.buildContentExtractor(content);
		linkExtractors.addAll(this.buildCommonExtractor());
		return linkExtractors;
	}

}
