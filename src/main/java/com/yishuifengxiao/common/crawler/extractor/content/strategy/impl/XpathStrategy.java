package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

import lombok.extern.slf4j.Slf4j;
import us.codecraft.xsoup.Xsoup;

/**
 * XPATH提取策略<br/>
 * 根据参数按照XPATH方式从输入数据中提取出所有符合要求的数据
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
@Slf4j
public class XpathStrategy implements Strategy {

	/**
	 * 根据参数按照XPATH方式从输入数据中提取出所有符合要求的数据
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，XPATH表达式
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1)) {
			return "";
		}

		try {
			List<String> list = Xsoup.compile(param1).evaluate(Jsoup.parse(input)).list();
			return String.join(CrawlerConstant.SEPARATOR, list);
		} catch (Exception e) {
			log.info("使用【XPATH规则】 提取 {} 时出现问题，提取参数为 param1= {} ,param2 = {},问题为 {}", input, param1, param2,
					e.getMessage());
		}

		return "";
	}
}
