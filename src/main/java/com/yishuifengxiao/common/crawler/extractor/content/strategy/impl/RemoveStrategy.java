package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 字符移除策略<br/>
 * 根据参数移除输入数据里指定的信息
 * 
 * @author yishui
 * @version 1.0.0
 */
public class RemoveStrategy implements Strategy {

	/**
	 * 根据参数移除输入数据里指定的信息
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，需要移除的字符，可以为正则表达式
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1)) {
			return "";
		}
		return StringUtils.replace(input, param1, "");
	}
}
