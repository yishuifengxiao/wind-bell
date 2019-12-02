package com.yishuifengxiao.common.crawler.domain.model;

import java.io.Serializable;

import com.yishuifengxiao.common.crawler.domain.eunm.Rule;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 具体的内容提取规则
 *
 * @author yishui
 * @version 1.0.0
 * @date 2019-11-5
 */
@ApiModel(value = "内容页中的属性提取规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class FieldExtractRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6972666882676555351L;
	/**
	 * 内容处理策略，直接获取、CSS提取、XPATH提取，正则提取,替换内容，移除内容
	 */
	@ApiModelProperty("内容处理策略，直接获取、CSS提取、XPATH提取，正则提取,替换内容，移除内容")
	private Rule rule;
	/**
	 * 第一个参数
	 */
	@ApiModelProperty("第一个参数")
	private String param1;
	/**
	 * 第二个参数，部分规则下生效（如替换时作为替换目标）
	 */
	@ApiModelProperty("第二个参数，部分规则下生效（如替换时作为替换目标）")
	private String param2;
	/**
	 * 下一个处理规则
	 */
	@ApiModelProperty("处理序号")
	private Integer sort = 0;
}
