package com.montnets.emp.employee.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.employee.vo.LfEmpDepConnVo;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.sysuser.vo.LfSysuser2Vo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.employee.TableLfEmployeeType;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.StringUtils;

/**
 * 
 * @project emp
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime
 * @description
 */
public class GenericLfEmployeeVoDAO extends AddrBookSuperDAO
{

	public List<LfEmployeeVo> findEmployeeVo(String corpCode) throws Exception 
	{
		//查询sql拼接
		String sql=new StringBuffer("select employee.*, employeeDep.").append(
		TableLfEmployeeDep.DEP_NAME).append(" from ").append(
		TableLfEmployee.TABLE_NAME).append(" employee inner join ").append(
		TableLfEmployeeDep.TABLE_NAME).append(" employeeDep on employee.").append(
		TableLfEmployee.DEP_ID).append(" = employeeDep.").append(
	    TableLfEmployeeDep.DEP_ID).append(" where employeeDep.").append(
	    TableLfEmployeeDep.CORP_CODE).append("='").append(corpCode+"'").append("and employee.").append(
	    TableLfEmployee.CORP_CODE).append("='").append(corpCode+"'").toString();
		
		List<LfEmployeeVo> employeeVoList = findVoListBySQL(LfEmployeeVo.class, sql, StaticValue.EMP_POOLNAME);		
		return employeeVoList;
	}	
	public List<LfEmployeeVo> findEmployeeVo(Long loginUserID,String corpCode,
			LfEmployeeVo lfEmployeeVo, PageInfo pageInfo) throws Exception {
		
		String depConnSql = new StringBuffer(" select ").append(
				TableLfEmpDepConn.DEP_ID).append(" from ").append(
				TableLfEmpDepConn.TABLE_NAME).append(" where ").append(
				TableLfEmpDepConn.USER_ID).append(" = ").append(loginUserID)
				.toString();
		List<LfEmpDepConn> codeDepConnsList = findPartEntityListBySQL(
				LfEmpDepConn.class, depConnSql, StaticValue.EMP_POOLNAME);

		if (codeDepConnsList == null || codeDepConnsList.size() == 0) {
			return new ArrayList<LfEmployeeVo>();
		}

		String depConnID = codeDepConnsList.get(0).getDepId().toString();//获取登录操作员所绑定的机构ID
		

		String sql = "";
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	   	 sql = "select employee.*,lfemployeedep."
				+ TableLfEmployeeDep.DEP_NAME;
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select employee.*, lfemployeedep.DEP_NAME, ROW_NUMBER() Over(Order By lfemployeedep.corp_code) As rn";
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
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
		StringBuffer conditionSql = new StringBuffer();
		/*if (null != lfEmployeeVo.getDepCode()
				&& !"".equals(lfEmployeeVo.getDepCode())) {
			conditionSql.append(" and employee.").append(
					TableLfEmployee.DEP_CODE).append(" like '").append(
					EmpUtils.getPartDepCode(lfEmployeeVo.getDepCode())).append(
					"%'");
		} else {
			conditionSql.append(" and employee.").append(
					TableLfEmployee.DEP_CODE).append(" like '").append(
					EmpUtils.getPartDepCode(depConnCode)).append("%'");
		}*/

        String depIdTemp = null;
		
		if (null != lfEmployeeVo.getDepId())
		{//查询选择的机构的子机构
			depIdTemp = lfEmployeeVo.getDepId().toString();
		} 
		else//没带depId条件，则查绑定机构的子机构
		{
			depIdTemp = depConnID;
		}
		conditionSql.append(" and (")
					.append(getEmployeeChildDepByParentID(Long.parseLong(depIdTemp),"employee."+TableLfEmployee.DEP_ID))
					.append(") ");//获取子机构，包含自己

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
		List<LfEmployeeVo> returnVoList = findPageVoListBySQL(
						LfEmployeeVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	
	
	//获得分页时的员工通讯录类型
	public List<LfEmployeeTypeVo> findEmployeeTypeVo(long uid,String corpCode,PageInfo pageInfo)
	throws Exception {
		
		String fieldSql = "";
		
		if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
			fieldSql = new StringBuffer("select * ").toString();
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			fieldSql = new StringBuffer("select * ").append(",ROW_NUMBER() Over(Order By corp_code) As rn").toString();
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			fieldSql = new StringBuffer("select * ").toString();
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			fieldSql = new StringBuffer("select * ").toString();
		}
	 	
		
		String countfieldsql = new StringBuffer("select count(*) totalcount").toString();
		String tableSql = new StringBuffer(" from ").append(TableLfEmployeeType.TABLE_NAME).toString();
		String whereSql = "";
		if(corpCode!=null)
			whereSql =  new StringBuffer(" ").append(" where ").append(TableLfEmployeeType.CORP_CODE).append(" = '").append(corpCode+"'").toString();
		else
			whereSql =  new StringBuffer(" ").append(" where ").append(TableLfEmployeeType.CORP_CODE).append(" is null ").toString();
		
		String sql = new StringBuffer(fieldSql).append(tableSql).append(whereSql).toString();	
		
		String countsql = new StringBuffer(countfieldsql).append(tableSql).append(whereSql).toString();	
		
		List<LfEmployeeTypeVo> returnVoList = findVoTypeListBySQL(LfEmployeeTypeVo.class,pageInfo, sql
				.toString(),countsql, StaticValue.EMP_POOLNAME,null,null);
	
		
		return returnVoList;
	}
	
	//获得所有员工通讯录类型
	public List<LfEmployeeTypeVo> findAllEmployeeTypeVo(long uid,String corpCode)
	throws Exception {
		
		String fieldSql = new StringBuffer("select * ").toString();
		String tableSql = new StringBuffer(" from ")
		.append(TableLfEmployeeType.TABLE_NAME).toString();
		String whereSql ="";
		if(corpCode!=null)
			whereSql =  new StringBuffer(" ").append(" where ").append(TableLfEmployeeType.CORP_CODE).append(" = '").append(corpCode+"'").toString();
		else
			whereSql =  new StringBuffer(" ").append(" where ").append(TableLfEmployeeType.CORP_CODE).append(" is null ").toString();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(whereSql).toString();	
		
		List<LfEmployeeTypeVo> returnVoList = findAllVoTypeListBySQL(LfEmployeeTypeVo.class, sql
				.toString(), StaticValue.EMP_POOLNAME,null,null);
	
		
		return returnVoList;
	}
	
	
	//增加员工通讯录类型
	public boolean addEmployeeTypeVo(long uid,String name,String corp_code)
	throws Exception {
		
		String fieldSql = new StringBuffer("insert into ").toString();
		String tableSql = "";
		if(corp_code!=null){
		 tableSql = new StringBuffer("LF_EMPLOYEE_TYPE (").append(TableLfEmployeeType.NAME).append(",").append(TableLfEmployeeType.USER_ID).append(",").append(TableLfEmployeeType.CORP_CODE)
		.append(")").append("values(").append("'").append(name).append("',").append(uid).append(",'").append(corp_code).append("')").toString();
		}
		else {
			tableSql = new StringBuffer("LF_EMPLOYEE_TYPE (").append(TableLfEmployeeType.NAME).append(",").append(TableLfEmployeeType.USER_ID)
			.append(")").append("values(").append("'").append(name).append("',").append(uid).append(")").toString();
		}
		
		String sql = new StringBuffer(fieldSql).append(tableSql).toString();
		
		boolean b = executeBySQL(sql, StaticValue.EMP_POOLNAME);
	
		return b;
	}
	
	public boolean delEmployeeTypeVo(String corpCode,int id)
	throws Exception {
		
		
		String fieldSql = new StringBuffer("delete").toString();
		
		String tableSql = new StringBuffer(" from ")
		.append(TableLfEmployeeType.TABLE_NAME).append(" where ").append(TableLfEmployeeType.ID).append(" = ")
		.append(id).append(" and ").append(TableLfEmployeeType.CORP_CODE).append(" = '").append(corpCode+"'")
				.toString();
		
		String sql = new StringBuffer(fieldSql).append(tableSql).toString();		
		
		boolean b = executeBySQL(sql, StaticValue.EMP_POOLNAME);
	
		return b;
	}
	
	public boolean updEmployeeTypeVo(long uid,String corpCode,int id,String typename)
	throws Exception {
		
		
		String fieldSql = new StringBuffer("update").toString();
		
		String tableSql = new StringBuffer(" ")
		.append(TableLfEmployeeType.TABLE_NAME).append(" set ").append(TableLfEmployeeType.NAME).append(" = '")
		.append(typename).append("' ,").append(TableLfEmployeeType.USER_ID).append("=").append(uid).append(" where ").append(TableLfEmployeeType.CORP_CODE).append(" = '").append(corpCode).append("' and ").append(TableLfEmployeeType.ID)
		.append("=").append(id).toString();
		
		String sql = new StringBuffer(fieldSql).append(tableSql).toString();		
		
		boolean b = executeBySQL(sql, StaticValue.EMP_POOLNAME);
	
		return b;
	}
	
	
	public boolean filtAdmin(long conn_id,String corpCode)
	throws Exception {
		String fieldSql = new StringBuffer("select  ").append("*").toString();
		String tableSql = new StringBuffer(" from ")
		.append(TableLfEmpDepConn.TABLE_NAME).toString();
		String whereSql ="";
			whereSql =  new StringBuffer(" ").append(" where ").append(TableLfEmpDepConn.CONN_ID).append(" = ").append(conn_id).toString();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(whereSql).toString();	
		
		List<LfEmpDepConnVo> returnVoList = findAllVoTypeListBySQL(LfEmpDepConnVo.class, sql
				.toString(), StaticValue.EMP_POOLNAME,null,null);
		String sqlDep = "select * from "+TableLfEmployeeDep.TABLE_NAME +" where "+TableLfEmployeeDep.DEP_ID+"="+returnVoList.get(0).getDep_id();

		List<LfEmployeeDep> empDep = findEntityListBySQL(LfEmployeeDep.class, sqlDep, StaticValue.EMP_POOLNAME);
		List<LfSysuser2Vo> returnVoList2 = new ArrayList();
		if(empDep.get(0).getParentId()==0&& StaticValue.getCORPTYPE() ==1){//多企业版
			long uid = returnVoList.get(0).getUser_id();
			String fieldSql2 = new StringBuffer("select  ").append(" * ").toString();
			String tableSql2 = new StringBuffer(" from ")
			.append(TableLfSysuser.TABLE_NAME).toString();
			String whereSql2 ="";
				whereSql2 =  new StringBuffer(" ").append(" where ").append(TableLfSysuser.HOLDER).append(" = 'sysadmin'  and ").append(TableLfSysuser.USER_ID)
				.append(" = ").append(uid).append(" and ").append(TableLfSysuser.CORP_CODE).append("='").append(corpCode+"'").toString();
				toString();
			String sql2 = new StringBuffer(fieldSql2).append(tableSql2).append(whereSql2).toString();	
			returnVoList2 = findAllVoTypeListBySQL(LfSysuser2Vo.class, sql2
					.toString(), StaticValue.EMP_POOLNAME,null,null);
		}
		else if(empDep.get(0).getParentId()== 0&& StaticValue.getCORPTYPE() ==0){ //单企业版
			long uid = returnVoList.get(0).getUser_id();
			String fieldSql2 = new StringBuffer("select  ").append(" * ").toString();
			String tableSql2 = new StringBuffer(" from ")
			.append(TableLfSysuser.TABLE_NAME).toString();
			String whereSql2 ="";
				whereSql2 =  new StringBuffer(" ").append(" where ").append(TableLfSysuser.USER_NAME).append(" = 'admin'  and ").append(TableLfSysuser.USER_ID)
				.append(" = ").append(uid).append(" and ").append(TableLfSysuser.CORP_CODE).append("='").append(corpCode+"'").toString();
				toString();
			String sql2 = new StringBuffer(fieldSql2).append(tableSql2).append(whereSql2).toString();	
			returnVoList2 = findAllVoTypeListBySQL(LfSysuser2Vo.class, sql2
					.toString(), StaticValue.EMP_POOLNAME,null,null);
			
		}
		if(returnVoList2.size()>0)
			return false;
		else 
			return true;
		
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
	   	 	String depIds = executeProcessReutrnCursorOfOracle(
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
		List<LfEmployeeVo> returnVoList = findPageVoListBySQL(
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
		   	String depIds = executeProcessReutrnCursorOfOracle(
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
			 /*String depIds = executeProcessReutrnCursorOfMySql(
	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	 			new Integer[]{1,Integer.valueOf(depId)});*/
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
			moreWhere = new StringBuffer(" where lfemployee.").append(TableLfEmployee.CORP_CODE).append("= '").append(corpCode+"'").toString();
		
		sql += tableSql;
		sql +=moreWhere;
		if (moreWhere.length() == 0) {
			sql += conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		} else {
			sql += conditionSql;
		}
		List<LfEmployee> returnList =findPartEntityListBySQL(LfEmployee.class, sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	//写一个不需要分页的获取员工机构下面所有子机构的方法
	public List<LfEmployeeDep> findEmployeeDepsByDepId(String corpCode,String depId) throws Exception {
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	sql = "select lfemployeedep.*";
		   	String depIds = executeProcessReutrnCursorOfOracle(
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
			 /*String depIds = executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(depId)});*/
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
				TableLfEmployeeDep.TABLE_NAME).append(" lfemployeeDep ").toString();

		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" where lfemployeedep.").append(TableLfEmployeeDep.CORP_CODE).append("= '").append(corpCode+"'").toString();
		
		sql += tableSql;
		sql +=moreWhere;
		if (moreWhere.length() == 0) {
			sql += conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		} else {
			sql += conditionSql;
		}
		List<LfEmployeeDep> returnVoList = findEntityListBySQL(LfEmployeeDep.class, sql, StaticValue.EMP_POOLNAME);
		return returnVoList;
	}
	/**
	 * 通过机构Id删除群组信息
	 * @param depId 机构Id
	 * @return 执行个数
	 * @throws Exception
	 */
	public int delUdgEmployeeByDepId(Connection conn,String depId) throws Exception
	{
		String sql = "delete from "+TableLfList2gro.TABLE_NAME+" where "+TableLfList2gro.L2G_TYPE+"=0 and "
			+TableLfList2gro.GUID+" in (select "+TableLfEmployee.GUID+" from "+TableLfEmployee.TABLE_NAME
			+" where "+TableLfEmployee.DEP_ID+"="+depId+")";
		
		return executeBySQLReturnCount(conn,sql);
	}
	
	//根据机构id获取所有子员工机构
	public String getEmployeeChildDepByParentID(Long depId) throws Exception{
		List<String> childList=null;
		String returnStr="";
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			childList= getChildByParentIDOracle(depId);
		}else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
			childList=getChildByParentIDSqlServer(depId);
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			childList=getChildByParentIDMySql(depId);
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			
		}
		if(childList != null) {
			returnStr = completeChild(childList);
		}
		return returnStr;
	}
	
	public String getEmployeeChildDepByParentID(Long depId, String refName) throws Exception{
		
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)
		{//oracle
			return this.getChildByParentIDOracle(depId, refName);
		}
		else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)
		{//sqlserver
			String sql = new StringBuffer(refName)
						.append(" in ( ").append("select DepID from GetEmpDepChildByPID(1,")
						.append(depId).append(") ) ").toString();//获取子机构，包含自己

			return sql;
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{//mysql
			return this.getChildByParentIDMySql(depId, refName);
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{//db2
			
		}
		return null;
	}
	
	private String completeChild(List<String> childList){
		StringBuffer childSb=new StringBuffer();
 
		for (int i = 0; i <childList.size(); i++)
		{
			if(i!=childList.size()-1){
			    childSb.append(TableLfDep.DEP_ID).append(" in (").append(childList.get(i)).append(") or ");
			}else{
				childSb.append(TableLfDep.DEP_ID).append(" in (").append(childList.get(i)).append(")");
			}
		}
		return childSb.toString();
	}
	
	//根据机构id获取所有子员工机构orcl
	public List<String> getChildByParentIDOracle(Long depId) throws Exception {
		List<String> returnList=new ArrayList<String>();
		Connection conn = null;
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer();
		CallableStatement proc = null;
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			proc = conn
					.prepareCall("{ call GETCLIDEPCHILDBYPID(?,?,?,?) }");
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
						deps=new StringBuffer();
				}
			}
			returnList.add(deps.toString());
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取员工机构的所有子机构失败！");
		} finally {
			try {
				close(rs, proc, conn);
			} catch (SQLException e) {
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return returnList;
	}
	
	public String getChildByParentIDOracle(Long depId, String refName) throws Exception {
		Connection conn = null;
		ResultSet rs = null;
		String depIds = "";
		CallableStatement proc = null;
		
		try 
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			
			proc = conn.prepareCall("{ call GETCLIDEPCHILDBYPID(?,?,?,?) }");
			proc.setInt(1, 1);
			proc.setLong(2, depId);
			proc.setString(3, StringUtils.getRandom());
			proc.registerOutParameter(4, oracle.jdbc.OracleTypes.CURSOR);
			proc.execute();
			rs = (ResultSet) proc.getObject(4);
			
			depIds = parseRS(rs, refName);
		} 
		catch (Exception e) 
		{
			EmpExecutionContext.error(e,"执行存储过程失败！");
			throw e;
		} 
		finally 
		{
			try 
			{
				close(rs, proc, conn);
			} 
			catch (SQLException e) 
			{
				EmpExecutionContext.error(e,"员工通讯录关闭连接失败！");
			}
		}
		return depIds;
	}
	
	
	//根据机构id获取所有子员工机构sqlserver
	public List<String> getChildByParentIDSqlServer(Long depId) throws Exception{
		List<String> returnList=new ArrayList<String>();
		String sql = new StringBuffer("select * from GetEmpDepChildByPID(1,").append(depId).append(")").toString();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer();
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			int  i=0;
			while (rs.next()) 
			{
				i++;
				if(i%1000!=0)
				{
					deps.append(rs.getLong("DepID")).append(",");
				}else{
					deps.append(rs.getLong("DepID"));
					returnList.add(deps.toString());
					deps=new StringBuffer();
				}
			}
			returnList.add(deps.toString());
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, ps, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return returnList;
	}

	//根据机构id获取所有子员工机构    mysql
	public List<String> getChildByParentIDMySql(Long depId) throws Exception{
		List<String> returnList=new ArrayList<String>();
		String sql  = "call GETCLIDEPCHILDBYPID (?,?,?)";
		Connection conn = null;
		ResultSet rs = null;
		StringBuffer deps = new StringBuffer();
		CallableStatement comm = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			comm = conn.prepareCall(sql);
			comm.setInt(1,1);
			comm.setLong(2,depId);
			comm.setString(3, StringUtils.getRandom());
			comm.execute();
			rs = comm.getResultSet();
			int i=0;
			while (rs.next()) {
				i++;
				if(i%1000!=0){
					deps.append(rs.getLong("DepID")).append(",");
				}else{
						deps.append(rs.getLong("DepID"));
						returnList.add(deps.toString());
						deps=new StringBuffer();
				}
			}
			returnList.add(deps.toString());
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, comm, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return returnList;
	}
	
	public String getChildByParentIDMySql(Long depId, String refName) throws Exception{
		String sql  = "call GETCLIDEPCHILDBYPID(?,?,?)";
		Connection conn = null;
		ResultSet rs = null;
		String depIds = "";
		CallableStatement comm = null;
		
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			comm = conn.prepareCall(sql);
			comm.setInt(1,1);
			comm.setLong(2,depId);
			comm.setString(3, StringUtils.getRandom());
			comm.execute();
			rs = comm.getResultSet();
			
			depIds = parseRS(rs, refName);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行sql语句异常！");
			throw e;
		} finally
		{
			try
			{
				close(rs, comm, conn);
			} catch (SQLException e)
			{
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return depIds;
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
	
	/**
	 * 获取员工机构的所有子级机构ID的字符串
	 * @param depId 需要查找的员工机构ID
	 * @return 所有子级机构ID的字符串
	 * @throws Exception
	 */
	public String getChildEmpDepByParentID(Long depId) throws Exception 
	{
		String depIds = "";
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			depIds = executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(depId.toString())});
		}else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE){
		}else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE){
			/*depIds = executeProcessReutrnCursorOfMySql(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(depId.toString())});*/
			depIds = getEmpChildIdByDepId(depId.toString());
		}else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE){
			
		}
		
		
		return depIds;
	}
	
	/**
	 * 解析结果集
	 * @param rs
	 * @param refName
	 * @return
	 * @throws Exception
	 */
	private String parseRS(ResultSet rs, String refName) throws Exception 
	{
		StringBuffer deps = new StringBuffer();
		int i = 1;//记录计数
		int cou=1;
		
		if(rs.next())//结果集有值
		{
			deps.append(refName).append(" in ( ").append(rs.getLong("DepID"));
		}
		else
		{
			return "";
		}
		
		while (rs.next()) {
			i++;
			//if(i > cou*StaticValue.inConditionMax)
			if(i > cou*StaticValue.getInConditionMax())
			{//记录数操作数量，则拆分为or xx in(id,id,id...)格式
				cou++;
				deps.append(") or ").append(refName).append(" in (").append(rs.getLong("DepID"));
			}
			else
			{
				deps.append(",").append(rs.getLong("DepID"));
			}
		}
		
		deps.append(" ) ");
		
		return deps.toString();
	}
}
