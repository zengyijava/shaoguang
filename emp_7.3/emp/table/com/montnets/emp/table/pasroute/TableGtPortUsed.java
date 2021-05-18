package com.montnets.emp.table.pasroute;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:30:34
 * @description
 */
public class TableGtPortUsed
{

	public static final String TABLE_NAME = "GT_PORT_USED";

	public static final String ID = "ID";

	public static final String GATE_TYPE = "GATETYPE";

	public static final String SPGATE = "SPGATE";

	public static final String CPNO = "CPNO";

	public static final String PORT_TYPE = "PORTTYPE";

	public static final String USER_CODE = "USERCODE";

	public static final String SPISUNCM = "SPISUNCM";

	public static final String ROUTE_FLAG = "ROUTEFLAG";

	public static final String STATUS = "STATUS";

	public static final String IS_EXCHANNEL = "ISEXCHANNEL";

	public static final String USER_ID = "USERID";

	public static final String LOGIN_ID = "LOGINID";

	public static final String SPNUMBER = "SPNUMBER";

	public static final String FIXED_FEE = "FIXEDFEE";

	public static final String FEE_FLAG = "FEEFLAG";

	public static final String FEE = "FEE";

	public static final String KHDATE = "KHDATE";

	public static final String USER_INFO = "USERINFO";

	public static final String MEMO = "MEMO";

	public static final String SPNUMBER11 = "SPNUMBER11";

	public static final String VALID_DATE = "VALIDDATE";

	public static final String WARN_DAYS = "WARNDAYS";

	public static final String MONEYS = "MONEYS";

	public static final String SIGNSTR = "SIGNSTR";

	public static final String SIGNLEN = "SIGNLEN";

	public static final String MAX_WORDS = "MAXWORDS";

	public static final String SINGLELEN = "SINGLELEN";

	public static final String MULTILEN1 = "MULTILEN1";

	public static final String MULTILEN2 = "MULTILEN2";

	public static final String FORBID_TIME_START = "FORBIDTIMESTART";

	public static final String FORBID_TIME_END = "FORBIDTIMEEND";

	public static final String SEND_TIME_BEGIN = "SENDTIMEBEGIN";

	public static final String SEND_TIME_END = "SENDTIMEEND";

	public static final String SEQUENCE = "GT_PORT_USER_MPSN";
	//英文签名
	public static final String ENSIGNSTR = "ENSIGNSTR";
	//英文签名长度
	public static final String ENSIGNLEN = "ENSIGNLEN";
	//英文短信最大字数
	public static final String ENMAXWORDS = "ENMAXWORDS";
	//单条英文短信字数
	public static final String ENSINGLELEN = "ENSINGLELEN";
	//英文长短信第一条至N-1 条短信内容长度
	public static final String ENMULTILEN1 = "ENMULTILEN1";
	//英文长短信最后一条短信内容长度
	public static final String ENMULTILEN2 = "ENMULTILEN2";

    protected static final Map<String, String> columns = new HashMap<String, String>();

	static
	{
		columns.put("GtPortUsed", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("gateType", GATE_TYPE);
		columns.put("spgate", SPGATE);
		columns.put("cpno", CPNO);
		columns.put("portType", PORT_TYPE);
		columns.put("userCode", USER_CODE);
		columns.put("spisuncm", SPISUNCM);
		columns.put("routeFlag", ROUTE_FLAG);

		columns.put("status", STATUS);
		//columns.put("isExchannel", IS_EXCHANNEL);
		columns.put("userId", USER_ID);
		columns.put("loginId", LOGIN_ID);
		columns.put("spnumber", SPNUMBER);
		//columns.put("fixedFee", FIXED_FEE);
		columns.put("feeFlag", FEE_FLAG);
		//columns.put("fee", FEE);

		//columns.put("khdate", KHDATE);
		//columns.put("userInfo", USER_INFO);
		columns.put("memo", MEMO);
		columns.put("spnumber11", SPNUMBER11);
		//columns.put("validDate", VALID_DATE);
		//columns.put("warnDays", WARN_DAYS);
		//columns.put("moneys", MONEYS);
		columns.put("signstr", SIGNSTR);

		columns.put("signlen", SIGNLEN);
		columns.put("maxwords", MAX_WORDS);
		columns.put("singlelen", SINGLELEN);
		columns.put("multilen1", MULTILEN1);
		columns.put("multilen2", MULTILEN2);
		//columns.put("forbidTimeStart", FORBID_TIME_START);
		//columns.put("forbidTimeEnd", FORBID_TIME_END);
		columns.put("sendTimeBegin", SEND_TIME_BEGIN);
		columns.put("sendTimeEnd", SEND_TIME_END);
		columns.put("ensignstr", ENSIGNSTR);
		columns.put("ensignlen", ENSIGNLEN);
		columns.put("enmaxwords", ENMAXWORDS);
		columns.put("ensinglelen", ENSINGLELEN);
		columns.put("enmultilen1", ENMULTILEN1);
		columns.put("enmultilen2", ENMULTILEN2);
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
