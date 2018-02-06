package com.vw.crawler;

import com.vw.crawler.annotation.CssSelector;
import com.vw.crawler.model.PageRequest;
import com.vw.crawler.proxy.ProxyBuilder;
import com.vw.crawler.service.CrawlerService;
import com.vw.crawler.util.CrawlerUtil;
import com.vw.crawler.util.JsoupUtil;
import com.vw.crawler.util.SelectType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/12
 * Time: 19:35
 * Description:
 */
public class VWCrawler {

    private Logger logger = Logger.getLogger(VWCrawler.class.getName());

    private String url;
    private int timeout = 2000;

    private Queue<String> waitCrawlerUrls = new LinkedList<>();

    private Set<String> crawledUrls = new HashSet<>();

    private Set<String> targetUrlRex = new HashSet<>();                     // 目标页面 正则

    private Set<String> seedsPageUrlRex = new HashSet<>();                // 存放目标页面链接的页面 正则

    private List<Proxy> proxys = new ArrayList<>();

    private ProxyBuilder.Type proxyType;

    private Proxy currentProxy;

    private Map<String, String> cookieMap;

    private Map<String, String> headerMap;

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
                    crawler.waitCrawlerUrls.add(rex);
                    crawler.targetUrlRex.add(rex);
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

//        public  Builder setHeaders()

        public Builder setProxys(ProxyBuilder proxys, ProxyBuilder.Type random) {
            if (proxys != null) {
                if (proxys.extractProxyIp().size() > 0) {
                    List<Proxy> list = new ArrayList<>();
                    for (ProxyBuilder.Proxy2 proxy2 : proxys.extractProxyIp()) {
                        list.add(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy2.getIp(), proxy2.getPort())));
                    }
                    crawler.proxys = list;
                    crawler.proxyType = random;
                }

            }
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

    public Queue<String> getWaitCrawlerUrls() {
        return waitCrawlerUrls;
    }

    public void setWaitCrawlerUrls(Queue<String> waitCrawlerUrls) {
        this.waitCrawlerUrls = waitCrawlerUrls;
    }

    public Set<String> getCrawledUrls() {
        return crawledUrls;
    }

    public void setCrawledUrls(Set<String> crawledUrls) {
        this.crawledUrls = crawledUrls;
    }

    public Set<String> getTargetUrlRex() {
        return targetUrlRex;
    }

    public void setTargetUrlRex(Set<String> targetUrlRex) {
        this.targetUrlRex = targetUrlRex;
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

    public List<Proxy> getProxys() {
        return proxys;
    }

    public void setProxys(List<Proxy> proxys) {
        this.proxys = proxys;
    }

    public ProxyBuilder.Type getProxyType() {
        return proxyType;
    }

    public void setProxyType(ProxyBuilder.Type proxyType) {
        this.proxyType = proxyType;
    }

    public Proxy getCurrentProxy() {
        return currentProxy;
    }

    public void setCurrentProxy(Proxy currentProxy) {
        this.currentProxy = currentProxy;
    }

    public void start() {
        logger.info("爬虫启动...");
        while (waitCrawlerUrls.size() > 0) {
            try {
                String currentUrl = waitCrawlerUrls.poll();
                if (currentUrl != null && currentUrl.length() > 0) {
                    if (crawledUrls.contains(currentUrl)) {
                        continue;
                    }
                    process(currentUrl);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.info(e.toString());
            }
        }
    }



    public boolean isTargetUrl(String url) {
        for (String s : targetUrlRex) {
            if (CrawlerUtil.isMatch(s, url)) {
                return true;
            }
        }
        return false;
    }
}
