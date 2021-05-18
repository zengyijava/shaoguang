/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-30 上午11:51:40
 */
package com.montnets.emp.monitor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.gateway.AgwAccount;
import com.montnets.emp.entity.monitor.LfBusareasend;
import com.montnets.emp.monitor.biz.MonDbConnection;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.monitor.dao.i.IMonitorDAO;

/**
 * @description
 * @project p_comm
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2013-12-30 上午11:51:40
 */

public class MonitorDAO extends SuperDAO implements IMonitorDAO
{
	/**
	 * 获取消息中心服务器配置信息
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-16 上午10:06:50
	 */
	public String[] getMqServerInfo()
	{
		// 消息中心服务器配置信息，0：消息队列名；1：IP端口号
		String[] mqServer = new String[2];
		String sqlUrl = "SELECT DEFAULTVALUE FROM A_GWPARAMCONF WHERE PARAMITEM='MONITORMQURL' AND GWTYPE=4000";
		List<DynaBean> mqServerUrlList = getListDynaBeanBySql(sqlUrl);
		if(mqServerUrlList != null && mqServerUrlList.size() > 0)
		{
			DynaBean mqServerUrlInfo = mqServerUrlList.get(0);
			mqServer[0] = mqServerUrlInfo.get("defaultvalue").toString();
		}
		String sqlName = "SELECT DEFAULTVALUE FROM A_GWPARAMCONF WHERE PARAMITEM='MONITORQUENAME' AND GWTYPE=4000";
		List<DynaBean> mqServerNamelList = getListDynaBeanBySql(sqlName);
		if(mqServerNamelList != null && mqServerNamelList.size() > 0)
		{
			DynaBean mqServerNameInfo = mqServerNamelList.get(0);
			mqServer[1] = mqServerNameInfo.get("defaultvalue").toString();
		}
		return mqServer;
	}

