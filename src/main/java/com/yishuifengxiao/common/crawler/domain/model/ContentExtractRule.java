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
 * 内容提取规则
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@ApiModel(value = "具体的内容提取规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
@Valid
public class ContentExtractRule  implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7745781283922404878L;

	/**
     * 需要提取的属性的名字
     */
    @ApiModelProperty("名字")
    @NotBlank(message = "需要提取的属性的名字不能为空")
    private String name;

    /**
     * 需要提取的属性的编码
     */
    @ApiModelProperty("需要提取的属性的编码")
    @NotBlank(message = "需要提取的属性的编码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "需要提取的属性的编码必须是数字或字母")
    private String filedName;
    /**
     * 需要提取的属性的描述
     */
    @ApiModelProperty("需要提取的属性的描述")
    private String descr;

    /**
     * 属性提取规则
     */
    @ApiModelProperty("属性提取规则")
    @NotEmpty(message = "请至少配置一个属性提取规则")
    private List<FieldExtractRule> rules;

}
