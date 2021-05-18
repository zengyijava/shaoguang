package com.montnets.emp.client.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.client.vo.AddrBookPermissionsVo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.client.LfCliDepConn;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SqlSplice;

/**
 * @project 
 * @author 
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 
 * @description
 */

public class GenericAddrBookPermissionsVoDAO extends AddrBookSuperDAO
{
	//暂时不用的方法
	private IGenericDAO genericDAO=null;
	public GenericAddrBookPermissionsVoDAO(){
		genericDAO = new DataAccessDriver().getGenericDAO();
	}
	public List<AddrBookPermissionsVo> getEmpBindPermissionsList(
			Long loginUserId,String corpCode, AddrBookPermissionsVo addrBookPermissionsVo,
			PageInfo pageInfo) throws Exception
	{
		String fieldSql = GenericAddrBookPermissionsVoForEmpSQL.getFieldSql();
		String tableSql = GenericAddrBookPermissionsVoForEmpSQL.getTableSql(corpCode);
		String conditionSql = GenericAddrBookPermissionsVoForEmpSQL.getConditionSql(addrBookPermissionsVo);
		
		if (addrBookPermissionsVo.getDepCode() == null || "".equals(addrBookPermissionsVo.getDepCode().trim())){
			StringBuffer queryEmpDepByUserId = new StringBuffer(" select * from ")
			.append(TableLfEmpDepConn.TABLE_NAME)
			.append(" lfempdepconn ")
			.append(StaticValue.getWITHNOLOCK()).append(" left join ")
			.append(TableLfEmployeeDep.TABLE_NAME)
			.append(" lfempdep ")
			.append(StaticValue.getWITHNOLOCK())
			.append(" on lfempdepconn.").append(TableLfEmpDepConn.DEP_ID)
			.append("=lfempdep.").append(TableLfEmployeeDep.DEP_ID);
			
			String moreWhere = "";
			if(corpCode!=null){
				moreWhere = new StringBuffer(" and ")
				.append( "lfempdep.").append(TableLfEmployeeDep.CORP_CODE)
				.append("= '").append(corpCode+"'").toString();
			}
			
			queryEmpDepByUserId = queryEmpDepByUserId.append(moreWhere);
			
			StringBuffer temp = new StringBuffer()
			.append(" where lfempdepconn.").append(TableLfEmpDepConn.USER_ID)
			.append("=").append(loginUserId);
			
			queryEmpDepByUserId = queryEmpDepByUserId.append(temp);
			
			List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
					queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);
			
			StringBuffer tempSql = new StringBuffer();
			if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
			{
				conditionSql += tempSql;
			}else{
				return new ArrayList<AddrBookPermissionsVo>();
			}
		}
		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" and lfsysuser.").append(TableLfSysuser.CORP_CODE)
			.append("= '").append(corpCode+"'").toString();
		
		conditionSql+=moreWhere;
		//存在查询条件
		if(conditionSql != null && conditionSql.length() > 0)
		{
			//将条件字符串首个and替换为where,不允许1 =1方式
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();
		
		String temp = new StringBuffer().append(" order by lfempdep.")
		.append(TableLfEmployeeDep.DEP_ID).toString();

		String count = "" ;
		//sql +=temp;
		if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
			sql +=temp;
			count = sql;
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			count = sql;
			
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			sql +=temp;
			count = sql;
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			sql +=temp;
			count = sql;
		}
	 	
		
		
		
	
	    String countSql = SqlSplice.getCountSql(count);
	    if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE 
	    		|| StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
	    	  countSql= countSql+" as t ";
		} 
	  
	    
		List<AddrBookPermissionsVo> addrBookPermissionsVos = findPageVoListBySQL(
						AddrBookPermissionsVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		
		return addrBookPermissionsVos;
	}
	
	public List<AddrBookPermissionsVo> getEmpBindPermissionsListByDepIds(
			Long loginUserId,String corpCode, AddrBookPermissionsVo addrBookPermissionsVo,
			PageInfo pageInfo) throws Exception
	{
		if(addrBookPermissionsVo.getDepIds() == null 
				|| "".equals(addrBookPermissionsVo.getDepIds()))
		{
			return null;
		}
		String fieldSql = GenericAddrBookPermissionsVoForEmpSQL.getFieldSql();
		String tableSql = GenericAddrBookPermissionsVoForEmpSQL.getTableSql(corpCode);
		String conditionSql = GenericAddrBookPermissionsVoForEmpSQL.getConditionSql(addrBookPermissionsVo);
		
		if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
	   	 	String depIds = executeProcessReutrnCursorOfOracle(
	   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
	   	 			new Integer[]{1,Integer.valueOf(addrBookPermissionsVo.getDepIds())});
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return null;
	   	 	}
	   	 	conditionSql = getInConditionSql(conditionSql, depIds);
		}
		if (StaticValue.DBTYPE ==  StaticValue.MYSQL_DBTYPE)
		{
			//适用mysql数据库的SQL语句
			 /*
			  * String depIds = executeProcessReutrnCursorOfMySql(
		   	 			StaticValue.EMP_POOLNAME,"GETEMPDEPCHILDBYPID",
		   	 			new Integer[]{1,Integer.valueOf(lfEmployeeVo.getDepIds())});
			  * */
			 String depIds = getEmpChildIdByDepId(addrBookPermissionsVo.getDepIds());
	   	 	if(depIds == null || depIds.length() == 0)
	   	 	{
	   	 		return null;
	   	 	}
	   	 	conditionSql = getInConditionSql(conditionSql, depIds);
		}
		if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			conditionSql=conditionSql+" and lfempdepconn."+
				TableLfEmpDepConn.DEP_ID+" in (select aa.DepId from GetEmpDepChildByPID(1,"
				+addrBookPermissionsVo.getDepIds()+") aa ) ";
		}
		String moreWhere = "";
		if(corpCode!=null)
			moreWhere = new StringBuffer(" and lfsysuser.").append(TableLfSysuser.CORP_CODE)
			.append("= '").append(corpCode+"'").toString();
		
		String temp = new StringBuffer().append(" order by lfempdep.")
			.append(TableLfEmployeeDep.DEP_ID).toString();
		
		conditionSql +=moreWhere;
		
		//存在查询条件
		if(conditionSql != null && conditionSql.length() > 0)
		{
			//将条件字符串首个and替换为where,不允许1 =1方式
			conditionSql = conditionSql.replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).toString();
