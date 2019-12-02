package com.yishuifengxiao.common.crawler.cache;

import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;

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
	public synchronized boolean extisHistory(String taskName, String url) {
		return this.history(taskName).isMember(url);
	}

	@Override
	public synchronized boolean lookHistoryAndCache(String taskName, String url) {
		boolean extis = this.extisHistory(taskName, url);
		this.history(taskName).add(url);
		return extis;
	}

	/**
	 * 获取到历史任务操作队列
	 * 
	 * @param name
	 */
	private BoundSetOperations<String, Object> history(String taskName) {
		return this.redisTemplate.boundSetOps(CrawlerConstant.REQUEST_HOSTORY + taskName);
	}

	@Override
	public synchronized boolean extisExtracted(String taskName, String url) {
		return this.extracted(taskName).isMember(url);
	}

	@Override
	public synchronized boolean lookExtractedAndCache(String taskName, String url) {
		boolean extis = this.extisExtracted(taskName, url);
		this.extracted(taskName).add(url);
		return extis;
	}

	/**
	 * 获取到已完成任务操作队列
	 * 
	 * @param name
	 */
	private BoundSetOperations<String, Object> extracted(String taskName) {
		return this.redisTemplate.boundSetOps(CrawlerConstant.HAS_CRAWLERED + taskName);
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public RedisRequestCache(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

}
