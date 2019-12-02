package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 替换其中的字符
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class ReplaceStrategy implements Strategy {
    @Override
    public String extract(String input, String param1, String param2) {
        return StringUtils.replace(input, param1, param2);
    }
}
