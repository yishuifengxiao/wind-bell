package com.yishuifengxiao.common.crawler.link;

import java.util.ArrayList;
import java.util.List;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.link.filter.LinkFilter;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 简单链接解析器<br/>
 * 功能如下：<br/>
 * 1 从网页的原始文本中统一转换成网络地址形式<br/>
 * 2 从转换后的地址里提取出所有符合要求的链接
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

	private LinkExtract linkExtract;
	/**
	 * 链接转换器,将提取的链接统一转成网络地址形式
	 */
	private LinkFilter linkFilter;
	/**
	 * 链接提取器
	 */
	private List<LinkExtractor> linkExtractors;

	@Override
	public void extract(Page page) throws ServiceException {
		//@formatter:off 
		// 调用实际处理类对信息进行处理
		this.linkExtractProxy.extract(page);

		//自定义解析数据
		if(this.linkExtract!=null) {
			this.linkExtract.extract(page);
		}
		//对提取出来的链接进行格式化，统一转化成网络地址形式
		List<String> urls=	this.linkFilter.filter(page.getUrl(),page.getLinks());
		//将提取出来的链接根据链接提取规则过滤
		urls=this.fliter(urls);
		page.setLinks(urls);
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

	public LinkExtractDecorator(LinkExtract linkExtractProxy, LinkExtract linkExtract, LinkFilter linkFilter,
			List<LinkExtractor> linkExtractors) {
		this.linkExtractProxy = linkExtractProxy;
		this.linkExtract = linkExtract;
		this.linkFilter = linkFilter;
		this.linkExtractors = linkExtractors;
	}

}
