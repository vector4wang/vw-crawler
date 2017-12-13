package com.vw.crawler.service;

import org.jsoup.nodes.Document;

import java.util.Set;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/12
 * Time: 19:33
 * Description:
 */
public abstract class CrawlerService<T> {




    /**
     * 待抓取的url，是否已经抓取过
     *
     * @param url
     * @return
     */
    public abstract boolean isExist(String url);

    /**
     * 当前待抓取的页面是否遇到WAP，账号是否被封
     *
     * @return
     */
    public abstract boolean isConinue(Document document);

    /**
     * @param doc
     * @param pageObj
     * @param <T>
     */
    public abstract void parsePage(Document doc, T pageObj);
}
