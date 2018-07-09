package zhilian;

import com.github.vector4wang.service.CrawlerService;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IDEA
 * User: vector 
 * Data: 2018/7/9 0009
 * Time: 11:29
 * Description: 
 */
public class ZhillianCrawlerService extends CrawlerService<Zhilian> {

	private Logger logger = LoggerFactory.getLogger(ZhillianCrawlerService.class.getName());

	@Override
	public void parsePage(Document doc, Zhilian pageObj) {

	}

	@Override
	public void save(Zhilian pageObj) {
		logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> " + pageObj);
	}
}
