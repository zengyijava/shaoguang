package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;
import java.util.List;

public class TempDataElement implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5254389039077104340L;
	private List<TempElement> images;
	private List<TempElement> audios;
	private List<TempElement> videos;
	private List<TempElement> qrcodes;
	private List<TempElement> texts;
	private List<TempElement> buttons;
	
	
	public List<TempElement> getButtons() {
		return buttons;
	}
	public void setButtons(List<TempElement> buttons) {
		this.buttons = buttons;
	}
	public List<TempElement> getImages() {
		return images;
	}
	public void setImages(List<TempElement> images) {
		this.images = images;
	}
	public List<TempElement> getAudios() {
		return audios;
	}
	public void setAudios(List<TempElement> audios) {
		this.audios = audios;
	}

	public List<TempElement> getVideos() {
		return videos;
	}

	public void setVideos(List<TempElement> videos) {
		this.videos = videos;
	}

	public List<TempElement> getQrcodes() {
		return qrcodes;
	}
	public void setQrcodes(List<TempElement> qrcodes) {
		this.qrcodes = qrcodes;
	}
	public List<TempElement> getTexts() {
		return texts;
	}
	public void setTexts(List<TempElement> texts) {
		this.texts = texts;
	}
	
	
	
}
