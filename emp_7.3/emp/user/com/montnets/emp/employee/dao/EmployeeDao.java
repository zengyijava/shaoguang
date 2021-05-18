package com.montnets.emp.employee.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.employee.vo.AddrBookPermissionsVo;
import com.montnets.emp.employee.vo.LfEmpDepConnVo;
import com.montnets.emp.employee.vo.LfEmployeeTypeVo;
import com.montnets.emp.employee.vo.LfEmployeeVo;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.employee.LfEmployeeType;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.sysuser.vo.LfSysuser2Vo;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.employee.TableLfEmployeeType;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
/**
 *   员工 DAO
 * @author Administrator
 *
 */
public class EmployeeDao extends SuperDAO {

	public List<LfEmployeeDep> findDomEmployeeDepBySysuserIDOnlyAddr(
			Long loginUserID, String corpCode) throws Exception
	{
		List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();

		if (new Long(1).equals(loginUserID))
		{
			String sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(" order by ").append(
					TableLfEmployeeDep.DEP_ID).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
			return lfEmployeeDepList;
		}
		StringBuffer queryEmpDepByUserId = new StringBuffer(" select * from ")
				.append(TableLfEmpDepConn.TABLE_NAME).append(
						" lfempdepconn left join ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" lfempdep on lfempdepconn.").append(
						TableLfEmpDepConn.DEP_ID).append("=lfempdep.").append(
						TableLfEmployeeDep.DEP_ID);

		if (loginUserID != null)
		{
			queryEmpDepByUserId.append(" where lfempdepconn.").append(
					TableLfEmpDepConn.USER_ID).append("=").append(loginUserID);
			queryEmpDepByUserId.append(" and lfempdep.").append(
					TableLfEmployeeDep.CORP_CODE).append("='").append(
					corpCode + "'");// 增加企业编号
		}

		lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
				queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);

