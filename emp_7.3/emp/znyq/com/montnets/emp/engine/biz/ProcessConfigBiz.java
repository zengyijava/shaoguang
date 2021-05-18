package com.montnets.emp.engine.biz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import com.montnets.emp.ottbase.util.StringUtils;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ReviewBiz;
import com.montnets.emp.common.biz.ReviewRemindBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.engine.dao.ProcessDao;
import com.montnets.emp.entity.approveflow.LfFlowRecord;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.entity.engine.LfMoService;
import com.montnets.emp.entity.engine.LfProCon;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.engine.LfReply;
import com.montnets.emp.entity.engine.LfService;
import com.montnets.emp.entity.sysuser.LfFlow;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.entity.template.LfTmplRela;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

/**
 * 
 * @author Administrator
 *
 */
public class ProcessConfigBiz
{
	//数据库操作dao
	private IEmpDAO empDao;
	//数据库操作dao，用于事务
	private IEmpTransactionDAO empTransDao;
	
	private SmsBiz smsBiz = new SmsBiz();
	
	private BaseBiz baseBiz = new BaseBiz();
	
	public ProcessConfigBiz(){
		//初始化数据库操作dao
		empDao=new DataAccessDriver().getEmpDAO();
		//初始化数据库操作dao，用于事务
		empTransDao=new DataAccessDriver().getEmpTransDAO();
	}
	
	/**
	 * 根据业务id，删除步骤
	 * @param conn
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public boolean deleProcessBySerId(Connection conn, Long serId) throws Exception {
		//业务id不能为空
		if(serId == null){
			return false;
		}
		//结果
		boolean result = false;

		try{
			//步骤对象集合
			List<LfProcess> processList = this.getProcescBySerId(serId);
			//无步骤
			if(processList == null || processList.size() == 0){
				return false;
			}
			//步骤对象
			LfProcess process = null;
			//循环处理步骤集合
			for(int i = 0;i<processList.size();i++){
				//取出每条步骤对象
				process = processList.get(i);
				//删除步骤
				this.delAllAboutProcess(process.getPrId());
			}
			//结果为成功
			result = true;
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据业务id，删除步骤异常。");
			throw e;
		}
		//返回结果
		return result;
	}

	/**
	 * 删除所有步骤相关的关联关系
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public boolean delAllAboutProcess(Long prId) throws Exception {
		//步骤id不能为空
		if(prId == null){
			return false;
		}
		//获取连接
		Connection conn = empTransDao.getConnection();
		try{
			//开启事务
			empTransDao.beginTransaction(conn);
			//删除回复条件
			this.delProConByPrId(conn, prId);
			//删除回复模板
			this.delReplyByPrId(conn, prId);
			//删除步骤
			this.deleteProcess(conn, prId);
			//提交事务
			empTransDao.commitTransaction(conn);
		}catch(Exception e){
			//异常处理
			//回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "删除所有步骤相关的关联关系异常。");
			throw e;
		}finally{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return true;
	}
	
	/**
	 * 根据步骤id删除步骤
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	private boolean deleteProcess(Connection conn, Long prId) throws Exception {
		//结果
		boolean result = false;
		try{
			//删除步骤
			int delnum = empTransDao.delete(conn, LfProcess.class, prId.toString());
			//删除记录数大于0
			if(delnum>0){
				//删除成功
				result = true;
				//返回结果
				return result;
			}
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据步骤id删除步骤异常。");
 			throw e;
		} 
		//删除失败，返回结果
		return result;
	}
	
	/**
	 * 根据业务id获取步骤集合
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public List<LfProcess> getProcescBySerId(Long serId) {
		//服务id不能为空
		if(serId == null){
			return null;
		}
		//步骤对象集合
		List<LfProcess> processList;
		
		try{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
			//排序
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			conditionMap.put("serId", serId.toString());
			orderbyMap.put("prId", "asc");
			//按条件查询
			processList = empDao.findListByCondition(LfProcess.class, conditionMap, orderbyMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据业务id获取步骤集合异常。");
			return null;
		}
		//返回结果
		return processList;
	}
	
	/**
	 * 更新业务记录
	 * @param conn
	 * @param serId
	 * @param msgSeparated
	 * @return
	 * @throws Exception
	 */
	public boolean updateProcessMsgSeparated(Connection conn, Long serId, String msgSeparated) throws Exception {
		//业务id不能为空
		if(serId == null){
			return false;
		}
		//结果
		boolean result = false;
		//更新对象
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			objectMap.put("msgSeparated", msgSeparated);
			conditionMap.put("serId", serId.toString());
			//执行更新
			result = empTransDao.update(conn, LfProcess.class, objectMap, conditionMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "更新业务记录异常。");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 根据业务id获取步骤集合
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public List<LfProcess> getProcescBySerIdOrderByPrNo(Long serId) throws Exception {
		//服务id不能为空
		if(serId == null){
			return null;
		}
		//步骤对象集合
		List<LfProcess> processList;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//排序
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("serId", serId.toString());
			orderbyMap.put("prNo", "asc");
			//按条件查询
			processList = empDao.findListByCondition(LfProcess.class, conditionMap, orderbyMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据业务id获取步骤集合异常。");
			throw e;
		}
		//返回结果
		return processList;
	}
	
	/**
	 * 获取未运行的步骤
	 * @param serId
	 * @param prNo
	 * @return
	 * @throws Exception
	 */
	public List<LfProcess> getProcescNoRun(Long serId, Integer prNo) throws Exception {
		//服务id
		if(serId == null){
			return null;
		}
		//步骤集合
		List<LfProcess> processList;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
		//排序
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("serId", serId.toString());
			conditionMap.put("prNo&>=", prNo.toString());
			orderbyMap.put("prNo", "asc");
			//按条件执行
			processList = empDao.findListBySymbolsCondition(LfProcess.class, conditionMap, orderbyMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "获取未运行的步骤异常。");
			throw e;
		}
		//返回结果
		return processList;
	}
	
