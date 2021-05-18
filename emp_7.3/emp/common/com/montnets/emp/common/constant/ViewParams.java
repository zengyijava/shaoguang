package com.montnets.emp.common.constant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;

/**
 * @project emp
 * @author linzhihan <zhihanking@163.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2012-12-18 下午2:09:09
 * @description 前台显示变量类
 */
public class ViewParams {

	//短信账号
	public static final String SMSACCOUNT="SP账号";
	
	//操作员登录账号
	public static final String LOGINID="登录账号";
	
	//操作员编码
	public static final String SYSUSERCODE="操作员编码";
	
	//公告模块编码
	public static final String GGCODE="1650-1000";
	
	//是否显示余额编码
	public static final String YECODE="1600-1900";
	
	//充值回收模块编码
	public static final String CZCODE="1600-1320";
	
	//提示机构下没有成员
	public static final String DEPNOBODY="该机构下没有成员！";
	
	//提示机构已经存在
	public static final String DEPISEXIST="该机构已添加！";
	
	//短信模板模块编码
	public static final String SMSTEMPCODE="1400-0900";
	
	//彩信模板模块编码
	public static final String MMSTEMPCODE="1100-1500";
	
	//网讯审核模块编码
	public static final String WXRECODE="2600-1090";
	
	//网讯编辑模块编码
	public static final String WXTEMPCODE="2600-1010";
	
	//网讯发送查询模块编码
	public static final String WXTASKCODE="2700-2100";
	
	//信息审批模块编码
	public static final String MSRECODE="1350-1230";
	
	//模板审批模块编码
	public static final String TEMPRECODE="1350-1350";
	
	//短信发送任务查看模块编码
	public static final String SMSTASKCODE="1050-1200";
	
	//彩信发送任务查看模块编码
	public static final String MMSTASKCODE="1100-1800";
	
	//彩信定时信息查看模块编码
	public static final String MMSTIMERCODE="1100-1900";
	
	//审核流程管理模块编码
	public static final String AUDPROCODE="1350-1220";
	
	//微信公众账号管理模块编码
	public static final String ACCTMANAGER="2500-1000";
	
	//短信模板模块权限
	public static final String TEMP_MENU_HTM="/tem_smsTemplate.htm";
	
	protected static final Map<String,String> positionMap = new HashMap<String, String>();
	protected static final Map<String,Map<String,String>> positionLanguageMap = new HashMap<String, Map<String,String>>();
	
	//当前位置分割符
	private static final String separator = "&gt;";
	//空格符
	private static final String kongge = "&nbsp;";
	/**
	 * 设置当前位置的map
	 */
	public static void setPositionMap()
	{
		List<DynaBean> posiList = new SpecialDAO().getPosition();
		if(posiList != null )
		{
			for(DynaBean dyb : posiList)
			{
				Map<String,String> zh_CN =(null==positionLanguageMap.get(StaticValue.ZH_CN))? new HashMap<String, String>():positionLanguageMap.get(StaticValue.ZH_CN);
				Map<String,String> zh_TW =(null==positionLanguageMap.get(StaticValue.ZH_TW))? new HashMap<String, String>():positionLanguageMap.get(StaticValue.ZH_TW);
				Map<String,String> zh_HK =(null==positionLanguageMap.get(StaticValue.ZH_HK))? new HashMap<String, String>():positionLanguageMap.get(StaticValue.ZH_HK);
				String menucode = dyb.get("menucode")==null?"":dyb.get("menucode").toString();
				String menuname = dyb.get("menuname")==null?"":dyb.get("menuname").toString();
				String zh_tw_menuname = dyb.get("zh_tw_menuname")==null?"":dyb.get("zh_tw_menuname").toString();
				String zh_hk_menuname = dyb.get("zh_hk_menuname")==null?"":dyb.get("zh_hk_menuname").toString();
				String modname = dyb.get("modname")==null?"":dyb.get("modname").toString();
				String zh_tw_modname = dyb.get("zh_tw_modname")==null?"":dyb.get("zh_tw_modname").toString();
				String zh_hk_modname = dyb.get("zh_hk_modname")==null?"":dyb.get("zh_hk_modname").toString();
				String title = dyb.get("title")==null?"":dyb.get("title").toString();
				String zh_tw_title = dyb.get("zh_tw_title")==null?"":dyb.get("zh_tw_title").toString();
				String zh_hk_title = dyb.get("zh_hk_title")==null?"":dyb.get("zh_hk_title").toString();
				
				String position = "当前位置：" + kongge + title + kongge + 	separator + kongge + modname + kongge + separator + kongge + menuname  ;
				String position_zh_TW = "當前位置：" + kongge + zh_tw_title + kongge + 	separator + kongge + zh_tw_modname + kongge + separator + kongge + zh_tw_menuname  ;
				String position_zh_HK = "location：" + kongge + zh_hk_title + kongge + 	separator + kongge + zh_hk_modname + kongge + separator + kongge + zh_hk_menuname  ;
				positionMap.put(menucode, position);
				zh_CN.put(menucode, position);
				zh_TW.put(menucode, position_zh_TW);
				zh_HK.put(menucode, position_zh_HK);
				positionLanguageMap.put(StaticValue.ZH_CN, zh_CN);
				positionLanguageMap.put(StaticValue.ZH_TW, zh_TW);
				positionLanguageMap.put(StaticValue.ZH_HK, zh_HK);
			}
		}
	}
	
