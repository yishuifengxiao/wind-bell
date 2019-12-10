package com.yishuifengxiao.common.crawler;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
/**
 * 测试提取雅虎新闻的标题
 * @author yishui
 * @date 2019年12月10日
 * @version 1.0.0
 */
public class YahuTest {
	@Test
	public void testCrawler() {
		CrawlerRule rule = JSONObject.parseObject(RULE, CrawlerRule.class);

		Crawler crawler = Crawler.create(rule);
		crawler.start();

		while (Statu.STOP != crawler.getStatu()) {
			try {
				Thread.sleep(1000 * 20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private final static String RULE = "{\r\n" + "    \"name\": \"雅虎新闻抓取\",\r\n" + "    \"interval\": 20,\r\n"
			+ "    \"waitTime\": 300,\r\n" + "    \"threadNum\": 1,\r\n" + "    \"site\": {\r\n"
			+ "        \"domain\": \"\",\r\n" + "        \"userAgent\": \"\",\r\n"
			+ "        \"cacheControl\": \"max-age=0\",\r\n" + "        \"charset\": \"utf-8\",\r\n"
			+ "        \"retryCount\": 3,\r\n" + "        \"expectContinueEnabled\": false,\r\n"
			+ "        \"redirectsEnabled\": true,\r\n" + "        \"relativeRedirectsAllowed\": false,\r\n"
			+ "        \"circularRedirectsAllowed\": false,\r\n" + "        \"maxRedirects\": 50,\r\n"
			+ "        \"authenticationEnabled\": true,\r\n" + "        \"connectionRequestTimeout\": -1,\r\n"
			+ "        \"connectTimeout\": -1,\r\n" + "        \"socketTimeout\": -1,\r\n"
			+ "        \"contentCompressionEnabled\": true,\r\n" + "        \"normalizeUri\": true,\r\n"
			+ "        \"headers\": []\r\n" + "    },\r\n" + "    \"link\": {\r\n"
			+ "        \"startUrl\": \"https://news.yahoo.com/world/\",\r\n" + "        \"rules\": [\r\n"
			+ "            \".+\"\r\n" + "        ]\r\n" + "    },\r\n" + "    \"content\": {\r\n"
			+ "        \"extractUrl\": \"https://news.yahoo.com/([a-z]|\\\\d|-)+.html\",\r\n"
			+ "        \"contents\": [\r\n" + "            {\r\n" + "                \"name\": \"新闻标题\",\r\n"
			+ "                \"filedName\": \"name\",\r\n" + "                \"descr\": \"雅虎新闻页的标题\",\r\n"
			+ "                \"rules\": [\r\n" + "                    {\r\n"
			+ "                        \"rule\": \"XPATH\",\r\n"
			+ "                        \"param1\": \"//h1[@itemprop='headline']/text()\",\r\n"
			+ "                        \"param2\": \"\",\r\n" + "                        \"sort\": 1\r\n"
			+ "                    }\r\n" + "                ]\r\n" + "            }\r\n" + "        ]\r\n"
			+ "    }\r\n" + "}";
}
