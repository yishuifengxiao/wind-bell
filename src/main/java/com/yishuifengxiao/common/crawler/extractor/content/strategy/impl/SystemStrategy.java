package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;

/**
 * 系统占位符替换策略<br/>
 * 将输入数据中的系统占位符<code>[@&#60;yishui>@]</code>替换为指定的字符
 * @author yishui
 * @version 1.0.0
 */
public class SystemStrategy extends ReplaceStrategy {


	/**
	 * 将输入数据中的系统占位符<code>[@&#60;yishui>@]</code>替换为指定的字符
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，需要替换后出现的字符
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		return super.extract(input, CrawlerConstant.SEPARATOR, null != param1 ? param1 : "");
	}

}
