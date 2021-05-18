package com.montnets.emp.table.blacklist;

import java.util.HashMap;
import java.util.Map;

/**
 * 彩信黑名单表
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-19 上午09:31:12
 * @description
 */
public class TableLfMmsBlist {

	//彩信黑名单表
	public static final String TABLE_NAME = "LF_MMSBLIST";
	//主键
	public static final String BL_ID = "BL_ID";
	//电话号码
	public static final String PHONE = "PHONE";
	//通道号码
	public static final String SPGATE = "SPGATE";
	//状态
	public static final String BL_STATE = "BL_STATE";
	//备注
	public static final String COMMENTS = "COMMENTS";
	//创建 人
	public static final String USER_ID = "USERID";
	//通道号+扩展号
	public static final String SPNUMBER = "SPNUMBER";
	//操作时间
	public static final String OPTIME = "OPTIME";
	//0移动，1联通，21电信
	public static final String SPISUNCM="SPISUNCM";
	//业务编码
	public static final String BUS_CODE = "BUS_CODE";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//序列
	public static final String SEQUENCE = "S_LF_MMSBLIST";
	
	protected final static Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfMmsBlist", TableLfMmsBlist.TABLE_NAME);
		columns.put("tableId", BL_ID);
        columns.put("sequence",SEQUENCE);
		columns.put("blId", BL_ID);
		columns.put("phone", PHONE);
		columns.put("spgate", SPGATE);
		columns.put("blState", BL_STATE);
		columns.put("comments", COMMENTS);
		columns.put("userId", USER_ID);
		columns.put("spnumber", SPNUMBER);
		columns.put("optime", OPTIME);
		columns.put("spisuncm", SPISUNCM);
		columns.put("busCode", BUS_CODE);
		columns.put("corpCode",CORP_CODE);
	};

	/**
	 * 返回实体类字段与数据库字段实体类映射的map集合
	 * 
	 * @return
	 */
	public static Map<String, String> getORM() {
		return columns;
	}

}
