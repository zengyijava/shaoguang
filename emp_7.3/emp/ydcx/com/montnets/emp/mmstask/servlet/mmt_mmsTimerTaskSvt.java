package com.montnets.emp.mmstask.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.conn.HttpHostConnectException;
import org.json.simple.JSONObject;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.MttaskBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.vo.LfMttaskVo2;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.mmstask.biz.MmsTaskBiz;
import com.montnets.emp.servmodule.ydcx.constant.ServerInof;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 彩信定时信息查看 mms_mmsSendBoxServlet
 * 
 * @author Administrator
 * 
 */

@SuppressWarnings("serial")
public class mmt_mmsTimerTaskSvt extends BaseServlet {

	// SuperOpLog spLog = new SuperOpLog();
	// 操作模快
	// String opModule = StaticValue.MMS_BOX;
	// 操作者
	// String opSper = StaticValue.OPSPER;
	// 操作类型
	// String opType = StaticValue.OTHER;
	// 操作内容
	// String opContent = "";
	/*
	 * MsgReviewBiz mrb = new MsgReviewBiz();
	 */
	private final MmsTaskBiz mtBiz = new MmsTaskBiz();
	private final BaseBiz baseBiz = new BaseBiz();
	// private TxtFileUtil tfu = new TxtFileUtil();
	// private SysuserBiz SysuserBiz = new SysuserBiz();
	private final String empRoot = "ydcx";
	private final String basePath = "/mmstask";

