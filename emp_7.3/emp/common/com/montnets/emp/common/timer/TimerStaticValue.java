/**
 * 
 */
package com.montnets.emp.common.timer;

/**
 * 定时器静态变量
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-10-14 下午07:15:58
 * @description
 */
public class TimerStaticValue {
	
	//重启时间间隔60s
	public static final Long REBOOTINTERVAL = 60*1000L;
	
	//最大重启次数
	public static final int MAXBOOTNUM = 300;
	
	/**
	 * 从数据库获取定时任务时间间隔
	 */
	public static final Long EXECUTEINTERVAL = 30*1000L;
	
	//系统时间差5分钟
	public static final Long SYSTEMINTERVAL = 5*60*1000L;
	
	/**
	 * 定时器启动延迟30s
	 */
	public static final Long LOADDELAY =30*1000L;
	
	//执行任务线程池初始线程数
	public static final int TASK_THREAD_COUNT = 2;
	
	//执行任务线程池最大线程数
	public static final int TASK_THREAD_MAXCOUNT = 100;
	
	//执行任务线程池线程空闲存活时间，单位秒
	public static final long TASK_THREAD_KEEP_ALIVE_TIME = 60l;
	
	//4-过期未执行
	public static final int RESULT_TIMEOUT =4;
	
	//定时任务处理等待时间(毫秒)
	public static final long WAIT_TIME =500;
	
	/**
	 * 心跳等待时间，10s
	 */
	public static final long HEART_BEAT_WAIT_TIME = 10 * 1000l;
	
	/**
	 * 心跳活跃时间，30s
	 */
	public static final long HEART_BEAT_ACTIVE_TIME = 30 * 1000l;
	
	/**
	 * 服务主线程活跃时间，60s
	 */
	public static final long TIME_SER_ACTIVE_TIME = 60 * 1000l;
	
	/**
	 * 定时服务接收消息servlet接口
	 */
	public static final String TIME_SER_REC_SVT = "timerRece.hts";
	
	/**
	 * http请求接管响应
	 */
	public static final String TIME_SER_RECE = "reced";
	
	/**
	 * http请求接管响应返回为异常。
	 */
	public static final String TIME_SER_RESP_ERR = "err";
	
	/**
	 * http请求接管成功
	 */
	public static final String TIME_SER_DEAL_STATE_TRUE = "true";
	
	/**
	 * 定时服务超时删除天数
	 */
	public static final int TIME_SER_DEL_TIME = 1;

}
