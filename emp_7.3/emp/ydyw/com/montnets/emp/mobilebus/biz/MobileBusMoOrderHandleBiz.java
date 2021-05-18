/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-14 上午08:41:28
 */
package com.montnets.emp.mobilebus.biz;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfMotask;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.ydyw.*;
import com.montnets.emp.mobilebus.constant.MobileBusStaticValue;
import com.montnets.emp.mobilebus.dao.MobileBusMoOrderHandleDAO;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.smstask.HttpSmsSend;
import com.montnets.emp.smstask.WGParams;
import org.apache.commons.beanutils.DynaBean;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.*;
import java.util.Map.Entry;

/**移动业务上行指令处理BIZ
 * @description
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2015-1-14 上午08:41:28
 */

public class MobileBusMoOrderHandleBiz extends SuperBiz
{

	MobileBusMoOrderHandleDAO	moOrderHandleDAO	= new MobileBusMoOrderHandleDAO();

	SmsBiz						smsbiz				= new SmsBiz();

	BalanceLogBiz				balanceBiz			= new BalanceLogBiz();

	IEmpTransactionDAO			smsTransDao			= new DataAccessDriver().getEmpTransDAO();

	WgMsgConfigBiz				wgMsgBiz			= new WgMsgConfigBiz();

	HttpSmsSend					httpSmsSend			= new HttpSmsSend();
	
	BuckleFeeBiz                buckleFeeBiz        = new BuckleFeeBiz();

	/**
	 * 上行指令处理
	 * 
	 * @description
	 * @param moTask
	 * @return ture 处理成功; false 处理失败
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-14 上午09:35:51
	 */
	public boolean OrderHandle(LfMotask moTask)
	{
		try
		{
			EmpExecutionContext.info("移动业务上行指令，处理开始。ptMsgId=" + moTask.getPtMsgId());
			boolean runResult = true;
			// 上行指令业务处理并返回下行短信内容
			String msg = MoOrdeHandle(moTask);
			if(msg != null && !"".equals(msg))
			{
				// 指令下行回复
				runResult = sendMtSms(moTask, msg);
			}
			else
			{
				EmpExecutionContext.error("返回指令下行短信内容为空！msg:" + msg);
				runResult = false;
			}
			EmpExecutionContext.info("移动业务上行指令，处理结束。ptMsgId=" + moTask.getPtMsgId() + ",结果：" + runResult);

			return runResult;
		}
		catch (Exception e)
		{
			// 捕获异常
			EmpExecutionContext.error(e, "移动业务指令上行业务处理上行信息异常。ptMsgId=" + moTask.getPtMsgId());
			return false;
		}
	}

	/**
	 * 上行指令处理
	 * 
	 * @description
	 * @param moTask
	 *        上行指令对象
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-14 上午10:46:51
	 */
	private String MoOrdeHandle(LfMotask moTask)
	{
		try
		{
			//获取套餐关系信息
			List<DynaBean> lfTaoCanCmdList = moOrderHandleDAO.getTaoCanCmd(moTask);
			if(lfTaoCanCmdList != null && lfTaoCanCmdList.size() > 0)
			{
				DynaBean lfTaoCanCmd = lfTaoCanCmdList.get(0);
				// 套餐编码
				String taoCanCode = lfTaoCanCmd.get("taocan_code") == null ? "" : lfTaoCanCmd.get("taocan_code").toString();
				// 套餐名称
				String taoCanName = "";
				// 套餐欠费补扣次数
				String timer = "";
				// 非全局指令,判断套餐是否存在或是启动状态
				if(!"".equals(taoCanCode))
				{
					
					//查询套餐基本信息
					List<DynaBean> lfBusTaoCanList = moOrderHandleDAO.getBusTaoCan(taoCanCode, moTask.getCorpCode());
					if(lfBusTaoCanList !=null && lfBusTaoCanList.size() > 0)
					{
						taoCanName = lfBusTaoCanList.get(0).get("taocan_name")==null?"":lfBusTaoCanList.get(0).get("taocan_name").toString();
						timer = lfBusTaoCanList.get(0).get("buckup_maxtimer")==null?"":lfBusTaoCanList.get(0).get("buckup_maxtimer").toString();
					}
					else
					{
						EmpExecutionContext.error("上行指令对应套餐编码不存在！指令ID:" + moTask.getMoOrder() + "，指令内容：" + moTask.getMsgContent());
						return null;
					}
				}
				// 上行内容
				String msgContent = moTask.getMsgContent();
				// 账号
				String accoutNo = "";
				//指令分割符
				String orderDelimiter = MobileBusStaticValue.getOrderDelimiter();
				//上行内容长度
				int msgLen = msgContent.length();
				// 上行内容带有账号
				if(msgContent.indexOf(orderDelimiter) > -1 && !orderDelimiter.equals(msgContent.subSequence(msgLen-1, msgLen)))
				{
					accoutNo = msgContent.split(orderDelimiter)[1];
				}
				// 指令类型
				String structType = lfTaoCanCmd.get("struct_type") == null ? "" : lfTaoCanCmd.get("struct_type").toString();
				if(!"".equals(structType))
				{
					// 根据套餐指令处理业务流程
					int index = taoCanBusHandle(structType, lfTaoCanCmd, moTask, accoutNo, taoCanCode, taoCanName, timer);
					
					if(index < 0)
					{
						EmpExecutionContext.error("移动业务上行指令业务处理失败。手机号:" + moTask.getPhone() + "，指令ID:" + moTask.getMoOrder() + "，指令内容：" + moTask.getMsgContent());
						return null;
					}

					// 指令下行短信回复内容
					return getMtSmsMsg(index, lfTaoCanCmd, taoCanName);
				}
				else
				{
					EmpExecutionContext.error("移动业务上行指令类型为空，指令ID:" + moTask.getMoOrder() + "，指令内容：" + moTask.getMsgContent());
					return null;
				}
			}
			else
			{
				EmpExecutionContext.error("移动业务上行指令代码错误，指令ID:" + moTask.getMoOrder() + "，指令内容：" + moTask.getMsgContent());
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "处理移动业务上行指令异常！手机号:" + moTask.getPhone() + "，指令ID:" + moTask.getMoOrder() + "，指令内容：" + moTask.getMsgContent());
			return null;
		}

	}

