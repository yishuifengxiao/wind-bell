package com.yishuifengxiao.common.crawler.macther.impl;

import com.yishuifengxiao.common.crawler.macther.PathMatcher;

/**
 * 简单匹配器<br/>
 * 不进行匹配，直接通过
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public class SimplePathMatcher implements PathMatcher {

	@Override
	public boolean match(String url) {
		return true;
	}

}
