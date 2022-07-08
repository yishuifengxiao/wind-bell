package com.yishuifengxiao.common.crawler.link;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.tool.exception.CustomException;

/**
 * 链接解析器<br/>
 * 从网页的原始文本中提取出所有符合规则要求的链接
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface LinkExtract {
	/**
	 * 提取出网页里所有的链接
	 * 
	 * @param linkRule 链接解析规则
	 * @param page     网页对象
	 * @throws CustomException
	 */
	void extract(final LinkRule linkRule, final Page page) throws CustomException;
}
