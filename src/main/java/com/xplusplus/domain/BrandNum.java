package com.xplusplus.domain;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 11:39 2018/4/10
 * @Modified By:
 */
public class BrandNum {
    private String brand;
    private Integer num;

    public BrandNum(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "BrandNum{" +
                "brand='" + brand + '\'' +
                ", num=" + num +
                '}';
    }
}
