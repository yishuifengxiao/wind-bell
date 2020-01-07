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
 * css文本提取策略<br/>
 * 此模式下只会包含内部的数据，不会包含外部html
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class CssTextStrategy implements Strategy {
	private final static Logger log = LoggerFactory.getLogger(CssTextStrategy.class);


	/**
	 * 此模式下只会包含内部的数据，不会包含外部html
	 *
	 * @param input    输入数据
	 * @param param1 第一个参数，必填，css选择器表达式
	 * @param param2 第二个参数 ，此模式下该参数无效
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
				out.add(e.ownText());
			});
		} catch (Exception e) {
			log.info("使用【css内部提取规则】 提取 {} 时出现问题，提取参数为 param1= {} ,param2 = {},问题为 {}", input, param1, param2,
					e.getMessage());
		}

		return out == null ? "" : String.join(CrawlerConstant.SEPARATOR, out);
	}

}
