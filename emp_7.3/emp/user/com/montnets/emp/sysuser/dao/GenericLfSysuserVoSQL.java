/**
 * 
 */
package com.montnets.emp.sysuser.dao;

import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * 操作员DAOsql
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-30 下午03:23:37
 * @description 
 */

public class GenericLfSysuserVoSQL
{
	/**
	 * 获取查询条件Sql	tring
	 * @param lfSpDepBindVo
	 * @return
	 */
	public static String getConditionSql(LfSysuserVo lfSysuserVo)
	{
		StringBuffer conditionSql = new StringBuffer();
		
		
		if (lfSysuserVo.getName() != null
				&& !"".equals(lfSysuserVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.NAME).append(" like '%").append(
					lfSysuserVo.getName()).append("%'");
		}
		
		if (lfSysuserVo.getUserName() != null
				&& !"".equals(lfSysuserVo.getUserName()))
		{
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.USER_NAME).append(" like '%").append(
					lfSysuserVo.getUserName()).append("%'");
		}
		//状态
		if (lfSysuserVo.getUserState() != null) {
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.USER_STATE).append(" =").append(
					lfSysuserVo.getUserState());
		}
		
		//创建者查询
		if (lfSysuserVo.getHolder() != null && !"".equals(lfSysuserVo.getHolder()))
		{
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.HOLDER).append(" like '%").append(
					lfSysuserVo.getHolder()).append("%'");
		}
		//手机
		if (lfSysuserVo.getMobile() != null && !"".equals(lfSysuserVo.getMobile()))
		{
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.MOBILE).append(" like '%").append(
					lfSysuserVo.getMobile()).append("%'");
		}
		//创建时间起始
		if (lfSysuserVo.getSubmitSartTime() != null && !"".equals(lfSysuserVo.getSubmitSartTime()))
		{
			String startTime = new DataAccessDriver().getGenericDAO().getTimeCondition(lfSysuserVo.getSubmitSartTime());
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.REG_TIME).append(" >= ").append(
							startTime);
		}
		
		//创建时间结束时间
		if (lfSysuserVo.getSubmitEndTime() != null && !"".equals(lfSysuserVo.getSubmitEndTime()))
		{
			String endTime = new DataAccessDriver().getGenericDAO().getTimeCondition(lfSysuserVo.getSubmitEndTime());
			conditionSql.append(" and sysuser.").append(
					TableLfSysuser.REG_TIME).append(" <= ").append(
							endTime);
		}
		//是否有子号
		if (lfSysuserVo.getIsExistSubno() != null)
		{
			
			if(lfSysuserVo.getIsExistSubno() == 1){
				conditionSql.append(" and sysuser.").append(
						TableLfSysuser.ISEXISTSUBNO).append(" = ").append(
						lfSysuserVo.getIsExistSubno());
			}else{
				conditionSql.append(" and sysuser.").append(
						TableLfSysuser.ISEXISTSUBNO).append(" <> 1");
			}
		}
		if(lfSysuserVo.getUserType() != null){
			if(lfSysuserVo.getUserType() == 2){
				conditionSql.append(" and sysuser.").append(TableLfSysuser.USER_TYPE).append("=2");
			}else{
				conditionSql.append(" and ( sysuser.").append(TableLfSysuser.USER_TYPE).append(" != 2 or sysuser.")
				.append(TableLfSysuser.USER_TYPE).append(" is null )");
			}
		}
		String sql = conditionSql.toString();
		return sql;
	}
}
