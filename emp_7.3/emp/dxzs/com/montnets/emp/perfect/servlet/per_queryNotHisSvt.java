package com.montnets.emp.perfect.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.perfect.LfPerfectNoticUp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.perfect.biz.PrefectExcel;
import com.montnets.emp.perfect.biz.PrefectNoticeBiz;
import com.montnets.emp.perfect.vo.PerfectNoticUpVo;
import com.montnets.emp.perfect.vo.PerfectNoticeVo;
import com.montnets.emp.util.DownloadFile;
import com.montnets.emp.util.PageInfo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @description 完美通知历史记录
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-24 上午10:48:13
 */
public class per_queryNotHisSvt extends BaseServlet
{

	private final BaseBiz				baseBiz				= new BaseBiz();

	private static final long	serialVersionUID	= 7132385885311854313L;

	private final PrefectNoticeBiz	prefectNoticeBiz	= new PrefectNoticeBiz();

	private final String				empRoot				= "dxzs";

	private final String				basePath			= "/perfect";

	private final String				excelPath			= "dxzs/perfect/file/";
	
	//格式化时间
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long startTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try
		{
			//String lgguid = "";
			//Long userGuid = null;
			LfSysuser lfsysuser = null;
			try
			{
				//lgguid = request.getParameter("lgguid");
				//userGuid = Long.valueOf(lgguid);
				//lfsysuser = baseBiz.getByGuId(LfSysuser.class, userGuid);
				
				lfsysuser=(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "完美通知历史查询，获取操作员信息异常。");
				throw e;
			}
			boolean isBack = request.getParameter("isback")==null?false:true;//是否返回操作
			// String corpCode = getCorpCode();

			// 发送者
			String sendName = null;
			// 发送类型
			String sendType = null;
			// 内容
			String content = null;
			// 发送时间
			String sendTime = null;
			// 接收时间
			String recvTime = null;
			HttpSession session = request.getSession(false);
			if(isBack){
				pageInfo = (PageInfo) session.getAttribute("per_query_pageinfo");
				sendName = session.getAttribute("per_query_sendName")==null?"":String.valueOf(session.getAttribute("per_query_sendName"));
				content = session.getAttribute("per_query_content")==null?"":String.valueOf(session.getAttribute("per_query_content"));
				sendTime = session.getAttribute("per_query_sendTime")==null?"":String.valueOf(session.getAttribute("per_query_sendTime"));
				recvTime = session.getAttribute("per_query_recvTime")==null?"":String.valueOf(session.getAttribute("per_query_recvTime"));
				request.setAttribute("senderName", sendName);
				request.setAttribute("content", content);
				request.setAttribute("sendTime", sendTime);
				request.setAttribute("recvTime", recvTime);
			}else{
				boolean isFirstEnter = pageSet(pageInfo, request);
				if(!isFirstEnter)
				{
					sendName = request.getParameter("senderName").trim();
					content = request.getParameter("content").trim();
					sendTime = request.getParameter("sendTime");
					recvTime = request.getParameter("recvTime");
				}
			}
			session.setAttribute("per_query_pageinfo",pageInfo);
			session.setAttribute("per_query_sendName", sendName);
			session.setAttribute("per_query_content", content);
			session.setAttribute("per_query_sendTime", sendTime);
			session.setAttribute("per_query_recvTime", recvTime);
			List<PerfectNoticeVo> perfectNoticeVos = prefectNoticeBiz.getPerfectNoticeVoHistroy(lfsysuser.getCorpCode(), sendName, sendType, content, sendTime, recvTime, lfsysuser.getUserId(), pageInfo);
			if(perfectNoticeVos != null && perfectNoticeVos.size() > 0)
			{
				PerfectNoticeVo perfectNoticeVo = null;
				List<LfPerfectNoticUp> noticUps = null;
				LfPerfectNoticUp up = null;
				LfSysuser sysuser = null;
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				int replyCount = 0;
				int receiveCount = 0;
				for (int i = 0; i < perfectNoticeVos.size(); i++)
				{
					perfectNoticeVo = perfectNoticeVos.get(i);
					conditionMap.clear();
					conditionMap.put("taskId", String.valueOf(perfectNoticeVo.getTaskId()));
					noticUps = baseBiz.getByCondition(LfPerfectNoticUp.class, conditionMap, null);
					replyCount = 0;
					receiveCount = 0;
					if(noticUps != null)
					{
						for (int j = 0; j < noticUps.size(); j++)
						{
							up = noticUps.get(j);
							//要接收到状态报告，且状态报告不是失败
							if(up.getIsReceive() == 0 && up.getIsAtrred() != null && !"4".equals(up.getIsAtrred().trim()))
							{
								receiveCount++;
							}
							if(up.getIsReply() == 0)
							{
								replyCount++;
							}
						}
					}
					else
					{
						continue;
					}
					try
					{
						sysuser = baseBiz.getByGuId(LfSysuser.class, perfectNoticeVo.getSenderGuid());
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "完美通知历史获取当前操作员对象失败！");
					}
					if(sysuser != null && sysuser.getUserState() == 2)
					{
						perfectNoticeVo.setRecevierType(2);
					}
					else
					{
						perfectNoticeVo.setRecevierType(1);
					}
					perfectNoticeVo.setReceiveCount(receiveCount);
					perfectNoticeVo.setReplyCount(replyCount);
					perfectNoticeVo.setContent(perfectNoticeVo.getContent());
					sysuser = null;
				}
				
				//完美通知加密
				//获取加密类
				ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
				//加密mtId
				encryptOrDecrypt.batchEncrypt(perfectNoticeVos, "TaskId", "TaskIdCipher");
			
			}
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("perfectNotics", perfectNoticeVos);
			request.setAttribute("findresult", "");
			//获取发送界面跳转请求RUL参数taskId,用于标识发送记录
			String taskId = request.getParameter("taskId");
			request.setAttribute("sendTaskId", taskId);
			if(!isBack)
			{
				//操作日志信息
				String opContent = "查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
				//LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				EmpExecutionContext.info("完美通知查看", lfsysuser.getCorpCode(), lfsysuser.getUserId().toString(), lfsysuser.getUserName(), opContent, "GET");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知历史跳转失败！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
		}
		finally
		{
			request.getRequestDispatcher(empRoot + basePath + "/per_queryNotHis.jsp").forward(request, response);
		}
	}

	/**
	 * 进入完美通知详情的查询页面
	 * 
	 * @param request
	 * @param response
	 */
	public void getPreNoticeHisInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long startTime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try
		{
			pageSet(pageInfo, request);
			// 完美通知主表ID
			String taskidStr = request.getParameter("taskid");
			
			 //获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密
			Long taskid=Long.parseLong(encryptOrDecrypt.decrypt(String.valueOf(taskidStr)));
			
			String lgguid = request.getParameter("lgguid");
			// 判断是首次进入 1是 2不是
			String type = request.getParameter("type");
			// 查询的名字
			String username = "";
			// 查询的手机号码
			String phone = "";
			// 查询上行回复内容
			String remsg = "";
			// 回复状态
			String isReAttr = "";
			// 回执状态
			String isGeAttr = "";
			// 已发次数
			String sendCount = "";
			if("2".equals(type))
			{
				username = request.getParameter("username").trim();
				phone = request.getParameter("phone").trim();
				remsg = request.getParameter("remsg").trim();
				isReAttr = request.getParameter("isReAttr").trim();
				isGeAttr = request.getParameter("isGeAttr").trim();
				sendCount = request.getParameter("reCount").trim();
			}
			request.setAttribute("username", username);
			request.setAttribute("phone", phone);
			request.setAttribute("remsg", remsg);
			request.setAttribute("isReAttr", isReAttr);
			request.setAttribute("isGeAttr", isGeAttr);
			request.setAttribute("reCount", sendCount);

			LfSysuser lfsysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfsysuser == null)
			{
				EmpExecutionContext.error("完美通知 获取当前操作员失败！");
				return;
			}
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 查询当前完美通知主表信息
			LfPerfectNotic perfectNotic = prefectNoticeBiz.findLfPerfectNoticByTaskId(taskid, lfsysuser.getCorpCode(), conditionMap, baseBiz);
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			List<PerfectNoticUpVo> noticUpVos = new ArrayList<PerfectNoticUpVo>();
			// 查询完美通知详细信息
			List<LfPerfectNoticUp> noticUps = prefectNoticeBiz.getPerfectNoticeUpByNoticeId(username, phone, remsg, taskid, isReAttr, isGeAttr, sendCount, pageInfo);
			LfPerfectNoticUp up = null;
			PerfectNoticUpVo upVo = null;
			String spUser = "";
			if(noticUps != null && noticUps.size() > 0)
			{
				for (int i = 0; i < noticUps.size(); i++)
				{
					upVo = new PerfectNoticUpVo();
					up = noticUps.get(i);
					upVo.setPnupId(up.getPnupId());
					String name = up.getName();
					if(name != null && !"".equals(name))
					{
						upVo.setName(name);
					}
					else
					{
						upVo.setName("-");
					}
					String moblie = up.getMobile();
					if(moblie != null && !"".equals(moblie))
					{
						upVo.setMobile(moblie);
					}
					else
					{
						upVo.setMobile("-");
					}
					String time = "";
					Timestamp receiveTime = up.getSendTime();
					if(receiveTime != null)
					{
						time = simpleDateFormat.format(receiveTime).toString();
					}
					if(time != null && !"".equals(time))
					{
						upVo.setSendTime(time);
					}
					else
					{
						upVo.setSendTime("");
					}
					String content = up.getContent();
					if(content != null && !"".equals(content))
					{
						upVo.setContent(content);
					}
					else
					{
						upVo.setContent("");
					}

					if(up.getIsReply() == 2)
					{
						upVo.setReceiveCountMsg("-");
						// 标识该条完美通知是否可以再发送 1是可以 2是不可以
						upVo.setIsValid("2");
					}
					else
					{
						upVo.setReceiveCountMsg(String.valueOf(up.getReceiveCount()));
						// 标识该条完美通知是否可以再发送 1是可以 2是不可以
						upVo.setIsValid("1");
						// 标识该条完美通知是否可以再发送
						if(spUser == null || "".equals(spUser))
						{
							spUser = up.getSpUser();
						}
					}
					if(up.getIsReply() == 0)
					{
						/*已回复*/
						upVo.setIsReplyMsg(MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_122",request));
					}
					else
						if(up.getIsReply() == 1)
						{
							/*未回复*/
							upVo.setIsReplyMsg(" <font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_123",request)+"</font>");
						}
						else
						{
							upVo.setIsReplyMsg("-");
						}
					if(up.getIsReceive() == 0)
					{
						if("4".equals(up.getIsAtrred())){
							/*接收失败*/
							upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_180",request)+"</font>");
						}
						else{
							/*已接收*/
							upVo.setIsReceiveMsg(MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_125",request));
						}
					}
					else
						if(up.getIsReceive() == 1)
						{
							/*未接收*/
							upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_126",request)+"</font>");
						}
						else
							if(up.getIsReceive() == 2)
							{
								/*未发送成功*/
								upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_127",request)+"</font>");
							}
							else
								if(up.getIsReceive() == 3)
								{
									/*手机号异常*/
									upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_1",request)+"</font>");
								}
								else
									if(up.getIsReceive() == 4)
									{
										/*手机号为空*/
										upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_2",request)+"</font>");
									}
									else
										if(up.getIsReceive() == 6)
										{
											/*短信余额不足*/
											upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_3",request)+"</font>");
										}
										else
											if(up.getIsReceive() == 7)
											{
												/*短信扣费失败*/
												upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_4",request)+"</font>");
											}
											else
												if(up.getIsReceive() == 8)
												{
													/*运营商不符合*/
													upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_5",request)+"</font>");
												}
												else
													if(up.getIsReceive() == 9)
													{
														/*所属机构未充值*/
														upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_6",request)+"</font>");
													}
													else
														if(up.getIsReceive() == -2)
														{
															/*号码重复*/
															upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_7",request)+"</font>");
														}
														else
															if(up.getIsReceive() == -1)
															{
																/*已接收*/
																upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_125",request)+"</font>");
															}
															else
																if(up.getIsReceive() == 5)
																{
																	/*黑名单*/
																	upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_8",request)+"</font>");
																}
																else
																	if(up.getIsReceive() == -4)
																	{
																		/*重复人员*/
																		upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_9",request)+"</font>");
																	}
																	else
																	{
																		/*出现异常*/
																		upVo.setIsReceiveMsg("<font color='red'>"+MessageUtils.extractMessage("dxzs","dxzs_errorInfo_text_10",request)+"</font>");
																	}

					noticUpVos.add(upVo);
				}
			}
			// 完美通知ID
			request.setAttribute("preNoticeId", String.valueOf(perfectNotic.getNoticId()));
			// 最大发送次数
			request.setAttribute("maxSendCount", String.valueOf(perfectNotic.getMaxSendCount()));
			// 间隔时间
			request.setAttribute("sendInterval", String.valueOf(perfectNotic.getSendInterval()));
			// 发送内容
			request.setAttribute("sendContent", perfectNotic.getContent());
			// 发送次数
			request.setAttribute("arySendCount", String.valueOf(perfectNotic.getArySendCount()));
			// 发送人名字
			request.setAttribute("senderName", lfsysuser != null ? lfsysuser.getName() : "1次");
			request.setAttribute("noticUpVos", noticUpVos);
			request.setAttribute("spUser", spUser);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("lgguid", lgguid);
			request.setAttribute("lgcorpcode", lfsysuser.getCorpCode());
			request.setAttribute("taskid", taskidStr);
			//操作日志信息
			String opContent = "详情查询："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("完美通知查看", lfsysuser.getCorpCode(), lfsysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知详情的查询页面跳转失败！");
		}
		finally
		{
			request.getRequestDispatcher(empRoot + basePath + "/per_queryNotDetail.jsp").forward(request, response);
		}
	}

	/**
	 * 导出完美通知详情的EXCEL
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void exportPerExcel(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long startTime = System.currentTimeMillis();
		
		PageInfo pageInfo = new PageInfo();

		// 获取导出文件路径
		String context = request.getSession(false).getServletContext().getRealPath(excelPath);
		try
		{
			// pageSet(request);
			// 获取完美通知查询条件
			// 接收对象
			String userName = (String) request.getParameter("pre_username");
			// 手机号码
			String phone = (String) request.getParameter("pre_phone");
			// 完美通知ID
			// Long noticeId = request.getParameter("pre_noticeId") == "" ? null
			// : Long.parseLong(request.getParameter("pre_noticeId"));
			//Long taskid = Long.parseLong(request.getParameter("taskid"));
			
			
			String taskidStr=request.getParameter("taskid");
			//获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密
			Long taskid=Long.parseLong(encryptOrDecrypt.decrypt(taskidStr));

			// 回复内容
			String remsg = (String) request.getParameter("remsg");
			// 回复状态
			String isReAttr = (String) request.getParameter("isReAttr");
			// 回执状态
			String isGeAttr = (String) request.getParameter("isGeAttr");
			// 发送次数
			String sendCount = (String) request.getParameter("sendCount");

			Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
			int ishidephome = 0;
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
			{
				ishidephome = 1;
			}

			/* ExcelTool et = new ExcelTool(context); */
			PrefectExcel et = new PrefectExcel(context);
			// 获取导出内容
			Map<String, String> resultMap = et.createPrefectExcel(userName, phone, remsg, taskid, isReAttr, isGeAttr, sendCount, pageInfo, ishidephome,request);
			//返回状态
			String result = "false";
			//操作日志信息
			String opContent = "";
			// 如果导出内容不为空
			if(resultMap != null && resultMap.size() > 0)
			{
	            HttpSession session = request.getSession(false);
	            session.setAttribute("perDetail_export",resultMap);
				//操作日志信息
				opContent = "详情导出成功。";
				result = "true";
				/*String fileName = (String) resultMap.get("FILE_NAME");
				String filePath = (String) resultMap.get("FILE_PATH");
				// 弹出下载页面。
				DownloadFile dfs = new DownloadFile();
				dfs.downFile(request, response, filePath, fileName);*/
			}
			else
			{
				//操作日志信息
				opContent = "详情导出失败。";	
			}
			opContent += "开始："+sdf.format(startTime)+"，耗时："+(System.currentTimeMillis()-startTime)/1000+"秒，总数：" + pageInfo.getTotalRec();
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			EmpExecutionContext.info("完美通知查看", lfSysuser.getCorpCode(), lfSysuser.getUserId().toString(), lfSysuser.getUserName(), opContent, "GET");
			response.getWriter().print(result);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "导出完美通知详情失败！");
			request.getSession(false).setAttribute("error", e);
		}

	}

	/**
	 * 完美通知详情EXCEL文件下载
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
        try
		{
			HttpSession session = request.getSession(false);
			Object obj = session.getAttribute("perDetail_export");
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
			EmpExecutionContext.error(e, "完美通知详情EXCEL文件下载失败，fileName:"+fileName+"，filePath:"+filePath);
		}
	}
	/**
	 * 获取当前发送余额
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getCt(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		BalanceLogBiz biz = new BalanceLogBiz();
		Long maxcount = null;
		Long maxMmsConunt = null;
		// 返回结果
		String result = "";
		//String id = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String id = SysuserUtil.strLguserid(request);

		long userId;
		// 登录用户id
		if(id == null || "".equals(id))
		{
			userId = ((LfSysuser) request.getSession(false).getAttribute("loginSysuser")).getUserId();
		}
		else
		{
			userId = Long.parseLong(id);
		}
		LfSysuser syuser = null;
		try
		{
			syuser = baseBiz.getLfSysuserByUserId(userId);
			if(!biz.IsChargings(syuser.getUserId()))
			{
				out.print("nojifei");
			}
			else
			{
				maxcount = biz.getAllowSmsAmount(syuser);
				maxMmsConunt = biz.getAllowMmsAmount(syuser);
				if(maxcount == null && maxMmsConunt == null)
				{
					result = "^";
				}
				out.print(result + "ye" + maxcount + "," + maxMmsConunt);
			}
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知发送获取当前发送余额失败！");
		}

	}
}
