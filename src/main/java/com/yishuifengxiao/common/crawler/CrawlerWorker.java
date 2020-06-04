package com.yishuifengxiao.common.crawler;

import com.yishuifengxiao.common.crawler.content.ContentExtract;
import com.yishuifengxiao.common.crawler.content.ContentExtractDecorator;
import com.yishuifengxiao.common.crawler.domain.entity.CrawlerData;
import com.yishuifengxiao.common.crawler.domain.entity.Page;
import com.yishuifengxiao.common.crawler.domain.entity.Request;
import com.yishuifengxiao.common.crawler.downloader.Downloader;
import com.yishuifengxiao.common.crawler.link.LinkExtract;
import com.yishuifengxiao.common.crawler.link.LinkExtractDecorator;
import com.yishuifengxiao.common.crawler.utils.LocalCrawler;
import com.yishuifengxiao.common.crawler.utils.RegexFactory;
import com.yishuifengxiao.common.tool.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 风铃虫处理核心
 * 
 * @author yishui
 * @date 2020年6月2日
 * @version 1.0.0
 */
@Slf4j
public class CrawlerWorker implements Runnable {
	/**
	 * 当前任务处理器
	 */
	private final CrawlerProcessor crawlerProcessor;
	/**
	 * 当前需要处理的请求
	 */
	private final Request request;

	/**
	 * 链接提取器，负责从内容中解析处理符合要求的链接
	 */
	private final LinkExtract linkExtract;
	/**
	 * 内容解析器，负责从内容中解析出需要提取的内容
	 */
	private final ContentExtract contentExtract;
	/**
	 * 下载器
	 */
	private final Downloader downloader;

	public CrawlerWorker(final Request request, final Downloader downloader, final CrawlerProcessor crawlerProcessor) {
		this.crawlerProcessor = crawlerProcessor;
		this.request = request;
		this.downloader = downloader;
		// 生成链接解析器
		this.linkExtract = new LinkExtractDecorator(crawlerProcessor.crawler.linkExtract);
		// 生成内容解析器
		this.contentExtract = new ContentExtractDecorator(crawlerProcessor.crawler.contentExtract);
	}

	@Override
	public void run() {
		Page page = null;
		try {
			LocalCrawler.put(this.crawlerProcessor.crawler);

			// 处理请求
			// 下载下载后的page信息里包含request信息
			page = this.downloader.down(this.request);
			if (page == null) {
				throw new ServiceException(new StringBuffer("Web page (").append(this.request.getUrl())
						.append(" ) download results are empty").toString());
			}

			// 补全URL信息
			page.setRequest(this.request);

			log.debug(
					"【id:{} , name:{} 】   The request {}  has been downloaded.The real path is {} ,and  downloaded content is {}",
					this.crawlerProcessor.crawler.getUuid(), this.crawlerProcessor.crawler.getName(), request.getUrl(),
					page.getRedirectUrl(), page);

			// 下载成功
			this.crawlerProcessor.crawler.crawlerListener.onDownSuccess(this.crawlerProcessor.crawler, page);

			// 服务器封杀检查
			if (!this.intercepCheck(page)) {

				// 资源处理过程真正的资源
				this.processRequest(page);

			}

		} catch (Exception e) {
			// 下载失败
			this.crawlerProcessor.crawler.crawlerListener.onDownError(this.crawlerProcessor.crawler, page, e);

			log.info(
					"【id:{} , name:{} 】  There was a problem requesting {} to download. The cause of the problem is {}",
					this.crawlerProcessor.crawler.getUuid(), this.crawlerProcessor.crawler.getName(), this.request, e);
		} finally {
			LocalCrawler.clear();
			crawlerProcessor.signalNewUrl();
		}
	}

	/**
	 * 服务器封杀检查
	 * 
	 * @param page 下载的网页
	 * @return true表示检测到封杀标志,false表示为检测到
	 */
	private boolean intercepCheck(Page page) {
		boolean match = false;
		if (this.crawlerProcessor.crawler.getCrawlerRule().getSite().statCheck()) {
			// 开启失败校验时才启用
			match = RegexFactory.find(this.crawlerProcessor.crawler.getCrawlerRule().getSite().getFailureMark(),
					page.getRawTxt());
			if (match) {
				long interceptCount = this.crawlerProcessor.incrementAndGet();
				log.debug("【id:{} , name:{} 】  The crawler was detected by the server for the {} time",
						this.crawlerProcessor.crawler.getUuid(), this.crawlerProcessor.crawler.getName(),
						interceptCount);

			} else {
				// 重置
				this.crawlerProcessor.clearInterceptCount();
			}
		}
		return match;
	}

	/**
	 * 资源处理
	 * 
	 * @param page 下载后的网页资源
	 */
	private void processRequest(final Page page) {
		try {
			// 进行真正的网页内容解析操作
			// 解析链接数据
			linkExtract.extract(this.crawlerProcessor.crawler.getCrawlerRule().getLink(), page);
			
			// 存储链接
			page.getLinks().stream()
					.map(t -> new Request(t, page.getRequest().getUrl(), page.getRequest().getDepth() + 1L))
					.forEach(t -> this.crawlerProcessor.crawler.scheduler.push(this.crawlerProcessor.crawler, t));

			// 抽取内容
			contentExtract.extract(this.crawlerProcessor.crawler.getCrawlerRule().getContent(),
					this.crawlerProcessor.crawler.getCrawlerRule().getRules(), page);

			// 输出数据
			this.output(page);
			// 解析成功任务数
			this.crawlerProcessor.incrementTaskCount();
			// 解析成功消息
			this.crawlerProcessor.crawler.crawlerListener.onExtractSuccess(this.crawlerProcessor.crawler, page);
		} catch (Exception e) {
			// 解析失败的任务数量
			this.crawlerProcessor.incrementFailCount();
			// 解析失败
			this.crawlerProcessor.crawler.crawlerListener.onExtractError(this.crawlerProcessor.crawler, page, e);
			log.info("【id:{} , name:{} 】  There was a problem requesting {} to extract. The cause of the problem is {}",
					this.crawlerProcessor.crawler.getUuid(), this.crawlerProcessor.crawler.getName(), this.request, e);
		} finally {
			crawlerProcessor.signalNewUrl();
		}
	}

	/**
	 * 输出解析后的数据
	 * 
	 * @param page 解析后的数据信息
	 * @throws ServiceException
	 */
	private void output(final Page page) throws ServiceException {

		// 输出数据
		if (!page.isSkip()) {
			// 输出数据
			CrawlerData resultData = new CrawlerData().setRawTxt(page.getRawTxt()).setData(page.getData())
					.setRequest(page.getRequest()).setRedirectUrl(page.getRedirectUrl())
					.setTask(this.crawlerProcessor.crawler);
			this.crawlerProcessor.crawler.pipeline.recieve(resultData);
		}
	}

}
