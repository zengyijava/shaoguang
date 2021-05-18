package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

public class ElementStyle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 669380891168053544L;
	
    //是否为圆角
    private String borderRadius;
	//文本装饰
	private String textDecoration;
	//文本位置
	private String textAlign;
	//字体大小
    private String fontSize;
    private String fontWeight;
    //字体形状
	private String fontFamily;
	//字体样式
	private String fontStyle;
	//字体颜色
    private String color;
    //背景颜色
	private String backgroundColor;
	
	private String decoration;
	
	private String press;
	
	public String getFontWeight() {
		return fontWeight;
	}
	public void setFontWeight(String fontWeight) {
		this.fontWeight = fontWeight;
	}
	public String getPress() {
		return press;
	}
	public void setPress(String press) {
		this.press = press;
	}
	public String getDecoration() {
		return decoration;
	}
	public void setDecoration(String decoration) {
		this.decoration = decoration;
	}
	public String getBorderRadius() {
		return borderRadius;
	}
	public void setBorderRadius(String borderRadius) {
		this.borderRadius = borderRadius;
	}
	public String getTextDecoration() {
		return textDecoration;
	}
	public void setTextDecoration(String textDecoration) {
		this.textDecoration = textDecoration;
	}
	public String getTextAlign() {
		return textAlign;
	}
	public void setTextAlign(String textAlign) {
		this.textAlign = textAlign;
	}
	public String getFontSize() {
		return fontSize;
	}
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	public String getFontFamily() {
		return fontFamily;
	}
	public void setFontFamily(String fontFamily) {
		this.fontFamily = fontFamily;
	}
	public String getFontStyle() {
		return fontStyle;
	}
	public void setFontStyle(String fontStyle) {
		this.fontStyle = fontStyle;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getBackgroundColor() {
		return backgroundColor;
	}
	public void setBackgroundColor(String backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

}
