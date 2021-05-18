/**
 * Program  : FileUploadServlet.java
 * Author   : chensj
 * Create   : 2013-6-18 上午09:07:58
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
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.dao.WebexUploadDao;
import com.montnets.emp.netnews.upload.MonitoredDiskFileItemFactory;
import com.montnets.emp.netnews.upload.UploadListener;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @author   chensj <510061684@qq.com>
 * @version  1.0.0
 * @2013-6-18 上午09:07:58
 */
public class FileUploadServlet extends HttpServlet{


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

		String userId="2";int userType=-1;
		// 创建了一个监听器，用来监听文件上传的状态
		// 进度条是通过这个监听器来动态改变的
		UploadListener listener = new UploadListener(request, 30);
		String msg = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_36",request);
		
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
			String AttachName="";
		    String Describe="";
			String type="";
			String Sortid="";
			String scfication="";
			HttpSession session= request.getSession(false);
			LfCorp corp=(LfCorp)session.getAttribute("loginCorp");
			String lgcorpcode=corp.getCorpCode();
			// 循环处理所有文件 
			while (iter.hasNext()) {
				   FileItem item = (FileItem) iter.next();
				   String path = null;
				   long size = 0;  
				   if(item.isFormField()){   
					    if("AttachmentName".equals(item.getFieldName()))
					    	AttachName = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("AttachmentDescribe".equals(item.getFieldName()))
					    	Describe = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("AttachType1".equals(item.getFieldName()))
					    	type = new String(item.getString().getBytes("iso8859-1"), "UTF-8");   
					    if("nodeid".equals(item.getFieldName()))
					    	Sortid = new String(item.getString().getBytes("iso8859-1"), "UTF-8");
					    if("scfication".equals(item.getFieldName()))
					    	scfication = new String(item.getString().getBytes("iso8859-1"), "UTF-8");

		    	  }else{
		    		  size = item.getSize();
					// 忽略简单form字段而不是上传域的文件域(<input type="text" />等)
					if (item == null || item.isFormField()) {
						continue;
					}
					// 得到文件的完整路径
					path = item.getName();

					// 得到去除路径的文件名
					String t_name = path.substring(path.lastIndexOf("\\") + 1);
					
					//附件名
					String nameTemp = t_name.substring(0,t_name.indexOf("."));
					
					// 得到文件的扩展名(无扩展名时将得到全名)
					String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
					if(nameTemp.length()>50)
					{
						msg=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_37",request);
						return msg;
					}
					// 根据系统时间生成上传后保存的文件名
					long now = System.currentTimeMillis();
					
					//保存文件路径的文件名称  StaticValue.SERVER_NUMBER 为服务编号，为了避免分布式集群并发
					String filename= userId +"_"+ String.valueOf(now) + StaticValue.getServerNumber() +"."+ t_ext;
			
					try {
			
						//获取配置文件的文件保存路径
						// *********2013-8-19 修改了路径
						TxtFileUtil fileUtil=new TxtFileUtil();
						String uploadpath =StaticValue.WX_MATER;
						String basePath13 = fileUtil.getWebRoot()+uploadpath;
						CommonBiz com=new CommonBiz();
						String basePath12=com.createDir(basePath13);
						basePath12=uploadpath+"/"+basePath12;
						String basePath1=fileUtil.getWebRoot()+basePath12;
						
						

						// **********************处理本地数据**********
						File f = new File(basePath1);
						if(!f.exists()){
							f.mkdirs();
						}
//							System.out.println(basePath12 + filename);
							item.write(new File(basePath1 + filename));
							boolean isFTPUpload=false;
							// **********************上传到FTP远程服务器上**********
							if(StaticValue.getISCLUSTER() ==1){
								isFTPUpload=com.upFileToFileServer(basePath12 + filename);
								if(!isFTPUpload){
									msg=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_38",request);
									return msg;
								}
							}
							boolean bb = uploadDao.UploadFile(AttachName,Describe,"",basePath12 + filename,userId, Sortid,nameTemp,0,userType,type,size,lgcorpcode);	
							if(bb){
								//增加操作日志
								Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
								if(loginSysuserObj!=null){
									LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
									EmpExecutionContext.info("网讯素材", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "文件上传成功。大小: " + size + "字节", "OTHER");
								}
								msg = MessageUtils.extractMessage("ydwx","ydwx_jsp_out_39",request) + size + MessageUtils.extractMessage("ydwx","ydwx_jsp_out_40",request);
								}else{
									//增加操作日志
									Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
									if(loginSysuserObj!=null){
										LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
										EmpExecutionContext.info("网讯素材", loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), "文件上传失败。大小: " + size + "字节", "OTHER");
									}
									msg=MessageUtils.extractMessage("ydwx","ydwx_jsp_out_41",request);
								}
						
						
					} catch (Exception e) {
						EmpExecutionContext.error(e,"文件夹创建出错！");
					}
				}
			}
		} catch (FileUploadException e) {// 处理文件尺寸过大异常
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

