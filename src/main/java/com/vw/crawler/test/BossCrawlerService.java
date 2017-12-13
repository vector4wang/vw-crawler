package com.vw.crawler.test;

import com.vw.crawler.service.CrawlerService;
import org.jsoup.nodes.Document;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
public class BossCrawlerService extends CrawlerService<Company> {



    @Override
    public boolean isExist(String url) {
        return false;
    }

    @Override
    public boolean isConinue(Document document) {

        return false;
    }

    @Override
    public void parsePage(Document doc, Company pageObj) {

    }
}
