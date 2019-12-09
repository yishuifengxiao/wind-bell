package com.yishuifengxiao.common.crawler.cache;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于redis实现的资源缓存器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class RedisRequestCache implements RequestCache {

	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public boolean lookAndCache(String cacheName, String value) {

		boolean extis = this.exist(cacheName, value);
		this.save(cacheName, value);
		return extis;
	}

	@Override
	public boolean exist(String cacheName, String value) {

		return this.getOps(cacheName).isMember(value);
	}

	@Override
	public void remove(String cacheName) {
		this.getOps(cacheName).expire(1L, TimeUnit.MILLISECONDS);

	}

	@Override
	public long getCount(String cacheName) {

		return this.getOps(cacheName).size();
	}

	@Override
	public void save(String cacheName, String value) {

		this.getOps(cacheName).add(value);
	}

	private BoundSetOperations<String, Object> getOps(String cacheName) {
		if (StringUtils.isNotEmpty(cacheName)) {
			throw new IllegalArgumentException("缓存集合的名字不能为空");
		}
		return this.redisTemplate.boundSetOps(cacheName);
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
