# 风铃虫

#### 介绍
风铃虫是一款轻量级的爬虫工具，似风铃一样灵敏，如蜘蛛一般敏捷，能感知任何细小的风吹草动，轻松抓取互联网上的内容。它是一款对目标服务器相对友好的蜘蛛程序，其内置了二十余种常见或不常见的浏览器标识，能够动态处理cookie和网页来源信息，智能调整请求间隔时间，还可以动态调整请求频率，防止对目标服务器造成干扰。此外，风铃虫还是一款对普通用户十分友好的工具，它提供的大量链接提取器和内容提取器让用户可以随心所欲地快速配置，甚至于只要提供一个开始请求地址就能配置出自己爬虫程序。同时，风铃虫也开放了许多自定义接口，让高级用户能够根据需要自定义爬虫功能。最后，风铃虫还天然支持分布式和集群功能，让你突破单机环境的束缚，释放出你的爬虫能力。

**【声明】
请勿将风铃虫应用到任何可能会违反法律规定和道德约束的工作中,请友善使用风铃虫，遵守蜘蛛协议，不要将风铃虫用于任何非法用途。如您选择使用风铃虫即代表您遵守此协议，作者不承担任何由于您违反此协议带来任何的法律风险和损失。**

<br/>

**快速使用**


```
<dependency>
    <groupId>com.yishuifengxiao.common</groupId>
    <artifactId>crawler</artifactId>
    <version>替换为最新的版本号</version>
</dependency>
```


交流 QQ 群 :<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=a81681f687ced1bf514d6226d00463798cefc0a9559fc7d34f1e17e719ca8573"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="易水组件交流群" title="易水组件交流群"></a> (群号 624646260)

**风铃虫内置的内容提取器有**：
1. 原文提取器
2. 中文提取器
3. 常量提取器
4. CSS内容提取器
5. CSS文本提取器
6. 邮箱提取器
7. 数字提取器
8. 正则提取器
9. 字符删除提取器
10. 字符替换提取器
11. 字符串截取提取器
12. XPATH提取器
13. ...
 
在进行文本内容提取时，用户可以将这些提取器自由组合以提取出自己需要的内容。

**风铃虫内置的浏览器标识有**：
1. 谷歌浏览器(windows版、linux版)
2. Opera浏览器 (windows版、MAC版)
3. 火狐浏览器(windows版、linux版、MAC版)
4. IE浏览器(IE9、IE11)
5. EDAG浏览器
6. safari浏览器(windows版、MAC版)
8. ...

**简单使用**

提取雅虎新闻的内容页的新闻标题
```
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
```
上述例子的作用提取雅虎新闻的标题，如果用户想要提取其他信息，只需要按照规则配置好其他的提取规则即可。


**分布式支持**

核心代码如下:


```
RequestCache requestCache = new RedisRequestCache(redisTemplate);

RedisTaskScheduler redisTaskScheduler = new RedisTaskScheduler(crawlerData.getId(), redisTemplate);

crawler = Crawler.create(crawlerData)
                 .setRequestCache(requestCache)
                 .setTaskScheduler(redisTaskScheduler)
                 .setPipeline(dataPipeline);
```
<br/><br/><br/>
