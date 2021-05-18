package com.montnets.emp.appmage.svt;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.app.LfAppClidowload;
import com.montnets.emp.entity.appmage.LfAppAccount;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 
 * @author   Administrator <510061684@qq.com>
 * @version  1.0.0
 * @2013-11-20 下午03:45:22
 */
public class app_clidownloadSvt  extends BaseServlet
{
	private static final String empRoot = "appmage";
	private static final String basePath = "/contact";
	
	//操作模块
	private static final String opModule="APP客户端下载";
	private static final BaseBiz baseBiz=new BaseBiz();
	
	/**
	 * 进入app客户端下载信息页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{	
		String corpCode = request.getParameter("lgcorpcode");
		try
		{
			//查询app客户端下载数据
			LinkedHashMap<String, String> condMap=new LinkedHashMap<String, String>();
			condMap.put("corpcode", corpCode);
			List<LfAppClidowload> acls=baseBiz.getByCondition(LfAppClidowload.class, condMap, null);
			LfAppClidowload acl=null;
			LfAppAccount appac=null;
			//判断是否有数据有数据则赋值
			if(acls.size()>0){
				acl=acls.get(0);
			}
			//判断对象是否存在存在则查询企业app帐号信息
			if(acl!=null){
				if(acl.getAid()!=null){
					appac=baseBiz.getById(LfAppAccount.class, acl.getAid());
				}
				TxtFileUtil fileu=new TxtFileUtil();
				if(acl.getAdfileurl()!=null&&!"".equals(acl.getAdfileurl().trim())){
					String filerealpath=fileu.getWebRoot()+acl.getAdfileurl();
					File file=new File(filerealpath);
					if(!file.exists()){
						acl.setAdfileurl("");
					}
				}
				if(acl.getIosfileurl()!=null&&!"".equals(acl.getIosfileurl().trim())){
					String filerealpath=fileu.getWebRoot()+acl.getIosfileurl();
					File file=new File(filerealpath);
					if(!file.exists()){
						acl.setIosfileurl("");
					}
				}
			}else{
				//不存在则只查询app信息
				 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				 conditionMap.put("corpcode", corpCode);
				 List<LfAppAccount> appacs=baseBiz.getByCondition(LfAppAccount.class, conditionMap, null);
				 if(appacs.size()>0){
					 appac=appacs.get(0);
				 }
			}
			request.setAttribute("acl", acl);
			request.setAttribute("appac", appac);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP客户端下载页面加载异常！");
		}finally{
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/app_clidownload.jsp").forward(request, response);
		}
	}
	
	/*
	 * 上传单图片
	 */
	public void uploadImg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 图片存放的根目录
        String baseFileDir = this.getServletContext().getRealPath("file/app/ewm");
		response.setContentType("text/html");
			// 构造出文件工厂，用于存放JSP页面中传递过来的文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
			factory.setSizeThreshold(1024 * 1024);
			// 设置上传文件的保存路径
			factory.setRepository(new File(baseFileDir));
			// 产生Servlet上传对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置可以上传文件大小的上界4MB
			upload.setSizeMax(1L * 1024 * 1024);
			// 取得所有上传文件的信息
			List<FileItem> fileList = null;
			// 上传图片保存url路径
			String imgUrl = "";
			try
			{
				fileList = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				EmpExecutionContext.error(e, "上传二维码解析上传对象异常！");
				return;
			}
			Iterator<FileItem> iter = fileList.iterator();
			LinkedHashMap<String,String> fieldMap = new LinkedHashMap<String,String>();
			while(iter.hasNext()){
				FileItem item = (FileItem) iter.next();
				String fieldName = item.getFieldName();
				if(!item.isFormField() && item.getName().length() > 0){
					// 文件名
					String fileName = item.getName();
					// 获得文件大小
					
					BufferedImage sourceImg =ImageIO.read(item.getInputStream());
					// 判断文件大小是否超过限制
//					if(width > 50||height>50)
//					{
//						item.delete();
//						item = null;
//						response.getWriter().print("overSize");
//						return;
//					}

					// 文件类型
					String allImgExt = ".jpg|.jpeg|.gif|.bmp|.png|";
					String extName = fileName.substring(fileName.lastIndexOf("."), fileName.length()).toLowerCase();

					// 如果是图片文件
					if(allImgExt.indexOf(extName + "|") != -1)
					{
						Calendar cal = Calendar.getInstance();
						// 年
						int year = cal.get(Calendar.YEAR);
						// 月
						int month = cal.get(Calendar.MONTH) + 1;
						// 日
						int day = cal.get(Calendar.DAY_OF_MONTH);

						String filepath = baseFileDir + File.separator + year + File.separator + month + File.separator + day;
						File uf = new File(filepath);
						// 更改文件的保存路径，以防止文件重名的现象出现
						if(!uf.exists())
						{
							uf.mkdirs();
						}
						try
						{
							String strid = UUID.randomUUID().toString();
							String newFileName = strid.replaceAll("-", "")+ StaticValue.getServerNumber() + extName;
							File uploadedFile = new File(filepath, newFileName);
							if(uploadedFile.exists())
							{
								boolean delSuccess = uploadedFile.delete();
								if(!delSuccess){
									EmpExecutionContext.error("删除失败！");
								}
							}
							
							item.write(uploadedFile);
							imgUrl ="file/app/ewm/" + year + "/" + month + "/" + day + "/" + newFileName;

							fieldMap.put(fieldName, imgUrl);
						}
						catch (Exception e)
						{
								EmpExecutionContext.error(e,"上传二维码失败！");
								return;
						}

					}
				}
			}
			response.getWriter().print(imgUrl);
	}
	

	/**
	 * 更新二维码以及软件信息
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void updateEwm(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//更新类型  ios 0  ad  1
		String type = request.getParameter("type");
		//账号id
		String aid =request.getParameter("aid");
		//主键id
		String clid=request.getParameter("clid");
		//发布日期
		String pushdate=request.getParameter("pushdate");
		//文件大小
		String filesize=request.getParameter("filesize");
		//文件版本
		String fileversion=request.getParameter("fileversion");
		//文件描述
		String filedescr=request.getParameter("filedescr");
		 //企业编码
		String corpCode =request.getParameter("lgcorpcode");
		//用户名
		String lgusername=request.getParameter("lgusername");
		//文件路径
		String companylogo=request.getParameter("company_logo");
		//日志信息
		String opContent=null;
		response.setContentType("text/html");
		LfAppClidowload clidl=null;
		if(clid!=null&&!"".equals(clid)){
			clidl=baseBiz.getById(LfAppClidowload.class, clid);
		}
		
		if(clidl==null){
			clidl=new LfAppClidowload();
		}
		
		if(aid==null||"".equals(aid)){
			 LfAppAccount appac=null;
			 LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			 conditionMap.put("corpcode", corpCode);
			 List<LfAppAccount> appacs=baseBiz.getByCondition(LfAppAccount.class, conditionMap, null);
			 if(appacs.size()>0){
				 appac=appacs.get(0);
				 aid=appac.getAid()!=null?appac.getAid().toString():"";
			 }
		}
		if(aid!=null&&!"".equals(aid)){
			clidl.setAid(Long.parseLong(aid));
		}
		 SimpleDateFormat sdf =  new SimpleDateFormat( "yyyy年MM月dd日" );
		Timestamp pushdatet=null;
		if(pushdate!=null&&!"".equals(pushdate)){
			if(StaticValue.ZH_HK.equals((String) request.getSession().getAttribute(StaticValue.LANG_KEY))){
				sdf =  new SimpleDateFormat( "yyyy/MM/dd/" );
				pushdatet=new Timestamp(sdf.parse(pushdate).getTime());
			} else {
				pushdatet=new Timestamp(sdf.parse(pushdate).getTime());
			}
		}

		
		if(type!=null&&!"".equals(type.trim())){
//			判断是
			if("0".equals(type)){
				opContent="APP客户端下载信息。[IOS端图片路径,发布日期,文件大小,文件版本,支持]("
					+clidl.getIosfileurl()+","+clidl.getIospushdate()+","+clidl.getIosfilesize()+","+
					clidl.getIosfileversion()+","+clidl.getIosfiledescr()+")";
				
				clidl.setIosfiledescr(filedescr);
				clidl.setIosfilesize(filesize);
				clidl.setIosfileurl(companylogo);
				clidl.setIosfileversion(fileversion);
				if(pushdate==null||"".equals(pushdate)){
					String dates="2014年9月10日";
					pushdatet=new Timestamp(sdf.parse(dates).getTime());
				}
				clidl.setIospushdate(pushdatet);
				clidl.setIosupdatedate(new Timestamp(System.currentTimeMillis()));
				clidl.setIosupdateuser(lgusername);
				opContent+="-->("+companylogo+","+pushdatet+","+filesize+","+fileversion+","+filedescr+")";
			}else if("1".equals(type)){
				opContent="APP客户端下载信息。[Android端图片路径,发布日期,文件大小,文件版本,支持]("+
				clidl.getAdfileurl()+","+clidl.getAdpushdate()+","+clidl.getAzfilesize()+","
				+clidl.getAdfileversion()+","+clidl.getAdfiledescr();
				clidl.setAdfiledescr(filedescr);
				clidl.setAzfilesize(filesize);
				clidl.setAdfileurl(companylogo);
				clidl.setAdfileversion(fileversion);
				if(pushdate==null||"".equals(pushdate)){
					String dates="2014年9月9日";
					pushdatet=new Timestamp(sdf.parse(dates).getTime());
				}
				clidl.setAdpushdate(pushdatet);
				clidl.setAdupdatedate(new Timestamp(System.currentTimeMillis()));
				clidl.setAdupdateuser(lgusername);
				opContent+="-->("+companylogo+","+pushdatet+","+filesize+","+fileversion+","+filedescr+")";
			}else{
				request.getRequestDispatcher(this.empRoot  + this.basePath+"/app_clidownload.htm?result=false").forward(request, response);
			}
		}else{
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/app_clidownload.htm?result=false").forward(request, response);
		}
		
		clidl.setCorpcode(corpCode);
		boolean boo=false;
        int	operationtype=1;   //操作类型     1 修改(默认）  0 新增
		try
		{
			if(clidl.getClid()!=null&&clidl.getClid()!=0){
				operationtype=1;
				boo=baseBiz.updateObj(clidl);
			}else{
				operationtype=0;
				boo=baseBiz.addObj(clidl);
			}
			if(operationtype==0){
				opContent+="新增";
			}else{
				opContent+="修改";
			}
			
			if(boo){
				opContent+="成功。";
			}else{
				opContent+="失败。";
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info("APP客户端下载", loginSysuser.getCorpCode(),
						loginSysuser.getUserId().toString(), loginSysuser
								.getUserName(), opContent, "UPDATE");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"更新企业客户端下载信息失败！");
		}finally{
			request.getRequestDispatcher(this.empRoot  + this.basePath+"/app_clidownload.jsp?result=true").forward(request, response);
		}
	}
	
	
}