	/**
	 * 
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public LfProcess getPorcessByPrId(Long prId) throws Exception {
		//获取步骤对象
		LfProcess process = (LfProcess)empDao.findObjectByID(LfProcess.class, prId);
		//返回步骤对象
		return process;
	}
	
	/**
	 * 获取select步骤
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public List<LfProcess> getSelectProcess(Long serId) throws Exception {
		//服务id不能为空
		if(serId == null){
			return null;
		}
		//步骤对象集合
		List<LfProcess> processList;
		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//排序
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("serId", serId.toString());
			conditionMap.put("prType", "4");
			orderbyMap.put("prId", "asc");
			//按条件查询
			processList = empDao.findListByCondition(LfProcess.class, conditionMap, orderbyMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "获取select步骤异常。");
			throw e;
		}
		//返回结果
		return processList;
	}
	
	/**
	 * 获取select步骤
	 * @param serId
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public List<LfProcess> getSelectProcess(Long serId,Long prId) throws Exception {
		//业务id不能为空
		if(serId == null){
			return null;
		}
		//步骤对象集合
		List<LfProcess> processList;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//排序
		LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("serId", serId.toString());
			conditionMap.put("prType&<", "5");
			conditionMap.put("prId&<", prId.toString());
			orderbyMap.put("prId", "asc");
			//按条件查询
			processList = empDao.findListBySymbolsCondition(LfProcess.class, conditionMap, orderbyMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "获取select步骤异常。");
			throw e;
		}
		//返回结果
		return processList;
	}
	
	/**
	 * 根据步骤id获取回复步骤
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public LfReply getReplyByPrId(Long prId) {
		//步骤id不能为空
		if(prId == null){
			return null;
		}
		//回复模板对象
		LfReply reply = null;
		
		try{
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("prId", prId.toString());
			//按条件查询
			List<LfReply> replyList = empDao.findListByCondition(LfReply.class, conditionMap, null);
			//有记录
			if(replyList != null && replyList.size() > 0){
				//取第一条
				reply = replyList.get(0);
			}
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据步骤id获取回复步骤异常。");
			return null;
		}
		//返回对象
		return reply;
	}
	
	/**
	 * 新增回复步骤
	 * @param reply
	 * @return
	 * @throws Exception
	 */
	public boolean addReply(LfReply reply) throws Exception {
		//结果
		boolean result = false;
		try{
			//保存记录
			result = empDao.save(reply);
		}catch(Exception e){
			//异常处理
			result = false;
			EmpExecutionContext.error(e, "新增回复步骤异常。");
 			throw e;
		} 
		//返回结果
		return result;
	}
	
