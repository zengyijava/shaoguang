package com.montnets.emp.perfect.biz;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.Message;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.perfect.LfPerFileInfo;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.perfect.LfPerfectNoticUp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue;
import com.montnets.emp.util.TxtFileUtil;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @description 完美通知 重新发送
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-26 上午11:43:42
 */
public class ReSendPerfectTimerThread extends Thread
{
	private LfPerfectNotic		lfPerfectNotic;

	private String				spId;

	private String				usedSubno;

	private String				oldtaskid;

	private boolean				isCharge;

	private LfSysuser			lfsysuser;

	private String				line				= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

	private PrefectNoticeBiz	prefectNoticeBiz	= new PrefectNoticeBiz();

	private WgMsgConfigBiz		wgMsgConfigBiz		= new WgMsgConfigBiz();

	private BalanceLogBiz		balanceBiz			= new BalanceLogBiz();

	private TxtFileUtil			txtfileutil			= new TxtFileUtil();

	private BaseBiz				baseBiz				= new BaseBiz();
	
	private SmsBiz				smsBiz				= new SmsBiz();

	private String empLangName;

	public ReSendPerfectTimerThread(LfPerfectNotic lfPerfectNotic, String spId, String usedSubno,String oldtaskid, boolean isCharge, LfSysuser lfsysuser,String empLangName)
	{
		this.lfPerfectNotic = lfPerfectNotic;
		this.spId = spId;
		this.usedSubno = usedSubno;
		this.oldtaskid = oldtaskid;
		this.isCharge = isCharge;
		this.lfsysuser = lfsysuser;
		this.empLangName = empLangName;
	}

