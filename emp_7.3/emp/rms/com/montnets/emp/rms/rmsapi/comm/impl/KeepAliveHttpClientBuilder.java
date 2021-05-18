package com.montnets.emp.rms.rmsapi.comm.impl;

import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.rmsapi.comm.HttpClientBuilder;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;

/**
 * 
 * 文件名称:KeepAliveHttpClientBuilder.java
 * 文件描述:获取长连接
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 下午04:17:13
 */
public class KeepAliveHttpClientBuilder implements HttpClientBuilder
{

	public HttpClient getHttClient()
	{
		return HttpConnectionManager.getInstance().getHttpClient();
	}

	/**
	 * 
	 * 文件名称:KeepAliveHttpClientBuilder.java
	 * 文件描述:连接池管理类
	 * 内容摘要:
	 * 修改日期       修改人员   版本	   修改内容 
	 * 2018-1-10    qiyin     0.1    0.1 新建
	 * 版权:版权所有(C)2018
	 * 公司:深圳市梦网科技有限发展公司
	 * @author:   qiyin<15112605627@163.com>
	 * @version:  0.1  
	 * @Date:     2018-1-10 下午04:17:27
	 */
	private static class HttpConnectionManager
	{
		private static HttpClient httpClient;

		private HttpConnectionManager()
		{
		}

		private static class ConnectorHolder
		{
			private static final HttpConnectionManager INSTANCE = new HttpConnectionManager();
		}

		public static HttpConnectionManager getInstance()
		{
			return ConnectorHolder.INSTANCE;
		}

		/**
		 * 获取http连接
		 *@anthor qiyin<15112605627@163.com>
		 *@return
		 */
		public synchronized HttpClient getHttpClient()
		{
			if (httpClient == null)
			{
				
					LayeredConnectionSocketFactory sslsf = null;
					try {
						sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
					} catch (NoSuchAlgorithmException e) {
						 EmpExecutionContext.error(e,"getHttpClient");
					}

					Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder
							.<ConnectionSocketFactory> create().register("https", sslsf)
							.register("http", new PlainConnectionSocketFactory()).build();
					PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);				
					cm.setMaxTotal(5 * RMSHttpConstant.POOL_NUMBER);
					cm.setDefaultMaxPerRoute(RMSHttpConstant.POOL_NUMBER);

					RequestConfig requestConfig = RequestConfig.custom()  
					        .setConnectTimeout(RMSHttpConstant.HTTP_REQUEST_TIMEOUT)
//						        .setConnectionRequestTimeout(1000)  
					        .setSocketTimeout(RMSHttpConstant.HTTP_RESPONSE_TIMEOUT).build();
					// 创建连接
					httpClient =  HttpClients.custom().setDefaultRequestConfig(requestConfig).setConnectionManager(cm).build();
				}

			return httpClient;
		}
	}

}
