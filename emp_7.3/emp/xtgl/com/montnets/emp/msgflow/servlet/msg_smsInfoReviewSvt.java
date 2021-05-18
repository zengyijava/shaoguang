package com.montnets.emp.msgflow.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobConfigBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfPageField;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.msgflow.biz.MsgExamineBiz;
import com.montnets.emp.servmodule.xtgl.constant.ServerInof;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.http.conn.HttpHostConnectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *   信息 审批 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class msg_smsInfoReviewSvt extends BaseServlet {
	private final String empRoot="xtgl";
	private final MsgExamineBiz examineBiz = new MsgExamineBiz();
	private final String basePath="/msgflow";
	private final BaseBiz baseBiz=new BaseBiz();
	public static final String opModule= "信息审批";


	
	@SuppressWarnings("unchecked")
	public void find(HttpServletRequest request, HttpServletResponse response){
		
		try {
			PageInfo pageInfo=new PageInfo();
			//是否第一次打开
			boolean isFirstEnter = false;
			String isOperateReturn = request.getParameter("isOperateReturn");
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LfSysuser lfsysuser = getLoginUser(request);
			//日志开始时间
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			long begin_time=System.currentTimeMillis();
			//审批状态
			String conrstate = "";
			//信息类型
			String conreviewtype = "";
			if("true".equals(isOperateReturn))
			{
				pageInfo = (PageInfo) request.getSession(false).getAttribute("msg_smsPageInfo");
				conditionMap = (LinkedHashMap<String, String>) request.getSession(false).getAttribute("msg_smsconditionMap");
				conrstate = (String) request.getSession(false).getAttribute("msg_smsconrstate");
				conreviewtype = (String) request.getSession(false).getAttribute("msg_smsconreviewtype");
			}
			else
			{
				isFirstEnter=pageSet(pageInfo, request);
				if(!isFirstEnter){
					//主题
					String conrcontent = request.getParameter("conrcontent");
					if(conrcontent != null && !"".equals(conrcontent)){
						conditionMap.put("RContent&like",conrcontent);
					}
					//发送人
					String conproname = request.getParameter("conproname");
					if(conproname != null && !"".equals(conproname)){
						conditionMap.put("ProUserCode",conproname);
					}
					//审批状态
					conrstate = request.getParameter("conrstate");
					if(conrstate != null && !"".equals(conrstate)){
						//审批完成
						if("3".equals(conrstate)){
							conditionMap.put("isComplete", "1");
							conditionMap.put("RState", "-1");
						//未审批
						}else if("-1".equals(conrstate)){
							conditionMap.put("isComplete", "2");
							conditionMap.put("RState", "-1");
						}else{
							conditionMap.put("RState",conrstate);
						}
					}
					//提交起始时间
					String submitSartTime = request.getParameter("startSubmitTime");
					if(submitSartTime != null && !"".equals(submitSartTime)){
						conditionMap.put("submitTime&>",submitSartTime);
					}
					//提交结束时间
					String submitEndTime = request.getParameter("endSubmitTime");
					if(submitEndTime != null && !"".equals(submitEndTime)){
						conditionMap.put("submitTime&<",submitEndTime);
					}
					//信息类型  1短信2彩信
					conreviewtype = request.getParameter("conreviewtype");
					if(conreviewtype != null && !"".equals(conreviewtype)){
						conditionMap.put("infoType",conreviewtype);
					}else{
						conditionMap.put("infoType&in","1,2,5");
					}
				}else{
					conditionMap.put("infoType&in","1,2,5");
				}
				conditionMap.put("userCode",String.valueOf(lfsysuser.getUserId()));
				request.getSession(false).setAttribute("msg_smsPageInfo", pageInfo);
				request.getSession(false).setAttribute("msg_smsconditionMap", conditionMap);
				request.getSession(false).setAttribute("msg_smsconreviewtype", conreviewtype);
				request.getSession(false).setAttribute("msg_smsconrstate", conrstate);
			}
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("frId", StaticValue.DESC);
			//根据条件查询出“审核流程记录”
			List<LfFlowRecord> recordList = baseBiz.getByConditionNoCount(LfFlowRecord.class, null, conditionMap, orderbyMap, pageInfo);
			
			//查询出审批操作员
			LinkedHashMap<String,String> userMap = new LinkedHashMap<String,String>();
//			userMap.put("infoType&in","1,2");
//			userMap.put("userCode",String.valueOf(lfsysuser.getUserId()));
			List<LfFlowRecord> userrecordList = baseBiz.getByCondition(LfFlowRecord.class, null, conditionMap, orderbyMap);
			StringBuffer sb = new StringBuffer();
			if(userrecordList != null && userrecordList.size()>0){
				Set<Long> hashSet=new HashSet<Long>();
				for(LfFlowRecord temp:userrecordList){
					hashSet.add(temp.getProUserCode());
					//sb.append(temp.getProUserCode()).append(",");
				}
				for (Long userid:hashSet)
				{
					sb.append(userid).append(",");
				}
			}
			List<LfSysuser> userList = null;
			userMap.clear();
			LinkedHashMap<Long,String> usernameMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				userMap.put("userId&in", sb.toString());
				userMap.put("corpCode", lfsysuser.getCorpCode());
				LinkedHashMap<String, String> orderbyUserMap = new LinkedHashMap<String, String>();
				orderbyUserMap.put("name", StaticValue.ASC);
				userList = baseBiz.getByCondition(LfSysuser.class, userMap, orderbyUserMap);
				if(userList != null && userList.size()>0){
					for(LfSysuser user:userList){
						String name =  user.getName();
						if(user.getUserState() != null && user.getUserState() ==2){
							name = name + "<font color='red'>("+MessageUtils.extractMessage("xtgl", "xtgl_cswh_dxmbgl_yzx", request)+")</font>";
						}else if(user.getUserState() != null && user.getUserState() == 0){
							name = name + "<font color='red'>("+MessageUtils.extractMessage("xtgl", "xtgl_cswh_dxmbgl_yjy", request)+")</font>";
						}else if(user.getUserState() != null && user.getUserState() == 3){
							name = name + "<font color='red'>("+MessageUtils.extractMessage("xtgl", "xtgl_cswh_dxmbgl_ysd", request)+")</font>";
						}
						usernameMap.put(user.getUserId(),name);
					}
				}
			}
			//request.setAttribute("mtidSet",mtidSet);
			 //获取系统定义的短彩类型值
			List<LfPageField> pagefileds = new GlobConfigBiz().getPageFieldById("100001");
			request.setAttribute("pagefileds", pagefileds);
			request.setAttribute("conreviewtype",conreviewtype);
			request.setAttribute("conrstate",conrstate);
			request.setAttribute("conditionMap",conditionMap);
			request.setAttribute("usernameMap",usernameMap);
			request.setAttribute("pageInfo",pageInfo);
			request.setAttribute("recordList",recordList);
			request.setAttribute("userList",userList);
			request.setAttribute("lfsysuser",lfsysuser);
			//增加查询日志
			long end_time=System.currentTimeMillis();
			if(pageInfo!=null){
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
				opSucLog(request, "信息审批", opContent, "GET");
			}
			//设置服务器名称
			new ServerInof().setServerName(getServletContext().getServerInfo());	
			request.getRequestDispatcher(empRoot + basePath +"/msg_examineMsg.jsp")
			.forward(request, response);
		}catch (Exception e) {
			EmpExecutionContext.error(e,"信息审批跳转出现异常！");
		}
	}

	
	
	/**
	 * 点击   短信 / 彩信    审批/查看操作
	 * @param request
	 * @param response
	 */
	public void getExamineMsg(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
	   try{
            EmpExecutionContext.logRequestUrl(request,"查看审批任务");
			//任务id
			String mttaskid=request.getParameter("mtId");
			//审核流程ID
			String flowid = request.getParameter("flowid");
			//任务审批记录id
			String frId = request.getParameter("frId");
           //任务审批记录级别
            String rlevelStr = request.getParameter("rlevel");
            if(StringUtils.isBlank(mttaskid) || StringUtils.isBlank(flowid) || StringUtils.isBlank(frId) || StringUtils.isBlank(rlevelStr)){
                EmpExecutionContext.error("查看审批任务获取参数异常。mtId="+mttaskid+",flowid="+flowid+",frId="+frId+",rlevel="+rlevelStr);
                return;
            }
			LfFlowRecord flowRecord = baseBiz.getById(LfFlowRecord.class, frId);
			
			//该审核对象的审核级别
			Integer rlevel = Integer.valueOf(rlevelStr);
			
			//1是短信  2是彩信
			String type = request.getParameter("type");
			LfSysuser lfsysuser = getLoginUser(request);
			
		/*	LfMttask lfmttask = baseBiz.getById(LfMttask.class, mttaskid);*/
			LfMttask lfmttask =  new CommonBiz().getLfMttaskbyTaskId(Long.valueOf(mttaskid));
			//该任务提交人的姓名
			LfSysuser taskSysuser = baseBiz.getById(LfSysuser.class, lfmttask.getUserId());
		   
		   LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		   LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		   conditionMap.put("FId", flowid);
		   conditionMap.put("mtId", String.valueOf(lfmttask.getTaskId()));
		   //流程完成
		   conditionMap.put("isComplete", "1");
		   //-1代表未审核；0代表无需审核；1代表审核通过；2代表审核不通过）
		   conditionMap.put("RState&in", "1,2");
		   conditionMap.put("infoType", type);
		   orderbyMap.put("RLevel", StaticValue.ASC);
		   List<LfFlowRecord> recordList = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderbyMap);
		   //最后一级审批人是否显示有审批记录
		   String isshow = "1";
			StringBuffer sb = new StringBuffer();
			if(recordList != null && recordList.size()>0){
				for(LfFlowRecord temp:recordList){
					sb.append(temp.getUserCode()).append(",");
					if(rlevel != null && rlevel.equals(temp.getRLevel()) && !"2".equals(isshow) && flowRecord.getIsComplete() == 1){
						isshow = "2";
					}
				}
			}
			List<LfSysuser> userList = null;
			LinkedHashMap<Long,String> usernameMap = new LinkedHashMap<Long, String>();
			if(sb != null && sb.toString().length()>0){
				conditionMap.clear();
				conditionMap.put("userId&in", sb.toString());
				conditionMap.put("corpCode", lfsysuser.getCorpCode());
				userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
				if(userList != null && userList.size()>0){
					for(LfSysuser user:userList){
						usernameMap.put(user.getUserId(),user.getName());
					}
				}
			}
		/*	conditionMap.clear();
		    conditionMap.put("FId", flowid);
		    conditionMap.put("mtId", mttaskid);
		    conditionMap.put("userCode", lguserid);
		    conditionMap.put("RLevel", String.valueOf(rlevel));
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, null);
			LfFlowRecord flowRecord = null;
			if(flowRecords != null && flowRecords.size()>0){
				flowRecord = flowRecords.get(0);
			}*/
			//判断是否不是最后一个审核
			String isLastCheck = examineBiz.judgeLastChecker(flowRecord);
			request.setAttribute("isLastCheck", isLastCheck);
			request.setAttribute("flowRecord", flowRecord);
			request.setAttribute("recordList", recordList);
			request.setAttribute("lfmttask", lfmttask);
			request.setAttribute("usernameMap", usernameMap);
			request.setAttribute("rlevel", String.valueOf(flowRecord.getRLevel()));
			request.setAttribute("lfsysuser", lfsysuser);
			
			request.setAttribute("name", taskSysuser.getName());
			request.setAttribute("isshow",isshow);
			request.setAttribute("type",String.valueOf(flowRecord.getInfoType()));
			
			request.getRequestDispatcher(empRoot + basePath +"/msg_examineMsgInfo.jsp")
			.forward(request, response);
	   }catch (Exception e) {
		   EmpExecutionContext.error(e," 信息审批或查看出现异常！");
	   }
	}
	
	
	
	//获取当前系统时间（断库断网断服务器版）
	public void getServerTime(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String serverTime=null;
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date date = new Date();
			serverTime = format.format(date);
		}catch (Exception e) {
			serverTime="error";
			EmpExecutionContext.error(e,"获取当前系统时间出现异常！");
		}finally{
			response.getWriter().print("@"+serverTime);
		}
	}
	
	
	/**
	 * 审批短信操作的方法
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void reviewMsg(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		
		String result="fail";
		try
		{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, "后台请求");
			//自增ID
			String frId = request.getParameter("frId");
			//审批状态
			Integer rstate = -1;//默认未审批
			String rstateStr = request.getParameter("RState");
			if(rstateStr != null && !"".equals(rstateStr.trim()))
			{
				rstate = Integer.valueOf(rstateStr);
			}
			//是否是最后一级审批人
			String isLastCheck = request.getParameter("isLastCheck");
			//描述
			String comments = request.getParameter("comments");
			//1短信  2彩信
			String msgtype = request.getParameter("msgtype");
			Integer timerStatus = 0;
			//定时
			if(request.getParameter("timerStatus") != null)
			{
				timerStatus = Integer.valueOf(request.getParameter("timerStatus"));
			}
			String timerTime =timerStatus==1? (request.getParameter("timerTime")!=null?(request.getParameter("timerTime")+":00"):null):null;
			LfFlowRecord flowRecord = baseBiz.getById(LfFlowRecord.class, frId);
			if(flowRecord != null){
				flowRecord.setRState(rstate);
				flowRecord.setComments(comments);
				//flowRecord.setReviewer(flowRecord.getUserCode());
				flowRecord.setRTime(new Timestamp(System.currentTimeMillis()));
				//flowRecord.setPreRv(flowRecord.getUserCode());
				if(flowRecord.getIsComplete() == 1){
					/*if(baseBiz.updateObj(flowRecord)){
						response.getWriter().print("finish");
					}else{
						response.getWriter().print("unfinish");
					}*/
					response.getWriter().print("unfinish");
					return;
				}
				if("1".equals(msgtype)||"5".equals(msgtype)){//增加状态为5网讯发送判断
					result = examineBiz.examineSMS(flowRecord, timerStatus, timerTime, isLastCheck);
				}else if("2".equals(msgtype)){
					result = examineBiz.examineMMS(flowRecord, timerStatus, timerTime, isLastCheck);
				}
			}else{
				response.getWriter().print("noflow");
				return;
			}
			//审批
			String reultClong=result;
			if(!"000".equals(result) && !"success".endsWith(result)
					&& !"timerSuccess".equals(result) && !"timerFail".equals(result)
					&& !"fail".equals(result)&&!"该任务已执行".equals(result)
					&&!"createSuccess".equals(result) && !"saveSuccess".equals(result) 
					&& !"overtime".equals(result) && !"addChildFail".equals(result) 
					&& !"feefail".equals(result)
					&& !"isrevoke".equals(result) && !"freeze".equals(result) )
			{
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				//根据错误编码得到错误信息
				result = new WGStatus(langName).getInfoMap().get(result);
				if(result==null)
				{
					result=reultClong;
				}
			}
			opSucLog(request, opModule, "短信审批（ID："+frId+"，审批状态："+rstate+"，审批结果："+result+"）。", "OTHER");
			response.getWriter().print(result);
		} catch (Exception e)
		{
			//网关没有开，跑出异常
			if(e.getClass().isAssignableFrom(HttpHostConnectException.class))
//			if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
			{
				result=e.getLocalizedMessage();
			}else
			{
				//错误
				result="error";
			}
			//异常打印
			EmpExecutionContext.error(e,"信息审批出现异常！");
			//将信息返回给页面
			response.getWriter().print(result);
		}
	}
	
	
	
	
	
	/**
	 *   审批彩信 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void reviewMms(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		String result="fail";
		try
		{
			String frId = request.getParameter("frId");
			//审批状态
			Integer rstate = Integer.valueOf(request.getParameter("RState"));
			//是否是最后一级审批人
			String isLastCheck = request.getParameter("isLastCheck");
			//描述
			String comments = request.getParameter("comments");
			Integer timerStatus = 0;
			//定时
			if(request.getParameter("timerStatus") != null)
			{
				timerStatus = Integer.valueOf(request.getParameter("timerStatus"));
			}
			String timerTime =timerStatus==1? (request.getParameter("timerTime")!=null?(request.getParameter("timerTime")+":00"):null):null;
			LfFlowRecord flowRecord = baseBiz.getById(LfFlowRecord.class, frId);
			if(flowRecord != null){
				flowRecord.setRState(rstate);
				flowRecord.setComments(comments);
				//flowRecord.setReviewer(flowRecord.getUserCode());
				flowRecord.setRTime(new Timestamp(System.currentTimeMillis()));
			//	flowRecord.setPreRv(flowRecord.getUserCode());
				if(flowRecord.getIsComplete() == 1){
					boolean flag = baseBiz.updateObj(flowRecord);
					response.getWriter().print("finish");
					return;
				}
				result = examineBiz.examineMMS(flowRecord, timerStatus, timerTime, isLastCheck);
			}else{
				response.getWriter().print("noflow");
				return;
			}
			String reultClong=result;
			if(!"000".equals(result) &&!"success".endsWith(result)
					&& !"timerSuccess".equals(result) && !"timerFail".equals(result)
					&& !"fail".equals(result)&& !"该任务已执行".equals(result)
					&& !"createSuccess".equals(result) && !"saveSuccess".equals(result)
					&& !"addChildFail".equals(result) && !"feefail".equals(result) )
			{
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				//根据错误编码得到错误信息
				result = new WGStatus(langName).getInfoMap().get(result);
				if(result==null)
				{
					result=reultClong;
				}
			}
			opSucLog(request, opModule, "彩信审批（ID："+frId+"，审批状态："+rstate+"，审批结果："+result+"）。", "OTHER");
		}catch (Exception e) {
			EmpExecutionContext.error(e,"彩信审批出现异常！");
		} finally {
			response.getWriter().print(result);
		}
		
		
	}

	
	
	/**
	 * 判断文件是否存在的方法
	 * @param request
	 * @param response
	 */
	public void checkFiles(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//文件路径
			String fileUrl = request.getParameter("url");
			//检查
			boolean r = checkFile(fileUrl);
			
			//检查结果返回给页面
			response.getWriter().print(r);
		}
		catch (Exception e)
		{
			//打印异常
			EmpExecutionContext.error(e,"判断文件是否存在出现异常！");
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
			result = new TxtFileUtil().checkFile(fileUrl);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e,"判断检查文件是否存在出现异常！");
		}
		//返回结果
		return result;
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
			if(obj == null) {
                return;
            }
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
