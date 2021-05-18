package com.montnets.emp.common.timer;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.system.LfTimerHistory;


/**
 * 
 * @project emp
 * @author huangzhibin <307260621@qq.com>
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-2-21 下午03:08:29
 * @description 定时任务管理类
 */
public class TaskManagerBiz extends SuperBiz
{
	
	/**
	 * 
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public List<LfTimer> getTaskByExpression(String expression) {
		try
		{
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//表达式
			conditionMap.put("taskExpression", expression);
			//按条件查询定时任务集合
			List<LfTimer> timerTasksList = empDao.findListByCondition(LfTimer.class, conditionMap, null);
			return timerTasksList;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "根据定时任务表达式获取定时任务集合异常。");
			return null;
		}
	}
	
	/**
	 * 根据定时任务表达式，删除定时任务
	 * @param expression
	 * @return
	 * @throws Exception
	 */
	public Integer delTaskByExpression(String expression) throws Exception {
		//保存结果
		Integer result = 0;

		//查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//表达式
		conditionMap.put("taskExpression", expression);
		//根据表达式获取任务集合
		List<LfTimer> tasksList = empDao.findListByCondition(LfTimer.class, conditionMap, null);
		
		if(tasksList == null || tasksList.size() == 0){
			EmpExecutionContext.error("根据定时任务表达式，删除定时任务，未找到定时任务记录。");
			return 0;
		}
		
		//获取第一条记录
		LfTimer task = tasksList.get(0);
		
		//停止任务
		this.stopTask(task.getTimerTaskId());
		//删除任务
		result = this.delTaskByTimerTaskId(task.getTimerTaskId().toString());
	
		//返回结果
		return result;
	}
	
	/**
	 * 根据任务id批量删除定时任务
	 * @param timerTaskIds 任务id
	 * @return 返回删除条数
	 * @throws Exception
	 */
	private Integer delTaskByTimerTaskId(String timerTaskIds) throws Exception
	{
		//任务id不能为空
		if (timerTaskIds == null || "".equals(timerTaskIds))
		{
			return 0;
		}
		//保存结果数
		Integer result = 0;
		//获取连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开启事务
			empTransDao.beginTransaction(conn);
			
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//任务id
			conditionMap.put("timerTaskId", timerTaskIds);
			//删除历史
			result = empTransDao.delete(conn, LfTimerHistory.class, conditionMap);
			
			//删除任务并返回结果
			result = empTransDao.delete(conn, LfTimer.class, timerTaskIds);
			
			//提交事务
			empTransDao.commitTransaction(conn);
		} catch (Exception e)
		{
			//异常回滚
			empTransDao.rollBackTransaction(conn);
			result = null;
			EmpExecutionContext.error(e, "根据任务id批量删除定时任务异常。");
			throw e;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
		//返回结果
		return result;
	}
	
