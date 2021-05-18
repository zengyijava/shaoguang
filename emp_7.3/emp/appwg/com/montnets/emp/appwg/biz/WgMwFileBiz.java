package com.montnets.emp.appwg.biz;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.appwg.util.HttpUtil;
import com.montnets.emp.appwg.util.Md5Util;
import com.montnets.emp.appwg.wginterface.IWgMwFileBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.appmage.LfAppAccount;

public class WgMwFileBiz extends SuperBiz implements IWgMwFileBiz
{
	/**
	 * 文件服务器url
	 */
	private static String fileSvrUrl;

	private static String FileSvrDownUrl;
	
	//文件服务器下载servlet
	private static final String fileSvrDownSvt = "DownloadServlet";
	
	/**
	 * 上传文件到app平台文件服务器
	 * @param filePath 上传文件路径
	 * @param type 上传文件类型  
						10：企业推送的图片。
						11：企业推送的语音。
						12：企业推送的视频。
						13：企业推送普通文件。
						20：企业的app安装包、sdk包。
						30：个人消息的图片。
						31：个人消息的语音。
						32：个人消息的视频。
						33：个人消息的普通文件。
						40：个人头像。
	 * @param corp_code 企业编码
	 * @return 
	 					-1	文件不存在。
	 					-2	文件服务器地址未配置。
	 					-3	上传文件路径为空。
	 					-4	上传文件类型为空。
	 					-5	企业编码为空。
	 			String字符串	长度大于1的相对文件路径 （data/file/2013/2013-09-17.........）。
						0	FileUploadException解析出错。
						1	传入的参数type、md5、size、corp_code、file_suffix 中有null值。
						2	索引中存在，但文件系统中不存在 。
						3	Request传入MD5	值校验不一致！请检查MD5值并重新提交POST上传请求。
						4	文件大小超过最大100M限制。
						其他	HTTP status code。
						异常	null。
	 */
	public String uploadToMwFileSer(String filePath, String type, String corp_code) 
	{
		try
		{
			EmpExecutionContext.info("上传文件到App平台文件服务器，filePath="+filePath+";type="+type+";corp_code="+corp_code);
			
			if(filePath == null || filePath.trim().length() == 0){
				EmpExecutionContext.error("上传文件到App平台文件服务器失败，上传文件路径为空。");
				return "-3";
			}
			if(type == null || type.trim().length() == 0){
				EmpExecutionContext.error("上传文件到App平台文件服务器失败，上传文件类型为空。");
				return "-4";
			}
			if(corp_code == null || corp_code.trim().length() == 0){
				EmpExecutionContext.error("上传文件到App平台文件服务器失败，企业编码为空。");
				return "-5";
			}
			File file = new File(filePath);
			if(!file.exists() || !file.isFile())
			{
				EmpExecutionContext.error("上传文件到App平台文件服务器失败，文件不存在。");
				return "-1";
			}

			//头信息参数
			Map<String, String> headers = new HashMap<String,String>();
			headers.put("Authorization", "Basic cm9vdDpyb290MTIzNDU2cm9vdA==");
			
			//url参数
			Map<String, String> params = new HashMap<String,String>();
			params.put("type", type);
			
			String md5Str = Md5Util.getMD5(file);
			params.put("md5", md5Str);
			
			String size = String.valueOf(file.length());
			params.put("size", size);
			params.put("corp_code", corp_code);
			
			String file_suffix = filePath.substring(filePath.lastIndexOf("."));
			params.put("file_suffix", file_suffix);
			
			//获取app文件服务器地址
			String fileSerurl = getFileSvrUrl();
			if(fileSerurl == null || fileSerurl.length() == 0){
				
				EmpExecutionContext.error("上传文件失败，文件服务器地址为空。");
				return "-2";
			}
			
			fileSerurl += "UploadServlet";
			
			String res = HttpUtil.upload(fileSerurl, file, params, headers);
			
			EmpExecutionContext.info("上传文件到App平台文件服务器结果："+res+"，filePath="+filePath+";type="+type+";corp_code="+corp_code);
			
			return res;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上传文件到App平台文件服务器异常。");
			return null;
		}
	}
	
