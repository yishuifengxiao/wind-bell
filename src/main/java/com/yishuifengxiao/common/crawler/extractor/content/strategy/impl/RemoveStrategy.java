package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;


import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 删除其中的字符
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class RemoveStrategy implements Strategy {
    @Override
    public String extract(String input, String param1, String param2) {
        if (!StringUtils.isNoneBlank(input, param1)) {
            return "";
        }
        return StringUtils.replace(input,param1,"");
    }
}