	@Override
	public void run()
	{
		// 获取路径的方法
		Integer delay = lfPerfectNotic.getSendInterval() * 60 * 1000;
		// 发送间隔
		Integer maxCount = lfPerfectNotic.getMaxSendCount();
		// 最大发送次数
		Long senderId = lfPerfectNotic.getSenderGuid();
		// 发送者GUID
		Long taskId = lfPerfectNotic.getTaskId();
		// 任务ID
		String content = lfPerfectNotic.getContent();
		// 发送内容
		String corpCode = lfPerfectNotic.getCorpCode();
		// 企业编码
		String noticeUpIds = lfPerfectNotic.getDialogId();
		// 这里放的是完美通知详情的IDS。暂时
		List<LfPerfectNoticUp> noticUps = null;
		// 数据库中所存在的完美通知详情
		try
		{
			if(maxCount > 5 || maxCount < 1)
			{
				// 防止大批量发送
				EmpExecutionContext.error("重新选择完美通知发送出现异常，maxCount:" + maxCount+",taskid："+taskId);
				return;
				
			}
			Message lfImMessage = new Message();
			// 创建短信载体
			lfImMessage.setFromId(lfsysuser.getUserCode());
			// 用户编码
			lfImMessage.setTaskId(taskId);
			// 任务ID
			lfImMessage.setMenuCode(StaticValue.PERFECT_NOTIC);
			// 完美通知编码
			lfImMessage.setTargetId(String.valueOf(senderId));
			// 发送者GUID
			lfImMessage.setIsSingle(isCharge);
			// 是否 计费
			lfImMessage.setMessageId(spId);
			// 发送的时候启用的SP帐号
			lfImMessage.setDialogId(corpCode);
			// 企业编码
			lfImMessage.setToId(usedSubno);
			// 发送时用的尾号
			lfImMessage.setName("M00000");
			// 业务类型

			// 获取黑名单内容
			Userdata userdata = wgMsgConfigBiz.getSmsUserdataByUserid(spId);
			String passWord = userdata.getUserPassword();
			lfImMessage.setMtmsgid(passWord);
			// 发送账号的密码
			StringBuffer effectMoblie = new StringBuffer("");
			// 有效的手机STR
			String effFileName = "";
			// 有效号码文件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			String[] url = null;
			int count = 0;
			// 提交网关返回值
			String resultMsg = "";
		
			LfPerfectNoticUp noticeUp = null;
			LfPerFileInfo perFileInfo = null;
			String perfectNotice = "zh_HK".equals(empLangName)?"Perfect notice":"完美通知";
			for (int sendcount = 0; sendcount < maxCount; sendcount++)
			{
				EmpExecutionContext.error("重新选择完美通知发送：sendcount:"+(sendcount+1)+", taskid:"+taskId+",corpcode:"+corpCode);
				url = prefectNoticeBiz.getSaveUrl(lfsysuser.getUserId(), taskId, sendcount + 1);
				if(url == null || url.length < 5)
				{
					EmpExecutionContext.error("重新选择完美通知发送获取号码文件名称出现异常 ,taskid："+taskId);
					break;
				}
				// 文件名称，相对路径
				effFileName = "";
				effFileName = url[1];
				String sign = "";
				if(sendcount == 0)
				{
					sign = String.format(DxzsStaticValue.getPERFECT_SIGN_NAME(), lfsysuser.getName()).replace("完美通知",perfectNotice);
					lfImMessage.setContent(sign + content);
				}
				else
					if(sendcount > 0)
					{
						count = sendcount + 1;
						sign = String.format(DxzsStaticValue.getPERFECT_SIGN_TIMER(), count).replace("完美通知",perfectNotice);
						lfImMessage.setContent(sign + content);
						effectMoblie.setLength(0);
						count = 0;
					}
				perFileInfo = new LfPerFileInfo();
				perFileInfo.setSendFileName(effFileName);
				perFileInfo.setTaskId(taskId);
				//做号码文件与taskid的新增处理   防止号码文件重复
				resultMsg = prefectNoticeBiz.addPerFileInfo(perFileInfo);
				//success新增成功       	fail 失败    	errer 出现异常 	
				if(!"success".equals(resultMsg)){
					EmpExecutionContext.error("重新选择完美通知发送新增文件信息表出现异常,taskid："+taskId+",错误编码："+resultMsg);
					break;
				}
				resultMsg = "";
				if(sendcount > 0){
					conditionMap.clear();
					conditionMap.put("taskId", String.valueOf(taskId));
					conditionMap.put("dialogId", "1");
					// 未收到上行消息的完美通知
					conditionMap.put("senderGuid", String.valueOf(senderId));
					noticUps = baseBiz.getByCondition(LfPerfectNoticUp.class, conditionMap, null);
				}
				if((noticUps == null || noticUps.size() == 0) && sendcount == 0)
				{
					resultMsg = prefectNoticeBiz.getReSendPerfectMsg(lfPerfectNotic,wgMsgConfigBiz, noticeUp, 
							conditionMap, noticeUpIds,oldtaskid,effectMoblie, lfImMessage, lfsysuser, txtfileutil, url,empLangName);
						if(!"success".equals(resultMsg)){
							EmpExecutionContext.error("重新选择完美通知发送生成号码文件出现异常,taskid："+taskId+",错误编码："+resultMsg);
							break;
						}
						resultMsg = "";
						//存放的是通知人数
						int preSendCount = smsBiz.countSmsNumberByPer(spId, content, 1, url[1], null);
						// 短信需要的总条数
						lfImMessage.setRecordCount(preSendCount*maxCount);
						// 文件号码的地址
						lfImMessage.setTargetPath(effFileName);
						// 处理运营商扣费
						resultMsg = balanceBiz.checkGwFee(spId, preSendCount*maxCount, corpCode, true, 1);
						if("nogwfee".equals(resultMsg) || "feefail".equals(resultMsg) || resultMsg.indexOf("lessgwfee") > -1)
						{
							resultMsg = "2"; // 表示 发送失败，未成功提交
						}
						else
						{
							// EMP扣费 以及提交网关处理
							resultMsg = prefectNoticeBiz.wmSendMsgByFile(lfImMessage, balanceBiz, wgMsgConfigBiz);
						}
						if("1".equals(resultMsg))
						{
							EmpExecutionContext.error("完美通知提交网关成功,taskid："+taskId + " corpcode: "+ corpCode +",filename:"+effFileName);
							// 更新完美通知明细
							prefectNoticeBiz.updatePerfectNoticUp(taskId, senderId, resultMsg, "noneed", objectMap, conditionMap, baseBiz);
						}
						else
						{
							EmpExecutionContext.error("重新选择完美通知发送提交失败,taskid："+taskId+",错误编码：" + resultMsg +",filename:"+effFileName);
							// 更新完美通知明细
							prefectNoticeBiz.updatePerfectNoticUp(taskId, senderId, resultMsg, "needadd", objectMap, conditionMap, baseBiz);
							if("6".equals(resultMsg) || "7".equals(resultMsg) || "9".equals(resultMsg) 
									|| "nogwfee".equals(resultMsg) || "feefail".equals(resultMsg) 
									|| resultMsg.indexOf("lessgwfee") > -1)
							{
								break;
							}
						}
				}
				else
				{
					if(noticUps != null && noticUps.size() > 0)
					{
						for (int k = 0; k < noticUps.size(); k++)
						{
							noticeUp = noticUps.get(k);
							effectMoblie.append(noticeUp.getMobile()).append(line);
							noticeUp.setReceiveCount(sendcount + 1);
						}
						if(effectMoblie.length() > 0)
						{
							txtfileutil.writeToTxtFile(url[0], effectMoblie.toString());
							effectMoblie.setLength(0);
						}
						else
						{
							// 没有可发送的有效号码
							EmpExecutionContext.error("重新选择完美通知发送没有获取发送号码 ,taskid："+taskId);
							break;
						}
						// 文件号码的地址
						lfImMessage.setTargetPath(effFileName);
						//第 2次不扣费用。第一次发送的时候已经扣取了。
						lfImMessage.setIsSingle(false);
						//完美通知发送
						resultMsg = prefectNoticeBiz.wmSendMsgByFile(lfImMessage, balanceBiz, wgMsgConfigBiz);
						EmpExecutionContext.error("重新选择完美通知发送提交网关，taskid："+taskId+",sendcount："+(sendcount+1)+", corpcode: "+ corpCode + ",returnmsg：" + resultMsg+",filename:"+effFileName);
						//更新明、细信息
						prefectNoticeBiz.updateAgainPerfectNoticUp(taskId, senderId, resultMsg,sendcount, objectMap, conditionMap, baseBiz);
					}
					else
					{
						// 已经没有需要发送的完美通知人员
						EmpExecutionContext.error("重新选择完美通知发送没有需要发送的人员 ,taskid："+taskId);
						break;
					}
				}
				//清空
				url = null;
				resultMsg = "";
				//通过taskid更新主表信息
				if(!prefectNoticeBiz.updateLfPerfectNotic(conditionMap, objectMap, taskId, sendcount+1)){
					EmpExecutionContext.error("重新选择完美通知发送更新LfPerfectNotic表信息出现异常,taskid："+taskId);
					break;
				}
				if((sendcount + 1) < lfPerfectNotic.getMaxSendCount())
				{
					EmpExecutionContext.error("重新选择完美通知发送 等待发送中；taskid："+taskId+",间隔时间："+delay);
					Thread.sleep(delay);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "重新选择完美通知发送失败 ,taskid："+taskId);
		}finally{
			EmpExecutionContext.error("重新选择完美通知发送完毕，taskid：" + taskId + ", corpcode: "+ corpCode);
		}

	}

}
