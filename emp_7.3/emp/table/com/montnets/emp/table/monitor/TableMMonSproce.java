package com.montnets.emp.table.monitor;


import java.util.HashMap;
import java.util.Map;

/**
 * 监控程序基本信息
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-11-26 下午03:49:58
 */
public class TableMMonSproce
{
	public static final String TABLE_NAME	= "M_MON_SPROCE";

   public static final String PROCETYPE= "PROCETYPE";

   public static final String PROCEID= "PROCEID";

   public static final String MONSTATUS= "MONSTATUS";

   public static final String PTCODE= "PTCODE";

   public static final String PROCENAME= "PROCENAME";

   public static final String MODIFYTIME= "MODIFYTIME";

   public static final String FILEPATH= "FILEPATH";

   public static final String UPGRADETIME= "UPGRADETIME";

   public static final String BAKHOSTID= "BAKHOSTID";

   public static final String BAKFILEPATH= "BAKFILEPATH";

   public static final String HOSTID= "HOSTID";

   public static final String CREATETIME= "CREATETIME";

   public static final String MONFREQ= "MONFREQ";

   public static final String DESCR= "DESCR";

   public static final String VERSION= "VERSION";

   public static final String SEQUENCE	= "S_M_MON_SPROCE";
   protected final static Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("MMonSproce", TABLE_NAME);
		columns.put("tableId", PROCEID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("procetype", PROCETYPE);
        
			columns.put("proceid", PROCEID);
        
			columns.put("monstatus", MONSTATUS);
        
			columns.put("ptcode", PTCODE);
        
			columns.put("procename", PROCENAME);
        
			columns.put("modifytime", MODIFYTIME);
        
			columns.put("filepath", FILEPATH);
        
			columns.put("upgradetime", UPGRADETIME);
        
			columns.put("bakhostid", BAKHOSTID);
        
			columns.put("bakfilepath", BAKFILEPATH);
        
			columns.put("hostid", HOSTID);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("monfreq", MONFREQ);
        
			columns.put("descr", DESCR);
        
			columns.put("version", VERSION);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					