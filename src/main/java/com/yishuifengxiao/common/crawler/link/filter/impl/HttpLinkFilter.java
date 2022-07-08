package com.yishuifengxiao.common.crawler.link.filter.impl;

import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.tool.utils.LinkUtils;

/**
 * 
 * 网络地址链接过滤器<br/>
 * 处理网络地址链接，直接将其输出
 * 
 * @author yishui
 * @version 1.0.0
 */
public class HttpLinkFilter extends BaseLinkFilter {

	public HttpLinkFilter(BaseLinkFilter next) {
		super(next);
	}

	@Override
	public String doFilter(BaseLinkFilter next, String path, String url) {

		if (LinkUtils.matchHttpRequest(url)) {
			// 网络地址

			return url;
		}
		return next.doFilter(path, url);
	}

}
