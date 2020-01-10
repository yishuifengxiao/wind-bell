package com.yishuifengxiao.common.crawler.content;

import java.util.List;

import org.apache.http.HttpStatus;

import com.yishuifengxiao.common.crawler.content.impl.SimpleContentExtract;
import com.yishuifengxiao.common.crawler.content.matcher.ContentMatcher;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.extractor.content.ContentExtractor;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 内容解析器装饰器<br/>
 * 进行内容解析前的前置操作<br/>
 * 功能如下：<br/>
 * 1. 决定是否对该网页进行内容提取<br/>
 * 2. 调用真正的内容解析器进行内容解析
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
	protected String contentPageRules;

	/**
	 * 根据风铃虫内容解析规则创建的内容解析器
	 */
	protected ContentExtract simpleContentExtract;
	/**
	 * 用户自定义的内容解析器
	 */
	protected ContentExtract contentExtract;
	/**
	 * 内容匹配器
	 */
	protected ContentMatcher contentMatcher;

	@Override
	public void extract(final Page page) throws ServiceException {
		if (null == page) {
			return;
		}

		if (HttpStatus.SC_OK != page.getCode()) {
			page.setSkip(true);
			log.debug("Page {} has a response code of {} and will not extract data from it", page.getUrl(),
					page.getCode());
			return;
		}

		// 判断是否符合内容页规则
		boolean match = this.matchContentExtractRule(this.contentPageRules, page.getUrl());

		if (match && null != page.getRedirectUrl()) {
			// 判断重定向之后的页面是否满足内容也规则
			match = this.matchContentExtractRule(this.contentPageRules, page.getRedirectUrl());
		}

		if (match) {
			match = this.contentMatcher.match(page.getRawTxt());
		}

		log.debug("Whether the web page [{}] matches the content page parsing rule is {}", page.getUrl(), match);

		page.setSkip(!match);

		if (match) {
			// 开始真正的内容解析操作
			this.simpleContentExtract.extract(page);
			if (null != this.contentExtract) {
				this.contentExtract.extract(page);
			}
		}

	}

	/**
	 * 是否符合内容页规则
	 * 
	 * @param contentExtractRules 内容页规则
	 * @param url                 需要提取的目标页面的地址
	 * @return 符合内容页规则则返回为true，否则为false
	 */
	protected abstract boolean matchContentExtractRule(String contentExtractRules, String url);

	public BaseContentExtractDecorator(String contentPageRules, ContentMatcher contentMatcher,
			ContentExtract contentExtract, List<ContentExtractor> contentExtractors) {
		this.contentPageRules = contentPageRules;
		this.contentMatcher = contentMatcher;
		this.contentExtract = contentExtract;
		this.simpleContentExtract = new SimpleContentExtract(contentExtractors);
	}

}
