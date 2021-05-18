package com.montnets.emp.monitor.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;

public class ViewMonBusAreaInfoVo
{
	protected static final Map<String, String>	columns	= new LinkedHashMap<String, String>();
	/**
	 * 加载字段
	 */
	static
	{
		columns.put("busName", "BUS_NAME");
		columns.put("busCode", "BUS_CODE");
		columns.put("areaCode", "AREA_CODE");
		columns.put("monPhone", "MONPHONE");
		columns.put("monemail", "MONEMAIL");
		columns.put("beginTime", "BEGIN_TIME");
		columns.put("endTime", "END_TIME");
		columns.put("id", "ID");
		columns.put("busBaseId", "BUSBASE_ID");
		columns.put("beginHour", "BEGIN_HOUR");
		columns.put("endHour", "END_HOUR");
		columns.put("mtHaveSnd", "MTHAVESND");
		columns.put("deviatHigh", "DEVIAT_HIGH");
		columns.put("deviatLow", "DEVIAT_LOW");
		columns.put("monLastTime", "MON_LASTTIME");
		columns.put("corpCode", "CORP_CODE");
		columns.put("createTime", "CREATE_TIME");
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
