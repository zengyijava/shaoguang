package com.montnets.emp.monmanage.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.monitor.dao.MonitorDAO;
import com.montnets.emp.monmanage.dao.i.ISpAcctMonCfgDAO;
import com.montnets.emp.util.PageInfo;
/**
 * sp账号监控管理dao
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 上午08:36:21
 */
public class SpAcctMonCfgDAO extends SuperDAO implements ISpAcctMonCfgDAO
{

	/**
	 * 获取sp账号监控管理
	 * @description    
	 * @param pageInfo 分页信息
	 * @param conditionMap 条件map
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-03 上午08:38:39
	 */
	public List<DynaBean> getspAcctMonCfg(PageInfo pageInfo,LinkedHashMap<String, String> conditionMap)
	{
		List<DynaBean> beanList = null;
		String sql = "select * from LF_MON_SSPACINFO sp";
		StringBuffer conSql = new StringBuffer();
		if(conditionMap!=null){
			String spaccountid = conditionMap.get("spaccountid");
			String spaccountType = conditionMap.get("spaccountType");
			String accountname = conditionMap.get("accountname");
			String monstatus = conditionMap.get("monstatus");
			if(spaccountid!=null && !"".equals(spaccountid))
			{
				if(!"0".equals(spaccountType)){
					conSql.append(" and sp.spaccountid like '%").append(spaccountid).append("%'");
				}else{
					conSql.append(" and sp.spaccountid = '").append(spaccountid).append("'");
				}
			}
			if(monstatus!=null && !"".equals(monstatus))
			{
				conSql.append(" and sp.monstatus =").append(monstatus);
			}
			if(accountname!=null && !"".equals(accountname))
			{
				conSql.append(" and sp.accountname like '%").append(accountname).append("%'");
			}
			//登录类型
			String logintype=conditionMap.get("logintype");
			if(logintype!=null && !"".equals(logintype))
			{
				conSql.append(" and sp.LOGIN_TYPE =").append(logintype);
			}
		}
		//处理并返回SQL条件
		String conditonSql = new MonitorDAO().getConditionSql(conSql.toString());
		if(conditonSql == null)
		{
			return null;
		}
		sql = sql + conditonSql;
		if(pageInfo==null)
		{
			beanList = this.getListDynaBeanBySql(sql);
		}
		else
		{
			String countSql = "select count(*) totalcount from LF_MON_SSPACINFO sp"  + conditonSql;
//			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
			beanList = new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQLNoCount(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
		}
		return beanList;
	}
	
}
