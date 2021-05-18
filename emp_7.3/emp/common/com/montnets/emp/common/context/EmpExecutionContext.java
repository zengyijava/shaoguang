package com.montnets.emp.common.context;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.http.client.methods.HttpGet;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.util.Logger;
import com.montnets.emp.util.MonLogger;

public class EmpExecutionContext
{

	private DataSource					dataSource;

	private static Logger				logger			= null;
	
	private static MonLogger				monLogger			= null;

	private static Map<Long, Long>		mtmsgIds		= new HashMap<Long, Long>();

	private static Map<String, String>	groupmtmsgIds	= new HashMap<String, String>();

	private static Map<Long, String>	perfectNoticIds	= new HashMap<Long, String>();
	
	private static String newline = System.getProperties().getProperty("line.separator");

	public EmpExecutionContext()
	{

	}

	public DataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public static void info(String message)
	{
		log(StaticValue.LOGFLAG_INFO + message, Logger.LEVEL_INFO);
	}
	
	/**
	 * 操作日志
	 * @param opModule  模块名称
	 * @param corpCode   企业编码
	 * @param userId     操作员ID
	 * @param userId     操作员登录名
	 * @param opContent    日志内容
	 * @param opType       操作类型     ADD:新建;DELETE:删除;UPDATE:修改;GET:查询;OTHER:其他
	 */
	public static void info(String opModule, String corpCode, String userId, String userName, String opContent, String opType)
	{
		StringBuffer message = new StringBuffer();
		message.append(StaticValue.LOGFLAG_INFO).append("[").append(opType).append("]").append(" mod：").append(opModule).append("，corpCode：").append(corpCode).append("，opt：")
				.append(userId).append("[").append(userName).append("]，").append(opContent);
		log(message.toString(), Logger.LEVEL_INFO);
	}

	public static void warn(String message)
	{
		log(StaticValue.LOGFLAG_WARN + message, Logger.LEVEL_WARN);
	}

	public static void error(String message)
	{
		log(StaticValue.LOGFLAG_ERROR + message, Logger.LEVEL_ERROR);
	}

	public static void error(Exception e, String message)
	{
		errorLog(e, StaticValue.LOGFLAG_ERROR + message, Logger.LEVEL_ERROR);
	}

	public static void error(Exception e, String lguserid, String lfcorpcode)
	{
		String message = StaticValue.LOGFLAG_ERROR + "[操作员：" + lguserid + "，" + "企业编码：" + lfcorpcode+"]";
		errorLog(e, message, Logger.LEVEL_ERROR);
	}

	public static void error(Exception e, Long lguserid, String lfcorpcode)
	{
		String lguseridStr = "";
		if(lguserid == null)
		{
			lguseridStr = "";
		}
		else
		{
			//转为字符串类型
			lguseridStr = lguserid.toString();
		}
		error(e, lguseridStr, lfcorpcode);

	}

	public static void error(Exception e, String lguserid, String lfcorpcode, String errorMsg, String errorCode)
	{
		String message = StaticValue.LOGFLAG_ERROR + "操作员：" + lguserid + "，企业编码：" + lfcorpcode + "，错误信息：" + errorMsg + "，错误码：" + errorCode;
		errorLog(e, message, Logger.LEVEL_ERROR);
	}

	public static void debug(String message)
	{
		log(StaticValue.LOGFLAG_DEBUG + message, Logger.LEVEL_DEBUG);
	}

	public static void sql(String sql)
	{
		if(logger.getShowSql())
		{
			System.out.println(sql);
		}
	}

	/**
	 * 加入请求队列
	 * @description    
	 * @param taskid 任务ID
	 * @param message   请求信息    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-7-31 下午04:35:10
	 */
	public static void requestInfo(long taskid, String message)
	{
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
		//默认记录接收请求
		String content="reciveTime="+time+newline+"taskid="+taskid+newline+"resultCode="+newline+message+newline;
		//传入0时为发送请求
		if(taskid-0==0)
		{
			content = "sendTime="+time+newline+"requestUrl="+newline+message+newline+newline;
		}
		requestInfoLog(content);
	}
	
	/**
	 * 加入请求队列
	 * @description    
	 * @param content       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-9-1 上午09:06:11
	 */
	public static void requestInfoLog(String content)
	{
		logger.putRequestEvnet(content);
	}
	
	/**
	 * 加入APP请求队列
	 * @description    
	 * @param content       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-9-1 上午09:06:11
	 */
	public static void appRequestInfoLog(String content)
	{
		logger.putAppRequestEvnet(content + newline);
	}
	
	/**
	 * 监控告警报文队列
	 * @description    
	 * @param content       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午02:39:24
	 */
	public static void monAlarmLog(String content)
	{
		monLogger.putMonAlarmEvnet(content + newline);
	}
	
