package com.montnets.emp.tczl.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.tczl.vo.LfTaocanCmdVo;
import com.montnets.emp.util.PageInfo;

public class ydyw_pkgInstructionSetDao extends SuperDAO
{
	/**
	 * FunName:条件查询业务集合（带分页）
	 * Description:
	 * @param  lfBusManagerVo 查询条件 、pageInfo 分页信息
	 * @retuen List<LfBusManagerVo>
	 */
	public List<LfTaocanCmdVo> findLfTaocanCmdVo(LfTaocanCmdVo lfTaocanCmdVo, PageInfo pageInfo)throws Exception
	{
		//查询字段拼接
		String fieldSql = ydyw_pkgInstructionSetSql.getFieldSql();
		//查询表拼接
		String tableSql = ydyw_pkgInstructionSetSql.getTableSql();
		//只显示订购和退订指令，增加企业编码条件
		tableSql += " taocancmd.CORP_CODE='"+lfTaocanCmdVo.getCorpCode()+"' and taocancmd.STRUCT_TYPE in(0,1) ";

		//组装过滤条件
		String conditionSql = ydyw_pkgInstructionSetSql.getConditionSql(lfTaocanCmdVo);
		//排序条件拼接
		String orderBySql = ydyw_pkgInstructionSetSql.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
				.append(conditionSql).append(orderBySql)
				.toString();
		//组装统计SQL语句
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql)
				.toString();
		//调用查询语句
		List<LfTaocanCmdVo> lfTaocanCmdVoList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
				LfTaocanCmdVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return lfTaocanCmdVoList;
	}
	/**
	 * FunName:条件查询业务集合
	 * Description:
	 * @param  lfBusManagerVo 查询条件 、pageInfo 分页信息
	 * @retuen List<LfBusManagerVo>
	 */
	public List<LfTaocanCmdVo> findLfTaocanCmdVo(LfTaocanCmdVo lfTaocanCmdVo)throws Exception
	{
		//查询字段拼接
		String fieldSql = ydyw_pkgInstructionSetSql.getFieldSql();
		//查询表拼接
		String tableSql = ydyw_pkgInstructionSetSql.getTableSql();
		//增加企业编码条件
		tableSql += " taocancmd.CORP_CODE='"+lfTaocanCmdVo.getCorpCode()+"' ";
		//组装过滤条件
		String conditionSql = ydyw_pkgInstructionSetSql.getConditionSql(lfTaocanCmdVo);
		//排序条件拼接
		String orderBySql = ydyw_pkgInstructionSetSql.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql)
		.append(conditionSql).append(orderBySql)
		.toString();
		//调用查询语句
//		System.out.println("sql="+sql);
		List<LfTaocanCmdVo> lfTaocanCmdVoList = findVoListBySQL(
				LfTaocanCmdVo.class, sql,StaticValue.EMP_POOLNAME);
		return lfTaocanCmdVoList;
	}
	
	/**
	 * 查询SP账号
	 */
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		
		//拼接sql(UID为固定条件，去除1 =1方式)
		String UID = StaticValue.DBTYPE == StaticValue.MYSQL_DBTYPE?"UID":"\"UID\"";
		String sql = "select * from " + TableUserdata.TABLE_NAME + " where " + UID + ">100001";

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
					} else 
					{
						if(e.getValue() != null && "loginId&=".equals(e.getKey()) && "WBS00A".equals(e.getValue()))
						{
							sql = sql + " and (" + columnName + "='" + e.getValue() + "' or " + columnName + " = 'PRE001' )";
						}else{
							if("STATUS".equals(columnName) || "USERTYPE".equals(columnName) || "ACCOUNTTYPE".equals(columnName) || "SPTYPE".equals(columnName))
							{
								sql = sql + " and " + columnName + "=" + e.getValue() + " ";
							}else
							{
								sql = sql + " and " + columnName + "='" + e.getValue() + "' ";
							}
						}
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
			//不分页，执行查询返回结果
			return findEntityListBySQL(Userdata.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(countSqlStr)
					.append(") A").toString();
			//分页，执行查询，返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(Userdata.class,
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
						if("STATUS".equals(columnName)  || "USERTYPE".equals(columnName) || "ACCOUNTTYPE".equals(columnName) || "SPTYPE".equals(columnName))
						{
							sql = sql + " and " + columnName + "=" + e.getValue() + " ";
						}else
						{
							sql = sql + " and " + columnName + "='" + e.getValue() + "' ";
						}
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
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(Userdata.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
}
