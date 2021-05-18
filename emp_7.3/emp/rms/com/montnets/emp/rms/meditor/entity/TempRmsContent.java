package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

public class TempRmsContent implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1206967333488467600L;
	
	private String w;
	private String h;
	private String bgSrc;
	private List<TempRmsElement> elements;
	public String getW() {
		return w;
	}
	public void setW(String w) {
		this.w = w;
	}
	public String getH() {
		return h;
	}
	public void setH(String h) {
		this.h = h;
	}
	public String getBgSrc() {
		return bgSrc;
	}
	public void setBgSrc(String bgSrc) {
		this.bgSrc = bgSrc;
	}
	public List<TempRmsElement> getElements() {
		return elements;
	}
	public void setElements(List<TempRmsElement> elements) {
		this.elements = elements;
	}
}
