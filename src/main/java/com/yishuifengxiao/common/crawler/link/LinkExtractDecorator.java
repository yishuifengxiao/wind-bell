package com.yishuifengxiao.common.crawler.link;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
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
	private BaseLinkFilter linkFilter;
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

		//将提取出来的链接根据链接提取规则过滤
	   List<String>	urls=this.fliter(StringUtils.isNotBlank(page.getRedirectUrl())? page.getRedirectUrl():  page.getUrl(),new HashSet<>(page.getLinks()));
		page.setLinks(urls);
		//@formatter:on  
	}

	/**
	 * 从所有的超链接里提取出符合配置规则的链接
	 * 
	 * @param path 当前正在解析的网页内容的地址
	 * @param urls 从当前网页内容里提取出来的链接集合
	 * @return
	 */
	private List<String> fliter(final String path, Set<String> urls) {
		//@formatter:off 
		// 链接统一转换成网络地址形式
		Set<String> links = urls.parallelStream().filter(t -> null!=t).map(t -> linkFilter.handle(path, t.toLowerCase())).collect(Collectors.toSet());
		urls.clear();
		linkExtractors.parallelStream().map(t -> t.extract(new ArrayList<>(links))).forEach(urls::addAll);

		//@formatter:on  
		return urls.parallelStream().filter(t -> StringUtils.isNotBlank(t)).collect(Collectors.toList());
	}

	public LinkExtractDecorator(LinkExtract linkExtractProxy, LinkExtract linkExtract, BaseLinkFilter linkFilter,
			List<LinkExtractor> linkExtractors) {
		this.linkExtractProxy = linkExtractProxy;
		this.linkExtract = linkExtract;
		this.linkFilter = linkFilter;
		this.linkExtractors = linkExtractors;
	}

}
