package com.montnets.emp.tczl.dao;

import com.montnets.emp.common.dao.DepDAO;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.table.pasgroup.TableAcmdRoute;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.ydyw.TableLfBusTaoCan;
import com.montnets.emp.tczl.table.TableLfTaocanCmd;
import com.montnets.emp.tczl.vo.LfTaocanCmdVo;

public class ydyw_pkgInstructionSetSql
{

	/**
	 * FunName:组装查询字段 Description:
	 * 
	 * @param
	 * @retuen String
	 */
	public static String getFieldSql()
	{
		// 拼接sql
		String sql = new StringBuffer("select taocancmd.").append(
				TableLfTaocanCmd.ID).append(",bustaocan.").append(
				TableLfBusTaoCan.TAOCAN_NAME).append(",taocancmd.").append(
				TableLfTaocanCmd.TAOCAN_CODE).append(",taocancmd.").append(
				TableLfTaocanCmd.TAOCAN_TYPE).append(",taocancmd.").append(
				TableLfTaocanCmd.STRUCT_TYPE).append(",taocancmd.").append(
				TableLfTaocanCmd.STRUCTCODE).append(",userdata.").append(
				TableUserdata.USER_ID).append(",acmdroute.").append(TableAcmdRoute.SP_ID)
				.append(",acmdroute.").append(TableAcmdRoute.ID).append(" as ACID ")
				.append(",taocancmd.").append(
						TableLfTaocanCmd.TAOCAN_MONEY).append(",taocancmd.").append(
								TableLfTaocanCmd.CORP_CODE).append(",taocancmd.").append(
				TableLfTaocanCmd.CREATE_TIME).append(",taocancmd.").append(
				TableLfTaocanCmd.UPDATE_TIME).append(",taocancmd.").append(
				TableLfTaocanCmd.DEP_ID).append(",taocancmd.").append(
				TableLfTaocanCmd.USER_ID).append(",sysuser.").append(
				TableLfSysuser.NAME).append(",sysuser.").append(
				TableLfSysuser.USER_NAME).append(",dep.").append(
				TableLfDep.DEP_NAME).toString();
		return sql;
	}

	/**
	 * FunName:组装表拼接 Description:
	 * 
	 * @param
	 * @retuen String
	 */
	public static String getTableSql()
	{
		String sql = new StringBuffer(" from ").append(
				TableLfTaocanCmd.TABLE_NAME).append(" taocancmd left join ")
				.append(TableLfBusTaoCan.TABLE_NAME).append(
				" bustaocan on taocancmd.").append(
						TableLfTaocanCmd.TAOCAN_CODE).append("=bustaocan.")
				.append(TableLfBusTaoCan.TAOCAN_CODE).append(" left join ")
				.append(TableAcmdRoute.TABLE_NAME).append(
						" acmdroute on taocancmd.").append(
						TableLfTaocanCmd.STRUCTCODE).append("=acmdroute.")
				.append(TableAcmdRoute.STRUCTCODE).append(" left join ")
				.append(TableUserdata.TABLE_NAME).append(
						" userdata on acmdroute.").append(TableAcmdRoute.SP_ID)
				.append("=userdata.").append(TableUserdata.UID).append(
						" left join ").append(TableLfSysuser.TABLE_NAME)
				.append(" sysuser on taocancmd.").append(
						TableLfTaocanCmd.USER_ID).append("=sysuser.").append(
						TableLfSysuser.USER_ID).append(" left join ").append(
						TableLfDep.TABLE_NAME).append(" dep on taocancmd.")
				.append(TableLfTaocanCmd.DEP_ID).append("=dep.").append(
						TableLfDep.DEP_ID).append(" where").toString();
		return sql;
	}

