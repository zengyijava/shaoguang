package com.montnets.emp.table.sysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * 审核对象管理表
 * @project p_tabl
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-7-19 上午11:22:53
 * @description
 */
public class TableLfFlowBindObj {

	//审核开关表
	public static final String TABLE_NAME = "LF_FLOWBINDOBJ";
	//自增ID
	public static final String ID = "ID";
	//审核流ID
	public static final String F_ID = "F_ID";
	//被审核人类型，1-操作员，2--机构
	public static final String Obj_TYPE = "OBJ_TYPE";
	//操作员或机构唯一编码，TYPE为1时，此字段为操作员，2时为机构编码
	public static final String Obj_CODE = "OBJ_CODE";
	//修改时间
	public static final String UPDATE_TIME = "UPDATE_TIME";
	//新增时间
	public static final String CREATE_TIME = "CREATE_TIME";
	//信息类型。1：短信发送；2：彩信发送；3：短信模板；4：彩信模板；
	public static final String INFO_TYPE = "INFO_TYPE";
	//企业编号
	public static final String CORP_CODE = "CORP_CODE";
    //机构是否包含子机构
    public static final String CTSUBDEP = "CTSUBDEP";
	//序列名
	public static final String SEQUENCE = "S_LF_FLOWBINDOBJ";
	
	
	
	protected static final Map<String , String> columns = new HashMap<String, String>();

	static {
		columns.put("LfFlowBindObj", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
		columns.put("id", ID);
		columns.put("FId", F_ID);
		columns.put("ObjType", Obj_TYPE);
		columns.put("ObjCode", Obj_CODE);
		columns.put("updateTime", UPDATE_TIME);
        columns.put("ctsubDep", CTSUBDEP);
		columns.put("createTime", CREATE_TIME);
		columns.put("infoType", INFO_TYPE);
		columns.put("corpCode", CORP_CODE);
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
