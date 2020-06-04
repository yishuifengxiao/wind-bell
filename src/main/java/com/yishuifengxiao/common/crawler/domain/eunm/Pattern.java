/**
 * 
 */
package com.yishuifengxiao.common.crawler.domain.eunm;

/**
 * 链接匹配模式
 * 
 * @author qingteng
 * @date 2020年5月23日
 * @version 1.0.0
 */
public enum Pattern {

	/**
	 * 不进行匹配
	 */
	NONE,
	/**
	 * 按照关键字匹配
	 */
	KEYWORD,
	/**
	 * 按照正则方式进行匹配
	 */
	REGEX,

	/**
	 * 排除匹配
	 */
	EXCLUDE

}
