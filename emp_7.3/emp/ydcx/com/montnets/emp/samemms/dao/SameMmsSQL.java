package com.montnets.emp.samemms.dao;

import com.montnets.emp.mmstemplate.dao.TemplateSQL;
import com.montnets.emp.samemms.vo.LfTemplateVo;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.table.template.TableLfTemplate;

public class SameMmsSQL {
	/**
	 * 获取查询字段
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接查询字段
		String fieldSql = new StringBuffer("select distinct lftemplate.* ,sysuser.")
				.append(TableLfSysuser.NAME).append(",sysuser.").append(
						TableLfSysuser.USER_NAME).append(",sysuser.")
				.append(TableLfSysuser.USER_STATE)
				.append(",lfdep.").append(TableLfDep.DEP_NAME).append(",0 as ").append(TableLfTmplRela.id)
				.append(",1 as ").append(TableLfTmplRela.shareType)
				.toString();
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
//		String tableSql = new StringBuffer(" from ").append(
//				TableLfTemplate.TABLE_NAME).append(" lftemplate left join ")
//				.append(TableLfSysuser.TABLE_NAME).append(
//						" sysuser  on lftemplate.").append(
//						TableLfTemplate.USER_ID).append("= sysuser.").append(
//						TableLfSysuser.USER_ID).append(" left join ").append(
//						TableLfDep.TABLE_NAME).append(" lfdep on sysuser.")
//				.append(TableLfSysuser.DEP_ID).append("=lfdep.").append(
//						TableLfDep.DEP_ID).toString();
		//返回查询表名字符串
		return TemplateSQL.getTableSql();
	}

	/**
	 * 获取共享条件sql
	 * @param loginUserId
	 * @return
	 */
	public static String getDominationSql(Long loginUserId)
	{
		//拼接sql
//		StringBuffer domination = new StringBuffer("select ").append(
//				TableLfDomination.DEP_ID).append(" from ").append(
//				TableLfDomination.TABLE_NAME).append(" where ").append(
//				TableLfDomination.USER_ID).append("=").append(loginUserId);
//		String dominationSql = new StringBuffer(" where (sysuser.").append(
//				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
//				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
//				.append(domination).append("))").toString();
		return TemplateSQL.getDominationSql(loginUserId)+TemplateSQL.getShareSql(loginUserId)+")";
	}
	
	/**
	 * 查询条件
	 * @param lfTemplateVo
	 * @return
	 */
	public static String getConditionSql(LfTemplateVo lfTemplateVo)
	{
		StringBuffer conditionSql = new StringBuffer();

		//查询条件---类型
		if (lfTemplateVo.getDsflag() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DS_FLAG).append("=").append(
					lfTemplateVo.getDsflag());
		}
		//查询条件---状态
		if (lfTemplateVo.getTmState() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_STATE).append("=").append(
					lfTemplateVo.getTmState());
		}
		//查询条件---姓名
		if (lfTemplateVo.getName() != null
				&& !"".equals(lfTemplateVo.getName())) {
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append(" like '%").append(lfTemplateVo.getName().trim())
					.append("%'");
		}
		//查询条件---模板内容
		if (lfTemplateVo.getTmMsg() != null
				&& !"".equals(lfTemplateVo.getTmMsg())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_MSG).append(" like '%").append(
					lfTemplateVo.getTmMsg()).append("%'");
		}
		//查询条件---模板名称
		if (lfTemplateVo.getTmName() != null
				&& !"".equals(lfTemplateVo.getTmName())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_NAME).append(" like '%").append(
					lfTemplateVo.getTmName()).append("%'");
		}
		//查询条件---类型
		if (lfTemplateVo.getTmpType() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TMP_TYPE).append("=").append(
					lfTemplateVo.getTmpType());
		}
		//查询条件---审核是否通过
		if (lfTemplateVo.getIsPass() != null) {
			if (lfTemplateVo.getIsPass() == 5) {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append(" in (0,1)");
			} else {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append("=").append(
						lfTemplateVo.getIsPass());
			}
		}
		//查询条件---操作员id
		if (lfTemplateVo.getUserId() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.USER_ID).append("=").append(
					lfTemplateVo.getUserId());
		}
		//查询条件---模板类型
//		if (lfTemplateVo.getDsflag() != null
//				&& !"".equals(lfTemplateVo.getDsflag())) {
//			conditionSql.append(" and lftemplate.").append(
//					TableLfTemplate.DS_FLAG).append("=").append(
//					lfTemplateVo.getDsflag());
//		}
		//查询条件----运营商审批状态
		if (lfTemplateVo.getAuditstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.AUDITSTATUS).append("=").append(
					lfTemplateVo.getAuditstatus());
		}
		//查询条件----运营商提交状态
		if (lfTemplateVo.getSubmitstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.SUBMITSTATUS).append("=").append(
					lfTemplateVo.getSubmitstatus());
		}
		//返回拼接sql
		return conditionSql.toString();
	}

	/**
	 * 获取排序sql
	 * @return
	 */
	public static String getOrderBySql()
	{
		//拼接sql
		String orderBySql = new StringBuffer(" order by lftemplate.").append(
				TableLfTemplate.ADD_TIME).append(" desc").toString();
		return orderBySql;
	}
}
