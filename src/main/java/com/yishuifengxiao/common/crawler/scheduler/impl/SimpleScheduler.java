package com.yishuifengxiao.common.crawler.scheduler.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;

/**
 * 简单资源调度器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleScheduler implements Scheduler {
	private Queue<Request> queue = new ConcurrentLinkedQueue<>();

	@Override
	public synchronized void push(final Task task,Request request) {

		this.queue.add(request);

	}

	@Override
	public synchronized void clear(final Task task) {
		this.queue.clear();

	}

	@Override
	public synchronized Request poll(final Task task) {
		return queue.poll();
	}

}
