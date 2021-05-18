/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 下午06:57:29
 */
package com.montnets.emp.monitor.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonDgtacinfo;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.monitor.constant.MonErrorParams;
import com.montnets.emp.monitor.dao.MonitorDAO;

/**
 * @description
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-1-11 下午06:57:29
 */

public class MonErrorInfo extends SuperBiz
{

	BaseBiz	baseBiz	= new BaseBiz();
	
	MonitorDAO monitorDAO = new MonitorDAO();
	
	/**
	 * 设置告警基本信息
	 * @description
	 * @param lfMonErr
	 * @param lfMonErrhis
	 * @param hostid
	 * @param proceId
	 * @param appType
	 * @param monStatus
	 * @param updateTime
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午06:12:40
	 */
	public void setHostErrObjectInfo(LfMonErr lfMonErr, Long hostid, Long proceId, String spId, String gateId, int appType, int monStatus, Timestamp updateTime)
	{
		/*** 错误告警信息 ***/
		// 主机编号
		lfMonErr.setHostid(hostid);
		// 程序编号（不是程序默认为0）
		lfMonErr.setProceid(proceId);
		// SP账号
		lfMonErr.setSpaccountid(spId);
		// 通道账号
		lfMonErr.setGateaccount(gateId);
		// 应用类型
		lfMonErr.setApptype(appType);
		// 监控状态
		lfMonErr.setMonstatus(monStatus);
		// 事件处理状态：0-已自动恢复；1-新事件；2-人工处理中；3-已人工处理
		lfMonErr.setDealflag(1);
		// 事件时间
		lfMonErr.setEvttime(updateTime);
		// 接收时间
		lfMonErr.setRcvtime(new Timestamp(System.currentTimeMillis()));
	}

	/**
	 * 设置告警基本信息(无更新时间)
	 * @description    
	 * @param lfMonErr
	 * @param hostid
	 * @param proceId
	 * @param spId
	 * @param gateId
	 * @param appType
	 * @param monStatus       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-12 下午05:59:12
	 */
	public void setHostErrObjectInfo1(LfMonErr lfMonErr, Long hostid, Long proceId, String spId, String gateId, int appType, int monStatus)
	{
		/*** 错误告警信息 ***/
		// 主机编号
		lfMonErr.setHostid(hostid);
		// 程序编号（不是程序默认为0）
		lfMonErr.setProceid(proceId);
		// SP账号
		lfMonErr.setSpaccountid(spId);
		// 通道账号
		lfMonErr.setGateaccount(gateId);
		// 应用类型
		lfMonErr.setApptype(appType);
		// 监控状态
		lfMonErr.setMonstatus(monStatus);
		// 事件处理状态：0-已自动恢复；1-新事件；2-人工处理中；3-已人工处理
		lfMonErr.setDealflag(1);
		// 接收时间
		lfMonErr.setRcvtime(new Timestamp(System.currentTimeMillis()));
	}
	
