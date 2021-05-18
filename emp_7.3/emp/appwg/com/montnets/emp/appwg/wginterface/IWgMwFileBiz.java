package com.montnets.emp.appwg.wginterface;

public interface IWgMwFileBiz
{
	/**
	 * 上传文件到app平台文件服务器
	 * @param filePath 上传文件路径
	 * @param type 上传文件类型  
						10：企业推送的图片
						11：企业推送的语音
						12：企业推送的视频
						13：企业推送普通文件
						20：企业的app安装包、sdk包
						30：个人消息的图片
						31：个人消息的语音
						32：个人消息的视频
						33：个人消息的普通文件
						40：个人头像
	 * @param corp_code 企业编码
	 * @return 返回
	 			String字符串	长度大于1的相对文件路径 （data/file/2013/2013-09-17.........）。
						0	FileUploadException解析出错。
						1	传入的参数typeStr 、md5Request、 sizeStr 、 中有null值。
						2	索引中存在，但文件系统中不存在 。
						3	Request传入MD5	值校验不一致！请检查MD5值并重新提交POST上传请求。
						4	文件大小超过最大100M限制。
	 */
	public String uploadToMwFileSer(String filePath, String type, String corp_code);
	
	/**
	 * 从app平台文件服务器下载文件
	 * @param fileSavePath 文件保存路径
	 * @param fileUrl 文件地址
	 * @return 成功返回true
	 */
	public boolean downloadFromMwFileSer(String fileSavePath, String fileUrl);
	
	/**
	 * 获取文件服务器接口名
	 * @return 返回文件服务器接口名
	 */
	public String getFileSvrDownSvt();
	
}
