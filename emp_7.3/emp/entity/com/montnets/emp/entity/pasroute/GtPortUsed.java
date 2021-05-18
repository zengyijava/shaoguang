package com.montnets.emp.entity.pasroute;

 

/**
 * TableGtPortUsed对应的实体类
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:18:09
 * @description 
 */
public class GtPortUsed  implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1587721156264950540L;
	//自增ID
	private Long id;
	//网关类型  默认从通道中继承
	private Integer gateType;
	//SP通道号（与通道表建立外键关联）
	private String spgate;
	//SP通道  扩展通道号
	private String cpno;
	//端口属性(0:可扩展,1:独占该端口,2:共享端口)
	private Integer portType;
	//指令代码（对上行路由设置有效）
	private String userCode;
	////0移动，1联通，21电信
	private Integer spisuncm;
	//路由标志(0:上下行路由不分 1:下行路由 2:上行路由)
	private Integer routeFlag;
	//状态
	private Integer status;
	//
	//private Integer isExchannel;
	private String userId;
	//
	private String loginId;
	//
	private String spnumber;
	
	//private Integer fixedFee;
	private Integer feeFlag;
	
	//private Integer fee;
	//private String khdate;
	//private String userInfo;
	private String memo;
	
	//private String spnumber11;
	//private String validDate;
	//private Integer warnDays;
	//private Integer moneys;
	
	private String signstr;
	
	private Integer signlen;
	
	private Integer maxwords;
	
	private Integer singlelen;
	
	private Integer multilen1;
	
	private Integer multilen2;
	
	//private String forbidTimeStart;
	//private String forbidTimeEnd;
	
	private String sendTimeBegin;
	
	private String sendTimeEnd;
	private String ensignstr;
	private Integer ensignlen;
	private Integer enmaxwords;
	private Integer ensinglelen;
	private Integer enmultilen1;
	private Integer enmultilen2;
	
	public GtPortUsed(){
		this.spgate=" ";
		this.cpno =" ";
		this.portType=new Integer(0);
		this.userCode=" ";
		this.spisuncm=new Integer(0);
		this.routeFlag=new Integer(0);
		this.status=new Integer(0);
		//this.isExchannel=new Integer(0);
		this.userId=" ";
		this.loginId=" ";
		this.spnumber=" ";
		//this.fixedFee=new Integer(0);
		this.feeFlag=1;
		//this.fee = new Integer(0);
		//this.khdate =" ";
		//this.userInfo="67";
		this.memo=" ";
		//this.spnumber11=" ";
		//this.validDate=" ";
		//this.warnDays=new Integer(3);
		//this.moneys=new Integer(0);
		this.signstr=" ";
		this.signlen=70;
		this.maxwords=360;
		this.singlelen=70;
		this.multilen1=67;
		this.multilen2=57;
		//this.forbidTimeStart="00:00:00";
		//this.forbidTimeEnd="00:00:00";
		this.sendTimeBegin="00:00:00";
		this.sendTimeEnd="23:59:59";
		this.ensignstr = " ";
		this.ensignlen = 10;
		this.enmaxwords = 2000;
		this.ensinglelen = 160;
		this.enmultilen1 = 154;
		this.enmultilen2 = 134;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Integer getGateType()
	{
		return gateType;
	}

	public void setGateType(Integer gateType)
	{
		this.gateType = gateType;
	}

	public String getSpgate()
	{
		return spgate;
	}

	public void setSpgate(String spgate)
	{
		this.spgate = spgate;
	}

	public String getCpno()
	{
		return cpno;
	}

	public void setCpno(String cpno)
	{
		this.cpno = cpno;
	}

	public Integer getPortType()
	{
		return portType;
	}

	public void setPortType(Integer portType)
	{
		this.portType = portType;
	}

	public String getUserCode()
	{
		return userCode;
	}

	public void setUserCode(String userCode)
	{
		this.userCode = userCode;
	}

	public Integer getSpisuncm()
	{
		return spisuncm;
	}

	public void setSpisuncm(Integer spisuncm)
	{
		this.spisuncm = spisuncm;
	}

	public Integer getRouteFlag()
	{
		return routeFlag;
	}

	public void setRouteFlag(Integer routeFlag)
	{
		this.routeFlag = routeFlag;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getLoginId()
	{
		return loginId;
	}

	public void setLoginId(String loginId)
	{
		this.loginId = loginId;
	}

	public String getSpnumber()
	{
		return spnumber;
	}

	public void setSpnumber(String spnumber)
	{
		this.spnumber = spnumber;
	}

	public Integer getFeeFlag()
	{
		return feeFlag;
	}

	public void setFeeFlag(Integer feeFlag)
	{
		this.feeFlag = feeFlag;
	}

 

	public String getMemo()
	{
		return memo;
	}

	public void setMemo(String memo)
	{
		this.memo = memo;
	}

	public String getSignstr()
	{
		return signstr;
	}

	public void setSignstr(String signstr)
	{
		this.signstr = signstr;
	}

	public Integer getSignlen()
	{
		return signlen;
	}

	public void setSignlen(Integer signlen)
	{
		this.signlen = signlen;
	}

	public Integer getMaxwords()
	{
		return maxwords;
	}

	public void setMaxwords(Integer maxwords)
	{
		this.maxwords = maxwords;
	}

	public Integer getSinglelen()
	{
		return singlelen;
	}

	public void setSinglelen(Integer singlelen)
	{
		this.singlelen = singlelen;
	}

	public Integer getMultilen1()
	{
		return multilen1;
	}

	public void setMultilen1(Integer multilen1)
	{
		this.multilen1 = multilen1;
	}

	public Integer getMultilen2()
	{
		return multilen2;
	}

	public void setMultilen2(Integer multilen2)
	{
		this.multilen2 = multilen2;
	}

	public String getSendTimeBegin()
	{
		return sendTimeBegin;
	}

	public void setSendTimeBegin(String sendTimeBegin)
	{
		this.sendTimeBegin = sendTimeBegin;
	}

	public String getSendTimeEnd()
	{
		return sendTimeEnd;
	}

	public void setSendTimeEnd(String sendTimeEnd)
	{
		this.sendTimeEnd = sendTimeEnd;
	}

	public String getEnsignstr()
	{
		return ensignstr;
	}

	public void setEnsignstr(String ensignstr)
	{
		this.ensignstr = ensignstr;
	}

	public Integer getEnsignlen()
	{
		return ensignlen;
	}

	public void setEnsignlen(Integer ensignlen)
	{
		this.ensignlen = ensignlen;
	}

	public Integer getEnmaxwords()
	{
		return enmaxwords;
	}

	public void setEnmaxwords(Integer enmaxwords)
	{
		this.enmaxwords = enmaxwords;
	}

	public Integer getEnsinglelen()
	{
		return ensinglelen;
	}

	public void setEnsinglelen(Integer ensinglelen)
	{
		this.ensinglelen = ensinglelen;
	}

	public Integer getEnmultilen1()
	{
		return enmultilen1;
	}

	public void setEnmultilen1(Integer enmultilen1)
	{
		this.enmultilen1 = enmultilen1;
	}

	public Integer getEnmultilen2()
	{
		return enmultilen2;
	}

	public void setEnmultilen2(Integer enmultilen2)
	{
		this.enmultilen2 = enmultilen2;
	}
	
}