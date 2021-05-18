package com.montnets.emp.shorturl.report.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.shorturl.report.table.TableVstDetail;


public class ViewVstDetailVo {
	
	protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	
	static{
		columns.put("phone", TableVstDetail.PHONE);
		//columns.put("mobileArea", TableVstDetail.MOBILEAREA);
		columns.put("srcAddress", TableVstDetail.SRCADDRESS);
		columns.put("vsttm", TableVstDetail.VSTTM);
	}
	
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
