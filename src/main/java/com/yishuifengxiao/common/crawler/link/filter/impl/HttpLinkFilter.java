package com.yishuifengxiao.common.crawler.link.filter.impl;

import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 网络地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class HttpLinkFilter extends BaseLinkFilter {

	public HttpLinkFilter(BaseLinkFilter next) {
		super(next);
	}

	@Override
	public String handle(String path, String url) {

		if (LinkUtils.matchHttpRequest(url)) {
			// 绝对地址

			return url;
		}
		return this.next != null ? this.next.handle(path, url) : null;
	}

}
