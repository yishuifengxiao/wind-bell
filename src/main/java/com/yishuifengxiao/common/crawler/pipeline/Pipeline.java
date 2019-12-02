package com.yishuifengxiao.common.crawler.pipeline;

import com.yishuifengxiao.common.crawler.domain.entity.ResultData;

/**
 * 信息输出器
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public interface Pipeline {
	/**
	 * 输出解析出来的数据
	 * 
	 * @param crawler    爬虫的定义信息
	 * @param resultData 爬虫的输出数据
	 */
	void recieve(final ResultData resultData);
}
