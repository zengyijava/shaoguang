package com.montnets.emp.perfect.servlet;

import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.SMParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllotDetail;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.perfect.biz.PerfectSendTimerThread;
import com.montnets.emp.perfect.biz.ReSendPerfectTimerThread;
import com.montnets.emp.security.keyword.KeyWordAtom;
import com.montnets.emp.util.CheckUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Map;
/**
 *     
 * @description   完美通知发送类，执行 LfPerfectNotic操作，以及启动线程
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-29 下午03:46:03
 */
@SuppressWarnings("serial")
public class per_sendNoticeSMSSvt extends BaseServlet
{
	private final BaseBiz						baseBiz			= new BaseBiz();

	private final BalanceLogBiz				balanceLogBiz	= new BalanceLogBiz();

	private final SmsBiz						smsBiz			= new SmsBiz();

	private final KeyWordAtom					keyWordAtom		= new KeyWordAtom();

	private final static GlobalVariableBiz	globalBiz		= GlobalVariableBiz.getInstance();

	private final SubnoManagerBiz				subnoManagerBiz	= new SubnoManagerBiz();

	/**
	 * @description 完美通知发送
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author yejiangmin <282905282@qq.com>
	 * @datetime 2013-10-22 上午11:47:31
	 */
	public void sendmsg(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		// 返回信息
		String returnMsg = "";
		try
		{
			// 获取用户信息
			LfSysuser lfsysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfsysuser == null)
			{
				EmpExecutionContext.error("完美通知提交发送，获取当前操作员失败！");
				returnMsg = "sysusererrer";
				return;
			}
			// 发送次数
			String sendCount = request.getParameter("sdCount");
			// 预发送条数
			String presendcount = request.getParameter("yct");
			// 发送间隔
			String sendInterval = request.getParameter("sdInterval");
			// 内容
			String content = request.getParameter("content");
			// 发送帐号
			String spUser = request.getParameter("spUser");
			// 业务类型
			String busCode = request.getParameter("busCode");
			// 尾号
			String usedSubno = request.getParameter("usedSubno");
			// 手机号码文件路径
			String phoneurl = request.getParameter("phoneurl");
			// 任务ID
			String perfectTaskId = request.getParameter("perfectTaskId");
			// 通知人数
			String effs = request.getParameter("effs");
			if(sendCount == null || presendcount == null || sendInterval == null || content == null || spUser == null || busCode == null || usedSubno == null || phoneurl == null || perfectTaskId == null || effs == null)
			{
				EmpExecutionContext.error("完美通知提交发送，页面参数值 ： sendCount：" + sendCount + " presendcount :" + presendcount + " sendInterval : " + sendInterval + " spUser:" + spUser + " busCode : " + busCode + " usedSubno:" + usedSubno + " phoneurl :" + phoneurl + " taskid : " + perfectTaskId + " content :" + content + "出现null");
				// 参数出现错误
				returnMsg = "parametererrer";
				return;
			}
			
			
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfsysuser, lfsysuser.getCorpCode(), spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("完美通知提交发送，检查操作员、企业编码、发送账号不通过，taskid:"+perfectTaskId
						+ "，corpCode:"+lfsysuser.getCorpCode()
						+ "，userid："+lfsysuser.getUserId()
						+ "，spUser："+spUser
						+ "，errCode:"+ IErrorCode.V10001);
						// 参数出现错误
						returnMsg = "parametererrer";
						return;	
			}
			
			EmpExecutionContext.info("完美通知提交发送，页面参数值 ： sendCount：" + sendCount + " presendcount :" + presendcount + " sendInterval : " + sendInterval + " spUser:" + spUser + " busCode : " + busCode + " usedSubno:" + usedSubno + " phoneurl :" + phoneurl + " taskid : " + perfectTaskId + " content :" + content);
			
