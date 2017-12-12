package com.vw.crawler.parser;

import org.jsoup.nodes.Document;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/12/12
 * Time: 19:30
 * Description:
 */
public abstract class PageParser<T> {
    public abstract void parse(Document document, T obj);
}
