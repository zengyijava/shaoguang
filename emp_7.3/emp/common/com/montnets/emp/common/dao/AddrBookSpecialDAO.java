package com.montnets.emp.common.dao;

import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmpDepConn;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.group.LfMalist;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.client.TableLfCliDepConn;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmpDepConn;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.engine.TableLfProcess;
import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.gateway.TableAgwParamConf;
import com.montnets.emp.table.gateway.TableAgwParamValue;
import com.montnets.emp.table.gateway.TableAgwSpBind;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfMalist;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.table.system.TableLfTimer;
import com.montnets.emp.table.system.TableLfTimerHistory;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfFlow;
import com.montnets.emp.table.sysuser.TableLfImpower;
import com.montnets.emp.table.sysuser.TableLfPrivilege;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.sysuser.TableLfUser2role;
import com.montnets.emp.util.PageInfo;


/**
 * @project emp
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 下午02:11:25
 * @description
 */

public class AddrBookSpecialDAO extends SuperDAO
{
	private IGenericDAO genericDAO =null;
	private IEmpDAO empDAO =null;
	

	public AddrBookSpecialDAO()
	{
		DataAccessDriver driver = new DataAccessDriver();
		genericDAO =driver.getGenericDAO();
		empDAO = driver.getEmpDAO();
	}
	 
    public List<LfEmployeeDep> findDomEmployeeDepBySysuserID(Long loginUserID,
                                                             String corpCode) throws Exception
	{
		List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();

		if (new Long(1).equals(loginUserID))
		{
			String sql = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(
					StaticValue.getWITHNOLOCK()).append(" order by ").append(
					TableLfEmployeeDep.DEP_ID).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
			return lfEmployeeDepList;
		}
		StringBuffer queryEmpDepByUserId = new StringBuffer(" select * from ")
				.append(TableLfEmpDepConn.TABLE_NAME).append(" lfempdepconn ")
				.append(StaticValue.getWITHNOLOCK()).append(" left join ").append(
						TableLfEmployeeDep.TABLE_NAME).append(" lfempdep ")
				.append(StaticValue.getWITHNOLOCK()).append(" on lfempdepconn.")
				.append(TableLfEmpDepConn.DEP_ID).append("=lfempdep.").append(
						TableLfEmployeeDep.DEP_ID);

		if (loginUserID != null)
		{
			queryEmpDepByUserId.append(" where lfempdepconn.").append(
					TableLfEmpDepConn.USER_ID).append("=").append(loginUserID);
			
			// 增加企业编号
			queryEmpDepByUserId.append(" and lfempdep.").append(
					TableLfEmployeeDep.CORP_CODE).append("='").append(
					corpCode + "'");
		}

		lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
				queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);

