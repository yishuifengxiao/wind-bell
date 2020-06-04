package com.yishuifengxiao.common.crawler.cache;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 请求任务缓存器<br/>
 * 主要是用于判断此请求任务是否曾经存在过,用于协助完成请求去重<br/>
 * 功能如下：<br/>
 * 1 查询此请求任务是否存在于指定的的请求任务集合中<br/>
 * 2 将请求任务存储到指定名字的集合中<br/>
 * 3 清空指定的请求任务缓存集合<br/>
 * 4 查询指定请求任务缓存集合里的请求任务数量
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface RequestCache {
	/**
	 * 将请求任务存储到指定的集合名中
	 * 
	 * @param task 风铃虫任务
	 * @param request   请求任务
	 */
	void save(final Task task, Request request);

	/**
	 * 先查找请求任务是否在集合中存在,然后将该请求任务存储到此集合中
	 * 
	 * @param task 风铃虫任务
	 * @param request   请求任务
	 * @return 如果存在则返回为true，否则为fasle
	 */
	boolean lookAndCache(final Task task, Request request);

	/**
	 * 先查找请求任务是否在集合中存在
	 * 
	 * @param  task 风铃虫任务
	 * @param request   请求任务
	 * @return 如果存在则返回为true，否则为fasle
	 */
	boolean exist(final Task task, Request request);

	/**
	 * 移除指定的缓存集合
	 * 
	 * @param  task 风铃虫任务
	 */
	void remove(final Task task);

	/**
	 * 获取指定缓存集合的请求任务数量
	 * 
	 * @param  task 风铃虫任务
	 * @return 缓存集合里请求任务值的数量
	 */
	long getCount(final Task task);
}
