					
package com.montnets.emp.table.wxgl;

import java.util.HashMap;
import java.util.Map;

public class TableLfWeixEmoji
{
	public static final String TABLE_NAME	= "LF_WEIX_EMOJI";

   public static final String ID= "ID";

   public static final String SUNICODE= "SUNICODE";

   public static final String SUTF8= "SUTF8";

   public static final String SUTF16= "SUTF16";

   public static final String SSBUNICODE= "SSBUNICODE";

   public static final String FILENAME= "FILENAME";

   public static final String SEQUENCE	= "S_LF_WEIX_EMOJI";
   protected static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfWeixEmoji", TABLE_NAME);
		columns.put("tableId", ID);
		columns.put("sequence", SEQUENCE);
       
		columns.put("id", ID);
		
		//Unicode编码
		columns.put("unicode", SUNICODE);
		//UTF8编码
		columns.put("utf8", SUTF8);
		//UTF16编码
		columns.put("utf16", SUTF16);
		//SBUnicode编码
		columns.put("sbunicode", SSBUNICODE);
		//文件名
		columns.put("filename", FILENAME);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					