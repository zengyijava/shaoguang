package com.montnets.emp.common.biz;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import com.montnets.emp.common.constant.*;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.bean.BalanceAlarmBean;
import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.common.timer.AlarmSMSTask;
import com.montnets.emp.entity.gateway.LfSpFee;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasgroup.Userfee;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDepUserBalance;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.gateway.TableAgwAccount;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.webservice.balance.RMSBalanceWebservice;

public class BalanceLogBiz  extends SuperBiz{
	
	private static final String QUERY_MBOSS_RMS_TYPE = "11";
	private static final String DEFAULT_QUERY_BALANCE_TYPE = "0";
	protected SuperOpLog spLog = new SuperOpLog();
	protected String opModule=StaticValue.SMS_BOX;
	protected String opType=StaticValue.OTHER;
	//阀值提醒任务短信发送线程池对象
	private static ExecutorService exec = null; 
	
	//超过多少时间则需更新运营商余额
	protected static long updateTimeInterval = (long)12 * 60 * 60 * 1000;
		
	/**
	 * 短信发送扣费，回收接口（根据userid）
	 * @param connection
	 * @param userId
	 * @param sendCount
	 * @return  返回值信息 			
	 * 		    1:短信回收成功
	 * 			0:短信扣费成功
	 * 		   -1:短信扣费失败
	 * 		   -2:短信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:短信发送或回收条数不能为空
	 * 		   -9:短信回收失败
	 * 		-9999:短信发送扣费回收接口调用异常
	 */	
	public int sendSmsAmountByUserId(Connection connection,Long userId,int sendCount){
		try {
			return sendSmsAmount(connection, new BaseBiz().getById(LfSysuser.class, userId), sendCount);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短信发送扣费回收接口调用异常！userId:"+userId);
			return StaticValue.EXP_RETURN;
		}
		
	}
	
	/**
	 * 短信发送扣费,回收接口(根据guid)
	 * @return  返回值信息 			
	 * 		    1:短信回收成功
	 * 			0:短信扣费成功
	 * 		   -1:短信扣费失败
	 * 		   -2:短信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:短信发送或回收条数不能为空
	 * 		   -9:短信回收失败
	 * 		-9999:短信发送扣费回收接口调用异常
	 */
	public int sendSmsAmountByGuid(Connection connection,Long guid,int sendCount){
		
		try {
			return sendSmsAmount(connection, new BaseBiz().getByGuId(LfSysuser.class, guid), sendCount);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短信发送扣费回收接口调用异常！guid:"+guid);
			return StaticValue.EXP_RETURN;
		}
		
			
	}
	
	/**
	 * 注销操作员时对彩信，短信进行回收费用时的方法
	 * @param connection
	 * @param lfSysuser
	 * @param smsCount
	 * @param mmsCount
	 * @return
	 */
	public boolean changeSmsAndMmsAmount(Connection connection,LfSysuser lfSysuser,Long smsCount,Long mmsCount)
	{
		boolean flag = false;
		try {
			Long depId = lfSysuser.getDepId();

			LfDep lfDep = new DepBiz().getDepById(depId);
			//根据当前机构获取当前机构的级别
			Integer level = new DepDAO().getUserDepLevel(depId);
			if(level > SystemGlobals.getMaxChargeLevel(lfSysuser.getCorpCode())){
				return false;
			}
			
			BaseBiz baseBiz = new BaseBiz() ;

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("targetId", lfDep.getDepId()+"");
			
			List<LfDepUserBalance> lfDepUserBalances = baseBiz.getByCondition(LfDepUserBalance.class, conditionMap, null);
			LfDepUserBalance lfDepUserBalance = null;
			if(lfDepUserBalances != null && lfDepUserBalances.size()>0){
				lfDepUserBalance = lfDepUserBalances.get(0);
				String opContent = "";
                if(smsCount < 0 ){
					lfDepUserBalance.setSmsBalance(lfDepUserBalance.getSmsBalance()-smsCount);
					lfDepUserBalance.setSmsCount(lfDepUserBalance.getSmsCount()+smsCount);
					opContent = "短信回收成功，总回收："+(smsCount*-1)+";";
                }
                if(mmsCount <0)
                {
                	lfDepUserBalance.setMmsBalance(lfDepUserBalance.getMmsBalance()-mmsCount);
                	lfDepUserBalance.setMmsCount(lfDepUserBalance.getMmsCount()+mmsCount);
                	opContent += "彩信回收成功，总回收："+(mmsCount*-1);
                }
				if(connection == null){
					flag = empDao.update(lfDepUserBalance);
				}else{
					flag = empTransDao.update(connection, lfDepUserBalance);
				}
				
				if (flag) {
					//回收成功写一下日志文件				
					spLog.logSuccessString(lfSysuser.getUserName(), opModule, opType, opContent,lfSysuser.getCorpCode());
					//add end 
				}
				return flag;
				
			}else{
				return flag;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"注销操作员（操作员ID"+lfSysuser.getUserId().toString()+"）时，回收费用异常！");
			return false;
		} 
	}
	

