import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * @author vector
 * @date: 2019/8/13 0013 17:39
 */
public class TestMain {
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            try {
                Document document = Jsoup.connect("http://200019.ip138.com/")
                        .proxy("101.231.104.82", 80)
                        .timeout(5000)
                        .get();
                System.out.println(document.body().select("p").get(0).text());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
