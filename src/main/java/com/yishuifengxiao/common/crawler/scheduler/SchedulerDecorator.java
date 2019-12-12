package com.yishuifengxiao.common.crawler.scheduler;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;

/**
 * 资源调度器装饰者<br/>
 * 负责资源的调度管理工作<br/>
 * 功能如下：<br/>
 * 1. 接收所有未过滤的链接并存储起来<br/>
 * 2. 从资源池中获取一个链接<br/>
 * 3. 判断资源是否需要存储<br/>
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SchedulerDecorator implements Scheduler {
	/**
	 * 请求缓存器，负责缓存所有需要抓取的网页的URL(包括历史记录)和已经爬取的url集合
	 */
	private RequestCache requestCache;
	/***
	 * 真实的资源调度器
	 */
	private Scheduler scheduler;

	@Override
	public void push(String... urls) {

		if (urls != null) {
			Arrays.asList(urls).parallelStream().filter(t -> t != null).filter(t -> this.needStore(t)).forEach(t -> {

				// 存储到待抓取集合中
				this.scheduler.push(t);
				// 存储在历史记录集之中
				this.requestCache.save(CrawlerConstant.REQUEST_HOSTORY + this.getName(), t);

			});

		}

	}

	/**
	 * 判断链接是否符合提取规则
	 * 
	 * @param url
	 * @return
	 */
	private boolean needStore(String url) {
		if (StringUtils.isBlank(url)) {
			return false;
		}
		// 在历史链接记录集不存在时才处理
		return !requestCache.exist(CrawlerConstant.REQUEST_HOSTORY + this.getName(), url);
	}

	@Override
	public String poll() {

		return this.scheduler.poll();
	}

	@Override
	public String getName() {
		return this.scheduler.getName();
	}

	@Override
	public void clear() {
		this.scheduler.clear();

	}

	public SchedulerDecorator(RequestCache requestCache, Scheduler scheduler) {
		this.requestCache = requestCache;
		this.scheduler = scheduler;
	}

}
