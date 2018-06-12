package com.xplusplus.processor;

import com.xplusplus.domain.Menu;
import com.xplusplus.domain.SpiderConfig;
import com.xplusplus.downloader.ClosingSeleniumDownloader;
import com.xplusplus.exception.AmazonException;
import com.xplusplus.exception.EnumException;
import com.xplusplus.pipeline.MenuPipeline;
import com.xplusplus.repository.MenuRepository;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.monitor.SpiderStatus;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;
import us.codecraft.webmagic.selector.Html;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 19:46 2018/4/4
 * @Modified By:
 */
public class MenuProcessor implements PageProcessor {
    private static Spider spider;
    private static SpiderConfig spiderConfig = null;

    private Site site = Site.me();
    private String url = "";

    public MenuProcessor() {
    }

    public MenuProcessor(int retryTimes,
                         int cycleRetryTimes,
                         int retrySleepTime,
                         int sleepTime,
                         int timeOut,
                         String domain,
                         String url) {
        this.url = url;
        site = Site.me()
                .setRetryTimes(retryTimes)
                .setCycleRetryTimes(cycleRetryTimes)
                .setRetrySleepTime(retrySleepTime)
                .setSleepTime(sleepTime)
                .setTimeOut(timeOut)
                .setDomain(domain);
    }

    /**
     * 启动定时器
     * @param spiderConfig
     */
    public static void enableSchedule(SpiderConfig spiderConfig) {
        MenuProcessor.spiderConfig = spiderConfig;
    }

    @Override
    public void process(Page page) {
        if (page == null) {
            return;
        }

        String ul = page.getHtml().xpath("//ul[@id='zg_browseRoot']/ul").get();
        while (ul != null && !ul.equals("")) {
            String parentText = new Html(ul).xpath("ul/li/span[@class='zg_selected']/text()").get();
            if (parentText != null && !parentText.equals("")) {
                parentText = parentText.trim();
            } else {
                ul = new Html(ul).xpath("ul/ul").get();
                continue;
            }

            List<Menu> menus = new ArrayList<Menu>();

            // 第一个菜单：Clothing, Shoes & Jewelry
            if (page.getUrl().toString().trim().equals(url)) {
                Menu menu = new Menu();
                menu.setName(parentText);
                menu.setUrl(url);

                menus.add(menu);
            }

            List<String> lis = new Html(ul).xpath("ul/ul/li").all();

            for (String li : lis) {
                String text = Jsoup.parse(li).select("a").text();
                String href = Jsoup.parse(li).select("a").attr("href");

                Menu menu = new Menu();
                if (text != null) {
                    menu.setName(text.trim());
                }
                if (href != null) {
                    ;
                    menu.setUrl(href.trim().split("/ref=")[0]);
                }
                menu.setParentUrl(page.getUrl().toString().trim());
                menus.add(menu);

                page.addTargetRequest(menu.getUrl());
            }
            page.putField("menus", menus);

            break;
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    // 每月1号爬取菜单
    @Scheduled(cron = "0 0 0 1 * *")
    public static void callStartCrawl(){
        if(spiderConfig != null){
            runSpider(spiderConfig);
        }
    }

    /**
     * 停止定时器
     */
    public static void stopSchedule() {
        MenuProcessor.spiderConfig = null;
    }

    /**
     * 启动爬虫
     *
     * @param spiderConfig 爬虫配置信息
     */
    public static void runSpider(SpiderConfig spiderConfig) {

        if(spider != null && spider.getStatus() == Spider.Status.Running){
            throw new AmazonException(EnumException.SPIDER_START_FAILED_RUNNING);
        }

        // 去除“/ref=”后面的内容
        if (spiderConfig.getStartUrl() != null) {
            spiderConfig.setStartUrl(spiderConfig.getStartUrl().trim().split("/ref=")[0]);
        }

        ClosingSeleniumDownloader closingSeleniumDownloader = new ClosingSeleniumDownloader(spiderConfig.getChromeDriverPath());
        closingSeleniumDownloader.setSleepTime(spiderConfig.getSleepTime());
        closingSeleniumDownloader.setThread(spiderConfig.getThreadNum());

        spider = Spider.create(new MenuProcessor(spiderConfig.getRetryTimes(), spiderConfig.getCycleRetryTimes(),
                spiderConfig.getRetrySleepTime(), spiderConfig.getSleepTime(), spiderConfig.getTimeOut(),
                spiderConfig.getDomain(), spiderConfig.getStartUrl()))
                .addPipeline(new MenuPipeline())
                .setScheduler(new QueueScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .setDownloader(closingSeleniumDownloader)
                .addUrl(spiderConfig.getStartUrl())
                .thread(spiderConfig.getThreadNum());
        spider.runAsync();
    }

    /**
     * 停止爬虫
     */
    public static void stopSpider() {
        if(spider != null && spider.getStatus() == Spider.Status.Running){
            MenuProcessor.spiderConfig = null;
            spider.stop();
        }
    }

    /**
     * 查询爬虫状态
     */
    public static Spider.Status getSpiderStatus() {
        if(spider == null){
            return null;
        }else{
            return spider.getStatus();
        }
    }
}