//		sql +=moreWhere;
		String count = "" ;
		//sql +=temp;
		if (StaticValue.DBTYPE ==  StaticValue.ORACLE_DBTYPE)
		{
			//适用ORACEL数据库的SQL语句
			sql +=temp;
			count = sql;
		} else if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			count = sql;
			
		} else if (StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)
		{
			//适用MYSQL数据库的SQL语句
			sql +=temp;
			count = sql;
		}else if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用DB2数据库的SQL语句
			sql +=temp;
			count = sql;
		}
	 	
	    String countSql = SqlSplice.getCountSql(count);
	    if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE 
	    		|| StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE || StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
	    	  countSql= countSql+" as t ";
		} 
	  
		List<AddrBookPermissionsVo> addrBookPermissionsVos = findPageVoListBySQL(
						AddrBookPermissionsVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		
		return addrBookPermissionsVos;
	}
	
	/**
	 * 获取权限绑定vo对象
	 * @param loginUserID 登录操作员id
	 * @param corpCode 企业编码
	 * @param addrBookPermissionsVo 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回权限绑定对象集合
	 * @throws Exception
	 */
	public List<AddrBookPermissionsVo> getClientBindPermissionsList(Long loginUserID, String corpCode, AddrBookPermissionsVo addrBookPermissionsVo,
			PageInfo pageInfo) throws Exception
	{

		////获取登录操作员所绑定的机构ID的sql
		String depConnSql = new StringBuffer(" select ")
				.append(TableLfCliDepConn.DEP_ID).append(" from ")
				.append(TableLfCliDepConn.TABLE_NAME).append(" where ")
				.append(TableLfCliDepConn.USER_ID).append(" = ").append(loginUserID).toString();
		
		List<LfCliDepConn> codeDepConnsList = findPartEntityListBySQL(
				LfCliDepConn.class, depConnSql, StaticValue.EMP_POOLNAME);//获取登录操作员所绑定的机构ID
		
		if (codeDepConnsList == null || codeDepConnsList.size() == 0)//当前登录操作员没绑定机构，无记录
		{
			return new ArrayList<AddrBookPermissionsVo>();
		}

		String depConnID = codeDepConnsList.get(0).getDepId().toString();//获取登录操作员所绑定的机构ID
		
		String sql="";//查询绑定记录的sql
		
		if (StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE)
		{
			//适用SQLSERVER2005数据库的SQL语句
			sql = new StringBuffer("select lfclientdepconn.").append(TableLfCliDepConn.CONN_ID)
				.append(",lfclientdepconn.").append(TableLfCliDepConn.DEP_ID)
				.append(",lfclientdepconn.").append(TableLfCliDepConn.USER_ID)
				.append(",lfclientdep.").append(TableLfClientDep.DEP_NAME)
				.append(",lfsysuser.").append(TableLfSysuser.USER_NAME)
				.append(",lfsysuser.").append(TableLfSysuser.NAME)
				.append(", ROW_NUMBER() Over(Order By lfclientdep.").append(TableLfClientDep.CORP_CODE).append(") As rn").toString();

		} 
		else
		{
			sql = new StringBuffer("select lfclientdepconn.").append(TableLfCliDepConn.CONN_ID)
				.append(",lfclientdepconn.").append(TableLfCliDepConn.DEP_ID)
				.append(",lfclientdepconn.").append(TableLfCliDepConn.USER_ID)
				.append(",lfclientdep.").append(TableLfClientDep.DEP_NAME)
				.append(",lfsysuser.").append(TableLfSysuser.USER_NAME)
				.append(",lfsysuser.").append(TableLfSysuser.NAME).toString();
		}

		String countSql = "select count(*) totalcount";
		
		String tableSql = new StringBuffer(" from ")
				.append(TableLfClientDep.TABLE_NAME).append(" lfclientdep ").append(StaticValue.getWITHNOLOCK())
				.append(" inner join ")
				.append(TableLfCliDepConn.TABLE_NAME).append(" lfclientdepconn ").append(StaticValue.getWITHNOLOCK())
				.append(" on")
				.append(" lfclientdepconn.").append(TableLfCliDepConn.DEP_ID).append("=lfclientdep.").append(TableLfClientDep.DEP_ID)
				.append(" and ")
				.append( "lfclientdep.").append(TableLfClientDep.CORP_CODE).append(" = '").append(corpCode+"' ")
				.append(" inner join ")
				.append(TableLfSysuser.TABLE_NAME).append(" lfsysuser ").append(StaticValue.getWITHNOLOCK())
				.append(" on ").append(" lfsysuser.").append(TableLfSysuser.USER_ID)
				.append("=lfclientdepconn.").append(TableLfCliDepConn.USER_ID)
				.append(" and ")
				.append( "lfsysuser.").append(TableLfSysuser.CORP_CODE).append("= '").append(corpCode+"' ")
				.append(" where  ").toString();
		
		sql += tableSql;
		countSql += tableSql;
		
		String depIdTemp = null;
		StringBuffer conditionSql = new StringBuffer();//查询条件
		
		if (null != addrBookPermissionsVo.getDepId())
		{//查询选择的机构的子机构
			depIdTemp = addrBookPermissionsVo.getDepId().toString();
		} 
		else//没带depId条件，则查绑定机构的子机构
		{
			depIdTemp = depConnID;
		}

		conditionSql.append("  (")
		.append(new GenericLfClientVoDAO().getClientChildByParentID(Long.parseLong(depIdTemp),"lfclientdepconn."+TableLfCliDepConn.DEP_ID))
		.append(") ");//获取子机构，包含自己
		
/*		conditionSql.append(" and lfclientdepconn.").append(TableLfCliDepConn.DEP_ID)
					.append(" in ( ").append("select DepID from GetCliDepChildByPID(1,").append(depIdTemp).append(") ) ");//获取子机构，包含自己
*/
		if (addrBookPermissionsVo.getName() != null
				&& !"".equals(addrBookPermissionsVo.getName()))
		{
			conditionSql.append(" and lfsysuser.").append(TableLfSysuser.NAME)
					.append(" like '%").append(addrBookPermissionsVo.getName()).append("%'");
		}
		
		sql += conditionSql.toString();
		countSql += conditionSql.toString();
		
		List<AddrBookPermissionsVo> addrBookPermissionsVos =findPageVoListBySQL(
				AddrBookPermissionsVo.class, sql, countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		
		return addrBookPermissionsVos;
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
		/*if(addrBookPermissionsVo.getDepId()!=null){
			moreWhere +=" and lfsysuser.DEP_ID="+addrBookPermissionsVo.getDepId();
		}*/
		if(addrBookPermissionsVo.getDepId()!=null)
		{
			moreWhere+=" and "+new DepDAO().getChildUserDepByParentID(addrBookPermissionsVo.getDepId(),"lfsysuser.DEP_ID");
		}
		sql = sql+tableSql+moreWhere;
		countSql=countSql+tableSql+moreWhere;
		List<LfSysuser> lfSysuserList = genericDAO.findPageEntityListBySQL(LfSysuser.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return lfSysuserList;
	}

	public List<LfSysuser> getClientUnBindPermissionsSysuserList(
			Long loginUserId,String cropcode, AddrBookPermissionsVo addrBookPermissionsVo,
			PageInfo pageInfo) throws Exception
	{
		String countSql = "select count(*) totalcount";
		String sql = new StringBuffer("select lfsysuser.* ").toString();
		String tableSql = new StringBuffer(" from ").append(TableLfSysuser.TABLE_NAME)
		.append(" lfsysuser ").append(StaticValue.getWITHNOLOCK())
		.append(" where lfsysuser.").append(TableLfSysuser.USER_STATE).append("=1 and ").append("lfsysuser.")
		.append(TableLfSysuser.USER_ID).append(" not in (select lfclientdepconn.").append(TableLfCliDepConn.USER_ID)
		.append(" from ").append(TableLfCliDepConn.TABLE_NAME).append(" lfclientdepconn ").append(StaticValue.getWITHNOLOCK())
		.append(" ) ")
		.toString();
		String moreWhere = "";
		if(cropcode!=null){
			moreWhere = new StringBuffer("and ").append("lfsysuser.").append(TableLfSysuser.CORP_CODE)
			.append("='").append(cropcode+"'").toString();
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
		List<LfSysuser> lfSysuserList =  genericDAO.findPageEntityListBySQL(LfSysuser.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return lfSysuserList;
	}
	
	private String getInConditionSql(String conditionSql ,String depIds)
	{
		/*******机构in查询处理***********/
		//int maxCount = StaticValue.inConditionMax;//in查询最大个数限制
		int maxCount = StaticValue.getInConditionMax();//in查询最大个数限制
		String[] depIdArry = depIds.split(",");
		int size = depIdArry.length;
		if(size>maxCount)
		{
			conditionSql =conditionSql+" and (lfempdepconn."+
					TableLfEmployee.DEP_ID+" in (";
			String inString = "";
			for(int i=0;i<size;i++)
			{
				inString +=  depIdArry[i] ;
				if( i > maxCount-2 && (i+1)%maxCount==0  && i<size - 1  )
				{
					conditionSql=conditionSql + inString + ") or "+" lfemployeedep."+
							TableLfEmployee.DEP_ID+" in (";
					inString = "";
				}else if( i<size - 1 )
				{
					inString += ",";
				}
			}
			conditionSql+=inString+"))";
		}else
		{
		
			conditionSql=conditionSql+" and lfempdepconn."+
					TableLfEmployee.DEP_ID+" in ("+depIds+")";
		}
		return conditionSql;
		/*******END-机构in查询处理**********/
	}
	
	
	
	
	
	//员工机构操作员权限绑定
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
		.append(TableLfEmpDepConn.USER_ID).append(" where  depconn.").append(TableLfEmpDepConn.USER_ID).append(" in (select sysuser.")
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
		List<AddrBookPermissionsVo> addrBookPermissionsVos =findPageVoListBySQL(
				AddrBookPermissionsVo.class, queryStr.toString(), countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		
		return addrBookPermissionsVos;
		
	}
	
	//客户权限绑定
	public List<AddrBookPermissionsVo> getPermissionsVo2(Long loginUserID,String depPath,String corpCode,String name , PageInfo pageInfo) throws Exception
	{
		StringBuffer queryStr = new StringBuffer();
		String countSql = "select count(*) totalcount from ( ";
		queryStr.append("select *  from (select depconn.").append(TableLfCliDepConn.USER_ID).append(" , depconn.")
		.append(TableLfCliDepConn.DEP_ID).append(" , depconn.").append(TableLfCliDepConn.CONN_ID).append(" , dep.")
		.append(TableLfClientDep.DEP_NAME).append(" , lfuser.").append(TableLfSysuser.USER_NAME).append(",lfuser.")
		.append(TableLfSysuser.NAME);
		if(StaticValue.DBTYPE ==  StaticValue.SQLSERVER_DBTYPE){
			queryStr.append(",ROW_NUMBER() Over(Order By lfuser.").append(TableLfSysuser.NAME).append(") As rn ");
		}
		
		queryStr.append(" from ").append(TableLfCliDepConn.TABLE_NAME).append(" depconn ")
		.append(StaticValue.getWITHNOLOCK()).append(" left join ").append(TableLfClientDep.TABLE_NAME).append(" dep ")
		.append(StaticValue.getWITHNOLOCK()).append(" on  depconn.").append(TableLfCliDepConn.DEP_ID).append(" = ").append(" dep.")
		.append(TableLfClientDep.DEP_ID).append(" left join ").append(TableLfSysuser.TABLE_NAME).append(" lfuser ")
		.append(StaticValue.getWITHNOLOCK()).append(" on lfuser.").append(TableLfSysuser.USER_ID).append(" = ").append(" depconn.")
		.append(TableLfCliDepConn.USER_ID).append(" where depconn.").append(TableLfCliDepConn.USER_ID).append(" in (select sysuser.")
		.append(TableLfSysuser.USER_ID).append(" from ").append(TableLfSysuser.TABLE_NAME).append(" sysuser ").append(StaticValue.getWITHNOLOCK())
		.append(" where sysuser.").append(TableLfSysuser.DEP_ID).append(" in (select domination.").append(TableLfDomination.DEP_ID)
		.append(" from ").append(TableLfDomination.TABLE_NAME).append(" domination ").append(StaticValue.getWITHNOLOCK()).append(" where domination.")
		.append(TableLfDomination.USER_ID).append(" =" ).append(loginUserID).append(" ))");
		if(name != null && !"".equals(name)){
			queryStr.append(" and lfuser.").append(TableLfSysuser.NAME).append(" like '%").append(name).append("%'");
		}
		
		if(depPath != null && !"".equals(depPath)){
			queryStr.append(" and dep.").append(TableLfClientDep.DEP_ID).append(" in ( select temp_dep.")
			.append(TableLfClientDep.DEP_ID).append(" from ").append(TableLfClientDep.TABLE_NAME)
			.append(" temp_dep ").append(StaticValue.getWITHNOLOCK()).append(" where temp_dep.")
			.append(TableLfClientDep.DEP_PATH).append(" like '").append(depPath).append("%')");
		}
		queryStr.append(" and lfuser.").append(TableLfSysuser.CORP_CODE).append(" = '").append(corpCode).append("'");
		
		queryStr.append(" ) ").append(" temp ")
		.append( " where temp.dep_name is not null and temp.user_name is not null");

		countSql = countSql + queryStr.toString()+" ) total ";
		
		List<AddrBookPermissionsVo> addrBookPermissionsVos =findPageVoListBySQL(
				AddrBookPermissionsVo.class, queryStr.toString(), countSql, pageInfo,
				StaticValue.EMP_POOLNAME);
		
		return addrBookPermissionsVos;
		
	}
	
}
