package com.yishuifengxiao.common.crawler.scheduler.remover;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 
 * 无查询参数去重器<br/>
 * 去除URL上所有的查询参数的简单去重器
 * 
 * @author qingteng
 * @version 1.0.0
 */
public class HostDuplicateRemover implements DuplicateRemover {

	@Override
	public boolean noDuplicate(final Task task, final RequestCache requestCache, final Request request) {
		if (null == request) {
			return false;
		}
		// 在历史链接记录集不存在时才处理
		return !requestCache.exist(task, removeQueryParameters(request));
	}

	/**
	 * 去除请求的URL中的查询参数
	 * 
	 * @param request 请求
	 * @return
	 */
	private Request removeQueryParameters(final Request request) {
		String url = request.getUrl();
		String target = StringUtils.substringBefore(url, "?");
		request.setUrl(target);
		return request;
	}

	@Override
	public void doWhenNoDuplicate(final Task task, final RequestCache requestCache, final Request request) {
		// 存储在历史记录集之中
		requestCache.save(task, removeQueryParameters(request));

	}

}
