package com.yishuifengxiao.common.crawler.link;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.extractor.ExtractorFactory;
import com.yishuifengxiao.common.crawler.link.filter.BaseLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.AbsoluteLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.HashLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.HttpLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.IllegalLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.RelativeLinkFilter;
import com.yishuifengxiao.common.crawler.link.filter.impl.ShortLinkFilter;
import com.yishuifengxiao.common.crawler.macther.MatcherFactory;
import com.yishuifengxiao.common.crawler.macther.PathMatcher;
import com.yishuifengxiao.common.crawler.utils.LocalCrawler;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 简单链接解析器<br/>
 * 功能如下：<br/>
 * 1 从网页的原始文本中统一转换成网络地址形式<br/>
 * 2 从转换后的地址里提取出所有符合要求的链接
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
@Slf4j
public class LinkExtractDecorator implements LinkExtract {

	/**
	 * 提取器生成工厂
	 */
	private final ExtractorFactory factory = new ExtractorFactory();
	/**
	 * 匹配器工厂
	 */
	private final MatcherFactory matcherFactory = new MatcherFactory();
	/**
	 * 链接过滤器
	 */
	private final BaseLinkFilter linkFilter = this.createLinkFilter();

	private LinkExtract linkExtract;

	@Override
	public void extract(final LinkRule linkRule, final Page page) throws ServiceException {

		// 调用实际处理类对信息进行处理
		List<String> links = this.factory.getLinkExtractor().extract(page);
		// 放入提取出来的链接
		page.setLinks(links);

		// 自定义解析数据
		if (this.linkExtract != null) {
			this.linkExtract.extract(linkRule, page);
		}
		// 当前请求的真实路径
		String path = StringUtils.isNotBlank(page.getRedirectUrl()) ? page.getRedirectUrl()
				: page.getRequest().getUrl();
		// 将链接统一转换成网络地址形式
		Set<String> urls = page.getLinks().stream().filter(Objects::nonNull).map(t -> linkFilter.doFilter(path, t))
				.collect(Collectors.toSet());
		// 过滤出来所有符合要求的连接
		links = this.fliter(linkRule, path, urls);
		// 放入提取出来的链接
		page.setLinks(links);

		log.debug("【id:{} , name:{} 】 The actual address of request {} is [ {} ], and the extracted link is {}",
				LocalCrawler.get().getUuid(), LocalCrawler.get().getName(), page.getRequest().getUrl(),
				page.getRedirectUrl(), page.getLinks());
		// @formatter:off
		// @formatter:on
	}

	/**
	 * 从所有的超链接里提取出符合配置规则的链接
	 * 
	 * @param linkRule 链接解析规则
	 * @param path     当前正在解析的网页内容的地址
	 * @param urls     从当前网页内容里提取出来的链接集合
	 * @return
	 */
	private List<String> fliter(final LinkRule linkRule, final String path, Set<String> urls) {

		List<PathMatcher> matchers = linkRule.getRules().stream().filter(Objects::nonNull)
				.filter(t -> t.getPattern() != null).map(matcherFactory::getMatcher).collect(Collectors.toList());

		return urls.stream().filter(Objects::nonNull).filter(t -> {
			return matchers.stream().anyMatch(v -> {
				return v.match(t);
			});
		}).collect(Collectors.toList());
	}

	/**
	 * 构建链接过滤器链
	 * 
	 * @return 链接过滤器链
	 */
	private BaseLinkFilter createLinkFilter() {
		RelativeLinkFilter relativeLinkFilter = new RelativeLinkFilter(null);
		HashLinkFilter hashLinkFilter = new HashLinkFilter(relativeLinkFilter);
		AbsoluteLinkFilter absoluteLinkFilter = new AbsoluteLinkFilter(hashLinkFilter);
		HttpLinkFilter httpLinkFilter = new HttpLinkFilter(absoluteLinkFilter);
		ShortLinkFilter shortLinkFilter = new ShortLinkFilter(httpLinkFilter);
		IllegalLinkFilter illegalLinkFilter = new IllegalLinkFilter(shortLinkFilter);
		return illegalLinkFilter;
	}

	public LinkExtractDecorator(LinkExtract linkExtract) {
		this.linkExtract = linkExtract;
	}

}
