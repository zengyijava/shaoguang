package com.montnets.emp.rms.commontempl.dao;

import com.montnets.emp.rms.commontempl.entity.LfIndustryUse;
import com.montnets.emp.rms.commontempl.table.TableLfIndustryUse;

public class IndustryUseSql {

	// 查询字段sql
	public static String getFiledSql() {
		StringBuffer filedSql = new StringBuffer("SELECT ");
		filedSql.append(TableLfIndustryUse.TABLE_NAME).append(".")
				.append(TableLfIndustryUse.ID).append(" id").append(",")
				.append(TableLfIndustryUse.TABLE_NAME).append(".")
				.append(TableLfIndustryUse.NAME).append(" name");
		return filedSql.toString();
	}

	// 表名sql
	public static String getTableSql() {
		StringBuffer tableSql = new StringBuffer(" FROM ");
		tableSql.append(TableLfIndustryUse.TABLE_NAME);
		return tableSql.toString();

	}

	// 查询条件sql

	public static String getConditionSql(LfIndustryUse lfIndustryUse ) {
		StringBuffer conditionSql = new StringBuffer(" 	WHERE  1=1 ");

		 
		return conditionSql.toString();

	}

}
