package com.yishuifengxiao.common.crawler.extractor.content.strategy;

/**
 * 提取策略<br/>
 * 根据对应的提取策略从输入数据里提取出对应的信息作为输出数据直接输出<br/>
 * 若处理失败或输入的参数为非法值时，输出数据为空字符串
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface Strategy {
	/**
	 * 根据规则提取输入字符里的数据
	 *
	 * @param input  输入字符
	 * @param param1 第一个参数
	 * @param param2 第二个参数
	 * @return 提取后的数据，注意可能是null或空字符串
	 */
	String extract(String input, String param1, String param2);
}
