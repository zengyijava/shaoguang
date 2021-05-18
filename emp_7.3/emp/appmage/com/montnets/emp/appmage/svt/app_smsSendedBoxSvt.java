package com.montnets.emp.appmage.svt;

import com.montnets.emp.appmage.biz.SmstaskBiz;
import com.montnets.emp.appmage.biz.SmstaskExcelTool;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfMttaskVo;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.gateway.AcmdQueue;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.http.conn.HttpHostConnectException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;




/**
 * 群发历史与群发任务查询的方法
 * @project emp
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 上午10:33:27
 * @description 
 */
@SuppressWarnings("serial")
public class app_smsSendedBoxSvt extends BaseServlet {

	//String titlePath="";
	private static final SmstaskBiz smstaskBiz=new SmstaskBiz();
	static String empRoot="appmage";
	static String basePath="/smstask";
	static SmsBiz smsBiz = new SmsBiz();
	static SuperOpLog spLog = new SuperOpLog();
	static BaseBiz baseBiz = new BaseBiz();
	private static final String path=new TxtFileUtil().getWebRoot();
	//模板路径
	protected static String  excelPath = path + "appmage/smstask/file/";

	//常用文件读写工具类
	static TxtFileUtil txtfileutil = new TxtFileUtil();
	
	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
		//短信biz
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String requestPath = request.getRequestURI();
		String path = requestPath.substring(requestPath.lastIndexOf("/")).replaceAll(".htm", ".jsp");
		String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord

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
			//conditionMap.put("platFormType", "1");
			//conditionMap.put("isValidate", "1");
            //如果不是10000用户登录，则需要带上企业编码查询
			if(corpCode !=null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode", corpCode);
			}
			conditionMap.put("isValidate", "1");
            //查找绑定的sp账号
			List<LfSpDepBind> userList = baseBiz.getByCondition(
					LfSpDepBind.class, conditionMap, null);

			String spUser = request.getParameter("spUser");
			String depid = request.getParameter("depid");
			String userid = request.getParameter("userid");
			String startSubmitTime = request.getParameter("sendtime");
			String endSubmitTime = request.getParameter("recvtime");
            String busCode = request.getParameter("busCode");
            String title = request.getParameter("title");
            String taskState= request.getParameter("taskState");
            String taskType= request.getParameter("taskType");
            
            mtVo.setCorpCode(corpCode);
            //任务批次查询条件
            String taskID=request.getParameter("taskID");
            if(taskID!=null&&!"".equals(taskID.trim())){
            	try{
            		mtVo.setTaskId(Long.parseLong(taskID.trim()));
            	}catch (Exception e) {
            		EmpExecutionContext.error("任务ID转换异常，taskID:" + taskID);
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
            
           //查询APP群发短信
            mtVo.setMsTypes("8");
            mtVo.setTaskState(taskState);
            if(taskType!=null&&!"".equals(taskType)){
            	mtVo.setTaskType(Integer.parseInt(taskType));
            }
			if(!isFirstEnter){
				mtVo.setSpUser(spUser);
				if (busCode!=null && !busCode.equals("")) {
                    mtVo.setBusCode(busCode);
                }
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);

				if(title != null && title.length() > 0)
				{
					mtVo.setTitle(title);
				}
				//如果用户请求"群发历史查询"，则查询出所有已发历史记录
				if(titlePath.equals("smsTaskRecord"))
				{
					mtVo.setOverSendstate("1,2,3,6");
					//查询发送时间
					if (!"".equals(endSubmitTime)) {
                        mtVo.setEndSendTime(endSubmitTime);
                    }
					if (!"".equals(startSubmitTime)) {
                        mtVo.setStartSendTime(startSubmitTime);
                    }
				}else
				{	
					//查询创建时间
					if (!"".equals(endSubmitTime)) {
                        mtVo.setEndSubmitTime(endSubmitTime);
                    }
					if (!("").equals(startSubmitTime)) {
                        mtVo.setStartSubmitTime(startSubmitTime);
                    }
				}
				
				//设置导出的查询条件
				request.getSession(false).setAttribute("r_sendTime", mtVo.getStartSendTime());
				request.getSession(false).setAttribute("r_recvTime", mtVo.getEndSendTime());
				request.getSession(false).setAttribute("r_userIds", mtVo.getUserIds());
				request.getSession(false).setAttribute("r_depIds", mtVo.getDepIds());
				request.getSession(false).setAttribute("r_sendTitle", mtVo.getTitle());
				
				//群发历史新增发送状态查询条件
				if("/app_appsmsTaskRecord.jsp".equals(path)){
					  String sendstate=request.getParameter("sendstate");
					  if(sendstate!=null&&!"".equals(sendstate)){
						  if(!"0".equals(sendstate)){
								mtVo.setOverSendstate(sendstate);
						  }
					  }
				}
				
				//查询lfmttask信息
				if(corpCode != null && corpCode.equals("100000"))
				{
					mtVoList = smstaskBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
				}else
				{
					mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
				}
				
				if(isContainsSun==null||"".equals(isContainsSun)){
					request.setAttribute("isContainsSun", "0");
				}else{
					request.setAttribute("isContainsSun", "1");
				}
			}

			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", userId);//当前登录用户id
			//加载机构树
			String departmentTree = smstaskBiz.getDepartmentByCorpCode(Long.parseLong(userId),corpCode);
			//如果用户请求是"群发历史查询"
			request.setAttribute("departmentTree", departmentTree);
			request.setAttribute("titlePath", titlePath);
			
			//当前登录用户的登录名
			Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
			String lguserName = null;
			if(sysuserObj != null)
			{
				LfSysuser sysuser = (LfSysuser) sysuserObj;
				lguserName = (sysuser != null && sysuser.getUserName()!= null 
						&& !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
			}
			//查询出的数据的总数量
			int totalCount = pageInfo.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP短信群发历史", corpCode, userId, lguserName, opContent, "GET");
			
			//页面跳转
			request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error("群发历史与群发任务查询方法请求URL:" + request.getRequestURI()+ "，请求参数，corpCode：" + corpCode + "，userId :" + userId);
			EmpExecutionContext.error(e,"群发历史与群发任务查询的方法异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
			request.setAttribute("titlePath", titlePath);
			request.setAttribute("pageInfo", pageInfo);
			//如果从短信助手或短信客服请求群发任务查看，则统一转向app_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/app_smsSendedBox.jsp";
			}
			try {
				request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"群发任务查询serlvet异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"群发任务查询serlvet跳转异常！");
			}
		}
	}
	
