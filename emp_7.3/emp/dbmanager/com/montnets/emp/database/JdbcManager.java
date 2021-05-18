package com.montnets.emp.database;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.montnets.emp.common.biz.PwdEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

public class JdbcManager {

	
	private static final String P_propFile = "SystemGlobals";

	private Map<String, String[]> proInfoMap = null;

	private static JdbcManager instance = new JdbcManager();

	private static  DataSource config = null;	
	
	private static  Integer isFirstConn = 1;//判断是否是第一次打印连接到数据库成功
	
	private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public JdbcManager() {
		getProInfo();
	}

	synchronized public static void ininPool(Set<String> poolName) {
		java.util.Iterator<String> iter = poolName.iterator();
		while (iter.hasNext()) {
			instance.createPool(iter.next());

		}
	}


	public synchronized static JdbcManager getInstance() {
		if (instance == null) {
			instance = new JdbcManager();
			EmpExecutionContext.debug("**********初始化BoneCP数据库连接池**********");
		}
		return instance;
	}


	public Connection getConnection(String poolName) {
		Connection conn = null;
		try {
			if (config == null) {
				instance.createPool(poolName);
			}else{
				conn = config.getConnection();
			}

			if( null != isFirstConn && 1 == isFirstConn)
			{
				Calendar s=Calendar.getInstance();
				System.out.println("["+format.format(s.getTime())+"] 数据库连接已成功!");
				System.out.println("["+format.format(s.getTime())+"] 智能引擎启动成功!");
				isFirstConn = 0;
			}
		} catch (Exception ex) {
			EmpExecutionContext.error(ex, "获取数据库连接异常!");
			if( null != isFirstConn && 1 == isFirstConn)
			{
				Calendar s=Calendar.getInstance();
				System.out.println("["+format.format(s.getTime())+"] 数据库连接失败!");
				System.out.println("["+format.format(s.getTime())+"] 智能引擎启动失败!");
			}
		}
		return conn;
	}

