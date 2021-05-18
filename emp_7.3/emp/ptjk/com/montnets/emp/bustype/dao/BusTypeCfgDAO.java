package com.montnets.emp.bustype.dao;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.bustype.dao.i.IBusTypeCfgDAO;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.monitor.LfMonBusbase;
import com.montnets.emp.util.PageInfo;

public class BusTypeCfgDAO extends SuperDAO implements IBusTypeCfgDAO
{

	/**
	 * 获得业务监控基础信息表
	 * 
	 * @Description: TODO
	 * @param @param conditionMap
	 * @param @return
	 * @param @throws Exception
	 * @return List<LfMonBusbase>
	 */
	public List<LfMonBusbase> getLfMonBusbase(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{

		String sql = " select * ";
		String countSql = "select count(*) totalcount ";
		String baseSql = " from LF_MON_BUSBASE ";

		StringBuffer conditionSql = getConditionSql(conditionMap);

		String orderSql = " order by create_time DESC";
		sql += baseSql;
		countSql += baseSql;
		sql += tranferSql(conditionSql) + orderSql;
		countSql += tranferSql(conditionSql);

		// 查询数据
		List<LfMonBusbase> returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfMonBusbase.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		return returnList;
	}

	/***
	 * 拼装SQL查询条件
	 */
	public StringBuffer getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		StringBuffer conditionSql = new StringBuffer(" ");
		// 公司编码是有的，如果没有说明传值出现异常
		if(conditionMap.get("lgcorpcode") != null && !"".equals(conditionMap.get("lgcorpcode")))
		{
			conditionSql.append(" and corp_code='" + conditionMap.get("lgcorpcode") + "'");
		}
		// 业务名称
		if(conditionMap.get("busname") != null && !"".equals(conditionMap.get("busname")))
		{
			conditionSql.append(" and bus_name like '%" + conditionMap.get("busname") + "%'");
		}
		// 业务编码
		if(conditionMap.get("buscode") != null && !"".equals(conditionMap.get("buscode")))
		{
			conditionSql.append(" and bus_code='" + conditionMap.get("buscode") + "'");
		}
		// 监控状态
		if(conditionMap.get("status") != null && !"".equals(conditionMap.get("status")))
		{
			conditionSql.append(" and mon_state=" + conditionMap.get("status"));
		}

		IGenericDAO genericDao = new DataAccessDriver().getGenericDAO();
		// 开始时间
		if(conditionMap.get("sendtime") != null && !"".equals(conditionMap.get("sendtime")))
		{
			conditionSql.append(" and begin_time>=").append(genericDao.getTimeCondition(conditionMap.get("sendtime") + " 00:00:00")).append("");
		}
		// 结束时间
		if(conditionMap.get("recvtime") != null && !"".equals(conditionMap.get("recvtime")))
		{
			conditionSql.append(" and end_time<=").append(genericDao.getTimeCondition(conditionMap.get("recvtime") + " 23:59:59")).append("");
		}
		// 区域处理
		if(conditionMap.get("areaCodes") != null && !"".equals(conditionMap.get("areaCodes")))
		{
			StringBuffer areasql = new StringBuffer("");
			String areacode = conditionMap.get("areaCodes");
			areacode = areacode.substring(0, areacode.lastIndexOf(","));
			String code[] = areacode.split(",");
			if(code != null && code.length > 0)
			{
				for (int i = 0; i < code.length; i++)
				{
					if(i == 0)
					{
						areasql.append(" AREA_CODE like '%," + code[i].trim() + ",%' ");
					}
					else
					{
						areasql.append(" and AREA_CODE like '%," + code[i].trim() + ",%' ");
					}
				}
			}
			String otherSql = conditionSql.toString();
			StringBuffer condition = new StringBuffer(" ");

			// 将条件字符串首个and替换为where,不允许1 =1方式
			otherSql = otherSql.replaceFirst("and", " ");
			String temp = " and (" + otherSql + " and " + areasql + ") or ( " + otherSql + " and AREA_CODE='-1')";
			condition.append(temp.toString().replaceFirst("^(\\s*)(?i)and", "$1where"));
			return condition;

		}
		return conditionSql;
	}

	/**
	 * 获得省份信息
	 * 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> getProvinceList(String code)
	{
		String sql = "";
		List<DynaBean> gateAccoutFeeList = new ArrayList<DynaBean>();
		if(code == null)
		{
			sql = "select distinct  province,provincecode from A_PROVINCECITY order by provincecode ";
		}
		else if(code != null && !"".equals(code.trim()) && !"-1".equals(code.trim()))
		{
			code = code.trim();
			if(",".equals(code.substring(0, 1)) && ",".equals(code.substring(code.length() - 1, code.length())))
			{
				code = code.substring(1, code.length() - 1);
				if(!"".equals(code))
				{
					sql = "select pro.province,pro.provincecode from (select distinct  province,provincecode from A_PROVINCECITY ) pro where pro.provincecode in(" + code + ")";
				}
			}
		}
		if("".equals(sql))
		{
			return gateAccoutFeeList;
		}
		try
		{
			gateAccoutFeeList = getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过区域对象获取区域省份数据失败");
		}
		return gateAccoutFeeList;
	}

	/**
	 * 处理SQL条件
	 * 
	 * @param conSql
	 *        条件
	 * @return 处理后的条件
	 */
	public String tranferSql(StringBuffer Sql)
	{
		String conSql = Sql.toString();
		String conditionSql = "";
		try
		{
			// 存在查询条件
			if(conSql != null && conSql.length() > 0)
			{
				// 将条件字符串首个and替换为where,不允许1 =1方式
				conditionSql = conSql.replaceFirst("^(\\s*)(?i)and", "$1where");
			}
			return conditionSql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error("处理SQL条件异常，conSql:" + conSql);
			return null;
		}
	}
}
