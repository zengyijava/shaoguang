package com.montnets.emp.common.biz;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONObject;

import com.montnets.emp.common.bean.BalanceAlarmBean;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.LfSpFee;

public class EmpMonAlarmThread extends Thread
{
	EmpMonUtil			empMonUtil		= new EmpMonUtil();

	SimpleDateFormat	sdf_fileCont	= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	BalanceLogBiz balanceLogBiz = new BalanceLogBiz();
	
	//数据库连接状态 0-正常；1-异常
	private static Integer dbConnectState = 0;
	
	//数据库连接检测异常次数
	private static Integer exCount = 0;

	public EmpMonAlarmThread()
	{
		this.setName("EMP监控告警处理线程");
	}

	public void run()
	{
		EmpExecutionContext.info(this.getName() + "启动。");
		while(StaticValue.isEmpThreadrunState())
		{
			try
			{
				Thread.sleep(60 * 1000L);
				EmpExecutionContext.info(this.getName()+"开始。");
				Long start = System.currentTimeMillis();
				//查询运营商余额告警
				this.queryBalanceAlarm();
				//数据库连接告警
				this.dbConnectExAlarm();
				EmpExecutionContext.info(this.getName()+"结束，耗时:"+(System.currentTimeMillis() - start)+"ms");
			}
			catch (InterruptedException e)
			{
				Thread.currentThread().interrupt();
				EmpExecutionContext.error(e, this.getName()+"，线程等待异常。");
			}
		}

	}
	
	/**
	 * 数据库连接告警
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午01:21:42
	 */
	public void dbConnectExAlarm()
	{
		Long start = System.currentTimeMillis();
		Integer state = empMonUtil.getDbConnectState();
		//数据库连接异常
		if(state - 1 == 0)
		{
			//连接失败超过5次，进行告警
			if(exCount >= 5)
			{
				//设置数据库连接状态
				dbConnectState = state;
				//写告警报文
				this.writeDbConnectMonFile("6000", "10", "DBERR001", "数据库连接出现异常");
				EmpExecutionContext.error(this.getName()+"，检测数据库连接异常，生成告警监控报文，state:"+state);
			}
			else
			{
				exCount++;
				EmpExecutionContext.error(this.getName()+"，检测数据库连接异常，出现异常次数"+exCount+"，生成告警监控报文，state:"+state);
			}
		}
		else
		{
			//原来为异常状态
			if(dbConnectState - 1 == 0)
			{
				//写数据库恢复报文
				this.writeDbConnectMonFile("6000", "19", "ok", "数据库连接恢复正常");
				EmpExecutionContext.info(this.getName()+"，检测数据库连接正常，生成恢复正常监控报文，state:"+state+"，原数据库连接状态:"+dbConnectState);
			}
			else
			{
				EmpExecutionContext.info(this.getName()+"，检测数据库连接正常，state:"+state+"，原数据库连接状态:"+dbConnectState+"，耗时:"+(System.currentTimeMillis() - start)+"ms");
			}
			dbConnectState = 0;
			exCount = 0;
		}
			
	}
	
