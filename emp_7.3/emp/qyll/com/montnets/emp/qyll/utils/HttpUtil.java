package com.montnets.emp.qyll.utils;

import com.montnets.emp.common.context.EmpExecutionContext;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Http工具类，用于模拟Http请求。
 * @author xiebk
 *
 */
public class HttpUtil {
	//日志输出类
	private static final Logger LOG = Logger.getLogger(HttpUtil.class);
	
	public static String sendPost(String url , String json){
		//返回信息
		String response = "";
		//输出流
		OutputStreamWriter out = null;
		//输入流
		BufferedReader br = null;
		//http 连接
		URL httpUrl = null; 
		
		HttpURLConnection conn= null;
		try {
			//创建URl
			httpUrl = new URL(url);
			//建立连接
			conn = (HttpURLConnection) httpUrl.openConnection();
			//设置请求方式，post请求
			conn.setRequestMethod("POST");
			//设置头信息
			conn.setRequestProperty("Content-Type", "application/json");
//			conn.setRequestProperty("connection", "keep-alive");
			//设置开启输入输出
			conn.setDoInput(true);
			conn.setDoOutput(true);
			//进行连接
			conn.connect();
			//获取输出流
			out = new OutputStreamWriter(conn.getOutputStream());
			//发送JSON数据
			out.write(json);
			out.flush();
			
			//读取响应信息
			String lines;
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
			while((lines = br.readLine()) != null){
				lines = new String(lines.getBytes(),"UTF-8");
				response += lines;
			}
//			EmpExecutionContext.info("====================请求的返回数据："+response);
			br.close();
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"发送post请求失败，URL："+url+",json:"+json);
//			LOG.error("发送post请求失败，URL："+url+",json:"+json,e);
		}finally{//清理资源
			if(conn != null){
				conn.disconnect();
			}
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
                    EmpExecutionContext.error(e,"发现异常！");
				}
			}
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
                    EmpExecutionContext.error(e,"发现异常！");
				}
			}
		}
		return response;
	}
	
}



