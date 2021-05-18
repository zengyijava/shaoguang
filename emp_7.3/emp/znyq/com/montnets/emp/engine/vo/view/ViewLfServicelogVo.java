package com.montnets.emp.engine.vo.view;

import com.montnets.emp.table.engine.TableLfService;
import com.montnets.emp.table.engine.TableLfServicelog;
import com.montnets.emp.table.pasgroup.TableUserdata;
import com.montnets.emp.table.sysuser.TableLfSysuser;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewLfServicelogVo
{
	//实体类字段与数据库字段实体类映射的map集合
    protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
    /**
     * 加载字段
     */
	static
	{
		//id
		columns.put("slId", TableLfServicelog.SL_ID);
		//运行时间
		columns.put("runTime", TableLfServicelog.RUNTIME);
		//状态
		columns.put("slState", TableLfServicelog.SL_STATE);
		//url
		columns.put("url", TableLfServicelog.URL);
		//发送账号
		columns.put("spUser", TableLfService.SP_USER);
		//姓名
		columns.put("staffName", TableUserdata.STAFF_NAME);
		//业务名称
		columns.put("serName", TableLfService.SER_NAME);
		//业务id
		columns.put("serId", TableLfService.SER_ID);
		//姓名
		columns.put("name", TableLfSysuser.NAME);
		//类型
		columns.put("serType", TableLfService.SER_TYPE);		
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
