package com.montnets.emp.notice.dao;

import com.montnets.emp.notice.vo.LfNoticeVo;
import com.montnets.emp.table.notice.TableLfNotice;
import com.montnets.emp.table.sysuser.TableLfSysuser;


/**
 * 通知公告
 * @project montnets_dao
 * @author tanglili <jack860127@126.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-9-29 下午02:05:54
 * @description
 */
public class LfNoticeVoSQL
{
	
	/**
	 * 获取查询字段
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接查询字段
		String fieldSql = new StringBuffer("select lfnotice.").append(
				TableLfNotice.NOTICE_ID).append(",lfnotice.").append(
				TableLfNotice.USER_ID).append(",lfnotice.").append(
				TableLfNotice.TITLE).append(",lfnotice.").append(
				TableLfNotice.CONTEXT).append(",lfnotice.").append(
				TableLfNotice.PUBLISH_TIME).append(",lfsysuser.").append(
				TableLfSysuser.NAME).toString();
		//返回查询字段字符串
		return fieldSql;
	}

	/**
	 * 获取查询表名sql
	 * @return
	 */
	public static String getTableSql()
	{
		//拼接sql
		String tableSql = new StringBuffer(" from ").append(
				TableLfNotice.TABLE_NAME).append(" lfnotice inner join ")
				.append(TableLfSysuser.TABLE_NAME).append(
						" lfsysuser on lfnotice.")
				.append(TableLfNotice.USER_ID).append("=lfsysuser.").append(
						TableLfSysuser.USER_ID).toString();
		//返回查询表名字符串
		return tableSql;
	}

	/**
	 * 
	 * 获取查询条件sql
	 * @param accountMonitorVo
	 * @return
	 */
	public static String getConditionSql(LfNoticeVo lfNoticeVo)
	{
		StringBuffer conditionSql = new StringBuffer();		
		if (lfNoticeVo.getCorpCode() != null
				&& !"".equals(lfNoticeVo.getCorpCode()))
		{
			//拼接sql
			conditionSql.append(" and lfsysuser.").append(TableLfSysuser.CORP_CODE)
					.append("='").append(lfNoticeVo.getCorpCode()).append("'");
		}

		String sql = conditionSql.toString();
		//返回查询条件字符串
		return sql;
	}
	
	/**
	 * 获取排序sql
	 * @return
	 */
	public static String getOrderBySql()
	{
		//拼接sql
		String orderBy = new StringBuffer(" order by lfnotice.").append(
				TableLfNotice.PUBLISH_TIME).append(" desc").toString();
		return orderBy;
	}
}
