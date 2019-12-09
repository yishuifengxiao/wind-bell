package com.yishuifengxiao.common.crawler.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 正则表达式工厂
 * 
 * @author yishui
 * @date 2019年12月2日
 * @version 1.0.0
 */
public class RegexFactory {
	/**
	 * 存储正则Pattern的集合 key ：正则表达式 value ：Pattern对象
	 */
	private static final Map<String, Pattern> PATTERN_CACHE = new HashMap<>();

	/**
	 * 根据正则表达式获取Pattern对象
	 * 
	 * @param regex
	 * @return
	 */
	public static Pattern pattern(String regex) {
		if (StringUtils.isBlank(regex)) {
			throw new RuntimeException("正则表达式不能为空");
		}
		regex = regex.trim();
		Pattern pattern = PATTERN_CACHE.get(regex);
		if (pattern == null) {
			pattern = Pattern.compile(regex);
			PATTERN_CACHE.put(regex, pattern);
		}
		return pattern;
	}

	/**
	 * 判断内容是否符合正则表达式
	 * 
	 * @param regex
	 * @param content
	 * @return
	 */
	public static boolean match(String regex, String content) {
		Pattern pattern = pattern(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches();
	}

	/**
	 * 判断内容是否包正则表达式标识的内容
	 * 
	 * @param regex
	 * @param content
	 * @return
	 */
	public static boolean find(String regex, String content) {
		Pattern pattern = pattern(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.find();
	}

	/**
	 * 根据正则表达式从内容中提取出一组匹配的内容
	 * 
	 * @param regex   正则表达式
	 * @param content 目标内容
	 * @return
	 */
	public static String extract(String regex, String content) {
		if (StringUtils.isBlank(content)) {
			return content;
		}
		Pattern pattern = pattern(regex);
		Matcher matcher = pattern.matcher(content);
		return matcher.matches() ? matcher.group() : null;
	}

	/**
	 * 根据正则表达式从内容中提取出所有匹配的内容
	 * 
	 * @param regex   正则表达式
	 * @param content 目标内容
	 * @return
	 */
	public static List<String> extractAll(String regex, String content) {
		if (StringUtils.isBlank(content)) {
			return new ArrayList<>();
		}
		List<String> list = new ArrayList<>();
		Pattern pattern = pattern(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			for (int i = 0; i < matcher.groupCount(); i++) {
				list.add(matcher.group(i));
			}
		}
		return list;
	}

}
