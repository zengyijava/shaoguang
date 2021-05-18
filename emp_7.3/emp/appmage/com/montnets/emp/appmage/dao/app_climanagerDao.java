package com.montnets.emp.appmage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.appmage.TableLfAppMwClient;
import com.montnets.emp.table.appmage.TableLfAppMwGroup;
import com.montnets.emp.util.PageInfo;

public class app_climanagerDao extends SuperDAO
{
	/**
	 * 查询APP客户
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> query(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		//关联关系 变化为LF_APP_MW_GPMEM 的GM_USER=lfMwClient的WC_ID
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		// 拼SQL语句
		String fieldSql = "SELECT lfMwClient.*,totl.gp_name as mwgroupname,totl.g_id as gid";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfAppMwClient.TABLE_NAME).append(" lfMwClient  left join " +
				" (SELECT gpmenm.GM_USER,gpmenm.g_id,mwgroup.name gp_name from LF_APP_MW_GPMEM gpmenm left join LF_APP_MW_GROUP mwgroup on " +
				" gpmenm.G_ID=mwgroup.g_id where  gpmenm.GM_STATU=1 )  totl  on totl.GM_USER=lfMwClient.WC_ID  ");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		conSql.append("  WHERE lfMwClient.UTYPE=4 ");
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			if(conditionMap.get("nickname") != null && !"".equals(conditionMap.get("nickname")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.NICK_NAME + " like '%" + conditionMap.get("nickname") + "%' ");
			}
			if(conditionMap.get("sex") != null && !"".equals(conditionMap.get("sex")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.SEX + " = '" + conditionMap.get("sex") + "' ");
			}
			if(conditionMap.get("app_code") != null && !"".equals(conditionMap.get("app_code")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.APP_CODE + " like '%" + conditionMap.get("app_code") + "%' ");
			}
			if(conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.PHONE + "  like '%" + conditionMap.get("phone") + "%' ");
			}
			if(conditionMap.get("age") != null && !"".equals(conditionMap.get("age")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.AGE + " = " + conditionMap.get("age") + " ");
			}
			if(conditionMap.get("createtime") != null && !"".equals(conditionMap.get("createtime")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.VERIFYTIME + " >= " + genericDao.getTimeCondition(conditionMap.get("createtime")));
			}
			if(conditionMap.get("endtime") != null && !"".equals(conditionMap.get("endtime")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.VERIFYTIME + " <= " +genericDao.getTimeCondition(conditionMap.get("endtime")));
			}
			if(conditionMap.get("groupid") != null && !"".equals(conditionMap.get("groupid")))
			{	if(!"-1".equals(conditionMap.get("groupid"))){
					conSql.append(" and totl.g_id=" + conditionMap.get("groupid"));
				}else {
					conSql.append(" and  totl.g_id is null");
				}
			}
		}
		// 排序
		String orderbySql = " order by lfMwClient.CREATETIME DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString()+orderbySql;
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
	
	/**
	 * 根据群组ID查询APP客户
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findByGroup(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		// 拼SQL语句
		String fieldSql = "SELECT lfMwClient.*,olgroup.name mwgroupname,gpmem.g_id as gid ";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfAppMwClient.TABLE_NAME).append(" lfMwClient " +
				"left join LF_APP_MW_GPMEM gpmem on " +
				" lfMwClient.WC_ID=gpmem.GM_USER left join LF_APP_MW_GROUP olgroup on  olgroup.G_ID=gpmem.G_ID WHERE gpmem.GM_STATU=1 and lfMwClient.UTYPE=4");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		//去掉公司条件
//		conSql.append(" and lfMwClient.").append(TableLfAppAccount.CORP_CODE).append("='").append(corpCode).append("'");
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			if(conditionMap.get("groupid") != null && !"".equals(conditionMap.get("groupid")))
			{
				conSql.append(" and olgroup." + TableLfAppMwGroup.G_ID + " =" + conditionMap.get("groupid") + "");
			}

			if(conditionMap.get("nickname") != null && !"".equals(conditionMap.get("nickname")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.NICK_NAME + " like '%" + conditionMap.get("nickname") + "%' ");
			}
			if(conditionMap.get("sex") != null && !"".equals(conditionMap.get("sex")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.SEX + " = '" + conditionMap.get("sex") + "' ");
			}
			if(conditionMap.get("app_code") != null && !"".equals(conditionMap.get("app_code")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.APP_CODE + " like '%" + conditionMap.get("app_code") + "%' ");
			}
			if(conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.PHONE + "  like '%" + conditionMap.get("phone") + "%' ");
			}
			if(conditionMap.get("age") != null && !"".equals(conditionMap.get("age")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.AGE + " = " + conditionMap.get("age") + " ");
			}
			if(conditionMap.get("createtime") != null && !"".equals(conditionMap.get("createtime")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.VERIFYTIME + " >= " + genericDao.getTimeCondition(conditionMap.get("createtime")));
			}
			if(conditionMap.get("endtime") != null && !"".equals(conditionMap.get("endtime")))
			{
				conSql.append(" and lfMwClient." + TableLfAppMwClient.VERIFYTIME + " <= " +genericDao.getTimeCondition(conditionMap.get("endtime")));
			}
		
		}
		// 排序
		String orderbySql = " order by lfMwClient.CREATETIME DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;
		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	
	}
	
	
	/**
	 * 查询未分群的
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 */
	public List<DynaBean> findByNotGroup(String corpCode, LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			if(conditionMap.get("nickname") != null && !"".equals(conditionMap.get("nickname")))
			{
				conSql.append(" and " + TableLfAppMwClient.NICK_NAME + " like '%" + conditionMap.get("nickname") + "%' ");
			}
			if(conditionMap.get("sex") != null && !"".equals(conditionMap.get("sex")))
			{
				conSql.append(" and " + TableLfAppMwClient.SEX + " = '" + conditionMap.get("sex") + "' ");
			}
			if(conditionMap.get("app_code") != null && !"".equals(conditionMap.get("app_code")))
			{
				conSql.append(" and " + TableLfAppMwClient.APP_CODE + " like '%" + conditionMap.get("app_code") + "%' ");
			}
			if(conditionMap.get("phone") != null && !"".equals(conditionMap.get("phone")))
			{
				conSql.append(" and " + TableLfAppMwClient.PHONE + "  like '%" + conditionMap.get("phone") + "%' ");
			}
			if(conditionMap.get("age") != null && !"".equals(conditionMap.get("age")))
			{
				conSql.append(" and " + TableLfAppMwClient.AGE + " = " + conditionMap.get("age") + " ");
			}
			if(conditionMap.get("createtime") != null && !"".equals(conditionMap.get("createtime")))
			{
				conSql.append(" and " + TableLfAppMwClient.VERIFYTIME + " >= " + genericDao.getTimeCondition(conditionMap.get("createtime")));
			}
			if(conditionMap.get("endtime") != null && !"".equals(conditionMap.get("endtime")))
			{
				conSql.append(" and " + TableLfAppMwClient.VERIFYTIME + " <= " +genericDao.getTimeCondition(conditionMap.get("endtime")));
			}

		}
		String sql = "select a.*,'' as mwgroupname,'' as gid from LF_APP_MW_CLIENT a where WC_ID not in (select GM_USER from LF_APP_MW_GPMEM)  and  UTYPE=4 "+conSql+" order by CREATETIME DESC";
		String countSql = "select count(*) totalcount from LF_APP_MW_CLIENT where WC_ID not in (select GM_USER from LF_APP_MW_GPMEM)  and  UTYPE=4" +conSql;
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	
	}
	/****
	 * 查询群组信息
	 * @return
	 */
		public List<DynaBean> querygroup()
		{
			String sql="select appgroup.NAME,personcount.countper,appgroup.g_id " +
					"from LF_APP_MW_GROUP  appgroup " +
					"LEFT JOIN (SELECT olgroup.G_ID as id,COUNT(clinet.GM_USER) as countper " +
					"from  LF_APP_MW_GROUP olgroup, LF_APP_MW_GPMEM  clinet " +
					"where olgroup.G_ID=clinet.G_ID and clinet.GM_STATU=1  group by olgroup.G_ID) " +
					"personcount on  personcount.id=appgroup.G_ID order by appgroup.NAME ";
			List<DynaBean> returnList = null;
			 returnList = this.getListDynaBeanBySql(sql);
			return returnList;
		}
		
		/****
		 * 查询未分群组信息
		 * @return
		 * 用户类型
			0:后台管理员账号
			1:企业管理员账号
			2:APP用户账号

		 */
			public List<DynaBean> queryNotAtGroup(String corpcode)
			{
				String sql="SELECT *  from LF_APP_MW_CLIENT client where (select COUNT(*) from  LF_APP_MW_GPMEM   where GM_USER =client.WC_ID)=0 and  UTYPE=4";
				List<DynaBean> returnList = null;
				 returnList = this.getListDynaBeanBySql(sql);
				return returnList;
			}
	
}
