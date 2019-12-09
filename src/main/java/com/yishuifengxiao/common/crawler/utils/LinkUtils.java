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

	private final static Pattern PATTERN = Pattern.compile(RuleConstant.REGEX_DIMAIN);

	/**
	 * 从url中提取出来域名
	 * 
	 * @param url
	 * @return 返回协议和域名，形如 http://www.yishuifengxiao.com
	 */
	public static String extractDomain(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		Matcher matcher = PATTERN.matcher(url);
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
		Matcher matcher = PATTERN.matcher(url);
		return matcher.matches();
	}

}
