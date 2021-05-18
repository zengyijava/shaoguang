package com.montnets.emp.qyll.entity;


import java.util.Arrays;
import java.util.Map;

import org.json.simple.JSONObject;

import com.montnets.emp.common.constant.PreviewParams;

/**
 * 流量订购参数预览类
 * @author Administrator
 *
 */
public class LlPreviewParam extends PreviewParams {
	//流量消费总金额
	private double flowSumPrice;
	
	//流量运营商余额
	private double flowBalance;
	
	//流量套餐 0:移动号码 ;1:联通号码;2:电信号码;3:国际号码
	private String[] flowNames = new String[3];
	
	//流量套餐编码
	private String[] productIdStrs =  new String[3];
	//流量套餐主键id
	private String[] proIds = new String[3];
	
	//短信预览内容
	private String smsContent;
	
	//没有选中套餐的号码
	private int noFlowPhone;
	
	private Long taskId;
	
	@SuppressWarnings("unchecked")
	public String getJsonStr(Map<String,Object> objMap){
		JSONObject resultJson = new JSONObject();
		resultJson.put("subCount", this.getSubCount());
		resultJson.put("effCount", this.getEffCount());
		resultJson.put("badModeCount", this.getBadModeCount());
		resultJson.put("repeatCount", this.getRepeatCount());
		resultJson.put("blackCount", this.getBlackCount());
		//1：有效号码文件相对路径
		resultJson.put("validFilePath", this.getPhoneFilePath()[1]);
		//2：无效号码绝对路径
		resultJson.put("badFilePath", this.getPhoneFilePath()[2]);
		//4：预览文件相对路径
		resultJson.put("viewFilePath", this.getPhoneFilePath()[4]);
		resultJson.put("kwCount", this.getKwCount());
		
		resultJson.put("flowSumPrice", flowSumPrice);
		resultJson.put("flowBalance", flowBalance);
		resultJson.put("flowNames", flowNames[0]+":"+flowNames[1]+":"+flowNames[2]);
		resultJson.put("productIdStrs", productIdStrs[0]+":"+productIdStrs[1]+":"+productIdStrs[2]);
		resultJson.put("proIds", proIds[0]+":"+proIds[1]+":"+proIds[2]);
		resultJson.put("smsContent", smsContent);
		
		resultJson.put("oprValidPhone", this.getOprValidPhone()[0]+":"+this.getOprValidPhone()[1]+":"+this.getOprValidPhone()[2]);
		resultJson.put("noFlowPhone", noFlowPhone);
		resultJson.put("taskId", taskId);
		
		//将传入的map合入到resultJson
		if(objMap != null && objMap.size() > 0){
			resultJson.putAll(objMap);
		}	
		return resultJson.toString();
	}
	
	public double getFlowSumPrice() {
		return flowSumPrice;
	}

	public void setFlowSumPrice(double flowSumPrice) {
		this.flowSumPrice = flowSumPrice;
	}

	public double getFlowBalance() {
		return flowBalance;
	}

	public void setFlowBalance(double flowBalance) {
		this.flowBalance = flowBalance;
	}

	public String[] getFlowNames() {
		return flowNames;
	}

	public void setFlowNames(String[] flowNames) {
		this.flowNames = flowNames;
	}

	public String getSmsContent() {
		return smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	public String[] getProductIdStrs() {
		return productIdStrs;
	}

	public void setProductIdStrs(String[] productIdStrs) {
		this.productIdStrs = productIdStrs;
	}

	public int getNoFlowPhone() {
		return noFlowPhone;
	}

	public void setNoFlowPhone(int noFlowPhone) {
		this.noFlowPhone = noFlowPhone;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public String[] getProIds() {
		return proIds;
	}

	public void setProIds(String[] proIds) {
		this.proIds = proIds;
	}

	@Override
	public String toString() {
		return "LlPreviewParam [flowSumPrice=" + flowSumPrice
				+ ", flowBalance=" + flowBalance + ", flowNames="
				+ Arrays.toString(flowNames) + ", productIdStrs="
				+ Arrays.toString(productIdStrs) + ", smsContent=" + smsContent
				+ ", noFlowPhone=" + noFlowPhone + ", taskId=" + taskId + "]";
	}
	
	
	
}
