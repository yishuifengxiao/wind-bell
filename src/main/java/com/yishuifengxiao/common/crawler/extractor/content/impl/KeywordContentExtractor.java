package com.yishuifengxiao.common.crawler.extractor.content.impl;

import com.yishuifengxiao.common.crawler.domain.constant.NestConstant;
import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;

/**
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public class KeywordContentExtractor implements ContentExtractor {


    private final static String XPATH_STR = "//meta[@name='description']/@content";

    @Override
    public Object extract(String rawText) {


        String extract = StrategyFactory.get(Rule.XPATH).extract(rawText, XPATH_STR, "");

        return extract;
    }

    @Override
    public String getName() {
        return NestConstant.KEY_WORD;
    }
}
