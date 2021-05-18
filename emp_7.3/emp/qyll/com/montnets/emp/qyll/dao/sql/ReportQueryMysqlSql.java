package com.montnets.emp.qyll.dao.sql;

import com.montnets.emp.qyll.entity.LlProduct;

public class ReportQueryMysqlSql extends ReportQuerySql{
	
	public String getLlCompInfoInsertSql() {
		return "INSERT INTO LL_COMP_INFO (ID,PASSWORD,ECID,ECNAME,IP,PORT,REMARK,PUSHADDR,UPDATETM,CREATETM) VALUES(1,?,?,?,?,?,?,?,NOW(),NOW())";
	}
	
	public String getLlCompInfoUpdateSql() {
		return "UPDATE LL_COMP_INFO SET PASSWORD=?,ECID=?,ECNAME=?,IP=?,PORT=?,REMARK=?,PUSHADDR=?,UPDATETM=NOW()";
	}

	public String getLlProductInsertSql() {
		return "INSERT INTO LL_PRODUCT (ECID,PRODUCTID,PRODUCTNAME,ISP,VOLUME,PRICE,DISCPRICE,AREA,PTYPE,PMOLD,RTYPE,STATUS,UPDATETM,CREATETM,OPERATORID) VALUES(?,?,?,?,?,?,?,?,?,?,?,1,NOW(),NOW(),?)";
	}
	
	public String getLlProductUpdateSql(LlProduct llProduct) {
		StringBuilder sql = new StringBuilder().append("UPDATE LL_PRODUCT SET STATUS=0,OPERATORID=?,UPDATETM=NOW() WHERE PRODUCTID=? AND ECID =").append(llProduct.getEcid());
		return sql.toString();
	}
	
	public String getLlProductUpdateInSql(LlProduct llProduct, String pros) {
		StringBuilder sql = new StringBuilder().append("UPDATE LL_PRODUCT SET STATUS=0,OPERATORID=?,UPDATETM=NOW() WHERE PRODUCTID in(").append(pros).append(") AND ECID =").append(llProduct.getEcid());
		return sql.toString();
	}
}
