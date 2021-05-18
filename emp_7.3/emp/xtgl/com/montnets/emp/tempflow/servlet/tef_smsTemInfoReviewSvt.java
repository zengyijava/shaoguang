package com.montnets.emp.tempflow.servlet;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;

import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.tempflow.biz.TempReviewBiz;
import com.montnets.emp.template.atom.CreateTmsFile;
import com.montnets.emp.template.biz.TemplateBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.i18n.util.MessageUtils;

@SuppressWarnings("serial")
public class tef_smsTemInfoReviewSvt extends BaseServlet {
	private final String opModule="模板审批";
	private final String opSper = StaticValue.OPSPER;
	//模板biz
	private final TemplateBiz mtb = new TemplateBiz();
	private final TempReviewBiz tempReviewBiz=new TempReviewBiz();
	
	private final String empRoot="xtgl";
	
	private final String basePath="/tempflow";
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();
	

	/**
	 * 模板审批
	 */
	@SuppressWarnings("unchecked")
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		
		//登录用户id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		PageInfo pageInfo=new PageInfo();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap2 = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		orderByMap.put("frId", StaticValue.DESC);
		try {
			//是否第一次打开
			boolean isFirstEnter = false;
            HttpSession session = request.getSession(false);
			//操作页面跳转
			String isOperateReturn = request.getParameter("isOperateReturn");
			if("true".equals(isOperateReturn))
			{
				pageInfo = (PageInfo) session.getAttribute("tef_smsPageInfo");
				conditionMap = (LinkedHashMap<String, String>)session.getAttribute("tef_smsCondition");
			}
			else
			{
				isFirstEnter=pageSet(pageInfo, request);
				//模板名称
				String tmName = request.getParameter("tmName");
				//模版类型
				String dsflag = request.getParameter("dsflag");
		
				
				//查询的是短信还是彩信模板
				String infoType = request.getParameter("infoType");
				conditionMap.put("infoType&in", "3,4");
				//不是第一次查询
				if (!isFirstEnter)
				{
					if (tmName != null && !"".equals(tmName))
					{
						conditionMap.put("RContent&like", tmName);
					}
					if (dsflag != null && !"".equals(dsflag))
					{
						conditionMap.put("reviewType", dsflag);
					}
					if (infoType != null || !"".equals(infoType))
					{
						conditionMap.put("infoType", infoType);
					}
					
				}
				conditionMap.put("userCode",lguserid);
				session.setAttribute("tef_smsPageInfo", pageInfo);
                session.setAttribute("tef_smsCondition", conditionMap);
			}
			
			//审核流程记录list
			List<LfFlowRecord> recordList = baseBiz.getByConditionNoCount(LfFlowRecord.class, null, conditionMap, orderByMap, pageInfo);
			StringBuffer sb = new StringBuffer();
			StringBuffer sb2 = new StringBuffer();
			if(recordList != null && recordList.size()>0){
				for(LfFlowRecord temp:recordList){
					sb2.append(temp.getMtId()).append(",");
					sb.append(temp.getProUserCode()).append(",");
				}
			}
			//从session中获取LfSysuser
			LfSysuser sysuser = getLoginUser(request);
			//当前登录企业编码
			String corpCode = (sysuser != null && sysuser.getCorpCode()!= null)?sysuser.getCorpCode():"";
			List<LfSysuser> userList = null;
			LinkedHashMap<Long,String> usernameMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				conditionMap2.put("userId&in", sb.toString());
				conditionMap2.put("corpCode", corpCode);
				userList = baseBiz.getByCondition(LfSysuser.class, conditionMap2, null);
				if(userList != null && userList.size()>0){
					for(LfSysuser user:userList){
						String name =  user.getName();
						if(user.getUserState() != null && user.getUserState() ==2){
							name = name + "<font color='red'>("+MessageUtils.extractMessage("xtgl", "xtgl_cswh_dxmbgl_yzx", request)+")</font>";
						}
						usernameMap.put(user.getUserId(),name);
					}
				}
			}
			List<LfTemplate> tempList = null;
			LinkedHashMap<Long,String> tempMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				conditionMap2.clear();
				conditionMap2.put("tmid&in", sb2.toString());
				conditionMap2.put("corpCode", corpCode);
				tempList = baseBiz.getByCondition(LfTemplate.class, conditionMap2, null);
				if(tempList != null && tempList.size()>0){
					for(LfTemplate tmp:tempList){
						String msg =  tmp.getTmMsg();
						tempMap.put(tmp.getTmid(),msg);
					}
				}
			}
			 //获取系统定义的短彩类型值
			List<LfPageField> pagefileds = new GlobConfigBiz().getPageFieldById("100001");
			request.setAttribute("pagefileds", pagefileds);
			request.setAttribute("tempMap", tempMap);
			request.setAttribute("usernameMap", usernameMap);
			request.setAttribute("recordList", recordList);
			request.setAttribute("conditionMap", conditionMap);
			//分页信息传给页面
			request.setAttribute("pageInfo", pageInfo);
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "模板审批", opContent, "GET");
			}
			
			//页面跳转
			request.getRequestDispatcher(this.empRoot  + this.basePath +"/tef_smsTemInfoReview.jsp")
			.forward(request, response);
		} catch (Exception e) {
			//异常打印
			EmpExecutionContext.error(e,"模板审批跳转出现异常！");
			//错误信息传给页面
			request.setAttribute("findresult", "-1");
			//分页信息传给页面
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot  + this.basePath +"/tef_smsTemInfoReview.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"模板审批servlet查询异常");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"模板审批servlet查询跳转异常");
			}
		}
	}

	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//删除日志
        String opType = StaticValue.DELETE;
      //日志内容
		String opContent = "删除彩信模板";
		String lgcorpcode=request.getParameter("lgcorpcode");
		lgcorpcode=lgcorpcode==null?"":lgcorpcode;
		String opUser = "";
		try
		{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();
			String idsString = request.getParameter("ids");
			String[] ids =idsString.split(",");
			//调用删除方法并返回操作成功数
			int count = mtb.delTempByTmId(ids);
			//日志内容
			opContent = "删除"+ids.length+"条彩信模板";
			//将信息返回给页面
			response.getWriter().print(count);
			opSucLog(request, opModule, "删除模板（"+ids.length+"条）（模板ID:"+idsString+"）成功。","DELETE");
			//保存日志
			spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		} catch (Exception e)
		{
			response.getWriter().print(0);
			//保存异常日志
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"删除模板出现异常！");
		}
	}
	

	/**
	 * 根据id查找模板内容
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getTmMsg(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//模板id
		String tmId=request.getParameter("tmId");
		try
		{
			//如果模板id不为空
			if("".equals(tmId))
			{
				response.getWriter().print("");
			}else
			{
				//如果模板id不为空
				LfTemplate template = baseBiz.getById(LfTemplate.class, tmId);
				response.getWriter().print(template.getTmMsg());
			}
		}catch (Exception e)
		{
			response.getWriter().print("");
			//异常打印
			EmpExecutionContext.error(e,"查找模板内容出现异常！");
		}
	}
	

	/**
	 * 审批操作
	 * @param request
	 * @param response
	 */
	public void review(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//模板id
			String frId = request.getParameter("frId");
			//审批意见
			String cont = request.getParameter("cont");
			//状态
			String rState = request.getParameter("rState");
			if (frId != null && !"".equals(frId))
			{
				//审批，并返回结果
				boolean result = tempReviewBiz.reviewTemplate(Long.parseLong(frId), Integer.parseInt(rState), cont);
				response.getWriter().print(result);
			}else
			{
				response.getWriter().print("false");
			}
			opSucLog(request,opModule,"短信模板（ID:"+frId+"，审批状态:"+rState+"）审批成功。", "OTHER");
		} catch (Exception e)
		{
			//异常打印
			EmpExecutionContext.error(e,"审批模板出现异常！");
		}
	}
	
	/**
	 * @param request
	 * @param response
	 */
	public void delSource(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//路径
			String sourceUrl = request.getParameter("sourceUrl");
			if (sourceUrl != null && !"".equals(sourceUrl) && sourceUrl.indexOf("mmsTemplate") < 0)
			{
				File f = new File(this.getServletContext().getRealPath("/") + sourceUrl);
				 if (!f.isDirectory()) 
				 {
					//删除文件
					 boolean r = f.delete();
					 if (r)
					 {
						//操作成功
						 response.getWriter().print("true");
					 }
				 }
			}
			else if (sourceUrl != null && !"".equals(sourceUrl) && sourceUrl.indexOf("mmsTemplate") >= 0)
			{
				//将成功操作结果返回给页面
				response.getWriter().print("true");
			}
		}
		catch (Exception e)
		{
			//异常打印
			EmpExecutionContext.error(e,"删除模板出现异常！");
		}
	}
	

	/**
	 * 审批/查看模板
	 * @param request
	 * @param response
	 */
	public void getReviewInfo(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//模板id
		String tmId = request.getParameter("tmId");
		//流程id
		String fId = request.getParameter("fId");
		try
		{
			//根据模板id查询模板信息
			LfTemplate temp = baseBiz.getById(LfTemplate.class,tmId);
			//模板类型
			String ttype = "通用静态模块";
			if ("0".equals(temp.getDsflag()+""))
			{
				ttype = "通用静态模块";
			}
			else if ("1".equals(temp.getDsflag()+""))
			{
				ttype = "通用动态模块";
			}
			else if ("2".equals(temp.getDsflag()+""))
			{
				ttype = "智能抓取模块";
			}
			else if ("3".equals(temp.getDsflag()+""))
			{
				ttype = "移动财务模块";
				if ("MF0001".equals(temp.getBizCode()))
				{
					ttype = ttype + "(电子工资单)";
				}
				else if ("MF0002".equals(temp.getBizCode()))
				{
					ttype = ttype + "(报销提醒)";
				}
				else if ("MF0003".equals(temp.getBizCode()))
				{
					ttype = ttype + "(回款通知)";
				}
			}
			//将信息写到页面
			response.getWriter().print("<input type='hidden' id='tmmsId' value='"+fId+"'/>"+
				"<table width='100%'>"+
				"<tr><td width='370px' height='30'>　模板名称：<label id='stmName'>"+temp.getTmName().replace("<","&lt;").replace(">","&gt;")+"</label></td></tr>"+
				"<tr><td width='370px' height='30'>　模板类型：<label id='stmType'>"+ttype+"</label></td></tr>"+
				"<tr><td width='370px' height='30' style='vertical-align: top;line-height:24px;word-break:break-all;word-wrap: break-word;'>　" +
				"模板内容：<textarea style='width:260px;height:100px;'" +
				" readonly='readonly' class='input_bd'>"+temp.getTmMsg()+"</textarea></td></tr>"+
				"<tr><td width='370px' valign='top'>　审批意见：<textarea style='width:260px;height:100px' id='cont' class='input_bd'></textarea></td></tr>"+
				"<tr><td width='370px' style='padding-top:8px;' align='center'>　<input type='button' class='btnClass5' value='同意' id='ok3' onclick='javascript:review(1,3)'/>" +
				"&nbsp;&nbsp;&nbsp;<input type='button' class='btnClass6' value='拒绝' id='rj3' onclick='javascript:review(2,3)'/></td></tr>"+
				"</table>");
		}catch (Exception e)
		{
			//异常打印
			EmpExecutionContext.error(e,"审批/查看模板出现异常！");
			response.getWriter().print("获取模板信息错误！");
		}
	}
	
	/**
	 * 点击审批/查看操作
	 * @param request
	 * @param response
	 */
	public void getExamineInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception 
	{
		try 
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			// 审核流程id
			String frid = request.getParameter("frid");
			// 审核流程信息
			LfFlowRecord record = baseBiz.getById(LfFlowRecord.class, frid);
			// 模板对象
			LfTemplate temp = baseBiz.getById(LfTemplate.class, record.getMtId());
			
			LfSysuser lfsysuser = baseBiz.getById(LfSysuser.class, record.getUserCode());

			int level = record.getRLevel();
			conditionMap.put("RLevel&<", String.valueOf(level + 1));
			// 审核通过或审核不通过
			conditionMap.put("RState&in", "1,2");
			// 相同审核流程
			conditionMap.put("FId", record.getFId().toString());
			// 相同任务
			conditionMap.put("mtId", record.getMtId().toString());
			// 等级逆序排序
			orderbyMap.put("RLevel", StaticValue.DESC);
			//审批历史记录
			List<LfFlowRecord> recordList = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderbyMap);
			
			StringBuffer sb = new StringBuffer();
			sb.append(record.getProUserCode()).append(",");
			if(recordList != null && recordList.size()>0){
				for(LfFlowRecord re:recordList){
					sb.append(re.getUserCode()).append(",");
				}
			}
			List<LfSysuser> userList = null;
			LinkedHashMap<Long,String> usernameMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				conditionMap.clear();
				conditionMap.put("userId&in", sb.toString());
				userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				if(userList != null && userList.size()>0){
					for(LfSysuser user:userList){
						String name =  user.getName();
						if(user.getUserState() != null && user.getUserState() ==2){
							name = name + "<font color='red'>(已注销)</font>";
						}
						usernameMap.put(user.getUserId(),name);
					}
				}
			}
			
			
			request.setAttribute("usernameMap", usernameMap);
			request.setAttribute("curRecord", record);
			request.setAttribute("temp", temp);
			request.setAttribute("recordList", recordList);
			request.setAttribute("lfsysuser", lfsysuser);
			request.getRequestDispatcher(
					this.empRoot + this.basePath + "/tef_examine.jsp")
					.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"审批/查看出现异常！");
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
			EmpExecutionContext.error(e, "记录操作日志异常，session为空!");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
