package com.xplusplus.processor;

import com.xplusplus.domain.Closing;
import com.xplusplus.domain.Menu;
import com.xplusplus.domain.SpiderConfig;
import com.xplusplus.downloader.ClosingSeleniumDownloader;
import com.xplusplus.pipeline.ClosingPipeline;
import com.xplusplus.repository.ClosingRepository;
import com.xplusplus.utils.ApplicationContextProvider;
import com.xplusplus.utils.InfoParseUtil;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.*;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.PriorityScheduler;
import us.codecraft.webmagic.scheduler.component.HashSetDuplicateRemover;
import us.codecraft.webmagic.selector.Html;

import java.util.*;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 21:23 2018/4/2
 * @Modified By:
 */
@Component
public class ClosingProcessor implements PageProcessor {

    @Autowired
    private ClosingRepository closingRepository;

    // url -> closing
    private static Map<String, Closing> closingUrl2Closing = new HashMap<String, Closing>();

    // url -> menu
    private static Map<String, Menu> url2Menu = new HashMap<String, Menu>();

    // 爬虫对象
    private static Spider spider;

    // 站点
    private Site site = Site.me();

    // 菜单
    private List<Menu> menus;

    public static SpiderConfig spiderConfig = null;

    private static SpiderConfig scheduleSpiderConfig = null;

    public static final String HOST = "https://www.amazon.com";

    public ClosingProcessor() {
        url2Menu.clear();
    }

