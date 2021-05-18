package com.montnets.emp.rms.rmsapi.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * 
 * 文件名称:JsonEntity.java
 * 文件描述:json解析类
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-19    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-19 上午08:49:37
 */
public class JsonEntity extends StringEntity
{

	public JsonEntity(List<BasicNameValuePair> parameters, String charset) throws UnsupportedEncodingException
	{
		
		super(parseParam(parameters),ContentType.create("application/json", charset));
	}

	public JsonEntity(Iterable<BasicNameValuePair> parameters, Charset charset)
	{
		super(parseParam(parameters), ContentType.create("application/json", charset));
	}

	public JsonEntity(List<BasicNameValuePair> parameters) throws UnsupportedEncodingException
	{
		this(((Iterable<BasicNameValuePair>) (parameters)), (Charset) null);
	}

	public JsonEntity(Iterable<BasicNameValuePair> parameters)
	{
		this(parameters, ((Charset) (null)));
	}
	
	
	private static String parseParam( List<BasicNameValuePair> params)
	{
		
		String temp="";
		if (params!=null)
		{
			Iterable<BasicNameValuePair> paramsIt=params;
			temp=parseParam(paramsIt);
		}
		return temp;
		
		
	}
	
	private static String parseParam(Iterable<BasicNameValuePair> params)
	{
		
		Map<String,Object> map=new HashMap<String, Object>();
		StringBuffer sb=new StringBuffer();
		String name=null;
		String value=null;
		sb.append("{");
		for (BasicNameValuePair basicNameValuePair : params)
		{
			name=basicNameValuePair.getName();
			value=basicNameValuePair.getValue();
			if("content".equals(name)){
				if(value==null||"".equals(value)||value.indexOf("[")==-1){
					//content为非集合
					sb.append("\""+name+"\":");
					sb.append("\""+value+"\",");
				}else{
					//content为集合
					sb.append("\""+name+"\":");
					sb.append(value+",");
				}
			}else{
				sb.append("\""+name+"\":");
				sb.append("\""+value+"\",");
			}
			map.put(name, value);
		}
		sb.delete(sb.length()-1, sb.length());
		sb.append("}");
		EmpExecutionContext.error("请求报文："+sb.toString());
		return sb.toString();
		
	}
	


}
