package com.montnets.emp.group.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.group.vo.LfClientVo;
import com.montnets.emp.group.vo.LfEmployeeVo;
import com.montnets.emp.group.vo.LfList2groVo;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

public class GroupManagerDao extends SuperDAO {
	public List<LfSysuser> getLfSysuserListByDepIdCon(String depIdCon) throws Exception{
		String sql = "select * from " + TableLfSysuser.TABLE_NAME
				+ "  where " + depIdCon +" and "+TableLfSysuser.USER_STATE+"=1";
			return  findEntityListBySQL(LfSysuser.class, sql,
					StaticValue.EMP_POOLNAME);
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
     * @param sysuser
     * @param depId
     * @return
     * @throws Exception
     */
    public List<LfClientDep> getCliSecondDepTreeByUserIdorDepId(LfSysuser sysuser,
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
                    TableLfCliDepConn.USER_ID).append(" = ").append(sysuser.getUserId())
                    .append(" and (c.").append(TableLfEmpDepConn.DEP_ID)
                    .append(" =e.").append(TableLfClientDep.DEP_ID).append(
                            " or ").append(TableLfClientDep.PARENT_ID).append(
                            " = c.").append(TableLfCliDepConn.DEP_ID).append(")")
                    .append(" and e.").append(TableLfClientDep.CORP_CODE).append(" = '").append(sysuser.getCorpCode()).append("'").toString();
        } else
        {
            sql = new StringBuffer(" select * from ").append(
                    TableLfClientDep.TABLE_NAME).append(
                    " " + StaticValue.getWITHNOLOCK()).append(" where ").append(
                    TableLfClientDep.PARENT_ID).append(" = ").append(depId)
                    .append(" and ").append(TableLfClientDep.CORP_CODE).append(" = '").append(sysuser.getCorpCode()).append("'")
                    .toString();
        }
        List<LfClientDep> lfClientDepList = findEntityListBySQL(
                LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
        return lfClientDepList;
    }

	/**
	 * 通过机构IDs获取员工数据
	 * @param loginUserID 当前登录的操作员ID	
	 * @param corpCode 企业编码
	 * @param lfEmployeeVo 查询条件
	 * @param pageInfo 分页信息
	 * @return 员工信息集合
	 * @throws Exception
	 */
	public List<LfEmployeeVo> findEmployeeVoByDepIds(Long loginUserID,String corpCode,
			LfEmployeeVo lfEmployeeVo, PageInfo pageInfo) throws Exception {
		if(lfEmployeeVo.getDepIds() == null || "".equals(lfEmployeeVo.getDepIds()))
		{
			return new ArrayList<LfEmployeeVo>();
		}
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	   	 sql = "select employee.*,lfemployeedep."
				+ TableLfEmployeeDep.DEP_NAME;
	   	 	String depIds = new GroupManagerSQL().executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
	   	 conditionSql = getInConditionSql(conditionSql, depIds);
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select employee.*, lfemployeedep.DEP_NAME, ROW_NUMBER() Over(Order By lfemployeedep.corp_code) As rn";
			  conditionSql.append("and lfemployeedep.").append(TableLfEmployeeDep.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(lfEmployeeVo.getDepIds()).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
			 /*
			  * String depIds = executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
			  * */
			 String depIds = getEmpChildIdByDepId(lfEmployeeVo.getDepIds());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
			 String depIds = getEmpChildIdByDepId(lfEmployeeVo.getDepIds());
		   	 	if(depIds == null || depIds.length() == 0)
		   	 	{
		   	 		return new ArrayList<LfEmployeeVo>();
		   	 	}
			   	 conditionSql = getInConditionSql(conditionSql, depIds);
		}

		
		String countSql = "select count(*) totalcount";

		String tableSql = new StringBuffer(" from ").append(
				TableLfEmployee.TABLE_NAME).append(" employee inner join ")
				.append(TableLfEmployeeDep.TABLE_NAME).append(
						" lfemployeedep on employee.").append(
						TableLfEmployee.DEP_ID).append(" = lfemployeedep.")
				.append(TableLfEmployeeDep.DEP_ID).toString();

		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" where lfemployeedep.").append(TableLfEmployeeDep.CORP_CODE).append("= '").append(corpCode+"'")
			.append(" and employee.").append(TableLfEmployee.CORP_CODE).append("='").append(corpCode+"'").toString();
		