    /**
     * @param retryTimes      重试次数
     * @param cycleRetryTimes 循环重试次数
     * @param retrySleepTime  重试时间间隔
     * @param sleepTime       请求时间间隔
     * @param timeOut         超时时间
     * @param menus           菜单
     */
    public ClosingProcessor(int retryTimes,
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
            ClosingProcessor.spiderConfig = null;
            spider.stop();
        }
    }

    /**
     * 停止定时器
     */
    public static void stopSchedule() {
        ClosingProcessor.scheduleSpiderConfig = null;
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
        if (page == null || page.getRequest() == null || page.getUrl() == null) {
            System.out.println("跳过!");
            return;
        }

        if (page.getUrl().regex("https://www.amazon.com/.*#[1-5]$").toString() != null) {

            // 获得当前的页面对应的菜单
            String url = page.getUrl().toString();

            // 1-5页
            Menu menu = url2Menu.get(url.substring(0, url.length() - 2));

            // 解析简单服装的信息
            List<String> divs = page.getHtml().xpath("//div[@class='zg_itemImmersion']").all();

            // 如果没有内容,直接退出
            if (divs == null || divs.size() == 0) {
                return;
            }

            divs.forEach((div) -> {
                String closingUrl = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/@href").get();

                if (closingUrl != null) {

                    Closing closing = new Closing();

                    // 1.菜单
                    closing.setMenu(menu);

                    // 2.url
                    closingUrl = HOST + closingUrl;
                    closing.setUrl(closingUrl);

                    Request request = new Request(closingUrl);
                    request.setPriority(1);

                    // 3.rank
                    String rankStr = new Html(div).xpath("//span[@class='zg_rankNumber']/text()").get();
                    if (rankStr != null) {
                        try {
                            int rank = Integer.parseInt(rankStr.replace(".", "").trim());
                            closing.setRank(rank);
                        } catch (Exception e) {
                        }
                    }

                    // 4.mainImageUrl
                    String imageSrc = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='a-section a-spacing-mini']/img/@src").get();
                    if (imageSrc != null) {
                        closing.setMainImageUrl(imageSrc);
                    }

                    closingUrl2Closing.put(closingUrl, closing);

                    page.addTargetRequest(request);
                }
            });
        } else if (page.getUrl().regex("https://www.amazon.com/.*?pg=[1-5]$").toString() != null) {
            // 获得当前的页面对应的菜单
            String url = page.getUrl().toString();

            // 1-5页
            Menu menu = url2Menu.get(url.substring(0, url.length() - 5));

            // 解析简单服装的信息
            List<String> divs = page.getHtml().xpath("//div[@class='zg_itemImmersion']").all();

            // 如果没有内容,直接退出
            if (divs == null || divs.size() == 0) {
                divs = page.getHtml().xpath("//li[@class='zg-item-immersion']").all();
                if (divs == null || divs.size() == 0){
                    return;
                }else{
                    divs.forEach((div) -> {
                        String closingUrl = new Html(div).xpath("//span/a[@class='a-link-normal']/@href").get();

                        if (closingUrl != null) {

                            Closing closing = new Closing();

                            // 1.菜单
                            closing.setMenu(menu);

                            // 2.url
                            closingUrl = HOST + closingUrl;
                            closing.setUrl(closingUrl);

                            Request request = new Request(closingUrl);
                            request.setPriority(1);

                            // 3.rank
                            String rankStr = new Html(div).xpath("//span[@class='zg-badge-text']/text()").get();
                            if (rankStr != null) {
                                try {
                                    int rank = Integer.parseInt(rankStr.replace(".", "").replace("#", "").trim());
                                    closing.setRank(rank);
                                } catch (Exception e) {
                                }
                            }

                            // 4.mainImageUrl
                            String imageSrc = new Html(div).xpath("//div[@class='a-section a-spacing-small']/img/@src").get();
                            if (imageSrc != null) {
                                closing.setMainImageUrl(imageSrc);
                            }

                            closingUrl2Closing.put(closingUrl, closing);

                            page.addTargetRequest(request);
                        }
                    });
                }
            }else{
                divs.forEach((div) -> {
                    String closingUrl = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/@href").get();

                    if (closingUrl != null) {

                        Closing closing = new Closing();

                        // 1.菜单
                        closing.setMenu(menu);

                        // 2.url
                        closingUrl = HOST + closingUrl;
                        closing.setUrl(closingUrl);

                        Request request = new Request(closingUrl);
                        request.setPriority(1);

                        // 3.rank
                        String rankStr = new Html(div).xpath("//span[@class='zg_rankNumber']/text()").get();
                        if (rankStr != null) {
                            try {
                                int rank = Integer.parseInt(rankStr.replace(".", "").replace("#", "").trim());
                                closing.setRank(rank);
                            } catch (Exception e) {
                            }
                        }

                        // 4.mainImageUrl
                        String imageSrc = new Html(div).xpath("//div[@class='zg_itemWrapper']/div/a/div[@class='a-section a-spacing-mini']/img/@src").get();
                        if (imageSrc != null) {
                            closing.setMainImageUrl(imageSrc);
                        }

                        closingUrl2Closing.put(closingUrl, closing);

                        page.addTargetRequest(request);
                    }
                });
            }
        } else {
            String url = page.getUrl().toString();

            // 详细信息
            Closing closing = null;
            if (closingUrl2Closing.containsKey(url)) {
                closing = closingUrl2Closing.remove(url);
            } else {
                closing = new Closing();
            }

            //1.链接
            closing.setUrl(url);

            //3.品牌
            String brand = page.getHtml().xpath("//a[@id='bylineInfo']/text()").get();
            closing.setBrand(brand);

            //4.名称
            String name = page.getHtml().xpath("//span[@id='productTitle']/text()").get();
            if (name != null) {
                closing.setName(name.trim());
            }

            // 5.星
            String starStr = page.getHtml().xpath("//div[@id='averageCustomerReviews']//span[@class='a-icon-alt']/text()").get();
            try {
                double star = Double.parseDouble(starStr.replace("out of 5 stars", "").trim());
                closing.setStar(star);
            } catch (Exception e) {
            }

            // 6.评论总数
            String reviewNumStr = page.getHtml().xpath("//div[@id='averageCustomerReviews']//span[@id='acrCustomerReviewText']/text()").get();
            try {
                Long reviewNum = Long.parseLong(reviewNumStr.replace("customer reviews", "").replace(",", "").trim());
                closing.setReviewNum(reviewNum);
            } catch (Exception e) {
            }

            // 7.回答总数
            String answerNumStr = page.getHtml().xpath("//div[@id='ask_feature_div']//span[@class='a-size-base']/text()").get();
            try {
                Long answerNum = Long.parseLong(answerNumStr.replace("answered questions", "").trim());
                closing.setAnswerNum(answerNum);
            } catch (Exception e) {
            }

            // 8.价钱
            String priceStr = page.getHtml().xpath("//span[@id='priceblock_ourprice']/text()").get();
            InfoParseUtil.parsePrice(priceStr, closing);

            // 9.fit
            String fitStr = page.getHtml().xpath("//div[@id='twisterContainer']//span[@id='fitRecommendationsLinkRatingText']/text()").get();
            try {
                double fit = 0.01 * Double.parseDouble(fitStr.replace("As expected", "").replace("(", "").replace(")", "").replace("%", "").trim());
                closing.setFit(fit);
            } catch (Exception e) {
            }

            // 10.大小size
            List<String> list = page.getHtml().xpath("//form[@id='twister']//select[@id='native_dropdown_selected_size_name']/option/text()").all();
            if (list != null && list.size() > 1) {
                list.remove(0);
                closing.setSize(list.toString());
            }

            // 11.颜色
            List<String> colors = page.getHtml().xpath("//form[@id='twister']/div[@id='variation_color_name']/ul/li//img[@class='imgSwatch']/@alt").all();
            if (colors != null) {
                closing.setColor(colors.toString());
            }

            // 12.材质
            String materialStr = page.getHtml().xpath("//div[@id='featurebullets_feature_div']/div[@id='feature-bullets']/ul").get();
            try {
                materialStr = Jsoup.parse(materialStr).text();
                closing.setMaterialQuality(InfoParseUtil.matchMaterial(materialStr));
            } catch (Exception e) {
            }

            // 13.产品大小
            // 14.产品重量
            // 15.ASIN
            // 16.第一次支持亚马逊的时间
            // 17.商品模型编号
            List<String> lis = page.getHtml().xpath("//div[@id='detailBullets_feature_div']/ul/li").all();
            if (lis != null && lis.size() > 0) {
                for (String li : lis) {
                    try {
                        String text = Jsoup.parse(li).text();
                        InfoParseUtil.parseOthers(text, closing);
                    } catch (Exception e) {
                    }
                }
            } else {
                lis = page.getHtml().xpath("//div[@class='content']/ul/li").all();
                if (lis != null && lis.size() > 0) {
                    for (String li : lis) {
                        try {
                            String text = Jsoup.parse(li).text();
                            InfoParseUtil.parseOthers(text, closing);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            // 18.主图
            // 19.第二张图
            List<String> srcs = page.getHtml().xpath("//div[@id='altImages']/ul/li//img/@src").all();
            if (srcs != null) {
                if (srcs.size() > 1) {
                    if (closing.getMainImageUrl() == null) {
                        closing.setMainImageUrl(srcs.get(0));
                    }
                    closing.setSecondImageUrl(srcs.get(1));
                } else if (srcs.size() == 1) {
                    if (closing.getMainImageUrl() == null) {
                        closing.setMainImageUrl(srcs.get(0));
                    }
                }
            }

            // 20.竞争指数 TODO

            // 21.下载日期
            closing.setDownloadDate(new Date());

            // 22.要点
            List<String> bullets = page.getHtml().xpath("//div[@id='feature-bullets']/ul/li/span/text()").all();
            if (bullets != null && bullets.size() > 0) {
                String bulletStr = "";
                for (String bullet : bullets) {
                    bulletStr += bullet + ";";
                }
                closing.setBulletPoints(bulletStr);
            }

            // 23.产品描述
            String description = page.getHtml().xpath("//div[@id='productDescription_feature_div']").get();
            if (description != null) {
                try {
                    description = Jsoup.parse(description).text().replace("Product description", "").trim();
                    closing.setDescription(description);
                } catch (Exception e) {
                }
            }

            page.putField("closing", closing);

        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    /**
     * 启动定时器
     *
     * @param scheduleSpiderConfig
     */
    public static void enableSchedule(SpiderConfig scheduleSpiderConfig) {
        ClosingProcessor.scheduleSpiderConfig = scheduleSpiderConfig;
    }

    /**
     * 每天0点爬一次
     */
    @Scheduled(cron = "0 0 0 * * *")
    public static void callStartCrawl() {
        if (scheduleSpiderConfig != null) {
            System.out.println("====================closing定时器触发===================");

            Spider.Status status = ClosingProcessor.getSpiderStatus();
            if (status != null && (status == Spider.Status.Init || status == Spider.Status.Running)) {
                System.out.println("====================正在下载, 跳过本次下载===================");
                return;
            }

            startCrawl(scheduleSpiderConfig);
        }
    }

    /**
     * 配置爬虫信息
     *
     * @param spiderConfig
     */
    public static void config(SpiderConfig spiderConfig){
        if(spiderConfig == null){
            return;
        }

        // 开始下载的时间
        spiderConfig.setStartTime(System.currentTimeMillis());
        ClosingRepository closingRepository = (ClosingRepository)ApplicationContextProvider.getApplicationContext().getBean("closingRepository");

        // 开始Id
        Long idMax = closingRepository.findIdMax();
        spiderConfig.setStartId(idMax);

        // 总数目
        spiderConfig.setSum(spiderConfig.getMenus().size() * 100);
    }

    /**
     * 启动爬虫
     *
     * @param spiderConfig
     */
    public static void startCrawl(SpiderConfig spiderConfig) {
        config(spiderConfig);
        ClosingProcessor.spiderConfig = spiderConfig;

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

        spider = Spider.create(new ClosingProcessor(spiderConfig.getRetryTimes(), spiderConfig.getCycleRetryTimes(),
                spiderConfig.getRetrySleepTime(), spiderConfig.getSleepTime(), spiderConfig.getTimeOut(),
                spiderConfig.getProxyIp(), spiderConfig.getProxyPort(), spiderConfig.getMenus()))
                .addPipeline(new ClosingPipeline())
                .setScheduler(new PriorityScheduler().setDuplicateRemover(new HashSetDuplicateRemover()))
                .setDownloader(closingSeleniumDownloader)
                .addUrl(urlList.toArray(new String[1]))
                .thread(spiderConfig.getThreadNum());

        spider.runAsync();
    }
}
