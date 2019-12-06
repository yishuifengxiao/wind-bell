package com.yishuifengxiao.common.crawler.domain.entity;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 爬虫完整信息
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@ApiModel(value = "爬虫完整信息")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Valid
@Validated
public class CrawlerRule implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3777184037897778033L;

	/**
	 * 爬虫的名字
	 */
	@ApiModelProperty("爬虫的名字")
	@NotBlank(message = "爬虫的名字不能为空")
	protected String name;

	/**
	 * 每次请求的间隔时间，单位为秒，间隔时间为0到改值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀
	 */
	@ApiModelProperty("每次请求的间隔时间，单位为秒,默认为10,请求间隔时间为0到该值得两倍之间的一个随机数")
	protected Integer interval = SiteConstant.REQUEST_INTERVAL_TIME;
	/**
	 * 超时等待时间，单位为秒,默认为300,连续间隔多长时间后没有新的请求任务表明此任务已经结束
	 */
	@ApiModelProperty("超时等待时间，单位为秒,默认为300,连续间隔多长时间后没有新的请求任务表明此任务已经结束")
	protected Integer waitTime = SiteConstant.WAIT_TIME_FOR_CLOSE;

	/**
	 * 爬虫线程数
	 */
	@ApiModelProperty("爬虫线程数")
	protected Integer threadNum = 1;

	/**
	 * 站点设置信息
	 */
	@ApiModelProperty("站点设置信息")
	@Valid
	private SiteRule site;

	/**
	 * 爬虫的链接处理规则
	 */
	@ApiModelProperty("爬虫的链接处理规则")
	@Valid
	private LinkRule link;

	/**
	 * 内容处理规则设置
	 */
	@ApiModelProperty("内容处理规则设置")
	@Valid
	private ContentRule content;

	/**
	 * 获取当前爬虫爬取的协议和域名信息<br/>
	 * 形式如 http://www.yishuifengxiao.com
	 * @return 协议和域名
	 */
	@ApiModelProperty(hidden = true)
	@JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
	public String getDomain() {
		if (link == null) {
			throw new IllegalArgumentException("链接提取规则不能为空");
		}
		String domain = LinkUtils.extractDomain(link.getStartUrl());
		return domain;
	}

}
