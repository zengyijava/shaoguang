package com.montnets.emp.tczl.vo.view;

import com.montnets.emp.table.pasgroup.TableAcmdRoute;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.table.ydyw.TableLfBusTaoCan;
import com.montnets.emp.tczl.table.TableLfTaocanCmd;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfTaocanCmdVo
{
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	/**
	 * 加载字段
	 */
	static
	{
		columns.put("id", TableLfTaocanCmd.ID);
		columns.put("structcode", TableLfTaocanCmd.STRUCTCODE);
		columns.put("taocanCode", TableLfTaocanCmd.TAOCAN_CODE);
		columns.put("taocanType", TableLfTaocanCmd.TAOCAN_TYPE);
		columns.put("taocanMoney", TableLfTaocanCmd.TAOCAN_MONEY);
		columns.put("structType", TableLfTaocanCmd.STRUCT_TYPE);
		columns.put("createTime", TableLfTaocanCmd.CREATE_TIME);
		columns.put("updateTime", TableLfTaocanCmd.UPDATE_TIME);
		columns.put("depId", TableLfTaocanCmd.DEP_ID);
		columns.put("userId", TableLfTaocanCmd.USER_ID);
		columns.put("corpCode", TableLfTaocanCmd.CORP_CODE);
		columns.put("spUser", TableUserdata.USER_ID);
		columns.put("spId", TableAcmdRoute.SP_ID);
		columns.put("acId", "ACID");
		columns.put("taocanName", TableLfBusTaoCan.TAOCAN_NAME);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userName", TableLfSysuser.USER_NAME);
		columns.put("depName", TableLfDep.DEP_NAME);
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
