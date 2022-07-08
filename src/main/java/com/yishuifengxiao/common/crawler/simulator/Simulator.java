package com.yishuifengxiao.common.crawler.simulator;

import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;

/**
 * 提取测试器<br/>
 * 用于测试风铃虫规则是否配置正确，请勿将此作为正式的批量抓取工具
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface Simulator {

	/**
	 * 测试网页下载功能
	 * 
	 * @param url        测试目标地址
	 * @param siteRule   站点规则
	 * @param downloader 下载器
	 * @return 下载的网页数据
	 */
	SimulatorData down(String url, SiteRule siteRule, Downloader downloader);

	/**
	 * 测试链接提取
	 * 
	 * @param url        测试目标地址
	 * @param siteRule   站点规则
	 * @param linkRule   链接提取规则
	 * @param downloader 下载器
	 * @return 模拟结果数据
	 */
	SimulatorData link(String url, SiteRule siteRule, LinkRule linkRule, Downloader downloader);

	/**
	 * 测试内容匹配
	 * 
	 * @param url        测试目标地址
	 * @param siteRule   站点规则
	 * @param content    内容解析规则
	 * @param downloader 下载器
	 * @return 模拟结果数据
	 */
	SimulatorData match(String url, SiteRule siteRule, ContentRule content, Downloader downloader);

	/**
	 * 提取测试
	 * 
	 * @param url                测试目标地址
	 * @param siteRule           站点规则
	 * @param contentExtractRule 内容提取规则
	 * @param downloader         下载器
	 * @return 模拟结果数据
	 */
	SimulatorData extract(String url, SiteRule siteRule, ExtractRule contentExtractRule, Downloader downloader);

}
