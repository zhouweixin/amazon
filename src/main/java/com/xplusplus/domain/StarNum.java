package com.xplusplus.domain;

/**
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 17:06 2018/4/8
 * @Modified By:
 */
public class StarNum {
    private Double star;
    private Integer num;

    public StarNum(){}

    public StarNum(Double star, Integer num){
        this.star = star;
        this.num = num;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "StarNum{" +
                "star=" + star +
                ", num=" + num +
                '}';
    }
}
