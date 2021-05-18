package com.montnets.emp.wxgl.dao;

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
import com.montnets.emp.entity.wxgl.LfWeiMsg;
import com.montnets.emp.table.wxgl.TableLfWeiAccount;
import com.montnets.emp.table.wxgl.TableLfWeiMsg;
import com.montnets.emp.table.wxgl.TableLfWeiUserInfo;
import com.montnets.emp.util.PageInfo;

public class MsgDao extends SuperDAO
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
	public List<DynaBean> findListMsgByCondition(String corpCode, LinkedHashMap<String, String> conditionMap, LinkedHashMap<String, String> orderbyMap, PageInfo pageInfo) throws Exception
	{
		String tableName = getTableName(conditionMap);

		// 拼SQL语句
		String fieldSql = "SELECT lfMsg.*,lfAccount.NAME as acctName,lfuser.NAME as userName,lfuser.CODE as userCode,lfuser.OPEN_ID as openid";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(tableName).append(" lfMsg ");

		tableSql.append(" LEFT JOIN ").append(TableLfWeiAccount.TABLE_NAME).append(" lfAccount ").append(" ON ");
		tableSql.append("lfMsg.A_ID").append("=").append("lfAccount." + TableLfWeiAccount.A_ID);
		tableSql.append(" LEFT JOIN ").append(TableLfWeiUserInfo.TABLE_NAME).append(" lfuser ").append("ON ");
		tableSql.append("lfMsg.WC_ID").append("=").append("lfuser." + TableLfWeiUserInfo.WC_ID);

		tableSql.append(" WHERE  ");
		// 查询条件
		StringBuffer conSql = new StringBuffer();
		conSql.append("  lfMsg.CORP_CODE").append("='").append(corpCode).append("'");

		if(conditionMap != null && !conditionMap.entrySet().isEmpty())
		{
			if(conditionMap.get("type") != null && !"".equals(conditionMap.get("type")))
			{
				conSql.append(" and lfMsg.TYPE" + "=" + conditionMap.get("type"));
			}
			if(conditionMap.get("aId") != null && !"".equals(conditionMap.get("aId")))
			{
				conSql.append(" and lfMsg.A_ID" + "=" + conditionMap.get("aId"));
			}
			if(conditionMap.get("msgtp") != null && !"".equals(conditionMap.get("msgtp")))
			{
				conSql.append(" and lfMsg.MSG_TYPE" + "=" + conditionMap.get("msgtp"));
			}
			if(conditionMap.get("wcname") != null && !"".equals(conditionMap.get("wcname")))
			{
				conSql.append(" and ( lfuser." + TableLfWeiUserInfo.NICK_NAME + " like '%" + conditionMap.get("wcname") + "%'");
			}
			if(conditionMap.get("openid") != null && !"".equals(conditionMap.get("openid")))
			{
				conSql.append(" OR alink." + TableLfWeiUserInfo.OPEN_ID + " like '%" + conditionMap.get("openid") + "%'").append(")");
			}

			String timeSql = getTimeSql(conditionMap);
			conSql.append(timeSql);
		}
		// 排序
		String orderbySql = " order by lfMsg.CREATETIME DESC";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;

		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}

	// 获取表名
	public String getTableName(LinkedHashMap<String, String> conditionMap)
	{
		// 开始时间
		String beginTime = conditionMap.get("beginTime");
		// 结束时间
		String endTime = conditionMap.get("endTime");
		// 查询表名
		String tableName = "";

		if((beginTime != null && !"".equals(beginTime)) && (endTime == null || "".equals(endTime)))
		{
			// 开始时间不为空，结束时间为空
			String year = beginTime.trim().substring(0, 4);
			String month = beginTime.trim().substring(5, 7);
			tableName = "LFWCMSG" + String.valueOf(year) + String.valueOf(month);
		}
		else
			if((beginTime == null || "".equals(beginTime)) && (endTime != null && !"".equals(endTime)))
			{
				// 开始时间为空，结束时间不为空
				String year = endTime.trim().substring(0, 4);
				String month = endTime.trim().substring(5, 7);
				tableName = "LFWCMSG" + String.valueOf(year) + String.valueOf(month);
			}
			else
				if((endTime != null && !"".equals(endTime) && (beginTime != null && !"".equals(beginTime))))
				{
					// 开始时间和结束事件都不为空
					String year = endTime.trim().substring(0, 4);
					String month = endTime.trim().substring(5, 7);
					tableName = "LFWCMSG" + String.valueOf(year) + String.valueOf(month);
				}
				else
				{
					// 根据当前的年月获取表名
					// 当前年和月
					Calendar calendar = Calendar.getInstance();
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;
					if(month > 0 && month <= 9)
					{
						tableName = "LFWCMSG" + String.valueOf(year) + "0" + String.valueOf(month);
					}
					else
					{
						tableName = "LFWCMSG" + String.valueOf(year) + String.valueOf(month);
					}
				}
		return tableName;
	}

	// 时间范围
	public String getTimeSql(LinkedHashMap<String, String> conditionMap)
	{
		StringBuffer timeSql = new StringBuffer();
		// 开始时间
		String beginTime = conditionMap.get("beginTime");
		// 结束时间
		String endTime = conditionMap.get("endTime");
		// 当前数据库类型
		String type = SystemGlobals.getValue("DBType");

		if((beginTime != null && !"".equals(beginTime)) && (endTime == null || "".equals(endTime)))
		{
			// 开始时间不为空，结束时间为空
			String year = beginTime.trim().substring(0, 4);
			String month = beginTime.trim().substring(5, 7);
			int daynum = getMonDay(String.valueOf(month));
			endTime = String.valueOf(year) + "-" + month + "-" + String.valueOf(daynum) + " 23:59:59";
		}
		else if((beginTime == null || "".equals(beginTime)) && (endTime != null && !"".equals(endTime)))
		{
			// 开始时间为空，结束时间不为空
			String year = endTime.trim().substring(0, 4);
			String month = endTime.trim().substring(5, 7);
			beginTime = String.valueOf(year) + "-" + month + "-01" + " 00:00:00";
		}
		else if((endTime == null || "".equals(endTime) && (beginTime == null || "".equals(beginTime))))
		{
			// 获取当前时间
			Calendar calendar = Calendar.getInstance();
			// 获取当前年，月
			int year = calendar.get(Calendar.YEAR);
			int startmonth = calendar.get(Calendar.MONTH) + 1;
			int daynum = getMonDay(String.valueOf(startmonth));
			beginTime = String.valueOf(year) + "-" + String.valueOf(startmonth) + "-01" + " 00:00:00";
			endTime = String.valueOf(year) + "-" + String.valueOf(startmonth) + "-" + String.valueOf(daynum) + " 23:59:59";
		}
		if("1".equals(type))
		{
			// oracle
			timeSql.append(" and lfMsg.CREATETIME " + ">= to_date('" + beginTime + "','yyyy-MM-dd HH24:MI:SS')");
			timeSql.append(" and lfMsg.CREATETIME " + "<= to_date('" + endTime + "','yyyy-MM-dd HH24:MI:SS')");
		}
		else
		{
			// sql server ,db2
			timeSql.append(" and lfMsg.CREATETIME " + ">= '" + beginTime + "'");
			timeSql.append(" and lfMsg.CREATETIME " + "<= '" + endTime + "'");
		}

		return timeSql.toString();
	}

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
	public List<DynaBean> findListMsgDefault(String corpCode, LinkedHashMap<String, String> conditionMap, Object orderbyMap, PageInfo pageInfo)
	{
		// 拼SQL语句
		String fieldSql = "SELECT lfMsg.*,lfAccount.NAME as acctName,lfuser.NAME as userName,lfuser.CODE as userCode";
		StringBuffer tableSql = new StringBuffer();
		tableSql.append(" FROM ").append(TableLfWeiMsg.TABLE_NAME).append(" lfMsg ");

		tableSql.append(" LEFT JOIN ").append(TableLfWeiAccount.TABLE_NAME).append(" lfAccount ").append(" ON ");
		tableSql.append(" lfMsg.A_ID ").append("=").append("lfAccount." + TableLfWeiAccount.A_ID);
		tableSql.append(" LEFT JOIN ").append(TableLfWeiUserInfo.TABLE_NAME).append(" lfuser ").append("ON ");
		tableSql.append(" lfMsg.WC_ID ").append("=").append("lfuser." + TableLfWeiUserInfo.WC_ID);
		tableSql.append(" WHERE  ");

		StringBuffer conSql = new StringBuffer();
		conSql.append("  lfAccount.").append(TableLfWeiAccount.CORP_CODE).append("='").append(corpCode).append("'");
		// 排序
		String orderbySql = " order by lfMsg.CREATETIME DESC ";
		// 返回结果
		String sql = fieldSql + tableSql.toString() + conSql.toString() + orderbySql;

		String countSql = "select count(*) totalcount " + tableSql.toString() + conSql.toString();
		return new DataAccessDriver().getGenericDAO().findPageDynaBeanBySQL(sql, countSql, pageInfo, StaticValue.EMP_POOLNAME, null);
	}

	/**
	 * 创建历史消息
	 * 
	 * @param msg
	 * @return
	 * @throws SQLException
	 */
	public void createMsg(LfWeiMsg msg) throws SQLException
	{
		String tableName = "";
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		// 获取当前年，月
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if(month > 0 && month <= 9)
		{
			tableName = "LFWCMSG" + String.valueOf(year) + "0" + String.valueOf(month);
		}
		else
		{
			tableName = "LFWCMSG" + String.valueOf(year) + String.valueOf(month);
		}

		StringBuffer insertSql = new StringBuffer();
		insertSql.append("INSERT INTO ").append(tableName).append(" ");

		insertSql.append("(").append(TableLfWeiMsg.MSG_ID).append(",");
		insertSql.append(TableLfWeiMsg.MSG_TYPE).append(",");
		insertSql.append(TableLfWeiMsg.WC_ID).append(",");
		insertSql.append(TableLfWeiMsg.A_ID).append(",");
		insertSql.append(TableLfWeiMsg.TYPE).append(",");
		insertSql.append(TableLfWeiMsg.MSG_TEXT).append(",");
		insertSql.append(TableLfWeiMsg.MSG_XML).append(',');
		insertSql.append(TableLfWeiMsg.PARENT_ID).append(',');
		insertSql.append(TableLfWeiMsg.CREATETIME).append(',');
		insertSql.append(TableLfWeiMsg.CORP_CODE);
		insertSql.append(')');
		insertSql.append(" VALUES ");
		insertSql.append("(?,?,?,?,?,?,?,?,?,?)");
		Connection conn = null;
		PreparedStatement ps = null;
		try
		{
			conn = connectionManager.getDBConnection(StaticValue.EMP_POOLNAME);
			ps = conn.prepareStatement(insertSql.toString());

			ps.setLong(1, msg.getMsgId());
			ps.setInt(2, msg.getMsgType());
			ps.setLong(3, msg.getWcId());
			ps.setLong(4, msg.getAId());
			ps.setInt(5, msg.getType());
			String msgText = msg.getMsgText();
			if(msgText == null || "".equals(msgText))
			{
				ps.setString(6, "-");
			}
			else
			{
				ps.setString(6, msgText);
			}
			ps.setString(7, msg.getMsgXml());
			ps.setLong(8, msg.getParentId() == null ? 0l : msg.getParentId());
			ps.setTimestamp(9, msg.getCreateTime());
			ps.setString(10, msg.getCorpCode());
			ps.execute();
		}
		catch (Exception ex)
		{
			EmpExecutionContext.error(ex, "创建历史消息失败");
		}
		finally
		{
			try
			{
				close(null, ps, conn);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "创建历史消息关闭连接失败");
			}
		}
	}

	/**
	 * 通过月份获取当月的天数
	 * 
	 * @param mon
	 * @return
	 */
	private int getMonDay(String mon)
	{
		int day = 30;
		if("01".equals(mon) || "1".equals(mon))
		{
			day = 31;
		}
		else if("02".equals(mon) || "2".equals(mon))
			{
				day = 29;
			}
			else if("03".equals(mon) || "3".equals(mon))
				{
					day = 31;
				}
				else if("04".equals(mon) || "4".equals(mon))
					{
						day = 30;
					}
					else if("05".equals(mon) || "5".equals(mon))
						{
							day = 31;
						}
						else if("06".equals(mon) || "6".equals(mon))
							{
								day = 30;
							}
							else if("07".equals(mon) || "7".equals(mon))
								{
									day = 31;
								}
								else if("08".equals(mon) || "8".equals(mon))
									{
										day = 31;
									}
									else if("09".equals(mon) || "9".equals(mon))
										{
											day = 30;
										}
										else if("10".equals(mon))
											{
												day = 31;
											}
											else if("11".equals(mon))
												{
													day = 30;
												}
												else if("12".equals(mon))
													{
														day = 31;
													}
		return day;
	}
}
