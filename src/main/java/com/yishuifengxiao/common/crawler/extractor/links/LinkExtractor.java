package com.yishuifengxiao.common.crawler.extractor.links;

import java.util.List;

/**
 * 链接提取器<br/>
 * 根据链接提取规则从原始的链接数据中提取出所有符合链接提取规则的链接数据
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
public interface LinkExtractor {
	/**
	 * 提取链接
	 * 
	 * @param links 原始的链接数据
	 * @return 经过处理后的链接数据
	 */
	List<String> extract(List<String> links);
}
