package com.yishuifengxiao.common.crawler.link;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.extractor.content.strategy.StrategyFactory;
import com.yishuifengxiao.common.crawler.extractor.links.LinkExtractor;

import lombok.extern.slf4j.Slf4j;

/**
 * 链接解析器器适配器 <br/>
 * 功能如下：<br/>
 * 1. 主要负责从原始网页文本中解析出所有符合要求的链接<br/>
 * 2. 从链接中提取出所有符合规则要求的链接 <br/>
 * 3. 将链接转换成网络地址形式
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
@Slf4j
public class LinkExtractApdater implements LinkExtract {

	private StrategyChain strategyChain;

	private List<LinkExtractor> linkExtractors;

	@Override
	public synchronized void extract(Page page) {
		// 提取出所有符合要求的超链接
		List<String> links = this.extract(page.getUrl(), page.getRawTxt());
		//必须在当前域名内
		links=links.parallelStream().filter(t->StringUtils.contains(t, page.getDomain())).collect(Collectors.toList());
		
		// 将超链接放入目标里备用
		page.setLinks(links);
	}

	/**
	 * 从原始文本中提取出所有的链接
	 *
	 * @param rawtxt
	 * @return
	 */
	private List<String> extract(String currentUrl, String rawtxt) {
		// 先提取出所有的链接
		List<String> urls = extractAllLinks(rawtxt);
		// 地址转换
		urls = convert(currentUrl, urls);
		// 初步提取出符合要求的链接
		urls = extract(urls);
		return urls;
	}

	/**
	 * 提取出所有的超链接
	 *
	 * @param rawtxt 原始文本
	 * @return
	 */
	private List<String> extractAllLinks(String rawtxt) {
		//@formatter:off 
		try {
			String extract = StrategyFactory.get(Rule.XPATH).extract(rawtxt, RuleConstant.XPATH_ALL_LINK, null);
			
			if (StringUtils.isNotBlank(extract)) {
				
				String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(extract,
						CrawlerConstant.SEPARATOR);
				
				if (tokens != null && tokens.length > 0) {
					return Arrays.asList(tokens);
				}
			}
		} catch (Exception e) {
			log.info("提取所有的超链接失败，失败的原因为 {}", e.getMessage());
		}
		//@formatter:on  
		return new ArrayList<>();
	}

	/**
	 * 从所有的超链接里提取出符合配置规则的链接
	 *
	 * @param urls
	 * @return
	 */
	private List<String> extract(List<String> urls) {
		//@formatter:off 
		final List<String> list = new ArrayList<>();
		
		Set<List<String>> collect = linkExtractors.parallelStream()
				.map(t -> t.extract(urls))
				.filter(t -> t != null && t.size() > 0)
				.collect(Collectors.toSet());

		for (List<String> link : collect) {
			list.addAll(link);
		}
		//@formatter:on  
		return list;
	}

	/**
	 * 对链接进行转换，将所有的链接都转换成网络地址
	 *
	 * @param currentUrl 当前正在爬取的网页的地址
	 * @param urls       未转换完成的链接
	 * @return 转换后的链接
	 */
	private List<String> convert(String currentUrl, List<String> urls) {
		//@formatter:off 
		return urls.parallelStream()
				.filter(t -> StringUtils.isNotBlank(t))
				.map(t -> strategyChain.handle(currentUrl, t))
				.collect(Collectors.toList());
		//@formatter:on  
	}

	public LinkExtractApdater(StrategyChain strategyChain, List<LinkExtractor> linkExtractors) {
		this.strategyChain = strategyChain;
		this.linkExtractors = linkExtractors;
	}

	public LinkExtractApdater() {

	}

}
