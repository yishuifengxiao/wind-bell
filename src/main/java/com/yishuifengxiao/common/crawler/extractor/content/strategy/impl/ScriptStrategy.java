package com.yishuifengxiao.common.crawler.extractor.content.strategy.impl;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;

import com.yishuifengxiao.common.crawler.extractor.content.strategy.Strategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 脚本提取器<br/>
 * 通过js脚本从输入参数中提取数据<br/>
 * 示例脚本如下:
 * 
 * <pre>
 *  var extractor = function(param) {
 * 		return param;
 *  };
 * </pre>
 * 
 * 脚本中必须包含一个名字为 extractor的函数
 * 
 * @author yishui
 * @version 1.0.0
 */
@Slf4j
public class ScriptStrategy implements Strategy {

	private ScriptEngine nashorn;

	public ScriptStrategy() {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		this.nashorn = scriptEngineManager.getEngineByName("nashorn");
	}

	/**
	 * 通过js脚本从输入参数中提取数据
	 *
	 * @param input  输入数据
	 * @param param1 第一个参数，必填，为js函数，必须有一个名字为 extractor 的函数
	 * @param param2 第二个参数 ，此模式下该参数无效
	 * @return 输出数据，注意可能是null或空字符串
	 */
	@Override
	public String extract(String input, String param1, String param2) {
		if (!StringUtils.isNoneBlank(input, param1)) {
			return "";
		}
		if (null == this.nashorn) {
			return "";
		}
		try {
			this.nashorn.eval(param1);
			Invocable invocable = (Invocable) nashorn;
			Object result = invocable.invokeFunction("extractor", input);
			return null != result ? result.toString() : "";
		} catch (Exception e) {
			log.info("使用【脚本提取规则】 提取 {} 时出现问题，提取参数为 param1= {} ,param2 = {},问题为 {}", input, param1, param2,
					e.getMessage());
		}
		return "";
	}

}
