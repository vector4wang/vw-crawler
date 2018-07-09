package com.github.vector4wang.service;

import org.jsoup.nodes.Document;

/**
 *  爬虫服务，可以自定义url是否存在(已抓取),自定义url是否要继续解析
 * @author : wangxc
 */
public abstract class CrawlerService<T> {



    /**
     * 待抓取的url，是否已经抓取过
     * 默认返回false，可根据数据库存储去判断
     * @param url 即将要抓取的url
	 * @return 是否存在
     */
    public boolean isExist(String url){
        return false;
    }

    /**
     * 当前待抓取的页面是否遇到WAP，账号是否被封
     * 默认返回true，是否需要重试链接，根据目标页面返回的值做处理
	 * @param document 即将要解析的document
	 * @return 是否继续
     */
    public boolean isContinue(Document document){
        if (document == null) {
            return false;
        }
        return true;
    }

    /**
	 * 自定义解析页面的方法
	 * @param doc 文档内容
	 * @param pageObj 封装的对象
     */
    public abstract void parsePage(Document doc, T pageObj);

	/**
	 * 保存 最终的对象
	 * @param pageObj
	 */
	public abstract void save(T pageObj);

}
