package com.yishuifengxiao.common.crawler.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import java.io.Serializable;
import java.util.List;

/**
 * 内容提取规则<br/>
 * 内容提取规则中包含了一组属性提取规则，定义了如何提取一项数据
 * 
 * @author yishui
 * @version 1.0.0
 */
@ApiModel(value = "内容提取规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
@Valid
public class ExtractRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7745781283922404878L;

	/**
	 * 内容提取项名字
	 */
	@ApiModelProperty("内容提取项名字")
	@NotBlank(message = "内容提取项名字不能为空")
	private String name;

	/**
	 * 内容提取项代码，同一组内容提取规则里每一个内容提起项的编码必须唯一
	 */
	@ApiModelProperty("内容提取项代码")
	@NotBlank(message = "内容提取项代码不能为空")
	@Pattern(regexp = "^[A-Za-z0-9]+$", message = "内容提取项代码必须是数字或字母")
	private String code;
	/**
	 * 内容提取项描述
	 */
	@ApiModelProperty("内容提取项描述")
	private String descp;

	/**
	 * 属性提取规则，可以通过多个属性提取规则自由排列组合来决定最终如何提取出来对应的数据
	 */
	@ApiModelProperty("属性提取规则")
	@NotEmpty(message = "请至少配置一个属性提取规则")
	@Valid
	private List<ExtractFieldRule> rules;

}
