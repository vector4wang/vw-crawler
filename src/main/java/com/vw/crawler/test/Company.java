package com.vw.crawler.test;

import com.vw.crawler.annotation.CssSelector;
import com.vw.crawler.util.SelectType;

/**
 * @Author: wangxc
 * @GitHub: https://github.com/vector4wang
 * @CSDN: http://blog.csdn.net/qqhjqs?viewmode=contents
 * @BLOG: http://vector4wang.tk
 * @wxid: BMHJQS
 */
public class Company {

    @CssSelector(selector = "body > div.main > div.mainLeft > div:nth-child(1) > h1",resultType = SelectType.TEXT)
    private String name;

    @CssSelector(selector = "body > div.main > div.mainLeft > div.part2 > div.company-content",resultType = SelectType.TEXT)
    private String desc;

    private String currentUrl;

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", currentUrl='" + currentUrl + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }
}
