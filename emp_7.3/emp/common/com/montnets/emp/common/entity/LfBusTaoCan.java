package com.montnets.emp.common.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/***
 *  套餐基本信息表(LF_BUSTAOCAN)
 * @author Administrator
 *
 */
public class LfBusTaoCan implements Serializable{
    //业务套餐ID，自增
    private Long taocan_id;
    //套餐编码，唯一
    private String taocan_code;
    //套餐名称
    private String taocan_name;
    //套餐描述
    private String taocan_des;
    //启用状态
    private Integer state;
    //企业编码
    private String corp_code;
    //创建时间
    private Timestamp create_time;
    //修改时间
    private Timestamp update_time;
    //操作员机构ID
    private  Long dep_id;
    //操作员ID
    private Long user_id;
    //套餐开始日期
    private Timestamp start_date;
    //套餐截止日期
    private Timestamp end_date;
    //套餐类型（0：免费1：包月；2：包年；）
    private Integer taocan_type;
    //资费
    private Long taocan_money;
    //签约用户数


    public String getTaocan_code() {
        return taocan_code;
    }
    public void setTaocan_code(String taocanCode) {
        taocan_code = taocanCode;
    }
    public String getTaocan_name() {
        return taocan_name;
    }
    public void setTaocan_name(String taocanName) {
        taocan_name = taocanName;
    }
    public String getTaocan_des() {
        return taocan_des;
    }
    public void setTaocan_des(String taocanDes) {
        taocan_des = taocanDes;
    }
    public Integer getState() {
        return state;
    }
    public void setState(Integer state) {
        this.state = state;
    }
    public Timestamp getCreate_time() {
        return create_time;
    }
    public void setCreate_time(Timestamp createTime) {
        create_time = createTime;
    }
    public Timestamp getUpdate_time() {
        return update_time;
    }
    public void setUpdate_time(Timestamp updateTime) {
        update_time = updateTime;
    }
    public Timestamp getStart_date() {
        return start_date;
    }
    public void setStart_date(Timestamp startDate) {
        start_date = startDate;
    }
    public Timestamp getEnd_date() {
        return end_date;
    }
    public void setEnd_date(Timestamp endDate) {
        end_date = endDate;
    }

    public Integer getTaocan_type() {
        return taocan_type;
    }
    public void setTaocan_type(Integer taocanType) {
        taocan_type = taocanType;
    }
    public Long getTaocan_id() {
        return taocan_id;
    }
    public void setTaocan_id(Long taocanId) {
        taocan_id = taocanId;
    }

    public String getCorp_code() {
        return corp_code;
    }
    public void setCorp_code(String corpCode) {
        corp_code = corpCode;
    }

    public Long getDep_id() {
        return dep_id;
    }
    public void setDep_id(Long depId) {
        dep_id = depId;
    }
    public Long getUser_id() {
        return user_id;
    }
    public void setUser_id(Long userId) {
        user_id = userId;
    }
    public Long getTaocan_money() {
        return taocan_money;
    }
    public void setTaocan_money(Long taocanMoney) {
        taocan_money = taocanMoney;
    }
}
