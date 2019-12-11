package com.yishuifengxiao.common.crawler.monitor;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;

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
     * @param task 任务
     * @param statu 变化之后的状态
     */
	void update(Task task,Statu statu);
}
