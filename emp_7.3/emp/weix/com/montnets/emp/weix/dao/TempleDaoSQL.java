package com.montnets.emp.weix.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;

public class TempleDaoSQL
{

	public static String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		LinkedHashMap<String, String> tempTypeMap = new LinkedHashMap<String, String>();
		tempTypeMap.put("1", "(0)");// 文本
		tempTypeMap.put("2", "(1)");// 单图文
		tempTypeMap.put("3", "(2)");// 多图文
		StringBuffer buffer = new StringBuffer();
		// 企业编码
		if(conditionMap.get("corpCode") != null && !"".equals(conditionMap.get("corpCode")))
		{
			buffer.append(" where template.corp_code='").append(conditionMap.get("corpCode")).append("'");
		}
		else
		{
			EmpExecutionContext.error("查询模板拼接条件异常，企业编码为空！");
			return null;
		}
		if(conditionMap.get("tempType") != null)
		{
			if(!"0".equals(conditionMap.get("tempType")))
			{
				buffer.append(" and template.msg_type in ").append(tempTypeMap.get(conditionMap.get("tempType")));
			}

		}
		if(conditionMap.get("startdate") != null)
		{
			buffer.append(" and template.createtime >=?");
		}
		if(conditionMap.get("enddate") != null)
		{
			buffer.append(" and template.createtime <=?");
		}
		return buffer.toString();

	}

	public static String getfFieldSql()
	{
		String fieldSql = "SELECT template.t_id,template.t_name,template.msg_text,template.msg_type,template.a_id,template.corp_code,template.createtime,accout.name accoutname,template.key_wordsvo ";
		return fieldSql;
	}

	public static String getTableSql()
	{
		String tableSql = " from LF_WC_TEMPLATE template " + StaticValue.getWITHNOLOCK() + " left join LF_WC_ACCOUNT accout" + StaticValue.getWITHNOLOCK() + " on template.a_id=accout.a_id";
		// +" left join LF_WC_TLINK tlink "
		// + StaticValue.WITHNOLOCK +
		// " on template.t_id=tlink.t_id left join LF_WC_KEYWORD keyword"
		// + StaticValue.WITHNOLOCK +" on tlink.k_id=keyword.k_id ";

		return tableSql;
	}

	/**
	 * 时间条件
	 * 
	 * @param conditionMap
	 * @return
	 */
	public static List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
	{
		List<String> timeList = new ArrayList<String>();
		if(conditionMap.get("startdate") != null)
		{
			timeList.add(conditionMap.get("startdate"));
		}
		if(conditionMap.get("enddate") != null)
		{
			timeList.add(conditionMap.get("enddate"));
		}
		return timeList;
	}

}
