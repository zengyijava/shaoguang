package com.montnets.emp.rms.rmsapi.comm.impl;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;

import com.montnets.emp.rms.rmsapi.comm.HttpClientBuilder;
import com.montnets.emp.rms.rmsapi.constant.RMSHttpConstant;

/**
 * 
 * 文件名称:ShortHttpClientBuilder.java
 * 文件描述:短连接
 * 内容摘要:
 * 修改日期       修改人员   版本	   修改内容 
 * 2018-1-10    qiyin     0.1    0.1 新建
 * 版权:版权所有(C)2018
 * 公司:深圳市梦网科技有限发展公司
 * @author:   qiyin<15112605627@163.com>
 * @version:  0.1  
 * @Date:     2018-1-10 下午03:24:52
 */
public class ShortHttpClientBuilder implements HttpClientBuilder
{

	
	/**
	 * 获取短http连接
	 */
	public HttpClient getHttClient()
	{
		RequestConfig requestConfig = RequestConfig.custom()  
		// 设置请求超时时间 设置为5秒
        .setConnectTimeout(RMSHttpConstant.HTTP_REQUEST_TIMEOUT)
        // 设置响应超时时间 设置为30秒
        .setSocketTimeout(RMSHttpConstant.HTTP_RESPONSE_TIMEOUT).build();
		
		// 创建连接
		HttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig).build();

		return httpClient;
	}

}
