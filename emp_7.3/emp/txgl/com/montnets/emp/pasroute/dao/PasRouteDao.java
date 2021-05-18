package com.montnets.emp.pasroute.dao;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.servmodule.txgl.dao.SuperTxglDAO;
import com.montnets.emp.servmodule.txgl.entity.AcmdPort;
import com.montnets.emp.servmodule.txgl.entity.AcmdRoute;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.servmodule.txgl.entity.XtGateQueue;
import com.montnets.emp.servmodule.txgl.table.TableUserdata;
import com.montnets.emp.servmodule.txgl.table.TableXtGateQueue;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.util.PageInfo;
/**
 *   账户通道配置 dao 
 * @author Administrator
 *
 */
public class PasRouteDao extends SuperDAO{

	
	public List<XtGateQueue> findGateInfoByCorp(String corp,
			LinkedHashMap<String,String> conditionMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select distinct xt.* from " + TableXtGateQueue.TABLE_NAME
				+ " xt join " + TableGtPortUsed.TABLE_NAME + " gt on " + "xt." + TableXtGateQueue.SPGATE
				+ "=gt." + TableGtPortUsed.SPGATE + " where gt." + TableGtPortUsed.USER_ID
				+ " in (select sp.spuser from " + TableLfSpDepBind.TABLE_NAME
				+ " sp where sp." + TableLfSpDepBind.CORP_CODE + "='" + corp + "')"
				+ " or gt."+ TableGtPortUsed.USER_ID 
				+ " in (select "+TableLfMmsAccbind.MMS_USER+" from "+TableLfMmsAccbind.TABLE_NAME
				+" where "+TableLfMmsAccbind.CORP_CODE+" = '"+corp+"' )";
				;
		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类字段与表的MAP映射
		Map<String, String> columns = getORMMap(XtGateQueue.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;

			while (iter.hasNext()) {
				e = iter.next();

				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = columns.get(eKey);

					sql = sql + " and xt." + columnName + "='" + e.getValue()
							+ "' ";
				}
			}
		}
		if (pageInfo == null) {
			return findEntityListBySQL(XtGateQueue.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")derivedtbl_1").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(XtGateQueue.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	
	
	public List<Userdata> findSpUserByCorp(String corp,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select * from " + TableUserdata.TABLE_NAME + "   where "
				+ " ( "
				+ TableUserdata.USER_ID + " in (select spuser from " + TableLfSpDepBind.TABLE_NAME
				+ " where " + TableLfSpDepBind.CORP_CODE + "='" + corp + "' and "
				+ TableLfSpDepBind.IS_VALIDATE + "=1) "
				+ " or "+ TableUserdata.USER_ID + " in (select "+TableLfMmsAccbind.MMS_USER+" from "+TableLfMmsAccbind.TABLE_NAME
				+ " where "+TableLfMmsAccbind.CORP_CODE+" = '"+corp+"' and "+TableLfMmsAccbind.IS_VALIDATE+" = 1 )"
				+ " ) "
				;

		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库表的MAP映射
		Map<String, String> columns = getORMMap(Userdata.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;

			while (iter.hasNext()) {
				e = iter.next();

				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					if (eKey.contains("&like")) {
						sql = sql + " and upper(" + columnName + ") like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else {
						sql = sql + " and " + columnName + "='" + e.getValue()
								+ "' ";
					}

				}
			}
		}
		String countSqlStr=sql;
		//排序条件拼接
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
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(countSqlStr)
					.append(") A").toString();
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	
	
	
	public List<GtPortUsed> findSpRouteByCorp(String corp,
			LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select * from " + TableGtPortUsed.TABLE_NAME + "   where "
				+ " ( "
				+ TableGtPortUsed.USER_ID + " in (select spuser from " + TableLfSpDepBind.TABLE_NAME
				+ " where " + TableLfSpDepBind.CORP_CODE + "='" + corp + "' and " + TableLfSpDepBind.IS_VALIDATE + "=1)"
				+ " or "+ TableGtPortUsed.USER_ID + " in (select "+TableLfMmsAccbind.MMS_USER+" from "+TableLfMmsAccbind.TABLE_NAME
				+ " where "+TableLfMmsAccbind.CORP_CODE+" = '"+corp+"' and "+TableLfMmsAccbind.IS_VALIDATE+" = 1 ) "
				+ " ) "
				;

		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库表的MAP映射
		Map<String, String> columns = getORMMap(GtPortUsed.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;

			while (iter.hasNext()) {
				e = iter.next();

				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.substring(0, eKey.indexOf("&")).trim());
					if (eKey.contains("&like")) {
						sql = sql + " and upper(" + columnName + ") like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + " and " + columnName + ">" + e.getValue()
								+ " ";
					} else {
						sql = sql + " and " + columnName + "='" + e.getValue()
								+ "' ";
					}

				}
			}
		}
		//排序条件拼接
		StringBuffer orderbySql = new StringBuffer("");
		if (orderbyMap != null && !orderbyMap.entrySet().isEmpty())
		{
			orderbySql.append(" order by ");
			Iterator<Map.Entry<String, String>> iter1 = orderbyMap.entrySet()
					.iterator();
			Map.Entry<String, String> e1 = null;
			while (iter1.hasNext())
			{
				e1 = iter1.next();
				orderbySql.append(columns.get(e1.getKey())).append(" ").append(
						e1.getValue()).append(",");
			}
			
			orderbySql.deleteCharAt(orderbySql.lastIndexOf(","));
		}
		//sql += orderbySql.toString();
		//记录数sql拼接
		String countSql = new StringBuffer("select count(*) totalcount from (")
				.append(sql).append(") derivedtbl_1").toString();
		//执行查询，返回结果
//		return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(GtPortUsed.class, sql, countSql,
//						pageInfo, StaticValue.EMP_POOLNAME);
		return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(GtPortUsed.class, sql, countSql,
				pageInfo, StaticValue.EMP_POOLNAME);
	}
	
	
	public String getUserIdBySpgate(String spgate, int spisuncm,String gatetype)
	throws Exception {
		String userId = null;
		//拼接sql
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
				.append("=").append(spisuncm).append(" and gtportused.")
				.append(TableGtPortUsed.GATE_TYPE).append("=").append(gatetype).toString();
		//执行查询
		userId = getString(TableUserdata.USER_ID, sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return userId;
	}
	
	
	public List<DynaBean> getFeeUrlByGate(String spgate,Integer spisuncm,String gatetype) throws Exception{
		String sql="select agw.feeurl from a_gwaccount agw  inner join a_gwspbind agwb on agw.ptaccuid=agwb.ptaccuid " +
				"inner join xt_gate_queue xt on xt.id=agwb.gateid where xt.spgate='"+spgate+"' and xt.spisuncm="+spisuncm+" and xt.gatetype="+gatetype;
		return new SuperTxglDAO().getListDynaBeanBySql(sql);
	}
	
	/**
	 * 获取账号绑定的指令
	 * @param spUser sp账号
	 * @return 返回指令集合
	 */
	public List<AcmdRoute> getSpUserCmd(String spUser) {
		try
		{
			String sql="select * from A_CMD_ROUTE where sp_id = (select uid from UserData where userid='"+spUser+"' and accounttype=1 ) and cmdtype=1 ";
			return this.findEntityListBySQL(AcmdRoute.class, sql, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取账号绑定的指令异常。");
			return null;
		}
	}
	
	/**
	 * 根据指令id删除通道指令
	 * @param conn
	 * @param cmdId
	 * @return
	 */
	public Boolean delPortCmd(Connection conn, String cmdId) {
		try
		{
			String sql = "delete from A_CMD_PORT where cmdid in ("+cmdId+")";
			
			return executeBySQL(conn, sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据指令id删除通道指令异常。");
			return null;
		}
	}
	
	/**
	 * 获取通道id
	 * @param spgate
	 * @param spisuncm
	 * @return 返回对象，异常或没有则返回null
	 */
	public XtGateQueue getGateId(String spgate, Integer spisuncm) {
		try
		{
			String sql = "select * from XT_GATE_QUEUE where gatetype=1 and spgate='"+spgate+"' and spisuncm="+spisuncm ;
			List<XtGateQueue> gateList = findEntityListBySQL(XtGateQueue.class, sql, StaticValue.EMP_POOLNAME);
			if(gateList == null || gateList.size() == 0){
				return null;
			}
			return gateList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取通道id异常。");
			return null;
		}
	}
	
	/**
	 * 根据spuser获取其路由绑定
	 * @param spUser
	 * @return 返回绑定记录
	 */
	public List<AcmdPort> getPortFromSpuser(String spUser, Long cmdId){
		
		try{
			//select gate.ID as gateid, port.cpno from GT_PORT_USED port left join XT_GATE_QUEUE gate on port.spgate = gate.spgate and port.SpIsUncm=gate.SpIsUncm where port.userId=@spuser and port.gatetype=1 and gate.gatetype=1
			StringBuffer sql = new StringBuffer();
			sql.append("select gate.ID as gateid, port.cpno as cpno, ")
					.append(cmdId).append(" as cmdid, ")
					.append(" 0 as status, 0 as failopt ")
					.append("from GT_PORT_USED port left join XT_GATE_QUEUE gate ")
					.append("on port.spgate = gate.spgate ")
					.append("and port.SpIsUncm=gate.SpIsUncm ")
					.append("where ")
					.append("port.userId='").append(spUser).append("' ")
					.append("and port.gatetype=1 ")
					.append("and gate.gatetype=1 ");
			
			
			List<AcmdPort> protList = findPartEntityListBySQL(AcmdPort.class, sql.toString(), StaticValue.EMP_POOLNAME);
			
			return protList;
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据spuser获取其路由绑定异常。");
			return null;
		}
		
	}
	
	/**
	 * 根据spuser获取其路由绑定
	 * @param spUser
	 * @return 返回绑定记录
	 */
	public List<AcmdPort> getPortFromSpuser(String spUser){
		
		try{
			//select gate.ID as gateid, port.cpno from GT_PORT_USED port left join XT_GATE_QUEUE gate on port.spgate = gate.spgate and port.SpIsUncm=gate.SpIsUncm where port.userId=@spuser and port.gatetype=1 and gate.gatetype=1
			StringBuffer sql = new StringBuffer();
			sql.append("select gate.ID as gateid, port.cpno as cpno ")
					.append("from GT_PORT_USED port left join XT_GATE_QUEUE gate ")
					.append("on port.spgate = gate.spgate ")
					.append("and port.SpIsUncm=gate.SpIsUncm ")
					.append("where ")
					.append("port.userId='").append(spUser).append("' ")
					.append("and port.gatetype=1 ")
					.append("and gate.gatetype=1 ");
			
			
			List<AcmdPort> protList = findPartEntityListBySQL(AcmdPort.class, sql.toString(), StaticValue.EMP_POOLNAME);
			
			return protList;
		}catch(Exception e){
			EmpExecutionContext.error(e, "根据spuser获取其路由绑定异常。");
			return null;
		}
		
	}
	
	
}
