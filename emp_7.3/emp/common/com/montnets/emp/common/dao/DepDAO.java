package com.montnets.emp.common.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.database.ConnectionManagerImp;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.util.StringUtils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class DepDAO extends SuperDAO
{
	/**
	 * 对PATH写值
	 * 
	 * @throws Exception
	 */
	public void updateLfDepPath()
	{
		boolean isUpdate = false;
		List<LfDep> lfdepList = null;
		IEmpDAO empDao = new DataAccessDriver().getEmpDAO();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//机构级别--第一级
		conditionMap.put("depLevel", "1");
		try
		{
			lfdepList = empDao.findListByCondition(LfDep.class, conditionMap,
					null);
			LfDep lfDep =null;
			if(lfdepList!=null&&lfdepList.size()>0){
				for (int i = 0; i < lfdepList.size(); i++)
				{
					lfDep = lfdepList.get(i);
					if (lfDep.getDeppath()==null||!lfDep.getDeppath().equals(String.valueOf(lfDep.getDepId())+"/"))
					{
						isUpdate = true;
					}
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查找机构对象异常！");
		}
		if (isUpdate)
		{
			IEmpTransactionDAO empTransDao = new DataAccessDriver().getEmpTransDAO();
			LinkedHashMap<Long, String> depPathMap = new LinkedHashMap<Long, String>();
			//获取数据库连接
			Connection conn = new ConnectionManagerImp()
					.getDBConnection(StaticValue.EMP_POOLNAME);
			int level = 1;
			try
			{
				while (true)
				{
					conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("depLevel", String.valueOf(level));
					lfdepList = empDao.findListByCondition(LfDep.class,
							conditionMap, null);
					if (lfdepList != null && lfdepList.size() > 0)
					{
						for (int i = 0; i < lfdepList.size(); i++)
						{
							LfDep lfdep = lfdepList.get(i);
							Long depId = null;
							Long pareDepId = null;
							if (level == 1)
							{
								depId = lfdep.getDepId();
								lfdep.setDeppath(String.valueOf(depId)+"/");
								depPathMap.put(depId, String.valueOf(lfdep
										.getDeppath()));
								//执行修改操作
								empTransDao.update(conn, lfdep);
							} else
							{
								depId = lfdep.getDepId();
								pareDepId = lfdep.getSuperiorId();
								String pareDepPath = depPathMap.get(pareDepId);
								String path = pareDepPath + depId+"/";
								lfdep.setDeppath(path);
								depPathMap.put(depId, String.valueOf(lfdep
										.getDeppath()));
								//执行修改操作
								empTransDao.update(conn, lfdep);
							}
						}
					} else
					{
						break;
					}
					level++;
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "更新机构信息失败。");
			} finally
			{
				if (conn != null)
				{
					try
					{
						//关闭连接
						conn.close();
					} catch (Exception e)
					{
						EmpExecutionContext.error(e,"关闭数据库连接异常！");
					}
				}
			}
		}
	}
	
	/**
	 * 更新员工机构deppath
	 */
	public void updateLfEmployeeDepPath()
	{
		boolean isUpdate = false;
		List<LfEmployeeDep> lfdepList = null;
		IEmpDAO empDao = new DataAccessDriver().getEmpDAO();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("depLevel", "1");
		try
		{
			lfdepList = empDao.findListByCondition(LfEmployeeDep.class, conditionMap,
					null);
			LfEmployeeDep lfDep =null;
			if(lfdepList!=null&&lfdepList.size()>0){
				for (int i = 0; i < lfdepList.size(); i++)
				{
					lfDep = lfdepList.get(i);
					if (lfDep.getDeppath()==null||!lfDep.getDeppath().equals(String.valueOf(lfDep.getDepId())+"/"))
					{
						isUpdate = true;
					}
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查找机构对象异常！");
		}
		if (isUpdate)
		{
			IEmpTransactionDAO empTransDao = new DataAccessDriver().getEmpTransDAO();
			LinkedHashMap<Long, String> depPathMap = new LinkedHashMap<Long, String>();
			//获取数据库连接
			Connection conn = new ConnectionManagerImp()
					.getDBConnection(StaticValue.EMP_POOLNAME);
			int level = 1;
			try
			{
				while (true)
				{
					conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("depLevel", String.valueOf(level));
					lfdepList = empDao.findListByCondition(LfEmployeeDep.class,
							conditionMap, null);
					if (lfdepList != null && lfdepList.size() > 0)
					{
						for (int i = 0; i < lfdepList.size(); i++)
						{
							LfEmployeeDep lfdep = lfdepList.get(i);
							Long depId = null;
							Long pareDepId = null;
							if (level == 1)
							{
								depId = lfdep.getDepId();
								lfdep.setDeppath(String.valueOf(depId)+"/");
								depPathMap.put(depId, String.valueOf(lfdep
										.getDeppath()));
								//执行修改操作
								empTransDao.update(conn, lfdep);
							} else
							{
								depId = lfdep.getDepId();
								pareDepId = lfdep.getParentId();
								String pareDepPath = depPathMap.get(pareDepId);
								String path = pareDepPath  + depId+"/";
								lfdep.setDeppath(path);
								depPathMap.put(depId, String.valueOf(lfdep
										.getDeppath()));
								//执行修改操作
								empTransDao.update(conn, lfdep);
							}
						}
					} else
					{
						break;
					}
					level++;
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "更新员工机构信息失败。");
			} finally
			{
				if (conn != null)
				{
					try
					{
						//关闭连接
						conn.close();
					} catch (Exception e)
					{
						EmpExecutionContext.error(e,"关闭数据库连接异常！");
					}
				}
			}
		}
	}
	
	/**
	 * 更新客户机构deppath
	 */
	public void updateLfClientDepPath()
	{
		boolean isUpdate = false;
		List<LfClientDep> lfdepList = null;
		IEmpDAO empDao = new DataAccessDriver().getEmpDAO();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("depLevel", "1");
		try
		{
			lfdepList = empDao.findListByCondition(LfClientDep.class, conditionMap,null);
			LfClientDep lfDep =null;
			if(lfdepList!=null&&lfdepList.size()>0){
				for(int i=0;i<lfdepList.size();i++)
				{
					lfDep = lfdepList.get(i);
					if (lfDep.getDeppath()==null || !lfDep.getDeppath().equals(String.valueOf(lfDep.getDepId())+"/"))
					{
						isUpdate = true;
					}
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查找机构对象异常！");
		}
		if (isUpdate)
		{
			IEmpTransactionDAO empTransDao = new DataAccessDriver().getEmpTransDAO();
			LinkedHashMap<Long, String> depPathMap = new LinkedHashMap<Long, String>();
			//获取数据库连接
			Connection conn = new ConnectionManagerImp()
					.getDBConnection(StaticValue.EMP_POOLNAME);
			int level = 1;
			try
			{
				while (true)
				{
					conditionMap = new LinkedHashMap<String, String>();
					conditionMap.put("depLevel", String.valueOf(level));
					lfdepList = empDao.findListByCondition(LfClientDep.class,
							conditionMap, null);
					if (lfdepList != null && lfdepList.size() > 0)
					{
						for (int i = 0; i < lfdepList.size(); i++)
						{
							LfClientDep lfdep = lfdepList.get(i);
							Long depId = null;
							Long pareDepId = null;
							if (level == 1)
							{
								depId = lfdep.getDepId();
								lfdep.setDeppath(String.valueOf(depId)+"/");
								depPathMap.put(depId, String.valueOf(lfdep
										.getDeppath()));
								//执行修改操作
								empTransDao.update(conn, lfdep);
							} else
							{
								depId = lfdep.getDepId();
								pareDepId = lfdep.getParentId();
								String pareDepPath = depPathMap.get(pareDepId);
								String path = pareDepPath+ depId+"/";
								lfdep.setDeppath(path);
								depPathMap.put(depId, String.valueOf(lfdep
										.getDeppath()));
								//执行修改操作
								empTransDao.update(conn, lfdep);
							}
						}
					} else
					{
						break;
					}
					level++;
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "更新客户机构失败。");
			} finally
			{
				if (conn != null)
				{
					try
					{
						//关闭连接
						conn.close();
					} catch (Exception e)
					{
						EmpExecutionContext.error(e,"关闭数据库连接异常！");
					}
				}
			}
		}
	}
	
	/**
	 * 根据机构ID查询包括该机构的所有下级机构ID组成的查询字符串
	 * @param depId 机构ID
	 * @param columnName 列名
	 * @return 返回值格式如下  columnName in () or columnName in () or ...
	 * @throws Exception
	 */
	public String getChildUserDepByParentID(Long depId,String columnName) throws Exception{
		List<String> childList=null;
		String returnStr="";
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			childList= getChildByParentIDOracle(null,depId);
			returnStr=completeChild(childList,columnName);
		}else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			//sqlserver数据库
			childList=getChildByParentIDSqlServer(null,depId);
			returnStr=completeChild(childList,columnName);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			String childs=getChildByParentIDMySql(null,String.valueOf(depId));
			String[] childarr=childs.split(",");
			StringBuilder childSb=new StringBuilder("");
			childSb.append(columnName).append(" in (");
			for (int i = 0; i < childarr.length; i++)
			{
				//if(i > (StaticValue.inConditionMax-1) && i%StaticValue.inConditionMax==0){
				if(i > (StaticValue.getInConditionMax()-1) && i%StaticValue.getInConditionMax()==0){
					childSb.deleteCharAt(childSb.lastIndexOf(","));
					childSb.append(") or ").append(columnName).append(" in (");
				}
				childSb.append(childarr[i]).append(",");
			}
			childSb.deleteCharAt(childSb.lastIndexOf(",")).append(")");
			returnStr=childSb.toString();
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			String childs=getChildByParentIDMySql(null,String.valueOf(depId));
			String[] childarr=childs.split(",");
			StringBuffer childSb=new StringBuffer("");
			childSb.append(columnName).append(" in (");
			for (int i = 0; i < childarr.length; i++)
			{
				//if(i > (StaticValue.inConditionMax-1) && i%StaticValue.inConditionMax==0){
				if(i > (StaticValue.getInConditionMax()-1) && i%StaticValue.getInConditionMax()==0){
					childSb.deleteCharAt(childSb.lastIndexOf(","));
					childSb.append(") or ").append(columnName).append(" in (");
				}
				childSb.append(childarr[i]).append(",");
			}
			childSb.deleteCharAt(childSb.lastIndexOf(",")).append(")");
			returnStr=childSb.toString();
		}
		
		return returnStr;
	}
	
	public List<String> getChildByParentIDOracle(Connection con,Long depId) throws Exception {
		List<String> returnList=new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer("");
		CallableStatement proc = null;
		try {
			//如果传入的con为空，则获取一个数据库连接。
			if(con==null){
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//如果传入的con不为空，则将con赋值给conn。
			}else{
				conn=con;
			}
			proc = conn
					.prepareCall("{ call GETUSERDEPCHILDBYPID(?,?,?,?) }");
			proc.setInt(1, 1);
			proc.setLong(2, depId);
			proc.setString(3, StringUtils.getRandom());
		
			proc.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
			proc.execute();
			rs = (ResultSet) proc.getObject(4);
			int i=0;
			while (rs.next()) {
				i++;
				if(i%1000!=0){
					deps.append(rs.getLong("DepID")).append(",");
				}else{
						deps.append(rs.getLong("DepID"));
						returnList.add(deps.toString());
						deps=new StringBuffer("");
				}
			}
			if(!deps.toString().equals("")){
				returnList.add(deps.deleteCharAt(deps.lastIndexOf(",")).toString());
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构的子级机构Id异常！");
		} finally {
			//如果传入的con为空，则将数据库连接关闭。
			if(con==null){
				try {
					close(rs, proc, conn);
				} catch (SQLException e) {
					EmpExecutionContext.error(e,"关闭数据库资源出错！");
				}
			//如果传入的con不为空，则不将数据库连接关闭。
			}else{
				try {
					close(rs, proc, null);
				} catch (SQLException e) {
					EmpExecutionContext.error(e,"关闭数据库资源出错！");
				}
			}
		}
		return returnList;
	}
	
	public List<String> getChildByParentIDSqlServer(Connection con,Long depId) throws Exception{
		List<String> returnList=new ArrayList<String>();
		String sql = new StringBuffer("select * from GetUserDepChildByPID(1,").append(depId).append(")").toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer("");
		try {
			//如果传入的con为空，则获取一个数据库连接。
			if(con==null){
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//如果传入的con不为空，则将con赋值给conn。
			}else{
				conn=con;
			}
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int i=0;
			while (rs.next()) {
				i++;
				if(i%1000!=0){
					deps.append(rs.getLong("DepID")).append(",");
				}else{
						deps.append(rs.getLong("DepID"));
						returnList.add(deps.toString());
						deps=new StringBuffer("");
				}
			}
			if(!deps.toString().equals("")){
				returnList.add(deps.deleteCharAt(deps.lastIndexOf(",")).toString());
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构的子级机构Id异常！");
			throw e;
		} finally {
			//如果传入的con为空，则将数据库连接关闭。
			if(con==null){
				close(rs, ps, conn);
			//如果传入的con不为空，则不将数据库连接关闭。
			}else{
				close(rs, ps, null);
			}
		}
		return returnList;
	}
	
	public String getChildByParentIDMySql(Connection con,String depId) throws Exception
	{
		String depIds = depId ;
		String conditionDepid = depId;
		String sql = "";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int incount = 0;
		//int maxIncount = StaticValue.inConditionMax;
		int maxIncount = StaticValue.getInConditionMax();
		try {
			//如果传入的con为空，则获取一个数据库连接。
			if(con==null){
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			//如果传入的con不为空，则将con赋值给conn。
			}else{
				conn=con;
			}
			boolean hasNext = true;
			while(hasNext)
			{	
				incount = 0;
				sql = "select "+TableLfDep.DEP_ID+" from "+TableLfDep.TABLE_NAME+" where ("+TableLfDep.SUPERIOR_ID+" in ("+conditionDepid+")) and "+TableLfDep.DEP_STATE+"=1";
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				conditionDepid = "";
				while (rs.next()) {
					incount ++;
					depIds += "," + rs.getString("dep_id");
					if (incount > maxIncount && (incount - 1) % maxIncount == 0) {
						conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
						conditionDepid += ") or "+TableLfDep.SUPERIOR_ID+" in (";
					}
					conditionDepid += rs.getString("dep_id") + ",";
				}
				if (conditionDepid.length() == 0) {
					hasNext = false;
				} else {
					hasNext = true;
					conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
				}
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取子级机构集合异常！");
			throw e;
		} finally {
			//如果传入的con为空，则将数据库连接关闭。
			try{
				close(rs, ps, conn);
			}catch(Exception e){
				EmpExecutionContext.error(e,"关闭资源异常");
			}

		}
		return depIds;	
	}
	
	private String completeChild(List<String> childList,String columnName){
		StringBuilder childSb=new StringBuilder();
 
		for (int i = 0; i < childList.size(); i++) {
			if(i!=childList.size()-1){
			    childSb.append(columnName).append(" in (").append(childList.get(i)).append(") or ");
			}else{
				childSb.append(columnName).append(" in (").append(childList.get(i)).append(")");
			}
		}
		return childSb.toString();
	}
	
	/**
	 * 通过当前机构查找该机构的所有子机构
	 * @param con 如果con为空，则方法获取一个连接，如果con不为空，则用con作为此次查询的数据库连接。
	 * @param depId depId 机构ID
	 * @return 返回值格式是所有机构ID以逗号隔开
	 * @throws Exception
	 */
	public String getChildUserDepByParentID(Connection con,Long depId) throws Exception{
		String returnStr="";
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			//oracle数据库
			StringBuffer returnBuff=new StringBuffer();
			List<String> childList= getChildByParentIDOracle(con,depId);
			for (int i = 0; i <childList.size(); i++)
			{
				if(i!=childList.size()-1){
					returnBuff.append(childList.get(i)).append(",");
				}else{
					returnBuff.append(childList.get(i));
				}
			}
			returnStr=returnBuff.toString();
		}else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			//sqlserver数据库
			StringBuffer returnBuff=new StringBuffer();
			List<String> childList=getChildByParentIDSqlServer(con,depId);
			for (int i = 0; i <childList.size(); i++)
			{
				if(i!=childList.size()-1){
					returnBuff.append(childList.get(i)).append(",");
				}else{
					returnBuff.append(childList.get(i));
				}
			}
			returnStr=returnBuff.toString();
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			//mysql数据库
			returnStr=getChildByParentIDMySql(con,String.valueOf(depId));
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			returnStr=getChildByParentIDMySql(con,String.valueOf(depId));
		}
		
		return returnStr;
	}
	
	/**
	 * 获取操作员机构级别
	 * @param depId 机构ID
	 * @return
	 * @throws Exception 
	 */
	public Integer getUserDepLevel(Long depId) throws Exception{
		Connection conn = null;
		CallableStatement proc = null;
		Integer currentLevel = 0;
		if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			//db2数据库
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				//获取数据库连接
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
				String sql = new StringBuffer("select ").append(TableLfDep.DEP_LEVEL)
				.append(" from ").append(TableLfDep.TABLE_NAME).append(" where ")
				.append(TableLfDep.DEP_ID).append(" = ").append(depId).toString();
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				//执行SQL
				rs = ps.executeQuery();
				if(rs.next()){
					currentLevel = rs.getInt(1);
				}
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取操作员机构级别调用存储过程异常（DB2）。");
				throw e;
			} finally {
				//关闭数据库资源
				close(rs, ps, conn);
			}
		}else{
			//oracle  mysql sqlserver数据库
			try {
				conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
				proc = conn
						.prepareCall("{ call GETUSERDEPLEVEL(?,?,?) }");
				proc.setInt(1, 1);
				proc.setLong(2, depId);
				proc.registerOutParameter(3, Types.INTEGER);
				proc.execute();
				currentLevel = proc.getInt(3);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "获取操作员机构级别调用存储过程异常。");
				throw e;
			} finally {
				try {
					//关闭数据库资源
					close(null, proc, conn);
				} catch (SQLException e) {
					EmpExecutionContext.debug("关闭数据库资源出错！");
				}
			}
		}
		return currentLevel;
	}
}
