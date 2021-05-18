package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Rms图表json类
 * @author dell
 *
 */
public class TempRmsImgText implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3191392648532117730L;
	private TempElement image;
	private List<TempElement> texts;
	
	
	public TempElement getImage() {
		return image;
	}
	public void setImage(TempElement image) {
		this.image = image;
	}
	public List<TempElement> getTexts() {
		return texts;
	}
	public void setTexts(List<TempElement> texts) {
		this.texts = texts;
	}
	
	
}
