package com.montnets.emp.weix.dao.i;

import java.util.LinkedHashMap;
import java.util.List;
import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.weix.LfWcAccount;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.table.weix.TableLfWcAccount;
import com.montnets.emp.table.weix.TableLfWcRevent;

public interface IFocusrepDao
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
	public List<DynaBean> findFocusReply(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;
	
	/**
	 * 根据机构编号查找绑定公众账号
	 * @description    
	 * @param corpCode 机构编号
	 * @param status 状态
	 * @return
	 * @throws Exception       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:04:54
	 */
	public List<LfWcAccount> findBindAccountByCorpCode(String corpCode, String status) throws Exception;
}
