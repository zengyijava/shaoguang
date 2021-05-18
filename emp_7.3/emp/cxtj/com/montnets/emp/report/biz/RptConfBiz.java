package com.montnets.emp.report.biz;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.report.bean.RptConfInfo;
import com.montnets.emp.report.bean.RptStaticValue;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RptConfBiz
{
	protected static final Map<String,List<RptConfInfo>> rptConfMap = new HashMap<String,List<RptConfInfo>>();
	
	protected static final Map<String,String> titleMap = new HashMap<String,String>();

    public static Map<String, List<RptConfInfo>> getRptConfMap() {
        return rptConfMap;
    }

    public static Map<String, String> getTitleMap() {
        return titleMap;
    }

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
			String configPathStr = "/cxtj/report/config/rptConf.xml";
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
		/**
		 * SP账号统计报表列配置菜单id，1002
		 */
		List<RptConfInfo> spuserRptConList = getSpRptConfInfo();
		rptConfMap.put(RptStaticValue.SPUSER_RPT_CONF_MENU_ID, spuserRptConList);
		/**
		 * 机构报表列配置菜单id，1003
		 */
		List<RptConfInfo> depRptConList = getDepRptConfInfo();
		rptConfMap.put(RptStaticValue.DEP_RPT_CONF_MENU_ID, depRptConList);
		
		/**
		 * 操作员报表列配置菜单id，1004
		 */
		List<RptConfInfo> userRptConList = getUserRptConfInfo();
		rptConfMap.put(RptStaticValue.USER_RPT_CONF_MENU_ID, userRptConList);
		
		/**
		 * 业务类型统计报表列配置菜单id，1005
		 */
		List<RptConfInfo> busRptConList = getBusRptConfInfo();
		rptConfMap.put(RptStaticValue.BUS_RPT_CONF_MENU_ID, busRptConList);
		
		/**
		 * 区域统计报表列配置菜单id，1006
		 */
		List<RptConfInfo> areaRptConList = getAreaRptConfInfo();
		rptConfMap.put(RptStaticValue.AREA_RPT_CONF_MENU_ID, areaRptConList);
		
		/**
		 * 自定义统计报表列配置菜单id，1008
		 */
		List<RptConfInfo> dynRptConList = getDynRptConfInfo();
		rptConfMap.put(RptStaticValue.DYN_RPT_CONF_MENU_ID, dynRptConList);

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
		rptCon1.setDescription("运营商统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.SPISUNCM_RPT_CONF_MENU_ID);
		rptCon2.setDescription("运营商统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		
		return rptConList;
	}
	
	
	/**
	 * SP账号统计报表
	 * @return
	 */
	private static List<RptConfInfo> getSpRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
		rptCon1.setDescription("SP账号统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.SPUSER_RPT_CONF_MENU_ID);
		rptCon2.setDescription("SP账号统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		
		return rptConList;
	}
	
	
	/**
	 * 获取机构报表默认数据
	 * @return
	 */
	private static List<RptConfInfo> getDepRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.DEP_RPT_CONF_MENU_ID);
		rptCon1.setDescription("机构统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.DEP_RPT_CONF_MENU_ID);
		rptCon2.setDescription("机构统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		
		return rptConList;
	}
	
	/**
	 * 获取操作员报表默认数据
	 * @return
	 */
	private static List<RptConfInfo> getUserRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.USER_RPT_CONF_MENU_ID);
		rptCon1.setDescription("操作员统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.USER_RPT_CONF_MENU_ID);
		rptCon2.setDescription("操作员统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		
		return rptConList;
	}
	
	
	/**
	 * 业务类型统计报表
	 * @return
	 */
	private static List<RptConfInfo> getBusRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.BUS_RPT_CONF_MENU_ID);
		rptCon1.setDescription("业务类型统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.BUS_RPT_CONF_MENU_ID);
		rptCon2.setDescription("业务类型统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		return rptConList;
	}
	
	
	/**
	 * 区域统计报表
	 * @return
	 */
	private static List<RptConfInfo> getAreaRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.AREA_RPT_CONF_MENU_ID);
		rptCon1.setDescription("区域统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.AREA_RPT_CONF_MENU_ID);
		rptCon2.setDescription("区域统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		return rptConList;
	}
	
	/**
	 * 自定义参数统计报表
	 * @return
	 */
	private static List<RptConfInfo> getDynRptConfInfo()
	{
		List<RptConfInfo> rptConList = new ArrayList<RptConfInfo>();
		RptConfInfo rptCon1 = new RptConfInfo();
		rptCon1.setMenuid(RptStaticValue.DYN_RPT_CONF_MENU_ID);
		rptCon1.setDescription("自定义参数统计报表");
		rptCon1.setColId(RptStaticValue.RPT_FSSUCC_COLUMN_ID);
		rptCon1.setDisplay("1");
		rptCon1.setName("发送成功数");
		rptConList.add(rptCon1);
		RptConfInfo rptCon2 = new RptConfInfo();
		rptCon2.setMenuid(RptStaticValue.DYN_RPT_CONF_MENU_ID);
		rptCon2.setDescription("自定义参数统计报表");
		rptCon2.setColId(RptStaticValue.RPT_RFAIL2_COLUMN_ID);
		rptCon2.setDisplay("1");
		rptCon2.setName("接收失败数");
		rptConList.add(rptCon2);
		return rptConList;
	}
	
}
