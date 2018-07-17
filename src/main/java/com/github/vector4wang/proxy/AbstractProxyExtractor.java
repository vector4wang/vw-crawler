package com.github.vector4wang.proxy;

import java.util.List;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/17 0017
 * Time: 11:54
 * Description: 
 */
public abstract class AbstractProxyExtractor implements ProxyExtract {

	private List<Proxy2> proxy2s;

	public void setProxy2s(List<Proxy2> proxy2s) {
		this.proxy2s = proxy2s;
	}

	public List<Proxy2> getProxy2s() {
		return proxy2s;
	}
}
