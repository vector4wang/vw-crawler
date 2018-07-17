package com.github.vector4wang.proxy;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Random;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/17 0017
 * Time: 11:48
 * Description: 
 */
public class RandomProxy extends AbstractProxyExtractor {

	public static Random random = new Random();

	@Override
	public Proxy extractProxyIp() {
		List<Proxy2> proxy2s = getProxy2s();
		if (proxy2s == null) {
			return null;
		}
		Proxy2 proxy2;
		if (proxy2s.size() == 1) {
			proxy2 = proxy2s.get(0);
		} else {
			proxy2 = proxy2s.get(random.nextInt(proxy2s.size()));
		}
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy2.getIp(), proxy2.getPort()));
	}
}
