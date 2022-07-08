package com.yishuifengxiao.common.crawler.domain.model;

import java.io.Serializable;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 链接解析规则<br/>
 * 确定起始页和需要提取哪些链接，即通过种子链接提出后续所有的列表页和内容页的连接
 * 
 * @author yishui
 * @version 1.0.0
 */
@ApiModel(value = "链接解析规则")
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
	@NotBlank(message = "起始链接不能为空")
	private String startUrl;

	/**
	 * 链接提取规则
	 */
	@ApiModelProperty("链接提取规则")
	private Set<MatcherRule> rules;

}
