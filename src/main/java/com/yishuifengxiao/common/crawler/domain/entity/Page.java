package com.yishuifengxiao.common.crawler.domain.entity;

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
 * @date 2019年11月26日
 * @version 1.0.0
 */
public class Page {
	/**
	 * 风铃虫下载页面是的响应码
	 */
	private int code;

	/**
	 * 对应页面的请求信息
	 */
	private String url;
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
	 * 该页面里所有提取出来的数据
	 */
	private Map<String, Object> outData = new WeakHashMap<>();
	/**
	 * 是否跳过该页面的解析
	 */
	private boolean isSkip;

	public int getCode() {
		return code;
	}

	public Page setCode(int code) {
		this.code = code;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRawTxt() {
		return rawTxt;
	}

	public Page setRawTxt(String rawTxt) {
		this.rawTxt = rawTxt;
		return this;
	}

	public List<String> getLinks() {
		return links;
	}

	public boolean isSkip() {
		return isSkip;
	}

	public Page setSkip(boolean isSkip) {
		this.isSkip = isSkip;
		return this;
	}

	/**
	 * 设置链接地址 <br/>
	 * 会替换原来的链接地址集合
	 * 
	 * @param links
	 * @return
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
	 * @return
	 */
	public Page addLinks(List<String> links) {
		Assert.notNull(links, "目标链接集合不能为空");
		if (this.links == null) {
			this.links = new ArrayList<>();
		}
		this.links.addAll(links.parallelStream().filter(t -> StringUtils.isNotBlank(t)).collect(Collectors.toSet()));
		return this;
	}

	/**
	 * 清空链接地址
	 * 
	 * @return
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
	 * @return
	 */
	public Page setResultItem(Map<String, Object> data) {
		Assert.notNull(data, "设置的数据不能为空");
		this.outData.clear();
		this.addResultItem(data);
		return this;
	}

	/**
	 * 增加输出数据
	 * 
	 * @param data
	 * @return
	 */
	public Page addResultItem(Map<String, Object> data) {
		Assert.notNull(data, "设置的数据不能为空");
		this.outData.putAll(data);
		return this;
	}

	/**
	 * 获取具备重定向功能的下载器在请求时重定向之后的地址
	 * 
	 * @return
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
	 * @param key
	 * @param value
	 * @return
	 */
	public Page addResultItem(String key, Object value) {
		Assert.notNull(key, "输出结果的键值不能为空");
		this.outData.put(key, value);
		return this;
	}

	public Object getResultItem(String key) {
		Assert.notNull(key, "输出结果的键值不能为空");
		return this.outData.get(key);
	}

	public boolean containResultItem(String key) {
		Assert.notNull(key, "输出结果的键值不能为空");
		return this.outData.containsKey(key);
	}

	public Map<String, Object> getAllResultItem() {
		return this.outData;
	}

	public Page(String url) {
		this.url = url;
	}

	public Page() {

	}

}
