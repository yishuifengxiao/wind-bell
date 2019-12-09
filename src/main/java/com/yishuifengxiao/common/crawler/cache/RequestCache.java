package com.yishuifengxiao.common.crawler.cache;

/**
 * 资源缓存器<br/>
 * 功能如下：<br/>
 * 1 查询此资源是否存在指定的的集合中<br/>
 * 
 * @author yishui
 * @date 2019年11月26日
 * @version 1.0.0
 */
public interface RequestCache {
	/**
	 * 将数据存储到指定的集合名中
	 * 
	 * @param cacheName 缓存集合的名字
	 * @param value     需要存储的值
	 */
	void save(String cacheName, String value);

	/**
	 * 先查找指定的值是否在集合中存在,然后将该值存储到此集合中
	 * 
	 * @param cacheName 缓存集合的名字
	 * @param value     需要存储的值
	 * @return 如果存在则返回为true，否则为fasle
	 */
	boolean lookAndCache(String cacheName, String value);

	/**
	 * 先查找指定的值是否在集合中存在
	 * 
	 * @param cacheName 缓存集合的名字
	 * @param value     需要存储的值
	 * @return 如果存在则返回为true，否则为fasle
	 */
	boolean exist(String cacheName, String value);
    /**
     * 移除指定的缓存集合
     * @param cacheName 缓存集合的名字
     */
	void remove(String cacheName);
    /**
     * 获取指定缓存集合的成员数量
     * @param cacheName 缓存集合的名字
     * @return
     */
	long getCount(String cacheName);
}
