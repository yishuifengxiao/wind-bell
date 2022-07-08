/**
 * 
 */
package com.yishuifengxiao.common.crawler.domain.model;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import com.yishuifengxiao.common.crawler.domain.eunm.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 链接过滤规则
 * 
 * @author qingteng
 * @version 1.0.0
 */
@ApiModel(value = "链接匹配规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
@Validated
@Valid
public class MatcherRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4205510200531436262L;

	/**
	 * 链接匹配模式
	 */
	@ApiModelProperty("链接匹配模式")
	private Pattern pattern = Pattern.NONE;

	/**
	 * 匹配表达式<br/>
	 * 在非正则模式下，匹配表达式可以包含多个参数，多个参数之间用半角逗号隔开，这个多个参数之间是与连接(&&)的关系
	 */
	@ApiModelProperty("匹配表达式")
	private String expression;

}