			// 判断是否需要机构计费
			Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
			// 如果启用了计费则为true;未启用则为false;
			boolean isCharge = false;
			if("true".equals(infoMap.get("feeFlag")))
			{
				isCharge = true;
				// 允许发送最大条数
				Long maxcount = balanceLogBiz.getAllowSmsAmount(lfsysuser);
				if(maxcount == null)
				{
					returnMsg = "getfeeerr";
					EmpExecutionContext.error("完美通知提交发送，获取发送最大条数出现异常！");
					return;
				}
				if(maxcount < Long.valueOf(presendcount))
				{
					returnMsg = "unenoughfee";
					EmpExecutionContext.error("完美通知提交发送，发送条数大于该操作员所能发送最大条数！");
					return;
				}
			}
			LfPerfectNotic lfPerfectNotic = new LfPerfectNotic();
			lfPerfectNotic.setCorpCode(lfsysuser.getCorpCode());
			lfPerfectNotic.setSenderGuid(lfsysuser.getGuId());
			content = content.replaceAll("•", "·").replace("¥", "￥");
			lfPerfectNotic.setContent(content);
			lfPerfectNotic.setSubmitTime(new Timestamp(System.currentTimeMillis()));
			lfPerfectNotic.setSendInterval(Integer.valueOf(sendInterval));
			lfPerfectNotic.setMaxSendCount(Integer.valueOf(sendCount));
			lfPerfectNotic.setArySendCount(0);
			lfPerfectNotic.setNoticCount(0);
			lfPerfectNotic.setTaskId(Long.valueOf(perfectTaskId));
			lfPerfectNotic.setNoticCount(Integer.valueOf(effs));
			// 任务创建成功，启动发送线程
			if(baseBiz.addObj(lfPerfectNotic))
			{
				new PerfectSendTimerThread(lfPerfectNotic, spUser, usedSubno, busCode, phoneurl, presendcount, isCharge, lfsysuser).start();
				returnMsg = "success";
				EmpExecutionContext.error("完美通知提交发送，lfPerfectNotic新增成功；发送线程启动成功。taskid="+perfectTaskId);
				
				Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
				if(loginSysuserObj!=null){
					LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
					String contents="完美通知发送(完美通知taskid为："+perfectTaskId+")发送线程启动成功。";
					EmpExecutionContext.info("完美通知发送", loginSysuser.getCorpCode(), String.valueOf(loginSysuser.getUserId()), loginSysuser.getUserName(), contents, "ADD");
				}
			}
			else
			{
				returnMsg = "adderrer";
				EmpExecutionContext.error("完美通知提交发送，lfPerfectNotic新增失败！");
			}
		}
		catch (Exception e)
		{
			returnMsg = "fail";
			EmpExecutionContext.error(e, "完美通知提交发送，提交出现异常！");
		}
		finally
		{
			EmpExecutionContext.info("完美通知提交发送，返回页面结果字符串："+returnMsg);
			response.getWriter().print(returnMsg);
		}
	}

	/**
	 * 重新发送完美通知
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reStartSendPreNotice(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String returnmsg = "";
		try
		{
			String reStartContent = request.getParameter("reStartContent");
			// 发送内容
			String oldtaskid = request.getParameter("taskid");
			
			 //获取加密类
			ParamsEncryptOrDecrypt encryptOrDecrypt=(ParamsEncryptOrDecrypt)request.getSession(false).getAttribute("decryptObj");
			//解密
			oldtaskid=encryptOrDecrypt.decrypt(oldtaskid);
			
			// 完美通知ID
			String items = request.getParameter("items");
			// 发送详情ID
			String sendInterval = request.getParameter("sendInterval");
			// 发送间隔
			String sendCountstr = request.getParameter("sendCount");
			// 发送次数
			String spUser = request.getParameter("spUser");
			// 发送的sp账号
			String noticecount = request.getParameter("noticecount");
			if(reStartContent == null || oldtaskid == null || items == null || sendInterval == null || sendCountstr == null || spUser == null || noticecount == null)
			{
				returnmsg = "stage1&isnull";
				EmpExecutionContext.error("完美通知 获取  oldtaskid : " + oldtaskid + " items : " + items + " sendInterval : " + sendInterval + " sendCountstr : " + sendCountstr + " spUser : " + spUser + " content : " + reStartContent +  " noticecount : " + noticecount);
				return;
			}
			// 获取当前操作员对象
			LfSysuser lfsysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			if(lfsysuser == null)
			{
				returnmsg = "stage1&isnull";
				EmpExecutionContext.error("完美通知获取当前操作员为NULL");
				return;
			}
			
			//操作员、企业编码、SP账号检查
			boolean checkFlag = new CheckUtil().checkSysuserInCorp(lfsysuser, lfsysuser.getCorpCode(), spUser, null);
			if(!checkFlag)
			{
				EmpExecutionContext.error("完美通知重新发送，检查操作员、企业编码、发送账号不通过，"
						+ "，corpCode:"+ lfsysuser.getCorpCode()
						+ "，userid："+ lfsysuser.getUserId()
						+ "，spUser："+spUser
						+ "，errCode:"+ IErrorCode.V10001);
				        returnmsg = "stage1&isnull";
						return;	
			}
			
			String words = new String();
			words = keyWordAtom.checkText(reStartContent, lfsysuser.getCorpCode());
			if(words != null && words.length() > 0)
			{
				returnmsg = "stage2&" + words;
				EmpExecutionContext.error("完美通知获取关键字为:" + words);
				return;
			}

			// 判断spuser的url是否可用
			String checkSpUerRes = smsBiz.checkSpUser(spUser, 1, 1);
			if(!"checksuccess".equals(checkSpUerRes))
			{
				returnmsg = "stage5&spfail_" + checkSpUerRes;
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
				EmpExecutionContext.error("重新发送完美通知前检测SP账号["+spUser+"]配置不通过，原因："+info+"。");
				return;
			}
			// 此次发送的完美通知的信息
			String usedSubno = request.getParameter("usedSubno");
			LfSubnoAllotDetail detail = null;
			if(usedSubno == null || "".equals(usedSubno))
			{
				ErrorCodeParam errorCode = new ErrorCodeParam();
				SMParams smParams = new SMParams();
				smParams.setCodes(String.valueOf(StaticValue.WMNOTICECODE));
				smParams.setCodeType(0);
				smParams.setCorpCode(lfsysuser.getCorpCode());
				smParams.setAllotType(1);
				smParams.setSpUserid(spUser);
				detail = globalBiz.getSubnoDetail(smParams, errorCode);
				if(detail != null && detail.getUsedExtendSubno() != null && !"".equals(detail.getUsedExtendSubno()))
				{
					usedSubno = detail.getUsedExtendSubno();
				}
				else
				{
					if("EZHB237".equals(errorCode.getErrorCode()))
					{
						returnmsg = "stage3&enoughSubno"; // 尾号不够
					}
					else
					{
						returnmsg = "stage3&notSubno"; // 获取尾号失败
					}
					EmpExecutionContext.error("完美通知获取尾号失败，错误编码：" + errorCode.getErrorCode());
					return;
				}
			}
			int resultInt = subnoManagerBiz.ischeckPortUsed(spUser, usedSubno);
			if(resultInt != 1)
			{
				returnmsg = "stage3&" + resultInt + "_" + usedSubno;
				return;
			}
			try
			{
				Long taskId = globalBiz.getValueByKey("taskId", 1L);
				if(taskId == null)
				{
					returnmsg = "stage1&isnull";
					EmpExecutionContext.error("完美通知 获取 taskid为null！");
					return;
				}
				// 判断是否需要机构计费
				Map<String, String> infoMap = (Map<String, String>) request.getSession(false).getAttribute("infoMap");
				// 如果启用了计费则为true;未启用则为false;
				boolean isCharge = false;
				if("true".equals(infoMap.get("feeFlag")))
				{
					isCharge = true;
				}
				LfPerfectNotic lfPerfectNotic = new LfPerfectNotic();
				lfPerfectNotic.setCorpCode(lfsysuser.getCorpCode());
				lfPerfectNotic.setSenderGuid(lfsysuser.getGuId());
				reStartContent = reStartContent.replaceAll("•", "．");
				lfPerfectNotic.setContent(reStartContent.trim());
				lfPerfectNotic.setSubmitTime(new Timestamp(System.currentTimeMillis()));
				lfPerfectNotic.setSendInterval(Integer.parseInt(sendInterval));
				lfPerfectNotic.setMaxSendCount(Integer.parseInt(sendCountstr));
				lfPerfectNotic.setArySendCount(0);
				lfPerfectNotic.setNoticCount(Integer.valueOf(noticecount));
				// 这里存放的是完美通知的老详情ID
				lfPerfectNotic.setDialogId(items);
				lfPerfectNotic.setTaskId(taskId);
				if(baseBiz.addObj(lfPerfectNotic))
				{
					new ReSendPerfectTimerThread(lfPerfectNotic, spUser, usedSubno, oldtaskid, isCharge, lfsysuser, MessageUtils.extractMessage("common","common_empLangName",request)).start();
					returnmsg = "stage4&success_" + usedSubno;
				}
				else
				{
					returnmsg = "stage4&fail_" + usedSubno;
					EmpExecutionContext.error("完美通知重新发送出现异常，新增lfPerfectNotic失败！");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "完美通知历史发送获取尾号失败！");
				returnmsg = "stage4&fail" + usedSubno;
			}
		}
		catch (Exception e)
		{
			returnmsg = "errer";
			EmpExecutionContext.error(e, "重新发送完美通知 失败！");
		}
		finally
		{
			response.getWriter().print(returnmsg);
		}
	}

}
