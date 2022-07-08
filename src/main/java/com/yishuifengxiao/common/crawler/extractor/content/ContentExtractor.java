package com.yishuifengxiao.common.crawler.extractor.content;

import com.yishuifengxiao.common.crawler.domain.entity.Page;

/**
 * 内容提取器<br/>
 * 根据内容提取规则从输入数据里提取出所有符合要求的数据
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface ContentExtractor {
	/**
	 * 获取内容提取器的名字
	 * 
	 * @return 内容提取器的名字
	 */
	String getName();

	/**
	 * 从网页里提取出对应的数据
	 * 
	 * @param page 下载后的网页对象
	 * @return 提取后的数据
	 */
	Object extract(Page page);
}
