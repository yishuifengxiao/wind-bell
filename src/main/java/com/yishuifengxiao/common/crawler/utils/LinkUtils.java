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

	private final static Pattern pattern = Pattern.compile(RuleConstant.REGEX_DIMAIN);

	/**
	 * 从url中提取出来域名
	 * 
	 * @param url
	 * @return
	 */
	public static String extractDomain(String url) {
		if (StringUtils.isBlank(url)) {
			return null;
		}
		Matcher matcher = pattern.matcher(url);
		return matcher.find() ? matcher.group() : null;

	}

}
