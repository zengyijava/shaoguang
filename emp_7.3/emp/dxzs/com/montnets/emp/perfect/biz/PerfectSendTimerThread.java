package com.montnets.emp.perfect.biz;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.atom.UserdataAtom;
import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.servmodule.dxzs.constant.DxzsStaticValue;
import com.montnets.emp.common.constant.Message;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.perfect.LfPerFileInfo;
import com.montnets.emp.entity.perfect.LfPerfectNotic;
import com.montnets.emp.entity.perfect.LfPerfectNoticUp;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.TxtFileUtil;

/**
 * 完美通知发送线程
 * 
 * @description
 * @project p_ydcx
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author yejiangmin <282905282@qq.com>
 * @datetime 2013-10-23 下午04:24:34
 */
public class PerfectSendTimerThread extends Thread
{

	private LfPerfectNotic		lfPerfectNotic;

	private String				spId;

	private String				busCode;

	private String				usedSubno;

	private String				phoneurl;

	private String				presendcount;

	private boolean				isCharge;

	private LfSysuser			lfsysuser;

	private WgMsgConfigBiz		wgMsgConfigBiz		= new WgMsgConfigBiz();

	private BalanceLogBiz		balanceBiz			= new BalanceLogBiz();

	private TxtFileUtil			txtfileutil			= new TxtFileUtil();

	private BaseBiz				baseBiz				= new BaseBiz();

	private String				line				= StaticValue.systemProperty.getProperty(StaticValue.LINE_SEPARATOR);

	private PrefectNoticeBiz	prefectNoticeBiz	= new PrefectNoticeBiz();

	public PerfectSendTimerThread(LfPerfectNotic lfPerfectNotic, String spId, String usedSubno, String busCode, String phoneurl, String presendcount, boolean isCharge, LfSysuser lfsysuser)
	{
		this.lfPerfectNotic = lfPerfectNotic;
		this.spId = spId;
		this.busCode = busCode;
		this.usedSubno = usedSubno;
		this.phoneurl = phoneurl;
		this.presendcount = presendcount;
		this.isCharge = isCharge;
		this.lfsysuser = lfsysuser;
	}

