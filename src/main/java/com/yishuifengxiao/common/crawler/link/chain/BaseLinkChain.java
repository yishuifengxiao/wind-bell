package com.yishuifengxiao.common.crawler.link.chain;

/**
 * 抽象链接处理器
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public abstract class BaseLinkChain {
	/**
	 * 下一个处理器
	 */
	protected BaseLinkChain next;

	/**
	 * 处理链接
	 * 
	 * @param currentUrl 当前正在解析的网页的地址
	 * @param url        当前网页中提取出来的需要处理的网页地址
	 * @return
	 */
	protected abstract String doHandle(String currentUrl, String url);

	/**
	 * 根据当前链接对本页面中提取出来的链接进行处理，统一转换为网络地址形式
	 * 
	 * @param currentUrl 当前正在解析的网页的地址
	 * @param url        当前网页中提取出来的需要处理的网页地址
	 * @return
	 */
	public String handle(String currentUrl, String url) {
		String link = doHandle(currentUrl, url);
		if (null == link || "" == link) {
			if (next != null) {
				next.doHandle(currentUrl, url);
			}
		}
		return link;
	}

	public BaseLinkChain(BaseLinkChain next) {
		this.next = next;
	}

}
