package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 数组截取策略<br/>
 * 将输入数据根据分隔符分割为数组，然后从分割的数组中提取第N个元素
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-8
 */
public class ArrayStrategy implements Strategy {
	/**
	 * 最小的索引值
	 */
	private final static int MIN_INDEX = 1;


	/**
	 * 根据规则提取输入字符里的数据
	 *
	 * @param input   输入数据
	 * @param param1 第一个参数，必填，分隔符
	 * @param param2 第二个参数 ，必填，必须为数字，提取的元素的序号，从1开始计数
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param2)) {
			return "";
		}
		int index = NumberUtils.isCreatable(param2) ? Integer.valueOf(param2) : 0;
		if (index < MIN_INDEX) {
			index = MIN_INDEX;
		}
		String[] strs = StringUtils.splitPreserveAllTokens(input,
				StringUtils.isNotBlank(param1) ? param1 : CrawlerConstant.SEPARATOR);
		if (index > strs.length) {
			return "";
		}
		return strs[index - 1];
	}

}