	/**
	 * 群发历史页面回复详情
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void getVerify(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		
		try{
			String  mtID = request.getParameter("mtId");
			LfMttask mttask = baseBiz.getById(LfMttask.class, mtID);
			StringBuffer buffer =null;
			buffer = new StringBuffer("<table border='1' width='100%' style='text-align:center'><tr  class='div_bd'><td class='div_bd'>审批级别</td>" +
					"<td class='div_bd'>审批人</td><td class='div_bd'>审批状态</td>" +
					"<td class='div_bd'>审批意见</td></tr><tbody>");
			//设置条件的MAP
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			conditionMap.put("ProUserCode",String.valueOf(mttask.getUserId()));
			conditionMap.put("infoType", "1");
			conditionMap.put("mtId",mtID);
			conditionMap.put("RState&in","1,2");
			conditionMap.put("isComplete","1");
			orderByMap.put("RLevel", StaticValue.ASC);
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			
			if(flowRecords != null && flowRecords.size()>0){
				LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
				conditionMap.clear();
				String auditName = "";
				for(int j=0;j<flowRecords.size();j++){
					LfFlowRecord temp = flowRecords.get(j);
					auditName = auditName + temp.getUserCode() + ",";
				}
				List<LfSysuser> sysuserList = null;
				if(auditName != null && !"".equals(auditName)){
					auditName = auditName.substring(0, auditName.length()-1);
					conditionMap.put("userId&in", auditName);
					conditionMap.put("corpCode", mttask.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size()>0){
						for(LfSysuser sysuser:sysuserList){
							nameMap.put(sysuser.getUserId(), sysuser.getName());
						}
					}
				}
				
				LfFlowRecord record = null;
				if(flowRecords!=null && flowRecords.size() != 0)
				{
					for(int i=0 ;i<flowRecords.size();i++)
					{
						record = flowRecords.get(i);
						buffer.append("<tr  class='div_bd'>");
						buffer.append("<td  class='div_bd'>").append(record.getRLevel().toString()).append("</td>");
						if(nameMap != null && nameMap.size()>0 && nameMap.containsKey(record.getUserCode())){
							buffer.append("<td  class='div_bd'>").append(nameMap.get(record.getUserCode())).append("</td>");
						}else{
							buffer.append("<td  class='div_bd'>-</td>");
						}
						if(record.getRState()==1) {
                            buffer.append("<td  class='div_bd'>").append("审批通过").append("</td>");
                        }
						if(record.getRState()==2) {
                            buffer.append("<td  class='div_bd'>").append("审批不通过").append("</td>");
                        }
						if(record.getRState()==-1) {
                            buffer.append("<td  class='div_bd'>").append("未审批").append("</td>");
                        }
						if(record.getComments()!=null&&!"".equals(record.getComments())) {
                            buffer.append("<td style='word-break: break-all;'  class='div_bd'><xmp style='word-break: break-all;white-space:normal;'>").append(record.getComments()).append("</xmp></td>");
                        } else{
							buffer.append("<td  class='div_bd'>-").append("</td>");
						}
						buffer.append("</tr>");
					}
				}else
				{
					buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>无记录</td></tr>");
				}
			}else
			{
				buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>无记录</td></tr>");
			}
			buffer.append("</tbody></table>");
			response.getWriter().print(buffer.toString());
		}catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e,"群发历史页面回复详情异常！");
		}
	}
	/**
	 * 查询条件中的机构树加载方法
	 * @param request
	 * @param response
	 * @throws Exception
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{
			//获取当前登录用户的userid
			String userid = request.getParameter("lguserid");
			//获取机构树信息
			String departmentTree = smstaskBiz.getDepartmentJosnData(Long.parseLong(userid));		
			//将信息传给页面
			writer.print(departmentTree);		
			EmpExecutionContext.info(departmentTree);	
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e);
		}
	 }
	 */	
	
