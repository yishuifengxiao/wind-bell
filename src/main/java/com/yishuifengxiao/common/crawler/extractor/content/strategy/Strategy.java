package com.yishuifengxiao.common.crawler.extractor.content.strategy;

/**
 * 提取策略
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public interface Strategy {
    /**
     * 根据规则提取输入字符里的数据
     *
     * @param input  输入字符
     * @param param1 第一个参数
     * @param param2 第二个参数
     * @return 提取后的数据，注意可能是null或空字符串
     */
    String extract(String input, String param1, String param2);
}
