package com.montnets.emp.weix.dao.i;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.client.TableLfClient;
import com.montnets.emp.table.weix.TableLfWcAccount;
import com.montnets.emp.table.weix.TableLfWcAlink;
import com.montnets.emp.table.weix.TableLfWcUserInfo;
import com.montnets.emp.util.PageInfo;

/**
 * @author chensj
 */
public interface IUserMgrDao
{
	/**
	 * 查找关注用户
	 * @description    
	 * @param corpCode
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:14:30
	 */
	public List<DynaBean> getUserInfoList(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo);

	public String getConditionSql(LinkedHashMap<String, String> conditionMap);

}
