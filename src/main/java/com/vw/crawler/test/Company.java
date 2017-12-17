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

    @CssSelector(selector = "body > div.job-detail.page.clear > div.job-detail-l > div:nth-child(2) > div.base_info > div:nth-child(1) > h1 > span.job_name",resultType = SelectType.TEXT)
    private String name;

    @CssSelector(selector = "body > div.job-detail.page.clear > div.job-detail-l > div.job_intro.jpadding.mt15 > div.job_intro_wrap > div",resultType = SelectType.TEXT)
    private String desc;

    @Override
    public String toString() {
        return "Company{" +
                "name='" + name + '\'' +
                ", desc='" + desc + '\'' +
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
}
