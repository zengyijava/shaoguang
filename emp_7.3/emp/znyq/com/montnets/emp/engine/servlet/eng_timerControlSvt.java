package com.montnets.emp.engine.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.timer.TaskManagerBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.FetchSendMsgBiz;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfSysuser;

@SuppressWarnings("serial")
public class eng_timerControlSvt extends BaseServlet
{
	private final BaseBiz baseBiz = new BaseBiz();
	private final String empRoot = "znyq";
	private final String moduleName = "下行业务监控";
	public void find(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		List<LfTimer> timerList = null;
		FetchSendMsgBiz serBiz = new FetchSendMsgBiz();
		PageInfo pageInfo = new PageInfo();
		try {
			//设置分页
			pageSet(pageInfo,request);
			LfTimer lfTimer = new LfTimer();
			lfTimer.setTaskExpression("ser|");
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务监控，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			String lgcorpcode = curUser.getCorpCode();
			
			String lgusername = curUser.getUserName();
			
			// 当前登录操作员id
			//String lguserid = request.getParameter("lguserid");
			//String lgcorpcode = request.getParameter("lgcorpcode");
			//String lgusername = request.getParameter("lgusername");
			timerList = serBiz.getTimerVos(lfTimer, Long.valueOf(lguserid), pageInfo);
			
			String content = "下行业务监控查询。" 
				+",结果数量："+(timerList==null?null:timerList.size());
		
			EmpExecutionContext.info("智能引擎", lgcorpcode, lguserid, lgusername, content, StaticValue.GET);
			
		} catch (Exception e)
		{
			//保存和打印异常信息
			EmpExecutionContext.error(e,"查询定时监控信息异常！");
		}
		request.setAttribute("timerList", timerList);
		//分页信息
		request.setAttribute("pageInfo", pageInfo);
		//页面跳转
		request.getRequestDispatcher(
				this.empRoot + "/engine/eng_timerControl.jsp").forward(request,
				response);
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

	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//日志对象
		SuperOpLog spLog = new SuperOpLog();
		//模块
		String opModule = StaticValue.MMS_BOX;
		//操作员
		String opSper = StaticValue.OPSPER;
		//操作类型
		String opType = StaticValue.DELETE;
		//任务名称
		String taskName = request.getParameter("taskName");
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("下行业务监控，删除，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		String lguserid = curUser.getUserId().toString();
		// 当前登录企业
		String lgcorpcode = curUser.getCorpCode();
		
		// 当前登录企业
		//String lgcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");

		//操作内容
		String opContent = "删除（业务名称：" + taskName + "）下行业务定时监控任务";

		String opUser = "";
		try
		{
			//任务id
			String serId = request.getParameter("taskId");

			TaskManagerBiz tmBiz = new TaskManagerBiz();
			//删除任务
			Integer count = tmBiz.delTaskByExpression(serId);
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			opUser = sysuser==null?"":sysuser.getUserName();
			//操作结果
			String opResult = "";
			if (count > 0)
			{
				//成功日志
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				response.getWriter().print(true);
				opResult = "删除下行业务定时监控任务成功。";
			} else
			{
				//失败日志
				spLog.logFailureString(opUser, opModule, opType, opContent,
						null, lgcorpcode);
				response.getWriter().print(false);
				opResult = "删除下行业务定时监控任务失败。";
			}
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务id，业务名称](").append(serId).append("，").append(taskName).append(")");
			//操作日志
			EmpExecutionContext.info(moduleName, lgcorpcode, lguserid, opUser, opResult + content.toString(), opType);
		} catch (Exception e)
		{
			//异步打印结果
			response.getWriter().print(false);
			spLog.logFailureString(opUser, opModule, opType,
					opContent + opSper, e, lgcorpcode);
			EmpExecutionContext.error(e,"删除（业务名称：" + taskName + 
					"）下行业务定时监控任务异常！ ");
		}
	}
}