		sql += tableSql;
		sql +=moreWhere;
		countSql += tableSql;
		countSql +=moreWhere;
		
		
		
		if (null != lfEmployeeVo.getName()
				&& !"".equals(lfEmployeeVo.getName())) {
			conditionSql.append(" and employee.").append(TableLfEmployee.NAME)
					.append(" like '%").append(lfEmployeeVo.getName()).append(
							"%'");
		}
		if (null != lfEmployeeVo.getMobile()
				&& !"".equals(lfEmployeeVo.getMobile())) {
			conditionSql.append(" and employee.")
					.append(TableLfEmployee.MOBILE).append(" like '%").append(
							lfEmployeeVo.getMobile()).append("%'");
		}
		if (null != lfEmployeeVo.getUdgId()) {
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
					lfEmployeeVo.getUdgId()).toString();
			conditionSql.append(" and employee.").append(TableLfEmployee.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		if (moreWhere.length() == 0) {
			String conditionSqlStr = conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
			sql += conditionSqlStr;
			countSql += conditionSqlStr;
		} else {
			sql += conditionSql;
			countSql += conditionSql;
		}
		List<LfEmployeeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
						LfEmployeeVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	
	
	//写一个不需要分页的获取员工机构下面所有员工的方法
	public List<LfEmployeeVo> findEmployeeVoByDepIds(Long loginUserID,String corpCode,
			LfEmployeeVo lfEmployeeVo) throws Exception {
		if(lfEmployeeVo.getDepIds() == null || "".equals(lfEmployeeVo.getDepIds()))
		{
			return new ArrayList<LfEmployeeVo>();
		}
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
		   	String depIds = new GroupManagerSQL().executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select employee.*, lfemployeedep.DEP_NAME, ROW_NUMBER() Over(Order By lfemployeedep.corp_code) As rn";
			  conditionSql.append("and lfemployeedep.").append(TableLfEmployeeDep.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(lfEmployeeVo.getDepIds()).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
			 /*
			  * String depIds = executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
			  * */
			 String depIds = getEmpChildIdByDepId(lfEmployeeVo.getDepIds());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
		   	 conditionSql = getInConditionSql(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
		}

		String tableSql = new StringBuffer(" from ").append(
				TableLfEmployee.TABLE_NAME).append(" employee inner join ")
				.append(TableLfEmployeeDep.TABLE_NAME).append(
						" lfemployeedep on employee.").append(
						TableLfEmployee.DEP_ID).append(" = lfemployeedep.")
				.append(TableLfEmployeeDep.DEP_ID).toString();

		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" where lfemployeedep.").append(TableLfEmployeeDep.CORP_CODE).append("= '").append(corpCode+"'")
			.append(" and employee.").append(TableLfEmployee.CORP_CODE).append("='").append(corpCode+"'").toString();
		
		sql += tableSql;
		sql +=moreWhere;
		
		if (null != lfEmployeeVo.getName()
				&& !"".equals(lfEmployeeVo.getName())) {
			conditionSql.append(" and employee.").append(TableLfEmployee.NAME)
					.append(" like '%").append(lfEmployeeVo.getName()).append(
							"%'");
		}
		if (null != lfEmployeeVo.getMobile()
				&& !"".equals(lfEmployeeVo.getMobile())) {
			conditionSql.append(" and employee.")
					.append(TableLfEmployee.MOBILE).append(" like '%").append(
							lfEmployeeVo.getMobile()).append("%'");
		}
		if (null != lfEmployeeVo.getUdgId()) {
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
					lfEmployeeVo.getUdgId()).toString();
			conditionSql.append(" and employee.").append(TableLfEmployee.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		if (moreWhere.length() == 0) {
			sql += conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		} else {
			sql += conditionSql;
		}
		List<LfEmployeeVo> returnVoList = findVoListBySQL(
				LfEmployeeVo.class, sql, StaticValue.EMP_POOLNAME, null);
		return returnVoList;
	}
	
	
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
				sql = "select dep_Id from lf_employee_dep where PARENT_ID in ( "+conditionDepid+")";
				ps = conn.prepareStatement(sql);
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
			EmpExecutionContext.error(e, "群组查询失败！");
			
		} finally
		{

			close(rs, ps, conn);
		}
		return depIds;	
	}
	
