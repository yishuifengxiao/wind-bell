package com.yishuifengxiao.common.crawler.task;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
/**
 * 基于redis的任务管理器
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class RedisTaskManager implements TaskManager {
	private String name;
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public String poll() {
		return (String) this.getOperation().pop();
	}

	@Override
	public void push(String url) {
		this.getOperation().add(url);

	}

	private BoundSetOperations<String, Object> getOperation() {
		return redisTemplate.boundSetOps(CrawlerConstant.WAIT_FOR_CRAWLER + this.name);
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RedisTaskManager(String name, RedisTemplate<String, Object> redisTemplate) {
		this.name = name;
		this.redisTemplate = redisTemplate;
	}

}
