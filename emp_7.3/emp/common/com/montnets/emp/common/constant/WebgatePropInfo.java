package com.montnets.emp.common.constant;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IEmpDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.segnumber.PbServicetype;
import com.montnets.emp.smstask.HttpSmsSend;

public class WebgatePropInfo
{
	private IEmpDAO baseDAO=new DataAccessDriver().getEmpDAO();
	private static WebgatePropInfo wp_instance = null;
	private static Map<String,String[]> prop = new HashMap<String,String[]>();


	private WebgatePropInfo() {
		this.getPropInfo();
	}
	
	private void getPropInfo() {
		Properties p = new Properties();   
		String fileName="/SystemGlobals.properties";
	    
	    try
		{
	    	InputStream in = HttpSmsSend.class.getResourceAsStream(fileName);
			p.load(in);
			in.close();
			String[] webgateProp = new String[2];
			//webgateProp[0] = StaticValue.FILE_SERVER_URL;
			webgateProp[0] = StaticValue.getFileServerUrl();
			webgateProp[1] = p.getProperty("montnets.webgate");
			prop.put("webgateProp",webgateProp);
			
			String[] haoduans = this.getHaoduan();
			prop.put("haoduans",haoduans);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取配置文件异常。");
		}
	    
	}
/**
 * 	
 * @return
 */
	synchronized public static Map<String, String[]> getProp()
	{
		if(wp_instance == null || prop == null || prop.size() == 0 ){
			wp_instance = new WebgatePropInfo();
		}
		return prop;
	}

/**
 * 
 * @return
 * @throws Exception
 */
	private String[] getHaoduan() throws Exception
	{
		String[] haoduans=new String[3];
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		List<PbServicetype> haoduanList=baseDAO.findListByCondition(PbServicetype.class, conditionMap, null);
		
		for(int i=0;i<haoduanList.size();i++)
		{
			PbServicetype pbSer=haoduanList.get(i);
			if(pbSer.getSpisuncm()==0)
			{
				haoduans[0]=pbSer.getServiceno();
			}else
			if(pbSer.getSpisuncm()==1)
			{
				haoduans[1]=pbSer.getServiceno();
			}else
			if(pbSer.getSpisuncm()==21)
			{
				haoduans[2]=pbSer.getServiceno();
			}
		}

		return haoduans;
	}
	
	
}
