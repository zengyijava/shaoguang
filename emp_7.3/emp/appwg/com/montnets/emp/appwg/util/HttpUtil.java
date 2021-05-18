package com.montnets.emp.appwg.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * HTTP工具类.
 * 
 * @author David.Huang
 */
public class HttpUtil
{

	/** 默认编码方式 -UTF8 */
	private static final String	DEFAULT_ENCODE	= "utf-8";

	/**
	 * 构造方法
	 */
	public HttpUtil()
	{
		// empty constructor for some tools that need an instance object of the
		// class
	}

	/**
	 * 下载文件保存到本地
	 * 
	 * @param path 文件保存位置
	 * @param url 文件地址
	 * @param header 头信息
	 * @return
	 */
	public static boolean downloadFile(String path, String url, Map<String, String> header)
	{
		DefaultHttpClient client = null;
		BufferedOutputStream bw = null;
		try
		{
			// 创建文件对象
			File f = new File(path);
			boolean mdidrRes = false;
			// 创建文件路径
			if(!f.getParentFile().exists()){
				mdidrRes = f.getParentFile().mkdirs();
			}
			else{
				mdidrRes = true;
			}
			if(!mdidrRes){
				EmpExecutionContext.error("下载文件保存到本地失败，生成文件目录失败。");
				return false;
			}
			
			// 创建HttpClient对象
			client = new DefaultHttpClient();
			// 获得HttpGet对象
			HttpGet httpGet = getHttpGet(url, null, header, null);
			// 发送请求获得返回结果
			HttpResponse response = client.execute(httpGet);
			// 如果成功
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				byte[] result = EntityUtils.toByteArray(response.getEntity());
				
				// 写入文件
				bw = new BufferedOutputStream(new FileOutputStream(path));
				bw.write(result);
				return true;
			}
			// 如果失败
			else
			{
				StringBuffer errorMsg = new StringBuffer();
				errorMsg.append("httpStatus:");
				errorMsg.append(response.getStatusLine().getStatusCode());
				errorMsg.append(response.getStatusLine().getReasonPhrase());
				errorMsg.append(", Header: ");
				Header[] headers = response.getAllHeaders();
				for (Header headerResp : headers)
				{
					errorMsg.append(headerResp.getName());
					errorMsg.append(":");
					errorMsg.append(headerResp.getValue());
				}
				EmpExecutionContext.error("HttpResonse Error:" + errorMsg);
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下载文件保存到本地异常,path=" + path + ",url=" + url);
			return false;
		}
		finally
		{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "finally shutdown error");
				}
			}
			if(client != null){
				client.close();

			}
		}
	}

	/**
	 * 文件上传到文件服务器
	 * @param fileSerurl 文件服务器url
	 * @param filePath 上传文件的路径
	 * @param params url参数
	 * @param headers 头信息参数
	 * @return 返回响应字符串
	 */
	public static String uploadToFileSer(String fileSerurl, String filePath, Map<String, String> params, Map<String, String> headers){
		File file = new File(filePath);
		if(!file.exists() || !file.isFile())
		{
			EmpExecutionContext.error("上传文件到App平台文件服务器失败，文件不存在。");
			return null;
		}
		String res = uploadToFileSer(fileSerurl, file, params, headers);
		return res;
	}
	
	/**
	 * 上传文件
	 * @param fileSerurl 文件服务器url
	 * @param targetFile 要上传的文件
	 * @param params 参数
	 * @param headers 消息头
	 * @return 
				String字符串	长度大于1的相对文件路径 （data/file/2013/2013-09-17.........）
						0	FileUploadException解析出错
						1	传入的参数type、md5、size、corp_code、file_suffix 中有null值
						2	索引中存在，但文件系统中不存在 
						3	Request传入MD5	值校验不一致！请检查MD5值并重新提交POST上传请求
						4	文件大小超过最大100M限制
						其他	HTTP status code
						异常	null
			
	 */
	public static String upload(String fileSerurl, File targetFile, Map<String, String> params, Map<String, String> headers)
	{

		PostMethod filePost = new PostMethod(fileSerurl);

		try
		{
			// 设置参数
			Part[] parts = setParams(params);
			// 设置投信息
			setHeaders(headers, filePost);

			parts[parts.length-1] = new FilePart("file_name", targetFile);

			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
			client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
			int status = client.executeMethod(filePost);

			if(status == HttpStatus.SC_OK)
			{
				//访问成功
				String respStr = getResponseStr(filePost);
				return respStr;
			}
			else
			{
				//访问失败
				EmpExecutionContext.error("上传文件失败，httpstatuscode="+status);
				return String.valueOf(status);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件异常。");
			return null;
		}
		finally
		{
			filePost.releaseConnection();
		}
	}
	
	/**
	 * 获取上传文件响应字符串
	 * @param filePost http请求对象
	 * @return 返回响应字符串，异常返回null
	 */
	private static String getResponseStr(PostMethod filePost){
		try
		{
			BufferedReader reader = new BufferedReader(new InputStreamReader(filePost.getResponseBodyAsStream()));
			StringBuffer stringBuffer = new StringBuffer();
			String str = "";
			while((str = reader.readLine())!=null){
				stringBuffer.append(str);
			}
			String ts = stringBuffer.toString();
			return ts;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取上传文件响应字符串异常。");
			return null;
		}
		
	}
	
	/**
	 * 设置请求参数
	 * @param params 参数集合，key为name，value为值
	 */
	private static Part[] setParams(Map<String, String> params){
		

		if(params == null || params.size() == 0){
			return new Part[1];
		}
		
		Part[] parts = new Part[params.size()+1];
		int index = 0;
		for (String name : params.keySet())
		{
			parts[index] = new StringPart(name, params.get(name));
			index++;
		}
		return parts;
	}
	
	/**
	 * 设置请求头参数
	 * @param headers 请求头参数集合，key为name，value为值
	 * @param filePost http请求对象
	 */
	private static void setHeaders(Map<String, String> headers, PostMethod filePost){
		
		if(headers == null || headers.size() == 0){
			return;
		}
		try
		{
			for (String name : headers.keySet())
			{
				org.apache.commons.httpclient.Header header = new org.apache.commons.httpclient.Header();
				header.setName(name);
				header.setValue(headers.get(name));
				filePost.setRequestHeader(header);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置请求头参数异常。");
		}
		
		
		   
		   
	}
	
	/**
	 * 文件上传到文件服务器
	 * @param fileSerurl 文件服务器url
	 * @param file 上传文件对象
	 * @param params url参数
	 * @param headers 头信息参数
	 * @return 返回响应字符串
	 */
	public static String uploadToFileSer(String fileSerurl, File file, Map<String, String> params, Map<String, String> headers) 
	{
		DataInputStream in = null;
		OutputStream out = null;
		BufferedReader reader = null;
		try
		{
			
			String url = getUriStr(fileSerurl, params, null);
			URL urlObj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
			
			con.setRequestMethod("POST"); // 以Post方式提交表单，默认get方式
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false); // post方式不能使用缓存
			// 设置请求头信息
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			// 设置边界
			String BOUNDARY = "----------" + System.currentTimeMillis();
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
			
			//设置用户header
			setHeaderValue(con, headers);
			
			// 请求正文信息
			StringBuilder sb = new StringBuilder();
			// 必须多两道线
			sb.append("--");
			sb.append(BOUNDARY);
			sb.append("\r\n");
			sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
			sb.append("Content-Type:application/octet-stream\r\n\r\n");
			byte[] head = sb.toString().getBytes("utf-8");
			// 获得输出流
			out = new DataOutputStream(con.getOutputStream());
			// 输出表头
			out.write(head);
			// 文件正文部分
			// 把文件已流文件的方式 推入到url中
			in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while((bytes = in.read(bufferOut)) != -1)
			{
				out.write(bufferOut, 0, bytes);
			}
			
			// 结尾部分
			byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// 定义最后数据分隔线
			out.write(foot);
			out.flush();

			StringBuffer buffer = new StringBuffer();

			// 定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while((line = reader.readLine()) != null)
			{
				buffer.append(line);
			}
			
			return buffer.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到App平台文件服务器异常。");
			return null;
		}
		finally
		{
			try
			{
				if(in != null){
					in.close();
				}
				if(out != null){
					out.close();
				}
				if(reader != null)
				{
					reader.close();
				}
			}
			catch (Exception e2)
			{
				EmpExecutionContext.error(e2, "上传文件关闭资源异常。");
			}
			
			
		}
	}
	
	/**
	 * 获得uri字符串
	 * 
	 * @param url
	 *        请求地址
	 * @param params
	 *        请求参数
	 * @param encode
	 *        编码方式
	 * @return uri字符串
	 */
	private static String getUriStr(String url, Map<String, String> params, String encode)
	{
		
		if(params == null)
		{
			return url;
		}
		try
		{
			if(encode == null || encode.length() == 0){
				encode = DEFAULT_ENCODE;
			}
			StringBuffer buf = new StringBuffer(url);
			// 地址增加?或者&
			String flag = (url.indexOf('?') == -1) ? "?" : "&";
			// 添加参数
			for (String name : params.keySet())
			{
				buf.append(flag);
				buf.append(name);
				buf.append("=");
				try
				{
					String param = params.get(name);
					if(param == null)
					{
						param = "";
					}
					buf.append(URLEncoder.encode(param, encode));
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "URLEncoder Error,encode=" + encode + ",param=" + params.get(name));
				}
				flag = "&";
			}
		
			return buf.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取请求url异常。");
			return null;
		}
		
	}
	
	/**
	 * 设置头信息
	 * @param con 连接对象
	 * @param header 头信息对象
	 * @return 成功返回true
	 */
	private static boolean setHeaderValue(HttpURLConnection con, Map<String, String> header)
	{
		if(header == null)
		{
			return true;
		}
		
		try
		{
			for (String key : header.keySet())
			{
				con.setRequestProperty(key, header.get(key));
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置请求头信息异常。");
			return false;
		}
			
	}
	
	/**
	 * POST请求, 结果以字符串形式返回.
	 * 
	 * @param url
	 *        请求地址
	 * @param params
	 *        请求参数
	 * @param reqHeader
	 *        请求头内容
	 * @param encode
	 *        编码方式
	 * @return 内容字符串
	 * @throws Exception
	 */
	public static String postUrlAsString(String url, Map<String, String> params, Map<String, String> reqHeader, String encode) throws Exception
	{
		// 获得HttpPost对象
		HttpPost httpPost = getHttpPost(url, params, encode);
		// 发送请求
		String result = executeHttpRequest(httpPost, reqHeader);
		// 返回结果
		return result;
	}

	/**
	 * 获得HttpGet对象
	 * 
	 * @param url
	 *        请求地址
	 * @param params
	 *        请求参数
	 * @param encode
	 *        编码方式
	 * @return HttpGet对象
	 */
	private static HttpGet getHttpGet(String url, Map<String, String> params, Map<String, String> header, String encode)
	{
		StringBuffer buf = new StringBuffer(url);
		if(params != null)
		{
			// 地址增加?或者&
			String flag = (url.indexOf('?') == -1) ? "?" : "&";
			// 添加参数
			for (String name : params.keySet())
			{
				buf.append(flag);
				buf.append(name);
				buf.append("=");
				try
				{
					String param = params.get(name);
					if(param == null)
					{
						param = "";
					}
					buf.append(URLEncoder.encode(param, encode));
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "URLEncoder Error,encode=" + encode + ",param=" + params.get(name));
				}
				flag = "&";
			}
		}
		
		HttpGet httpGet = new HttpGet(buf.toString());
		
		if(header != null)
		{
			// 添加参数
			for (String name : header.keySet())
			{
				httpGet.setHeader(name, header.get(name));
			}
		}
		
		return httpGet;
	}

	/**
	 * 获得HttpPost对象
	 * 
	 * @param url
	 *        请求地址
	 * @param params
	 *        请求参数
	 * @param encode
	 *        编码方式
	 * @return HttpPost对象
	 */
	private static HttpPost getHttpPost(String url, Map<String, String> params, String encode)
	{
		HttpPost httpPost = new HttpPost(url);
		if(params != null)
		{
			List<NameValuePair> form = new ArrayList<NameValuePair>();
			for (String name : params.keySet())
			{
				form.add(new BasicNameValuePair(name, params.get(name)));
			}
			try
			{
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, encode);
				httpPost.setEntity(entity);
			}
			catch (UnsupportedEncodingException e)
			{
				EmpExecutionContext.error(e, "UrlEncodedFormEntity Error,encode=" + encode + ",form=" + form);
			}
		}
		return httpPost;
	}

	/**
	 * 执行HTTP请求
	 * 
	 * @param request
	 *        请求对象
	 * @param reqHeader
	 *        请求头信息
	 * @return 内容字符串
	 */
	private static String executeHttpRequest(HttpUriRequest request, Map<String, String> reqHeader) throws Exception
	{
		DefaultHttpClient client = null;
		String result = null;
		try
		{
			// 创建HttpClient对象
			client = new DefaultHttpClient();
			// 设置连接超时时间
			client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60);
			// 设置Socket超时时间
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 36600);
			// 设置请求头信息
			if(reqHeader != null)
			{
				for (String name : reqHeader.keySet())
				{
					request.addHeader(name, reqHeader.get(name));
				}
			}
			// 获得返回结果
			HttpResponse response = client.execute(request);
			// 如果成功
			if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
			{
				result = EntityUtils.toString(response.getEntity());
			}
			// 如果失败
			else
			{
				StringBuffer errorMsg = new StringBuffer();
				errorMsg.append("httpStatus:");
				errorMsg.append(response.getStatusLine().getStatusCode());
				errorMsg.append(response.getStatusLine().getReasonPhrase());
				errorMsg.append(", Header: ");
				Header[] headers = response.getAllHeaders();
				for (Header header : headers)
				{
					errorMsg.append(header.getName());
					errorMsg.append(":");
					errorMsg.append(header.getValue());
				}
				EmpExecutionContext.error("HttpResonse Error:" + errorMsg);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "http连接异常");
			throw new Exception("http连接异常");
		}
		finally
		{
			if(client != null){
				client.close();
			}
		}
		return result;
	}

	
	
	public static void main(String[] args) throws IOException
	{
		
		/*WgMwFileBiz fileBiz = new WgMwFileBiz();
		
		String filePath = "C:\\Users\\benwork\\Desktop\\temp\\1.jpg";
		
		String res = fileBiz.uploadToMwFileSer(filePath, "10", "sz10000");*/
		
		 /*try
		{
			 for(int i=0;i<10000;i++){
					long msgid=540300000+i;
					String url = "http://192.169.1.253:8080/emp_sta/moreceive.hts" +
					"?command=MO_REQUEST" +
					"&momsgid=" + msgid +
					"&spid=TEST01" +
					"&sa=13798242456" +
					"&dc=15" +
					"&sm=412361646d696e6473653332343233343233343233343233" +
					"&sppassword=123456" +
					"&ec=0" +
					"&da=1065799991001" +
					"&exno=";
					postUrlAsString(url, null, null, null);
					
			}
			
			
			System.out.println("搞完");
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		// System.out.println(result);
		//downloadFile("E:/temp/logo3w.jpg", "http://www.nqwang.com/uploads/allimg/130313/1421129526_0.jpg", null);
	}
}
