/**
 * Program  : MnpManageDAO.java
 * Author   : zousy
 * Create   : 2014-3-24 下午01:37:53
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.wymanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.wy.AMnp;
import com.montnets.emp.table.wy.TableAMnp;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author   zousy <zousy999@qq.com>
 * @version  1.0.0
 * @2014-3-24 下午01:37:53
 */
public class MnpManageDAO extends SuperDAO {
	/***
	 * 携号转网号码管理查询
	 * @param pageInfo
	 * @param conditionMap
	 * @param
	 */
	public List<AMnp> getMnpList(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap) throws Exception{
		List<AMnp> beanList = null;
		String sql = "select * from "+TableAMnp.TABLE_NAME;
		String countSql  = "select count(*) totalcount from "+TableAMnp.TABLE_NAME;
		String orderSql = " order by "+TableAMnp.CREATETIME+" desc,"+TableAMnp.ID+" desc";
		StringBuffer sqlCon = new StringBuffer("");
		//逻辑删除标志
		sqlCon.append(" where "+TableAMnp.OPTTYPE+" = 0");
		//添加类型
		String addTypeStr = conditionMap.get("addType");
		if(addTypeStr!=null&&!"".equals(addTypeStr)){
			sqlCon.append(" and "+TableAMnp.ADDTYPE+" = "+addTypeStr);
		}
		//手机号码
		String phone = conditionMap.get("phone");
		if(phone!=null&&!"".equals(phone)){
			sqlCon.append(" and "+TableAMnp.PHONE+" like '%"+phone+"%'");
		}
		
		if("255".equals(conditionMap.get("unknown"))){
			sqlCon.append(" and "+TableAMnp.UNICOM+" + "+TableAMnp.PHONETYPE+" >= "+255);
		}else{
			String unicom = conditionMap.get("unicom");
			String phoneType = conditionMap.get("phoneType"); 
			if(unicom!=null){
				sqlCon.append(" and "+TableAMnp.UNICOM+" = "+unicom);
			}
			if(phoneType!=null){
				sqlCon.append(" and "+TableAMnp.PHONETYPE+" = "+phoneType);
			}
		}
		sql = sql + sqlCon.toString() + orderSql;
		if(pageInfo==null)
		{
			beanList = this.findEntityListBySQL(AMnp.class, sql, StaticValue.EMP_POOLNAME);
		}
		else
		{
			countSql = countSql + sqlCon;
			beanList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQLNoCount(AMnp.class,sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
		return beanList;
	}
}

