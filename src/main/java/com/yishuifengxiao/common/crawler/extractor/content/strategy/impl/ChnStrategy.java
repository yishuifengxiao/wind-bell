package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;



/**
 * 提取中文
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class ChnStrategy extends RegexStrategy {
    private final static String REGEX = "[\\u4e00-\\u9fa5]{1,}";


    @Override
    public String extract(String input, String param1, String param2) {
        return super.extract(input, REGEX, param2);
    }
}
