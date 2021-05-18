package com.montnets.emp.common.servlet;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.constant.PropertiesLoader;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

public class InitMenuSvt extends HttpServlet{


	/**
	 * 
	 */
	private static final long serialVersionUID = 7263797260049573839L;

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); 
	}

	public void init() throws ServletException {
		//加载模块权限
		modular();
		initVersion();
	}
	
	/**
	 * 读取配置文件是否是模块化并读取模块配置文件
	 */
	public void modular()
	{
		try
		{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String modular=SystemGlobals.getValue("modular");
			//非模块化
			if(modular!=null )
			{
				if("1".equals(modular))
				{
					modular=modular.trim();
					modular=modular.trim();
					//去缓存
					StaticValue.getInniMenuMap().clear();
					//StaticValue.menu_num=null;
					StaticValue.setMenu_num(null);
					
					StaticValue.setInniMenuMap(new HashMap<String, String>());
					//StaticValue.menu_num=new StringBuffer("");
					StaticValue.setMenu_num(new StringBuffer(""));
					//要读取的模块
					String modularMenuStr=SystemGlobals.getValue("modularMenu");
					String [] modularMenu=modularMenuStr.split(",");
					for(int i=0;i<modularMenu.length;i++)
					{
						this.initMenu("/menuPro/"+modularMenu[i]);
					}
				}
				else if("0".equals(modular))
				{
					getWebMenu();
				}
				else
				{
					EmpExecutionContext.error("["+simpleDateFormat.format(new Date())+"]modular模块化配置错误!");
				}
				
			}
			else
			{
				EmpExecutionContext.error("["+simpleDateFormat.format(new Date())+"]modular模块化配置未配置!");
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "svt读取配置文件是否是模块化并读取模块配置文件异常。");
		}
	}
	
	/**
	 *   获取配置的模块
	 * @return
	 */
	public List<String> getWebMenu() 
	{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//去缓存
		StaticValue.getInniMenuMap().clear();
		//StaticValue.menu_num=null;
		StaticValue.setMenu_num(null);
		
		StaticValue.setInniMenuMap(new HashMap<String, String>());
		//StaticValue.menu_num=new StringBuffer("");
		StaticValue.setMenu_num(new StringBuffer(""));
		
		//读取了哪些些模块的properties
		String strMenuPro="";
    	List<String> menuList = new ArrayList<String>();
    	BufferedReader br = null;
		try{
			String fileName = new TxtFileUtil().getWebRoot() +  "WEB-INF/web.xml";
			String str = null;
	    	br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
	    	//模块ID对应其模块名称 启用的模块
			while ((str = br.readLine()) != null) {
				try
				{
					if(str.contains("&") && str.contains(";")){
						str = str.trim();
						//判断是启用还是注释的模块
						//获取首几个字母
						String substr = str.substring(0,2);
						//该模块处于启用状态
						if(substr.contains("&")){
							String name = str.substring(str.indexOf("&")+1,str.indexOf(";"));
							//获取模块的名称
							if(name != null && !"".equals(name)){
								//读取启用的模块配置文件
								//menuList.add(name);
								this.initMenu("/menuPro/"+name);
								strMenuPro=strMenuPro+","+name;
							}
						}
					}
				}
				catch (Exception e) {
					EmpExecutionContext.error(e, "判断是启用还是注释的模块异常。");
					EmpExecutionContext.error(str);
				}
				str = null;
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "获取配置的模块异常。");
		}finally
		{
			if(br != null)
			{
				try {
					br.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "获取配置的模块，关闭资源异常。");
				}
			}
		}
		if(strMenuPro.indexOf(",")!=-1)
		{
			strMenuPro=strMenuPro.substring(1);
		}
		//打印已加载的模块配置文件
		System.out.println("["+simpleDateFormat.format(new Date())+"] "+strMenuPro+"!");
		return menuList;
	}
	
	private void initVersion()
	{
		BufferedReader br = null;
		try
		{
			String fileName = new TxtFileUtil().getWebRoot() +  "init.txt";
			File file = new File(fileName);
			if(!file.exists())
			{
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				System.out.println("["+simpleDateFormat.format(new Date())+"] "+"init配置文件不存在！");
				return;
			}
			String str = null;
	    	br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
			while ((str = br.readLine()) != null) 
			{
				if(str.indexOf("=")>-1)
				{
					String[] strArr = str.split("=");
					if(StaticValue.EMP_WEB_VERSION.equals(strArr[0]))
					{
						StaticValue.setEmpVersion(strArr[1]);
					}
				}
			}
		}catch (Exception e) {
			EmpExecutionContext.error(e, "读取init文件版本异常。");
		}finally
		{
			if(br != null)
			{
				try {
					br.close();
				} catch (IOException e) {
					EmpExecutionContext.error(e, "读取init文件版本，关闭资源异常。");
				}
			}
		}
	}
	
	/**
	 * 初始化读取配置文件信息
	 */
	public void initMenu(String path)
	{
		try
		{
			PropertiesLoader propertiesLoader = new PropertiesLoader();
			Properties properties =null;
	    	try
			{
				properties = propertiesLoader.getProperties(path);
			}
			catch (Exception e) {
				//EmpExecutionContext.error("["+path+"找不到]");
				EmpExecutionContext.error(e, "获取配置文件异常。");
			}
			if(properties!=null)
			{
				
				Set keyValue = properties.keySet();
				for (Iterator it = keyValue.iterator(); it.hasNext();)
				{
					String key = (String) it.next();
					String value=(String)properties.get(key);
					StaticValue.getInniMenuMap().put(value, value);
				}
				String menu_num=properties.getProperty("menu_num").toString();
				//StaticValue.menu_num.append(menu_num+",");
				StaticValue.getMenu_num().append(menu_num+",");
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "初始化读取配置文件信息，加载模块异常。");
		}
	}
	
}
