package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;

/**
 * @project emp
 * @author wuxiaotao <819475589@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-12 上午10:43:18
 * @description
 */

public class ViewLfSysuserVo
{

	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		// put("LfSysuserVo", "LF_SYSUSER");
		// put("tableId","USER_ID");
		columns.put("userId",TableLfSysuser.USER_ID);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userState",TableLfSysuser.USER_STATE);
		columns.put("userType",TableLfSysuser.USER_TYPE);
		columns.put("holder",TableLfSysuser.HOLDER);
		columns.put("regTime",TableLfSysuser.REG_TIME);
		columns.put("mobile",TableLfSysuser.MOBILE);
		columns.put("oph",TableLfSysuser.OPH);
		columns.put("guId",TableLfSysuser.GUID);
		columns.put("qq",TableLfSysuser.QQ);
		columns.put("EMail",TableLfSysuser.E_MAIL);
		columns.put("sex",TableLfSysuser.SEX);
		columns.put("password",TableLfSysuser.PASSW);
		columns.put("userName",TableLfSysuser.USER_NAME);
		columns.put("depId", TableLfSysuser.DEP_ID);
		columns.put("depName",TableLfDep.DEP_NAME);
		columns.put("permissionType",TableLfSysuser.PERMISSION_TYPE);
		columns.put("userCode",TableLfSysuser.USER_CODE);
		columns.put("isExistSubno",TableLfSysuser.ISEXISTSUBNO);
		columns.put("fax",TableLfSysuser.FAX);
		columns.put("msn",TableLfSysuser.MSN);
		columns.put("birthday",TableLfSysuser.BIRTHDAY);
		columns.put("isReviewer",TableLfSysuser.IS_REVIEWER);
		columns.put("duties","DUTIES");
		columns.put("showNum",TableLfSysuser.SHOWNUM);
		// put("domDepNameList","");
		// put("domDepIdList","");
		// put("roleNameList","");
		// put("roleIdList","");
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
