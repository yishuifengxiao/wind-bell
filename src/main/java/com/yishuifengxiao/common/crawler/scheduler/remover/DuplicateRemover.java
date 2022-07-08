package com.yishuifengxiao.common.crawler.scheduler.remover;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 请求去重器<br/>
 * 用于在调度器存储任务之前进行重复任务判断，判断该任务是否已经存在过
 * 
 * @author qingteng
 * @version 1.0.0
 */
public interface DuplicateRemover {
	/**
	 * 判断当前请求是否重复
	 * 
	 * @param task         当前任务信息
	 * @param requestCache 请求任务缓存器
	 * @param request      当前需要判断的请求
	 * @return true表示当前请求没有存在过，需要调度器调度，以便后期进行该请求,false表示该请求已经进行过，调度器会抛弃该请求
	 */
	boolean noDuplicate(final Task task, final RequestCache requestCache, final Request request);

	/**
	 * 当前请求没有重复时需要进行的操作，一般来说，只需将该请求存入请求任务缓存器即可
	 * 
	 * @param task         当前任务信息
	 * @param requestCache 请求任务缓存器
	 * @param request      当前需要判断的请求
	 */
	void doWhenNoDuplicate(final Task task, final RequestCache requestCache, final Request request);
}
