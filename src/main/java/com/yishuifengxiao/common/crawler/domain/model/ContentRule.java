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
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-14
 */
@ApiModel(value = "内容提取规则")
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
	 * 内容页url的规则,即只有该规则的网页内容才会被提取,多个规则用半角逗号隔开
	 */
	@ApiModelProperty("内容页url的规则,即只有该规则的网页内容才会被提取,多个规则用半角逗号隔开")
	private String extractUrl;

	/**
	 * 爬虫内容提取项
	 */
	@ApiModelProperty(" 爬虫内容提取项的配置")
	@NotEmpty(message = "请至少配置一个内容提取项")
	@Valid
	private List<ContentExtractRule> contents;

}
