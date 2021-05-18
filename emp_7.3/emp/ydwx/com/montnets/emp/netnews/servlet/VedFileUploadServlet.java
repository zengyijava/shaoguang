/**
 * Program  : VedFileUploadServlet.java
 * Author   : chensj
 * Create   : 2013-6-20 下午01:51:08
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.netnews.servlet;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.netnews.common.AllUtil;
import com.montnets.emp.netnews.dao.WebexUploadDao;
import com.montnets.emp.netnews.upload.MonitoredDiskFileItemFactory;
import com.montnets.emp.netnews.upload.UploadListener;
import com.montnets.emp.util.TxtFileUtil;


/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-20 下午01:51:08
 */
public class VedFileUploadServlet extends HttpServlet {

	private final WebexUploadDao uploadDao = new WebexUploadDao();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try{
			String msg = upload(request, response);
			request.getSession(false).setAttribute("msgshow",msg);
			response.sendRedirect(request.getContextPath()+"/wx_attached.htm?method=find");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"文件上传出现异常！");
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}

	/***
	 *  文件上传
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private  String upload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
//		SYSUSER sysUser = (SYSUSER)request.getSession(false).getAttribute("sysUser");
//		int userId = sysUser.getUID();
//		int userType=sysUser.getUSER_TYPE();
		String userId="2";int userType=-1;

		// 创建了一个监听器，用来监听文件上传的状态
		// 进度条是通过这个监听器来动态改变的
		UploadListener listener = new UploadListener(request, 30);

		String fType = AllUtil.toStringValue(request.getParameter("fType"), "0");
		String msg = "文件上传异常，请重新上传！";
		
		response.setContentType("text/html");

		// 设置字符编码为UTF-8, 统一编码，处理出现乱码问题
		response.setCharacterEncoding("UTF-8");
		// 实例化一个硬盘文件工厂,用来配置上传组件ServletFileUpload
		FileItemFactory dfif = new MonitoredDiskFileItemFactory(listener);
		// 用以上工厂实例化上传组件
		ServletFileUpload sfu = new ServletFileUpload(dfif);
		try {
 			List<FileItem > uploadlist = sfu.parseRequest(request);
			// 开始读取上传信息
			Iterator<FileItem> iter = uploadlist.iterator();  
			//fileList = sfu.parseRequest(request);
			// 得到所有上传的文件
			String vedName="";
		    String veddec="";
		    String VedNodeid="";
		    String Vedfication="";
		    String  type ="";
		    int i = 0;
		    
		    // 根据系统时间生成上传后保存的文件名
			String now = AllUtil.getDateStream("yyyyMMddHHmmss");
			HttpSession session= request.getSession(false);
			LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
			String lgcorpcode=corp.getCorpCode();
			// 循环处理所有文件 
			while (iter.hasNext()) {
				   FileItem item = (FileItem) iter.next();
				   String path = null;
				   long size = 0;  
				   if(item.isFormField()){   
					    if("vedName".equals(item.getFieldName()))
					    	vedName = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("veddec".equals(item.getFieldName()))
					    	veddec = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("VedioType1".equals(item.getFieldName()))
					    	type = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("VedNodeid".equals(item.getFieldName()))
					    	VedNodeid = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("Vedfication".equals(item.getFieldName()))
					    	Vedfication = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
		    	  }else{
		    		  size = item.getSize();
					// 得到文件的完整路径
					path = item.getName();
					
					if(!"".equals(path)){
						// 得到去除路径的文件名
						String t_name = path.substring(path.lastIndexOf("\\") + 1);
						
						//附件名
						String nameTemp = t_name.substring(0,t_name.indexOf("."));
						if(nameTemp.length()>50)
						{
							msg="上传的文件的名称过长，请控制在50个字符以内";
							return msg;
						}
						// 得到文件的扩展名(无扩展名时将得到全名)
						String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
				
						
						//保存文件路径的文件名称
						String filename= userId +"_"+ String.valueOf(now)+ StaticValue.getServerNumber() +"."+ t_ext;
						
						try {
								// *********2013-8-19 修改了路径
								String uploadpath =StaticValue.WX_MATER;
								TxtFileUtil fileUtil=new TxtFileUtil();
								String basePath13 = fileUtil.getWebRoot()+uploadpath;
								CommonBiz com=new CommonBiz();
								String basePath12=com.createDir(basePath13);
								String savePath = uploadpath+"/"+basePath12;
								basePath12=fileUtil.getWebRoot()+ uploadpath+"/"+basePath12;
								// **********************
								File f = new File(basePath12);
								if(!f.exists()){
									f.mkdirs();
								}
								item.write(new File(basePath12 + filename));
								boolean isFTPUpload=false;
								// **********************上传到FTP远程服务器上**********
								if(StaticValue.getISCLUSTER() ==1){
									isFTPUpload=com.upFileToFileServer(savePath + filename);
									if(!isFTPUpload){
										msg="上传文件失败！";
										return msg;
									}
								}
								if(i==0)
								{
									i=1;
									boolean bb = uploadDao.UploadFile(vedName,veddec, "", savePath + filename, userId,
											VedNodeid,nameTemp,0,userType,type,size,lgcorpcode);
									if(bb){
										//增加操作日志
										Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
										if(loginSysuserObj!=null){
											LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
											EmpExecutionContext.info("网讯素材", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "视频文件上传成功。大小: " + size + "字节", "OTHER");
										}
										msg = "添加素材成功！";
									}else{
										//增加操作日志
										Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
										if(loginSysuserObj!=null){
											LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
											EmpExecutionContext.info("网讯素材", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "视频文件上传失败。大小: " + size + "字节", "OTHER");
										}
										msg = "添加素材失败！";
									}
								}


							} catch (Exception e) {
								EmpExecutionContext.error(e,"文件夹创建出错！");
						}	
					}
							
		    	  }
			}
		} catch (FileUploadException e) {
			EmpExecutionContext.error(e,"文件上传出错！");
		}
		return msg;
	}

	// 取文件名后缀
	private static String getFiletype(String fileName) {

		String type = "";
		if (fileName == null || fileName.equals(""))
			return type;
		int position = fileName.lastIndexOf(".");
		if (position != -1) {
			type = fileName.substring(position + 1, fileName.length());
		}
		return type;
	}
}

