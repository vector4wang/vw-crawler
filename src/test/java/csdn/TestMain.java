package csdn;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.service.CrawlerService;
import org.jsoup.nodes.Document;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/10 0010
 * Time: 17:50
 * Description: csdn列表页的抓取示例
 */
public class TestMain {
	public static void main(String[] args) {
		new VWCrawler.Builder().setUrl("https://blog.csdn.net/qqhjqs").setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
				.setTargetUrlRex("https://blog.csdn.net/qqhjqs/article/details/[0-9]+").setThreadCount(1)
				.setTimeOut(1000).setPageParser(new CrawlerService<Blog>() {
			@Override
			public void parsePage(Document doc, Blog pageObj) {
				// 可进行二次处理
			}

			@Override
			public void save(Blog pageObj) {
				System.out.println("save blog summart: " + pageObj.toString());
			}
		}).build().start();
	}
}
