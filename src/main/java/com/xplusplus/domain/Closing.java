package com.xplusplus.domain;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: zhouweixin
 * @Description: 服装
 * @Date: Created in 18:40 2018/3/31
 * @Modified By:
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"asin", "downloadDate"}))
public class Closing {

    /**
     * 主键， 自增长
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ASIN
     */
    private String asin;

    /**
     * 下载日期
     */
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date downloadDate;

    /**
     * 主图本地地址
     */
    private String mainImagePath;

    /**
     * 主图网络地址
     */
    private String mainImageUrl;

    /**
     * 第二张图本地地址
     */
    private String secondImagePath;

    /**
     * 第二张图网络地址
     */
    private String secondImageUrl;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 名称
     */
    private String name;

    /**
     * 星级
     */
    private Double star;

    /**
     * 价钱
     */
    @Column(precision = 2)
    private Double priceLower;
    @Column(precision = 2)
    private Double priceHigher;

    /**
     * 合适率
     */
    @Column(precision = 2)
    private Double fit;

    /**
     * 尺寸
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BLOB COMMENT '尺寸'")
    private String size;

    /**
     * 颜色
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BLOB COMMENT '颜色'")
    private String color;

    /**
     * 卖家排名
     */
    private Integer rank;

    /**
     * 排名上次数
     */
    private Integer rankDeta = 0;

    /**
     * 链接
     */
    private String url;

    /**
     * 评论数量
     */
    private Long reviewNum;

    /**
     * 回答问题个数
     */
    private Long answerNum;

    /**
     * 第一次支持亚马逊的日期
     */
    @Temporal(TemporalType.DATE)
    private Date firstAvailAmazonDate;

    /**
     * 材质
     */
    private String materialQuality;

    /**
     * 竞争指数=评价总数/(最后一个日期-第一个日期)
     */
    @Column(precision = 2)
    private Double competitionIndex;

    /**
     * 描述
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BLOB COMMENT '产品描述'")
    private String description;

    /**
     * Bullet Points
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "BLOB COMMENT '产品要点'")
    private String bulletPoints;

    /**
     * 商品模型编号
     */
    private String itemModelNumber;

    /**
     * 大小
     */
    private String productDimensions;

    /**
     * 重量
     */
    private String shippingWeight;

    /**
     * 菜单id
     */
    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;

    public Closing() {
    }

    public Closing(Menu menu) {
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public String getMainImagePath() {
        return mainImagePath;
    }

    public void setMainImagePath(String mainImagePath) {
        this.mainImagePath = mainImagePath;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getSecondImagePath() {
        return secondImagePath;
    }

    public void setSecondImagePath(String secondImagePath) {
        this.secondImagePath = secondImagePath;
    }

    public String getSecondImageUrl() {
        return secondImageUrl;
    }

    public void setSecondImageUrl(String secondImageUrl) {
        this.secondImageUrl = secondImageUrl;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Double getFit() {
        return fit;
    }

    public void setFit(Double fit) {
        this.fit = fit;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getRankDeta() {
        if(rankDeta == null){
            rankDeta = 0;
        }
        return rankDeta;
    }

    public void setRankDeta(Integer rankDeta) {
        this.rankDeta = rankDeta;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(Long reviewNum) {
        this.reviewNum = reviewNum;
    }

    public Long getAnswerNum() {
        return answerNum;
    }

    public void setAnswerNum(Long answerNum) {
        this.answerNum = answerNum;
    }

    public Date getFirstAvailAmazonDate() {
        return firstAvailAmazonDate;
    }

    public void setFirstAvailAmazonDate(Date firstAvailAmazonDate) {
        this.firstAvailAmazonDate = firstAvailAmazonDate;
    }

    public String getMaterialQuality() {
        return materialQuality;
    }

    public void setMaterialQuality(String materialQuality) {
        this.materialQuality = materialQuality;
    }

    public Double getCompetitionIndex() {
        return competitionIndex;
    }

    public void setCompetitionIndex(Double competitionIndex) {
        this.competitionIndex = competitionIndex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemModelNumber() {
        return itemModelNumber;
    }

    public void setItemModelNumber(String itemModelNumber) {
        this.itemModelNumber = itemModelNumber;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public String getProductDimensions() {
        return productDimensions;
    }

    public void setProductDimensions(String productDimensions) {
        this.productDimensions = productDimensions;
    }

    public String getShippingWeight() {
        return shippingWeight;
    }

    public void setShippingWeight(String shippingWeight) {
        this.shippingWeight = shippingWeight;
    }

    public String getBulletPoints() {
        return bulletPoints;
    }

    public void setBulletPoints(String bulletPoints) {
        this.bulletPoints = bulletPoints;
    }

    @Override
    public String toString() {
        return "Closing{" +
                "id=" + id +
                ", asin='" + asin + '\'' +
                ", downloadDate=" + downloadDate +
                ", mainImagePath='" + mainImagePath + '\'' +
                ", mainImageUrl='" + mainImageUrl + '\'' +
                ", secondImagePath='" + secondImagePath + '\'' +
                ", secondImageUrl='" + secondImageUrl + '\'' +
                ", brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", star=" + star +
                ", priceLower=" + priceLower +
                ", priceHigher=" + priceHigher +
                ", fit=" + fit +
                ", size='" + size + '\'' +
                ", color='" + color + '\'' +
                ", rank=" + rank +
                ", rankDeta=" + rankDeta +
                ", url='" + url + '\'' +
                ", reviewNum=" + reviewNum +
                ", answerNum=" + answerNum +
                ", firstAvailAmazonDate=" + firstAvailAmazonDate +
                ", materialQuality='" + materialQuality + '\'' +
                ", competitionIndex=" + competitionIndex +
                ", description='" + description + '\'' +
                ", bulletPoints='" + bulletPoints + '\'' +
                ", itemModelNumber='" + itemModelNumber + '\'' +
                ", productDimensions='" + productDimensions + '\'' +
                ", shippingWeight='" + shippingWeight + '\'' +
                ", menu=" + menu +
                '}';
    }
}
