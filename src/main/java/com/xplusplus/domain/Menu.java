package com.xplusplus.domain;

import javax.persistence.*;

/**
 * @Author: zhouweixin
 * @Description: 菜单
 * @Date: Created in 11:10 2018/4/3
 * @Modified By:
 */
@Entity
public class Menu {
    /** 编码*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 名称*/
    private String name;

    /** 地址*/
    @Column(name = "url", unique = true)
    private String url;

    /** 菜单*/
    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Menu parentMenu;

    @Transient
    private String parentUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Menu getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(Menu parentMenu) {
        this.parentMenu = parentMenu;
    }

    public String getParentUrl() {
        return parentUrl;
    }

    public void setParentUrl(String parentUrl) {
        this.parentUrl = parentUrl;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", parentMenu=" + parentMenu +
                ", parentUrl='" + parentUrl + '\'' +
                '}';
    }
}
