package com.yishuifengxiao.common.crawler.builder;

import com.yishuifengxiao.common.crawler.builder.impl.SimpleExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.ResultData;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 解析生产者<br/>
 * 功能如下：<br/>
 * 1. 根据提取规则生成链接提取器和内容提取器<br/>
 * 2. 将提取器组合成相对应的解析器<br/>
 * 3. 解析下载完成的网页<br/>
 * 4. 输出解析后的数据
 * 
 * @author yishui
 * @date 2019年11月25日
 * @version 1.0.0
 */
@Slf4j
public class ExtractProducer {
	/**
	 * 解析器生成器
	 */
	private ExtractBuilder extractBuilder = new SimpleExtractBuilder();
	/**
	 * 当前爬虫配置
	 */
	private CrawlerRule crawlerRule;
	/**
	 * 链接提取器，负责从内容中解析处理符合要求的链接
	 */
	protected LinkExtract linkExtract;
	/**
	 * 内容解析器，负责从内容中解析出需要提取的内容
	 */
	protected ContentExtract contentExtract;

	/**
	 * 资源调度器
	 */
	private Scheduler scheduler;

	/**
	 * 解析数据
	 * 
	 * @param page
	 */
	public void extract(final Page page) throws ServiceException {
		this.invoke();
		// 抽取链接
		this.linkExtract.extract(page);
		// 存储链接
		this.storeLink(page);
		// 初步判断内容是否需要被解析
		boolean needExtract = this.scheduler.needExtract(page.getUrl());

		log.debug("Does the web page [{}] need to be parsed as {}", page.getUrl(), needExtract);

		if (needExtract && null != page) {
			// 抽取内容
			this.contentExtract.extract(page);
			// 输出数据
			this.output(page);
		}

	}

	/**
	 * 将提取的链接推送到资源调度器
	 * 
	 * @param page
	 */
	private void storeLink(final Page page) {
		if (null != page && page.getLinks() != null) {
			// 将提取出来的推送到资源调度器
			page.getLinks().parallelStream().filter(t -> t != null).forEach(t -> {
				scheduler.push(t);
			});
		}
	}

	/**
	 * 处理解析器
	 */
	private void invoke() {
		// 生成链接解析器
		this.linkExtract = extractBuilder.createLinkExtract(this.crawlerRule.getLink(), this.linkExtract);
		// 生成内容解析器
		this.contentExtract = extractBuilder.createContentExtract(this.crawlerRule.getContent(), this.contentExtract);
	}

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

	public ExtractProducer(CrawlerRule crawlerRule, LinkExtract linkExtract, ContentExtract contentExtract,
			Scheduler scheduler) {
		this.crawlerRule = crawlerRule;
		this.linkExtract = linkExtract;
		this.contentExtract = contentExtract;
		this.scheduler = scheduler;
	}

}
