package com.yishuifengxiao.common.crawler.content;

import org.apache.http.HttpStatus;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.ResultData;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;

import lombok.extern.slf4j.Slf4j;

/**
 * 内容解析器装饰器<br/>
 * 进行内容解析前的前置操作<br/>
 * 功能如下：<br/>
 * 1. 决定是否对该网页进行内容提取<br/>
 * 2. 提取前的数据过滤操作<br/>
 * 3. 输出解析后的数据
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
@Slf4j
public abstract class BaseContentExtractDecorator implements ContentExtract {

	/**
	 * 内容页url的规则，只有满足此规则的网页才会被解析
	 */
	protected String contentExtractRules;

	/**
	 * 爬虫处理器，负责解析下载后的网页内容
	 */
	protected ContentExtract contentExtract;
	/**
	 * 资源 调度器，负责资源的调度工作
	 */
	protected Scheduler scheduler;

	@Override
	public void extract(Page page) {

		if (page == null || page.getCode() != HttpStatus.SC_OK) {
			// 该网页不需要重新解析
			scheduler.recieve(new ResultData(page.getUrl(), false));
			return;
		}
		// 判断是否符合解析规则
		boolean match = this.matchContentExtractRule(this.contentExtractRules, page.getUrl());
		// 是否需要解析
		boolean needExtract = this.scheduler.needExtract(page.getUrl());
		log.debug(
				"Whether the page [{}] matches the content page parsing result is {}, whether it needs to parse the result is {}",
				page.getUrl(), match, needExtract);

		// 二次判断此网页是否被解析过
		if (match && needExtract) {
			// 开始真正的内容解析操作
			this.contentExtract.extract(page);
		}

	}

	/**
	 * 是否符合内容页解析规则
	 * 
	 * @param contentExtractRules 内容页规则
	 * @param url                 需要提取的目标页面的地址
	 * @return
	 */
	protected abstract boolean matchContentExtractRule(String contentExtractRules, String url);

	public BaseContentExtractDecorator(String contentExtractRules, ContentExtract contentExtract, Scheduler scheduler) {
		this.contentExtractRules = contentExtractRules;
		this.contentExtract = contentExtract;
		this.scheduler = scheduler;
	}

}
