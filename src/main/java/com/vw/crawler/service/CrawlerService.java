package com.vw.crawler.service;

import org.jsoup.nodes.Document;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/12
 * Time: 19:33
 * Description:
 */
public abstract class CrawlerService<T> {

    /**
     * 开始抓数据
     */
    public abstract  void run();

    /**
     * 待抓取的数据，是否已经存在
     * @param url
     * @return
     */
    public abstract boolean isExist(String url);

    /**
     * 当前待抓取的页面是否遇到WAP，账号是否被封
     * @return
     */
    public abstract boolean isConinue(Document document);

    /**
     *
     * @param doc
     * @param crawlUrl
     * @param pageObj
     * @param <T>
     */
    public abstract void parsePage(Document doc, String crawlUrl, T pageObj);
}
