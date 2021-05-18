package com.montnets.emp.mmstask.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.DepBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.vo.LfMttaskVo2;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mmstask.biz.MmsTaskBiz;
import com.montnets.emp.samemms.biz.CreateTmsFile;
import com.montnets.emp.servmodule.ydcx.constant.ServerInof;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 彩信发送信息查看 mms_examinInfo.htm
 * 
 * @author Administrator
 * 
 */
@SuppressWarnings("serial")
public class mmt_mmsTaskSvt extends BaseServlet {
	/* private MttaskBiz mtBiz = new MttaskBiz(); */
	private static MmsTaskBiz mtBiz = new MmsTaskBiz();
	private static TxtFileUtil txtfileutil = new TxtFileUtil();
	private static String empRoot = "ydcx";
	private static String basePath = "/mmstask";
	private static BaseBiz baseBiz = new BaseBiz();

	/**
	 * @param request
	 * @param response
	 */
	// 进入查询页面
	public void find(HttpServletRequest request, HttpServletResponse response) {
		PageInfo pageInfo = new PageInfo();
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time = System.currentTimeMillis();
		try {

			String lgguid = "";
			Long userGuid = null;
			String corpCode = "";
			Long userId = null;
			LfSysuser sysUser = null;
			// 获取操作员的GUID
			lgguid = request.getParameter("lgguid");
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
					.getAttribute("loginSysuser");
			lgguid = String.valueOf(loginSysuser.getUserId());
			userGuid = Long.valueOf(lgguid);
			// 获取当前操作员的对象
			// sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
			// 获取企业编码
			// corpCode = sysUser.getCorpCode();
			// 获取操作员的用户ID
			// userId = sysUser.getUserId();

			// 登录操作员信息

			// 当前登录用户的企业编码
			corpCode = loginSysuser.getCorpCode();
			userId = loginSysuser.getUserId();

			LfMttaskVo2 mtVo = new LfMttaskVo2();
			String depid = request.getParameter("depid");
			depid = (depid != null && depid.length() > 0) ? depid : "";
			String userid = request.getParameter("userid");
			userid = (userid != null && userid.length() > 0 && !userid
					.equals("请选择")) ? userid.substring(0, userid
					.lastIndexOf(",")) : "";
			boolean isFirstEnter = pageSet(pageInfo, request);

			// 是否包含子机构
			String isContainsSun = request.getParameter("isContainsSun");
			if (isContainsSun != null && !"".equals(isContainsSun)) {
				mtVo.setIsContainsSun(isContainsSun);
			}

			if (!isFirstEnter) {
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);
				// 主题
				String taskName = request.getParameter("taskName");
				if (taskName != null && !"".equals(taskName)) {
					taskName = taskName.replace("'", "");
					mtVo.setTaskName(taskName);
				}
				// 标题
				String theme = request.getParameter("theme");
				if (theme != null && !"".equals(theme)) {
					theme = theme.replace("'", "");
					mtVo.setTitle(theme);
				}
				// 彩信状态
				String state = request.getParameter("state");
				if (state != null && !"".equals(state)) {
					mtVo.setReState(Integer.parseInt(state));
				}
				// 时间段
				String submitEndTime = request.getParameter("submitEndTime");
				if (submitEndTime != null && !"".equals(submitEndTime)) {
					mtVo.setEndSubmitTime(submitEndTime);
				}
				String submitStartTime = request.getParameter("submitSartTime");
				if (submitStartTime != null && !"".equals(submitStartTime)) {
					mtVo.setStartSubmitTime(submitStartTime);
				}

				if (isContainsSun == null || "".equals(isContainsSun)) {
					request.setAttribute("isContainsSun", "0");
				} else {
					request.setAttribute("isContainsSun", "1");
				}
			}
			// 网关状态
			String sstate = request.getParameter("sstate");
			if (sstate != null && !"".equals(sstate)) {
				if ("3".equals(sstate)) {
					mtVo.setSendstate(0);
					mtVo.setSubState(3);
				} else {
					mtVo.setSendstate(Integer.parseInt(sstate));
					if ("0".equals(sstate)) {
						mtVo.setSubState(2);
					}
				}
				request.setAttribute("sstate", sstate);
			}
			if (userId - 1 != 0) {
				mtVo.setUserId(userId);
			}
			mtVo.setMsType(2);
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("submitTime", StaticValue.DESC);
			// 查询出彩信任务
			List<LfMttaskVo2> mtVoList = null;
			try {
				if (corpCode != null && corpCode.equals("100000")) {
					mtVoList = mtBiz.getMmsLfMttaskVoWithoutDomination(mtVo,
							pageInfo);
				} else {
					String flowId = request.getParameter("flowId");
					// 审批流程跳转 查询该审批流下待审批的任务
					if (flowId != null && !"".equals(flowId)) {
						mtVo.setFlowID(Long.parseLong(flowId));
					}
					mtVoList = mtBiz.getMmsLfMttaskVo(userId, mtVo, pageInfo);
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "查询彩信任务出现异常！");
			}
			request.setAttribute("mtList", mtVoList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("mtVo", mtVo);
			request.setAttribute("findresult", "");
			// 设置服务器名称
			new ServerInof().setServerName(getServletContext().getServerInfo());
			// 增加查询日志
			if (pageInfo != null) {
				long end_time = System.currentTimeMillis();
				String opContent = "查询开始时间：" + format.format(begin_time)
						+ ",耗时:" + (end_time - begin_time) + "毫秒，数量："
						+ pageInfo.getTotalRec();
				opSucLog(request, "移动彩讯-发送信息查看", opContent, "GET");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "查询彩信任务出现异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
		} finally {
			try {
				request.getRequestDispatcher(
						empRoot + basePath + "/mmt_mmsTask.jsp").forward(
						request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "彩信任务查询页面跳转失败！");
			}
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	/*
	 * //获取对应的彩信任务信息 public void getDetail(HttpServletRequest request,
	 * HttpServletResponse response) { //彩信任务ID String mtId =
	 * request.getParameter("mtId"); //类型 String type =
	 * request.getParameter("type"); //操作员用户ID String userId =
	 * request.getParameter("userId"); Long id = null; LfSysuser sysUser = null;
	 * String userName = ""; try { if(userId == null || "".equals(userId)){ id =
	 * getUserId(); }else{ id = Long.valueOf(userId); } //获取当前对象 sysUser =
	 * baseBiz.getById(LfSysuser.class, id); //获取用户名称 userName =
	 * sysUser.getUserName(); } catch (Exception e1) {
	 * EmpExecutionContext.error(e1); userName = getUserName(); } try { //获取彩信任务
	 * LfMttask mtTask = baseBiz.getById(LfMttask.class, mtId); //设值
	 * request.setAttribute("mtTask", mtTask); request.setAttribute("type",
	 * type); //设置条件的MAP LinkedHashMap<String, String> conditionMap = new
	 * LinkedHashMap<String, String>(); conditionMap.put("userId",userName);
	 * //获取审批信息 List<LfFlow> ll = baseBiz.getByCondition(LfFlow.class,
	 * conditionMap, null); LfFlow flow = new LfFlow(); if(ll != null &&
	 * ll.size() > 0) { flow = ll.get(0); } else { flow.setRLevelAmount(1); }
	 * MsgReviewBiz smsBiz=new MsgReviewBiz(); //获取审核信息 List<LfFlowRecordVo>
	 * frVoList
	 * =smsBiz.getFlowRecordVos(mtId,flow.getRLevelAmount().toString(),"2");
	 * request.setAttribute("frVoList", frVoList); //跳转
	 * request.getRequestDispatcher(this.empRoot+"/mms/mms_exDetail.jsp")
	 * .forward(request, response);
	 * 
	 * 
	 * } catch (Exception e) { //异常 EmpExecutionContext.error(e); } }
	 */

	/**
	 * 获取信息发送查询里的详细
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public void getMmsDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 彩信任务ID
		String mtId = request.getParameter("mtId");
		// 操作员用户ID
		String userId = request.getParameter("userId");
		try {
			LfSysuser user = baseBiz.getById(LfSysuser.class, userId);
			// 获取对应的彩信任务
			java.text.SimpleDateFormat df = new java.text.SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			LfMttask mtTask = baseBiz.getById(LfMttask.class, mtId);
			JSONObject jsonObject = new JSONObject();
			// 主题
			jsonObject.put("mmsname", String.valueOf(mtTask.getTaskName()));
			// 标题
			jsonObject.put("mmstitle", mtTask.getTitle());
			// 总数
			jsonObject.put("mmscount", String.valueOf(mtTask.getSubCount()));
			// 有效数
			jsonObject.put("mmseffcount", String.valueOf(mtTask.getEffCount()));
			// 发送时间
			jsonObject.put("mmstime", String.valueOf(df.format(mtTask
					.getSubmitTime())));
			// 提交状态
			jsonObject.put("mmsubstate", String.valueOf(mtTask.getSubState()));
			// 发送状态
			jsonObject
					.put("mmsendstate", String.valueOf(mtTask.getSendstate()));

			// 设置条件的MAP
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			conditionMap.put("ProUserCode", String.valueOf(mtTask.getUserId()));
			conditionMap.put("infoType", "2");
			conditionMap.put("mtId", String.valueOf(mtTask.getTaskId()));
			conditionMap.put("RState&in", "1,2");
			conditionMap.put("isComplete", "1");
			orderByMap.put("RLevel", StaticValue.ASC);
			orderByMap.put("preRv", StaticValue.DESC);
			List<LfFlowRecord> flowRecords = baseBiz.getByCondition(
					LfFlowRecord.class, conditionMap, orderByMap);
			JSONArray members = new JSONArray();
			// 标识该审核中最大的级
			// LfFlowRecord maxRecord = null;
			if (flowRecords != null && flowRecords.size() > 0) {
				LinkedHashMap<Long, String> nameMap = new LinkedHashMap<Long, String>();
				conditionMap.clear();
				String auditName = "";
				for (int j = 0; j < flowRecords.size(); j++) {
					LfFlowRecord temp = flowRecords.get(j);
					auditName = auditName + temp.getUserCode() + ",";
				}
				// 获取该审核流程最大的审批级别
				// maxRecord = flowRecords.get(flowRecords.size()-1);

				List<LfSysuser> sysuserList = null;
				if (auditName != null && !"".equals(auditName)) {
					auditName = auditName.substring(0, auditName.length() - 1);
					conditionMap.put("userId&in", auditName);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class,
							conditionMap, null);
					if (sysuserList != null && sysuserList.size() > 0) {
						for (LfSysuser sysuser : sysuserList) {
							nameMap.put(sysuser.getUserId(), sysuser.getName());
						}
					}
				}

				// 是否有审批信息1有 2 没有
				jsonObject.put("haveRecord", "1");
				JSONObject member = null;
				LfFlowRecord record = null;
				for (int i = 0; i < flowRecords.size(); i++) {
					member = new JSONObject();
					record = flowRecords.get(i);
					member.put("mmsRlevel", record.getRLevel().toString() + "/"
							+ record.getRLevelAmount().toString());
					// 审批人
					if (nameMap != null && nameMap.size() > 0
							&& nameMap.containsKey(record.getUserCode())) {
						member.put("mmsReviname", nameMap.get(record
								.getUserCode()));
					} else {
						member.put("mmsReviname", "-");
					}
					if (record.getRTime() == null) {
						member.put("mmsrtime", "-");
					} else {
						member.put("mmsrtime", df.format(record.getRTime()));
					}
					// 审批结果
					int state = record.getRState();
					switch (state) {
					case -1:
						member.put("mmsexstate", "未审批");
						break;
					case 1:
						member.put("mmsexstate", "审批通过");
						break;
					case 2:
						member.put("mmsexstate", "审批不通过");
						break;
					default:
						member.put("mmsexstate", "[无效的标示]");
					}

					if ("".equals(record.getComments())
							|| record.getComments() == null) {
						member.put("mmsComments", "");
					} else {
						member.put("mmsComments", record.getComments());
					}
					members.add(member);
				}
				jsonObject.put("members", members);
			} else {
				jsonObject.put("haveRecord", "2");
			}

			conditionMap.clear();
			String firstshowname = "";
			String firstcondition = "";
			// 一级都没有审核
			// 获取下一级审核
			conditionMap.put("ProUserCode", String.valueOf(mtTask.getUserId()));
			conditionMap.put("infoType", "2");
			conditionMap.put("mtId", String.valueOf(mtTask.getTaskId()));
			conditionMap.put("RState", "-1");
			conditionMap.put("isComplete", "2");
			List<LfFlowRecord> unflowRecords = baseBiz.getByCondition(
					LfFlowRecord.class, conditionMap, orderByMap);
			LfFlowRecord lastrecord = null;
			Long depId = null;
			String[] recordmsg = new String[2];
			recordmsg[0] = "";
			recordmsg[1] = "";
			String isshow = "2";
			if (unflowRecords != null && unflowRecords.size() > 0) {
				isshow = "1";
				StringBuffer useridstr = new StringBuffer();
				for (LfFlowRecord temp : unflowRecords) {
					useridstr.append(temp.getUserCode()).append(",");
				}
				if (lastrecord == null) {
					lastrecord = unflowRecords.get(0);
				}
				List<LfSysuser> sysuserList = null;
				if (useridstr != null && !"".equals(useridstr.toString())) {
					String str = useridstr.toString().substring(0,
							useridstr.toString().length() - 1);
					conditionMap.clear();
					conditionMap.put("userId&in", str);
					conditionMap.put("corpCode", user.getCorpCode());
					sysuserList = baseBiz.getByCondition(LfSysuser.class,
							conditionMap, null);
					if (sysuserList != null && sysuserList.size() > 0) {
						for (LfSysuser sysuser : sysuserList) {
							firstshowname = firstshowname + sysuser.getName()
									+ "&nbsp;&nbsp;";
							if (depId == null) {
								depId = sysuser.getDepId();
							}
						}
					}
				}
				if (lastrecord != null) {
					// 审核类型 1操作员 4机构 5逐级审核
					Integer rtype = lastrecord.getRType();
					firstcondition = lastrecord.getRCondition() + "";
					ReviewBiz reviewBiz = new ReviewBiz();
					// 当是逐步审批的时候
					if (rtype == 5) {
						// 获取逐级审批
						boolean isLastLevel = new ReviewBiz().checkZhujiIsOver(
								lastrecord.getPreRv().intValue(), lastrecord
										.getProUserCode());
						// 逐级审批的最后一级
						if (isLastLevel) {
							if (lastrecord.getRLevelAmount() != 1) {
								lastrecord.setRLevel(1);
								recordmsg = reviewBiz.getNextFlowRecord(
										lastrecord, user.getCorpCode());
							}
						} else {
							LfDep dep = baseBiz.getById(LfDep.class, depId);
							if (dep != null) {
								LfDep pareDep = baseBiz.getById(LfDep.class,
										dep.getSuperiorId());
								if (pareDep != null) {
									recordmsg[0] = pareDep.getDepName();
									recordmsg[1] = lastrecord.getRCondition()
											+ "";
								}
							}
						}
					} else {
						// 该流程审批的最后一级
						if (lastrecord.getRLevel().intValue() != lastrecord
								.getRLevelAmount().intValue()) {
							recordmsg = reviewBiz.getNextFlowRecord(lastrecord,
									user.getCorpCode());
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
		} catch (Exception e) {
			response.getWriter().print("fail");
			EmpExecutionContext.error(e, "获取彩信任务审批详情出现异常！");
		}
	}

	/**
	 * 根据彩信模板类型其彩信tms文件路径
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTmMsgByBmtype(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		try {
			String tmIdorUrl = request.getParameter("tmUrl");
			String bmtype = request.getParameter("bmtype");
			String tplPath = request.getParameter("tplPath");
			CreateTmsFile mpb = new CreateTmsFile();
			String tmUrl = "";
			if (!"10".equals(bmtype)) {
				tmUrl = tplPath;
			} else {
				tmUrl = tmIdorUrl;
			}

			String isokDownload = "";
			// 判断是否使用集群 以及如果不存在该文件
			if (StaticValue.getISCLUSTER() == 1 && !txtfileutil.checkFile(tmUrl)) {
				CommonBiz commBiz = new CommonBiz();
				// 下载到本地
				if (!"success"
						.equals(commBiz.downloadFileFromFileCenter(tmUrl))) {
					isokDownload = "notmsfile";
				}
			}
			if ("".equals(isokDownload)) {
				if (tmUrl == null || "".equals(tmUrl)) {
					response.getWriter().print("");
					return;
				} else {
					String mms = mpb.getTmsFileInfo(tmUrl);
					if (mms != null) {
						mms = mms.replace("\r\n", "&lt;BR/&gt;");
						mms = mms.replace("\n", "&lt;BR/&gt;");
					}
					response.getWriter().print(mms);
					return;
				}
			} else {
				response.getWriter().print("");
				return;
			}
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取彩信文件信息出现异常！");
		}
	}

	/**
	 * 群发历史页面获取操作员树的方法
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createUserTree2(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			// 设置语言
			String langName = (String) request.getSession().getAttribute(
					StaticValue.LANG_KEY);
			// 加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			Long depId = null;

			String depStr = request.getParameter("depId");
			// String userid = request.getParameter("lguserid");
			if (depStr != null && !"".equals(depStr.trim())) {
				depId = Long.parseLong(depStr);
			}
			// //如果传入登录操作员ID为空或者undefined，则从Session获取。
			// if(userid==null||"".equals(userid.trim())||"undefined".equals(userid.trim())){
			// EmpExecutionContext.error("彩信发送信息查看获取lguserid参数异常！lguserid="+userid+",depStr="+depStr+"。改成从Session获取。");
			// LfSysuser loginSysuser=(LfSysuser)
			// request.getSession(false).getAttribute("loginSysuser");
			// userid=String.valueOf(loginSysuser.getUserId());
			// }
			// 获取当前登录操作员
			LfSysuser currentSysuser = (LfSysuser) request.getSession(false)
					.getAttribute("loginSysuser");
			// 当前登录操作员的userid
			Long userid = currentSysuser.getUserId();
			// 调用公用创建树的方法
			String departmentTree = getDeptUserJosnData(langName, depId,
					userid, currentSysuser);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			response.getWriter().print("");
			EmpExecutionContext.error(e, "彩信任务查询获取操作员机构树出现异常！");
		}
	}

	/**
	 * 操作员树的加载方法
	 * 
	 * @param titlePath
	 * @param depId
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getDeptUserJosnData(String langName, Long depId, Long userid,
			LfSysuser loginSysuser) throws Exception {
		StringBuffer tree = new StringBuffer();
		// 根据userid获取当前操作员信息
		LfSysuser currUser = loginSysuser;
		if (currUser.getPermissionType() == 1) {
			tree = new StringBuffer("[]");
		} else {
			List<LfDep> lfDeps;
			List<LfSysuser> lfSysusers = null;

			DepBiz depBiz = new DepBiz();
			try {
				// 如果企业编码是10000的用户登录
				if (currUser.getCorpCode().equals("100000")) {
					if (depId == null) {
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class,
								conditionMap, orderbyMap);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else {
					if (depId == null) {
						lfDeps = new ArrayList<LfDep>();
						// 查询必须带操作员ID和企业编码
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(
								userid, currUser.getCorpCode()).get(0);
						lfDeps.add(lfDep);
					} else {
						// 查询必须带操作员ID和企业编码
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(
								depId, currUser.getCorpCode());
					}
				}
				// 拼结机构树
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append(
							"'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}

				if (depId != null) {
					// 如果当前登录用户的企业编码是10000
					if (currUser.getCorpCode().equals("100000")) {
						LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
						conMap.put("userId&<>", "1");
						conMap.put("depId", depId.toString());
						lfSysusers = baseBiz.getByCondition(LfSysuser.class,
								conMap, null);
					} else {
						// 获取所有状态操作员信息
						lfSysusers = mtBiz.getAllSysusersOfSmsTaskRecordByDep(
								userid, depId);

					}
				}
				// 拼结操作员信息
				LfSysuser lfSysuser = null;
				if (lfSysusers != null && !lfSysusers.isEmpty()) {
					if (lfDeps != null && lfDeps.size() > 0) {
						tree.append(",");
					}

					for (int i = 0; i < lfSysusers.size(); i++) {
						// 操作员信息
						lfSysuser = lfSysusers.get(i);
						tree.append("{");
						tree.append("id:'u").append(lfSysuser.getUserId())
								.append("'");
						tree.append(",name:'").append(lfSysuser.getName())
								.append("'");
						if (lfSysuser.getUserState() == 2) {
							// tree.append(",name:'").append(lfSysuser.getName()).append("(已注销)'");
							if (StaticValue.ZH_HK.equals(langName)) {
								tree.append(",name:'").append(
										lfSysuser.getName()).append(
										"(Canceled))'");
							} else if (StaticValue.ZH_TW.equals(langName)) {
								tree.append(",name:'").append(
										lfSysuser.getName()).append("(已註銷)'");
							} else {
								tree.append(",name:'").append(
										lfSysuser.getName()).append("(已注销)'");
							}
						} else {
							tree.append(",name:'").append(lfSysuser.getName())
									.append("'");
						}
						tree.append(",pId:").append(lfSysuser.getDepId());
						tree.append(",depId:'").append(
								lfSysuser.getDepId() + "'");
						tree.append(",isParent:").append(false);
						tree.append("}");
						if (i != lfSysusers.size() - 1) {
							tree.append(",");
						}
					}
				}

				tree.append("]");
			} catch (Exception e) {
				EmpExecutionContext.error(e, "操作员树的加载方法出现异常！");
			}
		}
		return tree.toString();
	}

	/**
	 * 操作员树的加载方法
	 * 
	 * @param titlePath
	 * @param depId
	 * @param userid
	 * @return
	 * @throws Exception
	 */
	public String getDeptUserJosnData(Long depId, Long userid) throws Exception {
		StringBuffer tree = null;
		// 根据userid获取当前操作员信息
		LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
		if (currUser.getPermissionType() == 1) {
			tree = new StringBuffer("[]");
		} else {
			List<LfDep> lfDeps;
			List<LfSysuser> lfSysusers = null;

			DepBiz depBiz = new DepBiz();
			try {
				// 如果企业编码是10000的用户登录
				if (currUser.getCorpCode().equals("100000")) {
					if (depId == null) {
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class,
								conditionMap, orderbyMap);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else {
					if (depId == null) {
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}
				}
				// 拼结机构树
				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append(
							"'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append(",nocheck:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}

				if (depId != null) {
					// 如果当前登录用户的企业编码是10000
					if (currUser.getCorpCode().equals("100000")) {
						LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
						conMap.put("userId&<>", "1");
						conMap.put("depId", depId.toString());
						lfSysusers = baseBiz.getByCondition(LfSysuser.class,
								conMap, null);
					} else {
						// 获取所有状态操作员信息
						lfSysusers = mtBiz.getAllSysusersOfSmsTaskRecordByDep(
								userid, depId);

					}
				}
				// 拼结操作员信息
				LfSysuser lfSysuser = null;
				if (lfSysusers != null && !lfSysusers.isEmpty()) {
					if (lfDeps != null && lfDeps.size() > 0) {
						tree.append(",");
					}

					for (int i = 0; i < lfSysusers.size(); i++) {
						// 操作员信息
						lfSysuser = lfSysusers.get(i);
						tree.append("{");
						tree.append("id:'u").append(lfSysuser.getUserId())
								.append("'");
						tree.append(",name:'").append(lfSysuser.getName())
								.append("'");
						if (lfSysuser.getUserState() == 2) {
							tree.append(",name:'").append(lfSysuser.getName())
									.append("(已注销)'");
						} else {
							tree.append(",name:'").append(lfSysuser.getName())
									.append("'");
						}
						tree.append(",pId:").append(lfSysuser.getDepId());
						tree.append(",depId:'").append(
								lfSysuser.getDepId() + "'");
						tree.append(",isParent:").append(false);
						tree.append("}");
						if (i != lfSysusers.size() - 1) {
							tree.append(",");
						}
					}
				}

				tree.append("]");
			} catch (Exception e) {
				EmpExecutionContext.error(e, "操作员树的加载方法出现异常！");
			}
		}
		if(tree==null){
			return "[]";
		}
		return tree.toString();
	}

	/**
	 * 输出机构代码数据
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void createDeptTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			// 加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			Long depId = null;
			Long userid = null;
			// 部门iD
			String depStr = request.getParameter("depId");
			// 操作员账号
			// String userStr = request.getParameter("lguserid");
			if (depStr != null && !"".equals(depStr.trim())) {
				depId = Long.parseLong(depStr);
			}
			// if(userStr != null &&
			// !"".equals(userStr.trim())&&!"undefined".equals(userStr.trim())){
			// userid = Long.parseLong(userStr.trim());
			// }
			// else{
			// EmpExecutionContext.error("彩信发送信息查看获取lguserid参数异常！lguserid="+userStr+",depStr="+depStr+"。改成从Session获取。");
			// //没有取到登录操作员USERID，就从Session中获取。
			// userid=((LfSysuser)
			// request.getSession(false).getAttribute("loginSysuser")).getUserId();
			// }
			// 从session中获取登录操作员
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
					.getAttribute("loginSysuser");
			// 从session中获取登录操作员ID
			userid = loginSysuser.getUserId();

			String departmentTree = this.getDepartmentJosnData(depId, userid,
					loginSysuser);
			response.getWriter().print(departmentTree);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取操作员机构出现异常！");
		}
	}

	/**
	 * 异步加载机构的主方法
	 * 
	 * @param depId
	 * @param userid
	 * @return
	 */
	private String getDepartmentJosnData(Long depId, Long userid) {

		StringBuffer tree = null;
		try {
			// 当前登录操作员
			LfSysuser curUser = baseBiz.getLfSysuserByUserId(userid);
			// 判断是否个人权限
			if (curUser.getPermissionType() == 1) {
				tree = new StringBuffer("[]");
			} else {
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if (curUser.getCorpCode().equals("100000")) {
					if (depId == null) {
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class,
								conditionMap, orderbyMap);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else {
					if (depId == null) {
						lfDeps = new ArrayList<LfDep>();
						LfDep lfDep = depBiz.getAllDeps(userid).get(0);
						lfDeps.add(lfDep);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append(
							"'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "彩信任务查询异步加载机构出现异常！");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}

	/**
	 * 异步加载机构的主方法(重载方法)
	 * 
	 * @param depId
	 *            页面传过来的机构ID
	 * @param userid
	 *            页面传过来的登录操作员ID
	 * @param loginSysuser
	 *            当前登录操作员对象
	 * @return
	 */
	private String getDepartmentJosnData(Long depId, Long userid,
			LfSysuser loginSysuser) {

		StringBuffer tree = null;
		try {
			// 当前登录操作员
			LfSysuser curUser = loginSysuser;
			// 判断是否个人权限
			if (curUser.getPermissionType() == 1) {
				tree = new StringBuffer("[]");
			} else {
				// 机构biz
				DepBiz depBiz = new DepBiz();
				List<LfDep> lfDeps;

				lfDeps = null;

				if (curUser.getCorpCode().equals("100000")) {
					if (depId == null) {
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
						// 只查询顶级机构
						conditionMap.put("superiorId", "0");
						// 查询未删除的机构
						conditionMap.put("depState", "1");
						// 排序
						orderbyMap.put("depId", StaticValue.ASC);
						orderbyMap.put("deppath", StaticValue.ASC);
						lfDeps = baseBiz.getByCondition(LfDep.class,
								conditionMap, orderbyMap);
					} else {
						lfDeps = new DepBiz().getDepsByDepSuperId(depId);
					}

				} else {
					if (depId == null) {
						lfDeps = new ArrayList<LfDep>();
						// 查询必须带操作员ID和企业编码
						LfDep lfDep = depBiz.getAllDepByUserIdAndCorpCode(
								userid, curUser.getCorpCode()).get(0);
						lfDeps.add(lfDep);
					} else {
						// 查询必须带操作员ID和企业编码
						lfDeps = new DepBiz().getDepsByDepSuperIdAndCorpCode(
								depId, curUser.getCorpCode());
					}
				}

				LfDep lfDep = null;
				tree = new StringBuffer("[");
				for (int i = 0; i < lfDeps.size(); i++) {
					lfDep = lfDeps.get(i);
					tree.append("{");
					tree.append("id:").append(lfDep.getDepId());
					tree.append(",name:'").append(lfDep.getDepName()).append(
							"'");
					tree.append(",pId:").append(lfDep.getSuperiorId());
					tree.append(",depId:'").append(lfDep.getDepId() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if (i != lfDeps.size() - 1) {
						tree.append(",");
					}
				}
				tree.append("]");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "彩信任务查询异步加载机构出现异常！");
			tree = new StringBuffer("[]");
		}
		return tree.toString();
	}

	/**
	 * 检测文件是否存在
	 * 
	 * @param request
	 * @param response
	 */
	public void goToFile(HttpServletRequest request,
			HttpServletResponse response) {
		String url = request.getParameter("url");
		TxtFileUtil tfu = new TxtFileUtil();
		try {
			response.getWriter().print(tfu.checkFile(url));
		} catch (Exception e) {
			// 异常处理
			EmpExecutionContext.error(e, "彩信任务检测文件是否存在出现异常！");
		}
	}

	/**
	 * @description 记录操作成功日志
	 * @param request
	 * @param modName
	 *            模块名称
	 * @param opContent
	 *            操作详情
	 * @param opType
	 *            操作类型 ADD UPDATE DELETE GET OTHER
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request, String modName,
			String opContent, String opType) {
		LfSysuser lfSysuser = null;
		Object obj = request.getSession(false).getAttribute("loginSysuser");
		if (obj == null)
			return;
		lfSysuser = (LfSysuser) obj;
		EmpExecutionContext.info(modName, lfSysuser.getCorpCode(), String
				.valueOf(lfSysuser.getUserId()), lfSysuser.getUserName(),
				opContent, opType);
	}

}
