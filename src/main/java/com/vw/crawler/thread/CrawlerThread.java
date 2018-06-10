package com.vw.crawler.thread;

import com.vw.crawler.VWCrawler;
import com.vw.crawler.annotation.CssSelector;
import com.vw.crawler.model.PageRequest;
import com.vw.crawler.proxy.ProxyBuilder;
import com.vw.crawler.util.CrawlerUtil;
import com.vw.crawler.util.JsoupUtil;
import com.vw.crawler.util.SelectType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.logging.Logger;

/**
 * Created with IDEA
 * User: vector
 * Data: 2018/2/6 0006
 * Time: 20:05
 * Description: vw爬虫的核心处理逻辑
 */
public class CrawlerThread implements Runnable {

	private Logger logger = Logger.getLogger(CrawlerThread.class.getName());
	private VWCrawler vwCrawler;


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

	}

	private void process(String url) {

		/**
		 * 自定义去重逻辑
		 */
		if (vwCrawler.getCrawlerService().isExist(url)) {
			return;
		}
		System.out.println(Thread.currentThread().getName() + " 开始抓取 " + url);
		try {
			Document document = null;
			boolean isProxyInvalid = false;
			do {
				PageRequest pageRequest = new PageRequest();
				pageRequest.setUrl(url);
				pageRequest.setTimeout(vwCrawler.getTimeout());
				if (vwCrawler.getProxys().size() > 0) {
					if (vwCrawler.getCurrentProxy() == null || isProxyInvalid) {
						vwCrawler.setCurrentProxy(JsoupUtil.getProxy(vwCrawler.getProxys(), ProxyBuilder.Type.RANDOM));
					}
					pageRequest.setProxy(vwCrawler.getCurrentProxy());
				}
				try {
					document = JsoupUtil.htmlDoc(pageRequest);
				} catch (ConnectException socketTimeoutException) {
					System.out.println("链接超时");
					isProxyInvalid = true;
					continue;
				} catch (SocketTimeoutException socketTimeoutException) {
					System.out.println("代理失效");
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
				logger.info("请求地址发生错误");
			} else {
				e.printStackTrace();
			}
		}
	}
}
