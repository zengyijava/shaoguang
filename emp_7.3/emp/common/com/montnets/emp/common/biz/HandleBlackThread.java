package com.montnets.emp.common.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IDataAccessDriver;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.timer.TimerManagerBiz;
import com.montnets.emp.entity.blacklist.PbListBlack;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.security.blacklist.dao.BlackListAtomDAO;

public class HandleBlackThread extends Thread
{

	private IDataAccessDriver	dataAccessDriver	= new DataAccessDriver();

	private IEmpTransactionDAO	empTransDao			= dataAccessDriver.getEmpTransDAO();

	private IEmpDAO				empDao				= dataAccessDriver.getEmpDAO();

	private BlackListAtomDAO	blackListAtomDAO	= new BlackListAtomDAO();

	private long				threadSleepTime		= 5 * 60 * 1000L;

	public void run()
	{
		EmpExecutionContext.info("根据上行记录指令设置黑名单处理线程启动.");
		//线程启动后等待1分钟
		try
		{
			Thread.sleep(60 * 1000L);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据上行记录指令设置黑名单处理线程启动等待时出现异常.");
		}
		TimerManagerBiz timerManagerBiz = TimerManagerBiz.getTMInstance();
		//任务运行标识
		boolean runState = true;
		//返回信息
		int result = 0;
		while(true)
		{
			try
			{
				//获取任务运行标识
				runState = timerManagerBiz.checkTimeSerRun();
				//本机运行
				if(runState)
				{
					Long start = System.currentTimeMillis();
					EmpExecutionContext.info("根据上行记录指令设置黑名单处理开始");
					// 根据上行记录指令设置黑名单
					result = this.setBlackByMoTaskOrder();
					EmpExecutionContext.info("根据上行记录指令设置黑名单处理完成，耗时:" + (System.currentTimeMillis() - start)+"ms");
					//处理记录为0，线程等待
					if(result < 1)
					{
						Thread.sleep(threadSleepTime);
					}
				}
				//非本机运行，线程等待
				else
				{
					EmpExecutionContext.info("根据上行记录指令设置黑名单处理完成，运行标识为非本机");
					Thread.sleep(threadSleepTime);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "根据上行记录指令设置黑名单处理线程异常.");
			}
		}
	}

