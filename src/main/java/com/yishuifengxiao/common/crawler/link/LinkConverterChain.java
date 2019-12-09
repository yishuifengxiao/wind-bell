package com.yishuifengxiao.common.crawler.link;

import com.yishuifengxiao.common.crawler.link.chain.BaseLinkChain;
import com.yishuifengxiao.common.crawler.link.chain.impl.AbsoluteLinkChain;
import com.yishuifengxiao.common.crawler.link.chain.impl.HttpLinkChain;
import com.yishuifengxiao.common.crawler.link.chain.impl.NotLinkChain;
import com.yishuifengxiao.common.crawler.link.chain.impl.RelativeLinkChain;

/**
 * 链接处理器<br/>
 * 将链接统一转换成网络地址形式
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class LinkConverterChain {

	private BaseLinkChain nextChain;

	public LinkConverterChain() {
		RelativeLinkChain relativechain = new RelativeLinkChain(null);
		AbsoluteLinkChain absolutechain = new AbsoluteLinkChain(relativechain);
		HttpLinkChain httpLinkStrategy = new HttpLinkChain(absolutechain);
		NotLinkChain notLinkChain = new NotLinkChain(httpLinkStrategy);
		this.nextChain = notLinkChain;
	}

	public String handle(String currentUrl, String url) {

		return nextChain.handle(currentUrl, url);
	}
}
