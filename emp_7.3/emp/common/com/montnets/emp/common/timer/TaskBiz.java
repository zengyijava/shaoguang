package com.montnets.emp.common.timer;

import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SpecialDAO;
import com.montnets.emp.entity.system.LfTimer;
import com.montnets.emp.entity.system.LfTimerHistory;

/**
 * 任务执行类，执行任务的相关方法都放这里
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2013-10-14 下午06:59:05
 * @description
 */
public class TaskBiz extends SuperBiz
{

	/**
	 * 运行定时任务
	 * @param lfTimer
	 * @param errorCode
	 * @return
	 */
	public boolean runTask(LfTimer lfTimer)
	{
		
		if(lfTimer == null){
			EmpExecutionContext.error("定时器执行任务失败，任务对象为null。");
			return false;
		}
		//由于下面会操作内存中的lfTimer，所以这里备份一个副本，在操作失败的时候可还原
		LfTimer originalTimer = lfTimer;
		TimerErrorCode errorCode = new TimerErrorCode();
		
		boolean isAllowRun = true;
		
		try
		{
			// 是否允许执行定时任务
			boolean isAllowed = isAllowRun(lfTimer, errorCode);
			if (errorCode.getErrorCode() != null)
			{
				// 出现异常，处理重发等
				this.errorExc(lfTimer, errorCode);
				EmpExecutionContext.error("定时器执行任务，判断任务是否允许执行异常。" 
						+ "错误代码："+errorCode.getErrorCode() 
						+ ",任务Id=" + lfTimer.getTimerTaskId()
						+ ",任务名=" + lfTimer.getTimerTaskName() 
						+ ",任务表达式=" + lfTimer.getTaskExpression() 
						+ ",runState=" + lfTimer.getRunState()
						+ ",startTime=" + lfTimer.getStartTime()
						+ ",preTime=" + lfTimer.getPreTime()
						+ ",nextTime=" + lfTimer.getNextTime()
						+ ",result=" + lfTimer.getResult()
						+ ",runInterval=" + lfTimer.getRunInterval()
						+ ",taskErrorCode=" + lfTimer.getTaskErrorCode()
						+ ",failRetryTime=" + lfTimer.getFailRetryTime()
						+ ",failRetryCount=" + lfTimer.getFailRetryCount()
						+ ",runCount=" + lfTimer.getRunCount()
						+ ",runPerCount=" + lfTimer.getRunPerCount()
						+ ",className=" + lfTimer.getClassName()
						+ ",customInterval=" + lfTimer.getCustomInterval()
						+ ",isRerun=" + lfTimer.getIsRerun()
						+ ",rerunCount=" + lfTimer.getRerunCount()
						+ ",intervalUnit=" + lfTimer.getIntervalUnit()
						);
				return false;
			}
			//符合条件，可执行
			if(isAllowed){
				//EmpExecutionContext.info("定时器执行任务，检查任务是否可执行成功。任务Id："+lfTimer.getTimerTaskId());
			}
			// 如果不满足发送条件，则不需要再处理重发
			else 
			{
				isAllowRun = false;
				
				//判断并设置继续执行，还是停止
				setNextTimebyCon(lfTimer);
				
				// 执行结果设为已执行过
				lfTimer.setResult(3);
				//设置错误代码
				lfTimer.setTaskErrorCode(IErrorCode.E_DS_B202);
				//执行更新并返回结果
				boolean isUpdate = empDao.update(lfTimer);
				EmpExecutionContext.error("定时器执行任务，检查任务是否可执行失败，任务不满足执行条件，更新状态" + (isUpdate ? "成功。" : "失败。")+"任务Id:"+lfTimer.getTimerTaskId());
				//返回结果
				return false;
			}
		}catch(Exception e){
			
			// 运行结果设为失败
			lfTimer.setResult(0);
			if(lfTimer.getTaskErrorCode() == null){
				lfTimer.setTaskErrorCode(IErrorCode.E_DS_B204);
			}
			//上面已设置重发和下次执行等，这里不需要再处理，如再处理，则会导致异常情况，执行时间会被计算错误，这里直接update，让前面的设置保存
			this.updateLfTimer(lfTimer);
			EmpExecutionContext.error(e, "定时器执行任务，执行任务前检查任务是否可运行异常。任务Id:"+lfTimer.getTimerTaskId());
			return false;
		}
		
		// 定时任务历史记录
		LfTimerHistory lfTimerHistory = new LfTimerHistory();
		try{
			// 运行前更新状态
			boolean resultBefroeUpdate = updateBeforeRun(lfTimer, lfTimerHistory, errorCode);
			//更新成功
			if (resultBefroeUpdate)
			{
				//数据库记录更新成功，内存中保存的备份也更新
				originalTimer = lfTimer;
				EmpExecutionContext.info("定时器执行任务，运行前更新状态成功。任务Id："+lfTimer.getTimerTaskId());
			}
			//更新失败
			else{
				//前面的方法有更改内存中lftimer对象的值，先还原为原来的lftimer对象，避免后面的设置错误
				lfTimer = originalTimer;
				isAllowRun = false;
				// 出现异常，处理重发等
				this.errorExc(lfTimer, errorCode);
				
				EmpExecutionContext.error("定时器执行任务，运行前更新状态失败。错误编码："+errorCode.getErrorCode()+"，任务id:"+lfTimer.getTimerTaskId());
				return false;
			}
			
			// 确认之前的状态更新成功
			boolean resultConfirm = new SpecialDAO().confirmUpdateBeforeRun(lfTimer,
					lfTimerHistory.getTaId().toString(), errorCode);

			//确认更新成功
			if (resultConfirm)
			{
				EmpExecutionContext.info("定时器执行任务，执行前更新后，确认更新任务状态成功。任务Id:"+lfTimer.getTimerTaskId());
			}
			//确认更新失败
			else{
				isAllowRun = false;
				if(errorCode.getErrorCode() == null){
					errorCode.setErrorCode(IErrorCode.E_DS_D202);
				}
				// 出现异常，处理重发等
				this.errorExc(lfTimer, errorCode);
				
				EmpExecutionContext.error("定时器执行任务，执行前更新后，确认更新任务状态失败。任务Id:"+lfTimer.getTimerTaskId());
				return false;
			}

			// 开始执行前检查类名是否可用，如果不可用，那就直接退出，不需要再设置重发了
			boolean cheResult = checkClassName(lfTimer.getClassName(), errorCode);

			//检查业务类成功
			if (cheResult)
			{
				//EmpExecutionContext.info("定时器执行任务，检查业务类成功。任务Id:"+lfTimer.getTimerTaskId());
			}
			//检查业务类失败
			else{
				// 设为失败
				lfTimer.setResult(0);
				// 停止
				lfTimer.setRunState(0);
				lfTimer.setNextTime(null);

				lfTimer.setTaskErrorCode(errorCode.getErrorCode());
				//更新任务
				empDao.update(lfTimer);
				
				EmpExecutionContext.error("定时器执行任务，检查业务类失败。" 
						+ "错误编码："+errorCode.getErrorCode()
						+ "任务Id=" + lfTimer.getTimerTaskId()
						+ ",任务名=" + lfTimer.getTimerTaskName() 
						+ ",任务表达式=" + lfTimer.getTaskExpression() 
						+ ",runState=" + lfTimer.getRunState()
						+ ",startTime=" + lfTimer.getStartTime()
						+ ",preTime=" + lfTimer.getPreTime()
						+ ",nextTime=" + lfTimer.getNextTime()
						+ ",result=" + lfTimer.getResult()
						+ ",runInterval=" + lfTimer.getRunInterval()
						+ ",taskErrorCode=" + lfTimer.getTaskErrorCode()
						+ ",failRetryTime=" + lfTimer.getFailRetryTime()
						+ ",failRetryCount=" + lfTimer.getFailRetryCount()
						+ ",runCount=" + lfTimer.getRunCount()
						+ ",runPerCount=" + lfTimer.getRunPerCount()
						+ ",className=" + lfTimer.getClassName()
						+ ",customInterval=" + lfTimer.getCustomInterval()
						+ ",isRerun=" + lfTimer.getIsRerun()
						+ ",rerunCount=" + lfTimer.getRerunCount()
						+ ",intervalUnit=" + lfTimer.getIntervalUnit()
						);
				return false;
			}
			
		}catch(Exception e){
			//无法确定lftimer是否有更改，先还原为原来的lftimer对象
			lfTimer = originalTimer;
			// 运行结果设为失败
			lfTimer.setResult(0);
			
			// 出现异常，处理重发等
			this.errorExc(lfTimer, errorCode);
			
			EmpExecutionContext.error(e, "定时器执行任务，执行任务前更新任务异常。"+"任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
		
		//执行结果，默认为false，即失败
		boolean resultRun = false;
		try{
			//反射任务类
			Class<MontnetTimerTask> c = (Class<MontnetTimerTask>) Class.forName(lfTimer.getClassName());
			MontnetTimerTask mtt = c.newInstance();

			mtt.getTeTask().setTaskExpression(lfTimer.getTaskExpression());
			// 调用业务类的业务方法
			resultRun = mtt.taskMethod(isAllowRun);
			
			EmpExecutionContext.info("定时器执行任务，执行成功。" 
					+ "任务Id=" + lfTimer.getTimerTaskId()
					+ ",任务名=" + lfTimer.getTimerTaskName() 
					+ ",任务表达式=" + lfTimer.getTaskExpression() 
					+ ",runState=" + lfTimer.getRunState()
					+ ",startTime=" + lfTimer.getStartTime()
					+ ",preTime=" + lfTimer.getPreTime()
					+ ",nextTime=" + lfTimer.getNextTime()
					+ ",result=" + lfTimer.getResult()
					+ ",runInterval=" + lfTimer.getRunInterval()
					+ ",taskErrorCode=" + lfTimer.getTaskErrorCode()
					+ ",failRetryTime=" + lfTimer.getFailRetryTime()
					+ ",failRetryCount=" + lfTimer.getFailRetryCount()
					+ ",runCount=" + lfTimer.getRunCount()
					+ ",runPerCount=" + lfTimer.getRunPerCount()
					+ ",className=" + lfTimer.getClassName()
					+ ",customInterval=" + lfTimer.getCustomInterval()
					+ ",isRerun=" + lfTimer.getIsRerun()
					+ ",rerunCount=" + lfTimer.getRerunCount()
					+ ",intervalUnit=" + lfTimer.getIntervalUnit()
					+ ",任务结果=" + resultRun
			);

			//结果为成功
			if (resultRun)
			{
				//设置结果为成功
				lfTimer.setResult(1);
				//设置下次执行
				setNextTimebyCon(lfTimer);
				empDao.update(lfTimer);
			} 
			else
			{
				//设置错误代码
				errorCode.setErrorCode(IErrorCode.E_DS_B400);
				// 出现异常，处理重发等
				this.errorExc(lfTimer, errorCode);
				errorCode.setErrorCode(null);
			}

			//数据库记录更新成功，内存中保存的备份也更新
			originalTimer = lfTimer;
			
			//运行后更新，只更新lfTimerHistory，执行结果都在lfTimerHistory
			boolean resultAfterUpdate = updateTimerTaskAfterRun(lfTimer, lfTimerHistory, errorCode);
			//更新成功
			if (resultAfterUpdate)
			{
				EmpExecutionContext.info("定时器执行任务，执行后更新数据库记录成功。任务Id:"+lfTimer.getTimerTaskId());
			}
			//更新失败
			else{
				EmpExecutionContext.error("定时器执行任务，执行后更新数据库记录失败。任务Id:"+lfTimer.getTimerTaskId());
				return false;
			}
			
			return true;
		} catch (Exception e)
		{
			//执行成功，则判断并设置下次执行时间
			if(resultRun){
				//执行成功，判断并设置下次执行时间
				setNextTimebyCon(lfTimer);
				updateLfTimer(lfTimer);
				
				EmpExecutionContext.error(e, "定时器执行任务，执行定时任务发生异常。但任务已运行成功"+"，任务Id:"+lfTimer.getTimerTaskId());
			}else{
				//执行失败，则判断重发
				// 出现异常，处理重发等
				this.errorExc(lfTimer, errorCode);
				EmpExecutionContext.error(e, "定时器执行任务，执行定时任务发生异常。但任务运行失败"+"，任务Id:"+lfTimer.getTimerTaskId());
			}
			return false;
		}
	}
	
	/**
	 * 定时任务过期处理
	 * @param lfTimer 定时任务对象
	 * @return 成功返回true
	 */
	public boolean runTimeOutTask(LfTimer lfTimer)
	{
		if(lfTimer == null){
			EmpExecutionContext.error("定时任务过期处理失败，任务对象为null。");
			return false;
		}
		TimerErrorCode errorCode = new TimerErrorCode();
		
		// 定时任务历史记录
		LfTimerHistory lfTimerHistory = new LfTimerHistory();
		try
		{
			//运行前更新，包括更新任务状态和插入任务历史记录
			boolean updateBeforRes = updateBeforeRun(lfTimer, lfTimerHistory, errorCode);
			if(updateBeforRes){
				EmpExecutionContext.info("定时任务过期处理，处理前更新数据库成功。任务Id："+lfTimer.getTimerTaskId());
			}
			else{
				EmpExecutionContext.error("定时任务过期处理，处理前更新数据库失败。任务Id:"+lfTimer.getTimerTaskId());
				return false;
			}
			
			// 开始执行前检查类名是否可用，如果不可用，那就直接退出，不需要再设置重发了
			boolean cheResult = checkClassName(lfTimer.getClassName(), errorCode);
			if (cheResult)
			{
				EmpExecutionContext.info("定时任务过期处理，检查业务类成功。任务id："+lfTimer.getTimerTaskId());
			}
			//检查类失败，把任务停止掉
			else{
				
				// 设为失败
				lfTimer.setResult(0);
				// 停止
				lfTimer.setRunState(0);
				lfTimer.setNextTime(null);

				lfTimer.setTaskErrorCode(errorCode.getErrorCode());
				//更新任务
				boolean checkUpdateRes = empDao.update(lfTimer);
				
				EmpExecutionContext.error("定时任务过期处理，检查业务类失败。错误编码："+errorCode.getErrorCode()+"，任务Id:"+lfTimer.getTimerTaskId()+"。更新结果："+checkUpdateRes);
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时任务过期处理，运行前处理任务异常。任务Id："+lfTimer.getTimerTaskId());
			return false;
		}
		
		//执行结果，默认为false，即失败
		boolean resultRun = false;
		try{
			//反射任务类
			Class<MontnetTimerTask> c = (Class<MontnetTimerTask>) Class.forName(lfTimer.getClassName());
			MontnetTimerTask mtt = c.newInstance();

			mtt.getTeTask().setTaskExpression(lfTimer.getTaskExpression());
			// 调用业务类的业务方法
			resultRun = mtt.taskMethodForTimeOut(lfTimer.getTaskExpression());

		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时任务过期处理，调用业务类异常。任务Id："+lfTimer.getTimerTaskId());
		}
		
		try
		{
			//结果为成功
			if (resultRun)
			{
				boolean setRes = setDealTimeOut(lfTimer, errorCode);
				EmpExecutionContext.info("定时任务过期处理，处理成功。任务Id:" + lfTimer.getTimerTaskId()
						+ ";任务名：" + lfTimer.getTimerTaskName() + "。更新结果："+setRes);
			} 
			else
			{
				//判断并设置继续执行，还是停止
				setNextTimebyCon(lfTimer);
				// 执行结果设为过期未执行
				lfTimer.setResult(TimerStaticValue.RESULT_TIMEOUT);
				//设置错误编码
				lfTimer.setTaskErrorCode(IErrorCode.E_DS_B205);
				
				boolean updatelfTierRes = empDao.update(lfTimer);
				
				EmpExecutionContext.error("定时任务过期处理，处理失败。任务Id:" + lfTimer.getTimerTaskId()
						+ ";任务名：" + lfTimer.getTimerTaskName() + "。更新结果："+updatelfTierRes);
			}
			
			//运行后更新，只更新lfTimerHistory，执行结果都在lfTimerHistory
			boolean resultAfterUpdate = updateTimerTaskAfterRun(lfTimer, lfTimerHistory, errorCode);
			
			if(resultAfterUpdate){
				EmpExecutionContext.info("定时任务过期处理，处理后更新成功。任务Id："+lfTimer.getTimerTaskId());
				return true;
			}
			else{
				EmpExecutionContext.error("定时任务过期处理，处理后更新失败。任务Id："+lfTimer.getTimerTaskId());
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时任务过期处理，处理后更新异常。任务Id："+lfTimer.getTimerTaskId());
			return false;
		}
		
	}
	
	/**
	 * 过期处理任务
	 * @param lfTimer
	 * @param errorCode
	 * @return
	 */
	private boolean setDealTimeOut(LfTimer lfTimer, TimerErrorCode errorCode){
		
		try
		{
			//判断并设置继续执行，还是停止
			setNextTimebyCon(lfTimer);
			
			// 执行结果设为过期未执行
			lfTimer.setResult(TimerStaticValue.RESULT_TIMEOUT);

			//设置错误编码
			lfTimer.setTaskErrorCode(IErrorCode.E_DS_B201);
			//执行更新并返回结果
			boolean isUpdate = empDao.update(lfTimer);
			if(isUpdate){
				EmpExecutionContext.info("任务已过期，更新状态成功。任务id="+lfTimer.getTimerTaskId());
				return true;
			}
			else{
				EmpExecutionContext.error("任务已过期，更新状态失败。任务id="+lfTimer.getTimerTaskId());
				return false;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时任务过期处理，设置任务并更新异常。");
			return false;
		}
	}
		
	/**
	 * 判断当天运行次数是否超过当天允许运行次数
	 * 
	 * @param lfTimer
	 *            定时任务对象
	 * @return true允许
	 */
	private boolean isAllowRun(LfTimer lfTimer, TimerErrorCode errorCode)
	{
		//任务id不为空
		if (lfTimer.getTimerTaskId() == null)
		{
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			EmpExecutionContext.error("判断是否允许运行失败，定时任务对象Id为空。");
			return false;
		} else if (lfTimer.getRunPerCount() == null)
		{
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			EmpExecutionContext.error("判断是否允许运行失败，任务每天允许运行次数为空。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}

		try
		{
			//获取任务当天运行次所
			Integer runedCount = this.getTaskRunedCount(lfTimer.getTimerTaskId(), errorCode);
			// 当天任务运行次数为null，则存在异常
			if (runedCount == null)
			{
				// 如果为空返回false
				EmpExecutionContext.error("判断是否允许运行失败，获取任务的当天运行次数为空。"+"，任务id:"+lfTimer.getTimerTaskId());
				return false;
			}
			
			//判断当天实际运行次数是否少于允许运行的次数
			if (runedCount < lfTimer.getRunPerCount())
			{
				// 当天运行次数小于当天允许运行次数则返回true(如果运行历史有29条，那么表示现在是第三十次运行)
				return true;
			}
		} catch (Exception e)
		{
			//异常处理
			errorCode.setErrorCode(IErrorCode.E_DS_B200);
			EmpExecutionContext.error(e, "判断当天运行次数是否超过当天允许运行次数异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
		//返回结果
		return false;
	}
	
	/**
	 * 获取任务当天的所有运行记录数
	 * 
	 * @param timerTaskId
	 *            定时任务id
	 * @return 返回任务当天运行次数，异常情况返回null
	 */
	private Integer getTaskRunedCount(Long timerTaskId, TimerErrorCode errorCode)
	{
		if (timerTaskId == null)
		{
			// 定时任务id不能为空
			errorCode.setErrorCode(IErrorCode.E_DS_D100);
			return null;
		}
		
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("timerTaskId", timerTaskId.toString());

			Calendar calRunTime = Calendar.getInstance();
			calRunTime.clear();
			// 将当前的时间转化成可以获取年月日的Calender
			calRunTime.setTimeInMillis(System.currentTimeMillis());

			conditionMap.put("runYear", String.valueOf(calRunTime
					.get(Calendar.YEAR)));
			conditionMap.put("runMonth", String.valueOf(calRunTime
					.get(Calendar.MONTH) + 1));
			conditionMap.put("runDay", String.valueOf(calRunTime
					.get(Calendar.DAY_OF_MONTH)));
			// 获取状态为"成功","等待执行结果"的发送历史记录
			conditionMap.put("runResult&in", "1,2");

			//获取历史记录
			List<LfTimerHistory> timerHisList = empDao
					.findListBySymbolsCondition(LfTimerHistory.class,
							conditionMap, null);
			
			//为null，则存在异常
			if (timerHisList == null){
				return null;
			}else {
				// 返回此定时的历史条数（当天运行次数）
				return timerHisList.size();
			}
			
		} catch (Exception e)
		{
			//异常处理
			errorCode.setErrorCode(IErrorCode.E_DS_D100);
			EmpExecutionContext.error(e, "获取某一天同一任务的所有运行记录数异常。"+"，任务id:"+timerTaskId);
			return null;
		}
	}
	
	/**
	 * 异常处理是否需要重发
	 * 
	 * @param lfTimer
	 *            需要重发必须设置IsRerun=1和customInterval的值大于0
	 * @param errorCode
	 * @return
	 * @throws Exception
	 */
	private boolean errorExc(LfTimer lfTimer, TimerErrorCode errorCode)
	{
		if(lfTimer == null){
			EmpExecutionContext.error("定时器执行任务失败，任务对象为null。");
			return false;
		}
		//由于下面会操作内存中的lfTimer，所以这里备份一个副本，在操作失败的时候可还原
		LfTimer originalTimer = lfTimer;
		
		try
		{
			//IsRerun为异常情况是否需要重发；1-需要；0,null-不需要
			if(lfTimer.getIsRerun() == 1){
				// 由于有customInterval大于0，即自定义有效时间必须为大于0，这样任务在这个有效时间内不会过期，只要状态为运行，则会再次被定时器执行
				
				// 重发次数加一
				lfTimer.setRerunCount(lfTimer.getRerunCount() + 1);
				//设置状态为运行
				lfTimer.setRunState(1);
				EmpExecutionContext.info("任务设置重发，任务运行多次，将会在自定义有效时间范围内重新执行。"+"，任务id:"+lfTimer.getTimerTaskId());
			
			}else{
				//不需要重发
				// 设为失败
				lfTimer.setResult(0);
				this.setNextTimebyCon(lfTimer);
			}
			
			lfTimer.setTaskErrorCode(errorCode.getErrorCode());
			//执行更新
			boolean result = empDao.update(lfTimer);
			//返回结果
			return result;
		}
		catch (Exception e)
		{
			//还原内存中的lftimer对象
			lfTimer = originalTimer;
			EmpExecutionContext.error(e, "定时器执行任务，异常处理是否需要重发异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
	/**
	 * 判判断并设置继续执行，还是停止，该方法不做更新数据库，只做设置值
	 * @param lfTimer
	 * @return
	 */
	public boolean setNextTimebyCon(LfTimer lfTimer){
		try
		{
			//只运行一次
			if (lfTimer.getRunInterval() == 0)
			{
				// 运行状态设为停止
				lfTimer.setRunState(0);
				// 下次执行时间设为空
				lfTimer.setNextTime(null);
			} else
			{
				// 设置下次执行时间
				this.setNextTime(lfTimer);
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时器执行定时任务，判断是否需要设置下次执行时间异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
	/**
	 * 设置下次执行时间
	 * @param lfTimer
	 * @return
	 */
	private boolean setNextTime(LfTimer lfTimer) 
	{
		try
		{
			// 状态设为启动
			lfTimer.setRunState(1);
			
			//计算下次执行日期时间
			Long nextTime = this.computeNextTime(lfTimer.getNextTime(), lfTimer.getRunInterval(), lfTimer.getIntervalUnit());
			if(nextTime == null){
				return false;
			}
			//设置下次执行时间
			lfTimer.setNextTime(new Timestamp(nextTime));
			
			lfTimer.setTaskErrorCode("");

			//日期格式化
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			EmpExecutionContext.info("ID：" + lfTimer.getTimerTaskId() + ";任务名：" + lfTimer.getTimerTaskName());
			EmpExecutionContext.info("下次运行时间：" + sdf.format(lfTimer.getNextTime()));

			//返回结果
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时器执行任务失败，从新设置任务下次执行时间异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
	/**
	 * 计算下次执行日期时间
	 * @return 返回日期时间毫秒数
	 */
	public Long computeNextTime(Timestamp preTime, Integer runInterval, Integer intervalUnit){
		try
		{
			//日期对象
			Calendar cal = Calendar.getInstance();
			cal.clear();
			// 执行时间
			cal.setTime(preTime);

			//获取一个当前日期对象，这样可让过期很久的任务能得到马上更新日期
			Calendar curCal = Calendar.getInstance();
			//取当前日期的年月日
			cal.set(Calendar.YEAR, curCal.get(Calendar.YEAR));
			cal.set(Calendar.MONTH, curCal.get(Calendar.MONTH));
			cal.set(Calendar.DAY_OF_MONTH, curCal.get(Calendar.DAY_OF_MONTH));

			//执行间隔单位。1-天；2-月 。执行间隔单位为空，则单位默认为天
			if(intervalUnit == null || intervalUnit == 1)
			{
				// 运行时间加上间隔天数
				cal.add(Calendar.DAY_OF_MONTH, runInterval);
			}
			//单位为月
			else if(intervalUnit == 2)
			{
				//用于对比的日期对象，用户对比增加的日期是否为无效日期
				Calendar compareCal = Calendar.getInstance();
				compareCal.setTimeInMillis(cal.getTimeInMillis());
				// 对比日期加上间隔月数
				compareCal.add(Calendar.MONTH, runInterval);
				//加月份后，可能日期会无效，如4月31日这种无效日期。而Calendar会自动适应为4月30日，所以这里增加判断，以跳过这个无效日期，即跳过这个月
				if(compareCal.get(Calendar.DAY_OF_MONTH) != cal.get(Calendar.DAY_OF_MONTH)){
					//日期无效，跳过这个月
					cal.add(Calendar.MONTH, runInterval+1);
				}else{
					// 运行日期加上间隔月数
					cal.add(Calendar.MONTH, runInterval);
				}
			}
			//单位为小时
			else if(intervalUnit == 3)
			{
				// 运行时间加上间隔小时数
				cal.add(Calendar.HOUR_OF_DAY, runInterval);
			}
			//其他未定义的单位，则默认为天
			else
			{
				// 运行时间加上间隔天数
				cal.add(Calendar.DAY_OF_MONTH, runInterval);
			}
			return cal.getTimeInMillis();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "运行定时任务，计算执行日期时间失败。");
			return null;
		}
	}
	
	/**
	 * 运行前更新数据库
	 * 
	 * @param lfTimer
	 *            任务对象
	 * @param lfTimerHistory
	 *            任务历史
	 * @return 成功true
	 */
	private boolean updateBeforeRun(LfTimer lfTimer, LfTimerHistory lfTimerHistory, TimerErrorCode errorCode)
	{
		if(lfTimer == null){
			EmpExecutionContext.error("定时器执行任务，执行前更新失败，任务对象为null。");
			return false;
		}
		
		try
		{
			// 设置上次执行时间
			lfTimer.setPreTime(new Timestamp(System.currentTimeMillis()));
			// 运行次数加一
			lfTimer.setRunCount(lfTimer.getRunCount() + 1);

			if (lfTimer.getRunInterval() == null || lfTimer.getRunInterval() == 0)
			{
				// 如果运行间隔为零则为执行一次(执行前更新状态)，所以状态设置为停止
				lfTimer.setRunState(0);
			}
		} catch (Exception e)
		{
			errorCode.setErrorCode(IErrorCode.E_DS_B200);
			EmpExecutionContext.error(e, "运行前更新定时任务状态异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}

		Connection conn = empTransDao.getConnection();
		try
		{
			if (conn == null || conn.isClosed())
			{
				errorCode.setErrorCode(IErrorCode.E_DS_D300);
				EmpExecutionContext.error("定时器执行任务，执行前更新任务，获取数据库连接异常。"+"，任务id:"+lfTimer.getTimerTaskId());
				return false;
			}

			empTransDao.beginTransaction(conn);

			// 更新LfTimer
			boolean resultUpdate = this.updateTimerTaskBeforeRun(conn, lfTimer, errorCode);
			if(resultUpdate){
				EmpExecutionContext.info("定时器执行任务，更新数据库中的定时任务信息成功。"+"，任务id:"+lfTimer.getTimerTaskId());
			}
			else{
				//失败回滚
				empTransDao.rollBackTransaction(conn);
				
				EmpExecutionContext.error("定时器执行任务，更新数据库中的定时任务信息失败。"+"，任务id:"+lfTimer.getTimerTaskId());
				return false;
			}

			// 添加LfTimerHistory
			boolean resultAdd = this.addTimerHistory(conn, lfTimer, lfTimerHistory, errorCode);
			if(resultAdd){
				EmpExecutionContext.info("定时器执行任务，保存定时任务历史记录成功。"+"任务id:"+lfTimer.getTimerTaskId());
			}
			else{
				//失败回滚
				empTransDao.rollBackTransaction(conn);
				
				EmpExecutionContext.error("定时器执行任务，保存定时任务历史记录失败。"+"任务id:"+lfTimer.getTimerTaskId());
				return false;
			}
			
			//提交事务
			empTransDao.commitTransaction(conn);
			return true;
			
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			
			errorCode.setErrorCode(IErrorCode.E_DS_B200);
			EmpExecutionContext.error(e, "定时器执行任务，更新数据库中的定时任务信息异常。"+"任务id:"+lfTimer.getTimerTaskId());
			return false;
		} finally
		{
			//关闭连接
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 定时任务执行前更新
	 * @param conn
	 * @param lfTimer
	 * @return
	 */
	private boolean updateTimerTaskBeforeRun(Connection conn,
			LfTimer lfTimer, TimerErrorCode errorCode)
	{
		//任务对象不能为空
		if (lfTimer == null)
		{
			EmpExecutionContext.error("运行前更新任务失败，定时任务对象为空。");
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			return false;
		} else if (conn == null)
		{
			//数据库连接不为空
			EmpExecutionContext.error("运行前更新任务失败，数据库连接为空。"+"，任务id:"+lfTimer.getTimerTaskId());
			errorCode.setErrorCode(IErrorCode.E_DS_D300);
			return false;
		}

		try
		{
			//更新对象
			LinkedHashMap<String, Object> objectMap = new LinkedHashMap<String, Object>();
			//设置更新对象
			objectMap.put("runState", lfTimer.getRunState());
			objectMap.put("preTime", lfTimer.getPreTime());
			objectMap.put("nextTime", lfTimer.getNextTime());
			objectMap.put("runCount", lfTimer.getRunCount());
			objectMap.put("taskErrorCode", lfTimer.getTaskErrorCode());
			
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//update时带上"运行中"的状态
			conditionMap.put("runState", "1");
			
			//执行更新
			boolean result = empTransDao.update(conn, LfTimer.class,
					objectMap, lfTimer.getTimerTaskId().toString(), conditionMap);
			
			//不成功，则设置编码
			if(!result){
				errorCode.setErrorCode(IErrorCode.E_DS_D600);
			}
			
			return result;
		} catch (Exception e)
		{
			//异常处理
			errorCode.setErrorCode(IErrorCode.E_DS_D200);
			EmpExecutionContext.error(e, "定时任务执行前更新异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
	/**
	 * 添加定时任务历史记录
	 * 
	 * @param conn
	 *            数据库连接
	 * @param lfTimer
	 *            定时任务对象
	 * @return
	 * @throws Exception
	 */
	private boolean addTimerHistory(Connection conn, LfTimer lfTimer,
			LfTimerHistory timerHistory, TimerErrorCode errorCode)
	{
		//数据库连接不能为空
		if (conn == null)
		{
			errorCode.setErrorCode(IErrorCode.E_DS_D300);
			EmpExecutionContext.error("添加定时任务历史记录失败，数据库连接为空。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		} else if (lfTimer == null)
		{
			//任务对象不能为空
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			EmpExecutionContext.error("添加定时任务历史记录失败，定时任务对象为空。");
			return false;
		} else if (timerHistory == null)
		{
			//任务历史不能为空
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			EmpExecutionContext.error("添加定时任务历史记录失败，定时历史任务对象为空。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}

		try
		{
			timerHistory.setTimerTaskId(lfTimer.getTimerTaskId());
			timerHistory.setRunTime(lfTimer.getPreTime());

			Calendar calRunTime = Calendar.getInstance();
			calRunTime.clear();
			calRunTime.setTimeInMillis(lfTimer.getPreTime().getTime());
			timerHistory.setRunYear(calRunTime.get(Calendar.YEAR));
			timerHistory.setRunMonth(calRunTime.get(Calendar.MONTH) + 1);
			timerHistory.setRunDay(calRunTime.get(Calendar.DAY_OF_MONTH));
			timerHistory.setTaskExpression(lfTimer.getTaskExpression());
			//执行结果（0-失败 ；1-成功； 2-等待执行结果；4-过期未执行）
			timerHistory.setRunResult(2);
			timerHistory.setTaskErrorCode(lfTimer.getTaskErrorCode());
			
			timerHistory.setTimerTaskName(lfTimer.getTimerTaskName());
			timerHistory.setStartTime(lfTimer.getStartTime());
			timerHistory.setPreTime(lfTimer.getPreTime());
			timerHistory.setRunInterval(lfTimer.getRunInterval());
			timerHistory.setFailRetryTime(lfTimer.getFailRetryTime());
			timerHistory.setFailRetryCount(lfTimer.getFailRetryCount());
			timerHistory.setRunCount(lfTimer.getRunCount());
			timerHistory.setRunPerCount(lfTimer.getRunPerCount());
			timerHistory.setClassName(lfTimer.getClassName());
			timerHistory.setCustomInterval(lfTimer.getCustomInterval());
			timerHistory.setIsRerun(lfTimer.getIsRerun());
			timerHistory.setRerunCount(lfTimer.getRerunCount());
			timerHistory.setIntervalUnit(lfTimer.getIntervalUnit());

			//保存历史到数据库
			Long taId = empTransDao.saveObjectReturnID(conn, timerHistory);
			//保存成功
			if (taId > 0)
			{
				//把返回的id设置到对象
				timerHistory.setTaId(taId);
				return true;
			} else
			{
				errorCode.setErrorCode(IErrorCode.E_DS_D400);
				//保存失败
				return false;
			}
			
		} catch (Exception e)
		{
			//处理异常
			errorCode.setErrorCode(IErrorCode.E_DS_D400);
			EmpExecutionContext.error(e, "添加定时任务历史记录异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
	/**
	 * 检查业务类是否存在
	 * 
	 * @param className业务类路径
	 * @return 成功true boolean
	 */
	private boolean checkClassName(String className, TimerErrorCode errorCode)
	{
		//类名不能为空
		if (className == null || "".equals(className.trim()))
		{
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			EmpExecutionContext.error("定时器执行任务前检查，类路径为空。");
			return false;
		}
		
		try
		{
			//反射初始化类
			Class<MontnetTimerTask> c = (Class<MontnetTimerTask>) Class
					.forName(className);
			//初始化失败
			if (c != null)
			{
				return true;
			}
			return false;
		} catch (Exception e)
		{
			//异常处理
			errorCode.setErrorCode(IErrorCode.E_DS_B300);
			EmpExecutionContext.error(e, "定时器执行任务，检查业务类是否存在异常。");
			return false;
		}
	}
	
	/**
	 * 定时器执行后更新
	 * @param mtt
	 * @return
	 */
	private boolean updateTimerTaskAfterRun(LfTimer lfTimer,
			LfTimerHistory lfTimerHistory, TimerErrorCode errorCode)
	{
		//定时任务不能为空
		if (lfTimer == null)
		{
			EmpExecutionContext.error("运行后更新任务历史失败，任务对象为空！");
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			return false;
		} else if (lfTimerHistory == null)
		{
			//定时任务历史不能为空
			EmpExecutionContext.error("运行后更新任务历史失败，定时任务历史对象为空。"+"，任务id:"+lfTimer.getTimerTaskId());
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			return false;
		} else if (lfTimerHistory.getTaId() == null)
		{
			//自增id不能为空
			EmpExecutionContext.error("运行后更新任务历史失败，定时任务历史Id为空。"+"，任务id:"+lfTimer.getTimerTaskId());
			errorCode.setErrorCode(IErrorCode.E_DS_B100);
			return false;
		}
		
		Connection conn = empTransDao.getConnection();
		try
		{
			empTransDao.beginTransaction(conn);
			
			//更新对象
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			//条件
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			
			if (lfTimer.getResult() != null)
			{
				objectMap.put("runResult", lfTimer.getResult().toString());
			}

			if (lfTimer.getTaskErrorCode() != null)
			{
				objectMap.put("taskErrorCode", lfTimer.getTaskErrorCode());
			}

			conditionMap.put("taId", lfTimerHistory.getTaId().toString());
			
			boolean result = empTransDao.update(conn, LfTimerHistory.class, objectMap, conditionMap);
			if(result){
				EmpExecutionContext.info("运行后更新任务历史成功。任务id："+lfTimer.getTimerTaskId());
			}
			else{
				empTransDao.rollBackTransaction(conn);
				EmpExecutionContext.error("运行后更新任务历史失败。任务id："+lfTimer.getTimerTaskId());
				return false;
			}
			
			//状态为停止，且只运行一次的，则删除
			if(lfTimer.getRunState() !=null && lfTimer.getRunState() == 0 && lfTimer.getRunInterval() != null && lfTimer.getRunInterval() == 0){
				int delCount = empTransDao.delete(conn, LfTimer.class, lfTimer.getTimerTaskId().toString());
				EmpExecutionContext.info("运行后更新任务历史，删除任务记录数："+delCount+"。" +
						"任务id="+lfTimer.getTimerTaskId()+
						"，任务表达式="+lfTimer.getTaskExpression()+
						"，任务结果="+lfTimer.getResult()+
						"，错误码="+lfTimer.getTaskErrorCode()
						);
			}
			
			empTransDao.commitTransaction(conn);
			
			return result;
		} catch (Exception e)
		{
			empTransDao.rollBackTransaction(conn);
			//异常处理
			errorCode.setErrorCode(IErrorCode.E_DS_D201);
			EmpExecutionContext.error(e, "运行后更新任务历史异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
		finally{
			empTransDao.closeConnection(conn);
		}
	}
	
	/**
	 * 更新lftimer，这方法由于在catch里调用，要带上trycatch
	 * @param lfTimer
	 * @return
	 */
	private boolean updateLfTimer(LfTimer lfTimer){
		try
		{
			//执行更新
			return empDao.update(lfTimer);
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "定时器执行定时任务，更新lftimer异常。"+"，任务id:"+lfTimer.getTimerTaskId());
			return false;
		}
	}
	
}