	/**
	 * 程序周期报文队列
	 * @description    
	 * @param content       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午02:40:00
	 */
	public static void proCycleLog(String content)
	{
		monLogger.putproCycleEvnet(content + newline);
	}
	/**
	 * 加入监控消息队列
	 * @description    
	 * @param content       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-3-31 下午08:58:06
	 */
	public static void monMsgLog(String content)
	{
		logger.putMonMsgEvnet(content + newline);
	}
	
	/**
	 * 普通日志加入队列
	 * @param message 日志信息
	 * @param level  日志级别
	 */
	public static void log(String message, int level)
	{
/*		if (level >= logger.getPrintFilter())
		{
			System.out.println(message);
		}*/
		if (level >= logger.getSaveFilter())
		{
			//加入线程队列
			logger.putEvent(message + newline);	
		}
	}
	
	/**
	 * 异常日志加入队列
	 * @param e  异常信息
	 * @param message  日志信息
	 */
	public static void errorLog(Exception e, String message, int level)
	{
		if (level >= logger.getSaveFilter())
		{
			Writer writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));
			//拼接异常日志信息
			String logInfo = message + newline + writer.toString();
			//加入线程队列
			logger.putEvent(logInfo);
		}
	}
	
	public static void setLogger(Logger logger)
	{
		EmpExecutionContext.logger = logger;
	}

	public static void setDataLogger(MonLogger monLogger)
	{
		EmpExecutionContext.monLogger = monLogger;
	}
	
	public static Map<Long, Long> getMtmsgIds()
	{
		return mtmsgIds;
	}

	public static Map<String, String> getGroupMtmsgIds()
	{
		return groupmtmsgIds;
	}

	public static Map<Long, String> getPerfectNoticIds()
	{
		return perfectNoticIds;
	}

	public static void addMtMsgId(Long mtmsgId, Long userId)
	{
		mtmsgIds.put(mtmsgId, userId);
	}

	public static void removeMtMsgId(Long mtmsgId)
	{
		mtmsgIds.remove(mtmsgId);
	}

	public static void addGroupMtMsgId(String taskIdAndTelNo, String msgIdAnduserId)
	{
		groupmtmsgIds.put(taskIdAndTelNo, msgIdAnduserId);
	}

	public static void removeGroupMtMsgId(String taskIdAndPhone)
	{
		groupmtmsgIds.remove(taskIdAndPhone);
	}

	public static void addPerfectNoticId(Long taskId, String pNoticAnduserId)
	{
		perfectNoticIds.put(taskId, pNoticAnduserId);
	}

	public static void removePerfectNoticId(Long taskId)
	{
		perfectNoticIds.remove(taskId);
	}

	/**
	 * 获取带参数url
	 * @param url 请求url
	 * @param params 请求参数Map
	 * @return
	 */
	private static String getUrl(String url , Map params) {
		try
		{
			if (params != null) {
				if(params.containsKey("method")){
					String action = ((String[])params.get("method"))[0];
					if(url.indexOf("?")==-1){
						url += "?";
					}else{
						url += "&";
					}
					url += "method="+action;
				}
				StringBuffer sb = new StringBuffer();
				Iterator<String> it = params.keySet().iterator();
				while (it.hasNext()) {
					String key = it.next();
					if("method".equals(key)){
						continue;
					}
					String value = ((String[]) params.get(key))[0];
					if (sb.length() < 1) {
						sb = new StringBuffer();
						if(url.indexOf("?")==-1){
							sb.append("?");
						}else{
							sb.append("&");
						}
					} else {
						sb.append("&");
					}
					sb.append(key);
					sb.append("=");
					sb.append(value);
				}
				url += sb.toString();
			}
		}
		catch (Exception e)
		{
			error(e, "记录请求日志，获取拼接带参数url时异常，url:"+url);
		}
		return url;
	}

	/**
	 * 将带参数url写入到日志文件
	 * @param request
	 * @param extInfo 扩展信息 默认为null
	 */
	public static void logRequestUrl(HttpServletRequest request,String extInfo){
		//请求类型
		String method = "";
		//带参数的url
		String uri = "";
		//请求url
		String url = "";
		try
		{
			method = request.getMethod();
			String doMethod = "";
			if(method != null && method.length()>1){
				doMethod = "do"+method.substring(0,1)+method.substring(1).toLowerCase();
			}
			uri = request.getRequestURI().replaceFirst(request.getContextPath(),"");
			if(HttpGet.METHOD_NAME.equals(method)){
				url = uri+"?"+request.getQueryString();
			}else{
				url = getUrl(uri,request.getParameterMap());
			}
			if(extInfo == null || "".equals(extInfo.trim())){
				extInfo = "";
			}else{
				extInfo += ",";
			}
			info(extInfo+doMethod + " " + url);
		}
		catch (Exception e)
		{
			error(e, "记录请求日志失败，请求url:"+uri+"，带参数的url:"+url+"，请求类型:"+method);
		}
	}
}
