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
	public String handle(String currentPath, String url) {

		if (LinkUtils.matchHttpRequest(url)) {
			// 绝对地址

			return url;
		}
		return this.next != null ? this.next.handle(currentPath, url) : null;
	}

}
