package com.montnets.emp.wyquery.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.tools.SysuserUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import com.montnets.emp.wyquery.biz.HistoryRecordBiz;
import com.montnets.emp.wyquery.biz.HistoryRecordExcelTool;
import com.montnets.emp.wyquery.biz.QueryBiz;
import com.montnets.emp.wyquery.vo.LfMttaskVo;
import com.montnets.emp.wyquery.vo.SendedMttaskVo;


/**
 * 
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-3-25 上午08:51:12
 * @description 网优历史记录
 */

public class wyq_historyRecordSvt extends BaseServlet {

	
	private static final long	serialVersionUID	= 1L;
	
	private final String empRoot="wygl";
	private final String basePath="/wyquery";
	private final String path=new TxtFileUtil().getWebRoot();
	//模板路径
	private final String  excelPath = path + "wygl/wyquery/file";
	
	private final HistoryRecordBiz sendTaskRecordBiz = new HistoryRecordBiz();
	private final BaseBiz baseBiz = new BaseBiz();
	
	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String path = "/wyq_historyRecord.jsp";
		String titlePath = "historyRecord";

		PageInfo pageInfo = new PageInfo();
		//当前登录用户的企业编码
		String corpCode = "";
		String  userId = "";
		try
		{
			//导出方法的查询条件清空
			request.getSession(false).setAttribute("r_sendTime", null);
			request.getSession(false).setAttribute("r_recvTime", null);
			request.getSession(false).setAttribute("r_userIds", null);
			request.getSession(false).setAttribute("r_depIds", null);
			request.getSession(false).setAttribute("r_sendTitle", null);
			request.getSession(false).setAttribute("r_mtSendState", null);
			
			boolean isFirstEnter = pageSet(pageInfo,request);
			//当前登录用户的企业编码
			corpCode =request.getParameter("lgcorpcode");
			//userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			userId = SysuserUtil.strLguserid(request);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			orconp.put("corpCode", "asc");
			List<LfBusManager> busList = baseBiz.getByCondition(
					LfBusManager.class, conditionMap, orconp);
			
			conditionMap.clear();
			conditionMap.put("platFormType", "1");
			conditionMap.put("isValidate", "1");
            //如果不是10000用户登录，则需要带上企业编码查询
			if(corpCode !=null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode", corpCode);
			}
			QueryBiz qbiz=new QueryBiz();
			List<String> lfsp = qbiz.getSpuserList("0", corpCode, StaticValue.getCORPTYPE());
            
			//查找绑定的sp账号
			/*List<LfSpDepBind> userList = baseBiz.getByCondition(
					LfSpDepBind.class, conditionMap, null);*/

			String spUser = request.getParameter("spUser");
			String depid = request.getParameter("depid");
			String userid = request.getParameter("userid");
			String startSubmitTime = request.getParameter("sendtime");
			String endSubmitTime = request.getParameter("recvtime");
            String busCode = request.getParameter("busCode");
            String title = request.getParameter("title");
            String taskState= request.getParameter("taskState");
            String taskType= request.getParameter("taskType");
            String mtSendState = request.getParameter("mtSendState");
            
            //任务批次查询条件
            String taskID=request.getParameter("taskID");
            if(taskID!=null&&!"".equals(taskID.trim())){
            	try{
            		mtVo.setTaskId(Long.parseLong(taskID.trim()));
            	}catch (Exception e) {
            		EmpExecutionContext.error(e, "网优群发历史查询设置taskid异常。");
				}
            }
            
            //获取过滤条件
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            
            //是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            if(isContainsSun!=null&&!"".equals(isContainsSun)){
            	mtVo.setIsContainsSun(isContainsSun);
            }
            
           //查询移动财务短信及群发短信
            mtVo.setMsTypes("1,5");
            mtVo.setTaskState(taskState);
            if(taskType!=null&&!"".equals(taskType)){
            	mtVo.setTaskType(Integer.parseInt(taskType));
            }
			if(!isFirstEnter){
				mtVo.setSpUser(spUser);
				if (busCode!=null && !busCode.equals(""))
					mtVo.setBusCode(busCode);
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);

				if(title != null && title.length() > 0)
				{
					mtVo.setTitle(title);
				}
				mtVo.setOverSendstate(mtSendState);
				//mtVo.setOverSendstate("1,2,3,6");
				//查询发送时间
				if (!"".equals(endSubmitTime)){
					mtVo.setEndSendTime(endSubmitTime);
				}
				if (!"".equals(startSubmitTime)){
					mtVo.setStartSendTime(startSubmitTime);
				}
				
				//设置导出的查询条件
				request.getSession(false).setAttribute("r_sendTime", mtVo.getStartSendTime());
				request.getSession(false).setAttribute("r_recvTime", mtVo.getEndSendTime());
				request.getSession(false).setAttribute("r_userIds", mtVo.getUserIds());
				request.getSession(false).setAttribute("r_depIds", mtVo.getDepIds());
				request.getSession(false).setAttribute("r_sendTitle", mtVo.getTitle());
				request.getSession(false).setAttribute("r_mtSendState", mtVo.getOverSendstate());
				
				//查询lfmttask信息
				if(corpCode != null && corpCode.equals("100000"))
				{
					mtVoList = sendTaskRecordBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
				}else
				{
					mtVoList = sendTaskRecordBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
				}
				
