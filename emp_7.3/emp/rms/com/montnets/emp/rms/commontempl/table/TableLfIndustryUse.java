package com.montnets.emp.rms.commontempl.table;

import java.util.LinkedHashMap;
import java.util.Map;

/** 
* @ClassName: TableLfIndustryUse 
* @Description: TODO 
* @author xuty  
* @date 2018-3-20 上午11:44:17 
*  
*/
public class TableLfIndustryUse {
	
		//表名
		public static final String TABLE_NAME = "LF_INDUSTRY_USE";

		//主键
		public static final String ID = "ID";
		
		//行业或用途名			
		public static final String NAME = "NAME";
		
		//操作员ID
		public static final String OPERATOR = "OPERATOR";
		
		//类型- 0：行业，1-用途 ，默认0
		public static final String TYPE = "TYPE";
		
		//创建时间
		public static final String CREATETM = "CREATETM";
		
		//更新时间
		public static final String UPDATETM = "UPDATETM";

		//模板类型
		public static final String TMPTYPE = "TMPTYPE";

		public static final String SEQUENCE	= "LF_INDUSTRY_USE_S";
		protected final static Map<String, String> columns = new LinkedHashMap<String, String>();
		
		static {
			columns.put("LfIndustryUse", TABLE_NAME);
			columns.put("tableId", ID);
			columns.put("id", ID);
			columns.put("name", NAME);
			columns.put("operator", OPERATOR);
			columns.put("type", TYPE);
			columns.put("createtm", CREATETM);
			columns.put("updatetm", UPDATETM);
			columns.put("sequence", SEQUENCE);
			columns.put("tmpType", TMPTYPE);
		}
		
		/**
		 * 返回实体类字段与数据库字段实体类映射的map集合
		 * 
		 * @return
		 */
		public static Map<String, String> getORM() {
			return columns;
		}
		

}
