package com.montnets.emp.corpmanage.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.corpmanage.vo.LfSpCorpBindVo;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @project sinolife
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-19 下午07:39:20
 * @description
 */
public class GenericLfSpCorpBindVoDAO extends SuperDAO
{


	public List<LfSpCorpBindVo> findLfSpCorpBindVos(LfSpCorpBindVo lfSpCorpBindVo,
			PageInfo pageInfo) throws Exception {
		//查询字段
		String fieldSql = GenericLfSpCorpBindVoSQL.getFieldSql();
		//查询表名
		String tableSql = GenericLfSpCorpBindVoSQL.getTableSql();
		//查询条件
		String conditionSql = GenericLfSpCorpBindVoSQL
				.getConditionSql(lfSpCorpBindVo);
		//排序条件
		String orderBySql = GenericLfSpCorpBindVoSQL.getOrderBySql();
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				conditionSql).append(orderBySql).toString();
		//记录数
		String countSql = new StringBuffer("select count(*) totalcount")
				.append(tableSql).append(conditionSql).toString();
		List<LfSpCorpBindVo> returnList = new DataAccessDriver().getGenericDAO().findPageVoListBySQLNoCount(
						LfSpCorpBindVo.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
		return returnList;
	}


	public List<GtPortUsed> findUnBindDBUserID() throws Exception {
		//拼接sql
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
						"='DBS00A'").append(" and ").append(TableGtPortUsed.GATE_TYPE).append("= 1 ").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	public List<GtPortUsed> findUnBindEmpMMsUserID() throws Exception {
		//拼接sql
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfMmsAccbind.MMS_USER).append(" from ").append(
				TableLfMmsAccbind.TABLE_NAME).toString();
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ")
				.append(TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) ")
				.append(" and ").append(TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql).append(") ")
				.append(" and ").append(TableGtPortUsed.GATE_TYPE).append("= 2 ")
				.append(" order by ").append(TableGtPortUsed.USER_ID).append(" asc ")
				.toString();
		
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	/**
	 * 查询发送账号
	 */
	public List<GtPortUsed> findAllEmpUserID() throws Exception {
		//拼接sql
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
		//返回结果
		return returnList;
	}
	
	public List<GtPortUsed> findAllDBServerUserID() throws Exception {
		//拼接sql
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
		//返回结果
		return returnList;
	}

	public List<GtPortUsed> findUnBindEmpUserID() throws Exception {
		//拼接sql
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
						"='WBS00A'").append(" and ").append(TableGtPortUsed.GATE_TYPE).append("= 1 ").append(" order by ").append(
						TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}

	/**
	 * 获取EMP账号
	 * @description    
	 * @param spType 1：EMP应用账号；2：EMP接入账号
	 * @return
	 * @throws Exception       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-7-18 下午07:16:51
	 */
	public List<GtPortUsed> findUnBindEmpUserID(int spType) throws Exception {
		//拼接sql
		String bindSpSql = new StringBuffer("select distinct ").append(
				TableLfSpDepBind.SP_USER).append(" from ").append(
				TableLfSpDepBind.TABLE_NAME).append(" where ").append(
				TableLfSpDepBind.PLATFORM_TYPE).append("=").append(spType).toString();
		String sql = new StringBuffer("SELECT USERID FROM USERDATA ").append("WHERE SPTYPE = ").append(spType)
				.append(" AND USERID IN (").append("select distinct ").append(
				TableGtPortUsed.USER_ID).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1) and ").append(
				TableGtPortUsed.USER_ID).append(" not in (").append(bindSpSql)
				.append(") and ").append(TableGtPortUsed.LOGIN_ID).append(
						"='WBS00A'").append(" and ").append(TableGtPortUsed.GATE_TYPE).append("= 1 ").append(")")
						.append(" order by ").append(TableGtPortUsed.USER_ID).append(" asc").toString();
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	public List<LfSysuser> findDomSysuserByDepID(String depID,
			LinkedHashMap<String, String> orderbyMap) throws Exception {
		//拼接sql
		String sql = "select sysuser.* from " + TableLfSysuser.TABLE_NAME
				+ " sysuser inner join " + TableLfDomination.TABLE_NAME
				+ " domination on sysuser." + TableLfSysuser.USER_ID
				+ "=domination." + TableLfDomination.USER_ID
				+ " where domination." + TableLfDomination.DEP_ID + "=" + depID;
		//排序条件拼接
		StringBuffer orderSqlSb = new StringBuffer();
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
		//返回结果
		return findEntityListBySQL(LfSysuser.class, sql,
				StaticValue.EMP_POOLNAME);
	}
}
