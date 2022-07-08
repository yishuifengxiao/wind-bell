package com.yishuifengxiao.common.crawler.extractor;

import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.SimpleContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.impl.SimpleLinkExtractor;

/**
 * 提取器生成工厂<br/>
 * 根据提取规则生成对应提取器
 * 
 * @author yishui
 * @version 1.0.0
 */
public class ExtractorFactory extends AbstractExtractorFactory {

	/**
	 * 获取链接提取器
	 * 
	 * @return 链接提取器
	 */
	@Override
	public LinkExtractor getLinkExtractor() {
		return new SimpleLinkExtractor();
	}

	/**
	 * 根据内容提取规则生成内容提取器
	 * 
	 * @param contentRule 内容提取规则
	 * @return 内容提取器
	 */
	@Override
	public ContentExtractor getContentExtractor(ExtractRule contentRule) {
		return new SimpleContentExtractor(contentRule);
	}
}
