package com.montnets.emp.entity.ydcx;

import java.sql.Timestamp;

public class LfDfadvanced
{

	//回复设置
	private Integer replyset;

	//业务编码
	private String buscode;

	//重号过滤
	private Integer repeatfilter;
	
	//操作员ID 
	private Long userid;

	//创建时间
	private Timestamp createtime;

	//SP账号ID
	private String spuserid;

	//发送级别
	private String priority;

	//自增ID
	private Long id;

	/*功能标识
	1：相同内容群发
	2：不同内容群发
	3：完美通知发送
	4：移动财务发送
	5：员工生日祝福
	6：APP消息发送
	7：静态网讯发送
	8：动态网讯发送
	9：静态彩讯发送
	10：动态彩讯发送
	11：客户群组群发
	12：客户生日祝福
	 */
	private Integer flag;

	public LfDfadvanced(){
	} 

	public Integer getReplyset(){

		return replyset;
	}

	public void setReplyset(Integer replyset){

		this.replyset= replyset;

	}

	public String getBuscode(){

		return buscode;
	}

	public void setBuscode(String buscode){

		this.buscode= buscode;

	}

	public Integer getRepeatfilter(){

		return repeatfilter;
	}

	public void setRepeatfilter(Integer repeatfilter){

		this.repeatfilter= repeatfilter;

	}

	public Long getUserid(){

		return userid;
	}

	public void setUserid(Long userid){

		this.userid= userid;

	}

	public Timestamp getCreatetime(){

		return createtime;
	}

	public void setCreatetime(Timestamp createtime){

		this.createtime= createtime;

	}

	public String getSpuserid(){

		return spuserid;
	}

	public void setSpuserid(String spuserid){

		this.spuserid= spuserid;

	}

	public String getPriority(){

		return priority;
	}

	public void setPriority(String priority){

		this.priority= priority;

	}

	public Long getId(){

		return id;
	}

	public void setId(Long id){

		this.id= id;

	}

	public Integer getFlag(){

		return flag;
	}

	public void setFlag(Integer flag){

		this.flag= flag;

	}

}

							
