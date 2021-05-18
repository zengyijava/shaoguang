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

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.qyll.biz.LlCompInfoBiz;
import com.montnets.emp.qyll.biz.OrderReportBiz;
import com.montnets.emp.qyll.utils.ExcelExportUtil;
import com.montnets.emp.qyll.vo.LlCompInfoVo;
import com.montnets.emp.qyll.vo.LlOrderReportVo;
import com.montnets.emp.util.IOUtils;
import com.montnets.emp.util.PageInfo;

public class OrderReportServlet extends BaseServlet{
	
	private static final long serialVersionUID = 1L;
	private static final String PATH = "qyll/tjbb";
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{	
			SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
			//起始ms数
			long startl=System.currentTimeMillis();
			PageInfo pageInfo = new PageInfo();
			// 是否第一次访问
			boolean isFirstEnter = pageSet(pageInfo, request);
			//获取参数
			LlOrderReportVo llOrderReport = getParm(request,pageInfo,isFirstEnter);
			request.setAttribute("llOrderReport", llOrderReport);
			try {
				//第一次进入
				if(("0").equals(llOrderReport.getIsFirstEnter()))
				{
					//第一次进入，清空session查询条件
					clearSearchCondition(request);
					return;
				}else{
					OrderReportBiz orderReportBiz = new OrderReportBiz();
					//获取list数据
					List<LlOrderReportVo> llOrderReportList = orderReportBiz.getLlOrderReportList(llOrderReport,pageInfo);
					
					LlOrderReportVo llOrderReportSum = new LlOrderReportVo();
					// 合计
					long[] sum= orderReportBiz.findSumCount(llOrderReport);
					//合计提交总数
					llOrderReportSum.setSunMitNumSum(sum[0]);
					//合计订购成功数
					llOrderReportSum.setSuccNumSum(sum[1]);
					//合计订购失败数
					llOrderReportSum.setFaildNumSum(sum[2]);
					request.setAttribute("llOrderReportSum", llOrderReportSum);
					
					if(sum == null || sum[0] + sum[1] + sum[2] == 0)
					{
						llOrderReportList=new ArrayList<LlOrderReportVo>();
						pageInfo.setTotalPage(1);
						pageInfo.setTotalRec(0);
					}
					//分页对象
					request.setAttribute("pageInfo", pageInfo);
					//套餐订购结果
					request.setAttribute("resultList", llOrderReportList);
				}
			} catch (Exception e) {
				request.setAttribute("findresult", "-1");
				request.setAttribute("pageInfo", pageInfo);
				EmpExecutionContext.error(e, "套餐订购统计报表查询异常。");
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
				String opContent = "套餐订购统计报表查询：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"毫秒";
				EmpExecutionContext.info("套餐订购统计报表",llOrderReport.getProductName() ,llOrderReport.getIsp() ,llOrderReport.getReportType() ,opContent, StaticValue.GET);
				request.getRequestDispatcher(PATH +"/orderReport.jsp").forward(request, response);
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
		LlOrderReportVo llOrderReport = getParm(request,pageInfo,isFirstEnter);
		request.setAttribute("llOrderReport", llOrderReport);
		
		try {
			OrderReportBiz orderReportBiz = new OrderReportBiz();
			//获取list数据
			List<LlOrderReportVo> llOrderReportList = orderReportBiz.getLlOrderReportList(llOrderReport,pageInfo);
			
			LlOrderReportVo llOrderReportSum = new LlOrderReportVo();
			// 合计
			long[] sum= orderReportBiz.findSumCount(llOrderReport);
			//合计提交总数
			llOrderReportSum.setSunMitNumSum(sum[0]);
			//合计订购成功数
			llOrderReportSum.setSuccNumSum(sum[1]);
			//合计订购失败数
			llOrderReportSum.setFaildNumSum(sum[2]);
			
			request.setAttribute("llOrderReportSum", llOrderReportSum);
			
			if(sum == null || sum[0] + sum[1] + sum[2] == 0)
			{
				llOrderReportList=new ArrayList<LlOrderReportVo>();
				pageInfo.setTotalPage(1);
				pageInfo.setTotalRec(0);
			}
			//分页对象
			request.setAttribute("pageInfo", pageInfo);
			//套餐订购结果
			request.setAttribute("resultList", llOrderReportList);
		} catch (Exception e) {
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			EmpExecutionContext.error(e, "套餐订购统计报表查询异常。");
		}finally
		{
			long count=0l;
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			//开始时间
			String starthms=hms.format(startl);
			// 写日志
			String opContent = "套餐订购统计报表查询：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"毫秒";
			EmpExecutionContext.info("套餐订购统计报表",llOrderReport.getProductName() ,llOrderReport.getIsp() ,llOrderReport.getReportType() ,opContent, StaticValue.GET);
			request.getRequestDispatcher(PATH +"/orderReport.jsp").forward(request, response);
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
		LlOrderReportVo llOrderReport = getParm(request,pageInfo,isFirstEnter);
		OrderReportBiz orderReportBiz = new OrderReportBiz();
		try {
			//调用查询方法 获取mtTaskList
			long start = System.currentTimeMillis();
			List<LlOrderReportVo> llOrderReportList = orderReportBiz.getLlOrderReportList(llOrderReport,null);
			LlOrderReportVo llOrderReportSum = new LlOrderReportVo();
			// 合计
			long[] sum= orderReportBiz.findSumCount(llOrderReport);
			//合计提交总数
			llOrderReportSum.setSunMitNumSum(sum[0]);
			//合计订购成功数
			llOrderReportSum.setSuccNumSum(sum[1]);
			//合计订购失败数
			llOrderReportSum.setFaildNumSum(sum[2]);
			String deletePath = request.getSession().getServletContext()
					.getRealPath("/"+MessageUtils.extractMessage("qyll","qyll_common_208",request)).replaceAll("%20", " ");
			File f = new File(deletePath);
			if(f.exists()){
				ExcelExportUtil excelExportUtil = new ExcelExportUtil();
				//每次下载前删除原来已存在的文件夹中的Excel
				excelExportUtil.deleteTmpExcel(deletePath);
				EmpExecutionContext.info("程序删除了在"+deletePath+"目录下原有的Excel");
			}
			if (!f.exists()) {
				f.mkdirs();
				EmpExecutionContext.info("程序在"+deletePath+"目录下新建了套餐订购统计报表的文件夹用于存放下载的Excel");
			}
			//获取list的长度，即Excel表格的数据量
			count = llOrderReportList.size();
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
								"/"+MessageUtils.extractMessage("qyll","qyll_common_208",request) + "/" + "orderReport_"+i+"-"+ datatime
								+ ".xlsx").replaceAll("%20", " ");
				srcfile.add(new File(path)); 
				//获取数据写入Excel中
				flag = this.exportClassroom(path,i,totalNum,llOrderReportList,llOrderReport,llOrderReportSum,request);
				
				if(!flag){
					response.getWriter().print("false");
					EmpExecutionContext.error("套餐统计报表导出到Excel失败！");
					return;
				}
			}
			ExcelExportUtil excelExportUtil =new ExcelExportUtil();
			//将文件夹压缩成zip
			File zipfile = new File(deletePath+".zip"); 
			flag=excelExportUtil.zipFiles(srcfile, zipfile); 
			if(!flag){
				response.getWriter().print("false");
				EmpExecutionContext.error("套餐统计报表压缩文件夹失败！");
				return;
			}
			response.getWriter().print("true");
	        long end = System.currentTimeMillis();
	        EmpExecutionContext.info("套餐统计报表下载成功！所花时间为"+(end-start)+"ms");
		} catch (Exception e) {
			EmpExecutionContext.error("套餐统计报表下载失败！");
		}
		
	}
	
