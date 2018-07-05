package club.wangxc.crawler.thread;

import club.wangxc.crawler.model.PageRequest;
import club.wangxc.crawler.util.CrawlerUtil;
import club.wangxc.crawler.util.JsoupUtil;
import club.wangxc.crawler.VWCrawler;
import club.wangxc.crawler.annotation.CssSelector;
import club.wangxc.crawler.proxy.ProxyBuilder;
import club.wangxc.crawler.util.SelectType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created with IDEA
 * User: vector
 * Data: 2018/2/6 0006
 * Time: 20:05
 * Description: vw爬虫的核心处理逻辑
 */
public class CrawlerThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(CrawlerThread.class.getName());
	private VWCrawler vwCrawler;

	private boolean isRunning; // 当前线程死亡标志


	public CrawlerThread(VWCrawler vwCrawler) {
		this.vwCrawler = vwCrawler;
	}

	@Override
	public void run() {
		while (!vwCrawler.getWaitCrawlerUrls().isEmpty()) {
			String waitCrawlerUrl = vwCrawler.getWaitCrawlerUrls().poll();
			if (waitCrawlerUrl != null && waitCrawlerUrl.length() > 0) {
				process(waitCrawlerUrl);
			}
		}
		logger.info("爬虫停止。。。。");
	}

	private void process(String url) {
		/**
		 * 自定义去重逻辑
		 */
		if (vwCrawler.getCrawlerService().isExist(url)) {
			return;
		}
		logger.info(Thread.currentThread().getName() + " 开始抓取 " + url);
		try {
			Document document = null;
			boolean isProxyInvalid = false;
			do {
				PageRequest pageRequest = new PageRequest();
				pageRequest.setUrl(url);
				pageRequest.setTimeout(vwCrawler.getTimeout());
				if (vwCrawler.getHeaderMap() != null && !vwCrawler.getHeaderMap().isEmpty()) {
					pageRequest.setHeader(vwCrawler.getHeaderMap());
				}
				if (vwCrawler.getProxys().size() > 0) {
					if (vwCrawler.getCurrentProxy() == null || isProxyInvalid) {
						vwCrawler.setCurrentProxy(JsoupUtil.getProxy(vwCrawler.getProxys(), ProxyBuilder.Type.RANDOM));
					}
					pageRequest.setProxy(vwCrawler.getCurrentProxy());
				}
				try {
					document = vwCrawler.getDownloader().downloadPage(pageRequest);

				} catch (ConnectException socketTimeoutException) {
					logger.warn("链接超时");
					isProxyInvalid = true;
					continue;
				} catch (SocketTimeoutException socketTimeoutException) {
					isProxyInvalid = true;
					continue;
				}

			} while (!vwCrawler.getCrawlerService().isConinue(document));

			if (document != null) {

				if (vwCrawler.getSeedsPageUrlRex().size() > 0) {
					/**
					 * 抽取满足正则的url
					 */
					Elements links = document.select("a[href]");
					if (links.size() > 0) {
						for (Element link : links) {
							String href = link.absUrl("href");
							for (String seedsPageUrlRex : vwCrawler.getSeedsPageUrlRex()) {
								if (CrawlerUtil.isMatch(seedsPageUrlRex, href)) {
									vwCrawler.getSeedsPageUrlRex().add(href);
								}
							}
						}
					}

				}
				vwCrawler.getCrawledUrls().add(url);

				/**
				 * 判断当前URL是否为target URL
				 */
				if (!vwCrawler.isTargetUrl(url)) {
					return;
				}


				Type[] type = ((ParameterizedType) vwCrawler.getCrawlerService().getClass().getGenericSuperclass())
						.getActualTypeArguments();
				Class aClass = (Class) type[0];
				Object pageVo = aClass.newInstance();

				/**
				 * 解析页面，封装obj
				 */
				Field[] declaredFields = pageVo.getClass().getDeclaredFields();
				if (declaredFields != null) {
					for (Field declaredField : declaredFields) {
						CssSelector annotation = declaredField.getAnnotation(CssSelector.class);
						if (annotation == null) {
							continue;
						}
						String selector = annotation.selector();
						SelectType selectType = annotation.resultType();
						if (selector == null || selector.length() <= 0) {
							continue;
						}
						String result = "";

						if (selectType == SelectType.HTML) {
							result = document.select(selector).toString();
						} else {
							result = document.select(selector).text();
						}
						declaredField.setAccessible(true);
						declaredField.set(pageVo, result);
					}
				}
				vwCrawler.getCrawlerService().parsePage(document, pageVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof IOException) {
				logger.warn("请求地址发生错误");
			} else {
				logger.error(e.getMessage());
			}
		}
	}
}
