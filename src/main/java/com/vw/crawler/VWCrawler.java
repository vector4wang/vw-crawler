package com.vw.crawler;

import com.vw.crawler.annotation.CssSelector;
import com.vw.crawler.service.CrawlerService;
import com.vw.crawler.util.SelectType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }

    public CrawlerService getCrawlerService() {
        return crawlerService;
    }

    public void setCrawlerService(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
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

            if (document != null) {
                Type[] type = ((ParameterizedType) crawlerService.getClass().getGenericSuperclass()).getActualTypeArguments();
                Class aClass = (Class) type[0];
                Object pageVo = aClass.newInstance();

                /**
                 * 解析页面，封装obj
                 */
                Field[] declaredFields = pageVo.getClass().getDeclaredFields();
                if (declaredFields != null) {
                    for (Field declaredField : declaredFields) {
                        CssSelector annotation = declaredField.getAnnotation(CssSelector.class);
                        String selector = annotation.selector();
                        SelectType selectType = annotation.resultType();
                        if (selector == null || selector.length() <= 0) {
                            continue;
                        }
                        String result = "";

                        if (selectType == SelectType.HTML) {
                            result = document.select(selector).html();
                        }else{
                            result = document.select(selector).text();
                        }
                        declaredField.setAccessible(true);
                        declaredField.set(pageVo, result);
                    }
                }
                crawlerService.parsePage(document,pageVo);
            }
        } catch (Exception e) {
            if (e instanceof IOException) {
                logger.info("请求地址发生错误");
            } else {
                e.printStackTrace();
            }
        }
    }
}
