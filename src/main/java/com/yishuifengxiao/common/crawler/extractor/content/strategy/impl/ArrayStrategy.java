package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 截取数组中指定序号的元素
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
