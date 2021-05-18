package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 前端卡片内容json类
 * @author moll
 *
 */
public class TempContent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4739110560549147888L;
	
	//宽
	private Integer w;
	//高
	private Integer h;
	//路径
	private String bgSrc;
	//总页数
	private String totalPage;
	//模板总数
	private String totalRecord;
	//权限
	private String rule;
	//组件
	private TempDataElement elements;



	public TempDataElement getElements() {
		return elements;
	}
	public void setElements(TempDataElement elements) {
		this.elements = elements;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public String getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public String getBgSrc() {
		return bgSrc;
	}
	public void setBgSrc(String bgSrc) {
		this.bgSrc = bgSrc;
	}
	public Integer getW() {
		return w;
	}
	public void setW(Integer w) {
		this.w = w;
	}
	public Integer getH() {
		return h;
	}
	public void setH(Integer h) {
		this.h = h;
	}
	
}
