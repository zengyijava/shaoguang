package com.montnets.emp.database;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.biz.PwdEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.datasource.servlet.OrderedProperties;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.util.TxtFileUtil;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 下午06:39:36
 * @description
 */
public final class ConnectionManagerImp implements IConnectionManager {

	public Connection getDBConnection(String poolName) {
		
		Connection conn = null;
		PwdEncryptOrDecrypt pwd=new PwdEncryptOrDecrypt();
		ResourceBundle rb = ResourceBundle.getBundle("SystemGlobals");
		String connectDataBaseFlag=rb.getString("ConnectDataBaseFlag");
		//增加配置判断，当第一次加载时候，不连接数据库
		if("0".equals(connectDataBaseFlag)){
			EmpExecutionContext.debug("**********进入配置文件页面**********");
			return null;
		}else{
			//先判断是否是新密码规则
			String password=rb.getString("montnets.emp.password");
			if(password!=null&&password.length()>0){
				//如果不是新密码，使用老解密方式
				if(password.length()>6&&!"YYY999".equals(password.substring(password.length()-6))){
					AuthenAtom aa=new AuthenAtom();
					String str2 = aa.parseCode(password);
					//如果使用旧密码能够测试通过，对旧的密码加密，否则不加密（不影响原来用户原来的密码）
					if(testContest(str2)){
						//然后使用新密码规则保存到文件中。
						String newpwd=pwd.encrypt(str2);
						setProperty("montnets.emp.password",newpwd);
						//同时赋值给静态变量，用于数据重新获得新的密码
						StaticValue.setDbPasswd(newpwd);
					}
				}
			}
			//备用数据库密码
			String password2=rb.getString("montnets.emp.password2");
			if(password2!=null&&password2.length()>0){
				//如果大于6位的里面没有包含标识符的，加密，如果小于6位的直接加密
				if((password2.length()>=6&&!"YYY999".equals(password2.substring(password2.length()-6)))||password2.length()<6){
					//由于使用明文所以如果存在，就直接加密
					String newpwd=pwd.encrypt(password2);
					setProperty("montnets.emp.password2",newpwd);
					//同时赋值给静态变量，用于数据重新获得新的密码
					StaticValue.setBackDbPasswd(newpwd);
				}
			}
			
		}
		
		
		switch(Integer.parseInt(SystemGlobals.getValue(StaticValue.POOL_TYPE)))
		{
			case 1://c3p0连接池
			    conn = C3p0ConnManager.getInstance().getConnection(poolName);
			    break;
			case 2://boneCp连接池
				conn = BoneCpManager.getInstance().getConnection(poolName);
				break;
			case 3:
				conn = DbCpManager.getInstance().getConnection(poolName);
				break;	
			case 4://jdbc pool连接池
				conn = JdbcManager.getInstance().getConnection(poolName);
				break;
		   default:
				break;
		}
		return conn;
		
	}
	
	/**
	 * 测试数据库连接
	 * @param pwd
	 * @return
	 */
	public boolean testContest(String pwd){
		boolean connState=false;
		//测试连接是否正常
		LfDBConnect dbConn = new LfDBConnect();
		
		//数据库类型
		String dbType = SystemGlobals.getValue("DBType");
		//连接类型 ORACLE数据库有用
		String connType=SystemGlobals.getValue("montnets.emp.connType");
		//数据库IP地址：
		String dbconIp =SystemGlobals.getValue("montnets.emp.databaseIp");
		//数据库端口号
		String port = SystemGlobals.getValue("montnets.emp.databasePort");
		//数据库名称/实例名
		String dbName =SystemGlobals.getValue("montnets.emp.databaseName");
		//数据库用户名
		String dbUser=SystemGlobals.getValue("montnets.emp.user");
		//数据库密码
		String strdbType="";
		if("1".equals(dbType)){
			strdbType="Oracle";
		}else if("2".equals(dbType)){
			strdbType="Sql Server";
		}else if("3".equals(dbType)){
			strdbType="Mysql";
		}else if("4".equals(dbType)){
			strdbType="DB2";
		}
		dbConn.setDbUser(dbUser);
		dbConn.setDbPwd(pwd);
		dbConn.setDbName(dbName);
		dbConn.setPort(port);
		dbConn.setDbType(strdbType);
		dbConn.setDbconIP(dbconIp);
		if (strdbType.equals("Oracle"))
		{
			dbConn.setDbconnType(Integer.parseInt(connType));
		}
		try
		{
			connState=this.testConnection(dbConn);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"测试连接异常！");
		}
		return connState;
	}
	
	public void close(Connection conn) {
		JDBCPoolConnectionManager jpcm = JDBCPoolConnectionManager
				.getInstance();
		jpcm.returnConnection(conn);
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
	 * 修改配置文件信息
	 * @param key 配置文件的KEY
	 * @param value 配置文件的值
	 * @return 是否保存成功
	 */
	public boolean  setProperty( String key,String value){
		TxtFileUtil fileUtil=new TxtFileUtil();
		String basePath13 = fileUtil.getWebRoot();
		boolean save=true;
		OrderedProperties prop = null;// 属性集合对象 
		FileInputStream fis =null;
		FileOutputStream fos =null;
		try
		{
			prop = new OrderedProperties();
			fis = new FileInputStream(basePath13+"WEB-INF/classes/SystemGlobals.properties");
			prop.load(fis);
		   //必须先用map将所有的内容先保存,不然一更新,原来的内容都没了
	       Map<String, String> map = new HashMap<String, String>();
	       Set<Object> keySet = prop.keySet();
	       for (Object object : keySet) {
	           String k = (String) object;
	           String v = (String) prop.get(k);
	           map.put(k, v);
	       }
	       if(map!=null&&map.size()>0){
		       map.put(key, value);
		       for (java.util.Map.Entry<String, String> entry: map.entrySet()) {
		    	   prop.setProperty(entry.getKey(), entry.getValue());
		       }
			       
				// 文件输出流 
				fos = new FileOutputStream(basePath13+"WEB-INF/classes/SystemGlobals.properties"); 
				prop.store(fos, "");
	       }

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"文件不存在！");
		}// 属性文件输入流 
		finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭文件写出流失败！");
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e,"关闭文件读入流失败！");
				}
			}
			if(prop!=null){
				try
				{
					prop.clear();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "关闭配置流失败！");
				}
			}

		}
		return save;
	}

}
