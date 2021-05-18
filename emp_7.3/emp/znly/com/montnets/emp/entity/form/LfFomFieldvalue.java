package com.montnets.emp.entity.form;

import java.sql.Timestamp;



/**
 * 实体类： LfFomFieldvalue
 *
 * @project p_fom
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfFomFieldvalue implements java.io.Serializable
{
   
    /**
     * @description  
     * @author yejiangmin <282905282@qq.com>
     * @datetime 2013-12-28 下午03:54:22
     */
    private static final long serialVersionUID = 357015680702979813L;
    
    // 自增ID
    private Long    vid;
   
    // 表单ID
    private Long    fid;
   
    // 问题ID
    private Long    qid;
   
    // 选项ID
    private Long    fieldId;
   
    // 选项值
    private Long    fieldValue;
   
    // 选项类型  1单选  2多选
    private String  fieldType;
   
    // 微信用户ID
    private String  wcId;
   
    //企业编码
    private String  corpCode;
   
    //创建时间
    private Timestamp   createtime;
    /**
     * 公众帐号ID
     */
    private Long   aid;

    public Long getVid()
    {
        return vid;
    }
    public void setVid(Long vid)
    {
        this.vid = vid;
    }
    public Long getFid()
    {
        return fid;
    }
    public void setFid(Long fid)
    {
        this.fid = fid;
    }
    public Long getQid()
    {
        return qid;
    }
    public void setQid(Long qid)
    {
        this.qid = qid;
    }
    public Long getFieldId()
    {
        return fieldId;
    }
    public void setFieldId(Long fieldId)
    {
        this.fieldId = fieldId;
    }
    public Long getFieldValue()
    {
        return fieldValue;
    }
    public void setFieldValue(Long fieldValue)
    {
        this.fieldValue = fieldValue;
    }
    public String getFieldType()
    {
        return fieldType;
    }
    public void setFieldType(String fieldType)
    {
        this.fieldType = fieldType;
    }
    public String getWcId()
    {
        return wcId;
    }
    public void setWcId(String wcId)
    {
        this.wcId = wcId;
    }
    public String getCorpCode()
    {
        return corpCode;
    }
    public void setCorpCode(String corpCode)
    {
        this.corpCode = corpCode;
    }
    public Timestamp getCreatetime()
    {
        return createtime;
    }
    public void setCreatetime(Timestamp createtime)
    {
        this.createtime = createtime;
    }
    public Long getAid()
    {
        return aid;
    }
    public void setAid(Long aid)
    {
        this.aid = aid;
    }

   
    
	

}