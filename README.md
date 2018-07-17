# vw-crawler

 ![Central](https://img.shields.io/maven-central/v/com.github.vector4wang/vw-crawler.svg)

平时简单的网站用webmagic抓取，接触了xxl-crawler之后就有自己开发一个简单的框架的想法，借鉴webmagic和xxl-crawler,
边学习边完善。


使用Java开发的简易爬虫框架

特点：
易配置；易修改；易控制


## Sample
抓取CSDN某用户的博客内容

设置爬虫的基本配置，如User-Agent、起始地址、目标页面的url正则表达式、线程数和超时时间等
```
new VWCrawler.Builder()
    .setHeader("User-Agent",
    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36") // 设置请求头
    .setUrl("https://blog.csdn.net/qqhjqs") // 设置爬虫起始地址
    .setTargetUrlRex("https://blog.csdn.net/qqhjqs/article/details/[0-9]+") // 设置目标页面url的正则表达式
    .setThreadCount(10) // 设置几个线程抓取数据
    .setTimeOut(5000) // 设置超时时间
    .setPageParser(new CrawlerService<Blog>() {
        /**
         * 目标页面的doc对象，还有通过注解处理后的对象
         * @param doc 文档内容
         * @param pageObj 封装的对象
         */
        @Override
        public void parsePage(Document doc, Blog pageObj) {
            // 可进行二次处理
            pageObj.setReadNum(pageObj.getReadNum().replace("阅读数：",""));
        }

        /**
         * 可以做保存对象的处理
         * @param pageObj 页面对象
         */
        @Override
        public void save(Blog pageObj) {
            System.out.println("save blog summery: " + pageObj.toString());
        }
    }) // 自定义解析service
    .build()
    .start(); // 启动
```

配置页面数据对象的注解
```
    @CssSelector(selector = "#mainBox > main > div.blog-content-box > div.article-title-box > h1", resultType = SelectType.TEXT)
    private String title;

    @CssSelector(selector = "#mainBox > main > div.blog-content-box > div.article-info-box > div > span.time", dateFormat = "yyyy年MM月dd日 HH:mm:ss")
    private Date lastUpdateDate;

    @CssSelector(selector = "#mainBox > main > div.blog-content-box > div.article-info-box > div > div > span", resultType = SelectType.TEXT)
    private String readNum;
```

这里可以简单的处理页面上的数据，初步解析，之后可以在public void parsePage(Document doc, Blog pageObj)进行数据二次处理



