package com.yishuifengxiao.common.crawler.listener;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.entity.Page;

/**
 * 默认的风铃虫处理事件监听器<br/>
 * 不输出任何信息
 * 
 * @author yishui
 * @version 1.0.0
 */
public class SimpleCrawlerListener implements CrawlerListener {

	@Override
	public void onDownError(final Task task, Page page, Exception e) {

	}

	@Override
	public void onDownSuccess(final Task task, final Page page) {

	}

	@Override
	public void onExtractError(final Task task, Page page, Exception e) {

	}

	@Override
	public void onExtractSuccess(final Task task, Page page) {

	}

	@Override
	public void exitOnBlock(Task task) {

	}

	@Override
	public void exitOnFinish(Task task) {

	}

	@Override
	public void onNullRquest(Task task) {

	}

}
