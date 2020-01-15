# wind-bell

#### Introduction
Wind-bell is a lightweight reptile tool that is as sensitive as a wind chime. It is agile like a spider. It is a spider program that is relatively friendly to the target server. It has more than 20 common or uncommon browser identifiers built in. It can automatically process cookies and webpage source information, easily bypass server restrictions, intelligently adjust request interval time, and dynamic. Adjust the request frequency to prevent interference with the target server. In addition, wind-bell is a tool that is very friendly to ordinary users. It provides a large number of link extractors and content extractors to allow users to quickly configure as they wish, and even configure their own crawlers by providing a starting request address. . At the same time, wind-bell has also opened many custom interfaces, allowing advanced users to customize the crawler function as needed. Finally, wind-bell also naturally supports distributed and cluster functions, allowing you to break through the constraints of a single machine environment and release your crawling capabilities. It can be said that wind-bell can crawl most of the content on all current websites.

**【Notice】
Do not apply wind-bell to any work that may violate legal requirements and ethical constraints. Please use wind-bell amicably, abide by the spider agreement, and do not use wind-bell for any illegal purpose. If you choose to use wind-bell, you are complying with this agreement. The author is not responsible for any legal risks and losses caused by your breach of this agreement. All consequences shall be borne by you.。**

<br/>

# Quick start


```
<dependency>
    <groupId>com.yishuifengxiao.common</groupId>
    <artifactId>crawler</artifactId>
    <version>Replace with the latest version number</version>
</dependency>
```

