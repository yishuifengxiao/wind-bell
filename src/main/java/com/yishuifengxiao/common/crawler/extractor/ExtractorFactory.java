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
 * @date 2019-11-6
 */
public class ExtractorFactory extends AbstractExtractorFactory {

	/**
	 * 生成链接提取器
	 * 
	 * @param regex 链接的提取规则，正则表达式
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
