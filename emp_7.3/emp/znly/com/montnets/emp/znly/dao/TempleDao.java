package com.montnets.emp.znly.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.wxgl.LfWeiRimg;
import com.montnets.emp.entity.wxgl.LfWeiTemplate;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.util.GlobalMethods;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiKeyword;
import com.montnets.emp.table.wxgl.TableLfWeiTemplate;
import com.montnets.emp.table.wxgl.TableLfWeiTlink;
import com.montnets.emp.util.PageInfo;

/**
 * @author chensj
 */
public class TempleDao extends SuperDAO
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

		String tableSql = " from "+TableLfWeiTemplate.TABLE_NAME+" temp left join "+TableLfWeiAccount.TABLE_NAME+" accout" + StaticValue.getWITHNOLOCK() + " on temp.a_id=accout.a_id ";

		String conditionSql = getConditionSql(conditionMap);
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
			String sql = "select name,type from "+ TableLfWeiKeyword.TABLE_NAME+" where k_id in (select k_id from "+ TableLfWeiTlink.TABLE_NAME +" where t_id =" + tid + ")";
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
				sql = "select t_id,t_name from "+TableLfWeiTemplate.TABLE_NAME+" where MSG_TYPE =1 and RIMG_IDS = '" + rids[i] + "'";
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

	public String getConditionSql(LinkedHashMap<String, String> conditionMap)
	{
		String buffer = new String("");
		if(conditionMap.get("tempType") != null)
		{
			if(!"0".equals(conditionMap.get("tempType")))
			{
				buffer = buffer + " and temp.msg_type in " + map.get(conditionMap.get("tempType"));
			}

		}
		// 企业编码
		if(conditionMap.get("lgcorpcode") != null && !"".equals(conditionMap.get("lgcorpcode")))
		{
			buffer = buffer + " and temp.CORP_CODE = '" + conditionMap.get("lgcorpcode") + "'";
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
		return buffer.replaceFirst("^(\\s*)(?i)and", "$1where");
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
	public static List<LfWeiRimg> getLfWcRimgbyids(String ids)
	{
		String[] items = ids.split(",");
		BaseBiz baseBiz = new BaseBiz();
		List<LfWeiRimg> rimgItemList = new ArrayList<LfWeiRimg>();
		LfWeiRimg i;
		try
		{
			for (String item : items)
			{
				i = baseBiz.getById(LfWeiRimg.class, item);
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
		LfWeiTemplate lt;
		try
		{
			for (String s : imgs)
			{
				lt = baseBiz.getById(LfWeiTemplate.class, s);
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
			LfWeiTemplate delt = baseBiz.getByCondition(LfWeiTemplate.class, conditionMap, null).get(0);
			if(!GlobalMethods.isInvalidString(delt.getRimgids()))
			{
				String[] rimid = delt.getRimgids().split(",");
				LfWeiRimg ri;
				for (String s : rimid)
				{
					ri = baseBiz.getById(LfWeiRimg.class, s);
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
	
	/**
	 * 删除关键字回复模板关联的图文 			 
	 * @author foyoto <foyoto@gmail.com>
	 * @datetime 2014年6月10日 下午2:37:03
	 */
	public static int ideltesRimgs(String tid, IEmpTransactionDAO empTransDao, Connection conn) throws Exception
	{
	    int reint = 0;
		BaseBiz baseBiz = new BaseBiz();
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("TId", tid);
		LfWeiTemplate delt = baseBiz.getByCondition(LfWeiTemplate.class, conditionMap, null).get(0);
		if(!GlobalMethods.isInvalidString(delt.getRimgids()))
		{
			reint = empTransDao.delete(conn, LfWeiRimg.class, delt.getRimgids());
		}
        return reint;
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
			String orderbySql = " order by template.t_id DESC";
			String sql = fieldSql + tableSql + conditionSql + orderbySql;
			String countSql = new StringBuffer("select count(*) totalcount ").append(tableSql).append(conditionSql).toString();
			List<String> timeList = TempleDaoSQL.getTimeCondition(conditionMap);
			beans = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL
(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, timeList);
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
	 * select lfTlink.*, lfTemplate.MSG_XML FROM LF_WEI_TLINK lfTlink LEFT JOIN
	 * LF_WEI_TEMPLATE lfTemplate ON lfTlink.T_ID = lfTemplate.T_ID where
	 * lfTlink.K_ID ='20'
	 * 
	 * @param kId
	 * @return
	 */
	public List<DynaBean> findTemplateByKeywordId(String kId) throws Exception
	{
		PageInfo pageInfo = new PageInfo();
		String fieldSql = "select lfTlink.*,lfTemplate.MSG_XML";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWeiTlink.TABLE_NAME).append(" lfTlink ");
		tableSql.append(" LEFT JOIN ").append(TableLfWeiTemplate.TABLE_NAME).append(" lfTemplate ");
		tableSql.append(" ON lfTlink.T_ID = lfTemplate.T_ID ");

		StringBuffer conSql = new StringBuffer();
		conSql.append(" where ").append(TableLfWeiTlink.K_ID).append("=").append(kId);

		String sql = fieldSql + tableSql.toString() + conSql.toString();
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
}
