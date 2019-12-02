package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

/**
 * 提取数字
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class NumStrategy  extends RegexStrategy {
    private final static String REGEX = "[0-9]*";


    @Override
    public String extract(String input, String param1, String param2) {
        return super.extract(input, REGEX, param2);
    }
}
