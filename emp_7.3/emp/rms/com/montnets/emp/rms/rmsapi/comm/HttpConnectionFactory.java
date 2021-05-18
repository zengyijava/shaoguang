package com.montnets.emp.rms.rmsapi.comm;

import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import com.montnets.emp.rms.rmsapi.comm.impl.KeepAliveHttpClientBuilder;
import com.montnets.emp.rms.rmsapi.comm.impl.ShortHttpClientBuilder;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;


/**
 * 
 * 文件名称:HttpConnectionFactory.java
 * 文件描述:http连接工程，底层会根据配置来选择是否使用http连接池。使用短连接不使用连接池。
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 下午04:29:03
 */
public class HttpConnectionFactory
{

	private static HttpConnectionFactory instance = new HttpConnectionFactory();

	private HttpConnectionFactory()
	{
	}

	public synchronized static HttpConnectionFactory getInstance()
	{
		if (instance == null)
		{
			instance = new HttpConnectionFactory();
		}
		return instance;
	}

	/**
	 * 根据不同配置来选择返回长连接还是短连接
	 *@anthor qiyin<15112605627@163.com>
	 *@return
	 */
	public synchronized HttpClient getHttpClient()
	{
		HttpClient httpClient = null;
		if (RMSHttpConstant.IS_KEEP_ALIVE)
		{
			// 长连接获取连接
			HttpClientBuilder httpClientBuilder = new KeepAliveHttpClientBuilder();
			httpClient = httpClientBuilder.getHttClient();
		} else
		{
			// 短连接获取连接
			HttpClientBuilder httpClientBuilder = new ShortHttpClientBuilder();
			httpClient = httpClientBuilder.getHttClient();
		}
		return httpClient;
	}

	public HttpPost httpPost(String httpUrl, List<BasicNameValuePair> params)
	{
		HttpPost httpPost = null;
		httpPost = new HttpPost(httpUrl);
		if (RMSHttpConstant.IS_KEEP_ALIVE)
		{
			// 设置为长连接，服务端判断有此参数就不关闭连接。
			httpPost.setHeader("Connection", RMSHttpConstant.KEEP_ALIVE);
		}

		return httpPost;
	}

}
