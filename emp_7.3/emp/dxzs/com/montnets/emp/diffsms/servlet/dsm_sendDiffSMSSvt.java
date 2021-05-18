package com.montnets.emp.diffsms.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.conn.HttpHostConnectException;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.SmsSendBiz2;
import com.montnets.emp.common.constant.CommonVariables;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.WGStatus;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.dxzs.LfDfadvanced;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

@SuppressWarnings("serial")
public class dsm_sendDiffSMSSvt extends BaseServlet
{

	final HttpSmsSend		smsSend		= new HttpSmsSend();

	final BaseBiz			baseBiz		= new BaseBiz();

	final CommonBiz		commonBiz	= new CommonBiz();

	final SuperOpLog		spLog		= new SuperOpLog();

	final SmsBiz			smsBiz		= new SmsBiz();

	// 常用文件读写工具类
	final TxtFileUtil		txtfileutil	= new TxtFileUtil();

	final BalanceLogBiz	balancebiz	= new BalanceLogBiz();

	private final PhoneUtil phoneUtil = new PhoneUtil();
	
	// 换行符
	final String			line		= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

	final String			empRoot		= "dxzs";

	final String			basePath	= "/diffsms";

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
			
			// 企业是否启用分批设置
            conditionMap.clear();
            conditionMap.put("corpCode", corpCode);
            conditionMap.put("paramKey", "sms.split");
            List<LfCorpConf> corpConf = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);
            if (corpConf != null && !corpConf.isEmpty()) {
                request.setAttribute("confSmsSplit", corpConf.get(0).getParamValue());
            } else {
                request.setAttribute("confSmsSplit", "0");
            }
            
			//用户的请求地址
			String s = request.getRequestURI();
			//是短信客服模块的相同内容还是短信助手模块
			String hjsp = s.substring(s.lastIndexOf("_")+1,s.lastIndexOf("."));
			request.setAttribute("reTitle", hjsp);
			
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
	public void send(HttpServletRequest request, HttpServletResponse response) throws Exception {

		try {
			// 获取分批设置
			String splitFlag = request.getParameter("splitFlag");
			String batchNodeNumStr = request.getParameter("batchNodeNum");
			batchNodeNumStr = (null != batchNodeNumStr && !"".equals(batchNodeNumStr.trim())) ? batchNodeNumStr : "0";
			int batchNodeNum = Integer.parseInt(batchNodeNumStr);
			// 未分批，走以前短信发送逻辑
			if (0 == batchNodeNum || splitFlag == null || "0".equals(splitFlag)) {
				sendDiffSMS(request, response);
			} else if (0 < batchNodeNum && 20 >= batchNodeNum) {
				// 分批限制最多分批20
				List<String> batchNodeList = new ArrayList<String>();
				// 遍历每个批次数量
				for (int i = 0; i < batchNodeNum; i++) {
					String batchNodeValueStr = request.getParameter("batchNode" + i);
					String batchNodeTimeValueStr = request.getParameter("batchNodeTimeValue" + i);
					// batchNodeTimeValueStr = (null != batchNodeTimeValueStr &&
					// !"".equals(batchNodeTimeValueStr.trim())) ?
					// batchNodeTimeValueStr : new
					// SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
					batchNodeValueStr = (null != batchNodeValueStr && !"".equals(batchNodeValueStr.trim())) ? batchNodeValueStr : "0";
					// 每个批次数量
					Integer batchNodeValue = Integer.parseInt(batchNodeValueStr);
					batchNodeList.add(batchNodeValue + "," + batchNodeTimeValueStr);
				}
				// 分批发送
				try {
					batchSendSMS(request, response, batchNodeList);
				} catch (Exception e) {
					EmpExecutionContext.error(e, "不同内容分批发送异常！");
				}

			} else {
				// 非法分批设置，不予处理
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "不同内容分批发送异常！");
		}

	}

	private void batchSendSMS(HttpServletRequest request, HttpServletResponse response, List<String> batchNodeList) {

		String result = "";
		// 任务主题
		String title = request.getParameter("taskname");
		// 任务ID
		String taskId = request.getParameter("taskId");
		// 当前登录操作员id
		// String lguserid = request.getParameter("lguserid");
		// 漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		// 草稿箱id
		String draftId = request.getParameter("draftId");
		// 提交类型
		String bmtType = request.getParameter("bmtType");
		// 发送账号
		String spUser = request.getParameter("spUser");
		// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
		String sendType = request.getParameter("sendType");
		EmpExecutionContext.info("[OTHER] mod：不同内容群发，发送servlet接收参数，userid:" + lguserid + "，corpCode:" + lgcorpcode + "，taskId:" + taskId + "，sendType:" + sendType + "，bmtType:" + bmtType + "，spUser:" + spUser);
		// 主题为默认时,直接返回(防止重发)
		if (title != null && "不作为短信内容发送".equals(title.trim())) {
			EmpExecutionContext.error("不同内容发送获取参数异常，" + "title:" + title + "，taskId：" + taskId + "，errCode:" + IErrorCode.V10001);
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
		} else {
			SmsSendBiz2 smsSendBiz = new SmsSendBiz2();
			// 提交发送
			result = smsSendBiz.send(request, response, batchNodeList);

			// 删除草稿箱，如果有的话
			smsSendBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
		}
		try {
			String s = request.getRequestURI();

			request.getSession(false).setAttribute("eq_diffResult", result);
			// 重定向
			s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode + "&oldTaskId=" + taskId + "&t=" + System.currentTimeMillis();
			response.sendRedirect(s);
		} catch (Exception e) {
			EmpExecutionContext.error(e, lguserid, lgcorpcode);
		}

	}

	private void sendDiffSMS(HttpServletRequest request, HttpServletResponse response) {
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
			String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
			result = ErrorCodeInfo.getInstance(langName).getErrorInfo(IErrorCode.V10001);
			EmpExecutionContext.error(result);
		}
		else
		{
			SmsSendBiz smsSendBiz = new SmsSendBiz();
			//提交发送
			result = smsSendBiz.send(request, response);
			
			//删除草稿箱，如果有的话
			smsSendBiz.dealDrafts(draftId, result, lguserid, lgcorpcode);
		}
		try
		{
			String s = request.getRequestURI();

			request.getSession(false).setAttribute("eq_diffResult", result);
			// 重定向
			s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode +"&oldTaskId=" + taskId + "&t=" + System.currentTimeMillis();
			response.sendRedirect(s);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, lguserid, lgcorpcode);
		}
	}

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
//			Date time = Calendar.getInstance().getTime();
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
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
				// 根据错误编码得到错误信息
				result = new WGStatus(langName).getInfoMap().get(result);
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
			//if(e.getClass().getName().equals("org.apache.http.conn.HttpHostConnectException"))
			if(HttpHostConnectException.class.isInstance(e))
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
		String serialNumber = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_101",request);
		String phoneNumber = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_119",request);
		String yys = MessageUtils.extractMessage("dxzs","dxzs_xtnrqf_title_178",request);
		String SMSContent = MessageUtils.extractMessage("dxzs","dxzs_sendDiffSMSS_text_1",request);
		String paramInFile = MessageUtils.extractMessage("dxzs","dxzs_sendDiffSMSS_text_2",request);
		String paramAmount = MessageUtils.extractMessage("dxzs","dxzs_sendDiffSMSS_text_3",request);
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
			String spUser = request.getParameter("spUser");
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
				writer.print("<thead><tr align='center'><th width='30px'>"+serialNumber+"</th><th><center><div style='width:89px'>"+phoneNumber+"</div></center></th><th width='40px'>"+yys + "</th><th width='80px'>"+paramInFile+"</th><th width='50px'>"+paramAmount+"</th>" + "<th>"+SMSContent+"</th></tr></thead><tbody>");
				// 逐行读取
				while((tmp = reader.readLine()) != null)
				{
					x++;
					tmp = tmp.trim();
					// 内容截取
					String[] snum = tmp.split("MWHS]#");
					// 手机号特殊处理
					String phone = snum[2] != null ? snum[2] : "";
					int rs = phoneUtil.getPhoneType(phone);
					if(phone != null && !"".equals(phone) && ishidephome == 0)
					{
						phone = cv.replacePhoneNumber(phone);
					}
					XtGateQueue xtGateQueue = smsBiz.getXtGateQueue(spUser, rs + "");
					
					String operator = "";
					if (0 == rs) {
						operator = "移动";
					} else if (1 == rs) {
						operator = "联通";
					} else if (21 == rs) {
						operator = "电信";
					} else if (5 == rs) {
						operator = "国外";
					} else {
						operator = "其他";
					}
					
					String html = "<tr align ='center'><td>" + x + "</td><td>" + phone+"</td><td>"+operator + "</td>" + "<td><xmp style='word-break: break-all;white-space:normal;'>" + snum[0] + "</xmp></td><td>" + snum[1] + "</td>" + "<td><xmp style='word-break: break-all;white-space:normal;'>";
					
					if(xtGateQueue != null){
						String signstr = (null != xtGateQueue.getSignstr() && !"".equals(xtGateQueue.getSignstr().trim()) ? xtGateQueue.getSignstr() : (null != xtGateQueue.getEnsignstr() && !"".equals(xtGateQueue.getEnsignstr())?xtGateQueue.getEnsignstr():""));
						if(xtGateQueue.getGateprivilege() != null && (xtGateQueue.getGateprivilege()&4)==4){
							html += (signstr + snum[3]);
						}else{
							html += (snum[3] + signstr);
						}
					}else{
						html += snum[3];
					}
					
					html += "</xmp></td></tr>";
					
					writer.print(html);
				}
				writer.print("</tbody>");
			}
			// 文件内容短信发送
			else
			{
				writer.print("<thead><tr align='center'><th>"+serialNumber+"</th>" + "<th><center><div style='width:89px'>"+phoneNumber+"</div></center></th><th>"+yys + "</th><th>"+SMSContent+"</th></thead><tbody>");
				while((tmp = reader.readLine()) != null)
				{
					x++;
					tmp = tmp.trim();
					index = tmp.indexOf("MWHS]#");
					phoneStr = tmp.substring(0, index);
					smsContent = tmp.substring(index + 6).trim();
					// 手机号特殊处理
					String phone = phoneStr != null ? phoneStr : "";
					int rs = phoneUtil.getPhoneType(phone);
					if(phone != null && !"".equals(phone) && ishidephome == 0)
					{
						phone = cv.replacePhoneNumber(phone);
					}
					if(phone != null && !"".equals(phone) && ishidephome == 0)
					{
						phone = cv.replacePhoneNumber(phone);
					}
					XtGateQueue xtGateQueue = smsBiz.getXtGateQueue(spUser, rs + "");
					String operator = "";
					if (0 == rs) {
						operator = "移动";
					} else if (1 == rs) {
						operator = "联通";
					} else if (21 == rs) {
						operator = "电信";
					} else if (5 == rs) {
						operator = "国外";
					} else {
						operator = "其他";
					}
					String html = "<tr align ='center'><td>" + x + "</td><td>" + phone + "</td><td>"+operator + "</td><td><xmp style='word-break: break-all;white-space:normal;'>";
					
					if(xtGateQueue != null){
						String signstr = (null != xtGateQueue.getSignstr() && !"".equals(xtGateQueue.getSignstr().trim()) ? xtGateQueue.getSignstr() : (null != xtGateQueue.getEnsignstr() && !"".equals(xtGateQueue.getEnsignstr())?xtGateQueue.getEnsignstr():""));
						if(xtGateQueue != null && xtGateQueue.getGateprivilege() != null && (xtGateQueue.getGateprivilege()&4)==4){
							html += (signstr + smsContent);
						}else{
							html += (smsContent + signstr);
						}
					}else{
						html += smsContent;
					}
					html += "</xmp></td></tr>";
					writer.print(html);
				}
				writer.print("</tbody>");
			}
			reader.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "不同内容群发，读取用于存储预览信息的文件失败！url:" + url);
		}finally{
			if(reader != null){
				try{
					reader.close();
				}catch(Exception e){
					EmpExecutionContext.error(e, "关闭资源异常");
				}
			}
		}
	}
	
}
