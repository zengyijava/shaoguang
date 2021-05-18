					
package com.montnets.emp.table.appmage;

import java.util.HashMap;
import java.util.Map;

public class TableLfAppAccount
{
	public static final String TABLE_NAME	= "LF_APP_ACCOUNT";

   public static final String A_ID= "A_ID";

   public static final String MODIFYTIME= "MODIFYTIME";

   public static final String APPID= "APPID";

   public static final String CREATETIME= "CREATETIME";

   public static final String QRCODE= "QRCODE";

   public static final String ACCESS_COUNT= "ACCESS_COUNT";

   public static final String SYNC_STATE= "SYNC_STATE";

   public static final String PORT= "PORT";

   public static final String GRANT_TYPE= "GRANT_TYPE";

   public static final String FAKE_ID= "FAKE_ID";

   public static final String URL= "URL";

   public static final String INFO= "INFO";

   public static final String TYPE= "TYPE";

   public static final String SECRET= "SECRET";

   public static final String TOKEN= "TOKEN";

   public static final String CODE= "CODE";

   public static final String BINDTIME= "BINDTIME";

   public static final String NAME= "NAME";

   public static final String RELEASE_TIME= "RELEASE_TIME";

   public static final String CORP_CODE= "CORP_CODE";

   public static final String PASS= "PWD";

   public static final String CREATER= "CREATER";

   public static final String ACCESS_TOKEN= "ACCESS_TOKEN";

   public static final String ACCESS_TIME= "ACCESS_TIME";

   public static final String APP_TYPE= "APP_TYPE";

   public static final String OPEN_ID= "OPEN_ID";

   public static final String IS_APPROVE= "IS_APPROVE";

   public static final String IMG= "IMG";

   public static final String SYNC_TIME= "SYNC_TIME";
   
   public static final String FILESVR_URL= "FILESVR_URL";
   

   public static final String SEQUENCE	= "509";
   protected static final Map<String , String> columns = new HashMap<String, String>();

	static
	{
		columns.put("LfAppAccount", TABLE_NAME);
		columns.put("tableId", A_ID);
		columns.put("sequence", SEQUENCE);
       
			columns.put("aid", A_ID);
        
			columns.put("modifytime", MODIFYTIME);
        
			columns.put("appid", APPID);
        
			columns.put("createtime", CREATETIME);
        
			columns.put("qrcode", QRCODE);
        
			columns.put("accesscount", ACCESS_COUNT);
        
			columns.put("syncstate", SYNC_STATE);
        
			columns.put("port", PORT);
        
			columns.put("granttype", GRANT_TYPE);
        
			columns.put("fakeid", FAKE_ID);
        
			columns.put("url", URL);
        
			columns.put("info", INFO);
        
			columns.put("type", TYPE);
        
			columns.put("secret", SECRET);
        
			columns.put("token", TOKEN);
        
			columns.put("code", CODE);
        
			columns.put("bindtime", BINDTIME);
        
			columns.put("name", NAME);
        
			columns.put("releasetime", RELEASE_TIME);
        
			columns.put("corpcode", CORP_CODE);
        
			columns.put("pwd", PASS);
        
			columns.put("creater", CREATER);
        
			columns.put("accesstoken", ACCESS_TOKEN);
        
			columns.put("accesstime", ACCESS_TIME);
        
			columns.put("apptype", APP_TYPE);
        
			columns.put("openid", OPEN_ID);
        
			columns.put("isapprove", IS_APPROVE);
        
			columns.put("img", IMG);
        
			columns.put("synctime", SYNC_TIME);
			
			columns.put("fileSvrUrl", FILESVR_URL);
        
	};


	public static Map<String , String> getORM()
	{
		return columns;
	}
}

					