	/**
	 * 查询余额告警
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-11 下午06:55:35
	 */
	public void queryBalanceAlarm()
	{
		try
		{
			long start = System.currentTimeMillis();
			//失败账号队列为空
			if(StaticValue.getBalanceFailAccount().size() < 1)
			{
				EmpExecutionContext.info(this.getName()+"，查询运营商余额失败账号队列为空，无需生成监控报文。");
				return;
			}
			Map<String, BalanceAlarmBean> failAccountMap =  new ConcurrentHashMap<String, BalanceAlarmBean>();
			failAccountMap.putAll(StaticValue.getBalanceFailAccount());
			//计数
			int count = 0;
			//账号
			StringBuffer accounts = new StringBuffer();
			for(BalanceAlarmBean balanceAlarmBean:failAccountMap.values())
			{
				//如果状态为成功
				if("ok".equals(balanceAlarmBean.getStateCode()))
				{
					StaticValue.getBalanceFailAccount().get(balanceAlarmBean.getSpUser()).setStateCode("ok");
					StaticValue.getBalanceFailAccount().get(balanceAlarmBean.getSpUser()).setErrInfo("账号["+balanceAlarmBean.getSpUser()+"]查询运营商余额成功，状态恢复正常。");
					//写监控报文 19：恢复正常
					this.writeBalanceMonFile("6000", "19", balanceAlarmBean.getSpUser());
					//队列中移除
					StaticValue.getBalanceFailAccount().remove(balanceAlarmBean.getSpUser());
					EmpExecutionContext.info(this.getName()+"，查询运营商账号["+balanceAlarmBean.getSpUser()+"]检测正常，从失败账号队列中移除。");
					continue;
				}
				//获取账号信息
				List<DynaBean> dynaBeanList = empMonUtil.getUserDataInfo(balanceAlarmBean.getSpUser());
				if(dynaBeanList != null && dynaBeanList.size() > 0)
				{
					//账号状态
					String status = dynaBeanList.get(0).get("status")==null?"":dynaBeanList.get(0).get("status").toString();
					//状态为禁用
					if("1".equals(status))
					{
						StaticValue.getBalanceFailAccount().get(balanceAlarmBean.getSpUser()).setStateCode("ok");
						StaticValue.getBalanceFailAccount().get(balanceAlarmBean.getSpUser()).setErrInfo("账号["+balanceAlarmBean.getSpUser()+"]状态为禁用，不再检测运营商余额查询状态。");
						//写监控报文 19：恢复正常
						this.writeBalanceMonFile("6000", "19", balanceAlarmBean.getSpUser());
						//队列中移除
						StaticValue.getBalanceFailAccount().remove(balanceAlarmBean.getSpUser());
						EmpExecutionContext.info(this.getName()+"，账号["+balanceAlarmBean.getSpUser()+"]状态为禁用，不再进行运营商余额查询，从失败账号队列中移除。");
						continue;
					}
				}
				
				accounts.append("'").append(balanceAlarmBean.getSpUser()).append("',");
				count++;
				//批量处理
				if(count == 100)
				{
					//去掉最后一个逗号
					accounts = accounts.deleteCharAt(accounts.lastIndexOf(","));
					//处理查询运营商失败账号列表
					this.handleAccount(accounts.toString());
					//重新计数
					count = 0;
					//清空账号
					accounts.setLength(0);
				}
			}
			//处理剩余的
			if(accounts.length() > 0)
			{
				accounts = accounts.deleteCharAt(accounts.lastIndexOf(","));
				//处理查询运营商失败账号列表
				this.handleAccount(accounts.toString());
			}
			EmpExecutionContext.info(this.getName()+"，处理查询运营商余额失败账号完成，共处理"+failAccountMap.size()+"条记录，耗时:"+(System.currentTimeMillis() - start)+"ms");
			failAccountMap.clear();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, this.getName()+"，检测处理查询运营商余额失败账号队列异常。");
		}
		
	}
	
	/**
	 * 处理失败账号列表
	 * @description    
	 * @param accounts       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午12:20:06
	 */
	public void handleAccount(String accounts)
	{
		//根据账号获取账号对象
		List<LfSpFee> spFeeList = empMonUtil.getFailSpUserList(accounts.toString());
		if(spFeeList != null && spFeeList.size() > 0)
		{
			//循环检测账号
			for(LfSpFee spFee : spFeeList)
			{
				//检测账号状态
				balanceLogBiz.queryBalancd(spFee);
				//如果查询成功，重新设置余额
				if("ok".equals(spFee.getSpResult()))
				{
					StaticValue.getBalanceFailAccount().get(spFee.getSpUser()).setStateCode("ok");
					StaticValue.getBalanceFailAccount().get(spFee.getSpUser()).setErrInfo("账号["+spFee.getSpUser()+"]查询运营商余额成功，状态恢复正常。");
					//写监控报文 19：恢复正常
					this.writeBalanceMonFile("6000", "19", spFee.getSpUser());
					//更新运营商余额信息
					if(empMonUtil.updateSpFee(spFee))
					{
						//队列中移除
						StaticValue.getBalanceFailAccount().remove(spFee.getSpUser());
						EmpExecutionContext.info(this.getName()+"，查询运营商账号["+spFee.getSpUser()+"]检测正常，从失败账号队列中移除。");
					}
				}
				else
				{
					//写监控报文 10：告警
					this.writeBalanceMonFile("6000", "10", spFee.getSpUser());
				}
			}
		}
		else
		{
			EmpExecutionContext.error(this.getName()+"，根据账号获取运营商余额表记录为空，accounts:"+accounts);
		}
	}
	
	/**
	 * 写查询余额失败账号监控报文
	 * @description    
	 * @param evtId
	 * @param type
	 * @param spUser
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午12:27:54
	 */
	public void writeBalanceMonFile(String evtId, String type, String spUser)
	{
		try
		{
			// 报文时间
			String evtTm = sdf_fileCont.format(System.currentTimeMillis());
			//状态码
			String stateCode = StaticValue.getBalanceFailAccount().get(spUser).getStateCode();
			//描述信息
			String errInfo = StaticValue.getBalanceFailAccount().get(spUser).getErrInfo();
			//设置6000报文内容
			JSONObject json = empMonUtil.setAlarmInfo("10", "EMP", stateCode, errInfo);
			if(json != null)
			{
				//设置报文信息
				String message = empMonUtil.setMessage(json, evtTm, evtId, type);
				if(message != null)
				{
					//写入文件
					EmpExecutionContext.monAlarmLog(message);
				}
				else
				{
					EmpExecutionContext.error("生成查询运营商余额失败账号告警信息监控报文信息失败，message为null，spUser:"+spUser);
				}
			}
			else
			{
				EmpExecutionContext.error("生成查询运营商余额失败账号告警信息监控报文信息失败，json为null，spUser:"+spUser);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生成查询运营商余额失败账号告警信息监控报文信息失败，spUser:"+spUser);
		}
	}
	
	/**
	 * 写数据库连接监控报文
	 * @description    
	 * @param evtId
	 * @param type       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午02:41:37
	 */
	public void writeDbConnectMonFile(String evtId, String type, String stateCode, String errInfo)
	{
		try
		{
			// 报文时间
			String evtTm = sdf_fileCont.format(System.currentTimeMillis());
			//设置6000报文内容
			JSONObject json = empMonUtil.setAlarmInfo("11", "EMP", stateCode, errInfo);
			if(json != null)
			{
				//设置报文信息
				String message = empMonUtil.setMessage(json, evtTm, evtId, type);
				if(message != null)
				{
					//写入文件
					EmpExecutionContext.monAlarmLog(message);
				}
				else
				{
					EmpExecutionContext.error("生成数据库连接告警信息监控报文信息失败，message为null");
				}
			}
			else
			{
				EmpExecutionContext.error("生成数据库连接告警信息监控报文信息失败，json为null");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "生成数据库连接告警信息监控报文信息失败");
		}
	}
}
