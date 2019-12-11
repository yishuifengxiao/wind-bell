package com.yishuifengxiao.common.crawler.extractor;

import com.yishuifengxiao.common.crawler.domain.model.ContentExtractRule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;

/**
 * 抽象提取器工厂
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-6
 */
public abstract class AbstractExtractorFactory {
	/**
	 * 生成链接提取器
	 * 
	 * @param regex 链接的提取规则，正则表达式
	 * @return
	 */
	public abstract LinkExtractor getLinkExtractor(String regex);

	/**
	 * 生成内容提取器
	 * 
	 * @param contentRule 内容提取规则
	 * @return
	 */
	public abstract ContentExtractor getContentExtractor(ContentExtractRule contentRule);
}
