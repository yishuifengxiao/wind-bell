package com.yishuifengxiao.common.crawler.scheduler;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.entity.ResultData;
import com.yishuifengxiao.common.crawler.pipeline.Pipeline;
import com.yishuifengxiao.common.crawler.task.TaskManager;

/**
 * 简单资源调度器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleScheduler implements Scheduler {
	/**
	 * 请求缓存器，负责缓存所有需要抓取的网页的URL(包括历史记录)和已经爬取的url集合
	 */
	protected RequestCache requestCache;
	/**
	 * 信息输出器，输出信息
	 */
	protected Pipeline pipeline;
	/**
	 * 任务管理器
	 */
	protected TaskManager taskManager;

	@Override
	public void push(String... urls) {
		if (urls != null) {
			Arrays.asList(urls).parallelStream().filter(t -> t != null).forEach(t -> {
				if (this.needStore(t)) {
					//存储到待抓取集合中
					this.taskManager.push(t);
					// 存储在历史记录集之中
					this.requestCache.save(CrawlerConstant.REQUEST_HOSTORY + this.taskManager.getName(), t);

				}

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
		return !requestCache.exist(CrawlerConstant.REQUEST_HOSTORY + this.taskManager.getName(), url);
	}

	/**
	 * 接收解析出来的数据并输出
	 */
	@Override
	public void recieve(ResultData resultData) {
		// 保存抓取记录
		this.requestCache.exist(CrawlerConstant.HAS_CRAWLERED + this.taskManager.getName(), resultData.getUrl());
		// 输出数据
		this.pipeline.recieve(resultData);
	}

	/**
	 * 判断此资源是否需要解析
	 */
	@Override
	public boolean needExtract(String url) {
		// 查询该资源是否在已下载的资源中，同时将其存储在已下载的资源中
		return !this.requestCache.lookAndCache(CrawlerConstant.HAS_DOWN + this.taskManager.getName(), url);
	}

	public SimpleScheduler(RequestCache requestCache, Pipeline pipeline, TaskManager taskManager) {

		this.requestCache = requestCache;
		this.pipeline = pipeline;
		this.taskManager = taskManager;
	}

	@Override
	public String poll() {

		return this.taskManager.poll();
	}

}
