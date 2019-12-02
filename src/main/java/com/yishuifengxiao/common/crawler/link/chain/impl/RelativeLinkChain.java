package com.yishuifengxiao.common.crawler.link.chain.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.chain.AbstractLinkChain;

/**
 * 相对地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class RelativeLinkChain extends AbstractLinkChain {

	@Override
	public String handle(String currentUrl, String url) {
		if (!StringUtils.startsWithAny(url, RuleConstant.ABSOLUTE_ADDR_LINK, RuleConstant.NETWORK_ADDR_LINK)) {
			StringBuffer sb = new StringBuffer(currentUrl);
			if (!StringUtils.endsWith(currentUrl, CrawlerConstant.LEFT_SALASH)) {
				sb.append(CrawlerConstant.LEFT_SALASH);
			}
			return sb.append(url).toString();
		}
		return chain == null ? null : chain.handle(currentUrl, url);
	}

	public RelativeLinkChain(AbstractLinkChain chain, String domain) {
		super(chain, domain);
	}
}
