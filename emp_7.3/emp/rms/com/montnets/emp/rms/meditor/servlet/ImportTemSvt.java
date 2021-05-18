package com.montnets.emp.rms.meditor.servlet;

import com.alibaba.fastjson.JSONObject;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.rms.meditor.biz.imp.ImportTempBiz;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.IOUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

/**
 * @description 批量模板导入入口
 * @author hsh
 * @datetime 2018-11-07 上午09:27:16
 */
public class ImportTemSvt extends BaseServlet {
	
	final String empRoot = "rms";
	final String basePath = "/meditor";
    
	final BaseBiz baseBiz = new BaseBiz();
	final ImportTempBiz tempBiz  = new ImportTempBiz();
    
    public void find(HttpServletRequest request, HttpServletResponse response) {

    	 try {
         	 request.getRequestDispatcher(empRoot + basePath + "/importTem.jsp").forward(request, response);
	      } catch (Exception e) {
	             try {
	                 request.getRequestDispatcher(empRoot + basePath + "/importTem.jsp").forward(request, response);
	             } catch (ServletException e1) {
	                 EmpExecutionContext.error(e1, "富新应用批量导入模板serlvet异常！");
	             } catch (IOException e1) {
	                 EmpExecutionContext.error(e1, "富新应用批量导入模板serlvet跳转异常！");
	             }
	     }
    }
    
    public void importTemp(HttpServletRequest request, HttpServletResponse response) {
    	//获取企业名称
  		 List<LfCorp> corps = null;
  		 try {
  			 corps = baseBiz.getByCondition(LfCorp.class, null, null);
  			 request.setAttribute("corps", corps);
        	 request.getRequestDispatcher(empRoot + basePath + "/importTemkk.jsp").forward(request, response);
	      } catch (Exception e) {
	             try {
	                 request.getRequestDispatcher(empRoot + basePath + "/importTemkk.jsp").forward(request, response);
	             } catch (ServletException e1) {
	                 EmpExecutionContext.error(e1, "富新应用批量导入模板获取企业名称serlvet异常！");
	             } catch (IOException e1) {
	                 EmpExecutionContext.error(e1, "富新应用批量导入模板获取企业名称serlvet跳转异常！");
	             }
	     }
   }    
    
    public void importTempDatas(HttpServletRequest request, HttpServletResponse response) throws Exception {
    
    	//1.获取文件上传工厂类
		DiskFileItemFactory factory = new DiskFileItemFactory();
		//--设置内存缓冲区的大小，默认是10KB
		factory.setSizeThreshold(1024*100);
		//--设置临时文件夹的位置
		String path1 = getServletContext().getRealPath("file/temp");
		File filetemp = new File(path1);
		if(!filetemp.exists()){
			filetemp.mkdirs();
		}
		
		factory.setRepository(new File(getServletContext().getRealPath("file/temp")));
		//2.获取文件上传的核心类
		ServletFileUpload fileUpload = new ServletFileUpload(factory);

		//--检查当前表单是否是multipart/form-data类型
		if(!fileUpload.isMultipartContent(request)){
			throw new RuntimeException("请使用正确的表单上传数据~！");
		}
		//--设置单个文件大小限制
//		fileUpload.setFileSizeMax(1024*1024*100);
		//--设置总大小限制
//		fileUpload.setSizeMax(1024*1024*200);
		//--设置编码解决上传文件名的乱码问题
		fileUpload.setHeaderEncoding("utf-8");
		
		//获取企业名称
 		 List<LfCorp> corps = null;
 		 String corp = "";
 		 String zhuti = "";
 		 String fileName = "";
 		 String tmName = "";
 		 PrintWriter writer = null;
 		 File file = null;
 		 JSONObject importTemp = null;
 		
 		 try {
				//3.解析request
				List<FileItem> list = fileUpload.parseRequest(request);
				//4.遍历集合拿到每一个FileItem处理
				for(FileItem item : list){
					if(item.isFormField()){
						//--当前item代表的是一个普通字段项
						String name = item.getFieldName();
						String value = item.getString("utf-8");
						if(name!=null&&name.equals("corp")){
							corp = value;
						}
						
						if(name!=null&&name.equals("zhuti")){
							zhuti = value;
						}
						
						if(name!=null&&name.equals("numFile")){
							fileName = value;
						}
						
					}else{
						//--当前item代表的是一个文件上传项
						String fname = item.getName();
						fname = UUID.randomUUID().toString()+"_"+fname;
		
						String path = "file"+File.separator+"empUpload";
						path = path + File.separator+ getYMD();
						
						path = getServletContext().getRealPath(path);
						file = new File(path);
						if(!file.exists()){//目录不存在则直接创建
				            file.mkdirs();
				        }
						
						String pathXlsx = path + File.separator + fname;
						file = new File(pathXlsx);
						if(!file.exists()){
                            boolean flag = file.createNewFile();
                            if (!flag) {
                                EmpExecutionContext.error("创建文件失败！");
                            }
						}
						
						InputStream in = item.getInputStream();
						OutputStream out = new FileOutputStream(pathXlsx);
						try{
							byte [] bs = new byte [1024];
							int i = 0;
							while((i=in.read(bs))!=-1){
								out.write(bs,0,i);
							}
						}finally{
							IOUtils.closeIOs(in, out, null, null, getClass());
						}
						
						//--删除临时文件
						item.delete();
					}
				}
			if (StaticValue.getCORPTYPE() == 0){
					corp = "100001";
			}
  			//调用导入的方法 {"cause":"传入的文件不是excel文件,请传入.xls，.xlsx表格文件","status":false}  
  			importTemp = tempBiz.importTemp(file, corp, zhuti, request);
  			writer = response.getWriter();
  			Boolean status = importTemp.getBoolean("status");
  			String cause = importTemp.getString("cause");
  			if(importTemp!=null){
  				writer.write(cause);
  			}else{
  				writer.write("导入失败");
  			}
	      } catch (Exception e) {
	             try {
	                 request.getRequestDispatcher(empRoot + basePath + "/importTemkk.jsp").forward(request, response);
	             } catch (ServletException e1) {
	                 EmpExecutionContext.error(e1, "富新应用批量导入模板获取企业名称serlvet异常！");
	             } catch (IOException e1) {
	                 EmpExecutionContext.error(e1, "富新应用批量导入模板获取企业名称serlvet跳转异常！");
	             }
	     }
   } 
    
