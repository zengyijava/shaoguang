/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2016-4-5 下午07:58:13
 */
package com.montnets.emp.monitor.biz;

import java.sql.Connection;
import java.sql.SQLException;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.database.IConnectionManager;

/**
 * @功能概要：
 * @项目名称： emp
 * @初创作者： chentingsheng <cts314@163.com>
 * @公司名称： ShenZhen Montnets Technology CO.,LTD.
 * @创建时间： 2016-4-5 下午07:58:13
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

public class MonDbConnection
{
	private static MonDbConnection	instance;
	
	IConnectionManager connectionManager = new ConnectionManagerImp();
	
	private Connection connection = null;

	private MonDbConnection()
	{
	}

	public static synchronized MonDbConnection getInstance()
	{
		if(instance == null)
		{
			instance = new MonDbConnection();
		}
		return instance;
	}

	/**
	 * 获取连接
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-5 下午08:03:54
	 */
	public Connection getConnection()
	{
		try
		{
			if(connection == null || connection.isClosed())
			{
				connection = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			}
			return connection;
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error(e, "监控分析线程获取数据库连接异常！");
			return null;
		}
	}

	public void setConnection(Connection connection)
	{
		this.connection = connection;
	}
	
	/**
	 * 关闭连接
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-6 下午02:13:39
	 */
	public void closeConnection()
	{
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				EmpExecutionContext.error(e, "关闭监控数据库连接异常！");
			}
		}
	}
	
	/**
	 * 设置连接状态为自动提交
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-4-6 下午08:30:46
	 */
	public void setConnectionStutes()
	{
    	try
		{
			connection.setAutoCommit(true);
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error(e, "设置连接状态为自动提交异常！");
		}
	}
	
}
