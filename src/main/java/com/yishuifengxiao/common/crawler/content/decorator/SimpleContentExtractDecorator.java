package com.yishuifengxiao.common.crawler.content.decorator;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.ContentExtractDecorator;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.scheduler.Scheduler;
import com.yishuifengxiao.common.crawler.utils.RegexFactory;

/**
 * 默认的内容提取器装饰器<br/>
 * 判断是否符合解析规则
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class SimpleContentExtractDecorator extends ContentExtractDecorator {

	public SimpleContentExtractDecorator(String filterUrls, ContentExtract contentExtract, Scheduler scheduler) {
		super(filterUrls, contentExtract, scheduler);
	}

	/**
	 * 判断是否符合提取页规则
	 *
	 * @param url 需要提取的网页的url的地址
	 * @return 需要提取则返回为true，否则为false
	 */
	@Override
	public boolean matchContentRule(String url) {
		if (StringUtils.isBlank(filterUrls)
				|| StringUtils.equalsAnyIgnoreCase(url, RuleConstant.REGEX_MATCH_ALL, RuleConstant.ANT_MATCH_ALL)) {
			return true;
		}
		String[] urls = StringUtils.splitByWholeSeparatorPreserveAllTokens(filterUrls, ",");
		for (String str : urls) {
			// 判断当前网页是否符合内容提取页的提取提取规则
			if (match(str, url)) {
				return true;

			}
		}
		return false;
	}

	/**
	 * 根据正则判断是否匹配
	 *
	 * @param pattern 正则
	 * @param url     url
	 * @return
	 */
	private boolean match(String pattern, String url) {
		if (StringUtils.equalsAnyIgnoreCase(pattern, RuleConstant.ANT_MATCH_ALL, RuleConstant.REGEX_MATCH_ALL)) {
			return true;
		}
		return RegexFactory.match(pattern, url);
	}

}
