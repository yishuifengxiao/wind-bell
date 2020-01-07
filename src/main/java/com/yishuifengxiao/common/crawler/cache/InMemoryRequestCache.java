package com.yishuifengxiao.common.crawler.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * 基于内存实现的资源缓存器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class InMemoryRequestCache implements RequestCache {

	private final Set<String> cacheSet = new HashSet<>();

	@Override
	public synchronized void save(String cacheName, String value) {
		if (null != value) {
			cacheSet.add(value);
		}

	}

	@Override
	public boolean lookAndCache(String cacheName, String value) {

		boolean extis = this.exist(cacheName, value);
		this.save(cacheName, value);
		return extis;
	}

	@Override
	public boolean exist(String cacheName, String value) {
		return this.cacheSet.contains(value);
	}

	@Override
	public synchronized void remove(String cacheName) {

		this.cacheSet.clear();
	}

	@Override
	public long getCount(String cacheName) {
		return this.cacheSet.size();
	}

}
