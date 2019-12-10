package com.yishuifengxiao.common.crawler.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

/**
 * 基于内存实现的资源缓存器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public class InMemoryRequestCache implements RequestCache {

	private final Map<String, Set<String>> CACHE_MAP = new HashMap<>();

	@Override
	public synchronized void save(String cacheName, String value) {

		Set<String> set = this.getOps(cacheName);
		set.add(value);
		CACHE_MAP.put(cacheName, set);
	}

	private Set<String> getOps(String cacheName) {
		if (StringUtils.isEmpty(cacheName)) {
			throw new IllegalArgumentException("缓存集合的名字不能为空");
		}
		Set<String> set = CACHE_MAP.get(cacheName);
		if (set == null) {
			set = new HashSet<>();
		}
		return set;
	}

	@Override
	public boolean lookAndCache(String cacheName, String value) {

		boolean extis = this.exist(cacheName, value);
		this.save(cacheName, value);
		return extis;
	}

	@Override
	public boolean exist(String cacheName, String value) {
		return this.getOps(cacheName).contains(value);
	}

	@Override
	public synchronized void remove(String cacheName) {

		CACHE_MAP.put(cacheName, new HashSet<>());
	}

	@Override
	public long getCount(String cacheName) {
		return this.getOps(cacheName).size();
	}

}
