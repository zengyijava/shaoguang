package com.montnets.emp.menuParam.dao;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.tailmanage.GwMsgtail;
import com.montnets.emp.entity.tailmanage.GwTailctrl;
import com.montnets.emp.table.pasgrpbind.TableLfAccountBind;
import com.montnets.emp.table.pasroute.TableLfSpDepBind;
import com.montnets.emp.table.tailmanage.TableGwMsgtail;
import com.montnets.emp.table.tailmanage.TableGwTailbind;
import com.montnets.emp.table.tailmanage.TableGwTailctrl;
import com.montnets.emp.util.PageInfo;

public class MenuParamConfigDAO extends SuperDAO{

	public List<LfSpDepBind> getSpDepBindWhichUserdataNoBind(LinkedHashMap<String, String> conditionMap,
			LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo)
			throws Exception {
		//拼接sql
		String sql = "select * from " + TableLfSpDepBind.TABLE_NAME + "  where "
				+ TableLfSpDepBind.SP_USER + " not in (select "+TableLfAccountBind.SPUSERID+" from " + TableLfAccountBind.TABLE_NAME
				+ "  where " + TableLfAccountBind.CORP_CODE + "='"+conditionMap.get("corpCode")+"')";

		Iterator<Map.Entry<String, String>> iter = null;
		//获取实体类与数据库字段的MAP映射
		Map<String, String> columns = getORMMap(LfSpDepBind.class);
		Map.Entry<String, String> e = null;
		if (conditionMap != null && !conditionMap.entrySet().isEmpty()) {
			iter = conditionMap.entrySet().iterator();
			String columnName = null;
			//获取实体类属性
			Field[] fields = LfSpDepBind.class.getDeclaredFields();
			//属性类型
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
						//是否是日期类型
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
			//不分页，执行查询，返回结果
			return findEntityListBySQL(LfSpDepBind.class, sql,
					StaticValue.EMP_POOLNAME);
		} else {
			//记录数sql
			String countSql = new StringBuffer(
					"select count(*) totalcount from (").append(sql)
					.append(")").toString();
			//分页，执行查询，返回结果
			return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfSpDepBind.class,
							sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
	}
    
	public List<GwMsgtail> getMsgtailList(String lgcorpcode) throws Exception {
		StringBuffer sqlSb = new StringBuffer();
		String sql = sqlSb.append("SELECT * FROM ").append(TableGwMsgtail.TABLE_NAME).append(" m WHERE m.")
		.append(TableGwMsgtail.CORP_CODE).append(" = '").append(lgcorpcode).append("' AND m.")
		.append(TableGwMsgtail.TAIL_ID).append(" IN ( ").append("SELECT b.")
		.append(TableGwTailbind.TAIL_ID).append(" FROM ").append(TableGwTailbind.TABLE_NAME)
		.append(" b WHERE b.").append(TableGwTailbind.CORP_CODE).append(" = '").append(lgcorpcode)
		.append("' AND b.").append(TableGwTailbind.TAIL_TYPE).append(" = 0 )").toString();
		List<GwMsgtail> msgtailList = findEntityListBySQL(GwMsgtail.class,sql,StaticValue.EMP_POOLNAME);
		return msgtailList;
		
	}

	public List<GwTailctrl> getTailctrlList(String corpCode) throws Exception {
		StringBuffer sqlSb = new StringBuffer();
		String sql = sqlSb.append("SELECT * FROM ").append(TableGwTailctrl.TABLE_NAME).append(" WHERE ")
		.append(TableGwTailctrl.CORP_CODE).append(" = '").append(corpCode).append("'").toString();
		List<GwTailctrl> tailctrlList = findEntityListBySQL(GwTailctrl.class,sql,StaticValue.EMP_POOLNAME);
		return tailctrlList;
	}
	
}
