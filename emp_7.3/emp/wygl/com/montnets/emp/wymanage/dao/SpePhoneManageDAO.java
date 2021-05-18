package com.montnets.emp.wymanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;

/**
 * 特殊号码管理dao
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-24 下午04:25:48
 */
public class SpePhoneManageDAO extends SuperDAO {

	/**
	 * 特殊号码管理查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-24 下午04:36:08
	 */
	public List<DynaBean> getSpePhone(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select ph.id,ph.phone,ph.userid,ph.opttype,ph.createtime,ph.unicom from a_spe_phone ph where opttype=0";
		String conSql="";
		String phone = conditionMap.get("phone");
		String unicom = conditionMap.get("unicom");
		if(phone!=null&&!"".equals(phone))
		{
			conSql = conSql + " and ph.phone like '%" + phone +"%'"; 
		}
		
		if(unicom!=null&&!"".equals(unicom))
		{
			conSql = conSql + " and ph.unicom =" + unicom ; 
		}
		String orderSql=" order by ph.createtime desc";
		sql = sql + conSql.toString() + orderSql;
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from a_spe_phone ph where opttype=0" + conSql.toString();
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
}
