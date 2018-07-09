package zhilian;

import com.github.vector4wang.VWCrawler;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/9 0009
 * Time: 11:26
 * Description: 智联爬虫示例
 */
public class TestMain {
	public static void main(String[] args) {
		String[] targetUrlRex = {"http://jobs.zhaopin.com/CC[0-9]+.htm"};
		String[] seedsUrlRex = {"http://sou.zhaopin.com/jobs/searchresult.ashx?\\*", "http://jobs.zhaopin.com/sj[0-9]+/"};
		new VWCrawler.Builder()
				.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.57 Safari/537.36")
				.setThreadCount(10)
				.setUrl("http://jobs.zhaopin.com/")
				.setTargetUrlRex(targetUrlRex)
				.setSeedsPageUrlRex(seedsUrlRex)
				.setPageParser(new ZhillianCrawlerService()).setTimeOut(10000).build().start();
	}
}
