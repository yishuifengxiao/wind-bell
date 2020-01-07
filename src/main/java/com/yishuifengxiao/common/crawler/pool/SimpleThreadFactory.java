package com.yishuifengxiao.common.crawler.pool;

import java.util.concurrent.ThreadFactory;

import org.apache.commons.lang3.RandomUtils;

/**
 * 线程工厂
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class SimpleThreadFactory implements ThreadFactory {

	private String name;

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(new StringBuffer(this.name).append(":").append(RandomUtils.nextInt()).toString());
		return thread;
	}

	public SimpleThreadFactory(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
