package com.yishuifengxiao.common.crawler.scheduler.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;

/**
 * 基于redis的资源调度器
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class RedisScheduler implements Scheduler {

	private String name;

	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public void push(String... urls) {
		if (null != urls) {
			for (String url : urls) {
				this.getOperation().add(url);
			}
		}

	}

	@Override
	public String poll() {

		return (String) this.getOperation().pop();
	}

	
	@Override
	public void clear() {
		this.getOperation().expire(1L, TimeUnit.MILLISECONDS);
		
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private BoundSetOperations<String, Object> getOperation() {
		return redisTemplate.boundSetOps(CrawlerConstant.WAIT_DOWN + this.name);
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisScheduler(String name, RedisTemplate<String, Object> redisTemplate) {
		this.name = name;
		this.redisTemplate = redisTemplate;
	}



}
