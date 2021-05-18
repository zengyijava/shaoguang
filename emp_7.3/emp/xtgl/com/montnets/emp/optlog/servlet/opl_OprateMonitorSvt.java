package com.montnets.emp.optlog.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfOpratelog;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.optlog.biz.OptLogBiz;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * 
 * @project emp
 * @author Sam Wang 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */

@SuppressWarnings("serial")
public class opl_OprateMonitorSvt extends BaseServlet {

	private final String empRoot="xtgl";
	private final String basePath="/optlog";
	private final BaseBiz baseBiz=new BaseBiz();
	
	/**
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String guid = request.getParameter("lgguid");
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		PageInfo pageInfo=new PageInfo();
		//加请求日志
		EmpExecutionContext.logRequestUrl(request, "后台请求");
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);
			if (!isFirstEnter) {
				//起始操作时间
				conditionMap.put("opSendtime", request.getParameter("sendtime"));
				//截止操作时间
				conditionMap.put("opRecvtime", request.getParameter("recvtime"));
				//操作类型
				conditionMap.put("opAction", request.getParameter("opAction"));
				//conditionMap.put("opModule", request.getParameter("opModule"));
				//操作结果
				conditionMap.put("opResult", request.getParameter("opResult"));
				//操作用户
				conditionMap.put("opUser",request.getParameter("opUser"));
				//操作内容
				String opContent =  request.getParameter("opContent");
				if(opContent != null && !"".equals(opContent)){
					opContent = opContent.replace("'", "");
				}
				conditionMap.put("opContent", opContent);
			}
			LfSysuser sysuser = baseBiz.getByGuId(LfSysuser.class,Long.parseLong(guid));
			List<LfOpratelog> logsList = new OptLogBiz().getUserOpratelog(sysuser, conditionMap, pageInfo);;
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("logsList", logsList);
			request.setAttribute("conditionMap", conditionMap);
			request.setAttribute("beforeTime", (String)request.getParameter("sendtime"));
			request.setAttribute("afterTime", (String)request.getParameter("recvtime"));
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "操作日志", opContent, "GET");
			}
			request.getRequestDispatcher(this.empRoot + basePath + "/opl_oprateMonitor.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"操作日志跳转失败！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			try {
				request.getRequestDispatcher(this.empRoot + basePath+"/opl_oprateMonitor.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"操作日志servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"操作日志servlet查询跳转异常");
			}
		}
	}
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
