package com.yishuifengxiao.common.crawler.cache;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 基于内存实现的请求任务缓存器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class InMemoryRequestCache implements RequestCache {

	private final Map<String, Set<Request>> cacheMap = new WeakHashMap<>();

	@Override
	public synchronized void save(final Task task, Request request) {
		String cacheName=this.getCacheName(task);
		Set<Request> set = cacheMap.get(cacheName);

		if (null == set) {
			set = new HashSet<>();
		}
		set.add(request);
		cacheMap.put(cacheName, set);
	}

	@Override
	public synchronized boolean lookAndCache(final Task task,  Request request) {
		boolean extis = this.exist(task, request);
		this.save(task, request);
		return extis;
	}

	@Override
	public synchronized boolean exist(final Task task, Request request) {
		String cacheName=this.getCacheName(task);
		Set<Request> set = cacheMap.get(cacheName);
		if (null == set) {
			return false;
		}
		return set.contains(request);
	}

	@Override
	public synchronized void remove(final Task task) {
		String cacheName=this.getCacheName(task);
		this.cacheMap.put(cacheName, new HashSet<>());
	}

	@Override
	public long getCount(final Task task) {
		String cacheName=this.getCacheName(task);
		Set<Request> set = cacheMap.get(cacheName);
		if (null == set) {
			return 0;
		}
		return set.size();
	}
	
	
	private String getCacheName(final Task task) {
		String cacheName = CrawlerConstant.REQUEST_HOSTORY + task.getName();
		return cacheName;
	}

}
