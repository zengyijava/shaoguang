package com.montnets.emp.translog.entity;

import java.sql.Timestamp;


/**
 * @author lianggp
 * @datetime 2021-1-27
 * @description 汇总日志实体类
 */
public class LfTransLog implements java.io.Serializable {

    private Long id;
    // 执行时间
    private Timestamp createtime;
    // 汇总类型
    private String usetype;
    // 汇总名称
    private String transname;
    // 日志内容
    private String tstatus;
    // 执行标志 1：结束  0：开始
    private Integer runflag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Timestamp getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Timestamp createtime) {
        this.createtime = createtime;
    }

    public String getUsetype() {
        return usetype;
    }

    public void setUsetype(String usetype) {
        this.usetype = usetype;
    }

    public String getTransname() {
        return transname;
    }

    public void setTransname(String transname) {
        this.transname = transname;
    }

    public String getTstatus() {
        return tstatus;
    }

    public void setTstatus(String tstatus) {
        this.tstatus = tstatus;
    }

    public Integer getRunflag() {
        return runflag;
    }

    public void setRunflag(Integer runflag) {
        this.runflag = runflag;
    }

}
