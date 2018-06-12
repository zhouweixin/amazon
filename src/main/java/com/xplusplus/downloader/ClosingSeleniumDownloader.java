package com.xplusplus.downloader;

import org.apache.log4j.Logger;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 20:26 2018/4/8
 * @Modified By:
 */
public class ClosingSeleniumDownloader implements Downloader, Closeable {
    private volatile ClosingWebDriverPool webDriverPool;

    private Logger logger = Logger.getLogger(getClass());

    private int sleepTime = 0;

    private int poolSize = 1;

    private static final String DRIVER_PHANTOMJS = "phantomjs";

    /**
     * 新建
     *
     * @param chromeDriverPath chromeDriverPath
     */
    public ClosingSeleniumDownloader(String chromeDriverPath) {
        System.getProperties().setProperty("webdriver.chrome.driver",
                chromeDriverPath);
    }

    /**
     * Constructor without any filed. Construct PhantomJS browser
     *
     * @author bob.li.0718@gmail.com
     */
    public ClosingSeleniumDownloader() {
        // System.setProperty("phantomjs.binary.path",
        // "/Users/Bingo/Downloads/phantomjs-1.9.7-macosx/bin/phantomjs");
    }

    /**
     * set sleep time to wait until load success
     *
     * @param sleepTime sleepTime
     * @return this
     */
    public ClosingSeleniumDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }

    @Override
    public Page download(Request request, Task task) {
        Page page = Page.fail();

        if (task == null || task.getSite() == null) {
            throw new NullPointerException("task or site can not be null");
        }

        // 初始化线程池
        checkInit();

        // 定义驱动
        WebDriver webDriver = null;
        try {
            // 获取驱动
            webDriver = webDriverPool.get();
        } catch (InterruptedException e) {
            logger.warn("interrupted", e);
            return page;
        }

        if (request == null || request.getUrl().toString() == null) {
            logger.error("Error " + request.getUrl());
            webDriverPool.returnToPool(webDriver);
            return page;
        }

        // 开始下载页面
        logger.info("downloading page " + request.getUrl());
        webDriver.get(request.getUrl());

        // 延时
        delay(sleepTime);

        // 添加cookie
        WebDriver.Options manage = webDriver.manage();
        Site site = task.getSite();
        if (site.getCookies() != null) {
            for (Map.Entry<String, String> cookieEntry : site.getCookies()
                    .entrySet()) {
                Cookie cookie = new Cookie(cookieEntry.getKey(),
                        cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }

        /*
         * TODO You can add mouse event or other processes
         *
         * @author: bob.li.0718@gmail.com
         */

        // 取出网页
//        WebElement webElement = webDriver.findElement(By.xpath("/html"));
//
//        String content = webElement.getAttribute("outerHTML");

        String content = webDriver.getPageSource();
        if (content == null) {
            System.out.println("====================content is null, skip==================");
            logger.error("Error " + request.getUrl());
            webDriverPool.returnToPool(webDriver);
            return page;
        }

        page = new Page();
        page.setRawText(content);
        String url = request.getUrl();

        page.setHtml(new Html(content, url));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        webDriverPool.returnToPool(webDriver);
        return page;
    }

    private void checkInit() {
        if (webDriverPool == null) {
            synchronized (this) {
                webDriverPool = new ClosingWebDriverPool(poolSize);
            }
        }
    }

    /**
     * 延时
     *
     * @param sleepTime
     */
    private void delay(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setThread(int thread) {
        this.poolSize = thread;
    }

    @Override
    public void close() throws IOException {
        webDriverPool.closeAll();
    }
}
