					
package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppMwClient
{
	public static final String TABLE_NAME	= "LF_APP_MW_CLIENT";

   public static final String LOCAL_IMG_URL= "LOCAL_IMG_URL";

   public static final String GUID= "GUID";

   public static final String UNAME= "UNAME";

   public static final String MODIFYTIME= "MODIFYTIME";

   public static final String A_ID= "A_ID";

   public static final String WC_ID= "WC_ID";

   public static final String G_ID= "G_ID";

   public static final String VERIFYTIME= "VERIFYTIME";

   public static final String CREATETIME= "CREATETIME";

   public static final String PHONE= "PHONE";

   public static final String SUBSCRIBE= "SUBSCRIBE";

   public static final String SUBSCRIBE_TIME= "SUBSCRIBE_TIME";

   public static final String QQ= "QQ";

   public static final String SEX= "SEX";

   public static final String EMAIL= "EMAIL";

   public static final String FAKE_ID= "FAKE_ID";

   public static final String U_ID= "U_ID";

   public static final String DESCR= "DESCR";

   public static final String HEAD_IMG_URL= "HEAD_IMG_URL";

   public static final String SYN_ID= "SYN_ID";

   public static final String AGE= "AGE";

   public static final String LANGUAGE= "LANGUAGE";

   public static final String UTYPE= "UTYPE";

   public static final String COUNTRY= "COUNTRY";

   public static final String APP_CODE= "APP_CODE";

   public static final String CORP_CODE= "CORP_CODE";

   public static final String NICK_NAME= "NICK_NAME";

   public static final String PROVINCE= "PROVINCE";

   public static final String CITY= "CITY";

   public static final String OPEN_ID= "OPEN_ID";
   
   public static final String ECODE= "ECODE";
   

   public static final String SEQUENCE	= "S_LF_APP_MW_CLIENT";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppMwClient", TABLE_NAME);
		columns.put("tableId", WC_ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("localimgurl", LOCAL_IMG_URL);
        
			columns.put("guid", GUID);
        
			columns.put("uname", UNAME);
        
			columns.put("modifytime", MODIFYTIME);
        
			columns.put("aid", A_ID);
        
			columns.put("wcid", WC_ID);
        
			columns.put("gid", G_ID);
        
			columns.put("verifytime", VERIFYTIME);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("phone", PHONE);
        
			columns.put("subscribe", SUBSCRIBE);
        
			columns.put("subscribetime", SUBSCRIBE_TIME);
        
			columns.put("qq", QQ);
        
			columns.put("sex", SEX);
        
			columns.put("email", EMAIL);
        
			columns.put("fakeid", FAKE_ID);
        
			columns.put("uid", U_ID);
        
			columns.put("descr", DESCR);
        
			columns.put("headimgurl", HEAD_IMG_URL);
        
			columns.put("synid", SYN_ID);
        
			columns.put("age", AGE);
        
			columns.put("language", LANGUAGE);
        
			columns.put("utype", UTYPE);
        
			columns.put("country", COUNTRY);
        
			columns.put("appcode", APP_CODE);
        
			columns.put("corpcode", CORP_CODE);
        
			columns.put("nickname", NICK_NAME);
        
			columns.put("province", PROVINCE);
        
			columns.put("city", CITY);
        
			columns.put("openid", OPEN_ID);
			
			columns.put("ecode", ECODE);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					