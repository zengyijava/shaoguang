/**
 * 
 */
package com.montnets.emp.perfect.servlet;

import com.montnets.emp.common.atom.AddrBookAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.perfect.LfPerfectNoticUp;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.perfect.biz.PrefectNoticeBiz;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.TxtFileUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 完美通知发送
 * 
 * @author Administrator
 */

public class per_sendNotSvt extends BaseServlet
{
	private static final long			serialVersionUID	= 2843744480169688217L;

	private static GlobalVariableBiz	globalBiz			= GlobalVariableBiz.getInstance();

	private final SmsBiz						smsBiz				= new SmsBiz();

	private final PrefectNoticeBiz			prefectNoticeBiz	= new PrefectNoticeBiz();

	private static final Integer		SIZE				= 50;

	private final String						empRoot				= "dxzs";

	private final BaseBiz						baseBiz				= new BaseBiz();

	private final String						basePath			= "/perfect";

	private final WgMsgConfigBiz				wgMsgConfigBiz		= new WgMsgConfigBiz();

	private final TxtFileUtil					txtfileutil			= new TxtFileUtil();


	private final AddrBookAtom				addrAtom			= new AddrBookAtom();

	private final PhoneUtil					phoneUtil			= new PhoneUtil();

	private final KeyWordAtom					keyWordAtom			= new KeyWordAtom();

	private final SubnoManagerBiz				subnoManagerBiz		= new SubnoManagerBiz();