	/**
	 * 
	 */
	private void getProInfo() {

		EmpExecutionContext.debug("开始读取配置文件...");

		String[] emp = new String[26];
 		Integer databaseType = null;//用于判断数据库的类型##1 oracle 2 SQL Server2005  3 MySQL 4-DB2
		String driverClass = null;//数据库驱动
		String jdbcUrl = null;//数据库连接字符串
		String databaseIp = null;//数据库所在服务器IP地址 eg:192.169.1.215
		String databasePort = null;//数据库的端口 oracle默认为1521 sqlserver默认为1433 mysql默认为3306；DB2默认为50001
		String databaseName = null;//数据名称
		String printDataTypeStr = "当前所连接的数据库为：";//当前所连接的数据类型
		
		proInfoMap = new HashMap<String, String[]>();
		
		try {
			ResourceBundle rb = ResourceBundle.getBundle(P_propFile);
			databaseType = Integer.parseInt(rb.getString("DBType"));//获取配置文件中的数据库类型；默认为1-oracle
			databaseIp = rb.getString("montnets.emp.databaseIp");//获取配置文件中的数据库所在服务器的IP地址
			databaseName = rb.getString("montnets.emp.databaseName");//获取配置文件中的数据库名称
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
				 break;
			case 2://sqlserver2005驱动
				 printDataTypeStr +=" SqlServer数据库";
				 driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";//sqlserver2005驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"1433":rb.getString("montnets.emp.databasePort");
				 jdbcUrl = "jdbc:sqlserver://"+databaseIp+":"+databasePort+";DatabaseName="+databaseName;//sqlserver2005连接字符串
				 break;
			case 3://Mysql驱动
				 printDataTypeStr +=" MySql数据库";
				 driverClass = "com.mysql.jdbc.Driver";//mysql驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"3306":rb.getString("montnets.emp.databasePort");
				 jdbcUrl = "jdbc:mysql://"+databaseIp+":"+databasePort+"/"+databaseName+"?characterEncoding=gbk";//mysql连接字符串
				 break;
			case 4://DB2驱动
				 printDataTypeStr +=" DB2数据库";
				 driverClass = "com.ibm.db2.jcc.DB2Driver";//db2驱动
				 databasePort = "xxxx".equalsIgnoreCase(rb.getString("montnets.emp.databasePort"))?"50000":rb.getString("montnets.emp.databasePort");
				 jdbcUrl = "jdbc:db2://"+databaseIp+":"+databasePort+"/"+databaseName;//DB2连接字符串
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
			EmpExecutionContext.error("JDBC连接池,数据库名:"+databaseName+",数据库加密密码:"+ StaticValue.getDbPasswd());
			//String str2 = aa.parseCode(str);
			//解密密码
			PwdEncryptOrDecrypt pwdDecrypt=new PwdEncryptOrDecrypt();
			String str2 = pwdDecrypt.decrypt(str);
			emp[3] = str2;
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
			emp[19] = rb.getString("montnets.emp.partitionCount");
			emp[20] = rb.getString("montnets.emp.connectionTimeout");
			emp[21] = rb.getString("montnets.emp.poolAvailabilityThreshold");
			emp[22] = rb.getString("montnets.emp.releaseHelperThreads");
			emp[23] = rb.getString("montnets.emp.statementReleaseHelperThreads");
			emp[24] = rb.getString("montnets.emp.maxConnectionAge");
  			emp[25] = rb.getString("montnets.emp.idleMaxAge");
			proInfoMap.put(StaticValue.EMP_POOLNAME, emp);
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

		String[] dbInfo = new String[17];
		String driverURL = null;
		String driverName = null;
		String user = null;
		String password = null;
		int maxPoolSize = 0;
		int minPoolSize = 0;
		int initialPoolSize = 0;
		int maxIdleTime = 0;
		int acquireIncrement = 0;
		int acquireRetryAttempts = 0;
		int acquireRetryDelay = 0;
		boolean autoCommitOnClose = false;
		int checkoutTimeout = 0;
		int idleConnectionTestPeriod = 240;
		boolean testConnectionOnCheckin = false;
		boolean testConnectionOnCheckout = false;
		boolean breakAfterAcquireFailure = false;
		int maxStatements = 0;
		int maxStatementPerconnection = 100;
		int partitionCount = 30;
		int connectionTimeout = 3000;
		int poolAvailabilityThreshold  = 20;
		int releaseHelperThreads  = 3;
		int maxConnectionAge  = 0;
		int statementReleaseHelperThreads = 3;
  		
		if (proInfoMap == null) {
			getProInfo();
		}
		if (null != proInfoMap.get(poolName)
				) {
			dbInfo = (String[]) proInfoMap.get(poolName);
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
			partitionCount = Integer.parseInt(dbInfo[19]);
			connectionTimeout = Integer.parseInt(dbInfo[20]);
			poolAvailabilityThreshold  = Integer.parseInt(dbInfo[21]);
			releaseHelperThreads  = Integer.parseInt(dbInfo[22]);
			statementReleaseHelperThreads = Integer.parseInt(dbInfo[23]);
			maxConnectionAge  = Integer.parseInt(dbInfo[24]);
 		}

		try {

			config = new DataSource();

			config.setDriverClassName(driverName);
						
			config.setUrl(driverURL);
			
			config.setUsername(user);
			
			config.setPassword(password);
			
			config.setMaxActive(maxPoolSize);//设置每个分区含有connection最大个数

			config.setInitialSize(initialPoolSize);//设置每个分区含有connection最小个数
			

			/*
			 * 设置获取connection超时的时间。这个参数默认为Long.MAX_VALUE;单位：毫秒。 
			        在调用getConnection获取connection时，获取时间超过了这个参数，就视为超时并报异常。
			 */
			config.setMaxWait(connectionTimeout);
			
			//获取连接时测试连接有效性
			//config.setTestOnBorrow(true);
			
			//归还连接时测试连接有效性
			//config.setTestOnReturn(true);
			
			//空闲时测试连接有效性
			//config.setTestWhileIdle(false);
			
			//最大的空闲连接数
			//config.setMaxIdle(10);
			
			//config.setMaxWait(connectionTimeout);
			//config.setInitialPoolSize(initialPoolSize);
			//config.setMaxIdleTime(maxIdleTime);
 			//config.setIdleMaxAge(idleMaxAge);//连接池中未使用的链接最大存活时间
			//config.setPartitionCount(partitionCount);
			//config.setAcquireIncrement(acquireIncrement);//当连接池中的连接耗尽的时候 BoneCP一次同时获取的连接数 设置分区中的connection增长数量。这个参数默认为1。 当每个分区中的connection大约快用完时，BoneCP动态批量创建connection
			//config.setAcquireRetryAttempts(acquireRetryAttempts);
			//config.setAcquireRetryDelayInMs(acquireRetryDelay);
			//config.setAutoCommitOnClose(autoCommitOnClose);
			//config.setCheckoutTimeout(checkoutTimeout);
			//config.setIdleConnectionTestPeriodInSeconds(idleConnectionTestPeriod);//检查数据库连接池中空闲连接的间隔时间
			//config.setTestConnectionOnCheckin(testConnectionOnCheckin);
		/*	config.setTestConnectionOnCheckout(testConnectionOnCheckout);
			config.setBreakAfterAcquireFailure(breakAfterAcquireFailure);
			config.setMaxStatements(maxStatements);
			config.setMaxStatementsPerConnection(maxStatementPerconnection);*/
			
			
			/*
			 * 设置连接池阀值。这个参数默认为20。如果小于0或是大于100，BoneCP将设置为20。 
				连接池观察线程(PoolWatchThread)试图为每个分区维护一定数量的可用connection。 
				这个数量趋于maxConnectionPerPartition和minConnectionPerPartition之间。这个参数是以百分比的形式来计算的。例如：设置为20，下面的条件如果成立：Free Connections / MaxConnections < poolAvailabilityThreshold；就会创建出新的connection。 
				换句话来说连接池为每个分区至少维持20%数量的可用connection。 
				设置为0时，每当需要connection的时候，连接池就要重新创建新connection，这个时候可能导致应用程序可能会为了获得新connection而小等一会。 

			 */
			//config.setPoolAvailabilityThreshold(poolAvailabilityThreshold);
			
			/*
			 *  设置connection助手线程个数。这个参数默认为3。如果小于0，BoneCP将设置为3。 
				设置为0时，应用程序线程被阻塞，直到连接池执行必要地清除和回收connection，并使connection在其它线程可用。 
				设置大于0时，连接池在每个分区中创建助手线程处理回收关闭后的connection（应用程序会通过助手线程异步地将这个connection放置到一个临时队列中进行处理)。 
				对于应用程序在每个connection上处理大量工作时非常有用。可能会降低运行速度，不过在高并发的应用中会提高性能。 

			 */
			//config.setReleaseHelperThreads(releaseHelperThreads);
			
			/*
			 * 设置statement助手线程个数。这个参数默认为3。如果小于0，BoneCP将设置为3。 
				设置为0时，应用程序线程被阻塞，直到连接池或JDBC驱动程序关闭statement。 
				设置大于0时，连接池会在每个分区中创建助理线程，异步地帮助应用程序关闭statement当应用程序打开了大量的statement是非常有用的。可能会降低运行速度，不过在高并发的应用中会提高性能。
			 * 
			 */
			//config.setStatementReleaseHelperThreads(statementReleaseHelperThreads);
			
			/*
			 * 设置connection的存活时间。这个参数默认为0，单位：毫秒。设置为0该功能失效。 
			 * 通过ConnectionMaxAgeThread观察每个分区中的connection，不管connection是否空闲，如果这个connection距离创建的时间大于这个参数就会被清除。当前正在使用的connection不受影响，直到返回到连接池再做处理。
			 */
			//config.setMaxConnectionAgeInSeconds(maxConnectionAge);
			
			 
			

			EmpExecutionContext.debug("**************配置文件信息**************");
			EmpExecutionContext.debug("DriverUrl:'" + driverURL + "'\tUserName:'"
					+ user + "'\t");
			EmpExecutionContext.debug("*********创建名为：" + poolName + "的连接池成功*********");
			
			/* } */
		} catch (Exception e) {
			EmpExecutionContext.error(e, "*********创建连接池失败*********");
			
		}
		}

}