	/**
	 * 设置通道账号余额至缓存
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-17 下午03:43:31
	 */
	public void setGateAccoutFee()
	{
		try
		{
			String sql = "SELECT A.PTACCID AS GATEACCOUNT,SP.BALANCE FROM LF_SPFEE SP, A_GWACCOUNT A WHERE SP.SP_USER = A.SPACCID AND SP.ACCOUNTTYPE = 1";
			List<DynaBean> gateAccoutFeeList = getListDynaBeanBySql(MonDbConnection.getInstance().getConnection(), true, sql);
			if(gateAccoutFeeList != null && gateAccoutFeeList.size() > 0)
			{
				DynaBean gateAccoutFee;
				// 通道账号
				String agteAccout;
				// 费用余额
				String balanceStr;
				int balace = 0;
				for (int i = 0; i < gateAccoutFeeList.size(); i++)
				{
					gateAccoutFee = gateAccoutFeeList.get(i);
					agteAccout = gateAccoutFee.get("gateaccount").toString();
					balanceStr = gateAccoutFee.get("balance").toString();
					if(!"".equals(balanceStr) && balanceStr != null)
					{
						balace = Integer.valueOf(balanceStr);
					}
					// 设置缓存
					MonitorStaticValue.getGateAccoutFee().put(agteAccout, balace);
				}
			}
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "设置通道账号余额至缓存失败！");
		}
	}

	/**
	 * 设置通道账号余额至缓存
	 * 
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-1-17 下午03:43:31
	 */
	public void setGateAccoutFee(boolean isUseConnection)
	{
		try
		{
			String sql = "SELECT A.PTACCID AS GATEACCOUNT,SP.BALANCE FROM LF_SPFEE SP, A_GWACCOUNT A WHERE SP.SP_USER = A.SPACCID AND SP.ACCOUNTTYPE = 1";
			List<DynaBean> gateAccoutFeeList = null;
			if(isUseConnection)
			{
				gateAccoutFeeList = getListDynaBeanBySql(MonDbConnection.getInstance().getConnection(), true, sql);
			}
			else
			{
				gateAccoutFeeList = getListDynaBeanBySql(sql);
			}
			if(gateAccoutFeeList != null && gateAccoutFeeList.size() > 0)
			{
				DynaBean gateAccoutFee;
				// 通道账号
				String agteAccout;
				// 费用余额
				String balanceStr;
				int balace = 0;
				for (int i = 0; i < gateAccoutFeeList.size(); i++)
				{
					gateAccoutFee = gateAccoutFeeList.get(i);
					agteAccout = gateAccoutFee.get("gateaccount").toString();
					balanceStr = gateAccoutFee.get("balance").toString();
					if(!"".equals(balanceStr) && balanceStr != null)
					{
						balace = Integer.valueOf(balanceStr);
					}
					// 设置缓存
					MonitorStaticValue.getGateAccoutFee().put(agteAccout, balace);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置通道账号余额至缓存失败！");
		}
	}
	
	/**
	 * 获取通道账号网关编号
	 * 
	 * @description
	 * @param gateAccount
	 *        通道账号
	 * @return 网关编号
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-2-18 下午04:52:41
	 */
	public Long getGatewayId(String gateAccount)
	{
		try
		{
			String sql = new StringBuffer("SELECT GWNO FROM A_GWACCOUNT WHERE PTACCID = '").append(gateAccount).append("' AND SPTYPE = 0").toString();
			List<DynaBean> GatewayIdList = getListDynaBeanBySql(sql);
			if(GatewayIdList != null && GatewayIdList.size() > 0)
			{
				return Long.valueOf(GatewayIdList.get(0).get("gwno").toString());
			}
			else
			{
				return -2L;
			}
		}
		catch (NumberFormatException e)
		{
			EmpExecutionContext.error(e, "获取通道账号网关编号失败！");
			return -2L;
		}
	}

	/**
	 * 获取网优通道及SIM卡信息
	 * 
	 * @description
	 * @return 0:通道名称;1:IPCOM IP; 2:IPCOM 端口; 3:SIM卡号码
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-2 下午08:52:57
	 */
	public String[] getIpcomAndSimInfo()
	{
		String[] gateInfo = new String[] {"0","0","0","0"};
		try
		{
			String sql = "SELECT IPCOMINFO.GATENAME,IPCOMINFO.IP,IPCOMINFO.PORT,SIMINFO.PHONENO FROM A_IPCOMINFO IPCOMINFO,A_SIMINFO SIMINFO WHERE IPCOMINFO.GATEID = SIMINFO.GATEID";
			List<DynaBean> pcomAndSimList = getListDynaBeanBySql(sql);
			if(pcomAndSimList != null && pcomAndSimList.size() > 0)
			{
				DynaBean pcomAndSimInfo = null;
				for (int i = 0; i < pcomAndSimList.size(); i++)
				{
					pcomAndSimInfo = pcomAndSimList.get(i);
					String gateName = pcomAndSimInfo.get("gatename").toString();
					String ip = pcomAndSimInfo.get("ip").toString();
					String port = pcomAndSimInfo.get("port").toString();
					String phoneNo = pcomAndSimInfo.get("phoneno").toString();
					if(!"".equals(ip) && !"".equals(port) && !"".equals(phoneNo))
					{
						gateInfo[0] = gateName;
						gateInfo[1] = ip;
						gateInfo[2] = port;
						gateInfo[3] = phoneNo;
						break;
					}
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取网优通道及SIM卡信息失败！");
		}
		return gateInfo;

	}

	/**
	 * 告警短信发送记录是否小于3分钟未收到状态报告
	 * 
	 * @description
	 * @param msgId
	 * @return true 是; false 否
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午06:09:35
	 */
	public String getAlarmSmsRecord(String msgId)
	{
		String message = "";
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 三分钟之内的时间
			String Timestamp = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(System.currentTimeMillis() - (3 * 60 * 1000L)));
			StringBuffer sql = new StringBuffer("SELECT MESSAGE FROM LF_ALSMSRECORD WHERE ")
								.append("MSGID = '").append(msgId)
								.append("' AND SENDTIME >= ").append(Timestamp);

			List<DynaBean> alarmSmsList = getListDynaBeanBySql(sql.toString());
			//小于等于3分钟
			if(alarmSmsList != null && alarmSmsList.size() > 0)
			{
				message = alarmSmsList.get(0).get("message").toString();
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取告警短信发送记录是否小于3分钟未收到状态报告记录失败！");
		}
		return message;
	}
	
	/**
	 * 告警短信发送记录发送失败,未收到状态报告并且记录大于等于3分钟小于6分钟
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-9 下午07:21:27
	 */
	public List<DynaBean> getAlarmSmsRecord()
	{
		List<DynaBean> sendrecordList = null;
		try
		{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			long timeMillis = System.currentTimeMillis();
			// 三分钟之后的时间
			String TimestampStart = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(timeMillis - (3 * 60 * 1000L)));
			// 六分钟之前的时间
			String TimestampEnd = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(timeMillis - (6 * 60 * 1000L)));
			StringBuffer sql = new StringBuffer("SELECT * FROM LF_ALSMSRECORD WHERE ")
								.append("SENDSTATUS = ").append("0")
								.append(" AND SENDFLAG = ").append("0")
								.append(" AND SENDTIME <= ").append(TimestampStart)
								.append(" AND SENDTIME > ").append(TimestampEnd);
			sendrecordList = getListDynaBeanBySql(sql.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取告警短信发送记录发送失败,未收到状态报告并且记录大于等于3分钟小于6分钟失败！");
		}
		
		return sendrecordList;
	}
	
	/**
	 * 获取监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-11 上午10:55:38
	 */
	public List<DynaBean> getMonConfig()
	{
		List<DynaBean> monConfigList = null;
		try
		{
			String sql = "SELECT * FROM LF_GLOBAL_VARIABLE";
			monConfigList = getListDynaBeanBySql(MonDbConnection.getInstance().getConnection(), true, sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取监控配置信息，包括页面刷新时间 、数据分析间隔时间、网络异常时间间隔失败！");
		}
		return monConfigList;
		
	}
	
	/**
	 * 查询告警短信滞留记录
	 * @description    
	 * @param mtMsgid 消息流水号
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-17 上午11:19:26
	 */
	public List<DynaBean> getAlarmSmsRemained(String mtMsgid)
	{
		List<DynaBean> alarmSmsRemainedList = null;
		try
		{
			if(mtMsgid != null && !"".equals(mtMsgid))
			{
				
				String sql = new StringBuffer("SELECT * FROM MT_LEVEL0_QUEUE WHERE PTMSGID IN (").append(mtMsgid).append(")").toString();
				alarmSmsRemainedList = getListDynaBeanBySql(sql);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询告警短信滞留记录失败！");
		}
		return alarmSmsRemainedList;
	}
	
	/**
	 * 删除告警短信发送记录半年前的记录
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-14 上午10:28:07
	 */
	public void delAlarmSmsSendRecode()
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//删除告警短信发送记录半年前的记录
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String Timestamp = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(System.currentTimeMillis() - (182 *24 * 60 * 60 * 1000L)));
			//拼接SQL
			String sql = new StringBuffer("DELETE FROM LF_ALSMSRECORD WHERE")
			.append(" SENDTIME <= ").append(Timestamp.toString()).toString();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时删除告警短信记录失败!");
		} finally
		{
			try
			{
				close(null, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "定时删除告警短信关闭数据库资源出错！");
			}
		}
	}
	
	/**
	 * 删除业务区域发送数据两个月前的记录
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-28 上午10:02:12
	 */
	public void delBusAreaSend()
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//删除业务区域发送数据两个月前的记录
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -2);
			int data = Integer.parseInt(format.format(cal.getTime()));
			//拼接SQL
			String sql = new StringBuffer("DELETE FROM LF_BUSAREASEND WHERE")
			.append(" DATA_DATE <= ").append(data).toString();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时删除业务区域发送数据记录失败!");
		} finally
		{
			try
			{
				close(null, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "定时删除业务区域发送数据关闭数据库资源出错！");
			}
		}
	}
	
	/**
	 * 处理SQL条件,不允许使用1 =1方式
	 * @param conSql 条件
	 * @return 处理后的条件
	 */
	public String getConditionSql(String conSql)
	{
		String conditionSql = "";
		try {
			//存在查询条件
			if(conSql != null && conSql.length() > 0)
			{
				//将条件字符串首个and替换为where,不允许1 =1方式
				conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
			return conditionSql;
		} catch (Exception e) {
			EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
			return null;
		}
	}
	
	/**
	 * 根据消息时间获取业务区域数据
	 * @description    
	 * @param messageDates 消息时间
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-12-2 下午04:37:39
	 */
	public List<LfBusareasend> getBusAreaSendByDate(int messageDates)
	{
		try
		{
			//查询语句
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT * FROM LF_BUSAREASEND WHERE DATA_DATE = ").append(messageDates);
			//查询结果
			List<LfBusareasend> busareasendList = findEntityListBySQL(LfBusareasend.class, sql.toString(), StaticValue.EMP_POOLNAME);
			return busareasendList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据消息时间获取业务区域数据DAO异常！messageDates:" + messageDates);
			return null;
		}
	}
	
	
	/**
	 * 更新告警短信发送状态
	 * @description    
	 * @param hostId
	 * @param thresholdflag
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午01:51:34
	 */
	public boolean setAlarmSmsFlag(int AlarmSource, Long id, String thresholdflag, long value, boolean contion, String idStr)
	{
		try
		{
			String sql = "";
			//主机
			if(AlarmSource == 1)
			{
				sql = "UPDATE LF_MON_DHOST SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE HOSTID="+id;
			}
			//程序
			else if(AlarmSource == 2)
			{
				sql = "UPDATE LF_MON_DPROCE SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE PROCEID="+id;
			}
			//SP
			else if(AlarmSource == 3)
			{
				sql = "UPDATE LF_MON_DSPACINFO SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE SPACCOUNTID='"+idStr+"'";
			}
			else if(AlarmSource == 4)
			{
				sql = "UPDATE LF_MON_DGTACINFO SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE GATEACCOUNT='"+idStr+"'";
			}
			//主机邮件
			else if(AlarmSource == 5){
				sql = "UPDATE LF_MON_DHOST SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE HOSTID="+id;
			}
			//程序邮件
			else if(AlarmSource == 6){
				sql = "UPDATE LF_MON_DPROCE SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE PROCEID="+id;
			}
			//sp邮件
			else if(AlarmSource == 7){
				sql = "UPDATE LF_MON_DSPACINFO SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE SPACCOUNTID='"+idStr+"'";
			}
			//gate邮件
			else if(AlarmSource == 8){
				sql = "UPDATE LF_MON_DGTACINFO SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE GATEACCOUNT='"+idStr+"'";
			}
			if(contion)
			{
				if(AlarmSource<=4){
					sql += " AND THRESHOLDFLAG"+thresholdflag+" = 0";
				}else{
					sql += " AND SENDMAILFLAG"+thresholdflag+" = 0";
				}

			}
			//return executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警短信发送状态异常！AlarmSource："+AlarmSource);
			return false;
		}
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
	 * @datetime 2016-5-4 上午10:55:06
	 */
	public boolean setAlarmFlag(int alarmSource, Long proceNode, Integer proceType, Long webNode, String thresholdflag, long value)
	{
		try
		{
			String sql = "";
			//数据库告警
			if(alarmSource == 10)
			{
				sql = "UPDATE LF_MON_DBSTATE SET SMSALFLAG"+thresholdflag+" = "+value+", MAILALFLAG"+thresholdflag+" = "+ value 
						+" WHERE PROCENODE="+proceNode;
			}
			//主机网络告警
			else if(alarmSource == 20)
			{
				sql = "UPDATE LF_MON_HOSTNET SET SMSALFLAG"+thresholdflag+" = "+value+", MAILALFLAG"+thresholdflag+" = "+ value 
				+" WHERE PROCENODE="+proceNode+" AND WEBNODE="+webNode;
			}
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警发送状态异常！AlarmSource："+alarmSource);
			return false;
		}
	}
	
	/**
	 * 更新告警短信发送状态
	 * @description    
	 * @param hostId
	 * @param thresholdflag
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午01:51:34
	 */
	public boolean setAlarmSmsFlag(int AlarmSource, Long id, String thresholdflag, long value, boolean contion, String idStr, Long minId)
	{
		try
		{
			String sql = "";
			//主机
			if(AlarmSource == 1)
			{
				sql = "UPDATE LF_MON_DHOST SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE HOSTID="+id;
			}
			//程序
			else if(AlarmSource == 2)
			{
				sql = "UPDATE LF_MON_DPROCE SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE PROCEID="+id;
			}
			//SP
			else if(AlarmSource == 3)
			{
				sql = "UPDATE LF_MON_DSPACINFO SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE SPACCOUNTID='"+idStr+"'";
			}
			else if(AlarmSource == 4)
			{
				sql = "UPDATE LF_MON_DGTACINFO SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE GATEACCOUNT='"+idStr+"'";
			}
			//主机邮件
			else if(AlarmSource == 5){
				sql = "UPDATE LF_MON_DHOST SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE HOSTID="+id;
			}
			//程序邮件
			else if(AlarmSource == 6){
				sql = "UPDATE LF_MON_DPROCE SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE PROCEID="+id;
			}
			//sp邮件
			else if(AlarmSource == 7){
				sql = "UPDATE LF_MON_DSPACINFO SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE SPACCOUNTID='"+idStr+"'";
			}
			//gate邮件
			else if(AlarmSource == 8){
				sql = "UPDATE LF_MON_DGTACINFO SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE GATEACCOUNT='"+idStr+"'";
			}
			if(contion)
			{
				if(AlarmSource<=4){
					sql += " AND THRESHOLDFLAG"+thresholdflag+" = 0";
				}else{
					sql += " AND SENDMAILFLAG"+thresholdflag+" = 0";
				}
				sql += " AND ID = "+minId;
			}
			//return executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警短信发送状态异常！AlarmSource："+AlarmSource);
			return false;
		}
	}
	
	/**
	 * 更新告警短信发送状态
	 * @description    
	 * @param hostId
	 * @param thresholdflag
	 * @param value
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午01:51:34
	 */
	public boolean setAlarmSmsFlag(int AlarmSource, Long id, String thresholdflag, long value, String idStr)
	{
		try
		{
			String sql = "";
			//主机
			if(AlarmSource == 1)
			{
				sql = "UPDATE LF_MON_DHOST SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE HOSTID="+id+ " AND THRESHOLDFLAG"+thresholdflag+"<> 1";
			}
			//程序
			else if(AlarmSource == 2)
			{
				sql = "UPDATE LF_MON_DPROCE SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE PROCEID="+id+ " AND THRESHOLDFLAG"+thresholdflag+"<> 1";
			}
			//SPGATE
			else if(AlarmSource == 4)
			{
				sql = "UPDATE LF_MON_DGTACINFO SET THRESHOLDFLAG"+thresholdflag+" = "+value+" WHERE GATEACCOUNT = '"+idStr+ "' AND THRESHOLDFLAG"+thresholdflag+"<> 1";
			}
			//数据库
			else if(AlarmSource == 10)
			{
				sql = "UPDATE LF_MON_DBSTATE SET SMSALFLAG"+thresholdflag+" = "+value+" WHERE ID = "+id+" AND SMSALFLAG"+thresholdflag+"<> 1";
			}
			//主机网络
			else if(AlarmSource == 11)
			{
				sql = "UPDATE LF_MON_HOSTNET SET SMSALFLAG"+thresholdflag+" = "+value+" WHERE ID = "+id+" AND SMSALFLAG"+thresholdflag+"<> 1";
			}
			//邮件标识
			//主机
			if(AlarmSource == 5)
			{
				sql = "UPDATE LF_MON_DHOST SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE HOSTID="+id+ " AND SENDMAILFLAG"+thresholdflag+"<> 1";
			}
			//程序
			else if(AlarmSource == 6)
			{
				sql = "UPDATE LF_MON_DPROCE SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE PROCEID="+id+ " AND SENDMAILFLAG"+thresholdflag+"<> 1";
			}
			//SPGATE
			else if(AlarmSource == 8)
			{
				sql = "UPDATE LF_MON_DGTACINFO SET SENDMAILFLAG"+thresholdflag+" = "+value+" WHERE GATEACCOUNT = '"+idStr+ "' AND SENDMAILFLAG"+thresholdflag+"<> 1";
			}
			//数据库
			else if(AlarmSource == 20)
			{
				sql = "UPDATE LF_MON_DBSTATE SET MAILALFLAG"+thresholdflag+" = "+value+" WHERE ID = "+id+" AND MAILALFLAG"+thresholdflag+"<> 1";
			}
			//数据库
			else if(AlarmSource == 21)
			{
				sql = "UPDATE LF_MON_HOSTNET SET MAILALFLAG"+thresholdflag+" = "+value+" WHERE ID = "+id+" AND MAILALFLAG"+thresholdflag+"<> 1";
			}
//			return executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警短信发送状态异常！AlarmSource："+AlarmSource);
			return false;
		}
	}
	
	
	/**
	 *  设置动态信息状态
	 * @description    
	 * @param AlarmSource
	 * @param status
	 * @param EvtType
	 * @param ThresholdFlag
	 * @param id
	 * @param idStr       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:54:59
	 */
	public void setMonStatu(int AlarmSource, Integer status, Integer EvtType, String ThresholdFlag, Long id, String idStr)
	{
		StringBuffer sqlSb = new StringBuffer();
		try
		{
			//主机
			if(AlarmSource == 1)
			{
				sqlSb.append("UPDATE LF_MON_DHOST SET HOSTSTATUS = ").append(status);
				if(EvtType != null)
				{
					sqlSb.append(", EVTTYPE = ").append(EvtType);
				}
				if(ThresholdFlag != null)
				{
					sqlSb.append(", THRESHOLDFLAG").append(ThresholdFlag).append(" = ").append(0);
					sqlSb.append(", SENDMAILFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				sqlSb.append(" WHERE HOSTID = ").append(id);
			}
			//程序
			else if(AlarmSource == 2)
			{
				sqlSb.append("UPDATE LF_MON_DPROCE SET PROCESTATUS = ").append(status);
				if(EvtType != null)
				{
					sqlSb.append(", EVTTYPE = ").append(EvtType);
				}
				if(ThresholdFlag != null)
				{
					sqlSb.append(", THRESHOLDFLAG").append(ThresholdFlag).append(" = ").append(0);
					sqlSb.append(", SENDMAILFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				sqlSb.append(" WHERE PROCEID = ").append(id);
			}
			//SP账号
			else if(AlarmSource == 3)
			{
				sqlSb.append("UPDATE LF_MON_DSPACINFO SET EVTTYPE = ").append(EvtType)
				.append(" WHERE SPACCOUNTID = '").append(idStr).append("'");
			}
			//SPGATE
			else if(AlarmSource == 4)
			{
				if(EvtType != null && ThresholdFlag != null)
				{
					
					sqlSb.append("UPDATE LF_MON_DGTACINFO SET EVTTYPE = ").append(EvtType)
					.append(", THRESHOLDFLAG").append(ThresholdFlag).append(" = ").append(0)
					.append(", SENDMAILFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				else if(EvtType != null)
				{
					sqlSb.append("UPDATE LF_MON_DGTACINFO SET EVTTYPE = ").append(EvtType);
				}
				else if(ThresholdFlag != null)
				{
					sqlSb.append("UPDATE LF_MON_DGTACINFO SET THRESHOLDFLAG").append(ThresholdFlag).append(" = ").append(0)
					.append(", SENDMAILFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				sqlSb.append(" WHERE GATEACCOUNT = '").append(idStr).append("'");
			}
			//数据库监控
			else if(AlarmSource == 10)
			{
				if(EvtType != null && ThresholdFlag != null)
				{
					sqlSb.append("UPDATE LF_MON_DBSTATE SET EVTTYPE = ").append(EvtType)
					.append(" , SMSALFLAG").append(ThresholdFlag).append(" = ").append(0)
					.append(", MAILALFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				else if(EvtType != null)
				{
					sqlSb.append("UPDATE LF_MON_DBSTATE SET EVTTYPE = ").append(EvtType);
				}
				else if(ThresholdFlag != null)
				{
					sqlSb.append("UPDATE LF_MON_DBSTATE SET SMSALFLAG").append(ThresholdFlag).append(" = ").append(0)
					.append(", MAILALFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				sqlSb.append(" WHERE ID = ").append(id);
			}
			//主机网络监控
			else if(AlarmSource == 11)
			{
				if(EvtType != null && ThresholdFlag != null)
				{
					sqlSb.append("UPDATE LF_MON_HOSTNET SET EVTTYPE = ").append(EvtType)
					.append(" , NETSTATE").append(" = ").append(status)
					.append(" , SMSALFLAG").append(ThresholdFlag).append(" = ").append(0)
					.append(", MAILALFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				else if(EvtType != null)
				{
					sqlSb.append("UPDATE LF_MON_HOSTNET SET EVTTYPE = ").append(EvtType)
					.append(" , NETSTATE").append(" = ").append(status);
				}
				else if(ThresholdFlag != null)
				{
					sqlSb.append("UPDATE LF_MON_HOSTNET SET SMSALFLAG").append(ThresholdFlag).append(" = ").append(0)
					.append(", MAILALFLAG").append(ThresholdFlag).append(" = ").append(0);
				}
				sqlSb.append(" WHERE ID = ").append(id);
			}
			//executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			executeBySQL(MonDbConnection.getInstance().getConnection(), sqlSb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置状态异常！sqlSb:"+sqlSb+"，AlarmSource："+AlarmSource);
		}
	}

	/**
	 * 设置告警状态
	 * @description    
	 * @param AlarmSource
	 * @param EvtType
	 * @param id
	 * @param idStr       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:55:37
	 */
	public void setMonEvtType(int AlarmSource, Integer EvtType, Long id, String idStr)
	{
		StringBuffer sqlSb = new StringBuffer();
		try
		{
			//主机
			if(AlarmSource == 1)
			{
				sqlSb.append("UPDATE LF_MON_DHOST SET EVTTYPE = ").append(EvtType).append(" WHERE HOSTID = ").append(id);
			}
			//程序
			else if(AlarmSource == 2)
			{
				sqlSb.append("UPDATE LF_MON_DPROCE SET EVTTYPE = ").append(EvtType).append(" WHERE PROCEID = ").append(id);
			}
			else if(AlarmSource == 3)
			{
				sqlSb.append("UPDATE LF_MON_DSPACINFO SET EVTTYPE = ").append(EvtType).append(" WHERE SPACCOUNTID = '").append(idStr).append("'");
			}
			else if(AlarmSource == 4)
			{
				sqlSb.append("UPDATE LF_MON_DGTACINFO SET EVTTYPE = ").append(EvtType).append(" WHERE GATEACCOUNT = '").append(idStr).append("'");
			}
			//executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			executeBySQL(MonDbConnection.getInstance().getConnection(), sqlSb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置告警级别异常!AlarmSource："+AlarmSource);
		}
	}
	
	/**
	 * 重置动态信息状态
	 * @description    
	 * @param AlarmSource
	 * @param id
	 * @param num
	 * @param idStr       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:56:11
	 */
	public void resetMonStatu(int AlarmSource, Long id, Integer num, String idStr)
	{
		StringBuffer sqlSb = new StringBuffer();
		try
		{
			//主机
			if(AlarmSource == 1)
			{
				sqlSb.append("UPDATE LF_MON_DHOST SET EVTTYPE = 0");
				for(int i=1; i<=num; i++)
				{
					sqlSb.append(", THRESHOLDFLAG").append(i).append(" = 0 ");
					sqlSb.append(", SENDMAILFLAG").append(i).append(" = 0 ");
				}
				sqlSb.append(" WHERE HOSTID = ").append(id);
			}
			else if(AlarmSource == 2)
			{
				sqlSb.append("UPDATE LF_MON_DPROCE SET EVTTYPE = 0");
				for(int i=1; i<=num; i++)
				{
					sqlSb.append(", THRESHOLDFLAG").append(i).append(" = 0 ");
					sqlSb.append(", SENDMAILFLAG").append(i).append(" = 0 ");
				}
				sqlSb.append(" WHERE PROCEID = ").append(id);
			}
			else if(AlarmSource == 3)
			{
				sqlSb.append("UPDATE LF_MON_DSPACINFO SET EVTTYPE = 0");
				for(int i=1; i<=num; i++)
				{
					sqlSb.append(", THRESHOLDFLAG").append(i).append(" = 0 ");
					sqlSb.append(", SENDMAILFLAG").append(i).append(" = 0 ");
				}
				sqlSb.append(" WHERE SPACCOUNTID = '").append(idStr).append("'");
			}
			else if(AlarmSource == 4)
			{
				sqlSb.append("UPDATE LF_MON_DGTACINFO SET EVTTYPE = 0");
				for(int i=1; i<=num; i++)
				{
					sqlSb.append(", THRESHOLDFLAG").append(i).append(" = 0 ");
					sqlSb.append(", SENDMAILFLAG").append(i).append(" = 0 ");
				}
				sqlSb.append(" WHERE GATEACCOUNT = '").append(idStr).append("'");
			}
			
			//executeBySQL(sqlSb.toString(), StaticValue.EMP_POOLNAME);
			executeBySQL(MonDbConnection.getInstance().getConnection(), sqlSb.toString());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "重置状态异常！sqlSb:"+sqlSb+"，AlarmSource："+AlarmSource);
		}
	}
	
	/**
	 * 设置在线用户状态
	 * @description    
	 * @param monStatus       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-13 下午11:56:36
	 */
	public void setOnlineCfg(int monStatus)
	{
		try
		{
			String sql = "UPDATE LF_MON_ONLCFG SET THRESHOLDFLAG = " + monStatus;
			sql = sql+ (", SENDMAILFLAG = ")+monStatus;
			
			//executeBySQL(sql, StaticValue.EMP_POOLNAME);
			executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置在线用户数状态异常！");
		}
		
	}
	
	/**
	 * 设置实时动态信息处理标识
	 * @description    
	 * @param AlarmSource
	 * @param id
	 * @param num
	 * @param idStr
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-15 下午05:33:04
	 */
	public boolean setHandleFlag(int AlarmSource, Long id, Integer num, String idStr)
	{
		String sql = "";
		try
		{
			//主机
			if(AlarmSource == 1)
			{
				sql = "UPDATE LF_MON_DHOST SET HANDLEFLAG = "+num+" WHERE HOSTID = " + id + " AND HANDLEFLAG = " +0;
				
			}
			else if(AlarmSource == 2)
			{
				sql = "UPDATE LF_MON_DPROCE SET HANDLEFLAG = "+num+" WHERE PROCEID = " + id + " AND HANDLEFLAG = " +0;
			}
			else if(AlarmSource == 3)
			{
				sql = "UPDATE LF_MON_DSPACINFO SET HANDLEFLAG = "+num+" WHERE SPACCOUNTID = '" + idStr + "'" + " AND HANDLEFLAG = " +0;
			}
			else if(AlarmSource == 4)
			{
				sql = "UPDATE LF_MON_DGTACINFO SET HANDLEFLAG = "+num+" WHERE GATEACCOUNT = '" + idStr + "'" + " AND HANDLEFLAG = " +0;
			}
			//return executeBySQL(sql, StaticValue.EMP_POOLNAME);
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置实时动态信息处理标识异常！");
			return false;
		}
	}
	
	/**
	 * 删除失效的SP告警时间段
	 * @description    
	 * @param spOfflineThreshold       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-24 下午12:30:10
	 */
	public void delFailSpOffline(String spOfflinePrd, String spAccountId)
	{
		try
		{
			String sql = "DELETE FROM LF_SPOFFCTRL WHERE MONOFFLINEPRD NOT IN ("+spOfflinePrd+") AND SPACCOUNTID = '"+spAccountId+"'";
			//executeBySQL(sql, StaticValue.EMP_POOLNAME);
			executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "删除失效的SP告警时间段异常!spOfflinePrd:"+spOfflinePrd);
		}
	}
	
	/**
	 * 更新在线用户人数
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-12 下午04:51:37
	 */
	public void setMonOnlcfgFlag()
	{
		try
		{
			String sql="UPDATE LF_MON_ONLCFG SET SERVERNUM = '0', ONLINENUM = (SELECT COUNT(*) FROM LF_MON_ONLUSER)";
			executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新在线用户人数异常！");
		}
	}
	
	/**
	 * 数据库查询操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:10:29
	 */
	public Integer getDbDispOprState()
	{
		try
		{
			String sql="SELECT count(id) totalcount FROM LF_MON_DBOPR WHERE ID=(SELECT MIN(ID) FROM LF_MON_DBOPR)";
			int count = findCountBySQL(sql);
			
			if(count == 1)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据库查询操作状态异常！");
			return 1;
		}
	}
	
	/**
	 * 数据库更新操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:11:45
	 */
	public Integer getDbModiOprState()
	{
		try
		{
			String sql="UPDATE LF_MON_DBOPR SET PROCENODE='"+ StaticValue.getServerNumber()
					+"' WHERE ID=(SELECT DBOPR.MINID FROM (SELECT MIN(ID) MINID FROM LF_MON_DBOPR) DBOPR)";
			executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
			return 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据库更新操作状态异常！");
			return 1;
		}
	}
	
	/**
	 *  数据库删除操作状态
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-25 下午03:24:54
	 */
	public Integer getDbDelOprState()
	{
		try
		{
			String sql="DELETE FROM LF_MON_DBOPR WHERE (SELECT DBOPR1.COUNTID FROM (SELECT COUNT(ID) COUNTID FROM LF_MON_DBOPR) DBOPR1) > 1 " +
					"AND ID=(SELECT DBOPR1.MINID FROM (SELECT MIN(ID) MINID FROM LF_MON_DBOPR) DBOPR1)";
			executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
			return 0;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据库删除操作状态异常！");
			return 1;
		}
	}
	
	/**
	 * 
	 * @description    
	 * @param AlarmSource
	 * @param id
	 * @param thresholdflag
	 * @param value
	 * @param contion
	 * @param idStr
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-26 上午08:48:41
	 */
	public boolean setAlarmSmsAndMailFlag1(int AlarmSource, Long id, String thresholdflag, long value, boolean contion)
	{

		try
		{
			String sql = "";
			//数据库短信告警
			if(AlarmSource == 10)
			{
				sql = "UPDATE LF_MON_DBSTATE SET SMSALFLAG"+thresholdflag+" = "+value+", MAILALFLAG"+thresholdflag+" = "+value+" WHERE ID="+id;
			}
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警短信发送状态异常！AlarmSource："+AlarmSource);
			return false;
		}
	
	}
	
	/**
	 * 
	 * @description    
	 * @param alarmSource
	 * @param id
	 * @param thresholdflag
	 * @param value
	 * @param contion
	 * @param idStr
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-26 上午08:48:41
	 */
	public boolean setAlarmSmsAndMailFlag(int alarmSource, Long id, String thresholdflag, long value, boolean contion)
	{

		try
		{
			String sql = "";
			//数据库短信告警
			if(alarmSource == 10)
			{
				sql = "UPDATE LF_MON_DBSTATE SET SMSALFLAG"+thresholdflag+" = "+value+" WHERE ID="+id;
			}
			//主机网络短信告警
			else if(alarmSource == 11)
			{
				sql = "UPDATE LF_MON_HOSTNET SET SMSALFLAG"+thresholdflag+" = "+value+" WHERE ID="+id;
			}
			//数据库邮件告警
			else if(alarmSource == 20)
			{
				sql = "UPDATE LF_MON_DBSTATE SET MAILALFLAG"+thresholdflag+" = "+value+" WHERE ID="+id;
			}
			//主机网络邮件告警
			else if(alarmSource == 21)
			{
				sql = "UPDATE LF_MON_HOSTNET SET MAILALFLAG"+thresholdflag+" = "+value+" WHERE ID="+id;
			}
			
			if(contion)
			{
				if(alarmSource<20){
					sql += " AND SMSALFLAG"+thresholdflag+" = 0";
				}else{
					sql += " AND MAILALFLAG"+thresholdflag+" = 0";
				}
			}
			return executeBySQL(MonDbConnection.getInstance().getConnection(), sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新告警短信发送状态异常！alarmSource："+alarmSource);
			return false;
		}
	
	}
	
	/**
	 * 根据节点获取通道账户信息
	 * @description    
	 * @param appId
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-6 下午03:24:04
	 */
	public List<AgwAccount> getGateAccountInfoByGwno(Long appId)
	{
		try
		{
			String sql = "SELECT * FROM A_GWACCOUNT A LEFT JOIN GW_CLUSPBIND GW ON A.PTACCUID = GW.PTACCUID WHERE GW.GWNO="+appId;
			List<AgwAccount> agwAccountList = findEntityListBySQL(AgwAccount.class, sql.toString(), StaticValue.EMP_POOLNAME);
			return agwAccountList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据节点获取通道账户信息失败！appId:"+appId);
			return null;
		}
	}
	
	/**
	 * 获取数据库当前时间
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-5-16 下午02:03:17
	 */
	public String getDbServerTime(Connection connection)
	{
		String time = "";
		try
		{
			String sql = "";
			if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				sql = "SELECT getdate() as servertime";
			}
			else if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				sql = "SELECT to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') AS servertime from dual";
			}
			else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				sql = "SELECT current timestamp as servertime FROM sysibm.sysdummy1";
			}
			else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				sql = "SELECT now() as servertime";
			}
			List<DynaBean> dbServerTime = null;
			if(connection == null)
			{
				dbServerTime = new SuperDAO().getListDynaBeanBySql(sql);
			}
			else
			{
				dbServerTime = new SuperDAO().getListDynaBeanBySql(connection, true, sql);
			}
			time = dbServerTime.get(0).get("servertime").toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据库当前时间异常！");
		}
		return time;
	}
}
