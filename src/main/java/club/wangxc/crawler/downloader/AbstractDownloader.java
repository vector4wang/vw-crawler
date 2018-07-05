package club.wangxc.crawler.downloader;

import club.wangxc.crawler.model.PageRequest;
import org.jsoup.nodes.Document;

import java.io.IOException;


/**
 * 自定义下载器
 */
public abstract class AbstractDownloader {


	public abstract Document downloadPage(PageRequest requestBody) throws IOException;
}
