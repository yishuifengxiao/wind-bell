package com.yishuifengxiao.common.crawler.listener;

import com.yishuifengxiao.common.crawler.domain.entity.Page;

/**
 * 爬虫处理事件监听器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public interface CrawlerListener {
	
	/**
	 * 抓取失败的消息
	 * 
	 * @param page 抓取失败的网页
	 * @param e 失败的原因
	 */
	void onError(final Page page, Exception e);

	/**
	 * 抓取成功
	 * 
	 * @param page 抓取成功的网页
	 */
	  void onSuccess(final Page page);

}
