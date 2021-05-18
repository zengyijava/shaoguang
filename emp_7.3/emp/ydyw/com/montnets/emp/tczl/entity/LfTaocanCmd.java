package com.montnets.emp.tczl.entity;

import java.sql.Timestamp;

/**
 *<p>project name p_ydyw</p>
 *<p>Title: LfTaocanCmd</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author dingzx
 * @date 2015-1-20上午10:08:51
 */
public class LfTaocanCmd implements java.io.Serializable
{

	/**serialVersionUID*/
	private static final long serialVersionUID = 8786250929453473481L;
	//自增id
	private Long id;
	//指令代码
	private String structcode;
	//套餐编号
	private String taocanCode;
	//套餐类型 （1：VIP免费；2：包月；3：包季；4：包年；）
	private Integer taocanType;
	//套餐金额
	private Long taocanMoney;
	//指令类型  0：订购；1：退订；2：全局查看；3：全局退订，4：签约；5：取消签约
	private Integer structType;
	//创建时间
	private Timestamp createTime;
	//更新时间
	private Timestamp updateTime;
	//操作员机构ID
	private Long depId;
	//操作员ID
	private Long userId;
	//企业编码
	private String corpCode;
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getStructcode()
	{
		return structcode;
	}
	public void setStructcode(String structcode)
	{
		this.structcode = structcode;
	}
	public String getTaocanCode()
	{
		return taocanCode;
	}
	public void setTaocanCode(String taocanCode)
	{
		this.taocanCode = taocanCode;
	}
	public Integer getTaocanType()
	{
		return taocanType;
	}
	public void setTaocanType(Integer taocanType)
	{
		this.taocanType = taocanType;
	}
	public Long getTaocanMoney()
	{
		return taocanMoney;
	}
	public void setTaocanMoney(Long taocanMoney)
	{
		this.taocanMoney = taocanMoney;
	}
	public Integer getStructType()
	{
		return structType;
	}
	public void setStructType(Integer structType)
	{
		this.structType = structType;
	}
	public Timestamp getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime()
	{
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime)
	{
		this.updateTime = updateTime;
	}
	public Long getDepId()
	{
		return depId;
	}
	public void setDepId(Long depId)
	{
		this.depId = depId;
	}
	public Long getUserId()
	{
		return userId;
	}
	public void setUserId(Long userId)
	{
		this.userId = userId;
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
