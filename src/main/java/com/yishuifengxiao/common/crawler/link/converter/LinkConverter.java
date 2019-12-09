package com.yishuifengxiao.common.crawler.link.converter;

import java.util.List;

/**
 * 链接转换器<br/>
 * 作用如下：<br/>
 * 1. 剔除非本一级域名下的所有非法链接<br/>
 * 2. 将链接统一转换成网络地址形式
 * 
 * @author yishui
 * @date 2019年12月9日
 * @version 1.0.0
 */
public interface LinkConverter {

	/**
	 * 将链接转换成网络地址形式
	 * 
	 * @param path 当前正在解析的网页的地址
	 * @param urls 需要转换的地址
	 * @return
	 */
	List<String> format(String path, List<String> urls);

}
