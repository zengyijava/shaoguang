package com.montnets.emp.common.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.pasgroup.dao.UserDataDao;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfCustFieldValueVo;
import com.montnets.emp.entity.birthwish.LfBirthdayMember;
import com.montnets.emp.entity.biztype.LfBusManager;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.employee.LfEmployeeDep;
import com.montnets.emp.entity.engine.LfProcess;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.entity.group.LfUdgroup;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.entity.sms.LfSubDrafts;
import com.montnets.emp.entity.sms.MtTask;
import com.montnets.emp.entity.sms.MtTask01_12;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.birthwish.TableLfBirthdayMember;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.engine.TableLfProcess;
import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.gateway.TableAgwParamConf;
import com.montnets.emp.table.gateway.TableAgwParamValue;
import com.montnets.emp.table.gateway.TableAgwSpBind;
import com.montnets.emp.table.group.TableLfList2gro;
import com.montnets.emp.table.group.TableLfUdgroup;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasgrpbind.TableLfAccountBind;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.table.sms.TableMtTask;
import com.montnets.emp.table.sms.TableMtTask01_12;
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

public class SmsSpecialDAO extends SuperDAO{
	
	
	private IEmpDAO smsDao;
	
	private IGenericDAO genericDAO;
	
	public SmsSpecialDAO(){
		DataAccessDriver driver = new DataAccessDriver();
		smsDao=driver.getEmpDAO();
		genericDAO = driver.getGenericDAO();
	}

