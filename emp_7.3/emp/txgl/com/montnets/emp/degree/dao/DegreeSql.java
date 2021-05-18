package com.montnets.emp.degree.dao;

import com.montnets.emp.degree.table.TableLfDegree;
import com.montnets.emp.degree.vo.LfDegreeManageVo;
import com.montnets.emp.util.PageInfo;

public class DegreeSql {
	/**
	 * 获取字段
	 * @param
	 * @retuen String
	 */
	public static String getFieldSql() {
		// 拼接sql
		String getFieldSql = new StringBuffer("select ")
				.append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE).append(" degree")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE_BEGIN).append(" degreeBegin")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.ID).append(" id")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE_END).append(" degreeEnd")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.USER_ID).append(" userId")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.CREATE_TIME).append(" createTime")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.VALID_DATE_BEGIN).append(" validDateBegin")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.VALID_DATE_END).append(" validDateEnd")
				.append("," + TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.STATUS).append(" status")
				.append("," + "LF_SYSUSER").append(".").append("NAME").append(" userName").toString();
		return getFieldSql;
	}

	/**
	 * 获取表名
	 * @param
	 * @retuen String
	 */
	public static String getTableSql() {
		// 拼接sql
		String getTableSql = new StringBuffer(" from ")
				.append(TableLfDegree.TABLE_NAME)
				.append(" left  join ")
				.append(" LF_SYSUSER")
				.append(" on ")
				.toString();
		return getTableSql;
	}

	/**
	 * 获取查询条件 
	 * @param
	 * @retuen String
	 */
	public static String getConditionSql(LfDegreeManageVo lfDegreeManageVo , String orderBy,PageInfo pageinfo) {
		StringBuffer conditionSql = new StringBuffer();
		conditionSql.append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.USER_ID).append("=")
						.append("LF_SYSUSER.USER_ID");

		boolean boo1 = false;
		boolean boo2 = false;
		//计费档位
		if (lfDegreeManageVo.getDegree() != null ) {
			if(orderBy.equals("asc")){
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE)
					.append(" >").append(lfDegreeManageVo.getDegree());
			}else if(orderBy.equals("desc")){
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE)
					.append(" <").append(lfDegreeManageVo.getDegree());
			}else{				
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE)
					.append(" =").append(lfDegreeManageVo.getDegree());
			}
			boo1 = true;
		}
		
		// 状态
		if (lfDegreeManageVo.getStatus() != null ) {
			if(boo1){
				conditionSql.append(" and ").append(TableLfDegree.TABLE_NAME)
				.append(".").append(TableLfDegree.STATUS).append(" =")
				.append(lfDegreeManageVo.getStatus());
			} else {
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME)
				.append(".").append(TableLfDegree.STATUS).append(" =")
				.append(lfDegreeManageVo.getStatus());
			}
			boo2 = true;
		}
				
		//创建人
		if (lfDegreeManageVo.getUserName() != null
				&&!"".equals(lfDegreeManageVo.getUserName())) {
			if(boo1 || boo2){
				conditionSql.append(" and ").append("LF_SYSUSER.NAME")
				.append(" like '%").append(lfDegreeManageVo.getUserName()).append("%'");
			}else{
				conditionSql.append(" where ").append("LF_SYSUSER.NAME")
				.append(" like '%").append(lfDegreeManageVo.getUserName()).append("%'");
				
				if(pageinfo.getPageIndex()>1){
					conditionSql.setLength(0);
					conditionSql.append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.USER_ID).append("=")
					.append("LF_SYSUSER.USER_ID")
					.append(" and ");
					conditionSql.append(" ").append("LF_SYSUSER.CORP_CODE")
					.append(" ='").append(lfDegreeManageVo.getCorpCode()).append("'")
					.append(" and ").append("LF_SYSUSER.NAME")
					.append(" like '%").append(lfDegreeManageVo.getUserName()).append("%'");
				}
			}						
			
		}
		

		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 * 获取排序条件 
	 * @param
	 * @retuen String
	 */
	public static String getOrderBySql(String orderBy) {
		String sql = "";
		if(orderBy.equals("asc")){
			sql = new StringBuffer(" order by ")
			.append(TableLfDegree.TABLE_NAME).append(".")
			.append(TableLfDegree.DEGREE).append(" asc").toString();
		} else if (orderBy.equals("desc")) {
			sql = new StringBuffer(" order by ")
			.append(TableLfDegree.TABLE_NAME).append(".")
			.append(TableLfDegree.DEGREE).append(" desc").toString();
		} else {			
			sql = new StringBuffer(" order by ")
			.append(TableLfDegree.TABLE_NAME).append(".")
			.append(TableLfDegree.STATUS).append(" asc")
			.append(" , ")
			.append(TableLfDegree.ID).append(" desc").toString();
		}
		return sql;
	}
	
	/**
	 * 获取查询条件 
	 * @param
	 * @retuen String
	 */
	public static String getConditionSql(LfDegreeManageVo lfDegreeManageVo , String orderBy) {
		StringBuffer conditionSql = new StringBuffer();
		conditionSql.append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.USER_ID).append("=")
						.append("LF_SYSUSER.USER_ID");

		boolean boo1 = false;
		boolean boo2 = false;
		//计费档位
		if (lfDegreeManageVo.getDegree() != null  ) {
			if(orderBy.equals("asc")){
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE)
					.append(" >").append(lfDegreeManageVo.getDegree());
			}else if(orderBy.equals("desc")){
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE)
					.append(" <").append(lfDegreeManageVo.getDegree());
			}else{				
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME).append(".").append(TableLfDegree.DEGREE)
					.append(" =").append(lfDegreeManageVo.getDegree());
			}
			boo1 = true;
		}
		
		// 状态
		if (lfDegreeManageVo.getStatus() != null ) {
			if(boo1){
				conditionSql.append(" and ").append(TableLfDegree.TABLE_NAME)
				.append(".").append(TableLfDegree.STATUS).append(" =")
				.append(lfDegreeManageVo.getStatus());
			} else {
				conditionSql.append(" where ").append(TableLfDegree.TABLE_NAME)
				.append(".").append(TableLfDegree.STATUS).append(" =")
				.append(lfDegreeManageVo.getStatus());
			}
			boo2 = true;
		}

		//企业编码
		if(lfDegreeManageVo.getCorpCode() != null
				&&!"".equals(lfDegreeManageVo.getCorpCode())) {
			conditionSql.append(" and ").append("LF_SYSUSER.CORP_CODE")
			.append(" ='").append(lfDegreeManageVo.getCorpCode()).append("'");
		}
				
		//创建人
		if (lfDegreeManageVo.getUserName() != null
				&&!"".equals(lfDegreeManageVo.getUserName())) {
			if(boo1 || boo2){
				conditionSql.append(" and ").append("LF_SYSUSER.NAME")
				.append(" ='").append(lfDegreeManageVo.getUserName()).append("'");
			}else{
				conditionSql.append(" where ").append("LF_SYSUSER.NAME")
				.append(" ='").append(lfDegreeManageVo.getUserName()).append("'");
				
			}						
			
		}

		String sql = conditionSql.toString();
		return sql;
	}

}
