package com.montnets.emp.table.wy;

import java.util.HashMap;
import java.util.Map;

/**
 * 网关路由表table
 * @description 
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2014-3-27 上午09:21:04
 */
public class TableAGwroute 
{

	public static final String TABLE_NAME	= "A_GWROUTE";

	   public static final String AREA= "AREA";

	   public static final String GATESEQ= "GATESEQ";

	   public static final String CREATETIME= "CREATETIME";

	   public static final String GATEID= "GATEID";

	   public static final String STATUS= "STATUS";

	   public static final String SENDTIMEBEGIN= "SENDTIMEBEGIN";

	   public static final String P1= "P1";

	   public static final String P2= "P2";

	   public static final String SIMID= "SIMID";

	   public static final String SENDTIMEEND= "SENDTIMEEND";

	   public static final String ID= "ID";

	   public static final String TYPE= "TYPE";

	   public static final String UNICOM= "UNICOM";

	   public static final String SEQUENCE	= "SEQ_A_GWROUTE";
	   protected static final Map<String , String> columns = new HashMap<String, String>();

		static
		{
			columns.put("AGwroute", TABLE_NAME);
			
			columns.put("tableId", ID);
			
			columns.put("sequence", SEQUENCE);
	       
			columns.put("area", AREA);
        
			columns.put("gateseq", GATESEQ);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("gateid", GATEID);
        
			columns.put("status", STATUS);
        
			columns.put("sendtimebegin", SENDTIMEBEGIN);
        
			columns.put("p1", P1);
        
			columns.put("p2", P2);
        
			columns.put("simid", SIMID);
        
			columns.put("sendtimeend", SENDTIMEEND);
        
			columns.put("id", ID);
        
			columns.put("type", TYPE);
        
			columns.put("unicom", UNICOM);
	        
		};


		public static Map<String , String> getORM()
		{
			return columns;
		}

}
