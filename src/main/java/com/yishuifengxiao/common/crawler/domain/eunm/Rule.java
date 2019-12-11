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
	 * 原始文本
	 */
	ALL,
	/**
	 * CSS提取
	 */
	CSS,
	/**
	 * CSS模式文本提取
	 */
	TEXT,
	/**
	 * XPATH提取
	 */
	XPATH,
	/**
	 * 正则提取
	 */
	REGEX,
	/**
	 * 替换
	 */
	REPLACE,
	/**
	 * 移除
	 */
	REMOVE,
	/**
	 * 自定义常量
	 */
	CONSTANT,
	/**
	 * 中文
	 */
	CHN,
	/**
	 * 数字
	 */
	NUM,
	/**
	 * 邮件
	 */
	EMAIL,
	/**
	 * 切割
	 */
	SUBSTR,
	/**
	 * 域名提取
	 */
	DOMAIN,
	/**
	 * 替换系统占位符
	 */
	SYSTEM,
	/**
	 * 数字截取
	 */
	ARRAY,
	/**
	 * URL提取
	 */
	URL;

}