	/**
	 * 从app平台文件服务器下载文件
	 * @param fileSavePath 文件保存路径
	 * @param fileUrl app平台的文件路径
	 * @return 成功返回true
	 */
	public boolean downloadFromMwFileSer(String fileSavePath, String fileUrl){
		
		EmpExecutionContext.info("从app平台文件服务器下载文件，fileSavePath="+fileSavePath+";fileUrl="+fileUrl);
		
		if(fileSavePath == null || fileSavePath.trim().length() == 0){
			EmpExecutionContext.error("从app平台文件服务器下载文件失败，文件保存路径为空。");
			return false;
		}
		if(fileUrl == null || fileUrl.trim().length() == 0){
			EmpExecutionContext.error("从app平台文件服务器下载文件失败，app平台的文件路径为空。");
			return false;
		}
		
		//头信息参数
		Map<String, String> headers = new HashMap<String,String>();
		headers.put("Authorization", "Basic cm9vdDpyb290MTIzNDU2cm9vdA==");
		
		//获取app文件服务器地址
		String fileSerurl = getFileSvrUrl();
		
		if(fileSerurl == null || fileSerurl.length() == 0){
			
			EmpExecutionContext.error("从app平台文件服务器下载文件失败，文件服务器地址为空。");
			return false;
		}
		
		//检查是否拼好完整url
		if(fileUrl.indexOf(fileSvrDownSvt) == -1){
			//未拼接url，则这里拼接上
			fileUrl = fileSerurl + fileSvrDownSvt+"/" + fileUrl;
		}
		
		boolean res = HttpUtil.downloadFile(fileSavePath, fileUrl, headers);
		
		EmpExecutionContext.info("从app平台文件服务器下载文件结果："+res+"，fileSavePath="+fileSavePath+";fileUrl="+fileUrl);
		
		return res;
	}
	
	/**
	 * 获取文件服务器下载url
	 * @return 返回文件服务器下载url
	 */
	public String getDownLoadSerUrl(){
		
		if(FileSvrDownUrl != null && FileSvrDownUrl.length() > 0){
			return FileSvrDownUrl;
		}
		
		String fileUrl = getFileSvrUrl();
		FileSvrDownUrl = fileUrl + fileSvrDownSvt + "/";
		return FileSvrDownUrl;
	}
	
	/**
	 * 获取文件服务器地址
	 * @return 返回文件服务器地址
	 */
	public String getFileSvrUrl(){
		try
		{
			if(WgMwFileBiz.fileSvrUrl != null && WgMwFileBiz.fileSvrUrl.length() > 0){
				return WgMwFileBiz.fileSvrUrl;
			}
			
			List<LfAppAccount> appAccoutsList =  empDao.findListByCondition(LfAppAccount.class, null, null);
			if(appAccoutsList == null || appAccoutsList.size() == 0){
				EmpExecutionContext.error("未配置企业账户，登录失败。");
				return null;
			}
			
			String fileUrl = appAccoutsList.get(0).getFileSvrUrl();
			//判断文件服务器路径最后有没有/，没就加上
			if(fileUrl.lastIndexOf("/")+1 != fileUrl.length()){
				fileUrl += "/";
			}
			
			//设置文件服务器url
			WgMwFileBiz.fileSvrUrl = fileUrl;
			return fileUrl;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取文件服务器地址异常。");
			return null;
		}
		
	}
	
	/**
	 * 设置文件服务器地址
	 * @param url 新的文件服务器地址
	 * @return 成功返回true
	 */
	public static boolean setFileSvrUrl(String url){
		try
		{
			//先设置为空，如果失败，获取的时候还能根据空来重新获取
			WgMwFileBiz.fileSvrUrl = null;
			
			if(url == null || url.trim().length() == 0){
				EmpExecutionContext.error("设置文件服务器地址失败，文件服务器地址为空。");
				return false;
			}
			//判断文件服务器路径最后有没有/，没就加上
			if(url.lastIndexOf("/")+1 != url.length()){
				url += "/";
			}
			WgMwFileBiz.fileSvrUrl = url;
			
			//清掉，下次获取时会重新获取新值
			WgMwFileBiz.FileSvrDownUrl = null;
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置文件服务器地址异常。");
			return false;
		}
	}
	
	/**
	 * 获取文件服务器接口名
	 * @return 返回文件服务器接口名
	 */
	public String getFileSvrDownSvt(){
		return fileSvrDownSvt;
	}
}
