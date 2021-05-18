package com.montnets.emp.datasource.biz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.util.PageInfo;

public class DbconnectBiz extends SuperBiz
{

	/**
	 * 
	 * @param dbConnect
	 * @return
	 * @throws Exception
	 */
	public boolean addDBConnect(LfDBConnect dbConnect) throws Exception
	{
		//返回结果
		boolean result = false;

		try
		{
			// 数据库地址字符串
			String conStr = this.createConnUrl(dbConnect);
			//设置对象的连接字符串
			dbConnect.setConStr(conStr);
			// 返回结果
			result = empDao.save(dbConnect);
		} catch (Exception e)
		{
			// 异常设为否
			result = false;
			EmpExecutionContext.error(e, "新增数据源异常。");
			throw e;
		}
		// 返回结果
		return result;
	}

	/**
	 * 
	 * @param dbIds
	 * @return
	 * @throws Exception
	 */
	public Integer delSelectedDbConnect(String[] dbIds) throws Exception
	{
		// 删除记录数
		int delnum = 0;

		// id格式
		String ids = "";

		try
		{
			//循环
			for (int index = 0; index < dbIds.length; index++)
			{
				// 循环拼id
				ids += dbIds[index] + ",";

			}
			//有值则需做处理
			if (ids != null && ids.length() != 0)
			{
				//去掉多了的句号
				ids = ids.substring(0, ids.lastIndexOf(","));
			} else
			{
				return null;
			}

			// 执行语句，返回结果
			delnum = empDao.delete(LfDBConnect.class, ids);
		} catch (Exception e)
		{
			//异常则返回0，且抛出
			delnum = 0;
			EmpExecutionContext.error(e, "删除选中数据源异常。");
			throw e;
		}
		// 返回删除记录数
		return delnum;
	}

	/**
	 * 
	 * @param dbconnect
	 * @return
	 * @throws Exception
	 */
	public boolean update(LfDBConnect dbconnect) throws Exception
	{

		// 结果
		boolean updateok = false;
		try
		{
			// 数据库字符串
			String conStr = this.createConnUrl(dbconnect);
			// 设置数据库字符串
			dbconnect.setConStr(conStr);
			// 执行结果并返回结果
			updateok = empDao.update(dbconnect);
		} catch (Exception e)
		{
			//结果设置否，抛出异常
			updateok = false;
			EmpExecutionContext.error(e, "更新数据源异常。");
			throw e;
		}
		//返回结果
		return updateok;
	}

	/**
	 * 
	 * @param dbId
	 * @return
	 * @throws Exception
	 */
	public LfDBConnect getDBConnectById(Long dbId) throws Exception
	{
		if (dbId == null)
		{
			// 如果id为空，则返回空
			return null;
		}
		// 数据库连接对象
		LfDBConnect dbConnect = null;

		try
		{
			// 从数据库获取数据库连接对象
			dbConnect = (LfDBConnect) empDao.findObjectByID(LfDBConnect.class,
					dbId);

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过Id获取数据源异常。");
			//抛出异常
			throw e;
		}
		// 返回对象
		return dbConnect;
	}

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<LfDBConnect> getAllDBConnects(String corpCode) throws Exception
	{
		// 数据库连接对象集合
		List<LfDBConnect> connectsList = null;
		// 条件
		LinkedHashMap<String, String> con = new LinkedHashMap<String, String>();
		try
		{
			// 设置条件
			con.put("corpCode", corpCode);
			// 按条件从数据库查询
			connectsList = empDao.findListByCondition(LfDBConnect.class, con,
					null);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取所有数据源异常");
			//抛出异常
			throw e;
		}
		// 返回结果
		return connectsList;
	}

