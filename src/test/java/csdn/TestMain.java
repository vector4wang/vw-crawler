package csdn;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.service.CrawlerService;
import com.sun.org.apache.regexp.internal.RE;
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
		new VWCrawler.Builder()
				.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36") // 设置请求头
				.setUrl("https://blog.csdn.net/qqhjqs") // 设置爬虫起始地址
				.setTargetUrlRex("https://blog.csdn.net/qqhjqs/article/details/[0-9]+") // 设置目标页面url的正则表达式
				.setThreadCount(10) // 设置几个线程抓取数据
				.setTimeOut(5000) // 设置超时时间
				.setPageParser(new CrawlerService<Blog>() {

					/**
					 * 有的url可能在某个场景下不需要可在此处理
					 * @param url 即将要抓取的url
					 * @return
					 */
					@Override
					public boolean isExist(String url) {
						if ("https://blog.csdn.net/qqhjqs/article/details/79101846".equals(url)) {
							return true;
						}
						return false;
					}

					/**
					 * 有的页面有WAF，可以再真正解析前，做个判断，遇到特殊标志的直接可以跳过
					 * @param document 即将要解析的document
					 * @return
					 */
					@Override
					public boolean isContinue(Document document) {
						if ("最近和未来要做的事 - CSDN博客".equals(document.title())) {
							System.out.println("模拟遇到WAF此页面不做解析");
							return false;
						}
						return true;
					}

					/**
					 * 目标页面的doc对象，还有通过注解处理后的对象
					 * @param doc 文档内容
					 * @param pageObj 封装的对象
					 */
					@Override
					public void parsePage(Document doc, Blog pageObj) {
						// 可进行二次处理
						pageObj.setReadNum(pageObj.getReadNum().replace("阅读数：",""));
					}

					/**
					 * 可以做保存对象的处理
					 * @param pageObj 页面对象
					 */
					@Override
					public void save(Blog pageObj) {
						System.out.println("save blog summery: " + pageObj.toString());
					}
				}) // 自定义解析service
				.build()
				.start(); // 启动


	}
}
