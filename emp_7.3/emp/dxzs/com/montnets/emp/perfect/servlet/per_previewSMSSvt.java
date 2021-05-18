package com.montnets.emp.perfect.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.PreviewParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.perfect.biz.PerErrorStatus;
import com.montnets.emp.perfect.biz.PrefectNoticeBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.CheckUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class per_previewSMSSvt extends BaseServlet
{
	private final PrefectNoticeBiz	prefectNoticeBiz	= new PrefectNoticeBiz();

	private final WgMsgConfigBiz		msgConfigBiz		= new WgMsgConfigBiz();

	private final SmsBiz				smsBiz				= new SmsBiz();

	private final BalanceLogBiz		biz					= new BalanceLogBiz();

	/**
	 * @description 完美通知预览
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-22 下午06:29:12
	 */
	@SuppressWarnings("unchecked")
	public void priviewNotice(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		StringBuffer returnMsg = new StringBuffer("");
		Map<String, String> fieldInfo = new HashMap<String, String>();
		// 处理json返回值
		Map<String, Object> objMap = new HashMap<String, Object>();
		// 预览参数传递变量类
		PreviewParams preParams = new PreviewParams();
		try
		{
			//String lguseridstr = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguseridstr = SysuserUtil.strLguserid(request);

			String perfectTaskIdstr = request.getParameter("perfectTaskId");
			if(lguseridstr == null || "".equals(lguseridstr)|| "undefined".equals(lguseridstr) || perfectTaskIdstr == null || "".equals(perfectTaskIdstr))
			{
				prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage1", PerErrorStatus.PERF100);
				EmpExecutionContext.error("完美通知预览，获取参数异常， lguserid : " + lguseridstr + "  taskid : " + perfectTaskIdstr);
				return;
			}
			
			EmpExecutionContext.info("完美通知预览， lguserid : " + lguseridstr + "，taskid : " + perfectTaskIdstr);
			
			Long lguserid = Long.parseLong(lguseridstr);
			// 任务ID
			Long perfectTaskId = Long.parseLong(perfectTaskIdstr);
			// 获取运营商号码段
			String[] haoduan = msgConfigBiz.getHaoduan();
			if(haoduan == null || haoduan.length == 0)
			{
				prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage1", PerErrorStatus.PERF100);
				EmpExecutionContext.error("完美通知预览， 获取运营商号码段为空！");
				return;
			}
			LfSysuser lfsysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfsysuser == null)
			{
				prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage1", PerErrorStatus.PERF100);
				EmpExecutionContext.error("完美通知预览， 获取当前操作员失败！");
				return;
			}

			// 获取完美通知号码文件路径
			String[] url = prefectNoticeBiz.getSaveUrl(lguserid, perfectTaskId,1);
			if(url == null || url.length < 5)
			{
				prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage1", PerErrorStatus.PERF100);
				EmpExecutionContext.error("完美通知预览， 获取完美通知号码文件路径出现异常！");
				return;
			}
			else
			{
				//号码文件是否已存在
				File file = new File(url[0]);
				//判断文件是否存在，存在则返回
				if (file.exists())
				{
					prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage1", PerErrorStatus.PERF100);
					EmpExecutionContext.error("完美通知预览，发送文件路径已存在，文件路径："+url[0]);
					return;
				}
				preParams.setPhoneFilePath(url);
			}
			Map<String, String> btnMap = (Map<String, String>) request.getSession(false).getAttribute("btnMap");
			// 是否有预览号码权限.// 号码是否可见，0：不可见，1：可见
			if(btnMap.get(StaticValue.PHONE_LOOK_CODE) != null)
			{
				preParams.setIshidephone(1);
			}
			// 表单元素集合
			List<FileItem> fileList = null;
			try
			{
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setSizeThreshold(1024 * 1024);
				String temp = url[0].substring(0, url[0].lastIndexOf("/"));
				factory.setRepository(new File(temp));
				ServletFileUpload upload = new ServletFileUpload(factory);
				// 以文件方式解析表单
				fileList = upload.parseRequest(request);
			}
			catch (FileUploadException e)
			{
				returnMsg.append("uploaderrer");
				EmpExecutionContext.error(e, "完美通知预览， 号码文件出现异常！");
				return;
			}
			Iterator<FileItem> it = fileList.iterator();
			FileItem fileItem = null;
			// 表单控件name
			String fileName = "";
			// 表单控件值
			String fileValue = "";
			while(it.hasNext())
			{
				fileItem = (FileItem) it.next();
				if(!fileItem.isFormField() && fileItem.getName().length() > 0)
				{
					// 处理上传文件 待定
				}
				else
				{
					fileName = fileItem.getFieldName();
					fileValue = fileItem.getString("UTF-8").toString();
					fieldInfo.put(fileName, fileValue);
				}
			}
			// 发送内容
			String content = fieldInfo.get("msg");
			// 员工机构ids
			String depIds = fieldInfo.get("depIds");
			// 群组IDS
			String groupIds = fieldInfo.get("groupIds");
			// 员工IDS
			String empUserIds = fieldInfo.get("empUserIds");
			// 自定义IDs
			String deUserIds = fieldInfo.get("deUserIds");
			// 发送帐号spUser
			String spUser = fieldInfo.get("spUser");
			// 业务编码
			String busCode = fieldInfo.get("busCode");
			// 发送次数
			String sendCountStr = fieldInfo.get("sdCount");
			// 尾号
			String usedSubno = fieldInfo.get("usedSubno");
			// 手工增加的手机号码
			String addphonename = fieldInfo.get("handphonename");
			// 清空MAP
			fieldInfo.clear();
		
			if(busCode == null || spUser == null || usedSubno == null || sendCountStr == null)
			{
				prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage1", PerErrorStatus.PERF100);
				EmpExecutionContext.error("完美通知预览， 获取参数异常， busCode : " + busCode + "  spUser : " + spUser + "  usedSubno : " + usedSubno + " sdCount : "+ sendCountStr);
				return;
			}
			
			EmpExecutionContext.info("完美通知预览， 获取参数值， busCode : " + busCode + "，spUser : " + spUser + "，usedSubno : " + usedSubno + "，sdCount : "+ sendCountStr);
			
			
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfsysuser, lfsysuser.getCorpCode(), spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("完美通知预览，检查操作员、企业编码、发送账号不通过，taskid:"+perfectTaskId
						+ "，corpCode:"+lfsysuser.getCorpCode()
						+ "，spUser："+spUser
						+ "，errCode:"+ IErrorCode.V10001);
				String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
						ErrorCodeInfo info = ErrorCodeInfo.getInstance(langName);
						String desc = info.getErrorInfo(IErrorCode.V10001);
						return;	
			}
			
			int sendCount = Integer.parseInt(sendCountStr);
			//解析所选择的对象  //success表示解析成功      nonum没有动态bean    numerrer写号码文件失败    写 信息文件失败 infofileerrer   出现异常  errer
			String returnmsg = prefectNoticeBiz.parsePerfectPhone(preParams, depIds, groupIds, empUserIds, deUserIds, addphonename, haoduan, lfsysuser.getCorpCode(), busCode, MessageUtils.extractMessage("common","common_empLangName",request));
			if(!"success".equals(returnmsg))
			{
				prefectNoticeBiz.handlPerErrer(returnMsg, objMap, preParams, "stage7", PerErrorStatus.PERF101);
				EmpExecutionContext.error("完美通知预览， 解析文件出现异常，错误编码："+returnmsg);
				return;
			}
			if(preParams.getEffCount() == 0)
			{
				returnMsg.append("noPhone");
				EmpExecutionContext.error("完美通知预览， 没有效号码。");
				return;
			}
			// 预发送条数
			int preSendCount = -1;
			try
			{
				//替换短信内的特殊字符
				content = smsBiz.smsContentFilter(content);
				preSendCount = smsBiz.countSmsNumberByPer(spUser, content, 1, url[1], null);
				preSendCount = preSendCount * sendCount;
				if(preSendCount < 0)
				{
					returnMsg.append("presendcounterrer");
					EmpExecutionContext.error("完美通知预览， 获取预发送条数异常。错误码：" + IErrorCode.B20006);
					return;
				}
				EmpExecutionContext.info("完美通知预览， 获取预发送条数：" + preSendCount);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "完美通知预览， 计算预发送条数出现异常。错误码：" + IErrorCode.B20006);
				returnMsg.append("presendcounterrer");
				return;
			}
			// 判断是否需要机构计费 如果启用了计费则为true;未启用则为false;
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			boolean isCharge = false;
			if(infoMap == null)
			{
				new CommonBiz().setSendInfo(request, response, lfsysuser.getCorpCode(), lguserid);
				infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			}
			if("true".equals(infoMap.get("feeFlag")))
			{
				isCharge = true;
			}
			
			//获取SP账号计费类型，用于确定是否计费
			Long spUserFeeFlag = biz.getSpUserFeeFlag(spUser, 1);
			if(spUserFeeFlag == null || spUserFeeFlag < 0)
			{
				EmpExecutionContext.error("完美通知预览， 获取SP账号计费类型出现异常！spUser="+spUser+",resultcode="+spUserFeeFlag);
				returnMsg.append("spUserFeeFlagErr");
				return;
			}
			//2:后付费，不需要计费
			else if(spUserFeeFlag == 2)
			{
				//后附费账号 不用计费标志
				objMap.put("spUserFeeFlag", "2");
				//SP账号余额，给个默认值
				objMap.put("spUserIdFee", "0");
			}
			//1:预付费，需要计费，则获取账号余额
			else if(spUserFeeFlag == 1)
			{
				//获取SP账号可发送短信费用
				Long spUserIdFee = biz.getSpUserAmount(spUser);
				//异常获取不到
				if(spUserIdFee == null)
				{
					EmpExecutionContext.error("完美通知预览， 获取获取SP账号可发送短信费用为null。spUser="+spUser);
					returnMsg.append("spUserIdFeeErr");
					return;
				}
				//预付费账号，计费标志
				objMap.put("spUserFeeFlag", "1");
				//SP账号余额
				objMap.put("spUserIdFee", Long.toString(spUserIdFee));
			}
			else
			{
				EmpExecutionContext.error("完美通知预览， 获取SP账号计费类型出现未定义返回值！spUser="+spUser+",resultcode="+spUserFeeFlag);
				returnMsg.append("spUserFeeFlagErr");
				return;
			}
			
			// 该操作员最大发送条数
			Long maxcount = -1L;
			// (还需判断计费机制是否开启
			if(isCharge)
			{
				// 提供一个可获取最大可发送条数的方法.
				maxcount = biz.getAllowSmsAmount(lfsysuser);
				if(maxcount == null || maxcount < 0)
				{
					EmpExecutionContext.error("完美通知预览， 获取最大可发送条数出现异常！");
					returnMsg.append("maxcounterrer");
					return;
				}
			}
			
			String spFeeResult = biz.checkGwFee(spUser, preSendCount, lfsysuser.getCorpCode(), 1);
			
			objMap.put("presendcount", preSendCount); // 最大发送号数
			objMap.put("maxcount", maxcount); // 系统所能发送数目
			objMap.put("isCharg", isCharge); // 是否付费
			objMap.put("usedSubno", usedSubno); // 尾号
			objMap.put("spFeeResult", spFeeResult);
			objMap.put("errormsg", "000");
			objMap.put("stage", "stage4");
			returnMsg.append(preParams.getJsonStr(objMap));
		}
		catch (Exception e)
		{
			returnMsg.append("errer");
			EmpExecutionContext.error(e, "完美通知预览， 预览失败！");
		}
		finally
		{
			EmpExecutionContext.info("完美通知预览， 返回页面预览数据：" + returnMsg.toString());
			request.setAttribute("result", returnMsg.toString());
			request.getRequestDispatcher("dxzs/perfect/per_preNotice.jsp").forward(request, response);
		}
	}
}
