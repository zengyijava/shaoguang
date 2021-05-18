package com.montnets.emp.engine.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * 上行业务记录dao
 * @project p_znyq
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-16 上午10:12:30
 * @description
 */
public class ProcessDao extends SuperDAO
{
	
	public List<DynaBean> getNetTemplate(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) {
		try
		{
			String sql = "select baseinfo.id,baseinfo.netid,baseinfo.name,baseinfo.CREATDATE,sysuser.user_name userName ";

			String baseSql = " from LF_WX_BASEINFO baseinfo left join " 
				+ TableLfSysuser.TABLE_NAME + " sysuser on baseinfo.CREATID = sysuser.USER_ID where baseinfo.CORP_CODE='"+conditionMap.get("corpCode")+"' ";
			String conditionSql = getTemplateCondition(conditionMap);
			String dominationSql = "";
			if(conditionMap.get("creatId") != null && !"0".equals(conditionMap.get("creatId")))
			{
				dominationSql = getDominationSql(conditionMap.get("creatId"));
			}

			// 增加 排序功能
			String ordersql = " order by baseinfo.id DESC";
			String countSql = "select count(baseinfo.id)totalcount " + baseSql + dominationSql + conditionSql;
			sql += baseSql + dominationSql + conditionSql + ordersql;
			List<String> timeList = getTimeCondition(conditionMap);
			List<DynaBean> list = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
			return list;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "智能引擎获取网讯模板异常。");
			return null;
		}
	}
	
	private String getTemplateCondition(LinkedHashMap<String, String> conditionMap){
		if(conditionMap == null || conditionMap.size() == 0){
			return "";
		}
		
		StringBuffer conditionSql = new StringBuffer(); 
		/*String corpCode = conditionMap.get("corpCode");
		conditionSql.append(" and baseinfo.CORP_CODE='").append(corpCode+"'");*/
		
		conditionSql.append(" and baseinfo.status in (2,4) ");
		
		if(conditionMap.get("timeOut") != null){
			conditionSql.append(" and baseinfo.timeout >?");
		}
		String name = conditionMap.get("name");
		if(name != null && !"".equals(name) ){
			conditionSql.append(" and baseinfo.name like '%").append(name).append("%'");
		}

		String tempType = conditionMap.get("tempType");
		if(tempType != null ){
			conditionSql.append(" and baseinfo.tempType =").append(tempType);
		}
		
		String operAppStatus = conditionMap.get("operAppStatus");
		//新增运营商审核状态的条件查询
		if(operAppStatus != null){
			conditionSql.append(" and baseinfo.OPERAPPSTATUS =").append(operAppStatus);
		}
		return conditionSql.toString();
	}

	//获取权限 
	public  String getDominationSql(String userId)
	{
		StringBuffer dominationSql = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(userId);
		String sql = new StringBuffer(" and (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(userId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(dominationSql).append(")")
				.append(")").toString();
		return sql;
	}
	
	private List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
	{
		List<String> timeList = new ArrayList<String>();
		
		if(conditionMap.get("timeOut") != null)
		{
			Timestamp timeout = new Timestamp(Long.parseLong(conditionMap.get("timeOut")));
			timeList.add(timeout.toString());
		}
		return timeList;
	}
	
}
