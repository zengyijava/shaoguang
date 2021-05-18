/**
 * Program : AlarmSMSTask.java
 * Author : zousy
 * Create : 2013-11-19 下午07:33:17
 * company ShenZhen Montnets Technology CO.,LTD.
 */

package com.montnets.emp.monitor.biz;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SmsSendBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.MonAlarmDsmParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.ChangeCharset;

/**
 * @author zousy
 * @version 1.0.0
 * @2013-11-19 下午07:33:17
 */
public class sendAlarmSms extends Thread
{

	private IEmpTransactionDAO	smsTransDao;

	SmsBiz						smsbiz	= new SmsBiz();
	
	BaseBiz	baseBiz	= new BaseBiz();
	
	AlarmSmsBiz					alarmSmsBiz	= new AlarmSmsBiz();
	
	@SuppressWarnings("unused")
	private LfSysuser			sysuser;

	private String				msg;

	private String				phone;

	private String				corpCode;
	//告警发送类型，0:不同号码相同内容；1：不同号码不同内容
	private int 				flag = 0;

	public sendAlarmSms(LfSysuser sysuser, String msg, String phone, int flag)
	{
		this.sysuser = sysuser;
		if(sysuser == null)
		{
			corpCode = "100001";
		}
		else
		{

			// 当前登录用户的企业编码
			corpCode = sysuser.getCorpCode();

		}
		this.msg = msg;
		this.phone = phone;
		this.flag = flag;
		smsTransDao = new DataAccessDriver().getEmpTransDAO();
	}
	
	public sendAlarmSms(LfSysuser sysuser, String msg, String phone)
	{
		this.sysuser = sysuser;
		if(sysuser == null)
		{
			corpCode = "100001";
		}
		else
		{

			// 当前登录用户的企业编码
			corpCode = sysuser.getCorpCode();

		}
		this.msg = msg;
		this.phone = phone;
		smsTransDao = new DataAccessDriver().getEmpTransDAO();
	}

	/**
	 * 发送线程
	 */

