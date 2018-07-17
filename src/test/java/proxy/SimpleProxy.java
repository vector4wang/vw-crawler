package proxy;

import com.github.vector4wang.proxy.AbstractProxyExtractor;
import com.github.vector4wang.proxy.Proxy2;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/17 0017
 * Time: 13:33
 * Description: 
 */
public class SimpleProxy extends AbstractProxyExtractor {
	@Override
	public Proxy extractProxyIp() {
		Proxy2 proxy2 = getProxy2s().get(0);
		return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy2.getIp(), proxy2.getPort())) ;
	}
}
