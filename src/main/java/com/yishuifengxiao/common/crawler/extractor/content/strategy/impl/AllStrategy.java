package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;


import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 直接返回原始文本
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class AllStrategy implements Strategy {
    @Override
    public String extract(String input, String param1, String param2) {
        return input;
    }
}
