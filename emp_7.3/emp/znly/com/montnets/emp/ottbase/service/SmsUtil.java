package com.montnets.emp.ottbase.service;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.util.TxtFileUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;

public class SmsUtil {

	
	private String httpUrl;
	/**
	 * 执行请求
	 * @param obj
	 * @param httpUrl
	 * @return
	 * @throws Exception
	 */
	public String execute(Object obj, String httpUrl) throws Exception {

	    this.httpUrl=httpUrl;
		String result = "";
		Class<? extends Object> cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		
		String fieldName = null;
		String fieldNameUpper = null;
		Method getMethod = null;
		String value = null;
//		HttpClient httpclient = null;
		//TODO
		CloseableHttpClient httpclient = null;
		try {
		
		//组装请求参数
		for (int i = 0; i < fields.length; i++)   {
			fieldName = fields[i].getName();
			fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			getMethod = cls.getMethod("get" + fieldNameUpper);
			value = (String) getMethod.invoke(obj);
			if(value != null) {
				sb.append("&").append(fieldName).append("=").append(value);
			}
		}
		//设置参数到httppost中
		String param = sb.toString().substring(1);
		StringEntity paramEntity = new StringEntity(param);
		//记录当前发送请求到日志记录，与发送号码文件同级目录
		new TxtFileUtil().writeSendResult(0l,httpUrl+"?"+param);
		HttpPost httppost = new HttpPost(httpUrl);

		httppost.setEntity(paramEntity);
		httpclient = new DefaultHttpClient();
		//执行
		HttpEntity entity = httpclient.execute(httppost).getEntity();
		//获取返回结果
		if(entity != null && entity.getContentLength() != -1) {
			result=EntityUtils.toString(entity);
			//EmpExecutionContext.error(result);
		}
		return result;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "execute");
			return result;
		}finally{
			//关闭连接
			if(httpclient != null){
//				httpclient.getConnectionManager().shutdown();
				httpclient.close();
			}
			
		}
		

	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}
	/**
	 * http请求
	 * @param method
	 * @param destUrl
	 * @param obj
	 * @return
	 */
	public String requestHttp(String method, String destUrl, Object obj) { 
		
		HttpURLConnection huc = null;
		URL url = null;
		String result="";
		InputStream in = null;
		try{
			//建立链接
			url = new URL(destUrl);
			//开启一个url连接
			huc = (HttpURLConnection) url.openConnection();
			//设置属性
			huc.setDoInput(true);  
			huc.setDoOutput(true);
			huc.setConnectTimeout(60000);
			
			if("POST".equals(method)){
				huc.setUseCaches(false);
			}
			huc.setRequestMethod(method);		
			huc.connect();
		    //设置参数值
			Class<? extends Object> cls = obj.getClass();
			Field[] fields = cls.getDeclaredFields();
			StringBuffer sb = new StringBuffer();
			
			String fieldName = null;
			String fieldNameUpper = null;
			Method getMethod = null;
			String value = null;
			for (int i = 0; i < fields.length; i++)   {
				fieldName = fields[i].getName();
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				getMethod = cls.getMethod("get" + fieldNameUpper);
				value = (String) getMethod.invoke(obj);
				if(value != null) {
					sb.append("&").append(fieldName).append("=").append(value);
				}
			}
			//设置输入流
			String param = sb.toString().substring(1);
			if(param != null && !"".equals(param)){
				huc.getOutputStream().write(param.getBytes("utf-8"));
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}			
			// 获取页面内容
			in = huc.getInputStream();
			BufferedReader breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			
			StringBuffer str = new StringBuffer();
			String line = null;
			//读出输出流
			while((line = breader.readLine()) != null)
			{
				str.append(line);
			}
			result = str.toString().trim();
	
	        //关闭连接
			huc.disconnect();
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "请求"+destUrl+"地址异常！");
		}finally{
			//关闭连接
			if(huc != null){
				huc.disconnect();
			}
		}
		//返回结果
		return result;
	}	
	
	/**
	 * http请求
	 * @param method
	 * @param destUrl
	 * @param obj
	 * @return
	 * @throws Exception 
	 */
	public String requestSMSHttp(String method, String destUrl, Object obj) throws Exception { 
		
		HttpURLConnection huc = null;
		URL url = null;
		String result="";
		InputStream in = null;
		try{
			//建立链接
			url = new URL(destUrl);
			//开启一个url连接
			huc = (HttpURLConnection) url.openConnection();
			//设置属性
			huc.setDoInput(true);  
			huc.setDoOutput(true);
			huc.setConnectTimeout(60000);
			
			if("POST".equals(method)){
				huc.setUseCaches(false);
			}
			huc.setRequestMethod(method);		
			huc.connect();
		    //设置参数值
			Class<? extends Object> cls = obj.getClass();
			Field[] fields = cls.getDeclaredFields();
			StringBuffer sb = new StringBuffer();
			
			String fieldName = null;
			String fieldNameUpper = null;
			Method getMethod = null;
			String value = null;
			for (int i = 0; i < fields.length; i++)   {
				fieldName = fields[i].getName();
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				getMethod = cls.getMethod("get" + fieldNameUpper);
				value = (String) getMethod.invoke(obj);
				if(value != null) {
					sb.append("&").append(fieldName).append("=").append(value);
				}
			}
			//设置输入流
			String param = sb.toString().substring(1);
			if(param != null && !"".equals(param)){
				huc.getOutputStream().write(param.getBytes("utf-8"));
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}
			new TxtFileUtil().writeSendResult(0l,destUrl+"?"+param);
			// 获取页面内容
			in = huc.getInputStream();
			BufferedReader breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			
			StringBuffer str = new StringBuffer();
			String line = null;
			//读出输出流
			while((line = breader.readLine()) != null)
			{
				str.append(line);
			}
			result = str.toString().trim();		
			
	        //关闭连接
			huc.disconnect();
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "请求"+destUrl+"地址异常！");
			throw e;
		}finally{
			//关闭连接
			if(huc != null){
				huc.disconnect();
			}
		}
		//返回结果
		return result;
	}	

	/**
	 * 请求http
	 * @param method 请求方式，GET/POST
	 * @param destUrl 请求目标url
	 * @param param 请求参数
	 * @param timeout 超时毫秒数，默认1分钟连接不上则超时
	 * @return 返回回应字符串
	 */
	public String requestHttp(String method, String destUrl, String param, Integer timeout) { 
		
		HttpURLConnection huc = null;
		URL url = null;
		String result="";
		InputStream in = null;
		BufferedReader breader =null;
		try{
			//建立链接
			url = new URL(destUrl);
			
			huc = (HttpURLConnection) url.openConnection();
			
			huc.setDoInput(true);  
			huc.setDoOutput(true);
			if(timeout == null)
			{
				//请求超时，默认1分钟
				huc.setConnectTimeout(60*1000);
			}else{
			huc.setConnectTimeout(timeout.intValue());
			}
			
			if("POST".equals(method)){
				huc.setUseCaches(false);
			}
			//设置请求方式
			huc.setRequestMethod(method);
			//建立连接
			huc.connect();
			//写参数
			if(param != null && !"".equals(param)){
				huc.getOutputStream().write(param.getBytes("utf-8"));
				huc.getOutputStream().flush();
				huc.getOutputStream().close();
			}
			
			//int code = huc.getResponseCode();
			// 获取页面内容
			in = huc.getInputStream();
			breader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			String str = breader.readLine();
			while (str != null)
			{
				result += str;
				str = breader.readLine();
			}
			
			//关闭连接
			huc.disconnect();
		}catch(Exception e){
			EmpExecutionContext.error(e,"请求http连接异常！");
		}finally{
			//关闭
			try {
				if(breader!=null){
					breader.close();
				}
				if(in!=null){
					in.close();
				}
				if(huc != null){
					huc.disconnect();
				}
			} catch (IOException e) {
				EmpExecutionContext.error(e,"关闭连接异常！");
			}
			
		}
		//返回结果
		return result;
	}
	
	/**
	 * http访问检查url是否连通
	 * @param url 检查地址
	 * @param type 1:mo;2:rpt
	 * @return noresult：无响应结果；recsuccess：成功连通，可用；recfail：成功连通，返回错误；recerror：异常失败
	 */
	public String checkHttpUrl(String url, Integer type)
	{
		try
		{
			
			String command = "";
			//String stat = "";
			String testcodevalue = "";
			if(type == 1)
			{
				//mo
				command = "MO_TEST";
				testcodevalue = "motest";
			}
			else if(type == 2)
			{
				//rpt
				command = "RT_TEST";
				testcodevalue = "rpttest";
			}
			else
			{
				//类型错误，无该类型
				return "typeerror";
			}
			
			StringBuffer paramEntity =new StringBuffer("command="+command);
			
			String responseStr = requestHttp("POST",url,paramEntity.toString(),5*1000);

			if(responseStr.length()==0)
			{
				//无响应结果
				return "noresult";
			}
			
			
			//处理响应
			
			String testcode = responseStr.substring(responseStr.lastIndexOf("=")+1);
			
			if(testcode.trim().equals(testcodevalue))
			{
				//成功接收上行请求
				return "recsuccess";
			}
			else {
				//失败
				return "recfail";
			}
			
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e,"http访问检查url是否连通异常！");
			return "recerror";
		}
		
	}

}

