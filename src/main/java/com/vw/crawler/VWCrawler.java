package com.vw.crawler;

import com.vw.crawler.service.CrawlerService;
import com.vw.crawler.test.Company;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/12
 * Time: 19:35
 * Description:
 */
public class VWCrawler {

    private Logger logger = Logger.getLogger("VWCrawler");

    private String url;
    private int timeout = 2000;

    private Map<String, String> paramMap;

    private CrawlerService crawlerService;

    public static class Builder {
        private VWCrawler crawler = new VWCrawler();

        public Builder setUrl(String url) {
            crawler.url = url;
            return this;
        }

        public Builder setTimeOut(int timeOut) {
            crawler.timeout = timeOut;
            return this;
        }

        public Builder setParamMap(Map<String, String> paramMap) {
            if (crawler.url == null) {
                throw new RuntimeException("请先设置URL");
            }
            crawler.paramMap = paramMap;
            return this;
        }

        public Builder setPageParser(CrawlerService crawlerService) {
            crawler.crawlerService = crawlerService;
            return this;
        }

        public VWCrawler build() {
            return crawler;
        }
    }

    public void start() {
        logger.info("爬虫启动...");
        if (url == null) {
            throw new RuntimeException("抓取地址不能为空.");
        }
        process(url);

    }

    private void process(String url) {

        if (crawlerService.isExist(url)) {
            return;
        }

        try {
            Document document = null;
            do {
                document = Jsoup.connect(url).timeout(timeout).get();
            } while (!crawlerService.isConinue(document));


            crawlerService.parsePage(document, new Company());
        } catch (Exception e) {
            if (e instanceof IOException) {
                logger.info("请求地址发生错误");
            } else {
                e.printStackTrace();
            }
        }
    }
}
