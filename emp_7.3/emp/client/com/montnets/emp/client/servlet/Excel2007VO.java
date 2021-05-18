package com.montnets.emp.client.servlet;

import java.util.List;

/**
 * 
 * @功能概要：用于传递excel的相关值
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-1-19 下午12:30:17
 */
public class Excel2007VO {
	/**
	 * 当前行
	 */
	private int curRow = 0;
	/**
	 * 列数
	 */
	private int rowsize = 0;
	/**
	 * 第一行
	 */
	private String firstline = "";
	/**
	 * 每一行内容
	 */
	private List<String> lineContent;

	public String getFirstline()
	{
		return firstline;
	}
	public void setFirstline(String firstline)
	{
		this.firstline = firstline;
	}
	public int getCurRow()
	{
		return curRow;
	}
	public void setCurRow(int curRow)
	{
		this.curRow = curRow;
	}
	public int getRowsize()
	{
		return rowsize;
	}
	public void setRowsize(int rowsize)
	{
		this.rowsize = rowsize;
	}

	public List<String> getLineContent() {
		return lineContent;
	}

	public void setLineContent(List<String> lineContent) {
		this.lineContent = lineContent;
	}
}
