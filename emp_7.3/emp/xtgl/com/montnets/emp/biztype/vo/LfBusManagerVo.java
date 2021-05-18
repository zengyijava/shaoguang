package com.montnets.emp.biztype.vo;

import java.sql.Timestamp;

/**
 *<p>project name p_xtgl</p>
 *<p>Title: LfBusManagerVo</p>
 *<p>Description: </p>
 *<p>Company: Montnets Technology CO.,LTD.</p>
 * @author dingzx
 * @date 2015-1-15下午01:54:32
 */
public class LfBusManagerVo implements java.io.Serializable{

	/**serialVersionUID*/
	private static final long serialVersionUID = 9009809490856609787L;
	//业务ID
	private Long busId;
	//业务编码
	private String busCode;
	//业务名称
	private String busName;
	//业务描述
	private String busDescription;
	//企业编码
	private String corpCode;
	//创建时间
	private Timestamp createTime;
	//更新时间
	private Timestamp updateTime;
	//操作员ID
	private Long userId;
	//操作员机构ID
	private Long depId;
	//业务员状态
	private Integer state;
	//业务类型
	private Integer busType;
	//优先级
	private Integer riseLevel;
	//操作员名称
	private String name;
	//操作员登录名
	private String userName;
	//操作员机构名称
	private String depName;
	//是否包含子机构
	private String isContainsSun;
	//机构、子机构集合
	private String depIds;
	//查询开始时间
	private String startSubmitTime;
	//查询结束时间
	private String endSubmitTime;
	
	public String getStartSubmitTime()
	{
		return startSubmitTime;
	}
	public void setStartSubmitTime(String startSubmitTime)
	{
		this.startSubmitTime = startSubmitTime;
	}
	public String getEndSubmitTime()
	{
		return endSubmitTime;
	}
	public void setEndSubmitTime(String endSubmitTime)
	{
		this.endSubmitTime = endSubmitTime;
	}
	public String getDepIds()
	{
		return depIds;
	}
	public void setDepIds(String depIds)
	{
		this.depIds = depIds;
	}
	public String getIsContainsSun()
	{
		return isContainsSun;
	}
	public void setIsContainsSun(String isContainsSun)
	{
		this.isContainsSun = isContainsSun;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getUserName()
	{
		return userName;
	}
	public void setUserName(String userName)
	{
		this.userName = userName;
	}
	public String getDepName()
	{
		return depName;
	}
	public void setDepName(String depName)
	{
		this.depName = depName;
	}
	public Long getBusId() {
		return busId;
	}
	public void setBusId(Long busId) {
		this.busId = busId;
	}
	public String getBusCode() {
		return busCode;
	}
	public void setBusCode(String busCode) {
		this.busCode = busCode;
	}
	public String getBusName() {
		return busName;
	}
	public void setBusName(String busName) {
		this.busName = busName;
	}
	public String getBusDescription() {
		return busDescription;
	}
	public void setBusDescription(String busDescription) {
		this.busDescription = busDescription;
	}
	public String getCorpCode() {
		return corpCode;
	}
	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}
	public Timestamp getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Integer getBusType() {
		return busType;
	}
	public void setBusType(Integer busType) {
		this.busType = busType;
	}
	public Integer getRiseLevel()
	{
		return riseLevel;
	}
	public void setRiseLevel(Integer riseLevel)
	{
		this.riseLevel = riseLevel;
	}
	public Long getDepId() {
		return depId;
	}
	public void setDepId(Long depId) {
		this.depId = depId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
