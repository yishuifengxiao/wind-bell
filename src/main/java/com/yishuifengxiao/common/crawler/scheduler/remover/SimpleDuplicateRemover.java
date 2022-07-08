package com.yishuifengxiao.common.crawler.scheduler.remover;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.cache.RequestCache;
import com.yishuifengxiao.common.crawler.domain.entity.Request;

/**
 * 
 * 全路径去重器<br/>
 * 简单实现的请求去重器
 * 
 * @author qingteng
 * @version 1.0.0
 */
public class SimpleDuplicateRemover implements DuplicateRemover {

	@Override
	public boolean noDuplicate(final Task task, final RequestCache requestCache, final Request request) {
		if (null == request) {
			return false;
		}
		// 在历史链接记录集不存在时才处理
		return !requestCache.exist(task, request);
	}

	@Override
	public void doWhenNoDuplicate(final Task task, final RequestCache requestCache, final Request request) {
		// 存储在历史记录集之中
		requestCache.save(task, request);

	}

}
