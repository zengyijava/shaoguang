package com.montnets.emp.database;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.montnets.emp.common.biz.PwdEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-15 下午07:25:08
 */
public class C3p0ConnManager {

	
	private static final String P_propFile = "SystemGlobals";

	private Map<String, String[]> proInfoMap = null;

 
	private static C3p0ConnManager instance = new C3p0ConnManager();

	private static ComboPooledDataSource cpds = null;

	private static ComboPooledDataSource cpds2 = null;

	private static  Integer isFirstConn = 1;//判断是否是第一次打印连接到数据库成功
	
	private static Integer backup ;//判断是否使用备用服务器
	
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	C3p0ConnManager() {
		getProInfo();
	}

	synchronized public static void ininPool(Set<String> poolName) {
		java.util.Iterator<String> iter = poolName.iterator();
		while (iter.hasNext()) {
			instance.createPool(iter.next());

		}
	}


	public synchronized static C3p0ConnManager getInstance() {
		if (instance == null) {
			instance = new C3p0ConnManager();
			EmpExecutionContext.debug("**********初始化C3P0数据库连接池**********");
		}
		return instance;
	}


	public synchronized Connection getConnection(String poolName) {
		Connection conn = null;
		try {
			if (cpds == null) {
				instance.createPool(poolName);
			}
			
			if( StaticValue.EMP_BACKUP.equals(poolName) && backup ==1)
			{
				conn = cpds2.getConnection();
			}else if(cpds != null){
				conn = cpds.getConnection();
			}
			if( null != isFirstConn && 1 == isFirstConn)
			{
				Calendar s=Calendar.getInstance();
				System.out.println("["+format.format(s.getTime())+"] 数据库连接已成功!");
				System.out.println("["+format.format(s.getTime())+"] 智能引擎启动成功!");
				isFirstConn = 0;
			}
		} catch (Exception ex) {
			EmpExecutionContext.error("连接异常!");
			Calendar s=Calendar.getInstance();
			System.out.println("["+format.format(s.getTime())+"] 数据库连接失败!");
			System.out.println("["+format.format(s.getTime())+"] 智能引擎启动失败!");
		}
		return conn;
	}

