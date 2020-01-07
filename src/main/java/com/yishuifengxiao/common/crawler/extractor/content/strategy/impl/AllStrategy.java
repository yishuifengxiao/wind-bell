package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 原文提取策略<br/>
 * 不对输入参数进行处理， 直接返回输入数据
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class AllStrategy implements Strategy {

	/**
	 * 不对输入参数进行处理， 直接返回输入数据
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，此模式下该参数无效
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		return input;
	}
}
