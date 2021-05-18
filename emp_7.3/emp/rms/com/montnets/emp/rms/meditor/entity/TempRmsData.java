package com.montnets.emp.rms.meditor.entity;
/**
 * json转富信类
 * @author dell
 *
 */
public class TempRmsData extends TempData{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7645063453652871591L;
	private TempRmsContent content;

	public TempRmsContent getContent() {
		return content;
	}

	public void setContent(TempRmsContent content) {
		this.content = content;
	}
	
}
