package com.yishuifengxiao.common.crawler.downloader;

import java.io.File;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.util.Assert;

import com.yishuifengxiao.common.crawler.domain.constant.SiteConstant;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.model.SiteRule;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * selenium下载器基类<br/>
 * 所有基于selenium的下载器最好根据此基类完成
 * 
 * @author yishui
 * @date 2019年12月25日
 * @version 1.0.0
 */
@Slf4j
public abstract class BaseDownloader implements Downloader {
	/**
	 * 使用firefox时需要设置的系统环境变量的常量名
	 */
	private final static String GECKO_DRIVER = "webdriver.gecko.driver";

	/**
	 * Web浏览器对象
	 */
	private WebDriver driver;

	/**
	 * 正式下载前的前置操作<br/>
	 * 可以在此操作中修改 Web浏览器对象，进行属性设置
	 * 
	 * @param siteRule 站点规则信息
	 * @param driver   Web浏览器对象
	 * @param siteRule 站点规则信息
	 */
	protected abstract void preHandle(final SiteRule siteRule, final WebDriver driver);

	/**
	 * 执行真正的下载操作
	 * 
	 * @param driver Web浏览器对象
	 * @param url
	 * @return
	 * @throws ServiceException
	 */
	protected abstract Page down(WebDriver driver, String url) throws ServiceException;

	@Override
	public Page down(final SiteRule siteRule, final String url) throws ServiceException {

		this.initData(SiteConstant.IMPLICITLY_WAIT_MILLIS, SiteConstant.SCRIPT_TIME_OUT_MILLIS,
				SiteConstant.PAGE_LOAD_SCRIPT_TIME_OUT_MILLIS);
		// 进行前置操作
		this.preHandle(siteRule, this.driver);

		return this.down(this.driver, url);
	}

	@Override
	public void close() {

		if (null != this.driver) {
			this.driver.close();
		}

	}

	/**
	 * 根据参数构建一个FirefoxDriver
	 * 
	 * @param implicitlyWaitMillis  识别对象时的超时时间。过了这个时间如果对象还没找到的话就会抛出NoSuchElement异常。单位毫秒。
	 * @param scriptTimeoutMillis   异步脚本的超时时间。WebDriver可以异步执行脚本，这个是设置异步执行脚本脚本返回结果的超时时间。单位毫秒。
	 * @param pageLoadTimeoutMillis 页面加载时的超时时间。因为WebDriver会等页面加载完毕再进行后面的操作，所以如果页面超过设置时间依然没有加载完成，那么WebDriver就会抛出异常。单位毫秒。
	 * @return
	 */
	private void initData(long implicitlyWaitMillis, long scriptTimeoutMillis, long pageLoadTimeoutMillis) {

		// 识别对象时的超时时间。过了这个时间如果对象还没找到的话就会抛出NoSuchElement异常。
		this.driver.manage().timeouts().implicitlyWait(implicitlyWaitMillis, TimeUnit.MILLISECONDS);
		// 异步脚本的超时时间。WebDriver可以异步执行脚本，这个是设置异步执行脚本脚本返回结果的超时时间。
		this.driver.manage().timeouts().setScriptTimeout(scriptTimeoutMillis, TimeUnit.MILLISECONDS);
		// 页面加载时的超时时间。因为WebDriver会等页面加载完毕再进行后面的操作，所以如果页面超过设置时间依然没有加载完成，那么WebDriver就会抛出异常。
		this.driver.manage().timeouts().pageLoadTimeout(pageLoadTimeoutMillis, TimeUnit.MILLISECONDS);

	}

	/**
	 * 构造函数<br/>
	 * 必须传入浏览器驱动文件geckodriver所在地址的路径<br/>
	 * geckodriver文件的下载路径为 https://github.com/mozilla/geckodriver/releases<br/>
	 * 请根据运行环境的信息配置好此参数
	 * 
	 * @param driverPath 浏览器驱动文件geckodriver的地址
	 * @throws ServiceException 创建浏览器对象时出现的问题
	 * 
	 */
	public BaseDownloader(String driverPath) throws ServiceException {
		Assert.notNull(driverPath, "Web浏览器驱动不能为空");
		if (!new File(driverPath).exists()) {
			throw new ServiceException("Web浏览器驱动文件不存在");
		}
		// 初始化浏览器对象
		this.initDriver(driverPath);
	}

	/**
	 * 初始化浏览器对象
	 * 
	 * @param driverPath 浏览器驱动文件geckodriver的地址
	 * @throws ServiceException
	 */
	private void initDriver(String driverPath) throws ServiceException {
		log.debug("===》 Web浏览器对象的驱动的路径为 {}", driverPath);
		System.setProperty(GECKO_DRIVER, driverPath);
		FirefoxOptions options = new FirefoxOptions();
		options.setHeadless(true);
		options.setAcceptInsecureCerts(true);
		try {
			this.driver = new FirefoxDriver(options);
		} catch (Exception e) {
			log.info("根据路径 {} 构建浏览器对象时出现问题，出现问题的原因为 {}", driverPath, e.getMessage());
			throw new ServiceException(
					MessageFormat.format("根据路径 {0} 构建浏览器对象时出现问题，出现问题的原因为 {1}", driverPath, e.getMessage()));
		}
	}

}
