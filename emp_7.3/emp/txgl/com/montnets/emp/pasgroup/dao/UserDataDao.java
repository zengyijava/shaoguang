package com.montnets.emp.pasgroup.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.servmodule.txgl.entity.Userdata;
import com.montnets.emp.servmodule.txgl.table.TableUserdata;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.util.PageInfo;

public class UserDataDao extends SuperDAO{
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
					} else if(eKey.contains("&&"))
					{
						sql = sql + " and " + columnName + "&" + e.getValue()
								+ " ";
					}  else {
						if("STATUS".equals(columnName)  || "USERTYPE".equals(columnName) 
								|| "ACCOUNTTYPE".equals(columnName) || "SPTYPE".equals(columnName)
								|| "FEEFLAG".equals(columnName))
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
//			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
//							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(Userdata.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	
	
	/**
	 * 查询SP账号
	 */
	public List<Userdata> findSpUser(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select * from " + TableUserdata.TABLE_NAME ;

		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库表的MAP映射
		Map<String, String> columns = getORMMap(Userdata.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			boolean isFirst = true;
			String concat;
			while (iter.hasNext()) {
				e = iter.next();
				if (!"".equals(e.getValue())) {
					String eKey = e.getKey();
					columnName = eKey.indexOf("&") < 0 ? columns.get(eKey)
							: columns.get(eKey
									.subSequence(0, eKey.indexOf("&")));
					concat = isFirst?" where ":" and ";
					isFirst = false;
					if (eKey.contains("&like")) {
						sql = sql + concat+"upper(" + columnName + ") like '%"
								+ e.getValue() + "%' ";
					} else if (eKey.contains("&>")) {
						sql = sql + concat + columnName + ">" + e.getValue()
								+ " ";
					}else if(eKey.contains("&<>"))
					{
						sql = sql + concat + columnName + "<>" + e.getValue()
						+ " ";
					}
					else 
					{
						if(e.getValue() != null && "loginId&=".equals(e.getKey()) && "WBS00A".equals(e.getValue()))
						{
							sql = sql + concat +"(" + columnName + "='" + e.getValue() + "' or " + columnName + " = 'PRE001' )";
						}else{
							if("STATUS".equals(columnName) || "USERTYPE".equals(columnName) || "ACCOUNTTYPE".equals(columnName) || "SPTYPE".equals(columnName))
							{
								sql = sql + concat + columnName + "=" + e.getValue() + " ";
							}else
							{
								sql = sql + concat + columnName + "='" + e.getValue() + "' ";
							}
						}
					}
				}
			}
		}
		/**
		 * 不显示代理账号
		 */
		String tgbsql=" and (userid <> loginid or userid='PRE001') ";
		sql=sql+tgbsql;
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
//			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(Userdata.class,
//							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(Userdata.class,
					sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
	
	
	public List<Userdata> findAgentUserdata() throws Exception {
		// select * from userdata where userid=loginid and usertype=0
		//拼接sql
		String sql = new StringBuffer("select * ").append(" from ").append(
				TableUserdata.TABLE_NAME).append("  where ").append(
				TableUserdata.USER_ID).append(" = ").append(
				TableUserdata.LOGIN_ID).append(" and ").append(
				TableUserdata.USER_TYPE).append(" = 0 ").toString();
		//执行查询
		List<Userdata> userdatas = findPartEntityListBySQL(Userdata.class, sql,
				StaticValue.EMP_POOLNAME);
		//返回结果
		return userdatas;
	}


	/**
	 * 获取是短信彩信富信
	 * @param type 0短信，1彩信，2富信
	 * @return
	 */
	public static String getConditionAccType(int type) throws EMPException {
		return getConditionAccType(type, false, null);
	}

	/**
	 * 获取是短信彩信富信
	 * @param type 0短信，1彩信，2富信
	 * @param alis 别名名称
	 * @return
	 */
	public static String getConditionAccType(int type, String alis) throws EMPException {
		return getConditionAccType(type, true, alis);
	}

	/**
	 * 获取是短信彩信富信
	 * @param type 0短信，1彩信，2富信
	 * @param flag 是否有别名
	 * @param alis 别名名称
	 * @return
	 */
	private static String getConditionAccType(int type, boolean flag, String alis) throws EMPException {
		StringBuilder sql = new StringBuilder();
		// 兼容不同数据库 1 oracle ; 2 SQL Server2005 ; 3 MySQL ; 4 DB2
		switch (StaticValue.DBTYPE) {
			case 1:
				if (type == 0) {
					sql.append(" AND ( BITAND(" + (flag ? alis + "." : "") + "ACCABILITY,1) = 1 or "+(flag ? alis + "." : "")+"ACCABILITY = 0)");
				}else if(type == 1) {
					sql.append(" AND BITAND(" + (flag ? alis + "." : "") + "ACCABILITY,2) = 2 ");
				}else if(type == 2) {
					sql.append(" AND BITAND(" + (flag ? alis + "." : "") + "ACCABILITY,4) = 4 ");
				}
				break;
			case 2:
				if (type == 0) {
					sql.append(" AND (" + (flag ? alis + "." : "") + "ACCABILITY&0x00000001 = 1 or "+(flag ? alis + "." : "")+"ACCABILITY = 0)");
				}else if(type == 1) {
					sql.append(" AND " + (flag ? alis + "." : "") + "ACCABILITY&0x00000002 = 2 ");
				}else if(type == 2) {
					sql.append(" AND " + (flag ? alis + "." : "") + "ACCABILITY&0x00000004 = 4 ");
				}
				break;
			case 3:
				if (type == 0) {
					sql.append(" AND (" + (flag ? alis + "." : "") + "ACCABILITY&0x00000001 = 1 or "+(flag ? alis + "." : "")+"ACCABILITY = 0)");
				}else if(type == 1) {
					sql.append(" AND " + (flag ? alis + "." : "") + "ACCABILITY&0x00000002 = 2 ");
				}else if(type == 2) {
					sql.append(" AND " + (flag ? alis + "." : "") + "ACCABILITY&0x00000004 = 4 ");
				}
				break;
			case 4:
				if (type == 0) {
					sql.append(" AND ( BITAND(" + (flag ? alis + "." : "") + "ACCABILITY,1) = 1 or "+(flag ? alis + "." : "")+"ACCABILITY = 0)");
				}else if(type == 1) {
					sql.append(" AND BITAND(" + (flag ? alis + "." : "") + "ACCABILITY,2) = 2 ");
				}else if(type == 2) {
					sql.append(" AND BITAND(" + (flag ? alis + "." : "") + "ACCABILITY,4) = 4 ");
				}
				break;
			default:
                throw new EMPException("DAO层-UserData数据查询异常,无法识别的数据库类型！");
		}
		return sql.toString();
	}
	
	
	
	
}
