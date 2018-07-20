package com.github.vector4wang.thread;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.annotation.CssSelector;
import com.github.vector4wang.model.PageRequest;
import com.github.vector4wang.proxy.Proxy2;
import com.github.vector4wang.util.CrawlerUtil;
import com.github.vector4wang.util.GenericsUtils;
import com.github.vector4wang.util.ReflectUtils;
import com.github.vector4wang.util.SelectType;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;

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
		logger.info("{}开始抓取[{}]当前待抓取数为{},已抓取数为{}", Thread.currentThread().getName(), url,
				vwCrawler.getWaitCrawlerUrls().size(), vwCrawler.getCrawledUrls().size());
		try {
			Document document = null;
			int timeoutCount = 0;
			do {
				PageRequest pageRequest = new PageRequest();
				pageRequest.setUrl(url);
				pageRequest.setTimeout(vwCrawler.getTimeout());
				if (vwCrawler.getHeaderMap() != null && !vwCrawler.getHeaderMap().isEmpty()) {
					pageRequest.setHeader(vwCrawler.getHeaderMap());
				}
				List<Proxy2> proxy2s = vwCrawler.getProxyExtractor().getProxy2s();
				if (proxy2s != null && proxy2s.size() > 0) {
					pageRequest.setProxy(vwCrawler.getProxyExtractor().extractProxyIp());
				}
				try {
					document = vwCrawler.getDownloader().downloadPage(pageRequest);

				} catch (ConnectException socketTimeoutException) {
					logger.warn("链接超时");
					continue;
				} catch (SocketTimeoutException socketTimeoutException) {
					continue;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					timeoutCount++;
					if (timeoutCount >= vwCrawler.getRetryCount()) {
						break;
					}
				}

			} while (document == null);

			if (!vwCrawler.getCrawlerService().isContinue(document)) {
				return;
			}

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

				/**
				 * 判断当前URL是否为target URL
				 */
				if (!vwCrawler.isTargetUrl(url)) {
					return;
				}

				Class aClass = GenericsUtils.getSuperClassGenericType(vwCrawler.getCrawlerService().getClass());
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
