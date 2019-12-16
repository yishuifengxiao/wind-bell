package com.yishuifengxiao.common.crawler;

import java.time.LocalDateTime;

import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;

/**
 * 风铃虫任务
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public interface Task {
	/**
	 * 获取任务的名字
	 * 
	 * @return
	 */
	String getName();

	/**
	 * 获取任务的状态
	 * 
	 * @return
	 */
	Statu getStatu();

	/**
	 * 获取任务的定义规则
	 * 
	 * @return
	 */
	CrawlerRule getCrawlerRule();

	/**
	 * 获取任务的启动时间
	 * 
	 * @return
	 */
	LocalDateTime getStartTime();

	/**
	 * 获取所有的任务总数<br/>
	 * 注意此数量是在变化的，且应该在任务启动时调用
	 * 
	 * @return
	 */
	long getAllTaskCount();

	/**
	 * 获取已经解析成功的网页的数量<br/>
	 * 注意此数量是在变化的，且应该在任务启动时调用
	 * 
	 * @return
	 */
	long getExtractedTaskCount();

	/**
	 * 获取已经解析失败的网页的数量<br/>
	 * 注意此数量是在变化的，且应该在任务启动时调用
	 * 
	 * @return
	 */
	long getFailTaskCount();

}
