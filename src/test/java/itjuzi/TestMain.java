package itjuzi;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.service.CrawlerService;
import org.jsoup.nodes.Document;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestMain {

	@Test
	@Ignore
	public void login() throws IOException {
		// https://www.itjuzi.com/user/login
//		final WebClient webClient = new WebClient(BrowserVersion.CHROME);
//		webClient.getOptions().setJavaScriptEnabled(false);
//		webClient.getOptions().setCssEnabled(false);
//		webClient.getOptions().setRedirectEnabled(true);
//		webClient.getCookieManager().setCookiesEnabled(true);
//		final HtmlPage page = webClient.getPage("https://www.itjuzi.com/user/login?flag=radar&redirect=/company");
//		HtmlForm loginForm = page.getFormByName("login_form");
//		HtmlTextInput inputByName = loginForm.getInputByName("identity");
//		HtmlPasswordInput password = loginForm.getInputByName("password");
//		HtmlButton submit = (HtmlButton) loginForm.getByXPath("//*[@id=\"login_btn\"]").get(0);
//
//		inputByName.type("18255062124");
//		password.type("wxc772704457@qq");
//		HtmlPage clickedPage = submit.click();
//		webClient.waitForBackgroundJavaScript(5000); // 等待js渲染
//		Set<Cookie> cookies = webClient.getCookieManager().getCookies();
		Map<String, String> cookiesMap = new HashMap<>();
//		for (Cookie cookie : cookies) {
//			cookiesMap.put(cookie.getName(), cookie.getValue());
//		}
//		String wel = clickedPage.getByXPath("/html/body/div[1]/div[1]/div/a[1]/text()").get(0).toString();
//		System.out.println(wel);
//		List<String> companyDetailList = clickedPage.getByXPath("//*[@id=\"searchList\"]/div[1]/ul/li/a[1]/@href");
//		for (String url : companyDetailList) {
//			System.out.println(url);
//		}

//		System.out.println(cookies);
//		Document document = Jsoup.connect("http://radar.itjuzi.com/company/infonew")
//				.cookies(cookiesMap)
//				.header("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8")
//				.header("X-Requested-With","XMLHttpRequest")
//				.header("Accept","application/json, text/javascript, */*; q=0.01")
//				.header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
//				.header("User-Agent",
//						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
//				.referrer("http://radar.itjuzi.com/company").header("Host", "radar.itjuzi.com").ignoreContentType(true).get();
//		System.out.println(document.text());


		new VWCrawler.Builder()
				.setCookies(cookiesMap)
				.setUrl("https://www.itjuzi.com/")
				.setSeedUrl("https://www.itjuzi.com/")
				.setTargetUrlRex("https://www.itjuzi.com/")
				.setHeader("Accept-Language","zh-CN,zh;q=0.9,en;q=0.8")
				.setHeader("X-Requested-With","XMLHttpRequest")
				.setHeader("Accept","application/json, text/javascript, */*; q=0.01")
				.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
				.setHeader("User-Agent",
						"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
				.setHeader("Referer","http://radar.itjuzi.com/company")
				.setHeader("Host", "radar.itjuzi.com")
				.setThreadCount(1)
				.setPageParser(new CrawlerService() {

			@Override
			public void parsePage(Document doc, Object pageObj) {
				System.out.println(doc.toString());
			}

			@Override
			public void save(Object pageObj) {

			}
		}).build().start();

	}
}
