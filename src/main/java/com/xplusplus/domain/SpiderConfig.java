package com.xplusplus.domain;

import java.util.List;

/**
 * @Author: zhouweixin
 * @Description: 线程配置信息
 * @Date: Created in 17:58 2018/4/5
 * @Modified By:
 */
public class SpiderConfig implements Cloneable{
    /** 下载开始时钟*/
    private Long startTime;
    /** 结束时钟*/
    private Long endTime;
    /** 总数目*/
    private int sum;
    /** 开始数目*/
    private Long startId;
    private Long endId;
    private int code;
    private String message;

    /** 重试次数*/
    private Integer retryTimes = 2;

    /** 循环重试次数*/
    private Integer cycleRetryTimes = 2;

    /** 重试间隔时间*/
    private Integer retrySleepTime = 2000;

    /** 线程数*/
    private Integer threadNum = 1;

    /** 请求间隔时间*/
    private Integer sleepTime = 2000;

    /** 超时时间*/
    private Integer timeOut = 10000;

    /** 域*/
    private String domain = "www.amazon.com";

    /** 启动代理*/
    private Boolean enableProxy = false;

    /** 代理IP*/
    private String proxyIp = "";

    /** 代理端口号*/
    private Integer proxyPort = 80;

    /** 开始url*/
    private String startUrl = "https://www.amazon.com/Best-Sellers/zgbs/fashion";

    /** 菜单*/
    private List<Menu> menus;

    private String chromeDriverPath = "chromedriver.exe";

    public Long getEndId() {
        return endId;
    }

    public void setEndId(Long endId) {
        this.endId = endId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public Long getStartId() {
        return startId;
    }

    public void setStartId(Long startId) {
        this.startId = startId;
    }

    public Integer getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Integer getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    public void setCycleRetryTimes(Integer cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
    }

    public Integer getRetrySleepTime() {
        return retrySleepTime;
    }

    public void setRetrySleepTime(Integer retrySleepTime) {
        this.retrySleepTime = retrySleepTime;
    }

    public Integer getThreadNum() {
        return threadNum;
    }

    public void setThreadNum(Integer threadNum) {
        this.threadNum = threadNum;
    }

    public Integer getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(Integer sleepTime) {
        this.sleepTime = sleepTime;
    }

    public Integer getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(Integer timeOut) {
        this.timeOut = timeOut;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Boolean getEnableProxy() {
        return enableProxy;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public String getProxyIp() {
        return proxyIp;
    }

    public void setProxyIp(String proxyIp) {
        this.proxyIp = proxyIp;
    }

    public Integer getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(Integer proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getStartUrl() {
        return startUrl;
    }

    public void setStartUrl(String startUrl) {
        this.startUrl = startUrl;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public String getChromeDriverPath() {
        return chromeDriverPath;
    }

    public void setChromeDriverPath(String chromeDriverPath) {
        this.chromeDriverPath = chromeDriverPath;
    }

    @Override
    public SpiderConfig clone()  {
        try {
            return (SpiderConfig)super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "SpiderConfig{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", sum=" + sum +
                ", startId=" + startId +
                ", endId=" + endId +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", retryTimes=" + retryTimes +
                ", cycleRetryTimes=" + cycleRetryTimes +
                ", retrySleepTime=" + retrySleepTime +
                ", threadNum=" + threadNum +
                ", sleepTime=" + sleepTime +
                ", timeOut=" + timeOut +
                ", domain='" + domain + '\'' +
                ", enableProxy=" + enableProxy +
                ", proxyIp='" + proxyIp + '\'' +
                ", proxyPort=" + proxyPort +
                ", startUrl='" + startUrl + '\'' +
                ", menus=" + menus +
                ", chromeDriverPath='" + chromeDriverPath + '\'' +
                '}';
    }
}
