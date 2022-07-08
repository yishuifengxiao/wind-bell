package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;
import com.yishuifengxiao.common.tool.utils.RegexUtil;


/**
 * 正则提取策略<br/>
 * 按照正则表达式从输入数据里提取出所有符合正则表达式的信息
 * 
 * @author yishui
 * @version 1.0.0
 */
public class RegexStrategy implements Strategy {
	private final static Logger log = LoggerFactory.getLogger(RegexStrategy.class);

	/**
	 * 按照正则表达式从输入数据里提取出所有符合正则表达式的信息
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，正则表达式
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1)) {
			return "";
		}
		try {

			List<String> list = RegexUtil.extractAll(param1, input);
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