	/**
	 * 短信发送扣费回收接口
	 * @return  返回值信息 			
	 * 		    1:短信回收成功
	 * 			0:短信扣费成功
	 * 		   -1:短信扣费失败
	 * 		   -2:短信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:短信发送或回收条数不能为空
	 *  	   -8:短信回收已发送条数异常
	 * 		   -9:短信回收失败
	 * 		-9999:短信发送扣费回收接口调用异常
	 */
	private int sendSmsAmount(Connection connection,LfSysuser lfSysuser,int sendCount)
	{
		//默认返回失败
		int result = -1;
		//操作标识
		String oprInfo;
		if(sendCount > 0)
		{
			//扣费
			oprInfo = "短信发送扣费";
		}
		else
		{
			//回收
			oprInfo = "短信发送回收";
		}
		//传入lfSysuser对象为空,返回失败
		if(lfSysuser == null)
		{
			EmpExecutionContext.error(oprInfo + "接口获取参数异常，lfSysuser为null");
			return result; 
		}
  		long depId = lfSysuser.getDepId();
		CallableStatement cs = null;
		//声明一个连接(存储过程不支持事务，不使用传进来的连接，防止上层事务未结束，无法再操作该存储过程)
		Connection conn = null;
		try {
			//传入连接为空,开启新的连接
			conn = empTransDao.getConnection();
			//调用存储过程
			try {
				cs = conn.prepareCall("{call LF_FEEDEDTV0(?,?,?)}");
				cs.setLong(1, depId);
				cs.setLong(2, sendCount);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);
				cs.execute();
				//返回值
				result = cs.getInt(3);
			} catch (SQLException e) {
				EmpExecutionContext.error(e, oprInfo + "失败,短信发送扣费回收接口调用异常!depId:"+depId+"，sendCount:"+sendCount
						+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
				if(sendCount > 0)
				{
					//短信扣费失败
					result = -1;
				}
				else
				{
					//短信回收失败
					result = -9;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "短信发送扣费回收接口调用异常!depId:"+depId+"，sendCount:"+sendCount
					+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			result = -9999;
		}finally{		
			if (cs != null)
			{
				try {
					cs.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e, "短信发送扣费回收接口关闭存储过程异常!depId:"+depId+"，sendCount:"+sendCount
							+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
				}
			}
			try
			{
				//写操作日志
				String opContent;
				//操作状态
				String oprInfoStatu = "成功，共扣除费用：";
				if(result >= 0)
				{
					if(sendCount < 0)
					{
						sendCount = sendCount*-1;
						oprInfoStatu = "成功，共回收费用：";
					}
					opContent = oprInfo + oprInfoStatu + sendCount;
					spLog.logSuccessString(lfSysuser.getUserName(), oprInfo, opType, opContent,lfSysuser.getCorpCode());
				}
				else
				{
					EmpExecutionContext.error("短信发送扣费回收接口调用异常,错误码:" + result + "，depId:"+depId+"，sendCount:"+sendCount);
					opContent ="短信发送扣费回收接口调用异常,错误码:" + result;
					spLog.logFailureString(lfSysuser.getUserName(), oprInfo, opType, opContent, null, lfSysuser.getCorpCode());
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "短信发送扣费回收接口调用写操作日志失败，depId:"+depId+"，sendCount:"+sendCount
					+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}
			try
			{
				//关闭连接
				if(conn != null)
				{
					empTransDao.closeConnection(conn);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "短信发送扣费回收接口关闭数据库连接失败，depId:"+depId+"，sendCount:"+sendCount
						+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}
			try
			{
				//扣费时
				if(sendCount>0){
					getExecutorService().execute(new AlarmSMSTask(lfSysuser,1));
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "短信发送扣费回收接口启动告警阀值短信线程异常!depId:"+depId+"，sendCount:"+sendCount
						+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}
		}
		return result;
		
/*		调用存储过程,此方法暂时保留
		if(sendCount == 0){
			//短信发送或回收条数不能为空
			return -5;
		}
		LfDep lfDep = null;
		try {
			Long depId = lfSysuser.getDepId();
			lfDep = new DepBiz().getDepById(depId);
			BaseBiz baseBiz = new BaseBiz() ;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("targetId", lfDep.getDepId()+"");
			List<LfDepUserBalance> lfDepUserBalances = baseBiz.getByCondition(LfDepUserBalance.class, conditionMap, null);
			LfDepUserBalance lfDepUserBalance = null;
			if(lfDepUserBalances != null && lfDepUserBalances.size()>0){
				lfDepUserBalance = lfDepUserBalances.get(0);
				if(sendCount > 0){
					if(sendCount <= lfDepUserBalance.getSmsBalance()){
						lfDepUserBalance.setSmsBalance(lfDepUserBalance.getSmsBalance()-sendCount);
						lfDepUserBalance.setSmsCount(lfDepUserBalance.getSmsCount()+sendCount);
						if(connection == null){
							empDao.update(lfDepUserBalance);
						}else{
							empTransDao.update(connection, lfDepUserBalance);
						}
						//扣费成功写一下日志文件
						String opContent ="短信扣费成功，总扣费："+sendCount;
						spLog.logSuccessString(lfSysuser.getUserName(), opModule, opType, opContent,lfSysuser.getCorpCode());
						//add end 
						//短信扣费成功
						return 0;
					}else{
						//短信余额不足
						return -2;
					}
				}else if(sendCount < 0){
					lfDepUserBalance.setSmsBalance(lfDepUserBalance.getSmsBalance()-sendCount);
					lfDepUserBalance.setSmsCount(lfDepUserBalance.getSmsCount()+sendCount);
					if(connection == null){
						empDao.update(lfDepUserBalance);
					}else{
						empTransDao.update(connection, lfDepUserBalance);
					}
					//回收成功写一下日志文件					
					String opContent ="短信回收成功，总回收："+(sendCount*-1);
					spLog.logSuccessString(lfSysuser.getUserName(), opModule, opType, opContent,lfSysuser.getCorpCode());
					//add end 
					//短信回收成功
					return 1;
				}
			}else{
				//用户所属机构没有充值
				return -4;
			}
		} catch (Exception e) {
			if(sendCount > 0){
				//短信扣费失败
				EmpExecutionContext.error("短信扣费失败,短信发送扣费回收接口调用异常!");
				return -1;
			}else{
				//短信回收失败
				EmpExecutionContext.error("短信回收失败,短信发送扣费回收接口调用异常!");
				return -9;
			}
		}
		return -9999;*/
	}
	
	/**
	 * 短信发送SP账号扣费回收接口
	 * @param spUser  SP账号  
	 * @param sendCount 发送数量
	 * @param lfSysuser 操作员对象
	 * @return  返回值信息 			
	 * 		    1:SP账号回收成功
	 * 			0:SP账号扣费成功
	 * 		   -1:SP账号扣费失败
	 * 		   -2:SP账号余额不足
	 *  	   -5:发送或回收条数不能为空
	 *  	   -8:回收已发送条数异常
	 * 		   -9:回收失败
	 * 		-9999:发送SP账号扣费回收接口调用异常
	 */
	public int sendSmsSpUserAmount(String spUser, int sendCount, LfSysuser lfSysuser)
	{
		//默认返回失败
		int result = -1;
		//操作标识
		String oprInfo;
		if(sendCount > 0)
		{
			//扣费
			oprInfo = "SP账号费用扣除";
		}
		else
		{
			//回收
			oprInfo = "SP账号费用回收";
		}
		
		CallableStatement cs = null;
		Connection connection = null;
		try {
			connection = empTransDao.getConnection();
			//调用存储过程
			try {
				cs = connection.prepareCall("{call LF_SPFEEDEDT(?,?,?)}");
				cs.setString(1, spUser);
				cs.setLong(2, sendCount);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);
				cs.execute();
				//返回值
				result = cs.getInt(3);
			} catch (SQLException e) {
				EmpExecutionContext.error(e, oprInfo + "失败,SP账号扣费回收接口调用异常!spUser:"+spUser+"，sendCount:"+sendCount
										+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
				if(sendCount > 0)
				{
					//扣费失败
					result = -1;
				}
				else
				{
					//回收失败
					result = -9;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "SP账号扣费回收接口调用异常!spUser:"+spUser+"，sendCount:"+sendCount
								+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			result = -9999;
		}finally{		
			if (cs != null)
			{
				try {
					cs.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e, "SP账号扣费回收接口关闭存储过程异常!spUser:"+spUser+"，sendCount:"+sendCount
											+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
				}
			}
			try
			{
				//写操作日志
				String opContent;
				//操作状态
				String oprInfoStatu = "成功，共扣除费用：";
				if(result >= 0)
				{
					if(sendCount < 0)
					{
						sendCount = sendCount*-1;
						oprInfoStatu = "成功，共回收费用：";
					}
					opContent = oprInfo + oprInfoStatu + sendCount;
					spLog.logSuccessString(lfSysuser.getUserName(), oprInfo, opType, opContent,lfSysuser.getCorpCode());
				}
				else
				{
					EmpExecutionContext.error("SP账号扣费回收接口调用异常,错误码:" + result + "，spUser:"+spUser+"，sendCount:"+sendCount
											+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
					opContent ="SP账号扣费回收接口调用异常,错误码:" + result;
					spLog.logFailureString(lfSysuser.getUserName(), oprInfo, opType, opContent, null, lfSysuser.getCorpCode());
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "SP账号扣费回收接口调用写操作日志失败，spUser:"+spUser+"，sendCount:"+sendCount
					+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}
			try
			{
				empTransDao.closeConnection(connection);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "SP账号扣费回收接口关闭数据库连接异常!spUser:"+spUser+"，sendCount:"+sendCount
						+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}
		/*	try
			{
				//扣费时
				if(sendCount>0){
					getExecutorService().execute(new AlarmSMSTask(lfSysuser,1));
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "SP账号扣费回收接口启动告警阀值短信线程异常!spUser:"+spUser+"，sendCount:"+sendCount
						+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}*/
		}
		return result;
	}
	
	/**
	 * 彩信发送扣费，回收接口(根据userid)
	 * @param connection
	 * @param userId
	 * @param sendCount
	 * @return  返回值信息 			
	 * 		    1:彩信回收成功
	 * 			0:彩信扣费成功
	 * 		   -1:彩信扣费失败
	 * 		   -2:彩信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:彩信发送或回收条数不能为空
	 *  	   -8:彩信回收已发送条数异常
	 * 		   -9:彩信回收失败
	 * 		-9999:彩信发送扣费回收接口调用异常
	 */
	public int sendMmsAmountByUserId(Connection connection,Long userId,Long sendCount){
		try {
			return sendMmsAmount(connection, new BaseBiz().getById(LfSysuser.class, userId), sendCount);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "彩信发送扣费回收接口调用异常！userId:"+userId);
			return StaticValue.EXP_RETURN;
		}
	}
	
	/**
	 * @return  返回值信息 			
	 * 		    1:彩信回收成功
	 * 			0:彩信扣费成功
	 * 		   -1:彩信扣费失败
	 * 		   -2:彩信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:彩信发送或回收条数不能为空
	 *  	   -8:彩信回收已发送条数异常
	 * 		   -9:彩信回收失败
	 * 		-9999:彩信发送扣费回收接口调用异常
	 */
	public int sendMmsAmountByGuid(Connection connection,Long guid,Long sendCount){
		
		try {
			return sendMmsAmount(connection, new BaseBiz().getByGuId(LfSysuser.class, guid), sendCount);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "彩信发送扣费回收接口调用异常！guid:"+guid);
			return StaticValue.EXP_RETURN;
		}
	}

	/**
	 * 彩信发送扣费，回收接口
	 * @return  返回值信息 			
	 * 		    1:彩信回收成功
	 * 			0:彩信扣费成功
	 * 		   -1:彩信扣费失败
	 * 		   -2:彩信余额不足
	 *  	   -4:用户所属机构没有充值
	 *  	   -5:彩信发送或回收条数不能为空
	 *  	   -8:彩信回收已发送条数异常
	 * 		   -9:彩信回收失败
	 * 		-9999:彩信发送扣费回收接口调用异常
	 */
	public int sendMmsAmount(Connection connection,LfSysuser lfSysuser,Long sendCount)
	{
		//默认返回失败
		int result = -1;
		//操作标识
		String oprInfo;
		if(sendCount > 0)
		{
			//扣费
			oprInfo = "彩信发送扣费";
		}
		else
		{
			//回收
			oprInfo = "彩信发送回收";
		}
		//传入lfSysuser对象为空,返回失败
		if(lfSysuser == null)
		{
			EmpExecutionContext.error(oprInfo + "接口获取参数异常，lfSysuser:" + lfSysuser);
			return result; 
		}
		long depId = lfSysuser.getDepId();
		CallableStatement cs = null;
		//连接标识 0:有传入连接,1:无传入连接
		int connFlag = 0;
		try {
			//传入连接为空,开启新的连接
			if(connection == null)
			{
				connection = empTransDao.getConnection();
				//设置标识为无传入连接
				connFlag = 1;
			}
			//调用存储过程
			try {
				cs = connection.prepareCall("{call LF_MMSFEEDEDTV0(?,?,?)}");
				cs.setLong(1, depId);
				cs.setLong(2, sendCount);
				cs.registerOutParameter(3, java.sql.Types.INTEGER);
				cs.execute();
				//返回值
				result = cs.getInt(3);
			} catch (SQLException e) {
				EmpExecutionContext.error(e, oprInfo + "失败,短信发送扣费回收接口调用异常!depId:"+depId+"，sendCount:"+sendCount
					+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
				if(sendCount > 0){
					//短信扣费失败
					result = -1;
				}else{
					//短信回收失败
					result = -9;
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "彩信发送扣费回收接口调用异常!depId:"+depId+"，sendCount:"+sendCount
										+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			result = -9999;
		}finally{		
			if (cs != null)
			{
				try {
					cs.close();
				} catch (SQLException e) {
					EmpExecutionContext.error(e, "彩信发送扣费回收接口关闭存储过程异常!depId:"+depId+"，sendCount:"+sendCount
					+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
				}
			}
			try
			{
				//写操作日志
				String opContent;
				//操作状态
				String oprInfoStatu = "成功，共扣除费用：";
				if(result >= 0)
				{
					if(sendCount < 0)
					{
						oprInfoStatu = "成功，共回收费用：";
						sendCount = sendCount*-1;
					}
					opContent = oprInfo + oprInfoStatu + sendCount;
					spLog.logSuccessString(lfSysuser.getUserName(), oprInfo, opType, opContent,lfSysuser.getCorpCode());
				}
				else
				{
					opContent ="彩信发送扣费回收接口调用异常,错误码:" + result;
					spLog.logFailureString(lfSysuser.getUserName(), oprInfo, opType, opContent, null, lfSysuser.getCorpCode());
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "彩信发送扣费回收接口调用写操作日志失败，depId:"+depId+"，sendCount:"+sendCount
					+"，corpCode:"+lfSysuser.getCorpCode()+"，userId:"+lfSysuser.getUserId());
			}
			//无传入连接时，关闭连接
			if(connFlag == 1)
			{
				empTransDao.closeConnection(connection);
			}
			if(sendCount>0){
				getExecutorService().execute(new AlarmSMSTask(lfSysuser,2));
		}
		}
		return result;
		
		/*调用存储过程,此方法暂时保留
		if(sendCount == null || sendCount == 0){
			//-5:彩信发送或回收条数不能为空
			return -5;
		}
		
		LfDep lfDep = null;
		try {
			
			Long depId = lfSysuser.getDepId();

			lfDep = new DepBiz().getDepById(depId);

			BaseBiz baseBiz = new BaseBiz() ;

			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("targetId", lfDep.getDepId()+"");
			
			List<LfDepUserBalance> lfDepUserBalances = baseBiz.getByCondition(LfDepUserBalance.class, conditionMap, null);
			LfDepUserBalance lfDepUserBalance = null;
			if(lfDepUserBalances != null && lfDepUserBalances.size()>0){
				lfDepUserBalance = lfDepUserBalances.get(0);
				if(sendCount > 0){
					if(sendCount <= lfDepUserBalance.getMmsBalance()){
						lfDepUserBalance.setMmsBalance(lfDepUserBalance.getMmsBalance()-sendCount);
						lfDepUserBalance.setMmsCount(lfDepUserBalance.getMmsCount()+sendCount);
						
						if(connection == null){
							empDao.update(lfDepUserBalance);
						}else{
							empTransDao.update(connection, lfDepUserBalance);
						}
						//扣费成功写一下日志文件
						String opContent ="彩信扣费成功，总扣费："+sendCount;
						spLog.logSuccessString(lfSysuser.getUserName(), opModule, opType, opContent,lfSysuser.getCorpCode());
						//add end 		
						//彩信扣费成功
						return 0;
					}else{
						//彩信余额不足
						return -2;
					}
				}else if(sendCount < 0){
					lfDepUserBalance.setMmsBalance(lfDepUserBalance.getMmsBalance()-sendCount);
					lfDepUserBalance.setMmsCount(lfDepUserBalance.getMmsCount()+sendCount);
					
					if(connection == null){
						empDao.update(lfDepUserBalance);
					}else{
						empTransDao.update(connection, lfDepUserBalance);
					}
					//回收成功写一下日志文件					
					String opContent ="彩信回收成功，总回收："+(sendCount*-1);
					spLog.logSuccessString(lfSysuser.getUserName(), opModule, opType, opContent,lfSysuser.getCorpCode());
					//add end 
					//彩信回收成功
					return 1;
				}
			}else{
				//用户所属机构没有充值
				return -4;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e);
			if(sendCount > 0){
				//彩信扣费失败
				return -1;
			}else{
				//彩信回收失败
				return -9;
			}
		}
		//彩信发送扣费回收接口调用异常
		return -9999;*/
	}
	
	/**
	 * 指定用户的可发送短信数量
	 * @return
	 */
	public Long getAllowSmsAmountByUserId(Long userId){
		
		try {
			return getAllowSmsAmount(new BaseBiz().getById(LfSysuser.class, userId));
		} catch (Exception e) {
			EmpExecutionContext.error(e, "指定用户的可发送短信数量异常。userId:"+userId);
			return null;
		}
	}
	/**
	 * 指定用户的可发送短信数量
	 * @return
	 */
	public Long getAllowSmsAmountByGuid(Long guid){
		try {
			return getAllowSmsAmount(new BaseBiz().getByGuId(LfSysuser.class, guid));
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户的可发送短信数量失败。guid:"+guid);
			return null;
		}
		//return guid;
	}
	/**
	 * 指定用户的可发送短信数量
	 * @param
	 * @return
	 */
	public Long getAllowSmsAmount(LfSysuser lfSysuser){
		
		try {
			BaseBiz baseBiz = new BaseBiz() ;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("targetId", lfSysuser.getDepId()+"");
			
			List<LfDepUserBalance> lfDepUserBalances = baseBiz.getByCondition(LfDepUserBalance.class, conditionMap, null);
			LfDepUserBalance lfDepUserBalance = null;
			if(lfDepUserBalances != null && lfDepUserBalances.size()>0){
				lfDepUserBalance = lfDepUserBalances.get(0);
				return lfDepUserBalance.getSmsBalance();
			}else{
				return 0L;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取指定用户的可发送短信数量失败。corpcode:"+lfSysuser.getCorpCode()
					+"，depId:"+lfSysuser.getDepId()+"，userid:"+lfSysuser.getUserId());
			return null;
		}
	}
	
	/**
	 * 获取SP账号计费类型
	 * @description    
	 * @param spUser SP账号
	 * @param accountType SP账号类型，1:短信，2:彩信
	 * @return  1:预付费
	 * 			2:后付费     		
	 * 			-1:SP账号为空
	 * 			-3:获取不到账号信息
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-12 下午01:15:44
	 */
	public Long getSpUserFeeFlag(String spUser, int accountType)
	{
		Long feeFlag = -3L;
		try
		{
			//SP账号校验
			if(spUser == null || spUser.trim().length() < 1)
			{
				EmpExecutionContext.error("获取SP账号计费类型，SP账号为空，spUser:"+spUser);
				//SP账号为空,返回-1
				return -1L;
			}
			//以SP账号为条件查询USERDATA表
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", spUser);
			conditionMap.put("accouttype", String.valueOf(accountType));
			//查询SP账号信息
			List<Userdata> userdataList = empDao.findListByCondition(Userdata.class, conditionMap, null);
			//查询SP账号信息
			if(userdataList != null && userdataList.size() > 0)
			{
				//计费类型(1:预付费,2:后付费)
				feeFlag = userdataList.get(0).getFeeFlag();
			}
			else
			{
				EmpExecutionContext.error("获取SP账号计费类型，查询不到账号信息，spUser:"+spUser+"，accountType:"+accountType);
			}
			return feeFlag;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SP账号计费类型失败，spUser:"+spUser+"，accountType:"+accountType);
			return -1L;
		}
	}
	
	
	/**
	 * 获取SP账号可发送短信费用
	 * @description    
	 * @param spUser sp账号
	 * @return     	 SP账号费用
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-11 下午02:51:43
	 */
	public Long getSpUserAmount(String spUser)
	{
		try
		{
			//SP账号校验
			if(spUser == null || spUser.trim().length() < 1)
			{
				EmpExecutionContext.error("获取SP账号费用，SP账号为空，spUser:"+spUser);
				return null;
			}
			//设置查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", spUser);
			//排序
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("sendNum", StaticValue.DESC);
			//根据SP账号查询USERFEE费用表
			List<Userfee> userfeeList = empDao.findListByCondition(Userfee.class, conditionMap, orderbyMap);
			if(userfeeList != null && userfeeList.size() > 0)
			{
				//返回SP账号费用
				return userfeeList.get(0).getSendNum();
			}
			else
			{
				EmpExecutionContext.error("获取SP账号费用，查询不到账号费用信息，spUser:"+spUser);
				return null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取SP账号费用失败，spUser:"+spUser);
			return null;
		}
	}

	/**
	 * 指定用户的可发送彩信数量
	 * @return
	 */
	public Long getAllowMmsAmountByUserId(Long userId){
		try {
			return getAllowMmsAmount(new BaseBiz().getById(LfSysuser.class, userId));
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户的可发送彩信数量失败。userId:"+userId);
			return null;
		}
		//return userId;//必须修改-廖骥荣
	}
	/**
	 * 指定用户的可发送彩信数量
	 * @return
	 */
	public Long getAllowMmsAmountByGuid(Long guid){
		try {
			return getAllowMmsAmount(new BaseBiz().getByGuId(LfSysuser.class, guid));
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取用户的可发送彩信数量失败。guid:"+guid);
			return null;
		}
		//return guid;
	}
	/**
	 * 指定用户的可发送彩信数量
	 * @return
	 */
	public Long getAllowMmsAmount(LfSysuser lfSysuser){
		
		LfDep lfDep = null;
		try {
			BaseBiz baseBiz = new BaseBiz() ;
			
			Long depId = lfSysuser.getDepId();

			
			lfDep = new DepBiz().getDepById(depId);
			//根据当前机构获取当前机构的级别
			Integer level = new DepDAO().getUserDepLevel(depId);
			if(level > SystemGlobals.getMaxChargeLevel(lfSysuser.getCorpCode())){
				return null;
			}
		/*	
			boolean isLoop = true;
			while(isLoop){
				
				if(lfDep.getDepLevel()<=4){
					isLoop = false;
					break;
				}else{
					depId = lfDep.getSuperiorId();
				}
			}*/
			
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("targetId", lfDep.getDepId()+"");
			
			List<LfDepUserBalance> lfDepUserBalances = baseBiz.getByCondition(LfDepUserBalance.class, conditionMap, null);
			LfDepUserBalance lfDepUserBalance = null;
			if(lfDepUserBalances != null && lfDepUserBalances.size()>0){
				lfDepUserBalance = lfDepUserBalances.get(0);
				return lfDepUserBalance.getMmsBalance();
			}else{
				return 0L;
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "指定用户的可发送彩信数量异常。corpcode:"+lfSysuser.getCorpCode()
					+"，depId:"+lfSysuser.getDepId()+"，userid:"+lfSysuser.getUserId());
			return null;
		}
	}
	/**
	 * 当前操作用户是否需要启用计费机制 add by chenhong 2012.06.14
	 * @param userid
	 * @return
	 */	
	public boolean IsChargings(Long userid)
	{
		boolean result = false;
		try {
			//是否启用计费机制
			result= SystemGlobals.isDepBilling(userid);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "当前操作用户是否需要启用计费机制异常。userid:"+userid);
			result = false;
		}
		return result;
	}
	
	/**
	 * 检查运营商余额
	 * @param spUser
	 * @param count
	 * @return 	nogwfee-没有运营商余额，
	 * 			lessgwfee-运营商余额不足
	 * 			feewarn-警告余额不足
	 * 			koufeiSuccess-成功扣费
	 * 			feefail-获取运营商余额失败
	 * 			notneedtocheck-后付费账户无需检查
	 * @throws EMPException 
	 */
	public String checkGwFee(String spUser,int count,String corpCode,Integer mstype) throws EMPException
	{
		return checkGwFee(spUser, count,corpCode,false,mstype);
	}
	/**
	 * 回收运营商余额
	 * @param sendCount 回收数量
	 * @param spUser 
	 * 
	 * @description  修改退费时更新运营商账户余额表
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-30 下午03:24:43
	 */
	public void huishouFee(int sendCount,String spUser,Integer mstype)
	{
		//检查配置文件是否需要判断运营商计费
		//String gwFeeCheck = SystemGlobals.getValue("emp.gwfee.check");
		//修改为缓存中获取(缓存会读取数据库)，每2分钟会刷新一次缓存
		String gwFeeCheck=String.valueOf(SysConfValue.getGwfeeCheck());
		if(gwFeeCheck == null || !"1".equals(gwFeeCheck))
		{
			return ;
		}
		//单企业：0，多企业：1
		int corptype = StaticValue.getCORPTYPE();
		String spacc = "";
		//如果是单企业，则去查找前端账户对应的后端账户
		if(corptype == 0)
		{
			spacc = getSpaccuidBySpUser(spUser,mstype);
			//获取不到后端账户或付费类型为后付费，则直接返回
			if(spacc == null || "spfeeflag=2".equals(spacc))
			{
				return ;
			}
		}else
		{
			//如果是多企业，则直接使用前端账户
			spacc = spUser;
		}
		spacc = spacc.toUpperCase();
		SpecialDAO specialDao = new SpecialDAO();
		try
		{
			//更新数据库
			specialDao.updateLfSpFeeBalance(spacc,String.valueOf(sendCount),mstype.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"运营商退费失败！spUser="+spUser+"，spacc:"+spacc+"，sendCount:"+sendCount+"，mstype:"+mstype);
		}
	}
	
	/**
	 * 检查运营商余额
	 * @param spUser SP账号
	 * @param count  发送条数(扣费金额)
	 * @param corpCode 企业编码
	 * @param iskoufei 是否扣费(false:否;true:是)
	 * @param mstype 账户类型
	 * @return 	
	 * 			-1：	lessgwfee-运营商余额不足
	 * 			-2：	nogwfee-没有运营商余额，
	 * 			-3：	feefail-执行运营商余额扣费失败
	 * 			0：	koufeiSuccess-扣费成功
	 * 			1：	feewarn-扣费成功，但余额不多
	 *			2：	notneedtocheck-后付费账户无需扣费,或没有配置需要检查运营商计费
	 */
	public String checkGwFee(String spUser,int count,String corpCode,
			boolean iskoufei,Integer mstype) 
	{
		//检查配置文件是否需要判断运营商计费
		//String gwFeeCheck = SystemGlobals.getValue("emp.gwfee.check");
		//修改为缓存中获取(缓存会读取数据库)，每2分钟会刷新一次缓存
		String gwFeeCheck=String.valueOf(SysConfValue.getGwfeeCheck());
		if(gwFeeCheck == null || !"1".equals(gwFeeCheck))
		{
			return "notneedtocheck";
		}
		if(spUser == null || "".equals(spUser))
		{
			EmpExecutionContext.error("运营商余额检查，前端应用账户异常，spUser为空，返回feefail。count：" + count
												+ "，corpCode:" + corpCode
												+ "，iskoufei:" + iskoufei
												+ "，mstype:" + mstype);
			return "feefail";			
		}
		try
		{
			String spaccuid = "";
			//单企业：0，多企业：1
			int corptype = StaticValue.getCORPTYPE();
			//单企业版时需要去查找前端账户对应的后端账户
			if(corptype == 0)
			{
				//获取发送账户spsuer对应的运营商账号
				spaccuid = getSpaccuidBySpUser(spUser,mstype);
				if(spaccuid== null || "".equals(spaccuid))
				{
					EmpExecutionContext.error("运营商扣费查询余额信息失败，前端应用账户无法找到对应的后端账户，返回nogwfee。spUser="+spUser
												+ "，count：" + count
												+ "，corpCode:" + corpCode
												+ "，iskoufei:" + iskoufei
												+ "，mstype:" + mstype);
					return "nogwfee";
				}
				if("spfeeflag=2".equals(spaccuid))
				{
					//后付费账户无需检查
					return "notneedtocheck";
				}
			}else
			{
				//多企业版使用前端账户进行扣费
				spaccuid = spUser;
			}
			spaccuid = spaccuid.toUpperCase();
			//获取对应账户的运营商余额信息
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("spUser", spaccuid);
			conditionMap.put("accountType", mstype.toString());
			
			List<LfSpFee> spFeeList = empDao.findListByCondition(LfSpFee.class, conditionMap, null);
			//判断是否获取到余额信息
			if(spFeeList == null || spFeeList.size() == 0)
			{
				EmpExecutionContext.error("运营商扣费查询余额信息失败，LfSpFee获取不到数据，返回feefail，spUser:" + spUser 
						+ "，count：" + count
						+ "，corpCode:" + corpCode
						+ "，iskoufei:" + iskoufei
						+ "，mstype:" + mstype);
				return "feefail";
			}
			LfSpFee fee = spFeeList.get(0);
			if(fee.getSpFeeFlag()==2)
			{
				EmpExecutionContext.info("后付费账户无需进行运营商扣费，返回notneedtocheck，spUser:" + spUser 
						+ "，count：" + count
						+ "，corpCode:" + corpCode
						+ "，iskoufei:" + iskoufei
						+ "，mstype:" + mstype);
				//后付费账户无需检查
				return "notneedtocheck";
			}
			//对应企业的余额更新时间
			Timestamp feeUpdateTime = fee.getUpdateTime();
			//上一次获取余额的状态
			String spResult = fee.getSpResult();
			//获取余额
			int feeCount = fee.getBalance();
			//如果更新时间为空，或上一次获取余额的状态未成功，或扣费金额大于余额，或更新时间离现在时间大于特定的时间间隔
			if(feeUpdateTime==null || !"ok".equals(spResult) || count-feeCount>0
					|| System.currentTimeMillis() - feeUpdateTime.getTime() > updateTimeInterval)
			{
				//执行运营商余额获取方法
				updateSpFee(fee);
				//重新获取运营商余额状态
				spResult = fee.getSpResult();
				//重新获取的运营商余额
				feeCount = fee.getBalance();
				//获取运营商失败
				if(spResult == null || !"ok".equals(spResult))
				{
					EmpExecutionContext.error("运营商扣费调用接口获取余额信息失败，使用本地余额进行扣费，spUser:" + spUser 
							+ "，count：" + count
							+ "，corpCode:" + corpCode
							+ "，iskoufei:" + iskoufei
							+ "，mstype:" + mstype
							+ "，spResult:"+spResult
							+ "，feeCount:"+feeCount);
					//return "feefail";
				}
				//扣费金额大于余额，返回运营商余额不足
				if(count-feeCount>0)
				{
					EmpExecutionContext.error("运营商扣费失败，扣费金额大于余额，返回lessgwfee="+String.valueOf(feeCount) 
							+"，spUser:" + spUser 
							+ "，count：" + count
							+ "，corpCode:" + corpCode
							+ "，iskoufei:" + iskoufei
							+ "，mstype:" + mstype);
					return "lessgwfee="+String.valueOf(feeCount); 
				}
			}

			//扣费后剩余的余额
			int afterCount = feeCount - count;
			//如果扣费，更新余额
			if(iskoufei)
			{
				//更新时间
				fee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				//更新余额
				fee.setBalance(afterCount);
				if(!empDao.update(fee))
				{
					EmpExecutionContext.error("运营商扣费更新余额信息失败，返回feefail，spUser:" + spUser 
							+ "，count：" + count
							+ "，corpCode:" + corpCode
							+ "，iskoufei:" + iskoufei
							+ "，mstype:" + mstype);
					return "feefail"; 
				}
			}
			return "koufeiSuccess";
		}catch(Exception e)
		{
			EmpExecutionContext.error(e, "运营商扣费异常，spUser:" + spUser 
										+ "，count：" + count
										+ "，corpCode:" + corpCode
										+ "，iskoufei:" + iskoufei
										+ "，mstype:" + mstype);
			return "feefail";
		}
	}

	/**
	 * @description 请求平台获取运营商余额，更新数据库记录 (暂不使用)   
	 * @param fee   余额信息对象
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-30 下午06:11:18
	 */
	/*private void updateSpFee(LfSpFee fee)
	{
		//账户
		String userId = fee.getSpUser();
		//密码
		String password = fee.getSpUserpassword();
		//请求地址
		String feeUrl = fee.getSpFeeUrl();
		int balance = 0;
		String result = "fail";
		if(feeUrl == null || "".equals(feeUrl))
		{
			result = "urlerror";
		}else
		{
		//短信账号
		if(fee.getAccountType() == 1){
			//调用方法并返回结果
			SmsUtil smsUtil = new SmsUtil();
			try
			{
				OperatorsFee opFee = new OperatorsFee();
				opFee.setUserId(userId);
				opFee.setPassword(password);
				EmpExecutionContext.error("发送模块运营商扣费查询短信余额，账号："+opFee.getUserId()+",密码："+opFee.getPassword()+",余额查询地址:"+feeUrl);
				result = smsUtil.execute(opFee, feeUrl);
				if(result != null && result.length()>0)
				{
					//结果字符串截取
					result = result.substring(result.indexOf("rg/\">")+5,result.lastIndexOf("</int>"));
					if(checkNumber(result))
					{
						balance = Integer.parseInt(result);
						result = "ok";
					}
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "发送模块短信账号["+userId+"]查询余额失败！");
				result = "fail";
			}
		}else{
		    //彩信账号
		    IMmsSend mmsSend=new WebServiceMmsSend();
		    EmpExecutionContext.error("发送模块运营商扣费查询彩信余额，账号："+userId+",密码："+password+",余额查询地址:"+feeUrl);
		    result=mmsSend.getMmsBalance("", userId, password,feeUrl);
			if(checkNumber(result))
			{
				balance = Integer.parseInt(result);
				result = "ok";
			}else{
				EmpExecutionContext.error("发送模块彩信账号["+userId+"]查询余额失败！");
			}
	   }
		}
		//更新时间
		fee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
		//请求结果
		fee.setSpResult(result);
		//余额
		fee.setBalance(balance);
		try
		{
			empDao.update(fee);
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"更新运营商余额信息异常！");
		}
	}*/
	

	/**
	 * @description 请求MBOSS平台获取运营商余额，更新数据库记录    
	 * @param fee   余额信息对象
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-7-10 下午05:40:58
	 */
	public void updateSpFee(LfSpFee fee)
	{
		try
		{
			//查询余额
			this.queryBalancd(fee);
			//更新数据库
			empDao.update(fee);
			//如果查询成功，重新设置余额
			if("ok".equals(fee.getSpResult()))
			{
				//在失败队列中存在,设置状态为成功
				if(StaticValue.getBalanceFailAccount().containsKey(fee.getSpUser()))
				{
					StaticValue.getBalanceFailAccount().get(fee.getSpUser()).setStateCode(fee.getSpResult());
					EmpExecutionContext.info("查询账户运营商余额成功，将账号失败队列中["+fee.getSpUser()+"]账号状态设置为ok。");
				}
			}
			else
			{
				//是否生成客户端监控文件 0:否；1:是
				if(StaticValue.getISCLIENTMONFILE() == 1)
				{
					//失败队列中不存，加入队列
					if(!StaticValue.getBalanceFailAccount().containsKey(fee.getSpUser()))
					{
						//设置告警对象
						BalanceAlarmBean balanceAlarmBean = new BalanceAlarmBean();
						balanceAlarmBean.setSpUser(fee.getSpUser());
						balanceAlarmBean.setAccountType(fee.getAccountType());
						balanceAlarmBean.setStateCode(fee.getSpResult());
						balanceAlarmBean.setErrInfo("账号["+fee.getSpUser()+"]查询运营商余额失败，"+ErrorCodeInfo.getInstance().getErrorDes(fee.getSpResult()));
						//加入告警队列
						StaticValue.getBalanceFailAccount().put(fee.getSpUser(), balanceAlarmBean);
						EmpExecutionContext.error("查询账户运营商余额失败，账号失败队列中不存在["+fee.getSpUser()+"]账号记录，加入队列。");
					}
					else
					{
						EmpExecutionContext.error("查询账户运营商余额失败，账号失败队列中已存在["+fee.getSpUser()+"]账号记录。");
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"更新运营商余额信息异常！账号："+fee.getSpUser()+",密码："+fee.getSpUserpassword()+",地址:"+fee.getSpFeeUrl());
		}
	}
	
	/**
	 * 查询运营商余额
	 * @description    
	 * @param fee       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-11 下午08:27:18
	 */
	public void queryBalancd(LfSpFee fee)
	{
		//账户
		String userId = "";
		//密码
		String password = null;
		//请求地址
		String feeUrl = "";
		//运营商余额
		int balance = 0;
		//运营商接口返回值，索引0:余额；1：返回码
		String[] result = {"0","-1"};
		//富信运营商接口返回值，索引0:余额；1：返回码
		String[] rmsResult = {"0","-1"};
		//查询类型，11为富信余额   ，0为彩信/短信，如果不传默认是短信/彩信
		String proType=DEFAULT_QUERY_BALANCE_TYPE;
		try
		{
			
			//账号类型
			if(fee.getAccountType() == 1)
			{
//				accountType = "短信";
			}else if(fee.getAccountType() == 2){
//				accountType = "彩信";
			}
			else
			{
//				accountType = "富信";
				//富信类型
				proType=QUERY_MBOSS_RMS_TYPE;
		    }
			//账户
			userId = fee.getSpUser();
			//密码
			password = fee.getSpUserpassword();
			//请求地址
			feeUrl = fee.getSpFeeUrl();
			//调用接口获取运营商余额
			RMSBalanceWebservice balanceWebservice = new RMSBalanceWebservice();
			//主URL不为空，使用主URL查询余额
			if(feeUrl != null && !"".equals(feeUrl.trim()))
			{
				//账户余额
				result = balanceWebservice.getAccountBalance(userId, password, feeUrl,proType);
				//返回成功
				if(result != null && "0".equals(result[1]))
				{
					//余额为数字
					if(checkNumber(result[0]))
					{
						balance = Integer.parseInt(result[0]);
						result[1] = "ok";
						//更新查询时间
						fee.setQueryTime(new Timestamp(System.currentTimeMillis()));
					}
					else
					{
						result[1] = IErrorCode.B20033;
						EmpExecutionContext.error("运营商余额查询失败，余额为非数字格式，账号："+userId+"，feeUrl:"+feeUrl+"，余额："+result[0]+"，返回码:"+result[1]);
					}
				}
				else
				{
					EmpExecutionContext.error("运营商余额查询失败，主URL调用查询接口返回，余额："+result[0]+"，返回码:"+result[1]);
				}
			}
			//主URL查询失败
			if(!"ok".equals(result[1]))
			{
				//据URL生成客户端连接失败，或调用查询运营商余额平台接口错误或无主URL地址，使用备用URL进行查询
				if("B20026".equals(result[1]) || "B20028".equals(result[1]) || "-1".equals(result[1]))
				{
					//获取备用URL
					List<String> bakUrlList = this.getbalanceBakUrl();
					if(bakUrlList != null && bakUrlList.size() > 0)
					{
						for(String bakUrl:bakUrlList)
						{
							//账户余额
							result = balanceWebservice.getAccountBalance(userId, password, bakUrl,proType);
							//返回成功，并且返回值合法
							if(result != null && "0".equals(result[1]))
							{
								if(checkNumber(result[0]))
								{
									//获取余额
									balance = Integer.parseInt(result[0]);
									//设置查询结果状态
									result[1] = "ok";
									//更新查询时间
									fee.setQueryTime(new Timestamp(System.currentTimeMillis()));
									EmpExecutionContext.info("运营商余额查询成功，账号：" + userId+",feeUrl："+feeUrl+",余额：" + result[0]);
									break;
								}
								else
								{
									result[1] = IErrorCode.B20033;
									EmpExecutionContext.error("运营商余额查询失败，余额为非数字格式，账号："+userId+"，feeUrl:"+feeUrl+"，余额："+result[0]+"，返回码:"+result[1]);
								}
							}
							else
							{
								//设置查询结果状态
								//result[1] = IErrorCode.B20033;
								EmpExecutionContext.error("运营商余额查询使用备用查询运营商地址查询失败，账号："+userId+"，bakUrl:"+bakUrl+"，余额："+(result == null ? null : result[0])+"，返回码:"+(result == null ? null : result[1]));
							}
						}
					}
					else
					{
						EmpExecutionContext.info("运营商余额查询，获取备用查询运营商地址为空。");
					}
				}
			}
			List<String> bakUrlList = this.getbalanceBakUrl();
			rmsResult=queryRMSBalance(fee, bakUrlList, balanceWebservice);
			if("ok".equals(rmsResult[1]))
			{
				//富信余额
				fee.setRmsBalance(Long.parseLong(rmsResult[0]));
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "运营商余额查询失败，账号："+userId+",密码："+password+",地址:"+feeUrl);
			//设置返回码
			if(result != null){
				result[1] = IErrorCode.B20034;
			}
		}
		try {
			//更新时间
			fee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			//请求结果
			if (result != null) {
				fee.setSpResult(result[1]);
				//如果查询成功，重新设置余额
				if ("ok".equals(result[1])) {
					//余额
					fee.setBalance(balance);
				}
			}
		
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,"获取运营商余额信息异常！账号："+userId+",密码："+password+",地址:"+feeUrl);
		}
	}
	
	
	/**
	 * 获取前端账户对应的运营商账户
	 * @param spUser
	 * @param mstype
	 * @return notneedtocheck-后付费账户无需检查,""则为未绑定
	 */
	public String getSpaccuidBySpUser(String spUser,Integer mstype)
	{
		try
		{
			SpecialDAO specialDao = new SpecialDAO();
			List<DynaBean> beanList = specialDao.getSpuserBind(spUser, mstype);
			if(beanList== null || beanList.size() == 0)
			{
				return "";
			}else
			{
				DynaBean bean  = beanList.get(0);
				Object feeObject = bean.get(TableAgwAccount.SPFEEFLAG.toLowerCase());
				String spfeeflag = "2";
				if(feeObject != null)
				{
					spfeeflag = feeObject.toString();
				}
				//如果是后付费账户
				if("2".equals(spfeeflag))
				{
					return "spfeeflag=2";
				}else
				{
					return (String) bean.get(TableAgwAccount.SPACCID.toLowerCase());
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取前端账户对应的运营商账户失败，spUser:"+spUser+"，mstype:"+mstype);
			return null;
		}
	}
	/**
	 * 减去定时任务的费用。因为定时任务创建时已经扣费，但重新同步后之前扣费需重新搞过
	 * @param corpCode 企业编码
	 */
	public void checkTimerFee(String corpCode,Map<String,LfSpFee> feeMap)
	{
		SpecialDAO specialDao = new SpecialDAO();
		try {
			//获取定时的任务
			List<LfMttask> mtList=specialDao.findLfMttaskByCorpCode(corpCode);
			//单企业：0，多企业：1
			int corptype = StaticValue.getCORPTYPE();
			//MAp<key=前端账户，value=运营商账户>
			Map<String,String> spUserIdMap = new HashMap<String, String>();
			
			//单企业版时需获取前端发送账户与运营商账户的关联关系
			if(corptype == 0)
			{
				List<DynaBean> beanList = specialDao.getSpuserBind();
				if(beanList != null && beanList.size() > 0)
				{
					for(DynaBean dyb : beanList)
					{
						spUserIdMap.put((String)dyb.get(TableUserdata.USER_ID.toLowerCase()), 
								(String)dyb.get(TableAgwAccount.SPACCID.toLowerCase()));
					}
				}else
				{
					return;
				}
			}
			if(mtList != null && mtList.size() > 0)
			{
				for(LfMttask mttask: mtList)
				{
					String spuserid = "";
					if(corptype == 0)
					{
						spuserid = spUserIdMap.get(mttask.getSpUser());
					}else
					{
						//多企业版时发送账户即等于后端账户
						spuserid = mttask.getSpUser();
					}
					if(spuserid == null )
					{
						continue;
					}else
					{
						spuserid = spuserid.toUpperCase();
					}
					//需要扣除费定时任务
					spuserid+="0";
					LfSpFee spFee = feeMap.get(spuserid);
					if(spFee == null)
					{
						continue;
					}
					String result = spFee.getSpResult();
					//判断余额是否有效
					if(!"ok".equals(result))
					{
						continue;
					}
					//发送条数
					int sendCount = 0;
					if(mttask.getMsType() == 2)
					{
						//彩信任务时条数为有效号码数
						sendCount = mttask.getEffCount().intValue();
					}else
					{
						sendCount = Integer.parseInt(mttask.getIcount());
					}
					//运营商余额
					int fee = spFee.getBalance();
					//计算余额
					int lessFee = fee - sendCount ;
					if(lessFee < 0)
					{
						lessFee = 0;
					}
					feeMap.get(spuserid).setBalance(lessFee);
				}
			}
		} catch (Exception e) {
			//异常处理
			EmpExecutionContext.error(e, "获取运营商余额时减去定时任务的费用异常。corpCode:"+corpCode);
		}
	}
	/**
	 * 重载运营商余额信息，重新向平台发送请求
	 * @param corpCode
	 */
	public List<LfSpFee> getPtFee(String corpCode)
	{
		//运营商余额信息，key-账户，value-账户对应的余额信息
		Map<String,LfSpFee> inFeeMap = new HashMap<String, LfSpFee>();
		//获取运营商余额数据
		inFeeMap = getSpFeeMap(corpCode);
		//处理定时相关的费用
		checkTimerFee(corpCode,inFeeMap);
		//更新数据库，返回余额信息
		return updateSpFee(inFeeMap);
	}
	
	/**
	 * @description 更新数据库运营商余额信息  
	 * @param inFeeMap 余额信息
	 * @return 余额信息集合      			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-30 下午06:14:51
	 */
	private List<LfSpFee> updateSpFee(Map<String,LfSpFee> inFeeMap)
	{
		List<LfSpFee> feeList = new LinkedList<LfSpFee>();
		Iterator<String> iter = inFeeMap.keySet().iterator();
		String key = "";
		while(iter.hasNext())
		{
			key = iter.next();
			LfSpFee fee = inFeeMap.get(key);
			fee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			feeList.add(fee);
			//单次捕获异常不影响其他循环
			try
			{
				empDao.update(fee);
			}catch(Exception e)
			{
				EmpExecutionContext.error(e,"更新运营商余额信息异常！spUser="+fee.getSpUser());
			}
		}
		return feeList;
	}

/**
	 * 查询短信账户和彩信账户余额(暂不使用)
 	 * @param corpCode 企业编码
	 * @param accoutType 1:信短账户; 2:彩信账户
	 * 
	 */
	/*private Map<String,LfSpFee> getSpFeeMap(String corpCode)
	{
		SmsUtil smsUtil = new SmsUtil();
		//方法内变量
		Map<String,LfSpFee> feeMap = new LinkedHashMap<String,LfSpFee>();
		IMmsSend mmsSend=new WebServiceMmsSend();
		try {
			//查询运营商账户表，获取运营商账户表信息
			List<LfSpFee> spfeeList = getSpFeeByCorpCode(corpCode);
			if(spfeeList == null || spfeeList.size() == 0)
			{
				return feeMap;
			}
			for(LfSpFee fee : spfeeList)
			{
				
				//id
				String userId = fee.getSpUser();
				//密码
				String password = fee.getSpUserpassword();
				
				OperatorsFee opFee = new OperatorsFee();
				opFee.setUserId(userId);
				opFee.setPassword(password);
				
				String feeUrl = fee.getSpFeeUrl();
				int accoutType = fee.getAccountType();
				int balance = 0;
				String result = null;
				if(feeUrl == null || "".equals(feeUrl))
				{
					result = "urlerror";
				}else
				{
					//信短账户
					if(accoutType == 1)
					{
						try 
						{
							EmpExecutionContext.error("查询短信运营商余额，账号："+opFee.getUserId()+",密码："+opFee.getPassword()+",余额查询地址:"+feeUrl);
							//调用方法并返回结果
							result = smsUtil.execute(opFee, feeUrl);
							if(result != null && result.length()>0)
							{
								//结果字符串截取
								result = result.substring(result.indexOf("rg/\">")+5,result.lastIndexOf("</int>"));
								if(checkNumber(result))
								{
									balance = Integer.parseInt(result);
									result = "ok";
								}
							}
						} catch (Exception e) 
						{
							EmpExecutionContext.error(e, "短信账号["+opFee.getUserId()+"]查询余额失败！");
							result = "fail";
						}
					}
					//彩短账户
					else
					{
						//调用方法并返回结果
						//result = getMmsBalance(userId, password,feeUrl);
						//调用C++的webservice查询彩信余额
						//第一参数是企业编码参数，可选。
						EmpExecutionContext.error("查询彩信运营商余额，账号："+userId+",密码："+password+",余额查询地址:"+feeUrl);
						result=mmsSend.getMmsBalance("", userId, password,feeUrl);
						if(checkNumber(result))
						{
							balance = Integer.parseInt(result);
							result = "ok";
						}else{
							EmpExecutionContext.error("彩信账号["+userId+"]查询余额失败！");
						}
					}
				}
				fee.setSpResult(result);
				fee.setBalance(balance);
				feeMap.put(fee.getSpUser(),fee);
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询账户运营商余额失败！");
		}
		return feeMap;
	}*/

	/**
	 * 获取彩信运营商账户余额
	 * @param userid
	 * @param password
	 * @param feeUrl
	 * @return
	 */
	public String getMmsBalance(String userid, String password,String feeUrl)
	{
	    String result = null;
	    try
	    {
	      Service service = new Service();
	      Call call = (Call)service.createCall();
	      call.setTargetEndpointAddress(new URL(feeUrl));
	      call.setOperationName(new QName("http://montnets.com/", "GetMmsBalance"));
		  call.addParameter(new QName("http://montnets.com/", "UserId"), 
		    XMLType.XSD_STRING, ParameterMode.IN);
		  call.addParameter(new QName("http://montnets.com/", "Password"), 
		    XMLType.XSD_STRING, ParameterMode.IN);
		  call.setReturnType(XMLType.XSD_STRING);
		  call.setUseSOAPAction(true);
		  call.setSOAPActionURI("http://montnets.com/GetMmsBalance");
		  result = (String)call.invoke(
		    new Object[] { userid, password });
		}
		catch (Exception localException)
		{
			result = "fail";
			EmpExecutionContext.error(localException, "获取彩信账户["+userid+"]运营商余额异常。userid:"+userid
					+",password:"+password+",feeUrl:"+feeUrl);
		}
		return result;
	}
	
	  
	/**
	 * 查询MBOSS平台短信账户和彩信账户余额
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-7-10 下午05:40:58
	 */
	private Map<String,LfSpFee> getSpFeeMap(String corpCode)
	{
		//获取运营商余额
		RMSBalanceWebservice balanceWebservice = new RMSBalanceWebservice();
		//方法内变量
		Map<String,LfSpFee> feeMap = new LinkedHashMap<String,LfSpFee>();
		try {
			//查询运营商账户表，获取运营商账户表信息
			List<LfSpFee> spfeeList = getSpFeeByCorpCode(corpCode);
			if(spfeeList == null || spfeeList.size() == 0)
			{
				return feeMap;
			}
			//id
			String userId = "";
			//密码
			String password = null;
			//请求地址
			String feeUrl = null;
			//运营商富信接口返回值，索引0:余额；1：返回码
			String[] rmsResult = {"0","-1"};
			//账户类型
			String accountType = "";
			//查询类型，11为富信余额   ，0为彩信/短信，如果不传默认是短信/彩信
			String  proType=DEFAULT_QUERY_BALANCE_TYPE;
			//运营商余额
			int balance = 0;
			//运营商余额
			long rmsBalance = 0;
			//运营商短信/彩信接口返回值，索引0:余额；1：返回码
			String[] result = {"0","-1"};
			//查询时间
			Timestamp queryTime = null;
			//状态
			String spResult = "";
			//时间间隔
			long requestInterval= StaticValue.getBalanceRequestInterval();
			//扣除定时任务状态标识，0：需要扣除定时任务费用；1：不需要
			String flag= "0";
			//获取备用URL
			List<String> bakUrlList = this.getbalanceBakUrl();
			for(LfSpFee fee : spfeeList)
			{
				result[1] = "-1";
				userId = fee.getSpUser();
				password = fee.getSpUserpassword();
				
				feeUrl = fee.getSpFeeUrl();
				balance = fee.getBalance();
				spResult = fee.getSpResult();
				queryTime = fee.getQueryTime();
				accountType = "";
				flag= "0";
				//账号类型
				if(fee.getAccountType() == 1)
				{
					accountType = "短信/富信";
				}else{
					accountType = "彩信";
				}
				
				//最后一次查询时间为空，或当前时间大于最后一次查询时间5分钟，或状态不为成功
				if(queryTime == null || System.currentTimeMillis() - queryTime.getTime() > requestInterval || !"ok".equals(spResult))
				{
					/**
					 * 短信或彩信彩信
					 */
					//主URL不为空，使用主URL查询余额
					if(feeUrl != null && !"".equals(feeUrl.trim()))
					{
						//调用接口获取运营商余额
						result = balanceWebservice.getAccountBalance(userId, password, feeUrl,proType);
						//查询成功
						if(result != null && "0".equals(result[1]))
						{
							//余额为数字
							if(checkNumber(result[0]))
							{
								EmpExecutionContext.info("查询运营商"+accountType+"余额成功，账号：" + userId+",corpCode："+corpCode+",余额：" + result[0]);
								balance = Integer.parseInt(result[0]);
								result[1] = "ok";
							}
							else
							{
								result[1] = IErrorCode.B20033;
								EmpExecutionContext.error("查询运营商"+accountType+"余额失败，余额为非数字格式，账号：" + userId+",corpCode："+corpCode+",余额：" + result[0]);
							}
						}
						else
						{
							EmpExecutionContext.error("查询运营商"+accountType+"余额失败，主URL调用查询接口返回，余额："+result[0]+"，返回码:"+result[1]+"，corpCode："+corpCode);
						}
					}
					//主URL查询失败并且有备用URL
					if(!"ok".equals(result[1]) && bakUrlList.size() > 0)
					{
						//根据URL生成客户端连接失败，或调用查询运营商余额平台接口错误或无主URL，使用备用URL进行查询
						if("B20026".equals(result[1]) || "B20028".equals(result[1]) || "-1".equals(result[1]))
						{
							for(String bakUrl:bakUrlList)
							{
								//账户余额
								result = balanceWebservice.getAccountBalance(userId, password, bakUrl,proType);
								//返回成功，并且返回值合法
								if(result != null && "0".equals(result[1]))
								{
									if(checkNumber(result[0]))
									{
										//获取余额
										balance = Integer.parseInt(result[0]);
										//设置查询结果状态
										result[1] = "ok";
										//更新查询时间
										fee.setQueryTime(new Timestamp(System.currentTimeMillis()));
										break;
									}
									else
									{
										//设置查询结果状态
										result[1] = IErrorCode.B20033;
										EmpExecutionContext.error("发送模块运营商扣费查询失败，余额为非数字格式，账号："+userId+"，feeUrl:"+feeUrl+"，corpCode："+corpCode);
									}
								}
								else
								{
									EmpExecutionContext.error("发送模块使用备用查询运营商地址查询失败，账号："+userId+"，bakUrl:"+bakUrl+"，corpCode："+corpCode);
								}
							}
						}
					}
					
					
					//富信余额查询
					rmsResult=queryRMSBalance(fee,bakUrlList,balanceWebservice);
					if(rmsResult!=null&& "ok".equals(rmsResult[1])){
						rmsBalance=Long.parseLong(rmsResult[0]);
						rmsResult[1] = "ok";
					}
					
					
				}
				else
				{
					EmpExecutionContext.info(accountType+"账号["+userId+"]查询余额，查询间隔小于"+(requestInterval/1000/60)
							+"分钟 ，使用前次查询结果。最后查询时间："+queryTime+"，corpCode："+corpCode+"，余额："+balance);
					result[1] = spResult;
					//不需要扣除定时任务费用
					flag= "1";
				}
				//设置状态
				fee.setSpResult("-1".equals(result == null ? null : result[1])?IErrorCode.B20032:(result == null ? null : result[1]));
				//设置余额
				fee.setBalance(balance);
				//设置富信余额
				fee.setRmsBalance(rmsBalance);
				//设置更新时间
				fee.setUpdateTime(new Timestamp(System.currentTimeMillis()));
				feeMap.put(fee.getSpUser()+flag,fee);
				//短信和富信余额都能查询才不告警
				if("ok".equals(result[1])&&"ok".equals(rmsResult == null ? null : rmsResult[1]))
				{
					//在失败队列中存在,设置状态为成功
					if(StaticValue.getBalanceFailAccount().containsKey(fee.getSpUser()))
					{
						StaticValue.getBalanceFailAccount().get(fee.getSpUser()).setStateCode(result[1]);
						EmpExecutionContext.info("查询账户运营商余额成功，将账号失败队列中["+fee.getSpUser()+"]账号状态设置为ok。");
					}
				}
				else
				{
					//是否生成客户端监控文件 0:否；1:是
					if(StaticValue.getISCLIENTMONFILE() == 1)
					{
						//失败队列中不存，加入队列
						if(!StaticValue.getBalanceFailAccount().containsKey(fee.getSpUser()))
						{
							//设置告警对象
							BalanceAlarmBean balanceAlarmBean = new BalanceAlarmBean();
							balanceAlarmBean.setSpUser(fee.getSpUser());
							balanceAlarmBean.setAccountType(fee.getAccountType());
							balanceAlarmBean.setStateCode(result[1]);
							balanceAlarmBean.setErrInfo("账号["+fee.getSpUser()+"]查询运营商余额失败，"+ErrorCodeInfo.getInstance().getErrorDes(result[1]));
							//加入告警队列
							StaticValue.getBalanceFailAccount().put(fee.getSpUser(), balanceAlarmBean);
							EmpExecutionContext.error("查询账户运营商余额失败，账号失败队列中不存在["+fee.getSpUser()+"]账号记录，加入队列。");
						}
						else
						{
							EmpExecutionContext.error("查询账户运营商余额失败，账号失败队列中已存在["+fee.getSpUser()+"]账号记录。");
						}
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "查询账户运营商余额失败！corpCode："+corpCode);
		}
		return feeMap;
	}
	
	/**
	 * 富信余额查询
	 *@anthor qiyin<15112605627@163.com>
	 *@param fee
	 *@return
	 */
	private String[] queryRMSBalance(LfSpFee fee,List<String> bakUrlList,RMSBalanceWebservice balanceWebservice)
	{
		String userId = fee.getSpUser();
		String password = fee.getSpUserpassword();
		String[] rmsResult =
		{ "0", "-1" };
		String feeUrl = fee.getSpFeeUrl();
		String accountType = "富信";
		// 主URL不为空，使用主URL查询余额
		if (feeUrl != null && !"".equals(feeUrl.trim()))
		{
			// 调用接口获取运营商余额
			rmsResult = balanceWebservice.getAccountBalance(userId, password, feeUrl, QUERY_MBOSS_RMS_TYPE);
			// 查询成功
			if (rmsResult != null && "0".equals(rmsResult[1]))
			{
				// 余额为数字
				if (checkNumber(rmsResult[0]))
				{
					EmpExecutionContext.info("查询运营商" + accountType + "余额成功，账号：" + userId + ",余额：" + rmsResult[0]);
					rmsResult[1] = "ok";
				} else
				{
					rmsResult[1] = IErrorCode.B20033;
					EmpExecutionContext
							.error("查询运营商" + accountType + "余额失败，余额为非数字格式，账号：" + userId + ",余额：" + rmsResult[0]);
				}
			} else
			{
				EmpExecutionContext.error("查询运营商" + accountType + "余额失败，主URL调用查询接口返回，余额：" + (rmsResult == null ? null : rmsResult[0]) + "，返回码:"
						+ (rmsResult == null ? null : rmsResult[1]));
			}
		}
		// 主URL查询失败并且有备用URL
		if (rmsResult != null && !"ok".equals(rmsResult[1]) && bakUrlList.size() > 0)
		{
			// 根据URL生成客户端连接失败，或调用查询运营商余额平台接口错误或无主URL，使用备用URL进行查询
			if ("B20026".equals(rmsResult[1]) || "B20028".equals(rmsResult[1]) || "-1".equals(rmsResult[1]))
			{
				for (String bakUrl : bakUrlList)
				{
					// 账户余额
					rmsResult = balanceWebservice.getAccountBalance(userId, password, bakUrl, QUERY_MBOSS_RMS_TYPE);
					// 返回成功，并且返回值合法
					if (rmsResult != null && "0".equals(rmsResult[1]))
					{
						if (checkNumber(rmsResult[0]))
						{
							// 设置查询结果状态
							rmsResult[1] = "ok";
							// 更新查询时间
							fee.setQueryTime(new Timestamp(System.currentTimeMillis()));
							break;
						} else
						{
							// 设置查询结果状态
							rmsResult[1] = IErrorCode.B20033;
							EmpExecutionContext.error("发送模块运营商扣费查询失败，余额为非数字格式，账号：" + userId + "，feeUrl:" + feeUrl);
						}
					} else
					{
						EmpExecutionContext.error("发送模块使用备用查询运营商地址查询失败，账号：" + userId + "，bakUrl:" + bakUrl);
					}
				}
			}
		}
		return rmsResult;
	}

	/**
	 * 验证数字
	 * @param str 传入字符串
	 * @return
	 */
	public static boolean checkNumber(String str)
	{
		if(str == null)
		{
			return false;
		}
		for (int k = str.length(); --k >= 0;)
		{
			if (!Character.isDigit(str.charAt(k)))
			{
				return false;
			}
		}
		return true;
	}
		
	/**
	 * @description 通过账户条件查找运营商余额信息    
	 * @param spUserCondition 账户，数据以逗号分隔的多个账号
	 * @return 符合条件的运营商余额信息      			 
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-30 下午07:19:27
	 */
	public List<LfSpFee> getSpFee(String spUserCondition)
	{
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		if(spUserCondition != null)
		{
			conditionMap.put("spUser&in", spUserCondition);
		}
		orderbyMap.put("spUser", StaticValue.ASC);
		try
		{
			return empDao.findListBySymbolsCondition(LfSpFee.class, conditionMap, orderbyMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询数据库运营商余额信息异常！spUser:"+spUserCondition);
			return null;
		}
	}
		
	/**
	 * 兼容多企业版获取运营商余额信息
	 * @param corpCode 企业编码
	 * @return
	 */
	public List<LfSpFee> getSpFeeByCorpCode(String corpCode)
	{
		//0:单企业，1：多企业
		int corptype = StaticValue.getCORPTYPE();
		//如果是多企业
		if(corptype == 0)
		{
			return getSpFee(null);
		}
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try {
			String spUserCondition = "";
			conditionMap.put("isValidate", "1");
			conditionMap.put("corpCode", corpCode);
			//查询企业与短信发送账户的绑定表
			List<LfSpDepBind> smsBindList = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
			for(LfSpDepBind sdb : smsBindList)
			{
				String spUser = sdb.getSpUser();
				spUserCondition += spUser + ",";
			}
			//查询企业与彩信发送账户的绑定表
			List<LfMmsAccbind> mmsBindList = empDao.findListByCondition(LfMmsAccbind.class, conditionMap, null);
			for(LfMmsAccbind sdb : mmsBindList)
			{
				String spUser = sdb.getMmsUser();
				spUserCondition += spUser + ",";
			}
			if(spUserCondition.length() > 0)
			{
				spUserCondition = spUserCondition.substring(0,spUserCondition.length() - 1);
			}
			//如果没有绑定账户，则返回长度为0的list
			if("".equals(spUserCondition))
			{
				return new ArrayList<LfSpFee>();
			}
			return getSpFee(spUserCondition);
		}catch(Exception ex)
		{
			EmpExecutionContext.error(ex,"获取运营商余额信息异常！corpCode:"+corpCode);
			return null;
		}
	}
		
	/**
	 * 执行运营商余额扣费
	 * @return 	
	 * 			-1：	lessgwfee-运营商余额不足
	 * 			-2：	nogwfee-没有运营商余额，
	 * 			-3：	feefail-获取运营商余额失败
	 * 			0：	koufeiSuccess-扣费成功
	 * 			1：	feewarn-扣费成功，但余额不多
	 *			2：	notneedtocheck-后付费账户无需扣费
	 * 		
	 * @throws EMPException 
	 */
	public String wgKoufei(LfMttask mt)
	{
		//运营商扣费结果，默认为扣费成功
		String wgresult = "";
		try
		{
			wgresult = checkGwFee(mt.getSpUser(), Integer.parseInt(mt.getIcount()),mt.getCorpCode(),true,1);
			
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "运营商扣费失败！corpCode:"+mt.getCorpCode()+",spUser:"
										+ mt.getSpUser() + ",icount:" + mt.getIcount()+",taskid:"+mt.getTaskId());
			return "feefail";
		}
		
		return wgresult;
	}
		
	/**
	 * 
	 * return
	 * 		-3：不存在机构信息
	 * 		-2：余额不足 
	 * 		-1:扣费失败
	 * 		0：成功
	 * 		1:不需要扣费
	 */
	public int depKoufei(Connection conn, LfMttask mt,int sendCount,Map<String,String> infoMap)
	{
		//默认扣费失败
		int returnInt = -1;
		try
		{
			//如果是带有审批流程的,则在创建任务时就扣费,当后序拒绝后在补回来 add by chenhong 2012.06.13		
			if("true".equals(infoMap.get("feeFlag")))
			{	
				LfSysuser lfSysuser = new BaseBiz().getById(LfSysuser.class, mt.getUserId());
				//用户存在机构信息
				if(lfSysuser != null)
				{
					//执行机构扣费	
					returnInt = sendSmsAmount(conn, lfSysuser, sendCount);
					if(returnInt != 0 && returnInt != -2)
					{	
						//扣费失败
						returnInt = -1;
					}
				}
				else
				{
					//不存在机构信息
					returnInt = -3;
				}
			}
			else
			{
				//不需要扣费
				returnInt = 1;
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e, "机构扣费失败！corpCode:"+mt.getCorpCode()+",spUser:"
										+ mt.getSpUser() + ",sendCount:" + sendCount+",taskid:"+mt.getTaskId());
			//扣费失败
			return -1;
		}
		return returnInt;
	}
	
	/**
	 * 检查SP账号余额是否大于等于发送条数
	 * @description    
	 * @param spUser  SP账号  
	 * @param sendCount  发送数量
	 * @param accountType SP账号类型，1:短信，2:彩信
	 * @return  0:成功
	 * 			2:后附费账号
	 * 			-2:余额不足	
	 *          -3:不存在账号信息或异常情况
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-11 下午03:10:22
	 */
	public int checkSpUserFee(String spUser, int sendCount, int accountType)
	{
		try
		{
			//获取SP账号计费类型
			Long feeFlag = getSpUserFeeFlag(spUser, accountType);
			//预付费类型
			if(feeFlag == 1)
			{
				//SP账号余额
				Long spUserBalance = getSpUserAmount(spUser);
				//余额为null,返回-3
				if(spUserBalance == null)
				{
					return -3;
				}
				//余额大于等于发送条数，返回0:成功
				else if(spUserBalance - sendCount >= 0)
				{
					return 0;
				}
				else
				{
					//返回-2:余额不足
					return -2;
				}
			}
			//2：后付费，直接返回2
			else if(feeFlag == 2)
			{
				return 2;
			}
			else
			{
				return -3;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "检查SP账号余额是否大于等于发送条数异常，spUser:"+spUser+"，sendCount:"+sendCount
									+"，accountType:"+accountType);
			return -3;
		}
	}
	
	
	/**
	 * 短信发送SP账号扣费接口
	 * @description    
	 * @param spUser  SP账号  
	 * @param sendCount  发送数量
	 * @param userId  操作员ID
	 * @param accountType SP账号类型，1:短信，2:彩信
	 * @return  0:成功
	 * 			2:后附费账号
	 * 			-1:	扣费失败	
	 * 			-2:余额不足	
	 *          -3:不存在账号信息 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-11 下午03:10:22
	 */
	/*public int spUserKoufei(String spUser, int sendCount, long userId, int accountType)
	{
		return spUserAmount(spUser, sendCount, userId, accountType);
	}*/
	
	/**
	 * 短信发送SP账号回收接口(SP账号扣费由网关进行扣费，前台不进行扣费，直接返回0)
	 * @description    
	 * @param spUser  SP账号  
	 * @param sendCount  发送数量(负数)
	 * @param userId  操作员ID
	 * @param accountType SP账号类型，1:短信，2:彩信
	 * @return  0:成功
	 * 			2:后附费账号
	 * 			-1:	扣费失败	
	 * 			-2:余额不足	
	 *          -3:不存在账号信息 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-11 下午03:10:22
	 */
	/*public int spUserHuiShou(String spUser, int sendCount, long userId, int accountType)
	{
		return spUserAmount(spUser, sendCount, userId, accountType);
	}*/
	/**
	 * 短信发送SP账号扣费回收接口
	 * @description    
	 * @param spUser  SP账号  
	 * @param sendCount  发送数量
	 * @param userId  操作员ID
	 * @param accountType SP账号类型，1:短信，2:彩信
	 * @return  0:成功
	 * 			2:后附费账号
	 * 			-1:	扣费失败	
	 * 			-2:余额不足	
	 *          -3:不存在账号信息 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-11 下午03:10:22
	 */
	private int spUserAmount(String spUser, int sendCount, long userId, int accountType)
	{
		try
		{
			//获取SP账号计费类型
			long feeFlag = getSpUserFeeFlag(spUser, accountType);
			//返回结果
			int result = 0;
			//预付费类型
			if(feeFlag == 1)
			{
				LfSysuser lfSysuser = new BaseBiz().getById(LfSysuser.class, userId);
				//执行SP账号扣费回收
				result = sendSmsSpUserAmount(spUser, sendCount, lfSysuser);
				//返回值为1:回收成功，设置为0:成功
				if(result==1)
				{
					result = 0;
					return result;
				}
				if(result != 0 && result != -2)
				{
					//扣费失败
					EmpExecutionContext.error("短信发送SP账号扣费回收失败，spUser:"+spUser+"，sendCount:"+sendCount+"，userId:"+userId+"，result:"+result);
					return -1;
				}
				return result;
			}
			else
			{
				//返回
				return (int)feeFlag;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "短信发送SP账号扣费回收接口异常，spUser:"+spUser+"，sendCount:"+sendCount+"，userId:"+userId);
			return -1;
		}
	}
	
	/**
	 * 获取提醒信息
	 * @description    
	 * @param sysuser
	 * @param type 1 短信 2 彩信
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-28 下午06:20:41
	 */
	public String getAlarmMessage(LfSysuser sysuser,LfDepUserBalance balance,int type){
		//判断是否达到阀值提醒条件
		BaseBiz baseBiz = new BaseBiz();
		LfDep dep=new LfDep();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			try
			{
				Long depId = sysuser.getDepId();
				dep=baseBiz.getById(LfDep.class,depId);
				conditionMap.put("targetId", String.valueOf(depId));
				conditionMap.put("corpCode", sysuser.getCorpCode());
				List<LfDepUserBalance> balances = baseBiz.findListByCondition(LfDepUserBalance.class, conditionMap, null);
				if(balances.size() == 0)
				{
					EmpExecutionContext.error("阀值提醒短信任务获取创建操作员信息失败！");
					return null;
				}
				BeanUtils.copyProperties(balance, balances.get(0));
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1,"阀值提醒短信任务获取创建操作员信息失败！");
				return null;
			}
			// 短信内容
			String msg;
			//短信阀值
			Integer smsAlarm = balance.getSmsAlarm();
			//彩信阀值
			Integer mmsAlarm = balance.getMmsAlarm();
			//是否每天提醒
			//Integer hasDay=balance.getHasDayAlarm();
			//每天提醒次数
			Integer alarmCount=balance.getAlarmCount();
			//短信阀值已提醒次数
			Integer smsAlarmedCount=balance.getAlarmedCount();
            //彩信阀值已提醒次数
			Integer mmsAlarmedCount=balance.getMmsAlarmedCount();
			if(alarmCount==null){
                alarmCount=0;
                balance.setAlarmCount(0);
            }
			if(smsAlarmedCount==null){
                smsAlarmedCount=0;
                balance.setAlarmedCount(0);
            }
			if(mmsAlarmedCount==null){
                mmsAlarmedCount=0;
                balance.setMmsAlarmedCount(0);
            }
			//如果未设置提醒次数 则直接返回
			if(alarmCount <= 0){
				return null;
			}
			if(smsAlarm == null)
			{
				smsAlarm = 0;
			}
			if(mmsAlarm == null)
			{
				mmsAlarm = 0;
			}
			// 该机构下未设置阀值
			if(smsAlarm + mmsAlarm <= 0)
			{
				return null;
			}
			boolean isReach=false;
			msg = "机构"+dep.getDepName();
			// 短信余额达到阀值条件
			if(type==1&&smsAlarm > 0 && balance.getSmsBalance().intValue() <= smsAlarm && smsAlarmedCount < alarmCount)
			{
                //设置短信已提醒次数加1
                balance.setAlarmedCount(smsAlarmedCount+1);
				isReach=true;
				msg += "短信余额不足" + smsAlarm + "条，";
			}
			// 彩信余额达到阀值条件
			if(type==2&&mmsAlarm > 0 && balance.getMmsBalance().intValue() <= mmsAlarm && mmsAlarmedCount < alarmCount)
			{
                //设置彩信已提醒次数加1
                balance.setMmsAlarmedCount(mmsAlarmedCount+1);
				isReach=true;
				msg += "彩信余额不足" + mmsAlarm + "条，";
			}
			//未达到阀值则不发送
			if(!isReach){
				return null;
			}
			msg+="请及时充值，以免影响正常使用，谢谢！";

		String name = balance.getAlarmName();
		// 是否需要尊称
		if(name != null)
		{
			msg = "尊敬的"+name+"用户，"+ msg;
		}
		return msg;
	}
	
	/**
	 * 获取查询运营商备用URL
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-11 下午12:33:39
	 */
	public List<String> getbalanceBakUrl()
	{
		List<String> bakUrlList = new ArrayList<String>();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.clear();
			conditionMap.put("globalKey","BALANCEBAKURL");
			List<LfGlobalVariable> globalVariableList = empDao.findListByCondition(LfGlobalVariable.class, conditionMap, null);
			if(globalVariableList != null && globalVariableList.size() > 0)
			{
				String balanceBakUrl = globalVariableList.get(0).getGlobalStrValue();
				if(balanceBakUrl != null && balanceBakUrl.trim().length() > 0)
				{
					EmpExecutionContext.info("查询运营商余额，获取查询运营商备用URL为:"+balanceBakUrl);
					//有多个查询运营商余额备用地址
					if(balanceBakUrl.indexOf("@")>=0)
					{
						//获取查询运营商余额备用地址
						String[] url = balanceBakUrl.split("@");
						//设置查询运营商余额备用地址
						for(int i=0; i<url.length; i++)
						{
							bakUrlList.add(url[i]);
						}
					}
					//一个查询运营商余额备用地址
					else
					{
						bakUrlList.add(balanceBakUrl);
					}
				}
				else
				{
					EmpExecutionContext.info("查询运营商余额，获取查询运营商备用URL为空。");
				}
			}
			else
			{
				EmpExecutionContext.info("查询运营商余额，通过BALANCEBAKURL获取查询运营商备用URL为空。");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询运营商余额，获取查询运营商备用URL异常。");
		}
		return bakUrlList;
	}
	
	
	/**
	 * 线程池
	 * @description    
	 * @return     线程池对象  			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-5-16 下午03:00:13
	 */
	synchronized public static ExecutorService getExecutorService(){
		if(exec==null){
	/**		corePoolSize： 线程池维护线程的最少数量
			maximumPoolSize：线程池维护线程的最大数量
			keepAliveTime： 线程池维护线程所允许的空闲时间
			unit： 线程池维护线程所允许的空闲时间的单位
			workQueue： 线程池所使用的缓冲队列
			*/
			exec =  new ThreadPoolExecutor(1, 10, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		}
		return exec;
	}
}
