package com.montnets.emp.query.dao;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.query.vo.SysMoMtSpgateVo;

/**
 * sql类
 * @author Administrator
 *
 */
public class GenericMoMtSpgateVoSQL {

	/**
	 *获取查询字段 
	 * @param sysMoMtSpVo
	 * @return
	 */
	public static String getFieldSql(SysMoMtSpgateVo sysMoMtSpVo) {
		
		//System.out.println("qiyebianm===="+sysMoMtSpVo.getCropCode());
		
		String sql = "select SPGATE from XT_GATE_QUEUE "+StaticValue.getWITHNOLOCK()+" where  SPGATE in (select SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='-1'))";
		
		if(sysMoMtSpVo.getCropCode()!=null&&!"".equals(sysMoMtSpVo.getCropCode()))
		{
			String  SQL="";
			//多企业
			if("100000".equals(sysMoMtSpVo.getCropCode())){
				
				SQL = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" ";
				
			}else{
				//单企业
				
				SQL = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='"+sysMoMtSpVo.getCropCode()+"')";
			}
			
			
			return SQL; //多企业版
		}
		else if("".equals(sysMoMtSpVo.getCropCode())||sysMoMtSpVo.getCropCode()==null){
            
			String Sql = "select distinct(SPGATE) from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='-1')";
			
			return Sql;
			
		}
		//返回sql
		return sql;
	}
	
	
	
	/**
	 *获取查询字段 
	 * @param sysMoMtSpVo
	 * @return
	 */
	public static String getMtFieldSql(SysMoMtSpgateVo sysMoMtSpVo) {
		
		//System.out.println("qiyebianm===="+sysMoMtSpVo.getCropCode());
		
		String sql = "select SPGATE from XT_GATE_QUEUE "+StaticValue.getWITHNOLOCK()+" where  SPGATE in (select SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='-1'))";
		
		if(StaticValue.getCORPTYPE() ==0){
			sql = "select SPGATE from XT_GATE_QUEUE "+StaticValue.getWITHNOLOCK()+" where  SPGATE in (select SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='"+sysMoMtSpVo.getCropCode()+"')) or SPGATE like '200%'";
			return sql;
		}
		
		if(sysMoMtSpVo.getCropCode()!=null&&!"".equals(sysMoMtSpVo.getCropCode()))
		{
			String  SQL="";
			//多企业
			if("100000".equals(sysMoMtSpVo.getCropCode())){
				
				SQL = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" ";
				
			}else{
				//单企业
				
				SQL = "select distinct(SPGATE) SPGATE from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='"+sysMoMtSpVo.getCropCode()+"')";
			}
			
			
			return SQL; //多企业版
		}
		else if("".equals(sysMoMtSpVo.getCropCode())||sysMoMtSpVo.getCropCode()==null){
            
			String Sql = "select distinct(SPGATE) from GT_PORT_USED "+StaticValue.getWITHNOLOCK()+" where USERID in (select SPUSER from LF_SP_DEP_BIND "+StaticValue.getWITHNOLOCK()+" where CORP_CODE='-1')";
			
			return Sql;
			
		}
		//返回sql
		return sql;
	}


}
