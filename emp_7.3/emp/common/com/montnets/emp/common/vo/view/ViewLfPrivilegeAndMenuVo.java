package com.montnets.emp.common.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.table.system.TableLfThiMenuControl;
import com.montnets.emp.table.sysuser.TableLfPrivilege;

public class ViewLfPrivilegeAndMenuVo {
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		columns.put("tid",TableLfThiMenuControl.TID);
		columns.put("menuNum",TableLfThiMenuControl.MENU_NUM);
		columns.put("title",TableLfThiMenuControl.TITLE);
		columns.put("priMenu", TableLfThiMenuControl.PRI_MENU);
		columns.put("priOrder",TableLfThiMenuControl.PRI_ORDER);
		columns.put("privilegeId",TableLfPrivilege.PRIVILEGE_ID);
		columns.put("resourceId",TableLfPrivilege.RESOURCE_ID);
		columns.put("operateId",TableLfPrivilege.OPERATE_ID);
		columns.put("comments",TableLfPrivilege.COMMENTS);
		columns.put("privCode",TableLfPrivilege.PRIV_CODE);
		columns.put("menuName",TableLfPrivilege.MENU_NAME);
		columns.put("modName",TableLfPrivilege.MOD_NAME);
		columns.put("menuCode",TableLfPrivilege.MENU_CODE);
		columns.put("menuSite",TableLfPrivilege.MENU_SITE);
		columns.put("zhTwTitle",TableLfThiMenuControl.ZH_TW_TITLE);
		columns.put("zhHkTitle",TableLfThiMenuControl.ZH_HK_TITLE);
		columns.put("zhTwComments",TableLfPrivilege.ZH_TW_COMMENTS);
		columns.put("zhHkComments",TableLfPrivilege.ZH_HK_COMMENTS);
		columns.put("zhTwMenuName",TableLfPrivilege.ZH_TW_MENU_NAME);
		columns.put("zhHkMenuName",TableLfPrivilege.ZH_HK_MENU_NAME);
		columns.put("zhTwModName",TableLfPrivilege.ZH_TW_MOD_NAME);
		columns.put("zhHkModName",TableLfPrivilege.ZH_HK_MOD_NAME);
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
