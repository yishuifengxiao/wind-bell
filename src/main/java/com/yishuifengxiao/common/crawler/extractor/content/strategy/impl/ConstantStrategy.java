package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 常量提取策略<br/>
 * 无论输入数据是什么，直接将设置的常量值作为输出数据输出
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class ConstantStrategy implements Strategy {

	/**
	 * 根据规则提取输入字符里的数据
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，设置的常量值
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		return param1;
	}
}
