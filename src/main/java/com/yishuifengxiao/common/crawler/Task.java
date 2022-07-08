package com.yishuifengxiao.common.crawler;

import java.time.LocalDateTime;
import java.util.Map;

import com.yishuifengxiao.common.crawler.domain.entity.CrawlerRule;
import com.yishuifengxiao.common.crawler.domain.eunm.Statu;

/**
 * 风铃虫任务
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface Task {
	/**
	 * 获取到风铃虫实例的唯一ID
	 * 
	 * @return 实例的唯一ID
	 */
	String getUuid();

	/**
	 * 获取风铃虫实例的名字
	 * 
	 * @return 实例的名字
	 */
	String getName();

	/**
	 * 异步启动风铃虫实例
	 */
	void start();

	/**
	 * 停止风铃虫实例
	 */
	void stop();

	/**
	 * 获取任务的状态
	 * 
	 * @return 任务的状态
	 */
	Statu getStatu();

	/**
	 * 获取任务的定义规则
	 * 
	 * @return 定义规则
	 */
	CrawlerRule getCrawlerRule();

	/**
	 * 获取创建风铃虫实例时传递的额外数据信息
	 * 
	 * @return 附带的额外数据信息
	 */
	Map<String, Object> getExtra();

	/**
	 * 获取任务的启动时间
	 * 
	 * @return 任务的启动时间
	 */
	LocalDateTime getStartTime();

	/**
	 * 获取所有的任务总数<br/>
	 * 注意此数量是在变化的，且应该在任务启动后调用
	 * 
	 * @return 所有的任务总数
	 */
	long getAllTaskCount();

	/**
	 * 获取本实例已经解析成功的网页的数量<br/>
	 * 注意此数量是在变化的，且应该在任务启动后调用
	 * 
	 * @return 本实例已经解析成功的网页的数量
	 */
	long getExtractedTaskCount();

	/**
	 * 获取本实例已经解析失败的网页的数量<br/>
	 * 注意此数量是在变化的，且应该在任务启动后调用
	 * 
	 * @return 本实例已经解析失败的网页的数量
	 */
	long getFailTaskCount();

}
