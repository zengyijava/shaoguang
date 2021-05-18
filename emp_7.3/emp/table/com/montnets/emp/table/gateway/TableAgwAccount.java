/**
 * 
 */
package com.montnets.emp.table.gateway;

import java.util.HashMap;
import java.util.Map;


/**
 * @project montnets_entity
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-11-12 下午04:44:38
 * @description 
 */

public class TableAgwAccount
{
	public static  final String TABLE_NAME = "A_GWACCOUNT";

	public static final String GWNO  = "GWNO";
 
	public static final String PTACCUID  = "PTACCUID";

	public static final String PTACCID  = "PTACCID";
	 
	public static final String PTACCPASS = "PTACCPWD";
	
	public static final String SPACCID  = "SPACCID";
	
	public static final String SPACCPASS  = "SPACCPWD";
	 
	public static final String SPID  = "SPID";
	
	public static final String SERVICETYPE  = "SERVICETYPE";
	 
	public static final String FEEUSERTYPE  = "FEEUSERTYPE";
	
	public static final String SPIP  = "SPIP";
	 
	public static final String SPPORT  = "SPPORT";
	
	public static final String SPEEDLIMIT = "SPEEDLIMIT";
	
	public static final String PROTOCOLCODE = "PROTOCOLCODE";
	
	public static final String PROTOCOLPARAM = "PROTOCOLPARAM";
	
	public static final String PTACCNAME = "PTACCNAME";
	
	public static final String PTIP = "PTIP";
	
	public static final String PTPORT = "PTPORT";
	
	public static final String SPTYPE = "SPTYPE";
	
	public static final String FEEURL = "FEEURL";
	
	public static final String BALANCE = "BALANCE";
	
	public static final String BALANCETH = "BALANCETH";
	
	public static final String UPDATETIME = "UPDATETIME";
	
	public static final String SPFEEFLAG = "SPFEEFLAG";
	 
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("AgwAccount", TABLE_NAME);
		//columns.put("tableId",PTACCUID );
 		columns.put("gwNo", GWNO);
 		columns.put("ptAccUid", PTACCUID);
 		columns.put("ptAccId", PTACCID);
 		columns.put("ptAccName",PTACCID );
 		columns.put("ptAccpwd", PTACCPASS);
 		columns.put("spAccid",SPACCID );
 		columns.put("spAccPwd",SPACCPASS );
 		columns.put("spId",SPID );
 		columns.put("serviceType",SERVICETYPE );
 		columns.put("feeUserType", FEEUSERTYPE);
 		columns.put("spIp", SPIP);
 		columns.put("spPort", SPPORT);
 		columns.put("speedLimit", SPEEDLIMIT);
 		columns.put("protocolCode", PROTOCOLCODE);
 		columns.put("protocolParam", PROTOCOLPARAM);
 		columns.put("ptAccName", PTACCNAME);
 		columns.put("ptIp", PTIP);
 		columns.put("ptPort", PTPORT);
 		columns.put("spType", SPTYPE);
 		columns.put("feeUrl", FEEURL);
 		columns.put("balance", BALANCE);
 		columns.put("balanceTh", BALANCETH);
 		columns.put("updateTime", UPDATETIME);
 		columns.put("spFeeFlag", SPFEEFLAG);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM()
	{

		return columns;
	}
}
