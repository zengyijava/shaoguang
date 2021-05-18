package com.montnets.emp.netnews.dao;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.GroupInfoVo;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.netnews.table.TableLfWXBASEINFO;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

public class WXSendDao extends SuperDAO{
	
	//写一个不需要分页的获取员工机构下面所有子机构的方法
	public List<LfEmployeeDep> findEmployeeDepsByDepId(String corpCode,String depId) throws Exception {
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	sql = "select lfemployeedep.*";
		   	String depIds = this.executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(depId)});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeDep>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select lfemployeedep.*, ROW_NUMBER() Over(Order By lfemployeedep.corp_code) As rn";
			  conditionSql.append("and lfemployeedep.").append(TableLfEmployeeDep.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(depId).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select lfemployeedep.*";
			 String depIds = getEmpChildIdByDepId(depId.toString());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeDep>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select lfemployeedep.*";
			 String depIds = getEmpChildIdByDepId(depId.toString());
		   	 if(depIds == null || depIds.length() == 0)
		   	 {
		   	 	return new ArrayList<LfEmployeeDep>();
		   	 }
		   	conditionSql = getInConditionSql(conditionSql, depIds);
		}

		String tableSql = new StringBuffer(" from ").append(
				TableLfEmployeeDep.TABLE_NAME).append(" lfemployeeDep ")
				.toString();

		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" and lfemployeedep.").append(TableLfEmployeeDep.CORP_CODE).append("= '").append(corpCode+"'").toString();
		sql += tableSql;
		String tempsql=moreWhere+conditionSql;
		//存在查询条件
		if(tempsql != null && tempsql.length() > 0)
		{
			//将条件字符串首个and替换为where,
			tempsql = tempsql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		sql += tempsql;
		List<LfEmployeeDep> returnVoList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	
	
	//写一个不需要分页的获取员工机构下面所有员工电话的方法
	public List<LfEmployee> findEmpMobileByDepIds(String corpCode,
			String depId) throws Exception {
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	 sql = "select lfemployee."+TableLfEmployee.MOBILE;
		   	String depIds = executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(depId)});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployee>();
	   	 	}
	   	 conditionSql.append(" and lfemployee.DEP_ID in (").append(depIds).append(")");
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select lfemployee.MOBILE, ROW_NUMBER() Over(Order By lfemployee.CORP_CODE) As rn";
			  conditionSql.append("and lfemployee.").append(TableLfEmployee.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(depId).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select lfemployee.MOBILE";
			 String depIds = getEmpChildIdByDepId(depId.toString());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployee>();
	   	 	}
		   	 conditionSql.append(" and lfemployee.DEP_ID in (").append(depIds).append(")");
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select lfemployee.MOBILE";
			 String depIds = getEmpChildIdByDepId(depId.toString());
		   	 if(depIds == null || depIds.length() == 0)
		   	 {
		   	 	return new ArrayList<LfEmployee>();
		   	 }
			 conditionSql.append(" and lfemployee.DEP_ID in (").append(depIds).append(")");
		}

		String tableSql = new StringBuffer(" from ").append(TableLfEmployee.TABLE_NAME).append(" lfemployee ").toString();

		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" and lfemployee.").append(TableLfEmployee.CORP_CODE).append("= '").append(corpCode+"'").toString();
		
		sql += tableSql;
		String tempsql=moreWhere+conditionSql;
		//存在查询条件
		if(tempsql != null && tempsql.length() > 0)
		{
			//将条件字符串首个and替换为where,
			tempsql = tempsql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		sql += tempsql;
		List<LfEmployee> returnList =findPartEntityListBySQL(LfEmployee.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	

	/* @param orderbyMap
	 *            排序条件
	 * @param pageInfo
	 *            分页信息，无需分析时传入null
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	public List<LfEmployee> getEmployeeByGuid(String udgId, String loginId,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<LfEmployee> employees = null;
		List<LfMalist> malists = null;
		String sql = "";
		String sql2 = "";
		//第一次进入选择员工页面时群组id为空，就根据loginId查询。点击群组之后udgId不会空
		if (udgId != null && !"".equals(udgId))
		{
			sql = "select * from " + TableLfEmployee.TABLE_NAME + "  where "
					+ TableLfEmployee.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in (" + udgId + ") and "
					+ TableLfList2gro.L2G_TYPE + "=0)";
			sql2 = "select * from " + TableLfMalist.TABLE_NAME + "  where "
					+ TableLfMalist.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in (" + udgId + ") and "
					+ TableLfList2gro.L2G_TYPE + "=2)";
		}else if (loginId != null && !"".equals(loginId))
		{
			sql = "select * from " + TableLfEmployee.TABLE_NAME + "  where "
					+ TableLfEmployee.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in(select  "
					+ TableLfUdgroup.UDG_ID + " from "
					+ TableLfUdgroup.TABLE_NAME + " where "
					+ TableLfUdgroup.USER_ID + "=" + loginId + "))";
			sql2 = "select * from " + TableLfMalist.TABLE_NAME + "  where "
					+ TableLfMalist.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in(select  "
					+ TableLfUdgroup.UDG_ID + " from "
					+ TableLfUdgroup.TABLE_NAME + " where "
					+ TableLfUdgroup.USER_ID + "=" + loginId + "))";
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfEmployee.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			String fieldType = null;
			while (iter.hasNext())
			{
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType().toString();
						break;
					}
				}
				if (!"".equals(e.getValue()))
				{
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like"))
					{
						sql = sql + " and " + columnName + " like '%"+ e.getValue() + "%' ";
						sql2 = sql2 + " and " + columnName + " like '%"+ e.getValue() + "%' ";
					} else if (eKey.contains("&>"))
					{
						sql = sql + " and " + columnName + ">" + e.getValue()+ " ";
						sql2 = sql2 + " and " + columnName + ">" + e.getValue()+ " ";
					} else
					{
						boolean isDateType = fieldType
								.equals(StaticValue.TIMESTAMP)
								|| fieldType.equals(StaticValue.DATE_SQL)
								|| fieldType.equals(StaticValue.DATE_UTIL);
						if (fieldType.equals("class java.lang.String")
								|| isDateType)
						{
							sql = sql + " and " + columnName + "='"+ e.getValue() + "' ";
							sql2 = sql2 + " and " + columnName + "='"+ e.getValue() + "' ";
						} else
						{
							sql = sql + " and " + columnName + "="+ e.getValue();
							sql2 = sql2 + " and " + columnName + "="+ e.getValue();
						}
					}

				}
			}
		}

		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext())
			{
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}

			sql = sql.substring(0, sql.lastIndexOf(","));
		}
		if (pageInfo == null)
		{
			employees= findEntityListBySQL(LfEmployee.class, sql,
					StaticValue.EMP_POOLNAME);
			malists =  findEntityListBySQL(LfMalist.class, sql2,
					StaticValue.EMP_POOLNAME);
			LfEmployee employee = null;
			for (LfMalist ma :malists) 
			{
				employee = new LfEmployee();
				employee.setGuId(ma.getGuId());
				employee.setRecState(2);
				employee.setMobile(ma.getMobile());
				employee.setName(ma.getName());
				
				employees.add(employee);
			}
			return employees;
		} else
		{
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfEmployee.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	/**
	 *  查询出客户群组的成员    个人 / 共享
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findGroupClientByIds(String groupId) throws Exception{
		//初始化LIST
		List<DynaBean> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where  udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		
		
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");
		returnVoList  = new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sqlStr.toString());
		//返回
		return returnVoList;
	}
	
	
	/**
	 * 获取员工机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(
			String userId, String depId) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if (depId == null || "".equals(depId))
		{
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			LfEmpDepConn conn = null;
			if (connList != null && connList.size() > 0)
			{
				conn = connList.get(0);
				Long id = conn.getDepId();
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where  e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" = ")
						.append(id).append(" or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" = ")
						.append(id).append(" order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();//修改排序
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" order by ").append(
							TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();//修改排序
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	/**
	 * 获取员工机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @param corpCode 公司编码            
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserIdorDepId(
			String userId, String depId,String corpCode) throws Exception
	{
		String sql = "";
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if (depId == null || "".equals(depId))
		{
			// 这里是为了解决员工机构关联表出现了多条记录的处理方法，产生2条记录的原因可能是机器卡，或者同步
			List<LfEmpDepConn> connList = null;
			StringBuffer sb = new StringBuffer();
			sb.append(" select * from ").append(TableLfEmpDepConn.TABLE_NAME)
					.append(" c " + StaticValue.getWITHNOLOCK()).append(" where c.")
					.append(TableLfEmpDepConn.USER_ID).append(" = ").append(
							userId);
			connList = findEntityListBySQL(LfEmpDepConn.class, sb.toString(),
					StaticValue.EMP_POOLNAME);
			LfEmpDepConn conn = null;
			if (connList != null && connList.size() > 0)
			{
				conn = connList.get(0);
				Long id = conn.getDepId();
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where  (e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" = ")
						.append(id).append(" or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" = ")
						.append(id).append(") and e."+TableLfEmployeeDep.CORP_CODE+"='"+corpCode+"'").append(" order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();//修改排序
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId).append(" and "+TableLfEmployeeDep.CORP_CODE+"='"+corpCode+"'").append(" order by ").append(
							TableLfEmployeeDep.ADD_TYPE).append(" " + StaticValue.ASC).toString();//修改排序
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	/***
	 * 执行存储过程查询
	 * @param POOLNAME 连接池名称
	 * @param processStr  进程字符串
	 * @param params 参数
	 * @return 部门ID
	 * @throws Exception
	 */
	private String executeProcessReutrnCursorOfOracle( String POOLNAME,
			String processStr,Integer[] params)
			throws Exception
	{
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement proc = null;
		StringBuffer deps = new StringBuffer();
		String depIds = "";
		try
		{
			
			conn = connectionManager.getDBConnection(POOLNAME);
			proc = conn.prepareCall("{ call "+processStr+"(?,?,?,?) }");
			proc.setInt(1, params[0]);
			proc.setInt(2, params[1]);
			proc.setString(3, StringUtils.getRandom());
			proc.registerOutParameter(4,oracle.jdbc.OracleTypes.CURSOR);
			proc.execute();
			rs = (ResultSet)proc.getObject(4);
			while (rs.next())
			{
				deps.append(rs.getLong("DepID")).append(",");
			}
			if(deps.length() > 0)
				depIds = deps.substring(0, deps.lastIndexOf(","));
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行存储过程查询出错！");
			throw e;
		} finally
		{
			
			try
			{
				close(rs, proc, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.error(e,"关闭数据库资源出错！");

			}
		}
		return depIds;
	}
	
	/**
	 * 产生查询员工sql语句
	 * @param conditionSql
	 * @param depIds
	 * @return
	 */
	private StringBuffer getInConditionSql(StringBuffer conditionSql,String depIds)
	{
		/*******机构in查询处理***********/
		//int maxCount = StaticValue.inConditionMax;//in查询最大个数限制
		int maxCount = StaticValue.getInConditionMax();//in查询最大个数限制
		String[] depIdArry = depIds.split(",");
		int size = depIdArry.length;
		if(size>maxCount)
		{
			conditionSql.append(" and (lfemployeedep.").append(
					TableLfEmployee.DEP_ID).append(" in (");
			
			String inString = "";
			for(int i=0;i<size;i++)
			{
				inString +=  depIdArry[i] ;
				if( i > maxCount-2 && (i+1)%maxCount==0  && i<size - 1  )
				{
					conditionSql.append(inString).append(") or ").append(" lfemployeedep.").append(
							TableLfEmployee.DEP_ID).append(" in (");
					inString = "";
				}else if( i<size - 1 )
				{
					inString += ",";
				}
			}
			conditionSql.append(inString).append("))");
		}else
		{
		
			conditionSql.append(" and lfemployeedep.").append(
					TableLfEmployee.DEP_ID).append(" in (").append(depIds).append(")");
		}
		/*******END-机构in查询处理**********/
		
		return conditionSql;
	}
	
	/**
	 * 获取子部门相关信息
	 * @param depId
	 * @return
	 */
	public String getEmpChildIdByDepId( String depId) throws Exception
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
		int n = 1;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			boolean hasNext = true;
			while(hasNext)
			{	
				incount = 0;
				sql = "select dep_Id from lf_employee_dep where PARENT_ID in ( ? )";
				ps = conn.prepareStatement(sql);
				ps.setString(1, conditionDepid);
				rs = ps.executeQuery();
				conditionDepid = "";
				while(rs.next())
				{
					incount ++;
					depIds += "," + rs.getString("dep_id");
					if(incount > maxIncount*n)
					{
						n++;
						conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
						conditionDepid += ") or PARENT_ID in (";
					}
					conditionDepid += rs.getString("dep_id") + ",";
				}
				if(conditionDepid.length() == 0)
				{
					hasNext = false;
				}else
				{
					hasNext = true;
					conditionDepid = conditionDepid.substring(0,conditionDepid.length()-1);
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取部门ID出错！");
			throw e;
			
		} finally
		{

			close(rs, ps, conn);
		}
		return depIds;	
	}
	
	/***
	 * 获取网讯模板信息
	 * @param info 查询条件实体
	 * @param pageInfo 翻页
	 */
	public List<DynaBean> getNetTemplate(LfWXBASEINFO info,PageInfo pageInfo)throws Exception{
		String sql = "select baseinfo.id,baseinfo.netid,baseinfo.name,baseinfo.CREATDATE,sysuser.user_name userCode,sysuser.name userName ";
	
		String baseSql = " from "+TableLfWXBASEINFO.TABLE_NAME+
		" baseinfo left join "+TableLfSysuser.TABLE_NAME+" sysuser on baseinfo.CREATID = sysuser.USER_ID where baseinfo.status in (2,4) ";
		String conditionSql = getTemplateCondition(info);
		String dominationSql="";
		if(info.getCREATID() != null&&info.getCREATID()!=0 ){
		long loginid = info.getCREATID();
		 dominationSql = getDominationSql(loginid+"");
		}

		// 增加 排序功能
		String ordersql=" order by baseinfo.id DESC";
		String countSql = "select count(baseinfo.id)totalcount "+baseSql+dominationSql+conditionSql;
		sql += baseSql+dominationSql+conditionSql+ordersql;
		List<String> timeList =getTimeCondition(info);
		List<DynaBean> list = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		return list;
	}
	
	
	//获取权限 
	public  String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" and (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(")").toString();
		return sql;
	}
	
	/**
	 * 获取查询条件
	 * @param info 网讯基本信息实体
	 * @return sql语句
	 */
	private String getTemplateCondition(LfWXBASEINFO info){
		StringBuffer conditionSql = new StringBuffer(); 
		String corpCode = info.getCORPCODE();
		if(corpCode != null && !"".equals(corpCode)){
			conditionSql.append(" and baseinfo.CORP_CODE='").append(corpCode+"'");
		}
		
		Timestamp timeout = info.getTIMEOUT();
		if(timeout != null){
			conditionSql.append(" and baseinfo.timeout >?");
		}
		String name = info.getNAME();
		if(name != null && !"".equals(name) ){
			conditionSql.append(" and baseinfo.name like '%").append(name).append("%'");
		}

		if(info.getTempType() != null ){
			int TempType = info.getTempType();
			conditionSql.append(" and baseinfo.tempType =").append(TempType);
		}
		//新增运营商审核状态的条件查询
		if(info.getOperAppStatus()!=null){
			conditionSql.append(" and baseinfo.OPERAPPSTATUS =").append(info.getOperAppStatus());
		}
		return conditionSql.toString();
	}
	
	
	/**
	 * 获取超时集合
	 * @param info 网讯基本信息实体
	 * @return
	 */
	private List<String> getTimeCondition(LfWXBASEINFO info)
	{
		List<String> timeList = new ArrayList<String>();
		if(info.getTIMEOUT()!= null)
		{
			timeList.add(info.getTIMEOUT().toString());
		}
		return timeList;
	}
	/**
	 * 获取客户机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,
			String depId) throws Exception
	{
		String sql = "";
		if (depId == null || "".equals(depId))
		{
			sql = new StringBuffer(" select e.* from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" e " + StaticValue.getWITHNOLOCK() + ",").append(
					TableLfCliDepConn.TABLE_NAME).append(
					" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(
					TableLfCliDepConn.USER_ID).append(" = ").append(userId)
					.append(" and (c.").append(TableLfEmpDepConn.DEP_ID)
					.append(" =e.").append(TableLfClientDep.DEP_ID).append(
							" or ").append(TableLfClientDep.PARENT_ID).append(
							" = c.").append(TableLfCliDepConn.DEP_ID).append(
							")").toString();
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfClientDep.PARENT_ID).append(" = ").append(depId)
					.toString();
		}
		List<LfClientDep> lfClientDepList = findEntityListBySQL(
				LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfClientDepList;
	}
	
	/**
	 * 获取客户机构列表——用于构建机构树
	 * 
	 * @param userId
	 *            当前操作员的ID
	 * @param depId
	 *            传NULL进来则通过userId去查，有传值时只查传入机构的子级
	 * @param corpCode   公司编码         
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(String userId,
			String depId,String corpCode) throws Exception
	{
		String sql = "";
		if (depId == null || "".equals(depId))
		{
			sql = new StringBuffer(" select e.* from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" e " + StaticValue.getWITHNOLOCK() + ",").append(
					TableLfCliDepConn.TABLE_NAME).append(
					" c " + StaticValue.getWITHNOLOCK()).append(" where c.").append(
					TableLfCliDepConn.USER_ID).append(" = ").append(userId)
					.append(" and (c.").append(TableLfEmpDepConn.DEP_ID)
					.append(" =e.").append(TableLfClientDep.DEP_ID).append(
							" or ").append(TableLfClientDep.PARENT_ID).append(
							" = c.").append(TableLfCliDepConn.DEP_ID).append(
							") and e."+TableLfClientDep.CORP_CODE+"='"+corpCode+"'").toString();
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfClientDep.PARENT_ID).append(" = ").append(depId).append(" and "+TableLfClientDep.CORP_CODE+"='"+corpCode+"'")
					.toString();
		}
		List<LfClientDep> lfClientDepList = findEntityListBySQL(
				LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfClientDepList;
	}
	
	/**
	 *   增加分页	查询员工群组中的内容
	 * @param groupId
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<GroupInfoVo> findGroupUserByIds(Long groupId,PageInfo pageInfo) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 0 then employee.").append(TableLfEmployee.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 0 then employee.").append(TableLfEmployee.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfEmployee.TABLE_NAME).append(" employee  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=employee.").append(TableLfEmployee.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		//查询
		String countSql = "select count(*) totalcount from ("+sqlStr.toString()+") a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc");
		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	
	public List<GroupInfoVo> findGroupClientByIds(Long groupId,PageInfo pageInfo) throws Exception{
		//初始化LIST
		List<GroupInfoVo> returnVoList = null;
		//拼凑查询字段
		StringBuffer sqlStr = new StringBuffer("select  list2gro.").append(TableLfList2gro.UDG_ID).append(",list2gro.").append(
				TableLfList2gro.L2G_ID).append(",list2gro.").append(TableLfList2gro.L2G_TYPE).append(",list2gro.").append(
				TableLfList2gro.GUID).append(",udgroup.").append(TableLfUdgroup.GP_ATTRIBUTE).append(",udgroup.").append(
				TableLfUdgroup.GROUP_TYPE).append(",udgroup.").append(TableLfUdgroup.USER_ID).append(",case(list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(") when 1 then client.").append(TableLfClient.MOBILE).append("  else malist.")
				.append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(
						") when 1 then client.").append(TableLfClient.NAME).append("  else malist.").append(TableLfMalist.NAME).append(
						" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(TableLfClient.TABLE_NAME).append(" client  ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=client.").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
						" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.UDG_ID).append(" where  udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(groupId);
		//查询
		String countSql = "select count(*) totalcount from ( " + sqlStr.toString() + " ) a";
		sqlStr.append( " order by list2gro.").append(TableLfList2gro.UDG_ID).append(" asc ");

		returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(GroupInfoVo.class, sqlStr.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		//返回
		return returnVoList;
	}
	/**
	 *    查询客户机构人员
	 * @param clientDep	当前机构对象
	 * @param containType	是否包含  1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientsByDepId(LfClientDep clientDep,Integer containType,PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		String sql = "select distinct client.NAME,client.MOBILE,client.GUID " ;
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
		StringBuffer conditionSql = new StringBuffer(" where  ");
		conditionSql.append("  client.CORP_CODE = '").append(clientDep.getCorpCode()).append("'");
		if(containType == 1){
			conditionSql.append(" and depsp.DEP_ID in (select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			conditionSql.append(" and depsp.DEP_ID = ").append(clientDep.getDepId());
		}
		String orderSql = " order by client.GUID DESC";
		sql += baseSql;
		countSql += baseSql;
		sql += conditionSql + orderSql;
		countSql += conditionSql;
		beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beanList;
	}
	/**
	 *    查询客户机构人员
	 * @param //clientDep	当前机构对象
	 * @param containType	是否包含  1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClients(String name,String lgcorpcode,Integer containType,PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		String sql = "select distinct client.NAME,client.MOBILE,client.GUID " ;
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
		StringBuffer conditionSql = new StringBuffer(" where  ");
		conditionSql.append("  client.CORP_CODE = '").append(lgcorpcode).append("'");
		if(name!=null){
			conditionSql.append(" and client.NAME like '%").append(name).append("%'");
		}
		String orderSql = " order by client.GUID DESC";
		sql += baseSql;
		countSql += baseSql;
		sql += conditionSql + orderSql;
		countSql += conditionSql;
		//解决了部分数据显示不全的问题
		beanList=new DataAccessDriver().getGenericDAO().findDynaBeanBySql(sql);
		return beanList;
	}
	
	/**
	 *   获取员工的手机号码
	 * @param depIdStr
	 * @param depPathList
	 * @return
	 * @throws Exception
	 */
	public String findEmployeePhoneByDepIds(String depIdStr,List<String> depPathList) throws Exception {
		//List<String> returnList=new ArrayList<String>();
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select emp.").append(TableLfEmployee.MOBILE).append(" from ")
		.append(TableLfEmployee.TABLE_NAME).append(" emp ").append(StaticValue.getWITHNOLOCK())
		.append(" where ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append("  emp.").append(TableLfEmployee.DEP_ID).append(" in ( ").append(depIdStr).append(" )");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" emp.").append(TableLfEmployee.DEP_ID).append(" in (select ")
				.append(depstr).append(".").append(TableLfEmployeeDep.DEP_ID).append(" from ")
				.append(TableLfEmployeeDep.TABLE_NAME).append(" ").append(depstr).append(StaticValue.getWITHNOLOCK()).append(" where ")
				.append(depstr).append(".").append(TableLfClientDep.DEP_PATH).append(" like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
		}
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.debug("获得数据库连接，执行查询出错！");
			throw e;
		}finally {
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	/**
	 *   处理客户机构查询其客户的手机号码的方法
	 * @param depIdStr	不包含的机构串
	 * @param depPathList	包含子机构的机构PATH
	 * @return
	 * @throws Exception
	 */
	public String getClientPhoneByDepIds(String depIdStr,List<String> depPathList) throws Exception {
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.MOBILE  from LF_CLIENT client ");
		buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
//		buffer.append(" where  1=1  and ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append(" where ( depsp.DEP_ID in (").append(depIdStr).append(")");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}else{
				buffer.append(" where (");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" depsp.DEP_ID in ( select ").append(depstr)
				.append(".DEP_ID from LF_CLIENT_DEP ").append(depstr)
				.append("  where ").append(depstr).append(".DEP_PATH like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
			buffer.append(" ) ");
		}else if(flag){
			buffer.append(" ) ");
		}
		
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.debug("处理客户机构查询其客户的手机号码的方法出错！");
			throw e;
		}finally {
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	/**
	 * 处理客户机构查询其客户的手机号码的方法 （方法重载，增加企业编码条件）
	 * @description    
	 * @param depIdStr
	 * @param depPathList
	 * @param corpCode
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-2-7 下午12:04:44
	 */
	public String getClientPhoneByDepIds(String depIdStr,List<String> depPathList, String corpCode) throws Exception {
		StringBuilder phoneStr = new StringBuilder();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		//示范sql语句
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.MOBILE  from LF_CLIENT client ");
		buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
		buffer.append(" where  client.CORP_CODE = '"+corpCode+"'  and ");
		boolean flag = false;
		if(!"".equals(depIdStr) && depIdStr.length() > 0){
			depIdStr = depIdStr.substring(0, depIdStr.length()-1);
			buffer.append("( depsp.DEP_ID in (").append(depIdStr).append(")");
			flag = true;
		}
		if(depPathList != null && depPathList.size() > 0){
			if(flag){
				buffer.append(" or ");
			}else{
				buffer.append("(");
			}
			for(int i=0;i<depPathList.size();i++){
				String depstr = "dep"+i;
				if(i > 0){
					buffer.append(" or ");
				}
				buffer.append(" depsp.DEP_ID in ( select ").append(depstr)
				.append(".DEP_ID from LF_CLIENT_DEP ").append(depstr)
				.append("  where ").append(depstr).append(".DEP_PATH like ")
				.append("'").append(depPathList.get(i)).append("%')");
			}
			buffer.append(" ) ");
		}else if(flag){
			buffer.append(" ) ");
		}
		
		try {
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + buffer.toString());
			ps = conn.prepareStatement(buffer.toString());
			//执行SQL
			rs = ps.executeQuery();
			while (rs.next()) {
				//获取手机号码
				//returnList.add(rs.getString(1));
				phoneStr.append(rs.getString(1)).append(",");
			}
		} catch (SQLException e) {
			EmpExecutionContext.debug("处理客户机构查询其客户的手机号码的方法出错！");
			throw e;
		}finally {
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	/**
	 *   获取机构客户 人数
	 * @param clientDep	机构对象
	 * @param containType 1包含  2是不包含
	 * @return
	 * @throws Exception
	 */
	public Integer findClientsCountByDepId(LfClientDep clientDep,Integer containType) throws Exception
	{
		StringBuffer sqlBuffer = new StringBuffer(" select COUNT(c.CLIENT_ID) as totalcount from ") ;
		sqlBuffer.append(" (select a.CLIENT_ID  from LF_ClIENT_DEP_SP a   ");
		if(containType == 1){
			sqlBuffer.append("where  a.DEP_ID in (select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			sqlBuffer.append("where  a.DEP_ID = ").append(clientDep.getDepId());
		}
		sqlBuffer.append(")c");
		
		Integer count = findCountBySQL(sqlBuffer.toString());
		
		return count;
	}

	public List<DynaBean> getGroupMemberByNameAndId(Long userId, LinkedHashMap<String, String> conditionMap) {
		//群组类型   1表示员工  2表示客户
		String type = conditionMap.get("groupType");
		String tableName = "1".equals(type) ? TableLfEmployee.TABLE_NAME : TableLfClient.TABLE_NAME;
		String flag = "1".equals(type) ? "0" : "1";
		String flagName = "1".equals(type) ? "employee" : "client";
		//群组Id
		String udgId = conditionMap.get("udgId");
		//名字
		String name = conditionMap.get("name");
		//拼凑查询字段
		StringBuilder sqlStr = new StringBuilder("select list2gro.").append(TableLfList2gro.GUID).append(",list2gro.").append(
				TableLfList2gro.L2G_TYPE).append(",case(list2gro.").append(TableLfList2gro.L2G_TYPE).append(") when ").append(flag).
				append(" then ").append(flagName).append(".").append(TableLfClient.MOBILE).
				append("  else malist.").append(TableLfMalist.MOBILE).append(" end as MOBILE,case(list2gro.").
				append(TableLfList2gro.L2G_TYPE).append(") when ").append(flag).append(" then ").append(flagName).
				append(".").append(TableLfClient.NAME).append("  else malist.").
				append(TableLfMalist.NAME).append(" end as NAME,udgroup.").append(TableLfUdgroup.UDG_NAME);
		//拼凑TABLE
		sqlStr.append(" from ").append(TableLfList2gro.TABLE_NAME).append(" list2gro " ).append(StaticValue.getWITHNOLOCK()).append(" left join ")
				.append(tableName).append(" ").append(flagName).append(" ").append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=").append(flagName).append(".").append(TableLfClient.GUID).append(" left join ").append(TableLfMalist.TABLE_NAME).append(
				" malist " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.GUID)
				.append("=malist.").append(TableLfMalist.GUID).append(" inner join ").append(TableLfUdgroup.TABLE_NAME)
				.append(" udgroup " ).append(StaticValue.getWITHNOLOCK()).append(" on list2gro.").append(TableLfList2gro.UDG_ID)
				.append("=udgroup.").append(TableLfUdgroup.GROUP_ID).append(" where  udgroup.")
				.append(TableLfUdgroup.UDG_ID).append("=").append(udgId);
		String finalSql = "select * from (" + sqlStr + ") a where a.name like '%" + name + "%'";
		return new DataAccessDriver().getGenericDAO().findDynaBeanBySql(finalSql);
	}
}
