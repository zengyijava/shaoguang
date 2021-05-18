package com.montnets.emp.qyll.utils;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

public class HttpClientUtil {
/*
	private String url = "http://192.169.1.253:5050/appserver/app2server.req" ;
	private SimpleDateFormat sdfStime = new SimpleDateFormat("yyyyMMddhhmmssSSS");
	*/
	static Logger logger = Logger.getLogger(HttpClientUtil.class);
	
	public static String doPostClient1(JsonObject json ,String url) throws Exception{
		String msg ="";
		//1、创建HttpClinet对象
		HttpClient httpClient =new HttpClient();
		System.setProperty("apache.commons.httpclient.cookiespec","COMPATIBILITY");
		//2、创建请求方法的实例，需要POST方式就创建PostMethod，需要GET方式就创建GETMethod
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postMethod.addRequestHeader("Content-Type","text/html;charset=UTF-8");
		postMethod.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		try {
			RequestEntity entity = new StringRequestEntity(json.toString(),"application/json","UTF-8");
			postMethod.setRequestEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.toString());
		}
	/*	try {
			in = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
			postMethod.setRequestBody(in);
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.toString());
		}*/
		HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(10000L);
		httpClient.setParams(params);
		try {
			httpClient.executeMethod(postMethod);
			BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"UTF-8"));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str ="";
			while((str = reader.readLine())!=null){  
			    stringBuffer.append(str);  
			}  
			msg = stringBuffer.toString();  
//			JSONObject js= JSONObject.fromObject(ts);
		}catch (Exception e) {
			throw e;
		}finally{
			postMethod.releaseConnection();
		}
		return msg;
	}
	
	public static String doPostClient(JsonObject json ,String url){
		String msg ="";
		//1、创建HttpClinet对象
		HttpClient httpClient =new HttpClient();
		System.setProperty("apache.commons.httpclient.cookiespec","COMPATIBILITY");
		//2、创建请求方法的实例，需要POST方式就创建PostMethod，需要GET方式就创建GETMethod
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		postMethod.addRequestHeader("Content-Type","text/html;charset=UTF-8");
		postMethod.setRequestHeader("Content-Type", "text/html;charset=UTF-8");
		try {
			RequestEntity entity = new StringRequestEntity(json.toString(),"application/json","UTF-8");
			postMethod.setRequestEntity(entity);
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.toString());
		}
	/*	try {
			in = new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
			postMethod.setRequestBody(in);
		} catch (UnsupportedEncodingException e1) {
			logger.error(e1.toString());
		}*/
		HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(10000L);
		httpClient.setParams(params);
		try {
			httpClient.executeMethod(postMethod);
			BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"UTF-8"));  
			StringBuffer stringBuffer = new StringBuffer();  
			String str ="";
			while((str = reader.readLine())!=null){  
			    stringBuffer.append(str);  
			}  
			msg = stringBuffer.toString();  
			 
		}catch (Exception e) {
			logger.error(e.toString()+":MDOS服务器未开启");
		}finally{
			postMethod.releaseConnection();
		}
		return msg;
	}
	
	public static String doPostClient(String url, Map<String, String> param){
		String msg ="";
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(url);
		
		//设置postMethod中文乱码
		postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
//		GetMethod getMethod = new GetMethod(uri);
		NameValuePair[] pmValuePairs = new NameValuePair[param.size()];
		int i =0;
		for (String key : param.keySet()) {
			pmValuePairs [i] = new NameValuePair(key, param.get(key));
			i++;
		}
		postMethod.setRequestBody(pmValuePairs);
		HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(10000L);
		httpClient.setParams(params);
		try {
			httpClient.executeMethod(postMethod);
			//获取二进制的byte流
			byte[] b = postMethod.getResponseBody();
			msg = new String(b,"UTF-8");
		}catch (Exception e) {
			logger.error(e.toString()+":语音发送失败！");
		}finally{
			postMethod.releaseConnection();
		}
		return msg;
	}
	public static String doGetClient(String url){
		String msg ="";
		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod(url);
		//使用系统的默认的恢复策略  
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler()); 
		try {
			httpClient.executeMethod(getMethod);
			//获取二进制的byte流
			byte[] b = getMethod.getResponseBody();
			msg = new String(b,"UTF-8");
		}catch (Exception e) {
			logger.error(e.toString()+":语音发送失败！");
		}finally{
			getMethod.releaseConnection();
		}
		return msg;
	}
	
	public static void main(String[] args) {
		Map<String,String> param = new HashMap<String, String>();
		HttpClientUtil.doPostClient("http://192.169.1.96:/mdgg/MdosEcHttp.hts", param);
	}
}
