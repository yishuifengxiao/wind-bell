package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;

/**
 * 域名提取策略<br/>
 * 从输入数据里提取出所有的域名
 * 
 * @author yishui
 * @version 1.0.0
 */
public class DomainStrategy extends RegexStrategy {

	/**
	 * 从输入数据里提取出所有的域名
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，此模式下该参数无效
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		return super.extract(input, RuleConstant.REGEX_DOMAIN, param2);
	}

}
