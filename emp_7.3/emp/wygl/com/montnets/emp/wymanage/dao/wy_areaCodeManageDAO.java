/**
 * @description  
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:52:47
 */
package com.montnets.emp.wymanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.wy.AAreacode;
import com.montnets.emp.util.PageInfo;

/**
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午11:52:47
 */

public class wy_areaCodeManageDAO extends SuperDAO 
{

	AuthenAtom authenAtom = new AuthenAtom();
	
	/**
	 * 
	 * @description    查询国际号码
	 * @param pageInfo 分页
	 * @param conditionMap 查询条件
	 * @return  beanList 国际号码列表     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 下午12:43:06
	 */
	public List<DynaBean> getAreaCode(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> areaCodeList = null;
		String sql = "SELECT AREACODE.* FROM A_AREACODE AREACODE ";
		String areaName = conditionMap.get("areaname");
		String areaCode = conditionMap.get("areacode");
		StringBuffer conSql = new StringBuffer();
		if(areaName!=null && !"".equals(areaName) && authenAtom.holesProcessing(areaName))
		{
			conSql.append(" AND AREACODE.AREANAME like '%").append(areaName.trim()).append("%'");
		}
		if(areaCode!=null && !"".equals(areaCode) && authenAtom.holesProcessing(areaCode))
		{
			conSql.append(" AND AREACODE.AREACODE like '%").append(areaCode.trim()).append("%'");
		}
		String conSqlStr = conSql.toString().replaceFirst("^(\\s*)(?i)and", "$1where");
		sql = sql + conSqlStr+ " ORDER BY AREANAME ASC";
		if(pageInfo==null)
		{
			areaCodeList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "SELECT COUNT(*) TOTALCOUNT FROM A_AREACODE AREACODE "  + conSqlStr;
			areaCodeList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return areaCodeList;
	}
	
	/**
	 * 
	 * @description  是否存在相同的国际区号  
	 * @param conditionMap
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 下午05:45:56
	 */
	public int getAreaCodeCount(LinkedHashMap<String, String> conditionMap)
	{
		int count = 0;
		StringBuffer conSql = new StringBuffer();
		conSql.append("SELECT COUNT(*) TOTALCOUNT FROM A_AREACODE AREACODE ");
		String areaCode = conditionMap.get("areacode");
		if(areaCode!=null && !"".equals(areaCode) && authenAtom.holesProcessing(areaCode))
		{
			conSql.append(" WHERE AREACODE.AREACODE = '").append(areaCode.trim()).append("'");
		}
		
		List<DynaBean> areaCodeList = this.getListDynaBeanBySql(conSql.toString());
		if(areaCodeList != null && areaCodeList.size() > 0)
		{
			String countStr = areaCodeList.get(0).get("totalcount").toString();
			if(countStr != null && !"".equals(countStr))
			{
				count = Integer.parseInt(countStr);
			}
		}
		return count;
	}
	
	/***
	 * 获取国家代码字符串
	* @Description: TODO
	* @param @param ids 代码ID
	* @param @return 结果集
	* @param @throws Exception
	* @return List<AAreacode>
	 */
	public List<AAreacode> findAreaCodeListByCondition(String ids) throws Exception
	{
		String sql ="select * from A_AREACODE where id in("+ids+")";
		List<AAreacode> AAreacodes = findEntityListBySQL(AAreacode.class, sql, StaticValue.EMP_POOLNAME);
		return AAreacodes;
	} 
}
