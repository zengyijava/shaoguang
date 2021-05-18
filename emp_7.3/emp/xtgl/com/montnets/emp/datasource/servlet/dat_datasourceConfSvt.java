/**
 * 
 */
package com.montnets.emp.datasource.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.datasource.biz.DbconnectBiz;
import com.montnets.emp.entity.datasource.LfDBConnect;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.SuperOpLog;

@SuppressWarnings("serial")
public class dat_datasourceConfSvt extends BaseServlet
{
	//操作模块
	private final String opModule = StaticValue.PARAM_PRESERVE;
	//操作用户
	private final String opSper = StaticValue.OPSPER;

	private final DbconnectBiz dbBiz = new DbconnectBiz();

	private final String empRoot = "xtgl";
	private final String basePath = "/datasource";
	private final BaseBiz baseBiz=new BaseBiz();
	private final SuperOpLog spLog=new SuperOpLog();
	
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
		List<LfDBConnect> dbList = null;
		PageInfo pageInfo=new PageInfo();
		//日志开始时间
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		long begin_time=System.currentTimeMillis();
		//查询出所有的数据源信息
		try
		{
			//是否第一次打开
			boolean isFirstEnter = false;
			isFirstEnter=pageSet(pageInfo, request);

			if (!isFirstEnter)
			{
				request.getParameter("");
			}

			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			String corpCode = lgcorpcode;
			//分页获取所有的数据源信息
			dbList = dbBiz.getAllDbconByPage(corpCode, pageInfo);
			
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"查询数据源信息异常！");
		}
		request.setAttribute("pageInfo", pageInfo);
		request.setAttribute("dbList", dbList);
		//增加查询日志
		if(pageInfo!=null){
			long end_time=System.currentTimeMillis();
			String opContent ="查询开始时间："+format.format(begin_time)+",耗时:"+(end_time-begin_time)+"ms，数量："+pageInfo.getTotalRec();
			opSucLog(request, "数据源配置", opContent, "GET");
		}
		request.getRequestDispatcher(
				this.empRoot + this.basePath + "/dat_datasourceConf.jsp")
				.forward(request, response);
	}

	/**
	 * 新增数据源配置
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void add(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

		//数据源名称
		String dbconName = request.getParameter("dbconName");
		//描述
		String comments = request.getParameter("comments");
//		端口号
		String port = request.getParameter("port");
//		数据库类型
		String dbType = request.getParameter("dbType");
//		数据库id地址
		String dbconIP = request.getParameter("dbconIp");
//		连接类型
		String dbConnType = request.getParameter("dbConnType");
		//用户名
		String dbName = request.getParameter("dbName");
		String dbUser = request.getParameter("dbUser");
		//密码
		String dbPwd = request.getParameter("dbPwd");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		String corpCode = lgcorpcode;

		LfDBConnect dbConn = new LfDBConnect();
		try
		{
			HttpSession session = request.getSession(false);
			//操作类型
			String opType = "";
			//操作内容
			String opContent = "";
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);


			String opUser="";
			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
			opUser = sysuser==null?"":sysuser.getUserName();
			dbConn.setComments(comments);
			dbConn.setDbconIP(dbconIP);
			dbConn.setDbconName(dbconName);
			dbConn.setDbUser(dbUser);
			dbConn.setDbPwd(dbPwd);
			dbConn.setDbName(dbName);
			dbConn.setPort(port);
			dbConn.setDbType(dbType);
			dbConn.setCorpCode(corpCode);
			opType = StaticValue.ADD;
			//保存新增日志
			opContent = "新建数据库连接（数据源名称：" + dbConn.getDbconName() + "）";

			if (dbType.equals("Oracle"))
			{
				//设置数据库类型
				dbConn.setDbconnType(Integer.parseInt(dbConnType));
			}
			boolean result = false;
			result = dbBiz.addDBConnect(dbConn);

			if (result)
			{
				//保存成功
				session.setAttribute("result1", "1");
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				
			} else
			{
				session.setAttribute("result1", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
			}
			
			//增加操作日志
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				String opContent1 = "新建数据源"+(result==true?"成功":"失败")+"。[数据源名称，端口，数据库地址，服务/实例名，用户名]" +
						"("+dbconName+"，"+port+"，"+dbconIP+"，"+dbName+"，"+dbUser+")";
				EmpExecutionContext.info("数据源配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "ADD");
			}
			
			request.getRequestDispatcher(this.empRoot + this.basePath + "/dat_addDatasourceConf.jsp")
			.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"新增数据源配置异常！");
		}
	}

	/**
	 * 测试连接
	 * @param request
	 * @param response
	 */
	public void testConnection(HttpServletRequest request,
			HttpServletResponse response)
	{
		//测试连接是否正常
		LfDBConnect dbConn = new LfDBConnect();
		//数据库连接类型
		String dbConnType = request.getParameter("dbConnType");
		//数据库名称
		String dbName = request.getParameter("dbName");
		//用户名
		String dbUser = request.getParameter("dbUser");
		//密码
		String dbPwd = request.getParameter("dbPwd");
		//端口号
		String port = request.getParameter("port");
		
		//数据库类型
		String dbType = request.getParameter("dbType");
		//ip地址
		String dbconIp = request.getParameter("dbconIp");

		dbConn.setDbUser(dbUser);
		dbConn.setDbPwd(dbPwd);
		dbConn.setDbName(dbName);
		dbConn.setPort(port);
		dbConn.setDbType(dbType);
		dbConn.setDbconIP(dbconIp);
		if (dbType.equals("Oracle"))
		{
			dbConn.setDbconnType(Integer.parseInt(dbConnType));
		}
		try
		{
			response.getWriter().print(dbBiz.testConnection(dbConn));
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"测试连接异常！");
		}
	}

	/**
	 * 验证连接名称是否重复
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkDbName(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException
	{
		//数据库名称
		String conName = request.getParameter("conName").trim();
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		try
		{
			//存在表示符
			boolean exists = false;
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("dbconName", conName);
			conditionMap.put("corpCode", lgcorpcode);
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);
			//验证名称重复
			if (dbList != null && dbList.size() > 0)
			{
				exists = true;
			}
			//异步返回结果
			response.getWriter().print(exists);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"验证连接名称是否重复异常！");
		}
	}

	/**
	 * 跳转到编辑页面
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void toEdit(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//编辑数据源信息
		String dbIdtemp = request.getParameter("dbId");
		Long dbId = null;
		LfDBConnect dbConn = new LfDBConnect();

		try
		{
			if (null != dbIdtemp && 0 != dbIdtemp.length())
			{
				dbId = Long.valueOf(dbIdtemp);
			}
			if (null != dbId)
			{
				//根据连接名称获取数据库连接信息
				dbConn = dbBiz.getDBConnectById(dbId);
			}

			request.setAttribute("dbConn", dbConn);
			request.getRequestDispatcher(
					this.empRoot + this.basePath + "/dat_editDatasourceConf.jsp")
					.forward(request, response);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"跳转到编辑连接页面查询异常！");
		}
	}

	/**
	 * 更新数据源配置
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void update(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//操作类型
		String opType = "";
		//操作内容
		String opContent = "";
		//操作日志：更新
		opType = StaticValue.UPDATE;
		//数据库名称
		String dbName = request.getParameter("dbName");
		//连接类型
		String dbConnType = request.getParameter("dbConnType");
		//连接名
		String dbconName = request.getParameter("dbconName");
		//描述
		String comment = request.getParameter("comments");
		//用户名
		String dbUser = request.getParameter("dbUser");
		//密码
		String dbPwd = request.getParameter("dbPwd");
		//端口号
		String port = request.getParameter("port");
		//数据库类型
		String dbType = request.getParameter("dbType");
		//ip地址
		String dbconIp = request.getParameter("dbconIp");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		LfDBConnect dbConn = new LfDBConnect();

		String opUser="";
		try
		{
			///String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			LfSysuser sysuser = baseBiz.getById(LfSysuser.class, Long.parseLong(lguserid));
			opUser = sysuser==null?"":sysuser.getUserName();
			
			//查询操作之前记录
			LfDBConnect befchgEntity = baseBiz.getById(LfDBConnect.class, Long.valueOf(request.getParameter("dbId")));
			String befchgCont = befchgEntity.getDbconName()+"，"+befchgEntity.getPort()+"，"+befchgEntity.getDbconIP()
								+"，"+befchgEntity.getDbName()+"，"+befchgEntity.getDbUser();
			
			dbConn.setDbName(dbName);
			dbConn.setDbUser(dbUser);
			dbConn.setDbPwd(dbPwd);
			dbConn.setDbconName(dbconName);
			dbConn.setPort(port);
			dbConn.setDbType(dbType);
			dbConn.setComments(comment);
			dbConn.setDbconIP(dbconIp);
			if (dbType.equals("Oracle"))
			{
				dbConn.setDbconnType(Integer.parseInt(dbConnType));
			}

			dbConn.setDbId(Long.valueOf(request.getParameter("dbId")));
			opContent = "编辑数据库连接（数据源名称：" + dbConn.getDbconName() + "）";

			HttpSession session = request.getSession(false);
			boolean result = false;
			//更新操作
			result = dbBiz.update(dbConn);
			request.setAttribute("dbConn", dbConn);
			if (result)
			{
				session.setAttribute("result1", "2");
				//保存成功日志信息
				spLog.logSuccessString(opUser, opModule, opType, opContent,
						lgcorpcode);
				
			} else
			{
				session.setAttribute("result1", "0");
				spLog.logFailureString(opUser, opModule, opType, opContent
						+ opSper, null, lgcorpcode);
			}
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "修改数据源"+(result==true?"成功":"失败")+"。[数据源名称，端口，数据库地址，服务/实例名，用户名]" +
						"("+befchgCont+")->("+dbconName+"，"+port+"，"+dbconIp+"，"+dbName+"，"+dbUser+")";
				EmpExecutionContext.info("数据源配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "UPDATE");
			}
			
			request.getRequestDispatcher(this.empRoot + this.basePath + "/dat_editDatasourceConf.jsp")
					.forward(request, response);
		} catch (Exception e)
		{
			spLog.logFailureString(opUser, opModule, opType, opContent, e,
					lgcorpcode);
			EmpExecutionContext.error(e,"更新数据源配置异常！");
		}

	}

	/**
	 * 删除数据库连接
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void delete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		String opUser="";
		//操作类型
		String opType = "";
		//操作内容
		String opContent = "";
		//服务日志操作类型：删除
		opType = StaticValue.DELETE;
		//数据库名称
		String dbconName2 = request.getParameter("dbconName");
		//当前登录企业
		String lgcorpcode = request.getParameter("lgcorpcode");
		
		opContent = "删除数据库连接（数据源名称：" + dbconName2 + "）";
		String dbId = request.getParameter("dbId");
		String dbIds[] = dbId.split(",");
		int delNum = -1;
		try
		{
			
			//查询操作之前记录
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("dbId&in", dbId);
			List<LfDBConnect> dbconnList = baseBiz.getByCondition(LfDBConnect.class, conditionMap, null);
			String befchgCont= "";
			if(dbconnList.size()>0){
				for(int i=0; i<dbconnList.size(); i++){
					befchgCont += "("+dbconnList.get(i).getDbconName()+"，"+dbconnList.get(i).getPort()+"，"
					+dbconnList.get(i).getDbId()+"，"+dbconnList.get(i).getDbName()+"，"+dbconnList.get(i).getDbUser()+")";
				}
			}
			
			//删除
			delNum = dbBiz.delSelectedDbConnect(dbIds);
			if (delNum >= 1)
			{
				//异步返回结果：成功
				response.getWriter().print("true");

			} else
			{
				response.getWriter().print("false");
			}
			
			//增加操作日志
			Object loginSysuserObj = request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser =(LfSysuser)loginSysuserObj;
				String opContent1 = "删除数据源"+(delNum>=1?"成功":"失败")+"。[数据源名称，端口，数据库地址，服务/实例名，用户名]" +
						"("+befchgCont+")";
				EmpExecutionContext.info("数据源配置", loginSysuser.getCorpCode().toString(), loginSysuser.getUserId().toString(), 
						loginSysuser.getUserName(), opContent1, "DETELE");
			}

		} catch (Exception e)
		{

			EmpExecutionContext.error(e,"删除数据库连接异常！");
			spLog.logFailureString(opUser, opModule, opType, opContent, e,
					lgcorpcode);
			response.getWriter().print("false");

		}
		//记录操作成功日志
		spLog.logSuccessString(opUser, opModule, opType, opContent,
				lgcorpcode);
	}

	public void toAdd(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		//跳转到新增数据源页面
		request.getRequestDispatcher(this.empRoot + this.basePath + "/dat_addDatasourceConf.jsp").forward(request, response);
	}

	/**
	 * 加载数据源配置信息列表
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getDBAsOption(HttpServletRequest request,
			HttpServletResponse response) throws IOException
	{
		//此方法的目的是取出所有可用数据源，生成select标签的html代码，供用户选择使用
		try
		{
			//当前登录企业
			String lgcorpcode = request.getParameter("lgcorpcode");
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", lgcorpcode);
			//查询本企业的数据源
			List<LfDBConnect> dbList = baseBiz.getByCondition(
					LfDBConnect.class, conditionMap, null);

			// List<LfDBConnect> dbList =
			// baseBiz.getEntityList(LfDBConnect.class);

			//生成html代码
			response.getWriter().print("<option value=''></option>");
			if (dbList != null)
			{
				for (LfDBConnect db : dbList)
				{
					response.getWriter().print("<option value='" + db.getDbId() + "'>"
							+ db.getDbconName() + "</option>");
				}
			} else
			{
				//异步打印到页面
				response.getWriter().print("");
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"加载数据源配置信息列表异常！");
			response.getWriter().print("");
		}
	}
	
	/**
	 * @description  记录操作成功日志  
	 * @param request
	 * @param modName 模块名称
	 * @param opContent 操作详情    	
	 * @param opType 操作类型 ADD UPDATE DELETE GET OTHER		 
	 * @author zousy <zousy999@qq.com>
	 * @datetime 2015-3-3 上午11:29:50
	 */
	public void opSucLog(HttpServletRequest request,String modName,String opContent,String opType){
		LfSysuser lfSysuser = null;
		try {
			Object obj = request.getSession(false).getAttribute("loginSysuser");
			if(obj == null) return;
			lfSysuser = (LfSysuser)obj;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "记录操作日志异常,session为空！");
		}
		if(lfSysuser!=null){
			EmpExecutionContext.info(modName,lfSysuser.getCorpCode(),String.valueOf(lfSysuser.getUserId()),lfSysuser.getUserName(),opContent,opType);
		}else{
			EmpExecutionContext.info(modName,"","","",opContent,opType);
		}
	}
}
