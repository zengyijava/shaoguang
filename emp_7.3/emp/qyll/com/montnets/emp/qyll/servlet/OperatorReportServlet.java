package com.montnets.emp.qyll.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.OperatorReportBiz;
import com.montnets.emp.qyll.utils.ExcelExportUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.qyll.vo.LlOperatorReportVo;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PageInfo;


public class OperatorReportServlet extends BaseServlet{
	private static final long serialVersionUID = 1L;
	
	private static final String PATH = "qyll/tjbb";

	public void find(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException, EMPException{
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		// 是否第一次访问
		boolean isFirstEnter = pageSet(pageInfo, request);
		//获取参数
		OperatorReportBiz operatorReportBiz = new OperatorReportBiz();
		LlOperatorReportVo llOperatorReport = getParm(request,pageInfo,isFirstEnter);
		request.setAttribute("llOperatorReport", llOperatorReport);
		// 企业编码
		String lgcorpcode = "";
		// 登录操作员id
		String lguserid = "";
		LfSysuser user = getLoginUser(request);
		if(user == null)
		{
			EmpExecutionContext.error("操作员统计报表查询，获取不到登录操作员对象。lgcorpcode=" + lgcorpcode + ",lguserid=" + lguserid);
			return ;
		}
		// 直接从session中取企业编码和操作员id
		lgcorpcode = user.getCorpCode();
		lguserid = user.getUserId().toString();
		try{
			if(("0").equals(llOperatorReport.getIsFirstEnter()))
			{
				//第一次进入，清空session查询条件
				clearSearchCondition(request);
				return;
			}else{
				//获取list数据
				List<LlOperatorReportVo> llOperatorReportList = operatorReportBiz.getLlOperatorReportList(llOperatorReport,pageInfo);
				// 合计
				LlOperatorReportVo llOperatorReportSum = new LlOperatorReportVo();
				long[] sum= operatorReportBiz.findSumCount(llOperatorReport);
				//合计提交总数
				llOperatorReportSum.setSunMitNumSum(sum[0]);
				//合计订购成功数
				llOperatorReportSum.setSuccNumSum(sum[1]);
				//合计订购失败数
				llOperatorReportSum.setFaildNumSum(sum[2]);
				request.setAttribute("llOperatorReportSum", llOperatorReportSum);
				
				if(sum == null || sum[0] + sum[1] + sum[2] == 0)
				{
					llOperatorReportList=new ArrayList<LlOperatorReportVo>();
					pageInfo.setTotalPage(1);
					pageInfo.setTotalRec(0);
				}
				//分页对象
				request.setAttribute("pageInfo", pageInfo);
				//操作员结果
				request.setAttribute("resultList", llOperatorReportList);
			}
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "操作员统计报表查询异常。");
		}finally
		{
			long count=0l;
			//从pageinfo中获取查询总条数
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			//开始时间
			String starthms=hms.format(startl);
			// 写日志
			String opContent = "操作员统计报表查询：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"毫秒";
			EmpExecutionContext.info("操作员统计报表",llOperatorReport.getProductName() ,llOperatorReport.getIsp() ,llOperatorReport.getReportType() ,opContent, StaticValue.GET);
			request.getRequestDispatcher(PATH +"/ll_operatorReport.jsp?lgcorpcode=" + lgcorpcode + "&lguserid=" + lguserid).forward(request, response);
		}
		
	}
	
	public void toDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		// 是否第一次访问
		boolean isFirstEnter = false;
		//获取参数
		LlOperatorReportVo llOperatorReport = getParm(request,pageInfo,isFirstEnter);
		request.setAttribute("llOperatorReport", llOperatorReport);
		
