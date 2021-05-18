
package com.montnets.emp.table.wy;

import java.util.HashMap;
import java.util.Map;

public class TableASiminfo
{
	//表名
	public static  final String TABLE_NAME	= "A_SIMINFO";
	//月上限
	public static final String MONTHLIMIT = "MONTHLIMIT";
	//时上限
	public static final String HOURLIMIT = "HOURLIMIT";
	//SIM卡号
	public static final String PHONENO = "PHONENO";
	//所属地区
	public static final String DESCRIPTION = "DESCRIPTION";
	//日上限
	public static final String DAYLIMIT = "DAYLIMIT";
	//序号
	public static final String SIMNO = "SIMNO";
	//所属国际地区
	public static final String MOBILEAREA = "MOBILEAREA";
	//自增id
	public static final String ID = "ID";
	//创建时间
	public static final String CREATETIME = "CREATETIME";
	//通道ID
	public static final String GATEID = "GATEID";
	//运营商
	public static final String UNICOM = "UNICOM";
	//序列
	public static final String SEQUENCE	= "SEQ_A_SIMINFO";
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("ASiminfo", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("monthlimit", MONTHLIMIT);
		columns.put("hourlimit", HOURLIMIT);
		columns.put("phoneno", PHONENO);
		columns.put("description", DESCRIPTION);
		columns.put("daylimit", DAYLIMIT);
		columns.put("simno", SIMNO);
		columns.put("mobilearea", MOBILEAREA);
		columns.put("id", ID);
		columns.put("createtime", CREATETIME);
		columns.put("gateid", GATEID);
		columns.put("unicom", UNICOM);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