	/**
	 * 根据上行记录指令设置黑名单
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-1 下午03:36:35
	 */
	public Integer setBlackByMoTaskOrder()
	{
		try
		{
			// 查询系统配置数据
			Map<String, String> blaOrder = blackListAtomDAO.getGlobalValByKey(new String[] {"ADDBLABYMOORDERFLAG","ADDBLAMOMAXID","ADDBLAMOORDER","DELBLAMOORDER","BLACKCORPCODE"});
			if(blaOrder == null || blaOrder.size() < 1)
			{
				EmpExecutionContext.error("根据上行记录指令设置黑名单，查询系统配置数据失败.");
				return -1;
			}
			// 通过上行指令新增黑名单0:关1:开
			Integer addBlaByBoOrderBlag = Integer.parseInt(blaOrder.get("ADDBLABYMOORDERFLAG"));
			// 上行记录新增黑名单指令最大ID
			Integer moAddBlaMaxId = Integer.parseInt(blaOrder.get("ADDBLAMOMAXID"));
			// 新增黑名单指令
			String addOrder = blaOrder.get("ADDBLAMOORDER").trim();
			// 开上行指令维护黑名单企业
			String blackCorpCode = blaOrder.get("BLACKCORPCODE").trim();

			// 开启
			if(addBlaByBoOrderBlag == 1)
			{
				// 未设置指令
				if(addOrder == null || addOrder.trim().length() < 0)
				{
					EmpExecutionContext.info("根据上行记录指令设置黑名单，未设置增加黑名单上行指令，order：" + addOrder);
					return -1;
				}

				// 指令集合
				StringBuffer orders = new StringBuffer();
				// 单个指令
				if(addOrder.indexOf(",") < 0)
				{
					orders.append("'").append(addOrder).append("'");
				}
				// 多个指令
				else
				{
					String[] orderClump = addOrder.split(",");
					for (int i = 0; i < orderClump.length; i++)
					{
						orders.append("'").append(orderClump[i]).append("'").append(",");
					}
					if(orders.indexOf(",") > -1)
					{
						orders.deleteCharAt(orders.lastIndexOf(","));
					}
				}
				// 存在开通上行指令维护黑名单企业
				if(blackCorpCode == null || blackCorpCode.length() <= 5)
				{
					EmpExecutionContext.info("根据上行记录指令设置黑名单，未存在开通上行指令维护黑名单企业，blackCorpCode：" + blackCorpCode);
					return -1;
				}
				// 企业编码集合
				StringBuffer blackCorpCodeSb = new StringBuffer();
				// 设置企业编码
				// 单个企业
				if(blackCorpCode.indexOf(",") < 0)
				{
					blackCorpCodeSb.append("'").append(blackCorpCode).append("'");
				}
				// 多个企业
				else
				{
					String[] blackCorpCodeClump = blackCorpCode.split(",");
					for (int i = 0; i < blackCorpCodeClump.length; i++)
					{
						blackCorpCodeSb.append("'").append(blackCorpCodeClump[i]).append("'").append(",");
					}
					if(blackCorpCodeSb.indexOf(",") > -1)
					{
						blackCorpCodeSb.deleteCharAt(blackCorpCodeSb.lastIndexOf(","));
					}
				}

				// 根据最大ID和指令查询上行记录
				List<DynaBean> moTaskList = blackListAtomDAO.getMoTaskByMaxId(moAddBlaMaxId, orders.toString(), blackCorpCodeSb.toString());

				if(moTaskList != null && moTaskList.size() > 0)
				{
					int count = moTaskList.size();
					// 保存黑名单
					this.saveBlack(moTaskList, addOrder);
					moTaskList.clear();
					moTaskList = null;
					return count;
				}
				else
				{
					// 无上行黑名单设置数据,线程等待5分钟后继续处理
					EmpExecutionContext.info("根据上行记录指令设置黑名单，无上行黑名单设置数据，order：" + addOrder + "，moAddBlaMaxId:" + moAddBlaMaxId + "，blackCorpCode:"+ blackCorpCode);
					return -1;
				}
			}
			else
			{
				EmpExecutionContext.info("根据上行记录指令设置黑名单功能未开启，addBlaByBoOrderBlag：" + addBlaByBoOrderBlag);
				return -1;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据上行记录指令新增黑名单异常！");
			return -1;
		}
	}

	/**
	 * 根据上行记录指令设置黑名单
	 * 
	 * @description
	 * @param moTaskList
	 *        上行记录
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-2-2 上午11:59:07
	 */
	public void saveBlack(List<DynaBean> moTaskList, String addOrder)
	{
		Connection conn = null;
		// 新增总数
		int count = 0;
		try
		{
			// 手机号
			String moPhone;
			// 企业编码
			String corpCode;
			// 上行内容
			String msgContent;
			// 以企业设置黑名单集合,KEY为企业编码,value为多个手机号
			Map<String, Set<String>> blackMap = new HashMap<String, Set<String>>();
			Set<String> balckList;
			// 大写的删除黑名单指令
			String addOrderUpper = "";
			if(addOrder != null && addOrder.length() > 0)
			{
				addOrderUpper = "," + addOrder + ",";
				addOrderUpper = addOrderUpper.toUpperCase();
			}
			long start = 0L;
			// 设置删除号码
			for (DynaBean dynaBean : moTaskList)
			{
				moPhone = dynaBean.get("phone").toString().trim();
				corpCode = dynaBean.get("corp_code").toString().trim();
				msgContent = dynaBean.get("msgcontent").toString().trim().toUpperCase();
				if(addOrderUpper.indexOf("," + msgContent + ",") < 0)
				{
					EmpExecutionContext.error("根据上行记录指令设置黑名单，上行记录回复指令错误！moPhone：" + moPhone + "，corpCode：" + corpCode + "，msgContent:" + msgContent);
					continue;
				}
				// 集合中存在企业
				if(blackMap.containsKey(corpCode))
				{
					// 号码是同一企业集合中已存在
					if(!blackMap.get(corpCode).add(moPhone))
					{
						EmpExecutionContext.info("根据上行记录指令设置黑名单，本次任务已新增过此号码！moPhone：" + moPhone + "，corpCode：" + corpCode);
						continue;
					}
				}
				// 集合中不存在企业,新增号码
				else
				{
					balckList = new HashSet<String>();
					balckList.add(moPhone);
					blackMap.put(corpCode, balckList);
				}
			}
			// 设置黑名单集合为空
			if(blackMap == null || blackMap.size() < 1)
			{
				EmpExecutionContext.error("根据上行记录指令设置黑名单，设置黑名单集合为空！");
				return;
			}
			conn = empTransDao.getConnection();
			empTransDao.beginTransaction(conn);
			// 删除返回标识
			boolean delResult;
			// 批量删除号码
			StringBuffer phones = new StringBuffer();
			// 新增的黑名单对象集合
			List<PbListBlack> listBlackList = new ArrayList<PbListBlack>();
			// 新增的黑名单对象
			PbListBlack listBlack = null;
			// 号码集合
			Set<String> phoneList = null;
			// 更新数据时间
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			int saveCount = 0;
			// 根据企业先删除所要增加的黑名单
			for (String keyCorpCode : blackMap.keySet())
			{
				//初始化
				phones.setLength(0);
				listBlackList.clear();
				// 通过企业编码从企业黑名单集合
				phoneList = blackMap.get(keyCorpCode);
				if(phoneList == null || phoneList.size() < 1)
				{
					EmpExecutionContext.error("根据上行记录指令设置黑名单，通过企业编码从企业黑名单集合中获取不到数据！corpCode：" + keyCorpCode);
					continue;
				}
				// 设置号码条件
				for (String phoneStr : phoneList)
				{
					phones.append("'").append(phoneStr).append("'").append(",");
					// 设置更新黑名单对象
					listBlack = new PbListBlack();
					listBlack.setUserId("000000");
					listBlack.setSpgate(" ");
					listBlack.setSpnumber(" ");
					listBlack.setPhone(Long.parseLong(phoneStr));
					listBlack.setOptype(1);
					listBlack.setBlType(1);
					listBlack.setOptTime(timestamp);
					listBlack.setCorpCode(keyCorpCode);
					listBlack.setSvrType(" ");
					listBlackList.add(listBlack);
				}
				if(phones.length() < 1 || listBlackList.size() < 1)
				{
					EmpExecutionContext.error("根据上行记录指令设置黑名单，根据黑名单列表生成查询条件和更新对象失败！corpCode：" + keyCorpCode);
					continue;
				}
				// 去掉最后个逗号
				phones.deleteCharAt(phones.lastIndexOf(","));
				start = System.currentTimeMillis();
				// 先删除已存在的黑名单
				delResult = blackListAtomDAO.deleteBlackList(conn, keyCorpCode, phones.toString());
				// 删除失败,回滚事务,记录日录
				if(!delResult)
				{
					EmpExecutionContext.error("根据上行内容设置黑名单,删除黑名单失败,corpCode:" + keyCorpCode + ",phones:" + phones);
					empTransDao.rollBackTransaction(conn);
					continue;
				}
				else
				{
					EmpExecutionContext.info("根据上行内容设置黑名单,删除黑名单成功！成功数:" + listBlackList.size() + "，corpCode:" + keyCorpCode
							+"，耗时:"+(System.currentTimeMillis() - start)+"ms");
				}
				start = System.currentTimeMillis();
				// 保存短信黑名单记录
				saveCount = empTransDao.save(conn, listBlackList, PbListBlack.class);
				// 成功
				if(saveCount > 0)
				{
					EmpExecutionContext.info("根据上行内容设置黑名单,新增黑名单成功！成功数:" + listBlackList.size()+"，耗时:"+(System.currentTimeMillis() - start)
							+"ms，corpCode:" + keyCorpCode+"，phones:"+phones);
					// 提交
					empTransDao.commitTransaction(conn);
					// 记录成功总数
					count += saveCount;
				}
				else
				{
					EmpExecutionContext.error("根据上行记录指令新增黑名单失败！moPhone：" + phones + "，corpCode：" + keyCorpCode+"，phones:"+phones);
					// 回滚
					empTransDao.rollBackTransaction(conn);
					continue;
				}
			}
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("globalKey", "ADDBLAMOMAXID");
			// 更新最大ID
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			// 数据最大ID
			String maxId = moTaskList.get(moTaskList.size() - 1).get("id").toString();
			objectMap.put("globalValue", maxId);
			boolean resutlUpdate = empDao.update(LfGlobalVariable.class, objectMap, conditionMap);
			if(resutlUpdate)
			{
				EmpExecutionContext.info("根据上行记录指令设置黑名单，更新上行记录最大ID成功！maxId:" + maxId);
			}
			else
			{
				EmpExecutionContext.info("根据上行记录指令设置黑名单，更新上行记录最大ID失败！maxId:" + maxId);
			}
			blackMap.clear();
			blackMap = null;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据上行记录指令设置黑名单异常.");
			empTransDao.rollBackTransaction(conn);
		}
		finally
		{
			if(conn != null)
			{
				empTransDao.closeConnection(conn);
			}
			EmpExecutionContext.info("根据上行记录指令维护黑名单完成，查询记录总数：" + moTaskList.size() + "，共新增：" + count);
		}
	}

}
