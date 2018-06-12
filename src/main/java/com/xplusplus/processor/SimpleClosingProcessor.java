package com.xplusplus.processor;

import com.xplusplus.domain.Menu;
import com.xplusplus.domain.SimpleClosing;
import com.xplusplus.domain.SpiderConfig;
import com.xplusplus.downloader.ClosingSeleniumDownloader;
import com.xplusplus.pipeline.SimpleClosingPipeline;
import com.xplusplus.utils.InfoParseUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;
import us.codecraft.webmagic.selector.Html;

import java.io.*;
import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 20:31 2018/4/4
 * @Modified By:
 */
@Component
public class SimpleClosingProcessor implements PageProcessor {

    // url -> menu
    private static Map<String, Menu> url2Menu = new HashMap<String, Menu>();

    // 爬虫对象
    private static Spider spider;
    private static SpiderConfig spiderConfig = null;

    // 站点
    private Site site = Site.me();

    // 菜单
    private List<Menu> menus;

    public SimpleClosingProcessor() {
    }

    /**
     * @param retryTimes      重试次数
     * @param cycleRetryTimes 循环重试次数
     * @param retrySleepTime  重试时间间隔
     * @param sleepTime       请求时间间隔
     * @param timeOut         超时时间
     * @param menus           菜单
     */
    public SimpleClosingProcessor(int retryTimes,
                                  int cycleRetryTimes,
                                  int retrySleepTime,
                                  int sleepTime,
                                  int timeOut,
                                  String proxyIp,
                                  int proxyPort,
                                  List<Menu> menus) {

        site = Site.me()
                .setRetryTimes(retryTimes)
                .setCycleRetryTimes(cycleRetryTimes)
                .setRetrySleepTime(retrySleepTime)
                .setSleepTime(sleepTime)
                .setTimeOut(timeOut)
                .setDomain("www.amazon.com");
    }

    /**
     * 停止爬虫
     */
    public static void stopSpider() {
        if (spider != null && spider.getStatus() == Spider.Status.Running) {
            SimpleClosingProcessor.spiderConfig = null;
            spider.stop();
        }
    }

    /**
     * 启动定时器
     *
     * @param spiderConfig
     */
    public static void enableSchedule(SpiderConfig spiderConfig) {
        SimpleClosingProcessor.spiderConfig = spiderConfig;
    }

    /**
     * 每天爬取一次
     */
    @Scheduled(cron = "0 0/1 * * * *")
    public static void callStartCrawl() {
        if (spiderConfig != null) {
            startCrawl(spiderConfig);
        }
    }

    /**
     * 查询爬虫状态
     */
    public static Spider.Status getSpiderStatus() {
        if (spider == null) {
            return null;
        } else {
            return spider.getStatus();
        }
    }

