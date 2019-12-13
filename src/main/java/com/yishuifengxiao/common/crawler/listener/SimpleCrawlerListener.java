package com.yishuifengxiao.common.crawler.listener;

import com.yishuifengxiao.common.crawler.domain.entity.Page;

/**
 * 默认的风铃虫处理事件监听器<br/>
 * 不输出任何信息
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class SimpleCrawlerListener implements CrawlerListener {

	@Override
	public void onDownError(Page page, Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDownSuccess(Page page) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExtractError(Page page, Exception e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExtractSuccess(Page page) {
		// TODO Auto-generated method stub

	}

}
