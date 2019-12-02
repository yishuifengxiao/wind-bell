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
import java.io.Serializable;
import java.util.List;

/**
 * 链接处理规则
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@ApiModel(value = "链接处理规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
@Valid
public class LinkRule implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3417006572526049049L;
	/**
     * 起始链接，多个起始链接之间用半角逗号隔开
     */
    @ApiModelProperty("起始链接，多个起始链接之间用半角逗号隔开")
    @NotBlank(message="起始链接不能为空")
    private String startUrl;
    /**
     * 链接的提取规则,正则表达式
     */
    @ApiModelProperty("链接的提取规则,正则表达式")
    @NotEmpty(message="请至少配置一个链接提取规则")
    private List<String> rules;
}
