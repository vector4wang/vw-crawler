package com.github.vector4wang.thread;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.annotation.CssSelector;
import com.github.vector4wang.model.PageRequest;
import com.github.vector4wang.proxy.ProxyBuilder;
import com.github.vector4wang.util.*;
import org.apache.commons.lang3.StringUtils;
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
 * @author vector
 */
public class CrawlerThread implements Runnable {

	private Logger logger = LoggerFactory.getLogger(CrawlerThread.class.getName());
	private VWCrawler vwCrawler;
	private boolean isRunning;

	public CrawlerThread(VWCrawler vwCrawler) {
		this.vwCrawler = vwCrawler;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean running) {
		isRunning = running;
	}

	@Override
	public void run() {
		try {
			while (true) {
				isRunning = false;
				vwCrawler.tryStop();
				String url = vwCrawler.generateUrl();
				isRunning = true;
				if (vwCrawler.getCrawledUrls().contains(url)) {
					continue;
				}
				if (StringUtils.isEmpty(url)) {
					logger.info("no url");
					break;
				}
				process(url);
			}

		} catch (Exception e) {
			if (e instanceof InterruptedException) {
				logger.info("vw-crawler[" + Thread.currentThread().getName() + "] stopped!", e.getMessage());
			} else {
				logger.error(e.getMessage(), e);
			}
		}
	}

	private void process(String url) {
		/**
		 * 自定义去重逻辑可以与数据库交互
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

			} while (!vwCrawler.getCrawlerService().isContinue(document));

			if (document != null) {

				/**
				 * 抽取满足正则的url
				 */
				Elements links = document.select("a[href]");
				if (links.size() > 0) {
					for (Element link : links) {
						String href = link.absUrl("href");
						for (String seedsPageUrlRex : vwCrawler.getSeedsPageUrlRex()) {
							if (CrawlerUtil.isMatch(seedsPageUrlRex, href)) {
								vwCrawler.addWaitCrawlerUrl(href);
							}
						}
					}

					for (Element link : links) {
						String href = link.absUrl("href");
						for (String targetUrlRex : vwCrawler.getTargetUrlRex()) {
							if (CrawlerUtil.isMatch(targetUrlRex, href)) {
								vwCrawler.addWaitCrawlerUrl(href);
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

				Class aClass = GenericsUtils
						.getSuperClassGenericType(vwCrawler.getCrawlerService().getClass());
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
						String result;

						if (selectType == SelectType.HTML) {
							result = document.select(selector).toString();
						} else {
							result = document.select(selector).text();
						}


						declaredField.setAccessible(true);
						Object transferVal = ReflectUtils.parseValueWithType(result, declaredField);
						declaredField.set(pageVo, transferVal);
					}
				}

				vwCrawler.getCrawlerService().parsePage(document, pageVo);
				vwCrawler.getCrawlerService().save(pageVo);
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
