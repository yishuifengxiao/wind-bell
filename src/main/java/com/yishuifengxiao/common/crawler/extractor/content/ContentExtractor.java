package com.yishuifengxiao.common.crawler.extractor.content;

/**
 * 内容提取器<br/>
 * 根据内容提取规则从输入数据里提取出所有符合要求的数据
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
public interface ContentExtractor {
	/**
	 * 获取内容提取器的名字
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 提取数据
	 * 
	 * @param rawText 原始的未提取的数据
	 * @return 提取后的数据
	 */
	Object extract(String rawText);
}
