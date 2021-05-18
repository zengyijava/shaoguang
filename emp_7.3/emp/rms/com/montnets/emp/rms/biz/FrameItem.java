package com.montnets.emp.rms.biz;


public class FrameItem  {
	private int delayTime;
	private String imageSrc;
	private String textSrc;
	private String audioSrc;
	
	public FrameItem(){
		delayTime = 10;
	}
	
	public int getDelayTime() {
		return delayTime;
	}
	
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public String getTextSrc() {
		return textSrc;
	}
	public void setTextSrc(String textSrc) {
		this.textSrc = textSrc;
	}
	public String getAudioSrc() {
		return audioSrc;
	}
	public void setAudioSrc(String audioSrc) {
		this.audioSrc = audioSrc;
	}
}
