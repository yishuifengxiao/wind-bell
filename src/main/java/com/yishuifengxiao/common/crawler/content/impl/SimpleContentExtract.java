package com.yishuifengxiao.common.crawler.content.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 默认实现的简单内容解析器<br/>
 * 功能如下：<br/>
 * 1. 调用内容提取器对输入内容里的数据进行解析
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-15
 */
public class SimpleContentExtract implements ContentExtract {

	private List<ContentExtractor> contentExtractors;

	@Override
	public void extract(final Page page) throws ServiceException {
		// 提取出所有属性数据
		Map<String, Object> data = this.extractContent(page.getRawTxt());
		// 设置输出数据
		page.setResultItem(data);
	}

	/**
	 * 数据抽取
	 *
	 * @param rawText
	 * @return
	 */
	private Map<String, Object> extractContent(String rawText) {
		// 调用内容抽取器进行抽取
		Map<String, Object> map = this.contentExtractors.parallelStream().filter(Objects::nonNull).collect(
				Collectors.toMap(ContentExtractor::getName, t -> t.extract(rawText), (a, b) -> b, WeakHashMap::new));
		return map;
	}

	public SimpleContentExtract(List<ContentExtractor> contentExtractors) {
		this.contentExtractors = contentExtractors;
	}

}
