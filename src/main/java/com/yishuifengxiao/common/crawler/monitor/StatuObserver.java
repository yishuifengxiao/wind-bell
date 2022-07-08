package com.yishuifengxiao.common.crawler.monitor;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;

/**
 * 风铃虫状态观察者<br/>
 * 监控风铃虫状态的变化
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface StatuObserver {
	/**
	 * 任务的状态发生了变化
	 * 
	 * @param task  当前任务，也就是当前运行的风铃虫实例
	 * @param statu 变化之后的状态
	 */
	void update(final Task task, final Statu statu);
}
