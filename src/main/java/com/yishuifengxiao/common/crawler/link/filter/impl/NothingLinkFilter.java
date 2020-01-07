package com.yishuifengxiao.common.crawler.link.filter.impl;

import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;

/**
 * 占位链接过滤器<br/>
 * 什么都不做,直接返回原始的url,一般用于占位，插入在过滤器链的最后一个位置
 * 
 * @author yishui
 * @date 2019年12月27日
 * @version 1.0.0
 */
public class NothingLinkFilter extends BaseLinkFilter {

	public NothingLinkFilter(BaseLinkFilter next) {
		super(next);
	}

	@Override
	protected String doFilter(BaseLinkFilter next, String path, String url) {
		return url;
	}

}
