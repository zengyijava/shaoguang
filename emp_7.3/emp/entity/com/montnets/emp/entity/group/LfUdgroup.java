/**
 * 
 */
package com.montnets.emp.entity.group;

import java.sql.Timestamp;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-28 上午09:32:48
 * @description 
 */

public class LfUdgroup implements java.io.Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5767513321354091228L;

	/**
	 * 
	 */
	//private static final long serialVersionUID = -5588947288747070372L;
	//分组ID
	private Long udgId;
	//分组名称
	private String udgName;
	//操作员ID
	private Long userId;
	//群组类型 1-互动群 ； 2-广播群 ； 3-超级群
	private Integer groupType;
	//群组所属 0-员工和自定义群组 ；1-客户和自定义群组
	private Integer gpAttribute;
	private Integer sendmode;
	//共享类型
	private Integer sharetype;
	//个人群组ID
	private Long groupid;
	//群组所属者
	private Long receiver;
	//共享状态
	private Integer shareStatus;
	//创建时间
	private Timestamp createTime;
	public LfUdgroup(){
		//默认群类型为：广播群
		this.groupType = new Integer(2);
		//默认群组所属为：员工和自定义群组
		this.gpAttribute = new Integer(0);
	}

	//分组ID
	public Long getUdgId()
	{
		return udgId;
	}

	public void setUdgId(Long udgId)
	{
		this.udgId = udgId;
	}

	
	public Integer getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(Integer shareStatus) {
		this.shareStatus = shareStatus;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	//分组名称
	public String getUdgName()
	{
		return udgName;
	}

	public void setUdgName(String udgName)
	{
		this.udgName = udgName;
	}

	
	//操作员ID
	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	
	//群组类型 1-互动群 ； 2-广播群 ； 3-超级群
	public Integer getGroupType()
	{
		return groupType;
	}

	public void setGroupType(Integer groupType)
	{
		this.groupType = groupType;
	}

	//群组所属 0-员工和自定义群组 ；1-客户和自定义群组
	public Integer getGpAttribute() {
		return gpAttribute;
	}

	public void setGpAttribute(Integer gpAttribute) {
		this.gpAttribute = gpAttribute;
	}

	public Integer getSendmode() {
		return sendmode;
	}

	public void setSendmode(Integer sendmode) {
		this.sendmode = sendmode;
	}

	public Integer getSharetype() {
		return sharetype;
	}

	public void setSharetype(Integer sharetype) {
		this.sharetype = sharetype;
	}

	public Long getGroupid() {
		return groupid;
	}

	public void setGroupid(Long groupid) {
		this.groupid = groupid;
	}

	public Long getReceiver() {
		return receiver;
	}

	public void setReceiver(Long receiver) {
		this.receiver = receiver;
	}

 
	
}
