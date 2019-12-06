package com.yishuifengxiao.common.crawler.link;

import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.link.chain.AbstractLinkChain;
import com.yishuifengxiao.common.crawler.link.chain.impl.AbsoluteLinkChain;
import com.yishuifengxiao.common.crawler.link.chain.impl.HttpLinkChain;
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

	private AbstractLinkChain nextChain;

	public LinkConverterChain(String domain) {
		Assert.notNull(domain, "域名不能为空");
		RelativeLinkChain relativechain = new RelativeLinkChain(null, domain);
		AbsoluteLinkChain absolutechain = new AbsoluteLinkChain(relativechain, domain);
		HttpLinkChain httpLinkStrategy = new HttpLinkChain(absolutechain, domain);
		this.nextChain = httpLinkStrategy;
	}

	public String handle(String currentUrl, String url) {

		return nextChain.handle(currentUrl, url);
	}
}
