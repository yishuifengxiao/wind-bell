package com.yishuifengxiao.common.crawler.downloader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;

import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.http.HttpClient;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于HTTP实现的网页下载器
 * 
 * @author yishui
 * @date 2019年11月28日
 * @version 1.0.0
 */
@Slf4j
public class HttpClientDownloader implements Downloader {

	private SiteRule siteRule;

	private HttpClient httpClient;

	@Override
	public Page down(String url) throws ServiceException {

		Page page = new Page(url);

		CloseableHttpResponse response;
		try {
			response = execute(url);
			extract(page, response);
		} catch (Exception e) {
			page.setCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			page.setSkip(true);
			page.setRawTxt(e.getMessage());
		}

		return page;
	}

	private Page extract(Page page, CloseableHttpResponse response) throws UnsupportedOperationException, IOException {

		// 获取到请求的原始字符数据
		String rawTxt = getContent(response.getEntity());
		page.setCode(response.getStatusLine().getStatusCode());
		page.setRawTxt(rawTxt);
		return null;
	}

	private CloseableHttpResponse execute(String url) throws Exception {
		CloseableHttpResponse response = null;

		try {
			log.debug("Trying to download the page {}", url);
			response = httpClient.execute(url);
			log.debug("Download page successfully ,the page is {}", url);

		} catch (Exception e) {
			log.info("An error occurred while downloading the page {}, the problem is {}", url, e.getMessage());

			throw e;
		}

		return response;

	}

	private String getContent(HttpEntity httpEntity) throws UnsupportedOperationException, IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader br = null;
		try {
			InputStream is = httpEntity.getContent();
			br = new BufferedReader(new InputStreamReader(is));
			String str = null;
			while ((str = br.readLine()) != null) {
				sb.append(str);
			}
		} finally {
			if (br != null) {
				br.close();
				br = null;
			}
		}
		return sb.toString();
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public SiteRule getSiteRule() {
		return siteRule;
	}

	public void setSiteRule(SiteRule siteRule) {
		this.siteRule = siteRule;
	}

	public HttpClientDownloader(SiteRule siteRule) {
		this.siteRule = siteRule;

		this.httpClient = new HttpClient(siteRule);
	}

}
