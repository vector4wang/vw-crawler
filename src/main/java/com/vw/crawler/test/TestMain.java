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
                .setUrl("http://www.chinahr.com/sou/")
                .setParamMap(null)
                .setPageParser(new CrawlerService<Company>() {

                    public boolean isExist(String url) {
                        System.out.println("重写isExist");
                        return false;
                    }

                    public boolean isConinue(Document document) {
                        System.out.println("重写isConinue");
                        return true;
                    }

                    public void parsePage(Document doc, Company pageObj) {
                        String pageUrl = doc.baseUri();
                        System.out.println(pageUrl + "：" + pageObj.toString());
                    }
                })
                .build().start();
    }
}