				if(isContainsSun==null||"".equals(isContainsSun)){
					request.setAttribute("isContainsSun", "0");
				}else{
					request.setAttribute("isContainsSun", "1");
				}
			}

			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("sendUserList", lfsp);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", userId);//当前登录用户id
			//加载机构树
			String departmentTree = sendTaskRecordBiz.getDepartmentJosnData(Long.parseLong(userId));
			//如果用户请求是"群发历史查询"
			
			request.setAttribute("departmentTree", departmentTree);
			request.setAttribute("titlePath", titlePath);
			// 定义行数
			long count = 0l;
			// 从pageinfo中获取查询总条数
			if(pageInfo != null)
			{
				count = pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "网优群发历史查询共" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
			new QueryBiz().setLog(request, "网优群发历史", opContent, StaticValue.GET);
			
			//页面跳转
			request.getRequestDispatcher(this.empRoot+this.basePath+path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error("网优群发历史记录查询方法请求URL:" + request.getRequestURI() + "，请求参数，corpCode：" + corpCode + "，userId :" + userId);
			EmpExecutionContext.error(e, "网优群发历史记录的方法异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);// 回到页面第一次加载时的状态
			request.setAttribute("titlePath", titlePath);
			request.setAttribute("pageInfo", pageInfo);

			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + path).forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "网优历史记录serlvet异常！");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "网优历史记录serlvet跳转异常！");
			}
		}
	}
	
	/**
	 * 查询条件中的机构树加载方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	//输出机构代码数据	
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		// 部门iD
		String depId = request.getParameter("depId");
		try
		{
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			String departmentTree = sendTaskRecordBiz.getDepJosn(depId, curUser);
			if(departmentTree == null)
			{
				response.getWriter().print("");
			}
			else
			{
				response.getWriter().print(departmentTree);
			}
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "网优群发历史，生成机构树字符串，异常。depId="+depId);
			response.getWriter().print("");
		}
	}
	
	/**
	 * 从session获取当前登录操作员对象
	 * @param request 请求对象
	 * @return 返回当前登录操作员对象，为空则返回null
	 */
	private LfSysuser getCurUserInSession(HttpServletRequest request)
	{
		Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
		if(loginSysuserObj == null)
		{
			return null;
		}
		return (LfSysuser)loginSysuserObj;
	}
	
	/**
	 * 查询条件中操作员树的加载方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{
			// 获取当前登录用户的userid
			//String userid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userid = SysuserUtil.strLguserid(request);

			String deptUserTree = "";
			String titlePath = "historyRecord";
			deptUserTree = getDeptUserJosnData(titlePath, Long.parseLong(userid));

			response.getWriter().print(deptUserTree);
			EmpExecutionContext.info(deptUserTree);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优群发历史或群发任务查询条件中操作员树的加载方法异常！");
		}
	}	

	/**
	 * 操作员树的加载方法
	 * @param titlePath
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long userid) throws Exception{
		StringBuffer tree = null;
		//根据userid获取当前用户信息
		LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
		//如果当前用户是个人权限，则直接返回
		if(currUser.getPermissionType()==1)
		{
			tree=new StringBuffer("[]");
		}
		//机构
		else
		{
			List<LfDep> lfDeps;
			List<LfSysuser> lfSysusers;
			
			DepBiz depBiz = new DepBiz();
			try {
				//如果当前用户的企业编码时10000,则获取所有的机构信息
				if(currUser.getCorpCode().equals("100000"))
				{
					LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
					orderbyMap.put("depId", StaticValue.ASC);
					orderbyMap.put("deppath", StaticValue.ASC);		
					
					lfDeps = baseBiz.getByCondition(LfDep.class, null, orderbyMap);
				}else
				{
					//否则只获取当前用户管辖范围内的机构信息
					lfDeps = depBiz.getAllDeps(userid);
				}
				
				//拼结需要返回的树
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				
				SysuserBiz sysBiz = new SysuserBiz();
				//如果当前登录用户的企业编码是10000,则获取所有的的用户信息
				if(currUser.getCorpCode().equals("100000"))
				{
					LinkedHashMap<String,String> conMap = new LinkedHashMap<String,String>();
					conMap.put("userId&<>","1" );
					lfSysusers = baseBiz.getByCondition(LfSysuser.class, conMap, null);
				}else
				{
					//群发历史查询页面，需要查询出所有状态的用户
					if(titlePath.equals("smsTaskRecord"))
					{
						lfSysusers = sysBiz.getAllSysusersOfSmsTaskRecord(userid);
					}
					else {
						//群发任务查询页面，只需要查询出状态为启用的用户信息
						lfSysusers = sysBiz.getAllSysusers(userid);
					}
				}
				//拼结操作员信息
				LfSysuser lfSysuser = null;
				if(!lfSysusers.isEmpty()){
					tree.append(",");
				}
				for (int i = 0; i < lfSysusers.size(); i++) {
					lfSysuser = lfSysusers.get(i);
					tree.append("{");
					tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
					tree.append(",name:'").append(lfSysuser.getName()).append("'");
					tree.append(",pId:").append(lfSysuser.getDepId());
					tree.append(",isParent:").append(false);
					tree.append("}");
					if(i != lfSysusers.size()-1){
						tree.append(",");
					}
				}
				
				tree.append("]");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"网优群发历史或群发任务操作员树的加载方法异常！");
			}
		}
		//返回操作员机构树
		return tree.toString();
	}

	/**
	 * 操作员树的加载方法
	 * @param titlePath
	 * @param depId
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long depId,Long userid) throws Exception{
		StringBuffer tree = null;
		//根据userid获取当前操作员信息
		LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
		if(currUser.getPermissionType()==1)
		{
			tree=new StringBuffer("[]");
		}else
		{
			List<LfDep> lfDeps;
			List<LfSysuser> lfSysusers = null;
			
			DepBiz depBiz = new DepBiz();
			try {
				//如果企业编码是10000的用户登录
				if(currUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
						LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
						//只查询顶级机构
						conditionMap.put("superiorId", "0");
						//查询未删除的机构
						conditionMap.put("depState", "1");
						//排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);	
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						/*备份 pengj
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			         	*/
			        	//新增 pengj
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, currUser.getCorpCode());
					}
					
					
				}else
				{
					if(depId == null){
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);						
					}else{
						/*备份 pengj
			            lfDeps = new DepBiz().getDepsByDepSuperId(depId);
			         	*/
			        	//新增 pengj
			        	lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId, currUser.getCorpCode());
					}
				}
				//拼结机构树
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId()+"'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");			
					if(i != lfDeps.size()-1){
						tree.append(",");
					}
				}
				
			//	SysuserBiz sysBiz = new SysuserBiz();
				if(depId != null)
				{
					//如果当前登录用户的企业编码是10000
					if(currUser.getCorpCode().equals("100000"))
					{
						LinkedHashMap<String,String> conMap = new LinkedHashMap<String,String>();
						conMap.put("userId&<>","1" );
	                    conMap.put("depId", depId.toString());
						lfSysusers = baseBiz.getByCondition(LfSysuser.class, conMap, null);
					}else
					{
                        //获取所有状态操作员信息
						/*lfSysusers = sysBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);*/
						lfSysusers = sendTaskRecordBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);
						
					}
				}
				//拼结操作员信息
				LfSysuser lfSysuser = null;
				if(lfSysusers != null && !lfSysusers.isEmpty()){
					if(lfDeps !=null && lfDeps.size()>0)
					{
					  tree.append(",");
					}
					
					for (int i = 0; i < lfSysusers.size(); i++) {
						//操作员信息
						lfSysuser = lfSysusers.get(i);
						tree.append("{");
						tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
						tree.append(",name:'").append(lfSysuser.getName()).append("'");
						if(lfSysuser.getUserState()==2)
						{
							tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
						}else
						{
							tree.append(",name:'").append(lfSysuser.getName()).append("'");
						}
						tree.append(",pId:").append(lfSysuser.getDepId());
						tree.append(",depId:'").append(lfSysuser.getDepId()+"'");
						tree.append(",isParent:").append(false);
						tree.append("}");
						if(i != lfSysusers.size()-1){
							tree.append(",");
						}
					}
				}
				
				tree.append("]");
			} catch (Exception e) {
				EmpExecutionContext.error(e,"网优群发历史或群发任务操作员树的加载方法异常！");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}

	/**
	 * 网优群发历史查询中发送详情信息查看
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void findAllSendInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception{	
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		response.setContentType("text/html;charset=UTF-8");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String,String> orderMap = new LinkedHashMap<String,String>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PageInfo pageInfos = new PageInfo();
		try {	

			//导出方法的查询条件清空
			request.getSession(false).setAttribute("r_sendTime", null);
			request.getSession(false).setAttribute("r_recvTime", null);
			request.getSession(false).setAttribute("r_userIds", null);
			request.getSession(false).setAttribute("r_depIds", null);
			request.getSession(false).setAttribute("r_sendTitle", null);
			pageSet(pageInfos,request);
			//获取页面传过来的lf_mttask表中的mtid
            String mtid  = request.getParameter("mtid");
            //获取页面传过来的查询条件
            String spisuncm = request.getParameter("spisuncm");//运营商
            String phone = request.getParameter("phone");//手机号
            String taskId = request.getParameter("taskId");//手机号
            String tableName="GW_MT_TASK_BAK";
            if(mtid !=null)
            {
                conditionMap.put("mtid", mtid);
            }
            //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,4:带页面查询条件的）
            String type = request.getParameter("type");
            if(type != null)
            {            
            	//接收错误
	            if(type.equals("2"))
	            {
	            	conditionMap.put("errorcode&not like", "E1:,E2:,DELIVRD");
	            }
	            //提交错误
	            else if(type.equals("3"))
	            {
	            	conditionMap.put("errorcode&like", "E1:,E2:");
	            }
	            //发送成功
	            else if(type.equals("5"))
	            {
	            	conditionMap.put("errorcode&in", "0     ,DELIVRD");
	            }
	            //状态未返
	            else if(type.equals("6"))
	            {
	            	conditionMap.put("errorcode", "      ");
	            }
	            
	            //带页面查询条件
	        	if(spisuncm!=null && spisuncm.length()>0)
	        	{
	        		conditionMap.put("unicom", spisuncm);
	        	}
	        	if(phone !=null && phone.length()>0)
	        	{
	        		conditionMap.put("phone&like", phone);
	        	}
	           
            }
            orderMap.put("phone", "asc");
            orderMap.put("pknumber", "asc");
            
            //发送详情
    		List<SendedMttaskVo> MttaskvoList = null;
    		//根据mtid获取任务信息
            LfMttask Lfmttask = baseBiz.getById(LfMttask.class, mtid);
            
            String[] wysendinfoArray = Lfmttask.getWySendInfo().split("/");
		 	//长度少于4，则数据有问题
		 	if(wysendinfoArray.length < 4){
		 		//号码个数
		 		Lfmttask.setEffCount(0l);
				//提交信息数，预览填
		 		Lfmttask.setIcount("0");
				//提交信息数，网关填
		 		Lfmttask.setIcount2("0");
				//发送成功数
		 		Lfmttask.setSucCount("0");
				//提交失败数/接收失败数
		 		Lfmttask.setFaiCount("0");
				//接收失败数
		 		Lfmttask.setRfail2(0L);
		 	}else{
				//号码个数
		 		Lfmttask.setEffCount(0l);
				//提交信息数，预览填
		 		Lfmttask.setIcount(wysendinfoArray[0].trim());
				//提交信息数，网关填
		 		Lfmttask.setIcount2(wysendinfoArray[0].trim());
				//发送成功数
		 		Lfmttask.setSucCount(wysendinfoArray[1].trim());
				//提交失败数
		 		Lfmttask.setFaiCount(wysendinfoArray[2].trim());
				//接收失败数
		 		Lfmttask.setRfail2(Long.parseLong(wysendinfoArray[3].trim()));
		 	}
            
            //获取当前任务的发送时间
    		Timestamp subTime=Lfmttask.getTimerTime();
    		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
    		Date date1=df.parse(df.format(new Date()));
    		Date date2=df.parse(df.format(subTime.getTime()));
    		//当前时间
    		Calendar c1=Calendar.getInstance();
    		//发送时间
    		Calendar c2=Calendar.getInstance();
    		c1.setTime(date1);
    		c2.setTime(date2);
    		//计算时间，当前时间减去发送时间小于3天,查实时表，否则查对应月的历史表
    		c2.add(c2.DATE, 4);
    		if(c2.after(c1))
    		{
    			//查询实时表，即mt_task
    			if(taskId == null || "null".equals(taskId))
    			{
    				if(Lfmttask.getTaskType()==1){
    					conditionMap.put("taskid", Lfmttask.getTaskId().toString());
    				}else{
    					conditionMap.put("batchid", Lfmttask.getBatchID().toString());
    				}
	
    			}else
    			{
    				if(Lfmttask.getTaskType()==1){
    					conditionMap.put("taskid", taskId);
    				}else{
    					conditionMap.put("batchid", Lfmttask.getBatchID().toString());
    				}
    			}
    			//查询正常点击获取全部，或者获取提交失败，或者获取接收失败的数据
    			if(type != null && !type.equals("0"))
        		{
    			    MttaskvoList = sendTaskRecordBiz.getMtTask(conditionMap,orderMap, pageInfos,tableName);
        		}
    		}
    		else
    		{
    			//查询对应月份历史表信息
    			if(taskId == null || "null".equals(taskId))
    			{
    				if(Lfmttask.getTaskType()==1){
    					conditionMap.put("taskid", Lfmttask.getTaskId().toString());
    				}else{
    					conditionMap.put("batchid", Lfmttask.getBatchID().toString());
    				}
    			}else
    			{
    				if(Lfmttask.getTaskType()==1){
    					conditionMap.put("taskid", taskId);
    				}else{
    					conditionMap.put("batchid", Lfmttask.getBatchID().toString());
    				}
    			}
    			//计算获得历史表的表名（发送时间的月份）
    			int month=subTime.getMonth()+1;
    			String year=df.format(subTime.getTime()).substring(0,4);
				if(month<10)
				{
					//表名
					tableName="MTTASK"+year+"0"+month;
				}
				else
				{
					//表名
					tableName="MTTASK"+year+month;
				}
				//验证历史表是否存在
				tableName = new CommonBiz().getTableName(tableName);
				LinkedHashMap<String, String> conMapcount=new LinkedHashMap<String, String>();
				
				if(Lfmttask.getTaskType()==1){
					conMapcount.put("taskid",taskId);
				}else{
					conMapcount.put("batchid",String.valueOf(Lfmttask.getBatchID()));
				}
				
				//跟据taskid统计mt_datareport表中的icout值
				String iymd;
				//当前时间
				Calendar curTime=Calendar.getInstance();
				//当前时间减三天
				curTime.add(curTime.DATE, -3);
				SimpleDateFormat sidf=new SimpleDateFormat("yyyy-MM-dd");
				//截取转换成mtdatareport的iymd字段的number,以便用来查询三天前的mtdatareport表的此任务的icount字段的和
				iymd=sidf.format(curTime.getTime()).replaceAll("-", "");
				conMapcount.put("iymd", iymd);
				//mtdatareport表里面对应任务的icount的总和
				long sumCount=sendTaskRecordBiz.getSumIcount(conMapcount);
				//预发送条数
				String count=Lfmttask.getIcount()==null?"0":Lfmttask.getIcount();
				//如果三天前的mtdatareport表里的此任务的icount的总和lfmttask的预发送条数icount则查历史表和实时两张表，否则只差对应历史表
				if(sumCount<Long.parseLong(count))
				{
					//需要查询两张表的记录
					if(type != null && !type.equals("0"))
		    		{
					    MttaskvoList = sendTaskRecordBiz.getMtTaskTwo(conditionMap,orderMap, pageInfos,tableName);
		    		}
				}
				else
				{
					//只需要查询一张表的记录
					if(type != null && !type.equals("0"))
		    		{
					    MttaskvoList = sendTaskRecordBiz.getMtTask(conditionMap,orderMap, pageInfos,tableName);
		    		}
				}
				
    		}
            
    		//暂时没用到该值
			String succ_count=(Lfmttask.getSucCount()==null?"0":Lfmttask.getSucCount());
			//接收失败总数
			String r_count=String.valueOf((Lfmttask.getRfail2()==null?"0":Lfmttask.getRfail2()));
			//提交失败总数
			String fail_count=(Lfmttask.getFaiCount()==null?"0":Lfmttask.getFaiCount());
			String icount=(Lfmttask.getIcount2()==null?"0":Lfmttask.getIcount2());
			
			//提交总数
			Long icount1=Long.parseLong(icount);
			//提交失败总数的long类型
			Long fail=Long.parseLong(fail_count);
			//发送成功数
			long suc=icount1-fail;
			
			//String sendinfo ="发送总数为"+icount+"条，其中发送成功数为"+suc+"条，提交失败数为"+fail_count+"条，接收失败数为"+r_count+"条。";
			String sendinfo =MessageUtils.extractMessage("wygl","wygl_common_text90",request)+icount+MessageUtils.extractMessage("wygl","wygl_common_text91",request)+suc+MessageUtils.extractMessage("wygl","wygl_common_text92",request)+fail_count+MessageUtils.extractMessage("wygl","wygl_common_text93",request)+r_count+MessageUtils.extractMessage("wygl","wygl_common_text94",request);
			if(Lfmttask.getIcount2()==null)
			{
				//sendinfo="待汇总";
				sendinfo=MessageUtils.extractMessage("wygl","wygl_common_text95",request);
				//sendinfo ="发送总数为-条，其中发送成功数为-条，提交失败数为-条，接收失败数为-条。";
			}
			
			//获取网优通道名称
			Map<String,String> spgatesMap = sendTaskRecordBiz.getWySpgateName();
			
			//String message =(Lfmttask.getBmtType()==3?"详见明细":Lfmttask.getMsg());
			String message =(Lfmttask.getBmtType()==3?MessageUtils.extractMessage("wygl","wygl_common_text96",request):Lfmttask.getMsg());
			//短信list
			request.setAttribute("mtList",MttaskvoList);
			//网优通道list
			request.setAttribute("spgatesMap",spgatesMap);
			//分页信息
			request.setAttribute("pageInfo", pageInfos);	
			request.setAttribute("title",Lfmttask.getTitle());
			request.setAttribute("message", message);
			request.setAttribute("sendtime", Lfmttask.getTimerTime()==null?"":sdf.format(Lfmttask.getTimerTime()));
			request.setAttribute("sendinfo", sendinfo);
			//短信任务id
			request.setAttribute("mtid", mtid);
			request.setAttribute("type", type);	
			//页面查询条件
			request.setAttribute("phone", phone);
			request.setAttribute("taskId", Lfmttask.getTaskId().toString());
			request.setAttribute("spisuncm", spisuncm);
			long count = 0l;
			// 从pageinfo中获取查询总条数
			if(pageInfos != null)
			{
				count = pageInfos.getTotalRec();
			}
			// 写日志
			String opContent = "网优群发历史查询中发送详情共" + count + "条 开始：" + starthms + ",耗时:" + (System.currentTimeMillis() - startl) + "ms";
			new QueryBiz().setLog(request, "网优群发历史查询中发送详情", opContent, StaticValue.GET);
			// 页面跳转
			request.getRequestDispatcher(this.empRoot + this.basePath + "/wyq_historyRecordDetail.jsp").forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"网优历史记录中的详情查询异常！");
			try {
				request.getSession(false).setAttribute("error", e);
			} catch (Exception e2) {
				EmpExecutionContext.error(e2,"session为null");
			}			
			request.setAttribute("findresult", "-1");
			//第一次查询
			request.setAttribute("isFirstEnter", true);
			//分页信息
			request.setAttribute("pageInfo", pageInfos);
			try {
				//页面跳转
				request.getRequestDispatcher(this.empRoot  + this.basePath+"/wyq_historyRecordDetail.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"网优历史记录详情查询serlvet异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"网优历史记录详情查询serlvet跳转异常！");
			}
		}
		
	}
	
	/**
	 * 群发历史查询中的发送详情的excel导出方法(包括提交失败，接收失败的全部导出)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void smsReportAllExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		// 设置语言
		String langName  = request.getParameter("langName");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
	    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	    LinkedHashMap<String,String> orderMap = new LinkedHashMap<String,String>();
	    //企业编码
		String corpCode = request.getParameter("lgcorpcode");
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

		try {
			String mtid  = request.getParameter("mtid");
            if(mtid !=null)
            {
                conditionMap.put("mtid", mtid);
            }
            //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,4:带页面查询条件的）
            String type = request.getParameter("type");
            //获取页面传过来的查询条件
            String spisuncm = request.getParameter("spisuncm");//运营商
            String phone = request.getParameter("phone");//手机号
            String IsexportAll = request.getParameter("IsexportAll");//是否导出全部
            if(type != null)
            {
            	//接收错误
	            if(type.equals("2"))
	            {
	            	conditionMap.put("errorcode&not like", "E1:,E2:,DELIVRD");
	            }
	            //提交错误
	            else if(type.equals("3"))
	            {
	            	conditionMap.put("errorcode&like", "E1:,E2:");
	            }
	            
	            //带页面查询条件
            	if(spisuncm!=null && spisuncm.length()>0)
            	{
            		conditionMap.put("unicom", spisuncm);
            	}
            	if(phone !=null && phone.length()>0)
            	{
            		conditionMap.put("phone&like", phone);
            	}
	            
            }
            //按手机号排序
            orderMap.put("phone", "asc");
            orderMap.put("pknumber", "asc");
            String tableName="GW_MT_TASK_BAK";
            LfMttask Lfmttask = baseBiz.getById(LfMttask.class, mtid);
			String taskid=Lfmttask.getTaskId().toString();
			
			if(Lfmttask.getTaskType()==1){
				conditionMap.put("taskid", taskid);
			}else{
				conditionMap.put("batchid", String.valueOf(Lfmttask.getBatchID()));
			}
			//导出的数据集合
			List<SendedMttaskVo> MttaskvoList = null;
            //发送时间
        	Timestamp subTime=Lfmttask.getTimerTime();
        	if(subTime!=null)
        	{
        		
        		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        		Date date1=df.parse(df.format(new Date()));
        		Date date2=df.parse(df.format(subTime.getTime()));
        		//当前时间
        		Calendar c1=Calendar.getInstance();
        		//发送时间
        		Calendar c2=Calendar.getInstance();
        		c1.setTime(date1);
        		c2.setTime(date2);
        		//计算时间，当前时间减去发送时间小于3,查实时表，否则查对应月的历史表
        		c2.add(c2.DATE, 4);
        		if(c2.after(c1))
        		{
        			MttaskvoList = sendTaskRecordBiz.getMtTask(conditionMap,orderMap, null,tableName);	
        		}
        		else
        		{
        			//计算获得历史表的表名（发送时间的月份）
        			int month=subTime.getMonth()+1;
        			String year=df.format(subTime.getTime()).substring(0,4);
    				if(month<10)
    				{
    					//表名
    					tableName="MTTASK"+year+"0"+month;
    				}
    				else
    				{
    					//表名
    					tableName="MTTASK"+year+month;
    				}
    				//验证历史表是否存在
    				tableName = new CommonBiz().getTableName(tableName);
        			//跟据taskid统计mt_datareport表中的icout的条件map
    				LinkedHashMap<String, String> conMapcount=new LinkedHashMap<String, String>();
    				
    				if(Lfmttask.getTaskType()==1){
    					conMapcount.put("taskid",taskid);
    				}else{
    					conMapcount.put("batchid",String.valueOf(Lfmttask.getBatchID()));
    				}
    				
    				String iymd;
    				Calendar curTime=Calendar.getInstance();
    				curTime.add(curTime.DATE, -3);
    				SimpleDateFormat sidf=new SimpleDateFormat("yyyy-MM-dd");
    				iymd=sidf.format(curTime.getTime()).replaceAll("-", "");
    				
    				conMapcount.put("iymd", iymd);
    				//mtdatareport表里面对应任务的icount总和
    				long sumCount=sendTaskRecordBiz.getSumIcount(conMapcount);
    				//预发送条数
    				String count=Lfmttask.getIcount()==null?"0":Lfmttask.getIcount();
    				//如果三天前的mtdatareport表里的此任务的icount的总和lfmttask的预发送条数icount则查历史表和实时两张表，否则只差对应历史表
    				if(sumCount<Long.parseLong(count))
    				{
    					//需要查询两张表的记录
    					MttaskvoList = sendTaskRecordBiz.getMtTaskTwo(conditionMap,orderMap, null,tableName);
    				}
    				else
    				{
    					MttaskvoList = sendTaskRecordBiz.getMtTask(conditionMap,orderMap, null,tableName);	
    				}
    				
        		}
        	}
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
    		//查询出来的记录不为空时，去创建需要导出的excel
			if (MttaskvoList != null && MttaskvoList.size()>0 ) 
			{
				HistoryRecordExcelTool et=new HistoryRecordExcelTool(excelPath);
				Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
				int ishidephome=0;
				if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
					ishidephome=1;
				}
				//获取网优通道
				Map<String,String> spgatesMap = sendTaskRecordBiz.getWySpgateName();
				Map<String, String> resultMap = et.createSmsMtReportExcel(langName,MttaskvoList, spgatesMap, IsexportAll,ishidephome);
				String fileName=(String)resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出网优群发历史详情：" +MttaskvoList.size() + "条  ,文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
				new QueryBiz().setLog(request, "网优群发历史详情", opContent, StaticValue.GET);
				MttaskvoList.clear();
				MttaskvoList=null;
				HttpSession session =request.getSession(false);
				session.setAttribute("wydetail_export_map",resultMap);
				out.print("true");
//		        String filePath=(String)resultMap.get("FILE_PATH");
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
//		        //用于判断是否下载加载完成了
//		        request.getSession().setAttribute("checkOver"+userId, "true");	       
			}
			else
			{
				out.print("false");
				//response.sendRedirect(request.getContextPath()+"/wyq_historyRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
				EmpExecutionContext.error(e,"网优历史记录详情的excel导出异常！");
			   //异常打印
			   response.sendRedirect(request.getContextPath()+"/wyq_historyRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		} 
	}
	
	
	/**
	 * 网优群发历史详情导出下载
	 * @param request
	 * @param response
	 */
	public void wydetaildownloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(false);
		Object obj = null;
		try {
			obj = session.getAttribute("wydetail_export_map");
			session.removeAttribute("wydetail_export_map");
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
	
	
	
	
	
	
	/**
	 * 机构树
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{		
			//部门id
			Long depId = null;
			
			String depStr = request.getParameter("depId");
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			String departmentTree = sendTaskRecordBiz.getDepartmentJosnData2(depId,null);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e) 
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"网优群发任务或群发历史机构树加载异常！");
		}
	}
	/**
	 * 群发历史页面获取操作员树的方法 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree2(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		// 设置语言
		String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
		String depId = request.getParameter("depId");
		try
		{		
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			String departmentTree = sendTaskRecordBiz.getDepUserJosn(langName,depId, curUser);
			
			if(departmentTree == null)
			{
				response.getWriter().print("");
			}
			else
			{
				response.getWriter().print(departmentTree);
			}
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "网优群发历史，生成机构操作员树字符串，异常。depId="+depId);
			response.getWriter().print("");
		}
	}
	
	/**
	 * 群发历史查询页面发送详情页面中的返回按钮调用的查询方法
	 * @param request
	 * @param response
	 */
	public void findallLfMttask(HttpServletRequest request, HttpServletResponse response)
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String path = "/wyq_historyRecord.jsp";
		String titlePath = "historyRecord";
		PageInfo pageInfo = new PageInfo();
		try
		{
			//企业编码
			String corpCode =request.getParameter("lgcorpcode");
			//登录用户的userid
			//String  userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);

			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			//排序
			orconp.put("corpCode", "asc");
			List<LfBusManager> busList = baseBiz.getByCondition(
					LfBusManager.class, conditionMap, orconp);
			
			conditionMap.clear();
			conditionMap.put("platFormType", "1");
			if(corpCode !=null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode", corpCode);
			}

			QueryBiz qbiz=new QueryBiz();
			// 获取绑定的sp账号
			List<String> lfsp = qbiz.getSpuserList("0", corpCode, StaticValue.getCORPTYPE());

			//sp账号
			String spUser = request.getParameter("spUser");
			//部门id
			String depid = request.getParameter("depid");
			//用户id
			String userid = request.getParameter("userid");
			//开始时间
			String startSubmitTime = request.getParameter("sendtime");
			//结束时间
			String endSubmitTime = request.getParameter("recvtime");
			//业务编码
            String busCode = request.getParameter("busCode");
            //主题
            String title = request.getParameter("title");
            
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            //查询移动财务短信及群发短信
            mtVo.setMsTypes("1,5");
			
			mtVo.setSpUser(spUser);
			if (busCode!=null && !busCode.equals(""))
				mtVo.setBusCode(busCode);
			mtVo.setDepIds(depid);
			mtVo.setUserIds(userid);

			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}
			
			mtVo.setOverSendstate("1,2,3,6");
			//查询发送时间
			if (endSubmitTime !=null && !endSubmitTime.equals("")){
				mtVo.setEndSendTime(endSubmitTime);
			}
			if (startSubmitTime !=null && !startSubmitTime.equals("")){
				mtVo.setStartSendTime(startSubmitTime);
			}
			
			if(corpCode != null && corpCode.equals("100000"))
			{
				mtVoList = sendTaskRecordBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
			}else
			{
				mtVoList = sendTaskRecordBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
			}

			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", false);
			request.setAttribute("sendUserList", lfsp);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			
			String departmentTree = sendTaskRecordBiz.getDepartmentJosnData(Long.parseLong(userId));
			//如果用户请求是"群发历史查询"			
			request.setAttribute("departmentTree", departmentTree);

			request.setAttribute("titlePath", titlePath);
			
			long count=0l;
			//从pageinfo中获取查询总条数
			if(pageInfo!=null){
				count=pageInfo.getTotalRec();
			}
			// 写日志
			String opContent = "网优群发历史查询点击返回：" + count + "条 开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
			new QueryBiz().setLog(request, "网优群发历史查询点击返回", opContent, StaticValue.GET);
			
			request.getRequestDispatcher(this.empRoot+this.basePath+path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e," 网优历史记录详情页面中的返回列表异常！");
			request.setAttribute("findresult", "-1");
			//回到页面第一次加载时的状态
			request.setAttribute("isFirstEnter", true);
			request.setAttribute("pageInfo", pageInfo);
			
			try {
				request.getRequestDispatcher(this.empRoot+this.basePath+path)
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"网优历史记录详情返回serlvet异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"网优历史记录详情返回serlvet跳转异常！");
			}
		}
	}
	
	/**
	 * 群发历史查询的excel导出方法(导出全部)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportCurrPageExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		// 设置语言
		String langName  = request.getParameter("langName");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		//下行短信list
		List<LfMttaskVo> mtVoList = null;
		//路径
		//String context=request.getSession().getServletContext().getRealPath("/fileUpload/excelDownload");
		LfMttaskVo mtVo = new LfMttaskVo();
		//企业编码
		String corpCode = request.getParameter("lgcorpcode");
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);


        try
        {
        	String title = (String) request.getSession(false).getAttribute("r_sendTitle");
        	String spUser = request.getParameter("spuser");
			String depids = (String)request.getSession(false).getAttribute("r_depIds");
			String userids = (String)request.getSession(false).getAttribute("r_userIds");

			String startSubmitTime = (String)request.getSession(false).getAttribute("r_sendTime");
			String endSubmitTime = (String)request.getSession(false).getAttribute("r_recvTime");
			String mtSendState = (String)request.getSession(false).getAttribute("r_mtSendState");
			
			 String taskType= request.getParameter("taskType");
			 
			 if(taskType!=null&&!"".equals(taskType)){
	            	mtVo.setTaskType(Integer.parseInt(taskType));
	         }
			 
			 //增加批次号
			 String taskID=request.getParameter("taskID");
			 if(taskID!=null&&!"".equals(taskID.trim())){
				 try{
	            		mtVo.setTaskId(Long.parseLong(taskID.trim()));
	            	}catch (Exception e) {
	            		EmpExecutionContext.error(e, "网优群发历史导出excel设置taskid异常。");
					}
	         }
			 
			 //是否包含子机构
	            String isContainsSun=request.getParameter("isContainsSun");
	            if(isContainsSun!=null&&!"".equals(isContainsSun)){
	            	mtVo.setIsContainsSun(isContainsSun);
	            }
        	
			PageInfo pageInfo = new PageInfo();
			//分页信息
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			orconp.put("corpCode", "asc");
			//清除map
			conditionMap.clear();
			conditionMap.put("platFormType", "1");
			conditionMap.put("isValidate", "1");
			if(corpCode !=null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode", corpCode);
			}

			//业务编码
            String busCode = request.getParameter("busCode");
            //查询移动财务短信及群发短信
            mtVo.setMsTypes("1,5");
            
            mtVo.setSpUser(spUser);
			if (busCode!=null && !busCode.equals(""))
				mtVo.setBusCode(busCode);
			mtVo.setDepIds(depids);
			mtVo.setUserIds(userids);
			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}
			mtVo.setOverSendstate(mtSendState);
			//mtVo.setOverSendstate("1,2,3,6");
			//查询发送时间
			if (!"".equals(endSubmitTime)&& null != endSubmitTime)
				mtVo.setEndSendTime(endSubmitTime);
			if (!"".equals(startSubmitTime)&& null != startSubmitTime)
				mtVo.setStartSendTime(startSubmitTime);
			
			if(corpCode != null && corpCode.equals("100000"))
			{
				//mtVoList = mtBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
				mtVoList = sendTaskRecordBiz.getLfMttaskVoWithoutDomination( mtVo, null);
			}else
			{
				//mtVoList = mtBiz.getLfMttaskVo(lfSysuser.getUserId(), mtVo, pageInfo);
				mtVoList = sendTaskRecordBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, null);
			}
			response.setContentType("html/text");
			PrintWriter out = response.getWriter();
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
				HistoryRecordExcelTool et = new HistoryRecordExcelTool(excelPath);
				Map<String, String> resultMap = et.createMtReportExcel(langName,mtVoList);
				String fileName=(String)resultMap.get("FILE_NAME");
				// 写日志
				String opContent = "导出网优群发历史：" +mtVoList.size() + "条  ,文件名："+fileName+",开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms";
				new QueryBiz().setLog(request, "网优群发历史", opContent, StaticValue.GET);
				mtVoList.clear();
				mtVoList=null;
				HttpSession session =request.getSession(false);
				session.setAttribute("wyhis_export_map",resultMap);
				out.print("true");
//		        String filePath=(String)resultMap.get("FILE_PATH");
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
			}
			else
			{
				out.print("false");
			}
		} catch (Exception e) {		
			   //异常打印
				response.sendRedirect(request.getContextPath()+"/wyq_historyRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			    EmpExecutionContext.error(e,"网优群发历史查询的excel导出异常！");
//			   this.find(request, response);
		} 
	}
	
	
	
	
	/**
	 * 网优群发历史导出下载
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		HttpSession session = request.getSession(false);
		Object obj = null;
		try {
			obj = session.getAttribute("wyhis_export_map");
			session.removeAttribute("wyhis_export_map");
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
	
	
	
	
	/**
	 * 群发任务查看的excel导出(导出全部)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportAllPageExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		//企业编码
		String corpCode = request.getParameter("lgcorpcode");
		//用户id
	//	String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

        try
        {
        	PageInfo pageInfo = new PageInfo();
        	//分页信息
			pageSet(pageInfo,request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			//排序
			orconp.put("corpCode", "asc");
			
			conditionMap.clear();
			conditionMap.put("platFormType", "1");
			conditionMap.put("isValidate", "1");
			if(corpCode !=null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode",corpCode);
			}

			String spUser = request.getParameter("spuser");
			String depid = request.getParameter("depid");
			//用户id
			String userid = request.getParameter("userid");

			//开始时间
			String startSubmitTime = request.getParameter("sendtime");
			//结束时间
			String endSubmitTime = request.getParameter("recvtime");

            String busCode = request.getParameter("busCode");
            String title = request.getParameter("title");
            //任务状态查询
            String taskState= request.getParameter("taskState");
            
            String taskType= request.getParameter("taskType");
            
            //增加批次号
			 String taskID=request.getParameter("taskID");
			 if(taskID!=null&&!"".equals(taskID.trim())){
				 try{
	            		mtVo.setTaskId(Long.parseLong(taskID.trim()));
	            	}catch (Exception e) {
	            		EmpExecutionContext.error(e, "网优群发历史导出全部excel设置taskid异常。");
					}
	         }
            
			 	//是否包含子机构
	            String isContainsSun=request.getParameter("isContainsSun");
	            if(isContainsSun!=null&&!"".equals(isContainsSun)){
	            	mtVo.setIsContainsSun(isContainsSun);
	            }
			 
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
          //查询移动财务短信及群发短信
            mtVo.setMsTypes("1,5");
            //任务状态查询
            mtVo.setTaskState(taskState);
            
            if(taskType!=null&&!"".equals(taskType)){
            	mtVo.setTaskType(Integer.parseInt(taskType));
            }
            
            mtVo.setSpUser(spUser);
			if (busCode!=null && !busCode.equals(""))
				mtVo.setBusCode(busCode);
			mtVo.setDepIds(depid);
			mtVo.setUserIds(userid);
			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}

			//查询时间
			if (!"".equals(endSubmitTime))
				mtVo.setEndSubmitTime(endSubmitTime);
			if (!"".equals(startSubmitTime))
				mtVo.setStartSubmitTime(startSubmitTime);
			
			if(corpCode != null && corpCode.equals("100000"))
			{
				mtVoList = sendTaskRecordBiz.getLfMttaskVoWithoutDomination( mtVo, null);
			}else
			{
				mtVoList = sendTaskRecordBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, null);
			}
			
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
        
				HistoryRecordExcelTool et=new HistoryRecordExcelTool(excelPath);
				Map<String, String> resultMap = et.createSmsSendedBoxExcel(mtVoList);
				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        	
		        DownloadFile dfs=new DownloadFile();
		        dfs.downFile(request, response, filePath, fileName);
			}
			else
			{
				response.sendRedirect(request.getContextPath()+"/smt_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
			//异常处理
			response.sendRedirect(request.getContextPath()+"/smt_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		    EmpExecutionContext.error(e,"网优群发任务查看的excel导出异常！");
		} 
	}
	
	protected int getIntParameter(HttpServletRequest request,String param, int defaultValue)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		} catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取整形异常。");
			//异常处理
			return defaultValue;
		}
	}
	
	/**
	 * 检查结果值
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkResult(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter(); 
		HttpSession session = request.getSession(false);
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

		try {
			if(session.getAttribute("checkOver"+userId) !=null)
			{
				out.print("over");	
				session.removeAttribute("checkOver"+userId);
			}
		} catch (Exception e) {
			out.print("over");
			EmpExecutionContext.error(e,"检查结果值异常！");
		}
	}
	
	/**
	 *   获取信息发送查询里的详细
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public void getSmsDetail(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		//时分秒格式化
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		//开始时间
		String starthms=hms.format(startl);
		//彩信任务ID
		String mtId = request.getParameter("mtId");
		//操作员用户ID
		String userId = request.getParameter("userId");
		try
		{
			LfSysuser user = baseBiz.getById(LfSysuser.class, userId);
			//获取对应的彩信任务
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			LfMttask mtTask = baseBiz.getById(LfMttask.class, mtId);
			JSONObject jsonObject = new JSONObject();
			//设置条件的MAP
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			conditionMap.put("ProUserCode",String.valueOf(mtTask.getUserId()));
			conditionMap.put("infoType", "1");
			conditionMap.put("mtId",mtTask.getTaskId().toString());
			conditionMap.put("RState&in","1,2");
			conditionMap.put("isComplete","1");
			orderByMap.put("RLevel", StaticValue.ASC);
			orderByMap.put("preRv", StaticValue.DESC);
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			JSONArray members = new JSONArray(); 
			//标识该审核中最大的级
			//LfFlowRecord maxRecord = null; 
			if(flowRecords != null && flowRecords.size()>0){
				LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
				conditionMap.clear();
				String auditName = "";
				for(int j=0;j<flowRecords.size();j++){
					LfFlowRecord temp = flowRecords.get(j);
					auditName = auditName + temp.getUserCode() + ",";
				}
				//获取该审核流程最大的审批级别
				//maxRecord = flowRecords.get(flowRecords.size()-1);
				
				List<LfSysuser> sysuserList = null;
				if(auditName != null && !"".equals(auditName)){
					auditName = auditName.substring(0, auditName.length()-1);
					conditionMap.put("userId&in", auditName);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size()>0){
						for(LfSysuser sysuser:sysuserList){
							nameMap.put(sysuser.getUserId(), sysuser.getName());
						}
					}
				}
				
				//是否有审批信息1有  2 没有
				jsonObject.put("haveRecord","1");
				JSONObject member = null; 
				LfFlowRecord record = null;
				for(int i=0;i<flowRecords.size();i++){
					member = new JSONObject(); 
					record = flowRecords.get(i);
					member.put("mmsRlevel", record.getRLevel().toString() + "/" + record.getRLevelAmount().toString());
					//审批人
					if(nameMap != null && nameMap.size()>0 && nameMap.containsKey(record.getUserCode())){
						member.put("mmsReviname",nameMap.get(record.getUserCode()));
					}else{
						member.put("mmsReviname","-");
					}
					if(record.getRTime()== null){
						member.put("mmsrtime", "-");
					}else{
						member.put("mmsrtime", df.format(record.getRTime()));
					}
					//审批结果
					int state = record.getRState();
					switch(state)
					{
						case -1:
							member.put("mmsexstate","未审批");
							break;
						case 1:
							member.put("mmsexstate","审批通过");
							break;
						case 2:
							member.put("mmsexstate","审批不通过");
							break;
						default:
							member.put("mmsexstate","[无效的标示]");
					} 
					
					if("".equals(record.getComments()) || record.getComments() == null){
						member.put("mmsComments","");
					}else{
						member.put("mmsComments",record.getComments());
					}
					members.add(member);
				}
				jsonObject.put("members",members);
			}else{
				jsonObject.put("haveRecord","2");
			}
			
			conditionMap.clear();
			String firstshowname = "";
			String firstcondition = "";
			//一级都没有审核
				//获取下一级审核
			conditionMap.put("ProUserCode",String.valueOf(mtTask.getUserId()));
			conditionMap.put("infoType", "1");
			conditionMap.put("mtId",mtTask.getTaskId().toString());
			conditionMap.put("RState","-1");
			conditionMap.put("isComplete","2");
			List<LfFlowRecord> unflowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			LfFlowRecord lastrecord = null;
			Long depId = null;
			String[] recordmsg = new String[2];
			recordmsg[0] = "";
			recordmsg[1] = "";
			String isshow = "2";
			if(unflowRecords != null && unflowRecords.size()>0){
				isshow = "1";
				StringBuffer useridstr = new StringBuffer();
				for(LfFlowRecord temp:unflowRecords){
					useridstr.append(temp.getUserCode()).append(",");
				}
				if(lastrecord == null){
					lastrecord = unflowRecords.get(0);
				}
				List<LfSysuser> sysuserList = null;
				if(useridstr != null && useridstr.length() != 0){
					String str = useridstr.toString().substring(0, useridstr.toString().length()-1);
					conditionMap.clear();
					conditionMap.put("userId&in", str);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size()>0){
						for(LfSysuser sysuser:sysuserList){
							firstshowname = firstshowname + sysuser.getName() + "&nbsp;&nbsp;";
							if(depId == null){
								depId = sysuser.getDepId();
							}
						}
					}
				}
				if(lastrecord != null){
					//审核类型  1操作员  4机构  5逐级审核
					Integer rtype = lastrecord.getRType();
					firstcondition = lastrecord.getRCondition()+"";
					ReviewBiz reviewBiz = new ReviewBiz();
					//当是逐步审批的时候
					if(rtype == 5){
						//获取逐级审批
						boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lastrecord.getPreRv().intValue(), lastrecord.getProUserCode());
						//逐级审批的最后一级
						if(isLastLevel){
							if(lastrecord.getRLevelAmount() != 1){
								lastrecord.setRLevel(1);
								recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
							}
						}else{
							LfDep dep = baseBiz.getById(LfDep.class, depId);
							if(dep != null){
								LfDep pareDep = baseBiz.getById(LfDep.class, dep.getSuperiorId());
								if(pareDep != null){
									recordmsg[0] = pareDep.getDepName();
									recordmsg[1] = lastrecord.getRCondition()+"";
								}
							}
						}
					}else{
						//该流程审批的最后一级
						if( !(lastrecord.getRLevel()).equals(lastrecord.getRLevelAmount())){
							recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
						}
					}
				}
			}
			jsonObject.put("isshow",isshow);
			jsonObject.put("firstshowname",firstshowname);
			jsonObject.put("firstcondition",firstcondition);
			jsonObject.put("secondshowname",recordmsg[0]);
			jsonObject.put("secondcondition",recordmsg[1]);
			
			response.getWriter().print(jsonObject.toString());
		}catch (Exception e){
			response.getWriter().print("fail");
			EmpExecutionContext.error(e,"群发任务 获取信息发送查询里的详细异常！");
		}
	}
	
}