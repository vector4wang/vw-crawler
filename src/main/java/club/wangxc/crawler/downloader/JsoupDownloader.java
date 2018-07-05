package club.wangxc.crawler.downloader;

import club.wangxc.crawler.model.PageRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class JsoupDownloader extends AbstractDownloader {

	@Override
	public Document downloadPage(PageRequest requestBody) throws IOException {
		if (requestBody.getUrl() == null && requestBody.getUrl().length() <= 0) {
			throw new RuntimeException("PageRequest中的URL不合法");
		}
		Connection connection = Jsoup.connect(requestBody.getUrl());

		if (requestBody.getTimeout() > 0) {
			connection.timeout(requestBody.getTimeout());
		}
		if (requestBody.getProxy() != null) {
			connection.proxy(requestBody.getProxy());
		}
		if (requestBody.getHeader() != null && !requestBody.getHeader().isEmpty()) {
			connection.headers(requestBody.getHeader());
		}

		Document document = connection.execute().parse();
		if (document == null) {
			throw new RuntimeException("页面请求失败，请检查网络、RequestBody或者其他配置参数是否有误");
		}
		return document;
	}
}
