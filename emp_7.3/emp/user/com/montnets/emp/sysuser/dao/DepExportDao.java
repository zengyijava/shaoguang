package com.montnets.emp.sysuser.dao;

import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.util.PageInfo;

public class DepExportDao extends SuperDAO {

	public List<LfDep> findByCoreCodeAndDepPathLike (String corde, String depPath,
			PageInfo pageInfo) throws Exception {
		SuperDAO superDao = new SuperDAO();
		String sql = new StringBuffer().append("select * from  ")
				.append(TableLfDep.TABLE_NAME).append("  where  ")
				.append(TableLfDep.CORP_CODE).append("=").append(corde)
				.append("  and   ").append(TableLfDep.DEP_STATE).append(" = 1")
				.append("  and  ").append(TableLfDep.DEP_PATH).append(" like ")
				.append("'" + depPath).append("%'").toString();

		String countSql = new StringBuffer().append("select count(*) totalcount from  (")
				.append(sql).append(") s").toString();

		List<LfDep> depList = null;
		if (pageInfo == null) {
			depList = superDao.findEntityListBySQL(LfDep.class, sql,
					StaticValue.EMP_POOLNAME);
		}
		else {
			depList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(
					LfDep.class, sql, countSql, pageInfo, StaticValue.EMP_POOLNAME);
		}
		return depList;
	}

	public List<LfDep> findByUserId (Long userId) throws Exception {
		String domDepSql = "select dep.*" + " from " + TableLfDomination.TABLE_NAME
				+ " domination inner join " + TableLfDep.TABLE_NAME
				+ " dep on domination." + TableLfDomination.DEP_ID + "=dep."
				+ TableLfDep.DEP_ID + " where domination." + TableLfDomination.USER_ID
				+ "=" + userId + " order by dep." + TableLfDep.DEP_ID + " asc";
		List<LfDep> lfdept = findEntityListBySQL(LfDep.class, domDepSql,
				StaticValue.EMP_POOLNAME);

		return lfdept;

	}
}
