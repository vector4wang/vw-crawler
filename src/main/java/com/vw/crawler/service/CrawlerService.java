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
     * 默认返回false，可根据数据库存储去判断
     * @param url
     * @return
     */
    public boolean isExist(String url){
        return false;
    }

    /**
     * 当前待抓取的页面是否遇到WAP，账号是否被封
     * 默认返回true，是否需要重试链接，根据目标页面返回的值做处理
     * @return
     */
    public boolean isConinue(Document document){
        if (document == null) {
            return false;
        }
        return true;
    }

    /**
     * @param doc
     * @param pageObj
     * @param <T>
     */
    public abstract void parsePage(Document doc, T pageObj);


}
