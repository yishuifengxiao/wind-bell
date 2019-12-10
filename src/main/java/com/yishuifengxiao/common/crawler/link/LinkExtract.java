package com.yishuifengxiao.common.crawler.link;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 链接解析器<br/>
 * 从网页的原始文本中提取出所有符合规则要求的链接
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface LinkExtract {
	/**
	 * 提取出网页里所有的链接
	 * 
	 * @param page
	 * @throws ServiceException
	 */
	void extract(Page page) throws ServiceException;
}
