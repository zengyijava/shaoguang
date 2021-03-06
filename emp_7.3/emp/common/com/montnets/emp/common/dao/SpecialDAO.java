package com.montnets.emp.common.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.constant.ViewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.timer.IErrorCode;
import com.montnets.emp.common.timer.TimerErrorCode;
import com.montnets.emp.common.vo.LfPrivilegeAndMenuVo;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.client.LfClient;
import com.montnets.emp.entity.client.LfClientDep;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sms.LfMttask;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfRoles;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.tailnumber.LfSubnoAllot;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.client.TableLfClientDep;
import com.montnets.emp.table.corp.TableLfCorp;
import com.montnets.emp.table.employee.TableLfEmployee;
import com.montnets.emp.table.employee.TableLfEmployeeDep;
import com.montnets.emp.table.gateway.TableAgwSpBind;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.table.system.TableLfThiMenuControl;
import com.montnets.emp.table.system.TableLfTimer;
import com.montnets.emp.table.system.TableLfTimerHistory;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfImpower;
import com.montnets.emp.table.sysuser.TableLfPrivilege;
import com.montnets.emp.table.sysuser.TableLfRoles;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.sysuser.TableLfUser2role;
import com.montnets.emp.table.tailnumber.TableLfSubnoAllot;
import com.montnets.emp.util.EmpUtils;
import com.montnets.emp.util.PageInfo;

public class SpecialDAO extends SuperDAO
{

