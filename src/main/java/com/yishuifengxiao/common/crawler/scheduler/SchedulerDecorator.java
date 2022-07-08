package com.yishuifengxiao.common.crawler.scheduler;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.scheduler.remover.DuplicateRemover;
import com.yishuifengxiao.common.crawler.scheduler.request.RequestCreater;
import com.yishuifengxiao.common.crawler.scheduler.request.SimpleRequestCreater;

import lombok.extern.slf4j.Slf4j;

/**
 * 资源调度器装饰者<br/>
 * 负责资源的调度管理工作<br/>
 * 功能如下：<br/>
 * 1. 接收所有未过滤的链接并存储起来<br/>
 * 2. 从资源池中获取一个链接<br/>
 * 3. 判断资源是否需要存储<br/>
 * 
 * @author yishui
 * @version 1.0.0
 */
@Slf4j
public class SchedulerDecorator implements Scheduler {
	/**
	 * 请求生成器
	 */
	private RequestCreater requestCreater = new SimpleRequestCreater();
	/**
	 * 请求缓存器，负责缓存所有需要抓取的网页的URL(包括历史记录)和已经爬取的url集合
	 */
	private RequestCache requestCache;
	/***
	 * 真实的资源调度器
	 */
	private Scheduler scheduler;
	/**
	 * 请求去重器
	 */
	private DuplicateRemover duplicateRemover;

	@Override
	public synchronized void push(final Task task, final Request request) {
		// 获取到站点规则
		SiteRule siteRule = task.getCrawlerRule().getSite();
		if (siteRule.getMaxDepth() > SiteConstant.DEFAULT_REQUEST_DEPTH
				&& siteRule.getMaxDepth() < request.getDepth()) {
			log.debug(
					"【id:{} , name:{} 】   The depth of the current request exceeds the specified maximum value. The current maximum value is {}, and the current request is {}",
					task.getUuid(), task.getName(), siteRule.getMaxDepth(), request);
			return;
		}

		try {
			if (this.duplicateRemover.noDuplicate(task, requestCache, request)) {

				// 补全请求信息
				requestCreater.create(siteRule, request);
				// 存储到待抓取集合中
				this.scheduler.push(task, request);

				log.debug("【id:{} , name:{} 】   Request task {} of instance has been submitted", task.getUuid(),
						task.getName(), request);

				this.duplicateRemover.doWhenNoDuplicate(task, requestCache, request);
			}
		} catch (Exception e) {
			log.debug(
					"【id:{} , name:{} 】   Scheduler encountered a problem while processing the load scheduling task of request {}. The cause of the problem is {}",
					task.getUuid(), task.getName(), request, e.getMessage());
		}

	}

	@Override
	public synchronized Request poll(final Task task) {

		return this.scheduler.poll(task);
	}

	@Override
	public synchronized void clear(final Task task) {
		this.requestCache.remove(task);
		this.scheduler.clear(task);

	}

	public SchedulerDecorator(RequestCache requestCache, Scheduler scheduler, DuplicateRemover duplicateRemover) {
		this.requestCache = requestCache;
		this.scheduler = scheduler;
		this.duplicateRemover = duplicateRemover;
	}

}
