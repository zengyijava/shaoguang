package com.montnets.emp.engine.biz;

import java.util.List;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.common.timer.TaskManagerBiz;

public class BaseAppTimerBiz
{
	//定时器管理biz
	private TaskManagerBiz tmBiz = new TaskManagerBiz();
	
/**
 * 
 * @param serId
 * @return
 * @throws Exception
 */
	public LfTimer getTaskByExpression(String serId) throws Exception {
		//定时任务对象
		LfTimer task = null;
		//定时任务对象集合
		List<LfTimer> tasksList = tmBiz.getTaskByExpression(serId);
		//有记录
		if(tasksList != null && tasksList.size() == 1){
			//取第一条
			task = tasksList.get(0);
		}
		//返回对象
		return task;
	}
	
	/**
	 * 
	 * @param timerTaskId
	 * @return
	 * @throws Exception
	 */
	public boolean stopTaskByTaskId(Long timerTaskId) throws Exception {
		//任务id不能为空
		if(timerTaskId == null){
			return false;
		}
		//停止任务并返回结果
		boolean result = tmBiz.stopTask(timerTaskId);
		//返回结果
		return result;
	}
	
	/**
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public boolean startTaskByTaskId(Long taskId) throws Exception {
		//任务id不能为空
		if(taskId == null){
			return false;
		}
		//开始任务并返回结果
		boolean result = tmBiz.startTask(taskId);
		//返回结果
		return result;
	}
	
}
