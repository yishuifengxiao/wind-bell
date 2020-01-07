package com.yishuifengxiao.common.crawler.domain.eunm;

/**
 * 提取类型
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
public enum Rule {
	/**
	 * 原文提取器
	 */
	ALL,
	/**
	 * CSS提取器
	 */
	CSS,
	/**
	 * CSS文本提取器
	 */
	TEXT,
	/**
	 * XPATH提取器
	 */
	XPATH,
	/**
	 * 正则提取器
	 */
	REGEX,
	/**
	 * 替换提取器
	 */
	REPLACE,
	/**
	 * 移除提取器
	 */
	REMOVE,
	/**
	 * 常量提取器
	 */
	CONSTANT,
	/**
	 * 中文提取器
	 */
	CHN,
	/**
	 * 数字提取器
	 */
	NUM,
	/**
	 * 邮件提取器
	 */
	EMAIL,
	/**
	 * 切割提取器
	 */
	SUBSTR,
	/**
	 * 域名提取器
	 */
	DOMAIN,
	/**
	 * 占位符替换器
	 */
	SYSTEM,
	/**
	 * 数组截取提取器
	 */
	ARRAY,
	/**
	 * 脚本提取器
	 */
	SCRIPT,
	/**
	 * URL提取器
	 */
	URL;

}