	/**
	 * 更新回复步骤
	 * @param reply
	 * @return
	 * @throws Exception
	 */
	public boolean updateReply(LfReply reply) throws Exception {
		//结果
		boolean result = false;
		try{
			//更新
			result = empDao.update(reply);
		}catch(Exception e){
			//异常处理
			result = false;
			EmpExecutionContext.error(e, "更新回复步骤异常。");
 			throw e;
		} 
		//返回结果
		return result;
	}
	
	/**
	 * 删除回复步骤
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public Integer delReplyByPrId(Long prId) throws Exception {
		//结果
		Integer result = 0;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("prId", prId.toString());
			//执行删除
			result = empDao.delete(LfReply.class, conditionMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "删除回复步骤异常。");
			result=0;
 			throw e;
		} 
		//返回结果
		return result;
	}
	
	/**
	 * 根据步骤id删除回复步骤
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	private Integer delReplyByPrId(Connection conn, Long prId) throws Exception {
		//结果数
		Integer result = 0;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("prId", prId.toString());
			//执行删除
			result = empTransDao.delete(conn, LfReply.class, conditionMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据步骤id删除回复步骤异常。");
			result=0;
 			throw e;
		} 
		//返回结果
		return result;
	}
	
	/**
	 * 判断指令代码是否已存在
	 * @param proCode
	 * @return
	 * @throws Exception
	 */
	public boolean isProCodeExists(String proCode) throws Exception {
		//结果
		boolean result = true;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("proCode", proCode);
			//按条件查询
			List<LfProcess> processList = empDao.findListByCondition(LfProcess.class, conditionMap, null);
			//没记录
			if(processList == null || processList.size() == 0){
				result = false;
			}
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "判断指令代码是否已存在异常。");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public LfProcess getProcessByProCode(String msg) throws Exception {
		//获取所有的步骤对象集合
		List<LfProcess> processList = empDao.findListByCondition(LfProcess.class, null, null);
		//步骤对象
		LfProcess process = null;
		//临时对象
		LfProcess tempProcess = null;
		//循环处理步骤对象集合
		for(int i = 0; i < processList.size(); i++){
			//获取步骤对象
			tempProcess = processList.get(i);
			if(tempProcess.getProCode() == null){
				continue;
			}
			if(tempProcess.getProCode() != null && tempProcess.getProCode().equals(msg)){
				process = tempProcess;
				break;
			}
			//短信内容分隔符为空
			if(tempProcess.getMsgSeparated() == null){
				//跳出，进入下一循环
				continue;
			}
			
			if(tempProcess.getProCode() != null && msg.indexOf(tempProcess.getMsgSeparated()) == -1 && !msg.equals(tempProcess.getProCode())){
				continue;
			}
			//获取指令代码
			String msgOrderCode = msg;
			
			//截取指令代码
			if(!msg.equals(tempProcess.getProCode())){
				msgOrderCode = msg.substring(0, msg.indexOf(tempProcess.getMsgSeparated()));
			}
			
			if(msgOrderCode.equals(tempProcess.getProCode())){
				process = tempProcess;
				break;
			}
		}
		//返回步骤
		return process;
	}
	
