/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-10-14 上午09:18:03
 */
package com.montnets.emp.common.timer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.GlobalVariableBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SubnoManagerBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.MonAlarmDsmParams;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.dao.TimeSerDAO;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import com.montnets.emp.util.ChangeCharset;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-10-14 上午09:18:03
 *        <p>
 *        修改记录1：
 *        </p>
 * 
 *        <pre>
 *      修改日期：
 *      修改人：
 *      修改内容：
 * </pre>
 */

public class SpBalanceAlarmSmsTask extends Thread
{
	BalanceLogBiz	balanceBiz	= new BalanceLogBiz();

	TimeSerDAO		timeSerDAO	= new TimeSerDAO();

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日HH点mm分");
	
	public void run()
	{
		// SP账号告警集合，KEY:企业编码；value:对应的通知人集合
		Map<String, List<MonAlarmDsmParams>> batchAlarmDsmMap = new HashMap<String, List<MonAlarmDsmParams>>();
		TimerManagerBiz timerManagerBiz = TimerManagerBiz.getTMInstance();
		//任务运行标识
		boolean runState = true;
		while(StaticValue.isSpUserBalanceAlarmState())
		{
			try
			{
				// 线程等待60s
				Thread.sleep((long)60 * 1000);
				//获取任务运行标识
				runState = timerManagerBiz.checkTimeSerRun();
				//本机运行
				if(runState)
				{
					long start = System.currentTimeMillis();
					int sendSmsCount = 0;
					EmpExecutionContext.info("SP账号余额阀值告警短信发送开始");
					// 获取SP账号告警短信发送对象集合
					this.setSpUserAlarmList(batchAlarmDsmMap);
					if(batchAlarmDsmMap == null || batchAlarmDsmMap.size() < 1)
					{
						EmpExecutionContext.info("SP账号余额阀值告警短信发送结束，耗时:"+(System.currentTimeMillis() - start)
								+"ms，无需要发送告警的记录");
						continue;
					}
					// admin操作员信息
					LfSysuser adminUser;
					// 获取发送的SP账号
					String sendSpUser;
					// 以key企业编码为批次发送短信
					for (String corpCode : batchAlarmDsmMap.keySet())
					{
						// 通过企业编码获取admin操作员信息
						adminUser = this.getAdminInfoBycorpCode(corpCode);
						if(adminUser == null)
						{
							EmpExecutionContext.error("SP账号余额阀值告警短信发送，通过企业编码获取admin操作员信息失败，corpCode:" + corpCode);
							continue;
						}
						// 获取发送的SP账号
						sendSpUser = getSendAlarmSmsSpUser(adminUser);
						if(sendSpUser == null || sendSpUser.trim().length() < 1)
						{
							EmpExecutionContext.error("SP账号余额阀值告警短信发送，没有可用的sp账号！corpCode：" + adminUser.getCorpCode());
							continue;
						}
						// 发送告警短信
						this.sendAlramDsm(adminUser, sendSpUser, batchAlarmDsmMap.get(corpCode));
						sendSmsCount += batchAlarmDsmMap.get(corpCode).size();
					}
					EmpExecutionContext.info("SP账号余额阀值告警短信发送结束，耗时:"+(System.currentTimeMillis() - start)
							+"ms，共处理"+sendSmsCount+"条告警记录");
					batchAlarmDsmMap.clear();
				}
				else
				{
					EmpExecutionContext.info("SP账号余额阀值告警短信发送，运行标识为非本机");
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送异常。");
				batchAlarmDsmMap.clear();
			}
		}
	}

	/**
	 * 获取SP账号告警短信发送对象集合
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 上午11:27:30
	 */
	private Map<String, List<MonAlarmDsmParams>> setSpUserAlarmList(Map<String, List<MonAlarmDsmParams>> batchAlarmDsmMap)
	{
		try
		{
			// 获取SP账号告警信息
			List<DynaBean> spFeeAlarmList = timeSerDAO.getSpFeeAlarmList();
			if(spFeeAlarmList != null && spFeeAlarmList.size() > 0)
			{
				// 告警短信
				String msg;
				// 企业编码
				String corpCode;
				// 要告警的SP账号
				String spUser;
				// SP账号余额告警阀值
				Long threshold;
				// 通知人姓名
				String noticeName;
				// 告警手机号码
				String alarmPhone;
				DynaBean dynaBean;
				// 上条记录的SP账号
				String lastSpUser = null;
				// 上条记录的企业编码
				String lastCorpCode = null;
				// SP账号告警号码及内容集合
				List<MonAlarmDsmParams> batchAlarmDsmList = new ArrayList<MonAlarmDsmParams>();
				// SP账号告警号码及内容对象
				MonAlarmDsmParams alarmDsmParams = null;
				for (int i = 0; i < spFeeAlarmList.size(); i++)
				{
					dynaBean = spFeeAlarmList.get(i);
					alarmPhone = dynaBean.get("alarmphone") != null ? dynaBean.get("alarmphone").toString().trim() : "";
					spUser = dynaBean.get("spuser") != null ? dynaBean.get("spuser").toString().trim() : "";
					corpCode = dynaBean.get("corpcode") != null ? dynaBean.get("corpcode").toString().trim() : "";
					// SP账号为空或未设置手机号码
					if(corpCode.length() < 1 || spUser.length() < 1 || alarmPhone.length() < 1)
					{
						continue;
					}
					threshold = dynaBean.get("threshold") != null ? Long.parseLong(dynaBean.get("threshold").toString().trim()) : 0L;
					noticeName = dynaBean.get("noticename") != null ? dynaBean.get("noticename").toString().trim() : "";
					// 获取告警短信内容
					msg = getAlarmSmsContent(spUser, noticeName, threshold);
					// 告警短信内容为空
					if(msg == null || msg.length() < 1)
					{
						continue;
					}
					// 设置告警对象
					alarmDsmParams = new MonAlarmDsmParams();
					alarmDsmParams.setPhone(alarmPhone);
					alarmDsmParams.setMsg(msg);
					// 第一条记录，设置上条记录SP账号为当前账号
					if(lastSpUser == null && lastCorpCode == null)
					{
						//设置上条记录SP账号和企业编码为当前账号和企业编码
						lastSpUser = spUser;
						lastCorpCode = corpCode;
						batchAlarmDsmList.add(alarmDsmParams);
					}
					else if(lastSpUser.equals(spUser))
					{
						batchAlarmDsmList.add(alarmDsmParams);
					}
					else
					{
						// 以上个SP账号和告警次数为0做为条件更新告警次数为1
						// 更新成功
						if(timeSerDAO.setSpUserAlarmCount(lastSpUser))
						{
							// 如果集合中已存在该企业
							if(batchAlarmDsmMap.containsKey(lastCorpCode))
							{
								// 增加到集合中的告警对象列表中
								batchAlarmDsmMap.get(lastCorpCode).addAll(batchAlarmDsmList);
							}
							// 集合不存在该企业
							else
							{
								// 设置到集合中
								batchAlarmDsmMap.put(lastCorpCode, batchAlarmDsmList);
							}
						}
						else
						{
							EmpExecutionContext.error("SP账号余额阀值告警短信发送，为告警次数为0做为条件更新SP账号告警次数为1时失败，spUser:" + lastSpUser + "，corpCode:" + corpCode);
						}
						//设置上条记录SP账号和企业编码为当前账号和企业编码
						lastSpUser = spUser;
						lastCorpCode = corpCode;
						//新声明一个账号队列
						batchAlarmDsmList = new ArrayList<MonAlarmDsmParams>();
						//设置队列信息
						batchAlarmDsmList.add(alarmDsmParams);
					}
				}
				//处理最后一个SP账号
				if(spFeeAlarmList.size() > 0)
				{
					// 以上个SP账号和告警次数为0做为条件更新告警次数为1
					// 更新成功
					if(timeSerDAO.setSpUserAlarmCount(lastSpUser))
					{
						// 如果集合中已存在该企业
						if(batchAlarmDsmMap.containsKey(lastCorpCode))
						{
							// 增加到集合中的告警对象列表中
							batchAlarmDsmMap.get(lastCorpCode).addAll(batchAlarmDsmList);
						}
						// 集合不存在该企业
						else
						{
							// 设置到集合中
							batchAlarmDsmMap.put(lastCorpCode, batchAlarmDsmList);
						}
					}
					else
					{
						EmpExecutionContext.error("SP账号余额阀值告警短信发送，为告警次数为0做为条件更新SP账号告警次数为1时失败，spUser:" + lastSpUser + "，lastCorpCode:" + lastCorpCode);
					}
				}
				spFeeAlarmList.clear();
				spFeeAlarmList=null;
				return batchAlarmDsmMap;
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送，获取SP账号告警短信发送对象集合失败。");
			return null;
		}
	}

	/**
	 * 通过企业编码获取admin操作员信息
	 * 
	 * @description
	 * @param corpCode
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 下午08:00:05
	 */
	private LfSysuser getAdminInfoBycorpCode(String corpCode)
	{
		try
		{
			// admin用户信息
			LfSysuser adminUser = null;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// admin用户
			conditionMap.put("userName", "admin");
			// 当前登录用户的企业编码
			conditionMap.put("corpCode", corpCode);
			// 获取admin用户信息
			List<LfSysuser> AdminLfSysuerList = new BaseBiz().findListByCondition(LfSysuser.class, conditionMap, null);
			if(AdminLfSysuerList != null && AdminLfSysuerList.size() > 0)
			{
				adminUser = AdminLfSysuerList.get(0);
			}
			else
			{
				EmpExecutionContext.error("SP账号余额阀值告警短信发送，通过企业编码获取admin用户信息失败，corpCode:" + corpCode);
			}
			return adminUser;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送，通过企业编码获取admin操作员信息异常。corpCode:" + corpCode);
			return null;
		}
	}

	/**
	 * 设置SP账号余额告警短信内容
	 * 
	 * @description
	 * @param spUser
	 *        SP账号
	 * @param threshold
	 *        通知人
	 * @param threshold
	 *        告警阀值
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 上午11:08:54
	 */
	public String getAlarmSmsContent(String spUser, String noticeName, Long threshold)
	{
		try
		{
			// 设置SP账号余额告警短信内容
			StringBuffer content = new StringBuffer();
			// 通知人不为空
			if(noticeName != null && noticeName.length() > 0)
			{
				content.append("尊敬的").append(noticeName).append("，");
			}
			content.append("SP账号[").append(spUser).append("]").append("短信余额不足").append(threshold).append("条，请及时充值，以免影响正常使用，谢谢！");
			return content.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SP账号余额告警短信内容异常，spUser:" + spUser + "，threshold" + threshold + "，noticeName:" + noticeName);
			return null;
		}
	}

	/**
	 * 获取发送SP账号
	 * 
	 * @description
	 * @param corpCode
	 *        企业编码
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 上午11:58:22
	 */
	public String getSendAlarmSmsSpUser(LfSysuser adminUser)
	{
		try
		{
			// sp账号
			String sendSpUser = "";
			// 检查SP账号状态，1通过 2失败 没有绑定一条上下行路由/下行路由 3失败 发送账号没有绑定路由 4
			// 该发送账号的全通道号有一个超过20位
			String isUsable = "";
			ErrorCodeParam errorCode = new ErrorCodeParam();
			// 扩展尾号
			String subno = GlobalVariableBiz.getInstance().getValidSubno(StaticValue.VERIFYREMIND_MENUCODE, 0, adminUser.getCorpCode(), errorCode);
			// 获取sp账号
			List<Userdata> spUserList = new SmsBiz().getSpUserListByAdmin(adminUser);
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
							sendSpUser = spUser.getUserId();
							break;
						}
					}
					else
					{
						EmpExecutionContext.error("SP账号余额阀值告警短信发送，获取发送SP账号的子号为空，spUser:" + spUser + "corpCode：" + adminUser.getCorpCode());
					}
				}
			}
			return sendSpUser;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送，获取发送SP账号异常！corpCode：" + adminUser.getCorpCode());
			return null;
		}
	}

	/**
	 * 不同号码不同内容告警信息发送
	 * 
	 * @description
	 * @param adminUser
	 * @param sendSpUser
	 * @param batchAlarmDsmList
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 下午06:41:00
	 */
	private void sendAlramDsm(LfSysuser adminUser, String sendSpUser, List<MonAlarmDsmParams> batchAlarmDsmList)
	{
		try
		{
			// 机构扣费是否成功
			boolean isChargeSuc = false;
			// 统计发送条数
			int icount = new SmsBiz().getCountDSmsSend(batchAlarmDsmList, sendSpUser);
			if(icount < 1)
			{
				EmpExecutionContext.error("SP账号余额阀值告警短信发送统计发送条数出现错误，，corpCode:" + adminUser.getCorpCode() + "，spUser:" + sendSpUser);
				return;
			}

			LfMttask mTask = new LfMttask();
			// 发送短信总数(网关发送总数)
			mTask.setIcount(String.valueOf(icount));
			mTask.setSpUser(sendSpUser);
			mTask.setCorpCode(adminUser.getCorpCode());
			// 运营商扣费结果
			String wgresult = balanceBiz.wgKoufei(mTask);
			if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || wgresult.indexOf("lessgwfee") > -1)
			{
				EmpExecutionContext.error("SP账号余额阀值告警短信发送运营商余额扣费失败，wgresult:" + wgresult + "，corpCode:" + adminUser.getCorpCode() + "，spUser:" + sendSpUser);
				return;
			}
			// 开启机构扣费
			if(balanceBiz.IsChargings(adminUser.getUserId()))
			{
				// 机构扣费
				int feeResult = balanceBiz.sendSmsAmountByUserId(null, adminUser.getUserId(), icount);
				// 0:短信扣费成功
				if(feeResult == 0)
				{
					isChargeSuc = true;
				}
				// -2:短信余额不足
				else if(feeResult == -2)
				{
					EmpExecutionContext.error("SP账号余额阀值告警短信发送短信机构余额不足，corpCode:" + adminUser.getCorpCode() + "，icount:" + icount);
				}
				else
				{
					EmpExecutionContext.error("SP账号余额阀值告警短信发送机构扣费失败:" + feeResult + "，corpCode:" + adminUser.getCorpCode() + "，icount:" + icount);
				}
			}
			// 发送短信(机构扣费失败也进行发送)
			String[] result = sendDsm(sendSpUser, "", batchAlarmDsmList, "0");
			// 提交网关失败
			if(result == null || !"000".equals(result[0]))
			{
				// 运营商余额回收
				balanceBiz.huishouFee(icount, sendSpUser, 1);
				// 机构费用回收
				if(isChargeSuc)
				{
					balanceBiz.sendSmsAmountByUserId(null, adminUser.getUserId(), icount * -1);
				}
				EmpExecutionContext.error("SP账号余额阀值告警短信发送失败，userId:" + adminUser.getUserId() + "，corpCode:" + adminUser.getCorpCode() + "，icount:" + icount + "，sendSpUser:" + sendSpUser);
			}
			else
			{
				EmpExecutionContext.info("SP账号余额阀值告警短信发送成功，corpCode:" + adminUser.getCorpCode() + "，icount:" + icount + "，spUser:" + sendSpUser);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送异常！userId:" + adminUser.getUserId() + "，corpCode:" + adminUser.getCorpCode() + "，spUser:" + sendSpUser);
		}
	}

	/**
	 * 不同号码不同内容发送接口
	 * 
	 * @description
	 * @param sendSpUser
	 * @param subno
	 * @param batchAlarmDsmList
	 * @param userCord
	 * @return
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午06:52:00
	 */
	public String[] sendDsm(String sendSpUser, String subno, List<MonAlarmDsmParams> batchAlarmDsmList, String userCord)
	{

		try
		{
			WgMsgConfigBiz wgMsgBiz = new WgMsgConfigBiz();
			HttpSmsSend jobBiz = new HttpSmsSend();
			String[] resultReceive = new String[2];
			Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(sendSpUser);
			String webGateResult;
			WGParams wgParams = new WGParams();
			wgParams.setCommand("MULTIX_MT_REQUEST");
			// spuser帐户
			wgParams.setSpid(sendSpUser);
			// 密码
			wgParams.setSppassword(userdata.getUserPassword());
			// 用户编码
			wgParams.setParam1(userCord);
			// 拓展子号
			wgParams.setSa(subno);
			// 优先级
			wgParams.setPriority("1");
			// 默认业务编码
			wgParams.setSvrtype("M00000");
			// 模块ID
			wgParams.setModuleid(StaticValue.MONITORSMS_MENUCODE);
			// 设值为0的不需要状态报告，其他值是需要状态报告
			wgParams.setRptflag("1");
			// 发送信息
			StringBuffer dasm = new StringBuffer();
			for (MonAlarmDsmParams monAlarmDsm : batchAlarmDsmList)
			{
				dasm.append(monAlarmDsm.getPhone()).append("/").append(ChangeCharset.encodeHex(monAlarmDsm.getMsg().getBytes(ChangeCharset.GBK))).append(",");
			}
			dasm.deleteCharAt(dasm.lastIndexOf(","));
			wgParams.setDasm(dasm.toString());
			EmpExecutionContext.info("SP账号余额阀值告警短信发送提交网关开始，spUser:" + sendSpUser);
			// 调用发送接口
			webGateResult = jobBiz.createbatchMtRequest(wgParams);
			// 解析返回信息
			resultReceive[1] = parseWebgateResult(webGateResult, "mtmsgid");
			int index = webGateResult.indexOf("mterrcode");
			resultReceive[0] = webGateResult.substring(index + 10, index + 13);
			EmpExecutionContext.info("SP账号余额阀值告警短信发送提交网关结束，spUser:" + sendSpUser + "，返回状态：" + resultReceive[0]);
			if(!resultReceive[0].equals("000"))
			{
				resultReceive[0] = webGateResult.substring(index - 8, index - 1);
				EmpExecutionContext.error("SP账号余额阀值告警短信发送失败，短信网关返回错误代码：" + resultReceive[0]);
			}

			return resultReceive;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送失败，spUser:" + sendSpUser);
			return null;
		}
	}

	/**
	 * 处理返回值
	 * 
	 * @description
	 * @param webGateResult
	 * @param param
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-14 下午12:16:13
	 */
	private String parseWebgateResult(String webGateResult, String param)
	{

		try
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
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "SP账号余额阀值告警短信发送，处理网关返回值失败.webGateResult:" + webGateResult);
			return null;
		}
	}
}
