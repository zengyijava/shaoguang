package com.montnets.emp.weix.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.weix.LfWcRimg;
import com.montnets.emp.entity.weix.LfWcTemplate;
import com.montnets.emp.table.weix.TableLfWcTemplate;
import com.montnets.emp.table.weix.TableLfWcTlink;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.common.util.GlobalMethods;
import com.montnets.emp.weix.dao.i.ITempleDao;

/**
 * @author chensj
 */
public class TempleDao extends SuperDAO implements ITempleDao
{

	static Map	map	= new HashMap<String, String>();

	static
	{
		//文本
		map.put("1", "(0)");
		//单图
		map.put("2", "(1)");
		//多图
		map.put("3", "(2)");
	}

	/**
	 * 查询图文模板信息
	 * @description    
	 * @param conditionMap
	 * @param pageInfo
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:11:45
	 */
	public List<DynaBean> getBaseInfos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		List<DynaBean> beans = null;
		String fieldSql = "SELECT temp.t_id,temp.t_name,temp.msg_text,temp.msg_type,temp.a_id,temp.corp_code,temp.createtime,temp.key_wordsvo wordnames,accout.name accoutname ";

		String tableSql = " from LF_WC_TEMPLATE temp left join LF_WC_ACCOUNT accout" + StaticValue.getWITHNOLOCK() + " on temp.a_id=accout.a_id ";

