package com.montnets.emp.entity.site;

import java.sql.Timestamp;

/**
 * 实体类： LfSitPlant
 * 
 * @project p_sit
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class LfSitPlant implements java.io.Serializable
{

    /**
     * @description  serialVersionUID
     * @author fanglu <fanglu@montnets.com>
     * @datetime 2014-1-13 上午10:32:16
     */
    private static final long serialVersionUID = -5127801471754403987L;

    // 程序自增ID
    private Long      plantId;

    // 板块的类型（1：滚动head，2联系方式link，3：内容列表List，4底部bottom）
    private String    plantType;

    // 所属页面
    private Long      pageId;

    // 板块名称
    private String    name;

    // 板块表单标题IDS
    private String    feildNames;

    // 模板表单值（格式待定）
    private String    feildValues;

    // 企业编码（0表示系统默认分类）
    private String    corpCode;

    // 创建时间
    private Timestamp createtime;

    // 更新时间
    private Timestamp moditytime;

    // 微站编号
    private Long      sId;

    public Long getPlantId()
    {
        return plantId;
    }

    public void setPlantId(Long plantId)
    {
        this.plantId = plantId;
    }

    public String getPlantType()
    {
        return plantType;
    }

    public void setPlantType(String plantType)
    {
        this.plantType = plantType;
    }

    public Long getPageId()
    {
        return pageId;
    }

    public void setPageId(Long pageId)
    {
        this.pageId = pageId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getFeildNames()
    {
        return feildNames;
    }

    public void setFeildNames(String feildNames)
    {
        this.feildNames = feildNames;
    }

    public String getFeildValues()
    {
        return feildValues;
    }

    public void setFeildValues(String feildValues)
    {
        this.feildValues = feildValues;
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

    public Timestamp getModitytime()
    {
        return moditytime;
    }

    public void setModitytime(Timestamp moditytime)
    {
        this.moditytime = moditytime;
    }

    public Long getSId()
    {
        return sId;
    }

    public void setSId(Long sId)
    {
        this.sId = sId;
    }
}
