/**
 * Program  : AMnperrcode.java
 * Author   : zousy
 * Create   : 2014-3-26 上午10:30:48
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.entity.wy;

import java.sql.Timestamp;
import java.util.List;

/**
 * 携号转网错误码
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-3-26 上午10:30:48
 */
public class AMnperrcode
{
	//ID
	private Long id;
	//错误码
	private String errorcode;
	//更新时间
	private Timestamp createtime;
	//状态
	private Integer status = 0;
	//前类型
	private Integer type;
	//后类型
	private Integer mnptype;

	public String getErrorcode()
	{
		return errorcode;
	}

	public void setErrorcode(String errorcode)
	{
		this.errorcode = errorcode;
	}

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Timestamp getCreatetime()
	{
		return createtime;
	}

	public void setCreatetime(Timestamp createtime)
	{
		this.createtime = createtime;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}
	
	public Integer getMnptype()
	{
		return mnptype;
	}

	public void setMnptype(Integer mnptype)
	{
		this.mnptype = mnptype;
	}

	public String getTypeStr(){
		String typeStr = getUnicomStr(type);
		String mnptypeStr = getUnicomStr(mnptype);
		if(typeStr==null||mnptypeStr == null){
			return "未知";
		}
		return typeStr+"转"+mnptypeStr;
	}
	public String getUnicomStr(Integer type)
	{
		if(type-0==0){
			return "移动";
		}else if(type-1==0){
			return "联通";
		}else if(type-21==0){
			return "电信";
		}else{
			return null;
		}
	}
	public AMnperrcode()
	{
		super();
	}

	public AMnperrcode(Long id, String errorcode, Integer type, Integer mnptype)
	{
		super();
		this.id = id;
		this.errorcode = errorcode;
		this.type = type;
		this.mnptype = mnptype;
	}

	
}

