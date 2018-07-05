package club.wangxc.crawler.model;

import java.net.Proxy;
import java.util.Map;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
public class PageRequest {

    private String url;
    private int timeout = 3000;
    private Proxy proxy;

	private Map<String, String> header;

    public PageRequest() {
    }

	public PageRequest(String url, int timeout, Proxy proxy, Map<String, String> header) {
		this.url = url;
		this.timeout = timeout;
		this.proxy = proxy;
		this.header = header;
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

	public Proxy getProxy() {
		return proxy;
	}

	public void setProxy(Proxy proxy) {
		this.proxy = proxy;
	}

	public Map<String, String> getHeader() {
		return header;
	}

	public void setHeader(Map<String, String> header) {
		this.header = header;
	}
}
