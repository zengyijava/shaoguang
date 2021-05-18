package com.montnets.emp.common.timer.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

public class TimerCommSQL
{
	/**
	 * 
	 * @description DAO获取当前时间sql
	 * @return 当前时间sql，异常返回null
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-23 下午07:10:29
	 */
	public String getCurrentTimeSql()
	{
		try
		{
			//获取当前时间sql
			String timeSql;
			if (StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
			{
				//oracle数据库
				timeSql = "SYSTIMESTAMP";
		   	} 
			else if (StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
			{
				//sqlserver数据库
				timeSql = "getdate()";
			}
			else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
			{
				//db2数据库
				timeSql = "current timestamp";
			}
			else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
			{
				//mysql数据库
				timeSql = "current_timestamp";
			}
			else
			{
				EmpExecutionContext.error("DAO获取当前时间sql，未知的数据库类型。dbType="+StaticValue.DBTYPE);
				return null;
			}
			return timeSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO获取当前时间sql，异常。");
			return null;
		}
	}
}