	/**
	 * 通过机构编码获取当前位置
	 * @param menuCode 机构编码
	 * @return 机构编码对应的当前位置
	 */
	public static String getPosition(String menuCode)
	{
		if(positionMap.size() == 0)
		{
			setPositionMap();
		}
		String position = "<div class='top'><div id='top_right'>" +
				"<div id='top_left'></div><div id='top_main'>" +
				positionMap.get(menuCode) +
				"</div></div></div>";
		return position;
	}
	
	/**
	 * 通过机构编码获取当前位置
	 * @param langName 语言设置(比如zh_CN,zh_TW,zh_HK等)
	 * @param menuCode 机构编码
	 * @return 机构编码对应的当前位置
	 */
	public static String getPosition(String langName, String menuCode)
	{
		if(positionLanguageMap.size() == 0)
		{
			setPositionMap();
		}
		String position = "<div class='top'><div id='top_right'>" +
				"<div id='top_left'></div><div id='top_main'>" +
				positionLanguageMap.get(langName).get(menuCode) +
				"</div></div></div>";
		return position;
	}
	
	/**
	 * 通过机构编码获取当前位置，并支持拼接子级模块
	 * @param langName 语言设置(比如zh_CN,zh_TW,zh_HK等)
	 * @param menuCode 机构编码
	 * @param strings 子级模块位置说明（任意个）
	 * @return
	 */
	public static String getPosition(String langName, String menuCode, String... strings )
	{
		if(positionLanguageMap.size() == 0)
		{
			setPositionMap();
		}
		String menuPosi = positionLanguageMap.get(langName).get(menuCode);
		for(int i=0;i<strings.length;i++)
		{
			menuPosi += kongge + separator + kongge + strings[i];
		}
		String position = "<div class='top'><div id='top_right'>" +
				"<div id='top_left'></div><div id='top_main'>" +
				menuPosi +
				"</div></div></div>";
		return position;
	}
	
	/**
	 * 传入当前模块位置，返回当前位置的html代码
	 * @param menuname 一级模块名称
	 * @param strings 子级模块位置说明（任意个）
	 * @return
	 */
	public static String getPositionWhitIn(String menuname,String... strings )
	{
		String menuPosi = "当前位置：" + kongge + menuname;
		for(int i=0;i<strings.length;i++)
		{
			menuPosi += kongge + separator + kongge + strings[i];
		}
		String position = "<div class='top'><div id='top_right'>" +
			"<div id='top_left'></div><div id='top_main'>" +
			menuPosi +
			"</div></div></div>";
		return position;
	}
	
	/**
	 * 转换显示条数  1000-10000用k条  10000+用万条显示
	 * @description    
	 * @param count
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-20 上午09:26:33
	 */
	public static String countView(int count)
	{
		String viewStr = "0条";
		try
		{
			if(count<10000)
			{
				viewStr = count+"条";
			}
			else if(count>=10000)
			{
				viewStr =(double)count%10000+"万条";
				
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"监控界面短信条数转换显示错误！");
		}
		return viewStr;
	}
	
	/**
	 * 监控主界面账号名称显示处理
	 * @description    
	 * @param count
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-20 上午09:26:33
	 */
	public static String getAcctName(String name)
	{
		String viewStr = "";
		try
		{
			if(name.length()>6)
			{
				viewStr = name.substring(0,6)+"...";
			}
			else
			{
				viewStr = name;
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"监控界面短信条数转换显示错误！");
		}
		return viewStr;
	}
	
	/**
	 * 监控主界面账号名称显示处理
	 * @description    
	 * @param count
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-20 上午09:26:33
	 */
	public static String getGtAcctName(String name)
	{
		String viewStr = "";
		try
		{
			if(name.length()>6)
			{
				viewStr = name.substring(0,6)+"...";
			}
			else
			{
				viewStr = name+"运营商";
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"监控界面短信条数转换显示错误！");
		}
		return viewStr;
	}
}
