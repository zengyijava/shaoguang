package com.montnets.emp.database;

import com.bitmechanic.sql.ConnectionPool;
import com.bitmechanic.sql.ConnectionPoolManager;
import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 下午07:24:03
 * @description
 */
public class JDBCPoolConnectionManager implements Serializable
{

	private static final long serialVersionUID = 8038180171811939864L;

	private static JDBCPoolConnectionManager myself = new JDBCPoolConnectionManager();

	private ConnectionPoolManager P_connpoolm;

	private Map<String, String[]> proInfoMap = null;

	private String MonitorInterval;

	private String MaxOpenConnections;

	private String IdleConnectionTimeout;

	private String CheckoutConnectingTimeout;

	private String MaxCheckoutConnectionTimes;

	private String porpName = "SystemGlobals";

	private JDBCPoolConnectionManager()
	{
		P_connpoolm = null;
		getProInfo(porpName);
	}

	/**
	 * 
	 * @param poolName
	 * @param poolType
	 */
	synchronized public static void initPool(String[] poolName, boolean poolType)
	{
		if (poolType)
		{
			for (int index = 0; index < poolName.length; index++)
			{
				myself.createPool(poolName[index]);
			}

		}
	}

	/**
	 * 
	 * @param poolName
	 * @param poolType
	 */
	synchronized public static void initPool(Set<String> poolName,
			boolean poolType)
	{
		if (poolType)
		{
			java.util.Iterator<String> iter = poolName.iterator();
			while (iter.hasNext())
			{
				myself.createPool(iter.next());
			}

		}
	}

