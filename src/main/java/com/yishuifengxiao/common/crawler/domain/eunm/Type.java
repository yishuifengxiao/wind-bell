/**
 * 
 */
package com.yishuifengxiao.common.crawler.domain.eunm;

/**
 * 内容侦测策略
 * 
 * @author yishui
 * @date 2019年11月20日
 * @version 1.0.0
 */
public enum Type {
	/**
	 * 文本侦测策略
	 */
	ALL,
	/**
	 * XPATH侦测策略
	 */
	XPATH,
	/**
	 * CSS侦测策略
	 */
	CSS,
	/**
	 * 正则侦测策略
	 */
	REGEX,
	/**
	 * 脚本侦测策略
	 */
	SCRIPT
}
