package com.yishuifengxiao.common.crawler.pipeline;

import com.yishuifengxiao.common.crawler.domain.entity.CrawlerData;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认实现的信息输出器<br/>
 * 输出信息到日志
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
@Slf4j
public class SimplePipeline implements Pipeline {

	private final static String SEPARATOR = System.getProperty("line.separator");

	@Override
	public void recieve(CrawlerData crawlerData) {

		log.debug(SEPARATOR);
		log.info("{} request : {} , out data : {} {}", SEPARATOR, crawlerData.getRequest(), crawlerData.getData(),
				SEPARATOR);

	}

}
