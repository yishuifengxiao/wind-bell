package com.yishuifengxiao.common.crawler.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 请求头参数配置信息
 * 
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-11
 */
@ApiModel(value = "请求头配置信息")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class HeaderRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5229074282610578282L;

	/**
	 * 请求头参数的名字,不能为中文
	 */
	@ApiModelProperty("请求头参数的名字,不能为中文")
	@NotBlank(message = "请求头的名字不能为空")
	@Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "请求头的名字必须是数字或字母")
	private String headerName;

	/**
	 * 请求头参数的值,不能为中文
	 */
	@ApiModelProperty("请求头参数的值,不能为中文")
	@NotBlank(message = "请求头的值不能为空")
	@Pattern(regexp = "^[A-Za-z0-9_-/,]+$", message = "请求头的值必须是数字或字母")
	private String headerValue;
}
