package com.montnets.emp.wyquery.dao;

import com.montnets.emp.wyquery.vo.SysMoMtSpgateVo;

/**
 * sql类
 * @author Administrator
 *
 */
public class GenericMoMtSpgateVoSQL {

	/**
	 *获取查询字段（只支持单企业，不存在是否绑定的情况） 
	 * @param sysMoMtSpVo
	 * @return
	 */
	public static String getFieldSql(SysMoMtSpgateVo sysMoMtSpVo) {
		
		//System.out.println("qiyebianm===="+sysMoMtSpVo.getCropCode());
		
//		String sql = "select SPGATE from XT_GATE_QUEUE where  SPGATE in (select SPGATE from GT_PORT_USED where USERID in (select SPUSER from LF_SP_DEP_BIND where CORP_CODE='-1'))";
//		
//		if(sysMoMtSpVo.getCropCode()!=null&&!"".equals(sysMoMtSpVo.getCropCode()))
//		{
//			String  SQL="";
//			//多企业
//			if("100000".equals(sysMoMtSpVo.getCropCode())){
//				
//				SQL = "select distinct(SPGATE) SPGATE from GT_PORT_USED";
//				
//			}else{
//				//单企业
//				
//				SQL = "select distinct(SPGATE) SPGATE from GT_PORT_USED where USERID in (select SPUSER from LF_SP_DEP_BIND where CORP_CODE='"+sysMoMtSpVo.getCropCode()+"')";
//			}
//			
//			
//			return SQL; //多企业版
//		}
//		else if("".equals(sysMoMtSpVo.getCropCode())||sysMoMtSpVo.getCropCode()==null){
//            
//			String Sql = "select distinct(SPGATE) from GT_PORT_USED where USERID in (select SPUSER from LF_SP_DEP_BIND where CORP_CODE='-1')";
//			
//			return Sql;
//			
//		}
//		//返回sql 
		
		String sql="select SPGATE from XT_GATE_QUEUE where SPGATE like '200%' order by SPGATE ";
		return sql;
	}


}
