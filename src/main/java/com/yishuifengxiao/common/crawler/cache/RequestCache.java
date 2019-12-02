package com.yishuifengxiao.common.crawler.cache;

/**
 * 资源缓存器<br/>
 * 功能如下：<br/>
 * 1 查询此资源是否在历史记录中<br/>
 * 2 将查询资源存储到历史记录中<br/>
 * 3 查询此资源是否存在已解析记录中<br/>
 * 4 将查询资源存储到已解析记录中<br/>
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface RequestCache {
	/**
	 * 历史记录中是否存在此URL<br/>
	 * 只查询但不缓存该记录
	 * 
	 * @param taskName
	 * @param url
	 * @return
	 */
	boolean extisHistory(String taskName, String url);

	/**
	 * 查询历史记录中是否存在改URL对应的记录，不管存在与否，都要缓存该记录
	 *  @param taskName
	 * @param url
	 * @return
	 */
	boolean lookHistoryAndCache(String taskName, String url);

	/**
	 * 查询已经抓取的记录中是否存在改URL
	 *  @param taskName
	 * @param url
	 * @return
	 */
	boolean extisExtracted(String taskName, String url);

	/**
	 * 查询已经抓取的记录中是否存在改URL对应的记录，不管存在与否，都要缓存该记录
	 *  @param taskName
	 * @param url
	 * @return
	 */
	boolean lookExtractedAndCache(String taskName, String url);

}
