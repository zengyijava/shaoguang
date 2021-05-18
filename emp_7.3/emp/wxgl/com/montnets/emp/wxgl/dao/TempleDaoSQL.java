package com.montnets.emp.wxgl.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiTemplate;

public class TempleDaoSQL
{

	public static String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		LinkedHashMap<String, String> tempTypeMap = new LinkedHashMap<String, String>();
		tempTypeMap.put("1", "(0)");// 文本
		tempTypeMap.put("2", "(1)");// 单图文
		tempTypeMap.put("3", "(2)");// 多图文
		tempTypeMap.put("23", "(1,2)");// 图文
		StringBuffer buffer = new StringBuffer();
		if(conditionMap.get("tempType") != null)
		{
			if(!"0".equals(conditionMap.get("tempType")))
			{
				buffer.append(" and template.msg_type in ").append(tempTypeMap.get(conditionMap.get("tempType")));
			}

		}
		if(conditionMap.get("nogif") != null && !"".equals(conditionMap.get("nogif")))
		{
			buffer.append(" and template.msg_xml not like '").append("%.gif]]></PicUrl>%'");
			buffer.append(" and template.msg_xml not like '").append("%.jpeg]]></PicUrl>%'");
			buffer.append(" and template.msg_xml not like '").append("%.png]]></PicUrl>%'");
			buffer.append(" and template.msg_xml not like '").append("%.bmp]]></PicUrl>%'");
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
		return buffer.toString();

	}

	public static String getfFieldSql()
	{
		String fieldSql = "SELECT template.t_id,template.t_name,template.msg_text,template.msg_type,template.a_id,template.corp_code,template.createtime,accout.name accoutname,template.key_wordsvo ";
		return fieldSql;
	}

	public static String getTableSql()
	{
		String tableSql = " from "+ TableLfWeiTemplate.TABLE_NAME+" template " + StaticValue.getWITHNOLOCK() + " left join "+ TableLfWeiAccount.TABLE_NAME +" accout" + StaticValue.getWITHNOLOCK() + " on template.a_id=accout.a_id";
		// +" left join LF_WEI_TLINK tlink "
		// + StaticValue.WITHNOLOCK +
		// " on template.t_id=tlink.t_id left join LF_WEI_KEYWORD keyword"
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
