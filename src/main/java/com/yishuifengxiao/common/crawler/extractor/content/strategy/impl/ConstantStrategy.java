package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 输出为常量
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class ConstantStrategy implements Strategy {
    @Override
    public String extract(String input, String param1, String param2) {
        return param1;
    }
}
