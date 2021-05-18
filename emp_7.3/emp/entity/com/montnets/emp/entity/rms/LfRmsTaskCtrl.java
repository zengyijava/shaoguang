package com.montnets.emp.entity.rms;

import java.sql.Timestamp;

public class LfRmsTaskCtrl implements java.io.Serializable {

	
	private static final long serialVersionUID = 2961363823244596450L;

	//主键
	private Long  id;

	//任务id
	private Long taskId ;
	
	//当前记录
	private Long currentCount;
	
	//修改时间
	private Timestamp updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public long getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(Long currentCount) {
		this.currentCount = currentCount;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}	
	
}
