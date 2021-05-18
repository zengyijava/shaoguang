package com.montnets.emp.rms.rmsapi.comm;

import org.apache.http.client.HttpClient;

public interface HttpClientBuilder
{

	/**
	 * 获取http连接
	 *@anthor qiyin<15112605627@163.com>
	 *@return
	 */
	public HttpClient getHttClient();
}
