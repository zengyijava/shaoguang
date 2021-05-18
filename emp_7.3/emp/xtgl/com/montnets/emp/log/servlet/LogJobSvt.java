package com.montnets.emp.log.servlet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.log.biz.LogJobBiz;

public class LogJobSvt extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2419693148638346958L;
	
	private BaseBiz baseBiz = new BaseBiz();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static String delFilethreadStartDate;

	public void init() throws ServletException {
		delFilethreadStartDate = sdf.format(new Date());
		Timer timer = new Timer();
		//获取当前系统时间
		Calendar cal = Calendar.getInstance();
		Date date = new Date(cal.get(Calendar.YEAR) - 1900, cal
				.get(Calendar.MONTH), cal.get(Calendar.DATE), 22,
				10, 0);
		timer.schedule(new TimerTask() {
			
			public void run() {
				try {
					//获取日志保存时间，单位：月
					LfGlobalVariable corpConf = baseBiz.getById(LfGlobalVariable.class, "41");
					
					//日志保存时间长度，单位：月
					int monSize = Integer.valueOf(corpConf.getGlobalValue().toString());
					
					//获取服务器当前系统时间
					Calendar cal = Calendar.getInstance();
					//年
					String year = new SimpleDateFormat("yyyy").format(cal.getTime());
					//月
					String month = new SimpleDateFormat("MM").format(cal.getTime());
					//天
					//String day = new SimpleDateFormat("dd").format(cal.getTime());				
					boolean flag = new LogJobBiz().delLogFile(monSize, year, month);
					
					//写日志
					if (flag) {
						EmpExecutionContext.info(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(cal.getTime()) + "删除过时日志成功！程序启动时间："+delFilethreadStartDate);
					} else {
						EmpExecutionContext.error(new SimpleDateFormat("yyyyMMdd HH:mm:ss").format(cal.getTime()) + "删除过时日志失败！程序启动时间："+delFilethreadStartDate);
					}
					
				} catch (Exception e) {
					EmpExecutionContext.error(e, "定时扫描日志文件异常！程序启动时间："+delFilethreadStartDate);
				}
			}
		}, date, 86400000L);
		
		
	}
	
}
