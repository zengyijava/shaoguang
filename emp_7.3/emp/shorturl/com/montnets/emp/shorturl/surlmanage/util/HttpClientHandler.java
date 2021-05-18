package com.montnets.emp.shorturl.surlmanage.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import com.montnets.emp.common.context.EmpExecutionContext;

@SuppressWarnings("deprecation")
public class HttpClientHandler {

	// http请求失败
	public static final String ERROR_310099 = "-310099";

	private static HttpClientHandler instance;

	private HttpClient httpClient;

	private HttpClientHandler(PoolManagerImpl manager) {
		this.httpClient = manager.buildHttpClient();
	}

	public static HttpClientHandler getInstance() {
		if (instance == null) {
			// 单例 并注入一个httpclient的实例
			instance = new HttpClientHandler(new PoolManagerImpl());
		}
		return instance;
	}

	/**
	 * 
	 * 
	 * @param url
	 *            请求地址
	 * @param body
	 *            请求体
	 * @param headers
	 *            请求头
	 * @param rescharset
	 *            响应字符编码
	 * @param reqcharset
	 *            请求字符编码
	 * @return
	 * 
	 * @Description: 执行请求
	 */
	public String execute(String url, String body, Map<String, String> headers,
			String rescharset, String reqcharset) {
		String result = null;
		HttpUriRequest request = null;
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(
				60000).setConnectionRequestTimeout(60000).setSocketTimeout(
				60000).build();
		if (null == body) {
			// get请求
			request = new HttpGet(url);
		} else {
			// post请求
			HttpPost post = new HttpPost(url);
			// 请求报文体字符编码
			if (HttpClientUtil.isBlankStr(reqcharset)) {
				reqcharset = HttpClientUtil.UTF_8;
			}
			StringEntity entity;
			entity = new StringEntity(body, reqcharset);
			// 设置报文体
			post.setEntity(entity);
			// 传递引用 方便下边的代码调用
			request = post;
		}

		// 设置请求报文头
		if (null != headers && headers.size() != 0) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				request.setHeader(header.getKey(), header.getValue());
			}
		}
		// 响应的字符编码
		if (HttpClientUtil.isBlankStr(rescharset)) {
			rescharset = HttpClientUtil.UTF_8;
		}
		// 得到响应报文
		result = getResponse(request, rescharset);
		return result;
	}

	/**
	 * 
	 * 
	 * @param request
	 * @param resCharset
	 * @return
	 * @throws IOException
	 * 
	 * @Description: 发送请求 并获取响应报文体
	 */
	private String getResponse(HttpUriRequest request, String resCharset) {
		String result = null;

		HttpResponse httpResponse = null;
		HttpEntity entity = null;
		try {
			// 请求
			httpResponse = httpClient.execute(request);
			/**
			 * 优化
			 */
			// 若状态码为200，则代表请求成功
			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == 200) {
				// 获取响应的实体
				entity = httpResponse.getEntity();
				// 响应的内容不为空，并且响应的内容长度大于0,则获取响应的内容
				if (entity != null && entity.getContentLength() > 0) {
					try {
						// 请求成功，能获取到响应内容
						result = EntityUtils.toString(entity, resCharset);
					} catch (Exception e) {
						// 获取内容失败，返回空字符串
						EmpExecutionContext.error(e, "获取内容失败");
						result = String.valueOf(ERROR_310099);
					}
				} else {
					// 请求成功，但是获取不到响应内容
					result = String.valueOf(ERROR_310099);
					EmpExecutionContext.error("调用短地址服务，响应实体为空");
				}
			} else {
				// 设置错误码
				result = String.valueOf(ERROR_310099);
				if (httpResponse !=null) {
					EmpExecutionContext.error("调用短地址服务，返回状态码："+httpResponse.getStatusLine().getStatusCode() );
				}
				
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "请求短地址服务中心异常");
			result = String.valueOf(ERROR_310099);
		} finally {
			if (entity != null) {
				entity = null;
			}
		}
		return result;
	}

}
