package com.yishuifengxiao.common.crawler.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;

/**
 * 链接处理工具类
 * 
 * @author yishui
 * @date 2019年12月2日
 * @version 1.0.0
 */
public class LinkUtils {
	/**
	 * 判断是否符合形如 http://www.yishuifengxiao.com 的正则表达式
	 */
	private final static Pattern PATTERN_PROTOCOL_AND_HOST = Pattern.compile(RuleConstant.REGEX_PROTOCOL_AND_HOST);

	/**
	 * 判断是否符合形如 www.yishuifengxiao.com 的正则表达式
	 */
	private final static Pattern PATTERN_DOMAIN = Pattern.compile(RuleConstant.REGEX_DOMAIN);

	/**
	 * 从url中提取出来协议和域名
	 * 
	 * @param url
	 * @return 返回协议和域名，形如 http://www.yishuifengxiao.com
	 */
	public static String extractProtocolAndHost(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		Matcher matcher = PATTERN_PROTOCOL_AND_HOST.matcher(url);
		return matcher.find() ? matcher.group() : null;

	}

	/**
	 * 判断是否符合网络请求的地址形式
	 * 
	 * @param url 需要判断的url
	 * @return 符合要求为true,否则为false
	 */
	public static boolean matchHttpRequest(String url) {
		if (StringUtils.isBlank(url)) {
			return false;
		}
		Matcher matcher = PATTERN_PROTOCOL_AND_HOST.matcher(url);
		return matcher.find();
	}

	/**
	 * 从url中提取出来域名
	 * 
	 * @param url
	 * @return 返回协议和域名，形如 www.yishuifengxiao.com
	 */
	public static String extractDomain(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		Matcher matcher = PATTERN_DOMAIN.matcher(url);
		return matcher.find() ? matcher.group() : null;
	}

	/**
	 * 从url中提取出来域名
	 * 
	 * @param url
	 * @return 返回协议和域名，形如 www.yishuifengxiao.com
	 */
	public static String extractTopLevelDomain(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		Matcher matcher = PATTERN_DOMAIN.matcher(url);
		if (matcher.find()) {
			String domain = matcher.group();
			int lastOrdinal = StringUtils.lastOrdinalIndexOf(domain, ".", 2);
			if (lastOrdinal != -1) {
				return StringUtils.substring(domain, lastOrdinal+1);
			}
		}

		return null;
	}
	
	public static void main(String[] args) {
		String str="https://c.runoob.com/front-end/854";
		System.out.println(extractTopLevelDomain(str));;
	}

}