	/**
	 * FunName:组装过滤条件
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getConditionSql(LfTaocanCmdVo lfTaocanCmdVo)throws Exception
	{
		StringBuffer conditionSql = new StringBuffer();
		//套餐名称
		if (lfTaocanCmdVo.getTaocanName() != null && !"".equals(lfTaocanCmdVo.getTaocanName()))
		{
			conditionSql.append(" and bustaocan.").append(TableLfBusTaoCan.TAOCAN_NAME)
					.append(" like '%").append(lfTaocanCmdVo.getTaocanName()).append("%'");
		}
		//套餐编号
		if (lfTaocanCmdVo.getTaocanCode() != null && !"".equals(lfTaocanCmdVo.getTaocanCode()))
		{
			conditionSql.append(" and taocancmd.").append(TableLfTaocanCmd.TAOCAN_CODE)
			.append(" like '%").append(lfTaocanCmdVo.getTaocanCode()).append("%'");
		}
		//指令
		if (lfTaocanCmdVo.getStructcode() != null && !"".equals(lfTaocanCmdVo.getStructcode()))
		{
			conditionSql.append(" and lower(taocancmd.").append(TableLfTaocanCmd.STRUCTCODE)
			.append(") like '%").append(lfTaocanCmdVo.getStructcode().toLowerCase()).append("%'");
		}
		//指令类型
		if (lfTaocanCmdVo.getStructType() != null)
		{
			conditionSql.append(" and taocancmd.").append(TableLfTaocanCmd.STRUCT_TYPE)
			.append("=").append(lfTaocanCmdVo.getStructType());
		}
		//SP账号
		if (lfTaocanCmdVo.getSpUser() != null && !"".equals(lfTaocanCmdVo.getSpUser()))
		{
			conditionSql.append(" and userdata.").append(TableUserdata.UID)
			.append("=").append(lfTaocanCmdVo.getSpUser());
		}
		//操作员名称
		if (lfTaocanCmdVo.getName() != null && !"".equals(lfTaocanCmdVo.getName()))
		{
			conditionSql.append(" and sysuser.").append(TableLfSysuser.NAME)
					.append(" like '%").append(lfTaocanCmdVo.getName()).append("%'");
		}
		//计费类型
		if (lfTaocanCmdVo.getTaocanType() != null)
		{
			conditionSql.append(" and taocancmd.").append(TableLfTaocanCmd.TAOCAN_TYPE)
			.append("=").append(lfTaocanCmdVo.getTaocanType());
		}
		//操作员ID
		if (lfTaocanCmdVo.getUserId() != null)
		{
			conditionSql.append(" and taocancmd.").append(TableLfTaocanCmd.USER_ID)
			.append("=").append(lfTaocanCmdVo.getUserId());
		}
		//操作员机构ID
		if (lfTaocanCmdVo.getDepId() != null)
		{
			//能查看所在机构节点及以下机构的记录
			String depid=new DepDAO().getChildUserDepByParentID(lfTaocanCmdVo.getDepId(),TableLfDep.DEP_ID);
			conditionSql.append(" and ").append(depid.replaceAll("DEP_ID","taocancmd.DEP_ID"));
		}
		//企业编码
		if (lfTaocanCmdVo.getCorpCode() != null && !"".equals(lfTaocanCmdVo.getCorpCode()))
		{
			conditionSql.append(" and taocancmd.").append(TableLfTaocanCmd.CORP_CODE)
			.append("='").append(lfTaocanCmdVo.getCorpCode()).append("'");
		}
		IGenericDAO genericDao =new DataAccessDriver().getGenericDAO();
		//开始查询时间
		if (lfTaocanCmdVo.getStartSubmitTime() != null
				&& !"".equals(lfTaocanCmdVo.getStartSubmitTime()))
		{
			conditionSql.append(" and taocancmd.").append(
					TableLfTaocanCmd.CREATE_TIME).append(">=").append(genericDao.getTimeCondition(lfTaocanCmdVo.getStartSubmitTime()));
		}
		//结束查询时间
		if (lfTaocanCmdVo.getEndSubmitTime() != null
				&& !"".equals(lfTaocanCmdVo.getEndSubmitTime()))
		{
			conditionSql.append(" and taocancmd.").append(
					TableLfTaocanCmd.CREATE_TIME).append("<=").append(genericDao.getTimeCondition(lfTaocanCmdVo.getEndSubmitTime()));
		}
		String sql = conditionSql.toString();
		return sql;
	}
	
	/**
	 * FunName:组装排序条件
	 * Description:
	 * @param 
	 * @retuen String
	 */
	public static String getOrderBySql()
	{
		String sql = new StringBuffer(" order by taocancmd.").append(
				TableLfTaocanCmd.CREATE_TIME).append(" desc").toString();
		return sql;
	}
	
}
