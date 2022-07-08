package com.yishuifengxiao.common.crawler.extractor.links;

import java.util.List;

import com.yishuifengxiao.common.crawler.domain.entity.Page;

/**
 * 链接提取器<br/>
 * 从原始数据里提取出所有的链接
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface LinkExtractor {

	/**
	 * 提取链接
	 * 
	 * @param page 下载后的网页对象
	 * @return 提取出来的链接
	 */
	List<String> extract(Page page);
}
