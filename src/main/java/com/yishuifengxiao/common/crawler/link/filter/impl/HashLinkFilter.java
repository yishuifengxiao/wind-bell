package com.yishuifengxiao.common.crawler.link.filter.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;

/**
 * 哈希链接过滤器<br/>
 * 当抓取出来的链接为相对地址，且地址的开头为 # 时，该地址可能是哈希地址，不对其进行处理<br/>
 * 该过滤器最好作用在相对链接过滤器的前面
 * 
 * @author yishui
 * @version 1.0.0
 */
public class HashLinkFilter extends BaseLinkFilter {

	public HashLinkFilter(BaseLinkFilter next) {
		super(next);
	}

	@Override
	protected String doFilter(BaseLinkFilter next, String path, String url) {
		if (StringUtils.startsWith(url, RuleConstant.HASH_ADDR)) {
			// 哈希地址
			return null;
		}
		return next.doFilter(path, url);
	}

}
