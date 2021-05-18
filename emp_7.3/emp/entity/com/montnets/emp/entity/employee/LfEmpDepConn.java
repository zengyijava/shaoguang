/**
 * 
 */
package com.montnets.emp.entity.employee;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-30 上午11:25:07
 * @description 员工与员工机构权限表
 */

public class LfEmpDepConn implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1198913691533876101L;
	//标识列ID
	private Long connId;
	//操作员ID
	private Long userId;

	private String depCodeThird;
	//部门ID
	private Long depId;

	public Long getDepId() {
		return depId;
	}

	public void setDepId(Long depId) {
		this.depId = depId;
	}

	public LfEmpDepConn() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	

	public String getDepCodeThird()
	{
		return depCodeThird;
	}

	public void setDepCodeThird(String depCodeThird)
	{
		this.depCodeThird = depCodeThird;
	}

	public Long getConnId() {
		return connId;
	}

	public void setConnId(Long connId) {
		this.connId = connId;
	}

}