		String conditionSql = getConditionSql(conditionMap);
		//条件不符合
		if(conditionSql == null)
		{
			return null;
		}
		String orderbySql = " order by t_id DESC";
		String sql = fieldSql + tableSql + conditionSql + orderbySql;
		String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conditionSql).toString();
		List<String> timeList = getTimeCondition(conditionMap);
		beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		return beans;
	}

	/**
	 * 查询关键字
	 * @description    
	 * @param tid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:12:35
	 */
	public String getkeyname(String tid)
	{
		String re = "";
		if(!GlobalMethods.isInvalidString(tid))
		{
			String sql = "select name,type from LF_WC_KEYWORD where k_id in (select k_id from LF_WC_TLINK where t_id =" + tid + ")";
			List<DynaBean> names = getListDynaBeanBySql(sql);

			if(names != null && names.size() > 0)
			{
				StringBuilder sb = new StringBuilder();
				for (DynaBean name : names)
				{
					sb.append(name.get("name") + ":" + name.get("type") + ",");
				}
				re = sb.substring(0, sb.length() - 1);
			}
		}
		return re;
	}

	/**
	 * 获得图文id与名称
	 * @description    
	 * @param rids
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:13:00
	 */
	public String getimgidnames(String[] rids)
	{
		String re = "";
		String sql;
		List<DynaBean> names;
		DynaBean ban;
		StringBuilder sb = new StringBuilder();
		if(!GlobalMethods.isNullStrArray(rids))
		{
			for (int i = 1; i < rids.length; i++)
			{
				sql = "select t_id,t_name from LF_WC_TEMPLATE where MSG_TYPE =1 and RIMG_IDS = '" + rids[i] + "'";
				names = getListDynaBeanBySql(sql);
				if(names != null)
				{
					ban = names.get(0);
					sb.append(ban.get("t_id") + "-" + ban.get("t_name") + ",");
					re = sb.substring(0, sb.length() - 1);
				}
			}
		}
		return re;
	}

	
	/**
	 * 查询回复模板条件拼装
	 */
	public String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		String buffer = new String("");
		// 企业编码
		if(conditionMap.get("lgcorpcode") != null && !"".equals(conditionMap.get("lgcorpcode")))
		{
			buffer = buffer + "where temp.CORP_CODE = '" + conditionMap.get("lgcorpcode") + "'";
		}
		else
		{
			//不存在企业编码
			EmpExecutionContext.error("查询回复模板条件拼装异常，企业编码为空！");
			return null;
		}
		if(conditionMap.get("tempType") != null)
		{
			if(!"0".equals(conditionMap.get("tempType")))
			{
				buffer = buffer + " and temp.msg_type in " + map.get(conditionMap.get("tempType"));
			}

		}
		if(conditionMap.get("templename") != null)
		{
			buffer = buffer + " and temp.t_name like'%" + conditionMap.get("templename") + "%'";
		}

		if(!GlobalMethods.isInvalidString(conditionMap.get("tempid")))
		{
			buffer = buffer + " and temp.t_id !=" + conditionMap.get("tempid");
		}
		if(conditionMap.get("accoutid") != null)
		{
			if("0".equals(conditionMap.get("accoutid")))
			{
				buffer = buffer + " and (temp.a_id = 0 OR temp.a_id IS NULL) ";
			}
			else
			{
				buffer = buffer + " and temp.a_id =" + conditionMap.get("accoutid");
			}
		}

		if(conditionMap.get("serkey") != null)
		{
			buffer = buffer + " and temp.key_wordsvo like'%" + conditionMap.get("serkey") + "%'";
		}

		if(conditionMap.get("serReply") != null)
		{
			buffer = buffer + " and temp.msg_text like'%" + conditionMap.get("serReply") + "%'";
		}

		if(conditionMap.get("startdate") != null)
		{
			buffer = buffer + " and temp.createtime >=?";
		}
		if(conditionMap.get("enddate") != null)
		{
			buffer = buffer + " and temp.createtime <=?";
		}
		return buffer;
	}

	private List<String> getTimeCondition(LinkedHashMap<String, String> conditionMap)
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

	/**
	 * 获取图文对象
	 * 
	 * @param ids
	 * @return
	 */
	public static List<LfWcRimg> getLfWcRimgbyids(String ids)
	{
		String[] items = ids.split(",");
		BaseBiz baseBiz = new BaseBiz();
		List<LfWcRimg> rimgItemList = new ArrayList<LfWcRimg>();
		LfWcRimg i;
		try
		{
			for (String item : items)
			{
				i = baseBiz.getById(LfWcRimg.class, item);
				if(i!=null)
				{
					rimgItemList.add(i);
				}
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过ids获取图文对象失败！");
		}
		return rimgItemList;
	}

	/**
	 * 通过模板ID获取图文ID集合
	 * 
	 * @param imgids
	 * @return
	 */
	public static String getimgids(String imgids)
	{
		BaseBiz baseBiz = new BaseBiz();
		String[] imgs = imgids.trim().split(",");
		StringBuilder sb = new StringBuilder();
		LfWcTemplate lt;
		try
		{
			for (String s : imgs)
			{
				lt = baseBiz.getById(LfWcTemplate.class, s);
				sb.append(lt.getRimgids() + ",");
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通过图文编号失败！");
		}
		return sb.toString();
	}

	/**
	 * 模板对应的图片地址
	 * 
	 * @param tid
	 * @return
	 */
	public static List<String> isurlbyexit(String tid)
	{
		BaseBiz baseBiz = new BaseBiz();
		List<String> list = new ArrayList<String>();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("TId", tid);
		try
		{
			LfWcTemplate delt = baseBiz.getByCondition(LfWcTemplate.class, conditionMap, null).get(0);
			if(!GlobalMethods.isInvalidString(delt.getRimgids()))
			{
				String[] rimid = delt.getRimgids().split(",");
				LfWcRimg ri;
				for (String s : rimid)
				{
					ri = baseBiz.getById(LfWcRimg.class, s);
					list.add(ri.getPicurl());
				}
				return list;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "模板对应的图片地址异常！");
		}
		return list;
	}

	/**
	 * 删除list中不包含listnew的图片地址
	 * 
	 * @param list
	 * @param listnew
	 */
	public static void delbyexit(List<String> list, List<String> listnew)
	{
		for (int i = 0; i < listnew.size(); i++)
		{
			if(list.contains(listnew.get(i)))
			{
				list.remove(listnew.get(i));
			}
		}
		String delurl = "";
		for (int i = 0; i < list.size(); i++)
		{
			delurl = list.get(i).toString().replaceAll(".*p_weix/", "");
			GlobalMethods.deleteFile(delurl);
		}

	}
	
	public static void ideltesRimgs(String tid, IEmpTransactionDAO empTransDao, Connection conn) throws Exception
	{
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("TId", tid);
		LfWcTemplate delt = baseBiz.getByCondition(LfWcTemplate.class, conditionMap, null).get(0);
		if(!GlobalMethods.isInvalidString(delt.getRimgids()))
		{
			int reint = empTransDao.delete(conn, LfWcRimg.class, delt.getRimgids());
			if(reint <= 0)
			{
				throw new RuntimeException("删除对象失败了，事务回滚！");
			}

		}
	}

	/**
	 * 获取回复模板（默认回复和关注时回复用到）
	 * 2013-7-30
	 * List<DynaBean>
	 * zm
	 * 
	 * @throws Exception
	 */
	public List<DynaBean> getBaseTempInfos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception
	{
		List<DynaBean> beans = null;
		try
		{
			String fieldSql = TempleDaoSQL.getfFieldSql();

			String tableSql = TempleDaoSQL.getTableSql();

			String conditionSql = TempleDaoSQL.getConditionSql(conditionMap);
			//条件不符合
			if(conditionSql == null)
			{
				return null;
			}
			String orderbySql = " order by template.t_id DESC";
			String sql = fieldSql + tableSql + conditionSql + orderbySql;
			String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conditionSql).toString();
			List<String> timeList = TempleDaoSQL.getTimeCondition(conditionMap);
			beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e,"获取回复模板失败！");
			throw e;
		}
		return beans;
	}

	/**
	 * 关键字ID找到它对于的模板
	 * 
	 * @param kId
	 * @return
	 */
	public List<DynaBean> findTemplateByKeywordId(String kId) throws Exception
	{
		PageInfo pageInfo = new PageInfo();
		String fieldSql = "select lfTlink.*,lfTemplate.MSG_XML";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWcTlink.TABLE_NAME).append(" lfTlink ");
		tableSql.append(" LEFT JOIN ").append(TableLfWcTemplate.TABLE_NAME).append(" lfTemplate ");
		tableSql.append(" ON lfTlink.T_ID = lfTemplate.T_ID ").append(" where ");

		StringBuffer conSql = new StringBuffer();
		conSql.append(TableLfWcTlink.K_ID).append("=").append(kId);

		String sql = fieldSql + tableSql.toString() + conSql.toString();
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
}
