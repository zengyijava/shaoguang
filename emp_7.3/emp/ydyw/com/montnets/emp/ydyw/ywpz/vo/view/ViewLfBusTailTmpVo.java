package com.montnets.emp.ydyw.ywpz.vo.view;

import com.montnets.emp.table.biztype.TableLfBusManager;
import com.montnets.emp.table.sysuser.TableLfDep;
import com.montnets.emp.table.sysuser.TableLfSysuser;
import com.montnets.emp.ydyw.ywpz.table.TableLfBusTailTmp;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfBusTailTmpVo {
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static {
		columns.put("busId", TableLfBusManager.BUS_ID);
		columns.put("busName", TableLfBusManager.BUS_NAME);
		columns.put("busCode", TableLfBusManager.BUS_CODE);
		columns.put("state", TableLfBusManager.STATE);
		columns.put("icount", "ICOUNT");
		columns.put("createTime", TableLfBusTailTmp.CREATE_TIME);
		columns.put("updateTime", TableLfBusTailTmp.UPDATE_TIME);
		columns.put("name", TableLfSysuser.NAME);
		columns.put("userName", TableLfSysuser.USER_NAME);
		columns.put("depName", TableLfDep.DEP_NAME);
	}
	
	public static Map<String, String> getORM() {
		return columns;
	}

}