	/**
	 * ?????????????????????
	 * @return
	 * blackListBiz??????
	 * @throws Exception
	 */
	public List<GtPortUsed> findMtSpgate() throws Exception {
		//??????sql
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.SPGATE).append(",").append(
				TableGtPortUsed.SPISUNCM).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1)").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//????????????
		return returnList;
	}
	
	/**
	 * ?????????????????????
	 * @return
	 * wgMsgConfigBiz??????
	 * @throws Exception
	 */
	public List<GtPortUsed> findMtSpgate(String spgate) throws Exception {
		//??????sql
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(",").append(
				TableGtPortUsed.CPNO).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1)").append(
				" and ").append(TableGtPortUsed.SPGATE).append("='").append(
				spgate).append("'").append(" order by ").append(
				TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//????????????
		return returnList;
	}
	
	public List<GtPortUsed> findGtPortUsedForBlackList(String spUser)
	throws Exception {
		// select * from gt_port_used where spgate in (select * from
		// gt_port_used where loginid = userid) and userid=''
		//??????sql
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
		//????????????
		List<GtPortUsed> gtportuseds = findPartEntityListBySQL(
				GtPortUsed.class, sql, StaticValue.EMP_POOLNAME);
		//????????????
		return gtportuseds;
	}
	
	/**
	 * ????????????????????????????????????????????????????????????
	 * @param conditionMap ??????????????????
	 * @param orderbyMap  ??????????????????
	 * @param pageInfo	????????????
	 * @return ????????????????????????????????????List<LfSpDepBind>
	 * @throws Exception
	 */
	public List<LfSpDepBind> getSpDepBindWhichUserdataIsOk(LinkedHashMap<String, String> conditionMap,
                                                           LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		//??????sql
		String sql = "select * from " + TableLfSpDepBind.TABLE_NAME + "  where "
				+ TableLfSpDepBind.SP_USER + " in (select "+TableUserdata.USER_ID+" from " + TableUserdata.TABLE_NAME+StaticValue.getWITHNOLOCK()
				+ "  where " +TableUserdata.ACCOUNTTYPE +" =1 and "+ TableUserdata.STATUS + "=0)";

		Iterator<Map.Entry<String, String>> iter = null;
		//????????????????????????????????????MAP??????
		Map<String, String> columns = getORMMap(LfSpDepBind.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			//?????????????????????
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			//????????????
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
						//?????????????????????
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
		//??????????????????
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
			//???????????????????????????????????????
			return findEntityListBySQL(LfSpDepBind.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			//?????????sql??????
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			//????????????????????????????????????
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(LfSpDepBind.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	public boolean confirmUpdateBeforeRun(LfTimer lfTimer, String timerHistoryID , TimerErrorCode errorCode) {
		boolean isSuccess = false;
		//??????????????????
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
			//?????????????????????
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			EmpExecutionContext.sql("execute sql : " + lfTimerSql);
			ps = conn.prepareStatement(lfTimerSql);
			if (lfTimer.getPreTime() != null) {
				ps.setTimestamp(1, lfTimer.getPreTime());
			}
			if (lfTimer.getNextTime() != null) {
				ps.setTimestamp(2, lfTimer.getNextTime());
			}
			//????????????
			rs = ps.executeQuery();
			if (rs.next()) {
				lfTimerFlag = true;
			}
			EmpExecutionContext.sql("execute sql : " + lfTimerHistorySql);
			ps = conn.prepareStatement(lfTimerHistorySql);
			//????????????
			rs = ps.executeQuery();
			if (rs.next()) {
				lfTimerHistoryFlag = true;
			}
			if (lfTimerFlag && lfTimerHistoryFlag) {
				isSuccess = true;
			}
		} catch (Exception e) {
			errorCode.setErrorCode(IErrorCode.E_DS_D500);
			EmpExecutionContext
					.error("EmpSpecialDAO???confirmUpdateBeforeRun???????????????");
		} finally {
			try {
				//?????????????????????
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error("??????????????????????????????");
			}
		}
		return isSuccess;
	}
	public String[] getNameCountByDepId(Integer type, Long depId, String corpCode, int memberType) throws Exception
	{
		//????????????
		String[] resultArray = new String[2];
		
		//????????????
		String depTableName = null;
		if (type == 1) 
		{
			depTableName = TableLfEmployeeDep.TABLE_NAME;
		}
		//????????????
		else 
		{
			depTableName = TableLfClientDep.TABLE_NAME;
			
		}
		
		//??????????????????sql
		StringBuffer sqlBuffer = new StringBuffer("select ").append(TableLfClientDep.DEP_NAME).append(" from ").append(depTableName)
						.append(" where ").append(TableLfClientDep.DEP_ID).append(" = ").append(depId);
		//??????????????????
		List<LfClientDep> depsList = findPartEntityListBySQL(LfClientDep.class, sqlBuffer.toString(), StaticValue.EMP_POOLNAME);
		
		if(depsList == null || depsList.size() == 0)
		{
			return new String[]{"",""};
		}
		
		resultArray[0] = depsList.get(0).getDepName();
		
		//????????????????????????
		int count = this.getCountByDepId(type, depId, corpCode, memberType);
		resultArray[1] = String.valueOf(count);
		
		return resultArray;
	}
	/**
	 * ??????????????????
	 * @param type ??????
	 * @param depId ??????id
	 * @param corpCode ????????????
	 * @param memberType 2 ??????????????????  ,3?????????????????????
	 * @return
	 */
	public int getCountByDepId(Integer type, Long depId, String corpCode, int memberType) throws Exception
	{
		StringBuffer sqlBuffer = new StringBuffer("select count(*) as totalcount from ");
		//????????????
		String tableName = null;
		//????????????
		String depTableName = null;
		//????????????
		if (type == 1) 
		{
			tableName = TableLfEmployee.TABLE_NAME;
			depTableName = TableLfEmployeeDep.TABLE_NAME;
		}
		//????????????
		else 
		{
			tableName = TableLfClient.TABLE_NAME;
			depTableName = TableLfClientDep.TABLE_NAME;
			
		}
		sqlBuffer.append(tableName).append(StaticValue.getWITHNOLOCK());
		
		StringBuffer whereSql = new StringBuffer();
		//??????????????????
		if(memberType == 2)
		{
			//?????????
			whereSql.append(" where ").append(TableLfClientDep.DEP_ID).append(" = ").append(depId)
									.append(" and ").append(TableLfClientDep.CORP_CODE).append("='").append(corpCode).append("'");
		}
		else if(memberType == 3)
		{
			String tempSql = new StringBuffer("select ").append(TableLfClientDep.DEP_PATH).append(" from ").append(depTableName).append(StaticValue.getWITHNOLOCK())
						.append(" where ").append(TableLfClientDep.DEP_ID).append(" = ").append(depId)
						.append(" and ").append(TableLfClientDep.CORP_CODE).append("='").append(corpCode).append("'").toString();
			List<LfClientDep> dep = findPartEntityListBySQL(LfClientDep.class, tempSql, StaticValue.EMP_POOLNAME);
			//?????????????????????path??????????????????????????????
			String deppath = (dep != null && dep.size() >0)?dep.get(0).getDeppath():"";
			
			whereSql.append(" where ").append(TableLfClientDep.DEP_ID).append(" in (")
					.append(" select ").append(TableLfClientDep.DEP_ID).append(" from ").append(depTableName).append(StaticValue.getWITHNOLOCK())
					.append(" where ").append(TableLfClientDep.DEP_PATH).append(" like ").append("'").append(deppath).append("%'").append(") ")
									.append(" and ").append(TableLfClientDep.CORP_CODE).append("='").append(corpCode).append("'");
		}
		
		//????????????
		return findCountBySQL(sqlBuffer.append(whereSql).toString());
	}
	
	public List<LfSysuser> findDomUserBySysuserID(String sysuserID)
	throws Exception {
		//??????sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
				TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
				" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
				domination).append(")) and ").append(TableLfSysuser.USER_ID)
				.append("<>1 and ").append(TableLfSysuser.USER_STATE).append(
						" = 1 ");
		String sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		//????????????
		return returnList;
	}
	
	public List<LfSysuser> findDomUserBySysuserID2(String depId, String sysuserID)
	throws Exception {
		List<LfSysuser> returnList = null;
		//??????sql
		String sql = "select dep.* from " + TableLfDep.TABLE_NAME
				+ " dep inner join " + TableLfDomination.TABLE_NAME
				+ " domination on dep." + TableLfDep.DEP_ID + "=domination."
				+ TableLfDomination.DEP_ID + " where domination."
				+ TableLfDomination.USER_ID + "=" + sysuserID + " and dep."+TableLfDep.DEP_STATE + "=1 order by dep."
				+TableLfDomination.DEP_ID;
		//??????????????????
		List<LfDep> lfDep = findEntityListBySQL(LfDep.class,
				sql, StaticValue.EMP_POOLNAME);
		if(lfDep != null && lfDep.size()>0){
			if(depId == null || "".equals(depId)){
				depId = lfDep.get(0).getDepId().toString();
			}
				StringBuffer domination = new StringBuffer("select ").append(
						TableLfDomination.DEP_ID).append(" from ").append(
						TableLfDomination.TABLE_NAME).append(" where ").append(
						TableLfDomination.USER_ID).append("=").append(sysuserID);
				StringBuffer dominationSql = new StringBuffer(" where (").append(
						TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
						" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
						domination).append("))and ").append(TableLfSysuser.DEP_ID)
						.append(" in(select ").append(TableLfDep.DEP_ID)
						.append(" from ").append(TableLfDep.TABLE_NAME).append(" where ")
						.append(TableLfDep.DEP_ID).append(" = ").append(depId)
						.append(") and ").append(TableLfSysuser.USER_ID)
						.append("<>1 and ").append(TableLfSysuser.USER_STATE).append(
								" = 1 ");
				String sql2 = new StringBuffer("select * from ").append(
						TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
				sql2 += " order by " + TableLfSysuser.NAME + " asc";
				returnList = findEntityListBySQL(LfSysuser.class, sql2,
						StaticValue.EMP_POOLNAME);
		}
		//????????????
		return returnList;
	}
	
	/**
	 * ??????????????????
	 */
	public List<LfPrivilege> findPrivilegesBySysuserId(String sysuserID,
                                                       String functionType) throws Exception {
		
		LfSysuser lfSysuser = new BaseBiz().getById(LfSysuser.class, sysuserID);
		String corpCode = lfSysuser.getCorpCode();
		
		String privilegeSql = "";
		
		if (functionType.equals("MENU")) {
			privilegeSql = "select  lfprivilege." + TableLfPrivilege.MENU_CODE
					+ "  ,lfprivilege." + TableLfPrivilege.MENU_SITE
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_TW_MOD_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_HK_MOD_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_TW_MENU_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.ZH_HK_MENU_NAME
					+ "  ,lfprivilege." + TableLfPrivilege.RESOURCE_ID
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
					+ TableLfPrivilege.OPERATE_ID + "=1 " ;
					if(!SystemGlobals.isDepBilling(corpCode) && !"1".equals(sysuserID)){
						privilegeSql += " and "+TableLfPrivilege.MENU_CODE+" not in ('"+ViewParams.CZCODE+"')";
					}
					privilegeSql += "group by ";
					privilegeSql +=  TableLfPrivilege.MENU_CODE + ",";
					privilegeSql +=  TableLfPrivilege.MENU_SITE + ",";
					privilegeSql +=  TableLfPrivilege.RESOURCE_ID + ",";
					privilegeSql +=  TableLfPrivilege.MENU_NAME + ",";
					privilegeSql +=  TableLfPrivilege.ZH_TW_MOD_NAME + ",";
					privilegeSql +=  TableLfPrivilege.ZH_HK_MOD_NAME + ",";
					privilegeSql +=  TableLfPrivilege.ZH_TW_MENU_NAME + ",";
					privilegeSql +=  TableLfPrivilege.ZH_HK_MENU_NAME + ",";
					privilegeSql +=  TableLfPrivilege.MOD_NAME + " order by ";
					privilegeSql +=  TableLfPrivilege.MENU_CODE + " asc";
		} else if ("SITE".equals(functionType)) {
			
			StringBuffer tempSql =  new StringBuffer("select * from ").append(
					TableLfPrivilege.TABLE_NAME).append(" where ");
			
			if(!SystemGlobals.isDepBilling(corpCode)){
				tempSql.append(" and "+TableLfPrivilege.MENU_CODE+" not in ('"+ViewParams.CZCODE+"')");
			}
			tempSql.append(
					TableLfPrivilege.OPERATE_ID).append("=1");
			privilegeSql = tempSql.toString();
		} else {
			privilegeSql = "select *  from " + TableLfPrivilege.TABLE_NAME
					+ " where " + TableLfPrivilege.PRIVILEGE_ID
					+ " in ( select " + TableLfImpower.PRIVILEGE_ID + " from "
					+ TableLfImpower.TABLE_NAME + " where "
					+ TableLfImpower.ROLE_ID + " in (select "
					+ TableLfUser2role.ROLE_ID + " from "
					+ TableLfUser2role.TABLE_NAME + " where "
					+ TableLfUser2role.USER_ID + " = " + sysuserID + "))  ";
					if(!SystemGlobals.isDepBilling(corpCode)){
						privilegeSql += " and "+TableLfPrivilege.MENU_CODE+" not in ('"+ViewParams.CZCODE+"')";
					}
			privilegeSql += " order by " + TableLfPrivilege.PRIV_CODE + " asc";
		}
		List<LfPrivilege> lfPrivilegesList = findPartEntityListBySQL(
				LfPrivilege.class, privilegeSql, StaticValue.EMP_POOLNAME);
		return lfPrivilegesList;
	}
	
	/**
	 * ???????????????????????????????????????
	 * @param userName
	 * @param passWord
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<LfSysuser> getLfSysUserByUP(String userName, String passWord, String corpCode)
			throws Exception {
		StringBuffer sql = new StringBuffer("select * from ").append(
				TableLfSysuser.TABLE_NAME).append(" lfSysUser ").append(StaticValue.getWITHNOLOCK());
				
		sql.append(" where upper(lfSysUser.").append(
				TableLfSysuser.USER_NAME).append(") = '").append(
						userName).append("' ");
		//???????????????password
		if (passWord != null && !"".equals(passWord)) {
			sql.append(" and lfSysUser.").append(TableLfSysuser.PASSW)
					.append(" = '").append(passWord).append("'");
		}
		//???????????????????????????
		if (corpCode != null && !"".equals(corpCode)) {
			sql.append(" and lfSysUser.").append(TableLfSysuser.CORP_CODE)
					.append(" = '").append(corpCode).append("'");
		}
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		return returnList;
	}
	
	/**
	 * ????????????id??????????????????????????????????????????
	 * @param sysuserId
	 * @return
	 * @throws Exception
	 */
	public List<LfPrivilegeAndMenuVo> findAllMenuVo(String corpCode, String sysuserId, String prType) throws Exception
	{
		
		String sql = "";
		if(prType.equals("0"))
		{
		    sql ="select distinct ltm.*, lp.*  from "+TableLfPrivilege.TABLE_NAME+" lp inner join "
			          + TableLfImpower.TABLE_NAME+" li on lp."+TableLfPrivilege.PRIVILEGE_ID+" = li."
			          +TableLfImpower.PRIVILEGE_ID+" inner join "+TableLfUser2role.TABLE_NAME+" lur on lur."
			          +TableLfUser2role.ROLE_ID+" = li."+TableLfImpower.ROLE_ID+" and lur."
			          +TableLfUser2role.USER_ID+" = "+sysuserId+" left join "+TableLfThiMenuControl.TABLE_NAME
			          +" ltm on ltm."+TableLfThiMenuControl.PRI_MENU+" = lp."+TableLfPrivilege.RESOURCE_ID
			          +" where lp."+TableLfPrivilege.OPERATE_ID+" = 1";
		    if(!SystemGlobals.isDepBilling(corpCode) && !"1".equals(sysuserId)){
				sql += " and lp."+TableLfPrivilege.MENU_CODE+" not in ('"+ViewParams.CZCODE+"')";
			}
		    sql = sql+" order by ltm."+TableLfThiMenuControl.PRI_ORDER
			          +" ,ltm."+TableLfThiMenuControl.PRI_MENU+" ,lp."+TableLfPrivilege.PRIV_CODE;
		}else if(prType.equals("1"))
		{
			 sql ="select distinct ltm.*, lp.*  from "+TableLfPrivilege.TABLE_NAME+" lp inner join "
	          + TableLfImpower.TABLE_NAME+" li on lp."+TableLfPrivilege.PRIVILEGE_ID+" = li."
	          +TableLfImpower.PRIVILEGE_ID+" inner join "+TableLfUser2role.TABLE_NAME+" lur on lur."
	          +TableLfUser2role.ROLE_ID+" = li."+TableLfImpower.ROLE_ID+" and lur."
	          +TableLfUser2role.USER_ID+" = "+sysuserId+" left join "+TableLfThiMenuControl.TABLE_NAME
	          +" ltm on ltm."+TableLfThiMenuControl.PRI_MENU+" = lp."+TableLfPrivilege.RESOURCE_ID;
			 if(!SystemGlobals.isDepBilling(corpCode) && !"1".equals(sysuserId)){
					sql += " where lp."+TableLfPrivilege.MENU_CODE+" not in ('"+ViewParams.CZCODE+"')";
				}
	          
	         sql += "  order by ltm."+TableLfThiMenuControl.PRI_ORDER
	          +" ,ltm."+TableLfThiMenuControl.PRI_MENU+" ,lp."+TableLfPrivilege.PRIV_CODE;
		}
		List<LfPrivilegeAndMenuVo> lfPMVoList = findVoListBySQL(LfPrivilegeAndMenuVo.class,sql,StaticValue.EMP_POOLNAME);
		return lfPMVoList;
	}
	
	/**
	 * ???????????????????????????????????????????????????
	 */
	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecord(String sysuserID)
	throws Exception {
		//??????sql
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
		//??????????????????
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
		//????????????
		return returnList;
	}
	
	public List<LfSysuser> findDomUserBySysuserIDOfSmsTaskRecordByDep(String sysuserID, String depId)
    throws Exception {
		//??????sql
		StringBuffer domination = new StringBuffer("select ").append(
			TableLfDomination.DEP_ID).append(" from ").append(
					TableLfDomination.TABLE_NAME).append(" where ").append(
							TableLfDomination.USER_ID).append("=").append(sysuserID);
		StringBuffer dominationSql = new StringBuffer(" where (").append(
			TableLfSysuser.USER_ID).append("=").append(sysuserID).append(
			" or ").append(TableLfSysuser.DEP_ID).append(" in (").append(
					domination).append(")) and ").append(TableLfSysuser.USER_ID)
					.append("<>1 and ").append(TableLfSysuser.DEP_ID).append("=").append(depId);
		String sql = new StringBuffer("select * from ").append(
			TableLfSysuser.TABLE_NAME).append(dominationSql).toString();
		//??????????????????
		sql += " order by " + TableLfSysuser.NAME + " asc";
		List<LfSysuser> returnList = findEntityListBySQL(LfSysuser.class, sql,
			StaticValue.EMP_POOLNAME);
		//????????????
		return returnList;
	}
	
	public List<LfDomination> findDomDepIdByUserID(String userID) throws Exception {
		//??????sql
		String sql = "select d.* from " + TableLfDomination.TABLE_NAME
		+ " d left join " + TableLfDep.TABLE_NAME
		+ " ld on ld." + TableLfDep.DEP_ID
		+ "=d." + TableLfDomination.DEP_ID
		+ " where d." + TableLfDomination.USER_ID + "=" + userID+"  order by ld.dep_level";

		//????????????
		return findEntityListBySQL(LfDomination.class, sql,
				StaticValue.EMP_POOLNAME);
	}
	
	public List<Userdata> findBindUserdataForRute() throws Exception {
		//??????sql
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
		//????????????
		List<Userdata> userdatas = findPartEntityListBySQL(Userdata.class, sql,
				StaticValue.EMP_POOLNAME);
		//????????????
		return userdatas;
	}
	
	public List<LfClient> findLfClient(String depCode, String corpCode) throws Exception
	{
		//??????sql
		StringBuffer sql = new StringBuffer("select ").append(
				TableLfClient.MOBILE).append(" from ").append(
				TableLfClient.TABLE_NAME).append(" where ")
				.append(TableLfClient.CORP_CODE).append("='").append(corpCode.trim()).append("' ");
		//????????????
		if (null != depCode && !"".equals(depCode))
		{
			sql.append(" and ").append(TableLfClient.DEP_CODE)
					.append(" like '").append(EmpUtils.getPartDepCode(depCode))
					.append("%'");
		}
		//????????????
		List<LfClient> returnList = findPartEntityListBySQL(LfClient.class, sql
				.toString(), StaticValue.EMP_POOLNAME);
		//????????????
		return returnList;
	}
	
	
	
	/**
	 * ??????id?????????????????????
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public LfSysuserVo findLfSysuserVoByID(String ID) throws Exception
	{
		LfSysuserVo returnVo = new LfSysuserVo();
		String sql = "select sysuser." + TableLfSysuser.USER_ID + ",sysuser."+TableLfSysuser.USER_TYPE+ ",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.GUID + ",sysuser."
				+ TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW+",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+ ",sysuser."
				+ TableLfSysuser.USER_NAME + ",sysuser."+TableLfSysuser.PERMISSION_TYPE
				+",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID
				+",sysuser."+TableLfSysuser.SHOWNUM
				+",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID;
		sql += baseSql;
		sql += " where sysuser." + TableLfSysuser.USER_ID + "=" + ID;
		List<LfSysuserVo> returnList = findVoListBySQL(LfSysuserVo.class, sql,
				StaticValue.EMP_POOLNAME);
		returnList = completeLfSysuserVo(returnList);
		if (returnList != null && returnList.size() > 0)
		{
			returnVo = (LfSysuserVo) returnList.get(0);
		}
		return returnVo;
	}
	
	/**
	 * ??????id????????????????????????????????????
	 * @description    
	 * @param ID
	 * @param corpCode
	 * @return
	 * @throws Exception
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 ??????08:37:56
	 */
	public LfSysuserVo findLfSysuserVoByIDAndCorpCode(String ID, String corpCode) throws Exception
	{
		LfSysuserVo returnVo = new LfSysuserVo();
		String sql = "select sysuser." + TableLfSysuser.USER_ID + ",sysuser."+TableLfSysuser.USER_TYPE+ ",sysuser."
				+ TableLfSysuser.NAME + ",sysuser." + TableLfSysuser.USER_STATE
				+ ",sysuser." + TableLfSysuser.HOLDER + ",sysuser."
				+ TableLfSysuser.REG_TIME + ",sysuser." + TableLfSysuser.MOBILE
				+ ",sysuser." + TableLfSysuser.GUID + ",sysuser."
				+ TableLfSysuser.OPH + ",sysuser."
				+ TableLfSysuser.QQ + ",sysuser." + TableLfSysuser.E_MAIL + ",sysuser." + TableLfSysuser.ISEXISTSUBNO
				+ ",sysuser." + TableLfSysuser.SEX + ",sysuser."
				+ TableLfSysuser.PASSW+",sysuser."+TableLfSysuser.FAX
				+",sysuser."+TableLfSysuser.MSN+",sysuser."+TableLfSysuser.BIRTHDAY
				+",sysuser."+TableLfSysuser.IS_REVIEWER+",sysuser."+TableLfSysuser.COMMENTS
				+",sysuser.duties"+ ",sysuser."
				+ TableLfSysuser.USER_NAME + ",sysuser."+TableLfSysuser.PERMISSION_TYPE
				+",sysuser."+TableLfSysuser.USER_CODE+",sysuser."+TableLfSysuser.GUID
				+",sysuser."+TableLfSysuser.SHOWNUM
				+",dep." + TableLfDep.DEP_ID
				+ ",dep." + TableLfDep.DEP_NAME;
		String baseSql = " from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDep.TABLE_NAME
				+ " dep on sysuser." + TableLfSysuser.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID;
		sql += baseSql;
		sql += " where sysuser." + TableLfSysuser.USER_ID + "=" + ID + " AND sysuser.CORP_CODE = '"+corpCode+"'";
		List<LfSysuserVo> returnList = findVoListBySQL(LfSysuserVo.class, sql,
				StaticValue.EMP_POOLNAME);
		returnList = completeLfSysuserVo(returnList);
		if (returnList != null && returnList.size() > 0)
		{
			returnVo = (LfSysuserVo) returnList.get(0);
		}
		return returnVo;
	}
	
	protected List<LfSysuserVo> completeLfSysuserVo(List<LfSysuserVo> lfsysuser)
	throws Exception
	{
		StringBuffer sb = new StringBuffer("");
		LinkedHashMap<Long, String> subnoMap = new LinkedHashMap<Long, String>();
		List<LfSubnoAllot> allotList = null;
		LfSysuserVo userVo = null;
		for(int j=0;j< lfsysuser.size(); j++){
			userVo = lfsysuser.get(j);
			if(userVo.getIsExistSubno() == 1){
				sb.append("'").append(userVo.getGuId()).append("',");
			}
		}
		String sql = "";
		if(sb.length()>0){
			sql = sb.toString().substring(0, sb.toString().length()-1);
			//??????sql
			StringBuffer subnoSql = new StringBuffer("SELECT * FROM ").append(TableLfSubnoAllot.TABLE_NAME)
			.append(" WHERE ").append(TableLfSubnoAllot.LOGINID).append(" IN ( ").append(sql).append(" ) and MENUCODE is null  and BUS_CODE is null");
			allotList = findEntityListBySQL(LfSubnoAllot.class, subnoSql.toString(),StaticValue.EMP_POOLNAME);
		}
		
		if(allotList != null && allotList.size()>0){
			LfSubnoAllot allot = null;
			for(int k=0;k<allotList.size();k++){
				allot = allotList.get(k);
				subnoMap.put(Long.valueOf(allot.getLoginId()), allot.getUsedExtendSubno());
			}
		}
		for (int i = 0; i < lfsysuser.size(); i++) {
			LfSysuserVo lfSysuserVo = lfsysuser.get(i);
			if(lfSysuserVo.getIsExistSubno() == 1){
				String usedSubno = subnoMap.get(lfSysuserVo.getGuId());
				if(usedSubno != null && !"".equals(usedSubno)){
					lfSysuserVo.setUsedSubno(usedSubno);
				}
			}
			//??????sql
			if(lfSysuserVo.getPermissionType() == 2){
				//???????????????
				String domDepSql = "select dep.*" + " from "
				+ TableLfDomination.TABLE_NAME + " domination inner join "
				+ TableLfDep.TABLE_NAME + " dep on domination."
				+ TableLfDomination.DEP_ID + "=dep." + TableLfDep.DEP_ID
				+ " where domination." + TableLfDomination.USER_ID + "="
				+ lfSysuserVo.getUserId() + " order by dep."
				+ TableLfDep.DEP_ID + " asc";
				lfSysuserVo.setDomDepList(findEntityListBySQL(LfDep.class,
						domDepSql, StaticValue.EMP_POOLNAME));
			}
			//??????sql
			String roleSql = "select roles.*" + " from "
					+ TableLfRoles.TABLE_NAME + " roles inner join "
					+ TableLfUser2role.TABLE_NAME + " user2role on roles."
					+ TableLfRoles.ROLE_ID + "=user2role."
					+ TableLfUser2role.ROLE_ID + " where user2role."
					+ TableLfUser2role.ROLE_ID + "!=1 and user2role."
					+ TableLfUser2role.USER_ID + "=" + lfSysuserVo.getUserId()
					+ " order by roles." + TableLfRoles.ROLE_ID + " asc";
			lfSysuserVo.setRoleList(findEntityListBySQL(LfRoles.class, roleSql,
					StaticValue.EMP_POOLNAME));
		}
		return lfsysuser;
	}
	/**
	 * ??????????????????????????????????????????????????????????????????
	 * @return
	 */
	public List<DynaBean> getSpuserBind()
	{
		String sql = "select ag.spaccid,ud.userid,ag.spfeeflag,ud.ACCOUNTTYPE from A_GWACCOUNT ag LEFT JOIN  A_GWSPBIND bd" +
				" on ag.ptaccuid = bd.PTACCUID LEFT JOIN xt_gate_queue xt"+
				" on bd.gateid = xt.id left JOIN  gt_port_used gt  on gt.spgate = xt.spgate " +
				"left JOIN userdata ud on ud.userid = gt.userid where ud.UserType =0";
		
		return this.getListDynaBeanBySql(sql);
	}
	/**
	 * ?????????????????????????????????????????????????????????
	 * @param userid ??????????????????
	 * @param mstype 1-?????????2-??????
	 * @return
	 */
	public List<DynaBean> getSpuserBind(String userid, Integer mstype)
	{
		String sql = "select ag.spaccid,ag.spfeeflag from A_GWACCOUNT  ag  where  exists ("+
		"select ptaccuid from A_GWSPBIND aw where aw.ptaccuid=ag.ptaccuid and exists ("+
		"select id from xt_gate_queue xg where xg.id=aw.gateid  and  exists ("+
		"select gt.spgate from gt_port_used gt left JOIN userdata ud on ud.userid = gt.userid" +
		"  where xg.spgate=gt.spgate and ud.userid='"+userid+"'  and ud.accounttype="+mstype.toString()+")))";
		
		return this.getListDynaBeanBySql(sql);
	}
	/**
	 * ????????????????????????
	 * @param userid
	 * @return
	 */
	public List<DynaBean> getFlowRecordWithNoReview(String userid)
	{
		String sql = "select INFO_TYPE,USER_CODE,count(USER_CODE) recount from LF_FLOWRECORD flow" + StaticValue.getWITHNOLOCK()+
				//" left join lf_mttask mt"+StaticValue.WITHNOLOCK+" on flow.mt_id=mt.mt_id" +
				" where flow.is_Complete=2 and flow.user_Code="
				+userid+" and  flow.r_State=-1 GROUP BY user_Code,INFO_TYPE";
		
		return this.getListDynaBeanBySql(sql);
	}
	
	/**
	 * ????????????????????????
	 * @param userid
	 * @return
	 */
	public List<DynaBean> getMttaskWithNoReview(String userid)
	{
		String sql = "select ms_type,COUNT(MT_ID) mtcount from lf_mttask "+StaticValue.getWITHNOLOCK()+" where " +
		"re_State = -1 and sub_state<>3 and user_id="+userid+" group by ms_type";
		
		return this.getListDynaBeanBySql(sql);
	}
	/**
	 * ??????????????????????????????
	 * @param userid
	 * @return
	 */
	public List<DynaBean> getTempWithNoReview(String userid)
	{
		String sql = "select tmp_Type,COUNT(tm_id) tmcount from LF_TEMPLATE " +StaticValue.getWITHNOLOCK()+
				"where  tmp_type !=11 and isPass = -1 and user_id="+userid+" group by tmp_Type";
		
		return this.getListDynaBeanBySql(sql);
	}
	/**
	 * ????????????????????????????????????
	 * @param userid ?????????id
	 * @return ??????????????????????????????
	 */
	public Integer getWxTempReviewCount(String userid)
	{
		String sql = "select count(id) totalcount from lf_wx_baseinfo "+
			StaticValue.getWITHNOLOCK()+"  where status = 1 and creatid = "+userid;
		return this.findCountBySQL(sql);
	}
	
	/**
	 * ?????????????????????????????????
	 * @param userid
	 * @return
	 */
	public List<DynaBean> getTimerTaskCount(String userid)
	{
		String sql = "select ms_type ,count(mt_id) tmcount from lf_mttask "+StaticValue.getWITHNOLOCK()+" where TIMER_STATUS = 1" +
				" and SENDSTATE=0 and sub_state = 2 and USER_ID="+userid+" GROUP BY MS_TYPE";
		
		return this.getListDynaBeanBySql(sql);
	}
	
	/**
	 * ??????????????????
	 * @return ?????????????????? 
	 */
	public List<DynaBean> getPosition()
	{
		String sql = "select pr.menucode,pr.menuName,pr.ZH_TW_MENUNAME,pr.ZH_HK_MENUNAME,pr.modname,pr.ZH_TW_MODNAME,pr.ZH_HK_MODNAME,tm.ZH_TW_TITLE,tm.ZH_HK_TITLE,tm.TITLE,pr.RESOURCE_ID from LF_PRIVILEGE pr "+
			"left join LF_THIR_MENUCONTROL tm on tm.PRI_MENU= pr.RESOURCE_ID where pr.OPERATE_ID = 1";
		
		return this.getListDynaBeanBySql(sql);
	}
	
	/**
	 * @description  ???????????????????????????????????????
	 * @param spUser ??????
	 * @param balance ??????
	 * @param accountType 1-?????????2-??????
	 * @return true-?????????false-??????
	 * @throws Exception
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-30 ??????03:24:43
	 */
	public boolean updateLfSpFeeBalance(String spUser, String balance, String accountType) throws Exception
	{
		//??????????????????????????????
		String sql = "update lf_spfee set balance=(balance+"+balance+") where SPFEE_FLAG = 1 and sp_User ='"+spUser+"' and accounttype="+accountType;
		
		return executeBySQL(sql, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * @description  ?????????????????????????????????????????????
	 * @param spUser ??????
	 * @param balance ??????
	 * @param accountType 1-?????????2-??????
	 * @return true-?????????false-??????
	 * @throws Exception
	 * @author linzhihan <zhihanking@163.com>
	 * @datetime 2013-10-30 ??????03:24:43
	 */
	public boolean updateLfSpFeeBalanceRMS(String spUser, String balance, String accountType) throws Exception
	{
		//??????????????????????????????
		String sql = "update lf_spfee set rms_balance=(rms_balance+"+balance+") where SPFEE_FLAG = 1 and sp_User ='"+spUser+"' and accounttype="+accountType;
		
		return executeBySQL(sql, StaticValue.EMP_POOLNAME);
	}
	
	public List<LfMttask> findLfMttaskByCorpCode(String corpCode) throws Exception {
		String sql="select * from lf_mttask where ((TIMER_STATUS=1 and RE_STATE<>2) or (TIMER_STATUS=0 and RE_STATE=-1)) and SUB_STATE=2 and SENDSTATE=0  and CORP_CODE='"+corpCode+"'";
		return findEntityListBySQL(LfMttask.class, sql, StaticValue.EMP_POOLNAME);
	}
	
	/**
	 * ????????????????????????
	 * @description           			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-12 ??????04:51:37
	 */
	public void setMonOnlcfg()
	{
		try
		{
			String sql="UPDATE LF_MON_ONLCFG SET ONLINENUM = (SELECT COUNT(*) FROM LF_MON_ONLUSER)";
			executeBySQL(sql, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "?????????????????????????????????");
		}
	}

	public List<LfPrivilege> getLfShortTempList(String corpcode, Long userId,HttpServletRequest req) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM LF_SHORTTEMP WHERE CORPCODE =? AND USERID=?";
		List<LfPrivilege> list = new ArrayList<LfPrivilege>();
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1, corpcode);
			ps.setLong(2, userId);
			rs = ps.executeQuery();
			//String menusite = "/rms_templateMana.htm";
			//long resourceId = getResourceId(menusite);
			String menusite = getMenusite();
			long resourceId = 93L;
			while (rs.next()) {
				LfPrivilege lfPrivilege = new LfPrivilege();
				lfPrivilege.setMenuCode(String.valueOf(rs.getString("TEMPID")));
				lfPrivilege.setMenuName(rs.getString("TEMPNAME"));
				lfPrivilege.setMenuSite(menusite+"?method=shortCut2Send&tempId="+String.valueOf(rs.getString("TEMPID")));
				lfPrivilege.setModName(StringUtils.defaultIfEmpty(MessageUtils.extractMessage("rms", "rms_kjcj_mdkjcj", req), "??????????????????"));
				lfPrivilege.setResourceId(resourceId);
				list.add(lfPrivilege);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????LF_SHORTTEMP??????????????????");
			return list;
		}finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "??????????????????");
			}
		}
		return list;
	}

	public List<LfPrivilege> getLfShortTempList(String corpcode, Long userId) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT * FROM LF_SHORTTEMP WHERE CORPCODE =? AND USERID=?";
		List<LfPrivilege> list = new ArrayList<LfPrivilege>();
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			ps.setString(1, corpcode);
			ps.setLong(2, userId);
			rs = ps.executeQuery();
			//String menusite = "/rms_templateMana.htm";
			//long resourceId = getResourceId(menusite);
			String menusite = getMenusite();
			long resourceId = 93L;
			while (rs.next()) {
				LfPrivilege lfPrivilege = new LfPrivilege();
				lfPrivilege.setMenuCode(String.valueOf(rs.getString("TEMPID")));
				lfPrivilege.setMenuName(rs.getString("TEMPNAME"));
				lfPrivilege.setZhHkMenuName(rs.getString("TEMPNAME"));
				lfPrivilege.setZhTwMenuName(rs.getString("TEMPNAME"));
				lfPrivilege.setModName("??????????????????");
				lfPrivilege.setZhHkModName("My shortcut");
				lfPrivilege.setZhTwModName("??????????????????");
				lfPrivilege.setMenuSite(menusite+"?method=shortCut2Send&tempId="+String.valueOf(rs.getString("TEMPID")));
				lfPrivilege.setResourceId(resourceId);
				list.add(lfPrivilege);
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "??????LF_SHORTTEMP??????????????????");
			return list;
		}finally
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "??????????????????");
			}
		}
		return list;
	}

	public String getMenusite() {
		String returnStr = null ;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "SELECT MENUSITE FROM LF_PRIVILEGE WHERE MENUNAME = '????????????' AND MODNAME = '????????????'";
		try {
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			if (rs.next()) {
				returnStr = rs.getString("MENUSITE");
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e,"??????LF_PRIVILEGE????????????");
			return null;
		}finally 
		{
			try {
				close(rs, ps, conn);
			} catch (Exception e) {
				EmpExecutionContext.error(e, "??????????????????");
			}
		}
		return returnStr;
	}
	
	/**
	 * ????????????????????????????????????
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public LfCorp getCorp(String corpCode) throws Exception {
		LfCorp corp = null;
		String sql = "SELECT * FROM "+TableLfCorp.TABLE_NAME+" WHERE "+TableLfCorp.CORP_CODE+" = '"+corpCode+"'";
		List<LfCorp> corps = findPartEntityListBySQL(LfCorp.class, sql, StaticValue.EMP_POOLNAME);
		if(corps != null && !corps.isEmpty()){
			corp = corps.get(0);
		}
		return corp;
	}

	/**
	 * ??????SP????????????
	 * @param spUser SP??????
     * @param recyleCount ????????????
     * @return ????????? true????????? false?????????
	 * @throws Exception ??????
	 */
    public boolean updateUserFeeBalanceRMS(String spUser, Long recyleCount) throws Exception {
		//??????????????????????????????
		String sql = "UPDATE USERFEE SET SENDNUM=(SENDNUM + "+recyleCount+"),SENDEDNUM=(SENDEDNUM - "+ recyleCount +") WHERE USERID = '"+spUser + "'";

		return executeBySQL(sql, StaticValue.EMP_POOLNAME);
    }
}
