package com.yishuifengxiao.common.crawler.link.filter.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

/**
 * 绝对地址链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class AbsoluteLinkFilter extends BaseLinkFilter {

	public AbsoluteLinkFilter(BaseLinkFilter next) {
		super(next);
	}

	@Override
	public String handle(String path, String url) {

		if (StringUtils.startsWith(url, RuleConstant.ABSOLUTE_ADDR_LINK)) {
			// 绝对地址

			return new StringBuffer(LinkUtils.extractProtocolAndHost(path)).append(url).toString();
		}
		return this.next != null ? this.next.handle(path, url) : null;
	}

}
