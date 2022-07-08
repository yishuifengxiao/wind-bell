package com.yishuifengxiao.common.crawler.extractor;

import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;

/**
 * 抽象提取器工厂
 * 
 * @author yishui
 * @version 1.0.0
 */
public abstract class AbstractExtractorFactory {
	/**
	 * 生成链接提取器
	 * 
	 * @return 链接提取器
	 */
	public abstract LinkExtractor getLinkExtractor();

	/**
	 * 根据内容提取规则生成内容提取器
	 * 
	 * @param contentRule 内容提取规则
	 * @return 内容提取器
	 */
	public abstract ContentExtractor getContentExtractor(ExtractRule contentRule);
}
