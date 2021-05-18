package com.montnets.emp.common.servlet.util;

import java.io.BufferedReader;


/**
 * 
 * @功能概要：用于传递excel的相关值
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-1-19 下午12:30:17
 */
public class Excel2007VO
{
	// 当前行
	private int					curRow		= 0;
	// 列数
	private int					rowsize		= 0;
	//读入文件流
	private BufferedReader      reader      =null;
	//第一行
	private String 				firstline   ="";
	
	
	
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
	public BufferedReader getReader()
	{
		return reader;
	}
	public void setReader(BufferedReader reader)
	{
		this.reader = reader;
	}
	

}
