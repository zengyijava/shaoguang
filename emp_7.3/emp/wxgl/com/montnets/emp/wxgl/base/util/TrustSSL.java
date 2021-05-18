/**
 * Program : TrustSSL.java
 * Author : Administrator
 * Create : 2013-9-13 下午05:03:13
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.wxgl.base.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.ottbase.util.GlobalMethods;

/**
 * https 请求接口调用
 * 
 * @project p_weix
 * @author zousy <zousy999@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-24 上午10:22:23
 * @description
 */
public class TrustSSL
{
	/**
	 * 证书信任管理器（用于https请求） 这个证书管理器的作用就是让它信任我们指定的证书，上面的代码意味着信任所有证书，不管是否权威机构颁发。
	 * @description 
	 * @project p_weix
	 * @company ShenZhen Montnets Technology CO.,LTD.
	 * @author fangyt <foyoto@gmail.com>
	 * @datetime 2013-10-21 上午10:25:00
	 */
	private static class TrustAnyTrustManager implements X509TrustManager
	{

		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
		{
		}

		public X509Certificate[] getAcceptedIssuers()
		{
			return new X509Certificate[] {};
		}
	}

	private class TrustAnyHostnameVerifier implements HostnameVerifier
	{
		public boolean verify(String hostname, SSLSession session)
		{
//			return true;
			//TODO
			return hostname.equalsIgnoreCase(session.getPeerHost());
		}
	}

	private SSLContext getSSLContent() throws NoSuchAlgorithmException, KeyManagementException
	{
		// 创建SSLContext对象，并使用我们指定的信任管理器初始化
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, new TrustManager[] {new TrustAnyTrustManager()}, new java.security.SecureRandom());
		return sc;
	}

	/**
	 * 获取凭证
	 * 
	 * @author Administrator
	 * @create 2013-9-13 下午05:20:16
	 * @since
	 * @param url
	 * @param appId
	 * @param appSecret
	 * @return {"access_token":"ACCESS_TOKEN","expires_in":7200}
	 * @throws Exception
	 */
	public JSONObject getRemoteAccessToken(String url, String appId, String appSecret) throws Exception
	{
		URL console = new URL(url + "&appid=" + appId + "&secret=" + appSecret);
		HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
		conn.setSSLSocketFactory(getSSLContent().getSocketFactory());
		conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
		conn.connect();
		InputStream is = conn.getInputStream();
		JSONObject json = (JSONObject) new JSONParser().parse(new InputStreamReader(is));
		return json;
	}

	/**
	 * 发布菜单
	 * 
	 * @author Administrator
	 * @create 2013-9-14 上午09:51:19
	 * @since
	 * @param url
	 * @param jsonMenu
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public JSONObject createRemoteMenu(String url, String jsonMenu, String token) throws Exception
	{
		//TODO
//		HttpClient httpClient = null;
		CloseableHttpClient httpClient = null;
		try {
			httpClient = new DefaultHttpClient();
			// 设置连接超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30 * 1000);
			// 设置Socket超时时间
			httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 30 * 1000);
			HttpPost post = new HttpPost(url + "access_token=" + token);

			/*URL console = new URL(url + "access_token=" + token);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(getSSLContent().getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setRequestMethod("POST");
			conn.setDoInput(true);
			conn.setDoOutput(true);
			OutputStream out = conn.getOutputStream();*/
			if(jsonMenu != null)
			{
				StringEntity postingString = new StringEntity(jsonMenu,"UTF-8");// json传递  
			    post.setEntity(postingString);
				// 编码
				//out.write(jsonMenu.getBytes("UTF-8"));
			}
			HttpResponse response = httpClient.execute(post);
			/*out.flush();
			out.close();
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(new InputStreamReader(conn.getInputStream()));*/
			String content = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(content);
			return jsonObject;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "发布菜单请求微信服务器异常。");
			return null;
		}finally
		{
			//关闭连接
			if(httpClient != null){
//			httpClient.getConnectionManager().shutdown();
				httpClient.close();	
			}
		}
	}
	
	/**
	 * 判断当前token是否可用
	 * @description 当前时间c在上次获取token的时间100分钟前，表明token没有过期
	 * @param token 当前token
	 * @param accessTime token生成时间
	 * @return    			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-27 上午10:22:13
	 */
	public boolean isAlive(String token, Timestamp accessTime)
	{
		if(!GlobalMethods.isInvalidString(token) && accessTime != null)
		{
			Calendar cal = Calendar.getInstance();
			cal.setTime(accessTime);
			cal.add(Calendar.MINUTE, 100);
			Calendar c = Calendar.getInstance();
			if(c.before(cal))
			{
				return true;
			}
		}
		return false;
	}
}
