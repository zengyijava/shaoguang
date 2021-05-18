package com.montnets.emp.datasource.servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.PwdEncryptOrDecrypt;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.system.LfUser2skin;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.TxtFileUtil;
/**
 * 处理EMP数据配置文件
 * @project p_jqzx
 * @author may
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-11-20 下午03:43:40
 * @description 用于修改系统数据连接的初始值
 */
public class systemManageSvt  extends BaseServlet
{

	private static final long	serialVersionUID	= 1L;
	private final String basePath = "/datasource";
	private final String empPath = "/txgl";
	final systemGlobalsSvt  sg=new systemGlobalsSvt();

	/**
	 * 跳转到菜单页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toMenu(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		//防止在地址栏用其他用户
		Object sysuserObj=request.getSession().getAttribute("loginSysuser");
        //mwadmin登录 会话中userid为null 其他操作员登录 userid都有非空值
		if(sysuserObj == null || ((LfSysuser)sysuserObj).getUserId() != null){
			response.sendRedirect(request.getContextPath()+"/common/logoutEmp.html");
            return;
		}

		request.getRequestDispatcher(this.empPath+this.basePath+"/dat_tree.jsp")
		.forward(request, response);
	}

	/**
	 * 跳转到框架页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toSkip(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		//防止在地址栏用其他用户
		Object sysuserObj=request.getSession().getAttribute("loginSysuser");
        //mwadmin登录 会话中userid为null 其他操作员登录 userid都有非空值
		if(sysuserObj == null || ((LfSysuser)sysuserObj).getUserId() != null){
			response.sendRedirect(request.getContextPath()+"/common/logoutEmp.html");
            return;
		}

		String path = request.getContextPath();
		HttpSession session = request.getSession(false);
		session.setAttribute("stlyeSkin", path + "/frame/frame3.0/skin/default");

		request.getRequestDispatcher(this.empPath+this.basePath+"/dat_systemConfig.jsp")
		.forward(request, response);
	}

	/**
	 * 查询数据源信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		//防止在地址栏用其他用户
		Object sysuserObj=request.getSession().getAttribute("loginSysuser");
        //mwadmin登录 会话中userid为null 其他操作员登录 userid都有非空值
		if(sysuserObj == null || ((LfSysuser)sysuserObj).getUserId() != null){
			response.sendRedirect(request.getContextPath()+"/common/logoutEmp.html");
            return;
		}
		//数据库类型
		request.setAttribute("DBType", sg.globalConf.get("DBType")==null?SystemGlobals.getValue("DBType"):sg.globalConf.get("DBType"));
		//连接类型 ORACLE数据库有用
		request.setAttribute("connType", sg.globalConf.get("connType")==null?SystemGlobals.getValue("montnets.emp.connType"):sg.globalConf.get("connType"));
		//数据库连接池
		request.setAttribute("poolType", sg.globalConf.get("poolType")==null?SystemGlobals.getValue("poolType"):sg.globalConf.get("poolType"));
		//数据库IP地址：
		request.setAttribute("databaseIp", sg.globalConf.get("databaseIp")==null?SystemGlobals.getValue("montnets.emp.databaseIp"):sg.globalConf.get("databaseIp"));
		//数据库端口号
		request.setAttribute("databasePort", sg.globalConf.get("databasePort")==null?SystemGlobals.getValue("montnets.emp.databasePort"):sg.globalConf.get("databasePort"));
		//数据库名称/实例名
		request.setAttribute("databaseName", sg.globalConf.get("databaseName")==null?SystemGlobals.getValue("montnets.emp.databaseName"):sg.globalConf.get("databaseName"));
		//数据库用户名
		request.setAttribute("user", sg.globalConf.get("user")==null?SystemGlobals.getValue("montnets.emp.user"):sg.globalConf.get("user"));
		//数据库密码
		PwdEncryptOrDecrypt aa=new PwdEncryptOrDecrypt();
		String str=SystemGlobals.getValue("montnets.emp.password");
		String str2 = aa.decrypt(str);
		request.setAttribute("password", sg.globalConf.get("password")==null?str2:sg.globalConf.get("password"));
		//EMP访问地址

		//*****备用数据库配置****
		//是否启用备用：
		request.setAttribute("use_backup_server", sg.globalConf.get("use_backup_server")==null?SystemGlobals.getValue("montnets.emp.use_backup_server"):sg.globalConf.get("use_backup_server"));
		//备用ip
		request.setAttribute("databaseIp2", sg.globalConf.get("databaseIp2")==null?SystemGlobals.getValue("montnets.emp.databaseIp2"):sg.globalConf.get("databaseIp2"));
		//备用数据库端口号
		request.setAttribute("databasePort2", sg.globalConf.get("databasePort2")==null?SystemGlobals.getValue("montnets.emp.databasePort2"):sg.globalConf.get("databasePort2"));
		//备用数据库名称/实例名
		request.setAttribute("databaseName2", sg.globalConf.get("databaseName2")==null?SystemGlobals.getValue("montnets.emp.databaseName2"):sg.globalConf.get("databaseName2"));
		//备用数据库用户名
		request.setAttribute("user2", sg.globalConf.get("user2")==null?SystemGlobals.getValue("montnets.emp.user2"):sg.globalConf.get("user2"));
		//备用数据库密码
		String pwd=SystemGlobals.getValue("montnets.emp.password2");
		String pwd2 = aa.decrypt(pwd);
		request.setAttribute("password2", sg.globalConf.get("password2")==null?pwd2:sg.globalConf.get("password2"));




		//模板文件外网地址
		request.setAttribute("outerUrl", sg.globalConf.get("outerUrl")==null?SystemGlobals.getValue("montnets.fileserver.outerUrl"):sg.globalConf.get("outerUrl"));
		//模板文件内网地址
		request.setAttribute("innerUrl",sg.globalConf.get("innerUrl")==null?SystemGlobals.getValue("montnets.fileserver.innerUrl"):sg.globalConf.get("innerUrl"));
		//网关通讯地址
		request.setAttribute("webgate", sg.globalConf.get("webgate")==null?SystemGlobals.getValue("montnets.webgate"):sg.globalConf.get("webgate"));
		request.setAttribute("EMPaddress", sg.globalConf.get("EMPaddress")==null?SystemGlobals.getValue("montnets.thisUrl"):sg.globalConf.get("EMPaddress"));
		request.setAttribute("EMPOuterAddress", sg.globalConf.get("EMPOuterAddress")==null?SystemGlobals.getValue("montnets.outerUrl"):sg.globalConf.get("EMPOuterAddress"));
		//网讯站点访问地址
		request.setAttribute("EMPwxaddress", sg.globalConf.get("EMPwxaddress")==null?SystemGlobals.getValue("wx.pageurl"):sg.globalConf.get("EMPwxaddress"));
		//数据库设置
		request.setAttribute("maxPoolSize", sg.globalConf.get("maxPoolSize")==null?SystemGlobals.getValue("montnets.emp.maxPoolSize"):sg.globalConf.get("maxPoolSize"));
		request.setAttribute("minPoolSize", sg.globalConf.get("minPoolSize")==null?SystemGlobals.getValue("montnets.emp.minPoolSize"):sg.globalConf.get("minPoolSize"));
		request.setAttribute("InitialPoolSize", sg.globalConf.get("InitialPoolSize")==null?SystemGlobals.getValue("montnets.emp.InitialPoolSize"):sg.globalConf.get("InitialPoolSize"));
		//页面设置
		request.setAttribute("defaultPageSize", sg.globalConf.get("defaultPageSize")==null?SystemGlobals.getValue("emp.pageInfo.defaultPageSize"):sg.globalConf.get("defaultPageSize"));
		request.setAttribute("frame", sg.globalConf.get("frame")==null?SystemGlobals.getValue("emp.web.frame"):sg.globalConf.get("frame"));
		request.setAttribute("smsSplit", sg.globalConf.get("emp.sms.split")==null?SystemGlobals.getValue("emp.sms.split"):sg.globalConf.get("emp.sms.split"));

		//EMP日志文件保存路径
		String loggeraddress=sg.globalConf.get("loggeraddress")==null?String.valueOf(SystemGlobals.getValue("montnets.emp.LogSavePath")):String.valueOf(sg.globalConf.get("loggeraddress"));
		//获取根目录地址
		String defaultloggeraddress=this.getServletConfig().getServletContext().getRealPath("/");
		//如果配置文件的地址为-1或者为空，则界面显示日志的默认地址
		if(loggeraddress==null||"null".equals(loggeraddress.trim())||"-1".equals(loggeraddress.trim())||"".equals(loggeraddress.trim())){
			loggeraddress=defaultloggeraddress;
		}
		request.setAttribute("loggeraddress",loggeraddress);
		request.setAttribute("defaultloggeraddress",defaultloggeraddress);
		//是否集群
		request.setAttribute("isCluster", sg.globalConf.get("isCluster")==null?SystemGlobals.getValue("cluster.iscluster"):sg.globalConf.get("isCluster"));
		//集群服务器编号
		request.setAttribute("serverNumber", sg.globalConf.get("serverNumber")==null?SystemGlobals.getValue("cluster.server.number"):sg.globalConf.get("serverNumber"));
		//备份内网地址
		request.setAttribute("bakinnerUrl", sg.globalConf.get("bakinnerUrl")==null?SystemGlobals.getValue("montnets.fileserver.bak.innerUrl"):sg.globalConf.get("bakinnerUrl"));
		//备份外网地址
		request.setAttribute("bakouterUrl", sg.globalConf.get("bakouterUrl")==null?SystemGlobals.getValue("montnets.fileserver.bak.outerUrl"):sg.globalConf.get("bakouterUrl"));
		//多语言开关
		request.setAttribute("defaultLanguage", sg.globalConf.get("defaultLanguage")==null?SystemGlobals.getValue("defaultLanguage"):sg.globalConf.get("defaultLanguage"));
		request.setAttribute("multiLanguageEnable", sg.globalConf.get("multiLanguageEnable")==null?SystemGlobals.getValue("multiLanguageEnable"):sg.globalConf.get("multiLanguageEnable"));
		request.setAttribute("selectedLanguage", sg.globalConf.get("selectedLanguage")==null?SystemGlobals.getValue("selectedLanguage"):sg.globalConf.get("selectedLanguage"));

		request.getRequestDispatcher(this.empPath+this.basePath+"/dat_systemDataSet.jsp")
		.forward(request, response);
	}
	/**
	 * 修改数据源信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		//setProperty(request,"montnets.fileserver.innerUrl",request.getParameter("innerUrl"));
		//数据库类型
		setProperty("DBType",request.getParameter("DBType"));
		//连接类型 ORACLE有用
		setProperty("montnets.emp.connType",request.getParameter("connType"));
		//数据库连接池
		String[] type=request.getParameterValues("type");
		String typeValue="1";
		if(type!=null&&type.length>0){
			typeValue=type[0];
			setProperty("poolType",typeValue);
		}
		PwdEncryptOrDecrypt dbed=new PwdEncryptOrDecrypt();
		//数据库IP地址：
		setProperty("montnets.emp.databaseIp",request.getParameter("dbconIp"));
		//数据库端口号
		setProperty("montnets.emp.databasePort",request.getParameter("port"));
		//数据库名称/实例名
		setProperty("montnets.emp.databaseName",request.getParameter("dbName"));
		//数据库用户名
		setProperty("montnets.emp.user",request.getParameter("dbUser"));
		//数据库密码
		setProperty("montnets.emp.password",dbed.encrypt(request.getParameter("dbPwd")));


		//备用数据库开关：
		setProperty("montnets.emp.use_backup_server",request.getParameter("use_backup_server"));
		if("1".equals(request.getParameter("use_backup_server")))
		{
			//备用数据库IP地址：
			setProperty("montnets.emp.databaseIp2",request.getParameter("dbconIp2"));
			//备用数据库端口号
			setProperty("montnets.emp.databasePort2",request.getParameter("port2"));
			//备用数据库名称/实例名
			setProperty("montnets.emp.databaseName2",request.getParameter("dbName2"));
			//备用数据库用户名
			setProperty("montnets.emp.user2",request.getParameter("dbUser2"));
			//备用数据库密码
			setProperty("montnets.emp.password2",dbed.encrypt(request.getParameter("dbPwd2")));
		}

		//模板文件外网地址
		setProperty("montnets.fileserver.outerUrl",request.getParameter("outerUrl"));
		//模板文件内网地址
		setProperty("montnets.fileserver.innerUrl",request.getParameter("innerUrl"));
		//网关通讯地址
		setProperty("montnets.webgate",request.getParameter("webgate"));
		//EMP地址
		setProperty("montnets.thisUrl",request.getParameter("EMPaddress"));
		setProperty("montnets.outerUrl",request.getParameter("EMPOuterAddress"));
		//网讯站点访问地址
		setProperty("wx.pageurl",request.getParameter("EMPwxaddress"));

		//是否集群
		setProperty("cluster.iscluster",request.getParameter("isCluster"));
		//集群服务器编号
		setProperty("cluster.server.number",request.getParameter("serverNumber"));
		setProperty("emp.sms.split",request.getParameter("smsSplit"));

		StringBuffer innersf=new StringBuffer();
		StringBuffer outersf=new StringBuffer();
		for(int i=1;i<6;i++){
			String bakinnerUrl=request.getParameter("bakinnerUrl"+i);
			if(bakinnerUrl!=null&&!"".equals(bakinnerUrl)&&!"null".equals(bakinnerUrl)){
				innersf.append(bakinnerUrl+",");
			}
			String bakouterUrl=request.getParameter("bakouterUrl"+i);
			if(bakouterUrl!=null&&!"".equals(bakouterUrl)&&!"null".equals(bakouterUrl)){
				outersf.append(bakouterUrl+",");
			}
		}
		//备份内网地址
		if(innersf!=null&&!"".equals(innersf.toString())){
			setProperty("montnets.fileserver.bak.innerUrl",innersf.substring(0, innersf.length()-1));
			//数据库类型
			sg.globalConf.put("bakinnerUrl", innersf.substring(0, innersf.length()-1));
		}

		//备份外网地址
		if(outersf!=null&&!"".equals(outersf.toString())){
			setProperty("montnets.fileserver.bak.outerUrl",outersf.substring(0, outersf.length()-1));
			//数据库类型
			sg.globalConf.put("bakouterUrl", outersf.substring(0, outersf.length()-1));
		}

		//EMP存储地址
		//如果默认存储地址和设置的存储地址一样，则设置为-1
		if(request.getParameter("defaultloggeraddress").equals(request.getParameter("loggeraddress"))){
			setProperty("montnets.emp.LogSavePath","-1");
		}else{
			//不一样，设置为新的存储地址
			setProperty("montnets.emp.LogSavePath",request.getParameter("loggeraddress"));
		}
		boolean result=setProperty("ConnectDataBaseFlag","1");


		//数据库类型
		sg.globalConf.put("DBType", request.getParameter("DBType"));
		//数据库连接池
		if(type!=null&&type.length>0){
			sg.globalConf.put("poolType", type[0]);
		}

		//数据库IP地址：
		sg.globalConf.put("databaseIp", request.getParameter("dbconIp"));
		//数据库端口号
		sg.globalConf.put("databasePort", request.getParameter("port"));
		//数据库名称/实例名
		sg.globalConf.put("databaseName", request.getParameter("dbName"));
		//数据库用户名
		sg.globalConf.put("user", request.getParameter("dbUser"));
		//数据库密码
		sg.globalConf.put("password", request.getParameter("dbPwd"));
		//EMP访问地址

		//模板文件外网地址
		sg.globalConf.put("outerUrl", request.getParameter("outerUrl"));
		//模板文件内网地址
		sg.globalConf.put("innerUrl", request.getParameter("innerUrl"));
		//网关通讯地址
		sg.globalConf.put("webgate", request.getParameter("webgate"));
		sg.globalConf.put("EMPaddress", request.getParameter("EMPaddress"));
		sg.globalConf.put("EMPOuterAddress", request.getParameter("EMPOuterAddress"));
		//网讯站点访问地址
		sg.globalConf.put("EMPwxaddress", request.getParameter("EMPwxaddress"));
		//数据库设置
		sg.globalConf.put("maxPoolSize", request.getParameter("maxPoolSize"));
		sg.globalConf.put("minPoolSize", request.getParameter("minPoolSize"));
		sg.globalConf.put("InitialPoolSize", request.getParameter("InitialPoolSize"));
//		//页面设置
		sg.globalConf.put("defaultPageSize", request.getParameter("defaultPageSize"));
		sg.globalConf.put("frame", request.getParameter("frame"));
		sg.globalConf.put("emp.sms.split", request.getParameter("smsSplit"));
		//将EMP存储日志路径放在内存
		sg.globalConf.put("loggeraddress", request.getParameter("loggeraddress"));
		//连接类型 ORACLE有用
		sg.globalConf.put("connType", request.getParameter("connType"));
		//是否集群
		sg.globalConf.put("isCluster", request.getParameter("isCluster"));
		//集群服务器编号
		sg.globalConf.put("serverNumber", request.getParameter("serverNumber"));

		/*语言相关*/
		sg.globalConf.put("defaultLanguage", request.getParameter("defaultLanguage"));
		sg.globalConf.put("multiLanguageEnable", request.getParameter("langEnable"));
		sg.globalConf.put("selectedLanguage", request.getParameter("langs"));