		try {
			OperatorReportBiz operatorReportBiz = new OperatorReportBiz();
			//获取list数据
			List<LlOperatorReportVo> llOperatorReportList = operatorReportBiz.getLlOperatorReportList(llOperatorReport,pageInfo);
			
			LlOperatorReportVo llOperatorReportSum = new LlOperatorReportVo();
			// 合计
			long[] sum= operatorReportBiz.findSumCount(llOperatorReport);
			//合计提交总数
			llOperatorReportSum.setSunMitNumSum(sum[0]);
			//合计订购成功数
			llOperatorReportSum.setSuccNumSum(sum[1]);
			//合计订购失败数
			llOperatorReportSum.setFaildNumSum(sum[2]);
			
			request.setAttribute("llOperatorReportSum", llOperatorReportSum);
			
			if(sum == null || sum[0] + sum[1] + sum[2] == 0)
			{
				llOperatorReportList=new ArrayList<LlOperatorReportVo>();
				pageInfo.setTotalPage(1);
				pageInfo.setTotalRec(0);
			}
			//分页对象
			request.setAttribute("pageInfo", pageInfo);
			//操作员结果
			request.setAttribute("resultList", llOperatorReportList);
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "操作员统计报表查询异常。");
		}finally
		{
			long count=0l;
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			//开始时间
			String starthms=hms.format(startl);
			// 写日志
			String opContent = "操作员统计报表查询：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"毫秒";
			EmpExecutionContext.info("操作员统计报表",llOperatorReport.getProductName() ,llOperatorReport.getIsp() ,llOperatorReport.getReportType() ,opContent, StaticValue.GET);
			request.getRequestDispatcher(PATH +"/ll_operatorReport.jsp").forward(request, response);
		}
		
	}
	
	public void rep_orderExportExcel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		boolean flag = true;
		//定义一个Excel的数据量
		int totalNum=500000;
		//用于获取list的长度
		int count = 0;
		PageInfo pageInfo = new PageInfo();
		// 是否第一次访问
		boolean isFirstEnter = false;
		LlOperatorReportVo llOperatorReport = getParm(request,pageInfo,isFirstEnter);
		OperatorReportBiz operatorReportBiz = new OperatorReportBiz();
		try {
			//调用查询方法 获取mtTaskList
			long start = System.currentTimeMillis();
			List<LlOperatorReportVo> llOperatorReportList = operatorReportBiz.getLlOperatorReportList(llOperatorReport,null);
			LlOperatorReportVo llOperatorReportSum = new LlOperatorReportVo();
			// 合计
			long[] sum= operatorReportBiz.findSumCount(llOperatorReport);
			//合计提交总数
			llOperatorReportSum.setSunMitNumSum(sum[0]);
			//合计订购成功数
			llOperatorReportSum.setSuccNumSum(sum[1]);
			//合计订购失败数
			llOperatorReportSum.setFaildNumSum(sum[2]);
			String deletePath = request.getSession().getServletContext()
					.getRealPath("/"+MessageUtils.extractMessage("qyll","qyll_common_210",request)).replaceAll("%20", " ");
			File f = new File(deletePath);
			if(f.exists()){
				//每次下载前删除原来已存在的文件夹中的Excel
				ExcelExportUtil excelExportUtil = new ExcelExportUtil();
				excelExportUtil.deleteTmpExcel(deletePath);
				EmpExecutionContext.info("程序删除了在"+deletePath+"目录下原有的Excel");
			}
			if (!f.exists()) {
				f.mkdirs();
				EmpExecutionContext.info("程序在"+deletePath+"目录下新建了OperatorReport的文件夹用于存放下载的Excel");
			}
			//获取list的长度，即Excel表格的数据量
			count = llOperatorReportList.size();
			// 获得当前时间，精确到秒，用于Excel取名记录
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
			String datatime = sdf.format(date);
			
			List<File> srcfile = new ArrayList<File>();
			//以50万为单位，超过50万则新建一个Excel
			//定义一个excel的总数据量
			
			int num = count%totalNum;
			int total = num==0 ? count/totalNum : count/totalNum +1;
			for(int i=0;i<total;i++){
				String path = request
						.getSession()
						.getServletContext()
						.getRealPath(
								"/"+MessageUtils.extractMessage("qyll","qyll_common_210",request) + "/" + "operatorReport_"+i+"-"+ datatime
								+ ".xlsx").replaceAll("%20", " ");
				srcfile.add(new File(path)); 
				//获取数据写入Excel中
				flag = exportClassroom(path,i,totalNum,llOperatorReportList,llOperatorReport,llOperatorReportSum,request);
				
				if(!flag){
					response.getWriter().print("false");
					EmpExecutionContext.error("操作员统计报表导出到Excel失败！");
					return;
				}
			}
			ExcelExportUtil excelExportUtil =new ExcelExportUtil();
			//将文件夹压缩成zip
			File zipfile = new File(deletePath+".zip"); 
			flag=excelExportUtil.zipFiles(srcfile, zipfile); 
			if(!flag){
				response.getWriter().print("false");
				EmpExecutionContext.error("操作员统计报表压缩文件夹失败！");
				return;
			}
			response.getWriter().print("true");
	        long end = System.currentTimeMillis();
	        EmpExecutionContext.info("操作员统计报表下载成功！所花时间为"+(end-start)+"ms");
		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员统计报表下载失败！");
		}
		
	}
	
	public void downFile(HttpServletRequest request,HttpServletResponse response) {    
        try {   
        	String str = MessageUtils.extractMessage("qyll","qyll_common_210",request)+".zip";
        	String serverPath = request.getSession().getServletContext().getRealPath("/"+str).replaceAll("%20", " "); 
            String path = serverPath;    
            EmpExecutionContext.info("操作员统计报表下载的Excel所在路径:"+path);  
            File file = new File(path);    
            if (file.exists()) {    
                InputStream ins = null;
                BufferedInputStream bins = null;
                OutputStream outs  = null;
                BufferedOutputStream bouts = null;
                try{
		                ins =new FileInputStream(path);    
		                bins   = new BufferedInputStream(ins);// 放到缓冲流里面    
		                outs   = response.getOutputStream();// 获取文件输出IO流    
		                bouts   = new BufferedOutputStream(outs);    
		                response.setContentType("application/download");// 设置response内容的类型    
		                response.setHeader(    
		                        "Content-disposition",    
		                        "attachment;filename="    
		                                + URLEncoder.encode(str, "UTF-8"));// 设置头部信息    
		                int bytesRead = 0;    
		                byte[] buffer = new byte[8192];    
		                 //开始向网络传输文件流    
		                while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {    
		                   bouts.write(buffer, 0, bytesRead);    
		                }   
				} finally {
					if(null != bouts){
						bouts.flush();// 这里一定要调用flush()方法
					}
					try{
						IOUtils.closeIOs(ins, bins, outs, bouts, getClass());
					}catch(Exception e){
						EmpExecutionContext.error(e,"error:");
					}				} 
            } else {    
            	EmpExecutionContext.error("操作员统计报表下载的Excel所在文件夹不存在:"+path);  
            }    
        } catch (IOException e) {
            EmpExecutionContext.error(e, "操作员统计报表下载zip包失败");
        }
    }
	// 打印创建excel
	public boolean exportClassroom(String path, int total,  int totalNum, List<LlOperatorReportVo> llOperatorReportList,LlOperatorReportVo llOperatorReport, LlOperatorReportVo llOperatorReportSum,HttpServletRequest request) {
		boolean flag = true;
		try {	
				int count =llOperatorReportList.size();
				//创建workbook    
			 	SXSSFWorkbook workBook = new SXSSFWorkbook(-1);
				//SXSSFWorkbook workBook =new SXSSFWorkbook();  
				//根据workBook创建sheet    
			 	Sheet sheet =null;
			 	if("1".equals(llOperatorReport.getIsDel())){
			 		sheet = workBook.createSheet(MessageUtils.extractMessage("qyll","qyll_common_211",request));  
			 	}else{
			 		sheet = workBook.createSheet(MessageUtils.extractMessage("qyll","qyll_common_210",request));  
			 	}
				//根据sheet创建行    
				Row rowHead = sheet.createRow(0);    
				//创建excel的头部标题行,及标题行的样式的设置  
				createTitleCell(workBook,rowHead,sheet,request); 
				//创建主体内容  
				if(count-total*totalNum>=totalNum){
					for(int i=0;i<totalNum+1;i++){
						flag = buildMainBody(llOperatorReportList, workBook, sheet, i,total,totalNum,llOperatorReport,llOperatorReportSum,request); 
					}
				}else{
					for(int i=0;i<count-total*totalNum+1;i++){
						flag = buildMainBody(llOperatorReportList, workBook, sheet, i,total,totalNum,llOperatorReport,llOperatorReportSum,request); 
					}
				}
				OutputStream os= new FileOutputStream(path);
	        	workBook.write(os);
	        	os.flush();
	        	os.close();
		} catch (Exception e) {
			EmpExecutionContext.error(e, "操作员统计报表导出文件出错");
//			System.out.println("导出文件出错");
			return false;
		}
		return flag;
	}

	/**  
     * 创建excel的头部标题行  
     * @param rowHead  
     * @param sheet   
     */  
    public void createTitleCell(SXSSFWorkbook workBook,Row rowHead, Sheet sheet,HttpServletRequest request) {  
    	String str1 = MessageUtils.extractMessage("qyll","qyll_common_157",request);//时间;
    	String str2 = MessageUtils.extractMessage("qyll","qyll_common_103",request);//操作员;
    	String str3 = MessageUtils.extractMessage("qyll","qyll_common_100",request);//隶属机构;
    	String str4 = MessageUtils.extractMessage("qyll","qyll_common_149",request);//套餐编号;
    	String str5 = MessageUtils.extractMessage("qyll","qyll_common_150",request);//套餐名称;
    	String str6 = MessageUtils.extractMessage("qyll","qyll_common_43",request);//运营商;
    	String str7 = MessageUtils.extractMessage("qyll","qyll_common_158",request);//提交号码数;
    	String str8 = MessageUtils.extractMessage("qyll","qyll_common_113",request);//订购成功数;
    	String str9 = MessageUtils.extractMessage("qyll","qyll_common_114",request);//订购失败数;
        //根据row创建cell    
        Cell time = rowHead.createCell(0);    
        time.setCellValue(str1);    
        Cell userName = rowHead.createCell(1);    
        userName.setCellValue(str2);    
        Cell depName = rowHead.createCell(2);    
        depName.setCellValue(str3);    
        Cell nameId = rowHead.createCell(3);    
        nameId.setCellValue(str4);    
        Cell nameAndId = rowHead.createCell(4);    
        nameAndId.setCellValue(str5);    
        Cell isp = rowHead.createCell(5);    
        isp.setCellValue(str6);    
        Cell sunMitNum = rowHead.createCell(6);    
        sunMitNum.setCellValue(str7);    
        Cell succNum = rowHead.createCell(7);    
        succNum.setCellValue(str8);    
        Cell faildNum = rowHead.createCell(8);    
        faildNum.setCellValue(str9);  
          
      //设置列宽（给时间的单元格的宽度给大点，防止时间显示格式错误！）    
        sheet.setColumnWidth(0, 20*500);    
        sheet.setColumnWidth(1, 20*200);    
        sheet.setColumnWidth(2, 20*200);    
        sheet.setColumnWidth(3, 20*200);    
        sheet.setColumnWidth(4, 20*400);    
        sheet.setColumnWidth(5, 20*200);    
        sheet.setColumnWidth(6, 20*150);    
        sheet.setColumnWidth(7, 20*150);    
        sheet.setColumnWidth(8, 20*150);    
    } 


	private boolean buildMainBody(List<LlOperatorReportVo> llOperatorReportList,SXSSFWorkbook workBook, Sheet sheet, int i, int total,
			int totalNum,LlOperatorReportVo llOperatorReport,LlOperatorReportVo llOperatorReportSum,HttpServletRequest request) {
		try{
			String str1 = MessageUtils.extractMessage("qyll","qyll_common_159",request);//"年";
			String str2 = MessageUtils.extractMessage("qyll","qyll_common_160",request);//"月";
			String str3 = MessageUtils.extractMessage("qyll","qyll_common_161",request);//"日";
			String str4 = MessageUtils.extractMessage("qyll","qyll_common_185",request);//移动;
			String str5 = MessageUtils.extractMessage("qyll","qyll_common_186",request);//联通;
			String str6 = MessageUtils.extractMessage("qyll","qyll_common_187",request);//电信 ;
			String str7 = MessageUtils.extractMessage("qyll","qyll_common_162",request);//合计;
	 		int listIndex = total*totalNum+i;
	        Row rowBody = sheet.createRow(i+1);  
	        //创建列数据  
	        if(listIndex <= (llOperatorReportList.size()-1)){
	        	LlOperatorReportVo mtTask = llOperatorReportList.get(listIndex); 
	        	String time = null;
	        	String nameId = mtTask.getProductId();
	        	String nameAndId = mtTask.getProductName();
	        	String isp = null;
	        	String sunMitNum = mtTask.getSunMitNum().toString();
	        	String succNum = mtTask.getSuccNum().toString();
	        	String faildNum =mtTask.getFaildNum().toString();
	        	String UName = mtTask.getUName()==null?"-":mtTask.getUName();
	        	String depName = mtTask.getDepNam()==null?"-":mtTask.getDepNam();
	        	if("1".equals(llOperatorReport.getIsDel())){
	        		if(("1").equals(llOperatorReport.getReportType())||("2").equals(llOperatorReport.getReportType())||("9999").equals(llOperatorReport.getReportType())){
	        			time = mtTask.getReportDate().substring(0, 4)+ str1 +mtTask.getReportDate().substring(4, 6)+ str2 +mtTask.getReportDate().substring(6, 8)+str3;
	        		}else if(("3").equals(llOperatorReport.getReportType())){
	        			time = mtTask.getReportDate().substring(4, 6) + str2;
	        		}else{
	        			time = "-";
	        		}
	        	}else{
	        		time = llOperatorReport.getShowTime();
	        	}
	        	if("1".equals(mtTask.getIsp())){
	        		isp = str4;
	        	}else if("2".equals(mtTask.getIsp())){
	        		isp = str6;
	        	}else if("3".equals(mtTask.getIsp())){
	        		isp = str5;
	        	}else{
	        		isp = "--";
	        	}
	        	Cell timeCell = rowBody.createCell(0);    
	        	timeCell.setCellValue(time);    
	        	Cell UNameCell = rowBody.createCell(1);    
	        	UNameCell.setCellValue(UName);    
	        	Cell depNameCell = rowBody.createCell(2);    
	        	depNameCell.setCellValue(depName);    
	        	Cell nameIdCell = rowBody.createCell(3);    
	        	nameIdCell.setCellValue(nameId);    
	        	Cell nameAndIdCell = rowBody.createCell(4);    
	        	nameAndIdCell.setCellValue(nameAndId);    
	        	Cell ispCell = rowBody.createCell(5);    
	        	ispCell.setCellValue(isp);    
	        	Cell sunMitNumCell = rowBody.createCell(6);    
	        	sunMitNumCell.setCellValue(sunMitNum);    
	        	Cell succNumCell = rowBody.createCell(7);    
	        	succNumCell.setCellValue(succNum);    
	        	Cell faildNumCell = rowBody.createCell(8);    
	        	faildNumCell.setCellValue(faildNum);  
	        }else{
	        	Cell timeCell = rowBody.createCell(0);    
	        	timeCell.setCellValue("");    
	        	Cell userNameCell = rowBody.createCell(1);    
	        	userNameCell.setCellValue("");    
	        	Cell depNameCell = rowBody.createCell(2);    
	        	depNameCell.setCellValue("");    
	        	Cell nameIdCell = rowBody.createCell(3);    
	        	nameIdCell.setCellValue("");    
	        	Cell nameAndIdCell = rowBody.createCell(4);    
	        	nameAndIdCell.setCellValue("");    
	        	Cell ispCell = rowBody.createCell(5);    
	        	ispCell.setCellValue(str7+"：");    
	        	Cell sunMitNumCell = rowBody.createCell(6);    
	        	sunMitNumCell.setCellValue(llOperatorReportSum.getSunMitNumSum());    
	        	Cell succNumCell = rowBody.createCell(7);    
	        	succNumCell.setCellValue(llOperatorReportSum.getSuccNumSum());    
	        	Cell faildNumCell = rowBody.createCell(8);    
	        	faildNumCell.setCellValue(llOperatorReportSum.getFaildNumSum());  
	        }
	        
	        if(i%100==0){
	        	((SXSSFSheet)sheet).flushRows();
	        }
	 	}catch(Exception e){
	 		EmpExecutionContext.error(e, "操作员统计报表记录拼接Excel的body出错");
	 		return false;
	 	}
		return true;

	}
	/**
	 * 生成机构树字符串
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 机构iD
		String depId = request.getParameter("depId");
		try
		{
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			OperatorReportBiz operatorReportBiz = new OperatorReportBiz();
			String depJson = operatorReportBiz.getDepJosn(depId, curUser);
			if(depJson == null)
			{
				response.getWriter().print("");
			}
			else
			{
				response.getWriter().print(depJson);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，生成机构树字符串，异常。depId="+depId);
			response.getWriter().print("");
		}
	}


	/**
	 * 生成机构操作员树字符串
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String depId = request.getParameter("depId");
		try
		{
			String replaceStr = MessageUtils.extractMessage("qyll","qyll_common_214",request);//已注销
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			OperatorReportBiz operatorReportBiz = new OperatorReportBiz();
			String deptUserTree = operatorReportBiz.getDepUserJosn(depId, curUser,replaceStr);
			if(deptUserTree == null)
			{
				response.getWriter().print("");
			}
			else
			{
				response.getWriter().print(deptUserTree);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询统计，生成机构操作员树字符串，异常。depId="+depId);
			response.getWriter().print("");
		}
	}
	/**
	 * 从session获取当前登录操作员对象
	 * @param request 请求对象
	 * @return 返回当前登录操作员对象，为空则返回null
	 */
	private LfSysuser getCurUserInSession(HttpServletRequest request)
	{
		Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
		if(loginSysuserObj == null)
		{
			return null;
		}
		return (LfSysuser)loginSysuserObj;
	}

	private void clearSearchCondition(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("llOperatorReport");
	}

	private LlOperatorReportVo getParm(HttpServletRequest request,
			PageInfo pageInfo, boolean isFirstEnter) throws UnsupportedEncodingException {
		String firstDay = null; 
		String lastDay = null;
		Date d = new Date();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);  
		String day = dateNowStr.substring(8,10);
		/*if("01".equals(day)){
			 //获取前一个月第一天
	        Calendar calendar1 = Calendar.getInstance();
	        calendar1.add(Calendar.MONTH, -1);
	        calendar1.set(Calendar.DAY_OF_MONTH,1);
	        firstDay = sdf.format(calendar1.getTime());
	        //获取前一个月最后一天
	        Calendar calendar2 = Calendar.getInstance();
	        calendar2.set(Calendar.DAY_OF_MONTH, 0);
	        lastDay = sdf.format(calendar2.getTime());
		}else{
			Calendar cal1 = Calendar.getInstance();    
			cal1.add(Calendar.MONTH, 0);
			cal1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
	        firstDay = sdf.format(cal1.getTime());
	        Calendar cal2 = Calendar.getInstance();
	        cal2.add(Calendar.DATE,   -1);
	        lastDay = sdf.format(cal2.getTime());
		}*/
		Calendar cal1 = Calendar.getInstance();    
		cal1.add(Calendar.MONTH, 0);
		cal1.set(Calendar.DAY_OF_MONTH,1);
		firstDay = sdf.format(cal1.getTime());
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, 0);
        lastDay = sdf.format(cal2.getTime());
        
		LlOperatorReportVo retrunVo = new LlOperatorReportVo();
		//流量套餐(产品名称)
		String productName = request.getParameter("productName")==null?request.getParameter("productName"):request.getParameter("productName").trim();
		if (productName != null && !productName.equals("")) {
			productName = URLDecoder.decode(productName, "utf-8");
		}
		//运营商
		String isp = request.getParameter("isp")==null?"9999":request.getParameter("isp");
		//报表类型(默认为日报表)
		String  reportType= request.getParameter("reportType")==null?"1":request.getParameter("reportType");
		//日报表初始时间
		String sendtime = request.getParameter("sendtime")==null?firstDay:request.getParameter("sendtime");
		//日报表结束时间
		String recvtime = request.getParameter("recvtime")==null?lastDay:request.getParameter("recvtime");
		//年、月表时间
		String statisticsTime = request.getParameter("statisticsTime");
		//流量套餐编号(产品编号)
		String productId = request.getParameter("productId")==null?request.getParameter("productId"):request.getParameter("productId").trim();
		//是否是详情(1:是)
		String isDel = request.getParameter("isDel");
		
		String selects = MessageUtils.extractMessage("qyll","qyll_common_101",request);//"请选择";
		//操作员
		String UName = request.getParameter("userName");
		// 用于回显的操作员
		String userName = request.getParameter("userString");
		if (UName != null && !UName.equals("")) {
			UName = URLDecoder.decode(UName, "utf-8");
			userName = UName;
			String replaceStr = "("+MessageUtils.extractMessage("qyll","qyll_common_214",request)+")";//已注销
			UName = UName.replace(replaceStr, "");
		}
		if(selects.equals(UName)){
			UName = "-9999";
		}
		
		//机构
		String depNam = request.getParameter("depNam");
		if (depNam != null && !depNam.equals("")) {
			depNam = URLDecoder.decode(depNam, "utf-8");
		}
		if(selects.equals(depNam)){
			depNam = "-9999";
		}
		// 部门ID组合字符串
		String depids = request.getParameter("deptString");
		String showTime = "";
		String str1 = MessageUtils.extractMessage("qyll","qyll_common_159",request);//"年";
		String str2 = MessageUtils.extractMessage("qyll","qyll_common_160",request);//"月";
		String str3 = MessageUtils.extractMessage("qyll","qyll_common_161",request);//"日";
		String str4 = MessageUtils.extractMessage("qyll","qyll_common_105",request);//"至";
		if(!"1".equals(isDel)){
			if("1".equals(reportType)&&sendtime!=null&&recvtime!=null){
				showTime = sendtime.substring(0,4)+str1+sendtime.substring(5,7)+str2+sendtime.substring(8,10)+str3+str4
						+recvtime.substring(0,4)+str1+recvtime.substring(5,7)+str2+recvtime.substring(8,10)+str2;
			}else if("2".equals(reportType)&&statisticsTime!=null){
				showTime = statisticsTime.substring(0,4)+str1+statisticsTime.substring(5,7)+str2;
			}else if("3".equals(reportType)&&statisticsTime!=null){
				showTime = statisticsTime.substring(0,4)+str1;
			}else{
				showTime = null;
			}
		}
		LlCompInfoBiz llCompInfoBiz = new LlCompInfoBiz();
		LlCompInfoVo llCompInfoBean = null;
		try {
			llCompInfoBean = llCompInfoBiz.getLlCompInfoBean();
		} catch (Exception e) {
			EmpExecutionContext.error(e,"查询企业信息获取企业编号报错");
		}
		if(llCompInfoBean == null ){
			EmpExecutionContext.error("查询企业信息异常");
		}else{
			retrunVo.setEcid(Integer.parseInt(llCompInfoBean.getCorpCode()==null?"0":llCompInfoBean.getCorpCode()));	
		}
		if(!isFirstEnter){
			retrunVo.setIsFirstEnter("1");
		}else{
			retrunVo.setIsFirstEnter("0");
		}
		retrunVo.setProductName(productName);
		retrunVo.setIsp(isp);
		retrunVo.setReportType(reportType);
		retrunVo.setSendtime(sendtime);
		retrunVo.setRecvtime(recvtime);
		retrunVo.setStatisticsTime(statisticsTime);
		retrunVo.setProductId(productId);
		retrunVo.setShowTime(showTime);
		retrunVo.setIsDel(isDel);
		retrunVo.setUserName(userName);
		retrunVo.setDepNam(depNam);
		retrunVo.setUName(UName);
		retrunVo.setDepids(depids);
		return retrunVo;
	}
	
}