	/**
	 * 设置告警基本信息(无更新时间)
	 * @description    
	 * @param lfMonErr
	 * @param hostid
	 * @param proceId
	 * @param spId
	 * @param gateId
	 * @param appType
	 * @param monStatus       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-12 下午05:59:12
	 */
	public void setHostErrObjectInfo(LfMonErr lfMonErr, Long hostid, Long proceId, String spId, String gateId, Long proceNode, Long webNode, int appType, int monStatus)
	{
		/*** 错误告警信息 ***/
		// 主机编号
		lfMonErr.setHostid(hostid);
		// 程序编号（不是程序默认为0）
		lfMonErr.setProceid(proceId);
		// SP账号
		lfMonErr.setSpaccountid(spId);
		// 通道账号
		lfMonErr.setGateaccount(gateId);
		// 程序节点
		lfMonErr.setProcenode(proceNode);
		//WEB服务器节点
		lfMonErr.setWebnode(webNode);
		// 应用类型
		lfMonErr.setApptype(appType);
		// 监控状态
		lfMonErr.setMonstatus(monStatus);
		// 事件处理状态：0-已自动恢复；1-新事件；2-人工处理中；3-已人工处理
		lfMonErr.setDealflag(1);
		// 接收时间
		lfMonErr.setRcvtime(new Timestamp(System.currentTimeMillis()));
	}
	/**
	 * 保存告警信息
	 * 
	 * @description
	 * @param lfMonErr
	 * @param lfMonErrhis
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-4 下午05:36:28
	 */
	public void addErrorInfo(LfMonErr lfMonErr, String msg, int monFlag, int evtType)
	{
		// 获取数据库连接
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			// 设置告警信息事件内容
			lfMonErr.setMsg(msg);
			// 设置告警标识
			lfMonErr.setMonthreshold(monFlag);
			// 事件类型
			lfMonErr.setEvttype(evtType);
			// 入库成功,写缓存
			lfMonErr.setId(empTransDao.saveObjectReturnID(conn, lfMonErr));
			// 设置缓存对象
			MonErrorParams MonErrorParams = new MonErrorParams();
			MonErrorParams.setId(lfMonErr.getId());
			MonErrorParams.setHostid(lfMonErr.getHostid());
			MonErrorParams.setProceid(lfMonErr.getProceid());
			MonErrorParams.setSpaccountid(lfMonErr.getSpaccountid());
			MonErrorParams.setGateaccount(lfMonErr.getGateaccount());
			MonErrorParams.setApptype(lfMonErr.getApptype());
			MonErrorParams.setEvtid(lfMonErr.getEvtid());
			MonErrorParams.setWho(lfMonErr.getWho());
			MonErrorParams.setEvttype(lfMonErr.getEvttype());
			MonErrorParams.setMonstatus(lfMonErr.getMonstatus());
			MonErrorParams.setDealflag(lfMonErr.getDealflag());
			MonErrorParams.setEvttime(lfMonErr.getEvttime());
			MonErrorParams.setRcvtime(lfMonErr.getRcvtime());
			MonErrorParams.setMsg(lfMonErr.getMsg());
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "向告警表写入告警数据异常！");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 设置告警信息至告警表中
	 * @description    
	 * @param lfMonErr  告警对象
	 * @param msg  告警内容
	 * @param monFlag  告警阀值标识
	 * @param evtType  事件类型     		
	 * @param AlarmSource  告警来源 1：主机；2：程序；3：SP账号；4：通道账号；5：在线人数	
	 * @param updateTime  告警时间		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-9 下午03:45:08
	 */
	public void setErrorAlarmInfo(LfMonErr lfMonErr, String msg, int monFlag, int evtType, int AlarmSource, Timestamp updateTime)
	{
		try
		{
			//设置查询条件
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("monthreshold", String.valueOf(monFlag));
			//主机
			if(AlarmSource == 1)
			{
				conditionMap.put("hostid", String.valueOf(lfMonErr.getHostid()));
			}
			//程序
			else if(AlarmSource == 2)
			{
				conditionMap.put("proceid", String.valueOf(lfMonErr.getProceid()));
			}
			//SP账号
			else if(AlarmSource == 3)
			{
				conditionMap.put("spaccountid", String.valueOf(lfMonErr.getSpaccountid()));
			}
			//通道账号
			else if(AlarmSource == 4)
			{
				conditionMap.put("gateaccount", String.valueOf(lfMonErr.getGateaccount()));
			}
			//数据库
			else if(AlarmSource == 10)
			{
				conditionMap.put("procenode", String.valueOf(lfMonErr.getProcenode()));
			}
			//数据库
			else if(AlarmSource == 11)
			{
				conditionMap.put("procenode", String.valueOf(lfMonErr.getProcenode()));
				conditionMap.put("webnode", String.valueOf(lfMonErr.getWebnode()));
			}
			conditionMap.put("apptype", String.valueOf(lfMonErr.getApptype()));
			//查询告警信息
			List<LfMonErr> lfMonErrList = null;
			lfMonErrList = baseBiz.getByCondition(MonDbConnection.getInstance().getConnection(), true, LfMonErr.class, conditionMap, null);
			//操作数据库标识,add:新增;update:更新
			String DBFlag = "add";
			String idStr = "";
			if(lfMonErrList != null)
			{
				Timestamp updateTm = lfMonErr.getRcvtime();
				 
				//如果存在记录，则更新记录
				if(lfMonErrList.size() > 0)
				{
					String timer = lfMonErrList.get(0).getMontimer()==null?"1":lfMonErrList.get(0).getMontimer().toString();
					idStr = lfMonErrList.get(0).getId()==null?"0":lfMonErrList.get(0).getId().toString();
					lfMonErr.setMontimer(Integer.valueOf(timer)+1);
					DBFlag = "update";
					updateTm = new Timestamp(System.currentTimeMillis());
				}
				lfMonErr.setEvttime(updateTm);
				saveErrorInfo(lfMonErr, msg, monFlag, evtType, DBFlag, idStr);
			}
			else
			{
				EmpExecutionContext.error("查询告警表数据异常！");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置告警表写入告警数据异常！");
		}
	}

	
	/**
	 * 保存告警表信息
	 * @description    
	 * @param lfMonErr 告警对象
	 * @param msg 告警内容
	 * @param monFlag  告警阀值标识
	 * @param evtType  事件类型
	 * @param DBFlag  数据库操作类型 add:新增;update:修改
	 * @param idStr   自增ID     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-12 下午03:42:31
	 */
	public void saveErrorInfo(LfMonErr lfMonErr, String msg, int monFlag, int evtType, String DBFlag, String idStr)
	{
		try
		{
			// 设置告警信息事件内容
			lfMonErr.setMsg(msg);
			// 设置告警标识
			lfMonErr.setMonthreshold(monFlag);
			// 事件类型
			lfMonErr.setEvttype(evtType);
			Long id = -1L;
			//新增
			if("add".equals(DBFlag))
			{
				// 入库成功
				id = empTransDao.saveObjectReturnID(MonDbConnection.getInstance().getConnection(), lfMonErr);
			}
			//修改
			else if("update".equals(DBFlag))
			{
				if(idStr != null && !"".equals(idStr))
				{
					id = Long.valueOf(idStr);
					LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
					objectMap.put("msg", msg);
					objectMap.put("monthreshold", monFlag);
					objectMap.put("evttype", evtType);
					objectMap.put("montimer", lfMonErr.getMontimer());
					objectMap.put("evttime", lfMonErr.getEvttime());
					objectMap.put("monstatus", lfMonErr.getMonstatus());
					if(!empTransDao.update(MonDbConnection.getInstance().getConnection(), LfMonErr.class, objectMap, idStr))
					{
						EmpExecutionContext.error("向告警表更新告警数据失败！数据ID:"+idStr);
						return;
					}
				}
				else
				{
					EmpExecutionContext.error("向告警表更新告警数据失败！数据为空！");
					return;
				}
			}
			/*lfMonErr.setId(id);
			// 设置缓存对象
			MonErrorParams monErrorParams = new MonErrorParams();
			monErrorParams.setId(lfMonErr.getId());
			monErrorParams.setHostid(lfMonErr.getHostid());
			monErrorParams.setProceid(lfMonErr.getProceid());
			monErrorParams.setSpaccountid(lfMonErr.getSpaccountid());
			monErrorParams.setGateaccount(lfMonErr.getGateaccount());
			monErrorParams.setApptype(lfMonErr.getApptype());
			monErrorParams.setEvtid(lfMonErr.getEvtid());
			monErrorParams.setWho(lfMonErr.getWho());
			monErrorParams.setEvttype(lfMonErr.getEvttype());
			monErrorParams.setMonstatus(lfMonErr.getMonstatus());
			monErrorParams.setDealflag(lfMonErr.getDealflag());
			monErrorParams.setEvttime(lfMonErr.getEvttime());
			monErrorParams.setRcvtime(lfMonErr.getRcvtime());
			monErrorParams.setMsg(lfMonErr.getMsg());*/
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "向告警表写入告警数据异常！");
		}
	}
	
	
	
