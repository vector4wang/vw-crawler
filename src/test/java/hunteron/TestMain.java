package hunteron;

import com.vw.crawler.VWCrawler;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/13
 * Time: 15:57
 * Description:
 */
public class TestMain {
    public static void main(String[] args) {
        String[] urls = new String[50000];
        for (int i = 0; i < 50000; i++) {
            urls[i] = "http://www.hunteron.com/elite/position/detail/" + i + ".htm";
        }

        new VWCrawler.Builder()
                .setSeedUrl(urls)
                .setPageParser(new HunteronCrawlerService())
                .setTimeOut(10000)
                .build().start();
    }
}
