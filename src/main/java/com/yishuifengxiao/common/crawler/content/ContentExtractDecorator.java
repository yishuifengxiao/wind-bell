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
public abstract class ContentExtractDecorator implements ContentExtract {

	/**
	 * 内容页url的规则，只有满足此规则的网页才会被解析
	 */
	protected String filterUrls;

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
		boolean match = matchContentRule(page.getUrl());
		log.debug("Does the page [{}] need to parse the matching result is {}", page.getUrl(), match);

		// 二次判断此网页是否被解析过
		if (match && this.scheduler.needExtract(page.getUrl())) {
			// 开始真正的内容解析操作
			contentExtract.extract(page);
			// 输出数据
			output(page);
		}

	}

	/**
	 * 是否符合内容页解析规则
	 * 
	 * @param url
	 * @return
	 */
	protected abstract boolean matchContentRule(String url);

	/**
	 * 输出数据
	 * 
	 * @param page
	 */
	private void output(Page page) {
		if (page != null && !page.isSkip()) {
			ResultData resultData = new ResultData();
			resultData.setData(page.getAllResultItem()).setUrl(page.getUrl());
			// 输出数据
			this.scheduler.recieve(resultData);
		}

	}

	public ContentExtractDecorator(String filterUrls, ContentExtract contentExtract, Scheduler scheduler) {
		this.filterUrls = filterUrls;
		this.contentExtract = contentExtract;
		this.scheduler = scheduler;
	}

}
