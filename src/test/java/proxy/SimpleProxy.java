package proxy;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.proxy.Proxy2;
import com.github.vector4wang.service.CrawlerService;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/17 0017
 * Time: 11:02
 * Description: 使用代理的示例
 */
public class SimpleProxy {
	public static void main(String[] args) {

		List<Proxy2> ps = new ArrayList<>();
		Proxy2 proxy21 = new Proxy2("221.141.2.52",8080);
		Proxy2 proxy22 = new Proxy2("81.211.23.6",3128);
		Proxy2 proxy23 = new Proxy2("207.148.90.14",808);
		Proxy2 proxy24 = new Proxy2("95.143.109.131",41258);
		Proxy2 proxy25 = new Proxy2("203.218.173.47",8580);

		ps.add(proxy21);
		ps.add(proxy22);
		ps.add(proxy23);
		ps.add(proxy24);
		ps.add(proxy25);

		new VWCrawler.Builder().setUrl("http://2018.ip138.com/ic.asp").setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
				.setTimeOut(5000).setProxys(ps).setPageParser(new CrawlerService() {
			@Override
			public void parsePage(Document doc, Object pageObj) {
				System.out.println(doc.select("body").text());
			}

			@Override
			public void save(Object pageObj) {
				// save data
			}
		}).build().start();
	}
}
