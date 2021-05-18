package com.montnets.emp.finance.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.finance.biz.ElecPayrollCommon;
import com.montnets.emp.finance.biz.YdcwBiz;

/**
 * 报销提醒
 * 
 * @author Jinny.Ding (djhsun@sina.com)
 * @version 1.0
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011/10/25
 * 
 */
@SuppressWarnings("serial")
public class ycw_reimbursementRemindSvt extends YdcwBaseServlet {
	//业务代码
	private static final String BUSCODE = "MF0002";
	private static final String _PAYROLL = "报销提醒";

	/**
	 * 报销提醒
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		ElecPayrollCommon epcObject = new ElecPayrollCommon();
		YdcwBiz ydcwBiz = new YdcwBiz();
		try {
			epcObject.clearSession(session);
			
			//当前登录账号的企业编码
			String corpCode = request.getParameter("lgcorpcode");
			//当前登录账号的guid
			//String userIdStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userIdStr = SysuserUtil.strLguserid(request);
			//获取短信模板 
			List<LfTemplate> lfTemplateList = ydcwBiz.getTemplatesList(corpCode, userIdStr, BUSCODE);
			request.setAttribute("tempsList",lfTemplateList);
			
			//当前登录账号的guid
			Long userId = Long.parseLong(userIdStr);
				
			LfSysuser sysuser = new BaseBiz().getById(LfSysuser.class, userId);
			//获取 发送帐号 [操作员绑定-->机构绑定-->全局发送账号]
			List<Userdata> spUserList =  new SmsBiz().getSpUserList(sysuser);
			request.setAttribute("spUserList", spUserList);
			//获取业务类型
			List<LfBusManager> busList = ydcwBiz.getBusManagerList(corpCode);
			request.setAttribute("busList", busList);
			//报销提醒发送
			request.setAttribute("businessType", _PAYROLL);
			//获取taksid
			Long taskId = null;
			if(session.getAttribute("taskId") == null){
				taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			}else{
				taskId = Long.valueOf((String)session.getAttribute("taskId"));
			}
			request.setAttribute("taskId",taskId.toString());
			// 跳转至电子工资单页
			request.getRequestDispatcher(PATH + "/ycw_reimbursementRemind.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.debug("Error code:E-CW-V104");
			EmpExecutionContext.error(e, "报销提醒跳转页面异常!");
		}
	}

/**
	 * 导入文件数据 预览(支持 txt 和 xls)
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void filePreview(HttpServletRequest request,
			HttpServletResponse response){
		super.filePreview(request, response);
	}

	/**
	 * 电子工资单短信发送
	 * @param request
	 * @param response
	 */
	public void send(HttpServletRequest request, HttpServletResponse response) {
		String lgcorpcode = request.getParameter("lgcorpcode");
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		String returnStr = "";
		//判断是否使用集群
		if(StaticValue.getISCLUSTER() ==1 )
		{
			HttpSession session = request.getSession(false);
			LfMttask mttask = (LfMttask) session.getAttribute("taskObj");
			CommonBiz commBiz = new CommonBiz();
			//上传文件到文件服务器
			if("success".equals(commBiz.uploadFileToFileCenter(mttask.getMobileUrl())))
			{
				//删除本地文件
				//commBiz.deleteFile(mttask.getMobileUrl());
			}else
			{
				returnStr = "上传号码文件失败，取消任务创建！";
			}
		}
		if("".equals(returnStr)){
			returnStr = super.add(request, response,_PAYROLL);
		}
		request.getSession(false).setAttribute("ycw_result", returnStr);
		// 发送完成后跳转到原来的页面
		String s = request.getRequestURI();
		s = s+"?method=find&lguserid="+lguserid+"&lgcorpcode="+lgcorpcode;
		try {
			response.sendRedirect(s);
		} catch (IOException e) {
			EmpExecutionContext.error(e, "电子工资单短信发送重定向异常!");
		}
	}
	
	/**
	 * 手工录入数据预览
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public void manualPreview(HttpServletRequest request,
			HttpServletResponse response){
		super.preview(request, response);
	}
	
	

}
