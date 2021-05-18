package com.montnets.emp.shorturl.diffsms.servlet;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.shorturl.surlmanage.biz.UrlSendBiz;
import com.montnets.emp.shorturl.surlmanage.biz.UrlTimerManagerBiz;
import com.montnets.emp.shorturl.surlmanage.entity.LfDfadvanced;
import com.montnets.emp.shorturl.surlmanage.entity.LfDomain;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.http.conn.HttpHostConnectException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.Timestamp;
import java.util.*;
@SuppressWarnings("serial")
public class Durl_sendDiffSMSSvt extends BaseServlet
{

	final HttpSmsSend		smsSend		= new HttpSmsSend();

	final BaseBiz			baseBiz		= new BaseBiz();

	final CommonBiz		commonBiz	= new CommonBiz();

	final SuperOpLog		spLog		= new SuperOpLog();

	final SmsBiz			smsBiz		= new SmsBiz();

	private final UrlTimerManagerBiz timerBiz = UrlTimerManagerBiz.getTMInstance();
	// 常用文件读写工具类
	final TxtFileUtil		txtfileutil	= new TxtFileUtil();

	final BalanceLogBiz	balancebiz	= new BalanceLogBiz();

	// 换行符
	final String			line		= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

	final String			empRoot		= "shorturl";

	final String			basePath	= "/durlsms";

