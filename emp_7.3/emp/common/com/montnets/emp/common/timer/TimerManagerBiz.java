package com.montnets.emp.common.timer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.timer.bean.TimeSerProperty;
import com.montnets.emp.common.timer.biz.TimeSerBiz;
import com.montnets.emp.common.timer.dao.GenericLfTimerVoDAO;
import com.montnets.emp.common.timer.dao.TimeSerDAO;
import com.montnets.emp.common.timer.entity.LfTimeSerState;
import com.montnets.emp.common.timer.thread.TimeSerThread;
import com.montnets.emp.common.timer.thread.TimerHeartbeatThread;
import com.montnets.emp.entity.system.LfTimer;

/**
 * 
 * @description 定时器业务逻辑类
 * 
 */
public class TimerManagerBiz
{
	private TimerManagerBiz(){}
	
	//定时器实例
	private static TimerManagerBiz tm_instance = new TimerManagerBiz();

	//任务执行线程池对象
	private ExecutorService exec = null;
	//心跳线程
	private TimerHeartbeatThread heartbeatThread = null;
	//定时服务线程
	private TimeSerThread timeSerThread = null;
	
	/**
	 * 是否繁忙，true：繁忙；false：空闲
	 */
	private static boolean runBusy = false;
	
	//空运行次数
	private static int runcount = 0;
	
	//任务运行状态，是否在运行中，key为任务id，value为状态，1为空闲，2为繁忙
	private static ConcurrentHashMap<Long,Integer> taskRunStateMap = new ConcurrentHashMap<Long,Integer>();
	
	//定时服务属性
	private TimeSerProperty property = new TimeSerProperty();
	
	private TimeSerBiz timeSerBiz = new TimeSerBiz();
	private GenericLfTimerVoDAO timerDAO = new GenericLfTimerVoDAO();
	private TimeSerDAO timeSerDAO = new TimeSerDAO();
	
	
	/**
	 * 获取实例
	 * @return
	 */
	public static TimerManagerBiz getTMInstance()
	{
		//返回实例
		return tm_instance;
	}
	
	/**
	 * 启动定时器
	 */
	public void StartTimer()
	{
		//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1)
		{
			this.initTimer();
		}
	}
	
	/**
	 * 停止定时器
	 */
	public void StopTimer()
	{
		//是否运行定时任务，1代表运行，0代表不运行
		if(StaticValue.ISRUNTIMEJOB==1)
		{
			this.stopTimer();
		}
	}
	
	/**
	 * 设置任务运行状态为繁忙
	 * @param timerTaskId 任务id
	 * @return 成功返回true
	 */
	public static boolean setTaskRunStateBusy(Long timerTaskId)
	{
		if(timerTaskId == null || timerTaskId == 0)
		{
			EmpExecutionContext.error("设置任务运行状态为繁忙失败，任务Id为空。");
			return false;
		}
		try
		{
			taskRunStateMap.put(timerTaskId, 2);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置任务运行状态为繁忙异常。");
			return false;
		}
	}
	
	/**
	 * 设置任务运行状态为空闲
	 * @param timerTaskId 任务id
	 * @return 成功返回true
	 */
	public static boolean setTaskRunStateFree(Long timerTaskId)
	{
		if(timerTaskId == null || timerTaskId == 0){
			EmpExecutionContext.error("设置任务运行状态为空闲失败，任务Id为空。");
			return false;
		}
		try
		{
			//taskRunStateMap.put(timerTaskId, 1);
			taskRunStateMap.remove(timerTaskId);
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置任务运行状态为空闲异常。");
			return false;
		}
	}
	
	/**
	 * 判断任务是否空闲
	 * @return 空闲则返回true
	 */
	private boolean isTaskFree(Long timerTaskId)
	{
		if(timerTaskId == null || timerTaskId == 0)
		{
			EmpExecutionContext.error("判断任务是否空闲失败，任务Id为空。");
			return false;
		}
		try
		{
			//1为空闲，2为繁忙
			Integer state = taskRunStateMap.get(timerTaskId);
			//为null，任务还没执行过，为空闲
			if(state == null){
				return true;
			}
			//空闲
			else if(state == 1){
				return true;
			}
			else{
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "判断任务是否空闲异常。");
			return false;
		}
	}

