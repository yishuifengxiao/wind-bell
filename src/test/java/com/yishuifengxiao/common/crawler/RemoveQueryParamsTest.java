package com.yishuifengxiao.common.crawler;

import org.apache.commons.lang3.StringUtils;

/**
 * 测试去除URL中的查询参数
 * 
 * @author qingteng
 * @date 2020年5月15日
 * @version 1.0.0
 */
public class RemoveQueryParamsTest {

	public static void main(String[] args) {
		String str = "https://fanyi.baidu.com/?aldtype=16047#zh/en/%E7%A7%BB%E9%99%A4%E8%AF%B7%E6%B1%82%E5%8F%82%E6%95%B0";
		System.out.println(StringUtils.indexOf(str, "?"));
		System.out.println(StringUtils.substringBefore(str, "?"));
	}

}
