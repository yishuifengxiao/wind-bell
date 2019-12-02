package com.yishuifengxiao.common.crawler.monitor;

import com.yishuifengxiao.common.crawler.Task;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认实现的爬虫状态监视器
 * 
 * @author yishui
 * @date 2019年11月29日
 * @version 1.0.0
 */
@Slf4j
public class SimpleStatuObserver implements StatuObserver {

	@Override
	public void update(Task task) {
		log.debug("爬虫 {} 的状态发生变化了，变化之后的状态为 {}", task.getName(), task.getStatu());

	}

}
