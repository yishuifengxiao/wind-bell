package com.yishuifengxiao.common.crawler.domain.model;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 内容解析规则<br/>
 * 定义了哪些页面为内容页<br/>
 * 根据 内容页地址规则 和 内容匹配规则 确定哪些页面是内容页，需要从中提取出数据
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
	 * 内容页地址规则,即只有该规则的网页内容才会被提取,多个规则用半角逗号隔开
	 */
	@ApiModelProperty("内容页地址规则,即只有该规则的网页内容才会被提取,多个规则用半角逗号隔开")
	private MatcherRule contentPageRule;

	/**
	 * 根据网页的内容判断此网页是否需要进行内容提取操作，可与 内容页规则 参数配合使用
	 */
	@ApiModelProperty("内容匹配规则")
	private PageRule pageRule;

}
