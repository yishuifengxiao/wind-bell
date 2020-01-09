package com.yishuifengxiao.common.crawler.domain.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

import com.yishuifengxiao.common.crawler.domain.eunm.Type;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 内容侦测规则<br/>
 * 内容侦测规则表明根据抓取内容判断该页面是否需要进行内容提取操作
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@ApiModel(value = "内容侦测规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
@Valid
public class ContentDetect implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8353534130930776695L;

	/**
	 * 内容侦测类型
	 */
	@ApiModelProperty("内容侦测类型")
	private Type type;

	/**
	 * 内容侦测参数
	 */
	@ApiModelProperty("内容侦测参数")
	@NotBlank(message = "内容侦测参数不能为空")
	private String pattern;

	/**
	 * 期待匹配参数
	 */
	@ApiModelProperty("期待匹配参数")
	private String target;

	/**
	 * 匹配类型，true表示必须包含期待匹配参数，false标识不能包含期待匹配参数
	 */
	@ApiModelProperty("匹配类型")
	private Boolean contained = true;

	/**
	 * 是否大小写敏感，即进行匹配时是否为大小写敏感，默认为 true
	 */
	@ApiModelProperty("是否大小写敏感")
	private Boolean caseSensitive = true;

	/**
	 * 是否为模糊匹配，默认为true
	 */
	@ApiModelProperty("是否为模糊匹配")
	private Boolean fuzzy = true;

}
