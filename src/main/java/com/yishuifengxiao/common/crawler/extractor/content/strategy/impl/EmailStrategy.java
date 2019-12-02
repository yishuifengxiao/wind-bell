package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

/**
 * 提取中文
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class EmailStrategy extends RegexStrategy {
    private final static String REGEX = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";


    @Override
    public String extract(String input, String param1, String param2) {
        return super.extract(input, REGEX, param2);
    }
}
