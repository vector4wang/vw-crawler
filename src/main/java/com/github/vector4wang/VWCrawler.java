package com.github.vector4wang;

import com.github.vector4wang.downloader.AbstractDownloader;
import com.github.vector4wang.downloader.JsoupDownloader;
import com.github.vector4wang.proxy.AbstractProxyExtractor;
import com.github.vector4wang.proxy.Proxy2;
import com.github.vector4wang.proxy.RandomProxy;
import com.github.vector4wang.service.CrawlerService;
import com.github.vector4wang.thread.CrawlerThread;
import com.github.vector4wang.util.CrawlerUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * vw爬虫的简单配置中心
 * @author vector
 */
public class VWCrawler {

	private static Logger logger = LoggerFactory.getLogger(CrawlerThread.class);

	private String url;
	private int timeout = 2000;
	private int retryCount = 2;

    /**
     * 目标页面
     */
	private volatile LinkedBlockingQueue<String> waitCrawlerUrls = new LinkedBlockingQueue<>();

    /**
     * 默认下载
     */
	private AbstractDownloader downloader = new JsoupDownloader();

    /**
     *  已抓取页面
     */
	private volatile Set<String> crawledUrls = new HashSet<>();

    /**
     * 目标页面 正则
     */
	private volatile Set<String> targetUrlRex = new HashSet<>();

    /**
     * 存放目标页面链接的页面 正则
     */
	private volatile Set<String> seedsPageUrlRex = new HashSet<>();

	private volatile AbstractProxyExtractor proxyExtractor = new RandomProxy();

	private volatile Map<String, String> cookieMap;

	private volatile Map<String, String> headerMap;

	private CrawlerService crawlerService;


	private int threadCount = 1;
	private ExecutorService crawler = Executors.newCachedThreadPool();


	private List<CrawlerThread> crawlerThreads = new CopyOnWriteArrayList<CrawlerThread>();


	/**
	 * 比较流行的链式编程
	 */
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
					if (StringUtils.isNotEmpty(url)) {
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
		 * @param targetUrlRex seed url正则表达式
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
		 * @param seedsPageUrlRex seeds url 正则表达式
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
			crawler.downloader = downloader;
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

		public Builder setCookie(String key, String value) {
			this.getCookies().put(key, value);
			return this;
		}

		public Builder setCookies(Map<String, String> cookies) {
			this.setCookies(cookies);
			return this;
		}

		public Builder setProxys(List<Proxy2> proxys) {
			if (proxys != null) {
				if (proxys.size() > 0) {
					crawler.proxyExtractor.setProxy2s(proxys);
				}

			}
			return this;
		}

		public Builder setAbsProxyExtracter(AbstractProxyExtractor extracter) {
			if (extracter != null) {
				if (!crawler.proxyExtractor.getProxy2s().isEmpty()) {
					extracter.setProxy2s(crawler.proxyExtractor.getProxy2s());
				}
				crawler.proxyExtractor = extracter;
			}
			return this;
		}

		private Map<String, String> getHeader() {
			if (this.crawler.headerMap == null) {
				this.crawler.headerMap = new HashMap<>(4);
			}
			return this.crawler.headerMap;
		}

		private Map<String, String> getCookies() {
			if (this.crawler.cookieMap == null) {
				this.crawler.cookieMap = new HashMap<>(4);
			}
			return this.crawler.cookieMap;
		}

		public Builder setRetryCount(int retryCount) {
			crawler.retryCount = retryCount;
			return this;
		}

		public VWCrawler build() {
			return crawler;
		}


	}

	public String getUrl() {
		return url;
	}

	public int getTimeout() {
		return timeout;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public int getThreadCount() {
		return threadCount;
	}

	public LinkedBlockingQueue<String> getWaitCrawlerUrls() {
		return waitCrawlerUrls;
	}

	public Set<String> getCrawledUrls() {
		return crawledUrls;
	}

	public Set<String> getTargetUrlRex() {
		return targetUrlRex;
	}

	public Set<String> getSeedsPageUrlRex() {
		return seedsPageUrlRex;
	}

	public CrawlerService getCrawlerService() {
		return crawlerService;
	}

	public AbstractProxyExtractor getProxyExtractor() {
		return proxyExtractor;
	}

	public void setProxyExtractor(AbstractProxyExtractor proxyExtractor) {
		this.proxyExtractor = proxyExtractor;
	}

	public Map<String, String> getHeaderMap() {
		return headerMap;
	}

	public AbstractDownloader getDownloader() {
		return downloader;
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

		crawler.shutdown();
        try {
            while (!crawler.awaitTermination(10, TimeUnit.MICROSECONDS)) {
                logger.debug("主线程等待所有抓取的线程终止！");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        logger.info("vw-crawler pool closed!");
    }

	/**
	 * 尝试停止(正常情况的停止)
	 */
	public void tryStop() {
		boolean isRunning = false;
		/**
		 * 只要有一个线程还在运行，就不能停止
		 */
		for (CrawlerThread crawlerThread : crawlerThreads) {
			if (crawlerThread.isRunning()) {
				isRunning = true;
				break;
			}
		}

		boolean isStop = waitCrawlerUrls.isEmpty() && !isRunning;
		if (isStop) {
			logger.info("vw-crawler is finished and will stop!");
			stop();
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
	 *
	 * @return 取出一个待抓取的url
	 * @throws InterruptedException 线程中断异常
	 */
	public String generateUrl() throws InterruptedException {
		String url = this.getWaitCrawlerUrls().take();
		this.getCrawledUrls().add(url);
		return url;
	}

	/**
	 * 将url保存在目标集合中
	 * @param href 即将添加到待抓取列表的url
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

	/**
	 * 停止爬虫
	 */
	public void stop() {
		crawler.shutdownNow();
	}

}
