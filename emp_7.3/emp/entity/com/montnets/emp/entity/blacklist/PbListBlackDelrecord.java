package com.montnets.emp.entity.blacklist;

import java.sql.Timestamp;


/**
 * TablePbListBlackDelrecord对应的实体类
 * @project emp
 * @author zhouxiangxian <203492752@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-11-28 
 * @description 
 */
public class PbListBlackDelrecord implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6393363309449428123L;
	private Long id;
	private String operateId;
	private Long phone;
	private Timestamp optTime;
	private String taskId;
	
	public String getTaskId() {
		return taskId;
	}
	public PbListBlackDelrecord() {}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOperateId() {
		return operateId;
	}

	public void setOperateId(String operateId) {
		this.operateId = operateId;
	}

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}

	public Timestamp getOptTime() {
		return optTime;
	}

	public void setOptTime(Timestamp optTime) {
		this.optTime = optTime;
	}

	 
	
	
  

}