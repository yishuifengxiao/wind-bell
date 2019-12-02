package com.yishuifengxiao.common.crawler.task;
/**
 * 任务管理器
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public interface TaskScheduler {
	/**
	 * 接收所有的超链接并存储起来
	 * 
	 * @param url 未过滤的超链接，可能会存在重复和已爬取等情况
	 */
	void push(String url);

	/**
	 * 从资源调度器里获取一个一个资源
	 * 
	 * @return
	 */
	String poll();
	/**
	 * 任务管理器的名字
	 * @return
	 */
	String getName();
}