	/**
	 * 初始化定时器
	 * @return 成功返回true
	 */
	private void initTimer()
	{
		//初始化定时服务属性
		boolean resProperty = this.initProperty();
		if(!resProperty)
		{
			EmpExecutionContext.error("初始化定时服务，初始化定时服务属性失败。");
			return;
		}
		EmpExecutionContext.info("初始化定时服务，初始化属性成功。TimeServerID="+property.getTimeServerID()+",NodeId="+property.getNodeId());
		
		// 服务器启动时，初始化参数
		boolean resParam = this.initParam();
		if(!resParam)
		{
			EmpExecutionContext.error("初始化定时服务，初始化定时服务参数失败。");
			return;
		}
		EmpExecutionContext.info("初始化定时服务，初始化参数成功。TimeServerID="+property.getTimeServerID()+",NodeId="+property.getNodeId());
		
		heartbeatThread = new TimerHeartbeatThread();
		boolean resHearThread = heartbeatThread.StartThread();
		if(!resHearThread)
		{
			EmpExecutionContext.error("初始化定时服务，启动心跳线程失败。");
			return;
		}
		EmpExecutionContext.info("初始化定时服务，启动心跳线程成功。TimeServerID="+property.getTimeServerID()+",NodeId="+property.getNodeId());
		
		timeSerThread = new TimeSerThread();
		boolean resSerThread = timeSerThread.StartThread();
		if(!resSerThread)
		{
			EmpExecutionContext.error("初始化定时服务，启动定时服务线程失败。");
			heartbeatThread.StopThread();
			return;
		}
		EmpExecutionContext.info("初始化定时服务，启动定时服务处理线程成功。TimeServerID="+property.getTimeServerID()+",NodeId="+property.getNodeId());
	}
	
