package com.github.vector4wang.util;

import com.github.vector4wang.proxy.ProxyBuilder;

import java.net.Proxy;
import java.util.List;
import java.util.Random;

/**
 * @author vector
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
