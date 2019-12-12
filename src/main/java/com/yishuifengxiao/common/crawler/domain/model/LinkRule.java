package com.yishuifengxiao.common.crawler.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yishuifengxiao.common.crawler.utils.LinkUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Arrays;
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
	@NotBlank(message = "起始链接不能为空")
	private String startUrl;

	/**
	 * 提取的链接域名中必须包含的关键字，多个关键字之间用半角逗号隔开,防止提取到第三方网站链接，默认为网站的域名的简单形式
	 */
	@ApiModelProperty("提取的链接域名中必须包含的关键字，多个关键字之间用半角逗号隔开,防止提取到第三方网站链接，默认为网站的域名的简单形式")
	private String keywords;
	/**
	 * 链接的提取规则,正则表达式
	 */
	@ApiModelProperty("链接的提取规则,正则表达式")
	@NotEmpty(message = "请至少配置一个链接提取规则")
	private List<String> rules;

	/**
	 * 获取到所有的关键字信息
	 * 
	 * @return
	 */
	@ApiModelProperty(hidden = true)
	@JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
	public List<String> getAllKeywords() {
		if (StringUtils.isBlank(this.keywords)) {
			this.keywords = LinkUtils.extractShortDomain(this.getStartUrl());
		}
		String[] tokens = StringUtils.splitByWholeSeparatorPreserveAllTokens(this.keywords, ",");
		return Arrays.asList(tokens);
	}
}
