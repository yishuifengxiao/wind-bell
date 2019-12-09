package com.yishuifengxiao.common.crawler.link.chain.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.chain.BaseLinkChain;

/**
 * 相对地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class RelativeLinkChain extends BaseLinkChain {

	public RelativeLinkChain(BaseLinkChain next) {
		super(next);

	}

	@Override
	public String handle(String currentPath, String url) {

		if (!StringUtils.startsWithAny(url, RuleConstant.ABSOLUTE_ADDR_LINK, RuleConstant.NETWORK_ADDR_LINK)) {

			// 不是网络地址和绝对地址
			StringBuffer sb = new StringBuffer(currentPath);
			if (!StringUtils.endsWith(currentPath, CrawlerConstant.LEFT_SALASH)) {
				sb.append(CrawlerConstant.LEFT_SALASH);
			}
			return sb.append(url).toString();

		}
		return this.next != null ? this.next.handle(currentPath, url) : null;
	}

}
