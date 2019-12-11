package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * 字符全截取
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-8
 */
public class SubstrStrategy implements Strategy {
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
