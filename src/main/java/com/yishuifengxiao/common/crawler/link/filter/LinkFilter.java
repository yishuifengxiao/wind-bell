package com.yishuifengxiao.common.crawler.link.filter;

import java.util.List;

/**
 * 链接过滤<br/>
 * 作用如下：<br/>
 * 1. 剔除非本一级域名下的所有非法链接<br/>
 * 2. 将链接统一转换成网络地址形式
 * 3. 剔除所有非法的链接
 * @author yishui
 * @date 2019年12月9日
 * @version 1.0.0
 */
public interface LinkFilter {

	/**
	 * 将链接转换成网络地址形式
	 * 
	 * @param path 当前正在解析的网页的地址
	 * @param urls 需要转换的地址
	 * @return
	 */
	List<String> filter(String path, List<String> urls);

}
