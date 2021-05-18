package com.montnets.emp.table.lottery;

import java.util.HashMap;
import java.util.Map;

/**
 * table名称: TableLfMarSweepstakes
 *
 * @project p_mar
 * @author fangyt <foyoto@gmail.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-9-25 下午07:29:21
 * @description
 */
public class TableLfMarSweepstakes
{
    /**
     * 表名
      */
	public static final String				TABLE_NAME	= "LF_MAR_SWEEPSTAKES";

    public static final String			S_ID	= "S_ID";

    public static final String			TITLE	= "TITLE";

    public static final String			KEYWORD	= "KEYWORD";

    public static final String			NOTE	= "NOTE";

    public static final String			BEGINTIME	= "BEGINTIME";

    public static final String			ENDTIME	= "ENDTIME";

    public static final String			EXPIRY_TIME	= "EXPIRY_TIME";

    public static final String			PARTAKE_TYPE	= "PARTAKE_TYPE";
    
    public static final String          PAR_TIMES  = "PAR_TIMES";

    public static final String			PARTAKE_COUNT	= "PARTAKE_COUNT";

    public static final String			USING_ATTR	= "USING_ATTR";

    public static final String			PROBABILITY_NUM	= "PROBABILITY_NUM";
    
    public static final String          PRIZE_WINNING = "PRIZE_WINNING";

    public static final String			CORP_CODE	= "CORP_CODE";

    public static final String			CREATETIME	= "CREATETIME";

    public static final String			MODITYTIME	= "MODITYTIME";

    /**
     * 序列
     */
	public static final String			SEQUENCE	= "40";

    /**
     * 映射集合
     */
	protected static final Map<String, String>	columns		= new HashMap<String, String>();

	static
	{
		columns.put("LfMarSweepstakes", TABLE_NAME);
		columns.put("tableId", S_ID);
		columns.put("sequence", SEQUENCE);
        columns.put("sid", S_ID);
        columns.put("title", TITLE);
        columns.put("keyword", KEYWORD);
        columns.put("note", NOTE);
        columns.put("begintime", BEGINTIME);
        columns.put("endtime", ENDTIME);
        columns.put("expiryTime", EXPIRY_TIME);
        columns.put("partakeType", PARTAKE_TYPE);
        columns.put("parTimes", PAR_TIMES);
        columns.put("partakeCount", PARTAKE_COUNT);
        columns.put("usingAttr", USING_ATTR);
        columns.put("probabilityNum", PROBABILITY_NUM);
        columns.put("prizeWinning", PRIZE_WINNING);
        columns.put("corpCode", CORP_CODE);
        columns.put("createtime", CREATETIME);
        columns.put("moditytime", MODITYTIME);
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