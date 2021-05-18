package com.montnets.emp.msgflow.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.MttaskBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfFlowRecordVo;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.msgflow.biz.msgFlowReviewBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class msg_mmsInfoReviewSvt extends BaseServlet {
	private final msgFlowReviewBiz mrb = new msgFlowReviewBiz();
	private final TxtFileUtil tfu = new TxtFileUtil();
	private final SysuserBiz SysuserBiz = new SysuserBiz();
	private static final String PATH = "/xtgl/msgflow";

	/**
	 * 彩信审批
	 * @param request
	 * @param response
	 */
	//进入查询页面
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfFlowRecordVo> mmsList = null;
		LfFlowRecordVo recordVo = new LfFlowRecordVo();
		PageInfo pageInfo=new PageInfo();
		try {
			//是否第一次打开
			pageSet(pageInfo, request);
			//标题
			String taskName = request.getParameter("taskName");
			//主题
		    String title = request.getParameter("title");
		    //发送者
		    String sender = request.getParameter("sender");
		    //时间段
		    String stime = request.getParameter("startSubmitTime");
		    String etime = request.getParameter("endSubmitTime");
		    //审批状态
		    String state = request.getParameter("state");
		    //操作员用户ID
		   // String userid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userid = SysuserUtil.strLguserid(request);

			if (taskName != null && !"".equals(taskName))
		    {
		    	recordVo.setTaskName(taskName);
		    }
		    if (title != null && !"".equals(title))
		    {
		    	recordVo.setTitle(title);
		    }
		    if (sender != null && !"".equals(sender))
		    {
		    	recordVo.setUserName(sender);
		    }
		    if (stime != null && !"".equals(stime))
		    {
		    	recordVo.setStartSubmitTime(stime);
		    }
		    if (etime != null && !"".equals(etime))
		    {
		    	recordVo.setEndSubmitTime(etime);
		    }
		    if (state != null && !"".equals(state))
		    {
		    	recordVo.setRState(Integer.parseInt(state));
		    }
			recordVo.setReviewType(2);
			//获取审批流程列表
            mmsList = mrb.getFlowRecordVos(Long.parseLong(userid), recordVo, pageInfo);
            //获取所有的操作员
            List<LfSysuser> sysList = null;
			sysList = SysuserBiz.getAllSysusers(Long.parseLong(userid));
			request.getSession(false).setAttribute("sysList",sysList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("recordVo", recordVo);
			request.setAttribute("mmsList", mmsList);
			request.getRequestDispatcher(PATH+"/ir_mmsInfoReview.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"信息审批页面跳转出现异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("recordVo", recordVo);
			try {
				request.getRequestDispatcher(PATH+"/mms/ir_mmsInfoReview.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"彩信审批servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"彩信审批servlet查询跳转异常");
			}
		}
	}


	/**
	 * 验证文件是否存在的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkFiles(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			//获取文件地址
			String fileUrl = request.getParameter("url");
			//验证文件是否存在
			boolean r = checkFile(fileUrl);
			response.getWriter().print(r);
		}
		catch (Exception e)
		{
			response.getWriter().print(ERROR);
			EmpExecutionContext.error(e,"验证文件是否存在出现异常！");
		}
	}
	
	/**
	 * 检查文件是存储在
	 * @param fileUrl
	 * @return
	 */
	protected boolean checkFile(String fileUrl)
	{
		//结果
		boolean result = false;
		try
		{
			//检查文件是否存在并返回结果
			result = tfu.checkFile(fileUrl);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"检查文件是否存在出现异常！");
		}
		//返回结果
		return result;
	}
	
	/**
	 * 获取当前系统时间
	 * @throws IOException 
	 */
	public void getServerTime(HttpServletRequest request, HttpServletResponse response) throws IOException{
		try
		{
			//获取服务器时间 
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date();
			String serverTime = format.format(date);
			response.getWriter().print(serverTime);
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"获取当前系统时间出现异常！");
		}
	
	}
	

	/**
	 * 点审批/查看操作方法
	 */
	public void getExamineInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//任务ID
		String mtId=request.getParameter("mtId");
		//审批级别
		String RLevel=request.getParameter("RLevel");
		
		List<LfFlowRecordVo> frVoList = new ArrayList<LfFlowRecordVo>();
		try
		{
			//审批列表
			frVoList=mrb.getFlowRecordVos(mtId,RLevel,"2");
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"审批/查看信息审批出现异常！");
		}finally
		{
			request.setAttribute("frVoList", frVoList);
			request.getRequestDispatcher(PATH+"/msg_mmsExamine.jsp")
				.forward(request, response);
		}
	}
}
