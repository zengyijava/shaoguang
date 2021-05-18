package com.montnets.emp.rms.templmanage.listener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.meditor.tools.ZipDownUtil;
import com.montnets.emp.rms.rmsapi.util.OOSUtil;
import com.montnets.emp.rms.templmanage.dao.MbglTemplateDAO;
import com.montnets.emp.rms.tools.AnalysisRMS;
import com.montnets.emp.rms.tools.RmsEntity;
import com.montnets.emp.rms.tools.ZipUtil;
import com.montnets.emp.rms.vo.LfTemplateVo;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.TxtFileUtil;

public class TemplateDownLoadThread extends Thread {
	private boolean isExit = false;
	private MbglTemplateDAO ltDao = new MbglTemplateDAO();
	private TxtFileUtil txtfileutil = new TxtFileUtil();
	private OOSUtil oosUtil = new OOSUtil();
	private CommonBiz commBiz = new CommonBiz();
	ZipDownUtil zipDownUtil = new ZipDownUtil();
	//定时间隔
	private int internal = 5 * 60*1000 ; //默认5分钟
	
	public TemplateDownLoadThread(int internal) {
		this.internal = internal;
		setName("模板下载线程");
	}

	public void run() {
		while (!isExit) {
			try {
				List<LfTemplateVo> templateList = ltDao.getTemplateList();
				if(null != templateList && templateList.size() > 0 ){
					for(LfTemplateVo lf : templateList){

						//获取相对路径
						String relativePath = lf.getTmMsg();

						// V2.0 V文件路径
						if("V2.0".equals(lf.getVer())){
							String [] pathArray = relativePath.split("/");
							String fileName= pathArray[pathArray.length-2];
							relativePath = relativePath.substring(0,relativePath.lastIndexOf("/"))+fileName+".zip";
						}
						boolean result = false;
						//下载文件单独抓取异常,出现异常继续后续模板下载
						try {
							if(relativePath.contains("/rms/")){
								result  = true;
							}else{//V3.0的才进行下载
								result = zipDownUtil.tempalteDown(relativePath,lf.getTmState());
							}
						}catch (Exception e){
							EmpExecutionContext.error(e,"模板资源文件下载失败:"+lf.getTmid());
						}
						if (!result){
							EmpExecutionContext.error("文件下载失败:id="+lf.getTmid()+";tmMsg=:"+lf.getTmMsg());
						}

							/*String firstFramePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "firstframe.jsp");
							File fFrame  = new File(firstFramePath);
							String tmid = lf.getTmid().toString();
							String filePath = dirUrl + lf.getTmMsg().replace("fuxin.rms", "");
							if(!fFrame.exists()){//不存在则从阿里云下载
								//V1.0 的rms 文件
								if(null != lf.getVer() && lf.getVer().equals("V1.0")){
									downLoadRmsFromOssV1(filePath);
								}
								//V2.0 的rms 文件
								if(null != lf.getVer() && lf.getVer().equals("V2.0")){
									downLoadRmsFromOss(null,filePath, tmid,"");
								}
								
							}
							//审核通过的生成 预览文件
							if(lf.getAuditstatus()== 1 ){//Auditstatus == 1 为网关审核通过
								createPreviewFile(lf);
							}*/
						}
					}
				
			} catch (Exception e) {
				EmpExecutionContext.error(e,"定时查询模板表出现异常");
			}
			
			// 每次间隔多长时间更新状态，时间可配
			try {
				Thread.sleep(internal);
			} catch (InterruptedException e) {
				EmpExecutionContext.error(e,"模板下载暂停出行异常");
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * 生成预览文件
	 */
	
	private void createPreviewFile(LfTemplateVo lf) {
		if(null != lf.getVer()&& "V1.0".equals(lf.getVer())){
			getTmMsgV1(lf);
		}else{
			getTmMsg(lf);
		}
	}
	/**
	 * V2.0下载模板文件
	 * @param request
	 * @param filePath
	 * @param tmid
	 * @param preview
	 * @return
	 */
	public boolean downLoadRmsFromOss(HttpServletRequest request, String filePath, String tmid, String preview) {
		boolean flag = false;
		String dirUrl = txtfileutil.getWebRoot();
		//拼接文件服务器下载zip文件相对地址
		String downFileZipUrl =filePath.replace(dirUrl, "");
		downFileZipUrl = downFileZipUrl.substring(0, downFileZipUrl.length()-1)+".zip";
		String sourcPath = filePath.replace(dirUrl, "");
		String rmsZipName =tmid+".zip";
		//判断文件夹是否存在，存在直接返回，不存在从阿里云下载
		File fDir = new File(filePath);
		if(!fDir.exists()){
			fDir.mkdirs();
		}else{
			File htmlFile = new File(filePath+"fuxin.html");
			File rmsFile = new File(filePath+"fuxin.rms");
			File previewFile = new File(filePath+"fuxinPreview.html");
			if("needPreview".equals(preview)){
				if(htmlFile.exists() && rmsFile.exists()&&previewFile.exists()){
					return true;
				}
			}else{
				if(htmlFile.exists() && rmsFile.exists()){
					return true;
				}
			}
		}
		/**
		 * //解析生成相关资源文件
	    	RmsEntity rEntity = analysisRMS.Parse(filePath+rmsName, filePath+"src/", rmsFileSize);
	    	//生成fuxin.html-用于预览
	    	String srcPath = (filePath+"src/").replace(dirUrl, "");
	    	analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);
		 */
		String destPath = (filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "");
		
		
		//从文件服务器下载 zip
		String downFileZip = commBiz.downFileFromFileCenWhitZip(downFileZipUrl);
		
		if(!"success".equals(downFileZip)){//文件服务器没有下载成功,则从阿里云下载 zip
			if(oosUtil.downLoadFile(sourcPath,destPath, rmsZipName)){
				//之前是只上传fuxin.rms 文件到阿里云，需要下载下来后再反解析rms 文件为资源文件，现在直接将源文件和fuxin.rms 文件打包上传，直接下载解压即可
				ZipUtil.unZip((filePath+"fuxin.rms").replaceAll(tmid+"/fuxin.rms", "")+rmsZipName);
			}else{
				return false;
			}
		} 
		
		//反解析rms
		//解析rms文件
		AnalysisRMS aRms = new AnalysisRMS();
		//获取fuxin.rms 文件大小
		File rmsFile = new File(filePath+"fuxin.rms");
		int fileByteSize =(int)rmsFile.length(); 
		//生成fuxin.html-用于预览
		String srcPath = (filePath+"src/").replace(dirUrl, "");
		RmsEntity rsRmsEntity=aRms.Parse(request,filePath+"fuxin.rms", filePath+"src/", fileByteSize,null);
		//aRms.createHtml(filePath+"src/", srcPath, rsRmsEntity);
		flag = true;
		
	     return flag;
	}

	/**
	 * V1.0从阿里云下载-解析rms文件
	 * @param filePath
	 * @return
	 */
	public boolean downLoadRmsFromOssV1(String filePath) {
		boolean flag = false;
		String dirUrl = txtfileutil.getWebRoot();
		String sourcPath =filePath.replace(dirUrl, "");
		String rmsZipName ="fuxin.zip";
		//判断文件夹是否存在，存在直接返回，不存在从阿里云下载
		File fDir = new File(filePath);
		if(!fDir.exists()){
			fDir.mkdirs();
		}else{
			File htmlFile = new File(filePath+"fuxin.html");
			File rmsFile = new File(filePath+"fuxin.rms");
			if(htmlFile.exists() && rmsFile.exists()){
				return true;
			}
		}
		if(oosUtil.downLoadFile(sourcPath, filePath, rmsZipName)){
			ZipUtil.unZip(filePath+rmsZipName);
			//解析rms文件
			AnalysisRMS aRms = new AnalysisRMS();
			//获取fuxin.rms 文件大小
			File rmsFile = new File(filePath+"fuxin.rms");
			int fileByteSize =(int)rmsFile.length(); 
			//生成fuxin.html-用于预览
	    	String srcPath = (filePath+"src/").replace(dirUrl, "");
			RmsEntity rsRmsEntity=aRms.Parse(filePath+"fuxin.rms", filePath+"src/", fileByteSize);
			aRms.createHtml(filePath+"src/", srcPath, rsRmsEntity);
			flag = true;
			
		}
	     return flag;
	}
	
	/**
	 * V2.0预览文件
	 * @param lf
	 */
	private void getTmMsg(LfTemplateVo lf) {
		txtfileutil = new TxtFileUtil();
		String dirUrl = txtfileutil.getWebRoot();
		String htmlUrl = lf.getTmMsg();
		String filePath = dirUrl+ htmlUrl.replace("fuxin.rms", "fuxinPreview.html");
		String tmId =lf.getTmid().toString();
		try {
			// 从阿里云下载
			boolean flag = downLoadRmsFromOss(null,filePath.replace("fuxinPreview.html", ""),tmId,"");
			// 根据smil文件生成 预览html
			String smilFileUrl = "";
			String filePath1 = filePath.split("fuxinPreview.html")[0];
			String srcPath = (filePath1 + "src/").replace(dirUrl, "");
			smilFileUrl = filePath1 + "src/00.smil";
			// analysisRMS.createHtml(filePath+"src/", srcPath, rEntity);

			new AnalysisRMS().createPreviewHtml(filePath1 + "src/",
					srcPath, "", smilFileUrl,false);

			// 读取生成的用于预览的html:fuxinPreview.html
			File file = new File(filePath);
			StringBuffer sbuf = new StringBuffer();
			if (file.isFile() && file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				try{
					String lineTxt = null;
					while ((lineTxt = br.readLine()) != null) {
						sbuf.append(lineTxt);
					}
				}finally{
					if(null != fis){
						fis.close();
					}
					IOUtils.closeReaders(this.getClass(), br,isr);
				}
//				br.close();
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"下载线程生成预览文件出现异常");
		} 

	}
	
	/**
	 * V1.0预览文件
	 * @param lf
	 */
	public void getTmMsgV1(LfTemplateVo lf){
		txtfileutil = new TxtFileUtil();
		String dirUrl = txtfileutil.getWebRoot();
		String htmlUrl = lf.getTmMsg();
		String filePath = dirUrl + htmlUrl.replace("fuxin.rms", "fuxinPreview.html");
		//根据smil文件生成 预览html
		String smilFileUrl ="";
		String  filePath1 = filePath.split("fuxinPreview.html")[0];
		String srcPath = (filePath1+"src/").replace(dirUrl, "");
		smilFileUrl = filePath1+"src/00.smil";
		try {
			new AnalysisRMS().createPreviewHtml(filePath1+"src/", srcPath,"", smilFileUrl);
			
			//读取生成的用于预览的html:fuxinPreview.html
			File file = new File(filePath);
			StringBuffer sbuf = new StringBuffer();
			if(file.isFile() && file.exists()) {
				FileInputStream fis = new FileInputStream(file);
				InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
				BufferedReader br = new BufferedReader(isr);
				try{
					String lineTxt = null;
					while ((lineTxt = br.readLine()) != null) {
						sbuf.append(lineTxt);
					}
				}finally{
				  	if(null != fis){
						fis.close();
					}
					IOUtils.closeReaders(this.getClass(), br,isr);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"下载模板线程生成V1.0 预览文件出现异常");
		}
	}
}
