package com.yishuifengxiao.common.crawler.macther;

/**
 * 路径匹配器<br/>
 * 判断路径于给定的模式是否匹配
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public interface PathMatcher {
	/**
	 * 判断路径于给定的模式是否完全匹配匹配
	 * 
	 * @param url 需要匹配的路径
	 * @return 匹配的话返回为true，否则为false
	 */
	boolean match(String url);
}