	/**
	 * 设置告警信息至告警表中
	 * @description    
	 * @param lfMonErr  告警对象
	 * @param msg  告警内容
	 * @param monFlag  告警阀值标识
	 * @param evtType  事件类型     		
	 * @param updateTime  告警时间	
	 * @param spOfflinePrd  SP未提交告警时间段和时长		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-9 下午03:45:08
	 */
	public void setSpOfflineErrorAlarmInfo(LfMonErr lfMonErr, String msg, int monFlag, int evtType, Timestamp updateTime, String spOfflinePrd)
	{
		try
		{
			//设置查询条件
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("monthreshold", String.valueOf(monFlag));
			//SP账号
			conditionMap.put("spaccountid", String.valueOf(lfMonErr.getSpaccountid()));
			//告警类型
			conditionMap.put("apptype", String.valueOf(lfMonErr.getApptype()));
			//SP未提交告警时间段和时长
			conditionMap.put("spOfflinePrd", spOfflinePrd);
			//查询告警信息
			List<LfMonErr> lfMonErrList = null;
			lfMonErrList = baseBiz.getByCondition(LfMonErr.class, conditionMap, null);
			//操作数据库标识,add:新增;update:更新
			String DBFlag = "add";
			String idStr = "";
			if(lfMonErrList != null)
			{
				 Timestamp updateTm = lfMonErr.getRcvtime();
				//如果存在记录，则更新记录
				if(lfMonErrList.size() > 0)
				{
					String timer = lfMonErrList.get(0).getMontimer()==null?"1":lfMonErrList.get(0).getMontimer().toString();
					idStr = lfMonErrList.get(0).getId()==null?"0":lfMonErrList.get(0).getId().toString();
					lfMonErr.setMontimer(Integer.valueOf(timer)+1);
					DBFlag = "update";
					updateTm = new Timestamp(System.currentTimeMillis());
				}
				lfMonErr.setEvttime(updateTm);
				saveSpOfflineErrorInfo(lfMonErr, msg, monFlag, evtType, DBFlag, idStr, spOfflinePrd);
			}
			else
			{
				EmpExecutionContext.error("查询告警表数据异常！");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置告警表写入告警数据异常！");
		}
	}
	
	/**
	 * 保存SP离线告警表信息
	 * @description    
	 * @param lfMonErr 告警对象
	 * @param msg 告警内容
	 * @param monFlag  告警阀值标识
	 * @param evtType  事件类型
	 * @param DBFlag  数据库操作类型 add:新增;update:修改
	 * @param idStr   自增ID     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-1-12 下午03:42:31
	 */
	public void saveSpOfflineErrorInfo(LfMonErr lfMonErr, String msg, int monFlag, int evtType, String DBFlag, String idStr, String spOfflinePrd)
	{
		// 获取数据库连接
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			// 设置告警信息事件内容
			lfMonErr.setMsg(msg);
			// 设置告警标识
			lfMonErr.setMonthreshold(monFlag);
			// 事件类型
			lfMonErr.setEvttype(evtType);
			//未提交时间段
			lfMonErr.setSpOfflinePrd(spOfflinePrd);
			Long id = -1L;
			//新增
			if("add".equals(DBFlag))
			{
				// 入库成功
				id = empTransDao.saveObjectReturnID(conn, lfMonErr);
			}
			//修改
			else if("update".equals(DBFlag))
			{
				if(idStr != null && !"".equals(idStr))
				{
					id = Long.valueOf(idStr);
					LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
					objectMap.put("msg", msg);
					objectMap.put("monthreshold", monFlag);
					objectMap.put("evttype", evtType);
					objectMap.put("montimer", lfMonErr.getMontimer());
					objectMap.put("evttime", lfMonErr.getEvttime());
					objectMap.put("monstatus", lfMonErr.getMonstatus());
					if(!empTransDao.update(conn, LfMonErr.class, objectMap, idStr))
					{
						EmpExecutionContext.error("向告警表更新告警数据失败！数据ID:"+idStr);
						return;
					}
				}
				else
				{
					EmpExecutionContext.error("向告警表更新告警数据失败！数据为空！");
					return;
				}
			}
			lfMonErr.setId(id);
			// 设置缓存对象
			MonErrorParams monErrorParams = new MonErrorParams();
			monErrorParams.setId(lfMonErr.getId());
			monErrorParams.setHostid(lfMonErr.getHostid());
			monErrorParams.setProceid(lfMonErr.getProceid());
			monErrorParams.setSpaccountid(lfMonErr.getSpaccountid());
			monErrorParams.setGateaccount(lfMonErr.getGateaccount());
			monErrorParams.setApptype(lfMonErr.getApptype());
			monErrorParams.setEvtid(lfMonErr.getEvtid());
			monErrorParams.setWho(lfMonErr.getWho());
			monErrorParams.setEvttype(lfMonErr.getEvttype());
			monErrorParams.setMonstatus(lfMonErr.getMonstatus());
			monErrorParams.setDealflag(lfMonErr.getDealflag());
			monErrorParams.setEvttime(lfMonErr.getEvttime());
			monErrorParams.setRcvtime(lfMonErr.getRcvtime());
			monErrorParams.setMsg(lfMonErr.getMsg());
			empTransDao.commitTransaction(conn);
		}
		catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "向告警表写入告警数据异常！");
		}
		finally
		{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 更新告警短信发送状态
	 * @description    
	 * @param AlarmSource
	 * @param Id
	 * @param thresholdflag
	 * @param value
	 * @param contion
	 * @param idStr
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-22 下午02:05:53
	 */
	public boolean setAlarmSmsFlag(int AlarmSource, Long Id, String thresholdflag, long value, boolean contion, String idStr)
	{
		return monitorDAO.setAlarmSmsFlag(AlarmSource, Id, thresholdflag, value, contion, idStr);
	}
	
	/**
	 * 更新告警发送状态
	 * @description    
	 * @param alarmSource
	 * @param proceNode
	 * @param proceType
	 * @param webNode
	 * @param thresholdflag
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-4 上午10:55:19
	 */
	public boolean setAlarmFlag(int alarmSource, Long proceNode, Integer proceType, Long webNode, String thresholdflag, long value)
	{
		return monitorDAO.setAlarmFlag(alarmSource, proceNode, proceType, webNode, thresholdflag, value);
	}
	
	/**
	 * 更新告警短信发送状态
	 * @description    
	 * @param AlarmSource
	 * @param Id
	 * @param thresholdflag
	 * @param value
	 * @param contion
	 * @param idStr
	 * @param minId
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-22 下午02:05:53
	 */
	public boolean setAlarmSmsFlag(int AlarmSource, Long Id, String thresholdflag, long value, boolean contion, String idStr, Long minId)
	{
		return monitorDAO.setAlarmSmsFlag(AlarmSource, Id, thresholdflag, value, contion, idStr, minId);
	}
	
	/**
	 * 更新告警短信发送状态
	 * @description    
	 * @param AlarmSource
	 * @param Id
	 * @param thresholdflag
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-22 下午02:06:16
	 */
	public boolean setAlarmSmsFlag(int AlarmSource, Long id, String thresholdflag, long value, String idStr)
	{
		return monitorDAO.setAlarmSmsFlag(AlarmSource, id, thresholdflag, value, idStr);
	}
	
	/**
	 * 更新告警状态
	 * @description    
	 * @param AlarmSource
	 * @param Id
	 * @param thresholdflag
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-22 下午02:06:16
	 */
	public boolean setAlarmFlag(int AlarmSource, Long id, String thresholdflag, long value, String idStr)
	{
		return monitorDAO.setAlarmSmsFlag(AlarmSource, id, thresholdflag, value, idStr);
	}
	/**
	 * 更新在线用户监控状态
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-12 下午03:26:45
	 */
	public void setMonOnlcfg()
	{
		try
		{
			empDao.update(StaticValue.getMonOnlinecfg());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新在线用户信息监控状态异常！");
		}
	}
	
	public boolean setOnlineAlarmSmsFlag(String thresholdflag, String evtType, boolean flag)
	{
		try
		{
			if(thresholdflag == null && evtType == null)
			{
				return false;
			}
			LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
			if(thresholdflag != null)
			{
				objectMap.put("monThresholdFlag", thresholdflag);
			}
			if(evtType != null)
			{
				objectMap.put("evttype", evtType);
			}
			if(flag)
			{
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("monThresholdFlag", "0");
				return empDao.update(MonDbConnection.getInstance().getConnection(), true, LfMonOnlcfg.class, objectMap, conditionMap);
			}
			else
			{
				return empDao.update(MonDbConnection.getInstance().getConnection(), true, LfMonOnlcfg.class, objectMap, null);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新在线用户告警标识异常！");
			return false;
		}
	}
	
	public boolean setOnlineAlarmSmsFlag(String thresholdflag, String evtType, boolean flag,String sendmailflag)
	{
		try
		{
			if(thresholdflag == null && evtType == null && sendmailflag==null)
			{
				return false;
			}
			LinkedHashMap<String,String> objectMap = new LinkedHashMap<String, String>();
			if(thresholdflag != null)
			{
				objectMap.put("monThresholdFlag", thresholdflag);
			}
			if(sendmailflag != null)
			{
				objectMap.put("sendmailFlag", sendmailflag);
			}
			if(evtType != null)
			{
				objectMap.put("evttype", evtType);
			}
			if(flag)
			{
				LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
				if(thresholdflag!=null){
					conditionMap.put("monThresholdFlag", "0");
				}
				if(sendmailflag!=null){
					conditionMap.put("sendmailFlag", "0");
				}
				return empDao.update(MonDbConnection.getInstance().getConnection(), true, LfMonOnlcfg.class, objectMap, conditionMap);
			}
			else
			{
				return empDao.update(MonDbConnection.getInstance().getConnection(), true, LfMonOnlcfg.class, objectMap, null);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新在线用户告警标识异常！");
			return false;
		}
	}
	
	
	/**
	 * 设置动态信息状态
	 * @description    
	 * @param AlarmSource
	 * @param status
	 * @param EvtType
	 * @param ThresholdFlag
	 * @param id
	 * @param idStr       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:54:21
	 */
	public void setMonStatu(int AlarmSource, Integer status, Integer EvtType, String ThresholdFlag, Long id, String idStr)
	{
		monitorDAO.setMonStatu(AlarmSource, status, EvtType, ThresholdFlag, id, idStr);
	}
	
	/**
	 * 设置告警状态
	 * @description    
	 * @param AlarmSource
	 * @param EvtType
	 * @param id
	 * @param idStr       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:55:09
	 */
	public void setMonEvtType(int AlarmSource, Integer EvtType, Long id, String idStr)
	{
		monitorDAO.setMonEvtType(AlarmSource, EvtType, id, idStr);
	}
	
	/**
	 * 重置动态信息状态
	 * @description    
	 * @param AlarmSource
	 * @param id
	 * @param num
	 * @param idStr       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:55:45
	 */
	public void resetMonStatu(int AlarmSource, Long id, Integer num, String idStr)
	{
		monitorDAO.resetMonStatu(AlarmSource, id, num, idStr);
	}
	
	/**
	 * 设置在线用户状态
	 * @description    
	 * @param monStatus       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:56:20
	 */
	public void setOnlineCfg(int monStatus)
	{
		monitorDAO.setOnlineCfg(monStatus);
	}
	
	/**
	 * 设置SPGATE在线状态
	 * @description    
	 * @param gateaccount
	 * @param onlinestatus       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-16 下午02:12:54
	 */
	public void setGateOnlinestatus(String gateaccount, Integer onlinestatus)
	{
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("onlinestatus", String.valueOf(onlinestatus));
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("gateaccount", gateaccount);

			baseBiz.update(MonDbConnection.getInstance().getConnection(), LfMonDgtacinfo.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置SPGATE在线状态异常！");
		}
	}
	
	public boolean setAlarmSmsAndMailFlag(int AlarmSource, Long id, String thresholdflag, long value, boolean contion)
	{
		return monitorDAO.setAlarmSmsAndMailFlag(AlarmSource, id, thresholdflag, value, contion);
	}
}
