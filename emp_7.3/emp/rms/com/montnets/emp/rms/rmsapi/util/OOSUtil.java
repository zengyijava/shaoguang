package com.montnets.emp.rms.rmsapi.util;

import java.io.File;
import java.util.ResourceBundle;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.StringUtils;

/** 
* @ClassName: OOSUtil 
* @Description: 阿里云上传工具类 
* @author xuty  
* @date 2018-1-16 下午4:32:04 
*  
*/
public class OOSUtil {
	private static String endpoint = "";
	private static String accessKeyId = "";
	private static String accessKeySecret = "";
	private static String bucketName = "";
	static ResourceBundle RB = ResourceBundle.getBundle("SystemGlobals");
	protected OSSClient ossClient  = null;
	static {
		endpoint = RB.getString("endpoint");
		accessKeyId = RB.getString("accessKeyId");
		accessKeySecret = RB.getString("accessKeySecret");
		bucketName = RB.getString("bucketName");
		
	}
	public  OOSUtil() {
		if(!StringUtils.IsNullOrEmpty(endpoint)
				&& !StringUtils.IsNullOrEmpty(accessKeyId)
				&& !StringUtils.IsNullOrEmpty(accessKeySecret)
				&& !StringUtils.IsNullOrEmpty(bucketName)){
			ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
			/*if(null == ossClient){
				EmpExecutionContext.error("阿里云连接出现异常，endpoint："+endpoint+",accessKeyId:"+accessKeyId+",accessKeySecret:"+accessKeySecret);
			}*/
		}
	}
	
	
	
	
	public OSSClient getOssClient() {
		return ossClient;
	}




	public void setOssClient(OSSClient ossClient) {
		this.ossClient = ossClient;
	}




	/**
	 * 上传文件
	 * @param filePath
	 */
	public boolean uploadFile(String filePath,File file){
	    PutObjectResult reuslt = ossClient.putObject(bucketName,filePath+file.getName(),file,null);
	    //TODO 
	    return reuslt !=null;
	}
	

	/**
	 * 删除文件
	 * @param filePath
	 */
	public  void deleteFile(String filePath){
		File file = new File(filePath);
		ossClient.deleteObject(bucketName, file.getName());
	}
	/**
	 * 下载
	 * @param sourcPath
	 * @param destPath
	 * @param rmsZipName
	 */
	public boolean downLoadFile(String sourcPath,String destPath,String rmsZipName){
		boolean flag;
		try {
			flag = false;
			destPath = destPath+rmsZipName;
			if(null == ossClient ){
				return flag;
			}
			ObjectMetadata object = ossClient.getObject(new GetObjectRequest(bucketName, sourcPath+rmsZipName), new File(destPath));
			if(null != object){
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			EmpExecutionContext.error(e,"阿里云下载出现异常");
		}
		// 关闭client
		//ossClient.shutdown();
		return flag;
	}
	public static void main(String[] args) {
		String sourcPath = "file/rms/templates/577/";
		String destPath = "E:/aliyun/";
		String rmsZipName = "fuxin.zip";
		new OOSUtil().downLoadFile(sourcPath,destPath,rmsZipName);
	}
}