	public void run()
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// sp账号
		String sp = "";
		LfSysuser AdminUser = null;
		// 获取sp账号
		try
		{
			conditionMap.put("userName", "admin");// admin用户
			conditionMap.put("corpCode", corpCode);// 当前登录用户的企业编码
			List<LfSysuser> AdminLfSysuerList = baseBiz.findListByCondition(LfSysuser.class, conditionMap, null);
			if(AdminLfSysuerList != null && AdminLfSysuerList.size() > 0)
			{
				AdminUser = AdminLfSysuerList.get(0);
			}
			String isUsable = "";
			ErrorCodeParam errorCode = new ErrorCodeParam();
			// 扩展尾号
			String subno = GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, corpCode, errorCode);
			// 获取sp账号
			List<Userdata> spUserList = smsbiz.getSpUserListByAdmin(AdminUser);
			// spuserlist为空说明没有可用的账号
			// 验证该发送账号的全通道号是否长度超过20位，绑定是否绑定了一条上下行路由
			// 发送账号
			// isUsable 1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4
			// 该发送账号的全通道号有一个超过20位
			if(spUserList.size() > 0)
			{
				for (Userdata spUser : spUserList)
				{
					if(subno != null)
					{
						isUsable = new SubnoManagerBiz().checkPortUsed(spUser.getUserId(), subno);
						if("1".equals(isUsable))
						{
							if("checksuccess".equals(smsbiz.checkSpUser(spUser.getUserId(), 2, 1)))
							{
								sp = spUser.getUserId();
								break;
							}
						}
					}
					else
					{
						EmpExecutionContext.error("监控告警短信发送，SP账号扩展尾号为空。spUser:"+spUser.getUserId());
					}
				}
			}
		}
		catch (Exception e2)
		{
			EmpExecutionContext.error(e2, "监控告警短信发送获取sp账号失败！");
			return;
		}
		if(sp == null || "".equals(sp))
		{
			EmpExecutionContext.info("监控告警短信发送，获取sp账号为空。");
		}
		//不同号码相同内容
		if(flag == 0)
		{
			this.alramSsm(AdminUser, sp);
		}
		//不同号码不同内容
		else
		{
			this.alramDsm(AdminUser, sp);
		}
	}

	/**
	 * 不同号码相同内容告警信息发送
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午02:35:20
	 */
	private void alramSsm(LfSysuser AdminUser, String sp)
	{
		try
		{
			String[] phoneList = null;
			if(phone.indexOf(",") != -1)
			{
				phoneList = phone.split(",");
			}
			else
			{
				phoneList = new String[1];
				phoneList[0] = phone;
			}
			//运营商号段
			String[] haoduans = new WgMsgConfigBiz().getHaoduan();
			if(!"".equals(sp) && sp != null)
			{
				// 扣费
				BalanceLogBiz balanceBiz = new BalanceLogBiz();
				// 机构扣费是否成功
				boolean isChargeSuc = false;
				// SP账号扣费是否成功
				boolean isSpUserChargeSuc = false;
				// 判断流程是否结束的标识
				boolean okToSend = true;
				// 统计发送条数
				int icount = getSMSCount(sp, msg, phoneList, haoduans);
				if(icount < 1)
				{
					EmpExecutionContext.error("监控告警短信发送统计发送条数出现错误！spUser:"+sp);
					return;
				}

				LfMttask mTask = new LfMttask();
				mTask.setIcount(String.valueOf(icount));// 发送短信总数(网关发送总数)
				mTask.setSpUser(sp);
				mTask.setCorpCode(corpCode);
				String wgresult = "";
				try
				{
					wgresult = balanceBiz.wgKoufei(mTask);
					if(okToSend && ("nogwfee".equals(wgresult) || "feefail".equals(wgresult) ||  wgresult.indexOf("lessgwfee") > -1))
					{
						okToSend = false;
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "监控告警短信运营商余额扣费失败：" + IErrorCode.B20011+"spUser:"+sp);
					okToSend = false;
				}
				try
				{
					if(okToSend)
					{
						//开户机构扣费
						if(balanceBiz.IsChargings(AdminUser.getUserId()))
						{
							//机构扣费
							int feeResult = sendAlarmSmsAmount(AdminUser, icount);
							// 0:短信扣费成功
							if(feeResult == 0)
							{
								isChargeSuc = true;
							}
							// -2:短信余额不足
							else if(feeResult == -2)
							{
								EmpExecutionContext.error("监控告警短信发送短信机构余额不足，spUser:"+sp+",icount:"+icount+",feeResult:"+feeResult);
							}
							else
							{
								EmpExecutionContext.error("监控告警短信发送短信机构扣费失败:" + feeResult+"，spUser:"+sp+",icount:"+icount);
							}
						}
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "监控告警短信发送机构扣费异常。");
				}
				String[] result = null;
				// 发送短信(机构扣费失败也允许发送)
				if(okToSend)
				{
					try
					{
						result = sendmsg(sp, "", msg, phone, "0");
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "监控告警短信发送异常！");
						result = new String[] {"exception"};
					}
				}
				// 提交网关成功
				if(result != null && "000".equals(result[0]))
				{
					//暂停1秒，避免发送记录还没保存在MT_TASK表中
					try
					{
						Thread.sleep(1000L);
					}
					catch (InterruptedException e)
					{
						EmpExecutionContext.error(e, "提交网关成功后,暂停线程异常!");
						Thread.currentThread().interrupt();
					}
					// 以mtmsgid为条件查询MT_TASK表对应记录并更新至告警短信发送记录表中，默认状态为0：失败
					String msgidStr = result[1];
					alarmSmsBiz.batchAddAlarmSmsRecord(msgidStr, phoneList, msg);
				}
				//提交网关失败,使用网优发送
				else
				{
					//新增短信告警发送记录，默认状态为0：失败，返回自增ID
					String id = alarmSmsBiz.addAlSmsRecordReturnId(phoneList, msg, haoduans);
					// 是否加载网优模块，17：网优
					if(!smsbiz.isWyModule("17"))
					{
						// 运营商余额回收
						if(okToSend)
						{
							balanceBiz.huishouFee(icount,sp, 1);
						}
						//机构费用回收
						if(isChargeSuc)
						{
							sendAlarmSmsAmount(AdminUser, 0-icount);
						}
						EmpExecutionContext.error("未加载网优模块，告警信息无法使用网优通道发送。");
						return;
					}
					// 获取网优通道及SIM卡信息
					String[] gateInfo = new MonitorDAO().getIpcomAndSimInfo();
					if(gateInfo == null || "0".equals(gateInfo[1]) || "0".equals(gateInfo[2]) || "0".equals(gateInfo[3]))
					{
						gateInfo = gateInfo == null?new String[] {"0","0","0","0"}:gateInfo;
						EmpExecutionContext.error("使用网优通道发送告警短信失败，获取网优通道及SIM卡信息异常!IP:" + gateInfo[1]+"，端口："+gateInfo[2]+"，SIM卡："+gateInfo[3]);
						return;
					}
					// 网优通道发送
					String sendResult = new SmsSendBiz().wySend(gateInfo, phone, msg);
					if("success".equals(sendResult))
					{
						if(id != null && !"".equals(id))
						{
							//去掉最后一个逗号
							if(id.lastIndexOf(",") > 0)
							{
								id = id.substring(0, id.lastIndexOf(","));
							}
							//以自增ID为条件更新告警短信发送记录
							alarmSmsBiz.setAlarmSmsRecord(id, 0, "2", "1");
						}
					}
					else
					{
						// 运营商余额回收
						if(okToSend)
						{
							balanceBiz.huishouFee(icount,sp, 1);
						}
						//机构费用回收
						if(isChargeSuc)
						{
							sendAlarmSmsAmount(AdminUser, 0-icount);
						}
						EmpExecutionContext.error("告警信息使用网优通道发送异常。");
						return;
					}

				}
			}
			else
			{
				//新增短信告警发送记录，默认状态为0：失败，返回自增ID
				String id = alarmSmsBiz.addAlSmsRecordReturnId(phoneList, msg, haoduans);
				// 是否加载网优模块，17：网优
				if(!smsbiz.isWyModule("17"))
				{
					EmpExecutionContext.error("未加载网优模块，告警信息无法使用网优通道发送。");
					return;
				}
				// 获取网优通道及SIM卡信息
				String[] gateInfo = new MonitorDAO().getIpcomAndSimInfo();
				if(gateInfo == null || "0".equals(gateInfo[1]) || "0".equals(gateInfo[2]) || "0".equals(gateInfo[3]))
				{
					EmpExecutionContext.error("使用网优通道发送告警短信失败，获取网优通道及SIM卡信息异常!IP:" + gateInfo[1]+"，端口："+gateInfo[2]+"，SIM卡："+gateInfo[3]);
					return;
				}
				// 网优通道发送
				String sendResult = new SmsSendBiz().wySend(gateInfo, phone, msg);
				if("success".equals(sendResult))
				{
					if(id != null && !"".equals(id))
					{
						//去掉最后一个逗号
						if(id.lastIndexOf(",") > 0)
						{
							id = id.substring(0, id.lastIndexOf(","));
						}
						//以自增ID为条件更新告警短信发送记录为1:成功
						alarmSmsBiz.setAlarmSmsRecord(id, 0, "2", "1");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送告警短信异常！");
		}
	}
	
	/**
	 * 不同号码不同内容告警信息发送
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午02:35:42
	 */
	private void alramDsm(LfSysuser AdminUser, String sp)
	{
		try
		{
			//
			List<MonAlarmDsmParams> alarmDsmList = new ArrayList<MonAlarmDsmParams>();
			List<MonAlarmDsmParams> batchAlarmDsmList = new ArrayList<MonAlarmDsmParams>();
			MonAlarmDsmParams monAlarmDsm = new MonAlarmDsmParams();
			alarmDsmList.addAll(MonitorStaticValue.getMonAlarmDsmList());
			for(int i=0; i<alarmDsmList.size(); i++)
			{
				monAlarmDsm = alarmDsmList.get(i);
				batchAlarmDsmList.add(monAlarmDsm);
				if((i+1) % 8 == 0)
				{
					sendAlramDsm(AdminUser, sp, batchAlarmDsmList);
					batchAlarmDsmList.clear();
				}
			}
			if(batchAlarmDsmList.size() > 0)
			{
				sendAlramDsm(AdminUser, sp, batchAlarmDsmList);
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "发送告警短信异常！");
		}
	}
	
	/**
	 * 不同号码不同内容告警信息发送
	 * @description    
	 * @param AdminUser
	 * @param sp
	 * @param batchAlarmDsmList       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午06:50:59
	 */
	private void sendAlramDsm(LfSysuser AdminUser, String sp, List<MonAlarmDsmParams> batchAlarmDsmList)
	{

		try
		{
			//运营商号段
			String[] haoduans = new WgMsgConfigBiz().getHaoduan();
			if(!"".equals(sp) && sp != null)
			{
				// 扣费
				BalanceLogBiz balanceBiz = new BalanceLogBiz();
				// 机构扣费是否成功
				boolean isChargeSuc = false;
				// 判断流程是否结束的标识
				boolean okToSend = true;
				// 统计发送条数
				int icount = smsbiz.getCountDSmsSend(batchAlarmDsmList, sp);
				if(icount < 1)
				{
					EmpExecutionContext.error("监控告警短信发送统计发送条数出现错误！");
					return;
				}

				LfMttask mTask = new LfMttask();
				mTask.setIcount(String.valueOf(icount));// 发送短信总数(网关发送总数)
				mTask.setSpUser(sp);
				mTask.setCorpCode(corpCode);
				String wgresult = "";
				try
				{
					wgresult = balanceBiz.wgKoufei(mTask);
					if(okToSend && ("nogwfee".equals(wgresult) || "feefail".equals(wgresult) ||  wgresult.indexOf("lessgwfee") > -1))
					{
						okToSend = false;
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "监控告警短信运营商余额扣费失败：" + IErrorCode.B20011);
					okToSend = false;
				}
				try
				{
					if(okToSend)
					{
						//开启机构扣费
						if(balanceBiz.IsChargings(AdminUser.getUserId()))
						{
							//机构扣费
							int feeResult = sendAlarmSmsAmount(AdminUser, icount);
							// 0:短信扣费成功
							if(feeResult == 0)
							{
								isChargeSuc = true;
							}
							// -2:短信余额不足
							else if(feeResult == -2)
							{
								EmpExecutionContext.error("监控告警短信发送短信机构余额不足");
							}
							else
							{
								EmpExecutionContext.error("监控告警短信发送短信机构扣费失败未知错误:" + feeResult);
							}
						}
					}
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "监控告警短信发送机构扣费异常。");
				}
				String[] result = null;
				// 发送短信(机构扣费失败也允许发送)
				if(okToSend)
				{
					try
					{
						result = sendDsm(sp, "", batchAlarmDsmList, "0");
					}
					catch (Exception e)
					{
						EmpExecutionContext.error(e, "监控告警短信发送异常！");
						result = new String[] {"exception"};
					}
				}
				// 提交网关成功
				if(result != null && "000".equals(result[0]))
				{
					//暂停1秒，避免发送记录还没保存在MT_TASK表中
					try
					{
						Thread.sleep(1000L);
					}
					catch (InterruptedException e)
					{
						EmpExecutionContext.error(e, "提交网关成功后,暂停线程异常!");
						Thread.currentThread().interrupt();
					}
					// 以mtmsgid为条件查询MT_TASK表对应记录并更新至告警短信发送记录表中，默认状态为0：失败
					String msgidStr = result[1];
					alarmSmsBiz.batchAddAlarmDsmRecord(msgidStr, batchAlarmDsmList);
				}
				//提交网关失败,使用网优发送
				else
				{
					//新增短信告警发送记录，默认状态为0：失败，返回自增ID
					String id = alarmSmsBiz.addDsmAlSmsRecordReturnId(batchAlarmDsmList, haoduans);
					// 是否加载网优模块，17：网优
					if(!smsbiz.isWyModule("17"))
					{
						// 运营商余额回收
						if(okToSend)
						{
							balanceBiz.huishouFee(icount,sp, 1);
						}
						//机构费用回收
						if(isChargeSuc)
						{
							sendAlarmSmsAmount(AdminUser, 0-icount);
						}
						EmpExecutionContext.error("未加载网优模块，告警信息无法使用网优通道发送。");
						return;
					}
					// 获取网优通道及SIM卡信息
					String[] gateInfo = new MonitorDAO().getIpcomAndSimInfo();
					if(gateInfo == null || "0".equals(gateInfo[1]) || "0".equals(gateInfo[2]) || "0".equals(gateInfo[3]))
					{
						gateInfo = gateInfo == null?new String[] {"0","0","0","0"}:gateInfo;
						EmpExecutionContext.error("使用网优通道发送告警短信失败，获取网优通道及SIM卡信息异常!IP:" + gateInfo[1]+"，端口："+gateInfo[2]+"，SIM卡："+gateInfo[3]);
						return;
					}
					// 网优通道发送
					String sendResult = new SmsSendBiz().wySend(gateInfo, phone, msg);
					if("success".equals(sendResult))
					{
						if(id != null && !"".equals(id))
						{
							//去掉最后一个逗号
							if(id.lastIndexOf(",") > 0)
							{
								id = id.substring(0, id.lastIndexOf(","));
							}
							//以自增ID为条件更新告警短信发送记录
							alarmSmsBiz.setAlarmSmsRecord(id, 0, "2", "1");
						}
					}
					else
					{
						// 运营商余额回收
						if(okToSend)
						{
							balanceBiz.huishouFee(icount,sp, 1);
						}
						//机构费用回收
						if(isChargeSuc)
						{
							sendAlarmSmsAmount(AdminUser, 0-icount);
						}
						EmpExecutionContext.error("告警信息使用网优通道发送异常。");
						return;
					}

				}
			}
			else
			{
				//新增短信告警发送记录，默认状态为0：失败，返回自增ID
				String id = alarmSmsBiz.addDsmAlSmsRecordReturnId(batchAlarmDsmList, haoduans);
				// 是否加载网优模块，17：网优
				if(!smsbiz.isWyModule("17"))
				{
					EmpExecutionContext.error("未加载网优模块，告警信息无法使用网优通道发送。");
					return;
				}
				// 获取网优通道及SIM卡信息
				String[] gateInfo = new MonitorDAO().getIpcomAndSimInfo();
				if(gateInfo == null || "0".equals(gateInfo[1]) || "0".equals(gateInfo[2]) || "0".equals(gateInfo[3]))
				{
					EmpExecutionContext.error("使用网优通道发送告警短信失败，获取网优通道及SIM卡信息异常!IP:" + gateInfo[1]+"，端口："+gateInfo[2]+"，SIM卡："+gateInfo[3]);
					return;
				}
				// 网优通道发送
				String sendResult = new SmsSendBiz().wySend(gateInfo, phone, msg);
				if("success".equals(sendResult))
				{
					if(id != null && !"".equals(id))
					{
						//去掉最后一个逗号
						if(id.lastIndexOf(",") > 0)
						{
							id = id.substring(0, id.lastIndexOf(","));
						}
						//以自增ID为条件更新告警短信发送记录为1:成功
						alarmSmsBiz.setAlarmSmsRecord(id, 0, "2", "1");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控告警短信发送失败！spUser:"+sp);
		}
	
	}
	
	/**
	 * 发送接口，下行阀值提醒的结果
	 * 
	 * @param sp
	 *        sp账号
	 * @param subno
	 *        子号
	 * @param msg
	 *        内容
	 * @param phone
	 *        手机号
	 * @param userCord
	 *        用户编码
	 * @return
	 * @throws Exception
	 */
	public String[] sendmsg(String sp, String subno, String msg, String phone, String userCord) throws Exception
	{

		WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
		HttpSmsSend jobBiz = new HttpSmsSend();
		String[] resultReceive = new String[2];
		Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
		String webGateResult;
		WGParams wgParams = new WGParams();
		wgParams.setCommand("MULTI_MT_REQUEST");
		// spuser帐户
		wgParams.setSpid(sp);
		// 密码
		wgParams.setSppassword(userdata.getUserPassword());
		// 用户编码
		wgParams.setParam1(userCord);
		// 拓展子号
		wgParams.setSa(subno);
		// 手机
		// wgParams.setDa(phone);
		wgParams.setDas(phone);
		wgParams.setPriority("1");
		// 短信内容
		wgParams.setSm(msg);
		// 默认业务编码
		wgParams.setSvrtype("M00000");
		// 模块ID
		wgParams.setModuleid(StaticValue.MONITORSMS_MENUCODE);
		// 设值为0的不需要状态报告，其他值是需要状态报告
		wgParams.setRptflag("1");
		EmpExecutionContext.info("监控告警短信不同号码相同内容发送开始，spUser:"+sp);
		webGateResult = jobBiz.createbatchMtRequest(wgParams);
		resultReceive[1] = parseWebgateResult(webGateResult, "mtmsgid");
		int index = webGateResult.indexOf("mterrcode");
		resultReceive[0] = webGateResult.substring(index + 10, index + 13);
		if(!resultReceive[0].equals("000"))
		{
			resultReceive[0] = webGateResult.substring(index - 8, index - 1);
			EmpExecutionContext.error("监控告警短信发送失败，短信网关返回错误代码：" + resultReceive[0]);
		}

		return resultReceive;
	}

	/**
	 * 不同号码不同内容发送接口
	 * @description    
	 * @param sp
	 * @param subno
	 * @param batchAlarmDsmList
	 * @param userCord
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午06:52:00
	 */
	public String[] sendDsm(String sp, String subno, List<MonAlarmDsmParams> batchAlarmDsmList, String userCord) throws Exception
	{

		try
		{
			WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
			HttpSmsSend jobBiz = new HttpSmsSend();
			String[] resultReceive = new String[2];
			Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sp);
			String webGateResult;
			WGParams wgParams = new WGParams();
			wgParams.setCommand("MULTIX_MT_REQUEST");
			// spuser帐户
			wgParams.setSpid(sp);
			// 密码
			wgParams.setSppassword(userdata.getUserPassword());
			// 用户编码
			wgParams.setParam1(userCord);
			// 拓展子号
			wgParams.setSa(subno);
			//优先级
			wgParams.setPriority("1");
			// 默认业务编码
			wgParams.setSvrtype("M00000");
			// 模块ID
			wgParams.setModuleid(StaticValue.MONITORSMS_MENUCODE);
			// 设值为0的不需要状态报告，其他值是需要状态报告
			wgParams.setRptflag("1");
			//发送信息
			StringBuffer dasm = new StringBuffer();
			for(MonAlarmDsmParams monAlarmDsm : batchAlarmDsmList)
			{
				dasm.append(monAlarmDsm.getPhone()).append("/")
				.append(ChangeCharset.encodeHex(monAlarmDsm.getMsg().getBytes(ChangeCharset.GBK))).append(",");
			}
			dasm.deleteCharAt(dasm.lastIndexOf(","));
			wgParams.setDasm(dasm.toString());
			EmpExecutionContext.info("监控告警短信不同号码不同内容发送开始，spUser:"+sp);
			//调用发送接口
			webGateResult = jobBiz.createbatchMtRequest(wgParams);
			//解析返回信息
			resultReceive[1] = parseWebgateResult(webGateResult, "mtmsgid");
			int index = webGateResult.indexOf("mterrcode");
			resultReceive[0] = webGateResult.substring(index + 10, index + 13);
			EmpExecutionContext.info("监控告警短信发送结束，spUser:"+sp+"，返回状态："+resultReceive[0]);
			if(!resultReceive[0].equals("000"))
			{
				resultReceive[0] = webGateResult.substring(index - 8, index - 1);
				EmpExecutionContext.error("监控告警短信发送失败，短信网关返回错误代码：" + resultReceive[0]);
			}

			return resultReceive;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控告警短信发送失败，短信提交网关失败。spUser:"+sp);
			return null;
		}
	}
	
	/**
	 * 解析网关返回信息
	 * @description    
	 * @param webGateResult
	 * @param param
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午06:51:36
	 */
	private String parseWebgateResult(String webGateResult, String param)
	{

		String[] testArray = webGateResult.split("&");

		String strParam;
		int index = -1;
		String value = "";
		for (int i = 0; i < testArray.length; i++)
		{
			strParam = testArray[i];
			index = strParam.indexOf(param + "=");
			if(index == -1)
			{
				continue;
			}

			value = strParam.substring(index + 8);
			break;
		}

		return value;
	}

	/**
	 * 阀值提醒短信扣费回收
	 * 
	 * @description
	 * @param connection
	 * @param lfSysuser
	 * @param sendCount
	 *        大于0扣费
	 * @return
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-28 下午07:24:31
	 */
	private int sendAlarmSmsAmount(LfSysuser lfSysuser, int sendCount)
	{
		// 默认返回失败
		int result = -1;
		// 操作标识
		long depId = lfSysuser.getDepId();
		CallableStatement cs = null;
		Connection connection = null;
		try
		{
			connection = smsTransDao.getConnection();
			// 调用存储过程
			try
			{
				cs = connection.prepareCall("{call LF_FEEDEDTV0(?,?,?)}");
				cs.setLong(1, depId);
				cs.setLong(2, sendCount);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);
				cs.execute();
				// 返回值
				result = cs.getInt(3);
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "监控告警短信发送扣费调用异常!");
				if(sendCount > 0)
				{
					// 短信扣费失败
					result = -1;
				}
				else
				{
					// 短信回收失败
					result = -9;
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "监控告警短信发送扣费调用异常!");
			result = -9999;
		}
		finally
		{
			try
			{
				if(cs != null)
				{
					cs.close();
				}
				if(connection != null)
				{
					smsTransDao.closeConnection(connection);
				}
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "监控告警短信发送扣费回收接口关闭存储过程异常!");
			}

		}
		return result;
	}

	/**
	 * 短信条数
	 * @description    
	 * @param sp
	 * @param msg
	 * @param phoneList
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-8 下午04:46:17
	 */
	public int getSMSCount(String sp, String msg, String[] phoneList, String[] haoduans)
	{
		
		int count = 0;
		try
		{
			// 拿到当前系统配置的号段
			//List<GtPortUsed> portsList = smsbiz.getPortByUserId(sp);
			
			for (int i = 0; i < phoneList.length; i++)
			{
				count += smsbiz.countAllOprSmsNumber(msg, phoneList[i], haoduans, sp);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "统计发送条数出现异常！");
			count = 0;
		}
		return count;
	}
}