	/**
	 * 关闭定时器
	 */
	private void stopTimer()
	{
		try
		{
			if(exec != null){
				
				//关闭线程池，并终止任务
				exec.shutdownNow();
				exec = null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关闭定时器，关闭任务线程池异常。");
		}
		/*if(future != null){
			//终止定时框架任务
			future.cancel(true);
			future = null;
		}*/
		/*try
		{
			if(timer != null){
				//关闭定时线程
				timer.shutdownNow();
				timer = null;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "关闭定时器，关闭定时任务对象异常。");
		}*/
		if(this.heartbeatThread != null)
		{
			//关闭心跳
			this.heartbeatThread.StopThread();
		}
		if(this.timeSerThread != null)
		{
			//关闭服务线程
			this.timeSerThread.StopThread();
		}
		
		//实例对象设置为null
		tm_instance = null;
	}
	
	/**
	 * 启动定时器，如果没启动
	 */
	public void StartTimerIfStop()
	{
		//如果基本对象null，则启动定时框架
		if(heartbeatThread == null || timeSerThread == null || exec == null)
		{
			this.StartTimer();
		}
	}
	
	/**
	 * 初始化参数
	 * @return 成功返回true
	 */
	private boolean initParam()
	{
		try
		{
			//创建定时器
			//timer = Executors.newScheduledThreadPool(1);
			//创建执行任务线程池
			exec = new ThreadPoolExecutor(TimerStaticValue.TASK_THREAD_COUNT, TimerStaticValue.TASK_THREAD_MAXCOUNT, TimerStaticValue.TASK_THREAD_KEEP_ALIVE_TIME, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时器初始化参数异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 初始化定时服务属性对象
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-24 下午02:27:50
	 */
	private boolean initProperty()
	{
		//生成TimeServerID
		String timeServerID = timeSerBiz.GenerateTimeServerID();
		property.setTimeServerID(timeServerID);
		
		property.setTimerActiveTime(System.currentTimeMillis());
		
		//节点id
		property.setNodeId(StaticValue.getServerNumber());

		String serverIP = timeSerBiz.GetServerIP();
		//有ip
		if(serverIP != null && serverIP.length() > 0)
		{
			property.setServerIp(serverIP);
		}
		else
		{
			property.setServerIp(" ");
		}
		property.setServerPort(0);
		
		property.setDealState(0);
		
		String serverURL = SystemGlobals.getValue("montnets.thisUrl");
		property.setServerURL(serverURL);
		
		boolean initRes = timeSerBiz.SaveTimerSerState(property);
		
		//删除未处理且过期超过1天的记录
		timeSerDAO.DelTimeSerState();
		
		return initRes;
	}
	
	/**
	 * 获取定时任务
	 * 
	 * @return
	 */
	public boolean continuteTimerTask()
	{
		if(runBusy == true){
			EmpExecutionContext.info("定时任务处理繁忙中，下次处理。");
			return true;
		}
		runBusy = true;
		try
		{
			//更新活动时间
			//activeTime();
			//更新数据库活动时间
			//timeSerBiz.updateSerStateActive(property.getTimeServerID());
			//更新活动时间
			//activeTime();
			
			//检查是否可处理
			boolean dealRes = checkTimeSerRun(true);
			if(!dealRes)
			{
				return false;
			}
			
			//更新活动时间
			activeTime();
			//获取状态为运行的定时任务
			List<LfTimer> tmTasksList = timerDAO.findTimerTask();
			
			//更新活动时间
			activeTime();

			//任务为空异常
			if (tmTasksList == null)
			{
				EmpExecutionContext.error("获取运行中定时任务失败！");
				runBusy = false;
				return false;
			}

			//无记录
			if (tmTasksList.size() == 0)
			{
				//空运行次数加1
				runcount++;
				if(runcount % 20 == 0)
				{
					EmpExecutionContext.info("获取运行中定时任务成功，但没有记录！已空运行"+runcount);
				}
				
				runBusy = false;
				return true;
			}
			else
			{
				//有记录，空运行清0
				runcount = 0;
			}
			EmpExecutionContext.info("获取运行中定时任务成功，共："+tmTasksList.size()+"条记录。");
			//错误编码对象
			TimerErrorCode errorCode = null;
			dealRes = false;
			//循环处理任务
			for (int i = 0; i < tmTasksList.size(); i++)
			{
				//检查是否可处理
				dealRes = checkTimeSerRun(false);
				if(!dealRes)
				{
					EmpExecutionContext.error("获取运行中定时任务，处理任务前，检查为不可处理。");
					break;
				}
				
				//初始化错误编码对象
				errorCode = new TimerErrorCode();
				// 处理任务
				execute(tmTasksList.get(i), errorCode);
				//等待
				Thread.sleep(TimerStaticValue.WAIT_TIME);
				
				//更新活动时间
				activeTime();
			}
			
			//清空定时任务集合
			tmTasksList.clear();
			tmTasksList = null;
			
			runBusy = false;
			
			//结果为成功
			return true;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "定时框架获取定时任务异常。");
			runBusy = false;
			return false;
		}
		finally
		{
			runBusy = false;
		}
		
	}
	
	/**
	 * 
	 * @description 检查定时服务是否可处理
	 * @return true则可处理，false为不可处理
	 */
	public boolean checkTimeSerRun()
	{
		return this.checkTimeSerRun(false);
	}
	
	/**
	 * 
	 * @description 检查定时服务是否可处理
	 * @param updateActiveTime 是否更新定时服务状态表，true为需要更新
	 * @return true则可处理，false为不可处理
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-3-25 上午11:09:25
	 */
	private boolean checkTimeSerRun(boolean updateActiveTime)
	{
		try
		{
			//获取数据库定时服务的状态记录
			LfTimeSerState serState = timeSerBiz.getTimeSerState(property.getTimeServerID());
			if(serState == null)
			{
				EmpExecutionContext.error("定时服务处理，获取不到服务状态记录。TimeServerID="+property.getTimeServerID());
				return false;
			}
			
			//更新活动时间
			activeTime();
			
			//控制权是本机，且本机服务状态为处理中，且未被接管
			if(property.getTimeServerID().equals(serState.getCtrlTimeSerID()) && serState.getDealState() == 1 && !property.getIsOtherCtrl())
			{
				//需要更新定时服务活动时间
				if(updateActiveTime)
				{
					//更新数据库活动时间
					timeSerBiz.updateSerStateActive(property.getTimeServerID());
					//更新活动时间
					activeTime();
				}
				return true;
			}
			
			//控制权不在本机，而且本机服务线程未处理，表示有其他定时服务在处理
			if(!property.getTimeServerID().equals(serState.getCtrlTimeSerID()) && serState.getDealState() == 0)
			{
				//更新数据库活动时间
				boolean updateActive = timeSerBiz.updateSerStateActive(property.getTimeServerID());
				//更新活动时间，更新成功才更新活跃时间
				activeTime(updateActive);
				return false;
			}
			//控制权不在本机，但本机服务线程为处理中。把处理交给其他服务
			if(!property.getTimeServerID().equals(serState.getCtrlTimeSerID()) && serState.getDealState() == 1)
			{
				//更新本机服务线程状态为未处理
				boolean updateStateRes = timeSerBiz.updateSerStateStop(property.getTimeServerID());
				//更新活动时间，更新成功才更新活跃时间
				activeTime(updateStateRes);
				EmpExecutionContext.info("获取运行中定时任务，更新本机服务线程状态为未处理，结果："+updateStateRes+"。TimeServerID="+property.getTimeServerID());
				return false;
			}
			
			//判断处理权是否以被其他定时服务接管
			if(property.getIsOtherCtrl())
			{
				property.setIsOtherCtrl(false);
				EmpExecutionContext.info("获取运行中定时任务，本机定时服务处理权已被接管。TimeServerID="+property.getTimeServerID());
				return false;
			}
			
			//控制权在本机，但本机服务线程为未处理
			if(property.getTimeServerID().equals(serState.getCtrlTimeSerID()) && serState.getDealState() == 0)
			{
				EmpExecutionContext.info("获取运行中定时任务，控制权在本机，但本机服务线程为未处理。TimeServerID="+property.getTimeServerID());
				setDealState();
				return false;
			}
			
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取运行中定时任务，检查是否可运行，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 定时服务状态处理
	 * @return 成功返回true
	 */
	public boolean setDealState()
	{
		List<LfTimeSerState> serStateList;
		try
		{
			//获取处理中的定时服务
			serStateList = timeSerDAO.getSerStateRun();
			//没找到处理中的定时服务
			if(serStateList == null || serStateList.size() < 1)
			{
				//更新为本机定时服务
				boolean updateRunRes = timeSerDAO.UpdateTimeSerStateRun(property.getTimeServerID());
				//更新失败则直接返回，不更新活动时间
				if(!updateRunRes)
				{
					return false;
				}
				//更新活动时间
				activeTime();
				return true;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时服务处理，获取所有处理中定时服务，异常。");
			return false;
		}
		
		//有处理中的定时服务
		
		try
		{
			//活动时间差
			long serActiveTime;
			boolean updateRes = false;
			for(LfTimeSerState serState : serStateList)
			{
				//计算活动时间差
				serActiveTime = serState.getDbCurrentTime().getTime() - serState.getUpdateTime().getTime();
				//未超时
				if(serActiveTime >= 0 && serActiveTime < TimerStaticValue.TIME_SER_ACTIVE_TIME)
				{
					EmpExecutionContext.info("定时服务处理，本机定时服务有控制权，但别的定时服务仍没超时。" 
							+ "本机TimeServerID=" + property.getTimeServerID()
							+ ",其他定时服务：" 
							+ "TimeServerID=" + serState.getTimeServerID()
							+ ",UpdateTime=" + serState.getUpdateTime()
							+ ",DbCurrentTime=" + serState.getDbCurrentTime()
							+ ",ServerURL=" + serState.getServerURL()
							);
					continue;
				}
				
				//已超时
				
				//其他定时服务URL不为空，且URL不是本机定时服务的URL
				if(serState.getServerURL() != null && serState.getServerURL().trim().length() > 0 && !serState.getServerURL().equals(property.getServerURL()))
				{
					//发送接管信息
					timeSerBiz.sendHttpReq(property.getTimeServerID(), serState.getTimeServerID(), serState.getServerURL());
				}
				
				//更新活动时间
				activeTime();
				//更新其他定时服务为未处理状态
				updateRes = timeSerDAO.UpdateTimeSerStateStop(serState.getTimeServerID());
				//更新成功
				if(updateRes)
				{
					//更新活动时间
					activeTime();
				}
				else
				{
					EmpExecutionContext.info("定时服务处理，更新其他定时服务为未处理状态，更新不成功。" 
							+ "本机TimeServerID=" + property.getTimeServerID()
							+ ",其他定时服务：" 
							+ "TimeServerID=" + serState.getTimeServerID()
							+ ",UpdateTime=" + serState.getUpdateTime()
							+ ",DbCurrentTime=" + serState.getDbCurrentTime()
							+ ",ServerURL=" + serState.getServerURL()
							);
				}
			}
			
			//未更新成功
			if(!updateRes)
			{
				return false;
			}
			
			//更新为本机定时服务
			boolean updateRunRes = timeSerDAO.UpdateTimeSerStateRun(property.getTimeServerID());
			//更新成功
			if(updateRunRes)
			{
				//更新活动时间
				activeTime();
			}
			else
			{
				EmpExecutionContext.error("定时服务处理，更新本机定时服务为处理中，更新不成功。" 
						+ "本机TimeServerID=" + property.getTimeServerID()
						);
			}
			
			return updateRunRes;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时服务状态处理，异常。");
			return false;
		}
	}

	/**
	 * 执行定时任务
	 * 
	 * @param lfTimer
	 *            定时任务对象 void
	 */
	private boolean execute(LfTimer lfTimer, TimerErrorCode errorCode)
	{
		try
		{
			// 判断执行时间是否大于当前系统时间，true还未到执行时间
			if (lfTimer.getNextTime().getTime() > System.currentTimeMillis())
			{
				EmpExecutionContext.info("定时任务没到执行时间，无需执行。任务id："+lfTimer.getNextTime());
				//返回结果
				return false;
			}
			// 任务执行时间+自定义时间差+系统时间差=任务执行时间范围
			Long nextTime = lfTimer.getNextTime().getTime()
					+ lfTimer.getCustomInterval()
					+ TimerStaticValue.SYSTEMINTERVAL;
			
			// 不在任务执行时间范围内，即已过期
			if (nextTime < System.currentTimeMillis())
			{
				//判断任务是否空闲
				boolean freeRes = isTaskFree(lfTimer.getTimerTaskId());
				//空闲，则加入执行线程
				if(freeRes){
					EmpExecutionContext.info("定时过期任务加入处理线程。任务id："+lfTimer.getTimerTaskId());
					//使用线程执行任务
					exec.execute(new ExeTimeOutTimerTask(lfTimer));
					//返回结果
					return true;
				}
				else{
					EmpExecutionContext.info("定时任务执行中，不再加入过期处理线程。任务id："+lfTimer.getTimerTaskId());
					return false;
				}
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时器执行任务，判断任务是否到期或过期异常。任务id："+lfTimer.getTimerTaskId());
			return false;
		}
		
		try
		{
			//判断任务是否空闲
			boolean freeRes = isTaskFree(lfTimer.getTimerTaskId());
			//空闲，则加入执行线程
			if(freeRes){
				EmpExecutionContext.info("定时任务加入处理线程。任务id："+lfTimer.getTimerTaskId());
				//使用线程执行任务
				exec.execute(new ExeTimerTask(lfTimer));
				return true;
			}
			else{
				EmpExecutionContext.info("定时任务执行中，不再加入处理线程。任务id："+lfTimer.getTimerTaskId());
				return false;
			}
			
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时器执行任务，调用线程执行任务异常。任务id："+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
	/**
	 * 更新活动时间
	 */
	private void activeTime()
	{
		activeTime(true);
	}
	
	/**
	 * 
	 * @description 更新活动时间
	 * @param isUpdate true为需要更新活动时间，false为不更新活动时间
	 */
	private void activeTime(boolean isUpdate)
	{
		if(!isUpdate)
		{
			return;
		}
		property.setTimerActiveTime(System.currentTimeMillis());
	}

	/**
	 * 
	 * @description 获取定时服务属性对象
	 * @return 定时服务属性对象
	 */
	public TimeSerProperty getProperty()
	{
		return property;
	}
	
}
