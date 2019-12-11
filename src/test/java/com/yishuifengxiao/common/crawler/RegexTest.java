package com.yishuifengxiao.common.crawler;

import java.util.List;

import org.junit.Test;

import com.yishuifengxiao.common.crawler.utils.RegexFactory;

/**
 * 测试正则提取工具
 * 
 * @author yishui
 * @date 2019年12月11日
 * @version 1.0.0
 */
public class RegexTest {

	@Test
	public void extractAll() {

		String regex = "[0-9]+";
		String content = "阅读数 30";
		List<String> str = RegexFactory.extractAll(regex, content);
		System.out.println(str);
	}
	
	@Test
	public void extract() {

		String regex = "[0-9]+";
		String content = " 阅读数 30";
		String str = RegexFactory.extract(regex, content);
		System.out.println(str);
	}
}
