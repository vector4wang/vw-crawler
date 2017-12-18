package com.vw.crawler;

import com.vw.crawler.annotation.CssSelector;
import com.vw.crawler.service.CrawlerService;
import com.vw.crawler.util.CrawlerUtil;
import com.vw.crawler.util.SelectType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
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

    private LinkedBlockingQueue<String> waitCrawlerUrls = new LinkedBlockingQueue<String>();

    private Set<String> crawledUrls = new HashSet<String>();

    private Set<String> seedUrlRex = new HashSet<String>();

    private Set<String> seedsPageUrlRex = new HashSet<String>();

    private CrawlerService crawlerService;

    public static class Builder {
        private VWCrawler crawler = new VWCrawler();

        public Builder setUrl(String url) {
            crawler.waitCrawlerUrls.add(url);
            return this;
        }

        public Builder setTimeOut(int timeOut) {
            crawler.timeout = timeOut;
            return this;
        }

        public Builder setSeedUrl(String... targetUrl) {
            if (targetUrl != null && targetUrl.length > 0) {
                for (String rex : targetUrl) {
                    crawler.seedUrlRex.add(rex);
                }
            }
            return this;
        }

        public Builder setSeedsPage(String... seedsPage) {
            if (seedsPage != null && seedsPage.length > 0) {
                for (String rex : seedsPage) {
                    crawler.seedsPageUrlRex.add(rex);
                }
            }
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

    public LinkedBlockingQueue<String> getWaitCrawlerUrls() {
        return waitCrawlerUrls;
    }

    public void setWaitCrawlerUrls(LinkedBlockingQueue<String> waitCrawlerUrls) {
        this.waitCrawlerUrls = waitCrawlerUrls;
    }

    public Set<String> getCrawledUrls() {
        return crawledUrls;
    }

    public void setCrawledUrls(Set<String> crawledUrls) {
        this.crawledUrls = crawledUrls;
    }

    public Set<String> getSeedUrlRex() {
        return seedUrlRex;
    }

    public void setSeedUrlRex(Set<String> seedUrlRex) {
        this.seedUrlRex = seedUrlRex;
    }

    public Set<String> getSeedsPageUrlRex() {
        return seedsPageUrlRex;
    }

    public void setSeedsPageUrlRex(Set<String> seedsPageUrlRex) {
        this.seedsPageUrlRex = seedsPageUrlRex;
    }

    public CrawlerService getCrawlerService() {
        return crawlerService;
    }

    public void setCrawlerService(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    public void start() {
        logger.info("爬虫启动...");
        while (waitCrawlerUrls != null && waitCrawlerUrls.size() > 0) {
            try {
                String link = waitCrawlerUrls.take();

                if (link != null && link.length() > 0) {
                    if (crawledUrls.contains(link)) {
                        continue;
                    }
                    crawledUrls.add(link);
                    process(link);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.toString());
            }
        }


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

                if (seedsPageUrlRex.size() > 0) {
                    /**
                     * 抽取满足正则的url
                     */
                    Elements links = document.select("a[href]");
                    if (links.size() > 0) {
                        for (Element link : links) {
                            String href = link.absUrl("href");
                            for (String seedsPageUrlRex : seedsPageUrlRex) {
                                if (CrawlerUtil.isMatch(seedsPageUrlRex, href)) {
                                    waitCrawlerUrls.add(href);
                                }
                            }

                        }
                        System.out.println(waitCrawlerUrls.size());
                    }

                }

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
                        if (annotation == null) {
                            continue;
                        }
                        String selector = annotation.selector();
                        SelectType selectType = annotation.resultType();
                        if (selector == null || selector.length() <= 0) {
                            continue;
                        }
                        String result = "";

                        if (selectType == SelectType.HTML) {
                            result = document.select(selector).html();
                        } else {
                            result = document.select(selector).text();
                        }
                        declaredField.setAccessible(true);
                        declaredField.set(pageVo, result);
                    }
                }
                crawlerService.parsePage(document, pageVo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e instanceof IOException) {
                logger.info("请求地址发生错误");
            } else {
                e.printStackTrace();
            }
        }
    }
}