		if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
		{
			queryEmpDepByUserId = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME)
					.append(" lfempdep ");
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < lfEmployeeDepList.size(); i++)
			{
				LfEmployeeDep employeeDep = lfEmployeeDepList.get(i);
				buffer.append(" lfempdep.").append(TableLfEmployeeDep.DEP_ID)
						.append(" =").append(employeeDep.getDepId()).append(
								" or lfempdep.").append(
								TableLfEmployeeDep.PARENT_ID).append("=")
						.append(employeeDep.getDepId());
				if (lfEmployeeDepList.size() - i != 1)
				{
					buffer.append(" or ");
				}
			}
			queryEmpDepByUserId.append(" where ( ").append(buffer.toString())
					.append(" ) ");

			String moreWhere = "";
			if (corpCode != null)
			{
				queryEmpDepByUserId.append(" and lfempdep.").append(
						TableLfEmployeeDep.CORP_CODE).append("='").append(
						corpCode + "'");// 增加企业编号
			}
			String orderBy = new StringBuffer(" order by ").append(
					TableLfEmployeeDep.DEP_ID).append(" ASC").toString();
			queryEmpDepByUserId.append(moreWhere).append(orderBy);
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
					queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);
		}
		return lfEmployeeDepList;
	}
	
	
	
	/**
	 *  权限绑定中已绑定的操作员
	 * @param sysuserID
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> findDomUserBySysuserID(String sysuserID)throws Exception
	{
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
				TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
				" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
				domination).append(")) and ").append(TableLfSysuser.USER_ID)
				.append("<>1 ");
		String sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	
	/**
	 * 员工机构操作员权限绑定
	 * @param loginUserID 
	 * @param depPath
	 * @param corpCode
	 * @param name
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getPermissionsVo(Long loginUserID,String depPath,String corpCode,String name , PageInfo pageInfo) throws Exception{
		
		
		StringBuffer queryStr = new StringBuffer();
		String countSql = "select count(*) totalcount from ( ";
		queryStr.append("select *  from (select depconn.").append(TableLfEmpDepConn.USER_ID).append(" , depconn.")
		.append(TableLfEmpDepConn.DEP_ID).append(" , depconn.").append(TableLfEmpDepConn.CONN_ID).append(" , dep.")
		.append(TableLfEmployeeDep.DEP_NAME).append(" , lfuser.").append(TableLfSysuser.USER_NAME).append(",lfuser.")
		.append(TableLfSysuser.NAME);
		
		if(StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE){
			queryStr.append(",ROW_NUMBER() Over(Order By lfuser.").append(TableLfSysuser.NAME).append(") As rn ");
		}
		queryStr.append(" from ").append(TableLfEmpDepConn.TABLE_NAME).append(" depconn ")
		.append(StaticValue.getWITHNOLOCK()).append(" left join ").append(TableLfEmployeeDep.TABLE_NAME).append(" dep ")
		.append(StaticValue.getWITHNOLOCK()).append(" on  depconn.").append(TableLfEmpDepConn.DEP_ID).append(" = ").append(" dep.")
		.append(TableLfEmployeeDep.DEP_ID).append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" lfuser ")
		.append(StaticValue.getWITHNOLOCK()).append(" on lfuser.").append(TableLfSysuser.USER_ID).append(" = ").append(" depconn.")
		.append(TableLfEmpDepConn.USER_ID).append(" where depconn.").append(TableLfEmpDepConn.USER_ID).append(" in (select sysuser.")
		.append(TableLfSysuser.USER_ID).append(" from ").append(TableLfSysuser.TABLE_NAME).append(" sysuser ").append(StaticValue.getWITHNOLOCK())
		.append(" where sysuser.").append(TableLfSysuser.DEP_ID).append(" in (select domination.").append(TableLfDomination.DEP_ID)
		.append(" from ").append(TableLfDomination.TABLE_NAME).append(" domination ").append(StaticValue.getWITHNOLOCK()).append(" where domination.")
		.append(TableLfDomination.USER_ID).append(" =" ).append(loginUserID).append(" ))");
		if(name != null && !"".equals(name)){
			queryStr.append(" and lfuser.").append(TableLfSysuser.NAME).append(" like '%").append(name).append("%'");
		}
		
		if(depPath != null && !"".equals(depPath)){
			queryStr.append(" and dep.").append(TableLfEmployeeDep.DEP_ID).append(" in ( select temp_dep.")
			.append(TableLfEmployeeDep.DEP_ID).append(" from ").append(TableLfEmployeeDep.TABLE_NAME)
			.append(" temp_dep ").append(StaticValue.getWITHNOLOCK()).append(" where temp_dep.")
			.append(TableLfEmployeeDep.DEP_PATH).append(" like '").append(depPath).append("%')");
		}
		queryStr.append(" and lfuser.").append(TableLfSysuser.CORP_CODE).append(" = '").append(corpCode).append("'");
		
		queryStr.append(" ) ").append(" temp ")
		.append( " where temp.dep_name is not null and temp.user_name is not null");

		countSql = countSql + queryStr.toString()+" ) total ";
		List<AddrBookPermissionsVo> addrBookPermissionsVos =new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				AddrBookPermissionsVo.class, queryStr.toString(), countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		return addrBookPermissionsVos;
	}
	
	
	/***
	 * 查询员工与员工机构权限信息（admin级别）
	 */
	public boolean filtAdmin(long conn_id,String corpCode)throws Exception {
		String fieldSql = new StringBuffer("select  ").append("*").toString();
		String tableSql = new StringBuffer(" from ")
		.append(TableLfEmpDepConn.TABLE_NAME).toString();
		String whereSql ="";
			whereSql =  new StringBuffer(" ").append(" where ").append(TableLfEmpDepConn.CONN_ID).append(" = ").append(conn_id).toString();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(whereSql).toString();	
		
		/*List<LfEmpDepConnVo> returnVoList = findAllVoTypeListBySQL(LfEmpDepConnVo.class, sql
				.toString(), StaticValue.EMP_POOLNAME,null,null);*/
		List<LfEmpDepConnVo> returnVoList = findAllVoListBySQL(LfEmpDepConnVo.class, sql
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
			/*returnVoList2 = findAllVoTypeListBySQL(LfSysuser2Vo.class, sql2
					.toString(), StaticValue.EMP_POOLNAME,null,null);*/
			returnVoList2 = findAllVoListBySQL(LfSysuser2Vo.class, sql2
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
			/*returnVoList2 = findAllVoTypeListBySQL(LfSysuser2Vo.class, sql2
					.toString(), StaticValue.EMP_POOLNAME,null,null);*/
				returnVoList2 = findAllVoListBySQL(LfSysuser2Vo.class, sql2
						.toString(), StaticValue.EMP_POOLNAME,null,null);
			
		}
		if(returnVoList2.size()>0)
			return false;
		else 
			return true;
		
	}
	
	
	
	/**
	 *   对员工通讯录的权限管理    查询出没有绑定的的操作员
	 * @param loginUserId
	 * @param corpCode
	 * @param addrBookPermissionsVo
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getEmpUnBindPermissionsSysuserList(
			Long loginUserId, String corpCode,AddrBookPermissionsVo addrBookPermissionsVo,
			PageInfo pageInfo) throws Exception
	{
		String countSql = "select count(*) totalcount";
		String sql = new StringBuffer("select lfsysuser.* ").toString();
		String tableSql = new StringBuffer(" from ").append(TableLfSysuser.TABLE_NAME)
		.append(" lfsysuser ").append(StaticValue.getWITHNOLOCK())
		.append(" where lfsysuser.").append(TableLfSysuser.USER_STATE).append("=1 and ").append("lfsysuser.")
		.append(TableLfSysuser.USER_ID).append(" not in (select lfempdepconn.").append(TableLfEmpDepConn.USER_ID)
		.append(" from ").append(TableLfEmpDepConn.TABLE_NAME).append(" lfempdepconn ").append(StaticValue.getWITHNOLOCK())
		.append(" ) ")
		.toString();
		String moreWhere = "";
		if(corpCode!=null){
			moreWhere = new StringBuffer("and ").append("lfsysuser.").append(TableLfSysuser.CORP_CODE)
			.append("='").append(corpCode+"'").toString();
		}
		if(addrBookPermissionsVo.getName()!=null){
			moreWhere +=" and lfsysuser.NAME like '%"+addrBookPermissionsVo.getName()+"%'";
		}
		if(addrBookPermissionsVo.getDepId()!=null)
		{
			moreWhere+=" and "+new DepDAO().getChildUserDepByParentID(addrBookPermissionsVo.getDepId(),"lfsysuser.DEP_ID");
		}
		sql = sql+tableSql+moreWhere;
		countSql=countSql+tableSql+moreWhere;
		List<LfSysuser> lfSysuserList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfSysuser.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return lfSysuserList;
	}
	
	/**
	 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	 */
	/**
	 * 获得所有员工通讯录类型
	 */
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
		
		/*List<LfEmployeeTypeVo> returnVoList = findAllVoTypeListBySQL(LfEmployeeTypeVo.class, sql
				.toString(), StaticValue.EMP_POOLNAME,null,null);*/
		List<LfEmployeeTypeVo> returnVoList = findAllVoListBySQL(LfEmployeeTypeVo.class, sql
				.toString(), StaticValue.EMP_POOLNAME,null,null);
		return returnVoList;
	}
	
	/**
	 *   删除职位
	 * @param corpCode
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delEmployeeTypeVo(String corpCode,int id)throws Exception {
		String fieldSql = new StringBuffer("delete").toString();
		String tableSql = new StringBuffer(" from ")
		.append(TableLfEmployeeType.TABLE_NAME).append(" where ").append(TableLfEmployeeType.ID).append(" = ")
		.append(id).append(" and ").append(TableLfEmployeeType.CORP_CODE).append(" = '").append(corpCode+"'")
				.toString();
		String sql = new StringBuffer(fieldSql).append(tableSql).toString();		
		boolean b = executeBySQL(sql, StaticValue.EMP_POOLNAME);
	
		return b;
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
		
		List<LfEmployeeTypeVo> returnVoList = new DataAccessDriver().getGenericDAO()
		.findPageVoListBySQLNoCount(LfEmployeeTypeVo.class, sql
				.toString(),countsql,pageInfo, StaticValue.EMP_POOLNAME);
		/*	findVoTypeListBySQL(LfEmployeeTypeVo.class,pageInfo, sql
				.toString(),countsql, StaticValue.EMP_POOLNAME,null,null);*/
	
		
		return returnVoList;
	}
	
	
	/**
	 *   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~员工机构操作dao  begin
	 */
	
	
	//写一个不需要分页的获取员工机构下面所有员工的方法
	public List<LfEmployeeVo> findEmployeeVoByDepIds(Long loginUserID,String corpCode,
			LfEmployeeVo lfEmployeeVo) throws Exception {
		if(lfEmployeeVo.getDepIds() == null || "".equals(lfEmployeeVo.getDepIds()))
		{
			return new ArrayList<LfEmployeeVo>();
		}
		String sql = "";
		StringBuffer conditionSql = new StringBuffer();
		EmployeeSQL employeeSql = new EmployeeSQL();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
				//适用ORACEL数据库的SQL语句
		   	 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME;
		   /*	String depIds = executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});*/
		 	String depIds = employeeSql.executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
		   	/* conditionSql = getInConditionSql(conditionSql, depIds);*/
	   	 	conditionSql = employeeSql.getInConditionSql(conditionSql, depIds);
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
			/* String depIds = getEmpChildIdByDepId(lfEmployeeVo.getDepIds());*/
			 String depIds = employeeSql.getEmpChildIdByDepId(lfEmployeeVo.getDepIds());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
		   	/* conditionSql = getInConditionSql(conditionSql, depIds);*/
	   	 	conditionSql = employeeSql.getInConditionSql(conditionSql, depIds);
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
		
		if (null != lfEmployeeVo.getDutyName() && !"".equals(lfEmployeeVo.getDutyName())) {
			if(">".equals(lfEmployeeVo.getDutyName()))
			{
				
				conditionSql.append(" and ( employee.").append(TableLfEmployee.DUTIES).append(" is null or employee.DUTIES='')");
			}
			else
			{
				conditionSql.append(" and employee.").append(TableLfEmployee.DUTIES).append(" = '").append(lfEmployeeVo.getDutyName().trim()).append("'");
			}
		}
		
		if (null != lfEmployeeVo.getSex()) {
			conditionSql.append(" and employee.").append(TableLfEmployee.SEX).append(" = ").append(lfEmployeeVo.getSex());
		}
		
		if(lfEmployeeVo.getBeginbir() != null && !"".equals(lfEmployeeVo.getBeginbir())){
			String beginbir = new DataAccessDriver().getGenericDAO().getTimeCondition(lfEmployeeVo.getBeginbir());
			conditionSql.append(" and employee.").append(TableLfEmployee.BIRTHDAY).append(" >= ").append(beginbir);
		}
		
		if(lfEmployeeVo.getEndbir() != null && !"".equals(lfEmployeeVo.getEndbir())){
			String endbir = new DataAccessDriver().getGenericDAO().getTimeCondition(lfEmployeeVo.getEndbir());
			conditionSql.append(" and employee.").append(TableLfEmployee.BIRTHDAY).append(" <= ").append(endbir);
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
		EmployeeSQL employeeSql = new EmployeeSQL();
	   	if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	   	 sql = "select employee.*,lfemployeedep."
				+ TableLfEmployeeDep.DEP_NAME +",sysuser."+TableLfSysuser.USER_NAME;
	   	 	String depIds = employeeSql.executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
	   	 conditionSql = employeeSql.getInConditionSql(conditionSql, depIds);
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			  sql = "select employee.*, lfemployeedep.DEP_NAME,sysuser.USER_NAME, ROW_NUMBER() Over(Order By lfemployeedep.corp_code) As rn";
			  conditionSql.append("and lfemployeedep.").append(TableLfEmployeeDep.DEP_ID)
			  	.append(" in ( select DepId from GetEmpDepChildByPID(1,").append(lfEmployeeVo.getDepIds()).append(")) ");
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME+",sysuser.USER_NAME";
			 /*
			  * String depIds = executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
			  * */
			 String depIds = employeeSql.getEmpChildIdByDepId(lfEmployeeVo.getDepIds());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return new ArrayList<LfEmployeeVo>();
	   	 	}
		   	 conditionSql = employeeSql.getInConditionSql(conditionSql, depIds);
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			 sql = "select employee.*,lfemployeedep."
					+ TableLfEmployeeDep.DEP_NAME+",sysuser.USER_NAME";
			 String depIds = employeeSql.getEmpChildIdByDepId(lfEmployeeVo.getDepIds());
		   	 	if(depIds == null || depIds.length() == 0)
		   	 	{
		   	 		return new ArrayList<LfEmployeeVo>();
		   	 	}
			   	 conditionSql = employeeSql.getInConditionSql(conditionSql, depIds);
		}

		
		String countSql = "select count(*) totalcount";

		String tableSql = new StringBuffer(" from ").append(
				TableLfEmployee.TABLE_NAME).append(" employee inner join ")
				.append(TableLfEmployeeDep.TABLE_NAME).append(
						" lfemployeedep on employee.").append(
						TableLfEmployee.DEP_ID).append(" = lfemployeedep.")
				.append(TableLfEmployeeDep.DEP_ID).append(" left join ")
				.append(TableLfSysuser.TABLE_NAME).append(" sysuser on employee.")
				.append(TableLfEmployee.GUID).append(" = sysuser.")
				.append(TableLfSysuser.GUID).toString();

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
		
		if (null != lfEmployeeVo.getDutyName() && !"".equals(lfEmployeeVo.getDutyName())) {
			if(">".equals(lfEmployeeVo.getDutyName()))
			{
				
				conditionSql.append(" and ( employee.").append(TableLfEmployee.DUTIES).append(" is null or employee.DUTIES='')");
			}
			else
			{
				conditionSql.append(" and employee.").append(TableLfEmployee.DUTIES).append(" = '").append(lfEmployeeVo.getDutyName().trim()).append("'");
			}
		}
		
		if (null != lfEmployeeVo.getSex()) {
			conditionSql.append(" and employee.").append(TableLfEmployee.SEX).append(" = ").append(lfEmployeeVo.getSex());
		}
		if(lfEmployeeVo.getBeginbir() != null && !"".equals(lfEmployeeVo.getBeginbir())){
			String beginbir = new DataAccessDriver().getGenericDAO().getTimeCondition(lfEmployeeVo.getBeginbir());
			conditionSql.append(" and employee.").append(TableLfEmployee.BIRTHDAY).append(" >= ").append(beginbir);
		}
		
		if(lfEmployeeVo.getEndbir() != null && !"".equals(lfEmployeeVo.getEndbir())){
			String endbir = new DataAccessDriver().getGenericDAO().getTimeCondition(lfEmployeeVo.getEndbir());
			conditionSql.append(" and employee.").append(TableLfEmployee.BIRTHDAY).append(" <= ").append(endbir);
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
		
		if(lfEmployeeVo.getUserName() != null && !"".equals(lfEmployeeVo.getUserName())){
			conditionSql.append(" and employee.").append(TableLfEmployee.IS_OPERATOR).append("=1")
			.append(" and sysuser.").append(TableLfSysuser.USER_NAME)
			.append(" like '%").append(lfEmployeeVo.getUserName()).append("%'");
		}
		if (moreWhere.length() == 0) {
			String conditionSqlStr = conditionSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
			sql += conditionSqlStr;
			countSql += conditionSqlStr;
		} else {
			sql += conditionSql;
			countSql += conditionSql;
		}
		sql += " ORDER BY employee."+TableLfEmployee.EMPLOYEE_ID+" DESC";
		/*List<LfEmployeeVo> returnVoList = findPageVoListBySQL(
						LfEmployeeVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);*/
		/*原先每页都要算出总数的分页方法 备份 pengj*/
		List<LfEmployeeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQL(
				LfEmployeeVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		
		/*第一页就算出总数，翻页后不用再算总数的分页方法 修改pengj
		List<LfEmployeeVo> returnVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
				LfEmployeeVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);*/
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
			/*
			 * sql = new StringBuffer(" select e.* from ").append(
			 * TableLfEmployeeDep
			 * .TABLE_NAME).append(" e "+StaticValue.getWITHNOLOCK()+",").append(
			 * TableLfEmpDepConn
			 * .TABLE_NAME).append(" c "+StaticValue.getWITHNOLOCK())
			 * .append(" where c.")
			 * .append(TableLfEmpDepConn.USER_ID).append(" = "
			 * ).append(userId).append(" and (c.")
			 * .append(TableLfEmpDepConn.DEP_ID
			 * ).append(" =e.").append(TableLfEmployeeDep.DEP_ID)
			 * .append(" or ")
			 * .append(TableLfEmployeeDep.PARENT_ID).append(" = c."
			 * ).append(TableLfEmpDepConn.DEP_ID) .append(")").toString();
			 */
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
				String ids = "";
				for(LfEmpDepConn co:connList){
					ids+=co.getDepId()+",";
				}
				ids = ids.substring(0,ids.lastIndexOf(","));
				//Long id = conn.getDepId();
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where  e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" in(")
						.append(ids).append(") or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" in(")
						.append(ids).append(") order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId)
					.append(" order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}

	/**
	 * 获取员工机构列表——用于构建机构树
	 *
	 * @param sysuser
	 *            当前操作员
	 * @param depId
	 *            传NULL进来则通过sysuser去查，有传值时只查传入机构的子级
	 * @return 员工机构的集合
	 * @throws Exception
	 */
	public List<LfEmployeeDep> getEmpSecondDepTreeByUserorDepId(
			LfSysuser sysuser, String depId) throws Exception
	{
		if(sysuser == null){
			return null;
		}
		//企业编码
		String  corpcode = sysuser.getCorpCode();
		String sql = "";
		//机构列表
		List<LfEmployeeDep> lfEmployeeDepList = null;
		if (depId == null || "".equals(depId))
		{
			long userId = sysuser.getUserId();
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
				String ids = "";
				for(LfEmpDepConn co:connList){
					ids+=co.getDepId()+",";
				}
				ids = ids.substring(0,ids.lastIndexOf(","));
				sql = new StringBuffer(" select e.* from ").append(
						TableLfEmployeeDep.TABLE_NAME).append(
						" e " + StaticValue.getWITHNOLOCK()).append(" where e.").append(TableLfEmployeeDep.CORP_CODE)
						.append(" = '").append(corpcode).append("' and (e.")
						.append(TableLfEmployeeDep.DEP_ID).append(" in(")
						.append(ids).append(") or ").append(
								TableLfEmployeeDep.PARENT_ID).append(" in(")
						.append(ids).append(")) order by ").append(
								TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						sql, StaticValue.EMP_POOLNAME);
			}
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfEmployeeDep.PARENT_ID).append(" = ").append(depId)
					.append(" and ").append(TableLfEmployeeDep.CORP_CODE)
					.append(" = '").append(corpcode)
					.append("' order by ").append(
							TableLfEmployeeDep.ADD_TYPE).append(
							" " + StaticValue.ASC).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}
	
	/**
	 * 查找员工机构的总数
	 */
	public long getEmployeeDepCount(String corpCode) throws Exception
	{
		String sql = "select count(*) totalcount from "
				+ TableLfEmployeeDep.TABLE_NAME + StaticValue.getWITHNOLOCK()
				+ " where " + TableLfEmployeeDep.CORP_CODE + "='" + corpCode
				+ "'";
		return findCountBySQL(sql);
	}
	
	
	
	/**
	 * 通过机构id查询有多少父级机构（级别限制）
	 */
	public long getEmployeeDepLevByDepId(long depId) throws Exception
	{
		Connection conn = null;
		CallableStatement c = null;
		try
		{

			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			c = conn.prepareCall("{call GETEMPDEPLEVEL(?,?,?)}");
			c.setInt(1, 1);
			c.setString(2, String.valueOf(depId));
			c.registerOutParameter(3, java.sql.Types.INTEGER);// 级别
			c.execute();
			// String
			// sql="select max(Lev) totalcount from GetEmpDepParentLevByPID(1,"+depId+")";
			return c.getInt(3);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"执行存储过程失败！");
			throw e;
		} finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
            if (conn != null) {
                conn.close();
            }
        }
	}
	
	/***
	 * 更新员工的职位
	 */
	public void updatePosition(Connection conn,LfEmployeeType type,String name) throws SQLException{
		if(type == null||conn==null||name==null) return;
		String sql = "update "+TableLfEmployee.TABLE_NAME+" set "+ TableLfEmployee.DUTIES +" = ? where "
		+TableLfEmployee.CORP_CODE+" = ? and "+TableLfEmployee.DUTIES+" = ?";
		PreparedStatement ps = null; 
		try{
			ps= conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, type.getCorpcode());
			ps.setString(3, type.getName());
			ps.executeUpdate();
		}finally{
			if(ps != null){
				ps.close();
			}
		}
	}
	
	/****
	 * 通过公司编号获得员工信息
	 */
	public List<LfEmployee> getAllEmployees(String lgcorpcode) throws SQLException{
		List<LfEmployee> list = new ArrayList<LfEmployee>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			String sql = "select "+TableLfEmployee.EMPLOYEE_NO
			+" ,"+TableLfEmployee.MOBILE+" ,"+TableLfEmployee.NAME+" ,"+TableLfEmployee.DEP_ID
			+" from "+TableLfEmployee.TABLE_NAME+" where "
			+TableLfEmployee.CORP_CODE+"=?";
			ps = conn.prepareStatement(sql);
			ps.setString(1,lgcorpcode);
			rs = ps.executeQuery();
			LfEmployee emp = null;
			while(rs.next()){
				emp = new LfEmployee();
				emp.setEmployeeNo(rs.getString(1));
				emp.setMobile(rs.getString(2));
				emp.setName(rs.getString(3));
				emp.setDepId(rs.getLong(4));
				list.add(emp);
			}
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error("查询机构员工信息数据库操作异常！");
			throw e; 
		}finally
		{
			if(ps != null){ps.close();}
			if(rs != null){rs.close();}
			if (conn != null&&!conn.isClosed()){
				conn.close();
			}
		}
		return list;
	}
	
	/***
	 * 获得某个部门的员工总数
	 */
	public int getEmployeesCount(String depId) throws SQLException{
		Connection conn = null;
		Statement st = null;
		ResultSet rs = null;
		int total = 0;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			String sql = "select count(*)"
			+" from "+TableLfEmployee.TABLE_NAME+" where "
			+TableLfEmployee.DEP_ID+" = "+depId;
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			if(rs.next()){
				total = rs.getInt(1);
			}
		}
		catch (SQLException e)
		{
			EmpExecutionContext.error("查询机构员工总数数据库操作异常！");
			throw e; 
		}finally
		{
			if (conn != null&&!conn.isClosed()){
				conn.close();
			}
			if(st != null){st.close();}
			if(rs != null){rs.close();}
		}
		return total;
	}
	/**
	 *  相同的机构名称或机构编码记录数
	 * @description    
	 * @param depName  机构名称
	 * @param depCode	机构编码
	 * @return       	记录数		 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-10-29 下午12:06:07
	 */
	public List<DynaBean> sameDepNameOrDepCode(String depName, String depCode, String scode, String corpcode)
	{
		
		String sql = "select DEP_CODE_THIRD from LF_EMPLOYEE_DEP where CORP_CODE = '"+corpcode+"' and (DEP_CODE_THIRD = '"
					+depCode+"' or (DEP_NAME = '"+depName+"' and PARENT_ID = "+scode+"))";

		return getListDynaBeanBySql(sql);
	}
}
