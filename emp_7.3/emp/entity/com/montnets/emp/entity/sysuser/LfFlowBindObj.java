/**
 * 
 */
package com.montnets.emp.entity.sysuser;

import java.sql.Timestamp;


/**
 * 审核对象管理表
 * @project p_tabl
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-7-19 上午11:06:51
 * @description
 */
public class LfFlowBindObj implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6202460836205890184L;

	
	//主键，自增id
	private Long id;
	//审核流ID，应用lf_flow的fid
	private Long FId;
	//被审核人类型，1-操作员，2--机构
	private Integer ObjType;
	//操作员或机构唯一编码，TYPE为1时，此字段为操作员，2时为机构编码
	private String ObjCode;
	//修改时间
	private Timestamp updateTime;
	//新增时间
	private Timestamp createTime;
	//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；
	private Integer infoType;
	//企业编号
	private String corpCode;
    //是否包含子机构
    private Integer ctsubDep = 1;

    public Integer getCtsubDep() {
        return ctsubDep;
    }

    public void setCtsubDep(Integer ctsubDep) {
        this.ctsubDep = ctsubDep;
    }

    public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public Long getFId()
	{
		return FId;
	}
	public void setFId(Long fId)
	{
		FId = fId;
	}
	
	public Integer getObjType()
	{
		return ObjType;
	}
	public void setObjType(Integer objType)
	{
		ObjType = objType;
	}
	public String getObjCode()
	{
		return ObjCode;
	}
	public void setObjCode(String objCode)
	{
		ObjCode = objCode;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public Timestamp getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
	public Integer getInfoType()
	{
		return infoType;
	}
	public void setInfoType(Integer infoType)
	{
		this.infoType = infoType;
	}
	public String getCorpCode()
	{
		return corpCode;
	}
	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}
	
	
	
	
	
	

	
}