See the latest version [https://mvnrepository.com/artifact/com.yishuifengxiao.common/crawler](https://mvnrepository.com/artifact/com.yishuifengxiao.common/crawler)


Exchange QQ Group :<a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=a81681f687ced1bf514d6226d00463798cefc0a9559fc7d34f1e17e719ca8573"><img border="0" src="//pub.idqqimg.com/wpa/images/group.png" alt="易水组件交流群" title="易水组件交流群"></a> (群号 624646260)

<br/>
# sample

Extract the name of the electronic currency of the content page of Yahoo Finance

```

        //Create an extraction rule
        //The extraction rule identifies the extraction using the XPATH extractor
        //The XPATH expression of this XPATH extractor is //h1/text() ， The order of the extraction extractor is0
        FieldExtractRule extractRule = new FieldExtractRule(Rule.XPATH, "//h1/text()", "", 0);

        //Create an extract
        ContentItem contentItem = new ContentItem();
        contentItem
                .setFiledName("name") //提取项代码，不能为空
                .setName("Cryptocurrency_name") //提取项名字，可以不设置
                .setRules(Arrays.asList(extractRule)); //设置提取规则

        //Create a wind-bell instance
        Crawler crawler = CrawlerBuilder.create()
                 //wind-bell starting link
                .startUrl("https://hk.finance.yahoo.com/cryptocurrencies") 
                // wind-bell will first extract all the URLs in the content of the webpage for each request, and then put the links that exactly match this rule into the link pool
                // If it is not set, it means that all links containing domain name keywords (such as yahoo in this example) are extracted into the link pool
                //The links in the link pool will be used as the seed link for the next crawl request
                //Link extraction rules, to add multiple link extraction rules
                .addLinkRule("https://hk.finance.yahoo.com/quote/.+")
                //You can set multiple content page rules. Multiple content page rules are separated by commas.
                //As long as the content page URL exactly matches this rule, content extraction is performed. If no identifier is set, all links under the domain name are extracted.
                .extractUrl("https://hk.finance.yahoo.com/quote/.+") //Content page rules
                //Multiple extraction items can be set, here only one extraction item is set for demonstration
                .addExtractItem(contentItem) //Add an extraction item
                //If not set, the default time is 10 seconds. This value is to prevent the crawl frequency from being blocked by the server too high.
                //The average interval time for each crawl, in milliseconds
                .interval(3)
                .creatCrawler();
        //Start the crawler instance
        crawler.start();
        // No information output device is set here, which means the default information output device is used.
        //The default logback log output method used by the information exporter, so you need to look at the console information

        //Since the wind-bell runs asynchronously, a loop is added here for demonstration
        while (Statu.STOP != crawler.getStatu()) {
            try {
                Thread.sleep(1000 * 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
```


The role of the above example is to extract the name of the electronic currency of the content page of Yahoo Finance. If the user wants to extract other information, he only needs to configure other extraction rules in accordance with the rules.。

>  **TIP** The above examples are for learning and demonstration purposes only. Users of wind-bell should strictly abide by relevant legal requirements and spider agreements of the target website when crawling webpage content.

<br/>


<br/>

# principle

![principle](https://images.gitee.com/uploads/images/2019/1208/213313_eb03a944_400404.png "原理图.png")


The principle of bluebells is extremely simple, mainly consisting of  **Scheduler** 、**Downloader** 、**ContentExtract** 、**LinkExtract**   and **Pipeline** .

Their functions and functions are shown below：

- Scheduler：Responsible for the resource scheduling process, such as task storage, task scheduling, and task management
- Downloader：Responsible for downloading web resources based on tasks scheduled by the scheduler
- LinkExtract：Responsible for parsing web page content downloaded by web page downloader, extract all qualified links from web page content
- ContentExtract：Responsible for content analysis of web content downloaded by web downloader
- Pipeline：Output the data parsed by the content parser

Among them, LinkExtract：Responsible is a combination of a series of link extractors. At present, the link extractor mainly supports regular extraction.。

ContentExtract is composed of a series of content extractors. Different content extractors have different functions and are suitable for different parsing scenarios. It supports multiple combinations of multiple extractors, such as repeating and looping.



Each of the above components provides a custom configuration interface, allowing users to customize the configuration according to actual needs to meet the requirements of various complex and even abnormal scenarios。

<br/>


# The built-in content extractor
1. Text extractor
2. Chinese Extractor
3. Constant extractor
4. CSS Content Extractor
5. CSS text extractor
6. Email extractor
7. Number extractor
8. Regex extractor
9. Character deletion extractor
10. Character replacement extractor
11. String interception extractor
12. XPATH extractor
13. Array Truncation
14. Script extractor
15. ...
 
When extracting text content, users can freely combine these extractors to extract the content they need. For more specific usage of extractors, see [extractors](http://doc.yishuifengxiao.com/windbell/extractor.html#%E5%B8%B8%E9%87%8F%E6%8F%90%E5%8F%96%E5%99%A8)。

<br/>

#  The built-in browser agent

1. Google Chrome(windows、linux)
2. Opera browser(windows、MAC)
3. Firefox browser(windows、linux、MAC)
4. IE browser(IE9、IE11)
5. EDAG browser
6. safaribrowser(windows、MAC)
8. ...

<br/>

#  Distributed support

The core code is as follows:


```
....
//Omit other code
....
    //create a redis Scheduler
    Scheduler scheduler=new RedisScheduler("Unique name",redisTemplate)
    //create a redis RequestCache
    RequestCache requestCache = new RedisRequestCache(redisTemplate);

     crawler     
            .setRequestCache(requestCache) //apply
            .setScheduler(scheduler); //apply
                 
....
//Omit other code
....

//start
crawler.start();

```
<br/>



# Condition monitoring

Wind-bell also provides powerful status monitoring and event monitoring capabilities. With CrawlerListener  and StatuObserver  wind-bell, you can know the running status of the task, and control the various problems encountered during the running of the instance in real time. The operation situation is like watching the fire, which is convenient for operation and maintenance。

<br/>

# Emulator

Because the analysis function of wind-bell is very powerful and the definition of rules is very flexible. In order to intuitively understand the role of the configured rule definitions, wind-bell provides XXX to allow users to quickly understand whether the effect of the rule definitions set by them is consistent with expectations Objectives, adjust rule definitions in time to facilitate configuration of wind-bell instances。


<br/><br/>

# Effect demonstration






1. Configure basic information

>   Configure the crawler name, number of threads used, and timeout stop time

![输入图片说明](https://images.gitee.com/uploads/images/2019/1207/221953_91be5498_400404.png "配置基本信息.png")

2. Configure link crawling information

>     Configure the crawler's initial seed link and extraction rules for extracting links from the webpage for the next crawl

![配置链接爬取信息](https://images.gitee.com/uploads/images/2019/1202/155432_8ab363a8_400404.png "配置链接爬取信息.png")

3. Configure site information

>     This step can generally be omitted, but this configuration is useful for some websites that verify cookies and request header parameters

![配置站点信息](https://images.gitee.com/uploads/images/2019/1202/155457_d4bf6e93_400404.png "配置站点信息.png")

4. Extraction item configuration

>     Configure the data that needs to be extracted from the website, such as news headlines and web page information

![内容页配置](https://images.gitee.com/uploads/images/2019/1202/155524_c5e82fd9_400404.png "内容页配置.png")

5. Attribute extraction configuration

>      Call the content extractor for any combination to extract the required data as needed

![属性提取配置](https://images.gitee.com/uploads/images/2019/1202/155554_15b869ae_400404.png "属性提取配置.png")

<br/><br/>

6. Attribute extraction test

Check in advance whether the configuration of the extracted items is correct and whether the extracted data meets the expected goals

![属性提取测试](https://images.gitee.com/uploads/images/2019/1209/163652_596bab35_400404.png "提取测试.png")

 **Related resource links** 

 **wikis** :[https://gitee.com/zhiyubujian/wind-bell/wikis/pages](https://gitee.com/zhiyubujian/wind-bell/wikis/pages)

 **Official document** :[http://doc.yishuifengxiao.com/windbell/)