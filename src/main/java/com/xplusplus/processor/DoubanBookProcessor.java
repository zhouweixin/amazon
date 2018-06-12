package com.xplusplus.processor;

import org.apache.log4j.BasicConfigurator;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:20 2018/4/2
 * @Modified By:
 */
public class DoubanBookProcessor implements PageProcessor {

    Site site = Site.me();

    @Override
    public void process(Page page) {
        System.out.println(page.getHtml().xpath("//div[@class='article']/div/table//div[@class='pl2']/a/text()").all());

        page.addTargetRequests(page.getHtml().xpath("//*[@id=\"content\"]/div/div[1]/div/div/a/@href").all());
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void run(){
        Spider.create(new DoubanBookProcessor())
                .addUrl("https://book.douban.com/top250?start=0")
                .setDownloader(new SeleniumDownloader("c:\\Windows\\chromedriver.exe"))
                .runAsync();
    }
}
