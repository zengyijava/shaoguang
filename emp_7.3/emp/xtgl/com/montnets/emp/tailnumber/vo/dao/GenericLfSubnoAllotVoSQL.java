/**
 * 
 */
package com.montnets.emp.tailnumber.vo.dao;

import com.montnets.emp.common.vo.LfSubnoAllotVo;
import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfPrivilege;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.tailnumber.TableLfSubnoAllot;

/**
 * @project sinolife
 * @author liujianjun <646654831@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-5-26 上午11:01:07
 * @description 
 */
public class GenericLfSubnoAllotVoSQL
{
	
	// LfSubnoAllotVo
	/**
	 * LfSubnoAllotVo
	 * 
	 * @return
	 */
	public static String getFieldSql(LfSubnoAllotVo subnoAllotVo)
	{
		//LfSubnoAllotVo表的字段
		StringBuffer subnoallotSql = new StringBuffer("select distinct lfsubnoallot.").append(
				TableLfSubnoAllot.SUID).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.ALLOT_TYPE).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.EXTEND_SUBNO_BEGIN).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.EXTEMD_SUBNO_END).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.USEDEXTEND_SUBNO).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.SP_NUMBER).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.SP_USER).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.CODES).append(",lfsubnoallot.").append(
				TableLfSubnoAllot.CODE_TYPE);
		
		StringBuffer otherSql = new StringBuffer("");

		if(subnoAllotVo.getCodeType() == 0){
			otherSql.append(",privilege.").append(TableLfPrivilege.MENU_NAME);
		}else if(subnoAllotVo.getCodeType() == 1){
			otherSql.append(",busmanager.").append(TableLfBusManager.BUS_NAME);
		}else if(subnoAllotVo.getCodeType() == 2){
			//otherSql.append(",products.").append(TableLfProducts.PRO_NAME);
		}else if(subnoAllotVo.getCodeType() == 3){
			otherSql.append(",dep.").append(TableLfDep.DEP_NAME);
		}else if(subnoAllotVo.getCodeType() == 4){
			otherSql.append(",sysuser.").append(TableLfSysuser.NAME);
		}
		
		String sql = subnoallotSql.append(otherSql).toString();
		return sql;
	}
	
	/**
	 * LfSubnoAllotVo
	 * 
	 * @return
	 */
	public static String getTableSql(LfSubnoAllotVo subnoAllotVo)
	{
		StringBuffer subnoallotSql = new StringBuffer(" from ").append(
				TableLfSubnoAllot.TABLE_NAME).append(" lfsubnoallot inner join ");
		
		StringBuffer otherSql = new StringBuffer("");

		if(subnoAllotVo.getCodeType() == 0){
			
			otherSql.append(TableLfPrivilege.TABLE_NAME)
			.append(" privilege on lfsubnoallot.")
			.append(TableLfSubnoAllot.CODES)
			.append("=privilege.")
			.append(TableLfPrivilege.MENU_CODE);
			
		}else if(subnoAllotVo.getCodeType() == 1){
			
			otherSql.append(TableLfBusManager.TABLE_NAME)
			.append(" busmanager on lfsubnoallot.")
			.append(TableLfSubnoAllot.CODES)
			.append("=busmanager.")
			.append(TableLfBusManager.BUS_CODE);
			
		}else if(subnoAllotVo.getCodeType() == 2){
			/*
			otherSql.append(TableLfProducts.TABLE_NAME)
			.append(" products on lfsubnoallot.")
			.append(TableLfSubnoAllot.CODES)
			.append("=products.")
			.append(TableLfProducts.PRO_ID);
			*/
		}
		
		else if(subnoAllotVo.getCodeType() == 4){
			otherSql.append(TableLfSysuser.TABLE_NAME)
			.append(" sysuser on lfsubnoallot.")
			.append(TableLfSubnoAllot.CODES)
			.append("=sysuser.")
			.append(TableLfSysuser.USER_NAME);
			
		}
		
		String sql = subnoallotSql.append(otherSql).toString();
		return sql;
	}
	
	/**
	 * 
	 * @param subnoAllotVo
	 * @return
	 */
	public static String getConditionSql(LfSubnoAllotVo subnoAllotVo)
	{
		
		StringBuffer conditionSql = new StringBuffer();
		
		
		if (subnoAllotVo.getUsedExtendSubno() != null
				&& !"".equals(subnoAllotVo.getUsedExtendSubno()))
		{
			conditionSql.append(" and lfsubnoallot.").append(
					TableLfSubnoAllot.USEDEXTEND_SUBNO).append("='").append(
							subnoAllotVo.getUsedExtendSubno()).append("'");
		}
		
		
		
		if (subnoAllotVo.getCorpCode()!= null
				&& !"".equals(subnoAllotVo.getCorpCode()))
		{
			conditionSql.append(" and lfsubnoallot.").append(
					TableLfSubnoAllot.CORP_CODE).append("='").append(
							subnoAllotVo.getCorpCode()).append("'");
		}
		
		
		if(subnoAllotVo.getCodeType() != null){
			conditionSql.append(" and lfsubnoallot.").append(
					TableLfSubnoAllot.CODE_TYPE).append("=").append(
							subnoAllotVo.getCodeType());
		}
		
		if(subnoAllotVo.getCodes() != null && !"".equals(subnoAllotVo.getCodes())){
			conditionSql.append(" and lfsubnoallot.").append(
					TableLfSubnoAllot.CODES).append(" in(").append(
							subnoAllotVo.getCodes()).append(")");
		}
		
		if(subnoAllotVo.getName() != null && !"".equals(subnoAllotVo.getName()) && subnoAllotVo.getCodeType() != null ){
			
			String name = null;
			String other = null;
			if(subnoAllotVo.getCodeType() == 0){
				other = "privilege";
				name = TableLfPrivilege.MENU_NAME;
			}else if(subnoAllotVo.getCodeType() == 1){
				other = "busmanager";
				name = TableLfBusManager.BUS_NAME;
			}else if(subnoAllotVo.getCodeType() == 2){
				//other = "products";
				//name = TableLfProducts.PRO_NAME;
			}else if(subnoAllotVo.getCodeType() == 3){
				other = "dep";
				name = TableLfDep.DEP_NAME;
			}else if(subnoAllotVo.getCodeType() == 4){
				other = "sysuser";
				name = TableLfSysuser.NAME;
			}
			
			conditionSql.append(" and ").append(other).append(".")
			.append(name).append("like %'").append(subnoAllotVo.getName()).append("'%");
		}
		
		String sql = conditionSql.toString();
		return sql;
	}
}
