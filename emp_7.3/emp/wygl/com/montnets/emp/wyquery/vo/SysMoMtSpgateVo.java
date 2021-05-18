package com.montnets.emp.wyquery.vo;


public class SysMoMtSpgateVo implements java.io.Serializable{
	
	private static final long serialVersionUID = 4335653678377063300L;
	//企业编码
	private String cropCode;
	//通道号
	private String spgate;	

	public String getSpgate() {
		return spgate;
	}

	public void setSpgate(String spgate) {
		this.spgate = spgate;
	}

	public String getCropCode() {
		return cropCode;
	}

	public void setCropCode(String cropCode) {
		this.cropCode = cropCode;
	}
}
