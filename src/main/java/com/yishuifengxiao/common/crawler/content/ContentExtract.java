package com.yishuifengxiao.common.crawler.content;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 内容解析器<br/>
 * 功能如下：<br/>
 * 1 解析下载的原始数据<br/>
 * 2 输出解析后的数据
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface ContentExtract {

	/**
	 * 从网页内容里解析出所有符合要求的数据
	 * @param page
	 * @throws ServiceException
	 */
	void extract(Page page) throws ServiceException;
}
