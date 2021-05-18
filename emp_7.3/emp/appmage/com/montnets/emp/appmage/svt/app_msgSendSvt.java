/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-12 上午08:50:44
 */
package com.montnets.emp.appmage.svt;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.tools.SysuserUtil;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.montnets.emp.appmage.biz.app_msgSendBiz;
import com.montnets.emp.appmage.util.FFmpegKit;
import com.montnets.emp.appwg.biz.WgMwCommuteBiz;
import com.montnets.emp.common.atom.SendSmsAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.app.LfDfadvanced;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @description APP信息发送
 * @project p_appmage
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-12 上午08:50:44
 */

public class app_msgSendSvt extends BaseServlet
{

	private static final String				empRoot				= "appmage";

	private static final CommonBiz			commonBiz			= new CommonBiz();

	private static final app_msgSendBiz			msgSendBiz			= new app_msgSendBiz();
	
	private static final SmsBiz						smsBiz				= new SmsBiz();
	
	private static final BaseBiz						baseBiz				= new BaseBiz();

	/**
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-13 下午04:15:59
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * 
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-21 下午04:22:48
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		String lgcorpcode = null;
		String lguserid = null;
		String lgguid = null;
		LfSysuser sysUser = null;
		try
		{
			// 企业编码
			lgcorpcode = request.getParameter("lgcorpcode");
			// 用户编号
			//lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			lguserid = SysuserUtil.strLguserid(request);



			lgguid = request.getParameter("lgguid");
			if(lgcorpcode != null && lguserid != null && lgguid != null)
			{
				// 获取APP企业账号
				//List<DynaBean> appCorpCodeList = msgSendBiz.getAppCorpCode(lgcorpcode);
				//String appAccount = appCorpCodeList.get(0).get("appaccount").toString();
				WgMwCommuteBiz wgbizBiz = new WgMwCommuteBiz();
				String appAccount = wgbizBiz.getAppECode();
				request.setAttribute("appAccount", appAccount != null?appAccount.toUpperCase():"");
				request.setAttribute("baseAppAccount", appAccount);
				// 产生taskId
				Long taskId = commonBiz.getAvailableTaskId();
				request.setAttribute("taskId", taskId.toString());
				
				// 获取GUID
				Long userGuid = Long.valueOf(lgguid);
				// 查询出登录对象
				sysUser = baseBiz.getByGuId(LfSysuser.class, userGuid);
				// 获取发送账号列表
				List<Userdata> userData = smsBiz.getSpUserList(sysUser);
				request.setAttribute("userData", userData);
				
				// 获取业务类型列表
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> orderByMap = new LinkedHashMap<String, String>();
				conditionMap.put("corpCode&in", "0," + lgcorpcode);
				orderByMap.put("corpCode", "asc");
				
				//设置启用查询条件
				conditionMap.put("state", "0");
				//设置查询手动和手动+触发
				conditionMap.put("busType&in", "0,2");
				
				List<LfBusManager> busList = baseBiz.getByCondition(LfBusManager.class, conditionMap, orderByMap);
				request.setAttribute("busList", busList);
				
				//获取高级设置默认信息
				conditionMap.clear();
				conditionMap.put("userid", String.valueOf(sysUser.getUserId()));
				//6:APP消息发送
				conditionMap.put("flag", "6");
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
			else
			{
				EmpExecutionContext.error("APP信息发送find方法参数获取异常！lgcorpcode:" + lgcorpcode + "，lguserid:" + lguserid + "，lgguid：" + lgguid);
			}
			request.getRequestDispatcher(this.empRoot + "/sendapp/app_msgSend.jsp").forward(request, response);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "跳转APP信息发送页面出现异常！lgcorpcode:" + lgcorpcode + "，lguserid:" + lguserid);
			try
			{
				request.getRequestDispatcher(this.empRoot + "/sendapp/app_msgSend.jsp").forward(request, response);
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1, lguserid, lgcorpcode);
			}
		}

	}

	/**
	 * 预览
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-26 下午02:29:50
	 */
	public void preview(HttpServletRequest request, HttpServletResponse response)
	{
		// 当前登录操作员id
		//String lgUserId = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lgUserId = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgCorpCode = request.getParameter("lgcorpcode");
		//APP企业编号
		String appAccount = request.getParameter("appaccount");
		// 发送客户账号
		String appCode = request.getParameter("appcode");
		// 发送客户群组
		String group = request.getParameter("group");
		// 发送APP企业账号
		String appCorpCode = request.getParameter("appcorpcode");
		//返回信息
		StringBuffer result = new StringBuffer();
		
		try
		{
			//获取APP企业发送客户账号
			if(appCorpCode != null && appCorpCode.length() > 0 && !",".equals(appCorpCode))
			{
				//去掉前面和最后一个逗号
				appCorpCode = appCorpCode.substring(1, appCorpCode.length()-1);
				appCode += msgSendBiz.getAccountByAppCorpCode(appCorpCode);
			}
			// 获取APP客户群组发送客户账号
			if(group.length() > 0 && !",".equals(group))
			{
				//去掉前面和最后一个逗号
				group = group.substring(1, group.length()-1);
				// 通过机构id查找电话
				appCode += msgSendBiz.getAccountByGroup(group, appAccount);
			}
			if(appCode == null || appCode.length() < 1 || ",".equals(appCode))
			{
				result.append("请选择发送对象！");
				return;
			}
			//客户号信息:有效客户+非法客户数
			String failCode = msgSendBiz.getAppCodeFailCount(appCode);
			//没有有效客户数
			if(Integer.parseInt(failCode.split("@@")[0]) < 1)
			{
				result.append("没有有效的发送对象！");
				return;
			}
			
			LfSysuser curSysuser= new LfSysuser();
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				 curSysuser=(LfSysuser)loginSysuserObj;
			}else{
				// 当前登录操作员id
				 curSysuser = baseBiz.getLfSysuserByUserId(lgUserId);//当前登录操作员对象
			}
			//SP账号
			String spUser = request.getParameter("spUser");
			//检查发送比较SP账号是否属于登录企业（防止攻击）
			boolean checkFlag= new CheckUtil().checkSysuserInCorp(curSysuser, lgCorpCode, spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("APP发送预览时，检查操作员和发送账号是否是当前企业下，checkFlag:"+checkFlag
											+"，userid:"+curSysuser.getUserId()
											+"，spuser:"+spUser);
				return;
			}
			
			//返回客户号信息
			result.append(failCode);
			//发送类型
			String sendTypeVal = request.getParameter("sendTypeVal");
			//是否同时发送短信
			String isSmsSend = request.getParameter("isSmsSend");
			//短信预览状态
			String status = "noSendSms";
			if(sendTypeVal != null && isSmsSend != null && "0".equals(sendTypeVal) && "1".equals(isSmsSend))
			{
				status = SmsSendPreview(appCode, request, response);
			}
			result.append("@@").append(status);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP发送预览，获取发送用户信息异常！lgCorpCode：" + lgCorpCode+"，lgUserId："+lgUserId);
			result.append("发送预览失败！");
			return;
		}finally
		{
			try
			{
				response.getWriter().print(result.toString());
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e, "APP发送预览跳转界面异常！lgCorpCode：" + lgCorpCode+"，lgUserId："+lgUserId);
			}
		}
		
	}
	
	/**
	 * APP消息短信发送预览
	 * @description
	 * @param appCode
	 * @param request
	 * @param response
	 * @return  成功返回预览对象JSON;1:异常错误;2:获取发送文件路径失败;3:没有提交号码;4:获取预发送条数异常;5:机构扣费失败 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-27 下午05:57:09
	 */
	@SuppressWarnings("unchecked")
	public String SmsSendPreview(String appCode, HttpServletRequest request, HttpServletResponse response)
	{
		String result = "1";
		// 当前登录操作员id
		//String lgUserIdStr = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lgUserIdStr = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgCorpCode = request.getParameter("lgcorpcode");
		//短信内容
		String sendContent = request.getParameter("sendContent");
		//替换短信内的特殊字符
		sendContent = smsBiz.smsContentFilter(sendContent);
		//SP账号
		String spUser = request.getParameter("spUser");
		//业务类型
		String busCode = request.getParameter("busCode");
		// 获取运营商号码段
		String[] haoduan = new WgMsgConfigBiz().getHaoduan();
		// 预览参数传递变量类
		PreviewParams preParams = new PreviewParams();
		try
		{
			Long lgUserId = Long.valueOf(lgUserIdStr);
			// 获取号码文件url
			String[] phoneFilePath = new TxtFileUtil().getSaveUrl(lgUserId);
//			if (phoneFilePath == null || phoneFilePath.length < 5)
			if (phoneFilePath == null)
			{
				EmpExecutionContext.error("APP消息短信发送预览获取发送文件路径失败。操作员："+ lgUserIdStr +"，错误码:" + IErrorCode.V10013);
				return "2";
			} else
			{
				preParams.setPhoneFilePath(phoneFilePath);
			}
			//解析发送号码
			new SendSmsAtom().parseAddrAndInputPhone(preParams, "", appCode, "", lgCorpCode, haoduan, busCode,request);
			
			//没有提交号码
			if(preParams.getSubCount() == 0){
				EmpExecutionContext.info("APP消息短信发送没有提交号码。");
				return "3";
			}
			
			//处理预览号码内容，有值则去掉最后一个分号
			String previewPhone = preParams.getPreviewPhone();
			if (previewPhone.lastIndexOf(";") > 0)
			{
				previewPhone = previewPhone.substring(0, previewPhone.lastIndexOf(";"));
				preParams.setPreviewPhone(previewPhone);
			}
			previewPhone = null;
			
			// 机构余额
			Long depFeeCount = -1L;
			//预发送条数
			int preSendCount = -1;

			//获取预发送条数
			preSendCount = smsBiz.getCountSmsSend(spUser, sendContent, preParams.getOprValidPhone());
			if(preSendCount < 0)
			{
				EmpExecutionContext.error("APP消息短信发送预览获取预发送条数异常。错误码：" + IErrorCode.B20006);
				return "4";
			}
			
			//检查运营商余额
			BalanceLogBiz balancebiz = new BalanceLogBiz();

			
			//判断是否需要机构计费			
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			// 如果启用了计费则为true;未启用则为false;
			boolean isCharge = false;
			if(infoMap == null)
			{
				new CommonBiz().setSendInfo(request, response, lgCorpCode, lgUserId);
				infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			}
			if("true".equals(infoMap.get("feeFlag")))
			{
				isCharge = true;
			}
			//机构、SP账号检查余额标识，只要有其一个失败，则为false，之后的扣费将不再执行
			boolean isAllCharge = true;
			//启用计费、运营商余额大于此次预发送条数或者运营商不计费时，查询机构余额
			if (isCharge)
			{
				//获取机构余额
				LfSysuser lfSysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				depFeeCount = balancebiz.getAllowSmsAmount(lfSysuser);
				if(depFeeCount == null || depFeeCount < 0)
				{
					return "5";
				}
				//机构余额小于发送条数
				if(depFeeCount - preSendCount < 0)
				{
					//设置检查标识为失败
					isAllCharge = false;
				}	
			}
			//----增加SP账号检查---
			// SP账号余额
			Long spUserAmount = -1L;
			Long feeFlag=2L;
			if(isAllCharge){
				//获取SP账号类型
				 feeFlag=balancebiz.getSpUserFeeFlag(spUser,1);
				if(feeFlag == null || feeFlag < 0)
				{
					EmpExecutionContext.error("APP消息短信发送预览， 获取SP账号计费类型出现异常！spUser="+spUser);
				}else if(feeFlag ==1){
					//----增加SP账号检查---
					 spUserAmount=balancebiz.getSpUserAmount(spUser);
					if(spUserAmount != null)
					{
						if(depFeeCount - spUserAmount < 0)
						{
							//设置检查标识为失败
							isAllCharge = false;
						}
					}
					//设置检查标识为失败

					EmpExecutionContext.info("APP消息短信发送预览， SP账号计费类型为预付费,spUser="+spUser);
				}else if(feeFlag ==2){
					EmpExecutionContext.info("APP消息短信发送预览， SP账号计费类型为后付费,spUser="+spUser);
				}
			}
			String spFeeResult ="";
			if(isAllCharge){
				 spFeeResult = balancebiz.checkGwFee(spUser, preSendCount,lgCorpCode, 1);
			}
//			preParams.setFeeFlag(feeFlag);
			//预览信息 json
			Map<String,Object> objMap = new HashMap<String,Object>();
			objMap.put("preSendCount", preSendCount);
			objMap.put("depFeeCount", depFeeCount);
			objMap.put("isCharge", isCharge);
			objMap.put("spFeeResult", spFeeResult);
			//修改传值方式
			objMap.put("spUserAmount", spUserAmount);
			objMap.put("feeFlag", feeFlag);
			return preParams.getJsonStr(objMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "APP消息短信发送预览异常！");
			return result;
		}
	}
	/**
	 * APP消息发送
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-21 下午04:22:38
	 */
	public void send(HttpServletRequest request, HttpServletResponse response)
	{
		// 提交发送
		String result = msgSendBiz.appMsgSend(request, response);
		// 消息类型，0-文字；1-多媒体
		String msgType = request.getParameter("sendType");
		//是否同时发送短信
		String isSmsSend = request.getParameter("hidisSmsSend");
		String smsResult = "noSendSms";
		// 当前登录操作员id
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		// 当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		
		//检查发送比较SP账号是否属于登录企业（防止攻击）
		LfSysuser curSysuser= null;
		Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
		if(loginSysuserObj!=null){
			 curSysuser=(LfSysuser)loginSysuserObj;
		}else{
			 try {
				curSysuser = baseBiz.getLfSysuserByUserId(lguserid);
			} catch (Exception e) {
				EmpExecutionContext.error("获取当前登录人异常，" + "lguserid:" + lguserid );
			}//当前登录操作员对象
		}
		// 发送账号
		String spUser = request.getParameter("spUser");
		boolean checkFlag= new CheckUtil().checkSysuserInCorp(curSysuser, lgcorpcode, spUser, null);
		if(!checkFlag)
		{
			EmpExecutionContext.error("app发送时，检查操作员和发送账号是否是当前企业下，checkFlag:"+checkFlag
										+"，userid:"+(curSysuser!=null? curSysuser.getUserId():"")
										+"，spuser:"+spUser);
			return;
		}
		if(msgType != null && "0".equals(msgType) && isSmsSend != null && "1".equals(isSmsSend))
		{
			//短信发送
			smsResult = msgSendBiz.appSmsSend(request, response);
		}
		
		String lgguid = request.getParameter("lgguid");
		try
		{
			String s = request.getRequestURI();

			request.getSession(false).setAttribute("app_sendResult", result+"@@"+smsResult);
			// 重定向
			s = s + "?method=find&lguserid=" + lguserid + "&lgcorpcode=" + lgcorpcode + "&lgguid=" + lgguid +"&t=" + System.currentTimeMillis();
			response.sendRedirect(s);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, lguserid, lgcorpcode);
		}
	}
	/**
	 * 获取APP客户群组
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 上午10:32:59
	 */
	public void getAppGroupList(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		// 企业编码
		String lgcorpcode = request.getParameter("lgcorpcode");
		//APP企业编号
		String appAccount = request.getParameter("appaccount");
		if(lgcorpcode == null || "".equals(lgcorpcode) || appAccount == null || "".equals(appAccount))
		{
			EmpExecutionContext.error("APP消息发送查询APP客户群组获取参数异常，lgcorpcode：" + lgcorpcode + "，appAccount：" + appAccount);
			response.getWriter().print("");
			return;
		}
		StringBuffer buffer = new StringBuffer("");
		try
		{
			//未分组成员数
			List<DynaBean> noGroupAppCountList = msgSendBiz.getNoGroupAppCount(appAccount);
			Integer noGroupCount = 0;
			//拼成html代码返回
			buffer.append("<select select-one name='groupList' id='groupList' " +
			"size='15' style='height: 240px; width: 240px; border: 0;color: black;font-size: 12px;'");
			buffer.append(" onclick='getAppMembers()' ondblclick='router()'>");
			if(noGroupAppCountList != null && noGroupAppCountList.size() > 0)
			{
				noGroupCount = noGroupAppCountList.size();
			}
			buffer.append("<option mcount='").append(noGroupCount.toString()).append("' value='").append("-1").append("'>")
			.append("未分组用户").append("</option>");
			
			List<DynaBean> appGroupList = msgSendBiz.getAppGroupList(lgcorpcode);
			if (appGroupList != null && appGroupList.size() > 0) 
			{
				String mcount="0";
				for(DynaBean appGroup : appGroupList)
				{
					mcount = appGroup.get("mcount").toString();
					mcount = mcount == null ? "0" : mcount;
					buffer.append("<option mcount='").append(mcount).append("' value='").append(appGroup.get("groupid").toString()).append("'>")
					.append(appGroup.get("groupname").toString().replace("<","&lt;").replace(">","&gt;")).append("</option>");
				}
			}
			buffer.append("</select>");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取APP客户群组信息异常！");
		}
		response.getWriter().print(buffer.toString());
	}
	
	/**
	 * 获取APP企业账号
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 下午07:02:05
	 */
	public void getAppCorpCodeList(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		StringBuffer buffer = new StringBuffer("");
		try
		{
			// 获取APP企业账号
			List<DynaBean> appCorpCodeList = msgSendBiz.getAppCorpCode(null);
			if (appCorpCodeList != null && appCorpCodeList.size() > 0) 
			{
				//拼成html代码返回
				buffer.append("<select select-one name='corpList' id='corpList' " +
						"size='15' style='height: 240px; width: 240px; border: 0;color: black;font-size: 12px;'");
				buffer.append(" onclick='getAppMembers()' ondblclick='router()'>");
				String mcount = "0";
				for(DynaBean appCorpCode : appCorpCodeList)
				{
					mcount = appCorpCode.get("mcount").toString();
					mcount = mcount == null ? "0" : mcount;
					buffer.append("<option mcount='").append(mcount).append("' value='").append(appCorpCode.get("appaccount").toString().toUpperCase())
					.append("'>").append(appCorpCode.get("corpname").toString().replace("<","&lt;").replace(">","&gt;")).append("</option>");
					}
				buffer.append("</select>");
			}else
			{
				buffer.append("");
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取APP企业帐号信息异常！");
		}
		response.getWriter().print(buffer.toString());
	}
	
	/**
	 * 获取APP发送客户信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 下午01:45:33
	 */
	public void getAppMembers(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//APP企业编号
			String appAccount = request.getParameter("appaccount");
			//群组ID或APP企业ID
			String id = request.getParameter("id");
			//选择类型,1:群组;2:APP企业
			String chooseType = request.getParameter("chooseType");
			
			if(appAccount == null || "".equals(appAccount) || id == null || "".equals(id) 
					|| chooseType == null || (!"1".equals(chooseType) && !"2".equals(chooseType)))
			{
				EmpExecutionContext.error("获取APP发送客户账号参数异常，appAccount:"+appAccount+"，id:" + id +"，chooseType：" + chooseType);
				response.getWriter().print("");
				return;
			}
			
			PageInfo pageInfo = new PageInfo();
			// 页码
			String pageIndex1 = request.getParameter("pageIndex1");
			// 区分上一页还是下一页
			String opType = request.getParameter("opType");
			if(opType != null && opType.equals("goNext"))
			{
				pageInfo.setPageIndex(Integer.parseInt(pageIndex1) + 1);
			}
			else
			{
				if(opType != null && opType.equals("goLast"))
				{
					pageInfo.setPageIndex(Integer.parseInt(pageIndex1) - 1);
				}
				else
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 每页显示50条记录
			pageInfo.setPageSize(50);
			List<LfAppMwClient> lfAppMwClientList=msgSendBiz.getAppMembersList(chooseType, id, appAccount, pageInfo);
			
			StringBuffer sb = new StringBuffer();
			//拼成html代码返回
			if (lfAppMwClientList != null && lfAppMwClientList.size() > 0) {
				for (LfAppMwClient lfAppMwClient : lfAppMwClientList) 
				{
					sb.append("<option value='").append(lfAppMwClient.getWcid().toString())
					.append("' appcode='").append(lfAppMwClient.getAppcode().toString())
					.append("' group='").append("1".equals(chooseType)?id:"")
					.append("' appcorpcode='").append(lfAppMwClient.getEcode().toString())
					.append("'>").append(lfAppMwClient.getNickname()==null?"":lfAppMwClient.getNickname())
					.append("(").append(lfAppMwClient.getAppcode()).append(")").append("</option>");
				}
			}
			String pageStr = pageInfo.getTotalRec() + "@" + pageInfo.getTotalPage() + "@";
			response.getWriter().print(pageStr + sb.toString());
	
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取APP客户信息异常！");
		}
	}
	
	/**
	 * 获取所有群组或所有企业下的APP发送客户信息
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-18 下午01:45:33
	 */
	public void getAllAppMembers(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			//搜索名称
			String epname = request.getParameter("epname");
			//选择类型,1:群组;2:APP企业
			String chooseType = request.getParameter("chooseType");
			//APP企业编号
			String appAccount = request.getParameter("appaccount");
			if("1".equals(chooseType) && (appAccount == null || "".equals(appAccount)))
			{
				EmpExecutionContext.error("获取所有企业下APP发送客户账号参数异常，appAccount:" + appAccount +"，chooseType：" + chooseType);
				response.getWriter().print("");
				return;
			}
			//分页对象
			PageInfo pageInfo = new PageInfo();
			// 页码
			String pageIndex1 = request.getParameter("pageIndex1");
			// 区分上一页还是下一页
			String opType = request.getParameter("opType");
			if(opType != null && opType.equals("goNext"))
			{
				pageInfo.setPageIndex(Integer.parseInt(pageIndex1) + 1);
			}
			else
			{
				if(opType != null && opType.equals("goLast"))
				{
					pageInfo.setPageIndex(Integer.parseInt(pageIndex1) - 1);
				}
				else
				{
					pageInfo.setPageIndex(1);
				}
			}
			// 每页显示50条记录
			pageInfo.setPageSize(50);
			List<LfAppMwClient> lfAppMwClientList=msgSendBiz.getAllAppMembersList(appAccount, chooseType, epname, pageInfo);
			
			StringBuffer sb = new StringBuffer();
			//拼成html代码返回
			if (lfAppMwClientList != null && lfAppMwClientList.size() > 0) {
				for (LfAppMwClient lfAppMwClient : lfAppMwClientList) 
				{
					sb.append("<option value='").append(lfAppMwClient.getWcid().toString())
					.append("' appcode='").append(lfAppMwClient.getAppcode().toString())
					.append("' group='").append("")
					.append("' appcorpcode='").append(lfAppMwClient.getEcode().toString())
					.append("'>").append(lfAppMwClient.getNickname()==null?"":lfAppMwClient.getNickname())
					.append("(").append(lfAppMwClient.getAppcode()).append(")").append("</option>");
				}
			}
			String pageStr = pageInfo.getTotalRec() + "@" + pageInfo.getTotalPage() + "@";
			response.getWriter().print(pageStr + sb.toString());
	
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询所有群组或所有企业下的APP发送客户信息异常！");
		}
	}
	
	
	/**
	 * 上传文件
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-20 上午09:44:21
	 */
	@SuppressWarnings("unchecked")
	public void uploadFile(HttpServletRequest request, HttpServletResponse response)
	{
		//绝对路径
		//String absolutePath = request.getParameter("absolutePath");
		
		// 图片存放的根目录
		String baseFileDir = this.getServletContext().getRealPath(StaticValue.APP_SENDFILE);
		// 上传文件最大为5M
		long maxSize = (long)5 * 1024 * 1024;
		response.setContentType("text/html");
			// 构造出文件工厂，用于存放JSP页面中传递过来的文件
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// 设置缓存大小，如果文件大于缓存大小时，则先把文件放到缓存中
			factory.setSizeThreshold(1 * 1024 * 1024);
			// 设置上传文件的保存路径
			factory.setRepository(new File(baseFileDir));
			// 产生Servlet上传对象
			ServletFileUpload upload = new ServletFileUpload(factory);
			// 设置可以上传文件大小的上界10MB
			upload.setSizeMax((long)10 * 1024 * 1024);
			// 取得所有上传文件的信息
			List<FileItem> fileList = null;
			// 上传文件保存url路径
			String fileUrl = "other";

			Iterator<FileItem> iter = null;
			try
			{
				fileList = upload.parseRequest(request);
				iter = fileList.iterator();
				while(iter.hasNext()){
					FileItem item = (FileItem) iter.next();
					if(!item.isFormField() && item.getName().length() > 0){
						// 文件名
						String fileName = item.getName();
						// 获得文件大小
						long fileSize = item.getSize();
						// 判断文件大小是否超过限制
						if(fileSize > maxSize)
						{
							item.delete();
							item = null;
							fileUrl = "overSize";
							return;
						}

						String extName = fileName.substring(fileName.indexOf("."), fileName.length()).toLowerCase();
						String dirpath = baseFileDir + File.separator;
						Calendar cal = Calendar.getInstance();
						// 年
						int year = cal.get(Calendar.YEAR);
						// 月
						int month = cal.get(Calendar.MONTH) + 1;
						// 日
						int day = cal.get(Calendar.DAY_OF_MONTH);

						String filepath = dirpath + File.separator + year + File.separator + month + File.separator + day;
						File uf = new File(filepath);
						// 更改文件的保存路径，以防止文件重名的现象出现
						if(!uf.exists())
						{
							uf.mkdirs();
						}
						try
						{
							String strid = UUID.randomUUID().toString();
							String newFileName = strid.replaceAll("-", "")+ StaticValue.getServerNumber() + extName;
							File uploadedFile = new File(filepath, newFileName);
							if(uploadedFile.exists())
							{
								boolean delSuccess = uploadedFile.delete();
								if(!delSuccess){
									EmpExecutionContext.error("删除失败");
								}
							}
							item.write(uploadedFile);

							fileUrl = StaticValue.APP_SENDFILE + "/" + year + "/" + month + "/" + day + "/" + newFileName;
						}
						catch (Exception e)
						{
								EmpExecutionContext.error(e,"APP信息发送，上传多媒体文件至服务器异常！");
								fileUrl = "loadServerError";
								return;
						}
						
					}
				}
				String fileSuffix = fileUrl.substring(fileUrl.lastIndexOf(".")).toUpperCase();
				//绝对路径
				String absolutePath = request.getSession(false).getServletContext().getRealPath("/").replaceAll("\\\\","/");
				
				//格式转换状态
				boolean converState = false;
				File file = new File(absolutePath,fileUrl);
				String imgType = ".JPG.JPEG.PNG.BMP";
				//格式转换
				if(".3GP".equals(fileSuffix))
				{
					converState = FFmpegKit.converVideo(file.getAbsolutePath());
				}
				else if(".AMR".equals(fileSuffix))
				{
					converState = FFmpegKit.converAudio(file.getAbsolutePath());
				}
				else if(".MP4".equals(fileSuffix) || imgType.indexOf(fileSuffix) >= 0)
				{
					converState = true;
				}
				if(!converState)
				{
					EmpExecutionContext.error("文件格式转换异常，无法进行预览！fileUrl:"+fileUrl);
				}
			}
			catch (FileUploadException e)
			{
				EmpExecutionContext.error(e, "APP信息发送，多媒体文件表单上传异常！");
				fileUrl = "subError";
				return;
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"APP信息发送，上传多媒体文件异常！");
				fileUrl = "other";
				return;
			}finally
			{
				try
				{
					response.getWriter().print(fileUrl);
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "APP信息发送，多媒体文件表单上传跳转异常！");
				}
			}
	}
	
	/**
	 * 获取SP账号短信拆分长度
	 * @description    
	 * @param request
	 * @param response
	 * @throws IOException       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-8-30 下午04:05:37
	 */
	public void getGtInfo(HttpServletRequest request, HttpServletResponse response) throws IOException 
	{
		String spUser = request.getParameter("spUser"); 
		String[] gtInfos = new String[]{"","",""};
		int index = 0;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		
		String info = "infos:";
		try{
			List<GtPortUsed> gtPortsList = new WgMsgConfigBiz().getPortByUserId(spUser);
			for(GtPortUsed gtPort : gtPortsList)
			{
				index = gtPort.getSpisuncm() - 2 > 0 ? 2 : gtPort.getSpisuncm();

				maxLen = gtPort.getMaxwords();
				totalLen = gtPort.getMultilen1();
				lastLen = gtPort.getMultilen2();
				signLen = gtPort.getSignlen() == 0?gtPort.getSignstr().trim().length():gtPort.getSignlen();
				
				gtInfos[index] =  String.valueOf(maxLen)+","+String.valueOf(totalLen) +
						","+String.valueOf(lastLen)+","+String.valueOf(signLen);
			}
			info += gtInfos[0]+"&"+gtInfos[1]+"&"+gtInfos[2]+"&";
		}catch(Exception e)
		{
			info = "error";
			EmpExecutionContext.error(e,"获取发送账户绑定的通道信息异常！");
		}finally
		{
			response.getWriter().print(info);
		}
	}
	
	/**
	 * 获取通道信息,包括英文短信配置参数
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getSpGateConfig(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String spUser = request.getParameter("spUser"); 
		String[] gtInfos = new String[]{"","","",""};
		int index = 0;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		int enmaxLen;
		int entotalLen;
		int enlastLen;
		int ensignLen;
		int gateprivilege = 0;
		int gatepri;
		//签名位置,1:前置;0:后置
		int signLocation = 0;
		int ensinglelen;
		int msgLen = 990;
		
		String info = "infos:";
		try{
			// 根据发送账号获取路由信息
			List<DynaBean> spGateList = smsBiz.getSpGateInfo(spUser);
			for (DynaBean spGate : spGateList)
			{
				gateprivilege = 0;
				//中文短信配置参数
				maxLen = Integer.parseInt(spGate.get("maxwords").toString());
				totalLen = Integer.parseInt(spGate.get("multilen1").toString());
				lastLen = Integer.parseInt(spGate.get("multilen2").toString());
				signLen = Integer.parseInt(spGate.get("signlen").toString());
				// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
				if(signLen == 0){
					signLen = spGate.get("signstr").toString().trim().length();
				}
				//英文短信配置参数
				enmaxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
				entotalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
				enlastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
				ensinglelen = Integer.parseInt(spGate.get("ensinglelen").toString());
				
				//是否支持英文短信，1：支持；0：不支持
				gatepri = Integer.parseInt(spGate.get("gateprivilege").toString());
				//运营商标识
				index =Integer.parseInt(spGate.get("spisuncm").toString());
				//电信
				if(index == 21){
					index = 2;
				//国外通道
				}else if(index == 5){
					index = 3;
					//是否支持英文短信，1：支持；0：不支持
					if((gatepri&2)==2)
					{
						gateprivilege = 1;
						//国外通道英文短信最大长度
						msgLen = enmaxLen - 20;
					}
					else
					{
						gateprivilege = 0;
						//国外通道中文短信最大长度
						msgLen = maxLen - 10;
					}
				}
				//签名位置,1:前置;0:后置
				if((gatepri&4)==4)
				{
					signLocation = 1;
				}
				else
				{
					signLocation = 0;
				}
				//英文短信签名长度
				ensignLen = Integer.parseInt(spGate.get("ensignlen").toString());
				// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
				if(ensignLen == 0){
					if(index == 3)
					{
						//国外通道英文短信签名长度
						ensignLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
					}
					else
					{
						//国内通道英文短信签名长度
						ensignLen = spGate.get("ensignstr").toString().trim().length();
					}
				}
				gtInfos[index] = new StringBuffer().append(maxLen).append(",").append(totalLen).append(",")
								.append(lastLen).append(",").append(signLen).append(",").append(enmaxLen).append(",")
								.append(entotalLen).append(",").append(enlastLen).append(",").append(ensignLen)
								.append(",").append(gateprivilege).append(",").append(ensinglelen).append(",").append(signLocation).toString();
			}
			info += gtInfos[0]+"&"+gtInfos[1]+"&"+gtInfos[2]+"&"+gtInfos[3]+"&"+msgLen+"&";
		}catch(Exception e)
		{
			info = "error";
			EmpExecutionContext.error(e,"获取发送账户绑定的通道信息异常！");
		}finally
		{
			response.getWriter().print(info);
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
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			String lgcorpcode = request.getParameter("lgcorpcode");
			String busCode = request.getParameter("busCode");
			String spUser = request.getParameter("spUser");
			String flag = request.getParameter("flag");
			if(flag == null || "".equals(flag))
			{
				EmpExecutionContext.error("APP消息发送高级设置存为默认参数异常！");
				response.getWriter().print(result);
				return;
			}
			if(lguserid == null || "".equals(lguserid))
			{
				EmpExecutionContext.error("APP消息发送高级设置存为默认参数异常！lguserid："+lguserid);
				response.getWriter().print(result);
				return;
			}
			if(busCode == null || "".equals(busCode))
			{
				EmpExecutionContext.error("APP消息发送高级设置存为默认参数异常！busCode："+busCode);
				response.getWriter().print(result);
				return;
			}
			if(spUser == null || "".equals(spUser))
			{
				EmpExecutionContext.error("APP消息发送高级设置存为默认参数异常！spUser："+spUser);
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

			result = msgSendBiz.setDefault(conditionMap, lfDfadvanced);
			
			//操作结果
			String opResult ="APP消息发送高级设置存为默认失败。";
			if(result != null && "seccuss".equals(result))
			{
				opResult = "APP消息发送高级设置存为默认成功。";
			}
			//操作员信息
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, lguserid);
			//操作员姓名
			String opUser = sysuser==null?"":sysuser.getUserName();
			//操作日志信息
			StringBuffer content = new StringBuffer();
			content.append("[业务编码，SP账号](").append(busCode).append("，").append(spUser).append(")");
			
			//操作日志
			EmpExecutionContext.info("APP消息发送", lgcorpcode, lguserid, opUser, opResult + content.toString(), "OTHER");
			
			response.getWriter().print(result);
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "APP消息发送高级设置存为默认异常！");
			response.getWriter().print(result);
		}
	}
}
