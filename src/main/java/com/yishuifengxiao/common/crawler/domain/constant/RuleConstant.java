package com.yishuifengxiao.common.crawler.domain.constant;

/**
 * 规则常量类
 * 
 * @author yishui
 * @date 2019年11月27日
 * @version 1.0.0
 */
public final class RuleConstant {
	/**
	 * 匹配所有的url的正则表达
	 */
	public final static String REGEX_MATCH_ALL = ".+";

	/**
	 * 匹配所有的url的正则表达
	 */
	public final static String REGEX_URL = "[a-zA-z]+://[^\\s]*";
	/**
	 * ant模式下匹配全部的表达式
	 */
	public final static String ANT_MATCH_ALL = "/**";

	/**
	 * 获取所有链接的xpath表达式
	 */
	public final static String XPATH_ALL_LINK = "//a/@href";
	/**
	 * 绝对地址标志
	 */
	public final static String ABSOLUTE_ADDR_LINK="/";
	
	/**
	 * 网络地址标志
	 */
	public final static String NETWORK_ADDR_LINK="http";
	/**
	 * a标签中非link的表达式
	 */
	public final static String NOT_LINK="javascript";
	
	/**
	 * 提取协议和域名
	 */
	public final static String REGEX_PROTOCOL_AND_HOST="http[s]?://[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";
	
	/**
	 * 提取域名
	 */
	public final static String REGEX_DOMAIN="[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+\\.?";

}
