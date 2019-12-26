package com.yishuifengxiao.common.crawler.domain.entity;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.Task;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 结果输出数据
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
@ApiModel(value = "结果输出数据")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ResultData {
	/**
	 * 页面里存储的数据
	 */
	private Map<String, Object> data = new WeakHashMap<>();
	/**
	 * 当前输出数据所属的任务的信息
	 */
	private Task task;
	/**
	 * 请求网页的地址
	 */
	private String url;
	
	/**
	 * 具备重定向功能的下载器在请求时重定向之后的地址,可能会为空
	 */
	private String redirectUrl;
	/**
	 * 是否为正常结果，如果为正常解析结果则为true,默认为true
	 */
	private boolean normal = true;

	/**
	 * 获取字符串类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		Assert.notNull(key, "键值不能为空");
		return data.getOrDefault(key, "").toString();
	}

	/**
	 * 获取int类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public Integer getInt(String key) {
		Assert.notNull(key, "键值不能为空");
		Object value = data.get(key);
		if (value == null) {
			return null;
		}
		return Integer.parseInt(value.toString());
	}

	/**
	 * 获取Float的数据
	 * 
	 * @param key
	 * @return
	 */
	public Float getFloat(String key) {
		Assert.notNull(key, "键值不能为空");
		Object value = data.get(key);
		if (value == null) {
			return null;
		}
		return Float.parseFloat(value.toString());
	}

	/**
	 * 获取Double类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public Double getDouble(String key) {
		Assert.notNull(key, "键值不能为空");
		Object value = data.get(key);
		if (value == null) {
			return null;
		}
		return Double.parseDouble(value.toString());
	}

	/**
	 * 获取Boolean类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getBoolean(String key) {
		Assert.notNull(key, "键值不能为空");
		Object value = data.get(key);
		if (value == null) {
			return null;
		}
		return Boolean.parseBoolean(value.toString());
	}

	/**
	 * 获取Object类型的数据
	 * 
	 * @param key
	 * @return
	 */
	public Object getObject(String key) {
		Assert.notNull(key, "键值不能为空");
		return data.get(key);
	}

	/**
	 * 获取全部的数据
	 * 
	 * @return
	 */
	public Map<String, Object> getAllData() {
		return data;
	}

	/**
	 * 判断是否存在某个键的数据
	 * 
	 * @param key
	 * @return
	 */
	public boolean extis(String key) {
		return data.containsKey(key);
	}

	/**
	 * 数据是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	/**
	 * 数据是否不为空
	 * 
	 * @return
	 */
	public boolean notEmpty() {
		return !this.isEmpty();
	}

	/**
	 * 增加数据
	 * 
	 * @param data
	 * @return
	 */
	public ResultData addData(Map<String, Object> data) {
		Assert.notNull(data, "添加的数据不能为空");
		this.data.putAll(data);
		return this;
	}

	/**
	 * 增加数据
	 * 
	 * @param key
	 * @param data
	 * @return
	 */
	public ResultData addData(String key, Object data) {
		this.data.put(key, data);
		return this;
	}

	/**
	 * 清空数据
	 * 
	 * @return
	 */
	public ResultData clear() {
		this.data.clear();
		return this;
	}

	
		
	public ResultData(String url, boolean normal) {
		this.url = url;
		this.normal = normal;
	}

	
	
}