	/**
	 * 不同内容群发
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{

		// 企业编码
		String corpCode = null;
		// 用户id
		String userid = null;

		try
		{
			// 错误编码
			String errorCode = "";
			//登录操作员信息
			LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			// 企业编码
			corpCode = lfSysuser.getCorpCode();
			// 用户id
			userid = String.valueOf(lfSysuser.getUserId());
			LinkedHashMap<String, String> conditionMapCorp = new LinkedHashMap<String, String>();

			// 查找当前用户是否带有审批流
			LinkedHashMap<String, String> orconp = new LinkedHashMap<String, String>();
			conditionMapCorp.put("corpCode&in", "0," + corpCode);
			// 排序
			orconp.put("corpCode", "asc");
			
			//设置启用查询条件
			conditionMapCorp.put("state", "0");
			//设置查询手动和手动+触发
			conditionMapCorp.put("busType&in", "0,2");

			// 查找当前机构下的所有业务类型
			try
			{
				List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMapCorp, orconp);
				request.setAttribute("busList", busList);
			}
			catch (Exception e)
			{
				request.setAttribute("findresult", "-1");
				// 错误码
				EmpExecutionContext.error(e, "查询业务类型失败，EBFV001");
			}

			// 查找当前用户的发送账户(sp账号)
			LfSysuser currUser = baseBiz.getById(LfSysuser.class, userid);
			List<Userdata> spUserList = smsBiz.getSpUserList(currUser);
			// 将查询结果返回前台
			request.setAttribute("spUserList", spUserList);
//			UserdataAtom userdataAtom = new UserdataAtom();
//			// 发送账户存放内存Map
//			userdataAtom.setUserdata(spUserList);

			// 查找动态短信模板
			LinkedHashMap<String, String> conditionMapTemp = new LinkedHashMap<String, String>();
			conditionMapTemp.put("tmpType", "3");
			conditionMapTemp.put("tmState", "1");
			conditionMapTemp.put("isPass&in", "0,1");
			// conditionMap.put("userId", getUserId().toString());
			conditionMapTemp.put("dsflag", "1");

			LinkedHashMap<String, String> orderbyMapTemp = new LinkedHashMap<String, String>();
			orderbyMapTemp.put("tmid", StaticValue.ASC);
			// 动态短信模板的集合
			// List<LfTemplate> tmpList =
			// baseBiz.getByCondition(LfTemplate.class,Long.parseLong(userid),
			// conditionMapTemp, orderbyMapTemp);

			// 产生taskId
			Long taskId = commonBiz.getAvailableTaskId();
			//操作日志信息
			String opContent = "获取taskid("+taskId+")成功";
			EmpExecutionContext.info("不同内容群发", corpCode, userid, lfSysuser.getUserName(), opContent.toString(), "GET");
			request.setAttribute("taskId", taskId.toString());

			// request.setAttribute("tmpList", tmpList);
			request.setAttribute("errorCode", errorCode);
			request.setAttribute("isExistSubNo", currUser.getIsExistSubNo());
			
			//获取高级设置默认信息
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userid", userid);
			//2:不同内容群发
			conditionMap.put("flag", "2");
			LinkedHashMap<String, String> orderMap = new LinkedHashMap<String, String>();
			orderMap.put("id", StaticValue.DESC);
			List<LfDfadvanced> lfDfadvancedList = baseBiz.getByCondition(LfDfadvanced.class, conditionMap, orderMap);
			LfDfadvanced lfDfadvanced = null;
			if(lfDfadvancedList != null && lfDfadvancedList.size() > 0)
			{
				lfDfadvanced = lfDfadvancedList.get(0);
			}
			request.setAttribute("lfDfadvanced", lfDfadvanced);
			
			//用户的请求地址
			String s = request.getRequestURI();
			//是短信客服模块的相同内容还是短信助手模块
			String hjsp = s.substring(s.lastIndexOf("_")+1,s.lastIndexOf("."));
			request.setAttribute("reTitle", hjsp);
			List<LfDomain> domainlist = new UrlSendBiz().findDomainByCorpcode(corpCode);
			request.setAttribute("domainlist", domainlist);
			// 页面跳转
			request.getRequestDispatcher(empRoot + basePath + "/dsm_sendDiffSMS.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, userid, corpCode);
			request.setAttribute("findresult", "-1");
			try
			{
				request.getRequestDispatcher(empRoot + basePath + "/dsm_sendDiffSMS.jsp").forward(request, response);
			}
			catch (ServletException e1)
			{
				EmpExecutionContext.error(e1, userid, corpCode);
			}
			catch (IOException e1)
			{
				EmpExecutionContext.error(e1, userid, corpCode);
			}
		}
	}

	/**
	 * 点击提交按钮发送
	 * 
	 * @param request
	 * @param response
	 */
	public void send(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String result = "";
		// 任务主题
		String title = request.getParameter("taskname");
		//任务ID
		String taskId = request.getParameter("taskId");
		// 当前登录操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		//草稿箱id
		String draftId = request.getParameter("draftId");
		// 提交类型
		String bmtType = request.getParameter("bmtType");
		// 发送账号
		String spUser = request.getParameter("spUser");
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = request.getParameter("sendType");
		EmpExecutionContext.info("[OTHER] mod：不同内容群发，发送servlet接收参数，userid:"+lguserid
				+"，corpCode:"+lgcorpcode+"，taskId:"+taskId+"，sendType:"+sendType+"，bmtType:"+bmtType+"，spUser:"+spUser);
		//主题为默认时,直接返回(防止重发)
		if(title != null && "不作为短信内容发送".equals(title.trim()))
		{
			EmpExecutionContext.error("不同内容发送获取参数异常，" + "title:" + title+"，taskId："+taskId+ "，errCode:"+ IErrorCode.V10001);
			result = ErrorCodeInfo.getInstance().getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
		}
		else
		{
			//保存短地址相关信息到数据库
			UrlSendBiz  urlSendBiz = new UrlSendBiz();
			SmsSendBiz smsSendBiz = new SmsSendBiz();
			// 提交发送
			result = urlSendBiz.send(request,2);
			
			//删除草稿箱，如果有的话
			smsSendBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
//			if ("000".equals(result)) {
//				createUrlTask(request);
//			}
			
		}
		try
		{
			String s = request.getRequestURI();

			request.getSession(false).setAttribute("durl_diffResult", result);
			// 重定向
			s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode +"&oldTaskId=" + taskId + "&t=" + System.currentTimeMillis();
			response.sendRedirect(s);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, lguserid, lgcorpcode);
		}
	}

