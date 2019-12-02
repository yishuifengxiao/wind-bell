package com.yishuifengxiao.common.crawler.link.chain.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.chain.AbstractLinkChain;

/**
 * 绝对地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class AbsoluteLinkChain extends AbstractLinkChain {

	@Override
	public String handle(String currentUrl, String url) {
		if (StringUtils.startsWith(url, RuleConstant.ABSOLUTE_ADDR_LINK)) {

			return new StringBuffer(this.domain).append(url).toString();
		}
		return chain == null ? null : chain.handle(currentUrl, url);
	}

	public String getDomain() {
		return this.domain;
	}

	private String preHandle(String url) {
		if (StringUtils.endsWith(domain, RuleConstant.ABSOLUTE_ADDR_LINK)) {
			this.domain = StringUtils.substring(domain, 0, domain.length() - 1);
		}
		return this.domain;
	}

	public void setDomain(String domain) {

		this.domain = preHandle(domain);
	}

	public AbsoluteLinkChain(AbstractLinkChain chain, String domain) {
		super(chain, domain);
	}
}
