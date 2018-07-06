package com.github.vector4wang.util;

import com.github.vector4wang.proxy.ProxyBuilder;

import java.net.Proxy;
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
}
