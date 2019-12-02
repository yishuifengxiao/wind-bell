package com.yishuifengxiao.common.crawler.extractor.links;

import java.util.List;

/**
 * 链接提取器
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
public interface LinkExtractor {
    /**
     * 提取链接
     * @param links
     * @return
     */
    List<String> extract(List<String> links);
}
