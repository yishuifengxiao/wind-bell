package com.yishuifengxiao.common.crawler.link.filter.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.crawler.utils.RegexFactory;

/**
 * 过滤掉非法的链接<br/>
 * 对不符合预期规则的链接不参与后续处理
 * 
 * @author yishui
 * @date 2019年12月26日
 * @version 1.0.0
 */
public class IllegalLinkFilter extends BaseLinkFilter {

	public IllegalLinkFilter(BaseLinkFilter next) {
		super(next);
	}

	@Override
	public String handle(String path, String url) {
		//空连接不予处理
		if (StringUtils.isBlank(url)) {
			return null;
		}
		//含有javascript字符的链接不予处理
		if (StringUtils.contains(url.toLowerCase(), RuleConstant.NOT_LINK)) {
			return null;
		}
		//双斜杠(//)开头的链接不予处理
		if (StringUtils.startsWith(url, RuleConstant.SHORT_ADDR_LINK)) {
			return null;
		}
		// 图片、css、js、字体文件链接不予处理
		if (RegexFactory.match(RuleConstant.ILLEGAL_LINKS_SUFFIX, url)) {
			return null;
		}
		return this.next != null ? this.next.handle(path, url) : null;
	}

}
