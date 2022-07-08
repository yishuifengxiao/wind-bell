package com.yishuifengxiao.common.crawler.content.matcher;

import com.yishuifengxiao.common.crawler.domain.model.PageRule;

/**
 * 内容匹配器<br/>
 * 根据内容解析规则判断下载的页面是否需要进行数据解析<br/>
 * 即网页地址和网页内容均符合此匹配规则才会进行接下来的内容提取工作
 * 
 * @author yishui
 * @version 1.0.0
 */
public interface ContentMatcher {

	/**
	 * 判断网页内容是否符合匹配规则
	 * 
	 * @param pageRule 内容匹配规则
	 * @param input    需要匹配的原始网页内容
	 * @return true表示符合，false表示不符合
	 */
	boolean match(PageRule pageRule, String input);

}