	/**
	 * 
	 */
	private void getProInfo() {

		EmpExecutionContext.debug("开始读取配置文件...");

		String[] emp = new String[19];
		String[] backupemp = new String[19];
 		Integer databaseType = null;//用于判断数据库的类型##1 oracle 2 SQL Server2005  3 MySQL 4-DB2
		String driverClass = null;//数据库驱动
		String jdbcUrl = null;//数据库连接字符串
		String databaseIp = null;//数据库所在服务器IP地址 eg:192.169.1.215
		String databasePort = null;//数据库的端口 oracle默认为1521 sqlserver默认为1433 mysql默认为3306；DB2默认为50001
		String databaseName = null;//数据名称
		
		//备用服务器
		String jdbcUrl2 = null;//数据库连接字符串
		String databaseIp2 = null;//数据库所在服务器IP地址 eg:192.169.1.215
		String databasePort2 = null;//数据库的端口 oracle默认为1521 sqlserver默认为1433 mysql默认为3306；DB2默认为50001
		String databaseName2 = null;//数据名称
		
		String printDataTypeStr = "当前所连接的数据库为：";//当前所连接的数据类型
		
		proInfoMap = new HashMap<String, String[]>();
 
		try {
			ResourceBundle rb = ResourceBundle.getBundle(P_propFile);
			databaseType = Integer.parseInt(rb.getString("DBType"));//获取配置文件中的数据库类型；默认为1-oracle
			databaseIp = rb.getString("montnets.emp.databaseIp");//获取配置文件中的数据库所在服务器的IP地址
			databaseName = rb.getString("montnets.emp.databaseName");//获取配置文件中的数据库名称
			
			try{
				backup = Integer.parseInt(rb.getString("montnets.emp.use_backup_server"));
			}catch(java.util.MissingResourceException resourceException)
			{
				EmpExecutionContext.error("获取备份服务数据异常!");
				backup = 0;
			}

			 if( 1 == backup )
			 {
				//备用服务器
				databaseIp2 = rb.getString("montnets.emp.databaseIp2");//获取配置文件中的数据库所在服务器的IP地址
				databaseName2 = rb.getString("montnets.emp.databaseName2");//获取配置文件中的数据库名称
			 }

			switch(databaseType)
			{
			case 1://oracle数据库
				 printDataTypeStr +=" oracle数据库";
				 driverClass = "oracle.jdbc.driver.OracleDriver";//oracle驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"1521":rb.getString("montnets.emp.databasePort");
				 //连接类型
				 String connType=rb.getString("montnets.emp.connType");
				 if("1".equals(connType)){
					 //SID 实例名方式
					 jdbcUrl = "jdbc:oracle:thin:@"+databaseIp+":"+databasePort+":"+databaseName;//oracle连接字符串
				 }else if("0".equals(connType)){
					 //Service Name 服务名方式
					 jdbcUrl = "jdbc:oracle:thin:@//"+databaseIp+":"+databasePort+"/"+databaseName;//oracle连接字符串
				 }
				 
				 if( 1 == backup )
				 {
					 databasePort2 = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort2"))?"1521":rb.getString("montnets.emp.databasePort2");
					 jdbcUrl2 = "jdbc:oracle:thin:@"+databaseIp2+":"+databasePort2+":"+databaseName2;//oracle连接字符串
				 }
				 break;
			case 2://sqlserver2005驱动
				 printDataTypeStr +=" SqlServer数据库";
				 driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//sqlserver2005驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"1433":rb.getString("montnets.emp.databasePort");
				 jdbcUrl = "jdbc:sqlserver://"+databaseIp+":"+databasePort+";DatabaseName="+databaseName;//sqlserver2005连接字符串
				
				 if( 1 == backup )
				 {
					 databasePort2 = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort2"))?"1433":rb.getString("montnets.emp.databasePort2");
					 jdbcUrl2 = "jdbc:sqlserver://"+databaseIp2+":"+databasePort2+";DatabaseName="+databaseName2;//sqlserver2005连接字符串
				 } 
				 break;
			case 3://Mysql驱动
				 printDataTypeStr +=" MySql数据库";
				 driverClass = "com.mysql.jdbc.Driver";//mysql驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"3306":rb.getString("montnets.emp.databasePort");
				 jdbcUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/"+databaseName+"?characterEncoding=utf8";//mysql连接字符串
				 
				 if( 1 == backup )
				 {
					 databasePort2 = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort2"))?"3306":rb.getString("montnets.emp.databasePort2");
					 jdbcUrl2 = "jdbc:mysql://"+databaseIp2+":"+databasePort2+"/"+databaseName2+"?characterEncoding=gbk";//mysql连接字符串
				  }
				 break;
			case 4://DB2驱动
				 printDataTypeStr +=" DB2数据库";
				 driverClass = "com.ibm.db2.jcc.DB2Driver";//db2驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"50000":rb.getString("montnets.emp.databasePort");
				 jdbcUrl = "jdbc:db2://"+databaseIp+":"+databasePort+"/"+databaseName;//DB2连接字符串
				 //jdbcUrl = "jdbc:db2://"+databaseIp+":"+databasePort+"/"+databaseName+":currentSchema=ADMINISTRATOR;";//DB2连接字符串
				 
				 
				 if( 1 == backup )
				 {
					 databasePort2 = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort2"))?"50000":rb.getString("montnets.emp.databasePort2");
					 jdbcUrl2 = "jdbc:db2://"+databaseIp2+":"+databasePort2+"/"+databaseName2;//DB2连接字符串
				 }
				 break;
			default:break;
			}
			//System.out.println("DATABASETYPE:"+databaseType+"\tDriverClass="+driverClass+"\tjdbcUrl="+jdbcUrl);
			emp[0] = jdbcUrl;
			emp[1] = driverClass;
			emp[2] = rb.getString("montnets.emp.user");
			//解密处理
			//AuthenAtom aa=new AuthenAtom();
			String str= StaticValue.getDbPasswd();
			EmpExecutionContext.error("C3P0连接池,数据库名:"+databaseName+",数据库加密密码:"+ StaticValue.getDbPasswd());
			//String str2 = aa.parseCode(str);
			//解密密码
			PwdEncryptOrDecrypt pwdDecrypt=new PwdEncryptOrDecrypt();
			String str2 = pwdDecrypt.decrypt(str);
			emp[3] = str2;
//			emp[3] = rb.getString("montnets.emp.password");
			emp[4] = rb.getString("montnets.emp.maxPoolSize");
			emp[5] = rb.getString("montnets.emp.minPoolSize");
			emp[6] = rb.getString("montnets.emp.InitialPoolSize");
			emp[7] = rb.getString("montnets.emp.MaxIdleTime");
			emp[8] = rb.getString("montnets.emp.AcquireIncrement");
			emp[9] = rb.getString("montnets.emp.AcquireRetryAttempts");
			emp[10] = rb.getString("montnets.emp.AcquireRetryDelay");
			emp[11] = rb.getString("montnets.emp.AutoCommitOnClose");
			emp[12] = rb.getString("montnets.emp.CheckoutTimeout");
			emp[13] = rb.getString("montnets.emp.IdleConnectionTestPeriod");
			emp[14] = rb.getString("montnets.emp.TestConnectionOnCheckin");
			emp[15] = rb.getString("montnets.emp.TestConnectionOnCheckout");
			emp[16] = rb.getString("montnets.emp.BreakAfterAcquireFailure");
			emp[17] = rb.getString("montnets.emp.MaxStatements");
			emp[18] = rb.getString("montnets.emp.MaxStatementsPerConnection");
			proInfoMap.put(StaticValue.EMP_POOLNAME, emp);
			
			
			if( 1 == backup )
			{
				backupemp[0] = jdbcUrl2;
				backupemp[1] = emp[1];
				backupemp[2] = rb.getString("montnets.emp.user2");
				//解密处理
				backupemp[3] = pwdDecrypt.decrypt(StaticValue.getBackDbPasswd());
				EmpExecutionContext.error("C3P0连接池,备用数据库名:"+databaseName2+",备用数据库加密密码:"+ StaticValue.getBackDbPasswd());
//				backupemp[3] = rb.getString("montnets.emp.password2");
				backupemp[4] = emp[4];
				backupemp[5] = emp[5];
				backupemp[6] = emp[6];
				backupemp[7] = emp[7];
				backupemp[8] = emp[8];
				backupemp[9] = emp[9];
				backupemp[10] = emp[10];
				backupemp[11] = emp[11];
				backupemp[12] = emp[12];
				backupemp[13] = emp[13];
				backupemp[14] = emp[14];
				backupemp[15] = emp[15];
				backupemp[16] = emp[16];
				backupemp[17] = emp[17];
				backupemp[18] = emp[18];
				  
				proInfoMap.put(StaticValue.EMP_BACKUP, backupemp);
			}
			Calendar s=Calendar.getInstance();
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("["+format2.format(s.getTime())+"] "+printDataTypeStr);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "读取配置文件异常。");
		}
		EmpExecutionContext.debug("读取配置文件成功！");

	}

	/**
	 * 
	 * @param poolName
	 */
	private void createPool(String poolName) {

		String[] dbInfo = new String[19];
		String[] dbInfo2 = new String[19];

		String driverURL = null;
		String driverName = null;
		String user = null;
		String password = null;
		
		
		String driverURL2 = null;
		String driverName2 = null;
		String user2 = null;
		String password2 = null;
		
		int maxPoolSize = 0;
		int minPoolSize = 0;
		int initialPoolSize = 0;
		int maxIdleTime = 0;
		int acquireIncrement = 0;
		int acquireRetryAttempts = 0;
		int acquireRetryDelay = 0;
		boolean autoCommitOnClose = false;
		int checkoutTimeout = 0;
		int idleConnectionTestPeriod = 0;
		boolean testConnectionOnCheckin = false;
		boolean testConnectionOnCheckout = false;
		boolean breakAfterAcquireFailure = false;
		int maxStatements = 0;
		int maxStatementPerconnection = 100;
		if (proInfoMap == null ) {
			getProInfo();
		}
		
		
		if (null != proInfoMap.get(poolName)
				&& !"".equals(proInfoMap.get(poolName)+"")) {
			
			 
				dbInfo = (String[]) proInfoMap.get(StaticValue.EMP_POOLNAME);

				driverURL = dbInfo[0];
				driverName = dbInfo[1];
				user = dbInfo[2];
				password = dbInfo[3];
				maxPoolSize = Integer.parseInt(dbInfo[4]);
				minPoolSize = Integer.parseInt(dbInfo[5]);
				initialPoolSize = Integer.parseInt(dbInfo[6]);
				maxIdleTime = Integer.parseInt(dbInfo[7]);
				acquireIncrement = Integer.parseInt(dbInfo[8]);
				acquireRetryAttempts = Integer.parseInt(dbInfo[9]);
				acquireRetryDelay = Integer.parseInt(dbInfo[10]);
				autoCommitOnClose = Boolean.valueOf(dbInfo[11]);
				checkoutTimeout = Integer.parseInt(dbInfo[12]);
				idleConnectionTestPeriod = Integer.parseInt(dbInfo[13]);
				testConnectionOnCheckin = Boolean.valueOf(dbInfo[14]);
				testConnectionOnCheckout = Boolean.valueOf(dbInfo[15]);
				breakAfterAcquireFailure = Boolean.valueOf(dbInfo[16]);
				maxStatements = Integer.parseInt(dbInfo[17]);
				maxStatementPerconnection = Integer.parseInt(dbInfo[18]);
			 
				
				if( 1 == backup )
				{
					dbInfo2 = (String[]) proInfoMap.get(StaticValue.EMP_BACKUP);
	
					driverURL2 = dbInfo2[0];
					driverName2 = dbInfo2[1];
					user2 = dbInfo2[2];
					password2 = dbInfo2[3];
					maxPoolSize = Integer.parseInt(dbInfo2[4]);
					minPoolSize = Integer.parseInt(dbInfo2[5]);
					initialPoolSize = Integer.parseInt(dbInfo2[6]);
					maxIdleTime = Integer.parseInt(dbInfo2[7]);
					acquireIncrement = Integer.parseInt(dbInfo2[8]);
					acquireRetryAttempts = Integer.parseInt(dbInfo2[9]);
					acquireRetryDelay = Integer.parseInt(dbInfo2[10]);
					autoCommitOnClose = Boolean.valueOf(dbInfo2[11]);
					checkoutTimeout = Integer.parseInt(dbInfo2[12]);
					idleConnectionTestPeriod = Integer.parseInt(dbInfo2[13]);
					testConnectionOnCheckin = Boolean.valueOf(dbInfo2[14]);
					testConnectionOnCheckout = Boolean.valueOf(dbInfo2[15]);
					breakAfterAcquireFailure = Boolean.valueOf(dbInfo2[16]);
					maxStatements = Integer.parseInt(dbInfo2[17]);
					maxStatementPerconnection = Integer.parseInt(dbInfo2[18]);
			 
				}

			
		
		}

		try {
			cpds = new ComboPooledDataSource();

			cpds.setDriverClass(driverName);
			cpds.setJdbcUrl(driverURL);
			cpds.setUser(user);
			cpds.setPassword(password);
			cpds.setMaxPoolSize(maxPoolSize);
			cpds.setMinPoolSize(minPoolSize);
			cpds.setInitialPoolSize(initialPoolSize);
			cpds.setMaxIdleTime(maxIdleTime);
			cpds.setAcquireIncrement(acquireIncrement);
			cpds.setAcquireRetryAttempts(acquireRetryAttempts);
			cpds.setAcquireRetryDelay(acquireRetryDelay);
			cpds.setAutoCommitOnClose(autoCommitOnClose);
			cpds.setCheckoutTimeout(checkoutTimeout);
			cpds.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
			cpds.setTestConnectionOnCheckin(testConnectionOnCheckin);
			cpds.setTestConnectionOnCheckout(testConnectionOnCheckout);
			cpds.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
            cpds.setMaxStatements(maxStatements);
            cpds.setMaxStatementsPerConnection(maxStatementPerconnection);
 			
            
            if ( 1 == backup )
            {
	            cpds2 = new ComboPooledDataSource();
	
				cpds2.setDriverClass(driverName2);
				cpds2.setJdbcUrl(driverURL2);
				cpds2.setUser(user2);
				cpds2.setPassword(password2);
				cpds2.setMaxPoolSize(maxPoolSize);
				cpds2.setMinPoolSize(minPoolSize);
				cpds2.setInitialPoolSize(initialPoolSize);
				cpds2.setMaxIdleTime(maxIdleTime);
				cpds2.setAcquireIncrement(acquireIncrement);
				cpds2.setAcquireRetryAttempts(acquireRetryAttempts);
				cpds2.setAcquireRetryDelay(acquireRetryDelay);
				cpds2.setAutoCommitOnClose(autoCommitOnClose);
				cpds2.setCheckoutTimeout(checkoutTimeout);
				cpds2.setIdleConnectionTestPeriod(idleConnectionTestPeriod);
				cpds2.setTestConnectionOnCheckin(testConnectionOnCheckin);
				cpds2.setTestConnectionOnCheckout(testConnectionOnCheckout);
				cpds2.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
	            cpds2.setMaxStatements(maxStatements);
	            cpds2.setMaxStatementsPerConnection(maxStatementPerconnection);
            
            
            }

            
			EmpExecutionContext.debug("**************配置文件信息**************");
			EmpExecutionContext.debug("DriverUrl:'" + driverURL + "'\tUserName:'"
					+ user + "'\t");
			//EmpExecutionContext.debug("*********创建名为：" + poolName + "的连接池成功*********");
			
			/* } */
		} catch (Exception e) {
			EmpExecutionContext.debug("*********创建连接池失败*********");
			
		}

	}
}
