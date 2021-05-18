package com.montnets.emp.table.wxsysuser;

import java.util.HashMap;
import java.util.Map;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-1-21 下午02:27:48
 * @description
 */

public class TableLfSysDep {

	//表名(机构表)
	public static final String TABLE_NAME = "LF_SYS_DEP";
	//机构ID
	public static final String DEP_ID = "DEP_ID";
	//机构名称
	public static final String DEP_NAME = "DEP_NAME";
	//机构联系人
	public static final String DEP_CONTACT = "DEP_CONTACT";
	//机构级别
	public static final String DEP_LEVEL = "DEP_LEVEL";
	//机构编码
	//public static final String DEP_CODE = "DEP_CODE";
	//机构描述
	public static final String DEP_RESP = "DEP_RESP";
	//父级ID
	public static final String SUPERIOR_ID = "SUPERIOR_ID";
	//与机构表管理关联的机构编码
	public static final String DEP_CODE_THIRD="DEP_CODE_THIRD";
	//企业编码
	public static final String CORP_CODE="CORP_CODE";
	//部门负责人
	public static final String DEP_DIRECT="DEP_DIRECT";
	//序列名
	public static final String SEQUENCE="S_LF_DEP";
	// 1正常 2删除，默认为1
	public static final String DEP_STATE="DEP_STATE";
	public static final String DEP_PATH="DEP_PATH";
	//统一平台机构标识ID
	public static final String UP_DEPID="UP_DEPID";

	protected static final Map<String, String> columns = new HashMap<String, String>();

	static {
		columns.put("LfDep", TABLE_NAME);
		columns.put("tableId", DEP_ID);
		columns.put("sequence", SEQUENCE);
		columns.put("depId", DEP_ID);
		columns.put("depName", DEP_NAME);
		columns.put("depContact", DEP_CONTACT);
		columns.put("depLevel", DEP_LEVEL);
		//columns.put("depCode", DEP_CODE);
		columns.put("depResp", DEP_RESP);
		columns.put("superiorId", SUPERIOR_ID);
		columns.put("depCodeThird", DEP_CODE_THIRD);
		columns.put("depDirect",DEP_DIRECT);
		columns.put("corpCode",CORP_CODE);
		columns.put("depState",DEP_STATE);
		columns.put("deppath", DEP_PATH);
		columns.put("upDepId", UP_DEPID);
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
