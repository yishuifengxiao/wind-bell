package com.yishuifengxiao.common.crawler.link.chain.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.chain.AbstractLinkChain;

/**
 * 网络地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class HttpLinkChain extends AbstractLinkChain {

	@Override
	public String handle(String currentUrl, String url) {
		if (StringUtils.startsWith(url, RuleConstant.NETWORK_ADDR_LINK)) {
			return url;
		}
		return chain == null ? null : chain.handle(currentUrl, url);
	}

	public HttpLinkChain(AbstractLinkChain chain, String domain) {
		super(chain, domain);
	}
}
