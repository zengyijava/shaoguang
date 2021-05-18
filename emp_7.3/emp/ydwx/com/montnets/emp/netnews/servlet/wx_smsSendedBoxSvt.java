package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
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
import com.montnets.emp.netnews.biz.SmstaskBiz;
import com.montnets.emp.netnews.biz.SmstaskExcelTool;
import com.montnets.emp.netnews.vo.LfMttaskVo;
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
public class wx_smsSendedBoxSvt extends BaseServlet {


	private final SmstaskBiz smstaskBiz=new SmstaskBiz();
	private final String empRoot="ydwx";
	private final String basePath="/report";
	private final SuperOpLog spLog = new SuperOpLog();
	private final BaseBiz baseBiz = new BaseBiz();
	private final String path=new TxtFileUtil().getWebRoot();
	//模板路径
	protected final String  excelPath = path + "ydwx/report/file/";

	//常用文件读写工具类
	private final TxtFileUtil txtfileutil = new TxtFileUtil();
	

	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		//短信biz
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String requestPath = request.getRequestURI();
		String path = requestPath.substring(requestPath.lastIndexOf("/")).replaceAll(".htm", ".jsp");
		String titlePath="";
		titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/wx_wmsTaskRecord.jsp->wmsTaskRecord

		PageInfo pageInfo = new PageInfo();
		try
		{
			//导出方法的查询条件清空
			request.getSession(false).setAttribute("r_sendTime", null);
			request.getSession(false).setAttribute("r_recvTime", null);
			request.getSession(false).setAttribute("r_userIds", null);
			request.getSession(false).setAttribute("r_depIds", null);
			request.getSession(false).setAttribute("r_sendTitle", null);
			request.getSession(false).setAttribute("r_netName", null);
			
			boolean isFirstEnter = pageSet(pageInfo,request);
			//当前登录用户的企业编码
//			String corpCode =request.getParameter("lgcorpcode");
			//从session取值，为了防止请求中恶意攻击
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = sysuser==null?"":sysuser.getCorpCode();
			
			//String  userId =request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);
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

			String spUser = request.getParameter("spUser");
			String depid = request.getParameter("depid");
			String userid = request.getParameter("userid");
			String startSubmitTime = request.getParameter("sendtime");
			String endSubmitTime = request.getParameter("recvtime");
            String busCode = request.getParameter("busCode");
            String title = request.getParameter("title");
            String netid = request.getParameter("netid");
            String netname = request.getParameter("netname");
            String taskid = request.getParameter("taskid");
            //获取过滤条件
            userid = (userid != null && userid.trim().length()>0 && !userid.equals(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request))&&userid.indexOf(",")!=-1)?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            
            //是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            if(isContainsSun!=null&&!"".equals(isContainsSun)){
            	mtVo.setIsContainsSun(isContainsSun);
            }else{
            	mtVo.setIsContainsSun("");
            }
            
           //查询移动财务短信及群发短信
            mtVo.setMsTypes("6");
			if(!isFirstEnter){
				mtVo.setNetid(netid);
				mtVo.setNetname(netname);
				mtVo.setSpUser(spUser);
				if (busCode!=null && !"".equals(busCode)){
					mtVo.setBusCode(busCode);
				}
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);
				if(taskid!=null&&!"".equals(taskid)){
					mtVo.setTaskId(Long.parseLong(taskid));
				}
			
				if(title != null && title.length() > 0)
				{
					mtVo.setTitle(title);
				}
				//如果用户请求"群发历史查询"，则查询出所有已发历史记录
				if(titlePath.equals("wmsTaskRecord"))
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
				
			
	            request.getSession(false).setAttribute("r_netid", netid);
	            request.getSession(false).setAttribute("r_taskid", taskid);
	            request.getSession(false).setAttribute("r_spUser", spUser);
				//设置导出的查询条件
				request.getSession(false).setAttribute("r_sendTime", mtVo.getStartSendTime());
				request.getSession(false).setAttribute("r_recvTime", mtVo.getEndSendTime());
				request.getSession(false).setAttribute("r_userIds", mtVo.getUserIds());
				request.getSession(false).setAttribute("r_depIds", mtVo.getDepIds());
				request.getSession(false).setAttribute("r_sendTitle", mtVo.getTitle());
				request.getSession(false).setAttribute("r_netName", mtVo.getNetname());
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
			
			
			
			//如果从短信助手或短信客服请求群发任务查看，则统一转向wx_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/wx_smsSendedBox.jsp";
			}
			//加密处理，防止攻击
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
				encryptOrDecrypt.batchEncrypt(mtVoList, "MtId", "StrMtID");
			}
			
            request.getSession(false).setAttribute("rpageInfo", pageInfo);
			request.setAttribute("title",title);
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", userId);//当前登录用户id
			//加载机构树
			String departmentTree = smstaskBiz.getDepartmentJosnData(Long.parseLong(userId),corpCode);
			//如果用户请求是"群发历史查询"
			
			
			request.setAttribute("departmentTree", departmentTree);
			request.setAttribute("titlePath", titlePath);
			
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				setLog(request, "网讯发送统计", opContent, "GET");
			}
			
			//页面跳转
			request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "群发历史与群发任务查询异常!");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
			request.setAttribute("titlePath", titlePath);
			request.setAttribute("pageInfo", pageInfo);
			//如果从短信助手或短信客服请求群发任务查看，则统一转向wx_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/wx_smsSendedBox.jsp";
			}
			try {
				request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "获取群发历史与群发任务异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "获取群发历史与群发任务异常!");
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
			//从session取值，为了防止请求中恶意攻击
			LfSysuser user = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String corpCode = user==null?"":user.getCorpCode();
			LinkedHashMap<String, String> condition = new LinkedHashMap<String, String>();
			condition.put("mtId",mtID);
			condition.put("corpCode", corpCode);
			LfMttask mttask =null;
			List<LfMttask> taskList = baseBiz.getByCondition(LfMttask.class, condition,null);
			if(taskList!=null&&taskList.size()>0){
				mttask=taskList.get(0);
			}else{
				EmpExecutionContext.error("群发历史页面回复详情 查询人员!");
			}
//			LfMttask mttask = baseBiz.getById(LfMttask.class, mtID);
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
				if(flowRecords==null || flowRecords.size() == 0)
				{
					buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>无记录</td></tr>");
				}else
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
				}
			}else
			{
				buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>无记录</td></tr>");
			}
			buffer.append("</tbody></table>");
			response.getWriter().print(buffer.toString());
		}catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取群发历史页面回复详情异常!");
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
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
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
			EmpExecutionContext.error(e, "网讯发送统计，生成机构树字符串，异常。depId="+depId);
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
			String titlePath="";
			//获取当前登录用户的userid
			//String userid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userid = SysuserUtil.strLguserid(request);

			String deptUserTree="";
			deptUserTree = getDeptUserJosnData(titlePath,Long.parseLong(userid));
			response.getWriter().print(deptUserTree);
			EmpExecutionContext.info(deptUserTree);
		}
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "查询条件中操作员树的加载异常!");
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
					lfDeps = depBiz.getAllDepByUserIdAndCorpCode(userid,currUser.getCorpCode());
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
					if(titlePath.equals("wmsTaskRecord"))
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
				EmpExecutionContext.error(e, "操作员树的加载异常!");
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
				EmpExecutionContext.error(e, "操作员树的加载异常!");
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
	@SuppressWarnings("unchecked")
	public void getSinglePerNoticDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try
		{
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			JSONObject json = new JSONObject();  
			response.setContentType("text/html;charset=UTF-8");

			//此处解密（网讯中的发送查询）
			String mtid="-1";
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
				mtid=encryptOrDecrypt.decrypt(request.getParameter("mtId"));
			}
			//短信任务id
			Long mtId = Long.valueOf(mtid);

		 	Integer pageIndex = Integer.valueOf(request.getParameter("pageIndex"));		
	    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	CommonVariables  CV = new CommonVariables();
	    	Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
			int ishidephome=0;//是否隐藏手机号
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
				ishidephome=1;
			}
	    	//根据短信id，获取短信信息
	    	LfMttask mt = null;
			LfSysuser objuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			LinkedHashMap<String, String> mttaskmap = new LinkedHashMap<String, String>();
			mttaskmap.put("corpCode", objuser.getCorpCode());
			mttaskmap.put("mtId", mtId+"");
			 List<LfMttask> mtTaskList =baseBiz.getByCondition(LfMttask.class, mttaskmap, null);
			 if(mtTaskList!=null&&mtTaskList.size()>0){
				 mt=mtTaskList.get(0);
			 }else{
				 EmpExecutionContext.error("获取信息发送查询里的详细异常！");
			 }
	    	
