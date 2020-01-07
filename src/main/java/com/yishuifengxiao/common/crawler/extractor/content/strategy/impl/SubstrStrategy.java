package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 字符截取策略<br/>
 * 根据参数从输入数据中截取指定长度的字符
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-8
 */
public class SubstrStrategy implements Strategy {

	/**
	 * 根据参数从输入数据中截取指定长度的字符
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，必须为数字，开始截取的位置
	 * @param param2 第二个参数 ，必填，必须为数字，结束截取的位置
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1, param2)) {
			return "";
		}
		if (!NumberUtils.isCreatable(param1) || !NumberUtils.isCreatable(param2)) {
			return "";
		}
		try {
			int start = Integer.valueOf(param1);
			int end = Integer.valueOf(param2);
			if (start > 0 && end > 0 && end > start) {
				return StringUtils.substring(input, start, end);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return "";
	}
}
