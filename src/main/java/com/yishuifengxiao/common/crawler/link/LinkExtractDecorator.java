package com.yishuifengxiao.common.crawler.link;

import java.util.ArrayList;
import java.util.List;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.link.converter.LinkConverter;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;

/**
 * 简单链接解析器<br/>
 * 功能如下：<br/>
 * 1 从网页的原始文本中解析出所有符合规则要求的链接<br/>
 * 2 将提取到的链接推送到资源调度器中<br/>
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class LinkExtractDecorator implements LinkExtract {

	/**
	 * 链接提取器代理器
	 */
	private LinkExtract linkExtractProxy;
	/**
	 * 资源调度器
	 */
	private Scheduler scheduler;

	private LinkExtract linkExtract;
	/**
	 * 链接转换器,将提取的链接统一转成网络地址形式
	 */
	private LinkConverter linkConverter;
    /**
     * 链接提取器
     */
	private List<LinkExtractor> linkExtractors;

	@Override
	public void extract(Page page) {
		//@formatter:off 
		// 调用实际处理类对信息进行处理
		linkExtractProxy.extract(page);

		//解析数据
		if(this.linkExtract!=null) {
			this.linkExtract.extract(page);
		}
		//对提取出来的链接进行过滤
		List<String> urls=	this.linkConverter.format(page.getUrl(),page.getLinks());
		urls=this.fliter(urls);
		//推送数据
		urls.parallelStream().forEach(t->{
			//将请求推送到调度器中
			scheduler.push(t);
		});
		//@formatter:on  
	}

	/**
	 * 从所有的超链接里提取出符合配置规则的链接
	 *
	 * @param urls
	 * @return
	 */
	private List<String> fliter(List<String> urls) {
		//@formatter:off 
		final List<String> list = new ArrayList<>();
		
			for(LinkExtractor linkExtractor:linkExtractors) {
			List<String> links=linkExtractor.extract(urls);
				if(links!=null) {
					list.addAll(links);
				}
			}
	
		
		//@formatter:on  
		return list;
	}

	public LinkExtractDecorator(LinkExtract linkExtractProxy, Scheduler scheduler, LinkExtract linkExtract,
			LinkConverter linkConverter, List<LinkExtractor> linkExtractors) {
		this.linkExtractProxy = linkExtractProxy;
		this.scheduler = scheduler;
		this.linkExtract = linkExtract;
		this.linkConverter = linkConverter;
		this.linkExtractors = linkExtractors;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
