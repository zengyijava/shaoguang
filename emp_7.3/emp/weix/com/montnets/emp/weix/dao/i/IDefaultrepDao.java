package com.montnets.emp.weix.dao.i;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.weix.TableLfWcAccount;

public interface IDefaultrepDao
{
	/**
	 * 默认回复查询(分页和不分页两种情况)
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findDefaltReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;

}
