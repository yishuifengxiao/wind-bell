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

	private Set<String> hostory = new HashSet<>();

	private Set<String> extracted = new HashSet<>();

	@Override
	public synchronized boolean extisHistory(String taskName, String url) {
		return hostory.contains(url);
	}

	@Override
	public synchronized boolean lookHistoryAndCache(String taskName, String url) {
		boolean extis = hostory.contains(url);
		hostory.add(url);
		return extis;
	}

	@Override
	public synchronized boolean extisExtracted(String taskName, String url) {
		return extracted.contains(url);
	}

	@Override
	public synchronized boolean lookExtractedAndCache(String taskName, String url) {
		boolean extis = extracted.contains(url);
		extracted.add(url);
		return extis;
	}

}
