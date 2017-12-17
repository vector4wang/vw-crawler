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
                .setSeedsPage("http://company.zhaopin.com/[0-9a-zA-Z]+.htm")
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