	@Override
	public void run()
	{
		// 写文件的方法
		String dirUrl = txtfileutil.getWebRoot();
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
		List<LfPerfectNoticUp> noticUps = null;
		//明细
		LfPerfectNoticUp noticUp = null;
		try
		{
			if(maxCount > 5 || maxCount < 1)
			{
				EmpExecutionContext.error("完美通知发送线程，出现异常，maxCount:"+maxCount +",taskid："+taskId);
				return;
				// 防止大批量发送
			}
			Message lfMessage = new Message();
			// 创建短信载体
			lfMessage.setFromId(lfsysuser.getUserCode());
			// 用户编码
			lfMessage.setTaskId(taskId);
			// 任务ID
			lfMessage.setMenuCode(StaticValue.PERFECT_NOTIC);
			// 完美通知编码
			lfMessage.setTargetId(String.valueOf(senderId));
			// 发送者GUID
			lfMessage.setIsSingle(isCharge);
			// 是否 计费
			lfMessage.setMessageId(spId);
			// 发送的时候启用的SP帐号
			lfMessage.setDialogId(corpCode);
			// 企业编码
			lfMessage.setToId(usedSubno);
			// 发送时用的尾号
			lfMessage.setName(busCode);
			// 业务类型
			// 第一次的发送文件名称
			lfMessage.setTargetPath(phoneurl);
			//预发送条数
			lfMessage.setRecordCount(Integer.valueOf(presendcount));
			UserdataAtom userdataAtom = new UserdataAtom();
			// 设置发送账户密码
			lfMessage.setMtmsgid(userdataAtom.getUserPassWord(spId));
			// 发送账号的密码
			StringBuffer effectMoblie = new StringBuffer();
			// 文件名称
			String effFileName = "";
			List<LfPerfectNoticUp> perfectNoticUpList = new ArrayList<LfPerfectNoticUp>();
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			String[] url = null;
			// 提交网关返回值
			String resultMsg = "";
			int count = 0 ;
			//文件名称与taskid
			LfPerFileInfo perFileInfo = null;
			String sign = "";
			for (int sendcount = 0; sendcount < maxCount; sendcount++)
			{
				EmpExecutionContext.info("完美通知发送线程，sendcount:"+(sendcount+1)+", taskid:"+taskId+",corpcode:"+corpCode);
				if(sendcount == 0)
				{
					sign = String.format(DxzsStaticValue.getPERFECT_SIGN_NAME(), lfsysuser.getName());
					lfMessage.setContent(sign + content);
					effFileName = phoneurl;
				}
				else
					if(sendcount > 0)
					{
						count = sendcount + 1;
						sign = String.format(DxzsStaticValue.getPERFECT_SIGN_TIMER(), count);
						lfMessage.setContent(sign + content);
						//更新号码文件名称
						url = prefectNoticeBiz.getSaveUrl(lfsysuser.getUserId(), taskId, count);
						if(url == null || url.length < 5)
						{
							EmpExecutionContext.error("完美通知发送线程，获取号码文件名称出现异常 。taskid："+taskId+"，url为空或集合长度小于5。");
							break;
						}
						//文件名称，相对路径
						effFileName = url[1];
						url = null;
						count = 0;
					}
				perFileInfo = new LfPerFileInfo();
				perFileInfo.setSendFileName(effFileName);
				perFileInfo.setTaskId(taskId);
				//做号码文件与taskid的新增处理   防止号码文件重复
				resultMsg = prefectNoticeBiz.addPerFileInfo(perFileInfo);
				//success新增成功       	fail 失败    	errer 出现异常 	 
				if(!"success".equals(resultMsg)){
					EmpExecutionContext.error("完美通知发送线程，新增文件信息表出现异常, taskid："+taskId+",错误编码："+resultMsg);
					break;
				}
				resultMsg = "";
				if(sendcount > 0){
					conditionMap.clear();
					conditionMap.put("taskId", String.valueOf(taskId));
					conditionMap.put("dialogId", "1");
					conditionMap.put("senderGuid", String.valueOf(senderId));
					noticUps = baseBiz.getByCondition(LfPerfectNoticUp.class, conditionMap, null);
				}
				// 为了判断数据库查询不出数据导致又重新插入
				if((noticUps == null || noticUps.size() == 0) && sendcount == 0)
				{
					String phonePath = phoneurl.substring(0, phoneurl.length() - 4) + "_temp.txt";
					if(!txtfileutil.checkFile(phonePath))
					{
						EmpExecutionContext.error("完美通知发送线程，获取其发送文件信息失败 ,taskid："+taskId+",phonePath="+phonePath);
						break;
					}
					//更新成全路径，进行读文件用
					phonePath = dirUrl + phonePath;
					//读取完美通知文件中需要发送的信息存放集合     // 成功返回 success 失败返回 nogtport 路由信息为空 nousedmap 全通道号为空 errer出现异常
					String resultstr = prefectNoticeBiz.getPerfectSendFileMsg(phonePath, lfPerfectNotic, perfectNoticUpList, wgMsgConfigBiz, spId, usedSubno,noticUp);
					if(!"success".equals(resultstr))
					{
						EmpExecutionContext.error("完美通知发送线程，获取文件信息出现异常,taskid：" + taskId + "错误编码："+resultstr);
						break;
					}
					// 处理运营商扣费
					resultMsg = balanceBiz.checkGwFee(spId, Integer.parseInt(presendcount), corpCode, true, 1);
					if("nogwfee".equals(resultMsg) || "feefail".equals(resultMsg) || resultMsg.indexOf("lessgwfee") > -1)
					{
						resultMsg = "2";		//表示 发送失败，未成功提交
					}
					else
					{
						//EMP扣费 以及提交网关处理
						resultMsg = prefectNoticeBiz.wmSendMsgByFile(lfMessage, balanceBiz, wgMsgConfigBiz);
					}
					if("1".equals(resultMsg))
					{
						EmpExecutionContext.info("完美通知发送线程，提交网关成功,taskid："+taskId + " corpcode: "+ corpCode +",filename:"+effFileName);
						//更新完美通知明细
						prefectNoticeBiz.updatePerfectNoticUp(taskId, senderId, resultMsg, "noneed", objectMap, conditionMap, baseBiz);
					}
					else
					{
						EmpExecutionContext.error("完美通知发送线程，提交网关失败,taskid：" + taskId + "错误编码：" + resultMsg +",filename:"+effFileName);
						//更新完美通知明细
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
							noticUp = noticUps.get(k);
							effectMoblie.append(noticUp.getMobile()).append(line);
							noticUp.setReceiveCount(sendcount + 1);
						}
						if(effectMoblie.length() > 0)
						{
							txtfileutil.writeToTxtFile(dirUrl + effFileName, effectMoblie.toString());
							effectMoblie.setLength(0);
						}
						else
						{
							// 没有可发送的有效号码
							EmpExecutionContext.error("完美通知发送线程，没有获取发送号码 ,taskid："+taskId);
							break;
						}
						// 文件号码的地址
						lfMessage.setTargetPath(effFileName);
						//第 2次不扣费用。第一次发送的时候已经扣取了。
						lfMessage.setIsSingle(false);
						//完美通知发送
						resultMsg = prefectNoticeBiz.wmSendMsgByFile(lfMessage, balanceBiz, wgMsgConfigBiz);
						EmpExecutionContext.info("完美通知发送线程，提交网关，taskid："+taskId+",sendcount："+(sendcount+1)+", corpcode: "+ corpCode + ",returnmsg：" + resultMsg+",filename:"+effFileName);
						//更新明、细信息
						prefectNoticeBiz.updateAgainPerfectNoticUp(taskId, senderId, resultMsg,sendcount, objectMap, conditionMap, baseBiz);
					}
					else
					{
						// 已经没有需要发送的完美通知人员
						EmpExecutionContext.error("完美通知发送线程，没有需要发送的人员 ,taskid："+taskId);
						break;
					}
				}
				resultMsg = "";
				effFileName = "";
				//通过taskid更新主表信息
				if(!prefectNoticeBiz.updateLfPerfectNotic(conditionMap, objectMap, taskId, sendcount+1)){
					EmpExecutionContext.error("完美通知发送线程，更新LfPerfectNotic表信息出现异常 ,taskid："+taskId);
					break;
				}
				if((sendcount + 1) < lfPerfectNotic.getMaxSendCount())
				{
					EmpExecutionContext.info("完美通知发送线程，等待发送中；taskid："+taskId+",间隔时间："+delay);
					Thread.sleep(delay);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "完美通知发送线程，发送出现异常！taskid：" + taskId);
		}finally{
			EmpExecutionContext.info("完美通知发送线程，发送完毕，taskid：" + taskId + ", corpcode: "+ corpCode);
		}

	}
	
	

	

}
