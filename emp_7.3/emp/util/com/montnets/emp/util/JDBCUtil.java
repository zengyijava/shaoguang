package com.montnets.emp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.context.EmpExecutionContext;


public class JDBCUtil {
	
	private String url;
	private String user;
	private String password;
	
	private Connection conn;
	private ResultSet resultSet;
	private PreparedStatement pState;
	
	public JDBCUtil(){}
	
	public JDBCUtil(String driver, String url, String user, String password) throws ClassNotFoundException {
		Class.forName(driver);
		this.url = url;
		this.user = user;
		this.password = password;
	}
	
	public String  getClassNameByDBType(String dbType) throws Exception {
		if(dbType == null || "".equals(dbType.trim())){
			return null;
		}
		String className = "";
		if(dbType.toLowerCase().equals("oracle")){
			className = "oracle.jdbc.driver.OracleDriver";
		}
		else if(dbType.toLowerCase().equals("sql server")) {
			className = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
		else if(dbType.toLowerCase().equals("mysql")) {
			className = "com.mysql.jdbc.Driver";
		}else if(dbType.toLowerCase().equals("db2")){
			className = "com.ibm.db2.jcc.DB2Driver";
		}
		return className;
	}
	
	public int executeUpdate(String sql, Object[] params) throws SQLException {
		try {
			conn = DriverManager.getConnection(url, user, password);
			EmpExecutionContext.sql("execute sql : "+sql);
			pState = conn.prepareStatement(sql);
			if(params != null) {
				for(int i = 0; i < params.length; i++) {
					pState.setObject(i + 1, params[i]);
				}
			}
			return pState.executeUpdate();
		}
		catch(SQLException ex) {
			EmpExecutionContext.error(ex, "执行更新语句失败，sql:" + sql + "，params：" + params);
			throw ex;
		}
		finally {
			closeAll();
		}
	}
	
	public List<Map<String, String>> executeQuery(String sql, Object[] params) throws SQLException {
		try {
			conn = DriverManager.getConnection(url, user, password);
			EmpExecutionContext.sql("execute sql : "+sql);
			pState = conn.prepareStatement(sql);
			if(params != null) {
				for(int i = 0; i < params.length; i++) {
					pState.setObject(i + 1, params[i]);
				}
			}
			resultSet = pState.executeQuery();
			
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			List<Map<String, String>> rsList = new ArrayList<Map<String, String>>();
			while(resultSet.next())
			{
				Map<String, String> rsMap = new HashMap<String, String>(columnCount);
				for(int i = 1; i < columnCount + 1; i++)
				{
					rsMap.put(rsmd.getColumnName(i).toUpperCase(), resultSet.getString(i)==null?"":resultSet.getString(i)); 
				}
				if(rsMap.get("PHONE") !=null && !"".equals(rsMap.get("PHONE").trim()))
				{
					rsList.add(rsMap);
					break;
				}
				
			}
			return rsList;
		}
		catch(SQLException ex) {
			EmpExecutionContext.error(ex, "执行查询语句失败，sql:" + sql + "，params：" + params);
			throw ex;
		}
		finally {
			closeAll();
		}
	}
	

	private void closeAll() throws SQLException {
		if(pState != null){
			pState.close();
		}
		if(resultSet != null){
			resultSet.close();
		}
		if(conn != null && conn.isClosed() == false) {
			conn.close();
		}
	}
	
	public void closeAll(Connection conn, ResultSet resultSet,
			PreparedStatement pState) {
		
		try{
			if (pState != null) {
				pState.close();
			}
			if (resultSet != null) {
				resultSet.close();
			}
			if (conn != null && conn.isClosed() == false) {
				conn.close();
			}
		}catch(SQLException e){
			EmpExecutionContext.error(e,"关闭数据库连接失败！");
		}
	}
}
