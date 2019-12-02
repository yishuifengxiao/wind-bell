package com.yishuifengxiao.common.crawler.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-11
 */
@ApiModel(value = "请求头配置信息")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class HeaderRule {

	@ApiModelProperty("请求头的名字")
	@NotBlank(message = "请求头的名字不能为空")
	@Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "请求头的名字必须是数字或字母")
	private String headerName;

	@ApiModelProperty("请求头的值")
	@NotBlank(message = "请求头的值不能为空")
	@Pattern(regexp = "^[A-Za-z0-9_-/,]+$", message = "请求头的值必须是数字或字母")
	private String headerValue;
}
