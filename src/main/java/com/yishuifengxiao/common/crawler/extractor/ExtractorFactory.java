package com.yishuifengxiao.common.crawler.extractor;


import com.yishuifengxiao.common.crawler.domain.model.ContentItem;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.SimpleContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.impl.SimpleLinkExtractor;


/**
 * 提取器生成工厂
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-6
 */
public class ExtractorFactory extends AbstractExtractorFactory {

    @Override
    public LinkExtractor getLinkExtractor(String regex) {
        return new SimpleLinkExtractor(regex);
    }

    @Override
    public ContentExtractor getContentExtractor(ContentItem contentRule) {
        return new SimpleContentExtractor(contentRule);
    }
}
