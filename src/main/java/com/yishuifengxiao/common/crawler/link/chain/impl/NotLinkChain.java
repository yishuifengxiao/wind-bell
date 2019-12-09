/**
 * 
 */
package com.yishuifengxiao.common.crawler.link.chain.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.chain.BaseLinkChain;

/**
 * 对于抓取的不是链接的处理方式
 * 
 * @author yishui
 * @date 2019年12月9日
 * @version 1.0.0
 */
public class NotLinkChain extends BaseLinkChain {

	public NotLinkChain(BaseLinkChain next) {
		super(next);
	}

	@Override
	public String handle(String currentPath, String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		if (StringUtils.contains(url.toLowerCase(), RuleConstant.NOT_LINK)) {
			return null;
		}
		return this.next != null ? this.next.handle(currentPath, url) : null;
	}

}
