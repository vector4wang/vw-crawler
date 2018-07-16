package csdn;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.service.CrawlerService;
import org.jsoup.nodes.Document;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/16 0016
 * Time: 17:55
 * Description:
 */
public class TestMain2 {
	public static void main(String[] args) {
		new VWCrawler.Builder().setUrl("https://blog.csdn.net/qqhjqs").setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
				.setTargetUrlRex("https://blog.csdn.net/qqhjqs/article/details/[0-9]+").setThreadCount(1)
				.setTimeOut(5000).setPageParser(new CrawlerService() {
			@Override
			public void parsePage(Document doc, Object pageObj) {
				System.out.println(doc.title());
			}

			@Override
			public void save(Object pageObj) {
				System.out.println("save");
			}
		}).build().start();
	}
}