//	    	LfMttask mt=baseBiz.getById(LfMttask.class, mtId);
	    	
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
		 	LinkedHashMap<String, String> map=new LinkedHashMap<String, String>();
		 	//短信id
			map.put("mtId", String.valueOf(mt.getMtId()));
			//List<LfTask> lftaskList=baseBiz.getByCondition(LfTask.class, map,null);
			//if(lftaskList!=null&&lftaskList.size()>0)
			//{
			//	LfTask lftask=lftaskList.get(0);
				//taskid
				conditionMap.put("taskId", mt.getTaskId()!=null?mt.getTaskId().toString():"-1");
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
		catch (Exception e) {
			EmpExecutionContext.error(e, "回复查询异常！");
			//异常错误
			response.getWriter().write("error");   
		}
	}
	
	/**
	 * 发送详情信息查看
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void findAllSendInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{	
		SimpleDateFormat hms=new SimpleDateFormat("HH:mm:ss");
		//日志开始时间
		long startl=System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		boolean isFirstEnter = pageSet(pageInfo, request);
		int count = 0;
		
		//获取页面传过来的lf_mttask表中的mtid
        String mtid  = request.getParameter("mtid");
		//此处解密（网讯中的发送查询）

		Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
		//加密对象不为空
		if(encrypOrDecrypttobject != null)
		{
			//强转类型
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			mtid=encryptOrDecrypt.decrypt(mtid);
		}
        //获取页面传过来的查询条件
        String spisuncm = request.getParameter("spisuncm");//运营商
        String phone = request.getParameter("phone");//手机号
        String taskId = request.getParameter("taskId");
        //获取页面传过来的类型（1：查询全部，2：查询接收错误，3:查询提交错误,4:带页面查询条件的）
        String type = request.getParameter("type");
		
		try 
		{
			if(mtid == null)
			{
				EmpExecutionContext.error("网讯发送统计详情查询，获取不到mtid。");
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
//            LfMttask Lfmttask = baseBiz.getById(LfMttask.class, mtid);
            
	    	LfMttask Lfmttask = null;
			LfSysuser objuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			if(objuser==null||objuser.getCorpCode()==null||"".equals(objuser.getCorpCode())){
				EmpExecutionContext.error("网讯发送统计详情查询，获取不到corpCode。");
				return;
			}
			LinkedHashMap<String, String> mttaskmap = new LinkedHashMap<String, String>();
			String corpCode=objuser.getCorpCode();
			mttaskmap.put("corpCode", corpCode);
			mttaskmap.put("mtId", mtid);
			 List<LfMttask> mtTaskList =baseBiz.getByCondition(LfMttask.class, mttaskmap, null);
			 if(mtTaskList!=null&&mtTaskList.size()>0){
				 Lfmttask=mtTaskList.get(0);
			 }
            
            if(Lfmttask == null)
            {
            	EmpExecutionContext.error("网讯发送统计详情查询，获取不到发送任务对象。mtid="+mtid);
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
            request.getSession(false).setAttribute("wxMttask", Lfmttask);
            //任务对象存到session中，方便分页的时候用
			request.getSession(false).setAttribute("wxConcondition", conditionMap);
            
			request.setAttribute("isFirstEnter", isFirstEnter);
			
			//第一次进入页面不查询
			if(isFirstEnter)
			{
				return;
			}
            
			Object realDbpageInfoObj = request.getSession(false).getAttribute("wx_realDbpageInfo");
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
    		List<SendedMttaskVo> MttaskvoList = smstaskBiz.getMtTaskList(Lfmttask, conditionMap, pageInfo,realDbpageInfo);
    		
    		request.getSession(false).setAttribute("wx_realDbpageInfo", realDbpageInfo);
			//短信list
			request.setAttribute("mtList",MttaskvoList);
			//没数据则不需要查询分页
			if(MttaskvoList == null || MttaskvoList.size() == 0)
			{
				pageInfo.setNeedNewData(2);
			}
			count = MttaskvoList==null?0:MttaskvoList.size();
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e, "网讯发送统计详情查询，异常。");
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
			String opContent = "网讯发送统计详情查询。" + count + "条,第"+pageInfo.getPageIndex()+"页 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms"
				+",条件：spisuncm="+spisuncm
				+",phone="+phone
				+",type="+type
				+",mtid="+mtid
				+",taskId="+taskId;
			setLog(request, "移动网讯", opContent, StaticValue.GET);
			
			//页面跳转
			request.getRequestDispatcher(empRoot + basePath + "/wx_smsTaskAllSendRecord.jsp")
			.forward(request, response);
		}
	}
	
	/**
	 * 发送详情分页信息
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
		int count = 0;
		//分页对象
		PageInfo pageInfo = new PageInfo();
		try
		{
			LfMttask Lfmttask = (LfMttask)request.getSession(false).getAttribute("wxMttask");
			LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getSession(false).getAttribute("wxConcondition");
			
			if(Lfmttask == null || conditionMap == null)
			{
				EmpExecutionContext.error("网讯发送统计详情查询分页，获取任务对象或查询条件对象为空。");
				return;
			}
			
			pageSet(pageInfo, request);
			
			//实时库分页对象
			Object realDbpageInfoObj = request.getSession(false).getAttribute("wx_DetailRealDbpageInfo");
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
			
			//发送详情
    		smstaskBiz.getMtTaskListPageInfo(Lfmttask, conditionMap, pageInfo,realDbpageInfo);
    		
			//设置到session中
			request.getSession(false).setAttribute("wx_DetailRealDbpageInfo", realDbpageInfo);
			
			conditionstr = "spisuncm:"+conditionMap.get("unicom")
							+ ",phone:"+conditionMap.get("phone")
							+ ",taskid:"+Lfmttask.getTaskId();
			
			count = pageInfo.getTotalRec();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计详情查询分页，异常。");
		}
		finally
		{
			response.getWriter().print("{totalRec:"+pageInfo.getTotalRec()+",totalPage:"+pageInfo.getTotalPage()+"}");
			//开始时间
			String starthms=hms.format(startl);
			String opContent = "网讯发送统计详情查询分页。" + count + "条 ,开始："+starthms+",耗时:"+(System.currentTimeMillis()-startl)+"ms,条件："+conditionstr;
			setLog(request, "移动网讯", opContent, StaticValue.GET);
		}
	}
	
	/**
	 * 网讯发送详情的excel导出方法(包括提交失败，接收失败的全部导出)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void smsReportAllExcel(HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		long startTime = System.currentTimeMillis();
		//用户id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		//是否导出全部
		String IsexportAll = request.getParameter("IsexportAll");
		try 
		{
			LfMttask Lfmttask = (LfMttask)request.getSession(false).getAttribute("wxMttask");
			if(Lfmttask == null)
			{
				EmpExecutionContext.error("网讯发送统计详情导出，获取任务对象为空。lguserid="+lguserid+",IsexportAll="+IsexportAll);
				response.getWriter().print("false");
				return;
			}
			LinkedHashMap<String, String> conditionMap = (LinkedHashMap<String, String>)request.getSession(false).getAttribute("wxConcondition");
			if(conditionMap == null)
			{
				EmpExecutionContext.error("网讯发送统计详情导出，获取查询条件为空。lguserid="+lguserid+",IsexportAll="+IsexportAll);
				response.getWriter().print("false");
				return;
			}
			//实时库分页对象
			Object realDbpageInfoObj = request.getSession(false).getAttribute("wx_excelRealDbpageInfo");
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
			
			//发送详情
			List<SendedMttaskVo> MttaskvoList = smstaskBiz.getMtTaskList(Lfmttask, conditionMap, null,realDbpageInfo);
			if (MttaskvoList == null || MttaskvoList.size() == 0) 
			{
				response.getWriter().print("false");
				return;
			}
			
		    //设置到session中
		    request.getSession(false).setAttribute("wx_excelRealDbpageInfo", realDbpageInfo);
			Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
			int ishidephome=0;
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null)
			{
				ishidephome=1;
			}
			SmstaskExcelTool et=new SmstaskExcelTool(excelPath);
			Map<String, String> resultMap = et.createSmsMtDetailExcel(MttaskvoList, IsexportAll, ishidephome,request);
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
			String fileName=(String)resultMap.get("FILE_NAME");
	        String filePath=(String)resultMap.get("FILE_PATH");
	        
			request.getSession(false).setAttribute("smsReportAllExcel", fileName+"@@"+filePath);
	        //用于判断是否下载加载完成了
	        request.getSession(false).setAttribute("checkOver"+lguserid, "true");
	        response.getWriter().print("true");
	        
	        //操作日志
	        setLog(request, "移动网讯", "网讯发送统计详情导出，共导出"+MttaskvoList.size()+"条记录,耗时："+(System.currentTimeMillis()-startTime)+"ms。", StaticValue.OTHER);

	        MttaskvoList.clear();
	        MttaskvoList = null;
		}
		catch (Exception e) 
		{		
			EmpExecutionContext.error(e, "网讯发送统计详情导出，异常。");
			response.getWriter().print("false");
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
			EmpExecutionContext.error(e, "获取机构树异常!");
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
		String depId = request.getParameter("depId");
		try
		{
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			String deptUserTree = smstaskBiz.getDepUserJosn(depId, curUser);
			deptUserTree = deptUserTree.replaceAll("已注销", MessageUtils.extractMessage("ydwx", "ydwx_jsp_out_yizhuxiao", request));
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
			EmpExecutionContext.error(e, "网讯发送统计，生成机构操作员树字符串，异常。depId="+depId);
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
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String requestPath = request.getRequestURI();
		String titlePath="";
		String path = requestPath.substring(requestPath.lastIndexOf("/")).replaceAll(".htm", ".jsp");
		titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
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
        String netid = request.getParameter("netid");
        String netname = request.getParameter("netname");
        String taskid=null;
		//String deptUserTree="";
		PageInfo pageInfo = new PageInfo();
		String skip=request.getParameter("skip");
		try
		{
	        if("true".equals(skip)){
	        	pageInfo=(PageInfo)request.getSession(false).getAttribute("rpageInfo");
	        	netid= (String)request.getSession(false).getAttribute("r_netid");
	        	taskid=(String) request.getSession(false).getAttribute("r_taskid");
		            spUser =  (String) request.getSession(false).getAttribute("r_spUser");
					//设置导出的查询条件
		            startSubmitTime=request.getSession(false).getAttribute("r_sendTime")!=null?(String)request.getSession(false).getAttribute("r_sendTime"):"";
		            endSubmitTime=request.getSession(false).getAttribute("r_recvTime")!=null?(String)request.getSession(false).getAttribute("r_recvTime"):"";
		            userid=request.getSession(false).getAttribute("r_userIds")!=null?(String)request.getSession(false).getAttribute("r_userIds")+",":"";
		            depid=request.getSession(false).getAttribute("r_depIds")!=null?(String)request.getSession(false).getAttribute("r_depIds"):"";
		            title=request.getSession(false).getAttribute("r_sendTitle")!=null?(String)request.getSession(false).getAttribute("r_sendTitle"):"";
		            netname=request.getSession(false).getAttribute("r_netName")!=null?(String)request.getSession(false).getAttribute("r_netName"):"";
	        }
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

			List<LfSpDepBind> userList = baseBiz.getByCondition(
					LfSpDepBind.class, conditionMap, null);

			
            userid = (userid != null && userid.length()>0 && !userid.equals(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request)))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            //查询移动财务短信及群发短信
            mtVo.setMsTypes("6");
			mtVo.setNetid(netid);
			mtVo.setNetname(netname);
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
			if(titlePath.equals("wmsTaskRecord"))
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
			
			if(taskid!=null&&!"".equals(taskid)){
				mtVo.setTaskId(Long.parseLong(taskid));
			}
			if(corpCode != null && corpCode.equals("100000"))
			{
				mtVoList = smstaskBiz.getLfMttaskVoWithoutDomination( mtVo, pageInfo);
			}else
			{
				mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
			}
			
			//加密处理，防止攻击
			Object encrypOrDecrypttobject = (ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
				encryptOrDecrypt.batchEncrypt(mtVoList, "MtId", "StrMtID");
			}
			
			//如果从短信助手或短信客服请求群发任务查看，则统一转向wx_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/wx_smsSendedBox.jsp";
			}

			request.setAttribute("skip", skip);
	        
	        request.setAttribute("depid", depid);
	        request.setAttribute("userid", userid);
	        request.setAttribute("sendtime", startSubmitTime);
	        request.setAttribute("recvtime", endSubmitTime);
	        request.setAttribute("busCode", busCode);
	        request.setAttribute("netid", netid);
	        request.setAttribute("netname", netname);
	        request.setAttribute("spUser", spUser);
	        request.setAttribute("taskid", taskid);
	       
			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", false);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			
			String departmentTree = smstaskBiz.getDepartmentJosnData(Long.parseLong(userId),corpCode);
			//如果用户请求是"群发历史查询"			
			request.setAttribute("departmentTree", departmentTree);

			request.setAttribute("titlePath", titlePath);
			request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "群发历史查询异常!");
			request.setAttribute("findresult", "-1");
			//回到页面第一次加载时的状态
			request.setAttribute("isFirstEnter", true);
			request.setAttribute("pageInfo", pageInfo);
			//如果从短信助手或短信客服请求群发任务查看，则统一转向wx_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/wx_smsSendedBox.jsp";
			}
			try {
				request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(e1, "群发历史查询异常!");
			} catch (IOException e1) {				
				EmpExecutionContext.error(e1, "群发历史查询异常!");
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
		
		//下行短信list
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		PrintWriter out = response.getWriter();
		//企业编码
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		if(sysuser==null||sysuser.getCorpCode()==null||"".equals(sysuser.getCorpCode())){
			EmpExecutionContext.error("网讯发送统计详情报表导出，获取不到corpCode。");
			out.print("false");
			return;
		}
		String corpCode =sysuser.getCorpCode();
		//用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
        try
        {
        	String spUser = request.getParameter("spuser");
			String depids = (String)request.getSession(false).getAttribute("r_depIds");
			String userids = (String)request.getSession(false).getAttribute("r_userIds");

			String startSubmitTime = (String)request.getSession(false).getAttribute("r_sendTime");
			String endSubmitTime = (String)request.getSession(false).getAttribute("r_recvTime");
			String netname = (String)request.getSession(false).getAttribute("r_netName");
			String title = (String)request.getSession(false).getAttribute("r_sendTitle");
			
            String netid = request.getParameter("netid");
            String taskid = request.getParameter("taskid");
            
            //是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            if(isContainsSun!=null&&!"".equals(isContainsSun)){
            	mtVo.setIsContainsSun(isContainsSun);
            }else{
            	mtVo.setIsContainsSun("");
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
            mtVo.setMsTypes("6");
            mtVo.setNetid(netid);
            mtVo.setNetname(netname);
            mtVo.setSpUser(spUser);
			if (busCode!=null && !"".equals(busCode)){
				mtVo.setBusCode(busCode);
			}
			//任务批次
			if (taskid!=null && !"".equals(taskid)){
				mtVo.setTaskId(Long.parseLong(taskid));
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
				Map<String, String> resultMap = et.createMtReportExcel(mtVoList,request);
				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        
		        //写日志
				long end_time=System.currentTimeMillis();
				String opContent = "导出开始时间："+format.format(System.currentTimeMillis())+",耗时:"+(end_time-begin_time)+",导出总数："+mtVoList.size() + "条 ";
				setLog(request, "网讯发送统计", opContent, StaticValue.GET);
				request.getSession(false).setAttribute("ReportCurrPageExcel", fileName+"@@"+filePath);
				out.print("true");
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
			}
			else
			{
				out.print("false");
				 //response.sendRedirect(request.getContextPath()+"/wx_wmsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
			out.print("false");
			   //异常打印
				//response.sendRedirect(request.getContextPath()+"/wx_wmsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			    EmpExecutionContext.error(e, "群发历史查询的excel导出异常!");
//			   this.find(request, response);
		} 
	}
	
	/**
	 * excel文件导出
	 * 
	 * @param request
	 * @param response
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String down_session = request.getParameter("down_session");
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute(down_session);
			if(obj == null)
		    {
		    	EmpExecutionContext.error("网讯发送统计导出excel下载，获取不到会话对象。");
		    	return;
		    }
			String result = (String) obj;
			if(result.indexOf("@@") < 0)
			{
				EmpExecutionContext.error("网讯发送统计导出excel下载，获取不到分隔参数。");
				return;
			}
			String[] file = result.split("@@");
			// 弹出下载页面。
			DownloadFile dfs = new DownloadFile();
			dfs.downFile(request, response, file[1], file[0]);
			session.removeAttribute(down_session);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送统计导出excel下载，异常。");
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
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		if(sysuser==null||sysuser.getCorpCode()==null||"".equals(sysuser.getCorpCode())){
			EmpExecutionContext.error("网讯发送统计详情报表导出，获取不到corpCode。");
			return;
		}
		String corpCode =sysuser.getCorpCode();
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
            String netid = request.getParameter("netid");
            String netname = request.getParameter("netname");
            userid = (userid != null && userid.length()>0 && !userid.equals(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request)))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
          //查询移动财务短信及群发短信
            mtVo.setMsTypes("6");
            mtVo.setNetid(netid);
            mtVo.setNetname(netname);
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
				Map<String, String> resultMap = et.createSmsSendedBoxExcel(mtVoList,request);
				
				 String opContent = "导出网讯发送统计发送详情：" + mtVoList.size() + "条成功 ";
					setLog(request, "网讯发送统计发送统计发送详情", opContent, StaticValue.GET);
				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        	
		        DownloadFile dfs=new DownloadFile();
		        dfs.downFile(request, response, filePath, fileName);
			}
			else
			{
				response.sendRedirect(request.getContextPath()+"/wx_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			}
		} catch (Exception e) {		
			//异常处理
			response.sendRedirect(request.getContextPath()+"/wx_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		    EmpExecutionContext.error(e, "群发任务查看的excel导出异常!");
		} 
	}
	
	//停止网关发送中的任务
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
			EmpExecutionContext.error(e, "停止网关发送中的任务异常!");
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
//		if(tmMsg!=null&!"".equals(tmMsg))
//		{
//			tmMsg=tmMsg.toUpperCase();
//		}
		String words=new String();
		try
		{
			KeyWordAtom keyWordAtom=new KeyWordAtom();
			words=keyWordAtom.checkText(tmMsg,corpCode);
		} catch (Exception e)
		{
			words="error";
			EmpExecutionContext.error(e, "发送内容关键字检查异常!");
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
			EmpExecutionContext.error(e, "撤销异常!");
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
		String opUser = "";
		
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
			Object obsysuser=request.getSession(false).getAttribute("loginSysuser");
			if(obsysuser!=null){
				LfSysuser user=(LfSysuser)obsysuser;
				if(user.getUserName()!=null){
					opUser=user.getUserName();
				}
			}
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
				//调用撤消任务的方法
				//根据任务id查出短信任务10-21 LIB包导致修改
				LfMttask lfMttask = baseBiz.getById(LfMttask.class, mtId);
				result=mtBiz.cancelSmsTask(lfMttask,subState);
			}
			spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		}catch(Exception e)
		{
			//异常处理
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
//			{
			if(e.getClass().isAssignableFrom(HttpHostConnectException.class)){
				result=e.getLocalizedMessage();
			}else
			{
				result="error";
			}
			spLog.logFailureString(opUser, opModule, opType, opContent+opSper, e,lgcorpcode);
			EmpExecutionContext.error(e, "撤消发送任务异常!");
		}finally
		{
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			WGStatus wg=new WGStatus(langName);
			if(!"000".equals(result) && wg.getInfoMap().get(result)!=null)
			{
				result=wg.getInfoMap().get(result);
			}
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
		SmsSendBiz cfsb = new SmsSendBiz();
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
			String strmtid=request.getParameter("mtid");
			//短信任务id
//			mtid = Long.parseLong(strmtid);
			//获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密mtId
			mtid=Long.parseLong(encryptOrDecrypt.decrypt(strmtid));
			username = request.getParameter("lgusername");
//			corpcode = request.getParameter("lgcorpcode");
			//从session获取，避免攻击
			LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			corpcode = sysuser.getCorpCode();
			
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
			if(lfMttask.getIsRetry() != null && lfMttask.getIsRetry().toString().equals("1"))
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
			//需要将原来的文件重生产生一个。
			Date time = Calendar.getInstance().getTime();
			String[] url  = txtfileutil.getSaveUrl(lfMttask.getUserId());
			String oldPath = txtfileutil.getWebRoot()+lfMttask.getMobileUrl();
			txtfileutil.copyFile(oldPath, url[0]);
			lfMttask.setMobileUrl(url[1]);
			//获取发送信息等缓存数据（是否计费、是否审核、用户编码）
			Map<String,String> infoMap = (Map<String,String>) request.getSession(false).getAttribute("infoMap");
			//调用发送方法
			result = cfsb.addSmsLfMttaskResend(lfMttask, infoMap);
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
				EmpExecutionContext.error(e2,"重新提交异常!");
				result ="error";
			}			
			
			EmpExecutionContext.error("EBFB010");
			EmpExecutionContext.error(e, "失败,重提异常!");
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
			EmpExecutionContext.error(e, "检查文件是否存在异常!");
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
			//异常处理
			EmpExecutionContext.error(e,"数字转换异常");
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
	//	String userId = request.getParameter("lguserid");
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
			EmpExecutionContext.error(e, "检查结果值异常!");
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
	public void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
	
}