	/**
	 * @param request
	 * @param response
	 */
	// 进入查询页面
	public void find(HttpServletRequest request, HttpServletResponse response) {

		String lgguid = "";
		Long userGuid = null;
		String corpCode = "";
		Long userId = null;
		LfSysuser sysUser = null;
		try {
			// 获取当前操作员的GUID
			lgguid = request.getParameter("lgguid");
			LfSysuser loginSysuser = (LfSysuser) request.getSession(false)
					.getAttribute("loginSysuser");
			lgguid = String.valueOf(loginSysuser.getUserId());

			userGuid = Long.valueOf(lgguid);
			// 获取操作员对象
			// sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
			// 企业编码
			// corpCode = sysUser.getCorpCode();
			// 用户ID
			// userId = sysUser.getUserId();
			// 登录操作员信息

			// 当前登录用户的企业编码
			corpCode = loginSysuser.getCorpCode();
			userId = loginSysuser.getUserId();
		} catch (Exception e) {
			// 进入异常
			EmpExecutionContext.error(e, "彩信定时任务查询获取当前操作员出现异常！");
		}
		PageInfo pageInfo = new PageInfo();
		// 日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time = System.currentTimeMillis();
		try {
			// 设置条件的MAP
			// 分页信息

			boolean isFirstEnter = pageSet(pageInfo, request);
			LfMttaskVo2 mtVo = new LfMttaskVo2();
			String depid = request.getParameter("depid");
			depid = (depid != null && depid.length() > 0) ? depid : "";
			String userid = request.getParameter("userid");
			userid = (userid != null && userid.length() > 0 && !userid
					.equals("请选择")) ? userid.substring(0, userid
					.lastIndexOf(",")) : "";

			// 是否包含子机构
			String isContainsSun = request.getParameter("isContainsSun");
			if (isContainsSun != null && !"".equals(isContainsSun)) {
				mtVo.setIsContainsSun(isContainsSun);
			}

			if (!isFirstEnter) {
				mtVo.setDepIds(depid);
				mtVo.setUserIds(userid);
				// 标题
				String taskName = request.getParameter("taskName");
				if (taskName != null && !"".equals(taskName)) {
					mtVo.setTaskName(taskName);
				}
				// 主题
				String theme = request.getParameter("theme");
				if (theme != null && !"".equals(theme)) {
					mtVo.setTitle(theme);
				}
				// 设置条件
				mtVo.setEndSubmitTime(request.getParameter("submitEndTime"));
				mtVo.setStartSubmitTime(request.getParameter("submitSartTime"));

				if (isContainsSun == null || "".equals(isContainsSun)) {
					request.setAttribute("isContainsSun", "0");
				} else {
					request.setAttribute("isContainsSun", "1");
				}
			}
			// 网关状态
			if (request.getParameter("sstate") != null
					&& !"".equals(request.getParameter("sstate"))) {
				if ("3".equals(request.getParameter("sstate"))) {
					mtVo.setSendstate(0);
					mtVo.setSubState(3);
				} else {
					mtVo.setSendstate(Integer.parseInt(request
							.getParameter("sstate")));
					// conditionMap.put("subState", "2");
					if ("0".equals(request.getParameter("sstate"))) {
						mtVo.setSubState(2);
					}
				}
				request.setAttribute("sstate", request.getParameter("sstate"));
			}
			if (userId - 1 != 0) {
				mtVo.setUserId(userId);
			}
			// 设置条件
			mtVo.setMsType(2);
			mtVo.setTimerStatus(1);
			// conditionMap.put("subState&in", "2");
			// conditionMap.put("sendstate","0");
			// 设置序列
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("submitTime", StaticValue.DESC);
			// 查询符合条件的彩信任务
			List<LfMttaskVo2> mtVoList = null;
			try {
				// mtList = baseBiz.getByCondition(LfMttask.class, null,
				// conditionMap, orderbyMap, pageInfo);
				if (corpCode != null && corpCode.equals("100000")) {
					mtVoList = mtBiz.getMmsLfMttaskVoWithoutDomination(mtVo,
							pageInfo);
				} else {
					mtVoList = mtBiz.getMmsLfMttaskVo(userId, mtVo, pageInfo);
				}
			} catch (Exception e) {
				// 异常
				EmpExecutionContext.error(e, "查询彩信定时任务出现异常！");
			}
			// 设置回添参数
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
				opSucLog(request, "移动彩讯-定时信息查看", opContent, "GET");
			}
		} catch (Exception e) {
			// 异常
			EmpExecutionContext.error(e, "查询彩信定时任务出现异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
		} finally {
			try {
				request.getRequestDispatcher(
						empRoot + basePath + "/mmt_mmsTimerTask.jsp").forward(
						request, response);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "彩信定时任务查询页面跳转失败！");
			}
		}
	}

	/**
	 * 彩信发送
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void sendMMS(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		TxtFileUtil tfu = new TxtFileUtil();
		// 任务ID
		String mtId = request.getParameter("mtId");
		// 号码文件URL
		String mobileUrl = request.getParameter("numUrl");
		// 信息
		String msgUrl = request.getParameter("msgUrl");
		// 提交状态
		String subState = request.getParameter("subState");
		// 标题
		String taskName = request.getParameter("taskName");
		String opContent = "";
		opContent = "提交彩信任务（彩信任务名称：" + taskName + "）";

		String result = null;
		try {
			Integer reState = Integer.valueOf(request.getParameter("reState"));
			MttaskBiz mttaskBiz = new MttaskBiz();

			if (subState.equals("2")) {
				// 检查文件是否存在
				if (tfu.checkFile(mobileUrl) && tfu.checkFile(msgUrl)) {
					if (reState - 0 == 0) {
						result = mttaskBiz.sendMms(Long.parseLong(mtId));
						if (result.equals("true")) {
							if (mtBiz.changeState(Long.valueOf(mtId),
									"sendstate", subState)) {
								result = "success";
							}
						}
					} else {
						if (mtBiz.changeState(Long.valueOf(mtId), null,
								subState)) {
							result = "subOk";
						} else {
							result = "error";
						}
					}
				} else {
					result = "errorFile";
				}
			} else {
				if (mtBiz.changeState(Long.valueOf(mtId), null, subState)) {
					result = "cancelOk";
				} else {
					result = "error";
				}
			}
		} catch (Exception e) {
			// 异常处理
//			if (e.getClass().getName().equals(
//					"org.apache.http.conn.HttpHostConnectException")) {
			if(e.getClass().isAssignableFrom(HttpHostConnectException.class)){
				result = e.getLocalizedMessage();
			} else {
				result = "error";
			}
			EmpExecutionContext.error(e, "彩信发送出现异常！");
		} finally {
			response.getWriter().print(result);
		}
	}

	/**
	 * @param request
	 * @param response
	 */
	// 检测文件
	public void checkFiles(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TxtFileUtil tfu = new TxtFileUtil();
			// 获取文件路径
			String fileUrl = request.getParameter("url");
			// 检测文件是否存在
			boolean r = tfu.checkFile(fileUrl);
			if (r) {
				response.getWriter().print("true");
			}
		} catch (Exception e) {
			// 异常
			EmpExecutionContext.error(e, "彩信检测文件存在出现异常！");
		}
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	// 取消彩信发送
	public void doCancel(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 设置语言
		String langName = (String) request.getSession().getAttribute(
				StaticValue.LANG_KEY);
		String result = "";
		try {
			// 获取其彩信任务ID
			String mtId = request.getParameter("mtId");
			if (mtId != null && !"".equals(mtId)) {

				String re = mtBiz.submitMmsLfMttask(mtId, 3);
				if ("success".equals(re)) {
					result = "操作成功！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Successful operation!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "操作成功！";
					} else {
						result = "操作成功！";
					}

				} else if ("000".equals(re)) {
					// result = "审批成功及发送到网关成功！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Approved successfully and sent to the gateway success!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "審批成功及發送到網關成功！";
					} else {
						result = "审批成功及发送到网关成功！";
					}
				} else if ("timerSuccess".equals(re)) {
					// result = "审批成功及定时任务添加成功！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Successful approval and timing tasks to add success!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "審批成功及定時任務添加成功！";
					} else {
						result = "审批成功及定时任务添加成功！";
					}
				} else if ("timerFail".equals(re)) {
					// result = "审批成功但定时任务失败！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Approval successful but the timing of the task failed!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "審批成功但定時任務失敗！";
					} else {
						result = "审批成功但定时任务失败！";
					}
				} else if ("cancelSuccess".equals(re)) {
					// result = "撤销成功！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Revoked successfully!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "撤銷成功！";
					} else {
						result = "撤销成功！";
					}
					LfMttask mt = baseBiz.getById(LfMttask.class, mtId);
					new ReviewBiz().updateFlowRecordByMtId(mt.getTaskId()
							.toString());

				} else if ("cancelFail".equals(re)) {
					// result = "撤销失败！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Undo failed!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "撤銷失敗！";
					} else {
						result = "撤销失败！";
					}

				} else {
					// result = "审批成功但发送到网关失败！";

					if (StaticValue.ZH_HK.equals(langName)) {
						result = "Approval successful but failed to send to the gateway!";
					} else if (StaticValue.ZH_TW.equals(langName)) {
						result = "審批成功但發送到網關失敗！";
					} else {
						result = "审批成功但发送到网关失败！";
					}
				}
			}
		} catch (Exception e) {
			// 异常
			EmpExecutionContext.error(e, "取消彩信发送出现异常！");
		} finally {
			response.getWriter().print(result);
		}
	}

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	// 创建彩信任务
	public void createMms(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String result = null;
		try {
			// 彩信任务ID
			String mtId = request.getParameter("mid");
			if (mtId != null && !"".equals(mtId)) {
				String re = mtBiz.submitMmsLfMttask(mtId, 2);
				if ("success".equals(re)) {
					result = "创建成功！";
				} else if ("000".equals(re)) {
					result = "创建彩信任务及发送到网关成功！";
				} else if ("timerSuccess".equals(re)) {
					result = "创建彩信任务及定时任务添加成功！";
				} else if ("timerFail".equals(re)) {
					result = "创建定时任务失败，取消任务创建！";
				} else {
					result = re;
				}
			}
		} catch (Exception e) {
			// 异常
			EmpExecutionContext.error(e, "创建彩信任务出现异常！");
		} finally {
			response.getWriter().print(result);
		}
	}

	/**
	 * 通过彩信任务id ，获取彩信的相关信息
	 * 
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	public void getMmsDetail(HttpServletRequest request,
			HttpServletResponse response) {
		// 彩信任务ID
		String mtId = request.getParameter("mtId");
		try {
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
			// 审批状态
			jsonObject.put("mmsrestate", String.valueOf(mtTask.getReState()));
			response.getWriter().print(jsonObject.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取彩信任务详情出现异常！");
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