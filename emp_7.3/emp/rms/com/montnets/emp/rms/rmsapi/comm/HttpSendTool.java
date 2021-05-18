package com.montnets.emp.rms.rmsapi.comm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;
import com.montnets.emp.rms.rmsapi.util.JsonEntity;

/**
 * 
 * 文件名称:HttpSendTool.java
 * 文件描述:http发送工具
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 上午11:23:04
 */
public class HttpSendTool {
	private static Integer reSendTime = 1;
	/**
	 * 发送Post
	 *@anthor qiyin<15112605627@163.com>
	 *@param ip   ip
	 *@param port   端口
	 *@param httpUrl 请求地址
	 *@param obj   参数
	 *@param reSend   异常处理次数
	 *@return 返回结果
	 *@throws Exception
	 */
	public String sendPost(String ip, int port, String httpUrl, Object obj, int reSend) throws Exception
	{

		HttpEntity entity = null;
		HttpResponse httpResponse = null;
		String result = "";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		HttpClient httpClient = null;
		try
		{
			// 解析参数
//			parseParam(obj, params);
			String jsonString=JSONObject.toJSONString(obj);
			EmpExecutionContext.info("网关发送接口信息:"+jsonString);
//			json.p
			// 创建连接
			httpClient = HttpConnectionFactory.getInstance().getHttpClient();

			String path = httpUrl;
			if (httpUrl.indexOf("http:") == -1)
			{
				// 拼接地址：
				path = "http://" + ip + ":" + port + httpUrl;
			}

			// 设置请求头和报文
			HttpPost httpPost = HttpConnectionFactory.getInstance().httpPost(path, params);
			// 设置报文和通讯格式,里面会打印请求报文
			StringEntity stringEntity=new StringEntity(jsonString, RMSHttpConstant.UTF8_ENCODE);
			stringEntity.setContentEncoding(RMSHttpConstant.UTF8_ENCODE);      
			stringEntity.setContentType(RMSHttpConstant.APPLICATION_JSON);      
//			httpPost.setEntity();
			httpPost.setEntity(stringEntity);
//			httpPost.get
			httpResponse = httpClient.execute(httpPost);
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == RMSHttpConstant.HTTP_SUCCESS_CODE)
			{
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0)
				{
					try
					{
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity);
						EmpExecutionContext.error("响应报文：" + result);
					} catch (Exception e)
					{
						// 记录日志
						EmpExecutionContext.error(e, "解析响应报文异常");
						// 获取内容失败，返回空字符串
						result = "";
					} finally
					{
						EntityUtils.consume(entity);
					}
				} else
				{
					// 请求成功，但是获取不到响应内容
					result = "";
				}
			} else if (httpResponse != null)
			{
				// 非200状态
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0)
				{
					try
					{
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity);
						EmpExecutionContext.error("响应报文：" + result);
					} catch (Exception e)
					{
						// 记录日志
						EmpExecutionContext.error(e, "解析响应报文异常");
						// 获取内容失败，返回空字符串
						result = "";
					} finally
					{
						EntityUtils.consume(entity);
					}
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送http请求异常");
			if (reSend >= RMSHttpConstant.RESEND_DONE)
			{
				EmpExecutionContext.error(e, "发送一次，进行重发。进行第" + (reSend - 1) + "次重发");
				result = sendPost(ip, port, httpUrl, obj, reSend - 1);
				if (result != null && !"".equals(result))
				{
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 上传模板专用请求
	 *@anthor qiyin<15112605627@163.com>
	 *@param ip
	 *@param port
	 *@param httpUrl
	 *@param obj
	 *@param reSend
	 *@return
	 *@throws Exception
	 */
	public String sendPostSubTemplate(String ip, int port, String httpUrl, Object obj, int reSend) throws Exception
	{
		
		HttpEntity entity = null;
		HttpResponse httpResponse = null;
		String result = "";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		HttpClient httpClient = null;
		try
		{
			// 解析参数
			parseParam(obj, params);
			
			// 创建连接
			httpClient = HttpConnectionFactory.getInstance().getHttpClient();
			
			String path = httpUrl;
			if (httpUrl.indexOf("http:") == -1)
			{
				// 拼接地址：
				path = "http://" + ip + ":" + port + httpUrl;
			}
			
			// 设置请求头和报文
			HttpPost httpPost = HttpConnectionFactory.getInstance().httpPost(path, params);
			// 设置报文和通讯格式,里面会打印请求报文
			httpPost.setEntity(new JsonEntity(params, RMSHttpConstant.UTF8_ENCODE));
			httpResponse = httpClient.execute(httpPost);
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == RMSHttpConstant.HTTP_SUCCESS_CODE)
			{
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0)
				{
					try
					{
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity);
						EmpExecutionContext.error("响应报文：" + result);
					} catch (Exception e)
					{
						// 记录日志
						EmpExecutionContext.error(e, "解析响应报文异常");
						// 获取内容失败，返回空字符串
						result = "";
					} finally
					{
						EntityUtils.consume(entity);
					}
				} else
				{
					// 请求成功，但是获取不到响应内容
					result = "";
				}
			} else if (httpResponse != null)
			{
				// 非200状态
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0)
				{
					try
					{
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity);
						EmpExecutionContext.error("响应报文：" + result);
					} catch (Exception e)
					{
						// 记录日志
						EmpExecutionContext.error(e, "解析响应报文异常");
						// 获取内容失败，返回空字符串
						result = "";
					} finally
					{
						EntityUtils.consume(entity);
					}
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送http请求异常");
			if (reSend >= RMSHttpConstant.RESEND_DONE)
			{
				EmpExecutionContext.error(e, "发送一次，进行重发。进行第" + (reSend - 1) + "次重发");
				result = sendPost(ip, port, httpUrl, obj, reSend - 1);
				if (result != null && !"".equals(result))
				{
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 发送Post to Mboss
	 *@anthor qiyin<15112605627@163.com>
	 *@param ip   ip
	 *@param port   端口
	 *@param httpUrl 请求地址
	 *@param obj   参数
	 *@param reSend   异常处理次数
	 *@return 返回结果
	 *@throws Exception
	 */
	public String sendPostToMBOSS(String ip, int port, String httpUrl, Object obj, int reSend) throws Exception {
		HttpEntity entity = null;
		HttpResponse httpResponse = null;
		String result = "";
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		HttpClient httpClient = null;
		try {
			// 解析参数
			parseParam(obj, params);
			// 创建连接
			httpClient = HttpConnectionFactory.getInstance().getHttpClient();
			
			String path=httpUrl;
			if (!httpUrl.contains("http:")) {
				//拼接地址：
				path = "http://" + ip + ":" + port + httpUrl;
			}
			
			// 设置请求头和报文
			HttpPost httpPost = HttpConnectionFactory.getInstance().httpPost(path, params);
			// 设置报文和通讯格式
			httpPost.setEntity(new UrlEncodedFormEntity(params, RMSHttpConstant.UTF8_ENCODE));
//			httpPost.setHeader("Content-Type", "application/json");
			EmpExecutionContext.info("企业富信-数据查询-发送明细查询。请求报文："+params.toString());
			httpResponse = httpClient.execute(httpPost);
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == RMSHttpConstant.HTTP_SUCCESS_CODE) {
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0) {
					try {
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity);
						EmpExecutionContext.info("企业富信-数据查询-发送明细查询。响应报文："+result);
					} catch (Exception e) {
						// 记录日志
						EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询。解析响应报文异常");
						// 获取内容失败，返回空字符串
						result = "";
					}finally{
						EntityUtils.consume(entity);
					}
				}
			}else if(httpResponse != null){
				//非200状态
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0) {
					try {
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity);
						EmpExecutionContext.info("企业富信-数据查询-发送明细查询。响应报文：" + result);
					} catch (Exception e) {
						// 记录日志
						EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询。解析响应报文异常");
						// 获取内容失败，返回空字符串
						result = "";
					}finally{
						EntityUtils.consume(entity);
					}
				} 
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询。发送http请求异常");
			if (reSend >= RMSHttpConstant.RESEND_DONE) {
				EmpExecutionContext.error(e, "企业富信-数据查询-发送明细查询。发送一次，进行重发。进行第" + reSendTime++ + "次重发");
				result = sendPostToMBOSS(ip, port, httpUrl, obj, reSend - 1);
				if (result != null &&!"".equals(result)) {
					return result;
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private void parseParam(Object obj, List<BasicNameValuePair> params) throws NoSuchMethodException,
			IllegalAccessException, InvocationTargetException
	{
		// 设置请求的参数
		String fieldName = null;
		String fieldNameUpper = null;
		Method getMethod = null;
		Class cls = obj.getClass();
		Field[] fields = cls.getDeclaredFields();
		Object value = null;
		for (int i = 0; i < fields.length; i++)
		{
			fieldName = fields[i].getName();
			if (!fieldName.equals("serialVersionUID"))
			{
				fieldNameUpper = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
				getMethod = cls.getMethod("get" + fieldNameUpper);
				value = getMethod.invoke(obj);
				if (value != null)
				{
					params.add(new BasicNameValuePair(fieldName, String.valueOf(value)));
				}
			}
		}
	}

}
