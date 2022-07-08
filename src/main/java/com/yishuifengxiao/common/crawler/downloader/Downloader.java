package com.yishuifengxiao.common.crawler.downloader;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.tool.exception.CustomException;

/**
 * 网页下载器<br/>
 * 负责根据下载请求任务从指定的网站下载数据
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface Downloader extends Cloneable {
	/**
	 * 下载对应的请求资源
	 * 
	 * @param request 当前的下载请求
	 * @return 网页对象
	 * @throws CustomException
	 */
	Page down(final Request request) throws CustomException;

	/**
	 * 关闭下载器以释放资源
	 */
	void close();

}