		if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
		{
			queryEmpDepByUserId = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME).append(" lfempdep ").append(
					StaticValue.getWITHNOLOCK());
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < lfEmployeeDepList.size(); i++)
			{
				LfEmployeeDep employeeDep = lfEmployeeDepList.get(i);
				buffer.append(" lfempdep.").append(TableLfEmployeeDep.DEP_ID)
						.append(" ='").append(employeeDep.getDepId()).append(
								"'").append(" or lfempdep.").append(
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
				// 增加企业编号
				queryEmpDepByUserId.append(" and lfempdep.").append(
						TableLfEmployeeDep.CORP_CODE).append("='").append(
						corpCode + "'");
			}

			String orderBy = new StringBuffer(" order by ").append(
					TableLfEmployeeDep.DEP_ID).append(" ASC").toString();

			queryEmpDepByUserId.append(moreWhere).append(orderBy);
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
					queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);
		}
		return lfEmployeeDepList;
	}

	 
    public int getBindType() throws Exception
	{
		String sql = new StringBuffer("select ").append(
				TableLfSpDepBind.BIIND_TYPE).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).toString();
		int bindType = getInt(TableLfSpDepBind.BIIND_TYPE, sql,
				StaticValue.EMP_POOLNAME);
		return bindType;
	}

	 
    public List<LfSysuser> findDomSysuserByDepID(String depID,
                                                 LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.USER_ID
				+ "=domination." + TableLfDomination.USER_ID
				+ " where domination." + TableLfDomination.DEP_ID + "=" + depID;
		StringBuffer orderSqlSb = new StringBuffer();
		Map<String, String> columns = TableLfSysuser.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext())
			{
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue().toString()))
				{
					orderSqlSb.append("sysuser.").append(
							columns.get(e.getKey())).append(" ").append(
							e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		return findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
	}

	 
    public List<LfClient> findClientByCusFied(String corpCode,
                                              List<LfCustFieldValueVo> custFieldValueVo) throws Exception
	{
		StringBuffer sql = new StringBuffer("select lfClient.* from ");
		sql.append(TableLfClient.TABLE_NAME)
				.append(" lfClient where lfClient.").append(
						TableLfClient.CORP_CODE).append("='").append(corpCode)
				.append("'");
		if (custFieldValueVo != null && !custFieldValueVo.isEmpty())
		{
			for (LfCustFieldValueVo custfieldvalue : custFieldValueVo)
			{
				sql.append(" and (lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '")
						.append(custfieldvalue.getId()).append(";%'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '%;")
						.append(custfieldvalue.getId()).append(";%'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" like '%;")
						.append(custfieldvalue.getId()).append("'");
				sql.append(" or lfClient.").append(
						custfieldvalue.getField_Ref()).append(" = '").append(
						custfieldvalue.getId()).append("')");
			}
		} else
		{
			return null;
		}
		return findEntityListBySQL(LfClient.class, sql.toString(),
				StaticValue.EMP_POOLNAME);
	}

	 
    public List<LfDep> findDomDepBySysuserID(String sysuserID,
                                             LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID;
		StringBuffer orderSqlSb = new StringBuffer();
		Map<String, String> columns = TableLfDep.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext())
			{
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue()))
				{
					orderSqlSb.append("dep.").append(columns.get(e.getKey()))
							.append(" ").append(e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		return findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * 返回所有的操作员机构
	 * @param //userId
	 * @return
	 * @throws Exception
	 */
	public List<LfDep> findDomDepBySysuserID(String sysuserID,String corpCode,
			LinkedHashMap<String, String> orderbyMap) throws Exception
	{
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID +" and dep."+TableLfDep.CORP_CODE +"='"+corpCode+"'";
		StringBuffer orderSqlSb = new StringBuffer();
		Map<String, String> columns = TableLfDep.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext())
			{
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue()))
				{
					orderSqlSb.append("dep.").append(columns.get(e.getKey()))
							.append(" ").append(e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		return findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
	}
	

	 
    public List<LfSysuser> findDomUserBySysuserID(String sysuserID)
			throws Exception
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

	 
    public List<LfSysuser> findDomUsedUserBySysuserID(String sysuserID)
			throws Exception
	{
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(sysuserID)
				.append(" and " + TableLfSysuser.USER_STATE + " = 1");
		StringBuffer dominationSql = new StringBuffer(" where (").append(
				TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
				" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
				domination).append("))");
		String sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<LfSysuser> findDomUserExceptSelfBySysuserID(String sysuserID)
			throws Exception
	{
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.DEP_ID
				+ "=domination." + TableLfDomination.DEP_ID
				+ " where domination." + TableLfDomination.USER_ID + "="
				+ sysuserID + " and sysuser." + TableLfSysuser.USER_ID + "!="
				+ sysuserID;
		sql += " order by sysuser." + TableLfSysuser.USER_ID + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		return returnList;
	}




	 
    public List<LfPrivilege> findPrivilegesBySysuserId(String sysuserID,
                                                       String functionType) throws Exception
	{
		String privilegeSql = "";
		if (functionType.equals("MENU"))
		{
			privilegeSql = "select  lfprivilege." + TableLfPrivilege.MENU_CODE
					+ "  ,lfprivilege." + TableLfPrivilege.MENU_SITE
					+ "  ,lfprivilege." + TableLfPrivilege.RESOURCE_ID
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_TW_MOD_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_HK_MOD_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_HK_MENU_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_TW_MENU_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.MENU_NAME + "  ,"
					+ TableLfPrivilege.MOD_NAME + "  from "
					+ TableLfPrivilege.TABLE_NAME + " lfprivilege" + " where "
					+ TableLfPrivilege.PRIVILEGE_ID + " in ( select "
					+ TableLfImpower.PRIVILEGE_ID + " from "
					+ TableLfImpower.TABLE_NAME + " where "
					+ TableLfImpower.ROLE_ID + " in (select "
					+ TableLfUser2role.ROLE_ID + " from "
					+ TableLfUser2role.TABLE_NAME + " where "
					+ TableLfUser2role.USER_ID + " = " + sysuserID + ")) and "
					+ TableLfPrivilege.OPERATE_ID + "=1 group by "
					+ TableLfPrivilege.MENU_CODE + ","
					+ TableLfPrivilege.MENU_SITE + ","
					+ TableLfPrivilege.RESOURCE_ID + ","
					+ TableLfPrivilege.ZH_TW_MOD_NAME + ","
					+ TableLfPrivilege.ZH_HK_MOD_NAME + ","
					+ TableLfPrivilege.ZH_HK_MENU_NAME + ","
					+ TableLfPrivilege.ZH_TW_MENU_NAME + ","
					+ TableLfPrivilege.MENU_NAME + ","
					+ TableLfPrivilege.MOD_NAME + " order by "
					+ TableLfPrivilege.MENU_CODE + " asc";
		} else if ("SITE".equals(functionType))
		{
			privilegeSql = new StringBuffer("select * from ").append(
					TableLfPrivilege.TABLE_NAME).append(" where ").append(
					TableLfPrivilege.OPERATE_ID).append("=1").toString();
		} else
		{
			privilegeSql = "select *  from " + TableLfPrivilege.TABLE_NAME
					+ " where " + TableLfPrivilege.PRIVILEGE_ID
					+ " in ( select " + TableLfImpower.PRIVILEGE_ID + " from "
					+ TableLfImpower.TABLE_NAME + " where "
					+ TableLfImpower.ROLE_ID + " in (select "
					+ TableLfUser2role.ROLE_ID + " from "
					+ TableLfUser2role.TABLE_NAME + " where "
					+ TableLfUser2role.USER_ID + " = " + sysuserID + "))  "
					+ " order by " + TableLfPrivilege.PRIV_CODE + " asc";
		}
		List<LfPrivilege> lfPrivilegesList = findPartEntityListBySQL(
				LfPrivilege.class, privilegeSql, StaticValue.EMP_POOLNAME);
		return lfPrivilegesList;
	}

	 
    public List<LfUdgroup> findUdgroupInfo(String sysuserId) throws Exception
	{
		List<LfSysuser> lfsysuserList = findDomUserBySysuserID(sysuserId);
		String userIds = "";
		for (int index = 0; index < lfsysuserList.size(); index++)
		{
			LfSysuser lfsysuser = lfsysuserList.get(index);
			userIds += lfsysuser.getUserId() + ",";
		}
		if (null != userIds && !"".equals(userIds))
		{
			userIds = userIds.substring(0, userIds.lastIndexOf(","));
		}
		String sql = "select udg.*" + " from " + TableLfUdgroup.TABLE_NAME
				+ " udg where udg.USER_ID IN (" + userIds + ")";
		List<LfUdgroup> lfUdgroupList = findEntityListBySQL(LfUdgroup.class,
				sql, StaticValue.EMP_POOLNAME);
		return lfUdgroupList;
	}


	 
    public List<Userdata> findPartUserdata() throws Exception
	{
		String sql = new StringBuffer("select distinct userdata.").append(
				TableUserdata.USER_ID).append(",userdata.").append(
				TableUserdata.STAFF_NAME).append(" from ").append(
				TableUserdata.TABLE_NAME).append(" userdata inner join ")
				.append(TableGtPortUsed.TABLE_NAME).append(
						" gtportused on userdata.").append(
						TableUserdata.USER_ID).append("=gtportused.").append(
						TableGtPortUsed.USER_ID).append(" where gtportused.")
				.append(TableGtPortUsed.USER_ID).append("<>").append(
						"gtportused.").append(TableGtPortUsed.LOGIN_ID).append(
						" order by userdata.").append(TableUserdata.USER_ID)
				.append(" asc").toString();
		List<Userdata> returnList = findPartEntityListBySQL(Userdata.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}


	 
    public List<GtPortUsed> findMtSpgate() throws Exception
	{
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.SPGATE).append(",").append(
				TableGtPortUsed.SPISUNCM).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1)").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findMtSpgate(String spgate) throws Exception
	{
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(",").append(
				TableGtPortUsed.CPNO).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1)").append(" and ")
				.append(TableGtPortUsed.SPGATE).append("='").append(spgate)
				.append("'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public boolean confirmUpdateBeforeRun(LfTimer lfTimer, String timerHistoryID)
	{
		boolean isSuccess = false;
		StringBuffer conditionSql = new StringBuffer();
		if (lfTimer.getPreTime() != null)
		{
			conditionSql.append(" and ").append(TableLfTimer.PRE_TIME).append(
					"=?");
		} else
		{
			conditionSql.append(" and ").append(TableLfTimer.PRE_TIME).append(
					" is null");
		}
		if (lfTimer.getNextTime() != null)
		{
			conditionSql.append(" and ").append(TableLfTimer.NEXT_TIME).append(
					"=?");
		} else
		{
			conditionSql.append(" and ").append(TableLfTimer.NEXT_TIME).append(
					" is null");
		}
		String lfTimerSql = new StringBuffer("select * from ").append(
				TableLfTimer.TABLE_NAME).append(" where ").append(
				TableLfTimer.TIMER_TASK_ID).append("=").append(
				lfTimer.getTimerTaskId()).append(" and ").append(
				TableLfTimer.RUN_COUNT).append("=").append(
				lfTimer.getRunCount()).append(" and ").append(
				TableLfTimer.RUNSTATE).append("=")
				.append(lfTimer.getRunState()).append(conditionSql).toString();
		String lfTimerHistorySql = new StringBuffer("select * from ").append(
				TableLfTimerHistory.TABLE_NAME).append(" where ").append(
				TableLfTimerHistory.TA_ID).append("=").append(timerHistoryID)
				.toString();
		boolean lfTimerFlag = false;
		boolean lfTimerHistoryFlag = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + lfTimerSql);
			ps = conn.prepareStatement(lfTimerSql);
			if (lfTimer.getPreTime() != null)
			{
				ps.setTimestamp(1, lfTimer.getPreTime());
			}
			if (lfTimer.getNextTime() != null)
			{
				ps.setTimestamp(2, lfTimer.getNextTime());
			}
			rs = ps.executeQuery();
			if (rs.next())
			{
				lfTimerFlag = true;
			}
			EmpExecutionContext.sql("execute sql : " + lfTimerHistorySql);
			ps = conn.prepareStatement(lfTimerHistorySql);
			rs = ps.executeQuery();
			if (rs.next())
			{
				lfTimerHistoryFlag = true;
			}
			if (lfTimerFlag && lfTimerHistoryFlag)
			{
				isSuccess = true;
			}
		} catch (Exception e)
		{
			EmpExecutionContext
					.debug("EmpSpecialDAO的confirmUpdateBeforeRun方法出错！");
		} finally
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
                EmpExecutionContext.error(e, "发现异常");
			}
		}
		return isSuccess;
	}

	 
    public List<GtPortUsed> findGtPortUsedMonopolize() throws Exception
	{
		List<GtPortUsed> returnList = null;
		String usedUserIDSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.BIIND_TYPE).append("=0 and ").append(
				TableLfSpDepBind.SHARE_TYPE).append("=0").toString();
		List<LfSpDepBind> lfimwg2empList = findPartEntityListBySQL(
				LfSpDepBind.class, usedUserIDSql, StaticValue.EMP_POOLNAME);
		if (lfimwg2empList != null && lfimwg2empList.size() > 0)
		{
			returnList = new ArrayList<GtPortUsed>();
		} else
		{
			usedUserIDSql = new StringBuffer("select distinct ").append(
					TableLfSpDepBind.SP_USER).append(" from ").append(
					TableLfSpDepBind.TABLE_NAME).append(" where ").append(
					TableLfSpDepBind.BIIND_TYPE).append("=1").toString();
			String sql = new StringBuffer("select distinct ").append(
					TableGtPortUsed.USER_ID).append(" from ").append(
					TableGtPortUsed.TABLE_NAME).append(" where ").append(
					TableGtPortUsed.USER_ID).append("<>").append(
					TableGtPortUsed.LOGIN_ID).append(" and ").append(
					TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ")
					.append(TableGtPortUsed.USER_ID).append(" not in (")
					.append(usedUserIDSql).append(")").append(" order by ")
					.append(TableGtPortUsed.USER_ID).append(" asc").toString();
			returnList = findPartEntityListBySQL(GtPortUsed.class, sql,
					StaticValue.EMP_POOLNAME);
		}
		return returnList;
	}

	 
    public List<GtPortUsed> findGtPortUsedShare() throws Exception
	{
		String usedUserIDSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.SHARE_TYPE).append("=1").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(
				usedUserIDSql).append(")").append(" order by ").append(
				TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUpdateGtPortUsedMonopolize(String depCodeThird)
			throws Exception
	{
		List<GtPortUsed> returnList = null;
		String usedUserIDSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.BIIND_TYPE).append("=0 and ").append(
				TableLfSpDepBind.SHARE_TYPE).append("=0").toString();
		List<LfSpDepBind> lfimwg2empList = findPartEntityListBySQL(
				LfSpDepBind.class, usedUserIDSql, StaticValue.EMP_POOLNAME);
		if (lfimwg2empList != null && lfimwg2empList.size() > 0)
		{
			returnList = new ArrayList<GtPortUsed>();
		} else
		{
			usedUserIDSql = new StringBuffer("select distinct ").append(
					TableLfSpDepBind.SP_USER).append(" from ").append(
					TableLfSpDepBind.TABLE_NAME).append(" where ").append(
					TableLfSpDepBind.BIIND_TYPE).append("=1 and ").append(
					TableLfSpDepBind.DEP_CODE_THIRD).append("<>'").append(
					depCodeThird).append("'").toString();
			String sql = new StringBuffer("select distinct ").append(
					TableGtPortUsed.USER_ID).append(" from ").append(
					TableGtPortUsed.TABLE_NAME).append(" where ").append(
					TableGtPortUsed.USER_ID).append("<>").append(
					TableGtPortUsed.LOGIN_ID).append(" and ").append(
					TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ")
					.append(TableGtPortUsed.USER_ID).append(" not in (")
					.append(usedUserIDSql).append(")").append(" order by ")
					.append(TableGtPortUsed.USER_ID).append(" asc").toString();
			returnList = findPartEntityListBySQL(GtPortUsed.class, sql,
					StaticValue.EMP_POOLNAME);
		}
		return returnList;
	}

	 
    public List<GtPortUsed> findGtPortUsedPersonal() throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.BIIND_TYPE).append("=1 and ").append(
				TableLfSpDepBind.SHARE_TYPE).append("=1").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(")").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindDBServerUserID() throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=2").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='DBS00A'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindDBServerUserID(String corpCode)
			throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=2").append(" and ")
				.append(TableLfSpDepBind.CORP_CODE).append(" = '").append(
						corpCode).append("'").append(" and ").append(
						TableLfSpDepBind.BIIND_TYPE).append("=0").append(
						" and ").append(TableLfSpDepBind.IS_VALIDATE).append(
						"=1").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='DBS00A'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindEmpUserID() throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=1").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='WBS00A'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindDBUserID() throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=1").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='DBS00A'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindEmpUserID(String corpCode)
			throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=1").append(" and ")
				.append(TableLfSpDepBind.CORP_CODE).append(" = '").append(
						corpCode.trim()).append("'").append(" and ").append(
						TableLfSpDepBind.BIIND_TYPE).append("=0").append(
						" and ").append(TableLfSpDepBind.IS_VALIDATE).append(
						"=1").toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append("  in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='WBS00A'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindCorpUserID(String corpCode,
                                                 String platformType) throws Exception
	{
		String bindSpSql = new StringBuffer(" select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=")
				.append(platformType).append(" and ").append(
						TableLfSpDepBind.BIIND_TYPE).append(" is null ")
				.append(" and ").append(TableLfSpDepBind.CORP_CODE).append(
						" = '").append(corpCode).append("'").toString();

		/*
		 * String bindSpSql = new StringBuffer("select distinct ").append(
		 * TableLfSpDepBind.SP_USER).append(" from ").append(
		 * TableLfSpDepBind.TABLE_NAME).append(" where ").append(
		 * TableLfSpDepBind.PLATFORM_TYPE).append("=1").toString();
		 */
		String loginId = "";
		if ("1".equals(platformType))
		{
			loginId = "WBS00A";
		} else
		{
			loginId = "DBS00A";
		}

		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID)
				.append("= '").append(loginId).append("' ")
				.append(" order by ").append(TableGtPortUsed.USER_ID).append(
						" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findUnBindUserIDByType(String platformType)
			throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=")
				.append(platformType).toString();

		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='WBS00A'").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findAllDBServerUserID() throws Exception
	{
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.LOGIN_ID).append("='DBS00A'").append(
				" order by ").append(TableGtPortUsed.USER_ID).append(" asc")
				.toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<GtPortUsed> findAllEmpUserID() throws Exception
	{
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.LOGIN_ID).append("='WBS00A'").append(
				" order by ").append(TableGtPortUsed.USER_ID).append(" asc")
				.toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<Userdata> findUnBindUserdataForFlow() throws Exception
	{
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfFlow.USERID).append(" from ").append(
				TableLfFlow.TABLE_NAME).toString();
		String sql = new StringBuffer("select distinct ").append(
				TableUserdata.USER_ID).append(",").append(
				TableUserdata.STAFF_NAME).append(" from ").append(
				TableUserdata.TABLE_NAME).append(" where ").append(
				TableUserdata.USER_ID).append("<>").append(
				TableUserdata.LOGIN_ID).append(" and ").append(
				TableUserdata.USER_ID).append(" not in (").append(bindSpSql)
				.append(")").append(" order by ").append(TableUserdata.USER_ID)
				.append(" asc").toString();
		List<Userdata> returnList = findPartEntityListBySQL(Userdata.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<Userdata> findUserdataListByUserId(Long userId)
			throws Exception
	{

		StringBuffer sqlBuffer = new StringBuffer();

		int bindType = getBindType();

		if (bindType == 1)
		{
			sqlBuffer.append("select lfspdepbind.* from ").append(
					TableLfSysuser.TABLE_NAME).append(" lfsysuser inner join ")
					.append(TableLfDep.TABLE_NAME).append(
							" lfdep on lfsysuser.").append(
							TableLfSysuser.DEP_ID).append("=lfdep.").append(
							TableLfDep.DEP_ID).append(" and").append(
							" lfsysuser.").append(TableLfSysuser.USER_ID)
					.append("=").append(userId).append(" inner join ").append(
							TableLfSpDepBind.TABLE_NAME).append(
							" lfspdepbind on lfdep.").append(
							TableLfDep.DEP_CODE_THIRD).append("=lfspdepbind.")
					.append(TableLfSpDepBind.DEP_CODE_THIRD).append(
							" where " + TableLfSpDepBind.BIIND_TYPE)
					.append("=").append(bindType);
		} else if (bindType == 2)
		{
			sqlBuffer.append("select * from ").append(
					TableLfSpDepBind.TABLE_NAME).append(
					" where " + TableLfSpDepBind.USER_ID).append("=").append(
					userId);
		} else
		{
			return null;
		}
		List<LfSpDepBind> lfSpDepBindList = findEntityListBySQL(
				LfSpDepBind.class, sqlBuffer.toString(),
				StaticValue.EMP_POOLNAME);

		LfSpDepBind lfSpDepBind = null;
		if (lfSpDepBindList.isEmpty())
		{
			return new ArrayList<Userdata>();
		} else
		{
			lfSpDepBind = lfSpDepBindList.get(0);
		}

		List<Userdata> userdataList = null;

		if (lfSpDepBind.getBindType() == 0)
		{
			userdataList = findPartUserdata();
		} else
		{
			StringBuffer spUsers = new StringBuffer();
			for (int i = 0; i < lfSpDepBindList.size(); i++)
			{
				lfSpDepBind = lfSpDepBindList.get(i);
				spUsers.append("'").append(lfSpDepBind.getSpUser());
				spUsers.append("'");
				if (i != lfSpDepBindList.size() - 1)
				{
					spUsers.append(",");
				}
			}
			sqlBuffer = new StringBuffer();
			sqlBuffer.append("select * from ").append(TableUserdata.TABLE_NAME)
					.append(" where ").append(TableUserdata.USER_ID).append(
							" in (").append(spUsers).append(")");
			userdataList = findPartEntityListBySQL(Userdata.class, sqlBuffer
					.toString(), StaticValue.EMP_POOLNAME);
		}
		return userdataList;
	}

	 
    public List<LfEmployeeDep> findDomEmployeeDepBySysuserID(Long loginUserID)
			throws Exception
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
		}

		lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
				queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);

		if (lfEmployeeDepList != null && lfEmployeeDepList.size() > 0)
		{
			queryEmpDepByUserId = new StringBuffer(" select * from ").append(
					TableLfEmployeeDep.TABLE_NAME)
					.append(" lfempdep");
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < lfEmployeeDepList.size(); i++)
			{
				LfEmployeeDep employeeDep = lfEmployeeDepList.get(i);
				buffer.append(" lfempdep.").append(TableLfEmployeeDep.DEP_ID)
						.append(" ='").append(employeeDep.getDepId()).append(
								"'").append(" or lfempdep.").append(
								TableLfEmployeeDep.PARENT_ID).append("=")
						.append(employeeDep.getDepId());
				if (lfEmployeeDepList.size() - i != 1)
				{
					buffer.append(" or ");
				}
			}
			queryEmpDepByUserId.append(" where ( ").append(buffer.toString())
					.append(" ) order by ").append(TableLfEmployeeDep.DEP_ID)
					.append(" ASC");

			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
					queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);
		}
		return lfEmployeeDepList;
	}

	
	// 根据当前用户id查询对哪些客户机构有权限
	 
    public List<LfClientDep> findDomClientDepBySysuserIDOnlyClientAddr(
			Long loginUserID, String corpCode) throws Exception
	{
		List<LfClientDep> lfClientDepList = new ArrayList<LfClientDep>();
		// 不是管理员,查询操作员拥有的客户机构权限绑定记录
		StringBuffer queryEmpDepByUserId = new StringBuffer(" select * from ")
				.append(TableLfCliDepConn.TABLE_NAME).append(
						" lfclientdepconn ").append(StaticValue.getWITHNOLOCK())
				.append(" left join ").append(TableLfClientDep.TABLE_NAME)
				.append(" lfclientdep ").append(StaticValue.getWITHNOLOCK()).append(
						" on lfclientdepconn.")
				.append(TableLfCliDepConn.DEP_ID).append("=lfclientdep.")
				.append(TableLfClientDep.DEP_ID).append(" where  lfclientdep.")
				.append(TableLfClientDep.CORP_CODE).append("='").append(
						corpCode).append("'");
		if (loginUserID != null)
		{
			queryEmpDepByUserId.append(" and lfclientdepconn.").append(
					TableLfCliDepConn.USER_ID).append("=").append(loginUserID);
		}

		lfClientDepList = findEntityListBySQL(LfClientDep.class,
				queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);

		if (lfClientDepList != null && lfClientDepList.size() > 0)
		{
			queryEmpDepByUserId = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(" lfclientdep ")
					.append(StaticValue.getWITHNOLOCK()).append(" where ").append(
							"lfclientdep.").append(TableLfClientDep.CORP_CODE)
					.append("='").append(corpCode).append("'");

	
				LfClientDep clientDep = lfClientDepList.get(0);
				// 查询当前机构和直接子机构
				queryEmpDepByUserId.append(" and (lfclientdep.").append(
						TableLfClientDep.DEP_ID).append(" =").append(
						clientDep.getDepId()).append(" or lfclientdep.")
						.append(TableLfClientDep.PARENT_ID).append(" =")
						.append(clientDep.getDepId()).append(" ) order by ")
						.append(TableLfClientDep.PARENT_ID).append(" ASC");
		
			lfClientDepList = findEntityListBySQL(LfClientDep.class,
					queryEmpDepByUserId.toString(), StaticValue.EMP_POOLNAME);
		}
		return lfClientDepList;
	}

	 
    public String getUserIdBySpgate(String spgate, int spisuncm)
			throws Exception
	{
		String userId = null;

		String sql = new StringBuffer("select userdata.").append(
				TableUserdata.USER_ID).append(" from ").append(
				TableUserdata.TABLE_NAME).append(" userdata left join ")
				.append(TableGtPortUsed.TABLE_NAME).append(
						" gtportused  on gtportused.").append(
						TableGtPortUsed.USER_ID).append("=userdata.").append(
						TableUserdata.USER_ID).append(" where userdata.")
				.append(TableUserdata.USER_TYPE).append("=1 and gtportused.")
				.append(TableGtPortUsed.SPGATE).append(" = '").append(spgate)
				.append("'and gtportused.").append(TableGtPortUsed.SPISUNCM)
				.append("=").append(spisuncm).toString();

		userId = getString(TableUserdata.USER_ID, sql, StaticValue.EMP_POOLNAME);

		return userId;
	}

	 
    public List<Userdata> findBindUserdataForRute() throws Exception
	{
		String sql = new StringBuffer("select u.")
				.append(TableUserdata.USER_ID).append(",u.").append(
						TableUserdata.STAFF_NAME).append(" from ").append(
						TableUserdata.TABLE_NAME).append(" u where u.").append(
						TableUserdata.USER_ID).append(" in (select rute.")
				.append(TableGtPortUsed.USER_ID).append(" from ").append(
						TableGtPortUsed.TABLE_NAME).append(
						" rute group by rute.").append(TableGtPortUsed.USER_ID)
				.append(")  and u.").append(TableUserdata.USER_TYPE).append(
						"=0").toString();

		List<Userdata> userdatas = findPartEntityListBySQL(Userdata.class, sql,
				StaticValue.EMP_POOLNAME);

		return userdatas;
	}

	
	 
    public List<Userdata> findAgentUserdata() throws Exception
	{
		String sql = new StringBuffer("select * ").append(" from ").append(
				TableUserdata.TABLE_NAME).append("  where ").append(
				TableUserdata.USER_ID).append(" = ").append(
				TableUserdata.LOGIN_ID).append(" and ").append(
				TableUserdata.USER_TYPE).append(" = 0 ").toString();

		List<Userdata> userdatas = findPartEntityListBySQL(Userdata.class, sql,
				StaticValue.EMP_POOLNAME);
		return userdatas;
	}

	 
    public List<GtPortUsed> findGtPortUsedForBlackList(String spUser)
			throws Exception
	{
		String sql = new StringBuffer(" select * from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.SPGATE).append(" in (").append(" select ")
				.append(TableGtPortUsed.SPGATE).append(" from ").append(
						TableXtGateQueue.TABLE_NAME).append(" where ").append(
						TableGtPortUsed.ID).append(" in ( select ").append(
						TableAgwSpBind.GATEID).append(" from ").append(
						TableAgwSpBind.TABLE_NAME).append("))").append(" and ")
				.append(TableGtPortUsed.USER_ID).append(" = '").append(spUser)
				.append("' ").toString();

		List<GtPortUsed> gtportuseds = findPartEntityListBySQL(
				GtPortUsed.class, sql, StaticValue.EMP_POOLNAME);
		return gtportuseds;
	}

	 
    public List<LfProcess> findLfProcessBySpUser(String proCode)
			throws Exception
	{
		String sql = new StringBuffer(" select * ").append(" from ").append(
				TableLfProcess.TABLE_NAME).append(" lfprocess where")
				.append(" lfprocess.").append(TableLfProcess.MSG_TYPE)
				.append(" = 1 ").append("and upper(lfprocess.").append(
						TableLfProcess.PRO_CODE).append(") = '").append(
						proCode.toUpperCase()).append("'").append(
						" and lfprocess.").append(TableLfProcess.SER_ID).append(
						" in (select lfservice.").append(TableLfService.SER_ID)
				.append(" from ").append(TableLfService.TABLE_NAME).append(
						" lfservice").append(") ").toString();

		List<LfProcess> lfprocess = findEntityListBySQL(LfProcess.class, sql,
				StaticValue.EMP_POOLNAME);

		return lfprocess;
	}

	 
    public List<LfUdgroup> findLfUdgroupByUserId(Long userId, int groupType,
                                                 PageInfo pageInfo) throws Exception
	{

		LfSysuser lfSysuser = empDAO.findObjectByID(LfSysuser.class,
				userId);
		String sql = new StringBuffer("select distinct udgroup.*").toString();
		String countSql = "select count(*) totalcount";
		String tableSql = new StringBuffer(" from ").append(
				TableLfUdgroup.TABLE_NAME).append(" udgroup ").append(
				"left join ").append(TableLfList2gro.TABLE_NAME).append(
				" list2gro on udgroup.").append(TableLfUdgroup.UDG_ID).append(
				"=list2gro.").append(TableLfList2gro.UDG_ID).append(
				" where").toString();

		sql += tableSql;
		countSql += tableSql;
		StringBuffer conditionSql = new StringBuffer();
		conditionSql.append(" (list2gro.").append(TableLfList2gro.GUID)
				.append("=").append(lfSysuser.getGuId());
		conditionSql.append(" or udgroup.").append(TableLfUdgroup.USER_ID)
				.append("=").append(userId).append(")");
		if (groupType != 0)
		{
			conditionSql.append(" and udgroup.").append(
					TableLfUdgroup.GROUP_TYPE).append("=").append(groupType);
		}
		sql += conditionSql.toString();

		sql += new StringBuffer(" order by udgroup.").append(
				TableLfUdgroup.UDG_NAME).append(" asc ").toString();

		countSql += conditionSql;
		List<LfUdgroup> returnVoList = genericDAO.findPageEntityListBySQL(LfUdgroup.class,
				sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return returnVoList;

	}


	 
    public List<LfSysuser> getLfSysUserByUP(String userName, String passWord)
			throws Exception
	{
		StringBuffer sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(" lfSysUser where")
				.append(" upper(lfSysUser.").append(
						TableLfSysuser.USER_NAME).append(") = '").append(
						userName).append("' ");
		if (passWord != null && !"".equals(passWord))
		{
			sql.append(" and lfSysUser.").append(TableLfSysuser.PASSW)
					.append(" = '").append(passWord).append("'");
		}
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
    public List<XtGateQueue> findGateInfoByCorp(String corp, PageInfo pageInfo)
			throws Exception
	{
		String sql = "select DISTINCT xt.* from " + TableXtGateQueue.TABLE_NAME
				+ " xt join " + TableGtPortUsed.TABLE_NAME + " gt on " + "xt." + TableXtGateQueue.SPGATE
				+ "=gt." + TableGtPortUsed.SPGATE + " where gt." + TableGtPortUsed.USER_ID
				+ " in (select sp.spuser from " + TableLfSpDepBind.TABLE_NAME
				+ " sp where sp." + TableLfSpDepBind.CORP_CODE + "='" + corp + "')";

		if (pageInfo == null)
		{
			return findEntityListBySQL(XtGateQueue.class, sql,
					StaticValue.EMP_POOLNAME);
		} else
		{
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return genericDAO.findPageEntityListBySQL(XtGateQueue.class, sql, countSql,
					pageInfo, StaticValue.EMP_POOLNAME);
		}
	}

	 
    public boolean isParamItemExists(String paramItem, String gwType)
			throws Exception
	{

		boolean result = false;
		String sql = "select * from " + TableAgwParamConf.TABLE_NAME + " where upper("
				+ TableAgwParamConf.PARAMITEM + ")='" + paramItem.toUpperCase() + "' and "
				+ TableAgwParamConf.GWTYPE + "=" + gwType;
		List<AgwParamConf> agwList = findEntityListBySQL(AgwParamConf.class,
				sql, StaticValue.EMP_POOLNAME);

		if (agwList != null && agwList.size() > 0)
		{
			result = true;
		}
		return result;
	}

	 
    public List<AgwParamValue> getAgwParamValue(String gwType) throws Exception
	{
		String sql = "select " + TableAgwParamValue.GWNO + " from " + TableAgwParamValue.TABLE_NAME
				+ " where " + TableAgwParamValue.GWTYPE + "=" + gwType + " group by  "
				+ TableAgwParamValue.GWNO;

		return findPartEntityListBySQL(AgwParamValue.class, sql,
				StaticValue.EMP_POOLNAME);

	}
	 
    public List<GtPortUsed> findMtSpgateByCorp(String corpCode)
			throws Exception
	{


		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.SPGATE).append(",").append(
				TableGtPortUsed.SPISUNCM).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1)").append(" and ")
				.append(TableGtPortUsed.USER_ID).append(" in ").append(
						"(select spuser from " + TableLfSpDepBind.TABLE_NAME + "  where "
								+ TableLfSpDepBind.CORP_CODE + "='" + corpCode + "' and "
								+ TableLfSpDepBind.IS_VALIDATE + "=1)").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	 
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
					.append(" lfempdep");
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
	 * 查询出机构2级的
	 * 
	 * @param lfSysuser
	 *            当前操作员
	 * @param depCode
	 *            点击该机构的DEPCODE 传NULL进来只查当前所属操作员
	 * @return
	 * @throws Exception
	 */
	 
    public List<LfEmployeeDep> getEmpSecondDepTree(LfSysuser lfSysuser,
                                                   String depId) throws Exception
	{
		List<LfEmployeeDep> lfEmployeeDepList = new ArrayList<LfEmployeeDep>();
		String corpCode = lfSysuser.getCorpCode();
		Long userId = lfSysuser.getUserId();

		// 查找当前登录用户所绑定的员工机构信息
		StringBuffer queryEmpDepByUserId = new StringBuffer(" select * from ")
				.append(TableLfEmpDepConn.TABLE_NAME).append(" lfempdepconn ")
				.append(StaticValue.getWITHNOLOCK()).append(
						" where lfempdepconn.").append(
						TableLfEmpDepConn.USER_ID).append("=").append(userId);

		List<LfEmpDepConn> lfempDepList = findEntityListBySQL(
				LfEmpDepConn.class, queryEmpDepByUserId.toString(),
				StaticValue.EMP_POOLNAME);

		if (lfempDepList != null && lfempDepList.size() > 0)
		{
			// 说明是第一次进入
			if (depId == null)
			{
				LfEmpDepConn dep = lfempDepList.get(0);
				String depconnid = dep.getDepId().toString();
				StringBuffer depCodeSql = new StringBuffer(" select * from ")
						.append(TableLfEmployeeDep.TABLE_NAME).append(
								" lfempdep ").append(StaticValue.getWITHNOLOCK())
						.append(" where ( lfempdep.").append(
								TableLfEmployeeDep.DEP_ID).append(" ='")
						.append(depconnid).append("' or lfempdep.").append(
								TableLfEmployeeDep.PARENT_ID).append(" ='")
						.append(depconnid).append("'").append(
								" ) and lfempdep.").append(
								TableLfEmployeeDep.CORP_CODE).append("='")
						.append(corpCode + "' order by ").append(
								TableLfEmployeeDep.DEP_ID).append(
								" " + StaticValue.ASC);
				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						depCodeSql.toString(), StaticValue.EMP_POOLNAME);
			} else
			{
				StringBuffer depCodeSql = new StringBuffer(" select * from ")
						.append(TableLfEmployeeDep.TABLE_NAME).append(
								" lfempdep ").append(StaticValue.getWITHNOLOCK())
						.append(" where ( lfempdep.").append(
								TableLfEmployeeDep.PARENT_ID).append(" =")
						.append(depId).append(" ) and lfempdep.").append(
								TableLfEmployeeDep.CORP_CODE).append("='")
						.append(corpCode + "' order by ").append(
								TableLfEmployeeDep.DEP_ID).append(
								" " + StaticValue.ASC);

				lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class,
						depCodeSql.toString(), StaticValue.EMP_POOLNAME);
			}
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
					.append(" order by ").append(TableLfEmployeeDep.ADD_TYPE).append(
								" " + StaticValue.ASC).toString();
			lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}

		return lfEmployeeDepList;
	}

    /**
     * 获取员工机构列表——用于构建机构树
     * @param userId 当前操作员的ID
     * @param depId 传NULL进来则通过userId去查，有传值时只查传入机构的子级
     * @param corpCode 企业编码
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
                        .append(id).append(") and e.").append(TableLfEmployeeDep.CORP_CODE)
                        .append(" = '").append(corpCode).append("'")
                                .append(" order by ").append(
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
                    .append(" and ").append(TableLfEmployeeDep.CORP_CODE).append(" = '").append(corpCode).append("'")
                    .append(" order by ").append(TableLfEmployeeDep.ADD_TYPE).append(
                            " " + StaticValue.ASC).toString();
            lfEmployeeDepList = findEntityListBySQL(LfEmployeeDep.class, sql,
                    StaticValue.EMP_POOLNAME);
        }

        return lfEmployeeDepList;
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
	 * @param userId
	 *            当前操作员的公司
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
							")").append(" and e."+TableLfClientDep.CORP_CODE+"= '"+corpCode+"'").toString();
		} else
		{
			sql = new StringBuffer(" select * from ").append(
					TableLfClientDep.TABLE_NAME).append(
					" " + StaticValue.getWITHNOLOCK()).append(" where ").append(
					TableLfClientDep.PARENT_ID).append(" = ").append(depId).
					append(" and "+TableLfClientDep.CORP_CODE+"= '"+corpCode+"'")
					.toString();
		}
		List<LfClientDep> lfClientDepList = findEntityListBySQL(
				LfClientDep.class, sql, StaticValue.EMP_POOLNAME);
		return lfClientDepList;
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
			return c.getInt(3);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询父级机构调用存储过程异常。");
			throw (e);
		} finally
		{
			if (c != null)
			{
				c.close();
				c = null;
			}
			if (conn != null)
			{
				conn.close();
			}
		}
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
	 * 获取客户机构级别
	 */
	 
    public long getClientDepLevByDepId(long depId) throws Exception
	{
		Connection conn = null;
		CallableStatement c = null;
		Integer currentLevel = 0;
		if (StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)
		{
			PreparedStatement ps = null;
			ResultSet rs = null;
			try
			{
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
				String sql = new StringBuffer("select ").append(
						TableLfDep.DEP_LEVEL).append(" from ").append(
						TableLfClientDep.TABLE_NAME).append(" where ").append(
						TableLfClientDep.DEP_ID).append(" = ").append(depId)
						.toString();
				EmpExecutionContext.sql("execute sql : " + sql);
				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				if (rs.next())
				{
					currentLevel = rs.getInt(1);
				}
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取客户机构级别。");
				throw e;
			} finally
			{
				close(rs, ps, conn);
			}
		} else
		{
			try
			{
				conn = connectionManager
						.getDBConnection(StaticValue.EMP_POOLNAME);
				c = conn.prepareCall("{call GETCLIDEPLEVEL(?,?,?)}");
				c.setInt(1, 1);
				c.setString(2, String.valueOf(depId));
				c.registerOutParameter(3, java.sql.Types.INTEGER);// 级别
				c.execute();
				currentLevel = c.getInt(3);
			} catch (Exception e)
			{
				EmpExecutionContext.error(e, "获取客户机构级别调用存储过程异常。");
				throw (e);
			} finally
			{
				if (c != null)
				{
					c.close();
					c = null;
				}
				if(conn !=null){
					conn.close();
				}
			}
		}
		return currentLevel;
	}

	/**
	 * 查找客户机构的总数
	 */
	 
    public long getClientDepCount(String corpCode) throws Exception
	{
		String sql = "select count(*) totalcount from "
				+ TableLfClientDep.TABLE_NAME + StaticValue.getWITHNOLOCK()
				+ " where " + TableLfClientDep.CORP_CODE + "='" + corpCode
				+ "'";
		return findCountBySQL(sql);
	}

	 
    public Map<Long, String> findDepIDandName(String sqlStr) throws Exception
	{
		Map<Long, String> depMap = new LinkedHashMap<Long, String>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sqlStr);
			ps = conn.prepareStatement(sqlStr);
			rs = ps.executeQuery();
			while (rs.next())
			{
				depMap.put(rs.getLong("DEP_ID"), rs.getString("DEP_NAME"));
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取机构编号和名称异常。");
			throw e;
		} finally
		{
			super.close(rs, ps, conn);
		}
		return depMap;
	}

	/**
	 * 通过群组ID及查询条件获取员工信息
	 * 
	 * @param udgId
	 *            群组Id
	 * @param loginId
	 *            操作员登录Id
	 * @param conditionMap
	 *            查询条件
	 * @param orderbyMap
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
			return genericDAO.findPageEntityListBySQL(LfEmployee.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	/**
	 * 通过群组ID及查询条件获取客户信息
	 * 
	 * @param udgId
	 *            群组Id
	 * @param loginId
	 *            操作员登录Id
	 * @param conditionMap
	 *            查询条件
	 * @param orderbyMap
	 *            排序条件
	 * @param pageInfo
	 *            分页信息，无需分析时传入null
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	 
    public List<LfClient> getClientByGuid(String udgId, String loginId,
                                          LinkedHashMap<String, String> conditionMap,
                                          LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception
	{
		List<LfClient> clients = null;
		String sql = "";
		//第一次进入选择员工页面时群组id为空，就根据loginId查询。点击群组之后udgId不会空
		if (udgId != null && !"".equals(udgId))
		{
			sql = "select client.* from " + TableLfClient.TABLE_NAME + " client inner join "
                    +TableLfList2gro.TABLE_NAME+" gro on client."
					+ TableLfClient.GUID + "  = gro."+ TableLfList2gro.GUID
                    + " where gro."+ TableLfList2gro.UDG_ID+" = "+udgId
                    +" and gro." + TableLfList2gro.L2G_TYPE + "=1";
		}else if (loginId != null && !"".equals(loginId))
		{
			sql = "select * from " + TableLfClient.TABLE_NAME + "  where "
					+ TableLfClient.GUID + "  in (select "
					+ TableLfList2gro.GUID + " from "
					+ TableLfList2gro.TABLE_NAME + "  where "
					+ TableLfList2gro.UDG_ID + " in(select  "
					+ TableLfUdgroup.UDG_ID + " from "
					+ TableLfUdgroup.TABLE_NAME + " where "
					+ TableLfUdgroup.USER_ID + "=" + loginId + "))";
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfClient.class);
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
					} else if (eKey.contains("&>"))
					{
						sql = sql + " and " + columnName + ">" + e.getValue()+ " ";
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
						} else
						{
							sql = sql + " and " + columnName + "="+ e.getValue();
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
			clients= findEntityListBySQL(LfClient.class, sql,
					StaticValue.EMP_POOLNAME);
			return clients;
		} else
		{
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return genericDAO.findPageEntityListBySQL(LfClient.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	/**
	 * 通过群组ID及查询条件获取员工信息
	 * @param udgId 群组Id
	 * @param loginId 操作员登录Id
	 * @param conditionMap 查询条件
	 * @param orderbyMap 排序条件
	 * @param pageInfo 分页信息，无需分析时传入null
	 * @param shareType 共享状态
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	 
    public List<LfMalist> getMalistByGuid(String udgId, String loginId, LinkedHashMap<String, String> conditionMap,
                                          LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo, Integer shareType)
			throws Exception {
		String sql = "";
		if(udgId != null && !"".equals(udgId))
		{
            sql = "select malist.* from " + TableLfMalist.TABLE_NAME + " malist inner join "
                    +TableLfList2gro.TABLE_NAME+" gro on malist."
                    + TableLfMalist.GUID + "  = gro."+ TableLfList2gro.GUID
                    + " where gro."+ TableLfList2gro.UDG_ID+" = "+udgId
                    +" and gro." + TableLfList2gro.L2G_TYPE + "= 2";
			if(shareType != null)
			{
				sql += " and gro."+TableLfList2gro.SHARE_TYPE+"="+String.valueOf(shareType);
			}
		}else if(loginId != null && !"".equals(loginId))
		{
			sql = "select * from " + TableLfMalist.TABLE_NAME + "  where "
				+ TableLfMalist.GUID + "  in (select "+TableLfList2gro.GUID+" from " + TableLfList2gro.TABLE_NAME
				+ "  where " + TableLfList2gro.UDG_ID + " in(select  "+TableLfUdgroup.UDG_ID+" from "
				+ TableLfUdgroup.TABLE_NAME + " where "+ TableLfUdgroup.USER_ID+"="+loginId+") and "+TableLfList2gro.L2G_TYPE+"=2)";
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfMalist.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			String fieldType = null;
			while (iter.hasNext()) {
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType()
								.toString();
						break;
					}
				}
				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like")) {
						sql = sql + " and " + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else {
						boolean isDateType = fieldType
						.equals(StaticValue.TIMESTAMP)
						|| fieldType.equals(StaticValue.DATE_SQL)
						|| fieldType.equals(StaticValue.DATE_UTIL);
						if(fieldType.equals("class java.lang.String") || isDateType){
							sql = sql + " and " + columnName + "='" + e.getValue()
							+ "' ";
						}else{
							sql = sql + " and " + columnName + "=" + e.getValue();
						}
					}

				}
			}
		}

		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext()) {
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}

			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		if (pageInfo == null) {
			return findEntityListBySQL(LfMalist.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return genericDAO.findPageEntityListBySQL(LfMalist.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	/**
	 * 通过群组ID及查询条件获取员工信息
	 * @param udgId 群组Id
	 * @param loginId 操作员登录Id
	 * @param conditionMap 查询条件
	 * @param orderbyMap 排序条件
	 * @param pageInfo 分页信息，无需分析时传入null
	 * @return 员工信息的集合
	 * @throws Exception
	 */
	 
    public List<LfEmployee> getEmployeeByGuidForGroup(String udgId, String loginId, LinkedHashMap<String, String> conditionMap,
                                                      LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		String sql = "";
		if(udgId != null && !"".equals(udgId))
		{
            sql = "select employee.* from " + TableLfEmployee.TABLE_NAME + " employee inner join "
                    +TableLfList2gro.TABLE_NAME+" gro on employee."
                    + TableLfEmployee.GUID + "  = gro."+ TableLfList2gro.GUID
                    + " where gro."+ TableLfList2gro.UDG_ID+" = "+udgId
                    +" and gro." + TableLfList2gro.L2G_TYPE + "=0";
		}else if(loginId != null && !"".equals(loginId))
		{
			sql = "select * from " + TableLfEmployee.TABLE_NAME + "  where "
				+ TableLfEmployee.GUID + "  in (select "+TableLfList2gro.GUID+" from " + TableLfList2gro.TABLE_NAME
				+ "  where " + TableLfList2gro.UDG_ID + " in(select  "+TableLfUdgroup.UDG_ID+" from "
				+ TableLfUdgroup.TABLE_NAME + " where "+ TableLfUdgroup.USER_ID+"="+loginId+") and "+TableLfList2gro.L2G_TYPE+"=0)";
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfEmployee.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			String fieldType = null;
			while (iter.hasNext()) {
				e = iter.next();
				for (int index = 0; index < fields.length; index++)
				{
					if (fields[index].getName().equals(e.getKey()))
					{
						fieldType = fields[index].getGenericType()
								.toString();
						break;
					}
				}
				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like")) {
						sql = sql + " and " + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else {
						boolean isDateType = fieldType
						.equals(StaticValue.TIMESTAMP)
						|| fieldType.equals(StaticValue.DATE_SQL)
						|| fieldType.equals(StaticValue.DATE_UTIL);
						if(fieldType.equals("class java.lang.String") || isDateType){
							sql = sql + " and " + columnName + "='" + e.getValue()
							+ "' ";
						}else{
							sql = sql + " and " + columnName + "=" + e.getValue();
						}
					}

				}
			}
		}

		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			sql += " order by ";
			iter = orderbyMap.entrySet().iterator();
			while (iter.hasNext()) {
				e = iter.next();
				sql = sql + columns.get(e.getKey()) + " " + e.getValue() + ",";
			}

			sql = sql.substring(0, sql.lastIndexOf(","));
		}

		if (pageInfo == null) {
			return findEntityListBySQL(LfEmployee.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return genericDAO.findPageEntityListBySQL(LfEmployee.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
}
