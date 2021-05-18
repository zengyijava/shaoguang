package com.montnets.emp.servmodule.txgl.blacklist.dao;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasroute.GtPortUsed;
import com.montnets.emp.entity.passage.XtGateQueue;
import com.montnets.emp.table.pasroute.TableGtPortUsed;
import com.montnets.emp.table.pasroute.TableLfMmsAccbind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.passage.TableXtGateQueue;
import com.montnets.emp.util.PageInfo;

/**
 * 黑名单dao
 * @author zhangmin
 *
 */
public class BlackListDAO extends SuperDAO {
	
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
	
	/**
	 * //获取通道号
	 * @param corpCode
	 * @return
	 * @throws Exception
	 */
	public List<GtPortUsed> findMtSpgateByCorp(String corpCode)
	throws Exception {
		//拼接sql
		String sql = new StringBuffer("select distinct ").append(
				TableGtPortUsed.SPGATE).append(",").append(
				TableGtPortUsed.SPISUNCM).append(" from ").append(
				TableGtPortUsed.TABLE_NAME).append(" where ").append(
				TableGtPortUsed.USER_ID).append("<>").append(
				TableGtPortUsed.LOGIN_ID).append(" and ").append(
				TableGtPortUsed.ROUTE_FLAG).append(" in (0,1)").append(
				" and ").append(TableGtPortUsed.USER_ID).append(" in ").append(
				"(select spuser from " + TableLfSpDepBind.TABLE_NAME + "  where "
						+ TableLfSpDepBind.CORP_CODE + "='" + corpCode + "' and "
						+ TableLfSpDepBind.IS_VALIDATE + "=1)").toString();
		//执行查询
		List<GtPortUsed> returnList = findPartEntityListBySQL(GtPortUsed.class,
				sql, StaticValue.EMP_POOLNAME);
		//返回结果
		return returnList;
	}
	
	/**
	 * 获取企业黑名单总数
	 * @description    
	 * @param corpCode 企业编码
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 上午09:52:41
	 */
	public List<DynaBean> getCorpBlackListCount(String corpCode)
	{
		String sql = "SELECT COUNT(ID) AS MCOUNT FROM PB_LIST_BLACK WHERE OPTYPE=1 AND BLTYPE=1 AND CORPCODE='"+corpCode+"'";
		
		return getListDynaBeanBySql(sql);
	}

}