//	private void createUrlTask(HttpServletRequest request) {
//		try {
//			LfUrlTask urlTask = new LfUrlTask();
//			// 任务主题
//			String title = request.getParameter("taskname");
//			// 信息内容
//			String msg = request.getParameter("msg");
//			// 有效总数
//			String hidSubCount = request.getParameter("hidEffCount");
//			// 任务id
//			String taskId = request.getParameter("taskId");
//			// 当前登录操作员id
//			String strlguserid = request.getParameter("lguserid");
//			// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
//			String sendType = request.getParameter("sendType");
//			//详情文件物理路径
//			String srcFileUrl = request.getParameter("hidDzUrl");
//			//企业编码
//			String lgcorpcode = request.getParameter("lgcorpcode");
//			//原始地址Id
//			String srcId = request.getParameter("srcId");
//			LfSysuser user = baseBiz.getById(LfSysuser.class, strlguserid);
//			
//			urlTask.setTitle(title);
//			urlTask.setMsg(msg==null?"":msg);
//			urlTask.setSubCount(Integer.valueOf(hidSubCount));
////			urlTask.setVisCount(0);
////			urlTask.setVisNum(0);
//			urlTask.setTaskId(Integer.valueOf(taskId));
//			urlTask.setCorpCode(lgcorpcode);
//			urlTask.setCreateUid(Integer.valueOf(strlguserid));
////			urlTask.setDepId(user.getDepId().intValue());
//			urlTask.setUtype("1".equals(sendType)?1:2);
//			urlTask.setSrcFile(srcFileUrl);
//			urlTask.setFileUrl(srcFileUrl);
//			urlTask.setHandleLine(0);
////			urlTask.setHandleNode(StaticValue.SERVER_NUMBER);
//			urlTask.setSubmittm(new Timestamp(new Date().getTime()));
//			urlTask.setUpdatetm(new Timestamp(new Date().getTime()));
//			urlTask.setStatus(1);
//			urlTask.setNetUrlId(0l);
//			Long urlTaskID = baseBiz.addObjReturnId(urlTask);
//			urlTask.setId(urlTaskID);
//			
//			boolean re = timerBiz.execute(urlTask);
//			if (!re) {
//				EmpExecutionContext.error("短链接入库任务失败！！！");
//			}
//		} catch (Exception e) {
//			EmpExecutionContext.error(e, "短链接入库任务生成异常");
//		}
//	}
	/**
	 * 失败重提的方法add 2012.07.05
	 * 
	 * @param request
	 * @param response
	 */
	public void reSendSMS(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String opModule = StaticValue.SMS_BOX;
		String opSper = StaticValue.OPSPER;
		String opType = StaticValue.ADD;
		String opContent = "重新提交短信任务";
		String result = "";
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 短信任务id
		Long mtid = 0L;
		// 用户名
		String username = null;
		String lgguid = null;
		// 企业编码
		String corpcode = null;
		PrintWriter writer = null;
		try
		{
			writer = response.getWriter();
			// 短信任务id
			mtid = Long.parseLong(request.getParameter("mtid"));
			username = request.getParameter("lgusername");
			corpcode = request.getParameter("lgcorpcode");
			lgguid = request.getParameter("lgguid");
			// 根据任务id查出短信任务
			LfMttask lfMttask = baseBiz.getById(LfMttask.class, mtid);
			if(!txtfileutil.checkFile(lfMttask.getMobileUrl()))
			{
				// 不允许重新提交.
				writer.print("nofindfile");
				return;
			}
			if(lfMttask.getIsRetry() != null && lfMttask.getIsRetry() == 1)
			{
				writer.print("isretry");
				return;
			}
			// 为了避免用户在上次请求还未返回时刷新页面.再次点失败重发造成的重复发送问题.这儿加一个Session判断这种情况的发生.
			if(request.getSession(false).getAttribute("isretryMtId") != null)
			{
				String a = request.getSession(false).getAttribute("isretryMtId").toString();
				if(a.equals(mtid.toString()))
				{
					// 已经重发
					writer.print("isretry");
					return;
				}
			}
			request.getSession(false).setAttribute("isretryMtId", mtid);

			// 重新生成一条taskId
			Long taskId = GlobalVariableBiz.getInstance().getValueByKey("taskId", 1L);
			lfMttask.setTaskId(taskId);

			if(lfMttask.getIsReply() == 1)
			{
				SMParams smParams = new SMParams();
				smParams.setCodes(taskId.toString());
				smParams.setCodeType(5);
				smParams.setCorpCode(corpcode);
				smParams.setAllotType(1);
				smParams.setSubnoVali(false);
				smParams.setTaskId(taskId);
				if(lgguid != null)
				{
					smParams.setLoginId(lgguid);
				}
				// 循环尾号发送,需获取新的尾号
				ErrorCodeParam errorCodeParam = new ErrorCodeParam();
				LfSubnoAllotDetail subnoAllotDetail = GlobalVariableBiz.getInstance().getSubnoDetail(smParams, errorCodeParam);
				if(errorCodeParam.getErrorCode() != null && "EZHB237".equals(errorCodeParam.getErrorCode()))
				{
					writer.print("noUsedSubNo");
					return;
				}
				else if(errorCodeParam.getErrorCode() != null && "EZHB238".equals(errorCodeParam.getErrorCode()))
				{
					writer.print("noSubNo");
					return;
				}
				if(subnoAllotDetail == null || subnoAllotDetail.getUsedExtendSubno() == null)
				{
					writer.print("noSubNo");
					return;
				}
				lfMttask.setSubNo(subnoAllotDetail.getUsedExtendSubno());
			}
			// 定时发送变成实时发送
			if(lfMttask.getTimerStatus() == 1)
			{
				lfMttask.setTimerStatus(0);
			}

			// 定时
			lfMttask.setTimerTime(new Timestamp(System.currentTimeMillis()));
			lfMttask.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			lfMttask.setSendstate(0);
			lfMttask.setMtId(null);
			// 需要将原来的文件重生产生一个。
			Date time = Calendar.getInstance().getTime();
			String[] url = txtfileutil.getSaveUrl(lfMttask.getUserId());
			String oldPath = txtfileutil.getWebRoot() + lfMttask.getMobileUrl();
			txtfileutil.copyFile(oldPath, url[0]);
			lfMttask.setMobileUrl(url[1]);
			// 调用发送方法
			// 获取发送信息等缓存数据（是否计费、是否审核、用户编码）
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			// 创建短信任务、判断审核流、扣费、判断创建定时任务、调用网关发送
			result = new SmsSendBiz().addSmsLfMttask(lfMttask, infoMap);
			String reultClong = result;

			if(!"createSuccess".equals(result) && !"000".equals(result) && !"saveSuccess".equals(result) && !"timerSuccess".equals(result) && !"timerFail".equals(result) && !"false".equals(result) && !"nomoney".equals(result))
			{
				// 根据错误编码得到错误信息
				result = new WGStatus("zh_CN").getInfoMap().get(result);
					//new WGStatus().infoMap.get(result);
				if(result == null)
				{
					result = reultClong;
				}
			}

			if(!"false".equals(result) && !"nomoney".equals(result))
			{
				// 将原来的设置 为已重新提交.
				objectMap.put("isRetry", "1");
				conditionMap.put("mtId", mtid.toString());
				baseBiz.update(LfMttask.class, objectMap, conditionMap);
			}
			else
			{
				request.getSession(false).setAttribute("isretryMtId", "");
			}

			opContent += "(任务名称:" + lfMttask.getTitle() + ")";
			// 存日志
			spLog.logSuccessString(username, opModule, opType, opContent, corpcode);

		}
		catch (Exception e)
		{
			if(e instanceof HttpHostConnectException)
			{
				result = e.getLocalizedMessage();
				spLog.logSuccessString(username, opModule, opType, opContent, corpcode);
			}
			else
			{
				result = "error";
				spLog.logFailureString(username, opModule, opType, opContent + opSper, e, corpcode);
			}
			try
			{
				// 将原来的设置 为已重新提交.
				objectMap.put("isRetry", "1");
				conditionMap.put("mtId", mtid.toString());
				baseBiz.update(LfMttask.class, objectMap, conditionMap);
			}
			catch (Exception e2)
			{
				result = "error";
				EmpExecutionContext.error(e, "设置为已重新提交异常!");
			}

			EmpExecutionContext.error(e, "重新提交短信任务失败，EBFB010");
		}
		finally
		{
			writer.print(result);
		}

	}

	/**
	 * 读取用来存储预览十条信息的文件并显示
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void readSmsContent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String url = "";
		BufferedReader reader = null;
		try
		{
			PrintWriter writer = response.getWriter();
			// 预览文件地址
			url = request.getParameter("url");
			// 地址处理
			url = txtfileutil.getPhysicsUrl(url);
			// 发送类型
			String sendType = request.getParameter("sendType");
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(url)), "GBK"));
			String tmp = "";
			String phoneStr = "";
			//
			String smsContent = "";
			int index;
			int x = 0;
			CommonVariables cv = new CommonVariables();
			Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
			int ishidephome = 0;
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
			{
				ishidephome = 1;
			}
			// 动态模板短信发送
			if("2".equals(sendType))
			{
				writer.print("<thead><tr align='center'><th>编号</th><th><center><div style='width:89px'>手机号码</div></center></th>" + "<th>文件内参数</th><th>文件内参数个数</th>" + "<th>短信内容</th><th>手机预览</th></tr></thead><tbody>");
				// 逐行读取
				while((tmp = reader.readLine()) != null)
				{
					x++;
					tmp = tmp.trim();
					// 内容截取
					String[] snum = tmp.split("MWHS]#");
					// 手机号特殊处理
					String phone = snum[2] != null ? snum[2] : "";
					if(phone != null && !"".equals(phone) && ishidephome == 0)
					{
						phone = cv.replacePhoneNumber(phone);
					}
					writer.print("<tr align ='center'><td>" + x + "</td><td>" + phone + "</td>" + "<td>" + snum[0] + "</td><td>" + snum[1] + "</td>" + "<td><xmp style='word-break: break-all;white-space:normal;'>" + snum[3] + "</xmp></td><td><label><a href='javascript:showUrl();'>预览</a></label></td></tr>");
				}
				writer.print("</tbody>");
			}
			// 文件内容短信发送
			else
			{
				writer.print("<thead><tr align='center'><th>编号</th>" + "<th><center><div style='width:89px'>手机号码</div></center></th>" + "<th>短信内容</th><th>手机预览</th></thead><tbody>");
				while((tmp = reader.readLine()) != null)
				{
					x++;
					tmp = tmp.trim();
					index = tmp.indexOf("MWHS]#");
					phoneStr = tmp.substring(0, index);
					smsContent = tmp.substring(index + 6).trim();
					// 手机号特殊处理
					String phone = phoneStr != null ? phoneStr : "";
					if(phone != null && !"".equals(phone) && ishidephome == 0)
					{
						phone = cv.replacePhoneNumber(phone);
					}
					writer.print("<tr align ='center'><td >" + x + "</td><td>" + phone + "</td>" + "<td ><xmp style='word-break: break-all;white-space:normal;'>" + smsContent + "</xmp></td><td><label><a href='javascript:showUrl();'>预览</a></label></td></tr>");
				}
				writer.print("</tbody>");
			}
//			reader.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "不同内容群发，读取用于存储预览信息的文件失败！url:" + url);
		}finally{
			if(reader != null){
				try{
					reader.close();
				}catch(IOException e){
					EmpExecutionContext.error(e, "reader关闭异常");
				}
			}
		}
	}

}
