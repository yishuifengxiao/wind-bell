package com.yishuifengxiao.common.crawler.scheduler;

import com.yishuifengxiao.common.crawler.domain.entity.ResultData;

/**
 * 资源调度器<br/>
 * 负责资源的调度管理工作<br/>
 * 功能如下：<br/>
 * 1. 接收所有未过滤的链接并存储起来<br/>
 * 2. 从资源池中获取一个链接<br/>
 * 3. 判断资源是否需要解析<br/>
 * 4. 接收解析出来的资源
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface Scheduler {
	/**
	 * 接收所有的超链接并存储起来
	 * 
	 * @param urls 未过滤的超链接，可能会存在重复和已爬取等情况
	 */
	void push(String... urls);

	/**
	 * 从资源调度器里获取一个一个资源
	 * 
	 * @return
	 */
	String poll();

	/**
	 * 判断此资源是否需要解析
	 * 
	 * @param url
	 * @return
	 */
	boolean needExtract(String url);

	/**
	 * 接收解析出来的数据
	 * 
	 * @param resultData
	 */
	void recieve(ResultData resultData);
}
