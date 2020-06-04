package com.yishuifengxiao.common.crawler.scheduler.impl;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;

/**
 * 基于redis的资源调度器
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class RedisScheduler implements Scheduler {

	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void push(final Task task, Request request) {
		this.getOperation(task).add(request);

	}

	@Override
	public Request poll(final Task task) {

		return (Request) this.getOperation(task).pop();
	}

	@Override
	public void clear(final Task task) {

		this.redisTemplate.delete(this.getKey(task));

	}

	private BoundSetOperations<String, Object> getOperation(final Task task) {
		return redisTemplate.boundSetOps(this.getKey(task));
	}

	private String getKey(final Task task) {
		return CrawlerConstant.WAIT_DOWN + task.getName();
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisScheduler(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
