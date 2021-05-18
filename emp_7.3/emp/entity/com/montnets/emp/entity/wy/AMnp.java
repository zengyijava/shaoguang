/**
 * Program  : AMnp.java
 * Author   : zousy
 * Create   : 2014-3-24 下午02:26:11
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.entity.wy;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 携号转网实体类
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-3-24 下午02:26:11
 */
public class AMnp
{
	//自增ID
	private Long id;
	//手机号
	private String phone = "";
	//转网前的运营商类型  0 移动    1 联通   21电信    99 未知
	private Long unicom = 0L;
	//转网后的运营商类型
	private Long phoneType = 0L;
	//录入类型(0 手动，1 系统自动)
	private Long addType = 0L;
	//0 增加，1 删除
	private Long optType = 0L;
	//信息更新时间
	private Timestamp createTime;
	public Long getId()
	{
		return id;
	}
	public void setId(Long id)
	{
		this.id = id;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public Long getUnicom()
	{
		return unicom;
	}
	public void setUnicom(Long unicom)
	{
		this.unicom = unicom;
	}
	public Long getPhoneType()
	{
		return phoneType;
	}
	public void setPhoneType(Long phoneType)
	{
		this.phoneType = phoneType;
	}
	public Long getAddType()
	{
		return addType;
	}
	public void setAddType(Long addType)
	{
		this.addType = addType;
	}
	public Long getOptType()
	{
		return optType;
	}
	public void setOptType(Long optType)
	{
		this.optType = optType;
	}
	public Timestamp getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(Timestamp createTime)
	{
		this.createTime = createTime;
	}
	public AMnp(Long id, String phone, Long unicom, Long phoneType)
	{
		super();
		this.id = id;
		this.phone = phone;
		this.unicom = unicom;
		this.phoneType = phoneType;
	}
	public AMnp()
	{
		super();
	}
	public String getCreateTimeStr()
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(createTime);
	}
	
	public String getPhoneTypeStr()
	{
		String unicomStr = getPhoneUnicomStr();
		String phoneStr = getPhoneStr();
		if(unicomStr==null||phoneStr == null){
			return "未知";
		}
		return getPhoneUnicomStr()+"转"+getPhoneStr();
	}
	public String getPhoneUnicomStr()
	{
		if(unicom-0==0){
			return "移动";
		}else if(unicom-1==0){
			return "联通";
		}else if(unicom-21==0){
			return "电信";
		}else{
			return null;
		}
	}
	public String getPhoneStr()
	{
		if(phoneType-0==0){
			return "移动";
		}else if(phoneType-1==0){
			return "联通";
		}else if(phoneType-21==0){
			return "电信";
		}else{
			return null;
		}
	}
	public String getAddTypeStr(){
		if(addType-1==0){
			return "系统录入";
		}
		return "手动添加";
	}
}