	/**
	 * 重新启动定时任务
	 * 
	 * @param timerTaskId
	 *            任务id
	 * @return 成功true
	 */
	public boolean startTask(Long timerTaskId)
	{
		//任务id不能为空
		if (timerTaskId == null)
		{
			EmpExecutionContext.error("启动任务失败，定时任务Id为null。");
			return false;
		}

		try
		{
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//任务id
			conditionMap.put("timerTaskId", timerTaskId.toString());
			
			//这里先查出来检查下状态
			List<LfTimer> timerTasksList = empDao.findListByCondition(LfTimer.class, conditionMap, null);
			
			if (timerTasksList == null || timerTasksList.size() == 0)
			{
				EmpExecutionContext.error("定时任务启动失败，未找到任务记录。");
				return false;
			}
			
			LfTimer timerTask = null;
			//有记录
			if (timerTasksList != null && timerTasksList.size() == 1)
			{
				//取第一条记录
				timerTask = timerTasksList.get(0);
			}

			//运行状态（0-停止,1-运行,2-暂停)
			// 任务状态已是运行，直接返回true
			if (timerTask != null && timerTask.getRunState() == 1)
			{
				return true;
			}
			
			// 任务正处于暂停中
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			//运行状态（0-停止,1-运行,2-暂停)，更新数据库的记录为1
			objectMap.put("runState", 1);
			
			boolean result = empDao.update(LfTimer.class, objectMap, timerTaskId.toString());
			return result;
			
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "重新启动定时任务异常。");
			return false;
		}
	}
	
	/**
	 * 停止任务
	 * 
	 * @param timerTaskId
	 *            定时任务id
	 * @return 成功true
	 */
	public boolean stopTask(Long timerTaskId)
	{
		//任务id不能为空
		if (timerTaskId == null)
		{
			EmpExecutionContext.error("停止任务失败，定时任务Id为null。");
			return false;
		}
		
		try
		{
			//更新对象
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			//运行状态（0-停止,1-运行,2-暂停)，更新数据库的记录为0
			objectMap.put("runState", 0);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//运行状态（0-停止,1-运行,2-暂停)，update时带上"运行中"的状态作为条件
			conditionMap.put("runState", "1");
			
			boolean result = empDao.update(LfTimer.class, objectMap, timerTaskId.toString(), conditionMap);
			return result;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "暂停定时任务异常");
			return false;
		}
	}
	
	/**
	 * 暂停定时任务
	 * 
	 * @param timerTaskId
	 *            定时任务id
	 * @return 成功返回true
	 */
	public boolean pauseTask(Long timerTaskId)
	{
		//任务id不能为空
		if (timerTaskId == null)
		{
			EmpExecutionContext.error("暂停任务失败，定时任务Id为null。");
			return false;
		}
		
		try
		{
			//更新对象
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			//运行状态（0-停止,1-运行,2-暂停)，更新数据库的记录为2
			objectMap.put("runState", 2);
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//运行状态（0-停止,1-运行,2-暂停)，update时带上"运行中"的状态作为条件
			conditionMap.put("runState", "1");
			
			boolean result = empDao.update(LfTimer.class, objectMap, timerTaskId.toString(), conditionMap);
			return result;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "暂停定时任务异常");
			return false;
		}
	}

	/**
	 * 设置定时任务，会删除所有与新任务表达式相同的已有任务
	 * 
	 * @param mt
	 *            定时任务
	 * @return 成功返回true
	 */
	public boolean setJobAndDelRepeat(MontnetTimerTask mt)
	{
		//获取连接
		Connection conn = empTransDao.getConnection();
		try
		{
			//开始事务
			empTransDao.beginTransaction(conn);

			//有任务则删掉原来的任务
			boolean delRes = this.delTaskByExpression(conn, mt.getTeTask().getTaskExpression());
			//删除失败
			if(!delRes){
				//回滚事务
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("添加定时任务，删除定时任务记录失败。");
				return false;
			}
			
			//设置定时任务
			boolean result = this.setJob(conn, mt);
			if(result){
				//提交事务
				empTransDao.commitTransaction(conn);
			}else{
				//回滚事务
				empTransDao.rollBackTransaction(conn);
			}
			return result;
		} catch (Exception e)
		{
			//回滚事务
			empTransDao.rollBackTransaction(conn);
			EmpExecutionContext.error(e, "添加定时任务，设置定时任务异常。");
			return false;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 根据任务表达式删除定时任务
	 * @param conn
	 * @param taskExpression 任务表达式
	 * @return 返回删除条数
	 * @throws Exception
	 */
	private boolean delTaskByExpression(Connection conn, String taskExpression)
	{
		//表达式不能为空
		if (taskExpression == null || "".equals(taskExpression))
		{
			EmpExecutionContext.error("添加定时任务，任务表达式为null。");
			return false;
		}

		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//表达式
			conditionMap.put("taskExpression", taskExpression);
			//删除历史
			Integer delHisRes = empTransDao.delete(conn, LfTimerHistory.class, conditionMap);
			
			//删除任务
			Integer delTimerRes = empTransDao.delete(conn, LfTimer.class, conditionMap);
			
			//删除历史和删除任务不判断影响行数，因为可能数据库没记录
			if(delHisRes != null && delTimerRes != null){
				return true;
			}
			return false;
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "添加定时任务，根据任务表达式删除定时任务失败。");
			return false;
		}
	}


	/**
	 * 添加定时任务
	 * 
	 * @param mt
	 *            定时任务对象
	 * @return 成功返回true
	 */
	public boolean setJob(MontnetTimerTask mt)
	{
		//设置定时
		return this.setJob(null, mt);
	}

	/**
	 * 添加定时任务
	 * 
	 * @param conn
	 * @param mt
	 *            定时任务对象
	 * @return 成功返回true
	 */
	private boolean setJob(Connection conn, MontnetTimerTask mt)
	{
		TimerManagerBiz timerBiz = TimerManagerBiz.getTMInstance();
		//如果定时器没启动，这里启动
		timerBiz.StartTimerIfStop();
		
		//任务不能为空
		if (mt == null || mt.getTeTask() == null)
		{
			EmpExecutionContext.error("设置定时失败，任务对象null。");
			return false;
		}
		//执行时间不能为null
		if (mt.getTeTask().getNextTime() == null || mt.getTeTask().getClassName() == null)
		{
			EmpExecutionContext.error("设置定时失败，必要参数为空，" 
					+ "任务表达式：" + mt.getTeTask().getTaskExpression()
					+ "，任务名称：" + mt.getTeTask().getTimerTaskName()
					+ "，执行时间为：" +mt.getTeTask().getNextTime()
					+ "，className为：" +mt.getTeTask().getClassName());
			return false;
		}

		try
		{
			//设置默认值
			boolean setDefRes = setTaskDefaultValue(mt);
			if(!setDefRes){
				EmpExecutionContext.error("设置定时任务，设置默认值失败。"
						+ "任务表达式：" + mt.getTeTask().getTaskExpression()
						+ "，任务名称：" + mt.getTeTask().getTimerTaskName()
						+ "，执行时间为：" +mt.getTeTask().getNextTime()
						+ "，className为：" +mt.getTeTask().getClassName());
				return false;
			}
			
			//获取当前时间的日期对象
			Calendar calNowTime = Calendar.getInstance();
			calNowTime.clear();
			//设置为当前时间
			calNowTime.setTimeInMillis(System.currentTimeMillis());
			
			//定时时间与当前时间的时间差，用于比较设置的定时是否是过去的时间，为负数时，即使用了过去的时间
			long interval = mt.getTeTask().getNextTime().getTime() - calNowTime.getTimeInMillis();
			
			//运行间隔大于0，定时时间已过期，运行状态为运行，重新计算执行时间，即从下个周期开始执行
			if (mt.getTeTask().getRunInterval() > 0 && interval < 0 && mt.getTeTask().getRunState() == 1)
			{
				TaskBiz taskBiz = new TaskBiz();
				//计算下次执行时间
				Long nextTime = taskBiz.computeNextTime(mt.getTeTask().getNextTime(), mt.getTeTask().getRunInterval(), mt.getTeTask().getIntervalUnit());
				if(nextTime == null){
					return false;
				}
				
				mt.getTeTask().setNextTime(new Timestamp(nextTime));
				mt.getTeTask().setStartTime(new Timestamp(nextTime));
			} 
			//过期，且只执行一次，则不再需要计算下次时间
			else if (mt.getTeTask().getRunInterval() == 0 && interval < 0)
			{
				EmpExecutionContext.info("任务定时时间已过期。" 
						+ "任务表达式：" + mt.getTeTask().getTaskExpression()
						+ "，任务名称：" + mt.getTeTask().getTimerTaskName()
						+ "，执行时间为：" +mt.getTeTask().getNextTime()
						+ "，className为：" +mt.getTeTask().getClassName());
				return false;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置定时任务失败。"
					+ "任务表达式：" + mt.getTeTask().getTaskExpression()
					+ "，任务名称：" + mt.getTeTask().getTimerTaskName()
					+ "，执行时间为：" +mt.getTeTask().getNextTime()
					+ "，className为：" +mt.getTeTask().getClassName());
			return false;
		}
		
		//添加结果
		boolean resultReturn = false;
		if (conn != null)
		{
			// 添加定时任务
			resultReturn = this.addJob(conn, mt.getTeTask());
		} else
		{
			// 添加定时任务
			resultReturn = this.addJob(null, mt.getTeTask());
		}

		if (resultReturn)
		{
			//日期格式化
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			EmpExecutionContext.info("设置定时成功：id:" + mt.getTeTask().getTimerTaskId() 
					+ "，任务表达式：" + mt.getTeTask().getTaskExpression()
					+ "，任务名称：" + mt.getTeTask().getTimerTaskName()
					+ "，执行时间：" + sdf.format(mt.getTeTask().getNextTime()) 
					+ "，定时间隔：" + mt.getTeTask().getRunInterval());
		}
		//返回结果
		return resultReturn;
	}

	/**
	 * 设置定时任务默认值
	 * @param mt
	 * @return
	 */
	private boolean setTaskDefaultValue(MontnetTimerTask mt){
		try
		{
			//运行状态（0-停止,1-运行,2-暂停)
			if(mt.getTeTask().getRunState() == null)
			{
				//未设置运行状态，则默认设为运行状态
				mt.getTeTask().setRunState(1);
			}
			//执行间隔（单位为：天）
			if(mt.getTeTask().getRunInterval() == null)
			{
				//未设置执行间隔，则默认只执行一次
				mt.getTeTask().setRunInterval(0);
			}
			//运行次数（每天定时任务允许运行的次数)
			if(mt.getTeTask().getRunPerCount() == null)
			{
				//未设置允许运行次数，则默认只允许运行一次
				mt.getTeTask().setRunPerCount(1);
			}
			//自定义时间差(有效范围)
			if(mt.getTeTask().getCustomInterval() == null)
			{
				//未设置自定义时间差(有效范围)，则默认设为0，即没自定义时间差
				mt.getTeTask().setCustomInterval(0l);
			}
			//开始时间
			if(mt.getTeTask().getStartTime() == null){
				//未设置开始时间，则设置为下次执行时间
				mt.getTeTask().setStartTime(mt.getTeTask().getNextTime());
			}
			//执行间隔单位，默认单位为天
			if(mt.getTeTask().getIntervalUnit() == null){
				//执行间隔单位。1-天；2-月 ;3-小时
				mt.getTeTask().setIntervalUnit(1);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置定时任务，设置任务默认值异常。");
			return false;
		}
	}
	
	/**
	 * 添加定时任务
	 * 
	 * @param timerTask
	 *            定时任务对象
	 * @return 成功true
	 */
	private boolean addJob(Connection conn, LfTimer timerTask)
	{

		if (timerTask == null)
		{
			EmpExecutionContext.error("添加定时任务，定时任务对象为null。");
			return false;
		} else if (timerTask.getNextTime() == null)
		{
			EmpExecutionContext.error("添加定时任务，任务下次运行时间为null。");
			return false;
		} else if (timerTask.getRunState() == null)
		{
			EmpExecutionContext.error("添加定时任务，任务运行状态为null。");
			return false;
		} else if (timerTask.getRunInterval() == null)
		{
			EmpExecutionContext.error("添加定时任务，任务运行间隔为null。");
			return false;
		} else if (timerTask.getRunPerCount() == null)
		{
			EmpExecutionContext.error("添加定时任务，每天允许运行次数为null。");
			return false;
		}

		try
		{
			//设置运行次数
			timerTask.setRunCount(0);
			//任务id
			Long timerTaskId = null;

			//数据库连接为空
			if (conn == null)
			{
				//保存记录到数据库
				timerTaskId = empDao.saveObjectReturnID(timerTask);
			} else
			{
				//保存记录到数据库
				timerTaskId = empTransDao.saveObjectReturnID(conn, timerTask);
			}

			//设置任务id
			timerTask.setTimerTaskId(timerTaskId);
			//成功结果
			return true;
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "添加定时任务异常。");
			return false;
		}
	}

	/**
	 * 停止定时任务根据expression 2012-10-23
	 * 
	 * @param taskExpression
	 * @return boolean
	 */
	public boolean stopTask(String taskExpression)
	{
		try
		{
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//更新对象
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			conditionMap.put("taskExpression", taskExpression);
			objectMap.put("runState", "0");
			//执行更新
			return empDao.update(LfTimer.class, objectMap, conditionMap);
		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "根据任务表达式停止定时任务异常。");
			return false;
		}
	}
	
	/**
	 * 检查任务是否未成功执行过
	 * @param taskExpression 任务表达式
	 * @return 返回true表示未执行过
	 */
	public boolean checkIsRun(String taskExpression)
	{
		try
		{
			//当前日期
			Calendar curTime = Calendar.getInstance();
			//查询条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("runYear", String.valueOf(curTime.get(Calendar.YEAR)));
			conditionMap.put("runMonth", String.valueOf(curTime.get(Calendar.MONTH) + 1));
			conditionMap.put("runDay", String.valueOf(curTime.get(Calendar.DAY_OF_MONTH)));
			conditionMap.put("taskExpression", taskExpression);
			//获取状态为"成功"的发送历史记录
			conditionMap.put("runResult&in", "1");
			
			//按条件查询
			List<LfTimerHistory> timersList = empDao.findListBySymbolsCondition(LfTimerHistory.class, conditionMap, null);
			
			//无记录
			if(timersList != null && timersList.size() == 0)
			{
				//返回true
				return true;
			}
			else if(timersList == null)
			{
				//有记录，则返回否
				EmpExecutionContext.error("任务id:"+taskExpression+"，查询历史记录为null，该次执行取消!");
				return false;
			}else
			{
				//有记录，则返回否
				EmpExecutionContext.error("任务id:"+taskExpression+"，在当天已执行过，该次执行取消!");
				return false;
			}
		}
		catch(Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "检查任务是否已成功执行过失败。");
			return false;
		}
		
	}
	
	/**
	 * 停止定时任务
	 * @param conn
	 * @param taskExpression
	 * @return
	 * @throws Exception
	 */
	public boolean stopTimerTask(Connection conn, String taskExpression) throws Exception
	{

		//结果
		boolean result = false;
		try
		{
			//更新内容
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			objectMap.put("runState", "0");
			//条件
			LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
			conditionmap.put("taskExpression", taskExpression);
			//执行更新
			result = empTransDao.update(conn, LfTimer.class,
							objectMap, conditionmap);

		} catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "停止定时任务失败。");
			throw e;
		}

		//返回结果
		return result;
	}
	
	/**
	 * 删除定时任务记录，先停止，再删除
	 * @param conn
	 * @param taskExpression
	 * @return 成功返回true
	 */
	public boolean delTimerTask(Connection conn, String taskExpression)
	{
		boolean result = false;
		try
		{
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			//运行状态（0-停止,1-运行,2-暂停)
			objectMap.put("runState", "0");
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("taskExpression", taskExpression);
			
			//先停止状态
			empTransDao.update(conn, LfTimer.class, objectMap, conditionMap);
			
			//再先删除历史
			conditionMap.clear();
			conditionMap.put("taskExpression", taskExpression);
			empTransDao.delete(conn, LfTimerHistory.class, conditionMap);
			
			//最后删除任务
			empTransDao.delete(conn, LfTimer.class, conditionMap);
			
			result = true;
		} 
		catch (Exception e) 
		{
			//异常处理
			EmpExecutionContext.error(e, "删除定时任务记录失败。");
			return false;
		}
		//返回结果
		return result;
	}

	
}
