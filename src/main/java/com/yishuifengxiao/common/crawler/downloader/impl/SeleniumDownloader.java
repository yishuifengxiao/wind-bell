package com.yishuifengxiao.common.crawler.downloader.impl;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import com.yishuifengxiao.common.crawler.domain.constant.CrawlerConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.downloader.BaseDownloader;
import com.yishuifengxiao.common.tool.exception.CustomException;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于Firefox的下载器 <br/>
 * 使用selenium-java实现
 * 
 * @author yishui
 * @version 1.0.0
 */
@Slf4j
public class SeleniumDownloader extends BaseDownloader {

	/**
	 * 是否已经执行过
	 */
	private boolean hasDone = false;
	/**
	 * 等待一段时间，以便在下载需要前端渲染的网站时让浏览器有时间完成前端渲染，单位为毫秒
	 */
	private long waitRender;

	/**
	 * 构造函数<br/>
	 * 必须传入浏览器驱动文件geckodriver所在地址的路径<br/>
	 * geckodriver文件的下载路径为 https://github.com/mozilla/geckodriver/releases<br/>
	 * 请根据运行环境的信息配置好此参数
	 * 
	 * @param driverPath 浏览器驱动文件geckodriver的地址
	 * @param waitRender 等待一段时间，以便在下载需要前端渲染的网站时让浏览器有时间完成前端渲染，单位为毫秒，如果此值不大于0表示不开启此功能
	 * @throws CustomException 创建浏览器对象时出现的问题
	 * 
	 */
	public SeleniumDownloader(String driverPath, long waitRender) throws CustomException {
		super(driverPath);
		this.waitRender = waitRender;
	}

	@Override
	public Page down(WebDriver driver, final Request request) throws CustomException {
		Page page = new Page(request);
		try {
			driver.get(request.getUrl());
			// 等待一段时间
			this.waitRender();
			// 设置真实的请求地址
			page.setRedirectUrl(driver.getCurrentUrl());
			page.setCode(CrawlerConstant.SC_OK);
			page.setRawTxt(driver.getPageSource());
		} catch (Exception e) {
			log.info("An error occurred while downloading the page {}, the problem is {}", request, e.getMessage());
			page.setCode(CrawlerConstant.SC_INTERNAL_SERVER_ERROR);
			page.setRawTxt(e.getMessage());
			throw new CustomException(e.getMessage());
		}

		return page;

	}

	/**
	 * 休眠一段时间，以便完成渲染
	 * 
	 * 
	 */
	private void waitRender() {
		if (this.waitRender > 0) {
			try {
				Thread.sleep(this.waitRender);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	protected void preHandle(Request request, final WebDriver driver) {
		if (!hasDone) {
			if (request.getConnectTimeout() > 0) {
				// 识别对象时的超时时间。过了这个时间如果对象还没找到的话就会抛出NoSuchElement异常。单位毫秒。
				driver.manage().timeouts().implicitlyWait(request.getConnectTimeout(), TimeUnit.MILLISECONDS);
			}

			request.getCookies().forEach((k, v) -> {
				driver.manage().addCookie(new Cookie(k, v));
			});
			hasDone = true;
		}

	}

}
