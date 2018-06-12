package com.xplusplus.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简略服装信息
 *
 * @Author: zhouweixin
 * @Description:
 * @Date: Created in 19:58 2018/4/4
 * @Modified By:
 */
@Entity
public class SimpleClosing {
    /** id*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 排名*/
    private Integer rank;

    /** 爬取日期*/
    @Temporal(TemporalType.DATE)
    private Date downloadDate;

    /** 名称*/
    private String name;

    /** 星*/
    private Double star;

    /** 评论数*/
    private Long reviewNum;

    /** 价钱*/
    private Double priceLower;
    private Double priceHigher;

    /** 图片网络地址*/
    private String imageUrl;

    /** 图片本地地址*/
    private String imagePath;

    /** 对应的类型*/
    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    public SimpleClosing(){

    }

    /**
     * 构造函数
     */
    public SimpleClosing(Menu menu){
        this.downloadDate = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddhhmmss.SSSSSSS");
        this.imagePath = "images/" + sdf.format(this.downloadDate) + ".jpg";

        if(menu != null) {
            this.menu = menu;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public Long getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(Long reviewNum) {
        this.reviewNum = reviewNum;
    }

    public Double getPriceLower() {
        return priceLower;
    }

    public void setPriceLower(Double priceLower) {
        this.priceLower = priceLower;
    }

    public Double getPriceHigher() {
        return priceHigher;
    }

    public void setPriceHigher(Double priceHigher) {
        this.priceHigher = priceHigher;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "SimpleClosing{" +
                "id=" + id +
                ", rank=" + rank +
                ", downloadDate=" + downloadDate +
                ", name='" + name + '\'' +
                ", star=" + star +
                ", reviewNum=" + reviewNum +
                ", priceLower=" + priceLower +
                ", priceHigher=" + priceHigher +
                ", imageUrl='" + imageUrl + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", menu=" + menu +
                '}';
    }
}
