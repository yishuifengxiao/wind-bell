package com.yishuifengxiao.common.crawler.domain.entity;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.model.ExtractRule;
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
	 * 每次请求的间隔时间，单位为毫秒，间隔时间为0到该值得两倍之间的一个随机数<br/>
	 * 防止因频繁请求而导致服务器封杀，该值必须不小于0，若该值为0表示不开启此功能<br/>
	 * 默认为10000 毫秒(10秒)
	 */
	@ApiModelProperty("请求的最大间隔时间，单位为毫秒,默认为10000 毫秒(10秒),请求间隔时间为0到该值得两倍之间的一个随机数")
	protected Long interval = SiteConstant.REQUEST_INTERVAL_TIME;
	/**
	 * 超时等待时间，单位为秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束
	 */
	@ApiModelProperty("超时等待时间，单位为毫秒,默认为300000毫秒(300秒),连续间隔多长时间后没有新的请求任务表明此任务已经结束")
	protected Long waitTime = SiteConstant.WAIT_TIME_FOR_CLOSE;

	/**
	 * 内容解析时使用到的线程数，默认值为 主机CPU的核心数
	 */
	@ApiModelProperty("解析时线程数，内容解析时使用到的线程数，默认值为1")
	protected Integer threadNum = SiteConstant.DEFAULT_THREAD_NUM;

	/**
	 * 站点规则
	 */
	@ApiModelProperty("站点规则")
	@Valid
	private SiteRule site;

	/**
	 * 链接解析规则
	 */
	@ApiModelProperty("链接解析规则")
	@Valid
	private LinkRule link;

	/**
	 * 内容解析规则,根据此规则判断哪些页面是内容页，需要进行数据提取操作
	 */
	@ApiModelProperty("内容解析规则")
	@Valid
	private ContentRule content;

	/**
	 * 内容提取规则
	 */
	@ApiModelProperty("内容提取规则")
	@Valid
	private List<ExtractRule> rules;

}
