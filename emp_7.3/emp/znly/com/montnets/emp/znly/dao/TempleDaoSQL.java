package com.montnets.emp.znly.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiTemplate;

public class TempleDaoSQL
{
	
	/***
	 * 增加查询条件
	* @Description: TODO
	* @param @param conditionMap
	* @param @return
	* @return String
	 */
	public static String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		LinkedHashMap<String, String> tempTypeMap = new LinkedHashMap<String, String>();
		tempTypeMap.put("1", "(0)");// 文本
		tempTypeMap.put("2", "(1)");// 单图文
		tempTypeMap.put("3", "(2)");// 多图文
		tempTypeMap.put("23", "(1,2)");// 图文
		StringBuffer buffer = new StringBuffer("");
		if(conditionMap.get("tempType") != null)
		{
			if(!"0".equals(conditionMap.get("tempType")))
			{
				buffer.append(" and template.msg_type in ").append(tempTypeMap.get(conditionMap.get("tempType")));
			}

		}

		// 企业编码
		if(conditionMap.get("corpCode") != null && !"".equals(conditionMap.get("corpCode")))
		{
			buffer.append(" and template.corp_code='").append(conditionMap.get("corpCode")).append("'");
		}
		if(conditionMap.get("startdate") != null)
		{
			buffer.append(" and template.createtime >=?");
		}
		if(conditionMap.get("enddate") != null)
		{
			buffer.append(" and template.createtime <=?");
		}
		return buffer.toString().replaceFirst("^(\\s*)(?i)and", "$1where");

	}
	
	/***
	 *查询语句 
	* @Description: TODO
	* @param @return
	* @return String
	 */
	public static String getfFieldSql()
	{
		String fieldSql = "SELECT template.t_id,template.t_name,template.msg_text,template.msg_type,template.a_id,template.corp_code,template.createtime,accout.name accoutname,template.key_wordsvo ";
		return fieldSql;
	}
	
	/***
	 * 表名语句
	* @Description: TODO
	* @param @return
	* @return String
	 */
	public static String getTableSql()
	{
		String tableSql = " from "+ TableLfWeiTemplate.TABLE_NAME+" template " + StaticValue.getWITHNOLOCK() + " left join "+ TableLfWeiAccount.TABLE_NAME +" accout" + StaticValue.getWITHNOLOCK() + " on template.a_id=accout.a_id";
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
