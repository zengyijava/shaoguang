package com.montnets.emp.common.servlet;

import java.io.File;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.util.TxtFileUtil;

public class InitServlet extends HttpServlet
{

	private static final long serialVersionUID = -1614005437488217583L;
	
	/**
	 * 初使化方法
	 */
	public void init() throws ServletException {
		//初使化下载模板
		initAddrExcelTem();
		//初使化机构级别
		updateDepPath();
		//还原初始存储过程执行状态
		initProcedurestatus();
		//初始化短信分批设置
		initSplitSMS();
	}
	
	/**
	 * 初始化短信分批设置
	 */
    private void initSplitSMS() {
    	String splitConfig = SystemGlobals.getValue("emp.sms.split");
    	if(splitConfig != null && !"1".equals(splitConfig.trim())){
    		splitConfig = "0";
    	}else{
    		splitConfig = "1";
    	}
    	try {
			new SuperDAO().executeBySQL("UPDATE LF_CORP_CONF SET PARAM_VALUE = '"+splitConfig+"' WHERE PARAM_KEY = 'sms.split'", StaticValue.EMP_POOLNAME);
		} catch (Exception e) {
			EmpExecutionContext.info("初始化短信分批设置异常");
		}
	}

	/**
     * 摧毁方法
     */
	public void destroy() {
		super.destroy();
	}
	
	
	/**
	 * 通讯录上传模板初始化
	 */
	public void initAddrExcelTem()
	{
		TxtFileUtil txtFile =new TxtFileUtil();
		String xslPath = txtFile.getWebRoot()+StaticValue.FILE_UPLOAD_PATH;
		File employeeFile = new File(xslPath+"/employeeTem.xls");
		if(!employeeFile.exists())
		{
			String uploadPath = this.getServletContext().getRealPath("/").toString()+StaticValue.FILE_UPLOAD_PATH ;
			txtFile.copyFile(uploadPath+"/employeeTem.xls", xslPath+"/employeeTem.xls");
		}
		File clientFile = new File(xslPath+"/clientTem.xls");
		if(!clientFile.exists())
		{
			String uploadPath = this.getServletContext().getRealPath("/").toString()+StaticValue.FILE_UPLOAD_PATH ;

			txtFile.copyFile(uploadPath+"/clientTem.xls", xslPath+"/clientTem.xls");
		}
		File contactFile = new File(xslPath+"/contactTem.xls");
		if(!contactFile.exists())
		{
			String uploadPath = this.getServletContext().getRealPath("/").toString()+StaticValue.FILE_UPLOAD_PATH ;
			txtFile.copyFile(uploadPath+"/contactTem.xls", xslPath+"/contactTem.xls");
		}
	}
	//机构级别处理
	 
	public void updateDepPath(){
		DepDAO depDao=new DepDAO();
		depDao.updateLfDepPath();
		depDao.updateLfEmployeeDepPath();
		depDao.updateLfClientDepPath();
	}
	
	//存储过程执行状态  初始化
	public void initProcedurestatus(){
		String isHdataFilish = SystemGlobals.getSysParam("isHdataFilish"); 
		if(!"0".equals(isHdataFilish)){
			//设置汇总执行完成状态
			SystemGlobals.setSysParam("isHdataFilish", "0");
			EmpExecutionContext.info("调度汇总isHdataFilish执行状态未还原");
		}
		
		String isSummFilish = SystemGlobals.getSysParam("isSummFilish"); 
		if(!"0".equals(isSummFilish)){
			//设置白天短信汇总执行完成状态
			SystemGlobals.setSysParam("isSummFilish", "0");
			EmpExecutionContext.info("EMP白天汇总isSummFilish执行状态未还原");
		}
		
		String ismSummFilish = SystemGlobals.getSysParam("ismSummFilish"); 
		if(!"0".equals(ismSummFilish)){
			//设置白天彩信汇总执行完成状态
			SystemGlobals.setSysParam("ismSummFilish", "0");
			EmpExecutionContext.info("EMP白天彩信汇总ismSummFilish执行状态未还原");
		}
		
		String isAppSummFilish = SystemGlobals.getSysParam("isAppSummFilish"); 
		if(!"0".equals(isAppSummFilish)){
			//设置白天app汇总执行完成状态
			SystemGlobals.setSysParam("isAppSummFilish", "0");
			EmpExecutionContext.info("EMPAPP汇总isAppSummFilish执行状态未还原");
		}
		
		try
		{
			String smscountstr=new SuperDAO().getString("count", "select count(*) count from lf_taskreport", StaticValue.EMP_POOLNAME);
			if(!"0".equals(smscountstr)){
				new SuperDAO().executeBySQL("delete from lf_taskreport", StaticValue.EMP_POOLNAME);
				EmpExecutionContext.info("短信汇总lf_taskreport 存在残余数据");
			}
			
			String mmscountstr=new SuperDAO().getString("count", "select count(*) count from lf_mmstaskreport", StaticValue.EMP_POOLNAME);
			if(!"0".equals(mmscountstr)){
				new SuperDAO().executeBySQL("delete from lf_mmstaskreport", StaticValue.EMP_POOLNAME);
				EmpExecutionContext.info("彩信汇总lf_mmstaskreport 存在残余数据");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"还原中间表异常");
		}

	}
	
	
	
}
