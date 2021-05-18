package com.montnets.emp.engine.servlet;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.SendedMttaskVo;
import com.montnets.emp.engine.bean.ZnyqParamValue;
import com.montnets.emp.engine.biz.SerMtTaskBiz;
import com.montnets.emp.engine.biz.SerMtTaskExcelTool;
import com.montnets.emp.engine.vo.LfMttaskVo;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.gateway.AcmdQueue;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
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
 * 下行业务记录svt
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-21 上午10:22:35
 * @description
 */
@SuppressWarnings("serial")
public class eng_serMtTaskSvt extends BaseServlet {

	private final SerMtTaskBiz smstaskBiz=new SerMtTaskBiz();
	private final String empRoot="znyq";
	private final String basePath="/engine";
	private final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	private final String path=new TxtFileUtil().getWebRoot();
	//模板路径
	protected final String  excelPath = path + empRoot+basePath+"/file/";
	
	private final String moduleName = "下行业务记录";

	//常用文件读写工具类
	private final TxtFileUtil txtfileutil = new TxtFileUtil();
	
	
	
	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		//短信biz
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();

		PageInfo pageInfo = new PageInfo();
		//当前登录用户的企业编码
		String corpCode = "";
		String  userId = "";
		try
		{
			//导出方法的查询条件清空
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_sendTime", null);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_recvTime", null);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_userIds", null);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_depIds", null);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_sendTitle", null);
			
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_serId", null);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_serName", null);
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_serSendState", null);
			
			boolean isFirstEnter = false;
			boolean isBack = request.getParameter("isback")==null?false:true;
			
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务记录，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			//String lgcorpcode = curUser.getCorpCode();
			//String lgusername = curUser.getUserName();
			
			//当前登录用户的企业编码
			corpCode =curUser.getCorpCode();
			userId =curUser.getUserId().toString();
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
            //查找绑定的sp账号
			List<LfSpDepBind> userList = baseBiz.getByCondition(
					LfSpDepBind.class, conditionMap, null);
			String spUser = null;
			String depid = null;
			String userid = null;
			String startSubmitTime = null;
			String endSubmitTime = null;
            String busCode = null;
            String serId = null;
        	String serName = null;
        	String serSendState = null;
        	String userName = null;
        	String depNam = null;
        	HttpSession session = request.getSession(ZnyqParamValue.GET_SESSION_FALSE);
			if(isBack){
				pageInfo = (PageInfo) session.getAttribute("eng_pageinfo");
				spUser = defaultVal(session.getAttribute("eng_spUser"));
				depid = defaultVal(session.getAttribute("eng_depid"));
				userid = defaultVal(session.getAttribute("eng_userid"));
				startSubmitTime = defaultVal(session.getAttribute("eng_sendtime"));
				endSubmitTime = defaultVal(session.getAttribute("eng_recvtime"));
	            busCode = defaultVal(session.getAttribute("eng_busCode"));
	            serId = defaultVal(session.getAttribute("eng_serId"));
	        	serName = defaultVal(session.getAttribute("eng_serName"));
	        	serSendState = defaultVal(session.getAttribute("eng_serSendState"));
	        	userName = defaultVal(session.getAttribute("eng_userName"));
	        	depNam = defaultVal(session.getAttribute("eng_depNam"));
	        	request.setAttribute("spUser", spUser);
	        	request.setAttribute("depid", depid);
	        	request.setAttribute("userid", userid);
	        	request.setAttribute("sendtime", startSubmitTime);
	        	request.setAttribute("recvtime", endSubmitTime);
	        	request.setAttribute("busCode", busCode);
	        	request.setAttribute("serId", serId);
	        	request.setAttribute("serName", serName);
	        	request.setAttribute("serSendState", serSendState);
	        	request.setAttribute("userName", userName);
	        	request.setAttribute("depNam", depNam);
			}else{
				isFirstEnter = pageSet(pageInfo,request);
				spUser = request.getParameter("spUser");
				depid = request.getParameter("depid");
				userid = request.getParameter("userid");
				startSubmitTime = request.getParameter("sendtime");
				endSubmitTime = request.getParameter("recvtime");
	            busCode = request.getParameter("busCode");
	            serId = request.getParameter("serId");
	        	serName = request.getParameter("serName");
	        	serSendState = request.getParameter("serSendState");
	        	userName = request.getParameter("userName");
	        	depNam = request.getParameter("depNam");
			}
			session.setAttribute("eng_pageinfo",pageInfo);
			session.setAttribute("eng_spUser", spUser);
			session.setAttribute("eng_depid", depid);
			session.setAttribute("eng_userid", userid);
			session.setAttribute("eng_sendtime", startSubmitTime);
			session.setAttribute("eng_recvtime", endSubmitTime);
			session.setAttribute("eng_busCode", busCode);
			session.setAttribute("eng_serId", serId);
			session.setAttribute("eng_serName", serName);
			session.setAttribute("eng_serSendState", serSendState);
			session.setAttribute("eng_userName", userName);
			session.setAttribute("eng_depNam", depNam);
            
            //获取过滤条件
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            mtVo.setCorpCode(corpCode);
           //查询移动财务短信及群发短信
            mtVo.setMsTypes("7");
            
			if(!isFirstEnter){
				if(serId != null && serId.length() > 0){
					mtVo.setSerId(Long.valueOf(serId));
				}
				
				mtVo.setSerName(serName);
				mtVo.setSpUser(spUser);
				if (busCode!=null && !busCode.equals("")) {
                    mtVo.setBusCode(busCode);
                }
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);

				mtVo.setOverSendstate(serSendState);
				//查询发送时间
				if (!"".equals(endSubmitTime)) {
                    mtVo.setEndSendTime(endSubmitTime);
                }
				if (!"".equals(startSubmitTime)) {
                    mtVo.setStartSendTime(startSubmitTime);
                }
				
				//设置导出的查询条件
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_sendTime", mtVo.getStartSendTime());
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_recvTime", mtVo.getEndSendTime());
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_userIds", mtVo.getUserIds());
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_depIds", mtVo.getDepIds());
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_sendTitle", mtVo.getTitle());
				
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_serId", mtVo.getSerId());
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_serName", mtVo.getSerName());
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("r_serSendState", mtVo.getOverSendstate());
				
				//查询lfmttask信息
				if(corpCode != null && corpCode.equals("100000"))
				{
					mtVoList = smstaskBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
				}else
				{
					mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
				}
			}
			
			String content = "下行业务记录查询。" 
			+"条件serId="+serId
			+",serName="+serName
			+",spUser="+spUser
			+",busCode="+busCode
			+",serId="+serId
			+",serName="+serName
			+",depid="+depid
			+",userid="+userid
			+",spUser="+spUser
			+",serSendState="+serSendState
			+",endSubmitTime="+endSubmitTime
			+",startSubmitTime="+startSubmitTime
			+",结果数量："+(mtVoList==null?null:mtVoList.size());
			
			//加密参数
			encryptionParam(mtVoList, request);
			
			//String lgusername = request.getParameter("lgusername");
			EmpExecutionContext.info("智能引擎", corpCode, userId, curUser.getUserName(), content, StaticValue.GET);
			
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", userId);//当前登录用户id
			//加载机构树
			//String departmentTree = smstaskBiz.getDepartmentJosnData(Long.parseLong(userId));
			
			//request.setAttribute("departmentTree", departmentTree);
			
		} catch (Exception e) {
			EmpExecutionContext.error("下行业务记录查询方法请求URL:" + request.getRequestURI()+ "，请求参数，corpCode：" + corpCode + "，userId :" + userId);
			EmpExecutionContext.error(e,"下行业务记录查询的方法异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
			request.setAttribute("pageInfo", pageInfo);
			
		}finally{
			try {
				request.getRequestDispatcher(empRoot + basePath +"/eng_serMttask.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1,"下行业务记录查询svt跳转异常。");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1,"下行业务记录查询svt跳转异常。");
			}
		}
	}
	
	/**
	 * 
	 * @description 加密参数
	 * @param mtVoList 页面显示列表数据对象
	 * @param request 请求对象
	 */
	private void encryptionParam(List<LfMttaskVo> mtVoList, HttpServletRequest request)
	{
		try
		{
			if(mtVoList == null || mtVoList.size() < 1)
			{
				return;
			}
			//从session中获取加密解密对象
			ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
			//加密解密对象不能为null
			if(encryptOrDecrypt == null)
			{
				EmpExecutionContext.error("查询下行业务记录列表，从session中获取加密对象为空。");
				return;
			}
			
			String keyId;
			//遍历操作员列表，对ID进行加密
			for(LfMttaskVo mttaskVo : mtVoList)
			{
				keyId = encryptOrDecrypt.encrypt(String.valueOf(mttaskVo.getMtId()));
				mttaskVo.setMtIdCipher(keyId);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("查询下行业务记录列表，加密参数，异常。");
		}
	}
	
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "从session获取加密对象异常。");
			return null;
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
			EmpExecutionContext.error(e,"下行业务记录页面回复详情异常！");
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

		// 设置语言
		String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
		//部门iD
		String depId = request.getParameter("depId");
		try
		{
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			String depJson = smstaskBiz.getDepJosn(depId, curUser);
			if(depJson == null)
			{
				response.getWriter().print("");
			}
			else
			{
				response.getWriter().print(depJson);
			}
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "下行业务记录，生成机构树字符串，异常。depId="+depId);
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
		Object loginSysuserObj = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
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
			//获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务记录，加载机构树，session获取当前登录操作员对象为空。");
				return;
			}
			// 当前登录操作员id
			//String lguserid = curUser.getUserId().toString();
			// 当前登录企业
			//String lgcorpcode = curUser.getCorpCode();
			//获取当前登录用户的userid
			String userid = curUser.getUserId().toString();
			String deptUserTree="";
			String requestPath = request.getRequestURI();
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord

			deptUserTree = getDeptUserJosnData(titlePath,Long.parseLong(userid));			
			
			response.getWriter().print(deptUserTree);
			EmpExecutionContext.info(deptUserTree);
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"下行业务记录查询条件中操作员树的加载方法异常！");
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
				EmpExecutionContext.error(e,"下行业务记录操作员树的加载方法异常！");
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
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}
					
					
				}else
				{
					if(depId == null){
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);						
					}else{
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
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
				EmpExecutionContext.error(e,"下行业务记录操作员树的加载方法异常！");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}

	
	/**
	 * 回复查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
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
	    	Map<String,String> btnMap=(Map<String,String>)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("btnMap");
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
//		 	try{
//		 	}catch (Exception e) {
//		 		EmpExecutionContext.error(e);
//		 	}
		 	//回复详情(回复详情查看三张表且七天内的方法)
		 	//reply = mtBiz.getSmsSendMotaskReply(mt,pageInfo);
		 	////回复详情查询(跟据taskid去lfmotask查询taskid为mtid的上行记录)
		 	//LinkedHashMap<String, String> map=new LinkedHashMap<String, String>();
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
			EmpExecutionContext.error(e,"下行业务记录回复详情查询异常！");
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
	public void findAllSendInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{	
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo, request);
		int count = 0;
		
		//获取页面传过来的lf_mttask表中的mtid密文
        String mtIdCipher = request.getParameter("mtid");
        //获取页面传过来的查询条件
        String spisuncm = request.getParameter("spisuncm");//运营商
        String phone = request.getParameter("phone");//手机号
        String taskId = request.getParameter("taskId");
        //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,4:带页面查询条件的）
        String type = request.getParameter("type");
        //明文业务id
		String mtid = null;
		
        try
		{
			if(mtIdCipher == null)
			{
				EmpExecutionContext.error("下行业务记录详情查询，获取不到mtid。");
				return;
			}
			
			//解密，获取明文业务id
			mtid = decryptionParam(mtIdCipher, request);
			//解密失败，返回了null
			if(mtid == null)
			{
				EmpExecutionContext.error("下行业务记录详情查询，解密serId失败。密文：" + mtIdCipher);
				return;
			}
			
            LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
            conditionMap.put("type", type);
            //带页面查询条件
        	if(spisuncm != null && spisuncm.length() > 0)
        	{
        		conditionMap.put("unicom", spisuncm);
        	}
        	if(phone != null && phone.length() > 0)
        	{
        		conditionMap.put("phone", phone);
        	}
            
            //根据mtid获取任务信息
            LfMttask Lfmttask = baseBiz.getById(LfMttask.class, mtid);
            if(Lfmttask == null)
            {
            	EmpExecutionContext.error("下行业务记录详情查询，获取不到发送任务对象。mtid="+mtid);
				return;
            }
            
          //获取当前登录操作员
			LfSysuser curUser = getCurUserInSession(request);
			if(curUser == null)
			{
				EmpExecutionContext.error("下行业务记录，详情查看，session获取当前登录操作员对象为空。");
				return;
			}
			if(!curUser.getCorpCode().equals(Lfmttask.getCorpCode()))
			{
				EmpExecutionContext.error("下行业务记录，详情查看，任务不是当前登录企业的任务。mtid="+mtid+",corpcode="+curUser.getCorpCode());
				return;
			}
            
            if(taskId == null || taskId.length() ==0 || "null".equals(taskId))
			{
    			conditionMap.put("taskid", Lfmttask.getTaskId().toString());
			}
			else
			{
				conditionMap.put("taskid", taskId);
			}
            
            //任务对象存到session中，方便jsp页面和分页的时候用
            request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("serMttask", Lfmttask);
            //任务对象存到session中，方便分页的时候用
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("serConcondition", conditionMap);
            
			request.setAttribute("isFirstEnter", isFirstEnter);
			
			//第一次进入页面不查询
			if(isFirstEnter)
			{
				return;
			}
			
			Object realDbpageInfoObj = request.getSession(false).getAttribute("realDbpageInfo");
			PageInfo realDbpageInfo;
			if(realDbpageInfoObj != null)
			{
				realDbpageInfo = (PageInfo)realDbpageInfoObj;
			}
			else
			{
				realDbpageInfo = new PageInfo();
			}
			
			//发送详情
    		List<SendedMttaskVo> MttaskvoList = smstaskBiz.getMtTaskList(Lfmttask, conditionMap, pageInfo, realDbpageInfo);
			//短信list
			request.setAttribute("mtList",MttaskvoList);
			//没数据则不需要查询分页
			if(MttaskvoList == null || MttaskvoList.size() == 0)
			{
				pageInfo.setNeedNewData(2);
				pageInfo.setTotalRec(0);
				pageInfo.setTotalPage(1);
			}
			count = MttaskvoList==null?0:MttaskvoList.size();
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"下行业务记录详情查询，异常。");
			request.setAttribute("findresult", "-1");
			//第一次查询
			request.setAttribute("isFirstEnter", true);
		}
		finally
		{
			//分页信息
			request.setAttribute("pageInfo", pageInfo);
			
			//开始时间
			String starthms = hms.format(startl);
			String opContent = "下行业务记录详情查询。" + count + "条,第"+pageInfo.getPageIndex()+"页 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms"
				+",条件：spisuncm="+spisuncm
				+",phone="+phone
				+",type="+type
				+",mtid="+mtid
				+",taskId="+taskId;
			setLog(request, "智能引擎", opContent, StaticValue.GET);
			
			request.getRequestDispatcher(empRoot + basePath +"/eng_serMtTaskAllRecord.jsp")
				.forward(request, response);
		}
	}
	
	/**
	 * 
	 * @description 解密参数
	 * @param paramCipher 参数密文
	 * @param request 请求对象
	 * @return 参数明文
	 */
	private String decryptionParam(String paramCipher, HttpServletRequest request)
	{
		if(paramCipher == null || paramCipher.length() < 1)
		{
			EmpExecutionContext.error("下行业务记录列表，传入的参数密文为空。密文："+paramCipher);
			return null;
		}
		//加密解密对象
		ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
		//加密解密对象不能为null
		if(encryptOrDecrypt == null)
		{
			EmpExecutionContext.error("下行业务记录列表，从session中获取加密对象为空。密文："+paramCipher);
			return null;
		}
		
		//解密
		String param = encryptOrDecrypt.decrypt(paramCipher);
		return param;
	}
	
	/**
	 * 查询下行业务记录分页信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getDetailPageInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//起始ms数
		long startl=System.currentTimeMillis();
		String conditionstr = "";
		//分页对象
		PageInfo pageInfo = new PageInfo();
		try
		{
			LfMttask Lfmttask = (LfMttask)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("serMttask");
			LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("serConcondition");
			
			if(Lfmttask == null || conditionMap == null)
			{
				EmpExecutionContext.error("下行业务记录详情查询分页，获取任务对象或查询条件对象为空。");
				return;
			}
			
			Object realDbpageInfoObj = request.getSession(false).getAttribute("realDbpageInfo");
			PageInfo realDbpageInfo;
			if(realDbpageInfoObj != null)
			{
				realDbpageInfo = (PageInfo)realDbpageInfoObj;
			}
			else
			{
				realDbpageInfo = new PageInfo();
			}
			
			pageSet(pageInfo, request);
			//发送详情
    		smstaskBiz.getMtTaskListPageInfo(Lfmttask, conditionMap, pageInfo, realDbpageInfo);
    		
    		request.getSession(false).setAttribute("realDbpageInfo", realDbpageInfo);
			
			conditionstr = "spisuncm:"+conditionMap.get("unicom")
							+ ",phone:"+conditionMap.get("phone")
							+ ",taskid:"+Lfmttask.getTaskId();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录详情查询分页，异常。");
		}
		finally
		{
			response.getWriter().print("{totalRec:"+pageInfo.getTotalRec()+",totalPage:"+pageInfo.getTotalPage()+"}");
			//开始时间
			String starthms=hms.format(startl);
			String opContent = "下行业务记录详情查询分页。" + pageInfo.getTotalRec() + "条 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms,条件："+conditionstr;
			setLog(request, "智能引擎", opContent, StaticValue.GET);
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
			Object loginSysuserObj = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
			if(loginSysuserObj == null)
			{
				EmpExecutionContext.error("下行业务记录，记录日志，获取不到session当前登录操作员对象。");
				return;
			}
			
			LfSysuser loginSysuser = (LfSysuser)loginSysuserObj;
			EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录，记录日志异常。");
		}
	}
	
	/**
	 * 群发历史查询中的发送详情的excel导出方法(包括提交失败，接收失败的全部导出)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void smsReportAllExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{

	    LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
	    LinkedHashMap<String,String> orderMap = new LinkedHashMap<String,String>();
	  //获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务记录，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		//String lguserid = curUser.getUserId().toString();
		// 当前登录企业
		//String lgcorpcode = curUser.getCorpCode();
	    //企业编码
		String corpCode = curUser.getCorpCode();
		//用户id
		String userId = curUser.getUserId().toString();
		try {
			String mtid  = request.getParameter("mtid");
            if(mtid !=null)
            {
                conditionMap.put("mtid", mtid);
            }
            //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,4:带页面查询条件的）
            String type = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("type");
            //获取页面传过来的查询条件
            String spisuncm = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("spisuncm");//运营商
            String phone = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("phone");//手机号
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
            if(Lfmttask == null)
            {
            	EmpExecutionContext.error("下行业务记录详情查询，导出excel，获取不到发送任务对象。mtid="+mtid);
				return;
            }
            if(!curUser.getCorpCode().equals(Lfmttask.getCorpCode()))
			{
				EmpExecutionContext.error("下行业务记录详情查询，导出excel，任务不是当前登录企业的任务。mtid="+mtid+",corpcode="+curUser.getCorpCode());
				return;
			}
			String taskid = Lfmttask.getTaskId().toString();
			conditionMap.put("taskid", taskid);
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
        		c2.add(Calendar.DATE, 4);
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
    				conMapcount.put("taskid",taskid);
    				String iymd;
    				Calendar curTime=Calendar.getInstance();
    				curTime.add(Calendar.DATE, -3);
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
        
				SerMtTaskExcelTool et=new SerMtTaskExcelTool(excelPath);
				Map<String,String> btnMap=(Map<String,String>)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("btnMap");
				int ishidephome=0;
				if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
					ishidephome=1;
				}
				Map<String, String> resultMap = et.createSmsMtReportExcel(MttaskvoList,IsexportAll,ishidephome);
				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        	
		        DownloadFile dfs=new DownloadFile();
		        dfs.downFile(request, response, filePath, fileName);
		        //用于判断是否下载加载完成了
		        request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("checkOver"+userId, "true");	  
				//登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
				//操作员名称
				String userName = " ";
				if(lfSysuser != null)
				{
					userName = lfSysuser.getUserName();
				}
				//操作日志
				EmpExecutionContext.info(moduleName, corpCode, userId, userName, "导出下行业务记录详情，共导出"+MttaskvoList.size()+"条记录" , "OTHER");
			}
			else
			{
				//response.sendRedirect(request.getContextPath()+"/eng_serMtTask.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
				EmpExecutionContext.error(e,"下行业务记录查询中的发送详情的excel导出异常！");
			   //异常打印
			   response.sendRedirect(request.getContextPath()+"/eng_serMtTask.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		} 
	}
	
	/**
	 * 下行业务记录详情的excel导出方法
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportMtSerDetailExcel(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		// 设置语言
		String langName  = request.getParameter("langName");
		long startTime = System.currentTimeMillis();
		//用户id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//是否导出全部
		String IsexportAll = request.getParameter("IsexportAll");
		try 
		{
			LfMttask Lfmttask = (LfMttask)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("serMttask");
			if(Lfmttask == null)
			{
				EmpExecutionContext.error("下行业务记录详情导出，获取任务对象为空。lguserid="+lguserid+",IsexportAll="+IsexportAll);
				response.getWriter().print("false");
				return;
			}
			LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("serConcondition");
			if(conditionMap == null)
			{
				EmpExecutionContext.error("下行业务记录详情导出，获取查询条件为空。lguserid="+lguserid+",IsexportAll="+IsexportAll);
				response.getWriter().print("false");
				return;
			}
			
			//发送详情
			List<SendedMttaskVo> MttaskvoList = smstaskBiz.getMtTaskList(Lfmttask, conditionMap, null, null);
			if (MttaskvoList == null || MttaskvoList.size() == 0) 
			{
				response.getWriter().print("false");
				return;
			}
			
			Map<String,String> btnMap=(Map<String,String>)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("btnMap");
			//是否隐藏手机号码
			int ishidephome=0;
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null)
			{
				ishidephome=1;
			}
			SerMtTaskExcelTool et = new SerMtTaskExcelTool(excelPath);
			Map<String, String> resultMap = et.createMtReportDetailExcel(langName,MttaskvoList, IsexportAll, ishidephome);
			if(resultMap == null || resultMap.size() == 0)
			{
				response.getWriter().print("false");
				return;
			}
			if(resultMap.get("FILE_PATH") == null || resultMap.get("FILE_PATH").length() == 0)
			{
				response.getWriter().print("false");
				return;
			}
			String fileName = resultMap.get("FILE_NAME");
	        String filePath = resultMap.get("FILE_PATH");
	        	
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("ReportMtSerDetailExcel", fileName+"@@"+filePath);
	        //用于判断是否下载加载完成了
	        request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("checkOver"+lguserid, "true");
	        response.getWriter().print("true");
	        
	        //操作日志
			setLog(request, "智能引擎", "下行业务记录详情导出，共导出"+MttaskvoList.size()+"条记录,耗时："+(System.currentTimeMillis()-startTime)+"ms。", StaticValue.OTHER);
			
	        MttaskvoList.clear();
	        MttaskvoList = null;
		} 
		catch (Exception e) 
		{		
			response.getWriter().print("false");
			EmpExecutionContext.error(e, "下行业务记录详情导出，异常。");
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
			String departmentTree = smstaskBiz.getDepartmentJosnData2(depId,null);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e) 
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e,"下行业务记录机构树加载异常！");
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
			String deptUserTree = smstaskBiz.getDepUserJosn(langName,depId, curUser);
			if(deptUserTree == null)
			{
				response.getWriter().print("");
			}
			else
			{
				response.getWriter().print(deptUserTree);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录，生成机构操作员树字符串，异常。depId="+depId);
			response.getWriter().print("");
		}
	}
	

	/**
	 * 群发历史查询的excel导出方法(导出全部)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportCurrPageExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		

		// 设置语言,此处前端需传值，还未传，支持多语言
		String langName  = request.getParameter("langName");
		
		//下行短信list
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务记录，导出全部excel，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		//String lguserid = curUser.getUserId().toString();
		// 当前登录企业
		//String lgcorpcode = curUser.getCorpCode();
		
		//企业编码
		String corpCode = curUser.getCorpCode();
		//用户id
		String userId = curUser.getUserId().toString();
        try
        {
        	String title = (String) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_sendTitle");
        	String spUser = request.getParameter("spuser");
			String depids = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_depIds");
			String userids = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_userIds");

			String startSubmitTime = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_sendTime");
			String endSubmitTime = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_recvTime");
			
			Long serId = (Long)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_serId");
			String serName = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_serName");
			String serSendState = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_serSendState");
        	
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
            mtVo.setCorpCode(corpCode);
            //查询移动财务短信及群发短信
            mtVo.setMsTypes("7");
            
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

			mtVo.setOverSendstate(serSendState);
			if(serId != null ){
				mtVo.setSerId(serId);
			}
			mtVo.setSerName(serName);
			
			//查询发送时间
			if (!"".equals(endSubmitTime)&& null != endSubmitTime) {
                mtVo.setEndSendTime(endSubmitTime);
            }
			if (!"".equals(startSubmitTime)&& null != startSubmitTime) {
                mtVo.setStartSendTime(startSubmitTime);
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
				
				SerMtTaskExcelTool et = new SerMtTaskExcelTool(excelPath);
				Map<String, String> resultMap = et.createMtReportExcel(langName,mtVoList);
				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        DownloadFile dfs=new DownloadFile();
		        dfs.downFile(request, response, filePath, fileName);
				//登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
				//操作员名称
				String userName = " ";
				if(lfSysuser != null)
				{
					userName = lfSysuser.getUserName();
				}
				//操作日志
				EmpExecutionContext.info(moduleName, corpCode, userId, userName, "导出下行业务记录数，共导出"+mtVoList.size()+"条记录" , "OTHER");
			}
			else
			{
				 response.sendRedirect(request.getContextPath()+"/eng_serMtTask.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
			   //异常打印
				response.sendRedirect(request.getContextPath()+"/eng_serMtTask.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			    EmpExecutionContext.error(e,"下行业务记录导出excel导出异常！");
		} 
	}
	
	/**
	 * 下行业务记录导出excel
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void ReportMtSerExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{

		// 设置语言
		String langName  = request.getParameter("langName");
		//下行短信list
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务记录，导出业务excel，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		//String lguserid = curUser.getUserId().toString();
		// 当前登录企业
		//String lgcorpcode = curUser.getCorpCode();
		
		//企业编码
		String corpCode = curUser.getCorpCode();
		//用户id
		String userId = curUser.getUserId().toString();
        try
        {
        	String title = (String) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_sendTitle");
        	String spUser = request.getParameter("spuser");
			String depids = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_depIds");
			String userids = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_userIds");

			String startSubmitTime = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_sendTime");
			String endSubmitTime = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_recvTime");
			
			Long serId = (Long)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_serId");
			String serName = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_serName");
			String serSendState = (String)request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("r_serSendState");
        	
			/*PageInfo pageInfo = new PageInfo();
			//分页信息
			pageSet(pageInfo,request);*/
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
            mtVo.setCorpCode(corpCode);
            //查询移动财务短信及群发短信
            mtVo.setMsTypes("7");
            
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

			mtVo.setOverSendstate(serSendState);
			if(serId != null ){
				mtVo.setSerId(serId);
			}
			mtVo.setSerName(serName);
			
			//查询发送时间
			if (!"".equals(endSubmitTime)&& null != endSubmitTime) {
                mtVo.setEndSendTime(endSubmitTime);
            }
			if (!"".equals(startSubmitTime)&& null != startSubmitTime) {
                mtVo.setStartSendTime(startSubmitTime);
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
				
				SerMtTaskExcelTool et = new SerMtTaskExcelTool(excelPath);
				Map<String, String> resultMap = et.createMtReportExcel(langName,mtVoList);
				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        /*DownloadFile dfs=new DownloadFile();
		        dfs.downFile(request, response, filePath, fileName);*/
				//登录操作员信息
				LfSysuser lfSysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
				//操作员名称
				String userName = " ";
				if(lfSysuser != null)
				{
					userName = lfSysuser.getUserName();
				}
				//操作日志
				EmpExecutionContext.info(moduleName, corpCode, userId, userName, "导出下行业务记录数，共导出"+mtVoList.size()+"条记录" , "OTHER");
				
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("ReportMtSerExcel", fileName+"@@"+filePath);
		        //用于判断是否下载加载完成了
		        request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("checkOver"+userId, "true");
		        response.getWriter().print("true");
			}
			else
			{
				response.getWriter().print("false");
				 //response.sendRedirect(request.getContextPath()+"/eng_serMtTask.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		}
        catch (Exception e) 
		{
        	response.getWriter().print("false");
			   //异常打印
				//response.sendRedirect(request.getContextPath()+"/eng_serMtTask.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
        	EmpExecutionContext.error(e,"下行业务记录导出excel导出异常！");
		} 
	}
	
	/**
	 * excel文件导出
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)   
	{
		try
		{
			String down_session=request.getParameter("down_session");
		    HttpSession session = request.getSession(ZnyqParamValue.GET_SESSION_FALSE);
		    Object obj = session.getAttribute(down_session);
		    if(obj == null)
		    {
		    	EmpExecutionContext.error("下行业务记录导出excel下载，获取不到会话对象。");
		    	return;
		    }
	        String result = (String) obj;
	        if(result.indexOf("@@") < 0)
			{
				EmpExecutionContext.error("下行业务记录导出excel下载，获取不到分隔参数。");
				return;
			}
        	String[] file=result.split("@@");
            // 弹出下载页面。
            DownloadFile dfs = new DownloadFile();
            dfs.downFile(request, response, file[1], file[0]);
            session.removeAttribute(down_session);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "下行业务记录导出excel下载，异常。");
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
		
		//获取当前登录操作员
		LfSysuser curUser = getCurUserInSession(request);
		if(curUser == null)
		{
			EmpExecutionContext.error("上行业务记录，导出业务全部excel，session获取当前登录操作员对象为空。");
			return;
		}
		// 当前登录操作员id
		//String lguserid = curUser.getUserId().toString();
		// 当前登录企业
		//String lgcorpcode = curUser.getCorpCode();
		
		//企业编码
		String corpCode = curUser.getCorpCode();
		//用户id
		String userId = curUser.getUserId().toString();
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
            
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            mtVo.setCorpCode(corpCode);
          //查询移动财务短信及群发短信
            mtVo.setMsTypes("7");
            //任务状态查询
            mtVo.setTaskState(taskState);
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
        
				SerMtTaskExcelTool et=new SerMtTaskExcelTool(excelPath);
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
		    EmpExecutionContext.error(e,"下行业务记录的excel导出异常！");
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
			EmpExecutionContext.error(e,"下行业务记录检查关键字异常！");
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
			EmpExecutionContext.error(e,"下行业务记录撤销任务前的判断异常！");
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
		
		String mtId = request.getParameter("mtId");
		String mobileUrl = request.getParameter("mobileUrl");
		Integer subState = Integer.valueOf(request.getParameter("subState"));
		String result = "";
		SmsBiz mtBiz=new SmsBiz();
		
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		opContent ="提交短信任务（短信任务ID："+mtId+"）";
		opType = StaticValue.OTHER;
		LfSysuser sysuser = (LfSysuser) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("loginSysuser");
		String opUser = sysuser.getUserName();
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
			//处理取消审核流程
			spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		}catch(Exception e)
		{
			result="error";
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e,"下行业务记录撤销任务异常！");
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
			if(lfMttask.getIsRetry() != null && lfMttask.getIsRetry() == 1)
			{
				response.getWriter().print("isretry");
				return;
			}
			//为了避免用户在上次请求还未返回时刷新页面.再次点失败重发造成的重复发送问题.这儿加一个Session判断这种情况的发生.
			if(request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("isretryMtId") !=null)
			{
				String a = request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("isretryMtId").toString();
				if(a.equals(mtid.toString()))
				{
					//已经重发
					response.getWriter().print("isretry");
					return;
				}			
			}
			request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("isretryMtId", mtid);
		
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
			//需要将原来的文件重生产生一个。
			//Date time = Calendar.getInstance().getTime();
			String[] url  = txtfileutil.getSaveUrl(lfMttask.getUserId());
			String oldPath = txtfileutil.getWebRoot()+lfMttask.getMobileUrl();
			txtfileutil.copyFile(oldPath, url[0]);
			lfMttask.setMobileUrl(url[1]);
			
			//获取发送信息等缓存数据（是否计费、用户编码）
			Map<String,String> infoMap = (Map<String,String>) request.getSession(ZnyqParamValue.GET_SESSION_FALSE).getAttribute("infoMap");
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
				request.getSession(ZnyqParamValue.GET_SESSION_FALSE).setAttribute("isretryMtId", "");
			}
			
				
			opContent+="(任务名称:"+lfMttask.getTitle()+")";
			//存日志
			spLog.logSuccessString(username, opModule, opType, opContent,corpcode);
			
		} catch (Exception e) {
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
//			{
			if(e.getClass().isAssignableFrom(HttpHostConnectException.class)){
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
				EmpExecutionContext.error(e2, "更新任务表异常。");
			}			
			
			EmpExecutionContext.error("EBFB010");
			EmpExecutionContext.error(e,"下行业务记录模块失败重发异常！");
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
			EmpExecutionContext.error(e,"下行业务记录检查文件是否存在异常！");
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
			EmpExecutionContext.error(e, "下行业务记录获取数字转型异常。");
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
		HttpSession session = request.getSession(ZnyqParamValue.GET_SESSION_FALSE);
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
			EmpExecutionContext.error(e,"下行业务记录检查结果值异常！");
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
				if(useridstr != null && useridstr.length() > 0){
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
						if(!lastrecord.getRLevel().equals(lastrecord.getRLevelAmount())){
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
			EmpExecutionContext.error(e,"下行业务记录获取信息发送查询里的详细异常！");
		}
	}
	
	public String defaultVal(Object obj){
		return obj==null?"":String.valueOf(obj);
	}
}