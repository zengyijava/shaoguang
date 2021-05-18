package com.montnets.emp.entity.sysuser;

import java.io.Serializable;
import java.sql.Timestamp;

public class LfDepRechargeLog implements Serializable {

	/**
	 * 机构充值/回收日志表
	 */
	private static final long serialVersionUID = -4368903746355027414L;

	private Long logId;
	// 充值类型（100为总公司充值/回收；101机构为机构充值/回收； 102机构为操作员充值/回收）
	private Integer optType; 
	// 充值源id 上级机构
	private Long srcTargetId; 
	// 充值目的id 充值机构
	private Long dstTargetId; 
	// 充值源名称/描述 充值目的名称/描述
	private String optInfo; 
	// 信息类型：1为短信，2为彩信
	private Integer msgType; 
	// 数量：充值/回收数量
	private Long count; 
	 // 操作员id：执行充值/回收的操作员id
	private Long optId;
	// 操作时间：执行充值/回收的时间
	private Timestamp optDate; 
	// 执行结果（0成功，其它失败）
	private Integer result; 
	// 备注
	private String memo; 
	// 企业编号
	private String corpCode; 

	public LfDepRechargeLog() {
	}

	public String getCorpCode() {
		return corpCode;
	}

	public void setCorpCode(String corpCode) {
		this.corpCode = corpCode;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public Integer getOptType() {
		return optType;
	}

	public String getOptInfo() {
		return optInfo;
	}

	public void setOptInfo(String optInfo) {
		this.optInfo = optInfo;
	}

	public void setOptType(Integer optType) {
		this.optType = optType;
	}

	public Long getSrcTargetId() {
		return srcTargetId;
	}

	public void setSrcTargetId(Long srcTargetId) {
		this.srcTargetId = srcTargetId;
	}

	public Long getDstTargetId() {
		return dstTargetId;
	}

	public void setDstTargetId(Long dstTargetId) {
		this.dstTargetId = dstTargetId;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getOptId() {
		return optId;
	}

	public void setOptId(Long optId) {
		this.optId = optId;
	}

	public Timestamp getOptDate() {
		return optDate;
	}

	public void setOptDate(Timestamp optDate) {
		this.optDate = optDate;
	}

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

}
