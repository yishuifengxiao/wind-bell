package com.yishuifengxiao.common.crawler.domain.entity;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.model.ContentRule;
import com.yishuifengxiao.common.crawler.domain.model.LinkRule;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 风铃虫规则
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@ApiModel(value = "风铃虫规则")
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
	 * 内容解析时使用到的线程数，默认为2
	 */
	@ApiModelProperty("内容解析时使用到的线程数，默认为2")
	protected Integer threadNum = SiteConstant.DEFAULT_THREAD_NUM;

	/**
	 * 站点规则信息
	 */
	@ApiModelProperty("站点规则信息")
	@Valid
	private SiteRule site;

	/**
	 * 风铃虫的链接处理规则
	 */
	@ApiModelProperty("链接处理规则信息")
	@Valid
	private LinkRule link;

	/**
	 * 内容处理规则信息
	 */
	@ApiModelProperty("内容处理规则信息")
	@Valid
	private ContentRule content;

}
