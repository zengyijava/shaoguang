/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-16 下午02:10:04
 */
package com.montnets.emp.appmage.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.appmage.LfAppMwClient;
import com.montnets.emp.util.PageInfo;

/**
 * @description APP信息发送DAO
 * @project emp_std_183
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-6-16 下午02:10:04
 */

public class app_msgSendDAO extends SuperDAO
{
	/**
	 * 根据企业编码获取APP企业编码
	 * 
	 * @description
	 * @param corpcode
	 *        企业编码
	 * @return APP企业账户信息
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-16 下午02:19:06
	 */
	public List<DynaBean> getAppCorpCode(String corpcode)
	{
		// 执行查询SQL
		String sql = "SELECT AC.FAKE_ID AS APPACCOUNT, AC.NAME AS CORPNAME , AC.CORP_CODE AS APPCORPCODE ,"
					+"(SELECT COUNT(*) FROM LF_APP_MW_CLIENT AL WHERE UTYPE = 4 AND AL.ECODE=UPPER(AC.FAKE_ID)) AS MCOUNT"
					+" FROM LF_APP_ACCOUNT AC WHERE APP_TYPE = 0";
		if(corpcode != null && !"".equals(corpcode))
		{
			sql += " AND CORP_CODE = '" + corpcode + "'";
		}
		List<DynaBean> appAccountList = getListDynaBeanBySql(sql);
		return appAccountList;
	}

	/**
	 * 获取客户群组发送客户账号
	 * @description
	 * @param group  APP客户群组
	 * @return APP客户
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-14 下午05:09:10
	 */
	public String getAccountByGroup(String group)
	{
		StringBuffer account = new StringBuffer();

		String sql = "SELECT APP_CODE AS ACCOUNT FROM LF_APP_MW_CLIENT WHERE UTYPE = 4 AND " +
				"WC_ID IN(SELECT GM_USER FROM LF_APP_MW_GPMEM WHERE APP_TYPE = 0 AND GM_STATU = 1 AND G_ID IN(" + group + "))";
		List<DynaBean> accountList = getListDynaBeanBySql(sql);
		if(accountList != null && accountList.size() > 0)
		{
			for(DynaBean appCode:accountList)
			{
				account.append(appCode.get("account").toString()).append(",");
			}
		}
		return account.toString();
	}

	/**
	 * 获取APP企业发送客户账号
	 * @description    
	 * @param appCorpCode APP企业编码
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-19 下午07:32:30
	 */
	public String getAccountByAppCorpCode(String appCorpCode)
	{
		StringBuffer account = new StringBuffer();

		String sql = "SELECT APP_CODE AS ACCOUNT FROM LF_APP_MW_CLIENT WHERE UTYPE = 4 AND ECODE IN (" + appCorpCode + ")";
		List<DynaBean> accountList = getListDynaBeanBySql(sql);
		if(accountList != null && accountList.size() > 0)
		{
			for(DynaBean appCode:accountList)
			{
				account.append(appCode.get("account").toString()).append(",");
			}
		}
		return account.toString();
	}
	
	/**
	 * 获取客户群组信息
	 * 
	 * @description
	 * @param corpcode 企业编码
	 * @return 客户群组列表
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 上午10:09:09
	 */
	public List<DynaBean> getAppGroupList(String corpcode)
	{
		String sql = "SELECT GP.G_ID AS GROUPID, GP.NAME AS GROUPNAME, (SELECT COUNT(*) FROM LF_APP_MW_GPMEM GM " +
					"WHERE GM.APP_TYPE = 0 AND GM.GM_STATU = 1 AND GP.G_ID = GM.G_ID) AS MCOUNT FROM LF_APP_MW_GROUP GP " +
					"WHERE CORP_CODE = '"+corpcode+"'";

		List<DynaBean> appGroupList = getListDynaBeanBySql(sql);

		return appGroupList;
	}

	/**
	 * 获取未分组APP客户账号
	 * @description
	 * @param appAccount 当前APP企业编码
	 * @return APP客户列表
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-17 上午10:09:09
	 */
	public List<DynaBean> getNoGroupAppCount(String appAccount)
	{
		String sql = "SELECT APP_CODE AS ACCOUNT FROM LF_APP_MW_CLIENT CL WHERE UTYPE = 4 AND CL.ECODE = '"+appAccount+
					"' AND (SELECT COUNT(*) FROM LF_APP_MW_GPMEM GP WHERE GP.APP_TYPE = 0 AND GP.GM_STATU = 1 AND CL.WC_ID = GP.GM_USER) = 0";

		List<DynaBean> noGroupAppCountList = getListDynaBeanBySql(sql);

		return noGroupAppCountList;
	}
	
