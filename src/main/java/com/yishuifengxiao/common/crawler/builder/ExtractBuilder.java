package com.yishuifengxiao.common.crawler.builder;

import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.link.LinkExtract;

/**
 * 解析器构造者<br/>
 * 根据不同的处理规则构造出对应的解析器
 * 
 * @author yishui
 * @date 2019年12月10日
 * @version 1.0.0
 */
public interface ExtractBuilder {
	/**
	 * 构建一个链接解析器
	 * 
	 * @param link        链接处理规则
	 * @param linkExtract 自定义链接解析器，允许为空
	 * @return 链接解析器
	 */
	LinkExtract createLinkExtract(LinkRule link, LinkExtract linkExtract);

	/**
	 * 构造一个内容解析器
	 * 
	 * @param content        内容解析规则
	 * @param contentExtract 自定义内容解析器，允许为空
	 * @return 内容解析器
	 */
	ContentExtract createContentExtract(ContentRule content, ContentExtract contentExtract);

}
