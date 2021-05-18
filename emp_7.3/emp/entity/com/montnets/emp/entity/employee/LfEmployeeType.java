/**
 * 
 */
package com.montnets.emp.entity.employee;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-30 上午11:20:32
 * @description 操作员与客户机构权限表
 */

public class LfEmployeeType implements java.io.Serializable
{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 512443984024874912L;
	
	private Long id;
	//操作员ID
	private Long userId;
	//操作员名称
	private String name;
	//企业编码
	private String corpcode;
	
	public String getCorpcode() {
		return corpcode;
	}

	public void setCorpcode(String corpcode) {
		this.corpcode = corpcode;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	
}
