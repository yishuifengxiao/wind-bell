package com.yishuifengxiao.common.crawler.link.filter.impl;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.constant.RuleConstant;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;

/**
 * 相对地址链接过滤器<br/>
 * 将抽取出来的相对地址转换成网络地址形式
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public class RelativeLinkFilter extends BaseLinkFilter {

	public RelativeLinkFilter(BaseLinkFilter next) {
		super(next);

	}

	@Override
	public String doFilter(BaseLinkFilter next, String path, String url) {

		if (!StringUtils.startsWithAny(url, RuleConstant.ABSOLUTE_ADDR_LINK, RuleConstant.NETWORK_ADDR_LINK)) {
			// 不是网络地址和绝对地址
			// 提取当前路径中?之前的部分
			String urlWithNoQuery = StringUtils.substringBefore(path, RuleConstant.QUERY_SEPARATOR);
			// 去除掉最后的那个/
			if (StringUtils.endsWith(urlWithNoQuery, RuleConstant.LEFT_SLASH)) {
				urlWithNoQuery = StringUtils.substringBeforeLast(urlWithNoQuery, RuleConstant.ABSOLUTE_ADDR_LINK);
			}
			if (StringUtils.countMatches(urlWithNoQuery,
					RuleConstant.LEFT_SLASH) > RuleConstant.CHILD_PATH_FLAG_COUNT) {
				// 提取出当前路径的父路径
				urlWithNoQuery = StringUtils.substringBeforeLast(urlWithNoQuery, RuleConstant.ABSOLUTE_ADDR_LINK);
			}

			// 凭借出最终路径
			StringBuffer sb = new StringBuffer(urlWithNoQuery).append(RuleConstant.ABSOLUTE_ADDR_LINK).append(url);

			return sb.toString();

		}
		return next.doFilter(path, url);
	}

}
