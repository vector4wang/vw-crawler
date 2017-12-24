package com.vw.crawler.test;

import com.vw.crawler.VWCrawler;
import com.vw.crawler.proxy.ProxyBuilder;
import com.vw.crawler.service.CrawlerService;
import org.jsoup.nodes.Document;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

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
                .setPageParser(new CrawlerService<Company>() {
                    @Override
                    public void parsePage(Document doc, Company pageObj) {
                        pageObj.setCurrentUrl(doc.location());
//                        System.out.println(pageObj.toString());
                    }
                })
//                .setProxys(new ProxyBuilder() {
//                    @Override
//                    public List<Proxy2> extractProxyIp() {
//                        List<Proxy2> proxies = new ArrayList<>();
//                        proxies.add(new Proxy2("58.57.75.142",63000));
//                        return proxies;
//                    }
//                }, ProxyBuilder.Type.RANDOM)
                .setTimeOut(10000)
                .build().start();
    }
}
