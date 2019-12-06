package com.yishuifengxiao.common.crawler.link.chain.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.chain.BaseLinkChain;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 绝对地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class AbsoluteLinkChain extends BaseLinkChain {

	public AbsoluteLinkChain(BaseLinkChain next) {
		super(next);
	}

	@Override
	protected String doHandle(String currentUrl, String url) {
		if (StringUtils.startsWith(url, RuleConstant.ABSOLUTE_ADDR_LINK)) {

			return new StringBuffer(LinkUtils.extractDomain(url)).append(url).toString();
		}
		return null;
	}

}
