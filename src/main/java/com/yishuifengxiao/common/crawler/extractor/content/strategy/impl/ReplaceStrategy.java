package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 字符替换策略<br/>
 * 根据参数将输入数据中的原始字符替换为目标字符
 * 
 * @author yishui
 * @version 1.0.0
 */
public class ReplaceStrategy implements Strategy {

	/**
	 * 根据参数将输入数据中的原始字符替换为目标字符
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，希望替换的原始字符
	 * @param param2 第二个参数 ，必填，需要替换的目标字符
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		return StringUtils.replace(input, param1, param2);
	}
}
