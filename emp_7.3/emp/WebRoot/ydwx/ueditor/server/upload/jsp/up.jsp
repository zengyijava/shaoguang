<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.io.File" %> 
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.fileupload.FileItem" %>
<%@page import="org.apache.commons.fileupload.FileUpload" %>
<%@page import="org.apache.commons.fileupload.FileUploadException" %>
<%@page import="org.apache.commons.fileupload.RequestContext" %>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory" %>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload" %>
<%@page import="org.apache.commons.fileupload.servlet.ServletRequestContext" %>
<%@page import="com.montnets.emp.util.TxtFileUtil" %>
<%@page import="com.montnets.emp.netnews.common.AllUtil" %>
<%@page import="com.montnets.emp.netnews.daoImpl.Wx_netAttachedDaoImpl" %>
<%@page import="com.montnets.emp.common.constant.StaticValue"%>
<%@page import="com.montnets.emp.common.biz.CommonBiz"%>
<%@page import="com.montnets.emp.common.context.EmpExecutionContext"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String iPath = request.getRequestURI().substring(0,request.getRequestURI().lastIndexOf("/"));
String inheritPath = iPath.substring(0,iPath.lastIndexOf("/"));

			String uploadpath = StaticValue.WX_MATER;
			String userid = "2";
			CommonBiz objCommonBiz = new CommonBiz();
			uploadpath = new TxtFileUtil().getWebRoot() + uploadpath + "/";
 			String basePath12 = objCommonBiz.createDir(uploadpath);
			//设置request编码，主要是为了处理普通输入框中的中文问题
			  request.setCharacterEncoding("gbk");
			  //这里对request进行封装，RequestContext提供了对request多个访问方法
			  RequestContext requestContext = new ServletRequestContext(request);
			  //判断表单是否是Multipart类型的。这里可以直接对request进行判断，不过已经以前的用法了
			  
			  if(FileUpload.isMultipartContent(requestContext)){

				  DiskFileItemFactory factory = new DiskFileItemFactory();
				  //设置文件的缓存路径
				  factory.setRepository(new File(uploadpath + basePath12));
				  ServletFileUpload upload = new ServletFileUpload(factory);
				  //设置上传文件大小的上限，-1表示无上限 
				  upload.setSizeMax(2*1024*1024);
				  List items = new ArrayList();
				   
			      try {
			          //上传文件，并解析出所有的表单字段，包括普通字段和文件字段
			    	  items = upload.parseRequest(request);
			      } catch (FileUploadException e1) {
			    	  EmpExecutionContext.error(e1,"上传文件异常!");
			    	  //System.out.println("文件上传发生错误" + e1.getMessage());
			      }
			       //下面对每个字段进行处理，分普通字段和文件字段
			      Iterator it = items.iterator();
			      String pictitle = ""; /*附件描述*/ 
			      String filename = "";  /*文件名称*/ 
			      String imgname = ""; //系统生成的文件名
			      boolean imgFlag=false;
			      while(it.hasNext()){
			    	  FileItem fileItem = (FileItem) it.next();
			      //如果是普通字段
			    	  if(fileItem.isFormField()){   
			    	  		if("Filename".equals(fileItem.getFieldName()))
			    	  			filename = new String(fileItem.getString().getBytes("iso8859-1"), "UTF-8");
			    	  		if("pictitle".equals(fileItem.getFieldName()))
			    	  			pictitle = new String(fileItem.getString().getBytes("iso8859-1"), "UTF-8");		 
			    	  }else{
			    		  //保存文件，其实就是把缓存里的数据写到目标路径下
			    		  if(fileItem.getName()!=null && fileItem.getSize()!=0){
			    			  	File fullFile = new File(fileItem.getName());
	    			  			String pic = fullFile.getName();
	    			  			if(pictitle.lastIndexOf(".")<0){
	    			  				int s = pic.lastIndexOf(".");
	    			  				pictitle = pictitle+pic.substring(s,pic.length());
	    			  			}

	    			 	 		String imghz = pictitle.split("\\.")[pictitle.split("\\.").length-1];
	    			 	 		//后缀名处理
	    			 	 		if(imghz!=null){
	    			 	 			String type=imghz.toLowerCase();
	    			 	 			String alltype = "jpg|jpeg|gif|bmp|png|";
	    			 	 			if(alltype.indexOf(type)==-1){
	    			 	 				imgFlag=true;
	    			 	 				break;
	    			 	 			}
	    			 	 			
	    			 	 		}
	    			 	 		imgname = userid+AllUtil.getDateStream("MMddHHmmss")+((Double)Math.random()).toString().substring(2,6)+"."+imghz;
	    			 	 		File newFile = new File(uploadpath + basePath12 + imgname);
			    			  try {
			    				  fileItem.write(newFile);
			    				  boolean isFTPUpload = false;
			    				  //网讯编辑 手动上传 也要上传到服务器上 may 10-22 add 
								if(StaticValue.getISCLUSTER() ==1){
									isFTPUpload=objCommonBiz.upFileToFileServer(StaticValue.WX_MATER+"/"+basePath12 + imgname);
									if(!isFTPUpload){
									 EmpExecutionContext.error("网讯编辑手动上传图片时异常！");
									}
								}
			    				if(StaticValue.getISCLUSTER() ==0 || isFTPUpload){
			    					out.write("{'url':'"+ StaticValue.WX_MATER + "/" + basePath12 + imgname+"','title':'"+pictitle+"','state':'SUCCESS'}");
			    				}
			    			  } catch (Exception e) {
			    				  EmpExecutionContext.error(e,"上传文件异常！");
			    			  }
			    		  }else{
			    			  EmpExecutionContext.error("文件没有选择 或 文件内容为空");
			    		  }
			    	  }

			      }
			      //如果是其他类型的文件，跳出处理
			      if(imgFlag){
			      	out.write("{'url':'"+ StaticValue.WX_MATER + "/" + basePath12 + imgname+"','title':'"+pictitle+"','state':'false'}");
			      	return;
			      }
			      /*文件名称*/ 
					  /*附件描述*/ 
					  /*附件版本*/ 
					  /*文件URL地址*/  
					  /*上传人ID*/
					  /*附件类型。0：其他；1：视频 3图片*/
					  /*附件名称*/
					  String nameTemp = "";
					  if(pictitle!=null && !"".equals(pictitle)){
					      nameTemp = pictitle.substring(0, pictitle.lastIndexOf(".")); /*附件描述*/ 
					      pictitle = pictitle.substring(0, pictitle.lastIndexOf(".")); /*附件描述*/ 
					  }else{
					 	nameTemp = filename.substring(0, filename.lastIndexOf("."));  /*文件名称*/ 
					  }
					  filename = filename.substring(0, filename.lastIndexOf("."));  /*文件名称*/
					  Wx_netAttachedDaoImpl netAttachedDao = new Wx_netAttachedDaoImpl();
					Long sort_id =netAttachedDao.getFiletypeByid(3,-1000,1);
					boolean bb =netAttachedDao.UploadFile(filename,pictitle,"",uploadpath + imgname,Long.valueOf(userid),sort_id,nameTemp,0,0);
					
			  }
				
			   	

%>