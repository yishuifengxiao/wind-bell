package com.yishuifengxiao.common.crawler;

import java.util.Arrays;

import com.yishuifengxiao.common.crawler.domain.eunm.Pattern;
import com.yishuifengxiao.common.crawler.domain.eunm.Rule;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;
import com.yishuifengxiao.common.crawler.domain.model.ExtractFieldRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.MatcherRule;

/**
 * 测试创建风铃虫实例
 * 
 * @author qingteng
 * @date 2020年5月23日
 * @version 1.0.0
 */
public class DemoTest {

	public static void main(String[] args) {

		// 创建一个提取属性规则
		// 该提取规则标识使用 XPATH提取器进行提取，
		// 该XPATH提取器的XPATH表达式为 //h1/text() ， 该提取提取器的作用顺序是0
		ExtractFieldRule extractFieldRule = new ExtractFieldRule(Rule.XPATH, "//h1/text()", "", 0);

		// 创建一个提取项
		ExtractRule extractRule = new ExtractRule();
		extractRule
				// 提取项代码，不能为空,同一组提取规则之内每一个提取项的编码必须唯一
				.setCode("code")
				// 提取项名字，可以不设置
				.setName("加密电子货币名字")
				// 设置提取属性规则
				.setRules(Arrays.asList(extractFieldRule));

		// 创建一个风铃虫实例
		Crawler crawler = CrawlerBuilder.create()
				// 风铃虫的起始链接
				.startUrl("https://hk.finance.yahoo.com/cryptocurrencies")
				// 风铃虫会将每次请求的网页的内容中的URL先全部提取出来，然后将完全匹配此规则的链接放入链接池
				// 如果不设置则表示提取链接中所有包含域名关键字（例如此例中的yahoo）的链接放入链接池
				// 链接池里的链接会作为下次抓取请求的种子链接
				// 链接提取规则的作用是风铃虫根据此规则从下载的网页里提取出符合此规则的链接，然后将链接放入链接池
				// 链接提取规则，多以添加多个链接提取规则，
				.addLinkRule(new MatcherRule(Pattern.REGEX, "https://hk.finance.yahoo.com/quote/.+"))
				// 可以设置多个内容页的规则
				// 只要内容页URL中完全匹配此规则就进行内容提取，如果不设置标识提取域名下所有的链接
				// 内容页规则是告诉风铃虫从哪些网页里提取出信息，因为不是所有的下载网页里都包含有需要的信息
				 // 内容页的规则，
				.contentPageRule(new MatcherRule(Pattern.REGEX, "https://hk.finance.yahoo.com/quote/.+"))
				// 风铃虫可以设置多个提取项,这里为了演示只设置了一个提取项
				// 增加一个提取项规则
				.addExtractRule(extractRule)
				// 如果不设置则使用默认时间10秒,此值是为了防止抓取频率太高被服务器封杀
				// 每次进行爬取时的平均间隔时间，单位为毫秒，
				.interval(3000)
				.creatCrawler();
		// 启动爬虫实例
		crawler.start();
		// 这里没有设置信息输出器，表示使用默认的信息输出器
		// 默认的信息输出器使用的logback日志输出方法，因此需要看控制台信息

		// 由于风铃虫是异步运行的，所以演示时这里加入循环
		while (Statu.STOP != crawler.getStatu()) {
			try {
				Thread.sleep(1000 * 20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
