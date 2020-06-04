/**
 * 
 */
package com.yishuifengxiao.common.crawler.domain.eunm;

/**
 * 内容匹配策略
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public enum Type {
	/**
	 * 不匹配
	 */
	NONE,
	/**
	 * 文本匹配策略
	 */
	ALL,
	/**
	 * XPATH匹配策略
	 */
	XPATH,
	/**
	 * CSS匹配策略
	 */
	CSS,
	/**
	 * 正则匹配策略
	 */
	REGEX,
	/**
	 * 脚本匹配策略
	 */
	SCRIPT
}
