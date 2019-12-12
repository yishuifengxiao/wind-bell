package com.yishuifengxiao.common.crawler.link.filter.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;

/**
 * 相对地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class RelativeLinkFilter extends BaseLinkFilter {

	public RelativeLinkFilter(BaseLinkFilter next) {
		super(next);

	}

	@Override
	public String handle(String path, String url) {

		if (!StringUtils.startsWithAny(url, RuleConstant.ABSOLUTE_ADDR_LINK, RuleConstant.NETWORK_ADDR_LINK)) {

			// 不是网络地址和绝对地址
			StringBuffer sb = new StringBuffer(path);
			if (!StringUtils.endsWith(path, CrawlerConstant.LEFT_SALASH)) {
				sb.append(CrawlerConstant.LEFT_SALASH);
			}
			return sb.append(url).toString();

		}
		return this.next != null ? this.next.handle(path, url) : null;
	}

}