	/**
	 * 获取步骤条件
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public List<LfProCon> getProCons(Long prId) throws Exception {
		//步骤id
		if(prId == null){
			return null;
		}
		//步骤对象集合
		List<LfProCon> proconsList;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("prId", prId.toString());
			//按条件查询
			proconsList = empDao.findListByCondition(LfProCon.class, conditionMap, null);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "获取步骤条件异常。");
			throw e;
		}
		//返回结果
		return proconsList;
	}
	
	/**
	 * 根据业务id删除上行步骤
	 * @param conn
	 * @param serId
	 * @return
	 * @throws Exception
	 */
	public boolean delUpProcessBySerId(Connection conn, Long serId) throws Exception {
		//业务id不为空
		if(serId == null){
			return false;
		}
		boolean result = false;
		try{
			//步骤对象集合
			List<LfProcess> processList = this.getProcescBySerId(serId);
			//无记录则返回
			if(processList == null || processList.size() == 0){
				return false;
			}
			//步骤对象
			LfProcess process = null;
			//循环步骤对象集合
			for(int i = 0;i<processList.size();i++){
				//取出步骤对象
				process = processList.get(i);
				//删除步骤
				this.delAllAboutUpProcess(conn, process.getPrId());
			}
			//结果为成功
			result = true;
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据业务id删除上行步骤异常。");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 根据步骤id删除所有上行业务步骤
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	public boolean delAllAboutUpProcess(Connection conn, Long prId) throws Exception {
		//步骤id不能为空
		if(prId == null){
			return false;
		}

		try{
			//删除回复短信模板
			this.delReplyByPrId(conn, prId);
			this.updateProConInDel(conn, prId);
			//删除执行条件
			this.delProConByPrId(conn, prId);
			//删除上行业务
			this.delMoServiceByPrId(conn, prId);
			//删除步骤
			this.deleteProcess(conn, prId);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据步骤id删除所有上行业务步骤异常。");
			throw e;
		}
		//返回成功
		return true;
	}
	
	/**
	 * 根据步骤id删除上行业务
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	private Integer delMoServiceByPrId(Connection conn, Long prId) throws Exception {
		//结果
		Integer result;
		//条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("prId", prId.toString());
			//执行删除
			result = empDao.delete(LfMoService.class, conditionMap);
		}catch(Exception e){
			//异常处理
			EmpExecutionContext.error(e, "根据步骤id删除上行业务异常。");
			throw e;
		}
		//返回结果
		return result;
	}
	
	/**
	 * 更新步骤条件
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	private Integer updateProConInDel(Connection conn, Long prId) throws Exception {
		//结果
		Integer result = 0;
		//更新对象
		LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
		//条件
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
		try{
			objectMap.put("UsedPrId", null);
			conditionMap.put("UsedPrId", prId.toString());
			//执行更新
			empTransDao.update(conn, LfProCon.class, objectMap, conditionMap);
		}catch(Exception e){
			//异常处理
			result=0;
			EmpExecutionContext.error(e, "更新步骤条件异常。");
 			throw e;
		} 
		//返回结果
		return result;
	}
	
	/**
	 * 删除步骤条件
	 * @param conn
	 * @param prId
	 * @return
	 * @throws Exception
	 */
	private Integer delProConByPrId(Connection conn, Long prId) 
	{
		//条件
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String, String>();
		try{
			conditionMap.put("prId", prId.toString());
			//执行删除
			Integer result = empTransDao.delete(conn, LfProCon.class, conditionMap);
			//返回结果
			return result;
		}catch(Exception e){
			EmpExecutionContext.error(e, "删除步骤条件异常。");
 			return 0;
		} 
		
	}
	
	/**
	 * 
	 * @param dbConnect
	 * @return
	 * @throws Exception
	 */
	public boolean testConnection(LfDBConnect dbConnect)
	{

		// 数据库连接
		Connection con = null;
		try
		{
			// 驱动名称
			String driverName;
	
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
		
			//设置驱动名
			Class.forName(driverName);
			con = DriverManager.getConnection(dbConnect.getConStr(), dbConnect
					.getDbUser(), dbConnect.getDbPwd());

			return true;

		}
		catch(SQLException e)
		{
			EmpExecutionContext.info("智能引擎测试数据源是否可用，数据源不可用。"
					+ "DbType="+dbConnect.getDbType()
					+ ",ConStr="+dbConnect.getConStr()
					+ ",DbUser="+dbConnect.getDbUser()
					+ ",DbPwd="+dbConnect.getDbPwd()
					+ ",errorMsg="+e.getMessage()
					);
			return false;
		}
		catch (Exception e)
		{
			//捕获异常记录日志
			EmpExecutionContext.error(e, "智能引擎测试数据源是否可用，测试数据源连接异常。"
					+ "DbType="+dbConnect.getDbType()
					+ ",ConStr="+dbConnect.getConStr()
					+ ",DbUser="+dbConnect.getDbUser()
					+ ",DbPwd="+dbConnect.getDbPwd()
					);
			return false;
		}
		finally
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
		try
		{
			// 数据连接字符串
			String url = null;
			if(dbconnect.getDbType().equals("Oracle") && dbconnect.getDbconnType() == 0)
			{
				// 设置oracle数据库的连接字符串
				url = "jdbc:oracle:thin:@(description=(address=(host=" + dbconnect.getDbconIP() + ")(protocol=tcp)(port=" + dbconnect.getPort() + "))(connect_data=(service_name=" + dbconnect.getDbName() + ")))";

			}
			else
				if(dbconnect.getDbType().equals("Oracle") && dbconnect.getDbconnType() == 1)
				{
					//type为1时设置
					url = "jdbc:oracle:thin:@" + dbconnect.getDbconIP() + ":" + dbconnect.getPort() + ":" + dbconnect.getDbName();
				}
				else
					if(dbconnect.getDbType().equals("Sql Server"))
					{
						// 设置sql server的数据库连接字符串
						url = "jdbc:sqlserver://" + dbconnect.getDbconIP() + ":" + dbconnect.getPort() + ";databaseName=" + dbconnect.getDbName();
					}
					else
						if(dbconnect.getDbType().equals("Mysql"))
						{
							// 设置mysql数据库连接字符串
							url = "jdbc:mysql://" + dbconnect.getDbconIP() + ":" + dbconnect.getPort() + "/" + dbconnect.getDbName();
						}
						else
							if(dbconnect.getDbType().equals("DB2"))
							{
								//设置db2数据库连接字符串
								url = "jdbc:db2://" + dbconnect.getDbconIP() + ":" + dbconnect.getPort() + "/" + dbconnect.getDbName();
							}
			return url;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎新增数据源，获取连接字符串异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @param dbConnect
	 * @return
	 * @throws Exception
	 */
	public boolean addDBConnect(LfDBConnect dbConnect) 
	{
		try
		{
			// 数据库地址字符串
			String conStr = this.createConnUrl(dbConnect);
			//设置对象的连接字符串
			dbConnect.setConStr(conStr);
			// 返回结果
			boolean result = empDao.save(dbConnect);
			return result;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎新增数据源异常。");
			// 异常设为否
			return false;
		}
	}
	
	/**
	 * 替换参数
	 * @param str
	 * @return
	 * @throws Exception
	 */
	public String changeParam(String str) throws Exception
	{
		String eg = "#[pP]_[1-9][0-9]*#";
		Matcher m = Pattern.compile(eg,
				Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE).matcher(str);
		while (m.find())
		{
			String rstr = m.group();
			str = str.replace(rstr, rstr.toUpperCase());
		}
		return str;
	}
	
	/**
	 * 添加短信模板
	 * 
	 * @param template
	 * @return
	 * @throws Exception
	 */
	public long addTemplate(LfTemplate template) throws Exception
	{
		Long id = 0L;		
		boolean issuccess = true;
		// 获取连接
		Connection conn = empTransDao.getConnection();
		
		try
		{
			// 开启事务
			empTransDao.beginTransaction(conn);
			ReviewBiz reviewBiz=new ReviewBiz();
			// 判断是否需要审核信息
			LfFlow flow = reviewBiz.checkUserFlow(template.getUserId()
					,template.getCorpCode(),template.getTmpType().toString());
			// 不需要审核
			if (flow == null)
			{

				template.setIsPass(0);
			}

			//Oracle NULL会报错
			if(StringUtils.isBlank(template.getExlJson()))
				template.setExlJson(" ");
			if(StringUtils.isBlank(template.getVer()))
				template.setVer(" ");

			// 保存数据库
			id = empTransDao.saveObjectReturnID(conn, template);
			//添加模板到lf_all_template
			LfTmplRela shareTemp=new LfTmplRela();
			shareTemp.setCorpCode(template.getCorpCode());
			shareTemp.setCreaterId(template.getUserId());
			shareTemp.setCreateTime(new Timestamp(System.currentTimeMillis()));
			shareTemp.setTemplId(id);
			shareTemp.setToUser(template.getUserId());
			empTransDao.save(conn, shareTemp);
			if (flow != null && template.getTmState() != 2)
			{
				// 设置审批信息
				issuccess = reviewBiz.addFlowRecordChild(conn, id, 
					template.getTmName(), template.getAddtime(), template.getTmpType(), 
					flow.getFId(), flow.getRLevelAmount(), template.getUserId(),
					"1", Integer.valueOf(template.getDsflag().toString()),null);

			}
			if(issuccess)
			{
				empTransDao.commitTransaction(conn);
			}else
			{
				empTransDao.rollBackTransaction(conn);
			}
		
		} catch (Exception e)
		{
			// 回滚
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e,"添加短彩信模板biz层异常！");
			throw e;
		} finally
		{
			// 关闭连接
			empTransDao.closeConnection(conn);
			if (issuccess)
			{
				// 获取配置文件中的信息
				String isre = SystemGlobals.getSysParam("isRemind");
				if ("0".equals(isre))
				{
					LfFlowRecord record = new LfFlowRecord();
            		record.setInfoType(template.getTmpType());
            		record.setMtId(id);
            		record.setProUserCode(template.getUserId());
            		record.setRLevelAmount(1);
            		record.setRLevel(0);
            		ReviewRemindBiz rere=new ReviewRemindBiz();
            		//调用审批提醒方法
            		rere.flowReviewRemind(record);
            		rere.sendMail(record);
				}
			}else
			{
				id=-1L;
			}
		}
		return id.longValue();
	}

	/**
	 * 获取网讯模板
	 * @param userId
	 * @param depId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getNetTemplate(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
		
		return new ProcessDao().getNetTemplate(conditionMap, pageInfo);
	}
	
	/**
	 * 获取通道信息
	 * @param serId
	 * @return
	 */
	public String getSmsContentMaxLen(String serId)
	{
		String[] gtInfos = new String[]{"","","",""};
		int index = 0;
		int maxLen;
		int totalLen;
		int lastLen;
		int signLen;
		int enmaxLen;
		int entotalLen;
		int enlastLen;
		int ensignLen;
		int gateprivilege = 0;
		int gatepri;
		//签名位置,1:前置;0:后置
		int signLocation = 0;
		int ensinglelen;
		int msgLen = 990;
		LfService service = null;
		String info = "infos:";
		try {
			if(serId != null && !"".equals(serId))
			{
				service = baseBiz.getById(LfService.class, serId);
				if(service != null)
				{
					String spUser = service.getSpUser();
					if(spUser != null && !"".equals(spUser))
					{
						// 根据发送账号获取路由信息
						List<DynaBean> spGateList = smsBiz.getSpGateInfo(spUser);
						for (DynaBean spGate : spGateList)
						{
							gateprivilege = 0;
							//中文短信配置参数
							maxLen = Integer.parseInt(spGate.get("maxwords").toString());
							totalLen = Integer.parseInt(spGate.get("multilen1").toString());
							lastLen = Integer.parseInt(spGate.get("multilen2").toString());
							signLen = Integer.parseInt(spGate.get("signlen").toString());
							// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
							if(signLen == 0){
								signLen = spGate.get("signstr").toString().trim().length();
							}
							//英文短信配置参数
							enmaxLen = Integer.parseInt(spGate.get("enmaxwords").toString());
							entotalLen = Integer.parseInt(spGate.get("enmultilen1").toString());
							enlastLen = Integer.parseInt(spGate.get("enmultilen2").toString());
							ensinglelen = Integer.parseInt(spGate.get("ensinglelen").toString());
							
							//是否支持英文短信，1：支持；0：不支持
							gatepri = Integer.parseInt(spGate.get("gateprivilege").toString());
							//gateprivilege = (gateprivilege&2)==2?1:0;
							//运营商标识
							index =Integer.parseInt(spGate.get("spisuncm").toString());
							//电信
							if(index == 21){
								index = 2;
							//国外通道
							}else if(index == 5){
								index = 3;
								//是否支持英文短信，1：支持；0：不支持
								if((gatepri&2)==2)
								{
									gateprivilege = 1;
									//国外通道英文短信最大长度
									msgLen = enmaxLen - 20;
								}
								else
								{
									gateprivilege = 0;
									//国外通道中文短信最大长度
									msgLen = maxLen - 10;
								}
							}
							//签名位置,1:前置;0:后置
							if((gatepri&4)==4)
							{
								signLocation = 1;
							}
							else
							{
								signLocation = 0;
							}
							//英文短信签名长度
							ensignLen = Integer.parseInt(spGate.get("ensignlen").toString());
							// 如果设定的英文短信签名长度为0则为实际短信签名内容的长度
							if(ensignLen == 0){
								if(index == 3)
								{
									//国外通道英文短信签名长度
									ensignLen = smsBiz.getenSmsSignLen(spGate.get("ensignstr").toString().trim());
								}
								else
								{
									//国内通道英文短信签名长度
									ensignLen = spGate.get("ensignstr").toString().trim().length();
								}
							}
							
							gtInfos[index] = new StringBuffer().append(maxLen).append(",").append(totalLen).append(",")
											.append(lastLen).append(",").append(signLen).append(",").append(enmaxLen).append(",")
											.append(entotalLen).append(",").append(enlastLen).append(",").append(ensignLen)
											.append(",").append(gateprivilege).append(",").append(ensinglelen).append(",").append(signLocation).toString();
						}
						info += gtInfos[0]+"&"+gtInfos[1]+"&"+gtInfos[2]+"&"+gtInfos[3]+"&"+msgLen+"&";
					}
					
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取发送账户绑定的通道信息异常！");
		}
		return info;
	}
}
