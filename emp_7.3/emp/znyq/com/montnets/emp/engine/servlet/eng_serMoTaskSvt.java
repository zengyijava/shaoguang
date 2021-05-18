package com.montnets.emp.engine.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.SerMoExcelTool;
import com.montnets.emp.engine.biz.SerMoTaskBiz;
import com.montnets.emp.engine.vo.LfMoServiceVo;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 上行业务记录svt
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-22 上午09:49:58
 * @description
 */
public class eng_serMoTaskSvt extends BaseServlet
{


	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	private final BaseBiz baseBiz = new BaseBiz();
	private final SmsBiz smsBiz = new SmsBiz();
	private final String empRoot="znyq";
	private final String basePath="/engine";
	private final String path=new TxtFileUtil().getWebRoot();
	//模板路径
	protected final String  excelPath = path + empRoot+basePath+"/file/";
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try {
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务记录，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			String lgusername = curUser.getUserName();
			String strLguserid = curUser.getUserId().toString();
			//String lgusername = request.getParameter("lgusername");
			// 当前登录企业
			//String lgcorpcode = request.getParameter("lgcorpcode");
			if(strLguserid == null || strLguserid.length() == 0 || lgcorpcode == null || lgcorpcode.length() == 0){
				EmpExecutionContext.error("跳转到上行业务记录失败。" 
						+ ",lguserid：" + strLguserid
						+ ",lgcorpcode：" + lgcorpcode
						);
				return;
			}
			
			PageInfo pageInfo = new PageInfo();
			boolean isFirstEnter = pageSet(pageInfo,request);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			List<LfMoServiceVo> serMoTaskList = null;
			String content = "上行业务记录查询。";
			if (!isFirstEnter)
			{
				//手机号码
				String phone = request.getParameter("phone");
				conditionMap.put("phone", phone);
				
				//姓名
				String name = request.getParameter("name");
				conditionMap.put("name", name);
				
				//上行内容
				String msgContent = request.getParameter("msgContent");
				conditionMap.put("msgContent", msgContent);
				
				//回复状态
				String replyState = request.getParameter("replyState");
				conditionMap.put("replyState", replyState);
				
				//业务编号
				String serId = request.getParameter("serId");
				conditionMap.put("serId", serId);
				
				//业务名
				String serName = request.getParameter("serName");
				conditionMap.put("serName", serName);
				
				//上行指令
				String orderCode = request.getParameter("orderCode");
				conditionMap.put("orderCode", orderCode);
				
				//创建人
				String createrName = request.getParameter("createrName");
				conditionMap.put("createrName", createrName);
				
				//sp账号
				String spUser = request.getParameter("spUser");
				conditionMap.put("spUser", spUser);
				
				//开始时间
				String moRecBeginTime = request.getParameter("moRecBeginTime");
				conditionMap.put("moRecBeginTime", moRecBeginTime);
				
				//结束时间
				String moRecEndTime = request.getParameter("moRecEndTime");
				conditionMap.put("moRecEndTime", moRecEndTime);
				
				serMoTaskList = new SerMoTaskBiz().getSerMoTaskList(conditionMap, strLguserid, lgcorpcode, pageInfo);
				
				content += "条件phone="+phone
					+",name="+name
					+",msgContent="+msgContent
					+",replyState="+replyState
					+",serId="+serId
					+",serName="+serName
					+",orderCode="+orderCode
					+",createrName="+createrName
					+",spUser="+spUser
					+",moRecBeginTime="+moRecBeginTime
					+",moRecEndTime="+moRecEndTime
					+",结果数量："+(serMoTaskList==null?null:serMoTaskList.size());
			}
			
			//当前登录操作员对象
			LfSysuser curSysuser = baseBiz.getLfSysuserByUserId(strLguserid);
			List<Userdata> sendUserList = smsBiz.getSpUserList(curSysuser);
			request.setAttribute("sendUserList", sendUserList);
			
			request.setAttribute("serMoTaskList", serMoTaskList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("isFirstEnter", isFirstEnter);
			//List<LfSysuser> sysList = SysuserBiz.getAllSysusers(Long.valueOf(strLguserid));
			//request.setAttribute("sysList", sysList);
			request.setAttribute("pageInfo", pageInfo);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("serMo_conditionMap", conditionMap);
			
			EmpExecutionContext.info("智能引擎", lgcorpcode, strLguserid, lgusername, content, StaticValue.GET);
		
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"上行业务记录查询异常！");
		}finally
		{
			request.getRequestDispatcher(
                    empRoot + "/engine/eng_serMotask.jsp").forward(
					request, response);
		}
	}
	
	/**
	 * 从session获取当前登录操作员对象
	 * @param request 请求对象
	 * @return 返回当前登录操作员对象，为空则返回null
	 */
	private LfSysuser getCurUserInSession(HttpServletRequest request)
	{
		Object loginSysuserObj = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		if(loginSysuserObj == null)
		{
			return null;
		}
		return (LfSysuser)loginSysuserObj;
	}
	
	public void exportExcel(HttpServletRequest request,HttpServletResponse response){
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务记录，导出excel，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		//String lguserid = curUser.getUserId().toString();
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		
		String strLguserid = curUser.getUserId().toString();
		//String lgcorpcode = request.getParameter("lgcorpcode");
		Object object = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("serMo_conditionMap");
		if(StringUtils.isBlank(strLguserid)||StringUtils.isBlank(lgcorpcode)||object == null)
		{
			return;
		}
		LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) object;
		List<LfMoServiceVo> serMoTaskList = new SerMoTaskBiz().getSerMoTaskList(conditionMap, strLguserid, lgcorpcode, null);
	    try
		{
			OutputStream o = response.getOutputStream();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddH24mmss");
			String dataString = sdf.format(new Date());
			String filename = "eng_serMoTaskAllRecord_" + dataString+".zip";
			response.setHeader("Content-disposition", "attachment;filename="
			      + filename);
			response.setContentType("text/html");
			//生成临时文件的路径
			String filepath = excelPath+File.separator+"eng_serMoTaskAllRecord_" + dataString+".xls";
			writeReportExcel(filepath,serMoTaskList, o);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP上行记录导出异常！");
		}
	}
	
	public void ReportMoSerExcel(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
	    try
		{
			// 设置语言
			String langName  = request.getParameter("langName");
	    	//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("上行业务记录，导出业务excel，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
	    	String strLguserid = curUser.getUserId().toString();
	    	//String lgcorpcode = request.getParameter("lgcorpcode");
	    	String lgusername = curUser.getUserName();
	    	
	    	Object object = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("serMo_conditionMap");
	    	if(StringUtils.isBlank(strLguserid)||StringUtils.isBlank(lgcorpcode)||object == null)
	    	{
	    		return;
	    	}
	    	LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>) object;
	    	List<LfMoServiceVo> serMoTaskList = new SerMoTaskBiz().getSerMoTaskList(conditionMap, strLguserid, lgcorpcode, null);
	    	
	    	SerMoExcelTool et = new SerMoExcelTool(excelPath);
			Map<String, String> resultMap = et.createSerMoExcel(langName,serMoTaskList);
			String fileName=(String)resultMap.get("FILE_NAME");
	        String filePath=(String)resultMap.get("FILE_PATH");
	        
	        //操作日志
			EmpExecutionContext.info("智能引擎", lgcorpcode, strLguserid, lgusername, "上行业务记录导出，共导出"+serMoTaskList.size()+"条记录" , "OTHER");
			
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("ReportMoSerExcel", fileName+"@@"+filePath);
	        //用于判断是否下载加载完成了
	        request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("checkOver"+strLguserid, "true");
	        response.getWriter().print("true");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务记录导出异常。");
			response.getWriter().print("false");
		}
	}
	
	/**
	 * excel文件导出
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)   
	{
		try
		{
			String down_session=request.getParameter("down_session");
		    HttpSession session = request.getSession(ZnyqParamValue.GET_SESSION_FALSE);
		    Object obj = session.getAttribute(down_session);
		    if(obj == null)
		    {
		    	EmpExecutionContext.error("上行业务记录导出excel下载，获取不到会话对象。");
		    	return;
		    }
	        String result = (String) obj;
	        if(result.indexOf("@@")>-1)
	        {
	        	String[] file=result.split("@@");
	            // 弹出下载页面。
	            DownloadFile dfs = new DownloadFile();
	            dfs.downFile(request, response, file[1], file[0]);
	            session.removeAttribute(down_session);
	        }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行业务记录导出excel下载，异常");
		}
	}
	
	public void writeReportExcel(String tempFilePath,List<LfMoServiceVo> list,OutputStream o) throws IOException, WriteException{
		if(list ==null || list.size()==0){
			return;
		}
		int intRowsOfPage = 50000;
		File tempFile = null;
		BufferedInputStream bis = null;
		ZipOutputStream out = null;
		try {
		
		tempFile = new File(tempFilePath);
		if(!tempFile.getParentFile().exists()){
			tempFile.getParentFile().mkdirs();
		}
		java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		WritableWorkbook wwb = Workbook.createWorkbook(tempFile);
		WritableSheet ws = null;
		WritableCellFormat format = new WritableCellFormat();
		format.setWrap(true);
		format.setVerticalAlignment(VerticalAlignment.CENTRE);
		format.setAlignment(Alignment.CENTRE);
		
		WritableCellFormat titleFmt = new WritableCellFormat(new WritableFont(WritableFont.TIMES, 12,WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK));
		titleFmt.setAlignment(Alignment.CENTRE);
		for(int i=0;i<list.size();i++){
			if(i%intRowsOfPage==0){
				ws = wwb.createSheet("eng_serMoTaskAllRecord_"+(i/intRowsOfPage+1), i/intRowsOfPage); 
				ws.addCell(new Label(0,0,"上行时间",titleFmt));
				ws.addCell(new Label(1,0,"手机号码",titleFmt));
				ws.addCell(new Label(2,0,"姓名",titleFmt));
				ws.addCell(new Label(3,0,"上行内容",titleFmt));
				ws.addCell(new Label(4,0,"回复状态",titleFmt));
				ws.addCell(new Label(5,0,"业务编号",titleFmt));
				ws.addCell(new Label(6,0,"业务名称",titleFmt));
				ws.addCell(new Label(7,0,"上行指令",titleFmt));
				ws.addCell(new Label(8,0,"创建人",titleFmt));
				ws.addCell(new Label(9,0,"SP账号",titleFmt));
				ws.setColumnView(0, 25);
				ws.setColumnView(1, 20);
				ws.setColumnView(2, 18);
				ws.setColumnView(3, 25);
				ws.setColumnView(6, 18);
				ws.setColumnView(9, 15);
			}
			LfMoServiceVo serMoTask = list.get(i);
			 Integer replyState = serMoTask.getReplyState();
			ws.addCell(new Label(0,(i%intRowsOfPage+1),df.format(serMoTask.getDeliverTime()),format));
			ws.addCell(new Label(1,(i%intRowsOfPage+1),serMoTask.getPhone(),format));
			ws.addCell(new Label(2,(i%intRowsOfPage+1),serMoTask.getClientName()==null?"-":serMoTask.getClientName(),format));
			ws.addCell(new Label(3,(i%intRowsOfPage+1),serMoTask.getMsgContent(),format));
			ws.addCell(new Label(4,(i%intRowsOfPage+1),replyState-1==0?"成功":replyState-3==0?"失败":"未回复",format));
			ws.addCell(new Label(5,(i%intRowsOfPage+1),serMoTask.getSerId()+"",format));
			ws.addCell(new Label(6,(i%intRowsOfPage+1),serMoTask.getSerName(),format));
			ws.addCell(new Label(7,(i%intRowsOfPage+1),serMoTask.getOrderCode(),format));
			ws.addCell(new Label(8,(i%intRowsOfPage+1),serMoTask.getCreaterName(),format));
			ws.addCell(new Label(9,(i%intRowsOfPage+1),serMoTask.getSpUser(),format));
		}
		if(list.size()>0){
			//写入到临时文件
			wwb.write(); 
		}
		//关闭Excel工作薄对象 
		wwb.close();
        bis = new BufferedInputStream(  
                new FileInputStream(tempFile));  
        ZipEntry entry = new ZipEntry(tempFile.getName());  
        out = new ZipOutputStream(o);
        out.putNextEntry(entry);  
        int count;  
        byte data[] = new byte[1024*256];  
        while ((count = bis.read(data, 0, 1024)) != -1) {  
            out.write(data, 0, count);  
        }
    	
		} catch (Exception e) {
			// TODO: handle exception
		}finally{
			if(bis != null){
				 bis.close(); 
			}
			if(out != null){
				 out.finish();
			     out.close();
			}
		    //删除临时文件
		    if(tempFile.exists()){
                boolean delete = tempFile.delete();
                if (!delete) {
                    EmpExecutionContext.error("文件删除失败！");
                }
            }
		}
       
	}
}