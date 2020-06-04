package com.yishuifengxiao.common.crawler.cache;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 基于redis实现的请求记录器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class RedisRequestCache implements RequestCache {

	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public boolean lookAndCache(final Task task, Request request) {

		boolean extis = this.exist(task, request);
		this.save(task, request);
		return extis;
	}

	@Override
	public boolean exist(final Task task, Request request) {

		return this.getOps(task).isMember(request);
	}

	@Override
	public void remove(final Task task) {
		this.redisTemplate.delete(this.getKey(task));
	}

	@Override
	public long getCount(final Task task) {

		return this.getOps(task).size();
	}

	@Override
	public void save(final Task task, Request request) {

		this.getOps(task).add(request);
	}

	private BoundSetOperations<String, Object> getOps(final Task task) {

		return this.redisTemplate.boundSetOps(this.getKey(task));
	}

	private String getKey(final Task task) {
		return CrawlerConstant.REQUEST_HOSTORY + task.getName();
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public RedisRequestCache setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		return this;
	}

	public RedisRequestCache(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
