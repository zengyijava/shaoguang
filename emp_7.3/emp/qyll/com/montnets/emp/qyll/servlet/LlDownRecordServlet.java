package com.montnets.emp.qyll.servlet;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.qyll.biz.QueryBiz;
import com.montnets.emp.qyll.biz.QueryMtRecordBiz;
import com.montnets.emp.qyll.biz.SysMtRecordBiz;
import com.montnets.emp.qyll.entity.RptStaticValue;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;

public class LlDownRecordServlet extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5016631301839948098L;

	
	private final String empRoot = "qyll";

	private final String base = "/sjcx";
	
//	private SysMtRecordBiz mtRecordBiz = new SysMtRecordBiz();
	private final QueryMtRecordBiz mtRecordBiz = new QueryMtRecordBiz();
	
	
	/**
	 * 系统下行记录查询
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		
		// 记录类型
		String recordType = request.getParameter("recordType");
		// 企业编码
		String lgcorpcode = "";
		// 当前登录操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);


		// 当前登录操作员
		String lgusername = request.getParameter("lgusername");
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
		//业务类型
		String buscode=request.getParameter("busCode");
		//运营应商
		String spisuncm=request.getParameter("spisuncm");
		// 操作员编码
		//String usercode = request.getParameter("usercode");
		//任务批次
		String taskid = request.getParameter("taskid");
		//错误码
		String mterrorcode = request.getParameter("mterrorcode");
		//自定义流水号
		String usermsgid = request.getParameter("usermsgid");
		
		//分页对象
		PageInfo pageInfo = new PageInfo();
		// 是否第一次访问
		boolean isFirstEnter = pageSet(pageInfo, request);
		long count = 0l;
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try
		{
			QueryBiz qbiz=new QueryBiz();
			//获取登录sysuser
			LfSysuser sysuser =qbiz.getCurrenUser(request);
			if(sysuser==null){
				EmpExecutionContext.error("下行记录查询,session中获取当前登录对象出现异常");
				return;
			}
			lgcorpcode=sysuser.getCorpCode();
			//判断企业编码获取
			if(lgcorpcode==null||"".equals(lgcorpcode)){
				EmpExecutionContext.error("下行记录查询,session中获取企业编码出现异常");
				return;
			}
			//判断SP账号是否是属于本企业的
			if(userid != null && !"".equals(userid.trim()))
			{
				//多企业才处理
				if(StaticValue.getCORPTYPE() ==1){
					boolean checkFlag=new CheckUtil().checkSysuserInCorp(sysuser, lgcorpcode, userid, null);
					if(!checkFlag){
						EmpExecutionContext.error("下行记录查询,检查操作员，企业编码，发送账号不通过，spuserid："+userid+",corpcode="+lgcorpcode);
						return;
					}
				}
			}
			
			//设置业务类型，页面条件查询下拉框用
			getAndSetBus(lgcorpcode, request);
			
			//设置通道号和发送账号，页面条件查询下拉框用
			getAndSetSp(lgcorpcode, request);
			
			//第一次进入
			if(isFirstEnter)
			{
				request.getSession(RptStaticValue.GET_SESSION_FALSE).removeAttribute("sysMtTaskCon");
				return;
			}
			
			//企业编码
			conditionMap.put("lgcorpcode", lgcorpcode.trim());
			//查询类型   实时 或 历史
			if(recordType == null || recordType.length() < 1)
			{
				//实时
				recordType = "realTime";
			}
			//查询类型
			conditionMap.put("recordType", recordType);
			//运营商
			conditionMap.put("spisuncm", spisuncm);
			//taskid
			conditionMap.put("taskid", taskid);
			//sp账户
			conditionMap.put("userid", userid);
			//通道号
			conditionMap.put("spgate", spgate);
			//手机号
			conditionMap.put("phone", phone);
			////业务类型
			conditionMap.put("buscode", buscode);
			//开始时间
			conditionMap.put("sendtime", sendtime);
			//结束时间
			conditionMap.put("recvtime", recvtime);
			//错误码条件
			conditionMap.put("mterrorcode", mterrorcode);
			//自定义流水号
			conditionMap.put("usermsgid", usermsgid);			
			
			//session中获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			//设置有权限看的操作员编码。不需要考虑权限则是空字符串，目前只有admin和管辖范围是顶级机构不需要考虑权限
			String usercode = mtRecordBiz.getPermissionUserCode(curUser);
			if(usercode == null)
			{
				//当前操作员编码
				conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
				//当前操作员id
				conditionMap.put("curUserId", curUser.getUserId().toString());
			}
			else
			{
				//当前操作员编码
				conditionMap.put("curUsercode", "'" + curUser.getUserCode() + "'");
				//当前操作员id
				conditionMap.put("curUserId", curUser.getUserId().toString());
				//有权限看的操作员编码
				conditionMap.put("domUsercode", usercode);
			}
			String spuserpri =mtRecordBiz.getPermissionSpuserMtpri(curUser);
			if(spuserpri==null){
				//当前操作员编码
				conditionMap.put("spcurcorpcode", "'" + curUser.getCorpCode() + "'");
				//当前操作员id
				conditionMap.put("spcurUserId", curUser.getUserId().toString());
			}else{
				//有权限看的操作员编码
				conditionMap.put("spuserpri", spuserpri);
			}
			
			//实时库分页对象
			Object realDbpageInfoObj = request.getSession(false).getAttribute("mtRealDbpageInfo");
			PageInfo realDbpageInfo;
			//从session获取
			if(realDbpageInfoObj != null)
			{
				realDbpageInfo = (PageInfo)realDbpageInfoObj;
			}
			//从session获取为空
			else
			{
				//声明一个对象
				realDbpageInfo = new PageInfo();
			}
			//查询下行记录，如果实时记录查询，则先查备份表，如果没记录，则再查实时表
			List<DynaBean> mtTaskList = getMtTasks(conditionMap, pageInfo, request, realDbpageInfo);
			
			//没记录就不需要查询分页
			if(mtTaskList == null || mtTaskList.size() == 0)
			{
				pageInfo.setNeedNewData(2);
				pageInfo.setTotalRec(0);
				pageInfo.setTotalPage(1);
			}
			
			//错误码说明map，key序号，value为错误码说明
			Map<String,String> errCodeDesMap = mtRecordBiz.getErrCodeDisMap(mtTaskList, lgcorpcode);
			request.setAttribute("errCodeDesMap", errCodeDesMap);
			
			request.setAttribute("sysMtTaskList", mtTaskList);
			//设置查询条件到session，这个要放在查询方法之后，因为查询方法里还会设置一些查询条件
			request.getSession(RptStaticValue.GET_SESSION_FALSE).setAttribute("sysMtTaskCon", conditionMap);
			
			//获取总数，用于记录日志
			count = mtTaskList==null?0:mtTaskList.size();
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "系统下行记录查询，异常。");
			request.setAttribute("findresult", "-1");
		}
		finally
		{
			// 是否第一次进入标示
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("pageInfo", pageInfo);
			
			String conditionstr = "userid=" + conditionMap.get("userid") + ",bindspUserid=" + conditionMap.get("spUsers")
								+ ",spgate=" + conditionMap.get("spgate") + ",buscode=" + conditionMap.get("buscode") 
								+ ",spisuncm=" + conditionMap.get("spisuncm") + ",phone=" + conditionMap.get("phone")
								+ ",taskid=" + conditionMap.get("taskid") + ",sendtime=" + conditionMap.get("sendtime")
								+ ",recvtime=" + conditionMap.get("recvtime") + ",pageindex=" + pageInfo.getPageIndex();
			//开始时间
			String starthms=hms.format(startl);
			//菜单初次点入判断
			if(recordType==null){
				recordType = "(菜单初次点入)";
			}
			String opContent = "短信提醒下行记录"+recordType+" totalcount:" + count + "条 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms,条件:"+conditionstr;
			EmpExecutionContext.info("数据查询", lgcorpcode, lguserid, lgusername, opContent, StaticValue.GET);
			
			request.getRequestDispatcher(this.empRoot + base + "/llDownRecord.jsp").forward(request, response);
		}
	}
	
	/**
	 * 
	 * @description 查询下行记录，如果是实时查询，则点击查询时，先查备份表，没记录，则查实时表
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页对象
	 * @param request 请求对象
	 * @return 返回下行记录对象集合
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-26 上午10:55:24
	 */
	private List<DynaBean> getMtTasks(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, HttpServletRequest request)
	{
		List<DynaBean> mtTaskList;
		//页面点击查询，则先用备份表查
		if(pageInfo.getNeedNewData() == 1)
		{
			//查备份表
			conditionMap.put("realTableName", "gw_mt_task_bak");
			mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
			
			//下行记录查询，之前是备份表没有记录则查询实时表，现在改成备份表和实时表联合查询。查询实时表的代码注释。
//			//如果是实时查询，且上面查备份表没记录，则查实时表
//			if((mtTaskList == null || mtTaskList.size() < 1) && "realTime".equals(conditionMap.get("recordType")))
//			{
//				//查实时表
//				conditionMap.put("realTableName", "mt_task");
//				mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
//			}
			return mtTaskList;
		}
		//页面点击分页，则用上次的表查
		
		//取session中，上次使用的表名
		LinkedHashMap<String, String> conditionMapPre = (LinkedHashMap<String, String>)request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
		//拿不到，则使用默认值
		if(conditionMapPre == null)
		{
			conditionMapPre = new LinkedHashMap<String, String>();
			conditionMapPre.put("realTableName", "gw_mt_task_bak");
		}
		//设置上次使用的表名
		conditionMap.put("realTableName", conditionMapPre.get("realTableName"));
		//查询
		mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
		return mtTaskList;
	}
	
	/**
	 * 
	 * @description 查询下行记录，如果是实时查询，则点击查询时，先查备份表，没记录，则查实时表
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页对象
	 * @param request 请求对象
	 * @return 返回下行记录对象集合
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-26 上午10:55:24
	 */
	private List<DynaBean> getMtTasks(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, HttpServletRequest request, PageInfo realDbpageInfo)
	{
		List<DynaBean> mtTaskList;
		//页面点击查询，则先用备份表查
		if(pageInfo.getNeedNewData() == 1)
		{
			//查备份表
			conditionMap.put("realTableName", "gw_mt_task_bak");
			mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo, realDbpageInfo);
			
			//下行记录查询，之前是备份表没有记录则查询实时表，现在改成备份表和实时表联合查询。查询实时表的代码注释。
//			//如果是实时查询，且上面查备份表没记录，则查实时表
//			if((mtTaskList == null || mtTaskList.size() < 1) && "realTime".equals(conditionMap.get("recordType")))
//			{
//				//查实时表
//				conditionMap.put("realTableName", "mt_task");
//				mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo);
//			}
			return mtTaskList;
		}
		//页面点击分页，则用上次的表查
		
		//取session中，上次使用的表名
		LinkedHashMap<String, String> conditionMapPre = (LinkedHashMap<String, String>)request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
		//拿不到，则使用默认值
		if(conditionMapPre == null)
		{
			conditionMapPre = new LinkedHashMap<String, String>();
			conditionMapPre.put("realTableName", "gw_mt_task_bak");
		}
		//设置上次使用的表名
		conditionMap.put("realTableName", conditionMapPre.get("realTableName"));
		//查询
		mtTaskList = mtRecordBiz.getMtRecords(conditionMap, pageInfo, realDbpageInfo);
		return mtTaskList;
	}
	 
	/**
	 * 查询下行记录分页信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getMtPageInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		SimpleDateFormat hms = new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl = System.currentTimeMillis();
		String conditionstr = "";
		//分页对象
		PageInfo pageInfo = new PageInfo();
		try
		{
			// 查询条件
			LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("sysMtTaskCon");
			if(conditionMap == null || conditionMap.size() < 1)
			{
				EmpExecutionContext.error("svt查询下行记录分页信息，获取查询条件对象集合为空。");
				return;
			}
			
			pageSet(pageInfo, request);
			//实时库分页对象
			Object realDbpageInfoObj = request.getSession(false).getAttribute("mtRealDbpageInfo");
			PageInfo realDbpageInfo;
			//从session获取
			if(realDbpageInfoObj != null)
			{
				realDbpageInfo = (PageInfo)realDbpageInfoObj;
			}
			//从session获取为空
			else
			{
				//声明一个对象
				realDbpageInfo = new PageInfo();
			}
			QueryMtRecordBiz mtRecordBiz = new QueryMtRecordBiz();
			//设置分页对象和实时库分页对象
			mtRecordBiz.getMtRecordsPageInfo(conditionMap, pageInfo, realDbpageInfo);
			//设置到session中
			request.getSession(false).setAttribute("mtRealDbpageInfo", realDbpageInfo);
			
			conditionstr = "userid=" + conditionMap.get("userid") + ",bindspUserid=" + conditionMap.get("spUsers")
						+ ",spgate=" + conditionMap.get("spgate") + ",buscode=" + conditionMap.get("buscode") 
						+ ",spisuncm=" + conditionMap.get("spisuncm") + ",phone=" + conditionMap.get("phone")
						+ ",taskid=" + conditionMap.get("taskid") + ",sendtime=" + conditionMap.get("sendtime")
						+ ",recvtime=" + conditionMap.get("recvtime") + ",totalRec=" + pageInfo.getTotalRec();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt查询短信提醒下行记录分页信息，异常。");
		}
		finally
		{
			response.getWriter().print("{totalRec:"+pageInfo.getTotalRec()+",totalPage:"+pageInfo.getTotalPage()+"}");
			//开始时间
			String starthms=hms.format(startl);
			String opContent = "系统下行记录。分页：" + pageInfo.getTotalRec() + "条 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms,条件："+conditionstr;
			setLog(request, "查询统计", opContent, StaticValue.GET);
		}
	}

	/**
	 * 获取并设置业务类型到request里
	 * @param lgcorpcode 企业编码
	 * @param request 请求对象
	 * @return 成功返回true
	 */
	private boolean getAndSetBus(String lgcorpcode, HttpServletRequest request)
	{
		try
		{
			// 查询条件
			LinkedHashMap<String, String> conditionMMap = new LinkedHashMap<String, String>();
			// 获取业务类型
			if(!"100000".equals(lgcorpcode))
			{
				// 只显示自定义业务
				conditionMMap.put("corpCode&in", "0," + lgcorpcode);
			}
			else
			{
				conditionMMap.put("corpCode&not in", "1,2");
			}
			
			BaseBiz	baseBiz	= new BaseBiz();
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, null, conditionMMap, null);
			LinkedHashMap<String, String> busMap = new LinkedHashMap<String, String>();
			if(busList != null && busList.size() > 0)
			{
				for (LfBusManager bus : busList)
				{
					busMap.put(bus.getBusCode(), bus.getBusName());
				}
			}
			request.setAttribute("busMap", busMap);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取并设置业务类型，异常。");
			return false;
		}
	}
	
	/**
	 * 获取并设置通道和发送账号
	 * @param lgcorpcode 企业编码
	 * @param request 请求对象
	 * @return 成功返回true
	 */
	private boolean getAndSetSp(String lgcorpcode, HttpServletRequest request)
	{
		try
		{
			SysMtRecordBiz mtRecordBiz = new SysMtRecordBiz();
			List<DynaBean> spList = mtRecordBiz.getSpgateList(lgcorpcode);
			request.setAttribute("spList", spList);
			
			// 页面SP账号查询条件
			List<String> lfsp = mtRecordBiz.getSpUserList(lgcorpcode);
			request.setAttribute("mrUserList", lfsp);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取并设置通道和发送账号，异常。");
			return false;
		}
	}
	
	/**
	 * 写日志
	 * 
	 * @param request
	 *        请求对象
	 * @param opModule
	 *        菜单名称
	 * @param opContent
	 *        操作内容
	 * @param opType
	 */
	private void setLog(HttpServletRequest request, String opModule, String opContent, String opType)
	{
		try
		{
			Object loginSysuserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			if(loginSysuserObj == null)
			{
				EmpExecutionContext.error("系统下行记录，记录日志，获取不到session当前登录操作员对象。");
				return;
			}
			
			LfSysuser loginSysuser = (LfSysuser)loginSysuserObj;
			EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，记录日志，异常。");
		}
	}
	
	/**
	 * 获取session当前登录操作员的企业编码
	 * @param request
	 * @return 返回企业编码
	 */
	private String getCurrenCorpCode(HttpServletRequest request)
	{
		try
		{
			Object loginSysuserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			if(loginSysuserObj == null)
			{
				return null;
			}
			LfSysuser sysUser = (LfSysuser)loginSysuserObj;
			return sysUser.getCorpCode();
			
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取企业编码失败。");
			return null;
		}
	}
	
	/**
	 * 从session获取当前登录操作员对象
	 * @param request 请求对象
	 * @return 返回当前登录操作员对象，为空则返回null
	 */
	private LfSysuser getCurUserInSession(HttpServletRequest request)
	{
		Object loginSysuserObj = request.getSession(RptStaticValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		if(loginSysuserObj == null)
		{
			EmpExecutionContext.error("系统下行记录，从session获取当前登录操作员对象为空。");
			return null;
		}
		return (LfSysuser)loginSysuserObj;
	}
	
	

}
