package com.montnets.emp.shorturl.task.servlet;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.IGenericDAO;
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
import com.montnets.emp.shorturl.task.biz.ReplyParams;
import com.montnets.emp.shorturl.task.biz.SmstaskBiz;
import com.montnets.emp.shorturl.task.biz.SmstaskExcelTool;
import com.montnets.emp.shorturl.task.vo.LfMttaskVo2;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;
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
public class Surl_surlSendedBoxSvt extends BaseServlet {

	//String titlePath="";
	private final SmstaskBiz smstaskBiz=new SmstaskBiz();
	final String empRoot="shorturl";
	final String basePath="/smstask";
	final SmsBiz smsBiz = new SmsBiz();
	final SuperOpLog spLog = new SuperOpLog();
	final BaseBiz baseBiz = new BaseBiz();
	private final String path=new TxtFileUtil().getWebRoot();
	//模板路径
	protected final String  excelPath = path + "shorturl/smstask/file/";

	//常用文件读写工具类
	final TxtFileUtil txtfileutil = new TxtFileUtil();
	
	
	
	/**
	 * 群发历史与群发任务查询的方法
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		long startTime = System.currentTimeMillis();
		//短信biz
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		String requestPath = request.getRequestURI();
		String path = requestPath.substring(requestPath.lastIndexOf("/")).replaceAll(".htm", ".jsp");
		String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord
        String actionPath=requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());
		PageInfo pageInfo = new PageInfo();
		//当前登录用户的企业编码
		String corpCode = "";
		String  userId = "";
		try
		{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			//导出方法的查询条件清空
			request.getSession(false).setAttribute("r_sendTime", null);
			request.getSession(false).setAttribute("r_recvTime", null);
			request.getSession(false).setAttribute("r_userIds", null);
			request.getSession(false).setAttribute("r_depIds", null);
			request.getSession(false).setAttribute("r_sendTitle", null);
			
			//登录操作员信息
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录用户的企业编码
			corpCode =loginSysuser.getCorpCode();
			userId =String.valueOf(loginSysuser.getUserId());
			if(corpCode==null||"".equals(corpCode.trim())||"undefined".equals(corpCode.trim())||userId==null||"".equals(userId.trim())||"undefined".equals(userId.trim())){
				EmpExecutionContext.error("群发任务或群发历史查询获取登录操作员ID和登录操作员企业编码异常！lguserid="+userId+",lgcorpcode="+corpCode+"。改成从Session获取。");
				//LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				userId=String.valueOf(loginSysuser.getUserId());
				corpCode=loginSysuser.getCorpCode();
			}
			
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
            String sendstate=request.getParameter("sendstate");
            //任务批次查询条件
            String taskID=request.getParameter("taskID");
            //信息内容
            String msg=request.getParameter("msg");
            //是否包含子机构
            String isContainsSun=request.getParameter("isContainsSun");
            String depNam = request.getParameter("depNam");
            String userName = request.getParameter("userName");
            boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
            HttpSession session = request.getSession(false);
            if(isBack){
            	if(session.getAttribute("smt_smsSended_pageinfo")!=null){pageInfo = (PageInfo) session.getAttribute("smt_smsSended_pageinfo");};
            	spUser = defaultValue(session.getAttribute("smt_smsSended_spUser"));
    			depid = defaultValue(session.getAttribute("smt_smsSended_depid"));
    			userid = defaultValue(session.getAttribute("smt_smsSended_userid"));
    			startSubmitTime = defaultValue(session.getAttribute("smt_smsSended_startSubmitTime"));
    			endSubmitTime = defaultValue(session.getAttribute("smt_smsSended_endSubmitTime"));
                title = defaultValue(session.getAttribute("smt_smsSended_title"));
                taskType= defaultValue(session.getAttribute("smt_smsSended_taskType"));
                sendstate= defaultValue(session.getAttribute("smt_smsSended_sendstate"));
                taskID= defaultValue(session.getAttribute("smt_smsSended_taskID"));
                msg=defaultValue(session.getAttribute("smt_smsSended_msg"));
                isContainsSun= defaultValue(session.getAttribute("smt_smsSended_isContainsSun"));
                depNam= defaultValue(session.getAttribute("smt_smsSended_depNam"));
                userName= defaultValue(session.getAttribute("smt_smsSended_userName"));
                request.setAttribute("spUser", spUser);
                request.setAttribute("userid", userid);
                request.setAttribute("depid", depid);
                request.setAttribute("sendtime", startSubmitTime);
                request.setAttribute("recvtime", endSubmitTime);
                request.setAttribute("taskType", taskType);
                request.setAttribute("sendstate", sendstate);
                request.setAttribute("taskID", taskID);
                request.setAttribute("msg", msg);
                request.setAttribute("depNam", depNam);
                request.setAttribute("userName", userName);
            }
            session.setAttribute("smt_smsSended_pageinfo", pageInfo);
            session.setAttribute("smt_smsSended_spUser", spUser);
            session.setAttribute("smt_smsSended_depid", depid);
            session.setAttribute("smt_smsSended_userid", userid);
            session.setAttribute("smt_smsSended_startSubmitTime", startSubmitTime);
            session.setAttribute("smt_smsSended_endSubmitTime", endSubmitTime);
            session.setAttribute("smt_smsSended_title", title);
            session.setAttribute("smt_smsSended_taskType", taskType);
            session.setAttribute("smt_smsSended_sendstate", sendstate);
            session.setAttribute("smt_smsSended_taskID", taskID);
            session.setAttribute("smt_smsSended_msg", msg);
            session.setAttribute("smt_smsSended_isContainsSun", isContainsSun);
            session.setAttribute("smt_smsSended_depNam", depNam);
            session.setAttribute("smt_smsSended_userName", userName);
            if(taskID!=null&&!"".equals(taskID.trim())){
            	try{
            		mtVo.setTaskId(Long.parseLong(taskID.trim()));
            	}catch (Exception e) {
            		EmpExecutionContext.error("任务ID转换异常，taskID:" + taskID);
				}
            }
            //设置短信内容查询条件
            if(msg!=null&&!"".equals(msg.trim()))
            {
            	mtVo.setMsg(msg.trim());
            }
            //获取过滤条件
            userid = (userid != null && userid.trim().length()>0 && !userid.equals("请选择")&&userid.indexOf(",")!=-1)?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
            

            if(isContainsSun!=null&&!"".equals(isContainsSun)){
            	mtVo.setIsContainsSun(isContainsSun);
            }
            
           //查询移动财务短信及群发短信
            //mtVo.setMsTypes("1,5");
            //群发短信、移动财务、员工生日祝福、客户生日祝福
            if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)||"smsTaskRecord".equals(titlePath)){
            	mtVo.setMsTypes("1,5,9,10");
            //移动财务
            }else if("sendTask".equals(titlePath)||"sendTaskHis".equals(titlePath)){
            	mtVo.setMsTypes("5");
            //员工生日祝福
            }else if("empSendTask".equals(titlePath)){
            	mtVo.setMsTypes("9");
            //客户生日祝福
            }else if("surlSendedBox".equals(titlePath)){
            	mtVo.setMsTypes("31");
            //客户生日祝福
            }else{
            	mtVo.setMsTypes("10");
            }
            
            mtVo.setTaskState(taskState);
            if(taskType!=null&&!"".equals(taskType)){
            	mtVo.setTaskType(Integer.parseInt(taskType));
            }
            boolean isFirstEnter = !isBack&&pageSet(pageInfo,request);
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
				if(titlePath.equals("smsTaskRecord")||titlePath.equals("sendTaskHis")||titlePath.equals("empSendTask")||titlePath.equals("cliSendTask"))
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
				if(titlePath.equals("smsTaskRecord")||titlePath.equals("sendTaskHis")||titlePath.equals("empSendTask")||titlePath.equals("cliSendTask")){
//					  String sendstate=request.getParameter("sendstate");
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
                    String flowId = request.getParameter("flowId");
                    //审批流程跳转 查询该审批流下待审批的任务
                    if(flowId != null && !"".equals(flowId)){
                        mtVo.setFlowID(Long.parseLong(flowId));
                    }
					mtVoList = smstaskBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
				}
				
				//获取加密类
				ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
				//加密mtId
				encryptOrDecrypt.batchEncrypt(mtVoList, "MtId", "MtIdCipher");
				
				
				if(isContainsSun==null||"".equals(isContainsSun)){
					request.setAttribute("isContainsSun", "0");
				}else{
					request.setAttribute("isContainsSun", "1");
				}
			}
			
			//操作模块
			String opModule = "群发历史查询";
			//如果从短信助手或短信客服请求群发任务查看，则统一转向smt_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)||"sendTask".equals(titlePath)||"surlSendedBox".equals(titlePath)){
				path = "/smt_smsSendedBox.jsp";
				opModule = "群发任务查看";
			}else{
				path = "/smt_smsTaskRecord.jsp";
				opModule = "群发历史查询";
			}

			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			
			List<LfMttaskVo2> rs = new ArrayList<LfMttaskVo2>();
			if(mtVoList != null && !mtVoList.isEmpty()){
				for (LfMttaskVo l : mtVoList) {
					LfMttaskVo2 lfMttaskVo2 = new LfMttaskVo2();
					lfMttaskVo2.setMtId(l.getMtId());
					BeanUtils.copyProperties(lfMttaskVo2,l);
					 IGenericDAO dao = new DataAccessDriver().getGenericDAO();
					 String sql = "SELECT NETURL,DOMAIN_URL FROM LF_URLTASK WHERE TASKID = "+l.getTaskId();
					   List<DynaBean>  returnList = dao.findDynaBeanBySql(sql);
					   if(returnList!= null && !returnList.isEmpty()){
						   lfMttaskVo2.setNetUrl(returnList.get(0).get("neturl")+"");
						   lfMttaskVo2.setDomainUrl(""+returnList.get(0).get("domain_url"));
					   }
					rs.add(lfMttaskVo2);
				}
			}
			request.setAttribute("mtList", rs);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", userId);//当前登录用户id
			//当前登录操作员
			LfSysuser currentSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//加载机构树
			String departmentTree = smstaskBiz.getDepartmentJosnData(Long.parseLong(userId),currentSysuser);
			//如果用户请求是"群发历史查询"
			
			
			request.setAttribute("departmentTree", departmentTree);
			request.setAttribute("titlePath", titlePath);
			request.setAttribute("actionPath", actionPath);
			
			if("smsTaskRecord".equals(titlePath)||"sendTaskHis".equals(titlePath)||"empSendTask".equals(titlePath)||"cliSendTask".equals(titlePath)){
				//对查询统计 群发历史查询中 增加 滞留条数列
				StringBuffer taskids = new StringBuffer();
				if(mtVoList!=null&&mtVoList.size()>0){
					for(LfMttaskVo mttaskVo:mtVoList){
						taskids.append(","+mttaskVo.getTaskId());
					}
				}
				Map<Long, Long> taskRemains = new HashMap<Long, Long>();
				if(taskids.length()>0){
					taskids.delete(0, 1);
					taskRemains = smstaskBiz.getTaskRemains(taskids.toString());
				}
				request.setAttribute("taskRemains", taskRemains);
			}
			if(!isFirstEnter)
			{
				//格式化时间
				SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
				//操作日志信息
				String opContent = "查询："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				EmpExecutionContext.info(opModule, corpCode, userId, lfSysuser.getUserName(), opContent, "GET");
			}
			//页面跳转
			request.getRequestDispatcher(empRoot + basePath +path)
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error("群发历史与群发任务查询方法请求URL:" + request.getRequestURI()+ "，请求参数，corpCode：" + corpCode + "，userId :" + userId);
			EmpExecutionContext.error(e,"群发历史与群发任务查询的方法异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);//回到页面第一次加载时的状态
			request.setAttribute("titlePath", titlePath);
			request.setAttribute("actionPath", actionPath);
			request.setAttribute("pageInfo", pageInfo);
			//如果从短信助手或短信客服请求群发任务查看，则统一转向smt_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)||"sendTask".equals(titlePath)){
				path = "/smt_smsSendedBox.jsp";
			}else{
				path = "/smt_smsTaskRecord.jsp";
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
			String spr = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_232", request);
			String spzt = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_233", request);
			String spyj = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_234", request);
			String sptg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_235", request);
			String wsp = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_236", request);
			String spbtg = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_150", request);
			String spjb = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_237", request);
			String wjl = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_87", request);

			StringBuffer buffer =null;
			buffer = new StringBuffer("<table border='1' width='100%' style='text-align:center'><tr  class='div_bd'><td class='div_bd'>"+spjb+"</td>" +
					"<td class='div_bd'>"+spr+"</td><td class='div_bd'>"+spzt+"</td>" +
					"<td class='div_bd'>"+spyj+"</td></tr><tbody>");
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
                            buffer.append("<td  class='div_bd'>").append(sptg).append("</td>");
                        }
						if(record.getRState()==2) {
                            buffer.append("<td  class='div_bd'>").append(spbtg).append("</td>");
                        }
						if(record.getRState()==-1) {
                            buffer.append("<td  class='div_bd'>").append(wsp).append("</td>");
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
					buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>"+wjl+"</td></tr>");
				}
			}else
			{
				buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>"+wjl+"</td></tr>");
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
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			Long depId = null;
			Long userid=null;
			//部门iD
			String depStr = request.getParameter("depId");
			//操作员账号
//			String userStr = request.getParameter("lguserid");
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
//			if(userStr != null && !"".equals(userStr.trim())&&!"undefined".equals(userStr.trim())){
//				userid = Long.parseLong(userStr.trim());
//			}
//			else{
//				EmpExecutionContext.error("群发任务或群发历史查询获取lguserid参数异常！lguserid="+userStr+",depStr="+depStr+"。改成从Session获取。");
//				//没有取到登录操作员USERID，就从Session中获取。
//				userid=((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserId();
//			}

			//登录操作员ID
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录操作员ID
			userid=loginSysuser.getUserId();
			String departmentTree = smstaskBiz.getDepartmentJosnData2(depId, userid,loginSysuser);
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
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord

			//获取当前登录操作员
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			
			/*if(titlePath.equals("smsTaskRecord"))*/
			deptUserTree = getDeptUserJosnData(titlePath,Long.parseLong(userid),loginSysuser,request);
	
