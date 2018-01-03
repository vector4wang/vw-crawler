package com.vw.crawler.util;

import com.vw.crawler.model.PageRequest;
import com.vw.crawler.proxy.ProxyBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Random;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
public class JsoupUtil {

    public static Random random = new Random();

    public static Document htmlDoc(PageRequest requestBody) throws IOException {
        if (requestBody == null) {
            throw new RuntimeException("请设置PageRequest");
        }

        if (requestBody.getUrl() == null && requestBody.getUrl().length() <= 0) {
            throw new RuntimeException("PageRequest中的URL不合法");
        }
        Connection connect = Jsoup.connect(requestBody.getUrl()).timeout(requestBody.getTimeout());
        if (requestBody.getProxy() != null) {
            connect.proxy(requestBody.getProxy());
        }
        Document document = connect.execute().parse();
        if (document == null) {
            throw new RuntimeException("页面请求失败，请检查网络、RequestBody或者其他配置参数是否有误");
        }
        return document;
    }

    public static String htmlString(PageRequest requestBody) throws IOException {
        return htmlDoc(requestBody).toString();
    }


    public static Proxy getProxy(List<Proxy> proxyList, ProxyBuilder.Type type) {
        if (type == ProxyBuilder.Type.RANDOM) {
            if (proxyList.size() == 1) {
                return proxyList.get(0);
            }else {
                return proxyList.get(random.nextInt(proxyList.size()));
            }
        }
        return null;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            PageRequest requestBody = new PageRequest();
            requestBody.setTimeout(10000);
            requestBody.setUrl("http://blog.csdn.net/qq_29384639/article/details/54617747");
            requestBody.setProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("58.57.75.142",63000)));
            try {
                Document document = htmlDoc(requestBody);

                System.out.println(document.body().text());
            } catch (SocketTimeoutException socketTimeoutException) {
                System.out.println("代理失效");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
