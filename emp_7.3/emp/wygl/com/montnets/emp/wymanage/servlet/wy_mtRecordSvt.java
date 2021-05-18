package com.montnets.emp.wymanage.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wyquery.biz.QueryBiz;
import com.montnets.emp.wyquery.biz.SysMoRealTimeRecordBiz;
import com.montnets.emp.wyquery.biz.SysMtHistoryRecordBiz;
import com.montnets.emp.wyquery.biz.SysMtRealTimeRecordBiz;
import com.montnets.emp.wyquery.vo.SysMoMtSpgateVo;
import com.montnets.emp.wyquery.vo.SystemMtTask01_12Vo;
import com.montnets.emp.wyquery.vo.SystemMtTaskVo;

/***
 * 网优下行记录
 * @project p_wygl
 * @author may
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-3-24 下午02:01:08
 * @description
 */

public class wy_mtRecordSvt extends BaseServlet
{
	private final BaseBiz	baseBiz		= new BaseBiz();

	private final int		corpType	= StaticValue.getCORPTYPE();

	private final String	empRoot		= "wygl";

	private final String	base		= "/wyquery";
	


	/**
	 * 网优下行记录查询方法
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 记录类型
		String recordType = request.getParameter("recordType");
		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 当前登录用户的userid
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//账户
		String userid=request.getParameter("userid");
		//通道号
		String spgate=request.getParameter("spgate");
		//开始时间
		String sendtime=request.getParameter("sendtime");
		//结束时间
		String recvtime=request.getParameter("recvtime");
		//电话号码
		String phone=request.getParameter("phone");
		// 操作员编码
		String usercode = "";
		//任务批次
		String staskid = request.getParameter("taskid");
		
		String gateName="";
		//reportStatus
		String reportStatus=request.getParameter("reportStatus");
		// 号码类型
		String numberStyle = request.getParameter("numberStyle");
		
		// 发送账户
		String spUsers = "";
		// 查询条件
		LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
		// 企业绑定SP帐号list
		List<String> lfsp = null;
		//获得用户编号
		try
		{
			LfSysuser user=baseBiz.getLfSysuserByUserId(lguserid);
			if(user!=null){
				if("1".equals(user.getPermissionType()+"")){
					usercode=user.getUserCode();
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取用户信息异常!");
		}
		
		//分页对象
		PageInfo mtPageInfo = new PageInfo();
		boolean isFirstEnter = false;
		try
		{

			QueryBiz qbiz=new QueryBiz();
			// 获取绑定的sp账号
			lfsp = qbiz.getSpuserList("0", lgcorpcode, StaticValue.getCORPTYPE());
			spUsers = qbiz.getSpusers("0", lgcorpcode, StaticValue.getCORPTYPE());
			// 默认查询实时记录
			if(recordType == null)
			{
				recordType = "realTime";
			}
			// 初始化分页数据
			if(request.getParameter("pageIndex") == null && request.getParameter("pageSize") == null)
			{
				isFirstEnter = true;
			}
			else
			{
				mtPageInfo.setPageSize(getIntParameter("pageSize", mtPageInfo.getPageSize(), request));
				mtPageInfo.setPageIndex(getIntParameter("pageIndex", 1, request));
				isFirstEnter = false;
			}
			//运营商
//			Long spisun=null;
//			//运营商字符串
//			if(spisuncm!=null&&!"".equals(spisuncm)){
//				spisun=Long.parseLong(spisuncm);
//			}
			//任务批次
			Long taskid=null;
			//任务批次
			if(staskid!=null&&!"".equals(staskid)){
				taskid=Long.parseLong(staskid);
			}
			// 获取通道号
			SysMoMtSpgateVo sysMoMtSpgateVo = new SysMoMtSpgateVo();
			// 企业编号
			sysMoMtSpgateVo.setCropCode(lgcorpcode);
			List<SysMoMtSpgateVo> spList = new SysMoRealTimeRecordBiz().getDownHisVos(Long.parseLong(lguserid), sysMoMtSpgateVo);
			// 获取业务类型
			conditionMMap.clear();
			if(!"100000".equals(lgcorpcode))
			{
				// 只显示自定义业务
				conditionMMap.put("corpCode&in", "0," + lgcorpcode);
			}
			else
			{
				conditionMMap.put("corpCode&not in", "1,2");
			}
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, null, conditionMMap, null);
			LinkedHashMap<String, String> busMap = new LinkedHashMap<String, String>();
			if(busList != null && busList.size() > 0)
			{
				for (LfBusManager bus : busList)
				{
					busMap.put(bus.getBusCode(), bus.getBusName());
				}
			}
			String typestr="";
			if("history".equals(recordType)) // 查询历史记录
			{
				typestr="历史";
				SysMtHistoryRecordBiz downBiz = new SysMtHistoryRecordBiz();
				SystemMtTask01_12Vo mtTask = new SystemMtTask01_12Vo();
				List<DynaBean> mtTaskList = null;

				// 第一次进入页面不加载数据
				if(!isFirstEnter)
				{
					//sp账户
					mtTask.setUserid(userid);
					//通道号
					mtTask.setSpgate(spgate);
					//开始时间
					mtTask.setStartTime(sendtime);
					//结束时间
					mtTask.setEndTime(recvtime);
					//电话号码
					mtTask.setPhone(phone);
					mtTask.setGateName(gateName);
					mtTask.setErrorcode(reportStatus);
					mtTask.setNumberStyle(numberStyle);
					// 运营商
//					mtTask.setUnicom(spisun);
					//业务类型
//					mtTask.setSvrtype(buscode);
					//任务批次
					mtTask.setTaskid(taskid);
					//用户ID
					mtTask.setLguserid(lguserid);
					//企业绑定的账户不为空，并且为多企业
					if(!"".equals(spUsers) && corpType == 1)
					{
						mtTask.setSpusers(spUsers);
						if(!"100000".equals(lgcorpcode))
						{
							mtTask.setCorpcode(lgcorpcode);
						}
					}

//					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//					conditionMap.put("corpCode", lgcorpcode);
					mtTask.setP1(usercode);
					// 获取历史记录数据
					mtTaskList = downBiz.getDownHisVos(Long.parseLong(lguserid), mtTask, mtPageInfo);
				}
				
				request.setAttribute("sysMtHisTaskList", mtTaskList);
				request.setAttribute("sysMtTask", mtTask);
				request.setAttribute("pageInfo", mtPageInfo);
				request.setAttribute("busMap", busMap);

			}
			else if("realTime".equals(recordType))// 查询实时记录
			{
				typestr="实时";
				SysMtRealTimeRecordBiz downBiz = new SysMtRealTimeRecordBiz();
				SystemMtTaskVo mtTask = new SystemMtTaskVo();
				List<DynaBean> mtTaskList = null;
				// 非第一次进入
				if(!isFirstEnter)
				{
					//帐号
					mtTask.setUserid(userid);
					//通道号
					mtTask.setSpgate(spgate);
					//开始时间
					mtTask.setStartTime(sendtime);
					//结束时间
					mtTask.setEndTime(recvtime);
					//电话号码
					mtTask.setPhone(phone);
					//运营商
//					mtTask.setUnicom(spisun);
					//业务类型
//					mtTask.setSvrtype(buscode);
					//用户编码
					mtTask.setP1(usercode);
					//任务批次
					mtTask.setTaskid(taskid);
					mtTask.setGateName(gateName);
					mtTask.setReportStatus(reportStatus);
					mtTask.setNumberStyle(numberStyle);
					//用户ID
					mtTask.setLguserid(lguserid);

					if(!"".equals(spUsers) && corpType == 1)
					{
						mtTask.setSpusers(spUsers);
						if(!"100000".equals(lgcorpcode))
						{
							mtTask.setCorpcode(lgcorpcode);
						}
					}

					mtTaskList = downBiz.getDownMtTaskVos(Long.parseLong(lguserid), mtTask, mtPageInfo);
//					LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
//					conditionMap.put("corpCode", lgcorpcode);

				}
				request.setAttribute("sysMtRealTimeTaskList", mtTaskList);
				request.setAttribute("sysMtTask", mtTask);
				request.setAttribute("pageInfo", mtPageInfo);
			}
			request.setAttribute("busMap", busMap);
			request.setAttribute("spList", spList);
			request.setAttribute("isFirstEnter", isFirstEnter);// 是否第一次进入标示
			request.setAttribute("mrUserList", lfsp);
			request.setAttribute("usercode", usercode);
			request.setAttribute("reportStatus", reportStatus);
			request.setAttribute("numberStyle", numberStyle);
			
			request.setAttribute("gateName", gateName);
			
			long count=0l;
			//从pageinfo中获取查询总条数
			if(mtPageInfo!=null){
				count=mtPageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "网优"+typestr+"下行记录查询：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			new QueryBiz().setLog(request, "网优下行记录", opContent, StaticValue.GET);
			
			request.getRequestDispatcher(this.empRoot + base + "/wy_mtRecord.jsp").forward(request, response);
			
		}
		catch (Exception e2)
		{
			// 异常处理
			EmpExecutionContext.error(e2, "网优下行记录查询出现异常");
			// 返回前台结果
			try {
				request.getSession(false).setAttribute("error", e2);
			} catch (Exception e) {
				EmpExecutionContext.error(e,"session为null");
			}
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", mtPageInfo);
			request.setAttribute("isFirstEnter", isFirstEnter);
			try
			{
				request.getRequestDispatcher(this.empRoot + base + "/wy_mtRecord.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "网优下行记录查询后跳转异常");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "网优下行记录查询后跳转IO异常");
			}

		}

	}
	
	/***
	 * 获得整型参数
	 * @param param
	 * @param defaultValue
	 * @param request
	 * @return
	 */
	protected int getIntParameter(String param, int defaultValue, HttpServletRequest request)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e,"获得整型参数异常");
			return defaultValue;
		}
	}
	/**
	 * 网优下行记录excel导出
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		// 设置语言
		String langName  = request.getParameter("langName");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		// 记录类型（实时记录，历史记录）
		String recordType = request.getParameter("recordType");
		// 企业编码
		String corpcode = request.getParameter("lgcorpcode");

		
		//账户
		String userid=request.getParameter("userid");
		//通道号
		String spgate=request.getParameter("spgate");
		//开始时间
		String sendtime=request.getParameter("sendtime");
		//结束时间
		String recvtime=request.getParameter("recvtime");
		//电话号码
		String phone=request.getParameter("phone");
		// 操作员编码
		String usercode = "";
		//任务批次
		String staskid = request.getParameter("taskid");
		// 用户id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//任务批次
		Long taskid=null;
		//任务批次
		if(staskid!=null&&!"".equals(staskid)){
			taskid=Long.parseLong(staskid);
		}
		String gateName="";
		//reportStatus
		String reportStatus=request.getParameter("reportStatus");
		// 发送账户
		String spUsers;
		// 动态拼接SP帐号变量
		StringBuffer spuser = new StringBuffer();
		//获得用户编号
		try
		{
			LfSysuser user=baseBiz.getLfSysuserByUserId(lguserid);
			if(user!=null){
				//1表示个人权限 2表示机构权限
				if("1".equals(user.getPermissionType()+"")){
					usercode=user.getUserCode();
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取用户信息异常!");
		}
		
		// 分页对象
		PageInfo pageInfo = new PageInfo();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 企业绑定帐号
			List<LfSpDepBind> lfsp = null;
			// 非10000用户则需要带上企业编码的查询条件
			if(!"100000".equals(corpcode))
			{
				conditionMap.put("corpCode", corpcode);
			}
			// 查询当前登录用户企业绑定的SP账户
			lfsp = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);


			// 拼接发送账户
			if(lfsp != null) for (int i = 0; i < lfsp.size(); i++)
			{
				if(i == lfsp.size() - 1)
				{
					spuser = spuser.append("'" + lfsp.get(i).getSpUser() + "'");
				}
				else
				{
					spuser = spuser.append("'" + lfsp.get(i).getSpUser() + "'" + ",");
				}
			}
			spUsers = spuser.toString();
			// 如果是十万号则查询不需要帐号条件
			if("100000".equals(corpcode))
			{
				spUsers = null;
			}
			
			if(recordType == null)
			{
				recordType = "realTime";
			}
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			// 查询历史记录
			if("history".equals(recordType))
			{
				SystemMtTask01_12Vo mtTask = new SystemMtTask01_12Vo();
				//sp账户
				mtTask.setUserid(userid);
				//通道号
				mtTask.setSpgate(spgate);
				//开始时间
				mtTask.setStartTime(sendtime);
				//结束时间
				mtTask.setEndTime(recvtime);
				//电话号码
				mtTask.setPhone(phone);
				mtTask.setGateName(gateName);
				mtTask.setErrorcode(reportStatus);
				mtTask.setLguserid(lguserid);
				// 运营商
//				mtTask.setUnicom(spisun);
				//业务类型
//				mtTask.setSvrtype(buscode);
				//任务批次
				mtTask.setTaskid(taskid);

				//企业绑定的账户不为空，并且为多企业
				if(!"".equals(spUsers) && corpType == 1)
				{
					mtTask.setSpusers(spUsers);
					if(!"100000".equals(corpcode))
					{
						mtTask.setCorpcode(corpcode);
					}
				}

				conditionMap.put("corpCode", corpcode);
				mtTask.setP1(usercode);
				// 获取系统上行历史数据
				Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
				int ishidephome = 0;
				if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
				{
					ishidephome = 1;
				}
				Map<String, String> resultMap = new SysMtHistoryRecordBiz().createMtHistoryExcel(langName,lguserid,mtTask, pageInfo, ishidephome);

				if(resultMap != null && resultMap.size() > 0)
				{

					// 文件名称
					String fileName = (String) resultMap.get("FILE_NAME");
					// 写日志
					String opContent = "导出系统历史下行查询记录：" +resultMap.get("SIZE") + "条  ,文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
					new QueryBiz().setLog(request, "网优下行查询记录", opContent, StaticValue.GET);
					HttpSession session =request.getSession(false);
					session.setAttribute("wyrecord_export_map",resultMap);
					out.print("true");
					// 路径
//					String filePath = (String) resultMap.get("FILE_PATH");
//					DownloadFile dfs = new DownloadFile();
//					// 导出
//					dfs.downFile(request, response, filePath, fileName);
				}
				else
				{
					out.print("false");
				}

			
			}else if("realTime".equals(recordType))
			{
				SystemMtTaskVo mtTask = new SystemMtTaskVo();
				// 用户id
				String userId = request.getParameter("lguserid");
				//帐号
				mtTask.setUserid(userid);
				//通道号
				mtTask.setSpgate(spgate);
				//开始时间
				mtTask.setStartTime(sendtime);
				//结束时间
				mtTask.setEndTime(recvtime);
				//电话号码
				mtTask.setPhone(phone);
				//运营商
//				mtTask.setUnicom(spisun);
				//业务类型
//				mtTask.setSvrtype(buscode);
				//用户编码
				mtTask.setP1(usercode);
				mtTask.setLguserid(lguserid);
				//任务批次
				mtTask.setTaskid(taskid);
				mtTask.setGateName(gateName);
				mtTask.setReportStatus(reportStatus);
//				mtTask.setNumberStyle(numberStyle);

				if(!"".equals(spUsers) && corpType == 1)
				{
					mtTask.setSpusers(spUsers);
					if(!"100000".equals(corpcode))
					{
						mtTask.setCorpcode(corpcode);
					}
				}
					List<SystemMtTaskVo> mtTaskList = null;

				Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
				int ishidephome = 0;
				if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
				{
					ishidephome = 1;
				}
				// 调用创建excel的方法
				Map<String, String> resultMap = new SysMoRealTimeRecordBiz().createMtTaskTimeExcel(langName,userId,mtTask, pageInfo, ishidephome);
				if(resultMap != null && resultMap.size() > 0)
				{
					// 文件名称
					String fileName = (String) resultMap.get("FILE_NAME");
					// 写日志
					String opContent = "导出系统实时下行查询记录：" +resultMap.get("SIZE") + "条  ,文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
					new QueryBiz().setLog(request, "网优下行查询记录", opContent, StaticValue.GET);
					HttpSession session =request.getSession(false);
					session.setAttribute("wyrecord_export_map",resultMap);
					out.print("true");
					// 路径
//					String filePath = (String) resultMap.get("FILE_PATH");
//					DownloadFile dfs = new DownloadFile();
//					dfs.downFile(request, response, filePath, fileName);
				}
				else
				{
					out.print("false");
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询下行历史记录异常");
		}

	}
	
	
	
	/**
	 * 网优下行历史导出下载
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(false);
		Object obj = null;
		try {
			obj = session.getAttribute("wyrecord_export_map");
			session.removeAttribute("wyrecord_export_map");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"session为null");
		}
		if(obj != null)
		{
			// 弹出下载页面。
			DownloadFile dfs = new DownloadFile();
			Map<String, String> resultMap = (Map<String, String>) obj;
			String fileName = (String) resultMap.get("FILE_NAME");
			String filePath = (String) resultMap.get("FILE_PATH");
			dfs.downFile(request, response, filePath, fileName);
		}
	}
	
}
