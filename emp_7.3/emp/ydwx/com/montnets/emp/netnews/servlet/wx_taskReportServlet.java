/**
 * 
 */
package com.montnets.emp.netnews.servlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.netnews.biz.SmstaskBiz;
import com.montnets.emp.netnews.biz.SmstaskExcelTool;
import com.montnets.emp.netnews.vo.LfMttaskVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public class wx_taskReportServlet extends BaseServlet
{

	private final String empRoot		= "ydwx";

	final String basePath	= "/report";

	private final String path	= new TxtFileUtil().getWebRoot();

	// 模板路径
	protected final String excelPath	= path + "ydwx/report/file/";

	// 常用文件读写工具类
	final TxtFileUtil	txtfileutil	= new TxtFileUtil();

	// 网讯发送查询 msType = 6
	private final String mstype= "6";

	private final BaseBiz	baseBiz	= new BaseBiz();
	
	
	/***
	 * 网讯发送查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		// 短信biz
		SmstaskBiz smsBiz = new SmstaskBiz();
		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		String requestPath = request.getRequestURI();
		String	titlePath	= "";
		String path = requestPath.substring(requestPath.lastIndexOf("/")).replaceAll(".htm", ".jsp");
		titlePath = requestPath.substring(requestPath.lastIndexOf("_") + 1, requestPath.lastIndexOf("."));
		PageInfo pageInfo = new PageInfo();
		try
		{
			// 导出方法的查询条件清空
			request.getSession(false).setAttribute("r_sendTime", null);
			request.getSession(false).setAttribute("r_recvTime", null);
			request.getSession(false).setAttribute("r_userIds", null);
			request.getSession(false).setAttribute("r_depIds", null);
			request.getSession(false).setAttribute("r_sendTitle", null);
			request.getSession(false).setAttribute("r_netName", null);
			boolean isFirstEnter = pageSet(pageInfo, request);
			// 当前登录用户的企业编码
			String corpCode = request.getParameter("lgcorpcode");
			//String userId = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userId = SysuserUtil.strLguserid(request);

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			orconp.put("corpCode", "asc");
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orconp);

			conditionMap.clear();
			conditionMap.put("platFormType", "1");
			conditionMap.put("isValidate", "1");
			// 如果不是10000用户登录，则需要带上企业编码查询
			if(corpCode != null && !corpCode.equals("100000"))
			{
				conditionMap.put("corpCode", corpCode);
			}
			// 查找绑定的sp账号
			List<LfSpDepBind> userList = baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);

			String spUser = request.getParameter("spUser");
			String depid = request.getParameter("depid");
			String userid = request.getParameter("userid");
			String startSubmitTime = request.getParameter("sendtime");
			String endSubmitTime = request.getParameter("recvtime");
			String busCode = request.getParameter("busCode");
			String title = request.getParameter("title");
			String netid = request.getParameter("netid");
			String netname = request.getParameter("netname");
			String conrstate = request.getParameter("conrstate");
			String taskstate = request.getParameter("taskstate");
			// 获取过滤条件
			userid = (userid != null && userid.length() > 0 && !userid.equals(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request))) ? userid.substring(0, userid.lastIndexOf(",")) : "";
			depid = (depid != null && depid.length() > 0) ? depid : "";
			// 查询移动财务短信及群发短信

			mtVo.setMsTypes(mstype);
			if(!isFirstEnter)
			{
				mtVo.setNetid(netid);
				mtVo.setNetname(netname);
				mtVo.setSpUser(spUser);
				if(busCode != null && !busCode.equals(""))
					mtVo.setBusCode(busCode);
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);

				// 审批状态
				if(conrstate != null && conrstate.length() > 0)
				{
					int cstate = Integer.parseInt(conrstate);
					// 无需审批:0，未审批:-1，同意:1，拒绝:2
					mtVo.setSubState(2);
					switch (cstate)
					{
						// 未审批
						case 1:
							mtVo.setReStates("-1");
							break;
						case 2:
							// 审批通过
							mtVo.setReStates("1");
							break;
						case 3:
							// 审批不通过
							mtVo.setReStates("2");
							break;
						case 4:
							// 审批完成
							mtVo.setReStates("0,1,2");
							break;

						default:
							break;
					}
				}

				if(title != null && title.length() > 0)
				{
					mtVo.setTitle(title);
				}
				// 如果用户请求"群发历史查询"，则查询出所有已发历史记录
				if(titlePath.equals("wmsTaskRecord"))
				{
					mtVo.setOverSendstate("1,2,3,6");
					// 查询发送时间
					if(!"".equals(endSubmitTime))
						mtVo.setEndSendTime(endSubmitTime);
					if(!"".equals(startSubmitTime))
						mtVo.setStartSendTime(startSubmitTime);
				}
				else
				{
					// 查询创建时间
					if(!"".equals(endSubmitTime))
						mtVo.setEndSubmitTime(endSubmitTime);
					if(!("").equals(startSubmitTime))
						mtVo.setStartSubmitTime(startSubmitTime);
				}

				if(taskstate != null && taskstate.length() > 0)
				{
					int tstate = Integer.parseInt(taskstate);
					// 任务状态

					switch (tstate)
					{
						case 1:
							// 待发送
							mtVo.setSubState(2);
							if(conrstate != null && conrstate.length() > 0)
							{
								int cstate = Integer.parseInt(conrstate);
								if(cstate == 1 || cstate == 3)
								{
									mtVo.setReStates("-16");
								}
								else
									if(cstate == 4)
									{
										mtVo.setReStates("0,1");
									}
							}
							else
							{
								mtVo.setReStates("0,1");
							}
							mtVo.setOverSendstate("0");
							break;
						case 2:
							// 已发送
							mtVo.setOverSendstate("1,2,3,6");
							break;
						case 3:
							// 已冻结
							mtVo.setSubState(4);
							break;
						case 4:
							// 已撤销
							mtVo.setSubState(3);
							break;
						case 5:
							// 超时未发送
							mtVo.setOverSendstate("5");
							break;
						default:
							break;
					}
				}

				// 设置导出的查询条件
				request.getSession(false).setAttribute("r_sendTime", mtVo.getStartSendTime());
				request.getSession(false).setAttribute("r_recvTime", mtVo.getEndSendTime());
				request.getSession(false).setAttribute("r_userIds", mtVo.getUserIds());
				request.getSession(false).setAttribute("r_depIds", mtVo.getDepIds());
				request.getSession(false).setAttribute("r_sendTitle", mtVo.getTitle());
				request.getSession(false).setAttribute("r_netName", mtVo.getNetname());
				// 查询lfmttask信息
				if(corpCode != null && corpCode.equals("100000"))
				{
					mtVoList = smsBiz.getLfMttaskVoWithoutDomination(mtVo, pageInfo);
				}
				else
				{
                    String flowId = request.getParameter("flowId");
                    //审批流程跳转 查询该审批流下待审批的任务
                    if(flowId != null && !"".equals(flowId)){
                        mtVo.setFlowID(Long.parseLong(flowId));
                    }
					mtVoList = smsBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, pageInfo);
				}
			}

			// 如果从短信助手或短信客服请求群发任务查看，则统一转向smt_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath))
			{
				path = "/smt_smsSendedBox.jsp";
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
			
			request.setAttribute("title", title);
			request.setAttribute("isFirstEnter", isFirstEnter);
			request.setAttribute("sendUserList", userList);
			request.setAttribute("busList", busList);
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lguserid", userId);// 当前登录用户id
			// 加载机构树
			String departmentTree = smsBiz.getDepartmentJosnData(Long.parseLong(userId),corpCode);
			// 如果用户请求是"群发历史查询"

			request.setAttribute("departmentTree", departmentTree);
			request.setAttribute("titlePath", titlePath);
			
			//增加查询日志
			if(pageInfo!=null){
				long end_time=System.currentTimeMillis();
				String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，查询总数："+pageInfo.getTotalRec();
				setLog(request, "网讯发送查询", opContent, "GET");
			}
			// 页面跳转
			request.getRequestDispatcher(this.empRoot + this.basePath + "/taskReport.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网讯发送查询异常");
			request.setAttribute("findresult", "-1");
			request.setAttribute("isFirstEnter", true);// 回到页面第一次加载时的状态
			request.setAttribute("titlePath", titlePath);
			request.setAttribute("pageInfo", pageInfo);
			// 如果从短信助手或短信客服请求群发任务查看，则统一转向smt_smsSendedBox.jsp
			if("smsSendedBox".equals(titlePath) || "smsSendedBox2".equals(titlePath))
			{
				path = "/smt_smsSendedBox.jsp";
			}
			try
			{
				request.getRequestDispatcher(this.empRoot + this.basePath + path).forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, "网讯发送查询跳转异常!");
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, "网讯发送查询IO异常!");
			}
		}
	}

	// 输出机构代码数据
	public void createDeptTree(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			Long depId = null;
			Long userid = null;
			// 部门iD
			String depStr = request.getParameter("depId");
			// 操作员账号
			//String userStr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String userStr = SysuserUtil.strLguserid(request);
			if(depStr != null && !"".equals(depStr.trim()))
			{
				depId = Long.parseLong(depStr);
			}
			if(userStr != null && !"".equals(userStr.trim()))
			{
				userid = Long.parseLong(userStr);
			}
			SmstaskBiz smsBiz = new SmstaskBiz();
			String departmentTree = smsBiz.getDepartmentJosnData2(depId, userid);
			response.getWriter().print(departmentTree);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "输出机构代码数据异常!");
		}
	}

	public void createUserTree2(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String depId = request.getParameter("depId");
		try
		{
			//获取当前登录操作员对象
			LfSysuser curUser = getCurUserInSession(request);
			String deptUserTree = new SmstaskBiz().getDepUserJosn(depId, curUser);
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
			EmpExecutionContext.error(e, "网讯发送查询，生成机构操作员树字符串，异常。depId="+depId);
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

	public String getDeptUserJosnData(String titlePath, Long depId, Long userid) throws Exception
	{
		StringBuffer tree = new StringBuffer();
		// 根据userid获取当前操作员信息
		LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
		if(currUser.getPermissionType() == 1)
		{
			tree = new StringBuffer("[]");
		}
		else
		{
			List<LfDep> lfDeps;
			List<LfSysuser> lfSysusers = null;

			DepBiz depBiz = new DepBiz();
			try
			{
				// 如果企业编码是10000的用户登录
				if(currUser.getCorpCode().equals("100000"))
				{
					if(depId == null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderbyMap);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,currUser.getCorpCode());
					}

				}
				else
				{
					if(depId == null)
					{
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(userid,currUser.getCorpCode()).get(0);
						lfDeps.add(lfDep);
					}
					else
					{
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(depId,currUser.getCorpCode());
					}
				}
				// 拼结机构树
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++)
				{
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					if(i != lfDeps.size() - 1)
					{
						tree.append(",");
					}
				}

				// SysuserBiz sysBiz = new SysuserBiz();
				if(depId != null)
				{
					// 如果当前登录用户的企业编码是10000
					if(currUser.getCorpCode().equals("100000"))
					{
						LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
						conMap.put("userId&<>", "1");
						conMap.put("depId", depId.toString());
						lfSysusers = baseBiz.getByCondition(LfSysuser.class, conMap, null);
					}
					else
					{
						SmstaskBiz smsBiz = new SmstaskBiz();
						// 获取所有状态操作员信息
						/*
						 * lfSysusers =
						 * sysBiz.getAllSysusersOfSmsTaskRecordByDep
						 * (userid,depId);
						 */
						lfSysusers = smsBiz.getAllSysusersOfSmsTaskRecordByDep(userid, depId);

					}
				}
				// 拼结操作员信息
				LfSysuser lfSysuser = null;
				if(lfSysusers != null && !lfSysusers.isEmpty())
				{
					if(lfDeps != null && lfDeps.size() > 0)
					{
						tree.append(",");
					}

					for (int i = 0; i < lfSysusers.size(); i++)
					{
						// 操作员信息
						lfSysuser = lfSysusers.get(i);
						tree.append("{");
						tree.append("id:'u").append(lfSysuser.getUserId()).append("'");
						tree.append(",name:'").append(lfSysuser.getName()).append("'");
						if(lfSysuser.getUserState() == 2)
						{
							tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
						}
						else
						{
							tree.append(",name:'").append(lfSysuser.getName()).append("'");
						}
						tree.append(",pId:").append(lfSysuser.getDepId());
						tree.append(",depId:'").append(lfSysuser.getDepId() + "'");
						tree.append(",isParent:").append(false);
						tree.append("}");
						if(i != lfSysusers.size() - 1)
						{
							tree.append(",");
						}
					}
				}

				tree.append("]");
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取机构下员工信息异常!");
			}
		}
		return tree.toString();
	}

	public void ReportAllPageExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{

		List<LfMttaskVo> mtVoList = null;
		LfMttaskVo mtVo = new LfMttaskVo();
		// 企业编码
		String corpCode = request.getParameter("lgcorpcode");
		// 用户id
		//String userId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String userId = SysuserUtil.strLguserid(request);

		PageInfo pageInfo = new PageInfo();
		PrintWriter out = response.getWriter();
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();		
		try
		{
			// 分页信息
			pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			// 排序
			orconp.put("corpCode", "asc");

			conditionMap.clear();
			conditionMap.put("platFormType", "1");
			conditionMap.put("isValidate", "1");
			if(corpCode != null && !corpCode.equals("100001"))
			{
				conditionMap.put("corpCode", corpCode);
			}

			String spUser = request.getParameter("spUser");
			String depid = request.getParameter("depid");
			// 用户id
			String userid = request.getParameter("userid");

			// 开始时间
			String startSubmitTime = request.getParameter("sendtime");
			// 结束时间
			String endSubmitTime = request.getParameter("recvtime");

			String busCode = request.getParameter("busCode");
			String netid = request.getParameter("netid");
			String netname = (String) request.getSession(false).getAttribute("r_netName");
			String title = (String) request.getSession(false).getAttribute("r_sendTitle");
			String conrstate = request.getParameter("conrstate");
			String taskstate = request.getParameter("taskstate");

			userid = (userid != null && userid.length() > 0 && !userid.equals(MessageUtils.extractMessage("ydwx","ydwx_jsp_out_qingxuanze",request))) ? userid.substring(0, userid.lastIndexOf(",")) : "";
			depid = (depid != null && depid.length() > 0) ? depid : "";
			// 查询移动财务短信及群发短信
			mtVo.setMsTypes(mstype);
			mtVo.setNetid(netid);
			mtVo.setNetname(netname);
			mtVo.setSpUser(spUser);
			if(busCode != null && !busCode.equals(""))
				mtVo.setBusCode(busCode);
			mtVo.setDepIds(depid);
			mtVo.setUserIds(userid);
			if(title != null && title.length() > 0)
			{
				mtVo.setTitle(title);
			}
			// 审批状态
			if(conrstate != null && conrstate.length() > 0)
			{
				int cstate = Integer.parseInt(conrstate);
				// 无需审批:0，未审批:-1，同意:1，拒绝:2
				mtVo.setSubState(2);
				switch (cstate)
				{
					// 未审批
					case 1:
						mtVo.setReStates("-1");
						break;
					case 2:
						// 审批通过
						mtVo.setReStates("1");
						break;
					case 3:
						// 审批不通过
						mtVo.setReStates("2");
						break;
					case 4:
						// 审批完成
						mtVo.setReStates("0,1,2");
						break;

					default:
						break;
				}
			}

			if(taskstate != null && taskstate.length() > 0)
			{
				int tstate = Integer.parseInt(taskstate);
				// 任务状态

				switch (tstate)
				{
					case 1:
						// 待发送
						mtVo.setSubState(2);
						if(conrstate != null && conrstate.length() > 0)
						{
							int cstate = Integer.parseInt(conrstate);
							if(cstate == 1 || cstate == 3)
							{
								mtVo.setReStates("-16");
							}
							else
								if(cstate == 4)
								{
									mtVo.setReStates("0,1");
								}
						}
						else
						{
							mtVo.setReStates("0,1");
						}
						mtVo.setOverSendstate("0");
						break;
					case 2:
						// 已发送
						mtVo.setOverSendstate("1,2,3,6");
						break;
					case 3:
						// 已冻结
						mtVo.setSubState(4);
						break;
					case 4:
						// 已撤销
						mtVo.setSubState(3);
						break;
					case 5:
						// 超时未发送
						mtVo.setOverSendstate("5");
						break;
					default:
						break;
				}
			}
			// 查询时间
			if(!"".equals(endSubmitTime))
				mtVo.setEndSubmitTime(endSubmitTime);
			if(!"".equals(startSubmitTime))
				mtVo.setStartSubmitTime(startSubmitTime);

			SmstaskBiz smsBiz = new SmstaskBiz();
			if(corpCode != null && corpCode.equals("100000"))
			{
				mtVoList = smsBiz.getLfMttaskVoWithoutDomination(mtVo, null);
			}
			else
			{
				mtVoList = smsBiz.getLfMttaskVo(Long.parseLong(userId), mtVo, null);
			}

			if(mtVoList != null && mtVoList.size() > 0)
			{

				SmstaskExcelTool et = new SmstaskExcelTool(excelPath);
				Map<String, String> resultMap = et.createSmsSendedBoxExcel(mtVoList,request);
				String fileName = (String) resultMap.get("FILE_NAME");
				String filePath = (String) resultMap.get("FILE_PATH");
				//增加查询日志
				long end_time=System.currentTimeMillis();
				String opContent ="导出开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，导出总数："+ mtVoList.size()+ "条";
				setLog(request, "网讯发送查询", opContent, StaticValue.GET);
//				DownloadFile dfs = new DownloadFile();
//				dfs.downFile(request, response, filePath, fileName);
				request.getSession(false).setAttribute("ReportAllPageExcel", fileName+"@@"+filePath);
				out.print("true");
			}
			else
			{
//				response.sendRedirect(request.getContextPath() + "/wx_taskreport.htm?lguserid=" + userId + "&lgcorpcode=" + corpCode);
				out.print("false");
			}
		}
		catch (Exception e)
		{
			// 异常处理
//			response.sendRedirect(request.getContextPath() + "/wx_taskreport.htm?lguserid=" + userId + "&lgcorpcode=" + corpCode);
			out.print("false");
			EmpExecutionContext.error(e, "导出网讯发送统计异常!");
		}
	}

	/**
	 * excel文件导出
	 * @param request
	 * @param response
	 */
	   public void downloadFile(HttpServletRequest request, HttpServletResponse response)   {
			try{
			   String down_session=request.getParameter("down_session");
			    HttpSession session = request.getSession(false);
			    Object obj = session.getAttribute(down_session);
		        if(obj != null){
		            String result = (String) obj;
		            if(result.indexOf("@@")>-1){
		            	String[] file=result.split("@@");
			            // 弹出下载页面。
			            DownloadFile dfs = new DownloadFile();
			            dfs.downFile(request, response, file[1], file[0]);
			            session.removeAttribute(down_session);
		            }
		        }
			}catch (Exception e1) {
				EmpExecutionContext.error(e1,"excel文件导出出现异常！");
			}
	    }
	/**
	 * 群发历史页面回复详情
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getVerify(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		try
		{
			String mtID = request.getParameter("mtId");
			LfMttask mttask = baseBiz.getById(LfMttask.class, mtID);
			StringBuffer buffer = null;
			buffer = new StringBuffer("<table border='1' width='100%' style='text-align:center'><tr  class='div_bd'><td class='div_bd'>审批级别</td>" + "<td class='div_bd'>审批人</td><td class='div_bd'>审批状态</td>" + "<td class='div_bd'>审批意见</td></tr><tbody>");
			// 设置条件的MAP
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			conditionMap.put("ProUserCode", String.valueOf(mttask.getUserId()));
			conditionMap.put("infoType", "1");
			conditionMap.put("mtId", mtID);
			conditionMap.put("RState&in", "1,2");
			conditionMap.put("isComplete", "1");
			orderByMap.put("RLevel", StaticValue.ASC);
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);

			if(flowRecords != null && flowRecords.size() > 0)
			{
				LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
				conditionMap.clear();
				String auditName = "";
				for (int j = 0; j < flowRecords.size(); j++)
				{
					LfFlowRecord temp = flowRecords.get(j);
					auditName = auditName + temp.getUserCode() + ",";
				}
				List<LfSysuser> sysuserList = null;
				if(auditName != null && !"".equals(auditName))
				{
					auditName = auditName.substring(0, auditName.length() - 1);
					conditionMap.put("userId&in", auditName);
					conditionMap.put("corpCode", mttask.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size() > 0)
					{
						for (LfSysuser sysuser : sysuserList)
						{
							nameMap.put(sysuser.getUserId(), sysuser.getName());
						}
					}
				}

				LfFlowRecord record = null;
				if(flowRecords == null || flowRecords.size() == 0)
				{
					buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>无记录</td></tr>");
				}
				else
				{
					for (int i = 0; i < flowRecords.size(); i++)
					{
						record = flowRecords.get(i);
						buffer.append("<tr  class='div_bd'>");
						buffer.append("<td  class='div_bd'>").append(record.getRLevel().toString()).append("</td>");
						if(nameMap != null && nameMap.size() > 0 && nameMap.containsKey(record.getUserCode()))
						{
							buffer.append("<td  class='div_bd'>").append(nameMap.get(record.getUserCode())).append("</td>");
						}
						else
						{
							buffer.append("<td  class='div_bd'>-</td>");
						}
						if(record.getRState() == 1)
							buffer.append("<td  class='div_bd'>").append("审批通过").append("</td>");
						if(record.getRState() == 2)
							buffer.append("<td  class='div_bd'>").append("审批不通过").append("</td>");
						if(record.getRState() == -1)
							buffer.append("<td  class='div_bd'>").append("未审批").append("</td>");
						if(record.getComments() != null && !"".equals(record.getComments()))
							buffer.append("<td style='word-break: break-all;'  class='div_bd'><xmp style='word-break: break-all;white-space:normal;'>").append(record.getComments()).append("</xmp></td>");
						else
						{
							buffer.append("<td  class='div_bd'>-").append("</td>");
						}
						buffer.append("</tr>");
					}
				}
			}
			else
			{
				buffer.append("<tr  class='div_bd'><td  class='div_bd' colspan='4'>无记录</td></tr>");
			}
			buffer.append("</tbody></table>");
			response.getWriter().print(buffer.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "群发历史页面回复详情异常！");
		}
	}

	/**
	 * 获取信息发送查询里的详细
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void getSmsDetail(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 彩信任务ID
		String mtId = request.getParameter("mtId");
		//此处解密（网讯中的发送查询）
		//获取加密类
		ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
		//解密mtId
		mtId=encryptOrDecrypt.decrypt(mtId);
		
		// 操作员用户ID
		String userId = request.getParameter("userId");
		try
		{
			LfSysuser user = baseBiz.getById(LfSysuser.class, userId);
			// 获取对应的彩信任务
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			//特别增加公司编码查询，防止攻击
			LfMttask mtTask = null;
			LfSysuser objuser = (LfSysuser)  request.getSession(false).getAttribute("loginSysuser");
			LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
			map.put("corpCode", objuser.getCorpCode());
			map.put("mtId", mtId);
			 List<LfMttask> mtTaskList =baseBiz.getByCondition(LfMttask.class, map, null);
			 if(mtTaskList!=null&&mtTaskList.size()>0){
				 mtTask=mtTaskList.get(0);
			 }else{
				 EmpExecutionContext.error("获取信息发送查询里的详细异常！");
			 }
			 
//			LfMttask mtTask = baseBiz.getById(LfMttask.class, mtId);
			JSONObject jsonObject = new JSONObject();
			// 设置条件的MAP
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			conditionMap.put("ProUserCode", String.valueOf(mtTask.getUserId()));
			conditionMap.put("infoType", "5");
			conditionMap.put("mtId", String.valueOf(mtTask.getTaskId()));
			conditionMap.put("RState&in", "1,2");
			conditionMap.put("isComplete", "1");
			orderByMap.put("RLevel", StaticValue.ASC);
			orderByMap.put("preRv", StaticValue.DESC);
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			JSONArray members = new JSONArray();
			// 标识该审核中最大的级
			// LfFlowRecord maxRecord = null;
			if(flowRecords != null && flowRecords.size() > 0)
			{
				LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
				conditionMap.clear();
				String auditName = "";
				for (int j = 0; j < flowRecords.size(); j++)
				{
					LfFlowRecord temp = flowRecords.get(j);
					auditName = auditName + temp.getUserCode() + ",";
				}
				// 获取该审核流程最大的审批级别
				// maxRecord = flowRecords.get(flowRecords.size()-1);

				List<LfSysuser> sysuserList = null;
				if(auditName != null && !"".equals(auditName))
				{
					auditName = auditName.substring(0, auditName.length() - 1);
					conditionMap.put("userId&in", auditName);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size() > 0)
					{
						for (LfSysuser sysuser : sysuserList)
						{
							nameMap.put(sysuser.getUserId(), sysuser.getName());
						}
					}
				}

				// 是否有审批信息1有 2 没有
				jsonObject.put("haveRecord", "1");
				JSONObject member = null;
				LfFlowRecord record = null;
				for (int i = 0; i < flowRecords.size(); i++)
				{
					member = new JSONObject();
					record = flowRecords.get(i);
					member.put("mmsRlevel", record.getRLevel().toString() + "/" + record.getRLevelAmount().toString());
					// 审批人
					if(nameMap != null && nameMap.size() > 0 && nameMap.containsKey(record.getUserCode()))
					{
						member.put("mmsReviname", nameMap.get(record.getUserCode()));
					}
					else
					{
						member.put("mmsReviname", "-");
					}
					if(record.getRTime() == null)
					{
						member.put("mmsrtime", "-");
					}
					else
					{
						member.put("mmsrtime", df.format(record.getRTime()));
					}
					// 审批结果
					int state = record.getRState();
					switch (state)
					{
						case -1:
							/*未审批*/
							member.put("mmsexstate", MessageUtils.extractMessage("common","common_reviewFlow_text_1",request));
							break;
						case 1:
							/*审批通过*/
							member.put("mmsexstate", MessageUtils.extractMessage("common","common_reviewFlow_text_6",request));
							break;
						case 2:
							/*审批不通过*/
							member.put("mmsexstate", MessageUtils.extractMessage("common","common_reviewFlow_text_5",request));
							break;
						default:
							/*无效的标示*/
							member.put("mmsexstate", "["+MessageUtils.extractMessage("common","common_reviewFlow_text_3",request)+"]");
					}

					if("".equals(record.getComments()) || record.getComments() == null)
					{
						member.put("mmsComments", "");
					}
					else
					{
						member.put("mmsComments", record.getComments());
					}
					members.add(member);
				}
				jsonObject.put("members", members);
			}
			else
			{
				jsonObject.put("haveRecord", "2");
			}

			conditionMap.clear();
			String firstshowname = "";
			String firstcondition = "";
			// 一级都没有审核
			// 获取下一级审核
			conditionMap.put("ProUserCode", String.valueOf(mtTask.getUserId()));
			conditionMap.put("infoType", "5");
			conditionMap.put("mtId", String.valueOf(mtTask.getTaskId()));
			conditionMap.put("RState", "-1");
			conditionMap.put("isComplete", "2");
			List<LfFlowRecord> unflowRecords = baseBiz.getByCondition(LfFlowRecord.class, conditionMap, orderByMap);
			LfFlowRecord lastrecord = null;
			Long depId = null;
			String[] recordmsg = new String[2];
			recordmsg[0] = "";
			recordmsg[1] = "";
			String isshow = "2";
			if(unflowRecords != null && unflowRecords.size() > 0)
			{
				isshow = "1";
				StringBuffer useridstr = new StringBuffer();
				for (LfFlowRecord temp : unflowRecords)
				{
					useridstr.append(temp.getUserCode()).append(",");
				}
				if(lastrecord == null)
				{
					lastrecord = unflowRecords.get(0);
				}
				List<LfSysuser> sysuserList = null;
				if(useridstr != null && !"".equals(useridstr.toString()))
				{
					String str = useridstr.toString().substring(0, useridstr.toString().length() - 1);
					conditionMap.clear();
					conditionMap.put("userId&in", str);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
					if(sysuserList != null && sysuserList.size() > 0)
					{
						for (LfSysuser sysuser : sysuserList)
						{
							firstshowname = firstshowname + sysuser.getName() + "&nbsp;&nbsp;";
							if(depId == null)
							{
								depId = sysuser.getDepId();
							}
						}
					}
				}
				if(lastrecord != null)
				{
					// 审核类型 1操作员 4机构 5逐级审核
					Integer rtype = lastrecord.getRType();
					firstcondition = lastrecord.getRCondition() + "";
					ReviewBiz reviewBiz = new ReviewBiz();
					// 当是逐步审批的时候
					if(rtype == 5)
					{
						// 获取逐级审批
						boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(lastrecord.getPreRv().intValue(), lastrecord.getProUserCode());
						// 逐级审批的最后一级
						if(isLastLevel)
						{
							if(lastrecord.getRLevelAmount() != 1)
							{
								lastrecord.setRLevel(1);
								recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
							}
						}
						else
						{
							LfDep dep = baseBiz.getById(LfDep.class, depId);
							if(dep != null)
							{
								LfDep pareDep = baseBiz.getById(LfDep.class, dep.getSuperiorId());
								if(pareDep != null)
								{
									recordmsg[0] = pareDep.getDepName();
									recordmsg[1] = lastrecord.getRCondition() + "";
								}
							}
						}
					}
					else
					{
						// 该流程审批的最后一级
						//if(lastrecord.getRLevel() != lastrecord.getRLevelAmount())  原来是这么写的 ，通过工具检测，就修改下面的情况
						if(lastrecord.getRLevel()!=null&&lastrecord.getRLevelAmount()!=null&&lastrecord.getRLevel().intValue() != lastrecord.getRLevelAmount().intValue())
						{
							recordmsg = reviewBiz.getNextFlowRecord(lastrecord, user.getCorpCode());
						}
					}
				}
			}
			jsonObject.put("isshow", isshow);
			jsonObject.put("firstshowname", firstshowname);
			jsonObject.put("firstcondition", firstcondition);
			jsonObject.put("secondshowname", recordmsg[0]);
			jsonObject.put("secondcondition", recordmsg[1]);

			response.getWriter().print(jsonObject.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("fail");
			EmpExecutionContext.error(e, "获取信息发送查询里的详细!");
		}
	}

	protected int getIntParameter(HttpServletRequest request, String param, int defaultValue)
	{
		try
		{
			return Integer.parseInt(request.getParameter(param));
		}
		catch (NumberFormatException e)
		{
			// 异常处理
			EmpExecutionContext.error(e,"网讯中数字转换异常");
			return defaultValue;
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
