package com.montnets.emp.wxmanger.dao;

import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.netnews.entity.LfWXBASEINFO;
import com.montnets.emp.util.PageInfo;

public class WxTemplateDao extends SuperDAO {	

	
	/**
	 * 网讯审核生成网讯访问页面
	 * 
	 * @param netId
	 * @param iphORhe
	 * @param corpcode
	 * @return Map
	 */
	public List<DynaBean> getTemplates(LfWXBASEINFO baseInfo,String corpcode,PageInfo pageInfo) {
		String baseSql = " from lf_wx_baseinfo a , lf_wx_sort b "+ 
				"where b.corp_code='"+corpcode+"' and a.sort=b.id";;
		String sql = "SELECT a.*,b.name typename "+baseSql;

		StringBuffer conSql = new StringBuffer();
		if(baseInfo.getNETID() != null && baseInfo.getNETID() != 0L){
			conSql.append(" and a.netid =").append(baseInfo.getNETID());
		}
		
		if(baseInfo.getSORT() != null && baseInfo.getSORT() != 0L){
			conSql.append(" and a.sort =").append(baseInfo.getSORT());
		}

		if(baseInfo.getNAME() != null && !"".equals(baseInfo.getNAME())){
			conSql.append(" and a.name like '%").append(baseInfo.getNAME()).append("%'");
		}
		sql+=conSql;
		String countSql = "select count(*) totalcount "+baseSql +conSql;
		return  new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}
}
