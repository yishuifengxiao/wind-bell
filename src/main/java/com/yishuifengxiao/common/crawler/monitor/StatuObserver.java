package com.yishuifengxiao.common.crawler.monitor;

import com.yishuifengxiao.common.crawler.Task;

/**
 * 爬虫状态观察者<br/>
 * 监控爬虫状态的变化
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public interface StatuObserver {
    /**
     * 任务的状态发生了变化
     * @param task
     */
	void update(Task task);
}
