package com.xplusplus.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @Author: zhouweixin
 * @Description: 服装种类
 * @Date: Created in 8:16 2018/4/3
 * @Modified By:
 */
@Entity
public class Type {
    /** 编码*/
    @Id
    private Integer id;

    /** 链接地址*/
    private String url;

    /** 名称*/
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
