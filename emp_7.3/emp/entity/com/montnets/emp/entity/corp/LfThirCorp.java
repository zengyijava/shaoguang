package com.montnets.emp.entity.corp;

import java.io.Serializable;

/**
 * @author hsh <lensener@foxmail.com>
 * @project montnets_entity
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-10-19 下午02:22:36
 * @description 企业与菜单的关联关系表，用于富信模块的权限控制
 */
public class LfThirCorp implements Serializable {


    private static final long serialVersionUID = 5649155564794214387L;

    /**
     * 标识ID
     */
    private Long id;

    /**
     * 一级菜单标识
     */
    private Integer menuNum;

    /**
     * 企业id
     */
    private String corpCode;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMenuNum() {
        return menuNum;
    }

    public void setMenuNum(Integer menuNum) {
        this.menuNum = menuNum;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    @Override
    public String toString() {
        return "LfThirCorp{" +
                "id=" + id +
                ", menuNum=" + menuNum +
                ", corpCode=" + corpCode +
                '}';
    }
}
