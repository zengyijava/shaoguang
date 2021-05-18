package com.montnets.emp.wxgl.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiRtext;
import com.montnets.emp.util.PageInfo;

public class DefaultrepDao extends SuperDAO
{
	/**
	 * 默认回复查询(分页和不分页两种情况)
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findDefaltReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		// 拼SQL语句
		String fieldSql = "SELECT rtext.tet_id,rtext.msg_text,rtext.t_id,rtext.tet_type,rtext.a_id,rtext.corp_code,rtext.createtime," + "rtext.title,rtext.msg_xml,rtext.modifytime,accout.name accoutname,accout.code code";

		String tableSql = " from "+TableLfWeiRtext.TABLE_NAME+" rtext " + StaticValue.getWITHNOLOCK() + " left join "+TableLfWeiAccount.TABLE_NAME+" accout" + StaticValue.getWITHNOLOCK() + " on rtext.a_id=accout.a_id ";
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{

			if(conditionMap.get("corpCode") != null && !"".equals(conditionMap.get("corpCode")))
			{
				conSql.append(" and rtext.corp_code='").append(conditionMap.get("corpCode")).append("'");
			}

			if(conditionMap.get("title") != null && !"".equals(conditionMap.get("title")))
			{
				conSql.append(" and rtext.title like '%" + conditionMap.get("title") + "%' ");
			}

			if(conditionMap.get("a_id") != null && !"".equals(conditionMap.get("a_id")))
			{
				conSql.append(" and rtext.a_id = " + conditionMap.get("a_id"));
			}

			if(conditionMap.get("tet_id") != null && !"".equals(conditionMap.get("tet_id")))
			{
				conSql.append(" and rtext.tet_id = " + conditionMap.get("tet_id"));
			}

		}
		String condition="";
		//存在查询条件
		if(conSql != null && conSql.length() > 0)
		{
			//将条件字符串首个and替换为where,
			condition = conSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		}
		// 排序
		String orderbySql = " order by rtext.TET_ID DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + condition + orderbySql;

		// 分页和不分页两种情况
		if(pageInfo == null)
		{
			return this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
			return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
	}

}
