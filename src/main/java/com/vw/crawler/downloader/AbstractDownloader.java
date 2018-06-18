package com.vw.crawler.downloader;

import com.vw.crawler.model.PageRequest;
import org.jsoup.nodes.Document;

import java.io.IOException;


/**
 * 自定义下载器
 */
public abstract class AbstractDownloader {


	public abstract Document downloadPage(PageRequest requestBody) throws IOException;
}
