package com.yishuifengxiao.common.crawler.task;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
/**
 * 基于内存的任务管理器
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class InMemoryTaskManager implements TaskManager {

	private Queue<String> queue = new ConcurrentLinkedQueue<>();

	@Override
	public String poll() {

		return queue.poll();
	}

	@Override
	public void push(String url) {
		queue.add(url);

	}

	@Override
	public String getName() {

		return UUID.randomUUID().toString();
	}

}