	/**
	 * 查询条件中的机构树加载方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	//输出机构代码数据	
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{
			Long depId = null;
			Long userid=null;
			//部门iD
			String depStr = request.getParameter("depId");
			LfSysuser user=getLoginUser(request);
			String corpCode="";
			if(user!=null){
				corpCode=user.getCorpCode();
			}
			//操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);

			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim())){
				userid = Long.parseLong(userStr);
			}
			
			String departmentTree = smstaskBiz.getDepByCorpCodeAndUser(depId, userid,corpCode);			
			response.getWriter().print(departmentTree);		
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"群发历史或群发任务查询条件中的机构树加载方法异常");
		}
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
			//获取当前登录用户的userid
			//String userid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userid = SysuserUtil.strLguserid(request);

			String deptUserTree="";
			String requestPath = request.getRequestURI();
			LfSysuser user=getLoginUser(request);
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/app_smsTaskRecord.jsp->smsTaskRecord
			String corpCode="";
			if(user!=null){
				corpCode=user.getCorpCode();
			}
//			if(titlePath.equals("smsTaskRecord"))
//			{
//				deptUserTree = getDeptUserJosnData(titlePath,Long.parseLong(userid));			
//			}
//			else{
				deptUserTree = getDeptUserJosnData(titlePath,Long.parseLong(userid),corpCode);			
//			}
			response.getWriter().print(deptUserTree);
			EmpExecutionContext.info(deptUserTree);
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"群发历史或群发任务查询条件中操作员树的加载方法异常！");
		}
	}	

	/**
	 * 操作员树的加载方法
	 * @param titlePath
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long userid,String corpCode) throws Exception{
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
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid,corpCode);
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
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树的加载方法异常！");
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
	public  String getDeptUserJosnData(String titlePath,Long depId,Long userid, HttpServletRequest request) throws Exception{
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
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,currUser.getCorpCode());
					}
					
					
				}else
				{
					if(depId == null){
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid,currUser.getCorpCode()).get(0);
						lfDeps.add(lfDep);						
					}else{
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,currUser.getCorpCode());
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
						lfSysusers = smstaskBiz.getAllSysusersOfSmsTaskRecordByDep(userid,depId);
						
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
							tree.append(",name:'").append(lfSysuser.getName()).append(MessageUtils.extractMessage("appmage", "appmage_smstask_text_36", request)).append("'");;
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
				EmpExecutionContext.error(e,"群发历史或群发任务操作员树的加载方法异常！");
			}
		}

		return tree ==null ? "[]" : tree.toString();
	}

	
	/**
	 * 回复查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void getSinglePerNoticDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try
		{
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			JSONObject json = new JSONObject();  
			response.setContentType("text/html;charset=UTF-8");
			//短信任务id
			Long mtId = Long.valueOf(request.getParameter("mtId"));			
		 	Integer pageIndex = Integer.valueOf(request.getParameter("pageIndex"));		
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	CommonVariables  CV = new CommonVariables();
	    	Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
			int ishidephome=0;//是否隐藏手机号
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
				ishidephome=1;
			}
	    	//根据短信id，获取短信信息
	    	LfMttask mt=baseBiz.getById(LfMttask.class, mtId);
	    	
	    	//MttaskBiz mtBiz = new MttaskBiz();
		 	//List<MotaskReplyVo> reply = null;
		 	//MotaskReplyVo mo = null;
	    	//分页信息
	    	PageInfo pageInfo = new PageInfo();
		 	pageSet(pageInfo,request);
		 	pageInfo.setPageIndex(pageIndex);
		 	//每页显示ide条数
		 	pageInfo.setPageSize(10);
		 	//短信id
			if(mt!=null)
			{
				//taskid
				conditionMap.put("taskId", mt.getTaskId().toString());
				//回复信息
			 	List<LfMotask> reply=baseBiz.getByCondition(LfMotask.class, null, conditionMap, null, pageInfo);
			 	LfMotask mo=new LfMotask();
			 	if(reply != null){
			 		JSONArray members = new JSONArray();  
			 		for(int i=0;i<reply.size();i++){   
			 			String moblie = "";
			 			String time = "";
			 			String content = "";
			 			mo =  reply.get(i);
			            JSONObject member = new JSONObject();   
			            
			            moblie = ishidephome==1?mo.getPhone():CV.replacePhoneNumber(mo.getPhone());
			            if(moblie != null && !"".equals(moblie)){
				            member.put("moblie",moblie);	
			            }else{
			            	member.put("moblie","-");
			            }
			            Timestamp receiveTime = mo.getDeliverTime();
			            //if(receiveTime != null && !"".equals(receiveTime)){  //findbugs
			            if(receiveTime != null){
			            	time  = simpleDateFormat.format(receiveTime).toString();
			            	member.put("time",time);
			            }else
			            {
			            	member.put("time","-");
			            }
			            member.put("sendDate", time);			
			            content = mo.getMsgContent();
			            if(content == null || "".equals(content)){
			            	content = "";
			            }
			            member.put("content", content);				
			            
			            members.add(member);
			        }   
			        json.put("jobs", members);   
			        json.put("index", pageInfo.getPageIndex());
			        json.put("count", pageInfo.getTotalRec()); 
			        json.put("pageSize", pageInfo.getPageSize());   
			        json.put("preNoticeId", String.valueOf(mtId));
			        
			        //System.out.println(json.toString()); 
			 	}
			 	//将json数据返回页面
			      response.getWriter().write(json.toString());   
			}
			else
			{
				//没有数据
				response.getWriter().write("noList");
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务回复详情查询异常！");
			//异常错误
			response.getWriter().write("error");   
		}
	}
	/**
	 * 群发历史查询中发送详情信息查看
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings({ "static-access", "deprecation" })
	public void findAllSendInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception{	
		SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
		//查询开始时间
		long stratTime = System.currentTimeMillis();
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
    			    MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,tableName);
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
				long sumCount=smstaskBiz.getSumIcount(conMapcount);
				//预发送条数
				String count=Lfmttask.getIcount()==null?"0":Lfmttask.getIcount();
				//如果三天前的mtdatareport表里的此任务的icount的总和lfmttask的预发送条数icount则查历史表和实时两张表，否则只差对应历史表
				if(sumCount<Long.parseLong(count))
				{
					//需要查询两张表的记录
					if(type != null && !type.equals("0"))
		    		{
					    MttaskvoList = smstaskBiz.getMtTaskTwo(conditionMap,orderMap, pageInfos,tableName);
		    		}
				}
				else
				{
					//只需要查询一张表的记录
					if(type != null && !type.equals("0"))
		    		{
					    MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,tableName);
		    		}
				}
				
    		}
            
    		//暂时没用到该值
			//String succ_count=(Lfmttask.getSucCount()==null?"0":Lfmttask.getSucCount());
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
			
			String sendinfo ="发送总数为"+icount+"条，其中发送成功数为"+suc+"条，提交失败数为"+fail_count+"条，接收失败数为"+r_count+"条。";
			if(Lfmttask.getIcount2()==null)
			{
				sendinfo="待汇总";
				//sendinfo ="发送总数为-条，其中发送成功数为-条，提交失败数为-条，接收失败数为-条。";
			}
			String message =(Lfmttask.getBmtType()==3?"详见明细":Lfmttask.getMsg());
			//短信list
			request.setAttribute("mtList",MttaskvoList);
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
			
			//当前登录用户的登录名
			Object sysuserObj = request.getSession(false).getAttribute("loginSysuser");
			String lguserName = null;
			String lgUserId  = null;
			String lgCorpCode = null;
			if(sysuserObj != null)
			{
				LfSysuser sysuser = (LfSysuser)sysuserObj;
				lgUserId = sysuser.getUserId()!= null ? sysuser.getUserName() : null;
				lguserName = (sysuser.getUserName()!= null && !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;
				lgCorpCode = (sysuser.getCorpCode()!= null && !"".equals(sysuser.getCorpCode())) ? sysuser.getCorpCode() : null;
			}

			//查询出的数据的总数量
			int totalCount = pageInfos.getTotalRec();
			//日志信息
			String opContent = "开始时间：" + sdf.format(stratTime) + " 耗时："+
			(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
			
			EmpExecutionContext.info("APP短信群发历史发送详情", lgCorpCode, lgUserId, lguserName, opContent, "GET");
			
			
			//页面跳转
			request.getRequestDispatcher(empRoot + basePath +"/app_smsTaskAllSendRecord.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史查询中的发送详情查询异常！");
			request.setAttribute("findresult", "-1");
			//第一次查询
			request.setAttribute("isFirstEnter", true);
			//分页信息
			request.setAttribute("pageInfo", pageInfos);
			try {		
				//页面跳转
				request.getRequestDispatcher(empRoot + basePath +"/app_smsTaskAllSendRecord.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"群发历史详情查询serlvet异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"群发历史详情查询serlvet跳转异常！");
			}
		}
		
	}
	
	/**
	 * 群发历史查询中的发送详情的excel导出方法(包括提交失败，接收失败的全部导出)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void smsReportAllExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//导出开始时间
		long stratTime = System.currentTimeMillis();
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
            String tableName="MT_TASK";
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
        			MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, null,tableName);	
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
    				long sumCount=smstaskBiz.getSumIcount(conMapcount);
    				//预发送条数
    				String count=Lfmttask.getIcount()==null?"0":Lfmttask.getIcount();
    				//如果三天前的mtdatareport表里的此任务的icount的总和lfmttask的预发送条数icount则查历史表和实时两张表，否则只差对应历史表
    				if(sumCount<Long.parseLong(count))
    				{
    					//需要查询两张表的记录
    					MttaskvoList = smstaskBiz.getMtTaskTwo(conditionMap,orderMap, null,tableName);
    				}
    				else
    				{
    					MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, null,tableName);	
    				}
    				
        		}
        	}
    		//查询出来的记录不为空时，去创建需要导出的excel
			if (MttaskvoList != null && MttaskvoList.size()>0 ) 
			{
        
				SmstaskExcelTool et=new SmstaskExcelTool(excelPath);
				Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
				int ishidephome=0;
				if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
					ishidephome=1;
				}
				
				String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);	
				
				Map<String, String> resultMap = et.createSmsMtReportExcel(MttaskvoList,IsexportAll,ishidephome, request);
			    //String fileName=(String)resultMap.get("FILE_NAME");
		        //String filePath=(String)resultMap.get("FILE_PATH");
		        request.getSession(false).setAttribute("app_smsSendedBoxMap", resultMap);
				PrintWriter out = response.getWriter();
				out.println("true");
		        // 增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if (loginSysuserObj != null) {
					LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
					String opContent = "导出成功,开始时间："+sdf.format(stratTime)+" 耗时："+
					(System.currentTimeMillis()-stratTime)+"ms  数量："+ MttaskvoList.size();
					EmpExecutionContext.info("APP短信群发历史发送详情(导出)", loginSysuser.getCorpCode(),
							loginSysuser.getUserId().toString(), loginSysuser
									.getUserName(), opContent, "OTHER");
				}
		        //DownloadFile dfs=new DownloadFile();
		        //dfs.downFile(request, response, filePath, fileName);
		        //用于判断是否下载加载完成了
		        //request.getSession(false).setAttribute("checkOver"+userId, "true");	       
			}
			else
			{
				response.sendRedirect(request.getContextPath()+"/app_appsmsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
				EmpExecutionContext.error(e,"群发历史查询中的发送详情的excel导出异常！");
			   //异常打印
			   response.sendRedirect(request.getContextPath()+"/app_appsmsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
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
			LfSysuser user=getLoginUser(request);
			String corpCode="";
			if(user!=null){
				corpCode=user.getCorpCode();
			}
			String departmentTree = smstaskBiz.getDepByCorpCodeAndUser(depId,null,corpCode);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e) 
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"群发任务或群发历史机构树加载异常！");
		}
	}
	/**
	 * 群发历史页面获取操作员树的方法 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try
		{		
			Long depId = null;
			
			String depStr = request.getParameter("depId");
			//String userid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userid = SysuserUtil.strLguserid(request);

			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			String requestPath = request.getRequestURI();
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/app_smsTaskRecord.jsp->smsTaskRecord

			//调用公用创建树的方法
			String departmentTree = getDeptUserJosnData(titlePath,depId,Long.parseLong(userid),request);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e) 
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"群发历史页面获取操作员树的方法 异常！");
		}
	}
	
	/**
	 * 群发历史查询页面发送详情页面中的返回按钮调用的查询方法
	 * @param request
	 * @param response
	 */
	public void findallLfMttask(HttpServletRequest request, HttpServletResponse response)
	{
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String requestPath = request.getRequestURI();
		String path = requestPath.substring(requestPath.lastIndexOf("/")).replaceAll(".htm", ".jsp");
		String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
		//String deptUserTree="";
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
			conditionMap.put("isValidate", "1");
			List<LfSpDepBind> userList = baseBiz.getByCondition(
					LfSpDepBind.class, conditionMap, null);

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
            mtVo.setCorpCode(corpCode);
            //查询APP群发短信
            mtVo.setMsTypes("8");
			
			mtVo.setSpUser(spUser);
			if (busCode!=null && !busCode.equals("")) {
                mtVo.setBusCode(busCode);
            }
			mtVo.setDepIds(depid);
			mtVo.setUserIds(userid);

			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}
			//如果用户请求"群发历史查询"，则查询出所有已发历史记录
			if(titlePath.equals("smsTaskRecord"))
			{
				mtVo.setOverSendstate("1,2,3,6");
				//查询发送时间
				if (endSubmitTime !=null && !endSubmitTime.equals("")) {
                    mtVo.setEndSendTime(endSubmitTime);
                }
				if (startSubmitTime !=null && !startSubmitTime.equals("")) {
                    mtVo.setStartSendTime(startSubmitTime);
                }
			}else
			{	
				//查询创建时间
				if (!endSubmitTime.equals("")) {
                    mtVo.setEndSubmitTime(endSubmitTime);
                }
				if (!startSubmitTime.equals("")) {
                    mtVo.setStartSubmitTime(startSubmitTime);
                }
			}
			if(corpCode != null && corpCode.equals("100000"))
			{
				mtVoList = smstaskBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
			}else
			{
				mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
			}

			
			//如果从短信助手或短信客服请求群发任务查看，则统一转向app_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/app_smsSendedBox.jsp";
			}

			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", false);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			
			String departmentTree = smstaskBiz.getDepartmentByCorpCode(Long.parseLong(userId),corpCode);
			//如果用户请求是"群发历史查询"			
			request.setAttribute("departmentTree", departmentTree);

			request.setAttribute("titlePath", titlePath);
			request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e," 群发历史查询页面发送详情页面中的返回列表异常！");
			request.setAttribute("findresult", "-1");
			//回到页面第一次加载时的状态
			request.setAttribute("isFirstEnter", true);
			request.setAttribute("pageInfo", pageInfo);
			//如果从短信助手或短信客服请求群发任务查看，则统一转向app_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/app_smsSendedBox.jsp";
			}
			try {
				request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"群发历史详情返回serlvet异常！");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"群发历史详情返回serlvet跳转异常！");
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
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//导出开始时间
		long stratTime = System.currentTimeMillis();
		//下行短信list
		List<LfMttaskVo> mtVoList = null;
		//路径
		//String context=request.getSession(false).getServletContext().getRealPath("/fileUpload/excelDownload");
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
			mtVo.setCorpCode(corpCode);
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
	            		EmpExecutionContext.error("任务ID转换异常，taskID：" + taskID);
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
            //查询APP群发短信
            mtVo.setMsTypes("8");
            
            mtVo.setSpUser(spUser);
			if (busCode!=null && !busCode.equals("")) {
                mtVo.setBusCode(busCode);
            }
			mtVo.setDepIds(depids);
			mtVo.setUserIds(userids);
			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}

			mtVo.setOverSendstate("1,2,3,6");
			//查询发送时间
			if (!"".equals(endSubmitTime)&& null != endSubmitTime) {
                mtVo.setEndSendTime(endSubmitTime);
            }
			if (!"".equals(startSubmitTime)&& null != startSubmitTime) {
                mtVo.setStartSendTime(startSubmitTime);
            }
			
			
			//群发历史新增发送状态查询条件
		     String sendstate=request.getParameter("sendstate");
			 if(sendstate!=null&&!"".equals(sendstate)){
				if(!"0".equals(sendstate)){
					mtVo.setOverSendstate(sendstate);
				}
			 }
			
			
			if(corpCode != null && corpCode.equals("100000"))
			{
				//mtVoList = mtBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
				mtVoList = smstaskBiz.getLfMttaskVoWithoutDomination( mtVo, null);
			}else
			{
				//mtVoList = mtBiz.getLfMttaskVo(lfSysuser.getUserId(), mtVo, pageInfo);
				mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, null);
			}
			
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
				
				SmstaskExcelTool et = new SmstaskExcelTool(excelPath);
				
				String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);	
				
				Map<String, String> resultMap = et.createMtReportExcel(mtVoList, request);
