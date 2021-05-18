package com.montnets.emp.common.biz;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.montnets.emp.common.context.EmpExecutionContext;

public class HttpBiz 
{

	/**
	 * 
	 * @description 发送请求
	 * @param obj 请求参数
	 * @param httpUrl 请求URL
	 * @param reqTimeOut 请求超时毫秒数
	 * @param respTimeOut 响应超时毫秒数
	 * @return 返回响应字符串，异常返回null
	 */
	public String SendRequest(Object obj, String httpUrl, int reqTimeOut, int respTimeOut)
	{
		StringBuffer sb = new StringBuffer();
		
		try
		{
			if(obj != null)
			{
				Class cls = obj.getClass();
				Field[] fields = cls.getDeclaredFields();
				String fieldName = null;
				String fieldNameUpper = null;
				Method getMethod = null;
				String value = null;
				//组装请求参数
				for (int i = 0; i < fields.length; i++)
				{
					fieldName = fields[i].getName();
					fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
					getMethod = cls.getMethod("get" + fieldNameUpper);
					value = (String) getMethod.invoke(obj);
					if(value != null) 
					{
						sb.append("&").append(fieldName).append("=").append(value);
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "Http请求，组装请求参数，异常。httpUrl="+httpUrl);
			return null;
		}
		
		//设置参数到httppost中
		String param = "";
		if(sb.length() > 0)
		{
			param = sb.toString().substring(1);
		}
		
		
		HttpClient httpclient = null;
		//发送请求开始时间
		try
		{
			HttpPost httppost = new HttpPost(httpUrl);
			httppost.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
			httppost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36");
			httppost.setHeader("Referer", httpUrl);
			
			StringEntity paramEntity = new StringEntity(param);
			httppost.setEntity(paramEntity);
			
			httpclient = new DefaultHttpClient();
			//设置请求超时时间
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, reqTimeOut);
			//设置响应超时时间
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, respTimeOut);
			//执行
			HttpEntity entity = httpclient.execute(httppost).getEntity();

			String result = null;
			//获取返回结果
			if(entity != null && entity.getContentLength() != -1) {
				result=EntityUtils.toString(entity);
			}
			return result;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "Http请求，异常。httpUrl="+httpUrl+",param="+param);
			return null;
		}
		finally
		{
			if(httpclient != null)
			{
				//关闭连接
				httpclient.getConnectionManager().shutdown();
				((DefaultHttpClient) httpclient).close();
			}
		}
	}
	

}

