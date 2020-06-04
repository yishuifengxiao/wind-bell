package com.yishuifengxiao.common.crawler.scheduler;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 资源调度器<br/>
 * 负责资源的调度管理工作<br/>
 * 功能如下：<br/>
 * 1. 接收所有未过滤的请求任并储存起来<br/>
 * 2. 从资源池中获取一个链接<br/>
 * 3. 清空资源调度器中所有储存的资源信息<br/>
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface Scheduler {
	/**
	 * 接收所有的请求任并存储起来
	 * 
	 * @param task     当前任务的任务信息
	 * @param requests 未过滤的请求任务，可能会存在重复和已爬取等情况
	 */
	void push(final Task task, final Request request);

	/**
	 * 从资源调度器里获取一个请求任务
	 * 
	 * @param task 当前任务的任务信息
	 * @return 需要执行的请求任务
	 */
	Request poll(final Task task);

	/**
	 * 
	 * 清空任务
	 * 
	 * @param task 当前任务的任务信息
	 */
	void clear(final Task task);

}
