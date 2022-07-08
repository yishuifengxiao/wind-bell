package com.yishuifengxiao.common.crawler.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 风铃虫页面对象
 * 
 * @author yishui
 * @version 1.0.0
 */
public class Page implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2146316828212720052L;
	/**
	 * 该页面里所有提取出来的数据
	 */
	private Map<String, Object> outData = new WeakHashMap<>();
	/**
	 * 响应码
	 */
	private int code;

	/**
	 * 对应页面的请求任务
	 */
	private Request request;
	/**
	 * 具备重定向功能的下载器在请求时重定向之后的地址
	 */
	private String redirectUrl;
	/**
	 * 对应的页面的原始文本
	 */
	private String rawTxt;
	/**
	 * 该页面里所有的超链接
	 */
	private List<String> links;
	/**
	 * 是否跳过该页面的解析
	 */
	private boolean isSkip = false;

	/**
	 * 获取响应码
	 * 
	 * @return 响应码
	 */
	public int getCode() {
		return code;
	}

	/**
	 * 设置响应码
	 * 
	 * @param code 响应码
	 * @return 风铃虫页面对象
	 */
	public Page setCode(int code) {
		this.code = code;
		return this;
	}

	/**
	 * 获取对应的页面的原始文本
	 * 
	 * @return 对应的页面的原始文本
	 */
	public String getRawTxt() {
		return rawTxt;
	}

	/**
	 * 
	 * @param rawTxt
	 * @return 风铃虫页面对象
	 */
	public Page setRawTxt(String rawTxt) {
		this.rawTxt = rawTxt;
		return this;
	}

	/**
	 * 获取该页面里所有的超链接
	 * 
	 * @return 该页面里所有的超链接
	 */
	public List<String> getLinks() {
		return links;
	}

	/**
	 * 获取是否跳过该页面的解析
	 * 
	 * @return 跳过该页面的解析返回为true，否则为false
	 */
	public boolean isSkip() {
		return isSkip;
	}

	/**
	 * 
	 * @param isSkip
	 * @return 风铃虫页面对象
	 */
	public Page setSkip(boolean isSkip) {
		this.isSkip = isSkip;
		return this;
	}

	/**
	 * 设置链接地址 <br/>
	 * 会替换原来的链接地址集合
	 * 
	 * @param links
	 * @return 页面对象
	 */
	public Page setLinks(List<String> links) {
		Assert.notNull(links, "目标链接集合不能为空");
		this.links = links;
		return this;
	}

	/**
	 * 在原来的链接地址集合里增加新的链接信息
	 * 
	 * @param links
	 * @return 页面对象
	 */
	public Page addLinks(List<String> links) {
		Assert.notNull(links, "目标链接集合不能为空");
		if (this.links == null) {
			this.links = new ArrayList<>();
		}
		this.links.addAll(links.stream().filter(t -> StringUtils.isNotBlank(t)).collect(Collectors.toSet()));
		return this;
	}

	/**
	 * 清空链接地址
	 * 
	 * @return 页面对象
	 */
	public Page clearLinks() {
		this.links = new ArrayList<>();
		return this;
	}

	/**
	 * 设置输出数据<br/>
	 * 会替换原始的输出输出
	 * 
	 * @param data
	 * @return 页面对象
	 */
	public Page setData(Map<String, Object> data) {
		Assert.notNull(data, "设置的数据不能为空");
		this.outData.clear();
		this.addData(data);
		return this;
	}

	/**
	 * 增加输出数据
	 * 
	 * @param data 待保存的数据
	 * @return 页面对象
	 */
	public Page addData(Map<String, Object> data) {
		Assert.notNull(data, "设置的数据不能为空");
		this.outData.putAll(data);
		return this;
	}

	/**
	 * 获取具备重定向功能的下载器在请求时重定向之后的地址
	 * 
	 * @return 在请求时重定向之后的地址
	 */
	public String getRedirectUrl() {
		return redirectUrl;
	}

	/**
	 * 设置 具备重定向功能的下载器在请求时重定向之后的地址
	 * 
	 * @param redirectUrl
	 */
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	/**
	 * 增加输出数据
	 * 
	 * @param key   输出数据的key
	 * @param value 输出数据的值
	 * @return 页面对象
	 */
	public Page addData(String key, Object value) {
		Assert.notNull(key, "输出结果的键值不能为空");
		this.outData.put(key, value);
		return this;
	}

	/**
	 * 根据键获取对应的解析出来的值
	 * 
	 * @param key 键值
	 * @return 对应的解析出来的值
	 */
	public Object getData(String key) {
		Assert.notNull(key, "输出结果的键值不能为空");
		return this.outData.get(key);
	}

	/**
	 * 该页面里所有提取出来的数据是否包含对应的键
	 * 
	 * @param key 键值
	 * @return 包含返回为true,否则为false
	 */
	public boolean contain(String key) {
		Assert.notNull(key, "输出结果的键值不能为空");
		return this.outData.containsKey(key);
	}

	/**
	 * 获取该页面里所有提取出来的数据
	 * 
	 * @return 该页面里所有提取出来的数据
	 */
	public Map<String, Object> getData() {
		return this.outData;
	}

	public Page(Request request) {
		this.request = request;
	}

	public Page() {

	}

	/**
	 * 获取对应页面的请求任务
	 * 
	 * @return 对应页面的请求任务
	 */
	public Request getRequest() {
		return request;
	}

	/**
	 * 设置对应页面的请求任务
	 * 
	 * @param request 对应页面的请求任务
	 * @return 页面对象
	 */
	public Page setRequest(Request request) {
		this.request = request;
		return this;
	}

}
