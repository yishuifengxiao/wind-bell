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
 * 属性提取规则<br/>
 * 定义了如何提取数据
 * 
 * @author yishui
 * @version 1.0.0
 */
@ApiModel(value = "属性提取规则")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ExtractFieldRule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6972666882676555351L;
	/**
	 * 内容处理策略，直接获取、CSS提取、XPATH提取，正则提取,替换内容，移除内容等，不能为空
	 */
	@ApiModelProperty("内容处理策略，直接获取、CSS提取、XPATH提取，正则提取,替换内容，移除内容等")
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
	 * 处理序号，在同一组属性提取规则中，处理序号小的属性提取规则会先执行
	 */
	@ApiModelProperty("处理序号")
	private Integer sort = 0;
}