	/**
	 * 查询绑定类型
	 */
	public int getBindType() throws Exception {
		String sql = new StringBuffer("select ").append(
				TableLfSpDepBind.BIIND_TYPE).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).toString();
		int bindType = getInt(TableLfSpDepBind.BIIND_TYPE, sql,
				StaticValue.EMP_POOLNAME);
		return bindType;
	}

	/**
	 * 查询操作员
	 */
	public List<LfSysuser> findDomSysuserByDepID(String depID,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		//sql拼接
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.USER_ID
				+ "=domination." + TableLfDomination.USER_ID
				+ " where domination." + TableLfDomination.DEP_ID + "=" + depID;
		//排序
		StringBuffer orderSqlSb = new StringBuffer();
		//获取列名
		Map<String, String> columns = TableLfSysuser.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue().toString())) {
					orderSqlSb.append("sysuser.").append(
							columns.get(e.getKey())).append(" ").append(
							e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		//返回查询结果
		return findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
	}

	/**
	 * 查询客户
	 */
	public List<LfClient> findClientByCusFied(String corpCode,
			List<LfCustFieldValueVo> custFieldValueVo) throws Exception {
		//sql拼接
		StringBuffer sql = new StringBuffer("select lfClient.* from ");
		sql.append(TableLfClient.TABLE_NAME)
				.append(" lfClient where lfClient.").append(
						TableLfClient.CORP_CODE).append("='").append(corpCode)
				.append("'");
		if (custFieldValueVo != null && !custFieldValueVo.isEmpty()) {
			for (LfCustFieldValueVo custfieldvalue : custFieldValueVo) {
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
		} else {
			return null;
		}
		//返回结果
		return findEntityListBySQL(LfClient.class, sql.toString(),
				StaticValue.EMP_POOLNAME);
	}
	
	/**
	 *根据客户属性值查询客户
	 */
	public List<LfClient> findClientByCusFieldValue(String corpCode,
			LfCustFieldValueVo custfieldvalue) throws Exception {
		//sql拼接
		StringBuffer sql = new StringBuffer("select lfClient.* from ");
		sql.append(TableLfClient.TABLE_NAME)
				.append(" lfClient where lfClient.").append(
						TableLfClient.CORP_CODE).append("='").append(corpCode)
				.append("'");
		if (custfieldvalue != null) {
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
		} else {
			return null;
		}
		//返回结果
		return findEntityListBySQL(LfClient.class, sql.toString(),
				StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * 根据客户属性查询客户
	 */
	public List<LfClient> findClientByFieldRef(String corpCode,
			String fieldRef) throws Exception {
		//sql拼接
		StringBuffer sql = new StringBuffer("select  lfClient.* from ");
		sql.append(TableLfClient.TABLE_NAME)
				.append(" lfClient where lfClient.").append(
						TableLfClient.CORP_CODE).append("='").append(corpCode)
				.append("' and ");
		if (fieldRef != null && !"".equals(fieldRef.trim())) {
			if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				sql.append(" lfClient.").append(
						fieldRef).append(" is not null ");
			}else{
				sql.append(" lfClient.").append(
						fieldRef).append(" !='' ");
			}
		} else {
			return null;
		}
		//EmpExecutionContext.info("[客户群组群发]sql："+sql.toString());
		//返回结果
		return findEntityListBySQL(LfClient.class, sql.toString(),
				StaticValue.EMP_POOLNAME);
	}
	

	/**
	 * 查询机构
	 */
	public List<LfDep> findDomDepBySysuserID(String sysuserID,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		//sql拼接
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID;
		//排序
		StringBuffer orderSqlSb = new StringBuffer();
		Map<String, String> columns = TableLfDep.getORM();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSqlSb.append(" order by ");
			Iterator<Map.Entry<String, String>> iter = orderbyMap.entrySet()
					.iterator();
			while (iter.hasNext()) {
				Map.Entry<String, String> e = iter.next();
				if (e.getValue() != null && !"".equals(e.getValue())) {
					orderSqlSb.append("dep.").append(columns.get(e.getKey()))
							.append(" ").append(e.getValue()).append(",");
				}
			}
		}
		String orderSql = orderSqlSb.toString();
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty()) {
			orderSql = orderSql.substring(0, orderSql.length() - 1);
		}
		sql += orderSql;
		//返回查询结果
		return findEntityListBySQL(LfDep.class, sql, StaticValue.EMP_POOLNAME);
	}

	/**
	 * 根据id查询操作员
	 */
	public List<LfSysuser> findDomUserBySysuserID(String sysuserID)
			throws Exception {
		//管辖范围sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(sysuserID);
		//管辖范围sql
		StringBuffer dominationSql = new StringBuffer(" where (").append(
				TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
				" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
				domination).append(")) and ").append(TableLfSysuser.USER_ID).append("<>1 ");
		//sql拼接
		String sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		sql += " order by " + TableLfSysuser.NAME + " asc";
		//调用公共方法查询
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		//返回查询结果
		return returnList;
	}
	
	/**
	 * 操作员查询
	 */
	public List<LfSysuser> findDomUsedUserBySysuserID(String sysuserID)
		throws Exception {
		//管辖范围
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(sysuserID).append(" and "+TableLfSysuser.USER_STATE+" = 1");
		//管辖范围sql拼接
		StringBuffer dominationSql = new StringBuffer(" where (").append(
				TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
				" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
				domination).append("))");
		//sql拼接
		String sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		//sql拼接
		sql += " order by " + TableLfSysuser.NAME + " asc";
		//调用公共方法查询
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		//返回查询结果
		return returnList;
	}

	/**
	 * 操作员查询，除本身之外
	 */
	public List<LfSysuser> findDomUserExceptSelfBySysuserID(String sysuserID)
			throws Exception {
		
		//sql拼接
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.DEP_ID
				+ "=domination." + TableLfDomination.DEP_ID
				+ " where domination." + TableLfDomination.USER_ID + "="
				+ sysuserID + " and sysuser." + TableLfSysuser.USER_ID + "!="
				+ sysuserID;
		//sql拼接
		sql += " order by sysuser." + TableLfSysuser.USER_ID + " asc";
		//调用公共方法查询
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		//返回查询结果
		return returnList;
	}

	/**
	 * 查询菜单模块
	 */
	public List<LfPrivilege> findPrivilegesBySysuserId(String sysuserID,
			String functionType) throws Exception {
		//sql
		String privilegeSql = "";
		//如果是查询菜单
		if (functionType.equals("MENU")) {
			privilegeSql = "select  lfprivilege." + TableLfPrivilege.MENU_CODE
					+ "  ,lfprivilege." + TableLfPrivilege.MENU_SITE
					+ "  ,lfprivilege." + TableLfPrivilege.RESOURCE_ID
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_TW_MOD_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_HK_MOD_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_TW_MENU_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_HK_MENU_NAME
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
					+ TableLfPrivilege.ZH_TW_MENU_NAME + ","
					+ TableLfPrivilege.ZH_HK_MENU_NAME + ","
					+ TableLfPrivilege.MENU_NAME + ","
					+ TableLfPrivilege.MOD_NAME + " order by "
					+ TableLfPrivilege.MENU_CODE + " asc";
		} else if ("SITE".equals(functionType)) {
			privilegeSql = new StringBuffer("select * from ").append(
					TableLfPrivilege.TABLE_NAME).append(" where ").append(
					TableLfPrivilege.OPERATE_ID).append("=1").toString();
		} else {
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
		//返回结果
		return lfPrivilegesList;
	}

	/**
	 * 查询用过群组信息
	 */
	public List<LfUdgroup> findUdgroupInfo(String sysuserId) throws Exception {
		//查询管辖范围内的操作员信息
		List<LfSysuser> lfsysuserList = findDomUserBySysuserID(sysuserId);
		String userIds = "";
		//操作员id拼接
		for (int index = 0; index < lfsysuserList.size(); index++) {
			LfSysuser lfsysuser = lfsysuserList.get(index);
			userIds += lfsysuser.getUserId() + ",";
		}
		if (null != userIds && !"".equals(userIds)) {
			userIds = userIds.substring(0, userIds.lastIndexOf(","));
		}
		//sql
		String sql = "select udg.*" + " from " + TableLfUdgroup.TABLE_NAME
				+ " udg where udg.USER_ID IN (" + userIds + ")";
		List<LfUdgroup> lfUdgroupList = findEntityListBySQL(LfUdgroup.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return lfUdgroupList;
	}

	/**
	 * 
	 */
	/*
	public List<MtLevelQueue0_9> findMtLevelInfo(Integer level, Long taskId)
			throws Exception {
		String sql = "select * from ";
		switch (level) {
		case 0:
			sql += TableMtLevelQueue0_9.TABLE_NAME_0;
			break;
		case 1:
			sql += TableMtLevelQueue0_9.TABLE_NAME_1;
			break;
		case 2:
			sql += TableMtLevelQueue0_9.TABLE_NAME_2;
			break;
		case 3:
			sql += TableMtLevelQueue0_9.TABLE_NAME_3;
			break;
		case 4:
			sql += TableMtLevelQueue0_9.TABLE_NAME_4;
			break;
		case 5:
			sql += TableMtLevelQueue0_9.TABLE_NAME_5;
			break;
		case 6:
			sql += TableMtLevelQueue0_9.TABLE_NAME_6;
			break;
		case 7:
			sql += TableMtLevelQueue0_9.TABLE_NAME_7;
			break;
		case 8:
			sql += TableMtLevelQueue0_9.TABLE_NAME_8;
			break;
		case 9:
			sql += TableMtLevelQueue0_9.TABLE_NAME_9;
			break;
		default:
			break;
		}
		sql += " where " + TableMtLevelQueue0_9.TASKID + "=" + taskId;
		List<MtLevelQueue0_9> mtLevelQueue0_9List = findEntityListBySQL(
				MtLevelQueue0_9.class, sql, StaticValue.SMSSVR_POOLNAME);

		return mtLevelQueue0_9List;
	}
*/
	/**
	 * 根据taskid查询mttask信息
	 */
	public List<MtTask> findMtTaskInfo(Long taskId) throws Exception {
		//sql拼接
		String sql = " select * from " + TableMtTask.TABLE_NAME
				+ " mttask where mttask." + TableMtTask.TASK_ID + "=" + taskId;
		//调用公共查询方法查询
		List<MtTask> mtTaskList = findEntityListBySQL(MtTask.class, sql,
				StaticValue.SMSSVR_POOLNAME);

		//返回结果
		return mtTaskList;
	}

	/**
	 * mttask查询
	 */
	public List<MtTask01_12> findMtTaskCMInfo(Long taskId) throws Exception {
		//sql拼接
		String sql = "select * from ";
		int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
		//表名获取
		switch (month) {
		case 1:
			sql += TableMtTask01_12.TABLE_NAME_01;
			break;
		case 2:
			sql += TableMtTask01_12.TABLE_NAME_02;
			break;
		case 3:
			sql += TableMtTask01_12.TABLE_NAME_03;
			break;
		case 4:
			sql += TableMtTask01_12.TABLE_NAME_04;
			break;
		case 5:
			sql += TableMtTask01_12.TABLE_NAME_05;
			break;
		case 6:
			sql += TableMtTask01_12.TABLE_NAME_06;
			break;
		case 7:
			sql += TableMtTask01_12.TABLE_NAME_07;
			break;
		case 8:
			sql += TableMtTask01_12.TABLE_NAME_08;
			break;
		case 9:
			sql += TableMtTask01_12.TABLE_NAME_09;
			break;
		case 10:
			sql += TableMtTask01_12.TABLE_NAME_10;
			break;
		case 11:
			sql += TableMtTask01_12.TABLE_NAME_11;
			break;
		case 12:
			sql += TableMtTask01_12.TABLE_NAME_12;
			break;
		default:
			break;
		}
		sql += " where " + TableMtTask01_12.TASK_ID + "=" + taskId;
		List<MtTask01_12> mtTaskList = findEntityListBySQL(MtTask01_12.class,
				sql, StaticValue.SMSSVR_POOLNAME);
		//返回结果
		return mtTaskList;
	}

	public List<Userdata> findPartUserdata() throws Exception {
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


	public List<GtPortUsed> findMtSpgate() throws Exception {
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.SPGATE).append(",").append(
				TableGtPortUsed.SPISUNCM).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in ('0','1')").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	public List<GtPortUsed> findMtSpgate(String spgate) throws Exception {
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(",").append(
				TableGtPortUsed.CPNO).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in ('0','1')").append(
				" and ").append(TableGtPortUsed.SPGATE).append("='").append(
				spgate).append("'").append(" order by ").append(
				TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	public boolean confirmUpdateBeforeRun(LfTimer lfTimer, String timerHistoryID) {
		boolean isSuccess = false;
		StringBuffer conditionSql = new StringBuffer();
		if (lfTimer.getPreTime() != null) {
			conditionSql.append(" and ").append(TableLfTimer.PRE_TIME).append(
					"=?");
		} else {
			conditionSql.append(" and ").append(TableLfTimer.PRE_TIME).append(
					" is null");
		}
		if (lfTimer.getNextTime() != null) {
			conditionSql.append(" and ").append(TableLfTimer.NEXT_TIME).append(
					"=?");
		} else {
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
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + lfTimerSql);
			ps = conn.prepareStatement(lfTimerSql);
			if (lfTimer.getPreTime() != null) {
				ps.setTimestamp(1, lfTimer.getPreTime());
			}
			if (lfTimer.getNextTime() != null) {
				ps.setTimestamp(2, lfTimer.getNextTime());
			}
			rs = ps.executeQuery();
			if (rs.next()) {
				lfTimerFlag = true;
			}
			EmpExecutionContext.sql("execute sql : " + lfTimerHistorySql);
			ps = conn.prepareStatement(lfTimerHistorySql);
			rs = ps.executeQuery();
			if (rs.next()) {
				lfTimerHistoryFlag = true;
			}
			if (lfTimerFlag && lfTimerHistoryFlag) {
				isSuccess = true;
			}
		} catch (Exception e) {
			EmpExecutionContext
					.debug("EmpSpecialDAO的confirmUpdateBeforeRun方法出错！");
		} finally {
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.debug("关闭数据库资源出错！");
			}
		}
		return isSuccess;
	}

	public List<GtPortUsed> findGtPortUsedMonopolize() throws Exception {
		List<GtPortUsed> returnList = null;
		String usedUserIDSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.BIIND_TYPE).append("=0 and ").append(
				TableLfSpDepBind.SHARE_TYPE).append("=0").toString();
		List<LfSpDepBind> lfimwg2empList = findPartEntityListBySQL(
				LfSpDepBind.class, usedUserIDSql, StaticValue.EMP_POOLNAME);
		if (lfimwg2empList != null && lfimwg2empList.size() > 0) {
			returnList = new ArrayList<GtPortUsed>();
		} else {
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

	public List<GtPortUsed> findGtPortUsedShare() throws Exception {
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
			throws Exception {
		List<GtPortUsed> returnList = null;
		String usedUserIDSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.BIIND_TYPE).append("=0 and ").append(
				TableLfSpDepBind.SHARE_TYPE).append("=0").toString();
		List<LfSpDepBind> lfimwg2empList = findPartEntityListBySQL(
				LfSpDepBind.class, usedUserIDSql, StaticValue.EMP_POOLNAME);
		if (lfimwg2empList != null && lfimwg2empList.size() > 0) {
			returnList = new ArrayList<GtPortUsed>();
		} else {
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

	public List<GtPortUsed> findGtPortUsedPersonal() throws Exception {
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

	public List<GtPortUsed> findUnBindDBServerUserID() throws Exception {
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
			throws Exception {
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=2").append(" and ")
				.append(TableLfSpDepBind.CORP_CODE).append(" = '").append(
						corpCode).append("'").append(" and ").append(
								TableLfSpDepBind.BIIND_TYPE).append("=0").append(" and ")
								.append(TableLfSpDepBind.IS_VALIDATE).append("=1").toString();
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

	public List<GtPortUsed> findUnBindEmpUserID() throws Exception {
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

	public List<GtPortUsed> findUnBindDBUserID() throws Exception {
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
			throws Exception {
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=1").append(" and ")
				.append(TableLfSpDepBind.CORP_CODE).append(" = '").append(
						corpCode.trim()).append("'").append(" and ").append(
						TableLfSpDepBind.BIIND_TYPE).append("=0").append(" and ")
						.append(TableLfSpDepBind.IS_VALIDATE).append("=1").toString();
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
			String platformType) throws Exception {
		String bindSpSql = new StringBuffer(" select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=")
				.append(platformType).append(" and ").append(
						TableLfSpDepBind.BIIND_TYPE).append(" is null ")
				.append(" and ").append(TableLfSpDepBind.CORP_CODE).append(
						" = ").append(corpCode).toString();

		/*
		 * String bindSpSql = new StringBuffer("select distinct ").append(
		 * TableLfSpDepBind.SP_USER).append(" from ").append(
		 * TableLfSpDepBind.TABLE_NAME).append(" where ").append(
		 * TableLfSpDepBind.PLATFORM_TYPE).append("=1").toString();
		 */
		String loginId = "";
		if ("1".equals(platformType)) {
			loginId = "WBS00A";
		} else {
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
			throws Exception {
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

	public List<GtPortUsed> findAllDBServerUserID() throws Exception {
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

	public List<GtPortUsed> findAllEmpUserID() throws Exception {
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

	public List<Userdata> findUnBindUserdataForFlow() throws Exception {
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
			throws Exception {

		StringBuffer sqlBuffer = new StringBuffer();

		int bindType = getBindType();

		if (bindType == 1) {
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
		} else if (bindType == 2) {
			sqlBuffer.append("select * from ").append(
					TableLfSpDepBind.TABLE_NAME).append(
					" where " + TableLfSpDepBind.USER_ID).append("=").append(
					userId);
		} else {
			return null;
		}
		List<LfSpDepBind> lfSpDepBindList = findEntityListBySQL(
				LfSpDepBind.class, sqlBuffer.toString(),
				StaticValue.EMP_POOLNAME);

		LfSpDepBind lfSpDepBind = null;
		if (lfSpDepBindList.isEmpty()) {
			return new ArrayList<Userdata>();
		} else {
			lfSpDepBind = lfSpDepBindList.get(0);
		}

		List<Userdata> userdataList = null;

		if (lfSpDepBind.getBindType() == 0) {
			userdataList = findPartUserdata();
		} else {
			StringBuffer spUsers = new StringBuffer();
			for (int i = 0; i < lfSpDepBindList.size(); i++) {
				lfSpDepBind = lfSpDepBindList.get(i);
				spUsers.append("'").append(lfSpDepBind.getSpUser());
				spUsers.append("'");
				if (i != lfSpDepBindList.size() - 1) {
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


	public String getUserIdBySpgate(String spgate, int spisuncm)
			throws Exception {
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

	public List<Userdata> findBindUserdataForRute() throws Exception {
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


	public List<Userdata> findAgentUserdata() throws Exception {
		// select * from userdata where userid=loginid and usertype=0
		String sql = new StringBuffer("select * ").append(" from ").append(
				TableUserdata.TABLE_NAME).append("  where ").append(
				TableUserdata.USER_ID).append(" = ").append(
				TableUserdata.LOGIN_ID).append(" and ").append(
				TableUserdata.USER_TYPE).append(" = 0 ").toString();

		List<Userdata> userdatas = findPartEntityListBySQL(Userdata.class, sql,
				StaticValue.EMP_POOLNAME);
		return userdatas;
	}
    /**
     * 查询通道号
     */
	public List<GtPortUsed> findGtPortUsedForBlackList(String spUser)
			throws Exception {
		// select * from gt_port_used where spgate in (select * from
		// gt_port_used where loginid = userid) and userid=''
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
			throws Exception {
		String sql = new StringBuffer(" select * ").append(" from ").append(
				TableLfProcess.TABLE_NAME).append(" lfprocess where")
				.append(" lfprocess.").append(TableLfProcess.MSG_TYPE)
				.append(" = 1 ").append("and upper(lfprocess.").append(
						TableLfProcess.PRO_CODE).append(") = '").append(
						proCode.toUpperCase()).append("'").append(
						" and lfprocess.").append(TableLfProcess.SER_ID).append(
						" in (select lfservice.").append(TableLfService.SER_ID)
				.append(" from ").append(TableLfService.TABLE_NAME).append(
						" lfservice ").append(") ").toString();

		List<LfProcess> lfprocess = findEntityListBySQL(LfProcess.class, sql,
				StaticValue.EMP_POOLNAME);

		return lfprocess;
	}

	public List<LfUdgroup> findLfUdgroupByUserId(Long userId, int groupType,
			PageInfo pageInfo) throws Exception {

		LfSysuser lfSysuser = smsDao.findObjectByID(LfSysuser.class, userId);
		String sql = new StringBuffer("select distinct udgroup.*").toString();
		String countSql = "select count(*) totalcount";
		String tableSql = new StringBuffer(" from ").append(
				TableLfUdgroup.TABLE_NAME).append(" udgroup ").append(
				"left join ").append(TableLfList2gro.TABLE_NAME).append(
				" list2gro on udgroup.").append(TableLfUdgroup.UDG_ID).append(
				"=list2gro.").append(TableLfList2gro.UDG_ID).toString();

		sql += tableSql;
		countSql += tableSql;
		StringBuffer conditionSql = new StringBuffer();
		conditionSql.append(" and (list2gro.").append(TableLfList2gro.GUID)
				.append("=").append(lfSysuser.getGuId());
		conditionSql.append(" or udgroup.").append(TableLfUdgroup.USER_ID)
				.append("=").append(userId).append(")");
		if (groupType != 0) {
			conditionSql.append(" and udgroup.").append(
					TableLfUdgroup.GROUP_TYPE).append("=").append(groupType);
		}
		//处理后的条件
		String conditionStr = getConditionSql(conditionSql.toString());
		
		sql +=conditionStr;

		sql += new StringBuffer(" order by udgroup.").append(
				TableLfUdgroup.UDG_NAME).append(" asc ").toString();

		countSql += conditionStr;
		List<LfUdgroup> returnVoList = genericDAO.findPageEntityListBySQL(
						LfUdgroup.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnVoList;

	}


	public List<LfSysuser> getLfSysUserByUP(String userName, String passWord)
			throws Exception {
		StringBuffer sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(" lfSysUser where")
				.append(" upper(lfSysUser.").append(
						TableLfSysuser.USER_NAME).append(") = '").append(
						userName).append("' ");
		if (passWord != null && !"".equals(passWord)) {
			sql.append(" and lfSysUser.").append(TableLfSysuser.PASSW)
					.append(" = '").append(passWord).append("'");
		}
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		return returnList;
	}

	public List<XtGateQueue> findGateInfoByCorp(String corp, PageInfo pageInfo)
			throws Exception {
		
		String sql = "select DISTINCT xt.* from " + TableXtGateQueue.TABLE_NAME + " xt join "
				+ TableGtPortUsed.TABLE_NAME + " gt on " + "xt." + TableXtGateQueue.SPGATE + "=gt."
				+ TableGtPortUsed.SPGATE + " where gt." + TableGtPortUsed.USER_ID
				+ " in (select sp.spuser from " + TableLfSpDepBind.TABLE_NAME
				+ " sp where sp." + TableLfSpDepBind.CORP_CODE + "='" + corp + "')";
		
		if(pageInfo == null)
		{
			return findEntityListBySQL(XtGateQueue.class, sql,
					StaticValue.EMP_POOLNAME);
		}else
		{
			 String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(sql).append(")").toString();
			return genericDAO.findPageEntityListBySQL(XtGateQueue.class, sql, countSql,
							pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	public List<GtPortUsed> findSpRouteByCorp(String corp,LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
		throws Exception 
	{
		String sql = "select * from " + TableGtPortUsed.TABLE_NAME + "   where " + TableGtPortUsed.USER_ID
				+ " in (select spuser from " + TableLfSpDepBind.TABLE_NAME
				+ "  where " + TableLfSpDepBind.CORP_CODE + "='" + corp+ "' and " +TableLfSpDepBind.IS_VALIDATE+"=1)";
		
		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(GtPortUsed.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			
			
			while (iter.hasNext())
			{
				e = iter.next();
				
				if (!"".equals(e.getValue()))
				{
					String eKey=e.getKey();
					columnName = columns.get(eKey);
					
					sql = sql + " and "+columnName+"='"+e.getValue()+"' ";
				}
			}
		}
		String countSql = new StringBuffer("select count(*) totalcount from (")
			.append(sql).append(")").toString();
		return genericDAO.findPageEntityListBySQL(GtPortUsed.class, sql, countSql,
				pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	public List<Userdata> findSpUserByCorp(String corp,LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
		throws Exception {
		String sql = "select * from " + TableUserdata.TABLE_NAME + "   where " + TableUserdata.USER_ID
				+ " in (select spuser from " + TableLfSpDepBind.TABLE_NAME
				+ "  where " + TableLfSpDepBind.CORP_CODE + "='" + corp+ "' and " +TableLfSpDepBind.IS_VALIDATE+"=1)";
		
		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(Userdata.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			
			
			while (iter.hasNext())
			{
				e = iter.next();
				
				if (!"".equals(e.getValue()))
				{
					String eKey=e.getKey();
					columnName = eKey.indexOf("&")<0?columns.get(eKey):columns.get(eKey.subSequence(0, eKey.indexOf("&")));
					if(eKey.contains("&like"))
					{
						sql = sql + " and "+columnName+" like '%"+e.getValue()+"%' ";
					}else if(eKey.contains("&>"))
					{
						sql = sql + " and "+columnName+">"+e.getValue()+" ";
					}else
					{
						sql = sql + " and "+columnName+"='"+e.getValue()+"' ";
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
				sql=sql +columns.get(e.getKey())+" "+e.getValue()+",";
			}
			
			sql=sql.substring(0,sql.lastIndexOf(","));
		}
		
		if(pageInfo == null )
		{
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		}else
		{
			 String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(sql).append(")").toString();
			return genericDAO.findPageEntityListBySQL(Userdata.class, sql, countSql,
							pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	

	public boolean isParamItemExists(String paramItem,String gwType)
		throws Exception {
		
		boolean result = false;
		String sql = "select * from "+TableAgwParamConf.TABLE_NAME+" where upper("+TableAgwParamConf.PARAMITEM+")='"
			+paramItem.toUpperCase()+"' and "+TableAgwParamConf.GWTYPE+"="+gwType ;
		List<AgwParamConf> agwList = findEntityListBySQL(AgwParamConf.class, sql,
				StaticValue.EMP_POOLNAME);
		
		if(agwList != null && agwList.size()>0)
		{
			result = true;
		}
		return result;
	}
	
	
	public List<AgwParamValue> getAgwParamValue(String gwType)
		throws Exception {
		
		String sql = "select "+TableAgwParamValue.GWNO+" from "+TableAgwParamValue.TABLE_NAME+" where "
			+TableAgwParamValue.GWTYPE+"="+gwType+" group by  "+TableAgwParamValue.GWNO;
		
		return  findPartEntityListBySQL(AgwParamValue.class, sql,
				StaticValue.EMP_POOLNAME);
		
	}
	
	/**
	 * xiangxiang
	 * @param corp
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<LfBusManager> findBusTypeByCorp(String corp,LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
		throws Exception {
		String sql = "select * from " + TableLfBusManager.TABLE_NAME + "   where (" + TableLfBusManager.CORP_CODE
		+ "='" + corp+ "' or "+ TableLfBusManager.CORP_CODE +" is null or "+TableLfBusManager.CORP_CODE+"='0')";
		
		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfBusManager.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			
			
			while (iter.hasNext())
			{
				e = iter.next();
				
				if (!"".equals(e.getValue()))
				{
					String eKey=e.getKey();
					columnName = eKey.indexOf("&")<0?columns.get(eKey):columns.get(eKey.subSequence(0, eKey.indexOf("&")));
					if(eKey.contains("&like"))
					{
						sql = sql + " and "+columnName+" like '%"+e.getValue()+"%' ";
					}else if(eKey.contains("&>"))
					{
						sql = sql + " and "+columnName+">"+e.getValue()+" ";
					}else
					{
						sql = sql + " and "+columnName+"='"+e.getValue()+"' ";
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
				sql=sql +columns.get(e.getKey())+" "+e.getValue()+",";
			}
			
			sql=sql.substring(0,sql.lastIndexOf(","));
		}
		
		if(pageInfo == null )
		{
			return findEntityListBySQL(LfBusManager.class, sql,
					StaticValue.EMP_POOLNAME);
		}else
		{
			 String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(sql).append(")").toString();
			return genericDAO.findPageEntityListBySQL(LfBusManager.class, sql, countSql,
							pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	public List<GtPortUsed> findMtSpgateByCorp( String corpCode) throws Exception {
		
		
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.SPGATE).append(",").append(
				TableGtPortUsed.SPISUNCM).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in ('0','1')").append(
				" and ").append(TableGtPortUsed.USER_ID).append(" in ").append("(select spuser from " + TableLfSpDepBind.TABLE_NAME
				+ "  where " + TableLfSpDepBind.CORP_CODE + "='" + corpCode+ "' and " +TableLfSpDepBind.IS_VALIDATE+"=1)").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/**
	 * 用于富信
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @param isContainsMourl
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getSpDepBindWhichUserdataIsOk(LinkedHashMap<String, String> conditionMap,
														LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo,boolean isContainsMourl,boolean flag)
			throws Exception {
		String conditionsql="";
		//查询出来的账户配置了上行URL
		StringBuffer sql=new StringBuffer();
		if(isContainsMourl){
			sql.append("select * from ").append(TableUserdata.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableUserdata.ACCOUNTTYPE).append(" = 1");
			//数据库兼容（mourl!=""这种写法不支持orcal）
			if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE || StaticValue.DBTYPE==StaticValue.SQLSERVER_DBTYPE)
			{
				sql.append(" and ").append(TableUserdata.MO_URL).append(" is not null and ").append(TableUserdata.MO_URL).append("!=''");
			}else if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				sql.append(" and trim(").append(TableUserdata.MO_URL).append(") is not null");
			}else{
				sql.append(" and ").append(TableUserdata.MO_URL).append(" is not null");
			}
			sql.append(" and ").append(TableUserdata.USER_ID).append(" in (select ")
					.append(TableLfSpDepBind.SP_USER).append(" from ").append(TableLfSpDepBind.TABLE_NAME);
			//查询出来的账户不一定配置了上行URL
		}else{
			sql.append("select * from ").append(TableUserdata.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableUserdata.ACCOUNTTYPE).append(" = 1 ").append(UserDataDao.getConditionAccType(2,"USERDATA")).append(" and ").append(TableUserdata.USER_ID).append(" in (select ")
					.append(TableLfSpDepBind.SP_USER).append(" from ").append(TableLfSpDepBind.TABLE_NAME).toString();
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfSpDepBind.class);
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
						conditionsql = conditionsql + " and " + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						conditionsql = conditionsql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else if (eKey.contains("&in")) {
						if(fieldType.equals("class java.lang.String")){
							conditionsql = conditionsql + " and " + columnName + "in '" + e.getValue()
									+ "' ";
						}else{
							conditionsql = conditionsql + " and " + columnName + "in " + e.getValue();
						}
					} else if(eKey.contains("&not in")){
						conditionsql = conditionsql + " and " + columnName + " not in (" + e.getValue() +")";
					} else {
						if (fieldType.equals("class java.lang.String")
								|| fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date")) {
							conditionsql = conditionsql + " and " + columnName + "='" + e.getValue()
									+ "' ";
						}else{
							conditionsql = conditionsql + " and " + columnName + "=" + e.getValue();
						}
					}

				}
			}
		}
		//限制只查询EMP应用账号 SPTYPE为1
		String lastSql = new StringBuffer(" ) and ")
				.append(TableUserdata.STATUS).append("=0 ")
				.append(" and ").append(TableUserdata.SPTYPE).append("=1 ")
				.append(" order by ").append(TableUserdata.USER_ID)
				.toString();
		//拼接conditionsql + lastSql处理后的条件
		sql.append(getConditionSql(conditionsql + lastSql));

		if (pageInfo == null) {
			return findEntityListBySQL(Userdata.class, sql.toString(),
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
					sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	public List<Userdata> getSpDepBindWhichUserdataIsOk(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo,boolean isContainsMourl)
			throws Exception {
		String conditionsql="";
		//查询出来的账户配置了上行URL
		StringBuffer sql=new StringBuffer();
		if(isContainsMourl){
			sql.append("select * from ").append(TableUserdata.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
			.append(" where ").append(TableUserdata.ACCOUNTTYPE).append(" = 1");
			//数据库兼容（mourl!=""这种写法不支持orcal）
			if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE || StaticValue.DBTYPE==StaticValue.SQLSERVER_DBTYPE)
			{
				sql.append(" and ").append(TableUserdata.MO_URL).append(" is not null and ").append(TableUserdata.MO_URL).append("!=''");
			}else if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
				sql.append(" and trim(").append(TableUserdata.MO_URL).append(") is not null");
			}else{
				sql.append(" and ").append(TableUserdata.MO_URL).append(" is not null");
			}
			sql.append(" and ").append(TableUserdata.USER_ID).append(" in (select ")
			.append(TableLfSpDepBind.SP_USER).append(" from ").append(TableLfSpDepBind.TABLE_NAME);
		//查询出来的账户不一定配置了上行URL
		}else{
			sql.append("select * from ").append(TableUserdata.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
			.append(" where ").append(TableUserdata.ACCOUNTTYPE).append(" = 1 ").append(UserDataDao.getConditionAccType(0,"USERDATA")).append(" and ").append(TableUserdata.USER_ID).append(" in (select ")
			.append(TableLfSpDepBind.SP_USER).append(" from ").append(TableLfSpDepBind.TABLE_NAME).toString();
		}

		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(LfSpDepBind.class);
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
						conditionsql = conditionsql + " and " + columnName + " like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						conditionsql = conditionsql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else if (eKey.contains("&in")) {
						if(fieldType.equals("class java.lang.String")){
							conditionsql = conditionsql + " and " + columnName + "in '" + e.getValue()
							+ "' ";
						}else{
							conditionsql = conditionsql + " and " + columnName + "in " + e.getValue();
						}
					} else if(eKey.contains("&not in")){
						conditionsql = conditionsql + " and " + columnName + " not in (" + e.getValue() +")";
					} else {
						if (fieldType.equals("class java.lang.String")
								|| fieldType.equals("class java.sql.Timestamp")
								|| fieldType.equals("class java.util.Date")) {
							conditionsql = conditionsql + " and " + columnName + "='" + e.getValue()
							+ "' ";
						}else{
							conditionsql = conditionsql + " and " + columnName + "=" + e.getValue();
						}
					}

				}
			}
		}
		//限制只查询EMP应用账号 SPTYPE为1
		String lastSql = new StringBuffer(" ) and ")
		.append(TableUserdata.STATUS).append("=0 ")
		.append(" and ").append(TableUserdata.SPTYPE).append("=1 ")
		.append(" order by ").append(TableUserdata.USER_ID)
		.toString();
		//拼接conditionsql + lastSql处理后的条件
		sql.append(getConditionSql(conditionsql + lastSql));

		if (pageInfo == null) {
			return findEntityListBySQL(Userdata.class, sql.toString(),
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
							sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	/**
	 * oracle数据库 当有 ！=''时查不出任何数据  is not null 包含不为空字符串和null的情况
	 * @description    
	 * @param field
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-12-6 下午02:37:42
	 */
	public String notNullSql(String field){
		StringBuffer sb= new StringBuffer();
		//如果为db2或sqlserver
		if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE || StaticValue.DBTYPE==StaticValue.SQLSERVER_DBTYPE){
			sb.append(field+" is not null ").append("and "+field+" !='' ");
		}else if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE){
			sb.append("trim("+field+") is not null ");
		}else{
			sb.append(field +" is not null ");
		}
		return sb.toString();
	}

	/**
	 * 用于富信
	 * @param lfSysuser
	 * @param isContainsMourl
	 * @param flag
	 * @return
	 * @throws Exception
	 */
	public List<Userdata> getAccountBinds(LfSysuser lfSysuser,boolean isContainsMourl,boolean flag)
			throws Exception {
		String sql="";
		if(isContainsMourl){
			sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
					.append("  where ").append(TableUserdata.STATUS).append("=0 ")
					.append(" and ").append(notNullSql(TableUserdata.MO_URL)).append(" and ")
					.append(TableUserdata.USER_ID).append(" in(select ").append(TableLfAccountBind.SPUSERID)
					.append(" from ").append(TableLfAccountBind.TABLE_NAME).append(" where ")
					.append(TableLfAccountBind.SYS_GUID).append(" = ").append(lfSysuser.getGuId())
					.append(" and ").append(TableLfAccountBind.BIND_TYPE).append("=1) ").append(UserDataDao.getConditionAccType(2))
					.append("  order by ").append(TableUserdata.USER_ID)
					.toString();
		}else{
			sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
					.append("  where ").append(TableUserdata.STATUS).append("=0 and ")
					.append(TableUserdata.USER_ID).append(" in(select ").append(TableLfAccountBind.SPUSERID)
					.append(" from ").append(TableLfAccountBind.TABLE_NAME).append(" where ")
					.append(TableLfAccountBind.SYS_GUID).append(" = ").append(lfSysuser.getGuId())
					.append(" and ").append(TableLfAccountBind.BIND_TYPE).append("=1) ").append(UserDataDao.getConditionAccType(2))
					.append("  order by ").append(TableUserdata.USER_ID)
					.toString();
		}

		List<Userdata> userdatas = null;
		userdatas = findEntityListBySQL(Userdata.class, sql,
				StaticValue.EMP_POOLNAME);
		if(userdatas != null && userdatas.size()>0){
			return userdatas;
		}else{
			LfDep lfDep = new LfDep();
			lfDep.setSuperiorId(lfSysuser.getDepId());
			return getAccountBindByDepCode(lfDep,isContainsMourl,flag);
		}
	}

	public List<Userdata> getAccountBinds(LfSysuser lfSysuser,boolean isContainsMourl)
			throws Exception {
		String sql="";
		if(isContainsMourl){
			 sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
				.append("  where ").append(TableUserdata.STATUS).append("=0 ")
				.append(" and ").append(notNullSql(TableUserdata.MO_URL)).append(" and ")
				.append(TableUserdata.USER_ID).append(" in(select ").append(TableLfAccountBind.SPUSERID)
				.append(" from ").append(TableLfAccountBind.TABLE_NAME).append(" where ")
				.append(TableLfAccountBind.SYS_GUID).append(" = ").append(lfSysuser.getGuId())
				.append(" and ").append(TableLfAccountBind.BIND_TYPE).append("=1) ").append(UserDataDao.getConditionAccType(0))
				.append("  order by ").append(TableUserdata.USER_ID)
				.toString();
		}else{
			 sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
			.append("  where ").append(TableUserdata.STATUS).append("=0 and ")
			.append(TableUserdata.USER_ID).append(" in(select ").append(TableLfAccountBind.SPUSERID)
			.append(" from ").append(TableLfAccountBind.TABLE_NAME).append(" where ")
			.append(TableLfAccountBind.SYS_GUID).append(" = ").append(lfSysuser.getGuId())
			.append(" and ").append(TableLfAccountBind.BIND_TYPE).append("=1) ").append(UserDataDao.getConditionAccType(0))
			.append("  order by ").append(TableUserdata.USER_ID)
			.toString();
		}
		
		List<Userdata> userdatas = null;
			userdatas = findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		if(userdatas != null && userdatas.size()>0){
			return userdatas;
		}else{
			LfDep lfDep = new LfDep();
			lfDep.setSuperiorId(lfSysuser.getDepId());
			return getAccountBindByDepCode(lfDep,isContainsMourl);
		}
	}

	/**
	 * 用于富信
	 * @param lfDep
	 * @param isContainsMourl
	 * @return
	 * @throws Exception
	 */
	private List<Userdata> getAccountBindByDepCode(LfDep lfDep,boolean isContainsMourl,boolean flag) throws Exception{

		//如果不是顶级机构
		if(lfDep.getSuperiorId() != 0L){
			//sql拼接
			String depSql = new StringBuffer("select * from ").append(
					TableLfDep.TABLE_NAME).append(" where ").append(
					TableLfDep.DEP_ID).append("=").append(lfDep.getSuperiorId()).toString();
			lfDep = findEntityListBySQL(LfDep.class, depSql,
					StaticValue.EMP_POOLNAME).get(0);
			String sql="";
			if(isContainsMourl){
				sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
						.append("  where ").append(notNullSql(TableUserdata.MO_URL)).append(" and ")
						.append(TableUserdata.USER_ID).append(" in (select ")
						.append(TableLfAccountBind.SPUSERID).append(" from ").append(TableLfAccountBind.TABLE_NAME)
						.append("  where ").append(TableLfAccountBind.DEP_CODE).append(" = '")
						.append(lfDep.getDepId()).append("' and ").append(TableLfAccountBind.BIND_TYPE)
						.append(" =0 )and ").append(TableUserdata.STATUS).append("=0 ").append(UserDataDao.getConditionAccType(2)).append(" order by ")
						.append(TableUserdata.USER_ID)
						.toString();
			}else{
				sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
						.append("  where ").append(TableUserdata.USER_ID).append(" in (select ")
						.append(TableLfAccountBind.SPUSERID).append(" from ").append(TableLfAccountBind.TABLE_NAME)
						.append("  where ").append(TableLfAccountBind.DEP_CODE).append(" = '")
						.append(lfDep.getDepId()).append("' and ").append(TableLfAccountBind.BIND_TYPE)
						.append(" =0 )and ").append(TableUserdata.STATUS).append("=0 ").append(UserDataDao.getConditionAccType(2)).append(" order by ")
						.append(TableUserdata.USER_ID)
						.toString();
			}
			List<Userdata> userdatas = null;
			userdatas = findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
			if(userdatas != null && userdatas.size()>0){
				return  userdatas;
			}else{
				return getAccountBindByDepCode(lfDep,isContainsMourl,flag);
			}
		}
		return null;
	}

	private List<Userdata> getAccountBindByDepCode(LfDep lfDep,boolean isContainsMourl) throws Exception{
		
		//如果不是顶级机构
		if(lfDep.getSuperiorId() != 0L){
			//sql拼接
			String depSql = new StringBuffer("select * from ").append(
					TableLfDep.TABLE_NAME).append(" where ").append(
					TableLfDep.DEP_ID).append("=").append(lfDep.getSuperiorId()).toString();
			lfDep = findEntityListBySQL(LfDep.class, depSql,
					StaticValue.EMP_POOLNAME).get(0);
			String sql="";
			if(isContainsMourl){
				sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
				.append("  where ").append(notNullSql(TableUserdata.MO_URL)).append(" and ")
				.append(TableUserdata.USER_ID).append(" in (select ")
				.append(TableLfAccountBind.SPUSERID).append(" from ").append(TableLfAccountBind.TABLE_NAME)
				.append("  where ").append(TableLfAccountBind.DEP_CODE).append(" = '")
				.append(lfDep.getDepId()).append("' and ").append(TableLfAccountBind.BIND_TYPE)
				.append(" =0 )and ").append(TableUserdata.STATUS).append("=0 ").append(UserDataDao.getConditionAccType(0)).append(" order by ")
				.append(TableUserdata.USER_ID)
				.toString();
			}else{
				sql = new StringBuffer("select * from ").append(TableUserdata.TABLE_NAME)
				.append("  where ").append(TableUserdata.USER_ID).append(" in (select ")
				.append(TableLfAccountBind.SPUSERID).append(" from ").append(TableLfAccountBind.TABLE_NAME)
				.append("  where ").append(TableLfAccountBind.DEP_CODE).append(" = '")
				.append(lfDep.getDepId()).append("' and ").append(TableLfAccountBind.BIND_TYPE)
				.append(" =0 )and ").append(TableUserdata.STATUS).append("=0 ").append(UserDataDao.getConditionAccType(0)).append(" order by ")
				.append(TableUserdata.USER_ID)
				.toString();
			}
			List<Userdata> userdatas = null;
				userdatas = findEntityListBySQL(Userdata.class, sql,
						StaticValue.EMP_POOLNAME);
			if(userdatas != null && userdatas.size()>0){
				return  userdatas;
			}else{
				return getAccountBindByDepCode(lfDep,isContainsMourl);
			}
 		}
		return null;
	}
	
	public List<Userdata> findSpUserBindPrefectByCorp(String corp,LinkedHashMap<String, String> conditionMap,LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
		throws Exception {
		String sql = "select * from " + TableUserdata.TABLE_NAME + "   where " + TableUserdata.USER_ID
				+ " in (select spuser from " + TableLfSpDepBind.TABLE_NAME
				+ "  where " + TableLfSpDepBind.CORP_CODE + "='" + corp+ "' and " +TableLfSpDepBind.IS_VALIDATE+"=1) and "
				+ TableUserdata.USER_ID +" in (select "+ TableGtPortUsed.USER_ID+" from "+ TableGtPortUsed.TABLE_NAME+" where "+TableGtPortUsed.ROUTE_FLAG+"=0)";
		
		Iterator<Map.Entry<String, String>> iter = null;
		Map<String, String> columns = getORMMap(Userdata.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			
			
			while (iter.hasNext())
			{
				e = iter.next();
				
				if (!"".equals(e.getValue()))
				{
					String eKey=e.getKey();
					columnName = eKey.indexOf("&")<0?columns.get(eKey):columns.get(eKey.subSequence(0, eKey.indexOf("&")));
					if(eKey.contains("&like"))
					{
						sql = sql + " and "+columnName+" like '%"+e.getValue()+"%' ";
					}else if(eKey.contains("&>"))
					{
						sql = sql + " and "+columnName+">"+e.getValue()+" ";
					}else
					{
						sql = sql + " and "+columnName+"='"+e.getValue()+"' ";
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
				sql=sql +columns.get(e.getKey())+" "+e.getValue()+",";
			}
			
			sql=sql.substring(0,sql.lastIndexOf(","));
		}
		
		if(pageInfo == null )
		{
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		}else
		{
			 String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(sql).append(")").toString();
			return genericDAO.findPageEntityListBySQL(Userdata.class, sql, countSql,
							pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	/**
	 * 获取当天生日的员工手机号码
	 * @param bsId 生日祝福设置Id
	 * @return 返回手机号码集合
	 * @throws Exception
	 */
	public Map<String,String> findEmployeePhones(Long bsId) throws Exception
	{
		
		//获取绑定包含子机构的机构的机构编码的sql
		StringBuffer sqlFindDeppath = new StringBuffer(" select ").append(TableLfEmployeeDep.DEP_PATH)
				.append(" from ").append(TableLfEmployeeDep.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
				.append(" where ").append(TableLfEmployeeDep.DEP_ID).append(" in ")
				.append(" ( ")
				.append(" select ").append(TableLfBirthdayMember.MEMBER_ID)
				.append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
				.append(" where ").append(TableLfBirthdayMember.TYPE).append(" = 1 ")
				.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 3 ")
				.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId)
				.append(" ) ");
		
		//获取绑定包含子机构的机构的机构编码，用于查子机构
		List<LfEmployeeDep> empDepsList = findPartEntityListBySQL(LfEmployeeDep.class, sqlFindDeppath.toString(), StaticValue.EMP_POOLNAME);
		
		//查询子机构的sql语句
		StringBuffer sqlFindSunDep = new StringBuffer();
		//循环构造查询子机构的sql语句
		for(int i = 0; i < empDepsList.size(); i++)
		{
			sqlFindSunDep.append(" or ").append(TableLfEmployee.DEP_ID).append(" in ")
						.append(" ( ")
						.append(" select ").append(TableLfEmployeeDep.DEP_ID)
						.append(" from ").append(TableLfEmployeeDep.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
						.append(" where ").append(TableLfEmployeeDep.DEP_PATH)
						.append(" like ").append(" '").append(empDepsList.get(i).getDeppath()).append("%' ")
						.append(" ) ");
		}
		
		//sql拼接
		StringBuffer sqlBuffer = new StringBuffer(" select ").append(TableLfEmployee.MOBILE).append(",").append(TableLfEmployee.NAME)
					.append(" from ").append(TableLfEmployee.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ")
					.append(" ( ")
					.append(TableLfEmployee.GUID).append(" in ( ")
					.append(" select ").append(TableLfBirthdayMember.MEMBER_ID).append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ")
					.append(TableLfBirthdayMember.TYPE).append(" = 1 ")
					.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 1 ")
					.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId)
					.append(" ) ")
					.append(" or ").append(TableLfEmployee.DEP_ID).append(" in ( ")
					.append(" select ").append(TableLfBirthdayMember.MEMBER_ID).append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableLfBirthdayMember.TYPE).append(" = 1 ")
					.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 2 ")
					.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId).append(" ) ");
		
		//组装到主sql语句
		sqlBuffer.append(sqlFindSunDep).append(" ) ");

		//查询当天生日例子
		/*db2:select guid,mobile,date(BIRTHDAY) as 生日日期, month(BIRTHDAY) as 生日月, day(BIRTHDAY) as 生日日, current date as 当前日期 
	 		from lf_employee where month(BIRTHDAY) = month(current date) and day(BIRTHDAY) = day(current date)*/
		
		/*oracle: select guid,mobile, to_char(BIRTHDAY,'mm-dd') as 月日, to_date(BIRTHDAY) as 生日日期, to_date(sysdate) as 当前日期
	 			from lf_employee where to_char(BIRTHDAY,'mm-dd') = to_char(sysdate,'mm-dd')*/
	 	 
		/*sql server:select guid,mobile, datepart(mm,BIRTHDAY) as 月, datepart(dd,BIRTHDAY) as 日, convert(varchar(10),BIRTHDAY,120) as 生日日期,convert(varchar(10),getdate(),120) as 当前日期
					from lf_employee where datepart(mm,BIRTHDAY) = datepart(mm,getdate()) and datepart(dd,BIRTHDAY) = datepart(dd,getdate())*/
		
		/*mysql: select guid,mobile,date_format(BIRTHDAY,'%m-%d') as 生日月日, date_format(now(),'%Y-%m-%d') as 生日日期, date_format(now(),'%m-%d') as 当前月日, date_format(now(),'%Y-%m-%d') as 当前日期 
	 			from lf_employee where date_format(BIRTHDAY,'%m-%d') = date_format(now(),'%m-%d')*/

		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
		String currentMD=sdf.format(Calendar.getInstance().getTime());
		String currentMonth=currentMD.split("-")[0].substring(0, 1).equals("0")?currentMD.split("-")[0].substring(1, 2):currentMD.split("-")[0];
		String currentDay=currentMD.split("-")[1].substring(0, 1).equals("0")?currentMD.split("-")[1].substring(1, 2):currentMD.split("-")[1];
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)//oracle
		{
			sqlBuffer.append(" and to_char(").append(TableLfEmployee.BIRTHDAY).append(",'mm-dd') ='").append(currentMD).append("' ");
		}
		else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)//sql server
		{
			sqlBuffer.append(" and datepart(mm,").append(TableLfEmployee.BIRTHDAY).append(") = ").append(currentMonth).append(" ")
					.append(" and datepart(dd,").append(TableLfEmployee.BIRTHDAY).append(") = ").append(currentDay).append(" ");
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)//db2
		{
			sqlBuffer.append(" and month(").append(TableLfEmployee.BIRTHDAY).append(") = ").append(currentMonth).append(" ")
					.append(" and day(").append(TableLfEmployee.BIRTHDAY).append(") = ").append(currentDay).append(" ");
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)//mysql
		{
			sqlBuffer.append(" and date_format(").append(TableLfEmployee.BIRTHDAY).append(",'%m-%d') = date_format(now(),'%m-%d') ");
		}
		
		List<LfEmployee> employeesList = findPartEntityListBySQL(LfEmployee.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
		
		if(employeesList == null || employeesList.size() == 0 )
		{
			return null;
		}
		
		Map<String,String> employeesMap = new HashMap<String,String>();
		for(int i = 0; i<employeesList.size(); i++)
		{
			if(employeesList.get(i).getMobile() == null || "".equals(employeesList.get(i).getMobile()))
			{
				continue;
			}
			employeesMap.put(employeesList.get(i).getMobile().trim(), employeesList.get(i).getName());
		}
		//返回结果
		return employeesMap;

	}
	
	/**
	 * 获取当天生日的客户手机号码
	 * @param bsId 生日祝福设置Id
	 * @return 返回手机号码集合
	 * @throws Exception
	 */
	public Map<String,String> findClientPhones1(Long bsId) throws Exception
	{
		
/*		  select  dep_path from lf_client_dep where dep_id in 
		  ( 
		  select MEMBER_ID from LF_BIRTHDAYMEMBER with(nolock)  where TYPE = 2  and MEMBER_TYPE = 3  and BS_ID = 38 
		  )*/
		//获取绑定包含子机构的机构的机构编码的sql
		StringBuffer sqlFindDeppath = new StringBuffer(" select ").append(TableLfClientDep.DEP_PATH)
				.append(" from ").append(TableLfClientDep.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
				.append(" where ").append(TableLfClientDep.DEP_ID).append(" in ")
				.append(" ( ")
				.append(" select ").append(TableLfBirthdayMember.MEMBER_ID)
				.append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
				.append(" where ").append(TableLfBirthdayMember.TYPE).append(" = 2 ")
				.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 3 ")
				.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId)
				.append(" ) ");
		
		//获取绑定包含子机构的机构的机构编码，用于查子机构
		List<LfClientDep> clientDepsList = findPartEntityListBySQL(LfClientDep.class, sqlFindDeppath.toString(), StaticValue.EMP_POOLNAME);
		
		//查询子机构的sql语句
		StringBuffer sqlFindSunDep = new StringBuffer();
		//循环构造查询子机构的sql语句
		for(int i = 0; i < clientDepsList.size(); i++)
		{
			sqlFindSunDep.append(" or ").append(TableLfClient.DEP_ID).append(" in ")
						.append(" ( ")
						.append(" select ").append(TableLfClientDep.DEP_ID)
						.append(" from ").append(TableLfClientDep.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
						.append(" where ").append(TableLfClientDep.DEP_PATH)
						.append(" like ").append(" '").append(clientDepsList.get(i).getDeppath()).append("%' ")
						.append(" ) ");
		}
		
		StringBuffer sqlBuffer = new StringBuffer(" select ").append(TableLfClient.MOBILE).append(",").append(TableLfClient.NAME)
					.append(" from ").append(TableLfClient.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ")
					.append(" ( ")
					.append(TableLfClient.GUID).append(" in ( ")
					.append(" select ").append(TableLfBirthdayMember.MEMBER_ID).append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ")
					.append(TableLfBirthdayMember.TYPE).append(" = 2 ")
					.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 1 ")
					.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId).append(" ) ")
					.append(" or ").append(TableLfClient.DEP_ID).append(" in ( ")
					.append(" select ").append(TableLfBirthdayMember.MEMBER_ID).append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ")
					.append(TableLfBirthdayMember.TYPE).append(" = 2 ")
					.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 2 ")
					.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId).append(" ) ");
		
		//组装到主sql语句
		sqlBuffer.append(sqlFindSunDep).append(" ) ");
		
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)//oracle
		{
			sqlBuffer.append(" and to_char(").append(TableLfClient.BIRTHDAY).append(",'mm-dd') = to_char(sysdate,'mm-dd') ");
		}
		else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)//sql server
		{
			sqlBuffer.append(" and datepart(mm,").append(TableLfClient.BIRTHDAY).append(") = datepart(mm,getdate()) ")
					.append("and datepart(dd,").append(TableLfClient.BIRTHDAY).append(") = datepart(dd,getdate())");
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)//db2
		{
			sqlBuffer.append(" and month(").append(TableLfClient.BIRTHDAY).append(") = month(current date) ")
					.append(" and day(").append(TableLfClient.BIRTHDAY).append(") = day(current date) ");
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)//mysql
		{
			sqlBuffer.append(" and date_format(").append(TableLfClient.BIRTHDAY).append(",'%m-%d') = date_format(now(),'%m-%d') ");
		}
		
		List<LfClient> clientsList = findPartEntityListBySQL(LfClient.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
		
		if(clientsList == null || clientsList.size() == 0 )
		{
			return null;
		}
		
		Map<String,String> clientsMap = new HashMap<String,String>();
		for(int i = 0; i<clientsList.size(); i++)
		{
			if(clientsList.get(i).getMobile() == null || "".equals(clientsList.get(i).getMobile()))
			{
				continue;
			}
			clientsMap.put(clientsList.get(i).getMobile().trim(), clientsList.get(i).getName());
		}
		//返回结果
		return clientsMap;

	}
	
	/**
	 * 查询生日祝福设置成员表记录
	 * @param member 查询条件
	 * @return 返回成员记录
	 * @throws Exception
	 */
	public List<LfBirthdayMember> findDBMember(LfBirthdayMember member) throws Exception
	{
		//表名
		String tableName = null;
		if(member != null && member.getType() == 1)
		{
			tableName = TableLfEmployee.TABLE_NAME;
		}
		else
		{
			tableName = TableLfClient.TABLE_NAME;
		}
		
		//sql拼接
		StringBuffer sqlBuffer = new StringBuffer(" select ")
				.append(TableLfBirthdayMember.ID)
				.append(",member.").append(TableLfBirthdayMember.BS_ID).append(",")
				.append(TableLfBirthdayMember.TYPE).append(",")
				.append(TableLfBirthdayMember.MEMBER_ID).append(",")
				.append(" employee.").append(TableLfEmployee.NAME)
				.append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(" member ").append(StaticValue.getWITHNOLOCK())
				.append(" left join ").append(tableName).append(" employee ").append(StaticValue.getWITHNOLOCK())
				.append(" on member.").append(TableLfBirthdayMember.MEMBER_ID).append("=employee.").append(TableLfEmployee.GUID);
		//SQL条件
		StringBuffer conditionSql = new StringBuffer();
		if(member != null && member.getType() != null)
		{
			conditionSql.append(" and ").append(TableLfBirthdayMember.TYPE).append("=").append(member.getType());
		}
		if(member != null && member.getUserId() != null)
		{
			conditionSql.append(" and member.").append(TableLfBirthdayMember.USER_ID).append("=").append(member.getUserId());
		}
		if(member != null && member.getCorpCode() != null)
		{
			conditionSql.append(" and ").append(TableLfBirthdayMember.CORPCODE).append("='").append(member.getCorpCode()).append("' ");
		}
		//拼接处理后的条件
		sqlBuffer.append(getConditionSql(conditionSql.toString()));
		
		List<String[]> tempList = findListByConditionByColumName(sqlBuffer.toString(), 5);
		
		List<LfBirthdayMember> bmList = new ArrayList<LfBirthdayMember>();
		LfBirthdayMember memberT = null;
		for(int i =0; i < tempList.size(); i++)
		{
			memberT = new LfBirthdayMember();
			if(member != null){
				memberT.setUserId(member.getUserId());//userid
				memberT.setCorpCode(member.getCorpCode());//corpcode
			}

			memberT.setId(Long.valueOf(tempList.get(i)[0]));//id
			memberT.setBsId(Long.valueOf(tempList.get(i)[1]));//bsid
			memberT.setType(Integer.valueOf(tempList.get(i)[2]));//type
			memberT.setMemberId(Long.valueOf(tempList.get(i)[3]));//memberid
			memberT.setAddName(tempList.get(i)[4]);//addname
			
			bmList.add(memberT);
		}
		tempList.clear();
		tempList = null;

		//返回结果
		return bmList;
		
	}
	
	/**
	 * 获取选择人数
	 * @param bsId 生日祝福标识id
	 * @param type 类型，1员工生日祝福；2客户生日祝福
	 * @param corpCode
	 * @return 异常返回null
	 * @throws Exception
	 */
	public Integer findDBMemberCount(Long bsId, Integer type, String corpCode) throws Exception
	{
		if(type == null)
		{
			EmpExecutionContext.error("获取生日祝福选择人数失败，类型为空");
			return null;
		}
		String tableName = null;
		if(type == 1)
		{
			tableName = TableLfEmployee.TABLE_NAME;
		}
		else if(type == 2 )
		{
			tableName = TableLfClient.TABLE_NAME;
		}
		else
		{
			EmpExecutionContext.error("获取生日祝福选择人数失败，未定义类型");
			return null;
		}
		
		//sql拼接
		StringBuffer sqlBuffer = new StringBuffer()
					.append(" select count(").append(" guid ").append(") as totalcount from ")
					.append(tableName).append(StaticValue.getWITHNOLOCK())
					.append(" where ")
					.append(TableLfBirthdayMember.CORPCODE).append(" = '").append(corpCode).append("' ")
					.append(" and ")
					.append(" ( ")
					.append(TableLfEmployee.GUID).append(" in ( ")
					.append(" select ").append(TableLfBirthdayMember.MEMBER_ID).append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableLfBirthdayMember.TYPE).append(" = ").append(type)
					.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 1 ")
					.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId)
					.append(" ) ")
					.append(" or ").append(TableLfEmployee.DEP_ID).append(" in ( ")
					.append(" select ").append(TableLfBirthdayMember.MEMBER_ID).append(" from ").append(TableLfBirthdayMember.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableLfBirthdayMember.TYPE).append(" = ").append(type)
					.append(" and ").append(TableLfBirthdayMember.MEMBER_TYPE).append(" = 2 ")
					.append(" and ").append(TableLfBirthdayMember.BS_ID).append(" = ").append(bsId)
					.append(" ) ")
					.append(" ) ")
					;
		//调用公共查询方法查询并返回结果
		return findCountBySQL(sqlBuffer.toString());
	}
	
	/**
	 * 客户信息查询
	 * @param depId
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfClient> findClientDB(String depId, String corpCode) throws Exception
	{
		//sql
		StringBuffer sqlBuffer = new StringBuffer()
						.append(" select ").append(TableLfClient.GUID).append(",").append(TableLfClient.NAME).append(" from ").append(TableLfClient.TABLE_NAME).append(StaticValue.getWITHNOLOCK())
						.append(" where ").append(TableLfClient.DEP_ID).append(" = ").append(depId)
						.append(" and ").append(TableLfClient.CORP_CODE).append(" = '").append(corpCode).append("'");
		//返回结果
		return findPartEntityListBySQL(LfClient.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
	}

	/**
	 * 获取机构的名称以及成员数
	 * @param type 类型
	 * @param depId 机构id
	 * @param corpCode 企业编码
	 * @param memberType 2 表示单个机构  ,3表示包含子机构
	 * @return 数组，索引0为机构名称，索引1为成员数
	 * @throws Exception
	 */
	public String[] getNameCountByDepId(Integer type, Long depId, String corpCode, int memberType) throws Exception
	{
		//返回结果
		String[] resultArray = new String[2];
		
		//机构表名
		String depTableName = null;
		if (type == 1) 
		{
			depTableName = TableLfEmployeeDep.TABLE_NAME;
		}
		//客户机构
		else 
		{
			depTableName = TableLfClientDep.TABLE_NAME;
			
		}
		
		//获取机构名称sql
		StringBuffer sqlBuffer = new StringBuffer("select ").append(TableLfClientDep.DEP_NAME).append(" from ").append(depTableName)
						.append(" where ").append(TableLfClientDep.DEP_ID).append(" = ").append(depId);
		//获取机构名称
		List<LfClientDep> depsList = findPartEntityListBySQL(LfClientDep.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
		
		if(depsList == null || depsList.size() == 0)
		{
			return new String[]{"",""};
		}
		
		resultArray[0] = depsList.get(0).getDepName();
		
		//获取机构成员数量
		int count = this.getCountByDepId(type, depId, corpCode, memberType);
		resultArray[1] = String.valueOf(count);
		
		return resultArray;
	}
	
	/**
	 * 机构总数查询
	 * @param type 类型
	 * @param depId 机构id
	 * @param corpCode 企业编码
	 * @param memberType 2 表示单个机构  ,3表示包含子机构
	 * @return
	 */
	public int getCountByDepId(Integer type, Long depId, String corpCode, int memberType) throws Exception
	{
		StringBuffer sqlBuffer = new StringBuffer("select count(*) as totalcount from ");
		//人员表名
		String tableName = null;
		//机构表名
		String depTableName = null;
		//员工机构
		if (type == 1) 
		{
			tableName = TableLfEmployee.TABLE_NAME;
			depTableName = TableLfEmployeeDep.TABLE_NAME;
		}
		//客户机构
		else 
		{
			tableName = TableLfClient.TABLE_NAME;
			depTableName = TableLfClientDep.TABLE_NAME;
			
		}
		sqlBuffer.append(tableName).append(StaticValue.getWITHNOLOCK());
		
		StringBuffer whereSql = new StringBuffer();
		//查询条件拼接
		if(memberType == 2)
		{
			//单机构
			whereSql.append(" where ").append(TableLfClientDep.DEP_ID).append(" = ").append(depId)
									.append(" and ").append(TableLfClientDep.CORP_CODE).append("='").append(corpCode).append("'");
		}
		else if(memberType == 3)
		{
			String tempSql = new StringBuffer("select ").append(TableLfClientDep.DEP_PATH).append(" from ").append(depTableName).append(StaticValue.getWITHNOLOCK())
						.append(" where ").append(TableLfClientDep.DEP_ID).append(" = ").append(depId)
						.append(" and ").append(TableLfClientDep.CORP_CODE).append("='").append(corpCode).append("'").toString();
			List<LfClientDep> dep = findPartEntityListBySQL(LfClientDep.class, tempSql, StaticValue.EMP_POOLNAME);
			//获取对应机构的path，用于模糊查询子机构
			String deppath = (dep != null && dep.size() >0)?dep.get(0).getDeppath():"";
			
			whereSql.append(" where ").append(TableLfClientDep.DEP_ID).append(" in (")
					.append(" select ").append(TableLfClientDep.DEP_ID).append(" from ").append(depTableName).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableLfClientDep.DEP_PATH).append(" like ").append("'").append(deppath).append("%'").append(") ")
									.append(" and ").append(TableLfClientDep.CORP_CODE).append("='").append(corpCode).append("'");
		}
		
		//返回结果
		return findCountBySQL(sqlBuffer.append(whereSql).toString());
	}
	public List<String[]> findListByConditionByColumName(String sql, int columCount) throws Exception
	{
		List<String[]> returnList = new ArrayList<String[]>();
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try
		{
			//获取数据库连接
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			
			ps = conn.prepareStatement(sql);
			//执行查询语句
			rs = ps.executeQuery();
			
			String[] strArray = null;
			
			while(rs.next())
			{
				strArray = new String[columCount];
				for(int i=0; i < columCount;i++)
				{
					strArray[i]=rs.getString(i+1);
				}
				returnList.add(strArray);
			}
		} catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "执行SQL异常，sql:"+sql);
			throw ex;
		} finally
		{
			//关闭数据库资源
			close(rs, ps, conn);
		}
		return returnList;
	}
	
	/**
	 * 验证taskid是否未被使用
	 * @param taskId
	 * @return 返回true，未被使用
	 */
	public boolean checkTaskIdNotUse(Long taskId)
	{
		try
		{
			String sql = "select taskid from lf_mttask where taskid ="+taskId;
			List<DynaBean> taskIdList = getListDynaBeanBySql(sql);
			if(taskIdList != null && taskIdList.size() == 0)
			{
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			EmpExecutionContext.error(e, "检查taskid在lf_mttask任务表是否使用异常。taskId:"+taskId);
			return false;
		}
	}
	
	/**
	 * 获取当天生日的客户手机号码
	 * @param bsId 生日祝福设置Id
	 * @return 返回手机号码集合
	 * @throws Exception
	 */
	public Map<String,String> findClientPhones(Long bsId) throws Exception
	{
		//获取绑定包含子机构的机构的机构编码的sql
		StringBuffer depBuffer = new StringBuffer();
		depBuffer.append("select mem.MEMBER_ID,mem.member_type ,dep.DEP_PATH from LF_BIRTHDAYMEMBER mem ");
		depBuffer.append(" inner join LF_CLIENT_DEP dep on mem.MEMBER_ID = dep.DEP_ID ");
		depBuffer.append(" where mem.TYPE = 2 and mem.member_type in (2,3) and mem.BS_ID = ").append(bsId);
		List<DynaBean> beanList = getListDynaBeanBySql(depBuffer.toString());
		String nocontainstr = "";
		String containstr = "";
		List<String> deppathList = new ArrayList<String>();
		if(beanList != null && beanList.size()>0){
			for(DynaBean bean:beanList){
				String type = String.valueOf(bean.get("member_type"));
				String id = String.valueOf(bean.get("member_id"));
				if(type != null && "2".equals(type)){
					nocontainstr = nocontainstr + id + ",";
				}else if(type != null && "3".equals(type)){
					String deppath = String.valueOf(bean.get("dep_path"));
					containstr = containstr + id + ",";
					deppathList.add(deppath);
				}
			}
		}
		Map<String,String> clientsMap = new HashMap<String,String>();

		if(nocontainstr != null && !"".equals(nocontainstr)){
			nocontainstr = nocontainstr.substring(0, nocontainstr.length()-1);
			depBuffer = new StringBuffer();
			depBuffer.append("select client.MOBILE,client.NAME from LF_CLIENT client ");
			depBuffer.append(" inner join LF_ClIENT_DEP_SP sp on client.CLIENT_ID = sp.CLIENT_ID ");
			depBuffer.append(" where sp.DEP_ID in (").append(nocontainstr).append(") ");
			depBuffer.append(this.getBirthdayMsg());
			List<DynaBean> nocontainstrList = getListDynaBeanBySql(depBuffer.toString());
			if(nocontainstrList != null && nocontainstrList.size()>0){
				for(DynaBean bean:nocontainstrList){
					String name = String.valueOf(bean.get("name"));
					String mobile = String.valueOf(bean.get("mobile"));
					if("".equals(name) || "".equals(mobile)){
						continue;
					}
					clientsMap.put(mobile, name);
				}
			}
		}
		if(deppathList != null && deppathList.size()>0){
			depBuffer = new StringBuffer();
			depBuffer.append("select client.MOBILE,client.NAME from LF_CLIENT client ");
			depBuffer.append(" inner join LF_ClIENT_DEP_SP sp on client.CLIENT_ID = sp.CLIENT_ID ");
			//SQL条件
			StringBuffer conditionSql = new StringBuffer("");
			conditionSql.append(this.getBirthdayMsg());
			conditionSql.append(" and (");
			for(int a=0;a<deppathList.size();a++){
				String temp = deppathList.get(a);
				if(temp != null && !"".equals(temp)){
					conditionSql.append(" sp.DEP_ID in (").append("select dep.DEP_ID from LF_CLIENT_DEP dep ");
					conditionSql.append(" where dep.DEP_PATH like '").append(temp).append("%')");
				}
				if(a != deppathList.size()-1){
					conditionSql.append(" or ");
				}
			}
			conditionSql.append(")");
			//拼接处理后的条件
			depBuffer.append(getConditionSql(conditionSql.toString()));
			
			List<DynaBean> containstrList = getListDynaBeanBySql(depBuffer.toString());
			if(containstrList != null && containstrList.size()>0){
				for(DynaBean bean:containstrList){
					String name = String.valueOf(bean.get("name"));
					String mobile = String.valueOf(bean.get("mobile"));
					if("".equals(name) || "".equals(mobile)){
						continue;
					}
					clientsMap.put(mobile, name);
				}
			}
		}
		
		//查找单个人的生日祝福
		StringBuffer singlePersonStr=new StringBuffer("select client.MOBILE,client.NAME from LF_CLIENT client " +
				"where client.GUID in (select MEMBER_ID from LF_BIRTHDAYMEMBER where TYPE=2 and MEMBER_TYPE=1 and BS_ID=")
		.append(bsId).append(")").append(getBirthdayMsg());
		List<DynaBean> singlePersonList = getListDynaBeanBySql(singlePersonStr.toString());
		if(singlePersonList != null && singlePersonList.size()>0){
			String name="";
			String mobile="";
			for(DynaBean bean:singlePersonList){
				name = String.valueOf(bean.get("name"));
				mobile = String.valueOf(bean.get("mobile"));
				if("".equals(name) || "".equals(mobile)){
					continue;
				}
				clientsMap.put(mobile, name);
			}
		}
		
		if(clientsMap == null || clientsMap.size() == 0 )
		{
			return null;
		}
		return clientsMap;

	}
	
	/**
	 *   获取时间条件
	 * @return
	 */
	public String getBirthdayMsg(){
		SimpleDateFormat sdf=new SimpleDateFormat("MM-dd");
		String currentMD=sdf.format(Calendar.getInstance().getTime());
		String currentMonth=currentMD.split("-")[0].substring(0, 1).equals("0")?currentMD.split("-")[0].substring(1, 2):currentMD.split("-")[0];
		String currentDay=currentMD.split("-")[1].substring(0, 1).equals("0")?currentMD.split("-")[1].substring(1, 2):currentMD.split("-")[1];
		
		StringBuffer sqlBuffer = new StringBuffer();
		if(StaticValue.DBTYPE == StaticValue.ORACLE_DBTYPE)//oracle
		{
			sqlBuffer.append(" and to_char(client.BIRTHDAY,'mm-dd') ='").append(currentMD).append("' ");
		}
		else if(StaticValue.DBTYPE == StaticValue.SQLSERVER_DBTYPE)//sql server
		{
			sqlBuffer.append(" and datepart(mm,client.BIRTHDAY) =").append(currentMonth).append(" ")
					.append(" and datepart(dd,client.BIRTHDAY) =").append(currentDay).append(" ");
		}
		else if(StaticValue.DBTYPE == StaticValue.DB2_DBTYPE)//db2
		{
			sqlBuffer.append(" and month(client.BIRTHDAY) = ").append(currentMonth).append(" ")
					.append(" and day(client.BIRTHDAY) = ").append(currentDay).append(" ");
		}
		else if(StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE)//mysql
		{
			sqlBuffer.append(" and date_format(client.BIRTHDAY,'%m-%d') = date_format(now(),'%m-%d') ");
		}
		return sqlBuffer.toString();
	}
	
	/**
	 * 删除告警短信发送记录半年前的记录
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-4-14 上午10:28:07
	 */
	public void delAlarmSmsSendRecode()
	{
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			//删除告警短信发送记录半年前的记录
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String Timestamp = new DataAccessDriver().getGenericDAO().getTimeCondition(format.format(System.currentTimeMillis() - (182 *24 * 60 * 60 * 1000L)));
			//拼接SQL
			String sql = new StringBuffer("DELETE FROM LF_ALSMSRECORD WHERE")
			.append(" SENDTIME <= ").append(Timestamp).toString();
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + sql);
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		} 
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时删除告警短信记录失败!");
		} finally
		{
			try
			{
				close(null, ps, conn);
			} 
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "定时删除告警短信关闭数据库资源出错！");
			}
		}
	}
	
	/**
	 * 获取通道配置信息
	 * @param sqUserid
	 * @return
	 */
	public List<DynaBean> getSpGateInfo(String sqUserid){
		try {
			String sql = "SELECT gt.spgate,gt.spisuncm,gt.signstr,gt.signlen,gt.maxwords,gt.multilen1,gt.multilen2,gt.ensignstr,gt.ensignlen," +
					"gt.enmaxwords,gt.enmultilen1,gt.enmultilen2,gt.ensinglelen,xt.gateprivilege FROM GT_PORT_USED gt " +
					"LEFT JOIN XT_GATE_QUEUE xt ON gt.SPGATE = xt.SPGATE AND gt.SPISUNCM = xt.SPISUNCM AND gt.GATETYPE = xt.GATETYPE " +
					"WHERE gt.USERID = '"+sqUserid+"' AND gt.ROUTEFLAG <> 2 ORDER BY gt.SPISUNCM ASC";
			return getListDynaBeanBySql(sql);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取通道配置信息失败！sqUserid:" + sqUserid);
			return null;
		}
	}
	

	/**
	 * 通过任务ID查询网关群发任务表及历史表
	 * @param taskId
	 * @return true:存在；false:不存在
	 */
	public boolean isExistTask(String taskId)
	{
		boolean isExist = false;
		try {
			String sql = "SELECT H.ID FROM BATCH_MT_REQ_HIS H WHERE H.Taskid = "+taskId+" UNION SELECT B.ID FROM BATCH_MT_REQ B WHERE B.TASKID = "+taskId;
			List<DynaBean> taskList = getListDynaBeanBySql(sql);
			if(taskList != null && taskList.size() > 0)
			{
				isExist = true;
			}
			return isExist;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "检查taskid在网关群发任务表及历史表是否被使用异常。taskId:"+taskId);
			return false;
		}
	}
	
	/**
	 * 处理SQL条件,不允许使用1 =1方式
	 * @param conSql 条件
	 * @return 处理后的条件
	 */
	public String getConditionSql(String conSql)
	{
		String conditionSql = "";
		try {
			//存在查询条件
			if(conSql != null && conSql.length() > 0)
			{
				//将条件字符串首个and替换为where,不允许1 =1方式
				conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
			return conditionSql;
		} catch (Exception e) {
			EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
			return null;
		}
	}

    /**
     * 获取用户对应发送功能保存的草稿箱
     * @param condMap
     * @param pageInfo
     * @return
     */
    public List<DynaBean> getUserDrafts(LinkedHashMap<String,String> condMap,PageInfo pageInfo){
        List<DynaBean> drafts = null;
        IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
        String sql = "SELECT * FROM LF_DRAFTS DRAFT ";
        String countSql = "SELECT COUNT(*) TOTALCOUNT FROM LF_DRAFTS DRAFT ";
        StringBuffer sqlCon = new StringBuffer(" WHERE ");
        String orderSql = " ORDER BY DRAFT.UPDATE_TIME DESC";
        //企业编码
        sqlCon.append(" DRAFT.CORP_CODE = '"+condMap.get("corpcode")+"'");
        //用户ID
        sqlCon.append(" AND DRAFT.USER_ID = "+condMap.get("userid"));
        //草稿箱类型
        sqlCon.append(" AND DRAFT.DRAFTS_TYPE = "+condMap.get("draftstype"));
        //主题
        String title = condMap.get("title");
        if(StringUtils.isNotBlank(title)){
            sqlCon.append(" AND DRAFT.TITLE LIKE '%"+title.trim()+"%'");
        }
        //短信内容
        String msg = condMap.get("msg");
        if(StringUtils.isNotBlank(msg)){
            sqlCon.append(" AND DRAFT.MSG LIKE '%"+msg.trim()+"%'");
        }
        //开始时间
        String starttime = condMap.get("starttime");
        if(StringUtils.isNotBlank(starttime)){
            sqlCon.append(" AND DRAFT.UPDATE_TIME >="+ genericDao.getTimeCondition(starttime));
        }
        //结束时间
        String endtime = condMap.get("endtime");
        if(StringUtils.isNotBlank(endtime)){
            sqlCon.append(" AND DRAFT.UPDATE_TIME <="+genericDao.getTimeCondition(endtime));
        }

        sql = sql + sqlCon.toString() + orderSql;
        if(pageInfo == null){
            drafts = getListDynaBeanBySql(sql);
        }else{
            countSql = countSql + sqlCon;
            drafts = genericDao.findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
        }
        return drafts;
    }

    public List<DynaBean> getUserDraftsFromShortUrl(LinkedHashMap<String,String> condMap, PageInfo pageInfo) {
        List<DynaBean> drafts = null;
        IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
        String sql = "SELECT DRAFT.*,SUBDRAFT.NETURLID,SUBDRAFT.DOMAINID,SUBDRAFT.TYPE FROM LF_DRAFTS DRAFT LEFT JOIN LF_SUB_DRAFTS SUBDRAFT ON DRAFT.ID=SUBDRAFT.DRAFTID ";
        String countSql = "SELECT COUNT(*) TOTALCOUNT FROM LF_DRAFTS DRAFT LEFT JOIN LF_SUB_DRAFTS SUBDRAFT ON DRAFT.ID=SUBDRAFT.DRAFTID ";
        StringBuffer sqlCon = new StringBuffer(" WHERE ");
        String orderSql = " ORDER BY DRAFT.UPDATE_TIME DESC";
        //企业编码
        sqlCon.append(" DRAFT.CORP_CODE = '"+condMap.get("corpcode")+"'");
        //用户ID
        sqlCon.append(" AND DRAFT.USER_ID = "+condMap.get("userid"));
        //草稿箱类型
        sqlCon.append(" AND DRAFT.DRAFTS_TYPE = "+condMap.get("draftstype"));
        //主题
        String title = condMap.get("title");
        if(StringUtils.isNotBlank(title)){
            sqlCon.append(" AND DRAFT.TITLE LIKE '%"+title.trim()+"%'");
        }
        //短信内容
        String msg = condMap.get("msg");
        if(StringUtils.isNotBlank(msg)){
            sqlCon.append(" AND DRAFT.MSG LIKE '%"+msg.trim()+"%'");
        }
        //开始时间
        String starttime = condMap.get("starttime");
        if(StringUtils.isNotBlank(starttime)){
            sqlCon.append(" AND DRAFT.UPDATE_TIME >="+ genericDao.getTimeCondition(starttime));
        }
        //结束时间
        String endtime = condMap.get("endtime");
        if(StringUtils.isNotBlank(endtime)){
            sqlCon.append(" AND DRAFT.UPDATE_TIME <="+genericDao.getTimeCondition(endtime));
        }

        sql = sql + sqlCon.toString() + orderSql;
        if(pageInfo == null){
            drafts = getListDynaBeanBySql(sql);
        }else{
            countSql = countSql + sqlCon;
            drafts = genericDao.findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
        }
        return drafts;
    }


    public long saveSubDrafts(LfSubDrafts subDrafts) throws Exception {
        long count = 0;
        if(subDrafts != null) {
            count = smsDao.saveObjectReturnID(subDrafts);
        }
        return count;
    }


    public boolean updateSubDrafts(LfSubDrafts subDrafts) throws Exception {
        boolean result = false;
        if(subDrafts != null) {
            result = smsDao.update(subDrafts);
        }
        return result;
    }

}
