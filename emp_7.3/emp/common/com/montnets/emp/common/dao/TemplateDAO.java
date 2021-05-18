/**
 * Program  : TemplateDAO.java
 * Author   : zousy
 * Create   : 2013-12-17 上午10:51:58
 * company ShenZhen Montnets Technology CO.,LTD.
 *
 */

package com.montnets.emp.common.dao;

import java.util.ArrayList;
import java.util.List;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.template.LfTemplate;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfDomination;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.template.TableLfTemplate;
import com.montnets.emp.table.template.TableLfTmplRela;
import com.montnets.emp.util.PageInfo;

/**
 * 
 * @author   Administrator <510061684@qq.com>
 * @version  1.0.0
 * @2013-12-17 上午10:51:58
 */
public class TemplateDAO extends SuperDAO
{
	public List<LfTemplate> findLfTemplateVoList(Long loginUserId,
			LfTemplate template,int templType,String dsflag, PageInfo pageInfo)
	{
		//获取查询SQL
		String fieldSql = getFieldSql();
		//获取表SQL
		String tableSql = getTableSql(templType);
		//获取审核的SQL
		String dominationSql = getDominationSql(loginUserId);
		//获取查询SQL
		String conditionSql = getConditionSql(template,dsflag);
		//获取排序SQL
		String orderBySql = getOrderBySql();
		//拼接SQL
		String sql = new StringBuffer(fieldSql).append(tableSql).append(
				dominationSql).append(conditionSql).append(orderBySql)
				.toString();
		String countSql = null;
		//查询数据
		List<LfTemplate> returnList =new ArrayList<LfTemplate>();
		try
		{
			if(pageInfo!=null){
				countSql = new StringBuffer("select count(distinct lftemplate.").append(TableLfTemplate.TM_ID).append(") totalcount ")
						.append(tableSql).append(dominationSql).append(conditionSql)
						.toString();
				returnList = new DataAccessDriver().getGenericDAO().findPageEntityListBySQL(
						LfTemplate.class, sql, countSql, pageInfo,
						StaticValue.EMP_POOLNAME);
			}else{
				returnList = this.findEntityListBySQL(LfTemplate.class, sql, StaticValue.EMP_POOLNAME);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询模板信息出现异常！");
		}
		//返回
		return returnList;
	}
	
	/**
	 * 获取查询字段
	 * @return
	 */
	public static String getFieldSql()
	{
		//拼接查询字段
		String fieldSql = new StringBuffer("select distinct lftemplate.* ").toString();
		//返回查询字段字符串
		return fieldSql;
	}
	/**
	 * @description    
	 * @param templType 1:短信  2 彩信
	 * @return       			 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2013-11-29 下午01:49:56
	 */
	public static String getTableSql(int templType)
	{
		//拼接sql
		String tableSql = new StringBuffer(" from ").
		append(TableLfTmplRela.TABLE_NAME).append(" lfalltemplate inner join ")
		.append(
				TableLfTemplate.TABLE_NAME).append(" lftemplate on lfalltemplate.")
				.append(TableLfTmplRela.templId).append("= lftemplate.")
				.append(TableLfTemplate.TM_ID).append(" and lfalltemplate.")
				.append(TableLfTmplRela.templType).append(" = ").append(templType)
				.append(" left join ").append(TableLfSysuser.TABLE_NAME).append(
						" sysuser  on lftemplate.").append(
						TableLfTemplate.USER_ID).append("= sysuser.").append(
						TableLfSysuser.USER_ID)
						.append(" left join ").append(
						TableLfDep.TABLE_NAME).append(" lfdep on sysuser.")
				.append(TableLfSysuser.DEP_ID).append("=lfdep.").append(
						TableLfDep.DEP_ID)
						.toString();
		//返回查询表名字符串
		return tableSql;
	}
	
	
	/**
	 * 获取共享条件sql
	 * @param loginUserId
	 * @return
	 */
	public static String getDominationSql(Long loginUserId)
	{
		return getOldDominationSql(loginUserId)+getShareSql(loginUserId)+")";
	}
	/**
	 * 机构权限sql拼接
	 * @param loginUserId
	 * @return
	 */
	public static String getOldDominationSql(Long loginUserId)
	{
		//拼接sql
		StringBuffer domination = new StringBuffer("select ").append(
				TableLfDomination.DEP_ID).append(" from ").append(
				TableLfDomination.TABLE_NAME).append(" where ").append(
				TableLfDomination.USER_ID).append("=").append(loginUserId);
		String dominationSql = new StringBuffer(" where (sysuser.").append(
				TableLfSysuser.USER_ID).append("=").append(loginUserId).append(
				" or sysuser.").append(TableLfSysuser.DEP_ID).append(" in (")
				.append(domination).append(")").toString();
		return dominationSql;
	}
	
	//获得共享模板sql
	public static String getShareSql(Long loginUserId)
	{
		//拼接sql
		StringBuffer share = new StringBuffer("select ").append(
			TableLfSysuser.DEP_ID).append(" from ").append(
			TableLfSysuser.TABLE_NAME).append(" where ").append(
			TableLfSysuser.USER_ID).append("=").append(loginUserId);
		String shareSql = new StringBuffer(" or (lfalltemplate.").append(
				TableLfTmplRela.toUserType).append("=").append(1).append(
				" and lfalltemplate.").append(TableLfTmplRela.toUser).append(" in (")
				.append(share).append("))").
				append(" or (lfalltemplate.").append(
				TableLfTmplRela.toUserType).append("=").append(2).append(
				" and lfalltemplate.").append(TableLfTmplRela.toUser).append(" =")
				.append(loginUserId).append(")").toString();
		return shareSql;
	}
	
	/**
	 * 查询条件
	 * @param template 
	 * @param dsflag  支持多种类型同时查询  逗号分隔
	 * @return
	 */
	public static String getConditionSql(LfTemplate template,String dsflag)
	{
		StringBuffer conditionSql = new StringBuffer();

		//查询条件---类型
		if (dsflag != null
				&& !"".equals(dsflag)) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.DS_FLAG).append(" in (").append(
							dsflag).append(")");
		}
		//查询条件---模板内容
		if (template.getTmMsg() != null
				&& !"".equals(template.getTmMsg())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_MSG).append(" like '%").append(
							template.getTmMsg()).append("%'");
		}
		//查询条件---模板名称
		if (template.getTmName() != null
				&& !"".equals(template.getTmName())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_NAME).append(" like '%").append(
							template.getTmName()).append("%'");
		}
		//查询条件---类型
		if (template.getTmpType() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TMP_TYPE).append("=").append(
							template.getTmpType());
		}
		//查询条件---审核是否通过
		if (template.getIsPass() != null) {
			if (template.getIsPass() == 5) {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append(" in (0,1)");
			} else {
				conditionSql.append(" and lftemplate.").append(
						TableLfTemplate.ISPASS).append("=").append(
								template.getIsPass());
			}
		}
		//查询条件---操作员id
		if (template.getUserId() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.USER_ID).append("=").append(
							template.getUserId());
		}
		//查询条件----运营商审批状态
		if (template.getAuditstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.AUDITSTATUS).append("=").append(
							template.getAuditstatus());
		}
		//查询条件----运营商提交状态
		if (template.getSubmitstatus() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.SUBMITSTATUS).append("=").append(
							template.getSubmitstatus());
		}
		//查询条件----模板启用 禁用 
		if (template.getTmState() != null) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.TM_STATE).append("=").append(
							template.getTmState());
		}
		//查询条件----企业编号
		if (template.getCorpCode()!= null&&!"".equals(template.getCorpCode())) {
			conditionSql.append(" and lftemplate.").append(
					TableLfTemplate.CORP_CODE).append("= '").append(
							template.getCorpCode()).append("'");
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

