package com.montnets.emp.rms.meditor.entity.view;

import java.util.LinkedHashMap;
import java.util.Map;

import com.montnets.emp.rms.meditor.table.TableLfHfive;

public class ViewLfHfive {
	protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
	/**
	 * 加载字段
	 */
	static
	{
		columns.put("hId", TableLfHfive.HID);
		columns.put("title", TableLfHfive.TITLE);
		columns.put("author", TableLfHfive.AUTHOR);
		//columns.put("createTime", TableLfHfive.CREATETIME);
		//columns.put("commitTime", TableLfHfive.COMMITTIME);
		columns.put("url", TableLfHfive.URL);
		columns.put("updateTime", TableLfHfive.UPDATETIME);
		//columns.put("useTime", TableLfHfive.USETIME);
		columns.put("staus", TableLfHfive.STAUS);
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
