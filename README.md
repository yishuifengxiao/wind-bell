# 风铃虫

#### 介绍
风铃虫是一款轻量级的爬虫工具，似风铃一样灵敏，如蜘蛛一般敏捷，能感知任何细小的风吹草动，轻松抓取互联网上的内容。它是一款对目标服务器相对友好的蜘蛛程序，内置了二十余种常见或不常见的浏览器标识，能够自动处理cookie和网页来源信息，轻松绕过服务器限制，智能调整请求间隔时间，动态调整请求频率，防止对目标服务器造成干扰。此外，风铃虫还是一款对普通用户十分友好的工具，它提供的大量链接提取器和内容提取器让用户可以随心所欲地快速配置，甚至于只要提供一个开始请求地址就能配置出自己爬虫程序。同时，风铃虫也开放了许多自定义接口，让高级用户能够根据需要自定义爬虫功能。最后，风铃虫还天然支持分布式和集群功能，让你突破单机环境的束缚，释放出你的爬虫能力。可以说，风铃虫几乎能抓取目前所有的网站里的绝大部分内容。

**【声明】
请勿将风铃虫应用到任何可能会违反法律规定和道德约束的工作中,请友善使用风铃虫，遵守蜘蛛协议，不要将风铃虫用于任何非法用途。如您选择使用风铃虫即代表您遵守此协议，作者不承担任何由于您违反此协议带来任何的法律风险和损失，一切后果由您承担。**

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

 **风铃虫的配置过程极为简单** ，主要为以下四步

1. 配置爬虫的基本信息
1. 配置爬虫的链接信息
1. 配置爬虫的站点信息
1. 配置提取项


 **风铃虫原理** 

![输入图片说明](https://images.gitee.com/uploads/images/2019/1208/114442_44449814_400404.png "原理图.png")

其中的链接解析器是由一系列的链接提取器组合而成，目前链接提取器主要是支持正则提取。

内容解析器由一系列的内容提取器组合而成，不同的内容提取器功能不同，适用于不同的解析场景，支持多个提取器的重复、循环等多种组合形式。

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
<br/>

 **效果演示** 






1. 配置基本信息

>   配置爬虫的名字、使用的线程数量和超时停止时间

![输入图片说明](https://images.gitee.com/uploads/images/2019/1207/221953_91be5498_400404.png "配置基本信息.png")

2. 配置链接爬取信息

>     配置爬虫的起始种子链接和从网页里提取下一次抓取时的链接的提取规则

![配置链接爬取信息](https://images.gitee.com/uploads/images/2019/1202/155432_8ab363a8_400404.png "配置链接爬取信息.png")

3. 配置站点信息

>     此步骤一般可以省略，但是某些可能会校验cookie和请求头参数的网站，此配置非常有用

![配置站点信息](https://images.gitee.com/uploads/images/2019/1202/155457_d4bf6e93_400404.png "配置站点信息.png")

4. 内容页配置

>      配置需要从网站里提取出来的数据，例如新闻标题和网页正文等信息 

![内容页配置](https://images.gitee.com/uploads/images/2019/1202/155524_c5e82fd9_400404.png "内容页配置.png")

5. 属性提取配置

>      调用内容提取器进行任意组合，以根据需要提取出需要的数据

![属性提取配置](https://images.gitee.com/uploads/images/2019/1202/155554_15b869ae_400404.png "属性提取配置.png")

<br/><br/>