package com.yishuifengxiao.common.crawler.content;

import java.util.List;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
import com.yishuifengxiao.common.tool.exception.CustomException;

/**
 * 内容解析器<br/>
 * 用于从网页里根据需要提取出目标数据<br/>
 * 
 * 使用方法核心示例如下：
 * 
 * <pre>
 * //获取到网页的源码
 * String rawtxt = page.getRawTxt();
 * // 执行解析操作
 * object value=解析出来的数据
 * page.addResultItem( 实例中唯一的键, value) ;
 * </pre>
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface ContentExtract {

	/**
	 * 从网页内容里解析出所有符合要求的数据
	 * 
	 * @param contentRule 内容解析规则
	 * @param rules       内容提取规则
	 * @param page        网页对象
	 * @throws CustomException 解析时遇到的异常
	 */
	void extract(final ContentRule contentRule, final List<ExtractRule> rules, final Page page) throws CustomException;
}