	/**
	 * 通过机构IDs获取客户数据
	 * @param loginUserID 当前登录的操作员ID	
	 * @param corpCode 企业编码
	 * @param pageInfo 分页信息
	 * @return 员工信息集合
	 * @throws Exception
	 */
	public List<LfClientVo> findClientVoByDepIds(Long loginUserID,String corpCode,
			LfClientVo lfClientVo, PageInfo pageInfo) throws Exception {
		if(lfClientVo.getDepId() == null)
		{
			return new ArrayList<LfClientVo>();
		}
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	   	 sql = "select client.*,lfclientdep."
				+ TableLfClientDep.DEP_NAME;
	   	 	String depIds = new GroupManagerSQL().executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETCLIDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfClientVo.getDepId().toString())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfClientVo>();
	   	 	}
	   	 conditionSql = getInConditionSql2(conditionSql, depIds);
	   	// conditionSql.append(new GenericLfClientVoDAO().getClientChildByParentID(lfClientVo.getDepId(), "client."+TableLfClient.DEP_ID));
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select client.*, lfclientdep.DEP_NAME, ROW_NUMBER() Over(Order By lfclientdep.corp_code) As rn";
			  conditionSql.append("and lfclientdep.").append(TableLfClientDep.DEP_ID)
			  	.append(" in ( select DepId from GetCliDepChildByPID(1,").append(lfClientVo.getDepId()).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select client.*,lfclientdep."
					+ TableLfClientDep.DEP_NAME;
			 String depIds = new GroupManagerSQL().executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETCLIDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(lfClientVo.getDepId().toString())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfClientVo>();
	   	 	}
		   	 conditionSql = getInConditionSql2(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select client.*,lfclientdep." + TableLfClientDep.DEP_NAME;
			 String depIds =  new GroupManagerSQL().getClientChildIdByDepId(lfClientVo.getDepId().toString());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfClientVo>();
	   	 	}
		   	 conditionSql = getInConditionSql2(conditionSql, depIds);
		   	 
		}

		
		String countSql = "select count(*) totalcount";

		String tableSql = new StringBuffer(" from ").append(
				TableLfClient.TABLE_NAME).append(" client inner join ")
				.append(TableLfClientDep.TABLE_NAME).append(
						" lfclientdep on client.").append(
						TableLfClient.DEP_ID).append(" = lfclientdep.")
				.append(TableLfClientDep.DEP_ID).toString();

		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" where lfclientdep.").append(TableLfClientDep.CORP_CODE).append("= '").append(corpCode+"'")
			.append(" and client.").append(TableLfClient.CORP_CODE).append("='").append(corpCode+"'").toString();
		
		sql += tableSql;
		sql +=moreWhere;
		countSql += tableSql;
		countSql +=moreWhere;
		
		
		
		if (null != lfClientVo.getName()
				&& !"".equals(lfClientVo.getName())) {
			conditionSql.append(" and client.").append(TableLfClient.NAME)
					.append(" like '%").append(lfClientVo.getName()).append(
							"%'");
		}
		if (null != lfClientVo.getMobile()
				&& !"".equals(lfClientVo.getMobile())) {
			conditionSql.append(" and client.")
					.append(TableLfClient.MOBILE).append(" like '%").append(
							lfClientVo.getMobile()).append("%'");
		}
		if (null != lfClientVo.getUdgId()) {
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
							lfClientVo.getUdgId()).toString();
			conditionSql.append(" and client.").append(TableLfClient.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		if(moreWhere.length()==0){
			String conditionSqlStr = conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
			sql += conditionSqlStr;
			countSql += conditionSqlStr;
		}else{
			sql += conditionSql;
			countSql += conditionSql;
		}
		List<LfClientVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfClientVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	
	
	//写一个不需要分页的获取客户机构下面所有客户的方法
	public List<LfClientVo> findClientVoByDepIds(Long loginUserID,String corpCode,
			LfClientVo lfClientVo) throws Exception {
		if(lfClientVo.getDepId() == null)
		{
			return new ArrayList<LfClientVo>();
		}
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	 sql = "select client.*,lfclientdep."
					+ TableLfClientDep.DEP_NAME;
		   	String depIds = new GroupManagerSQL().executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfClientVo.getDepId().toString())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfClientVo>();
	   	 	}
		   	 conditionSql = getInConditionSql2(conditionSql, depIds);
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select client.*, lfclientdep.DEP_NAME, ROW_NUMBER() Over(Order By lfclientdep.corp_code) As rn";
			  conditionSql.append("and lfclientdep.").append(TableLfClientDep.DEP_ID)
			  	.append(" in ( select DepId from GetCliDepChildByPID(1,").append(lfClientVo.getDepId()).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select client.*,lfclientdep."
					+ TableLfClientDep.DEP_NAME;
			 String depIds = new GroupManagerSQL().executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETCLIDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(lfClientVo.getDepId().toString())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfClientVo>();
	   	 	}
		   	 conditionSql = getInConditionSql2(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select client.*,lfclientdep."+ TableLfClientDep.DEP_NAME;
			 String depIds = new GroupManagerSQL().getClientChildIdByDepId(lfClientVo.getDepId().toString());
		   	 	if(depIds == null || depIds.length() == 0)
		   	 	{
		   	 		return new ArrayList<LfClientVo>();
		   	 	}
			   	 conditionSql = getInConditionSql2(conditionSql, depIds);
		}

		String tableSql = new StringBuffer(" from ").append(
				TableLfClient.TABLE_NAME).append(" client inner join ")
				.append(TableLfClientDep.TABLE_NAME).append(
						" lfclientdep on client.").append(
						TableLfClient.DEP_ID).append(" = lfclientdep.")
				.append(TableLfClientDep.DEP_ID).toString();

		String moreWhere = "";
		if(corpCode!=null){
			moreWhere = new StringBuffer(" where lfclientdep.").append(TableLfClientDep.CORP_CODE).append("= '").append(corpCode + "'")
					.append(" and client.").append(TableLfClient.CORP_CODE).append("='").append(corpCode + "'").toString();
		}
		sql += tableSql;
		sql +=moreWhere;
		
		if (null != lfClientVo.getName()
				&& !"".equals(lfClientVo.getName())) {
			conditionSql.append(" and client.").append(TableLfClient.NAME)
					.append(" like '%").append(lfClientVo.getName()).append(
							"%'");
		}
		if (null != lfClientVo.getMobile()
				&& !"".equals(lfClientVo.getMobile())) {
			conditionSql.append(" and client.")
					.append(TableLfClient.MOBILE).append(" like '%").append(
							lfClientVo.getMobile()).append("%'");
		}
		if (null != lfClientVo.getUdgId()) {
			String guidSql = new StringBuffer("select ").append(
					TableLfList2gro.GUID).append(" from ").append(
					TableLfList2gro.TABLE_NAME).append(" where ").append(
					TableLfList2gro.UDG_ID).append("=").append(
							lfClientVo.getUdgId()).toString();
			conditionSql.append(" and client.").append(TableLfClient.GUID)
					.append(" not in (").append(guidSql).append(")");
		}
		if(moreWhere.length()==0){
			sql += conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		}else{
			sql += conditionSql;
		}
		List<LfClientVo> returnVoList = findVoListBySQL(
				LfClientVo.class, sql, StaticValue.EMP_POOLNAME, null);
		return returnVoList;
	}
	
	private StringBuffer getInConditionSql2(StringBuffer conditionSql,String depIds)
	{
		/*******机构in查询处理***********/
		//int maxCount = StaticValue.inConditionMax;//in查询最大个数限制
		int maxCount = StaticValue.getInConditionMax();//in查询最大个数限制
		String[] depIdArry = depIds.split(",");
		int size = depIdArry.length;
		if(size>maxCount)
		{
			conditionSql.append(" and (lfclientdep.").append(
					TableLfClient.DEP_ID).append(" in (");
			String inString = "";
			for(int i=0;i<size;i++)
			{
				inString +=  depIdArry[i] ;
				if( i > maxCount-2 && (i+1)%maxCount==0  && i<size - 1  )
				{
					conditionSql.append(inString).append(") or ").append(" lfclientdep.").append(
							TableLfClient.DEP_ID).append(" in (");
					inString = "";
				}else if( i<size - 1 )
				{
					inString += ",";
				}
			}
			conditionSql.append(inString).append("))");
		}else
		{
		
			conditionSql.append(" and lfclientdep.").append(
					TableLfClient.DEP_ID).append(" in (").append(depIds).append(")");
		}
		/*******END-机构in查询处理**********/
		
		return conditionSql;
	}
	
	
	public List<LfList2groVo> findCliShowMember(LfList2groVo vo,Long udgid) throws Exception
	{
		//查询lfemployee表
		StringBuffer querySql = new StringBuffer("select client.guid,NAME,mobile,l2g_type,share_type from ")
		   					  .append(TableLfClient.TABLE_NAME).append(" client ").append(StaticValue.getWITHNOLOCK())
		   					  .append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
		   					  .append("client.guid=list2gro.guid ");
		//查询lfmalist表
		StringBuffer querySql2 = new StringBuffer("select malist.guid,NAME,mobile,l2g_type,share_type from ")
			  .append(TableLfMalist.TABLE_NAME).append(" malist").append(StaticValue.getWITHNOLOCK())
			  .append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
			  .append("malist.guid=list2gro.guid ");
		
		StringBuffer whereSql = new StringBuffer(" where ").append("udg_id=").append(udgid);
		//加入查询条件
		if(vo.getName()!=null)
		{
			whereSql.append(" and ").append(TableLfClient.NAME).append(" like ").append("'%").append(vo.getName()).append("%'");
		}
		if(vo.getMobile()!=null)
		{
			whereSql.append(" and ").append(TableLfClient.MOBILE).append(" like ").append("'%").append(vo.getMobile()).append("%'");
		}
		List<LfList2groVo> vos =new ArrayList<LfList2groVo>();
		vos=findVoListBySQL(LfList2groVo.class, querySql.append(whereSql).toString(), StaticValue.EMP_POOLNAME);
		List<LfList2groVo> vos2 =findVoListBySQL(LfList2groVo.class, querySql2.append(whereSql).toString(), StaticValue.EMP_POOLNAME);
		//查询出来的结果合并
		vos.addAll(vos2);
		return vos;
	}

	/**
	 * 查询客户群组成员 分页
	 * @param pageInfo
	 * @param vo
	 * @param udgid
	 * @return
	 * @throws Exception
	 */
	public List<LfList2groVo> findCliShowMember(PageInfo pageInfo,LfList2groVo vo,Long udgid) throws Exception
	{
		//查询lfemployee表
		StringBuffer querySql = new StringBuffer("select client.guid,NAME,mobile,l2g_type,share_type from ")
				.append(TableLfClient.TABLE_NAME).append(" client ").append(StaticValue.getWITHNOLOCK())
				.append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
				.append("client.guid=list2gro.guid ");
		//查询lfmalist表
		StringBuffer querySql2 = new StringBuffer("select malist.guid,NAME,mobile,l2g_type,share_type from ")
				.append(TableLfMalist.TABLE_NAME).append(" malist").append(StaticValue.getWITHNOLOCK())
				.append(" inner join ").append(TableLfList2gro.TABLE_NAME).append(" list2gro ").append(StaticValue.getWITHNOLOCK()).append("on ")
				.append("malist.guid=list2gro.guid ");

		StringBuffer whereSql = new StringBuffer(" where ").append("udg_id=").append(udgid);
		//加入查询条件
		if(vo.getName()!=null)
		{
			whereSql.append(" and ").append(TableLfClient.NAME).append(" like ").append("'%").append(vo.getName()).append("%'");
		}
		if(vo.getMobile()!=null)
		{
			whereSql.append(" and ").append(TableLfClient.MOBILE).append(" like ").append("'%").append(vo.getMobile()).append("%'");
		}
		String unionSql = "("+querySql+whereSql+" union all "+querySql2+whereSql+") temp";
		String sql = "select guid,NAME,mobile,l2g_type,share_type from "+unionSql;
		String countSql = "select count(*) totalcount from "+unionSql;
		List<LfList2groVo> vos =  new DataAccessDriver().getGenericDAO().findPageVoListBySQL(LfList2groVo.class,sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return vos;
	}
	
	
	/**
	 *   查 客户机构下的客户
	 * @param clientDep	客户机构对象
	 * @param LfClient	客户
	 * @param containType	是否包含   1包含   2不包含
	 * @param pageInfo	分页
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findClientsByDepId(LfClientDep clientDep,LfClient LfClient,Integer containType,PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beanList = null;
		String sql = "select client.NAME,client.MOBILE,client.GUID,depsp.DEP_ID" ;
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_CLIENT client inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ";
		StringBuffer conditionSql = new StringBuffer("");
		conditionSql.append(" where client.CORP_CODE = '").append(LfClient.getCorpCode()).append("'");
		
		if(LfClient.getName() != null && !"".equals(LfClient.getName())){
			conditionSql.append(" and client.NAME like '%").append(LfClient.getName()).append("%'");
		}
		if(clientDep != null){
			if(containType == 1){
				conditionSql.append(" and depsp.DEP_ID in (select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
			}else if(containType == 2){
				conditionSql.append(" and depsp.DEP_ID = ").append(clientDep.getDepId());
			}
		}
		String orderSql = " order by client.CLIENT_ID DESC";
		sql += baseSql;
		countSql += baseSql;
		sql += conditionSql + orderSql;
		countSql += conditionSql;
		beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		return beanList;
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
	/*	select COUNT(c.CLIENT_ID) from 
		(select DISTINCT a.CLIENT_ID  from LF_ClIENT_DEP_SP a where a.DEP_ID in
			(select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH LIKE '1/%')
		)c*/
		StringBuffer sqlBuffer = new StringBuffer(" select COUNT(c.CLIENT_ID) as totalcount from ") ;
		sqlBuffer.append(" (select  a.CLIENT_ID  from LF_ClIENT_DEP_SP a ");
		if(containType == 1){
			sqlBuffer.append(" where a.DEP_ID in (select b.DEP_ID from LF_CLIENT_DEP b where b.DEP_PATH like '").append(clientDep.getDeppath()).append("%') ");
		}else if(containType == 2){
			sqlBuffer.append(" where a.DEP_ID = ").append(clientDep.getDepId());
		}
		sqlBuffer.append(")c");
		
		Integer count = findCountBySQL(sqlBuffer.toString());
		
		return count;
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
		//示范sql语句
		/*select distinct client.MOBILE  from LF_CLIENT client 
			inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID 
			where client.CORP_CODE = '100001'
			and (depsp.DEP_ID in (1,2,3) or  depsp.DEP_ID in 
			(select DEP_ID from LF_CLIENT_DEP where DEP_PATH like '1/%') );*/
		StringBuffer buffer = new StringBuffer();
		buffer.append(" select client.MOBILE  from LF_CLIENT client ");
		buffer.append(" inner join LF_ClIENT_DEP_SP depsp on client.CLIENT_ID = depsp.CLIENT_ID ");
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
			EmpExecutionContext.error(e, "处理客户机构查询其客户手机号码数据库操作异常！");
			throw e;
		}finally {
			//关闭数据库资源
			close(rs, ps, conn);
		}
		//返回结果集
		return phoneStr.toString();
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