    /**
     * 导出样例
     * @param request
     * @param response
     */
    public void smsReportAllExcel(HttpServletRequest request, HttpServletResponse response) {

        JSONObject exportTempJson = null;
        PrintWriter out = null;

        try {

        	//查询批量导入数据 {"status":true,"data":{"src":"/D:/soft/tomcat/tomcat6/webapps/emp_sta/rms/meditor/file/download/importTemp/1541844955100.zip"}}
	        exportTempJson = tempBiz.exportDefaultTempExcel();
	        
	        out = response.getWriter();
			out.write(exportTempJson.toString());

        } catch (Exception e) {

            EmpExecutionContext.error(e, "富新应用批量导入详情serlvet异常！");

        }
    }

    /**
     * 下载文件
     * @param request
     * @param response
     */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) {
	    
	    JSONObject dataJson = null;
	    PrintWriter out = null;
	    DownloadFile downloadFile = new DownloadFile();
	  //任务批次
		String data = "";
		String filePath = "";
		String fileName = "";
	    try {
	    	//任务批次
	    	data= request.getParameter("data");
	    	if(data!=null){
	//    		dataJson = JSONObject.parseObject(data); 
	    		filePath = data;
	    		if(filePath!=null&&!"".endsWith(filePath)){
	    			fileName = filePath.substring(filePath.lastIndexOf("/")+1);
	    		}	
	    	}
			
	    	downloadFile.downFile(request, response, filePath, fileName);
	           
	    } catch (Exception e) {
	
	        EmpExecutionContext.error(e, "富新应用批量导入详情serlvet异常！");
	
	    }
	}

	/**
	 * 拼装年月日目录
	 * @return
	 */
	public String getYMD(){
		Calendar date = null;
		//获取年月日
		int year1 = 0;
		int mounth1 = 0;
		int day1 = 0;
		String year = "";
		String mounth = "";
		String day = "";
		
		String path = "";
		try {
			date = Calendar.getInstance();
			//获取年月日
			year1 = date.get(Calendar.YEAR);
			mounth1 = date.get(Calendar.MONTH)+1;
			day1 = date.get(Calendar.DAY_OF_MONTH);
			
			year = String.valueOf(year1);
			mounth = String.valueOf(mounth1);
			day = String.valueOf(day1);
			
			path = year + File.separator + mounth + File.separator + day;

		} catch (Exception e) {
			 EmpExecutionContext.error(e, "创建年月日目录失败！");
		}
		return path;
		
	}
}