	public void downFile(HttpServletRequest request,HttpServletResponse response) {    
        try {
        	String str = MessageUtils.extractMessage("qyll","qyll_common_208",request)+".zip";
        	String path = request.getSession().getServletContext().getRealPath("/"+str).replaceAll("%20", " "); 
            EmpExecutionContext.info("套餐统计报表下载的Excel所在路径:"+path);  
            File file = new File(path);    
            if (file.exists()) {    
				InputStream ins = null;
				BufferedInputStream bins = null;
				OutputStream outs = null;
				BufferedOutputStream bouts = null;
				try {
					ins = new FileInputStream(path);
					bins = new BufferedInputStream(ins);// 放到缓冲流里面
					outs = response.getOutputStream();// 获取文件输出IO流
					bouts = new BufferedOutputStream(outs);
            	
	                response.setContentType("application/download");// 设置response内容的类型    
	                response.setHeader(    
	                        "Content-disposition",    
	                        "attachment;filename="    
	                                + URLEncoder.encode(str, "utf-8"));// 设置头部信息    
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
						IOUtils.closeIOs(ins, bins, outs, bouts,this.getClass());
					}catch(Exception e){
						EmpExecutionContext.error(e,"error:");
					}				}
            } else {    
            	EmpExecutionContext.error("套餐统计报表下载的Excel所在文件夹不存在:"+path);  
            }    
        } catch (IOException e) {    
            EmpExecutionContext.error("套餐统计报表下载zip包失败");
        }
    }
	// 打印创建excel
	public boolean exportClassroom(String path, int total,  int totalNum, List<LlOrderReportVo> llOrderReportList,LlOrderReportVo llOrderReport, LlOrderReportVo llOrderReportSum,HttpServletRequest request) {
		boolean flag = true;
		try {	
				int count =llOrderReportList.size();
			 	SXSSFWorkbook workBook = new SXSSFWorkbook(-1);
				//根据workBook创建sheet    
			 	Sheet sheet =null;
			 	if("1".equals(llOrderReport.getIsDel())){
			 		sheet = workBook.createSheet(MessageUtils.extractMessage("qyll","qyll_common_209",request));  
			 	}else{
			 		sheet = workBook.createSheet(MessageUtils.extractMessage("qyll","qyll_common_208",request));  
			 	}
				//根据sheet创建行    
				Row rowHead = sheet.createRow(0);    
				//创建excel的头部标题行,及标题行的样式的设置  
				createTitleCell(workBook,rowHead,sheet,request); 
				//创建主体内容  
				if(count-total*totalNum>=totalNum){
					for(int i=0;i<totalNum+1;i++){
						flag = buildMainBody(llOrderReportList, workBook, sheet, i,total,totalNum,llOrderReport,llOrderReportSum,request); 
					}
				}else{
					for(int i=0;i<count-total*totalNum+1;i++){
						flag = buildMainBody(llOrderReportList, workBook, sheet, i,total,totalNum,llOrderReport,llOrderReportSum,request); 
					}
				}
				OutputStream os= new FileOutputStream(path);
	        	workBook.write(os);
	        	os.flush();
	        	os.close();
		} catch (Exception e) {
			EmpExecutionContext.error("套餐统计报表导出文件出错");
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
    	String str2 = MessageUtils.extractMessage("qyll","qyll_common_149",request);//套餐编号;
    	String str3 = MessageUtils.extractMessage("qyll","qyll_common_150",request);//套餐名称;
    	String str4 = MessageUtils.extractMessage("qyll","qyll_common_43",request);//运营商;
    	String str5 = MessageUtils.extractMessage("qyll","qyll_common_158",request);//提交号码数;
    	String str6 = MessageUtils.extractMessage("qyll","qyll_common_113",request);//订购成功数;
    	String str7 = MessageUtils.extractMessage("qyll","qyll_common_114",request);//订购失败数;
        //根据row创建cell    
        Cell time = rowHead.createCell(0);    
        time.setCellValue(str1);    
        Cell nameId = rowHead.createCell(1);    
        nameId.setCellValue(str2);    
        Cell nameAndId = rowHead.createCell(2);    
        nameAndId.setCellValue(str3);    
        Cell isp = rowHead.createCell(3);    
        isp.setCellValue(str4);    
        Cell sunMitNum = rowHead.createCell(4);    
        sunMitNum.setCellValue(str5);    
        Cell succNum = rowHead.createCell(5);    
        succNum.setCellValue(str6);    
        Cell faildNum = rowHead.createCell(6);    
        faildNum.setCellValue(str7);  
          
      //设置列宽（给时间的单元格的宽度给大点，防止时间显示格式错误！）    
        sheet.setColumnWidth(0, 20*500);    
        sheet.setColumnWidth(1, 20*400);    
        sheet.setColumnWidth(2, 20*400);    
        sheet.setColumnWidth(3, 20*100);    
        sheet.setColumnWidth(4, 20*200);    
        sheet.setColumnWidth(5, 20*100);    
        sheet.setColumnWidth(6, 20*150);    
    } 
    /**  
     * 创建excel的内容
     * 
     */ 
    public boolean buildMainBody(
			List<LlOrderReportVo> llOrderReportList, SXSSFWorkbook workBook,
			Sheet sheet, int i, int total, int totalNum,LlOrderReportVo llOrderReport, LlOrderReportVo llOrderReportSum,HttpServletRequest request) {
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
	        if(listIndex <= (llOrderReportList.size()-1)){
	        	LlOrderReportVo mtTask = llOrderReportList.get(listIndex); 
	        	String time = null;
	        	String nameId = mtTask.getProductId();
	        	String nameAndId = mtTask.getProductName();
	        	String isp = null;
	        	String sunMitNum = mtTask.getSunMitNum().toString();
	        	String succNum = mtTask.getSuccNum().toString();
	        	String faildNum =mtTask.getFaildNum().toString();
	        	if("1".equals(llOrderReport.getIsDel())){
	        		if(("1").equals(llOrderReport.getReportType())||("2").equals(llOrderReport.getReportType())||("9999").equals(llOrderReport.getReportType())){
	        			time = mtTask.getReportDate().substring(0, 4)+ str1 +mtTask.getReportDate().substring(4, 6)+ str2 +mtTask.getReportDate().substring(6, 8)+str3;
	        		}else if(("3").equals(llOrderReport.getReportType())){
	        			time = mtTask.getReportDate().substring(4, 6) + str2;
	        		}else{
	        			time = "-";
	        		}
	        	}else{
	        		time = llOrderReport.getShowTime();
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
	        	Cell nameIdCell = rowBody.createCell(1);    
	        	nameIdCell.setCellValue(nameId);    
	        	Cell nameAndIdCell = rowBody.createCell(2);    
	        	nameAndIdCell.setCellValue(nameAndId);    
	        	Cell ispCell = rowBody.createCell(3);    
	        	ispCell.setCellValue(isp);    
	        	Cell sunMitNumCell = rowBody.createCell(4);    
	        	sunMitNumCell.setCellValue(sunMitNum);    
	        	Cell succNumCell = rowBody.createCell(5);    
	        	succNumCell.setCellValue(succNum);    
	        	Cell faildNumCell = rowBody.createCell(6);    
	        	faildNumCell.setCellValue(faildNum);  
	        }else{
	        	Cell timeCell = rowBody.createCell(0);    
	        	timeCell.setCellValue("");    
	        	Cell nameIdCell = rowBody.createCell(1);    
	        	nameIdCell.setCellValue("");    
	        	Cell nameAndIdCell = rowBody.createCell(2);    
	        	nameAndIdCell.setCellValue("");    
	        	Cell ispCell = rowBody.createCell(3);    
	        	ispCell.setCellValue(str7+"：");    
	        	Cell sunMitNumCell = rowBody.createCell(4);    
	        	sunMitNumCell.setCellValue(llOrderReportSum.getSunMitNumSum());    
	        	Cell succNumCell = rowBody.createCell(5);    
	        	succNumCell.setCellValue(llOrderReportSum.getSuccNumSum());    
	        	Cell faildNumCell = rowBody.createCell(6);    
	        	faildNumCell.setCellValue(llOrderReportSum.getFaildNumSum());  
	        }
	        
	        if(i%100==0){
	        	((SXSSFSheet)sheet).flushRows();
	        }
	 	}catch(Exception e){
	 		EmpExecutionContext.error("套餐统计报表拼接Excel的body出错");
	 		return false;
	 	}
		return true;
    }


	private LlOrderReportVo getParm(HttpServletRequest request, PageInfo pageInfo, boolean isFirstEnter) throws UnsupportedEncodingException {
		String firstDay = null; 
		String lastDay = null;
		Date d = new Date();  
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateNowStr = sdf.format(d);  
		String day = dateNowStr.substring(8,10);
		Calendar cal1 = Calendar.getInstance();    
		cal1.add(Calendar.MONTH, 0);
		cal1.set(Calendar.DAY_OF_MONTH,1);
		firstDay = sdf.format(cal1.getTime());
        Calendar cal2 = Calendar.getInstance();
        cal2.add(Calendar.DATE, 0);
        lastDay = sdf.format(cal2.getTime());
		
		LlOrderReportVo retrunVo = new LlOrderReportVo();
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
		
		String str1 = MessageUtils.extractMessage("qyll","qyll_common_159",request);//"年";
		String str2 = MessageUtils.extractMessage("qyll","qyll_common_160",request);//"月";
		String str3 = MessageUtils.extractMessage("qyll","qyll_common_161",request);//"日";
		String str4 = MessageUtils.extractMessage("qyll","qyll_common_105",request);//"至";
		String showTime = "";
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
		return retrunVo;
	}

	private void clearSearchCondition(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.removeAttribute("llOrderReport");
	}
	
}
