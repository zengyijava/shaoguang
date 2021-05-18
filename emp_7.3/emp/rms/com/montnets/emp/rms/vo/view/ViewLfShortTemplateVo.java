package com.montnets.emp.rms.vo.view;

import java.util.LinkedHashMap;
import java.util.Map;


public class ViewLfShortTemplateVo {
	//实体类字段与数据库字段实体类映射的map集合
		protected static final Map<String, String> columns = new LinkedHashMap<String, String>();
	    /**
	     * 加载字段
	     */
		static
		{
			//id
			columns.put("id", "ID");
			//tempId
			columns.put("tempId", "TEMPID");
			//tempName
			columns.put("tempName", "TEMPNAME");
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
