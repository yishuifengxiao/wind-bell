package com.yishuifengxiao.common.crawler.content.decorator;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.content.BaseContentExtractDecorator;
import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.macther.PathMatcher;
import com.yishuifengxiao.common.crawler.macther.impl.SimplePathMatcher;

/**
 * 默认的内容提取器装饰器<br/>
 * 判断是否符合解析规则
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleContentExtractDecorator extends BaseContentExtractDecorator {
	/**
	 * 路径匹配工具
	 */
	private PathMatcher pathMatcher = new SimplePathMatcher();

	public SimpleContentExtractDecorator(String filterUrls, ContentExtract contentExtract) {
		super(filterUrls, contentExtract);
	}

	/**
	 * 判断是否符合提取页规则
	 *
	 * @param url 需要提取的网页的url的地址
	 * @return 需要提取则返回为true，否则为false
	 */
	@Override
	public boolean matchContentExtractRule(String contentExtractRules, String url) {
		if (StringUtils.isBlank(contentExtractRules)) {
			return true;
		}
		// 判断表达式是否符合选取所有
		if (this.pathMatcher.match(contentExtractRules, url)) {
			return true;
		}

		String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(contentExtractRules, ",");
		for (String str : urls) {
			// 判断表达式是否符合选取所有
			if (this.pathMatcher.match(str, url)) {
				return true;
			}

		}
		return false;
	}

}
