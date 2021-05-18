package com.montnets.emp.rms.templmanage.listener;

import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.servlet.http.HttpServlet;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.degree.listener.DegreeStatusCheckThread;
import com.montnets.emp.util.StringUtils;

/** 
* @ClassName: TmplateTaskListner 
* @Description: 同步模板审核状态监听器
* @author xuty  
* @date 2018-1-12 上午10:01:05 
*  
*/
public class TmplateTaskListner extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7408293451747449848L;
	static ResourceBundle RB = ResourceBundle.getBundle("SystemGlobals");
	private static  long oneDay = 24 * 60 * 60 * 1000L; 
	int isruntimejob =1;//=1开启定时任务
	public final ScheduledExecutorService scheduleExuc = Executors
			.newScheduledThreadPool(10);
	
	public void init() {
		EmpExecutionContext.info("开启模板状态读取线程...");
		EmpExecutionContext.info("开启档位状态检查线程...");
		if(null != RB.getString("cluster.isruntimejob") && !"".equals(RB.getString("cluster.isruntimejob"))){
			isruntimejob = Integer.parseInt(RB.getString("cluster.isruntimejob"));
		}
		
		if(isruntimejob ==1){
			int internal = StringUtils.IsNullOrEmpty(RB.getString("emp.rms.templateInternal"))?5*60*1000:Integer.parseInt(RB.getString("emp.rms.templateInternal"))*1000;
			new TemplateCheckThread(internal).start();
			EmpExecutionContext.info("开启模板状态读取线程完成...");
			
			//开启档位状态检查线程
			new DegreeStatusCheckThread().start();
			EmpExecutionContext.info("开启档位状态检查线程完成...");
			
			//开启模板下载线程
			new TemplateDownLoadThread(internal).start();
			EmpExecutionContext.info("开启模板文件下载线程完成...");
		}
		
	}
	
	/** 
	 * 获取指定时间对应的毫秒数 
	 * @param time "HH:mm:ss" 
	 * @return 
	 */  
 

}
