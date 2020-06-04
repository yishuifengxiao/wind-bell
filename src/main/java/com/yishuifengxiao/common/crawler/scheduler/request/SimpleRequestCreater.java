package com.yishuifengxiao.common.crawler.scheduler.request;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;

/**
 * 简单请求生成器
 * 
 * @author qingteng
 * @date 2020年5月17日
 * @version 1.0.0
 */
public class SimpleRequestCreater implements RequestCreater {

	@Override
	public Request create(final SiteRule siteRule, final Request request) {
		// 补全请求信息
		request.setConnectTimeout(siteRule.getConnectTimeout());
		request.setCookies(siteRule.getCookiValues());
		request.setHeaders(siteRule.getAllHeaders());
		request.setUserAgent(siteRule.getAutoUserAgent());
		request.setMethod("get");
		request.setPriority(0L);

		if (StringUtils.isNotBlank(siteRule.getReferrer())) {
			request.setReferrer(siteRule.getReferrer());
		}

		Map<String, Object> extras = new WeakHashMap<>();
		extras.put("cacheControl", siteRule.getCacheControl());
		extras.put("cookieSpec", siteRule.getCookieSpec());
		extras.put("cookieSpec", siteRule.isRedirectsEnabled());
		extras.put("relativeRedirectsAllowed", siteRule.isRelativeRedirectsAllowed());
		extras.put("circularRedirectsAllowed", siteRule.isCircularRedirectsAllowed());
		extras.put("maxRedirects", siteRule.getMaxRedirects());
		extras.put("contentCompressionEnabled", siteRule.isContentCompressionEnabled());
		extras.put("normalizeUri", siteRule.isNormalizeUri());
		request.setExtras(extras);
		return request;
	}

}
