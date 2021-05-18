package com.montnets.emp.sysuser.bean;

public class OptUpload {
	// 总数
	private Integer total;
	// 成功数
	private Integer success;
	// 失败数
	private Integer fail;
	// 时间
	private String time;
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getSuccess() {
		return success;
	}
	public void setSuccess(Integer success) {
		this.success = success;
	}
	public Integer getFail() {
		return fail;
	}
	public void setFail(Integer fail) {
		this.fail = fail;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "OptUpload [total=" + total + ", success=" + success + ", fail=" + fail + ", time=" + time + "]";
	}
	
	
}
