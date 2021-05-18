package com.montnets.emp.table.wy;

import java.util.HashMap;
import java.util.Map;

public class TableAIpcominfo
{
	//表名
	public static final String TABLE_NAME	= "A_IPCOMINFO";
	//备注
	public static final String COMMON = "COMMON";
	//端口
	public static final String PORT = "PORT";
	//端口
	public static final String PTPORT = "PTPORT";
	//通道名称
	public static final String GATENAME = "GATENAME";
	//企业签名
	public static final String CORPSIGN = "CORPSIGN";
	//ip
	public static final String IP = "IP";
	//ip
	public static final String PTIP = "PTIP";
	//主键id
	public static final String ID = "ID";
	//通道id
	public static final String GATEID = "GATEID";
	//创建时间
	public static final String CREATETIME = "CREATETIME";
	//序列
	public static final String SEQUENCE	= "SEQ_A_IPCOMINFO";
	//集合
	protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{

		columns.put("AIpcominfo", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("common", COMMON);
		columns.put("port", PORT);
		columns.put("ptport", PTPORT);
		columns.put("gatename", GATENAME);
		columns.put("corpsign", CORPSIGN);
		columns.put("ip", IP);
		columns.put("ptip", PTIP);
		columns.put("id", ID);
		columns.put("gateid", GATEID);
		columns.put("createtime", CREATETIME);
	};


	public static Map<String , String> getORM(){

		return columns;

	}
}
