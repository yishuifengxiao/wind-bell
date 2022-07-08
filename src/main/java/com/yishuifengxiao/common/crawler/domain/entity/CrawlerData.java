package com.yishuifengxiao.common.crawler.domain.entity;

import java.io.Serializable;
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
 * 解析结果输出数据
 * 
 * @author yishui
 * @version 1.0.0
 */
@ApiModel(value = "解析结果输出数据")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class CrawlerData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7545122886076123689L;
	/**
	 * 页面里存储的数据
	 */
	private Map<String, Object> data = new WeakHashMap<>();
	/**
	 * 抓取的网页原始文本
	 */
	private String rawTxt;
	/**
	 * 当前输出数据所属的任务的信息
	 */
	private Task task;
	/**
	 * 当前的请求任务信息
	 */
	private Request request;

	/**
	 * 具备重定向功能的下载器在请求时重定向之后的地址,可能会为空
	 */
	private String redirectUrl;

	/**
	 * 获取字符串类型的数据
	 * 
	 * @param key 数据的key
	 * @return 字符串类型的数据
	 */
	public String getString(String key) {
		Assert.notNull(key, "键值不能为空");
		return data.getOrDefault(key, "").toString();
	}

	/**
	 * 获取int类型的数据
	 * 
	 * @param key 数据的key
	 * @return int类型的数据
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
	 * @param key 数据的key
	 * @return Float的数据
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
	 * @param key 数据的key
	 * @return Double类型的数据
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
	 * @param key 数据的key
	 * @return Boolean类型的数据
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
	 * @param key 数据的key
	 * @return Object类型的数据
	 */
	public Object getObject(String key) {
		Assert.notNull(key, "键值不能为空");
		return data.get(key);
	}

	/**
	 * 判断是否存在某个键的数据
	 * 
	 * @param key 数据的key
	 * @return 存在返回为true,否则为false
	 */
	public boolean extis(String key) {
		return data.containsKey(key);
	}

	/**
	 * 数据是否为空
	 * 
	 * @return 若数据为空则返回为true,否则为false
	 */
	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	/**
	 * 数据是否不为空
	 * 
	 * @return 若数据不为空则返回为true,否则为false
	 */
	public boolean notEmpty() {
		return !this.isEmpty();
	}

	/**
	 * 增加数据
	 * 
	 * @param data 待增加的数据
	 * @return 解析结果输出数据实例
	 */
	public CrawlerData addData(Map<String, Object> data) {
		Assert.notNull(data, "添加的数据不能为空");
		this.data.putAll(data);
		return this;
	}

	/**
	 * 增加数据
	 * 
	 * @param key 待增加的数据的key
	 * @param data 待增加的数据的值
	 * @return 解析结果输出数据实例
	 */
	public CrawlerData addData(String key, Object data) {
		this.data.put(key, data);
		return this;
	}

	/**
	 * 清空数据
	 * 
	 * @return 解析结果输出数据实例
	 */
	public CrawlerData clear() {
		this.data.clear();
		return this;
	}
	
	/**
	 * 获取所有的数据
	 * @return 所有的数据
	 */
	public Map<String, Object> getAllData(){
		return this.data;
	}

	public CrawlerData(Request request) {
		this.request = request;
	}

}
