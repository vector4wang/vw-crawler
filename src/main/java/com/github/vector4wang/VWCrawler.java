package com.github.vector4wang;

import com.github.vector4wang.downloader.AbstractDownloader;
import com.github.vector4wang.downloader.JsoupDownloader;
import com.github.vector4wang.proxy.ProxyBuilder;
import com.github.vector4wang.service.CrawlerService;
import com.github.vector4wang.thread.CrawlerThread;
import com.github.vector4wang.util.CrawlerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * vw爬虫的简单配置中心
 * @author vector
 */
public class VWCrawler {

	private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

	private String url;
	private int timeout = 2000;

	private volatile LinkedBlockingQueue<String> waitCrawlerUrls = new LinkedBlockingQueue<>(); // 目标页面

	private AbstractDownloader downloader = new JsoupDownloader();            // 默认下载

	private volatile Set<String> crawledUrls = new HashSet<>(); // 已抓取页面

	private volatile Set<String> targetUrlRex = new HashSet<>();                     // 目标页面 正则

	private volatile Set<String> seedsPageUrlRex = new HashSet<>();                // 存放目标页面链接的页面 正则

	private volatile List<Proxy> proxys = new ArrayList<>();

	private volatile ProxyBuilder.Type proxyType;

	private volatile Proxy currentProxy;

	private volatile Map<String, String> cookieMap;

	private volatile Map<String, String> headerMap;

	private CrawlerService crawlerService;


	private int threadCount = 1;
	private ExecutorService crawler = Executors.newCachedThreadPool();
	private List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();


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
				for (String url : targetUrl) {
					if(StringUtils.isNotEmpty(url)){
						crawler.addWaitCrawlerUrl(url);
					}
				}
			}
			return this;
		}

		public Builder setThreadCount(int count) {
			if (count <= 0) {
				throw new RuntimeException("线程数不能小于0");
			}

			crawler.threadCount = count;
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

		/**
		 * 设置目标页URL的正则表达式
		 * @param targetUrlRex
		 * @return this
		 */
		public Builder setTargetUrlRex(String... targetUrlRex) {
			if (targetUrlRex != null && targetUrlRex.length > 0) {
				for (String urlRex : targetUrlRex) {
					crawler.targetUrlRex.add(urlRex);
				}
			}
			return this;
		}

		/**
		 * 设置种子页面URL的正则表达式
		 * @param seedsPageUrlRex
		 * @return this
		 */
		public Builder setSeedsPageUrlRex(String... seedsPageUrlRex) {
			if (seedsPageUrlRex != null && seedsPageUrlRex.length > 0) {
				for (String seedsRex : seedsPageUrlRex) {
					crawler.seedsPageUrlRex.add(seedsRex);
				}
			}
			return this;
		}

		public Builder setPageParser(CrawlerService crawlerService) {
			crawler.crawlerService = crawlerService;
			return this;
		}

		public Builder setDownloader(AbstractDownloader downloader) {
			crawler.setDownloader(downloader);
			return this;
		}

		public Builder setHeaders(HashMap<String, String> headerMap) {
			this.getHeader().putAll(headerMap);
			return this;
		}

		public Builder setHeader(String key, String value) {
			this.getHeader().put(key, value);
			return this;
		}

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

		private Map<String, String> getHeader() {
			if (this.crawler.headerMap == null) {
				this.crawler.headerMap = new HashMap<>();
			}
			return this.crawler.headerMap;
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

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
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

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public void setHeaderMap(Map<String, String> headerMap) {
		this.headerMap = headerMap;
	}

	public AbstractDownloader getDownloader() {
		return downloader;
	}

	public void setDownloader(AbstractDownloader downloader) {
		this.downloader = downloader;
	}

	public void start() {

		if (waitCrawlerUrls.isEmpty()) {
			throw new RuntimeException("待抓取URL为空，请确认是否有设置waitCrawlerUrls(爬虫起始URL)");
		}

		for (int i = 0; i < threadCount; i++) {
			crawlerThreads.add(new CrawlerThread(this));
		}

		for (CrawlerThread crawlerThread : crawlerThreads) {
			crawler.execute(crawlerThread);
		}

	}

	public boolean isTargetUrl(String url) {
		if (targetUrlRex.isEmpty()) {
			return true;
		}
		for (String s : targetUrlRex) {
			if (CrawlerUtil.isMatch(s, url)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从目标URL集合抽取
	 * @return 将要抓取的url
	 */
	public String generateUrl() throws InterruptedException {
		return this.getWaitCrawlerUrls().take();
	}

	/**
	 * 将url保存在目标集合中
	 * @param href
	 */
	public void addWaitCrawlerUrl(String href) {
		if (this.getCrawledUrls().contains(href)) {
			return;
		}

		if (this.getWaitCrawlerUrls().contains(href)) {
			return;
		}
		try {
			this.getWaitCrawlerUrls().put(href);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
