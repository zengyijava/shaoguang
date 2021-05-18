package com.montnets.emp.rms.report.biz;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.report.bean.RptConfInfo;
import com.montnets.emp.rms.report.bean.RptStaticValue;

public class RptConfBiz
{
	protected static Map<String,List<RptConfInfo>> rptConfMap = new HashMap<String,List<RptConfInfo>>();
	
	protected static Map<String,String> titleMap = new HashMap<String,String>();
	
	/**
	 * 初始化报表列配置
	 * @param basePath
	 */
	public static void initRptConf(String basePath)
	{
		// 输入流
		InputStream inputStream = null;
		try
		{
			String configPathStr = "/rms/report/config/rptConf.xml";
			
			File file = new File(basePath + configPathStr);
			if(file == null || file.isDirectory() || !file.exists())
			{
				//设置默认值
				setDefault();
				EmpExecutionContext.error("加载报表列配置数据，获取不到列配置文件。filePath="+basePath + configPathStr);
				return ;
			}
			inputStream = new FileInputStream(file);
			// 通过SAX解析输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
			// 得到根元素
			Element root = document.getRootElement();
			//得到列表
			List<Element> rootElementList = root.elements();
			
			// 遍历所有子节点
			for (Element elMenu : rootElementList)
			{
				List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
				List<Element> elementList = elMenu.elements();
				for (Element elColumn : elementList)
				{
					//配置为不显示的，则不加载
					if("2".equals(elColumn.attributeValue("display")))
					{
						continue;
					}
					RptConfInfo rptCon = new RptConfInfo();
					rptCon.setMenuid(elMenu.attributeValue("menuid"));
					rptCon.setDescription(elMenu.attributeValue("desc"));
					rptCon.setColId(elColumn.attributeValue("colid"));
					rptCon.setDisplay(elColumn.attributeValue("display"));
					rptCon.setName(elColumn.getStringValue());
					if(elColumn.attributeValue("type")!=null){
						rptCon.setType(elColumn.attributeValue("type"));
					}
					rptConList.add(rptCon);
				}
				rptConfMap.put(elMenu.attributeValue("menuid"), rptConList);
			}
		}
		catch (Exception e)
		{
			//设置默认值
			setDefault();
			EmpExecutionContext.error(e, "解析报表配置文件异常。");
		}
		finally
		{
			if(inputStream != null)
			{
				// 释放资源
				try
				{
					inputStream.close();
					inputStream = null;
				}
				catch (IOException e)
				{
					EmpExecutionContext.error(e, "解析报表配置文件，关闭资源异常。");
				}
			}
		}
	}
	
	/**
	 * 设置默认值
	 */
	public static void setDefault()
	{
		/**
		 * 运营商统计报表列配置菜单id，1001
		 */
		List<RptConfInfo> spiConList = getSpiRptConfInfo();
		rptConfMap.put(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID, spiConList);
		

	}
	
	/**
	 * 运营商统计报表
	 * @return
	 */
	private static List<RptConfInfo> getSpiRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
		rptCon1.setDescription("档位统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
		rptCon2.setDescription("档位统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL_COLUMN_ID);
		rptCon2.setDisplay("2");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		
		
		return rptConList;
	}
	

}
