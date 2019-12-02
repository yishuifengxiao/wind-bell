package com.yishuifengxiao.common.crawler.link;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
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
	 * 链接解析器器适配器
	 */
	private LinkExtractApdater linkExtractApdater;
	/**
	 * 资源调度器
	 */
	private Scheduler scheduler;

	private LinkExtract linkExtract;

	@Override
	public void extract(Page page) {
		//@formatter:off 
		// 调用实际处理类对信息进行处理
		linkExtractApdater.extract(page);
		//解析数据
		if(this.linkExtract!=null) {
			this.linkExtract.extract(page);
		}
        
		//推送数据
		if(page.getLinks()!=null) {
			//获取到所有提取到的链接
			page.getLinks().parallelStream()
			.filter(t -> StringUtils.isNotBlank(t))
			.forEach(t->{
				//将请求推送到调度器中
				scheduler.push(t);
			});
		}
	
		//@formatter:on  
	}

	public LinkExtractDecorator(LinkExtractApdater linkExtractApdater, Scheduler scheduler, LinkExtract linkExtract) {

		this.linkExtractApdater = linkExtractApdater;
		this.scheduler = scheduler;
		this.linkExtract = linkExtract;
	}

	public LinkExtractDecorator() {

	}

	public LinkExtractApdater getLinkExtractApdater() {
		return linkExtractApdater;
	}

	public void setLinkExtractApdater(LinkExtractApdater linkExtractApdater) {
		this.linkExtractApdater = linkExtractApdater;
	}

	public Scheduler getScheduler() {
		return scheduler;
	}

	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

}