//				String fileName=(String)resultMap.get("FILE_NAME");
//		        String filePath=(String)resultMap.get("FILE_PATH");
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
				request.getSession(false).setAttribute("app_smsSendedBoxDetilMap", resultMap);
				PrintWriter out = response.getWriter();
				out.println("true");
	            //当前登录用户登录名
				Object  sysuserObj = request.getSession(false).getAttribute("loginSysuser");
				String lguserName = null;
				if(sysuserObj != null)
				{
					LfSysuser sysuser = (LfSysuser) sysuserObj;
					lguserName = (sysuser != null && sysuser.getUserName()!= null 
							&& !"".equals(sysuser.getUserName())) ? sysuser.getUserName() : null;					
				}
						
				//查询出的数据的总数量
				int totalCount = mtVoList.size();
				//日志信息
				String opContent = "导出成功,开始时间：" + sdf.format(stratTime) + " 耗时："+
				(System.currentTimeMillis()-stratTime) + "ms  数量：" + totalCount;
				
				EmpExecutionContext.info("APP短信群发历史(导出)", corpCode, userId, lguserName, opContent, "OTHER");
		        
			}
			else
			{
//				this.find(request, response);
				 response.sendRedirect(request.getContextPath()+"/app_appsmsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
			   //异常打印
				response.sendRedirect(request.getContextPath()+"/app_appsmsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			    EmpExecutionContext.error(e,"群发历史查询的excel导出异常！");
//			   this.find(request, response);
		} 
	}
	
	
	/**
	 * 群发任务查看的excel导出(导出全部)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportAllPageExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		 //导出开始时间
		long stratTime = System.currentTimeMillis();
		
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		//企业编码
		String corpCode = request.getParameter("lgcorpcode");
		//用户id
		//String userId = request.getParameter("lguserid");
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
            mtVo.setCorpCode(corpCode);
            //增加批次号
			 String taskID=request.getParameter("taskID");
			 if(taskID!=null&&!"".equals(taskID.trim())){
				 try{
	            		mtVo.setTaskId(Long.parseLong(taskID.trim()));
	            	}catch (Exception e) {
	            		EmpExecutionContext.error("任务ID转换异常，taskID：" + taskID);
					}
	         }
            
			 	//是否包含子机构
	            String isContainsSun=request.getParameter("isContainsSun");
	            if(isContainsSun!=null&&!"".equals(isContainsSun)){
	            	mtVo.setIsContainsSun(isContainsSun);
	            }
			 
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            
            //查询APP群发短信
            mtVo.setMsTypes("8");
            //任务状态查询
            mtVo.setTaskState(taskState);
            
            if(taskType!=null&&!"".equals(taskType)){
            	mtVo.setTaskType(Integer.parseInt(taskType));
            }
            
            mtVo.setSpUser(spUser);
			if (busCode!=null && !busCode.equals("")) {
                mtVo.setBusCode(busCode);
            }
			mtVo.setDepIds(depid);
			mtVo.setUserIds(userid);
			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}

			//查询时间
			if (!"".equals(endSubmitTime)) {
                mtVo.setEndSubmitTime(endSubmitTime);
            }
			if (!"".equals(startSubmitTime)) {
                mtVo.setStartSubmitTime(startSubmitTime);
            }
			
			if(corpCode != null && corpCode.equals("100000"))
			{
				mtVoList = smstaskBiz.getLfMttaskVoWithoutDomination( mtVo, null);
			}else
			{
				mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, null);
			}
			
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
        
				SmstaskExcelTool et=new SmstaskExcelTool(excelPath);
				
				String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);	
				
				Map<String, String> resultMap = et.createSmsSendedBoxExcel(mtVoList, request);
				request.getSession(false).setAttribute("app_smsSendedBoxShowMap", resultMap);
				//String fileName=(String)resultMap.get("FILE_NAME");
		        //String filePath=(String)resultMap.get("FILE_PATH");
		        	
		        //DownloadFile dfs=new DownloadFile();
		        //dfs.downFile(request, response, filePath, fileName);
		        response.getWriter().println("true");
				 // 增加操作日志
				Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
				if (loginSysuserObj != null) {
					LfSysuser loginSysuser = (LfSysuser) loginSysuserObj;
					String lgUserName = loginSysuser.getUserName() != null && !"".equals(loginSysuser.getUserName())?loginSysuser.getUserName():null;
					String opContent = "导出成功, 开始时间："+ sdf.format(stratTime)+ 
					" 耗时：" + (System.currentTimeMillis()-stratTime) + "ms"+"数量：" + mtVoList.size();
					EmpExecutionContext.info("APP群发任务查看(导出)", corpCode,
							userId, lgUserName, opContent, "OTHER");
				}	 
		        
			}
			else
			{
				response.sendRedirect(request.getContextPath()+"/app_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
			//异常处理
			response.sendRedirect(request.getContextPath()+"/app_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		    EmpExecutionContext.error(e,"群发任务查看的excel导出异常！");
		} 
	}
	
	/**
	 * 停止网关发送中的任务
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void stopTask(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection conn =null;
		String mtId = request.getParameter("mtId");
		LfMttask  task = null;
		IEmpTransactionDAO empTransactionDAO=new DataAccessDriver().getEmpTransDAO();
		try{
			if(mtId!=null && mtId.trim().length()>0)
			{
				task = baseBiz.getById(LfMttask.class, mtId);
				boolean result = false;
				if(task!=null)
				{
					if(task.getIcount2()!=null && Long.parseLong(task.getIcount())-Long.parseLong(task.getIcount2())<=0L)
					{
						response.getWriter().print("AllSended");
						return;
					}
					AcmdQueue cmd = new AcmdQueue();
					cmd.setCmdType(6004);
					cmd.setCmdParam("taskid="+task.getTaskId().toString());
					
					conn = empTransactionDAO.getConnection();
					empTransactionDAO.beginTransaction(conn);
					Long aa = empTransactionDAO.saveObjectReturnID(conn, cmd);
					
					if(aa!=null && aa>0)
					{
						task.setSendstate(6);//发送终止
						result = empTransactionDAO.update(conn, task);
					}
					empTransactionDAO.commitTransaction(conn);
					if(result){
						response.getWriter().print("true");
					}
				}
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.error("模块名称：APP短信群发历史，企业："+loginSysuser.getCorpCode()+"，"
				+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
				+"停止网关发送中的任务(ID："+mtId+")成功。");
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e,"停止网关发送中的任务异常!");
			empTransactionDAO.rollBackTransaction(conn);
			response.getWriter().print("false");
		}finally{
			empTransactionDAO.closeConnection(conn);
		}
	}
	
	/**
	 * 检查关键字
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkBadWord1(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String tmMsg=request.getParameter("tmMsg");
		String corpCode = request.getParameter("corpCode");
		String words=new String();
		try
		{
			KeyWordAtom keyWordAtom=new KeyWordAtom();
			words=keyWordAtom.checkText(tmMsg,corpCode);
		} catch (Exception e)
		{
			words="error";
			EmpExecutionContext.error(e,"检查关键字异常！");
		}finally
		{
			response.getWriter().print("@"+words);
		}
	}
	
	/**
	 *  撤销前的判断
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void checkCancel(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String mtId = request.getParameter("mtId");
		String result = "true";
		try {
			LfMttask mtTask = baseBiz.getById(LfMttask.class, mtId);
			//获取当前时间
			//SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Calendar c = Calendar.getInstance();
            c.add(Calendar.SECOND,c.get(Calendar.SECOND)+30);
			//只允许在定时时间30秒之前的任务才可以撤消
			if(mtTask.getSendstate() != 0)//0未发送
			{
				result="false";
			}
			
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e,"群发任务撤销任务前的判断异常！");
		}
		response.getWriter().print(result);
	}
	
	/**
	 * 撤消发送任务
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void changeState(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String opModule=StaticValue.SMS_BOX;
		String opSper = StaticValue.OPSPER;
		String opType =null;
		String opContent =null;
		String opUser ="";
		try{
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			opUser = sysuser.getUserName();	
		}catch (Exception e) {
			EmpExecutionContext.error(e,"从Session取出用户信息出现异常！");
		}
		
		String mtId = request.getParameter("mtId");
		String mobileUrl = request.getParameter("mobileUrl");
		Integer subState = Integer.valueOf(request.getParameter("subState"));
		String result = "";
		SmsBiz mtBiz=new SmsBiz();
		
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		opContent ="提交短信任务（短信任务ID："+mtId+"）";
		opType = StaticValue.OTHER;
		try
		{
			boolean isSubmit=true;
			if(subState == 2)
			{
				//检查文件
				if(!txtfileutil.checkFile(mobileUrl))
				{
					result="errorFile";
					isSubmit = false;
				}
			}
			if(isSubmit)
			{
				// 查找当前需要撤消的任务
				LfMttask mt = baseBiz.getById(LfMttask.class, mtId);
				String taskid=mt.getTaskId().toString();
				//调用撤消任务的方法
				result=mtBiz.cancelSmsTask(mt,subState);
				if(result=="cancelSuccess"){
					new ReviewBiz().updateFlowRecordByMtId(taskid);
				}
			}
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info("模块名称：APP短信群发历史，企业："+loginSysuser.getCorpCode()+"，"
				+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
				+"撤消发送任务(ID："+mtId+")成功。");
			}
			//处理取消审核流程
			spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		}catch(Exception e)
		{
			result="error";
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"群发任务撤销任务异常！");
		}finally
		{
			response.getWriter().print(result);
		}
	}
	
	/**
	 * 失败重提的方法add 2012.07.05
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void reSendSMS(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String opModule=StaticValue.SMS_BOX;
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.ADD;
		String opContent = "重新提交短信任务";
		String result = "";
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//短信任务id
		Long mtid =0L;
		//用户名
		String username =null;
		String lgguid=null;
		//企业编码
		String corpcode =null;
		try {
			//短信任务id
			mtid = Long.parseLong(request.getParameter("mtid"));
			username = request.getParameter("lgusername");
			corpcode = request.getParameter("lgcorpcode");
			lgguid = request.getParameter("lgguid");
			//根据任务id查出短信任务
			LfMttask lfMttask = baseBiz.getById(LfMttask.class, mtid);
			//判断是否使用集群
			if(StaticValue.getISCLUSTER() ==1)
			{
				//把发送文件下载下来
				CommonBiz commonBiz=new CommonBiz();
				commonBiz.downloadFileFromFileCenter(lfMttask.getMobileUrl());
			}
			
			if(!goToFile(lfMttask.getMobileUrl()))
			{
				//不允许重新提交.
				response.getWriter().print("nofindfile");
				return;
			}
			//if(lfMttask.getIsRetry() != null && lfMttask.getIsRetry().equals("1"))  //findbugs
			if(lfMttask.getIsRetry() != null && lfMttask.getIsRetry() == 1)
			{
				response.getWriter().print("isretry");
				return;
			}
			//为了避免用户在上次请求还未返回时刷新页面.再次点失败重发造成的重复发送问题.这儿加一个Session判断这种情况的发生.
			if(request.getSession(false).getAttribute("isretryMtId") !=null)
			{
				String a = request.getSession(false).getAttribute("isretryMtId").toString();
				if(a.equals(mtid.toString()))
				{
					//已经重发
					response.getWriter().print("isretry");
					return;
				}			
			}
			request.getSession(false).setAttribute("isretryMtId", mtid);
		
			//重新生成一条taskId
			Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			lfMttask.setTaskId(taskId);
			
			if(lfMttask.getIsReply() == 1){
				SMParams smParams = new SMParams();
				smParams.setCodes(taskId.toString());
				smParams.setCodeType(5);
				smParams.setCorpCode(corpcode);
				smParams.setAllotType(1);
				smParams.setSubnoVali(false);
				smParams.setTaskId(taskId);
				if(lgguid!=null)
				{
					smParams.setLoginId(lgguid);
				}
				//循环尾号发送,需获取新的尾号
				ErrorCodeParam errorCodeParam = new ErrorCodeParam();
				LfSubnoAllotDetail subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams,errorCodeParam);
				if(errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode())){
					response.getWriter().print("noUsedSubNo");
					return ;
				}else if(errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode())){
					response.getWriter().print("noSubNo");
					return ;
				}
				if(subnoAllotDetail == null || subnoAllotDetail.getUsedExtendSubno() == null){
					response.getWriter().print("noSubNo");
					return;
				}
				lfMttask.setSubNo(subnoAllotDetail.getUsedExtendSubno());
			}
			// 定时发送变成实时发送
			if(lfMttask.getTimerStatus() == 1)
			{
				lfMttask.setTimerStatus(0);				
			}
			
			//定时
			lfMttask.setTimerTime(new Timestamp(System.currentTimeMillis()));
			lfMttask.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			lfMttask.setSendstate(0);
			lfMttask.setMtId(null);
			
			//将ICount2赋值为空
			lfMttask.setIcount2(null);
			
			//需要将原来的文件重生产生一个。
			String[] url  = txtfileutil.getSaveUrl(lfMttask.getUserId());
			String oldPath = txtfileutil.getWebRoot()+lfMttask.getMobileUrl();
			txtfileutil.copyFile(oldPath, url[0]);
			lfMttask.setMobileUrl(url[1]);
			
			//获取发送信息等缓存数据（是否计费、用户编码）
			Map<String,String> infoMap = (Map<String,String>) request.getSession(false).getAttribute("infoMap");
			//调用发送方法
			result = new SmsSendBiz().addSmsLfMttaskResend(lfMttask, infoMap);
			String reultClong=result;
			
			if(!"createSuccess".equals(result) && !"000".equals(result) && !"saveSuccess".equals(result)
					&& !"timerSuccess".equals(result) && !"timerFail".equals(result) && !"false".equals(result)&&!"nomoney".equals(result) )
			{
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				//根据错误编码得到错误信息
				result = new WGStatus(langName).getInfoMap().get(result);
				if(result==null)
				{
					result=reultClong;
				}
			}
			
			if(!"false".equals(result)&&!"nomoney".equals(result))
			{
				//将原来的设置 为已重新提交.
				objectMap.put("isRetry", "1");
				conditionMap.put("mtId", mtid.toString());
				baseBiz.update(LfMttask.class, objectMap, conditionMap);
			}else
			{
				request.getSession(false).setAttribute("isretryMtId", "");
			}
			
				
			opContent+="(任务名称:"+lfMttask.getTitle()+")";
			//存日志
			spLog.logSuccessString(username, opModule, opType, opContent,corpcode);
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info("模块名称：APP短信群发历史，企业："+loginSysuser.getCorpCode()+"，"
				+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
				+"失败重提(ID："+mtid+")成功。");
			}
		} catch (Exception e) {
			//if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
			if(HttpHostConnectException.class.isInstance(e))
			{
				result=e.getLocalizedMessage();
				spLog.logSuccessString(username, opModule, opType, opContent,corpcode);
			}else
			{
				result="error";
				spLog.logFailureString(username, opModule, opType, opContent+opSper, e,corpcode);
			}
			try {
				//将原来的设置 为已重新提交.
				objectMap.put("isRetry", "1");
				conditionMap.put("mtId", mtid.toString());
				baseBiz.update(LfMttask.class, objectMap, conditionMap);
			} catch (Exception e2) {
				result ="error";
				EmpExecutionContext.error(e, "更新任务失败。");
			}			
			
			EmpExecutionContext.error("EBFB010");
			EmpExecutionContext.error(e,"群发历史模块失败重发异常！");
		}finally
		{
			response.getWriter().print(result);
		}
		
	}
	
	/**
	 * 检查文件是否存在
	 * @param url：文件地址
	 * @return
	 */
	public boolean goToFile(String url)
	{
		TxtFileUtil tfu = new TxtFileUtil();
		//不存在
		boolean result = false;
		try
		{
			//返回结果，是否存在
			result = tfu.checkFile(url);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"检查文件是否存在异常！");
			return false;
		}
		//返回结果
		return result;
	}
	
	protected int getIntParameter(HttpServletRequest request,String param, int defaultValue)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		} catch (NumberFormatException e)
		{
			EmpExecutionContext.error("参数转换异常，param:" + param);
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
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

		try {
			HttpSession session = request.getSession(false);
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
					//if(record.getRTime()== null || "".equals(record.getRTime())){  //findbugs
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
				//if(useridstr != null && !"".equals(useridstr)){  //findbugs
				if(useridstr != null){
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
						//if(lastrecord.getRLevel() != lastrecord.getRLevelAmount()){  //findbugs
						if(!((lastrecord.getRLevel()).equals(lastrecord.getRLevelAmount()))){
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
	
	/**
	 * APP短信群发历史查询的excel导出
	 * @Title: downloadFile
	 * @Description: TODO
	 * @param  
	 * @return void    返回类型
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public void downloadFile(HttpServletRequest request,HttpServletResponse response)throws Exception{
		try {
			Object resultMapObj1 = request.getSession(false).getAttribute("app_smsSendedBoxMap");
			Object resultMapObj2 = request.getSession(false).getAttribute("app_smsSendedBoxDetilMap");
			Object resultMapObj3 = request.getSession(false).getAttribute("app_smsSendedBoxShowMap");
			if(resultMapObj1 != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj1;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
			    String filePath=(String)resultMap.get("FILE_PATH");
				
			    DownloadFile dfs=new DownloadFile();
			    dfs.downFile(request, response, filePath, fileName);
			    request.getSession(false).removeAttribute("app_smsSendedBoxMap");
			}
			if(resultMapObj2 != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj2;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
				String filePath=(String)resultMap.get("FILE_PATH");
				
				DownloadFile dfs=new DownloadFile();
				dfs.downFile(request, response, filePath, fileName);
				request.getSession(false).removeAttribute("app_smsSendedBoxDetilMap");
			}
			if(resultMapObj3 != null)
			{
				Map<String, String> resultMap = (Map<String, String>) resultMapObj3;
				//文件名
				String fileName=(String)resultMap.get("FILE_NAME");
				//文件路径
				String filePath=(String)resultMap.get("FILE_PATH");
				
				DownloadFile dfs=new DownloadFile();
				dfs.downFile(request, response, filePath, fileName);
				request.getSession(false).removeAttribute("app_smsSendedBoxShowMap");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			EmpExecutionContext.error(e, "APP短信群发历史查询的excel导出异常!");
		}
		
	}
	
	
	
}