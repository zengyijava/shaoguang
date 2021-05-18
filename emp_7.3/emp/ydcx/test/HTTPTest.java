package test;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Locale;

public class HTTPTest
{

	/**
	 * @description    
	 * @param args       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-7-30 下午01:43:29
	 */
	public static void main(String[] args)
	{
		HttpPost httppost = null;
//		   HttpClient httpclient = null;
		//TODO
		CloseableHttpClient  httpclient = new DefaultHttpClient();
		long start = 0;
		long end = 0;
		try
		{
			String httpUrl = "http://receipt.mms.360buy.com/safeReport/reportsUpload.action";

			String result = "";
			String param = String.valueOf(CityHash.hash64("{\"signature\":\"manDao.com\",\"reports\":[{\"desc\":\"DELV\",\"arrived\":1440575613170,\"reportId\":\"111111111\",\"mobileNum\":\"18500132008\",\"token\":2558040114848934640}]}"));
			StringEntity paramEntity = new StringEntity(param);
			if(httppost == null)
			{
				httppost = new HttpPost(httpUrl);
			}

			//httppost.setEntity(paramEntity);
			//设置连接超时时间
			httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 120000);
			//设置请求超时时间
			httpclient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 120000);
			start = System.currentTimeMillis();
			//执行
			HttpEntity entity = httpclient.execute(httppost).getEntity();
			end = System.currentTimeMillis();
			System.out.println("发送HTTP请求成功，共耗时"+((end-start)/1000)+"秒，请求开始时间："+getDateStr(start)+"，请求结束时间:"+getDateStr(end));
			//获取返回结果
			if(entity != null && entity.getContentLength() != -1) {
				result=EntityUtils.toString(entity);
			}
			System.out.println(result);
		}
		catch (Exception e)
		{
			end = System.currentTimeMillis();
			System.out.println("发送HTTP请求失败，共耗时"+((end-start)/1000)+"秒，请求开始时间："+getDateStr(start)+"，请求结束时间:"+getDateStr(end));
		}finally
		{
			try {
				httpclient.close();
			} catch (IOException e) {
				System.out.println("关闭异常");
			}
		}

	}
	private static String getDateStr(long millis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		Formatter ft=new Formatter(Locale.CHINA);
		try {
			return ft.format("%1$tY-%1$tm-%1$td %1$tT", cal).toString();
		} catch (Exception e) {
			return "";
		}finally{
			ft.close();
		}
		
		}

}
