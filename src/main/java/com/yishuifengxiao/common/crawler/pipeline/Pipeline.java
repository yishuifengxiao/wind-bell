package com.yishuifengxiao.common.crawler.pipeline;

import com.yishuifengxiao.common.crawler.domain.entity.CrawlerData;

/**
 * 信息输出器<br/>
 * 输出解析出来的数据
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public interface Pipeline {
	/**
	 * 输出解析出来的数据
	 * 
	 * @param crawlerData 风铃虫的输出数据
	 */
	void recieve(final CrawlerData crawlerData);
}
