/**LF_CONTRACT 签约客户信息表
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午08:44:56
 */
package com.montnets.emp.entity.ydyw;

import java.sql.Timestamp;

/**
 * @description
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-15 上午08:44:56
 */

public class LfContract
{
	// 客户GUID
	private Long		guid;

	// 账户客户证件类型
	private String		acctidenttype;

	// 签约操作员ID
	private Long		contractuser;

	// 取消签约方式（0：系统同步、1：主动上行、2：手动录入 ）
	private Integer		cancelcontype;

	// 客户类型(0：个人客户；1：单位客户)
	private Integer		customtype;

	// 签约状态(0:：已签约；1：已取消签约；2：已冻结)
	private Integer		contractstate;

	// 扣款账号
	private String		debitaccount;

	// 客户编号
	private String		clientcode;

	// 有效标识，0-账号正常 1-账号删除
	private String		isvalid;

	// 签约方式（0：IC卡；1：磁条卡；2：存折）
	private Integer		contracttype;

	// 账户姓名
	private String		acctname;

	// 扣款账号姓名
	private String		debitacctname;

	// 开通订制日期
	private Timestamp	addorderdate;

	// 客户姓名
	private String		customname;

	// 扣款账号开户行
	private Long		debitacctdep;

	// 签约来源（0：系统同步、1：主动上行、2：手动录入 ）
	private Integer		contractsource;

	// 账户证件号码
	private String		acctidentno;

	// 账户联系地址
	private String		acctaddress;

	// 签约机构ID
	private Long		contractdep;

	// 签约ID
	private Long		contractid;

	// 签约日期
	private Timestamp	contractdate;

	// 客户证件号码
	private String		identno;

	// 账户单位名称
	private String		acctcomname;

	// 客户证件类型
	private String		identtype;

	// 操作员ID
	private Long		userid;

	// 签约账号
	private String		acctno;

	// 客户联系地址
	private String		address;

	// 取消订制日期
	private Timestamp	cancelorderdate;

	// 更新时间
	private Timestamp	updatetime;

	// 企业编码
	private String		corpcode;

	// 操作员机构ID
	private Long		depid;

	// 取消/冻结签约时间
	private Timestamp	cancelcontime;

	// 手机号码
	private String		mobile;

	public LfContract()
	{
	}

	public Long getGuid()
	{

		return guid;
	}

	public void setGuid(Long guid)
	{

		this.guid = guid;

	}

	public String getAcctidenttype()
	{

		return acctidenttype;
	}

	public void setAcctidenttype(String acctidenttype)
	{

		this.acctidenttype = acctidenttype;

	}

	public Long getContractuser()
	{

		return contractuser;
	}

	public void setContractuser(Long contractuser)
	{

		this.contractuser = contractuser;

	}

	public Integer getCancelcontype()
	{

		return cancelcontype;
	}

	public void setCancelcontype(Integer cancelcontype)
	{

		this.cancelcontype = cancelcontype;

	}

	public Integer getCustomtype()
	{

		return customtype;
	}

	public void setCustomtype(Integer customtype)
	{

		this.customtype = customtype;

	}

	public Integer getContractstate()
	{

		return contractstate;
	}

	public void setContractstate(Integer contractstate)
	{

		this.contractstate = contractstate;

	}

	public String getDebitaccount()
	{

		return debitaccount;
	}

	public void setDebitaccount(String debitaccount)
	{

		this.debitaccount = debitaccount;

	}

	public String getClientcode()
	{

		return clientcode;
	}

	public void setClientcode(String clientcode)
	{

		this.clientcode = clientcode;

	}

	public String getIsvalid()
	{

		return isvalid;
	}

	public void setIsvalid(String isvalid)
	{

		this.isvalid = isvalid;

	}

	public Integer getContracttype()
	{

		return contracttype;
	}

	public void setContracttype(Integer contracttype)
	{

		this.contracttype = contracttype;

	}

	public String getAcctname()
	{

		return acctname;
	}

	public void setAcctname(String acctname)
	{

		this.acctname = acctname;

	}

	public String getDebitacctname()
	{

		return debitacctname;
	}

	public void setDebitacctname(String debitacctname)
	{

		this.debitacctname = debitacctname;

	}

	public Timestamp getAddorderdate()
	{

		return addorderdate;
	}

	public void setAddorderdate(Timestamp addorderdate)
	{

		this.addorderdate = addorderdate;

	}

	public String getCustomname()
	{

		return customname;
	}

	public void setCustomname(String customname)
	{

		this.customname = customname;

	}

	public Long getDebitacctdep()
	{

		return debitacctdep;
	}

	public void setDebitacctdep(Long debitacctdep)
	{

		this.debitacctdep = debitacctdep;

	}

	public Integer getContractsource()
	{

		return contractsource;
	}

	public void setContractsource(Integer contractsource)
	{

		this.contractsource = contractsource;

	}

	public String getAcctidentno()
	{

		return acctidentno;
	}

	public void setAcctidentno(String acctidentno)
	{

		this.acctidentno = acctidentno;

	}

	public String getAcctaddress()
	{

		return acctaddress;
	}

	public void setAcctaddress(String acctaddress)
	{

		this.acctaddress = acctaddress;

	}

	public Long getContractdep()
	{

		return contractdep;
	}

	public void setContractdep(Long contractdep)
	{

		this.contractdep = contractdep;

	}

	public Long getContractid()
	{

		return contractid;
	}

	public void setContractid(Long contractid)
	{

		this.contractid = contractid;

	}

	public Timestamp getContractdate()
	{

		return contractdate;
	}

	public void setContractdate(Timestamp contractdate)
	{

		this.contractdate = contractdate;

	}

	public String getIdentno()
	{

		return identno;
	}

	public void setIdentno(String identno)
	{

		this.identno = identno;

	}

	public String getAcctcomname()
	{

		return acctcomname;
	}

	public void setAcctcomname(String acctcomname)
	{

		this.acctcomname = acctcomname;

	}

	public String getIdenttype()
	{

		return identtype;
	}

	public void setIdenttype(String identtype)
	{

		this.identtype = identtype;

	}

	public Long getUserid()
	{

		return userid;
	}

	public void setUserid(Long userid)
	{

		this.userid = userid;

	}

	public String getAcctno()
	{

		return acctno;
	}

	public void setAcctno(String acctno)
	{

		this.acctno = acctno;

	}

	public String getAddress()
	{

		return address;
	}

	public void setAddress(String address)
	{

		this.address = address;

	}

	public Timestamp getCancelorderdate()
	{

		return cancelorderdate;
	}

	public void setCancelorderdate(Timestamp cancelorderdate)
	{

		this.cancelorderdate = cancelorderdate;

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

	public Long getDepid()
	{

		return depid;
	}

	public void setDepid(Long depid)
	{

		this.depid = depid;

	}

	public Timestamp getCancelcontime()
	{

		return cancelcontime;
	}

	public void setCancelcontime(Timestamp cancelcontime)
	{

		this.cancelcontime = cancelcontime;

	}

	public String getMobile()
	{

		return mobile;
	}

	public void setMobile(String mobile)
	{

		this.mobile = mobile;

	}
}
