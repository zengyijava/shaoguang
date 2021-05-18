package com.montnets.emp.weix.dao.i;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.weix.LfWcMsg;
import com.montnets.emp.util.PageInfo;

import com.montnets.emp.table.weix.TableLfWcAlink;
import com.montnets.emp.table.weix.TableLfWcMsg;
import com.montnets.emp.table.weix.TableLfWcAccount;
import com.montnets.emp.table.weix.TableLfWcUserInfo;

public interface IMsgDao 
{
	/**
	 * 根据条件获取企业微信上行/下行历史消息列表
	 * 
	 * @param conditionMap
	 *        查询条件
	 * @param orderbyMap
	 *        排序条件
	 * @param pageInfo
	 *        分页信息，无需分析时传入null
	 * @return 上行/下行历史消息集合
	 * @throws Exception
	 */
	public List<DynaBean> findListMsgByCondition(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception;

	// 获取表名
	public String getTableName(LinkedHashMap<String, String> conditionMap);

	// 时间范围
	public String getTimeSql(LinkedHashMap<String, String> conditionMap);

	/**
	 * 查询历史消息
	 * 
	 * @param corpCode
	 * @param conditionMap
	 * @param orderbyMap
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<DynaBean> findListMsgDefault(String corpCode, LinkedHashMap<String, String> conditionMap, Object orderbyMap, PageInfo pageInfo);

	/**
	 * 创建历史消息
	 * 
	 * @param msg
	 * @return
	 * @throws SQLException
	 */
	public void createMsg(LfWcMsg msg) throws SQLException;

}
