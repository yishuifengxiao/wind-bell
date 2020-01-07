package com.yishuifengxiao.common.crawler.content;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.tool.exception.ServiceException;

/**
 * 内容解析器<br/>
 * 解析下载的原始数据<br/>
 * 
 * 使用方法核心示例如下：
 * 
 * <pre>
 * //获取到网页的源码
 * //String rawtxt = page.getRawTxt();
 * // 执行解析操作
 * //object value=解析出来的数据
 * //page.addResultItem( 实例中唯一的键, value) ;
 * </pre>
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface ContentExtract {

	/**
	 * 从网页内容里解析出所有符合要求的数据
	 * 
	 * @param page 网页对象
	 * @throws ServiceException 解析时遇到的异常
	 */
	void extract(final Page page) throws ServiceException;
}