	/**
	 * 
	 * @param info
	 * @return
	 * @throws Exception
	 */
	public boolean[] getDBInfo(String[] info) throws Exception
	{
		// 保存结果的数组
		boolean[] result = new boolean[2];
		// 查询条件
		LinkedHashMap<String, String> conditionMap = null;
		try
		{
			// 条件1不为空则判断
			if (info[0] != null && !"".equals(info[0]))
			{
				conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("dbconIP", info[0]);
				// 获取数据库对象
				List<LfDBConnect> dbcon = empDao.findListByCondition(
						LfDBConnect.class, conditionMap, null);
				if (dbcon == null || dbcon.size() == 0)
				{
					// 没获取到该对象则返回true
					result[0] = true;
				}
			} else
			{
				result[0] = false;
			}

			if (info[1] != null && !"".equals(info[1]))
			{
				//查询条件
				conditionMap = new LinkedHashMap<String, String>();
				//以数据库名称查询
				conditionMap.put("dbconName", info[1]);
				//执行插叙，获取对象集合
				List<LfDBConnect> dbcon = empDao.findListByCondition(
						LfDBConnect.class, conditionMap, null);
				if (dbcon == null || dbcon.size() == 0)
				{
					//没记录则返回true
					result[1] = true;
				}
			} else
			{
				//有记录返回false
				result[1] = false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取数据源信息异常。");
			//抛异常
			throw e;
		}
		return result;

	}

	/**
	 * 
	 * @param dbConnect
	 * @return
	 * @throws Exception
	 */
	public boolean testConnection(LfDBConnect dbConnect) throws Exception
	{

		// 数据库连接
		Connection con = null;
		// 驱动名称
		String driverName = null;

		if (dbConnect.getDbType().equals("Oracle"))
		{
			// 如果是oracle
			driverName = "oracle.jdbc.driver.OracleDriver";
		} else if (dbConnect.getDbType().equals("Sql Server"))
		{
			// 如果是sql server
			driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else if (dbConnect.getDbType().equals("DB2"))
		{
			// 如果是db2
			driverName = "com.ibm.db2.jcc.DB2Driver";
		} else
		{
			driverName = "com.mysql.jdbc.Driver";
		}
		//设置连接字串串
		dbConnect.setConStr(this.createConnUrl(dbConnect));
		try
		{
			//设置驱动名
			Class.forName(driverName);
			con = DriverManager.getConnection(dbConnect.getConStr(), dbConnect
					.getDbUser(), dbConnect.getDbPwd());

			return true;

		} catch (Exception e)
		{
			//捕获异常记录日志
			EmpExecutionContext.error(e, "测试数据源是否可用，获取数据源连接异常。");
			return false;
		} finally
		{
			if (con != null)
			{
				try
				{
					//关闭连接
					con.close();
				} catch (SQLException e)
				{
					EmpExecutionContext.error(e, "测试数据源是否可用，关闭数据库资源异常。");
				}
			}
		}

	}

	/**
	 * 
	 * @param dbconnect
	 * @return
	 */
	private String createConnUrl(LfDBConnect dbconnect)
	{
		// 数据连接字符串
		String url = null;
		if (dbconnect.getDbType().equals("Oracle")
				&& dbconnect.getDbconnType() == 0)
		{
			// 设置oracle数据库的连接字符串
			url = "jdbc:oracle:thin:@(description=(address=(host="
					+ dbconnect.getDbconIP() + ")(protocol=tcp)(port="
					+ dbconnect.getPort() + "))(connect_data=(service_name="
					+ dbconnect.getDbName() + ")))";

		} else if (dbconnect.getDbType().equals("Oracle")
				&& dbconnect.getDbconnType() == 1)
		{
			//type为1时设置
			url = "jdbc:oracle:thin:@" + dbconnect.getDbconIP() + ":"
					+ dbconnect.getPort() + ":" + dbconnect.getDbName();
		} else if (dbconnect.getDbType().equals("Sql Server"))
		{
			// 设置sql server的数据库连接字符串
			url = "jdbc:sqlserver://" + dbconnect.getDbconIP() + ":"
					+ dbconnect.getPort() + ";databaseName="
					+ dbconnect.getDbName();
		} else if (dbconnect.getDbType().equals("Mysql"))
		{
			// 设置mysql数据库连接字符串
			url = "jdbc:mysql://" + dbconnect.getDbconIP() + ":"
					+ dbconnect.getPort() + "/" + dbconnect.getDbName();
		} else if (dbconnect.getDbType().equals("DB2"))
		{
			//设置db2数据库连接字符串
			url = "jdbc:db2://" + dbconnect.getDbconIP() + ":"
					+ dbconnect.getPort() + "/" + dbconnect.getDbName();
		}
		// 返回结果
		return url;
	}

	/**
	 * 
	 * @param dbconName
	 * @return
	 * @throws Exception
	 */
	public boolean validateDBExists(String dbconName) throws Exception
	{
		// 验证结果
		boolean validate = false;
		// 数据库连接对象集合
		List<LfDBConnect> dbcons = new ArrayList<LfDBConnect>();
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
			conditionMap.put("dbconName", dbconName);
			// 按查询结果查询数据库记录
			dbcons = empDao.findListBySymbolsCondition(LfDBConnect.class,
					conditionMap, null);
			if (dbcons.size() > 0)
			{
				// 数据库有值则返回true
				validate = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "验证数据源是否存在异常。");
			throw e;
		}
		return validate;
	}

	/**
	 * 
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfDBConnect> getAllDbconByPage(PageInfo pageInfo)
			throws Exception
	{
		// 数据库连接集合
		List<LfDBConnect> dbcons = new ArrayList<LfDBConnect>();
		// 排序条件
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			// 按id倒序
			orderbyMap.put("dbId", "desc");
			// 查询数据库
			dbcons = empDao.findPageListByCondition(null, LfDBConnect.class,
					null, orderbyMap, pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "分页获取所有数据源异常。");
			//异常抛出
			throw e;
		}
		// 返回对象
		return dbcons;
	}

	/**
	 * 
	 * @param corpCode
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfDBConnect> getAllDbconByPage(String corpCode,
			PageInfo pageInfo) throws Exception
	{
		// 数据库连接集合
		List<LfDBConnect> dbcons = new ArrayList<LfDBConnect>();
		// 条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 排序条件
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try
		{
			//按机构编码查询条件
			conditionMap.put("corpCode", corpCode);
			//按id倒序
			orderbyMap.put("dbId", "desc");
			// 查询数据库
			dbcons = empDao.findPageListByCondition(null, LfDBConnect.class,
					conditionMap, orderbyMap, pageInfo);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "分页获取企业下所有数据源异常。");
			throw e;
		}
		// 返回对象
		return dbcons;
	}

}
