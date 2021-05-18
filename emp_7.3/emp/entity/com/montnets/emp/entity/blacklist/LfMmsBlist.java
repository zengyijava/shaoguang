package com.montnets.emp.entity.blacklist;

import java.sql.Timestamp;

/**
 * TableLfBlacklist对应的实体类
 * 
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:22:57
 * @description
 */
public class LfMmsBlist implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3630157867154308016L;
	//主键
	private Long blId;  
	//电话号码
	private String phone;   
	//状态
	private Integer blState;   
	//备注
	private String comments;  
	//创建 人
	private String userId;  
	//通道号码
	private String spgate;   
	//通道号+扩展号
	private String spnumber;
	//操作时间
	private Timestamp optime;
	//0移动，1联通，21电信
	private Integer spisuncm;   
	//业务编码
	private String busCode;  
	//企业编码
	private String corpCode;   

	public LfMmsBlist()
	{
	}

	public Long getBlId()
	{
		return blId;
	}

	public String getBusCode()
	{
		return busCode;
	}

	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}

	public void setBlId(Long blId)
	{
		this.blId = blId;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public Integer getBlState()
	{
		return blState;
	}

	public void setBlState(Integer blState)
	{
		this.blState = blState;
	}

	public String getComments()
	{
		return comments;
	}

	public void setComments(String comments)
	{
		this.comments = comments;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}

	public Timestamp getOptime()
	{
		return optime;
	}

	public void setOptime(Timestamp optime)
	{
		this.optime = optime;
	}

	public Integer getSpisuncm()
	{
		return spisuncm;
	}

	public void setSpisuncm(Integer spisuncm)
	{
		this.spisuncm = spisuncm;
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