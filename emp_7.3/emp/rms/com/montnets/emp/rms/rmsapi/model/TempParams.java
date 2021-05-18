package com.montnets.emp.rms.rmsapi.model;

import java.io.Serializable;

/**
 * 上传文件参数实体类
 * @author chenly
 *
 */
public class TempParams implements Serializable {

	private static final long serialVersionUID = 780983207668210858L;
	//文件类型
	private Integer type;
	//档位
	private Integer degree;
	//参数个数(如果是静态模板为0，动态模板为参数个数)
	private Integer pnum;
	//档位对应的文件大小
	private Integer size;
	//文件内容字节
	private byte[] contentByte;
	//文件内容
	private String content;
	
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getDegree()
	{
		return degree;
	}
	public void setDegree(Integer degree)
	{
		this.degree = degree;
	}
	public Integer getPnum()
	{
		return pnum;
	}
	public void setPnum(Integer pnum)
	{
		this.pnum = pnum;
	}
	public Integer getSize()
	{
		return size;
	}
	public void setSize(Integer size)
	{
		this.size = size;
	}
	public byte[] getContentByte() {
		return contentByte;
	}
	public void setContentByte(byte[] contentByte) {
		this.contentByte = contentByte;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "{\"type\":" + type + ",\"degree\":" + degree + ",\"pnum\":"
				+ pnum + ",\"size\":" + size + ",\"content\":\"" + content + "\"}";
	}
 
	
	
	
}
