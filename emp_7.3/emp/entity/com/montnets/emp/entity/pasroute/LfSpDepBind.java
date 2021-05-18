/**
 * 
 */
package com.montnets.emp.entity.pasroute;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-30 下午04:59:54
 * @description TableLfImWg2emp对应的实体类
 */

public class LfSpDepBind implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6509512355238324016L;
	//标识列ID
	private Long dsgId;  
	//机构编码
	private String depCodeThird;  
	 // 网关账户标识列ID
	private String spUser;  
	//网关通道标识列ID
	private Long spgateId;  
	//操作员ID
	private Long userId;  
	//(0企业账户绑定  1-机构跟账户绑定   2-操作员跟账户绑定  3-业务账户绑定 ) 
	private Integer bindType;
	//分享类型 (0-共享 1-独占)
	private Integer shareType;
	/*菜单编码，标识模块的菜单唯一编码，如“1-1”，该编码的第一个数字表示模块id(RESOURCE_ID用作模块id)，
	即表示该菜单在“通道管理”模块（RESOURCE_ID为1）；第二个数字表示该模块下的第几个菜单，
  	所以“1-1”即为“通道管理”模块下的“通道设置”菜单
		*/
	private String menuCode;
	// 业务编码
	private String busCode;
	//选择绑定类型(0-全部 1-选择绑定)
	private Integer selBinType;
	//业务账户绑定类型  1-emp应用账号 2-emp接入账号 3-DBServer，emp指的是EMP平台的账号，DBSERVER指的是DB接入的账号
	private Integer platFormType;
	// 业务ID
	private Long busId;     
	//企业编码
	private String corpCode;
	//1- validate 0-invalidate
	private Integer isValidate; 
	
	public LfSpDepBind()
	{
	}

  
	public Long getDsgId()
	{
		return dsgId;
	}
 

	public void setDsgId(Long dsgId)
	{
		this.dsgId = dsgId;
	}





	public String getSpUser()
	{
		return spUser;
	}

	public void setSpUser(String spUser)
	{
		this.spUser = spUser;
	}

	public Long getSpgateId()
	{
		return spgateId;
	}

	public void setSpgateId(Long spgateId)
	{
		this.spgateId = spgateId;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	public Integer getBindType()
	{
		return bindType;
	}

	public void setBindType(Integer bindType)
	{
		this.bindType = bindType;
	}

	public Integer getShareType()
	{
		return shareType;
	}

	public void setShareType(Integer shareType)
	{
		this.shareType = shareType;
	}

	public String getDepCodeThird()
	{
		return depCodeThird;
	}

	public void setDepCodeThird(String depCodeThird)
	{
		this.depCodeThird = depCodeThird;
	}


	public String getMenuCode()
	{
		return menuCode;
	}


	public void setMenuCode(String menuCode)
	{
		this.menuCode = menuCode;
	}


	public String getBusCode()
	{
		return busCode;
	}


	public void setBusCode(String busCode)
	{
		this.busCode = busCode;
	}


	public Integer getSelBinType()
	{
		return selBinType;
	}


	public void setSelBinType(Integer selBinType)
	{
		this.selBinType = selBinType;
	}


	public Integer getPlatFormType()
	{
		return platFormType;
	}


	public void setPlatFormType(Integer platFormType)
	{
		this.platFormType = platFormType;
	}


	public Long getBusId()
	{
		return busId;
	}


	public void setBusId(Long busId)
	{
		this.busId = busId;
	}


	public String getCorpCode()
	{
		return corpCode;
	}


	public void setCorpCode(String corpCode)
	{
		this.corpCode = corpCode;
	}


	public Integer getIsValidate()
	{
		return isValidate;
	}


	public void setIsValidate(Integer isValidate)
	{
		this.isValidate = isValidate;
	}

 
}