    @Override
    public void process(Page page) {
        // 保证页面和网址非空
        if (page == null || page.getUrl() == null) {
            return;
        }

        if (page.getUrl().regex("https://www.amazon.com/.*?pg=[1-5]$").toString() != null) {
            // 获得当前的页面对应的菜单
            String url = page.getUrl().toString();

            // 1-5页
            Menu menu = url2Menu.get(url.substring(0, url.length() - 5));

            // 解析简单服装的信息
            List<String> divs = page.getHtml().xpath("//div[@class='zg_itemImmersion']").all();

            // 如果没有内容,直接退出
            if (divs == null || divs.size() == 0) {
                divs = page.getHtml().xpath("//li[@class='zg-item-immersion']").all();
                if (divs == null || divs.size() == 0) {
                    return;
                } else {
                    List<SimpleClosing> simpleClosings = new ArrayList<SimpleClosing>();
                    divs.forEach((div) -> {
                        // 创建对象
                        SimpleClosing simpleClosing = new SimpleClosing(menu);
                        String closingUrl = new Html(div).xpath("//span/a[@class='a-link-normal']/@href").get();

                        // 1.rank
                        String rankStr = new Html(div).xpath("//span[@class='zg-badge-text']/text()").get();
                        if (rankStr != null) {
                            try {
                                int rank = Integer.parseInt(rankStr.replace(".", "").replace("#", "").trim());
                                simpleClosing.setRank(rank);
                            } catch (Exception e) {
                            }
                        }

                        // 2.名称
                        String title = new Html(div).xpath("//div[@class='p13n-sc-truncated']/text()").get();
                        if (title != null) {
                            simpleClosing.setName(title);
                        }

                        // 3.星: 4.2 out of 5 stars
                        String starStr = new Html(div).xpath("//div[@class='a-icon-row a-spacing-none']/a[@class='a-link-normal']/@title").get();
                        try {
                            double star = Double.parseDouble(starStr.replace("out of 5 stars", "").trim());
                            simpleClosing.setStar(star);
                        } catch (Exception e) {
                        }

                        // 4.评论总数
                        String reviewNumStr = new Html(div).xpath("//div[@class='a-icon-row a-spacing-none']/a[@class='a-size-small a-link-normal']/text()").get();
                        try {
                            long reviewNum = Long.parseLong(reviewNumStr.replace(",", "").trim());
                            simpleClosing.setReviewNum(reviewNum);
                        } catch (Exception e) {
                        }

                        // 5.价钱
                        String priceStr = new Html(div).xpath("//span[@class='p13n-sc-price']/text()").get();
                        InfoParseUtil.parsePrice(priceStr, simpleClosing);

                        // 6.图片地址
                        String imageSrc = new Html(div).xpath("//div[@class='a-section a-spacing-small']/img/@src").get();
                        if (imageSrc != null) {
                            simpleClosing.setImageUrl(imageSrc);
                        }

                        simpleClosings.add(simpleClosing);
                    });
                    page.putField("simpleClosings", simpleClosings);
                }
            } else {
                // 存储所有信息
                List<SimpleClosing> simpleClosings = new ArrayList<SimpleClosing>();
                divs.forEach((div) -> {

                    // 创建对象
                    SimpleClosing simpleClosing = new SimpleClosing(menu);

                    // 1.排名
                    String rankStr = new Html(div).xpath("//span[@class='zg_rankNumber']/text()").get();
                    if (rankStr != null) {
                        try {
                            int rank = Integer.parseInt(rankStr.replace(".", "").trim());
                            simpleClosing.setRank(rank);
                        } catch (Exception e) {
                        }
                    }

                    // 2.名称
                    String title = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='p13n-sc-truncated']/@title").get();
                    String text = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='p13n-sc-truncated']/text()").get();

                    if (title == null) {
                        simpleClosing.setName(text);
                    } else if (text == null) {
                        simpleClosing.setName(title);
                    } else if (title.length() > text.length()) {
                        simpleClosing.setName(title);
                    } else {
                        simpleClosing.setName(text);
                    }

                    // 3.星: 4.2 out of 5 stars
                    String starStr = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/div[@class='a-icon-row a-spacing-none']/a[@class='a-link-normal']/@title").get();
                    try {
                        double star = Double.parseDouble(starStr.replace("out of 5 stars", "").trim());
                        simpleClosing.setStar(star);
                    } catch (Exception e) {
                    }

                    // 4.评论总数
                    String reviewNumStr = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/div[@class='a-icon-row a-spacing-none']/a[@class='a-size-small a-link-normal']/text()").get();
                    try {
                        long reviewNum = Long.parseLong(reviewNumStr.replace(",", "").trim());
                        simpleClosing.setReviewNum(reviewNum);
                    } catch (Exception e) {
                    }

                    // 5.价钱
                    String priceStr = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/div[@class='a-row']//span[@class='p13n-sc-price']/text()").get();
                    InfoParseUtil.parsePrice(priceStr, simpleClosing);

                    // 6.图片地址
                    final String imageSrc = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='a-section a-spacing-mini']/img/@src").get();
                    simpleClosing.setImageUrl(imageSrc);

                    simpleClosings.add(simpleClosing);
                });
                page.putField("simpleClosings", simpleClosings);
            }
        }
    }

    /**
     * 保存文件
     *
     * @param img
     * @param fileName
     */
    public void savaImage(byte[] img, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            File file = new File(fileName);
            file.createNewFile();

            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(img);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 停止定时器
     */
    public static void stopSchedule() {
        SimpleClosingProcessor.spiderConfig = null;
    }

    /**
     * 启动爬虫
     *
     * @param spiderConfig
     */
    public static void startCrawl(SpiderConfig spiderConfig) {

        // 拼接url
        List<String> urlList = new ArrayList<String>();

        url2Menu.clear();
        spiderConfig.getMenus().forEach((menu) -> {
            String url = menu.getUrl();
            url2Menu.put(url, menu);

            for (int i = 1; i <= 5; i++) {
                urlList.add(url + "?pg=" + i);
            }
        });

        ClosingSeleniumDownloader closingSeleniumDownloader = new ClosingSeleniumDownloader(spiderConfig.getChromeDriverPath());
        closingSeleniumDownloader.setSleepTime(spiderConfig.getSleepTime());
        closingSeleniumDownloader.setThread(spiderConfig.getThreadNum());

        spider = Spider.create(new SimpleClosingProcessor(spiderConfig.getRetryTimes(), spiderConfig.getCycleRetryTimes(),
                spiderConfig.getRetrySleepTime(), spiderConfig.getSleepTime(), spiderConfig.getTimeOut(),
                spiderConfig.getProxyIp(), spiderConfig.getProxyPort(), spiderConfig.getMenus()))
                .addPipeline(new SimpleClosingPipeline())
                .setScheduler(new PriorityScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .setDownloader(closingSeleniumDownloader)
                .addUrl(urlList.toArray(new String[1]))
                .thread(spiderConfig.getThreadNum());

        spider.runAsync();
    }
}
