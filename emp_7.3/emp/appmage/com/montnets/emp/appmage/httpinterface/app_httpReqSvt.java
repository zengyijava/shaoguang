package com.montnets.emp.appmage.httpinterface;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.appwg.bean.AppMessage;
import com.montnets.emp.appwg.biz.WgMwCommuteBiz;
import com.montnets.emp.appwg.biz.WgMwFileBiz;
import com.montnets.emp.appwg.biz.WgWeiCommuteBiz;
import com.montnets.emp.appwg.initbuss.HandleMsgBiz;
import com.montnets.emp.appwg.initbuss.IInterfaceBuss;
import com.montnets.emp.appwg.wginterface.IWgMwCommuteBiz;
import com.montnets.emp.appwg.wginterface.IWgMwFileBiz;
import com.montnets.emp.appwg.wginterface.IWgWeiCommuteBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.util.ChangeCharset;
import com.montnets.emp.util.GetSxCount;
import com.montnets.emp.util.TxtFileUtil;


public class app_httpReqSvt  extends BaseServlet
{

	private static final long	serialVersionUID	= 1L;
	
	private static TxtFileUtil tfu = new TxtFileUtil();
	
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) 
	{
		OutputStream os = null;
		String respCommand = "";
		//未知异常
		String errcode = "appreq:-999";
		String testCode = "";
		String paramStr = "";
		//测试标识，true为该状态报告为测试，不进行推送
		boolean isTest = false;
		
		try{
			
			os = response.getOutputStream();
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType("application/octet-stream");
			
			//返回参数对象
			AppHttpParam httpParam = null;
			
			//操作命令
			String command = request.getParameter("command");
			if(command == null || command.trim().length() == 0){
				//不存在的操作命令
				errcode = "appreq:-1";
				EmpExecutionContext.error("请求AppHttp，command参数为空。");
			}
			//发送单文字公众消息
			else if("mt_e_txtmsg".equals(command)){
				//调用对应的请求处理
				httpParam = mt_e_txtmsg(request, response);
				//获取错误代码
				errcode = httpParam.getErrcode();
				//获取需要返回的参数和值
				paramStr = httpParam.getParams();
				respCommand = httpParam.getRespCommand();
			}
			//发送单媒体公众消息
			else if("mt_e_mediamsg".equals(command)){
				httpParam = mt_e_mediamsg(request, response);
				//获取错误代码
				errcode = httpParam.getErrcode();
				//获取需要返回的参数和值
				paramStr = httpParam.getParams();
				respCommand = httpParam.getRespCommand();
			}
			else if("rt_test".equals(command.toUpperCase().trim()))
			{
				errcode = "000";
				testCode = "&testcode=reqtest";
				isTest = true;
			}
			else{
				//不存在的操作命令
				errcode = "appreq:-1";
				EmpExecutionContext.error("请求AppHttp，command参数不正确，不存在该操作命令。");
			}

		}
		catch(Exception e){
			EmpExecutionContext.error(e, "app_HttpReqSvt处理请求异常。");
			return;
		}
		finally{
			
			String resp = "command="+respCommand+"&mterrcode="+errcode+testCode+paramStr;
			response.setContentLength(resp.getBytes().length);
				
			try
			{
				if(os != null){
					os.write(resp.getBytes());
					os.close();
				}
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e, "app_HttpReqSvt处理请求，写流异常。");
			}
			
			EmpExecutionContext.debug("响应Apphttp请求！");
		}
		
		if(isTest)
		{
			EmpExecutionContext.debug("该状态报告为测试，不进行推送!");
			//该状态报告为测试，不进行推送
			return;
		}
		
	}
	
	/**
	 * 发送公众文字消息
	 * @param request
	 * @param response
	 */
	private AppHttpParam mt_e_txtmsg(HttpServletRequest request, HttpServletResponse response){
		
		AppHttpParam httpParam = new AppHttpParam();
		httpParam.setRespCommand("mt_e_txtresp");
		try
		{
			//企业编码
			String ecode = request.getParameter("ecode");
			if(ecode == null || ecode.trim().length() == 0){
				httpParam.setErrcode("appreq:-2");
				return httpParam;
			}
			
			//发送者
			String sa = request.getParameter("sa");
			if(sa == null || sa.trim().length() == 0){
				httpParam.setErrcode("appreq:-3");
				return httpParam;
			}
			
			//消息类型，1：app消息；2：微信消息。，默认1:app消息
			String tomsgtype = request.getParameter("tomsgtype");
			if(tomsgtype == null || tomsgtype.trim().length() == 0){
				tomsgtype = "1";
			}
			else if(!"1".equals(tomsgtype) && !"2".equals(tomsgtype)){
				httpParam.setErrcode("appreq:-15");
				return httpParam;
			}
			
			//接收者
			String da = request.getParameter("da");
			if(da == null || da.trim().length() == 0){
				httpParam.setErrcode("appreq:-4");
				return httpParam;
			}
			String[] daArray = da.split(",");
			if(daArray.length > 100){
				httpParam.setErrcode("appreq:-5");
				return httpParam;
			}
			
			//发送内容
			String sm = request.getParameter("sm");
			if(sm == null || sm.length() == 0){
				httpParam.setErrcode("appreq:-6");
				return httpParam;
			}
			sm = ChangeCharset.toStringHex(sm);
			
			AppMessage appMsg = new AppMessage();
			appMsg.setMsgContent(sm);
			appMsg.setMsgType(0);
			
			//操作员id
			String userid = request.getParameter("userid");
			if(userid != null && userid.trim().length() > 0){
				appMsg.setUserId(Long.valueOf(userid));
			}
			
			//标题
			String title = request.getParameter("title");
			if(title != null && title.trim().length() > 0){
				appMsg.setTitle(title);
			}
			
			appMsg.setEcode(ecode);
			appMsg.setFromUserName(sa);
			
			//构建接收者set
			LinkedHashSet<String> toUserNameSet = new LinkedHashSet<String>();
			for(String toUser : daArray){
				toUserNameSet.add(toUser);
			}
			
			appMsg.setToUserNameSet(toUserNameSet);
			appMsg.setToType(2);
			
			//消息类型，1：app消息；2：微信消息。，默认app消息
			if("1".equals(tomsgtype)){
				IInterfaceBuss wgBiz = new HandleMsgBiz();
				int sendRes = wgBiz.SendAppPublicMsg(appMsg);
				if(sendRes == 1){
					httpParam.setErrcode("000");
					httpParam.setParams("&mtmsgid="+String.valueOf(appMsg.getMsgId()));
				}
				else{
					httpParam.setErrcode("appwg:"+sendRes);
				}
			}
			//微信消息
			else if("2".equals(tomsgtype)){
				IWgWeiCommuteBiz wgWeiBiz = new WgWeiCommuteBiz();
				String sendWeiRes = wgWeiBiz.SendWeixMsg(appMsg);
				if(sendWeiRes != null && "000".equals(sendWeiRes)){
					httpParam.setErrcode("000");
					//httpParam.setParams("&mtmsgid="+String.valueOf(appMsg.getMsgId()));
				}
				else{
					httpParam.setErrcode("appwg:"+sendWeiRes);
				}
			}
			return httpParam;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "Http请求发送公众消息异常。");
			httpParam.setErrcode("appreq:-999");
			return httpParam;
		}
	}
	
	/**
	 * 发送公众媒体消息
	 * @param request
	 * @param response
	 */
	private AppHttpParam mt_e_mediamsg(HttpServletRequest request, HttpServletResponse response){
		
		AppHttpParam httpParam = new AppHttpParam();
		httpParam.setRespCommand("mt_e_mediaresp");
		try
		{
			//企业编码
			String ecode = request.getParameter("ecode");
			if(ecode == null || ecode.trim().length() == 0){
				httpParam.setErrcode("appreq:-2");
				return httpParam;
			}
			
			//发送者
			String sa = request.getParameter("sa");
			if(sa == null || sa.trim().length() == 0){
				httpParam.setErrcode("appreq:-3");
				return httpParam;
			}
			
			//消息类型，1：app消息；2：微信消息。，默认1:app消息
			String tomsgtype = request.getParameter("tomsgtype");
			if(tomsgtype == null || tomsgtype.trim().length() == 0){
				tomsgtype = "1";
			}
			else if(!"1".equals(tomsgtype) && !"2".equals(tomsgtype)){
				httpParam.setErrcode("appreq:-15");
				return httpParam;
			}
			
			//接收者
			String da = request.getParameter("da");
			if(da == null || da.trim().length() == 0){
				httpParam.setErrcode("appreq:-4");
				return httpParam;
			}
			String[] daArray = da.split(",");
			if(daArray.length > 100){
				httpParam.setErrcode("appreq:-5");
				return httpParam;
			}
			
			//发送内容
			String url = request.getParameter("url");
			if(url == null || url.trim().length() == 0){
				httpParam.setErrcode("appreq:-7");
				return httpParam;
			}
			
			//媒体类型。10：图片 11：语音 12：视频
			String medtype = request.getParameter("medtype");
			if(medtype == null || medtype.trim().length() == 0){
				httpParam.setErrcode("appreq:-8");
				return httpParam;
			}
			if(!"1".equals(medtype) && !"2".equals(medtype) && !"3".equals(medtype)){
				httpParam.setErrcode("appreq:-9");
				return httpParam;
			}
			
			AppMessage appMsg = new AppMessage();
			
			//获取文件下载到本地的路径
			String saveFilePath = null;
			//消息类型，1：app消息；2：微信消息。，默认app消息
			if("1".equals(tomsgtype)){
				//获取文件下载到本地的路径
				saveFilePath = getFilePath(url);
			}
			//微信消息
			else if("2".equals(tomsgtype)){
				String filePath = getWeixFilePath(url);
				appMsg.setLocalUrl(filePath);
				String webroot = tfu.getWebRoot();
				saveFilePath = webroot + filePath;
			}
			
			//下载远程文件到本地并上传到文件服务器
			String uploadRes = upToAppFileServer(url, saveFilePath, ecode, medtype);
			//处理上传失败
			if(uploadRes.length() < 4){
				httpParam.setErrcode("appreq:"+uploadRes);
				return httpParam;
			}
			
			
			appMsg.setUrl(uploadRes);
			appMsg.setMsgType(Integer.parseInt(medtype));
			
			//操作员id
			String userid = request.getParameter("userid");
			if(userid != null && userid.trim().length() > 0){
				appMsg.setUserId(Long.valueOf(userid));
			}
			
			//标题
			String title = request.getParameter("title");
			if(title != null && title.trim().length() > 0){
				appMsg.setTitle(title);
			}
			
			appMsg.setEcode(ecode);
			appMsg.setFromUserName(sa);
			
			//构建接收者set
			LinkedHashSet<String> toUserNameSet = new LinkedHashSet<String>();
			for(String toUser : daArray){
				toUserNameSet.add(toUser);
			}
			
			appMsg.setToUserNameSet(toUserNameSet);
			appMsg.setToType(2);
			
			//消息类型，1：app消息；2：微信消息。，默认app消息
			if("1".equals(tomsgtype)){
				IInterfaceBuss wgBiz = new HandleMsgBiz();
				int sendRes = wgBiz.SendAppPublicMsg(appMsg);
				if(sendRes == 1){
					httpParam.setErrcode("000");
					httpParam.setParams("&mtmsgid="+String.valueOf(appMsg.getMsgId()));
				}
				else{
					httpParam.setErrcode("appwg:"+sendRes);
				}
			}
			//微信消息
			else if("2".equals(tomsgtype)){
				IWgWeiCommuteBiz wgWeiBiz = new WgWeiCommuteBiz();
				String sendWeiRes = wgWeiBiz.SendWeixMsg(appMsg);
				if(sendWeiRes != null && "000".equals(sendWeiRes)){
					httpParam.setErrcode("000");
					//httpParam.setParams("&mtmsgid="+String.valueOf(appMsg.getMsgId()));
				}
				else{
					httpParam.setErrcode("appwg:"+sendWeiRes);
				}
			}
			
			return httpParam;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "Http请求发送公众消息异常。");
			httpParam.setErrcode("appreq:-999");
			return httpParam;
		}
	}
	
	/**
	 * 下载远程文件到本地并上传到文件服务器
	 * @param url 远程文件url
	 * @param corp_code 企业编码
	 * @param medtype 媒体类型。1：图片；2：视频；3：语音
	 * @return
	 * 		文件相对路径	:上传成功
	 * 			-9	:medtype值不正确
	 * 			-10	：处理解析url并获取本地存储路径失败
	 * 			-11	：下载远程文件失败
	 * 			-12	：上传到文件服务器失败
	 * 			-13   ：文件格式不支持
	 * 			-14	：文件不存在。
	 */
	private String upToAppFileServer(String url, String saveFilePath, String corp_code, String medtype){
		
		if(saveFilePath == null){
			EmpExecutionContext.error("上传到App文件服务器失败，获取文件本地路径为null。");
			return "-10";
		}
		else if(saveFilePath.length() < 4){
			EmpExecutionContext.error("上传到App文件服务器失败，获取文件本地路径失败，错误码："+saveFilePath);
			return saveFilePath;
		}
		
		//先下载到本地
		String downRes = downToLocal(url, saveFilePath);
		
		//下载失败
		if(!"1".equals(downRes)){
			EmpExecutionContext.error("上传到App文件服务器，下载远程文件失败，错误码："+downRes);
			return downRes;
		}
		
		//10：企业推送的图片 11：企业推送的语音 12：企业推送的视频 
		String type = null;
		if("1".equals(medtype)){
			//图片
			type = "10";
		}
		else if("2".equals(medtype)){
			//视频
			type = "12";
		}
		else if("3".equals(medtype)){
			//语音
			type = "11";
		}
		else{
			EmpExecutionContext.error("上传到App文件服务器，medtype值不正确，medtype值为"+medtype);
			return "-9";
		}
		
		IWgMwFileBiz wgFileBiz = new WgMwFileBiz();
		String fileSerUrl = wgFileBiz.uploadToMwFileSer(saveFilePath, type, corp_code);
		
		//上传成功
		if(fileSerUrl != null && fileSerUrl.trim().length() > 2){
			return fileSerUrl;
		}
		else{
			//上传失败
			EmpExecutionContext.error("上传到App文件服务器失败，返回错误码："+fileSerUrl);
			return "-12";
		}
	}
	
	/**
	 * 处理url并获取下载存储文件路径
	 * @param url 远程文件url
	 * @return 
	 * 		返回文件路径	：处理成功
	 * 			-13	：文件格式不支持
	 * 			-10	：处理url并获取下载存储文件路径失败
	 */
	private String getFilePath(String url){
		try
		{
			String webroot = tfu.getWebRoot();
			String fileDir = webroot + "file" + File.separator + "apphttpdown" + File.separator;
			
			//日历对象
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			fileDir += year + File.separator + month + File.separator + day + File.separator; 
			
			File physicsDir = new File(fileDir);
			if(!physicsDir.exists())
			{
				physicsDir.mkdirs();
			}
			
			//文件类型:.jpg
			String fileType = url.substring(url.lastIndexOf("."));
			//jpg、jpeg、png、bmp
			if(!".jpg".equals(fileType) && !".jpeg".equals(fileType) 
			&& !".png".equals(fileType) && !".bmp".equals(fileType)
			&& !".3gp".equals(fileType) && !".amr".equals(fileType))
			{
				EmpExecutionContext.error("处理url并获取下载存储文件路径失败，文件格式不支持。");
				return "-13";
			}
			
			//文件名
			String fileName = new Date().getTime() + "_" + GetSxCount.getInstance().getCount() + fileType;
			//文件路径
			String filePath = fileDir + fileName;
			return filePath;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理url并获取下载存储文件路径异常。");
			return "-10";
		}
	}
	
	/**
	 * 处理url并获取下载存储文件路径
	 * @param url 远程文件url
	 * @return 
	 * 	     返回文件相对路径	：处理成功
	 * 			-13	：文件格式不支持
	 * 			-10	：处理url并获取下载存储文件路径失败
	 */
	private String getWeixFilePath(String url){
		try
		{
			String fileDir = "file/weix/rimg/";
			
			//日历对象
			Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH) + 1;
			int day = c.get(Calendar.DAY_OF_MONTH);
			
			fileDir += year + "/" + month + "/" + day + "/"; 
			
			String webroot = tfu.getWebRoot();
			File physicsDir = new File(webroot+fileDir);
			if(!physicsDir.exists())
			{
				physicsDir.mkdirs();
			}
			
			//文件类型:.jpg
			String fileType = url.substring(url.lastIndexOf("."));
			//jpg、jpeg、png、bmp
			if(!".jpg".equals(fileType) && !".jpeg".equals(fileType) 
			&& !".png".equals(fileType) && !".bmp".equals(fileType)
			&& !".3gp".equals(fileType) && !".amr".equals(fileType))
			{
				EmpExecutionContext.error("处理url并获取下载存储文件路径失败，文件格式不支持。");
				return "-13";
			}
			
			//文件名
			String fileName = new Date().getTime() + "_" + GetSxCount.getInstance().getCount() + fileType;
			//文件路径
			String filePath = fileDir + fileName;
			return filePath;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理url并获取下载存储文件路径异常。");
			return "-10";
		}
	}
	
	/**
	 * 下载文件到本地
	 * @param url 远程文件url
	 * @param saveFilePath 保存路径
	 * @return 
	 * 			1	：下载成功。
	 * 			-11	：下载失败。
	 * 			-14	：文件不存在。
	 */
	private String downToLocal(String url, String saveFilePath)
	{
		
		BufferedInputStream bufferIs = null;
		FileOutputStream os = null;
		try
		{
			url = url.replace("%20", " ");
			saveFilePath = saveFilePath.replace("%20", " ");
			File file = new File(saveFilePath);
			
			URL urls = new URL(url);
			bufferIs = new BufferedInputStream(urls.openConnection().getInputStream());
			os = new FileOutputStream(file);
			
			byte buffer[] = new byte[4 * 1024];
			int size = 0;
			while((size = bufferIs.read(buffer)) != -1)
			{
				os.write(buffer, 0, size);
			}
			os.flush();
			return "1";
		}
		catch (FileNotFoundException e)
		{
			EmpExecutionContext.error(e, "下载远程文件失败，文件不存在。");
			return "-14";
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下载远程文件异常。");
			return "-11";
		}
		finally
		{
			try
			{
				if(os != null){
					os.close();
				}
				if(bufferIs != null){
					bufferIs.close();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "下载远程文件，关闭资源异常。");
			}
		}
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException
	{
	
	this.doGet(request, response);
	}
	
}
