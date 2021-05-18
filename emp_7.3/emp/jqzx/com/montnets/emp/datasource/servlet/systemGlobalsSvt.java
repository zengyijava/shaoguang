package com.montnets.emp.datasource.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import com.montnets.emp.common.biz.PwdEncryptOrDecrypt;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.TxtFileUtil;

	/**
	 * 初始化配置文件的值
	 * @project p_jqzx
	 * @author may
	 * @company ShenZhen Montnets Technology CO.,LTD.
	 * @datetime 2013-11-29 下午05:28:19
	 * @description
	 */
public class systemGlobalsSvt {
	 static  HashMap globalConf=new HashMap();
	 
	 /**
	  * 把系统配置文件信息加载到内存中
	  */
	public void Init(){
		//数据库类型
		globalConf.put("DBType", getSystemProperty("DBType"));
		//连接类型 Oracle数据库专用
		globalConf.put("connType", getSystemProperty("montnets.emp.connType"));
		//数据库连接池
		globalConf.put("poolType", getSystemProperty("poolType"));
		//数据库IP地址：
		globalConf.put("databaseIp", getSystemProperty("montnets.emp.databaseIp"));
		//数据库端口号
		globalConf.put("databasePort", getSystemProperty("montnets.emp.databasePort"));
		//数据库名称/实例名
		globalConf.put("databaseName", getSystemProperty("montnets.emp.databaseName"));
		//数据库用户名
		globalConf.put("user", getSystemProperty("montnets.emp.user"));
		//数据库密码
		//解密处理
		PwdEncryptOrDecrypt aa=new PwdEncryptOrDecrypt();
		String str=getSystemProperty("montnets.emp.password");
		String str2 = aa.decrypt(str);
		globalConf.put("password", str2);
//		globalConf.put("password", SystemGlobals.getValue("montnets.emp.password"));
		//*****备用数据库配置****
		//是否启用备用
		globalConf.put("use_backup_server", getSystemProperty("montnets.emp.use_backup_server"));
		//备用数据库IP地址：
		globalConf.put("databaseIp2", getSystemProperty("montnets.emp.databaseIp2"));
		//备用数据库端口号
		globalConf.put("databasePort2", getSystemProperty("montnets.emp.databasePort2"));
		//备用数据库名称/实例名
		globalConf.put("databaseName2", getSystemProperty("montnets.emp.databaseName2"));
		//备用数据库用户名
		globalConf.put("user2", getSystemProperty("montnets.emp.user2"));
		//备用数据库密码
		String pwd=getSystemProperty("montnets.emp.password2");
		String pwd2 = aa.decrypt(pwd);
		globalConf.put("password2", pwd2);
		
		//模板文件外网地址
		globalConf.put("outerUrl", getSystemProperty("montnets.fileserver.outerUrl"));
		//模板文件内网地址 
		globalConf.put("innerUrl", getSystemProperty("montnets.fileserver.innerUrl"));
		//网关通讯地址
		globalConf.put("webgate", getSystemProperty("montnets.webgate"));
		globalConf.put("EMPaddress", getSystemProperty("montnets.thisUrl"));
		globalConf.put("EMPOuterAddress", getSystemProperty("montnets.outerUrl"));
		//网讯站点访问地址
		globalConf.put("EMPwxaddress", getSystemProperty("wx.pageurl"));
		//数据库设置
		globalConf.put("maxPoolSize", getSystemProperty("montnets.emp.maxPoolSize"));
		globalConf.put("minPoolSize", getSystemProperty("montnets.emp.minPoolSize"));
		globalConf.put("InitialPoolSize", getSystemProperty("montnets.emp.InitialPoolSize"));
		//页面设置
		globalConf.put("defaultPageSize", getSystemProperty("emp.pageInfo.defaultPageSize"));		
		globalConf.put("frame", getSystemProperty("emp.web.frame"));
		//EMP日志文件存储地址
		globalConf.put("loggeraddress", getSystemProperty("montnets.emp.LogSavePath"));
		globalConf.put("defaultLanguage", this.getSystemProperty("defaultLanguage"));
        globalConf.put("multiLanguageEnable", this.getSystemProperty("multiLanguageEnable"));
        globalConf.put("selectedLanguage", this.getSystemProperty("selectedLanguage"));
	}
	
	/**
	 * 获得配置文件信息
	 * @param key 配置文件的KEY
	 * @param value 配置文件的值
	 * @return 是否保存成功
	 */
	public String   getSystemProperty(String key){
		OrderedProperties prop = new OrderedProperties();// 属性集合对象 
		TxtFileUtil fileUtil=new TxtFileUtil();
		String basePath13 = fileUtil.getWebRoot();

		FileInputStream fis;
		try
		{
			fis = new FileInputStream(basePath13+"WEB-INF/classes/SystemGlobals.properties");
			try
			{
				prop.load(fis);
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e,"加载配置文件出现异常！");
			}// 将属性文件流装载到Properties对象中 
			try
			{
				fis.close();
				
			}
			catch (IOException e)
			{
				EmpExecutionContext.error(e,"文件加载完成异常！");
			}// 关闭流 
			// 修改sitename的属性值 
			return prop.getProperty(key); 
		}
		catch (FileNotFoundException e)
		{
			EmpExecutionContext.error(e,"文件不存在！");
		}// 属性文件输入流 
		return "";
	}
}