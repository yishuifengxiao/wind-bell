package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

/**
 * 中文提取策略<br/>
 * 提取出输入数据里的所有中文信息
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-7
 */
public class ChnStrategy extends RegexStrategy {
	private final static String REGEX = "[\\u4e00-\\u9fa5]{1,}";

	/**
	 * 根据规则提取输入字符里的数据
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，此模式下该参数无效
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		return super.extract(input, REGEX, param2);
	}
}
