package com.yishuifengxiao.common.crawler.builder;

import java.util.List;

import com.yishuifengxiao.common.crawler.builder.impl.SimpleExtractBuilder;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.decorator.SimpleContentExtractDecorator;
import com.yishuifengxiao.common.crawler.content.impl.SimpleContentExtract;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.link.LinkExtractProxy;
import com.yishuifengxiao.common.crawler.link.LinkExtractDecorator;
import com.yishuifengxiao.common.crawler.link.LinkConverterChain;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;

/**
 * 解析器生成工具<br/>
 * 功能如下：<br/>
 * 1. 根据提取规则生成链接提取器和内容提取器<br/>
 * 2. 将提取器组合成相对应的解析器 3. 解析下载完成的网页
 * 
 * @author yishui
 * @date 2019年11月25日
 * @version 1.0.0
 */
public class ExtractProducer {
	/**
	 * 提取器构建者
	 */
	private ExtractorBuilder extractorBuilder = new SimpleExtractBuilder();

	/**
	 * 链接转换器
	 */
	private LinkConverterChain strategyChain = new LinkConverterChain();
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
	public void extract(final Page page) {
		LinkExtract linkExtract = this.createLinkExtract();
		ContentExtract contentExtract = this.createContentExtract();
		linkExtract.extract(page);
		contentExtract.extract(page);
	}

	/**
	 * 构建出内容解析器
	 * 
	 * @return
	 */
	private ContentExtract createContentExtract() {
		// 获取到所有的内容抽取器
		List<ContentExtractor> contentExtractors = extractorBuilder
				.buildAllContentExtractor(this.crawlerRule.getContent());
		// 构建一个内容解析器
		ContentExtract contentExtract = this.contentExtract != null ? this.contentExtract
				: new SimpleContentExtract(contentExtractors);
		// 构建一个内容解析装饰器
		return new SimpleContentExtractDecorator(this.crawlerRule.getContent().getExtractUrl(), contentExtract,
				this.scheduler);
	}

	/**
	 * 构建链接解析器
	 * 
	 * @return
	 */
	private LinkExtract createLinkExtract() {

		// 获取所有的链接提取器
		List<LinkExtractor> linkExtractors = extractorBuilder.buildLinkExtractor(this.crawlerRule.getLink());
		// 构建一个链接解析适配器
		LinkExtractProxy linkExtractProxy = new LinkExtractProxy(strategyChain);
		// 生成链接解析器
		return new LinkExtractDecorator(this.crawlerRule.getDomain(), linkExtractProxy, this.scheduler,
				this.linkExtract, linkExtractors);
	}

	public ExtractProducer(CrawlerRule crawlerRule, LinkExtract linkExtract, ContentExtract contentExtract,
			Scheduler scheduler) {
		this.crawlerRule = crawlerRule;
		this.linkExtract = linkExtract;
		this.contentExtract = contentExtract;
		this.scheduler = scheduler;
	}

}
