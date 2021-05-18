package com.montnets.emp.table.passage;

import java.util.HashMap;
import java.util.Map;


/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:00
 * @description
 */
public class TableXtGateQueue
{

	public static final String TABLE_NAME = "XT_GATE_QUEUE";

	public static final String ID = "ID";

	public static final String SPGATE = "SPGATE";

	public static final String STATUS = "STATUS";

	public static final String SPISUNCM = "SPISUNCM";

	public static final String GATE_TYPE = "GATETYPE";

	public static final String GATE_NAME = "GATENAME";

	public static final String PORT_TYPE = "PORTTYPE";

	public static final String SINGLELEN = "SINGLELEN";

	public static final String SIGNSTR = "SIGNSTR";

	public static final String RISE_LEVEL = "RISELEVEL";

	public static final String SPEED = "SPEED";

	public static final String LONG_SMS = "LONGSMS";

	public static final String MAX_WORDS = "MAXWORDS";

	public static final String SUBLEN = "SUBLEN";

	public static final String FEE_FLAG = "FEEFLAG";

	public static final String SIGNLEN = "SIGNLEN";

	public static final String FEE = "FEE";

	public static final String SEQUENCE = "SEQ_XT_GATE_QUE";

	public static final String GATEINFO = "GATEINFO";
	
	public static final String SPLITRULE = "SPLITRULE";
	
	public static final String WORDLEN = "WORDLEN";
	
	public static final String SHOWFLAG = "SHOWFLAG";
	
	public static final String SORTID = "SORTID";
	
	public static final String MULTILEN1 = "MULTILEN1";
	
	public static final String MULTILEN2 = "MULTILEN2";
	
	public static final String GATENO = "GATENO";
	
	
	public static final String SIGNTYPE = "SIGNTYPE";
	
	public static final String SIGNFIXLEN = "SIGNFIXLEN";
	
	public static final String PREFIXLEN = "PREFIXLEN";
	
	public static final String MAXLONGMSGSEQ = "MAXLONGMSGSEQ";
	
	
	public static final String ENDSPLIT = "ENDSPLIT";
	
	
	public static final String GATESEQ = "GATESEQ";
	
	public static final String SIGNDROPTYPE = "SIGNDROPTYPE";
	
	public static final String GATEAREA = "GATEAREA";
	
	public static final String AREATYPE = "AREATYPE";
	
	public static final String EACHSIGN = "EACHSIGN";
	
	public static final String GATEPRIVILEGE = "GATEPRIVILEGE";
	//英文签名
	public static final String ENSIGNSTR = "ENSIGNSTR";
	//英文签名长度
	public static final String ENSIGNLEN = "ENSIGNLEN";
	//英文长短信头字节数
	public static final String ENPREFIXLEN = "ENPREFIXLEN";
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
		columns.put("XtGateQueue", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("spgate", SPGATE);
		columns.put("status", STATUS);
		columns.put("spisuncm", SPISUNCM);
		columns.put("gateType", GATE_TYPE);
		columns.put("gateName", GATE_NAME);
		columns.put("portType", PORT_TYPE);
		columns.put("singleLen", SINGLELEN);
		columns.put("signstr", SIGNSTR);
		columns.put("riseLevel", RISE_LEVEL);
		columns.put("speed", SPEED);
		columns.put("longSms", LONG_SMS);
		columns.put("maxWords", MAX_WORDS);
		columns.put("sublen", SUBLEN);
		columns.put("feeFlag", FEE_FLAG);
		columns.put("signlen", SIGNLEN);
		columns.put("fee", FEE);
		columns.put("gateInfo", GATEINFO);
		columns.put("splitRule",SPLITRULE );
		columns.put("wordLen",WORDLEN );
		columns.put("showFlag",SHOWFLAG );
		columns.put("sortId", SORTID);
		columns.put("multilen1", MULTILEN1);
		columns.put("multilen2", MULTILEN2);
		columns.put("gateNo", GATENO);
		columns.put("signType", SIGNTYPE);
		columns.put("signFixLen",SIGNFIXLEN );
		columns.put("preFixLen", PREFIXLEN);
		columns.put("maxLongMsgEq", MAXLONGMSGSEQ);
		columns.put("endSplit",ENDSPLIT );
		columns.put("gateSeq", GATESEQ);
		columns.put("signDropType", SIGNDROPTYPE);
		columns.put("gateArea", GATEAREA);
		columns.put("areaType", AREATYPE);
		columns.put("eachSign", EACHSIGN);
		columns.put("gateprivilege", GATEPRIVILEGE);
		columns.put("ensignstr", ENSIGNSTR);
		columns.put("ensignlen", ENSIGNLEN);
		columns.put("enprefixlen", ENPREFIXLEN);
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
