package com.montnets.emp.rms.detailsend.table;
import java.util.HashMap;
import java.util.Map;


/**
 * 发送明细查询
 * @project ydcx
 * @author Lvxin
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2018-1-25下午02:10:07
 * @description
 */
public class TableLFdetailsend {
	//表名
	public final static String TABLE_NAME = "LF_RMS_REPORT";
	
	//运营商
	public static final String SPISUNCM="SPISUNCM";
	//sp账号
	public static final String USER_ID="USERID";
	//年月日
	public static final String IYMD="IYMD";
	//提交总数
	public static final String ICOUNT="ICOUNT";
	//发送成功数
	public static final String RSUCC="RSUCC";
	//接收失败数
	public static final String RFAIL="RFAIL";
	//create时间
	public static final String CREATE_TIME="CREATE_TIME";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//同步时间
	public static final String SYN_TIME="SYN_TIME";
	//档位
	public static final String DEGREE="DEGREE";
	
	
	
	protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LFdetailsend", TABLE_NAME);
		columns.put("spisuncm", SPISUNCM);
		columns.put("userid", USER_ID);
		columns.put("iymd", IYMD);
		columns.put("icount", ICOUNT);
		columns.put("rsucc", RSUCC);
		columns.put("rfail", RFAIL);
		columns.put("createTime", CREATE_TIME);
		columns.put("corpCode", CORP_CODE);
		columns.put("synTime", SYN_TIME);
		columns.put("degree", DEGREE);
	
	}
	
	public static Map<String, String> getORM()
	{
		return columns;
	}
		
	}


