package com.montnets.emp.servmodule.txgl.entity;

import java.sql.Timestamp;

/**
 * @功能概要：
 * @项目名称： emp_std_192.169.1.81
 * @初创作者： Administrator
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016/4/20 16:02
 * <p>修改记录1：</p>
 * <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */
public class GWCluSpBind {
    private Integer id;
    private Integer ptaccuid;
    private Integer gwno;
    private Integer gweight;
    private Timestamp updtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPtaccuid() {
        return ptaccuid;
    }

    public void setPtaccuid(Integer ptaccuid) {
        this.ptaccuid = ptaccuid;
    }

    public Integer getGwno() {
        return gwno;
    }

    public void setGwno(Integer gwno) {
        this.gwno = gwno;
    }

    public Integer getGweight() {
        return gweight;
    }

    public void setGweight(Integer gweight) {
        this.gweight = gweight;
    }

    public Timestamp getUpdtime() {
        return updtime;
    }

    public void setUpdtime(Timestamp updtime) {
        this.updtime = updtime;
    }
}