			response.getWriter().print(deptUserTree);
			EmpExecutionContext.info(deptUserTree);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"群发历史或群发任务查询条件中操作员树的加载方法异常！");
		}
	}

	/**
	 * 操作员树的加载方法(重载方法)
	 * @param titlePath
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long userid,LfSysuser loginSysuser,HttpServletRequest request) throws Exception{
		StringBuffer tree = null;
		//根据userid获取当前用户信息
		LfSysuser currUser = loginSysuser;
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
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long userid,HttpServletRequest request) throws Exception{
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
	public  String getDeptUserJosnData(String titlePath,Long depId,Long userid,HttpServletRequest request) throws Exception{
		StringBuffer tree = null;
		//已注销
		String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
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
							tree.append(",name:'").append(lfSysuser.getName()).append("("+yzx+")'");
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
		return tree==null?"":tree.toString();
	}


	/**
	 * 操作员树的加载方法(重载方法)
	 * @param titlePath
	 * @param depId
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public  String getDeptUserJosnData(String titlePath,Long depId,Long userid,LfSysuser loginSysuser,HttpServletRequest request) throws Exception{
		StringBuffer tree = null;
		//已注销
		String yzx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_139", request);
		//根据userid获取当前操作员信息
		LfSysuser currUser = loginSysuser;
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
							tree.append(",name:'").append(lfSysuser.getName()).append("("+yzx+")'");
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
		return tree==null?"":tree.toString();
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
//		 	try{
//		 	}catch (Exception e) {
//		 		EmpExecutionContext.error(e);
//		 	}
		 	//回复详情(回复详情查看三张表且七天内的方法)
		 	//reply = mtBiz.getSmsSendMotaskReply(mt,pageInfo);
		 	////回复详情查询(跟据taskid去lfmotask查询taskid为mtid的上行记录)
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
			EmpExecutionContext.error(e,"群发任务回复详情查询异常！");
			//异常错误
			response.getWriter().write("error");
		}
	}
	/**
	 * 重写回复查询
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getReplyDetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		try
		{
			long startTime = System.currentTimeMillis();
			//企业编码
			String corpCode =request.getParameter("lgcorpcode");
			//名称
			String replyName =request.getParameter("replyName");
			//手机号
			String replyMoblie =request.getParameter("replyMoblie");
			//内容
			String replyContent =request.getParameter("replyContent");

			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			JSONObject json = new JSONObject();
			response.setContentType("text/html;charset=UTF-8");
			//短信任务id
			Long mtId = Long.valueOf(request.getParameter("mtId"));
		 	Integer pageIndex = Integer.valueOf(request.getParameter("pageIndex"));
	    	CommonVariables  CV = new CommonVariables();
	    	Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
			int ishidephome=0;//是否隐藏手机号
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
				ishidephome=1;
			}
	    	//根据短信id，获取短信信息
	    	LfMttask mt=baseBiz.getById(LfMttask.class, mtId);

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
        		conditionMap.put("corpCode", corpCode);
				if(replyMoblie != null && !"".equals(replyMoblie))
				{
					replyMoblie = replyMoblie.trim();
					conditionMap.put("phone", replyMoblie);
				}
				if(replyContent != null && !"".equals(replyContent))
				{
					replyContent = replyContent.trim();
					conditionMap.put("msgContent", replyContent);
				}
            	if(replyName != null && !"".equals(replyName))
            	{
            		replyName = replyName.trim();
            		conditionMap.put("replyName", replyName);
            	}
				//回复信息
			 	//List<LfMotask> reply=baseBiz.getByCondition(LfMotask.class, null, conditionMap, null, pageInfo);
				List<DynaBean> replyDetailList = smstaskBiz.getReplyDetailList(conditionMap, pageInfo);
				DynaBean replyDetai = null;
			 	if(replyDetailList != null && replyDetailList.size() > 0){
			 		JSONArray members = new JSONArray();
			 		//号码
			 		String mobile = "";
			 		//回复时间
			 		String time = "";
			 		//回复内容
			 		String content = "";
			 		//姓名
			 		String name = "";
			 		for(int i=0;i<replyDetailList.size();i++){
			 			replyDetai =  replyDetailList.get(i);
			            JSONObject member = new JSONObject();
				 		mobile = "";
				 		time = "";
				 		content = "";
				 		//姓名
				 		name = "";

			            //号码
			            mobile = replyDetai.get("phone")!=null?replyDetai.get("phone").toString():"";
			            //设置号码是否可见
			            if(mobile!=null && !"".equals(mobile)){
			            	if(ishidephome!=1){
			            		mobile = CV.replacePhoneNumber(mobile);
			            	}
			            }
			            else{
			            	mobile = "-";
			            }
			            member.put("moblie",mobile);


			 			//姓名
			            name = replyDetai.get("name")!=null&&!"".equals(replyDetai.get("name").toString())?replyDetai.get("name").toString():"-";
			            member.put("name",name);

			            //回复时间
			            time = replyDetai.get("delivertime")!=null?replyDetai.get("delivertime").toString():"-";
			            member.put("time", time);

			            //回复内容
			            content = replyDetai.get("msgcontent")!=null?replyDetai.get("msgcontent").toString():"-";
			            member.put("content", content);
			            members.add(member);
			        }
			        json.put("jobs", members);
			        json.put("index", pageInfo.getPageIndex());
			        json.put("count", pageInfo.getTotalRec());
			        json.put("pageSize", pageInfo.getPageSize());
			        json.put("preNoticeId", String.valueOf(mtId));
			        json.put("replyName", replyName);
			        json.put("replyMoblie", replyMoblie);
			        json.put("replyContent", replyContent);
			 	}
			 	else{
					//没有数据
			 		json.put("preNoticeId", String.valueOf(mtId));
			        json.put("replyName", replyName);
			        json.put("replyMoblie", replyMoblie);
			        json.put("replyContent", replyContent);
			        json.put("jobs", "");
			 	}
			 	request.getSession(false).setAttribute("r_replyName", replyName);
			 	request.getSession(false).setAttribute("r_replyMoblie", replyMoblie);
			 	request.getSession(false).setAttribute("r_replyContent", replyContent);
			 	//将json数据返回页面
			    response.getWriter().write(json.toString());
			}
			else
			{
				//没有数据
				response.getWriter().write("noList");
			}
			//格式化时间
			SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
			//操作日志信息
			String opContent = "回复详情查询："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("群发历史查询", lfSysuser.getCorpCode(), String.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(), opContent, "GET");
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

		//格式化时间
		SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
		String requestPath = request.getRequestURI();
		String actionPath=requestPath.substring(requestPath.lastIndexOf("/")+1, requestPath.length());

		String fszsw = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_238", request);
		String fscgs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_239", request);
		String tjsbs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_240", request);
		String jssbs = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_241", request);
		String tiao = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_242", request);
		String dhz = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_243", request);
		String xjmx = MessageUtils.extractMessage("dxzs", "dxzs_xtnrqf_title_244", request);
		
		long startTime = System.currentTimeMillis();
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
            String encryptmtid  = request.getParameter("mtid");
            //获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密mtId
			String mtid=encryptOrDecrypt.decrypt(encryptmtid);
            
            //获取页面传过来的查询条件
            String spisuncm = request.getParameter("spisuncm");//运营商
            String phone = request.getParameter("phone");//手机号
            String taskId = request.getParameter("taskId");//手机号
            //String tableName="MT_TASK";
            //将下行实时表改成GW_MT_TASK_BAK
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
    			if(type != null && !"0".equals(type))
        		{
    				//是否需要新的分页信息 1需要  2不需要
    				String isclicksearch=request.getParameter("isclicksearch");
//    			    //需要新的分页信息
//    			    if("1".equals(isclicksearch))
//    			    {
    				String fieldSql=new StringBuffer("SELECT UNICOM,PHONE,MESSAGE,ERRORCODE,PKNUMBER,PKTOTAL,TASKID ").toString();
    				String tablename = new StringBuffer()
    				.append(" (")
    				.append(fieldSql).append(" FROM ").append("gw_mt_task_bak").append(StaticValue.getWITHNOLOCK())
    				.append(" union all ")
    				.append(fieldSql).append(" FROM ").append("mt_task").append(StaticValue.getWITHNOLOCK())
    				.append(")  ").toString();
    			    	 MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,tablename);
    			    	 request.getSession(false).setAttribute("taskRecordTableName", tablename);
//    			    	//在备份表GW_MT_TASK_BAK没有查询到数据，改成查询实时表MT_TASK
//    			    	if(MttaskvoList==null||MttaskvoList.size()==0)
//    			    	{
//    			    		 MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,"MT_TASK");
//    			    		 EmpExecutionContext.info("群发历史查询详情，备份表没有数据，查询实时表MT_TASK");
//    			    		 request.getSession(false).setAttribute("taskRecordTableName", "MT_TASK");
//    			    	}
    			    	 //不需要新的分页信息
    			    	isclicksearch="2";
//    			    }else{
//    			    	 tableName=(String)request.getSession(false).getAttribute("taskRecordTableName");
//    			    	 if(tableName!=null&&!"".equals(tableName)){
//    			    		 MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,tableName);
//    			    	 }else{
//    			    		 MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,"GW_MT_TASK_BAK");
//        			    	 request.getSession(false).setAttribute("taskRecordTableName", "GW_MT_TASK_BAK");
//        			    	//在备份表GW_MT_TASK_BAK没有查询到数据，改成查询实时表MT_TASK
//        			    	if(MttaskvoList==null||MttaskvoList.size()==0)
//        			    	{
//        			    		 MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,"MT_TASK");
//        			    		 EmpExecutionContext.info("群发历史查询详情，备份表没有数据，查询实时表MT_TASK");
//        			    		 request.getSession(false).setAttribute("taskRecordTableName", "MT_TASK");
//        			    	}
//    			    	 }
//    			    	
//    			    }
    			    //设置是否需要新的分页信息
    			    request.setAttribute("isclicksearch", isclicksearch);
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
    			String date = year;
				if(month<10)
				{
					//日期
					date+="0"+month;
				}
				else
				{
					//日期
					date+=month;
				}

				//表名
				tableName="MTTASK"+date;
				
				//根据时间确定查询数据的数据库,true:查历史库；false:查实时库
				boolean isBackDb = false;
				//读取配置文件里是否启用备用服务器
				String use_his_server = SystemGlobals.getValue("montnets.emp.use_history_server");
				if("1".equals(use_his_server))
				{
					//未开启实时数据保留实时库时间
					if(StaticValue.getReadDataSaveTime() == -1)
					{
						if(Integer.parseInt(date) < Integer.parseInt(StaticValue.getUseHistoryDBTime()))
						{
							//查历史库
							isBackDb = true;
						}
					}
					else
					{
						SimpleDateFormat sdf_yyyyMM = new SimpleDateFormat("yyyyMM");
						int curDate = Integer.parseInt(sdf_yyyyMM.format(System.currentTimeMillis()));
						//当前日期减去数据日期大于数据保留实时库时间，查询历史数据库
						if(curDate - Integer.parseInt(date) > StaticValue.getReadDataSaveTime())
						{
							isBackDb = true;
						}
					}
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
				curTime.add(curTime.DATE, -4);
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
						//实时库分页对象
						Object realDbpageInfoObj = request.getSession(false).getAttribute("realDbpageInfo");
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
						//查询发送详情记录
					    MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, pageInfos,tableName, isBackDb, realDbpageInfo);
					    //设置到session中
					    request.getSession(false).setAttribute("realDbpageInfo", realDbpageInfo);
						
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
			
			
			String sendinfo =fszsw+icount+fscgs+suc+tjsbs+fail_count+jssbs+r_count+tiao;
			if(Lfmttask.getIcount2()==null)
			{
				sendinfo=dhz;
				//sendinfo ="发送总数为-条，其中发送成功数为-条，提交失败数为-条，接收失败数为-条。";
			}
			String message =(Lfmttask.getBmtType()==3?xjmx:Lfmttask.getMsg());
			//短信list
			request.setAttribute("mtList",MttaskvoList);
			//分页信息
			request.setAttribute("pageInfo", pageInfos);	
			request.setAttribute("title",Lfmttask.getTitle());
			request.setAttribute("message", message);
			request.setAttribute("sendtime", Lfmttask.getTimerTime()==null?"":sdf.format(Lfmttask.getTimerTime()));
			request.setAttribute("sendinfo", sendinfo);
			//短信任务id
			request.setAttribute("mtid", encryptmtid);
			request.setAttribute("type", type);	
			//页面查询条件
			request.setAttribute("phone", phone);
			request.setAttribute("taskId", Lfmttask.getTaskId().toString());
			request.setAttribute("spisuncm", spisuncm);
			
			request.setAttribute("actionPath", actionPath);

			//操作日志信息
			String opContent = "详情查询："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数："+pageInfos.getTotalRec() ;
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("群发历史查询", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
			//页面跳转
			request.getRequestDispatcher(empRoot + basePath +"/smt_smsTaskAllSendRecord.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"群发历史查询中的发送详情查询异常！");
			request.getSession(false).setAttribute("error", e);			
			request.setAttribute("findresult", "-1");
			//第一次查询
			request.setAttribute("isFirstEnter", true);
			//分页信息
			request.setAttribute("pageInfo", pageInfos);
			
			request.setAttribute("actionPath", actionPath);
			try {
				//页面跳转
				request.getRequestDispatcher(empRoot + basePath +"/smt_smsTaskAllSendRecord.jsp")
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
	public void smsReportAllExcel(HttpServletRequest request,HttpServletResponse response)throws Exception{
		String requestPath = request.getRequestURI();
		String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
		String exportType="1";
		 if("smsTaskRecord".equals(titlePath)){
         	exportType="1";
         //移动财务
	        }else if("sendTaskHis".equals(titlePath)){
	        	exportType="2";
	        //员工生日祝福
	        }else if("empSendTask".equals(titlePath)){
	        	exportType="3";
	        //客户生日祝福
	        }else{
	        	exportType="4";
	        }
		long startTime = System.currentTimeMillis();
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
			
			 //获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密mtId
			mtid=encryptOrDecrypt.decrypt(mtid);
			
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
            try{
            	Long.parseLong(mtid);
            }catch (Exception e) {
            	//群发历史详情导出参数异常时将传入的参数都写日志
            	EmpExecutionContext.error(e,"群发历史详情导出参数异常，mtid:"+mtid+",type:"
            			+type+",spisuncm:"+spisuncm+",phone:"+phone+",IsexportAll:"+IsexportAll);
			}
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
            //String tableName="MT_TASK";
            //将下行实时表改成GW_MT_TASK_BAK
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
        			String fieldSql=new StringBuffer("SELECT UNICOM,PHONE,MESSAGE,ERRORCODE,PKNUMBER,PKTOTAL,TASKID ").toString();
    				String tablename = new StringBuffer()
    				.append(" (")
    				.append(fieldSql).append(" FROM ").append("gw_mt_task_bak").append(StaticValue.getWITHNOLOCK())
    				.append(" union all ")
    				.append(fieldSql).append(" FROM ").append("mt_task").append(StaticValue.getWITHNOLOCK())
    				.append(")  ").toString();
        			MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, null,tablename);	
//        			//备份表没有查询到，就查询实时表MT_TASK表
//        			if(MttaskvoList==null||MttaskvoList.size()==0)
//        			{
//        				MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, null,"MT_TASK");	
//        			}
        		}
        		else
        		{
        			//计算获得历史表的表名（发送时间的月份）
        			int month=subTime.getMonth()+1;
        			String year=df.format(subTime.getTime()).substring(0,4);
        			String date = year;
    				if(month<10)
    				{
    					//日期
    					date+="0"+month;
    				}
    				else
    				{
    					//日期
    					date+=month;
    				}

    				//表名
    				tableName="MTTASK"+date;
    				
    				//根据时间确定查询数据的数据库,true:查历史库；false:查实时库
    				boolean isbackDb = false;
    				//未开启实时数据保留实时库时间
    				if(StaticValue.getReadDataSaveTime() == -1)
    				{
    					if(Integer.parseInt(date) < Integer.parseInt(StaticValue.getUseHistoryDBTime()))
    					{
    						//查历史库
    						isbackDb = true;
    					}
    				}
    				else
    				{
    					SimpleDateFormat sdf_yyyyMM = new SimpleDateFormat("yyyyMM");
    					int curDate = Integer.parseInt(sdf_yyyyMM.format(System.currentTimeMillis()));
    					//当前日期减去数据日期大于数据保留实时库时间，查询历史数据库
    					if(curDate - Integer.parseInt(date) > StaticValue.getReadDataSaveTime())
    					{
    						isbackDb = true;
    					}
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
    				curTime.add(curTime.DATE, -4);
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
    					MttaskvoList = smstaskBiz.getMtTask(conditionMap,orderMap, null,tableName, isbackDb, null);	
    				}
    				
        		}
        	}
			//返回状态
			String result = "false";
			//操作日志信息
			String opContent = "";
    		//查询出来的记录不为空时，去创建需要导出的excel
			if (MttaskvoList != null && MttaskvoList.size()>0 ) 
			{
        
				SmstaskExcelTool et=new SmstaskExcelTool(excelPath);
				Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
				int ishidephome=0;
				if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
					ishidephome=1;
				}
				Map<String, String> resultMap = et.createSmsMtReportExcel(MttaskvoList,IsexportAll,ishidephome,exportType,request);
				HttpSession session = request.getSession(false);
	            session.setAttribute("smstaskallrecord_export",resultMap);
				//操作日志信息
				opContent = "详情导出成功。";
				result = "true";
//				String fileName=(String)resultMap.get("FILE_NAME");
//		        String filePath=(String)resultMap.get("FILE_PATH");
//		        	
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
//		        //用于判断是否下载加载完成了
//		        request.getSession(false).setAttribute("checkOver"+userId, "true");	       
			}
			else
			{
//				response.sendRedirect(request.getContextPath()+"/smt_smsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
				//操作日志信息
				opContent = "详情导出失败。";
			}

			//格式化时间
			SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
			opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + (MttaskvoList==null?0:MttaskvoList.size());
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("群发任务查看", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
			response.getWriter().print(result);
		} catch (Exception e) {		
				EmpExecutionContext.error(e,"群发历史查询中的发送详情的excel导出异常！");
			   //异常打印
//			   response.sendRedirect(request.getContextPath()+"/smt_smsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
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
			//登录操作员ID
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String departmentTree = smstaskBiz.getDepartmentJosnData2(depId,null,loginSysuser);
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
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			Long depId = null;
			
			String depStr = request.getParameter("depId");
			//String userid = request.getParameter("lguserid");
			if(depStr != null && !"".equals(depStr.trim())){
				depId = Long.parseLong(depStr);
			}
			//如果传入登录操作员ID为空或者undefined，则从Session获取。
//			if(userid==null||"".equals(userid.trim())||"undefined".equals(userid.trim())){
//				EmpExecutionContext.error("群发历史查询获取lguserid参数异常！lguserid="+userid+",depStr="+depStr+"。改成从Session获取。");
//				LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
//				userid=String.valueOf(loginSysuser.getUserId());
//			}
			String requestPath = request.getRequestURI();
			String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));//   比如/p_base/smt_smsTaskRecord.jsp->smsTaskRecord
			
			//当前登录操作员ID
			LfSysuser currentSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录操作员ID
			Long userid=currentSysuser.getUserId();
			//调用公用创建树的方法
			String departmentTree = getDeptUserJosnData(titlePath,depId,userid,currentSysuser,request);
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
            //查询移动财务短信及群发短信
            mtVo.setMsTypes("1,5");
			
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

			
			//如果从短信助手或短信客服请求群发任务查看，则统一转向smt_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/smt_smsSendedBox.jsp";
			}

			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", false);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			//当前登录操作员
			LfSysuser currentSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			String departmentTree = smstaskBiz.getDepartmentJosnData(Long.parseLong(userId),currentSysuser);
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
			//如果从短信助手或短信客服请求群发任务查看，则统一转向smt_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
				path = "/smt_smsSendedBox.jsp";
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
		String requestPath = request.getRequestURI();
		String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
		String exportType="1";
		
		long startTime = System.currentTimeMillis();
		//下行短信list
		List<LfMttaskVo> mtVoList = null;
		//路径
		//String context=request.getSession(false).getServletContext().getRealPath("/fileUpload/excelDownload");
		LfMttaskVo mtVo = new LfMttaskVo();
		//企业编码
		String corpCode = null;
		//用户id
		String userId = null;
        try
        {
        	//登录操作员信息
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录用户的企业编码
			corpCode =loginSysuser.getCorpCode();
			userId =String.valueOf(loginSysuser.getUserId());
			
        	String title = (String) request.getSession(false).getAttribute("r_sendTitle");
        	String spUser = request.getParameter("spuser");
			String depids = (String)request.getSession(false).getAttribute("r_depIds");
			String userids = (String)request.getSession(false).getAttribute("r_userIds");

			String startSubmitTime = (String)request.getSession(false).getAttribute("r_sendTime");
			String endSubmitTime = (String)request.getSession(false).getAttribute("r_recvTime");
			
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
			 

			 //导出excel 短信内容
			 String msg=request.getParameter("msg");
			 if(msg!=null&&!"".equals(msg.trim()))
			 {
				 mtVo.setMsg(msg.trim());
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
            //mtVo.setMsTypes("1,5");
            //群发短信、移动财务
            if("smsTaskRecord".equals(titlePath)){
            	exportType="1";
            	mtVo.setMsTypes("1,5,9,10");
            //移动财务
	        }else if("sendTaskHis".equals(titlePath)){
	        	exportType="2";
	        	mtVo.setMsTypes("5");
	        //员工生日祝福
	        }else if("empSendTask".equals(titlePath)){
	        	exportType="3";
	        	mtVo.setMsTypes("9");
	        //客户生日祝福
	        }else{
	        	exportType="4";
	        	mtVo.setMsTypes("10");
	        }
            
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
			//返回状态
			String result = "false";
			//操作日志信息
			String opContent = "";
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
				
				SmstaskExcelTool et = new SmstaskExcelTool(excelPath);
				Map<String, String> resultMap = et.createMtReportExcel(mtVoList,exportType,request);
	            HttpSession session = request.getSession(false);
	            session.setAttribute("smstaskrecord_export",resultMap);
				//操作日志信息
				opContent = "导出成功。";
				result = "true";
//				String fileName=(String)resultMap.get("FILE_NAME");
//		        String filePath=(String)resultMap.get("FILE_PATH");
//		        DownloadFile dfs=new DownloadFile();
//		        dfs.downFile(request, response, filePath, fileName);
			}
			else
			{
//				this.find(request, response);
				 //response.sendRedirect(request.getContextPath()+"/smt_smsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
				//操作日志信息
				opContent = "导出失败。";
			}

			//格式化时间
			SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
			opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + (mtVoList==null?0:mtVoList.size());
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("群发历史查询", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
			response.getWriter().print(result);
		} catch (Exception e) {		
			   //异常打印
				//response.sendRedirect(request.getContextPath()+"/smt_smsTaskRecord.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
			    EmpExecutionContext.error(e,"群发历史查询的excel导出异常！");
			    response.getWriter().print("false");
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
		String requestPath = request.getRequestURI();
		String titlePath = requestPath.substring(requestPath.lastIndexOf("_")+1, requestPath.lastIndexOf("."));
		String exportType="1";
		
		long startTime = System.currentTimeMillis();
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		//企业编码
		String corpCode = null;
		//用户id
		String userId = null;
        try
        {
        	//登录操作员信息
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//当前登录用户的企业编码
			corpCode =loginSysuser.getCorpCode();
			userId =String.valueOf(loginSysuser.getUserId());
			
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
	            		EmpExecutionContext.error("任务ID转换异常，taskID：" + taskID);
					}
	         }
            
			 //导出excel 短信内容
			 String msg=request.getParameter("msg");
			 if(msg!=null&&!"".equals(msg.trim()))
			 {
				 mtVo.setMsg(msg.trim());
			 }
			 
			 	//是否包含子机构
	            String isContainsSun=request.getParameter("isContainsSun");
	            if(isContainsSun!=null&&!"".equals(isContainsSun)){
	            	mtVo.setIsContainsSun(isContainsSun);
	            }
			 
            userid = (userid != null && userid.length()>0 && !userid.equals("请选择"))?userid.substring(0,userid.lastIndexOf(",")):"";
            depid = (depid != null && depid.length()>0)?depid:"";
          //查询移动财务短信及群发短信
            //mtVo.setMsTypes("1,5");
            //群发短信、移动财务
            if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath)){
            	exportType="1";
            	mtVo.setMsTypes("1,5,9,10");
            //移动财务
            }else if("sendTask".equals(titlePath)){
            	exportType="2";
            	mtVo.setMsTypes("5");
            }else if("surlSendedBox".equals(titlePath)){
            	//exportType="2";
				exportType="3";
            	mtVo.setMsTypes("31");
            }
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
			//返回状态
			String result = "false";
			//操作日志信息
			String opContent = "";
			if (mtVoList != null && mtVoList.size()>0 ) 
			{
        
				SmstaskExcelTool et=new SmstaskExcelTool(excelPath);
				Map<String, String> resultMap = et.createSmsSendedBoxExcel(mtVoList,exportType,request);
				
	            HttpSession session = request.getSession(false);
	            session.setAttribute("smssendedbox_export",resultMap);
				//操作日志信息
				opContent = "导出成功。";
				result = "true";
				
/*				String fileName=(String)resultMap.get("FILE_NAME");
		        String filePath=(String)resultMap.get("FILE_PATH");
		        	
		        DownloadFile dfs=new DownloadFile();
		        dfs.downFile(request, response, filePath, fileName);*/
			}
			else
			{
				//response.sendRedirect(request.getContextPath()+"/smt_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
				//操作日志信息
				opContent = "导出失败。";
			}

			//格式化时间
			SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
			opContent += "开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + (mtVoList==null?0:mtVoList.size());
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("群发任务查看", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
			response.getWriter().print(result);
		} catch (Exception e) {		
			//异常处理
//			response.sendRedirect(request.getContextPath()+"/smt_smsSendedBox.htm?lguserid="+userId+"&lgcorpcode="+corpCode);
		    EmpExecutionContext.error(e,"群发任务查看的excel导出异常！");
		    response.getWriter().print("false");
		} 
	}
	
	/**
	 * 群发任务查看EXCEL文件下载
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-8-13 下午12:26:22
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//文件名
		String fileName = "";
		//文件路径
		String filePath = "";
		//导出类型：smssendedbox_export(历史任务查看);smstaskrecord_export(群发历史查询)；smstaskreply_export（群发历史查询详情）
		String exportType = "";
        try
		{
			HttpSession session = request.getSession(false);
			exportType = request.getParameter("exporttype");
			Object obj = session.getAttribute(exportType);
			if(obj != null){
			    Map<String, String> resultMap = (Map<String, String>) obj;
			    fileName = (String) resultMap.get("FILE_NAME");
			    filePath = (String) resultMap.get("FILE_PATH");
			    //弹出下载页面
			    new DownloadFile().downFile(request, response, filePath, fileName);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "群发EXCEL文件下载失败，fileName:"+fileName+"，filePath:"+filePath+"，exportType:"+exportType);
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
		
		//获取加密类
		ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
		//解密mtId
		mtId=encryptOrDecrypt.decrypt(mtId);
		
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
		LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String opUser = sysuser.getUserName();
		
		String mtId = request.getParameter("mtId");
		
		//获取加密类
		ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
		//解密mtId
		mtId=encryptOrDecrypt.decrypt(mtId);
		
		String mobileUrl = request.getParameter("mobileUrl");
		Integer subState = Integer.valueOf(request.getParameter("subState"));
		String result = "";
		SmsBiz mtBiz=new SmsBiz();
		
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		opContent ="撤销短信任务（短信任务MT_ID："+mtId+"）";
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
				opContent+="(任务批次taskID："+taskid+")";
				//调用撤消任务的方法
				result=mtBiz.cancelSmsTask(mt,subState);
				if(result=="cancelSuccess"){
					new ReviewBiz().updateFlowRecordByMtId(taskid);
				}
			}
			EmpExecutionContext.info("群发任务查看", lgcorpcode, String.valueOf(sysuser.getUserId()), opUser, opContent.toString()+"成功！", "OTHER");
			//处理取消审核流程
			spLog.logSuccessString(opUser, opModule, opType, opContent,lgcorpcode);
		}catch(Exception e)
		{
			result="error";
			EmpExecutionContext.info("群发任务查看", lgcorpcode, String.valueOf(sysuser.getUserId()), opUser, opContent.toString()+"失败！", "OTHER");
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
			//mtid = Long.parseLong(request.getParameter("mtid"));
			
			String mtidStr=request.getParameter("mtid");
			//获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密mtId
			mtid=Long.parseLong(encryptOrDecrypt.decrypt(mtidStr));
			
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
			if(lfMttask.getIsRetry() != null && lfMttask.getIsRetry() ==1)
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
			Date time = Calendar.getInstance().getTime();
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
			
		} catch (Exception e) {
			if(e instanceof HttpHostConnectException)
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
							/*未审批*/
							member.put("mmsexstate",MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_236",request));
							break;
						case 1:
							/*审批通过*/
							member.put("mmsexstate",MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_235",request));
							break;
						case 2:
							/*审批不通过*/
							member.put("mmsexstate",MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_150",request));
							break;
						default:
							/*无效的标示*/
							member.put("mmsexstate","["+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_150",request)+"]");
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
				if(useridstr != null && !"".equals(useridstr.toString())){
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
						if(lastrecord.getRLevel() != null && !lastrecord.getRLevel().equals(lastrecord.getRLevelAmount()))
						{
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
	public static String defaultValue(Object obj){
		if(obj == null){
			return "";
		}
		return String.valueOf(obj);
	}
	/**
	 * 短信回复记录导出
	 * @param request
	 * @param response
	 */
	public void ReportReplyExcel(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			long startTime = System.currentTimeMillis();
			//企业编码
			String corpCode = request.getParameter("lgcorpcode");
			//名称
			String replyName = (String)request.getSession(false).getAttribute("r_replyName");
			//手机号
			String replyMoblie = (String)request.getSession(false).getAttribute("r_replyMoblie");
			//内容
			String replyContent = (String)request.getSession(false).getAttribute("r_replyContent");
			
			LinkedHashMap<String, String> conditionMap=new LinkedHashMap<String, String>();
			//短信任务id
			Long mtId = Long.valueOf(request.getParameter("mtId"));			
	    	CommonVariables  CV = new CommonVariables();
	    	Map<String,String> btnMap=(Map<String,String>)request.getSession(false).getAttribute("btnMap");
			int ishidephome=0;//是否隐藏手机号
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE)!=null){
				ishidephome=1;
			}
	    	//根据短信id，获取短信信息
	    	LfMttask mt=baseBiz.getById(LfMttask.class, mtId);

		 	//短信id
			if(mt!=null)
			{
				//taskid
				conditionMap.put("taskId", mt.getTaskId().toString());
				conditionMap.put("corpCode", corpCode);
				//号码查询条件
				if(replyMoblie != null && !"".equals(replyMoblie))
				{
					conditionMap.put("phone", replyMoblie);
				}
				//内容查询条件
				if(replyContent != null && !"".equals(replyContent))
				{
					conditionMap.put("msgContent&like", replyContent);
				}
            	if(replyName != null && !"".equals(replyName))
            	{
            		conditionMap.put("replyName", replyName);
            	}
            	
            	List<DynaBean> replyDetailList = smstaskBiz.getReplyDetailList(conditionMap, null);
				DynaBean replyDetai = null;
			 	if(replyDetailList != null && replyDetailList.size() > 0){
			 		List<ReplyParams> ReplyParamsList = new ArrayList<ReplyParams>();
			 		//号码
			 		String mobile = "";
			 		//回复时间
			 		String time = "";
			 		//回复内容
			 		String content = "";
			 		//姓名
			 		String name = "";
			 		for(int i=0;i<replyDetailList.size();i++){   
			 			//回复内容信息
			 			ReplyParams replyParams = new ReplyParams();  
			 			replyDetai =  replyDetailList.get(i);
				 		mobile = "";
				 		time = "";
				 		content = "";
				 		//姓名
				 		name = "";

			            //号码
			            mobile = replyDetai.get("phone")!=null?replyDetai.get("phone").toString():"";
			            //设置号码是否可见
			            if(mobile!=null && !"".equals(mobile)){
			            	if(ishidephome!=1){
			            		mobile = CV.replacePhoneNumber(mobile);
			            	}
			            }
			            else{
			            	mobile = "-";
			            }
			            replyParams.setPhone(mobile);

			            
			 			//姓名
			            name = replyDetai.get("name")!=null&&!"".equals(replyDetai.get("name").toString())?replyDetai.get("name").toString():"-";
			            replyParams.setName(name);
			            
			            //回复时间
			            time = replyDetai.get("delivertime")!=null?replyDetai.get("delivertime").toString():"-";
			            replyParams.setTime(time);	
			            
			            //回复内容
			            content = replyDetai.get("msgcontent")!=null?replyDetai.get("msgcontent").toString():"-";
			            replyParams.setContent(content);
			            ReplyParamsList.add(replyParams);
			        }
            	
			 		//导出操作
					SmstaskExcelTool et=new SmstaskExcelTool(excelPath);
					Map<String, String> resultMap = et.createSmsReplyReportExcel(ReplyParamsList, ishidephome,request);

		            HttpSession session = request.getSession(false);
		            session.setAttribute("smstaskreply_export",resultMap);

		        	//格式化时间
		        	SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
					//操作日志信息
		            String opContent = "回复详情导出成功。开始："+sdformat.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + ReplyParamsList.size();
					LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
					EmpExecutionContext.info("群发历史查询", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
					response.getWriter().print("true");
					
//					String fileName=(String)resultMap.get("FILE_NAME");
//			        String filePath=(String)resultMap.get("FILE_PATH");
//			        //文件下载	
//			        DownloadFile dfs=new DownloadFile();
//			        dfs.downFile(request, response, filePath, fileName);
//			        //操作员信息
//					LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
//					//操作员姓名
//					String opUser = sysuser==null?"":sysuser.getUserName();
			        //操作日志
//					EmpExecutionContext.info("群发历史查询", corpCode, lguserid, opUser, "群发历史查询回复详情记录导出，共导出"+ReplyParamsList.size()+"条记录。", "OTHER");
			 	}
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"群发任务回复详情查询异常！");
			response.getWriter().print("false");
		}
	
	}
}