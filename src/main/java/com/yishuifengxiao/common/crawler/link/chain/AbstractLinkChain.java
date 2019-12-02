package com.yishuifengxiao.common.crawler.link.chain;

/**
 * 抽象链接处理器
 * @author yishui
 * @version 1.0.0
 * @date 2019/11/20
 */
public abstract class AbstractLinkChain {

    protected AbstractLinkChain chain;

    protected String domain;
    
    /**
     * 处理链接
     * @param currentUrl
     * @param url
     * @return
     */
    public abstract String handle(String currentUrl, String url);

    public AbstractLinkChain(AbstractLinkChain chain, String domain) {
        this.chain = chain;
        this.domain = domain;
    }
}