	/**
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	// 进入完美通知发送界面
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		String lgguid = "";
		Long userGuid = null;
		String corpCode = "";
		LfSysuser sysUser = null;
		// 机构级别
		Integer depLevel = 0;
		// 短信条数
		Long smsCount = 0L;
		// 初始化不计费
		boolean flag = false;
		String jiFei = "2"; // 不计费
		try
		{
			//加请求日志
			EmpExecutionContext.logRequestUrl(request, null);
			// 获取GUID
			lgguid = request.getParameter("lgguid");
			if(lgguid==null||"".equals(lgguid.trim())||"undefined".equals(lgguid.trim())){
				EmpExecutionContext.error("完美通知跳转到发送界面获取lgguid异常！lgguid="+lgguid+"。改成从Session获取。");
				LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				lgguid=String.valueOf(loginSysuser.getGuId());
			}else{
				lgguid=lgguid.trim();
			}
			userGuid = Long.valueOf(lgguid);
			// 查询出登录对象
			sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
			// 获取企业编码
			corpCode = sysUser.getCorpCode();
			// 是否计费
			flag = SystemGlobals.isDepBilling(corpCode);
			if(flag)
			{
				// 计费
				jiFei = "1";
				BalanceLogBiz logBiz = new BalanceLogBiz();
				// 获取短信条数
				smsCount = logBiz.getAllowSmsAmountByGuid(userGuid);
				if(smsCount == null)
				{
					smsCount = 0L;
				}
			}
			// 获取发送账号列表
			List<Userdata> userData = smsBiz.getSpUserList(sysUser);
//			UserdataAtom userdataAtom = new UserdataAtom();
//			// 发送账户存放内存Map
//			userdataAtom.setUserdata(userData);
			conditionMap.clear();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode&in", "0," + corpCode);
			orderByMap.put("corpCode", "asc");
			
			//设置启用查询条件
			conditionMap.put("state", "0");
			//设置查询手动和手动+触发
			conditionMap.put("busType&in", "0,2");
			
			// 获取业务类型列表
			List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderByMap);
			Long taskId = globalBiz.getValueByKey("taskId", 1L);
			//操作日志信息
			String opContent = "获取taskid("+taskId+")成功";
			EmpExecutionContext.info("完美通知发送", sysUser.getCorpCode(), String.valueOf(sysUser.getUserId()), sysUser.getUserName(), opContent.toString(), "OTHER");
			request.setAttribute("busList", busList);
			request.setAttribute("depLevel", depLevel);
			request.setAttribute("smsCount", smsCount);
			request.setAttribute("jiFei", jiFei);
			request.setAttribute("userData", userData);
			request.setAttribute("name", sysUser.getName());
			request.setAttribute("lgcorpcode", sysUser.getCorpCode());
			request.setAttribute("lguserid", String.valueOf(sysUser.getUserId()));
			request.setAttribute("perfectTaskId", String.valueOf(taskId));
			
			//获取高级设置默认信息
			conditionMap.clear();
			conditionMap.put("userid", String.valueOf(sysUser.getUserId()));
			//3:完美通知发送
			conditionMap.put("flag", "3");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", StaticValue.DESC);
			List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
			LfDfadvanced lfDfadvanced = null;
			if(lfDfadvancedList != null && lfDfadvancedList.size() > 0)
			{
				lfDfadvanced = lfDfadvancedList.get(0);
			}
			request.setAttribute("lfDfadvanced", lfDfadvanced);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知发送界面跳转失败！");
		}
		finally
		{
			try
			{
				request.getRequestDispatcher(empRoot + basePath + "/per_sendPreNot.jsp").forward(request, response);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "完美通知发送界面跳转失败！");
			}

		}
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// 获取完美通知的历史
	@SuppressWarnings("unchecked")
	public void getPerNoticHistroy(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		// 接收者ID
		Long receiveId = Long.valueOf(request.getParameter("receiveId"));
		// 接收类型
		Integer type = Integer.valueOf(request.getParameter("type"));
		// 分页信息
		Integer pageIndex = Integer.valueOf(request.getParameter("pageIndex"));
		// 登录者GUID
		Long senderGuId = lfSysuser.getGuId();
		PrefectNoticeBiz noticeBiz = new PrefectNoticeBiz();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		pageInfo.setPageIndex(pageIndex);
		pageInfo.setPageSize(10);

		List<LfPerfectNotic> perfectNotics = null;
		List<LfPerfectNoticUp> noticUps = null;
		LfPerfectNotic notic = null;
		LfPerfectNoticUp up = null;
		JSONObject json = new JSONObject();
		// 获取完美通知信息
		try
		{
			perfectNotics = noticeBiz.getPreNoticeHistroy(senderGuId, type, receiveId, pageInfo);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知发送记录失败！");
		}
		// 对查询出信息进行JSON格式化
		if(perfectNotics != null)
		{
			JSONArray members = new JSONArray();
			for (int i = 0; i < perfectNotics.size(); i++)
			{
				notic = perfectNotics.get(i);
				String receiveType = notic.getRecevierType().toString();

				JSONObject member = new JSONObject();
				member.put("noticeId", notic.getNoticId().toString());
				member.put("receiverId", notic.getRecevierId().toString());
				member.put("receiverType", receiveType);
				member.put("sendDate", simpleDateFormat.format(notic.getSubmitTime()).toString());
				String sign = String.format(DxzsStaticValue.getPERFECT_SIGN_TIMER(), notic.getArySendCount()).replaceAll("完美通知", MessageUtils.extractMessage("dxzs","dxzs_PERFECT_SIGN",request)).replace("次", MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_115",request));
				member.put("content", sign + notic.getContent());
				member.put("noticeCount", notic.getNoticCount());
				Integer replyCount = 0;
				Integer receiveCount = 0;
				noticUps = noticeBiz.getPnUpByNoticIdAndUserId(notic.getNoticId());
				for (int j = 0; j < noticUps.size(); j++)
				{
					up = noticUps.get(j);
					if(up.getIsReceive() == 0)
					{
						receiveCount++;
					}
					if(up.getIsReply() == 0)
					{
						replyCount++;
					}
				}
				member.put("replyCount", String.valueOf(replyCount));
				member.put("receiveCount", String.valueOf(receiveCount));
				members.add(member);
			}
			json.put("jobs", members);
			json.put("count", pageInfo.getTotalRec());
			json.put("pageSize", pageInfo.getPageSize());
			// System.out.println(json.toString());
		}
		response.getWriter().write(json.toString());
	}

	/**
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	// 获取完美通知详情记录
	@SuppressWarnings("unchecked")
	public void getSinglePerNoticDetail(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		response.setContentType("text/html;charset=UTF-8");
		// 完美通知主表ID
		Long noticeId = Long.valueOf(request.getParameter("noticeId"));
		// 分页信息
		Integer pageIndex = Integer.valueOf(request.getParameter("pageIndex"));
		// 查询的名字
		String username = request.getParameter("username").trim();
		// 查询的手机号码
		String phone = request.getParameter("phone").trim();
		// 查询上行回复内容
		String remsg = request.getParameter("remsg").trim();
		// 每页大小
		Integer initPageSize = Integer.valueOf(request.getParameter("initPageSize"));
		// 回复状态
		String isReAttr = request.getParameter("isReAttr").trim();
		// 回执状态
		String isGeAttr = request.getParameter("isGeAttr").trim();
		// 已发次数
		String sendCount = request.getParameter("reCount").trim();

		LfPerfectNotic perfectNotic = baseBiz.getById(LfPerfectNotic.class, noticeId);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		request.getSession(false).setAttribute("pre_username", username);
		request.getSession(false).setAttribute("pre_phone", phone);
		request.getSession(false).setAttribute("pre_noticeId", noticeId);

		request.getSession(false).setAttribute("remsg", remsg);
		request.getSession(false).setAttribute("isReAttr", isReAttr);
		request.getSession(false).setAttribute("isGeAttr", isGeAttr);
		request.getSession(false).setAttribute("sendCount", sendCount);

		List<LfPerfectNoticUp> noticUps = null;
		LfPerfectNoticUp up = null;
		JSONObject json = new JSONObject();
		PageInfo pageInfo = new PageInfo();
		pageSet(pageInfo, request);
		pageInfo.setPageIndex(pageIndex);
		pageInfo.setPageSize(initPageSize);
		LfSysuser sysuser = null;
		String spUser = "";
		try
		{
			noticUps = prefectNoticeBiz.getPerfectNoticeUpByNoticeId(username, phone, remsg, noticeId, isReAttr, isGeAttr, sendCount, pageInfo);
			// noticUps =
			// prefectNoticeBiz.getPerfectNoticeUpByNoticeId(username,phone,noticeId,
			// pageInfo);
			// 获取详情信息进行JSON格式化
			if(noticUps != null && noticUps.size() > 0)
			{
				if(noticUps.get(0) != null)
				{
					Long senderGuid = noticUps.get(0).getSenderGuid();
					sysuser = baseBiz.getByGuId(LfSysuser.class, senderGuid);
				}
				JSONArray members = new JSONArray();
				for (int i = 0; i < noticUps.size(); i++)
				{
					String name = "";
					String moblie = "";
					String time = "";
					String receiveMsg = "";
					up = noticUps.get(i);
					JSONObject member = new JSONObject();
					member.put("pnupid", up.getPnupId());
					name = up.getName();
					if(name != null && !"".equals(name))
					{
						member.put("receiveName", name);
					}
					else
					{
						member.put("receiveName", "-");
					}
					moblie = up.getMobile();
					if(moblie != null && !"".equals(moblie))
					{
						member.put("moblie", moblie);
					}
					else
					{
						member.put("moblie", "-");
					}
					Timestamp receiveTime = up.getSendTime();
					if(receiveTime != null)
					{
						time = simpleDateFormat.format(up.getSendTime()).toString();
					}
					member.put("sendDate", time);
					receiveMsg = up.getContent();
					if(receiveMsg == null || "".equals(receiveMsg))
					{
						receiveMsg = "";
					}
					member.put("content", receiveMsg);

					if(up.getIsReply() == 2)
					{
						member.put("receiveCount", "-");
						// 标识该条完美通知是否可以再发送 1是可以 2是不可以
						member.put("isValid", 2);
					}
					else
					{
						member.put("receiveCount", up.getReceiveCount());
						// 标识该条完美通知是否可以再发送
						member.put("isValid", 1);
						if(spUser == null || "".equals(spUser))
						{
							spUser = up.getSpUser();
						}
					}
					if(up.getIsReply() == 0)
					{
						/*已回复*/
						member.put("isReply", MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_122",request));
					}
					else
						if(up.getIsReply() == 1)
						{
							/*未回复*/
							member.put("isReply", " <font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_123",request)+"</font>");
						}
						else
						{
							member.put("isReply", "-");
						}
					if(up.getIsReceive() == 0)
					{
						/*已接收*/
						member.put("isReceive", MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_125",request));
					}
					else
						if(up.getIsReceive() == 1)
						{
							/*未接收*/
							member.put("isReceive", "<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_126",request)+"</font>");
						}
						else
							if(up.getIsReceive() == 2)
							{
								/*未发送成功*/
								member.put("isReceive", "<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_127",request)+"</font>");
							}
							else
								if(up.getIsReceive() == 3)
								{
									/*手机号异常*/
									member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_1",request)+"</font>");
								}
								else
									if(up.getIsReceive() == 4)
									{
										/*手机号为空*/
										member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_2",request)+"</font>");
									}
									else
										if(up.getIsReceive() == 6)
										{
											/*短信余额不足*/
											member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_3",request)+"</font>");
										}
										else
											if(up.getIsReceive() == 7)
											{
												/*短信扣费失败*/
												member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_4",request)+"</font>");
											}
											else
												if(up.getIsReceive() == 8)
												{
													/*运营商不符合*/
													member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_5",request)+"</font>");
												}
												else
													if(up.getIsReceive() == 9)
													{
														/*所属机构未充值*/
														member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_6",request)+"</font>");
													}
													else
														if(up.getIsReceive() == -2)
														{
															/*号码重复*/
															member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_7",request)+"</font>");
														}
														else
															if(up.getIsReceive() == -1)
															{
																/*已接收*/
																member.put("isReceive", MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_125",request));
															}
															else
																if(up.getIsReceive() == 5)
																{
																	/*黑名单*/
																	member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_8",request)+"</font>");
																}
																else
																	if(up.getIsReceive() == -4)
																	{
																		/*重复人员*/
																		member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_9",request)+"</font>");
																	}
																	else
																	{
																		/*出现异常*/
																		member.put("isReceive", "<font color='blue'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_10",request)+"</font>");
																	}
					members.add(member);
				}
				json.put("jobs", members);
				// 查询出记录
				json.put("flag", 1);
			}
			else
			{
				// 未查询出记录
				json.put("flag", 2);
			}
			// 索引页
			json.put("index", pageInfo.getPageIndex());
			// 总记录数
			json.put("count", pageInfo.getTotalRec());
			// 页面大小
			json.put("pageSize", pageInfo.getPageSize());
			// 完美通知ID
			json.put("preNoticeId", String.valueOf(noticeId));
			// 最大发送次数
			json.put("maxSendCount", perfectNotic.getMaxSendCount());
			// 间隔时间
			json.put("sendInterval", perfectNotic.getSendInterval().toString());
			// 发送内容
			json.put("sendContent", perfectNotic.getContent());
			// 发送次数
			json.put("arySendCount", perfectNotic.getArySendCount());
			// 发送人名字
			json.put("senderName", sysuser != null ? sysuser.getName() : "1次");
			json.put("spUser", spUser);
			response.getWriter().write(json.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知记录详情失败！");
		}
	}

	/**
	 * ---------------------------------完美通知文件发送--------------------------------
	 * -----------------
	 */

	/**
	 * 查询完美通知列表中的 操作员树以及 操作员
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void findLfDepUser(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		StringBuffer tree = new StringBuffer("[");

		try
		{
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			// 操作员机构树
			String depId = request.getParameter("depId");
			String corpCode = lfSysuser.getCorpCode();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			boolean flag = false;
			// 查询的是顶级机构以及其子级
			if(depId == null || "".equals(depId))
			{
				// 判断是没有删除的机构
				conditionMap.put("depState", "1");
				conditionMap.put("corpCode", corpCode);
				// 顶级机构
				conditionMap.put("depLevel", "1");
				List<LfDep> deps = baseBiz.getByCondition(LfDep.class, conditionMap, null);
				LfDep dep = null;
				if(deps != null && deps.size() > 0)
				{
					dep = deps.get(0);
					depId = String.valueOf(dep.getDepId());
					tree.append("{");
					tree.append("id:'d").append(depId).append("'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'0'");
					tree.append(",isParent:").append(true);
					tree.append("}");
				}
				conditionMap.clear();
				flag = true;
			}
			// 判断是没有删除的机构
			conditionMap.put("depState", "1");
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("superiorId", depId);
			orderByMap.put("depLevel", StaticValue.ASC);
			List<LfDep> sonDeps = baseBiz.getByCondition(LfDep.class, conditionMap, orderByMap);
			if(sonDeps != null && sonDeps.size() > 0)
			{
				if(flag)
				{
					tree.append(",");
				}
				LfDep lfDep = null;
				for (int i = 0; i < sonDeps.size(); i++)
				{
					lfDep = sonDeps.get(i);
					tree.append("{");
					tree.append("id:'d").append(lfDep.getDepId()).append("'");
					tree.append(",name:'").append(lfDep.getDepName()).append("'");
					tree.append(",pId:''d").append(lfDep.getSuperiorId()).append("'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(sonDeps.size() - i != 1)
					{
						tree.append(",");
					}
				}
			}
			conditionMap.clear();
			// 过滤无效用户
			conditionMap.put("userId& not in", "1");
			// 过滤admin
			conditionMap.put("userName& not in", "admin");
			// 所在机构ID
			conditionMap.put("depId", depId);
			// 有效果用户
			conditionMap.put("userState", "1");
			// 企业编码
			conditionMap.put("corpCode", corpCode);
			List<LfSysuser> userList = baseBiz.getByCondition(LfSysuser.class, conditionMap, null);
			if(userList != null && userList.size() > 0)
			{
				tree.append(",");
				LfSysuser user = null;
				for (int k = 0; k < userList.size(); k++)
				{
					user = userList.get(k);
					tree.append("{");
					tree.append("id:'").append(user.getGuId()).append("'");
					tree.append(",name:'").append(user.getName()).append("'");
					tree.append(",pId:'d").append(depId).append("'");
					tree.append(",isParent:").append(false);
					tree.append("}");
					if(userList.size() - k != 1)
					{
						tree.append(",");
					}
				}
			}
			tree.append("]");
			response.getWriter().print(tree.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取完美通知操作员机构树失败！");
			response.getWriter().print(tree.toString());
		}
	}

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~处理选择对象中的操作begin
	 */

	/**
	 * 查询员工 机构下的员工，不包含
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getEmployeeByDepId(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
			StringBuffer sb = new StringBuffer();
			String depId = request.getParameter("depId");
			String pageIndex = request.getParameter("pageIndex");
			String searchname = request.getParameter("searchname");
			String lgcorpcode = request.getParameter("lgcorpcode");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));

			conditionMap.put("corpCode", lgcorpcode);
			List<LfEmployeeDep> employeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
			LinkedHashMap<Long, String> depnamemap = new LinkedHashMap<Long, String>();
			if(employeeDepList != null && employeeDepList.size() > 0)
			{
				for (LfEmployeeDep dep : employeeDepList)
				{
					depnamemap.put(dep.getDepId(), dep.getDepName());
				}
			}
			conditionMap.clear();
			if(depId != null && !"".equals(depId))
			{
				conditionMap.put("depId", depId);
				orderByMap.put("employeeId", StaticValue.ASC);
			}
			conditionMap.put("corpCode", lgcorpcode);
			if(searchname != null && !"".equals(searchname))
			{
				conditionMap.put("name&like", searchname);
				orderByMap.put("depId", StaticValue.ASC);
			}
			List<LfEmployee> lfEmployeeList = baseBiz.getByCondition(LfEmployee.class, null, conditionMap, orderByMap, pageInfo);
			if(lfEmployeeList != null && lfEmployeeList.size() > 0)
			{
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (LfEmployee user : lfEmployeeList)
				{
					sb.append("<option value='").append(user.getGuId()).append("' isdep='4' et='' moblie='" + user.getMobile() + "'>");
					sb.append(user.getName().trim().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
					if(depnamemap.containsKey(user.getDepId()))
					{
						sb.append(" [").append(depnamemap.get(user.getDepId()).replaceAll("<", "&lt;").replaceAll(">", "&gt;")).append("]");
					}
					else
					{
						sb.append("[未知]");
					}
					sb.append("</option>");
				}
			}
			response.getWriter().print(sb.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取完美通知员工机构下员工失败！");
		}
	}

	/**
	 * 通过群组ID查询出员工/客户群组 中的群组人员信息，分页
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getGroupUserByGroupId(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			String groupId = request.getParameter("depId");
			String pageIndex = request.getParameter("pageIndex");
			String type = request.getParameter("type");
			String searchname = request.getParameter("searchname");
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			pageInfo.setPageSize(SIZE);
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));
			List<GroupInfoVo> groupInfoList = null;
			groupInfoList = prefectNoticeBiz.getGroupUser(Long.valueOf(groupId), searchname, pageInfo, type);
			if(groupInfoList != null && groupInfoList.size() > 0)
			{
				sb.append(pageInfo.getTotalPage()).append(",").append(pageInfo.getTotalRec()).append("#");
				for (GroupInfoVo user : groupInfoList)
				{
					// 默认员工
					String l2gtype = "0";
					if(user.getL2gType() == 0)
					{
						// 员工
						l2gtype = "4";
					}
					else
						if(user.getL2gType() == 1)
						{
							// 客户
							l2gtype = "5";
						}
						else
							if(user.getL2gType() == 2)
							{
								// 自定义
								l2gtype = "6";
							}
					if(user.getName() != null)
					{
						sb.append("<option value='").append(user.getGuId()).append("' isdep='" + l2gtype + "' et='' moblie='" + user.getMobile() + "'>");
						sb.append(user.getName().trim().replace("<", "&lt;").replace(">", "&gt;")).append("</option>");
					}
					else
					{
						continue;
					}

				}
			}
			response.getWriter().print(sb.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "获取完美通知群组成员失败！");
		}
	}

	/**
	 * 这里是检测点击员工机构是否被选择了的机构包含
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void isEmpDepContained(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			// 点击机构的ID
			String depId = request.getParameter("depId");
			// 已经选择好的机构ID
			String empDepIds = request.getParameter("empDepIds");
			// 解析IDS
			String[] depIds = empDepIds.split(",");
			// 机构集合
			List<LfEmployeeDep> lfEmployeeDepList = null;
			// 处理是否包含机构ID的集合
			LinkedHashSet<Long> depIdsSet = new LinkedHashSet<Long>();
			// 查询条件集合
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 机构对象
			LfEmployeeDep dep = null;
			// 循环
			for (int i = 0; i < depIds.length; i++)
			{
				String id = depIds[i];
				// 如果包含了，则说明该机构包含子机构
				if(id == null || "".equals(id))
				{
					continue;
				}
				// 如果相等，则眺出
				if(id.equals(depId))
				{
					response.getWriter().print("depExist");
					return;
					// 遇到包含子机构的机构处理操作
				}
				else
					if(id.contains("e"))
					{
						Long temp = Long.valueOf(id.substring(1));
						conditionMap.clear();
						dep = null;
						lfEmployeeDepList = null;
						dep = baseBiz.getById(LfEmployeeDep.class, temp);
						if(dep != null)
						{
							conditionMap.put("deppath&like", dep.getDeppath());
							conditionMap.put("corpCode", dep.getCorpCode());
							lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
							if(lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
							{
								for (int j = 0; j < lfEmployeeDepList.size(); j++)
								{
									depIdsSet.add(lfEmployeeDepList.get(j).getDepId());
								}
							}
						}
						// 单个机构，不包含子机构
					}
					else
					{
						dep = null;
						dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(id));
						if(dep != null)
						{
							depIdsSet.add(dep.getDepId());
						}
					}
			}
			boolean isFlag = false;
			// 判断是否包含该机构
			if(depIdsSet.size() > 0)
			{
				Long tempDepId = Long.valueOf(depId);
				isFlag = depIdsSet.contains(tempDepId);
			}
			// 返回
			if(isFlag)
			{
				response.getWriter().print("depExist");
				return;
			}
			else
			{
				response.getWriter().print("noExist");
				return;
			}
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "完美通知发送处理机构包含出现异常！");
		}
	}

	/**
	 * 判断是否 该机构包含选择了的子机构，并且把子机构删除掉
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void isDepContaineDeps(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			// 是否包含关系 0不包含 1包含
			String ismut = request.getParameter("ismut");
			// 机构ID
			String depId = request.getParameter("depId");
			// 不包含子机构
			if("0".equals(ismut))
			{
				// 查询出单个机构下 （不包含子机构人员）的个数
				String number = addrAtom.getEmployeeCountByDepId(depId);
				if(number != null && !"".equals(number))
				{
					if("0".equals(number))
					{
						response.getWriter().print("nobody");
					}
					else
					{
						response.getWriter().print(number);
					}
				}
				else
				{
					response.getWriter().print("nobody");
				}
				return;
			}
			// 该机构的包含子机构
			List<LfEmployeeDep> lfEmployeeDepList = null;
			LinkedHashSet<Long> depIdSet = new LinkedHashSet<Long>();
			// 条件查询
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 已选好的机构
			String empDepIds = request.getParameter("empDepIds");
			String[] depIds = empDepIds.split(",");
			List<Long> depIdExistList = new ArrayList<Long>();
			// 循环
			for (int j = 0; j < depIds.length; j++)
			{
				String id = depIds[j];
				if(id != null && !"".equals(id))
				{
					if(id.indexOf("e") > -1)
					{
						if(!"".equals(id.substring(1)))
						{
							depIdExistList.add(Long.valueOf(id.substring(1)));
						}
					}
					else
					{
						depIdExistList.add(Long.valueOf(id));
					}
				}
			}
			// 查找出要添加的机构的所有子机构，放在一个set里面
			LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
			if(dep != null)
			{
				conditionMap.put("deppath&like", dep.getDeppath());
				conditionMap.put("corpCode", dep.getCorpCode());
				lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
				if(lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
				{
					for (int i = 0; i < lfEmployeeDepList.size(); i++)
					{
						depIdSet.add(lfEmployeeDepList.get(i).getDepId());
					}
					// 这里是把包含的机构的ID选择出来
					List<Long> depIdListTemp = new ArrayList<Long>();
					for (int a = 0; a < depIdExistList.size(); a++)
					{
						if(depIdSet.contains(depIdExistList.get(a)))
						{
							depIdListTemp.add(depIdExistList.get(a));
						}
					}
					// 如果确实有几个已经存在的机构是要添加机构的子机构，那么就从新生成select的html
					String depids = depIdSet.toString();
					depids = depids.substring(1, depids.length() - 1);
					// 计算机构人数
					String countttt = addrAtom.getEmployeeCountByDepId(depids);
					if(countttt != null && !"".equals(countttt))
					{
						if("0".equals(countttt))
						{
							response.getWriter().print("nobody");
							return;
						}
						else
							if(depIdListTemp.size() > 0)
							{
								String tempDeps = depIdListTemp.toString();
								tempDeps = tempDeps.substring(1, tempDeps.length() - 1);
								response.getWriter().print(countttt + "," + tempDeps);
								return;
							}
							else
							{
								response.getWriter().print("notContains" + "&" + countttt);
								return;
							}
					}
					else
					{
						response.getWriter().print("nobody");
						return;
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知发送处理机构包含出现异常！");
			response.getWriter().print("errer");
		}
	}

	/**
	 * 获取员工机构的人员
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getEmpDepCount(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			// 是否包含关系 0不包含 1包含
			String ismut = request.getParameter("ismut");
			// 机构ID
			String depId = request.getParameter("depId");
			// 不包含子机构
			if("0".equals(ismut))
			{
				// 查询出单个机构下 （不包含子机构人员）的个数
				String number = addrAtom.getEmployeeCountByDepId(depId);
				if(number != null && !"".equals(number))
				{
					if("0".equals(number))
					{
						response.getWriter().print("nobody");
						return;
					}
					else
					{
						response.getWriter().print(number);
						return;
					}
				}
				else
				{
					response.getWriter().print("nobody");
					return;
				}
			}
			else
				if("1".equals(ismut))
				{
					LfEmployeeDep dep = baseBiz.getById(LfEmployeeDep.class, Long.valueOf(depId));
					if(dep != null)
					{
						LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
						conditionMap.put("deppath&like", dep.getDeppath());
						conditionMap.put("corpCode", dep.getCorpCode());
						List<LfEmployeeDep> lfEmployeeDepList = baseBiz.getByCondition(LfEmployeeDep.class, conditionMap, null);
						if(lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
						{
							String idStr = "";
							for (int i = 0; i < lfEmployeeDepList.size(); i++)
							{
								idStr += lfEmployeeDepList.get(i).getDepId().toString() + ",";
							}
							if(!"".equals(idStr) && idStr.length() > 0)
							{
								idStr = idStr.substring(0, idStr.length() - 1);
								// 计算机构人数
								String countttt = addrAtom.getEmployeeCountByDepId(idStr);
								response.getWriter().print(countttt);
								return;
							}
						}
					}
				}
			response.getWriter().print("nobody");
			return;
		}
		catch (Exception e)
		{
			response.getWriter().print("errer");
			EmpExecutionContext.error(e, "完美通知发送获取机构人数出现异常！");
		}
	}

	/**
	 * 获取彩信发送时所对应的所有员工群组
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getStaMMSGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		try
		{
			String corpCode = "";
			// 用户ID
			String userId = request.getParameter("userId");
			// 1代表的是查询完美通知中的群组 2代表的是查询彩信发送中的群组
			String grouptype = request.getParameter("grouptype");
			try
			{
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				corpCode = lfSysuser.getCorpCode();
				if(userId == null || "".equals(userId))
				{
					userId = lfSysuser.getUserId().toString();
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "完美通知当前操作员对象获取失败　！");
			}

			List<LfUdgroup> lfUdgroupList = new ArrayList<LfUdgroup>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			StringBuffer sb = new StringBuffer("<select select-one name='groupList' id='groupList' size ='16'  onclick='a();' style='width: 245px;height: 242px;border:0 solid #000000;font-size: 12px;color: black; padding:4px;vertical-align:middle;margin:-4px -5px;'>");
			// 登录的用户ID
			conditionMap.put("receiver", userId);
			// 员工群组
			conditionMap.put("gpAttribute", "0");
			// 排序
			orderbyMap.put("udgId", StaticValue.ASC);
			// 群组列表
			lfUdgroupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderbyMap);
			String employee = MessageUtils.extractMessage("common","common_employee",request);
			String individual = MessageUtils.extractMessage("common","common_individual",request);
			String shared = MessageUtils.extractMessage("common","common_shared",request);
			String client = MessageUtils.extractMessage("common","common_client",request);
			if(lfUdgroupList != null && lfUdgroupList.size() > 0)
			{
				String udgIds = "";
				for (LfUdgroup udg : lfUdgroupList)
				{
					udgIds += udg.getGroupid().toString() + ",";
				}
				// 获取群组id字符串
				udgIds = udgIds.substring(0, udgIds.length() - 1);
				//Map<String, String> countMap = addrAtom.getGroupCount(udgIds, "1");
				Map<String,String> countMap = new GroupBiz().getGroupMemberCount(udgIds, 1, corpCode);
				for (LfUdgroup lfUdgroup : lfUdgroupList)
				{
					String shareType = "0";
					if(lfUdgroup.getSharetype() == 1)
					{
						// 共享
						shareType = "1";
					}
					String mcount = countMap.get(lfUdgroup.getGroupid().toString());
					mcount = mcount == null ? "0" : mcount;
					// shareType表示是0个人 1共享 groupType1 是员工群组 2是客户群组
					sb.append("<option gcount='" + mcount + "' isdep='3' sharetype ='" + shareType + "' gtype='1' value='" + lfUdgroup.getGroupid() + "' udgid='" + lfUdgroup.getUdgId() + "' style='padding-left: 5px;'>");
					sb.append(lfUdgroup.getUdgName().replace("<", "&lt;").replace(">", "&gt;"));
					if(lfUdgroup.getSharetype() == 0)
					{
						sb.append(" ["+employee+"/"+individual+"]");
					}
					else
						if(lfUdgroup.getSharetype() == 1)
						{
							sb.append(" ["+employee+"/"+shared+"]");
						}
					sb.append("</option>");
				}
			}

			// 将群组列表设为null
			lfUdgroupList = null;
			// 查询出客户群组
			conditionMap.clear();
			// 彩信才需要查询出客户群组，完美通知不需要
			if("2".equals(grouptype))
			{
				conditionMap.put("userId", userId);
				conditionMap.put("gpAttribute", "1");
				lfUdgroupList = baseBiz.getByCondition(LfUdgroup.class, conditionMap, orderbyMap);
				if(lfUdgroupList != null && lfUdgroupList.size() > 0)
				{
					String udgIds = "";
					for (LfUdgroup udg : lfUdgroupList)
					{
						udgIds += udg.getUdgId().toString() + ",";
					}
					// 获取群组id字符串
					udgIds = udgIds.substring(0, udgIds.length() - 1);
					Map<String, String> countMap = addrAtom.getGroupCount(udgIds, "2");
					for (LfUdgroup lfUdgroup : lfUdgroupList)
					{
						String mcount = countMap.get(lfUdgroup.getUdgId().toString());
						mcount = mcount == null ? "0" : mcount;
						sb.append("<option  gcount='" + mcount + "' isdep='3' sharetype ='0' gtype='2'  value='" + lfUdgroup.getUdgId() + "' udgid=''  style='padding-left: 5px;'>").append(lfUdgroup.getUdgName().replace("<", "&lt;").replace(">", "&gt;") + "["+client+"/"+individual+"]");
						sb.append("</option>");
					}
				}
			}
			sb.append("</select>");
			response.getWriter().print(sb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知发送获取当前群组信息失败！");
		}

	}

	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~处理选择对象中的操作 end
	 */

	/**
	 * 获取机构的 数，只 限查子级
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getEmpSecondDepJson(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String depId = request.getParameter("depId");
			//从请求获取到当前登录操作员ID，现在不使用
			//String lguserid = request.getParameter("lguserid");
			
			//从Session中获取当前登录操作员
			LfSysuser loginSysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			//从Session中获取当前登录操作员ID
			String lguserid=String.valueOf(loginSysuser.getUserId());
			//从Session中获取当前登录操作员企业编码
			String corpCode=loginSysuser.getCorpCode();
			
			List<LfEmployeeDep> empDepList = prefectNoticeBiz.getEmpSecondDepTreeByUserIdorDepId(lguserid, depId,corpCode);
			LfEmployeeDep dep = null;
			StringBuffer tree = new StringBuffer("");
			if(empDepList != null && empDepList.size() > 0)
			{
				tree.append("[");
				for (int i = 0; i < empDepList.size(); i++)
				{
					dep = empDepList.get(i);
					tree.append("{");
					tree.append("id:'").append(dep.getDepId() + "'");
					tree.append(",name:'").append(dep.getDepName()).append("'");
					tree.append(",pId:'").append(dep.getParentId() + "'");
					tree.append(",depcodethird:'").append(dep.getDepcodethird() + "'");
					tree.append(",isParent:").append(true);
					tree.append("}");
					if(i != empDepList.size() - 1)
					{
						tree.append(",");
					}
				}
				tree.append("]");
			}
			response.getWriter().print(tree.toString());
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "完美通知发送获取员工机构信息失败！");
		}
	}

	/**
	 * 过滤号码是否合法
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws Exception
	 */
	public void filterPhone(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		try
		{
			String[] haoduan = wgMsgConfigBiz.getHaoduan();
			String tmp = request.getParameter("tmp");
			if(phoneUtil.checkMobile(tmp, haoduan) != 1&&!phoneUtil.isAreaCode(tmp))
			{
				response.getWriter().print("2");
			}
			else
			{
				response.getWriter().print("1");
			}
		}
		catch (Exception e)
		{
			response.getWriter().print("");
			EmpExecutionContext.error(e, "完美通知发送过滤号码是否合法出现异常！");
		}
	}

	/**
	 * @description 完美通知发送预览前做 检测
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-24 下午07:06:48
	 */
	public void checkPerfectMsg(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		StringBuffer resultbuffer = new StringBuffer();
		try
		{
			// 用户ID
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			// 内容
			String content = request.getParameter("content");
			// 企业编码
			String lgcorpcode = request.getParameter("lgcorpcode");
			// SPUSER
			String spUser = request.getParameter("spUser");
			if(lguserid == null || lgcorpcode == null || spUser == null || content == null)
			{
				resultbuffer.append("stage1").append("&isnull");
				EmpExecutionContext.error("完美通知 获取  lguserid : " + lguserid + " lgcorpcode : " + lgcorpcode + " spUser : " + spUser + " content : " + content);
				return;
			}
			// 获取当前操作员对象
			LfSysuser lfsysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfsysuser == null)
			{
				resultbuffer.append("stage1").append("&isnull");
				EmpExecutionContext.error("完美通知获取当前操作员为NULL");
				return;
			}
			// 尾号
			String usedSubno = request.getParameter("usedSubno");
			String words = new String();
			words = keyWordAtom.checkText(content, lgcorpcode);
			if(words != null && words.length() > 0)
			{
				resultbuffer.append("stage2").append("&").append(words);
				return;
			}
			// 判断spuser的url是否可用
			String checkSpUerRes = smsBiz.checkSpUser(spUser, 1, 1);
			if(!"checksuccess".equals(checkSpUerRes))
			{
				resultbuffer.append("stage5").append("&").append("spfail_" + checkSpUerRes);
				String info="";
				if("checkerror".equals(checkSpUerRes))
				{
					info="检查SP账号是否可用失败";
				}else if("nouserid".equals(checkSpUerRes)){
					info="SP账号["+spUser+"]在系统中不存在";
				}else if("nomourl".equals(checkSpUerRes)){
					info="SP账号["+spUser+"]未配置上行推送URL";
				}else if("moconnfail".equals(checkSpUerRes)){
					info="检测出SP账号["+spUser+"]配置的上行推送URL不可用";
				}else if("norpturl".equals(checkSpUerRes)){
					info="SP账号["+spUser+"]未配置状态报告推送URL";
				}else if("rptconnfail".equals(checkSpUerRes)){
					info="检测出SP账号["+spUser+"]配置的状态报告推送URL不可用";
				}
				EmpExecutionContext.error("完美通知预览前检测SP账号["+spUser+"]配置不通过，原因："+info+"。");
				return;
			}
			LfSubnoAllotDetail detail = null;
			if(usedSubno == null || "".equals(usedSubno))
			{
				ErrorCodeParam errorCode = new ErrorCodeParam();
				SMParams smParams = new SMParams();
				smParams.setCodes(StaticValue.WMNOTICECODE);
				smParams.setCodeType(0);
				smParams.setCorpCode(lgcorpcode);
				smParams.setAllotType(1);
				smParams.setSpUserid(spUser);
				smParams.setDepId(lfsysuser.getDepId());
				smParams.setLoginId(lfsysuser.getGuId().toString());
				detail = globalBiz.getSubnoDetail(smParams, errorCode);
				if(detail != null && detail.getUsedExtendSubno() != null && !"".equals(detail.getUsedExtendSubno()))
				{
					usedSubno = detail.getUsedExtendSubno();
				}
				else
				{
					if("EZHB237".equals(errorCode.getErrorCode()))
					{
						resultbuffer.append("stage3").append("&").append("enoughSubno");// 尾号不够
					}
					else
					{
						resultbuffer.append("stage3").append("&").append("notSubno");// 获取尾号失败
					}
					EmpExecutionContext.error("完美通知获取尾号失败，错误编码：" + errorCode.getErrorCode());
					return;
				}
			}
			// 对发送账号的通道号进行判断 返回1表示判断通过 其他值表示不通过
			// 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4 该发送账号的全通道号有一个超过20位
			int resultInt = subnoManagerBiz.ischeckPortUsed(spUser, usedSubno);
			if(resultInt != 1)
			{
				resultbuffer.append("stage3&").append(resultInt + "_" + usedSubno);
			}
			else
			{
				// 检测成功
				resultbuffer.append("success&" + usedSubno);
			}
		}
		catch (Exception e)
		{
			resultbuffer.append("errer");
			EmpExecutionContext.error(e, "完美通知预览检测出现异常！");
		}
		finally
		{
			response.getWriter().print(resultbuffer.toString());
		}
	}

	

	/**
	 * 检测文件是否存在
	 * 
	 * @param request
	 * @param response
	 */
	public void goToFile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 文件地址url
		String url = request.getParameter("url");
		try
		{
			response.getWriter().print(txtfileutil.checkFile(url));
		}
		catch (Exception e)
		{
			// 异常处理
			response.getWriter().print("");
			EmpExecutionContext.error(e, "完美通知验证文件是否存在出现异常！");
		}
	}

	/**
	 * 高级设置存为默认
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void setDefault(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

		//返回信息
		String result = "fail";
		try {
			//String lguserid = request.getParameter("lguserid");
			// 漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String lgcorpcode = request.getParameter("lgcorpcode");
			String busCode = request.getParameter("busCode");
			String spUser = request.getParameter("spUser");
			String flag = request.getParameter("flag");
			if(flag == null || "".equals(flag))
			{
				EmpExecutionContext.error("完美通知发送高级设置存为默认参数异常！");
				response.getWriter().print(result);
				return;
			}
			if(lguserid == null || "".equals(lguserid))
			{
				EmpExecutionContext.error("完美通知发送高级设置存为默认参数异常！lguserid："+lguserid);
				response.getWriter().print(result);
				return;
			}
			if(busCode == null || "".equals(busCode))
			{
				EmpExecutionContext.error("完美通知发送高级设置存为默认参数异常！busCode："+busCode);
				response.getWriter().print(result);
				return;
			}
			if(spUser == null || "".equals(spUser))
			{
				EmpExecutionContext.error("完美通知发送高级设置存为默认参数异常！spUser："+spUser);
				response.getWriter().print(result);
				return;
			}

			//原记录删除条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userid", lguserid);
			conditionMap.put("flag", flag);
			
			//更新对象
			LfDfadvanced lfDfadvanced = new LfDfadvanced();
			lfDfadvanced.setUserid(Long.parseLong(lguserid));
			lfDfadvanced.setBuscode(busCode);
			lfDfadvanced.setSpuserid(spUser);
			lfDfadvanced.setFlag(Integer.parseInt(flag));
			lfDfadvanced.setCreatetime(new Timestamp(System.currentTimeMillis()));

			result = prefectNoticeBiz.setDefault(conditionMap, lfDfadvanced);
			
			//操作结果
			String opResult ="完美通知发送高级设置存为默认失败。";
			if(result != null && "seccuss".equals(result))
			{
				opResult = "完美通知发送高级设置存为默认成功。";
			}
			//操作员信息
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//操作员姓名
			String opUser = sysuser==null?"":sysuser.getUserName();
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务编码，SP账号](").append(busCode).append("，").append(spUser).append(")");
			
			//操作日志
			EmpExecutionContext.info("完美通知发送", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");
			
			response.getWriter().print(result);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "完美通知发送高级设置存为默认异常！");
			response.getWriter().print(result);
		}
	}
	
}
