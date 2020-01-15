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

最新的版本号请参见 [https://mvnrepository.com/artifact/com.yishuifengxiao.common/crawler](https://mvnrepository.com/artifact/com.yishuifengxiao.common/crawler)


交流 QQ 群 :<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=a81681f687ced1bf514d6226d00463798cefc0a9559fc7d34f1e17e719ca8573"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="易水组件交流群" title="易水组件交流群"></a> (群号 624646260)


**简单使用**

提取雅虎财经的内容页的电子货币的名字

```

        //创建一个提取规则
        //该提取规则标识使用 XPATH提取器进行提取，
        //该XPATH提取器的XPATH表达式为 //h1/text() ， 该提取提取器的作用顺序是0
        FieldExtractRule extractRule = new FieldExtractRule(Rule.XPATH, "//h1/text()", "", 0);

        //创建一个提取项
        ContentItem contentItem = new ContentItem();
        contentItem
                .setFiledName("name") //提取项代码，不能为空
                .setName("加密电子货币名字") //提取项名字，可以不设置
                .setRules(Arrays.asList(extractRule)); //设置提取规则

        //创建一个风铃虫实例
        Crawler crawler = CrawlerBuilder.create()
                .startUrl("https://hk.finance.yahoo.com/cryptocurrencies") //风铃虫的起始链接
                // 风铃虫会将每次请求的网页的内容中的URL先全部提取出来，然后将完全匹配此规则的链接放入链接池
                // 如果不设置则表示提取链接中所有包含域名关键字（例如此例中的ifeng）的链接放入链接池
                //链接池里的链接会作为下次抓取请求的种子链接
                .addLinkRule("https://hk.finance.yahoo.com/quote/.+")//链接提取规则，多以添加多个链接提取规则，
                //可以设置多个内容页的规则，多个内容页规则之间用半角逗号隔开
                //只要内容页URL中完全匹配此规则就进行内容提取，如果不设置标识提取域名下所有的链接
                .extractUrl("https://hk.finance.yahoo.com/quote/.+") //内容页的规则，
                //风铃虫可以设置多个提取项，这里为了演示只设置了一个提取项
                .addExtractItem(contentItem) //增加一个提取项
                //如果不设置则使用默认时间10秒，此值是为了防止抓取频率太高被服务器封杀
                .interval(3)//每次进行爬取时的平均间隔时间，单位为秒，
                .creatCrawler();
        //启动爬虫实例
        crawler.start();
        // 这里没有设置信息输出器，表示使用默认的信息输出器
        //默认的信息输出器使用的logback日志输出方法，因此需要看控制台信息

        //由于风铃虫时异步运行的，所以演示时这里加入循环
        while (Statu.STOP != crawler.getStatu()) {
            try {
                Thread.sleep(1000 * 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
```


上述例子的作用提取雅虎财经的内容页的电子货币的名字，如果用户想要提取其他信息，只需要按照规则配置好其他的提取规则即可。

>  **注意** 上述示例仅供学习演示所用，风铃虫使用者在抓取网页内容请严格遵守相关的法律规定和目标网站的蜘蛛协议

<br/>



 **风铃虫原理** 

![风铃虫原理](https://images.gitee.com/uploads/images/2019/1208/213313_eb03a944_400404.png "原理图.png")

风铃虫的原理极为简单，主要由 **资源调度器**、**网页下载器**、**链接解析器**、**内容解析器**、**信息输出器** 这极大部分组成。

他们的作用与功能如下所示：

- 资源调度器：负责风铃虫资源的调度过程，例如进行任务的储存、任务的调度和任务的管理
- 网页下载器：负责根据调度器调度的任务下载网页资源
- 链接解析器：负责解析网页下载器下载的网页内容，从网页内容中提取出所有符合要求的链接
- 内容解析器：负责对网页下载器下载的网页内容进行内容解析
- 信息输出器：输出内容解析器解析出来的数据

其中的链接解析器是由一系列的链接提取器组合而成，目前链接提取器主要是支持正则提取。

内容解析器由一系列的内容提取器组合而成，不同的内容提取器功能不同，适用于不同的解析场景，支持多个提取器的重复、循环等多种组合形式。

上述个组件均提供了自定义配置接口，使用户可以根据实际需要进行自定义配置，满足各种复杂乃至异常场景的要求。

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
13. 数组截取
14. ...
 
在进行文本内容提取时，用户可以将这些提取器自由组合以提取出自己需要的内容,更多提取器的具体用法请参见 [内容提取器用法](https://gitee.com/zhiyubujian/wind-bell/wikis/%E6%88%AA%E5%8F%96%E6%8F%90%E5%8F%96%E5%99%A8?sort_id=1783680)。

**风铃虫内置的浏览器标识有**：
1. 谷歌浏览器(windows版、linux版)
2. Opera浏览器 (windows版、MAC版)
3. 火狐浏览器(windows版、linux版、MAC版)
4. IE浏览器(IE9、IE11)
5. EDAG浏览器
6. safari浏览器(windows版、MAC版)
8. ...

**分布式支持**

核心代码如下:


```
....
//省略其他代码
....
    //创建redis资源调度器
    Scheduler scheduler=new RedisScheduler("唯一的名字",redisTemplate)
    //创建一个redis资源缓存器
    RequestCache requestCache = new RedisRequestCache(redisTemplate);

     crawler     
            .setRequestCache(requestCache) //设置使用redis资源缓存器
            .setScheduler(scheduler); //设置使用redis资源调度器
                 
....
//省略其他代码
....

//启动爬虫实例
crawler.start();

```
<br/>

 **状态监控** 

风铃虫还提供了强大的状态监控和事件监控能力，通过 [状态监听器](https://gitee.com/zhiyubujian/wind-bell/wikis/pages?sort_id=1793843&doc_id=471326)和[事件监听器](https://gitee.com/zhiyubujian/wind-bell/wikis/pages?sort_id=1793843&doc_id=471326)，风铃虫让你对任务的运行情况了如指掌，实时掌控实例运行过程中遇到的各种问题，真正做到对任务的运行情况洞若观火，方便运维。

 **解析模拟器** 

由于风铃虫的解析功能十分强大，规定定义十分灵活，为了直观地了解已配置的规则定义的作用，风铃虫提供了[解析模拟器](https://gitee.com/zhiyubujian/wind-bell/wikis/pages?sort_id=1797313&doc_id=471326)，让使用者能够快速了解自己设置的规则定义的效果是否符合预期目标，及时调整规则定义，方便风铃虫实例的配置。


<br/><br/>

 **风铃虫平台效果演示** 






1. 配置基本信息

>   配置爬虫的名字、使用的线程数量和超时停止时间

![输入图片说明](https://images.gitee.com/uploads/images/2019/1207/221953_91be5498_400404.png "配置基本信息.png")

2. 配置链接爬取信息

>     配置爬虫的起始种子链接和从网页里提取下一次抓取时的链接的提取规则

![配置链接爬取信息](https://images.gitee.com/uploads/images/2019/1202/155432_8ab363a8_400404.png "配置链接爬取信息.png")

3. 配置站点信息

>     此步骤一般可以省略，但是对于某些会校验cookie和请求头参数的网站，此配置非常有用

![配置站点信息](https://images.gitee.com/uploads/images/2019/1202/155457_d4bf6e93_400404.png "配置站点信息.png")

4. 提取项配置

>      配置需要从网站里提取出来的数据，例如新闻标题和网页正文等信息 

![内容页配置](https://images.gitee.com/uploads/images/2019/1202/155524_c5e82fd9_400404.png "内容页配置.png")

5. 属性提取配置

>      调用内容提取器进行任意组合，以根据需要提取出需要的数据

![属性提取配置](https://images.gitee.com/uploads/images/2019/1202/155554_15b869ae_400404.png "属性提取配置.png")

<br/><br/>

6. 属性提取测试

提前检验提取项的配置是否正确，提取出来的数据是否符合预期目标

![属性提取测试](https://images.gitee.com/uploads/images/2019/1209/163652_596bab35_400404.png "提取测试.png")

 **相关资源链接** 

 **文档地址** :[https://gitee.com/zhiyubujian/wind-bell/wikis/pages](https://gitee.com/zhiyubujian/wind-bell/wikis/pages)

 **官方文档** :[http://doc.yishuifengxiao.com/windbell/)