/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午10:36:19
 */
package com.montnets.emp.entity.ydyw;

import java.sql.Timestamp;

/**
 * LF_CONTRACT_TAOCAN 签约客户套餐对应关系表
 * 
 * @description
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午10:36:19
 */

public class LfContractTaocan
{

	// 签约ID
	private Long		contractid;

	// 客户GUID
	private Long		guid;

	// 操作员ID
	private Long		userid;

	// 创建时间
	private Timestamp	createtime;

	// 签约操作员ID
	private Long		contractuser;

	// 扣款账号
	private String		debitaccount;

	// 更新时间
	private Timestamp	updatetime;

	// 企业编码
	private String		corpcode;

	// 资费
	private Long		taocanmoney;

	// 有效标识，0-正常 1-禁用
	private String		isvalid;

	// 操作员机构ID
	private Long		depid;

	// 套餐编号
	private String		taocancode;

	// 自增ID
	private Long		id;

	// 签约机构ID
	private Long		contractdep;

	// 套餐类型（1：VIP免费；2：包月；3：包季；4：包年；）
	private Integer		taocantype;

	// 扣费时间
	private Timestamp	bucklefeetime;
	
	private String taocanname;
	
	private String lastbucklefee;

	public LfContractTaocan()
	{
	}

	public Long getContractid()
	{

		return contractid;
	}

	public void setContractid(Long contractid)
	{

		this.contractid = contractid;

	}

	public Long getGuid()
	{

		return guid;
	}

	public void setGuid(Long guid)
	{

		this.guid = guid;

	}

	public Long getUserid()
	{

		return userid;
	}

	public void setUserid(Long userid)
	{

		this.userid = userid;

	}

	public Timestamp getCreatetime()
	{

		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{

		this.createtime = createtime;

	}

	public Long getContractuser()
	{

		return contractuser;
	}

	public void setContractuser(Long contractuser)
	{

		this.contractuser = contractuser;

	}

	public String getDebitaccount()
	{

		return debitaccount;
	}

	public void setDebitaccount(String debitaccount)
	{

		this.debitaccount = debitaccount;

	}

	public Timestamp getUpdatetime()
	{

		return updatetime;
	}

	public void setUpdatetime(Timestamp updatetime)
	{

		this.updatetime = updatetime;

	}

	public String getCorpcode()
	{

		return corpcode;
	}

	public void setCorpcode(String corpcode)
	{

		this.corpcode = corpcode;

	}

	public Long getTaocanmoney()
	{

		return taocanmoney;
	}

	public void setTaocanmoney(Long taocanmoney)
	{

		this.taocanmoney = taocanmoney;

	}

	public String getIsvalid()
	{

		return isvalid;
	}

	public void setIsvalid(String isvalid)
	{

		this.isvalid = isvalid;

	}

	public Long getDepid()
	{

		return depid;
	}

	public void setDepid(Long depid)
	{

		this.depid = depid;

	}

	public String getTaocancode()
	{

		return taocancode;
	}

	public void setTaocancode(String taocancode)
	{

		this.taocancode = taocancode;

	}

	public Long getId()
	{

		return id;
	}

	public void setId(Long id)
	{

		this.id = id;

	}

	public Long getContractdep()
	{

		return contractdep;
	}

	public void setContractdep(Long contractdep)
	{

		this.contractdep = contractdep;

	}

	public Integer getTaocantype()
	{

		return taocantype;
	}

	public void setTaocantype(Integer taocantype)
	{

		this.taocantype = taocantype;

	}

	public Timestamp getBucklefeetime()
	{

		return bucklefeetime;
	}

	public void setBucklefeetime(Timestamp bucklefeetime)
	{

		this.bucklefeetime = bucklefeetime;

	}

	public String getTaocanname()
	{
		return taocanname;
	}

	public void setTaocanname(String taocanname)
	{
		this.taocanname = taocanname;
	}

	public String getLastbucklefee()
	{
		return lastbucklefee;
	}

	public void setLastbucklefee(String lastbucklefee)
	{
		this.lastbucklefee = lastbucklefee;
	}

}
