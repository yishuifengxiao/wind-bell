package com.yishuifengxiao.common.crawler.domain.model;

import java.io.Serializable;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 内容解析规则<br/>
 * 内容解析规则由内容页url的规则 和 一组内容提取规则组成
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
@ApiModel(value = "内容解析规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
@Valid
public class ContentRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4675000242006133909L;

	/**
	 * 内容页规则,即只有该规则的网页内容才会被提取,多个规则用半角逗号隔开
	 */
	@ApiModelProperty("内容页规则,即只有该规则的网页内容才会被提取,多个规则用半角逗号隔开")
	private String extractUrl;

	/**
	 * 根据网页的内容判断此网页是否需要进行内容提取操作，可与 内容页规则 参数配合使用
	 */
	@ApiModelProperty("内容匹配规则")
	private MatcherRule matcher;

	/**
	 * 内容提取规则
	 */
	@ApiModelProperty("内容提取规则")
	@NotEmpty(message = "请至少配置一组内容提取规则")
	@Valid
	private List<ContentItem> contents;

}