	/**
	 * 获取APP客户信息
	 * @description    
	 * @param chooseType 类型：1：群组；2：企业
	 * @param id	群组ID或企业ID
	 * @param appAccount 当前APP企业编码
	 * @param pageInfo 分页对象
	 * @return       APP成员列表			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-23 下午05:07:27
	 */
	public List<LfAppMwClient> getAppMembersList(String chooseType, String id, String appAccount, PageInfo pageInfo)
	{
		StringBuffer sql = new StringBuffer().append("SELECT * FROM LF_APP_MW_CLIENT CL WHERE CL.UTYPE = 4");
		try
		{
			//获取群组用户账号
			if("1".equals(chooseType))
			{
				//未分组用户账号
				if("-1".equals(id))
				{
					sql.append(" AND CL.ECODE = '").append(appAccount)
					.append("' AND (SELECT COUNT(*) FROM LF_APP_MW_GPMEM GP WHERE GP.APP_TYPE = 0 AND GP.GM_STATU = 1 AND CL.WC_ID = GP.GM_USER) = 0");
				}
				else
				{
					sql.append(" AND CL.WC_ID IN(SELECT GM_USER FROM LF_APP_MW_GPMEM WHERE APP_TYPE = 0 AND GM_STATU = 1 AND G_ID = ").append(id).append(")");
				}
			}
			//APP企业用户账号
			else
			{
				sql.append(" AND CL.ECODE='").append(id).append("'");
			}
			String sqlStr = sql.toString();
			sql.append(" ORDER BY UNAME ");
			if(pageInfo == null)
			{
				List<LfAppMwClient> appGroupMember = findEntityListBySQL(LfAppMwClient.class, sql.toString(), StaticValue.EMP_POOLNAME);
				return appGroupMember;
			}
			else
			{
				String countSql = new StringBuffer("SELECT COUNT(*) totalcount FROM (").append(sqlStr).append(")").append(" tmp").toString();
				return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfAppMwClient.class, sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
			}
		}
		catch (Exception e1)
		{
			EmpExecutionContext.error(e1, "获取APP客户信息异常！");
			return null;
		}
	}
	
	/**
	 * 获取所有群组或所有企业下的APP发送客户信息
	 * @description    
	 * @param chooseType 类型：1：群组；2：企业
	 * @param appAccount 当前APP企业编码
	 * @param epname 姓名
	 * @param pageInfo 分页对象
	 * @return       APP成员列表			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-6-23 下午05:07:27
	 */
	public List<LfAppMwClient> getAllAppMembersList(String appAccount, String chooseType, String epname, PageInfo pageInfo)
	{
		StringBuffer sql = new StringBuffer().append("SELECT * FROM LF_APP_MW_CLIENT WHERE UTYPE = 4");
		try
		{
			//获取群组用户账号
			if("1".equals(chooseType))
			{
				sql.append(" AND ECODE = '").append(appAccount).append("'");
			}
			if(epname != null && !"".equals(epname))
			{
				sql.append(" AND APP_CODE LIKE '%").append(epname).append("%'");
			}
			String sqlStr = sql.toString();
			sql.append(" ORDER BY UNAME ");
			if(pageInfo == null)
			{
				List<LfAppMwClient> appGroupMember = findEntityListBySQL(LfAppMwClient.class, sql.toString(), StaticValue.EMP_POOLNAME);
				return appGroupMember;
			}
			else
			{
				String countSql = new StringBuffer("SELECT COUNT(*) totalcount FROM (").append(sqlStr).append(")").append(" tmp").toString();
				return new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(LfAppMwClient.class, sql.toString(), countSql, pageInfo, StaticValue.EMP_POOLNAME);
			}
		}
		catch (Exception e1)
		{
			EmpExecutionContext.error(e1, "获取所有群组或所有企业下的APP发送客户信息异常!appAccount:" + appAccount);
			return null;
		}
	}
}
