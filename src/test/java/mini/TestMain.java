package mini;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.service.CrawlerService;
import org.jsoup.nodes.Document;

public class TestMain {
	public static void main(String[] args) {
		new VWCrawler.Builder().setUrl("https://www.qiushibaike.com/").setPageParser(new CrawlerService() {
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
