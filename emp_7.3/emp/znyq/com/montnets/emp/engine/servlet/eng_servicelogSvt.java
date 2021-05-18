package com.montnets.emp.engine.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.AppLogBiz;
import com.montnets.emp.engine.vo.LfServicelogVo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * 
 * @project emp
 * @author
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 上午10:33:27
 * @description
 */
@SuppressWarnings("serial")
public class eng_servicelogSvt extends BaseServlet
{

	private final AppLogBiz serBiz = new AppLogBiz();
	private final SysuserBiz SysuserBiz = new SysuserBiz();
	private final String empRoot = "znyq";
	
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LfServicelogVo serVo = new LfServicelogVo();
		PageInfo pageInfo = new PageInfo();
		List<LfSysuser> sysList = null;
		List<LfServicelogVo> serList = null;
		boolean isFirstEnter;
		try {
			isFirstEnter = pageSet(pageInfo,request);
			String content = "业务服务日志查询。";
			if (!isFirstEnter)
			{
				//服务名
				String serName = request.getParameter("serName");
				//提交人
				String subUserName = request.getParameter("subUserName");
				//日志开始时间
				String logStartTime = request.getParameter("logStartTime");
				//结束时间
				String logEndTime = request.getParameter("logEndTime");
				//设置服务名
				serVo.setSerName(serName);
				//设置提交人
				serVo.setName(subUserName);
				//设置日志开始时间
				serVo.setStartSubmitTime(logStartTime);
				//设置结束时间
				serVo.setEndSubmitTime(logEndTime);
				
				content += "条件serName="+serName
				+",subUserName="+subUserName
				+",logStartTime="+logStartTime
				+",logEndTime="+logEndTime
				;
			}
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("业务服务日志，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			//String lgcorpcode = request.getParameter("lgcorpcode");
			String lgusername = curUser.getUserName();
			// 当前登录操作员id
			Long lguserid = null;
			String userid = curUser.getUserId().toString();
			if(userid != null && userid.length() > 0){
				lguserid = Long.valueOf(userid);
			}else{
				LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
				lguserid = sysuser.getUserId();
			}
			
			sysList = SysuserBiz.getAllSysusers(lguserid);
			request.setAttribute("sysList", sysList);
			serList = serBiz.getSerLogVos(lguserid, serVo,
					pageInfo);
			
			content += ",结果数量："+(serList==null?null:serList.size());
			EmpExecutionContext.info("智能引擎", lgcorpcode, userid, lgusername, content, StaticValue.GET);
			
			request.setAttribute("serVo", serVo);
			request.setAttribute("serList", serList);
	
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询业务日志异常！");
		}finally
		{
			request.setAttribute("serVo", serVo);
			request.setAttribute("serList", serList);
			request.setAttribute("pageInfo", pageInfo);
			request.getRequestDispatcher(
					this.empRoot + "/engine/eng_servicelog.jsp").forward(
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

	
}