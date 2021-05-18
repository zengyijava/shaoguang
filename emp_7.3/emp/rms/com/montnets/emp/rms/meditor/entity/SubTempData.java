package com.montnets.emp.rms.meditor.entity;

import java.io.Serializable;

/**
 *
 * ClassName: SubTempData 
 * @Description: 模板集合子类
 * @author xuty
 * @date 2018-8-4下午3:44:04
 */
public class SubTempData implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 6872389148960743273L;

	private Integer tmpType ;

	private Object content ;

	private String cardHtml;

	private Integer degree;

	private Integer degreeSize;

	//富文本的宽
	private Integer w;
	//富文本的高
	private Integer h;
	//H5类型 0-长页，1-短页
	private Integer h5Type;

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

	public Integer getTmpType() {
		return tmpType;
	}

	public void setTmpType(Integer tmpType) {
		this.tmpType = tmpType;
	}




	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public String getCardHtml() {
		return cardHtml;
	}

	public void setCardHtml(String cardHtml) {
		this.cardHtml = cardHtml;
	}

	public Integer getDegree() {
		return degree;
	}

	public void setDegree(Integer degree) {
		this.degree = degree;
	}

	public Integer getDegreeSize() {
		return degreeSize;
	}

	public void setDegreeSize(Integer degreeSize) {
		this.degreeSize = degreeSize;
	}

	public Integer getH5Type() {
		return h5Type;
	}

	public void setH5Type(Integer h5Type) {
		this.h5Type = h5Type;
	}
}