		if(result){
			response.getWriter().print("savesucess");
		}


		//添加日志信息 pengj
		//EMP日志文件保存路径
		String loggeraddress1=String.valueOf(SystemGlobals.getValue("montnets.emp.LogSavePath"));
		//获取根目录地址
		String defaultloggeraddress1=this.getServletConfig().getServletContext().getRealPath("/");
		//如果配置文件的地址为-1，则界面显示日志的默认地址
		if("-1".equals(loggeraddress1)||"".equals(loggeraddress1)){
			loggeraddress1=defaultloggeraddress1;
		}
		String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)
				+"，修改前："
				+"数据库类型："+SystemGlobals.getValue("DBType")
				+"，连接类型："+SystemGlobals.getValue("montnets.emp.connType")
				+"，数据库连接池:"+SystemGlobals.getValue("poolType")
				+"，数据库IP地址：" + SystemGlobals.getValue("montnets.emp.databaseIp")
				+"，数据库端口号：" + SystemGlobals.getValue("montnets.emp.databasePort")
				+"，数据库名称/实例名：" + SystemGlobals.getValue("montnets.emp.databaseName")
				+"，数据库用户名：" + SystemGlobals.getValue("montnets.emp.user")
				+"，数据库密码：" + SystemGlobals.getValue("montnets.emp.password")
				+"，模板文件外网地址：" + SystemGlobals.getValue("montnets.fileserver.outerUrl")
				+"，模板文件内网地址 ：" + SystemGlobals.getValue("montnets.fileserver.innerUrl")
				+"，网关通讯地址：" + SystemGlobals.getValue("montnets.webgate")
				+"，网讯站点访问地址：" + SystemGlobals.getValue("wx.pageurl")
				+"，EMP日志文件存储地址：" + loggeraddress1
				+"；修改后："
				+"数据库类型："+sg.globalConf.get("DBType")
				+"，连接类型："+sg.globalConf.get("connType")
				+"，数据库连接池:"+sg.globalConf.get("poolType")
				+"，数据库IP地址：" + sg.globalConf.get("databaseIp")
				+"，数据库端口号：" + sg.globalConf.get("databasePort")
				+"，数据库名称/实例名：" + sg.globalConf.get("databaseName")
				+"，数据库用户名：" + sg.globalConf.get("user")
				+"，数据库密码：" + dbed.encrypt(sg.globalConf.get("password").toString())
				+"，模板文件外网地址：" + sg.globalConf.get("outerUrl")
				+"，模板文件内网地址 ：" + sg.globalConf.get("innerUrl")
				+"，网关通讯地址：" + sg.globalConf.get("webgate")
				+"，网讯站点访问地址：" + sg.globalConf.get("EMPwxaddress")
				+"，EMP日志文件存储地址：" + sg.globalConf.get("loggeraddress");
		EmpExecutionContext.info(opContent);



	}

	/**
	 * 修改配置文件信息
	 * @param key 配置文件的KEY
	 * @param value 配置文件的值
	 * @return 是否保存成功
	 */
	public boolean  setProperty( String key,String value){
		OrderedProperties prop = new OrderedProperties();// 属性集合对象
		TxtFileUtil fileUtil=new TxtFileUtil();
		String basePath13 = fileUtil.getWebRoot();
		boolean save=true;
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
				save=false;
				EmpExecutionContext.error(e,"文件加载完成异常！");
			}// 关闭流
			// 获取属性值，sitename已在文件中定义
			// 获取属性值，country未在文件中定义，将在此程序中返回一个默认值，但并不修改属性文件
			// System.out.println("获取属性值：country=" + prop.getProperty("country", "中国"));

			// 修改sitename的属性值
			prop.setProperty(key, value);
			// 文件输出流
			FileOutputStream fos = new FileOutputStream(basePath13+"WEB-INF/classes/SystemGlobals.properties");
			// 将Properties集合保存到流中
			try
			{
				prop.store(fos, "");
			}
			catch (IOException e)
			{
				save=false;
				EmpExecutionContext.error(e,"将Properties集合保存到流中异常！");
			}
			try
			{
				fos.close();
			}
			catch (IOException e)
			{
				save=false;
				EmpExecutionContext.error(e,"关闭流异常！");
			}// 关闭流
		}
		catch (FileNotFoundException e)
		{
			EmpExecutionContext.error(e,"文件不存在！");
		}// 属性文件输入流
		return save;
	}


	/**
	 * 修改用户密码
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void modifPwd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String configAdminPwd=SystemGlobals.getValue("configAdminPwd");

		String confirmPwd=request.getParameter("confirmPwd");
		String oldpwd=request.getParameter("oldPwd");
		if(!configAdminPwd.equals(MD5.getMD5Str(oldpwd))){
			request.setAttribute("executResult", "differ");
		}
		boolean result=setProperty("configAdminPwd",MD5.getMD5Str(confirmPwd));
		sg.globalConf.put("configAdminPwd", MD5.getMD5Str(confirmPwd));

		if(result){
			response.getWriter().print("modifPwdsuccess");
		}

		String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，修改密码：老密码：" + MD5.getMD5Str(oldpwd) + "；新密码：" + MD5.getMD5Str(confirmPwd);
        EmpExecutionContext.info(opContent);
	}

	/**
	 * 修改数据库配置文件信息
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
		public void dataSubmit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		String maxPoolSize=request.getParameter("maxPoolSize");
		String minPoolSize=request.getParameter("minPoolSize");

		String InitialPoolSize=request.getParameter("InitialPoolSize");
		boolean result=setProperty("montnets.emp.maxPoolSize",maxPoolSize);
		result=setProperty("montnets.emp.minPoolSize",minPoolSize);
		result=setProperty("montnets.emp.InitialPoolSize",InitialPoolSize);

		sg.globalConf.put("maxPoolSize", maxPoolSize);
		sg.globalConf.put("minPoolSize", minPoolSize);
		sg.globalConf.put("InitialPoolSize", InitialPoolSize);



		if(result){
			response.getWriter().print("confsucc");
		}

		//添加日志信息 pengj
		String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，修改数据库配置，修改前："
		+"数据连接池最大连接数：" + SystemGlobals.getValue("montnets.emp.maxPoolSize")
		+"，数据连接池最小连接数：" + SystemGlobals.getValue("montnets.emp.minPoolSize")
		+"，数据连接池初始连接数：" + SystemGlobals.getValue("montnets.emp.InitialPoolSize")
		+"；修改后："
		+"数据连接池最大连接数：" + sg.globalConf.get("maxPoolSize")
		+"，数据连接池最小连接数：" + sg.globalConf.get("minPoolSize")
		+"，数据连接池初始连接数：" + sg.globalConf.get("InitialPoolSize");
		EmpExecutionContext.info(opContent);
	}
		/**
		 * 修改WEB配置文件信息
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		public void webSubmit(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			String defaultPageSize=request.getParameter("defaultPageSize");
			String frame=request.getParameter("frame");
			String smsSplit=request.getParameter("smsSplit");

	        String langEnable = request.getParameter("langEnable");
	        String langs = request.getParameter("langs");
	        if(!"Yes".equals(langEnable)){
	        	langs="";
	        }
			boolean result=setProperty("emp.pageInfo.defaultPageSize",defaultPageSize)&&
					setProperty("multiLanguageEnable",langEnable)&&
					setProperty("selectedLanguage",langs)&&setProperty("emp.sms.split",smsSplit);
			result=setProperty("emp.web.frame",frame);
			sg.globalConf.put("defaultPageSize", defaultPageSize);
			sg.globalConf.put("frame", frame);
			sg.globalConf.put("emp.sms.split", smsSplit);
			sg.globalConf.put("multiLanguageEnable", langEnable);
			sg.globalConf.put("selectedLanguage", langs);

			if(result){
				response.getWriter().print("confsucc");
			}

			//添加日志信息 pengj
			String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，修改WEB信息，修改前："
			+"默认页面大小：" + SystemGlobals.getValue("emp.pageInfo.defaultPageSize")
			+"，肤色版本：" + SystemGlobals.getValue("emp.web.frame")
			+"，是否启用分批设置：" + SystemGlobals.getValue("emp.sms.split")
			+",是否开启多语言：" + SystemGlobals.getValue("multiLanguageEnable")
			+",默认语言：" + SystemGlobals.getValue("defaultLanguage")
			+",已选语言列表：" + SystemGlobals.getValue("selectedLanguage")
			+"修改后："
			+"默认页面大小：" + sg.globalConf.get("defaultPageSize")
			+"，肤色版本：" + sg.globalConf.get("frame")
			+"，是否启用分批设置：" + sg.globalConf.get("emp.sms.split")
			+",是否开启多语言：" + sg.globalConf.get("multiLanguageEnable") +
            ",默认语言：" + sg.globalConf.get("defaultLanguage") +
            ",已选语言列表：" + sg.globalConf.get("selectedLanguage");
			EmpExecutionContext.info(opContent);

		}

		/**
		 * 恢复数据库的默认信息
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		public void dataDefalut(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			boolean result=false;
			result=setProperty("montnets.emp.maxPoolSize","30");
			result=setProperty("montnets.emp.minPoolSize","15");
			result=setProperty("montnets.emp.InitialPoolSize","10");
			sg.globalConf.put("maxPoolSize", "30");
			sg.globalConf.put("minPoolSize", "15");
			sg.globalConf.put("InitialPoolSize", "10");
			if(result){
				response.getWriter().print("defalutSucc");

				//添加日志
				String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，恢复数据库的默认信息成功。";
				EmpExecutionContext.info(opContent);

			}else{
				response.getWriter().print("defalutFail");

				//添加日志
				String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，恢复数据库的默认信息失败。";
				EmpExecutionContext.info(opContent);

			}
		}
		/**
		 * 恢复页面的默认信息
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		public void webDefalut(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
		{
			boolean result=false;
			result=setProperty("emp.pageInfo.defaultPageSize","15")&&setProperty("emp.web.frame","frame3.0")
					&&setProperty("multiLanguageEnable","No")&&setProperty("defaultLanguage","zh_CN")
					&&setProperty("selectedLanguage","")&&setProperty("emp.sms.split", "0");
			sg.globalConf.put("frame", "frame3.0");
			sg.globalConf.put("defaultPageSize", "15");
			sg.globalConf.put("emp.sms.split", "0");
			sg.globalConf.put("defaultLanguage", "zh_CN");
			sg.globalConf.put("multiLanguageEnable", "No");
			sg.globalConf.put("selectedLanguage", "");
			if(result){
				response.getWriter().print("defalutSucc");

				//添加日志
				String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，恢复页面的默认信息成功。";
				EmpExecutionContext.info(opContent);

			}else{
				response.getWriter().print("defalutFail");

				//添加日志
				String opContent = "mod：EMP参数配置，客户端ip地址：" + getIpAddr(request)+"，恢复页面的默认信息失败。";
				EmpExecutionContext.info(opContent);

			}
		}
		/**
		 * 验证密码
		 *
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		public void checkPass(HttpServletRequest request, HttpServletResponse response) throws Exception
		{
			PrintWriter writer = response.getWriter();
			// 老密码
			String agoPass = request.getParameter("agoPass");

			try
			{
				// 获取当前登录用户的信息

				Object sk=sg.globalConf.get("configAdminPwd")==null?SystemGlobals.getValue("configAdminPwd"):sg.globalConf.get("configAdminPwd");



				// 检查密码
				if(MD5.getMD5Str(agoPass).equals(sk.toString()) || MD5.getMD5Str(sk.toString() + "mwadmin".toLowerCase()).equals(sk.toString()))
				{

					writer.print("true");
				}
				else
				{
					writer.print("false");
				}

			}
			catch (Exception e)
			{
				// 异常日志
				EmpExecutionContext.error(e, "svt验证密码异常。");
			}

		}


}
