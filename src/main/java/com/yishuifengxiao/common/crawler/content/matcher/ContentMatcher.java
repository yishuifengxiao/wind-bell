package com.yishuifengxiao.common.crawler.content.matcher;

/**
 * 内容匹配器<br/>
 * 根据内容匹配规则判断页面是否需要进行数据解析，即只有网页内容符合此匹配规则才会进行接下来的内容提取工作
 * 
 * @author yishui
 * @date 2020年01月09日
 * @version 1.0.0
 */
public interface ContentMatcher {

	/**
	 * 判断网页内容是否符合匹配规则
	 * 
	 * @param input 需要匹配的内容
	 * @return true表示符合，false表示不符合
	 */
	boolean match(String input);

}
