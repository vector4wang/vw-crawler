package proxy;

import com.github.vector4wang.VWCrawler;
import com.github.vector4wang.proxy.Proxy2;
import com.github.vector4wang.proxy.RandomProxy;
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
 *
 * 注意：如果给的代理失效，则会使用本机的ip，这一点需要注意
 */
public class TestMain {
	public static void main(String[] args) {

		/**
		 * https://cn-proxy.com/
		 */

		List<Proxy2> ps = new ArrayList<>();
		Proxy2 proxy22 = new Proxy2("101.231.104.82", 80);
		Proxy2 proxy21 = new Proxy2("119.41.236.180",8010);
		Proxy2 proxy23 = new Proxy2("36.25.243.50",80);
		Proxy2 proxy25 = new Proxy2("39.105.229.239",8080);
		Proxy2 proxy24 = new Proxy2("183.146.213.157",80);


		ps.add(proxy21);
		ps.add(proxy22);
		ps.add(proxy23);
		ps.add(proxy24);
		ps.add(proxy25);

        new VWCrawler.Builder()
                .setUrl("http://200019.ip138.com/")
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36")
                .setTimeOut(5000)
                .setRetryCount(5)
                .setProxys(ps)
                .setAbsProxyExtracter(new RandomProxy())
                .setPageParser(new CrawlerService() {
                    @Override
                    public void parsePage(Document doc, Object pageObj) {
//                        System.out.println(doc);
                        System.out.println(doc.body().select("p").get(0).text());
                    }
                    @Override
                    public void save(Object pageObj) {
                        // save data
                    }
                })
                .build()
                .start();
	}
}
