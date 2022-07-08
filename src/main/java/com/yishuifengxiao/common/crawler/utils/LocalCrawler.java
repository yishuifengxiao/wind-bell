package com.yishuifengxiao.common.crawler.utils;

import com.yishuifengxiao.common.crawler.Task;

/**
 * 风铃虫任务信息线程缓存类
 * 
 * @author yishui
 * @version 1.0.0
 */
public final class LocalCrawler {

	private volatile static ThreadLocal<Task> LOCAL = new ThreadLocal<>();

	/**
	 * 放置一个风铃虫任务信息
	 * 
	 * @param crawler 风铃虫任务
	 */
	public synchronized static void put(Task crawler) {
		LOCAL.set(crawler);
	}

	/**
	 * 获取风铃虫任务信息
	 * 
	 * @return
	 */
	public synchronized static Task get() {
		return LOCAL.get();
	}

	/**
	 * 清理缓存
	 */
	public synchronized static void clear() {
		LOCAL.remove();
	}

}
