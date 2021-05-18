package com.montnets.emp.reportform.bean;

/**
 * 发送类型Bean
 * @author renmeng
 * @date 2018-12-17 10:00:00
 */
public class SendType {
	/**
	 * 发送类型
	 */
	private Integer sendType;
	/**
	 * 发送类型名称
	 */
	private String sendName;
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public String getSendName() {
		return sendName;
	}
	public void setSendName(String sendName) {
		this.sendName = sendName;
	}
	
}
