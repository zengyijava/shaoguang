package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/***
 * tempRms的Element
 * @author dell
 *
 */
public class TempRmsElement implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1122699819528381277L;
	
	private TempElement text;
	private TempElement image;
	private TempElement audio;
	private TempElement vedio;
	private TempRmsImgText imageText;
	private TempRmsChart chart;
	public TempElement getText() {
		return text;
	}
	public void setText(TempElement text) {
		this.text = text;
	}
	public TempElement getImage() {
		return image;
	}
	public void setImage(TempElement image) {
		this.image = image;
	}
	public TempElement getAudio() {
		return audio;
	}
	public void setAudio(TempElement audio) {
		this.audio = audio;
	}
	public TempElement getVedio() {
		return vedio;
	}
	public void setVedio(TempElement vedio) {
		this.vedio = vedio;
	}
	public TempRmsImgText getImageText() {
		return imageText;
	}
	public void setImageText(TempRmsImgText imageText) {
		this.imageText = imageText;
	}
	public TempRmsChart getChart() {
		return chart;
	}
	public void setChart(TempRmsChart chart) {
		this.chart = chart;
	}
	
}
