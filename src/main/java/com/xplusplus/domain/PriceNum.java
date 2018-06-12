package com.xplusplus.domain;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 10:58 2018/4/10
 * @Modified By:
 */
public class PriceNum {
    private String interval;
    private int lowerNum = 0;
    private int higherNum = 0;
    private int basePrice = 0;

    public PriceNum(String interval) {
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public int getLowerNum() {
        return lowerNum;
    }

    public void setLowerNum(int lowerNum) {
        this.lowerNum = lowerNum;
    }

    public int getHigherNum() {
        return higherNum;
    }

    public void setHigherNum(int higherNum) {
        this.higherNum = higherNum;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(int basePrice) {
        this.basePrice = basePrice;
    }

    @Override
    public String toString() {
        return "PriceNum{" +
                "interval='" + interval + '\'' +
                ", lowerNum=" + lowerNum +
                ", higherNum=" + higherNum +
                ", basePrice=" + basePrice +
                '}';
    }
}
