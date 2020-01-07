package com.yishuifengxiao.common.crawler.listener;

import com.yishuifengxiao.common.crawler.Task;
import com.yishuifengxiao.common.crawler.domain.entity.Page;

/**
 * 风铃虫处理事件监听器<br/>
 * 监听风铃虫处理过程中的各种消息时间<br/>
 * 注意此监听器是同步的，请勿在此进行任务可能会导致风铃虫阻塞的操作
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
public interface CrawlerListener {
	/**
	 * 获取的调度命令的请求的url为空时触发
	 * 
	 * @param task 当前任务信息
	 */
	void onNullRquest(final Task task);

	/**
	 * 下载页面失败的消息
	 * 
	 * @param task 当前任务信息
	 * @param page 原始的页面信息
	 * @param e    失败的原因
	 */
	void onDownError(final Task task, final Page page, Exception e);

	/**
	 * 下载页面成功的消息
	 * 
	 * @param task        当前任务信息
	 * @param url         对应页面的请求信息
	 * @param redirectUrl 具备重定向功能的下载器在请求时重定向之后的地址
	 * @param code        风铃虫下载页面的响应码
	 * @param rawTxt      对应的页面的原始文本
	 */
	void onDownSuccess(final Task task, final String url, final String redirectUrl, final int code,
			final String rawTxt);

	/**
	 * 解析页面失败的消息
	 * 
	 * @param task 当前任务信息
	 * @param page 原始的页面信息
	 * @param e    失败的原因
	 */
	void onExtractError(final Task task, final Page page, Exception e);

	/**
	 * 解析页面成功的消息
	 * 
	 * @param task 当前任务信息
	 * @param page 原始的页面信息
	 */
	void onExtractSuccess(final Task task, final Page page);

	/**
	 * 任务因为被目标服务器封杀而退出
	 * 
	 * @param task 当前任务信息
	 */
	void exitOnBlock(final Task task);

	/**
	 * 任务因为已经完成而退出
	 * 
	 * @param task 当前任务信息
	 */
	void exitOnFinish(final Task task);
}
