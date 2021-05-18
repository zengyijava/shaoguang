package com.montnets.emp.weix.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.dao.i.IDefaultrepDao;

public class DefaultrepDao extends SuperDAO implements IDefaultrepDao
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

		String tableSql = " from LF_WC_RTEXT rtext " + StaticValue.getWITHNOLOCK() + " left join LF_WC_ACCOUNT accout" + StaticValue.getWITHNOLOCK() + " on rtext.a_id=accout.a_id ";
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{

			if(conditionMap.get("corpCode") != null && !"".equals(conditionMap.get("corpCode")))
			{
				conSql.append(" where rtext.corp_code='").append(conditionMap.get("corpCode")).append("'");
			}
			else
			{
				EmpExecutionContext.error("默认回复查询失败，企业编码为空！");
				return null;
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
		// 排序
		String orderbySql = " order by rtext.TET_ID DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;

		// 分页和不分页两种情况
		if(pageInfo == null)
		{
			return this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
			return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
	}

}
