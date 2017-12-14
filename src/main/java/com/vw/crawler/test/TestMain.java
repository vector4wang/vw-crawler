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
                .setUrl("http://www.chinahr.com/job/577b4dde6f201a0fc64d368ch.html?searchplace=292")
                .setParamMap(null)
                .setPageParser(new CrawlerService<Company>() {
                    @Override
                    public boolean isExist(String url) {
                        System.out.println("重写isExist");
                        return false;
                    }
                    @Override
                    public boolean isConinue(Document document) {
                        System.out.println("重写isConinue");
                        return true;
                    }

                    @Override
                    public void parsePage(Document doc, Company pageObj) {
                        System.out.println(pageObj.toString());
                    }
                })
                .build().start();
    }
}