	/**
	 * 套餐业务处理流程
	 * 
	 * @description
	 * @param structType
	 *        指令类型,0:订阅套餐;1:退订套餐;3:全局退订套餐
	 * @param lfTaoCanCmd
	 *        套餐对应关系对象
	 * @param moTask
	 *        上行指令对象
	 * @param accoutNo
	 *        上行账号
	 * @param taoCanCode
	 *        套餐编码
	 * @return 100： 订阅成功；101：重复订阅 ；200： 退订成功；300：全局退订成功
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-15 下午04:11:19
	 */
	private int taoCanBusHandle(String structType, DynaBean lfTaoCanCmd, LfMotask moTask, String accoutNo, String taoCanCode, String taoCanName, String timer)
	{
		// 返回标识
		int index = -1;
		try
		{
			// 上行手机号码
			String mobile = moTask.getPhone();
			// 签约账户信息
			List<LfContract> lfContractList = null;
			try
			{
				// 根据手机号码查询签约信息
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("mobile", mobile);
				conditionMap.put("corpcode", moTask.getCorpCode());
				conditionMap.put("isvalid", "0");
				conditionMap.put("contractstate", "0");
				// 上行指令带有账号
				if(accoutNo != null && !"".equals(accoutNo))
				{
					conditionMap.put("acctno", accoutNo);
				}
				lfContractList = empDao.findListByCondition(LfContract.class, conditionMap, null);
				// 查询不到签约信息
				if(lfContractList == null || lfContractList.size() <= 0)
				{
					EmpExecutionContext.error("通过上行指令获取不到对应签约信息。手机号码：" + mobile + "，指令内容：" + moTask.getMsgContent());
					return index;
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "通过上行手机号码查询签约信息异常！手机号码：" + mobile + "，指令内容：" + moTask.getMsgContent());
				return index;
			}

			// 套餐订购指令
			if("0".equals(structType))
			{
				index = subscription(moTask, lfContractList, taoCanCode, lfTaoCanCmd, taoCanName, timer);
			}
			// 套餐退订指令
			else if("1".equals(structType))
			{
				index = unSubscription(moTask, lfContractList, taoCanCode, structType);
			}
			// 全局退订指令
			else if("3".equals(structType))
			{
				index = unSubscription(moTask, lfContractList, taoCanCode, structType);
			}
			return index;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "指令上行套餐业务处理流程异常！");
			return -1;
		}

	}

	/**
	 * 订购套餐业务处理流程
	 * 
	 * @description
	 * @param moTask
	 *        指令对象
	 * @param lfContractList
	 *        签约信息
	 * @param taoCanCode
	 *        套餐编码
	 * @param lfTaoCanCmd
	 *        套餐对应关系对象
	 * @return -2：订阅失败；100： 订阅成功；101：重复订阅
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-15 上午11:57:54
	 */
	private int subscription(LfMotask moTask, List<LfContract> lfContractList, String taoCanCode, DynaBean lfTaoCanCmd, String taoCanName, String timer)
	{
		// 返回值
		int index = -1;
		try
		{
			// 套餐类型
			int taocanType = Integer.parseInt(lfTaoCanCmd.get("taocan_type").toString());
			// 套餐金额
			int taocanMoney = Integer.parseInt(lfTaoCanCmd.get("taocan_money").toString());
			// 是否当天扣费,false:否;true:是
			boolean isBuckleTime = false;
			// 扣费时间
			Calendar buckleTime = null;
			if(taocanType != 1)
			{
				// 扣费时间
				buckleTime = getBuckleTime(taoCanCode);
				if(buckleTime != null)
				{
					//当前时间
					Date date = new Date();
					//当天需要扣费
					if(date.getYear()+1900 == buckleTime.get(Calendar.YEAR) && date.getMonth() == buckleTime.get(Calendar.MONTH) 
							&& date.getDate() == buckleTime.get(Calendar.DATE))
					{
						isBuckleTime = true;
						//设置下次扣费时间
						if(taocanType == 2){
							buckleTime.add(Calendar.MONTH, 1);
						}else if(taocanType == 3){
							buckleTime.add(Calendar.MONTH, 3);
						}else if(taocanType == 4){
							buckleTime.add(Calendar.YEAR, 1);
						}
					}
				}
				else
				{
					EmpExecutionContext.error("上行订阅指令获取扣费时间异常！taoCanCode：" + taoCanCode + "，taocanType：" + taocanType);
					return -2;
				}
			}
			// 订购套餐列表
			List<LfContractTaocan> lfContractTaocanList = new ArrayList<LfContractTaocan>();
			// 订购套餐信息
			LfContractTaocan lfContractTaocan = null;
			//机构套餐统计列表
			Map<Long, LfDepTaocan> lfDepTaocanMap = new HashMap<Long, LfDepTaocan>();
			//机构套餐统计
			LfDepTaocan lfDepTaocan = null;
			// 客户计费信息列表
			List<LfDeductionsDisp> lfDeductionsDisp = new ArrayList<LfDeductionsDisp>();
			// 客户计费信息表
			LfDeductionsDisp disp = new LfDeductionsDisp();
			// 客户计费结算流水列表
			List<LfDeductionsList> lfDeductionsList = new ArrayList<LfDeductionsList>();
			// 客户计费结算流水表
			LfDeductionsList list = new LfDeductionsList();
			// 签约ID
			StringBuffer contractIdStrBf = new StringBuffer();
			// 签约信息列表,签约ID为KEY,签约对象为VALUE
			Map<Long, LfContract> lfContractMap = new HashMap<Long, LfContract>();
			// 签约信息内容
			LfContract lfContractInfo = null;
			// 操作员ID
			StringBuffer userId = new StringBuffer();
			for (LfContract lfContract : lfContractList)
			{
				// 拼接签约ID
				contractIdStrBf.append(lfContract.getContractid()).append(",");
				// 签约信息放入MAP
				lfContractMap.put(lfContract.getContractid(), lfContract);
			}
			//去掉最后一个逗号
			String contractIdStr = contractIdStrBf.toString();
			if(contractIdStr.length() > 0 && contractIdStr.indexOf(",") > -1)
			{
				contractIdStr = contractIdStr.substring(0, contractIdStr.length()-1);
			}
			// 扣费信息
			StringBuffer buckleFee = new StringBuffer();
			// 根据签ID查询已签约上行套餐编码的签约信息
			List<DynaBean> lfContractBindList = moOrderHandleDAO.getContractBind(contractIdStr, taoCanCode);
			if(lfContractBindList != null)
			{
				//流水号
				String msgId = "";
				String lsatBuckle = "1";
				//是否最后一次扣费
				if(timer != null && !"".equals(timer))
				{
					if(Integer.valueOf(timer) > 0)
					{
						lsatBuckle = "0";
					}
				}
				// 需要订购的套餐有至少一个账号已开通此套餐
				if(lfContractBindList.size() > 0)
				{
					// 是否已开通过套餐，false:未开通;true:已开通
					boolean isOpen = false;
					for (long contractIdMap : lfContractMap.keySet())
					{
						// 重置状态
						isOpen = false;
						for (DynaBean lfContractBind : lfContractBindList)
						{
							// 已开通此套餐的签约ID
							long contractId = -1L;
							if(lfContractBind.get("contractid") == null)
							{
								// contractid异常,进入下一次
								continue;
							}
							else
							{
								// 获取开通此套餐的签约ID
								contractId = Long.parseLong(lfContractBind.get("contractid").toString());
								// 签约ID相等,说明已开通此套餐
								if(contractIdMap - contractId == 0)
								{
									// 设置此签约ID为已开通此套餐
									isOpen = true;
									// 退出此次循环,进入到MAP的下一次循环
									break;
								}
							}
						}
						// 未开通此套餐
						if(!isOpen)
						{
							// 设置签约与套餐对应关系对象
							lfContractInfo = new LfContract();
							lfContractInfo = lfContractMap.get(contractIdMap);
							lfContractTaocan = setContractTaocan(lfContractInfo, contractIdMap, taoCanCode, taocanType, taocanMoney, buckleTime, taoCanName);
							lfDepTaocan = new LfDepTaocan();
							// 设置机构套餐统
							if(!lfDepTaocanMap.containsKey(lfContractInfo.getContractuser()))
							{
								lfDepTaocan.setContractdep(lfContractInfo.getContractdep());
								lfDepTaocan.setContractuser(lfContractInfo.getContractuser());
								lfDepTaocan.setCorpcode(lfContractInfo.getCorpcode());
								lfDepTaocan.setTaocancode(taoCanCode);
								lfDepTaocanMap.put(lfContractInfo.getContractuser(), lfDepTaocan);
								userId.append(lfContractInfo.getContractuser()).append(",");
							}
							//当天扣费
							if(isBuckleTime)
							{
								//设置客户计费信息表
								disp = new LfDeductionsDisp();
								//客户计费结算流水表
								list = new LfDeductionsList();
								//获取流水号
								msgId = buckleFeeBiz.getMsgId(lfContractInfo.getCorpcode());
								//设置计费信息
								setBuckleFeeInfo(disp, list, lfContractInfo);
								disp.setDeductionsmoney(taocanMoney);
								disp.setTaocancode(taoCanCode);list.setTaocancode(taoCanCode);
								disp.setTaocanmoney(taocanMoney);list.setTaocanmoney(taocanMoney);
								disp.setTaocantype(taocanType);list.setTaocantype(taocanType);
								disp.setTaocanname(taoCanName);list.setTaocanname(taoCanName);
								disp.setMsgid(msgId);list.setMsgid(msgId);
								String acctNo = lfContractInfo.getAcctno();
								//扣费信息
								if(acctNo != null && !"".equals(acctNo))
								{
									buckleFee.append(msgId).append("|").append(lfContractInfo.getMobile()).append("|").append(lfContractInfo.getAcctno())
									.append("|").append(taoCanCode).append("|").append(taocanMoney).append("|").append("0").append("|").append(lsatBuckle)
									.append("|").append(System.currentTimeMillis()).append("&");
								}
								lfDeductionsDisp.add(disp);
								lfDeductionsList.add(list);
								//设置最后一次扣费时间
								if(lfContractTaocan != null)
								{
									lfContractTaocan.setLastbucklefee(buckleFeeBiz.getFlag(taocanType));
								}
							}
							if(lfContractTaocan != null)
							{
								lfContractTaocanList.add(lfContractTaocan);
							}
						}
					}
				}
				// 需要订购的套餐无账号开通此套餐
				else
				{
					for (long contractIdMap : lfContractMap.keySet())
					{
						// 设置签约与套餐对应关系对象
						lfContractInfo = new LfContract();
						lfContractInfo = lfContractMap.get(contractIdMap);
						lfContractTaocan = setContractTaocan(lfContractInfo, contractIdMap, taoCanCode, taocanType, taocanMoney, buckleTime, taoCanName);
						lfDepTaocan = new LfDepTaocan();
						// 设置机构套餐统
						if(!lfDepTaocanMap.containsKey(lfContractInfo.getContractuser()))
						{
							lfDepTaocan.setContractdep(lfContractInfo.getContractdep());
							lfDepTaocan.setContractuser(lfContractInfo.getContractuser());
							lfDepTaocan.setCorpcode(lfContractInfo.getCorpcode());
							lfDepTaocan.setTaocancode(taoCanCode);
							lfDepTaocanMap.put(lfContractInfo.getContractuser(), lfDepTaocan);
							userId.append(lfContractInfo.getContractuser()).append(",");
						}
						//当天扣费
						if(isBuckleTime)
						{
							//设置客户计费信息表
							disp = new LfDeductionsDisp();
							//客户计费结算流水表
							list = new LfDeductionsList();
							//获取流水号
							msgId = buckleFeeBiz.getMsgId(lfContractInfo.getCorpcode());
							//设置计费信息
							setBuckleFeeInfo(disp, list, lfContractInfo);
							disp.setDeductionsmoney(taocanMoney);
							disp.setTaocancode(taoCanCode);list.setTaocancode(taoCanCode);
							disp.setTaocanmoney(taocanMoney);list.setTaocanmoney(taocanMoney);
							disp.setTaocantype(taocanType);list.setTaocantype(taocanType);
							disp.setTaocanname(taoCanName);list.setTaocanname(taoCanName);
							disp.setMsgid(msgId);list.setMsgid(msgId);
							String acctNo = lfContractInfo.getAcctno();
							//扣费信息
							if(acctNo != null && !"".equals(acctNo))
							{
								buckleFee.append(msgId).append("|").append(lfContractInfo.getMobile()).append("|").append(lfContractInfo.getAcctno())
								.append("|").append(taoCanCode).append("|").append(taocanMoney).append("|").append("0").append("|").append(lsatBuckle)
								.append("|").append(System.currentTimeMillis()).append("&");
							}
							lfDeductionsDisp.add(disp);
							lfDeductionsList.add(list);
							//设置最后一次扣费时间
							if(lfContractTaocan != null)
							{
								lfContractTaocan.setLastbucklefee(buckleFeeBiz.getFlag(taocanType));
							}
						}
						if(lfContractTaocan != null)
						{
							lfContractTaocanList.add(lfContractTaocan);
						}
					}
				}
			}
			else
			{
				EmpExecutionContext.error("获取签约套餐关系异常！签约ID：" + contractIdStrBf.toString() + "，套餐编码：" + taoCanCode);
				return -2;
			}
			// 存在可订购套餐签约账户
			if(lfContractTaocanList != null && lfContractTaocanList.size() > 0)
			{
				return contractHandle(taoCanCode, isBuckleTime, buckleFee.toString(), lfContractTaocanList, lfDeductionsList, lfDeductionsDisp, lfDepTaocanMap, userId.toString());
			}
			// 无可订购套餐约签账户
			else
			{
				EmpExecutionContext.info("指令上行订阅套餐，签约账号已开通此套餐，上行指令内容:" + moTask.getMsgContent());
				// 重复订阅
				index = 101;
				return index;
			}
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "上行指令订购套餐业务处理流程异常！上行指令内容：" + moTask.getMsgContent());
			return -2;
		}
	}

	/**
	 * 签约处理
	 * @description    
	 * @param isBuckleTime 是否当天扣费
	 * @param buckleFee	扣费信息
	 * @param lfContTcanList 签约套餐关系
	 * @param list  计费信息
	 * @param disp  计费流水
	 * @param lfDepTaocanMap  机构套餐
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-28 下午12:28:12
	 */
	private int contractHandle(String taoCanCode,boolean isBuckleTime, String buckleFee, List<LfContractTaocan> lfContTcanList, List<LfDeductionsList> list, List<LfDeductionsDisp> disp, Map<Long, LfDepTaocan> lfDepTaocanMap, String userId)
	{
		int index = -2;
		// 更新签约客户套餐对应关系表返条数
		int updateCount = 0;
		// 更新计费表返回条数
		int updateDisp = 0;
		//更新流水表返回条数
		int updateList = 0;
		try
		{
			boolean buckleFeeResult = false;
			//如果为当天扣费并且有扣费信息,提交扣费
			if(isBuckleTime && buckleFee.length() > 0)
			{
				//扣费操作
				buckleFeeResult = buckleFeeBiz.saveBuckleFeeFile(buckleFee.toString());
				if(!buckleFeeResult)
				{
					EmpExecutionContext.error("指令上行订阅扣费失败!");
					return -2;
				}
			}
			// 新增套餐，更新数据库
			updateCount = empDao.save(lfContTcanList, LfContractTaocan.class);
			// 订阅套餐成功，返回100；失败，返回-2
			if(updateCount > 0)
			{
				index = 100;
				//更新机构套餐表
				if(lfDepTaocanMap != null && lfDepTaocanMap.size()>0)
				{
					int count = saveDepTaocan(lfDepTaocanMap, taoCanCode, userId);
					EmpExecutionContext.info("更新机构套餐表返回值:"+count);
				}
			}
			else
			{
				EmpExecutionContext.error("更新签约套餐对应关系表失败！");
				return -2;
			}
			//更新计费表
			if(isBuckleTime)
			{
				Connection conn = empTransDao.getConnection();
				boolean isSuccess=true;
				try
				{
					empTransDao.beginTransaction(conn);
					//操作失败,循环三次
					for(int i=0; i<3; i++)
					{
						//更新计费表
						updateList = empTransDao.save(conn, list, LfDeductionsList.class);
						if(updateList < 1)
						{
							empTransDao.rollBackTransaction(conn);
							EmpExecutionContext.error("更新客户计费信息列表失败！");
							isSuccess = false;
						}
						else
						{
							//更新流水表
							updateDisp = empTransDao.save(conn, disp, LfDeductionsDisp.class);
							if(updateDisp < 1)
							{
								empTransDao.rollBackTransaction(conn);
								EmpExecutionContext.error("更新客户计费结算流水表失败！");
								isSuccess = false;
							}
						}
						if(isSuccess)
						{
							break;
						}
					}
					//执行成功,提交事务
					if(isSuccess)
					{
						empTransDao.commitTransaction(conn);
					}
				}
				catch (Exception e)
				{
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error(e, "更新客户计费信息列表及客户计费结算流水表异常！");
				}finally{
					empTransDao.closeConnection(conn);
				}
			}
			return index;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "指令上行订阅套餐，更新签约客户套餐对应关系表异常！");
			index = -2;
			return index;
		}
	}
	/**
	 * 退订套餐业务处理流程
	 * 
	 * @description
	 * @param moTask
	 *        指令对象
	 * @param lfContractList
	 *        签约信息
	 * @param taoCanCode
	 *        套餐编码
	 * @param structType
	 *        1:套餐退订;3:全局退订(退订账号下所有套餐)
	 * @return -3：退订失败；200： 套餐退订成功；300：全局退订成功
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-15 下午02:06:28
	 */
	private int unSubscription(LfMotask moTask, List<LfContract> lfContractList, String taoCanCode, String structType)
	{
		// 返回值
		int index = -1;
		// 更新签约客户套餐对应关系状态返回条数
		int updateResult = 0;
		try
		{
			// 签约ID
			StringBuffer contractIdStrBf = new StringBuffer();
			for (LfContract lfContract : lfContractList)
			{
				// 拼接签约ID
				contractIdStrBf.append(lfContract.getContractid()).append(",");
			}
			String contractId = contractIdStrBf.toString();
			if(contractId.length() > 0 && contractId.indexOf(",") > -1)
			{
				contractId = contractId.substring(0, contractId.length()-1);
			}
			// 修改签约套餐关系为禁用
			if(contractId != null && contractId.length() > 0)
			{
				updateResult = moOrderHandleDAO.updateContractTaocan(contractId, taoCanCode, structType);
			}
			//更新成功
			if(updateResult > 0)
			{
				if("1".equals(structType))
				{
					//套餐退订
					return 200;
				}
				else
				{
					//全局退订
					return 300;
				}
			}
			else if(updateResult == 0)
			{
				if("1".equals(structType))
				{
					//套餐退订未开通此套餐
					return 201;
				}
				else
				{
					//全局退订未开通套餐
					return 301;
				}
			}
			else
			{
				EmpExecutionContext.error("上行指令退订套餐，更新签约套餐关系表异常或无对应的更新数据！");
				return -3;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "上行指令退订套餐业务处理流程异常！上行指令内容：" + moTask.getMsgContent());
			index = -3;
			return index;
		}
	}

	/**
	 * 整合下行短信内容
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-14 上午10:47:00
	 */
	private String getMtSmsMsg(int index, DynaBean lfTaoCanCmd, String taoCanName)
	{
		String msg = null;
		try
		{
			// 短信回复模板
			String reSmsMsgTmp = MobileBusStaticValue.getReSmsMsgList().get(String.valueOf(index));
			//套餐类型
			String taoCanType = lfTaoCanCmd.get("taocan_type") == null ? "" : lfTaoCanCmd.get("taocan_type").toString();
			// 套餐订购成功
			if(index == 100)
			{
				//套餐金额
				String taoCanMoney = lfTaoCanCmd.get("taocan_money") == null ? "" : lfTaoCanCmd.get("taocan_money").toString();
				msg = String.format(reSmsMsgTmp, taoCanName, taoCanMoney, taoCanTypeChange(taoCanType));
			}
			// 套餐重复订购或套餐退订成功
			else if(index == 101 || index == 200 || index == 201)
			{
				msg = String.format(reSmsMsgTmp, taoCanName);
			}
			// 套餐全局订退成功
			else if(index == 300 || index == 301)
			{
				msg = reSmsMsgTmp;
			}
			return msg;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "整合下行短信内容异常！");
			return null;
		}
	}
	
	/**
	 * 套餐类型描述
	 * 
	 * @description
	 * @param taoCanType
	 *        套餐类型
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-15 下午05:06:54
	 */
	private String taoCanTypeChange(String taoCanType)
	{
		String taoCanTypeDes = "";
		try
		{
			if(!"".equals(taoCanType))
			{
				if("1".equals(taoCanType))
				{
					taoCanTypeDes = "VIP免费";
				}
				else if("2".equals(taoCanType))
				{
					taoCanTypeDes = "月";
				}
				else if("3".equals(taoCanType))
				{
					taoCanTypeDes = "季";
				}
				else if("4".equals(taoCanType))
				{
					taoCanTypeDes = "年";
				}
				else
				{
					return taoCanTypeDes;
				}
			}
			return taoCanTypeDes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置套餐类型描述异常！");
			return "";
		}
	}

	/**
	 * 下行短信回复
	 * @description    
	 * @param moTask
	 * @param msg
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-19 上午11:44:04
	 */
	private boolean sendMtSms(LfMotask moTask, String msg)
	{
		boolean result = true;

		try
		{
			// 获取短信条数
			int count = getSMSCount(moTask, msg);
			if(count < 1)
			{
				EmpExecutionContext.error("移动业务指令下行回复，获取短信条数异常！phone:" + moTask.getPhone() + "，msg:" + msg + "，SP:" + moTask.getSpUser());
				return false;
			}
			// 扣费
			boolean isBuckleFee = BuckleFee(moTask, count);
			if(!isBuckleFee)
			{
				EmpExecutionContext.error("移动业务指令下行回复，扣费失败！phone:" + moTask.getPhone() + "，msg:" + msg + "，SP:" + moTask.getSpUser());
				return false;
			}
			// 短信发送
			String[] resultReceive = sendmsg(moTask, msg);
			//提交至网关成功
			if(resultReceive != null && "000".equals(resultReceive[0]))
			{
				return result;
			}
			//失败
			else
			{
				EmpExecutionContext.error("移动业务指令下行回复短信发送失败，phone:" + moTask.getPhone() + "，msg:" + msg + "，SP:" + moTask.getSpUser());
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("移动业务指令下行短信回复失败，phone:" + moTask.getPhone() + "，msg:" + msg + "，SP:" + moTask.getSpUser());
			return false;
		}
		
	}

	/**
	 * 获取短信条数
	 * 
	 * @description
	 * @param moTask
	 *        上行指令对象
	 * @param msg
	 *        短信内容
	 * @return 短信条数
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-16 下午07:05:29
	 */
	private int getSMSCount(LfMotask moTask, String msg)
	{
		int count = 0;
		try
		{
			// 获取信息条数
			count = smsbiz.countAllOprSmsNumber(msg, moTask.getPhone(), null, moTask.getSpUser());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "统计发送条数出现异常！");
			count = 0;
		}
		return count;
	}

	/**
	 * 扣费
	 * 
	 * @description
	 * @param moTask
	 * @param count
	 * @return true:成功;false:失败
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-19 上午11:10:26
	 */
	private boolean BuckleFee(LfMotask moTask, int count)
	{
		boolean result = true;
		LfMttask mTask = new LfMttask();
		// 发送短信条数
		mTask.setIcount(String.valueOf(count));
		mTask.setSpUser(moTask.getSpUser());
		mTask.setCorpCode(moTask.getCorpCode());
		String wgresult = "";
		// 运营商扣费
		wgresult = balanceBiz.wgKoufei(mTask);
		// 扣费失败
		if("nogwfee".equals(wgresult) || "feefail".equals(wgresult) || wgresult.indexOf("lessgwfee") > -1)
		{
			EmpExecutionContext.error("移动业务手机下行短信回复运营商余额扣费失败！");
			return false;
		}
		// 获取企业管理员对象
		LfSysuser admin = getAdminInCorp(moTask.getCorpCode());
		// 获取失败
		if(admin == null)
		{
			EmpExecutionContext.error("移动业务手机下行短信回复机构扣费，获取不到企业管理员。corpCode：" + moTask.getCorpCode());
			return false;
		}
		// 开启机构扣费
		if(SystemGlobals.isDepBilling(moTask.getCorpCode()))
		{
			int feeResult = balanceBiz.sendSmsAmountByUserId(null, admin.getUserId(), count);
			// 机构扣费失败，短信正常回复
			if(feeResult < 0)
			{
				EmpExecutionContext.error("移动业务手机下行短信回复机构扣费失败，返回值：" + feeResult);
			}
		}
		return result;
	}

	/**
	 * 获取企业管理员对象
	 * 
	 * @description
	 * @param corpCode
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-19 上午09:50:34
	 */
	private LfSysuser getAdminInCorp(String corpCode)
	{
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 企业编码
			conditionMap.put("corpCode", corpCode);
			conditionMap.put("userName", "admin");

			// admin的用户信息
			List<LfSysuser> sysUsersList = empDao.findListByCondition(LfSysuser.class, conditionMap, null);
			if(sysUsersList != null && sysUsersList.size() > 0)
			{
				return sysUsersList.get(0);
			}
			return null;
		}
		catch (Exception e1)
		{
			EmpExecutionContext.error(e1, "查询操作员失败！");
			return null;
		}
	}

	/**
	 *  下行回复短信发送
	 * @description    
	 * @param moTask
	 * @param msg
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-19 下午01:35:14
	 */
	private String[] sendmsg(LfMotask moTask, String msg)
	{
		try
		{
			String[] resultReceive = new String[2];
			Userdata userdata = wgMsgBiz.getSmsUserdataByUserid(moTask.getSpUser());
			String webGateResult;
			WGParams wgParams = new WGParams();
			wgParams.setCommand("MULTI_MT_REQUEST");
			// spuser帐户
			wgParams.setSpid(moTask.getSpUser());
			// 密码
			wgParams.setSppassword(userdata.getUserPassword());
			// 用户编码
			wgParams.setParam1("0");
			// 有尾号，则设置尾号
			String subNo = moTask.getSubno();
			if(subNo != null && subNo.trim().length() > 0 && !"*".equals(subNo))
			{
				wgParams.setSa(subNo);
			}
			// 手机
			wgParams.setDas(moTask.getPhone());
			// 设置发送优先级
			wgParams.setPriority("1");
			// 短信内容
			wgParams.setSm(msg);
			// 默认业务编码
			wgParams.setSvrtype("M00000");
			// 设值为0的不需要状态报告，其他值是需要状态报告
			wgParams.setRptflag("0");
			//发送请求至网关
			webGateResult = httpSmsSend.createbatchMtRequest(wgParams);
			//处理回值结果
			resultReceive[1] = parseWebgateResult(webGateResult, "mtmsgid");
			int index = webGateResult.indexOf("mterrcode");
			resultReceive[0] = webGateResult.substring(index + 10, index + 13);
			//提交网关返回失败
			if(!resultReceive[0].equals("000"))
			{
				resultReceive[0] = webGateResult.substring(index - 8, index - 1);
				EmpExecutionContext.error("移动业务手机下行回复短信发送失败，网关返回错误代码：" + resultReceive[0]);
			}

			return resultReceive;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "移动业务手机下行回复短信发送失败异常！手机号："+moTask.getPhone()+"，SP账号："+moTask.getSpUser());
			return null;
		}
	}

	/**
	 * 短信发送返回值处理
	 * 
	 * @description
	 * @param webGateResult
	 * @param param
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-16 上午11:35:58
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
	 * 获取扣费时间
	 * @description    
	 * @param taoCanCode  套餐编码
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-26 下午07:04:41
	 */
	public Calendar getBuckleTime(String taoCanCode)
	{
		Calendar buckleTime = null;
		try
		{
			// 根据套餐编码获取扣费时间
			buckleTime = buckleFeeBiz.buckleTime(taoCanCode);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取套餐编码获取扣费时间异常！taoCanCode:" + taoCanCode);
			return null;
		}
		return buckleTime;
	}
	
	/**
	 * 设置签约与套餐对应关系对象
	 * @description    
	 * @param lfContractInfo
	 * @param contractIdMap
	 * @param taoCanCode
	 * @param taocanType
	 * @param taocanMoney
	 * @param buckleTime
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-27 上午09:51:22
	 */
	public LfContractTaocan setContractTaocan(LfContract lfContractInfo, long contractIdMap, String taoCanCode, int taocanType, int taocanMoney, Calendar buckleTime, String taoCanName)
	{
		LfContractTaocan lfContractTaocan;
		try
		{
			lfContractTaocan = new LfContractTaocan();
			// 设置签约与套餐对应关系对象
			lfContractTaocan.setContractid(contractIdMap);
			lfContractTaocan.setTaocancode(taoCanCode);
			lfContractTaocan.setDebitaccount(lfContractInfo.getDebitaccount());
			lfContractTaocan.setContractdep(lfContractInfo.getContractdep());
			lfContractTaocan.setContractuser(lfContractInfo.getContractuser());
			lfContractTaocan.setTaocantype(taocanType);
			lfContractTaocan.setTaocanmoney(Long.valueOf(taocanMoney));
			lfContractTaocan.setDepid(lfContractInfo.getDepid());
			lfContractTaocan.setUserid(lfContractInfo.getUserid());
			lfContractTaocan.setCreatetime(new Timestamp(System.currentTimeMillis()));
			lfContractTaocan.setUpdatetime(new Timestamp(System.currentTimeMillis()));
			lfContractTaocan.setCorpcode(lfContractInfo.getCorpcode());
			lfContractTaocan.setTaocanname(taoCanName);
			if(buckleTime==null)
			{
				Calendar curTime = Calendar.getInstance();
				curTime.set(Calendar.HOUR_OF_DAY, 0);
				curTime.set(Calendar.MINUTE, 0);
				curTime.set(Calendar.SECOND, 0);
				curTime.set(Calendar.MILLISECOND, 0);
				lfContractTaocan.setBucklefeetime(new Timestamp(curTime.getTimeInMillis()));
			}
			else
			{
				lfContractTaocan.setBucklefeetime(new Timestamp(buckleTime.getTimeInMillis()));
			}
			
			
			lfContractTaocan.setGuid(lfContractInfo.getGuid());
			lfContractTaocan.setIsvalid("0");
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置签约与套餐对应关系对象异常！");
			return null;
		}
		return lfContractTaocan;
	}
	
	/**
	 * 设置计费信息
	 * @description    
	 * @param disp
	 * @param list
	 * @param lfContractInfo       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-27 下午03:46:29
	 */
	public void setBuckleFeeInfo(LfDeductionsDisp disp, LfDeductionsList list, LfContract lfContractInfo)
	{
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		disp.setAcctno(lfContractInfo.getAcctno());list.setAcctno(lfContractInfo.getAcctno());
		disp.setContractdep(lfContractInfo.getContractdep());list.setContractdep(lfContractInfo.getContractdep());
		disp.setContractid(lfContractInfo.getContractid());list.setContractid(lfContractInfo.getContractid());
		disp.setContractstate(lfContractInfo.getContractstate());list.setContractstate(lfContractInfo.getContractstate());
		disp.setCorpcode(lfContractInfo.getCorpcode());list.setCorpcode(lfContractInfo.getCorpcode());
		disp.setCustomname(lfContractInfo.getCustomname());list.setCustomname(lfContractInfo.getCustomname());
		disp.setDebitaccount(lfContractInfo.getDebitaccount());list.setDebitaccount(lfContractInfo.getDebitaccount());
		disp.setDeductionstype(0);list.setDeductionstype(0);//扣费
		disp.setMobile(lfContractInfo.getMobile());list.setMobile(lfContractInfo.getMobile());
		disp.setOprstate(0);
		disp.setOprtime(timestamp);
		disp.setContractuser(lfContractInfo.getContractuser()); list.setContractuser(lfContractInfo.getContractuser());
		disp.setUpdatetime(timestamp);list.setUdpatetime(timestamp);
		list.setBucklefeetime(timestamp);
		list.setBuckuptimer(0);
		list.setBuptimer(0);
		list.setContracttype(lfContractInfo.getContractsource());
		Calendar calendar = Calendar.getInstance();
		list.setImonth(calendar.get(calendar.MONTH)+1);
		list.setIyear(calendar.get(calendar.YEAR));
	}
	
	/**
	 * 更新机构套餐统计表
	 * @description    
	 * @param lfDepTaocanMap
	 * @param taoCanCode
	 * @param userIdStr
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-2 下午01:34:31
	 */
	public int saveDepTaocan(Map<Long, LfDepTaocan> lfDepTaocanMap, String taoCanCode, String userIdStr)
	{
		try
		{
			List<LfDepTaocan> lfDepTaocanList = new ArrayList<LfDepTaocan>();
			// 更新机构套餐统计表
			if(lfDepTaocanMap != null && lfDepTaocanMap.size() > 0)
			{
				if(userIdStr.length() > 0 && userIdStr.endsWith(","))
				{
					userIdStr = userIdStr.substring(0, userIdStr.length()-1) ;
					List<DynaBean> depTaocanList = moOrderHandleDAO.getDepTaocan(userIdStr, taoCanCode);
					//过滤已存在操作员ID记录
					if(depTaocanList != null && depTaocanList.size() > 0)
					{
						Long userId = -9999L;
						for(DynaBean lfDepTaocan : depTaocanList)
						{
							userId = lfDepTaocan.get("contract_user")==null||"".equals(lfDepTaocan.get("contract_user"))?-9999L:Long.valueOf(lfDepTaocan.get("contract_user").toString());
							if(lfDepTaocanMap.containsKey(userId))
							{
								lfDepTaocanMap.remove(userId);
							}
						}
					}
				}
				
				//获取机构套餐表操作员ID
				if(lfDepTaocanMap.size() > 0)
				{
					Entry<Long, LfDepTaocan> entry= null;
					Iterator<Entry<Long, LfDepTaocan>> it = lfDepTaocanMap.entrySet().iterator();
					while(it.hasNext())
					{
						entry = it.next();
						lfDepTaocanList.add(entry.getValue());
					}
					return empDao.save(lfDepTaocanList, LfDepTaocan.class);
				}
				else
				{
					return 0;
				}
			}
			else
			{
				return 0;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新机构套餐统计表异常！");
			return -1;
		}
	}

}
