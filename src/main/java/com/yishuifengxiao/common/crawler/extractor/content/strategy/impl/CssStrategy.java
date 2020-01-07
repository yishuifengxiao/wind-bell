package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

/**
 * css提取策略<br/>
 * 根据css选择器和属性按照CSS提取模式从输入数据里提取出对应区域的数据(可能是HTML片段)
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class CssStrategy implements Strategy {
	private final static Logger log = LoggerFactory.getLogger(CssStrategy.class);

	/**
	 * 根据css选择器和属性按照CSS提取模式从输入数据里提取出对应区域的数据(可能是HTML片段)
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，css选择器表达式
	 * @param param2 第二个参数 ，选填，希望提取的属性的名字
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1)) {
			return "";
		}
		List<String> out = new ArrayList<>();
		try {
			Document document = Jsoup.parse(input);
			Elements elements = document.select(param1);
			if (elements == null) {
				return "";
			}

			elements.forEach(e -> {
				if (StringUtils.isBlank(param2)) {
					out.add(e.outerHtml());
				} else {
					out.add(e.attr(param2));
				}
			});
		} catch (Exception e) {
			log.info("使用【css规则】 提取 {} 时出现问题，提取参数为 param1= {} ,param2 = {},问题为 {}", input, param1, param2,
					e.getMessage());
		}

		return out == null ? "" : String.join(CrawlerConstant.SEPARATOR, out);
	}
}