	/**
	 * 
	 * @param P_propFile
	 */
	private void getProInfo(String P_propFile)
	{

		EmpExecutionContext.debug("开始读取配置文件...");

		proInfoMap = new HashMap<String, String[]>();
		String[] emp = new String[5];
		String[] smsacc = new String[5];
		String[] smssvr = new String[5];

		try
		{
			ResourceBundle rb = ResourceBundle.getBundle(P_propFile);

			try
			{
				emp[0] = rb.getString("montnets.emp.jdbcUrl");
				emp[1] = "oracle.jdbc.driver.OracleDriver";
				emp[2] = rb.getString("montnets.emp.user");
				//解密处理
				AuthenAtom aa=new AuthenAtom();
				String str=rb.getString("montnets.emp.password");
				String str2 = aa.parseCode(str);
				emp[3] = str2;
//				emp[3] = rb.getString("montnets.emp.password");
				emp[4] = "";
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取异常取配置文件相关值异常。");
				emp[4] = "NOTFOUND";
			} finally
			{
				if (null != emp[0] && !"".equals(emp[0])
						&& !"NOTFOUND".equals(emp[4]))
				{
					proInfoMap.put(StaticValue.EMP_POOLNAME, emp);
				}

			}

			try
			{
				smsacc[0] = rb.getString("montnets.webgate.jdbcUrl");
				smsacc[1] = rb.getString("montnets.webgate.driverClass");
				smsacc[2] = rb.getString("montnets.webgate.user");
				smsacc[3] = rb.getString("montnets.webgate.password");
				smsacc[4] = "";
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取异常取配置文件相关值异常。");
				smsacc[4] = "NOTFOUND";
			} finally
			{

				if (null != smsacc[0] && !"".equals(smsacc[0])
						&& !"NOTFOUND".equals(smsacc[4]))
					proInfoMap.put(StaticValue.SMSACC_POOLNAME, smsacc);
			}

			try
			{
				smssvr[0] = rb.getString("montnets.history.jdbcUrl");
				smssvr[1] = rb.getString("montnets.history.driverClass");
				smssvr[2] = rb.getString("montnets.history.user");
				smssvr[3] = rb.getString("montnets.history.password");
				smssvr[4] = "";
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取异常取配置文件相关值异常。");
				smssvr[4] = "NOTFOUND";
			} finally
			{
				if (null != smssvr[0] && !"".equals(smssvr[0])
						&& !"NOTFOUND".equals(smssvr[4]))
					proInfoMap.put(StaticValue.SMSSVR_POOLNAME, smssvr);
			}

			MonitorInterval = rb.getString("MonitorInterval");
			MaxOpenConnections = rb.getString("MaxOpenConnections");
			IdleConnectionTimeout = rb.getString("IdleConnectionTimeout");
			CheckoutConnectingTimeout = rb
					.getString("CheckoutConnectingTimeout");
			MaxCheckoutConnectionTimes = rb
					.getString("MaxCheckoutConnectionTimes");

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "读取配置文件异常。");
		}
		EmpExecutionContext.debug("读取配置文件成功！");

	}

	private void createPool(String poolName)
	{

		String P_alias = poolName;
		String User = null;
		String Password = null;
		String DriverName = null;
		String DriverURL = null;

		String[] dbInfo = new String[4];
		if (proInfoMap == null)
		{
			getProInfo(porpName);
		}
		if (null != proInfoMap.get(P_alias)
				)
		{
			dbInfo = (String[]) proInfoMap.get(P_alias);
			DriverURL = dbInfo[0];
			DriverName = dbInfo[1];
			User = dbInfo[2];
			Password = dbInfo[3];
			EmpExecutionContext.debug("配置文件信息**************" + DriverURL);

		}

		try
		{

			if (myself.P_connpoolm == null)
				myself.P_connpoolm = new ConnectionPoolManager((stringToInt(
						MonitorInterval, 300)));
			if (null != proInfoMap.get(P_alias)
					)
			{
				myself.P_connpoolm.addAlias(P_alias, DriverName, DriverURL,
						User, Password, stringToInt(MaxOpenConnections, 500),
						stringToInt(IdleConnectionTimeout, 150), stringToInt(
								CheckoutConnectingTimeout, 200), stringToInt(
								MaxCheckoutConnectionTimes, 40), 100, false);
				if (null != myself.P_connpoolm.getPools())
				{
					EmpExecutionContext.debug((new StringBuilder("\u540D\u4E3A:"))
							.append(DriverURL).append(
									" \u7684\u8FDE\u63A5\u6C60\u5EFA\u7ACB!")
							.toString());
				}
			}
		} catch (MissingResourceException mrsex)
		{
			System.out
					.println((new StringBuilder(
							"\u6CA1\u6709\u627E\u5230\u6307\u5B9A\u7684\u6587\u4EF6 web-inf/classes/"))
							.append(poolName).append(".propreties").toString());
			EmpExecutionContext.debug(mrsex.toString());
			EmpExecutionContext.error(mrsex, "创建连接池异常。");
		} catch (NullPointerException nex)
		{
			EmpExecutionContext.debug("\u7A7A\u6307\u9488\u9519\u8BEF!");
			EmpExecutionContext.debug((new StringBuilder("User = ")).append(User)
					.toString());
			EmpExecutionContext.debug((new StringBuilder("Password = ")).append(
					Password).toString());
			EmpExecutionContext.debug((new StringBuilder("DriverName = ")).append(
					DriverName).toString());
			EmpExecutionContext.debug((new StringBuilder("DriverURL = ")).append(
					DriverURL).toString());
			EmpExecutionContext.debug(nex.toString());
			EmpExecutionContext.error(nex, "创建连接池空指针异常。");
		} catch (IllegalAccessException iex)
		{
			EmpExecutionContext.error(iex, "创建连接池不合法入口异常。");
			EmpExecutionContext.debug(iex.toString());
		} catch (InstantiationException inex)
		{
			EmpExecutionContext.error(inex, "创建连接池实例化异常。");
			EmpExecutionContext.debug(inex.toString());
		} catch (ClassNotFoundException cnex)
		{
			EmpExecutionContext.error(cnex, "创建连接池相关类找不到异常。");
			EmpExecutionContext.debug(cnex.toString());
		} catch (SQLException sqlex)
		{
			System.out
					.println("\u5EFA\u7ACB\u6570\u636E\u5E93\u8FDE\u63A5\u6C60\u5931\u8D25!");
			EmpExecutionContext.debug((new StringBuilder("User = ")).append(User)
					.toString());
			EmpExecutionContext.debug((new StringBuilder("Password = ")).append(
					Password).toString());
			EmpExecutionContext.debug((new StringBuilder("DriverName = ")).append(
					DriverName).toString());
			EmpExecutionContext.debug((new StringBuilder("DriverURL = ")).append(
					DriverURL).toString());
			EmpExecutionContext.debug(sqlex.toString());
			EmpExecutionContext.error(sqlex, "创建连接池SQL异常。");
		}

	}

	public static JDBCPoolConnectionManager getInstance()
	{
		return myself;
	}

	public Connection requestConnection(String poolName)
	{
		Connection connection = null;
		ConnectionPool connpool = null;
		synchronized (this)
		{
			if (myself.P_connpoolm == null)
				myself.createPool(poolName);
			try
			{
				connpool = myself.P_connpoolm.getPool(poolName);
			} catch (SQLException sqlex)
			{
				EmpExecutionContext.error(sqlex, "此连接池不存在。");
				System.out
						.println((new StringBuilder(String.valueOf(poolName)))
								.append("//此连接池不存在").toString());
				myself.createPool(poolName);
			}
			try
			{
				if (connpool == null)
					connpool = myself.P_connpoolm.getPool(poolName);
				connection = connpool.getConnection();
			} catch (SQLException sqlex)
			{
				EmpExecutionContext.error(sqlex, "此连接池不存在。");
				EmpExecutionContext.debug(sqlex.toString());
				 clearConnection(connpool);
			}
		}
		return connection;
	}

	/*
	 * public Connection requestConnection() { return
	 * requestConnection("DataBase"); }
	 */

	public void returnConnection(Connection connection)
	{
		if (connection == null)
			return;
		try
		{
			connection.close();
		} catch (Exception exception)
		{
			EmpExecutionContext.error(exception, "关闭连接异常。");
		}
	}

	public void clearConnection(ConnectionPool connpool)
	{
		try
		{
			connpool.removeAllConnections();
			String alias = connpool.getAlias();
			myself.P_connpoolm.removeAlias(alias);
			EmpExecutionContext.debug((new StringBuilder("\u540D\u4E3A:")).append(
					alias).append(
					" \u7684\u8FDE\u63A5\u6C60\u88AB\u5220\u9664!").toString());
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "清空连接异常。");
			System.out.print((new StringBuilder("clearConnection=")).append(
					ex.toString()).toString());
		}
	}

	@SuppressWarnings("unchecked")
	protected void finalize()
	{
		if (myself.P_connpoolm != null)
		{

			Enumeration<ConnectionPool> enu = (Enumeration<ConnectionPool>) myself.P_connpoolm
					.getPools();
			while (enu.hasMoreElements())
			{
				clearConnection(enu.nextElement());
			}
			EmpExecutionContext.debug("清空数据库连接，连接池关闭......");
		}
	}

	private int stringToInt(String str, int def)
	{
		if (str == null)
			return def;
		try
		{
			return Integer.parseInt(str);
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "转化整型异常。");
			return def;
		}
	}

}
