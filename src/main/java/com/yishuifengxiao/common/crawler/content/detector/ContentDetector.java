package com.yishuifengxiao.common.crawler.content.detector;

/**
 * 内容侦测器<br/>
 * 根据内容侦测规则判断页面是否需要进行数据解析
 * 
 * @author yishui
 * @date 2020年01月09日
 * @version 1.0.0
 */
public interface ContentDetector {

	/**
	 * 判断网页内容是否符合匹配规则
	 * 
	 * @param input 需要侦测的内容
	 * @return true表示符合，false表示不符合
	 */
	boolean match(String input);

}
