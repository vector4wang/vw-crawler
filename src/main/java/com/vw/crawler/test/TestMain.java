package com.vw.crawler.test;

import com.vw.crawler.VWCrawler;
import com.vw.crawler.service.CrawlerService;
import org.jsoup.nodes.Document;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/13
 * Time: 15:57
 * Description:
 */
public class TestMain {
    public static void main(String[] args) {
        new VWCrawler.Builder()
                .setUrl("http://company.zhaopin.com/")
                .setSeedsPage("http://company.zhaopin.com/[0-9a-zA-Z]+.htm","http://company.zhaopin.com/[a-z]+/")
                .setSeedUrl("http://company.zhaopin.com/[0-9a-zA-Z]+.htm")
                .setPageParser(new CrawlerService<Company>() {
                    @Override
                    public void parsePage(Document doc, Company pageObj) {
                        pageObj.setCurrentUrl(doc.location());
                        System.out.println(pageObj.toString());
                    }
                })
                .setTimeOut(10000)
                .build().start();
    }
}
