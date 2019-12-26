package com.yishuifengxiao.common.crawler.simulator;

import com.yishuifengxiao.common.crawler.domain.entity.SimulatorData;
import com.yishuifengxiao.common.crawler.domain.model.ContentItem;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;

/**
 * 提取测试器
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface Simulator {
	/**
	 * 测试链接提取
	 * 
	 * @param siteRule 站点规则
	 * @param linkRule 链接提取规则
	 * @param downloader 下载器
	 * @return
	 */
	SimulatorData link(SiteRule siteRule, LinkRule linkRule, Downloader downloader);

	/**
	 * 提取测试
	 * 
	 * @param url                测试目标地址
	 * @param siteRule           站点规则
	 * @param contentExtractRule 内容提取规则
	 * @param downloader 下载器
	 * @return
	 */
	SimulatorData extract(String url, SiteRule siteRule, ContentItem contentExtractRule, Downloader downloader);

}
