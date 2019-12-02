package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;
import com.yishuifengxiao.common.crawler.utils.RegexFactory;

/**
 * 正则提取
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class RegexStrategy implements Strategy {
	private final static Logger log = LoggerFactory.getLogger(RegexStrategy.class);

	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1)) {
			return "";
		}
		try {

			List<String> list = RegexFactory.extractAll(param1, input);
			if (list != null) {
				return String.join(CrawlerConstant.SEPARATOR, list);
			}

		} catch (Exception e) {
			log.info("使用【正则规则】 提取 {} 时出现问题，提取参数为 param1= {} ,param2 = {},问题为 {}", input, param1, param2,
					e.getMessage());
		}

		return "";
	}
}
