package com.yishuifengxiao.common.crawler.builder;

import java.util.List;

import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;

/**
 * 提取器构建者<br/>
 * 根据配置构造出所有需要的链接提取器和内容提取器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public interface ExtractorBuilder {
	/**
	 * 根据链接提取规则构建出所有的链接提取器
	 * 
	 * @param link
	 * @return
	 */
	List<LinkExtractor> buildLinkExtractor(LinkRule link);

	/**
	 * 根据内容提取规则构建出所有的内容提取器
	 * 
	 * @param content
	 * @return
	 */
	List<ContentExtractor> buildContentExtractor(ContentRule content);

	/**
	 * 构建出系统内容提取器
	 * 
	 * @return
	 */
	List<ContentExtractor> buildCommonExtractor();

	/**
	 * 根据内容提取规则构建出所有的内容提取器和系统内容提取器
	 * 
	 * @param content
	 * @return
	 */
	List<ContentExtractor> buildAllContentExtractor(ContentRule content);
}
