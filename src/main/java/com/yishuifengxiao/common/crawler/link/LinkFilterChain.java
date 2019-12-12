package com.yishuifengxiao.common.crawler.link;

import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.AbsoluteLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.HttpLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.NotLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.RelativeLinkFilter;

/**
 * 链接处理器<br/>
 * 将链接统一转换成网络地址形式
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class LinkFilterChain {

	private BaseLinkFilter nextChain;

	public LinkFilterChain() {
		RelativeLinkFilter relativeLinkFilter = new RelativeLinkFilter(null);
		AbsoluteLinkFilter absoluteLinkFilter = new AbsoluteLinkFilter(relativeLinkFilter);
		HttpLinkFilter httpLinkFilter = new HttpLinkFilter(absoluteLinkFilter);
		NotLinkFilter notLinkFilter = new NotLinkFilter(httpLinkFilter);
		this.nextChain = notLinkFilter;
	}

	public String handle(String path, String url) {
		return nextChain.handle(path, url);
	}
}
