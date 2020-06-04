package com.yishuifengxiao.common.crawler;

import java.util.List;

import org.seimicrawler.xpath.JXDocument;

/**
 * XPATH提取测试
 * 
 * @author qingteng
 * @date 2020年5月17日
 * @version 1.0.0
 */
public class XpathTest {

	public static void main(String[] args) throws Exception {
		String html = "<html><div><a href='https://github.com'>github.com</a></div>"
				+ "<table><tr><td>a</td><td>b</td></tr></table></html>";

		JXDocument jxDocument = JXDocument.create(html);
		List<Object> r1s = jxDocument.sel("//a/@href");

		for (Object obj : r1s) {
			System.out.println(obj);
		}

		List<Object> r2s = jxDocument.sel("//tr/td");

		for (Object obj : r2s) {
			System.out.println(obj);
		}

		List<Object> r3s = jxDocument.sel("//tr/td/text()");

		for (Object obj : r3s) {
			System.out.println(obj);
		}
	}

}
