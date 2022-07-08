package com.yishuifengxiao.common.crawler.content.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.tool.exception.CustomException;

/**
 * 默认实现的简单内容解析器<br/>
 * 调用内容提取器对输入内容里的数据进行解析
 * 
 * @author yishui
 * @version 1.0.0
 */
public class SimpleContentExtract implements ContentExtract {

	private List<ContentExtractor> contentExtractors;

	@Override
	public void extract(final ContentRule contentRule, final List<ExtractRule> rules, final Page page)
			throws CustomException {

		// 调用内容抽取器进行抽取
		Map<String, Object> data = this.contentExtractors.stream().filter(Objects::nonNull).collect(
				Collectors.toMap(ContentExtractor::getName, t -> t.extract(page), (a, b) -> b, WeakHashMap::new));
		// 设置输出数据
		page.setData(data);
	}

	public SimpleContentExtract(List<ContentExtractor> contentExtractors) {
		this.contentExtractors = contentExtractors;
	}

}
