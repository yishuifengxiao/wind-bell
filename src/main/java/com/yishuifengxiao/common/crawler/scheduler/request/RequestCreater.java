package com.yishuifengxiao.common.crawler.scheduler.request;

import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;

/**
 * 请求生成器<br/>
 * 根据站点规则和请求任务生成一个完整的请求任务
 * 
 * @author qingteng
 * @date 2020年5月17日
 * @version 1.0.0
 */
public interface RequestCreater {

	/**
	 * 根据站点设置规则补全请求信息
	 * 
	 * @param siteRule 站点规则
	 * @param request  请求信息
	 * @return 请求信息
	 */
	Request create(final SiteRule siteRule, final Request request);

}
