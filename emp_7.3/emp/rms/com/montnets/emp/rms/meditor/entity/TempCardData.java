package com.montnets.emp.rms.meditor.entity;


/**
 * 前端卡片json类
 * @author moll
 *
 */
public class TempCardData extends TempData{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7003861712426757619L;
	//卡片内容
	private TempContent content;
	
	public TempContent getContent() {
		return content;
	}
	public void setContent(TempContent content) {
		this.content = content;
	}
}
