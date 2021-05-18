package com.montnets.emp.weix.dao.i;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpTransactionDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.weix.LfWcRimg;
import com.montnets.emp.entity.weix.LfWcTemplate;
import com.montnets.emp.table.weix.TableLfWcTemplate;
import com.montnets.emp.table.weix.TableLfWcTlink;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.weix.common.util.GlobalMethods;

/**
 * @author chensj
 */
public interface ITempleDao 
{

	/**
	 * 查询图文模板信息
	 * @description    
	 * @param conditionMap
	 * @param pageInfo
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:11:45
	 */
	public List<DynaBean> getBaseInfos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo);

	/**
	 * 查询关键字
	 * @description    
	 * @param tid
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:12:35
	 */
	public String getkeyname(String tid);

	/**
	 * 获得图文id与名称
	 * @description    
	 * @param rids
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-9-26 下午03:13:00
	 */
	public String getimgidnames(String[] rids);

	public String getConditionSql(LinkedHashMap<String, String> conditionMap);
	/**
	 * 获取回复模板（默认回复和关注时回复用到）
	 * 2013-7-30
	 * List<DynaBean>
	 * zm
	 * 
	 * @throws Exception
	 */
	public List<DynaBean> getBaseTempInfos(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo) throws Exception;

	/**
	 * 关键字ID找到它对于的模板
	 * 
	 * @param kId
	 * @return
	 */
	public List<DynaBean> findTemplateByKeywordId(String kId) throws Exception;
}
