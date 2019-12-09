package com.yishuifengxiao.common.crawler.link.chain.impl;

import com.yishuifengxiao.common.crawler.link.chain.BaseLinkChain;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 网络地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class HttpLinkChain extends BaseLinkChain {

	public HttpLinkChain(BaseLinkChain next) {
		super(next);
	}

	@Override
	protected String doHandle(String currentUrl, String url) {
		if (LinkUtils.matchHttpRequest(url)) {
			return url;
		}
		return null;
	}

}
