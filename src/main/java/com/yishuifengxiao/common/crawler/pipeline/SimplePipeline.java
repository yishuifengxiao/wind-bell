package com.yishuifengxiao.common.crawler.pipeline;

import com.yishuifengxiao.common.crawler.domain.entity.ResultData;

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

	@Override
	public void recieve(ResultData resultData) {

		log.debug("\r\n");
		log.debug("The output is ");
		log.info("request : {} , out data : {}", resultData.getUrl(), resultData.getAllData());
		log.debug("\r\n");
	}

}
