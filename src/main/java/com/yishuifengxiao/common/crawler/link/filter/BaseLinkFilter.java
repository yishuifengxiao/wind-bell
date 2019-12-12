package com.yishuifengxiao.common.crawler.link.filter;

/**
 * 抽象链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public abstract class BaseLinkFilter {
	/**
	 * 下一个处理器
	 */
	protected BaseLinkFilter next;

	/**
	 * 处理链接
	 * 
	 * @param path 当前正在解析的网页的地址
	 * @param url        当前网页中提取出来的需要处理的网页地址
	 * @return
	 */
	public abstract String handle(String path, String url);


	public BaseLinkFilter(BaseLinkFilter next) {
		this.next = next;
	}

}
