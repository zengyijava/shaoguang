package com.montnets.emp.rms.degree.listener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.rms.degree.biz.DegreeBiz;
import com.montnets.emp.rms.degree.entity.LfDegree;
import com.montnets.emp.rms.degree.vo.LfDegreeManageVo;

/** 
* @ClassName: DegreeStatusCheckThread 
* @Description: 档位状态检查线程类
* @author zengy2  
* @date 2018-1-24 上午10:22:05 
*  
*/
public class DegreeStatusCheckThread extends Thread {

	public DegreeStatusCheckThread() {
		setName("档位状态检查线程");
	}

	public void run() {
		
		if (StaticValue.ISRUNTIMEJOB == 1)
		{

			TimerTask task = new TimerTask()
			{
				@Override
				public void run()
				{
					try
					{
						// 获得当前时间
						long time = System.currentTimeMillis();
						//查询计费档位信息集合的值对象
						LfDegreeManageVo lfDegreeManageVo = new LfDegreeManageVo();
						//设置查询时档位状态为启用
						lfDegreeManageVo.setStatus(0);
						DegreeBiz degreeBiz = new DegreeBiz();
						
						// 获得所有已启用的计费档位的信息集合
						List<LfDegreeManageVo> LfDegreeManageVoList = degreeBiz.getDegreeBiz(lfDegreeManageVo, "");
						
						//遍历计费档位信息集合的值对象
						LfDegreeManageVo degreeManage = new LfDegreeManageVo();
						//需要修改的档位所在的值对象
						LfDegree lfDegree =new LfDegree();
						
						//日期格式
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						// 判断所获取的集合是否为空
						if (LfDegreeManageVoList!=null&&!LfDegreeManageVoList.isEmpty())
						{
							for (int i = 0; i < LfDegreeManageVoList.size(); i++)
							{
								// 遍历信息
								degreeManage = LfDegreeManageVoList.get(i);
								
								// 获得有效结束时间的long型数据
								long vaildDateEndTime = degreeManage.getValidDateEnd().getTime();
								// 将有效结束时间与当前时间比较，查看是否过期，若过期，改成过期
								if (vaildDateEndTime < time)
								{
									lfDegree = degreeBiz.getById(LfDegree.class, degreeManage.getId());
									//修改为过期
									lfDegree.setStatus(2);
									degreeBiz.updateObj(lfDegree);
								}
							}
						}
					} catch (ParseException e1)
					{
						EmpExecutionContext.error("日期格式解析出错:" + e1.toString());
					} catch (Exception e)
					{
						EmpExecutionContext.error("更新LF_DEGREE 表出错");
					}
				}
			};
			//定时器
			Timer timer = new Timer();
			
			//任务执行时间
			Date date = getDate();
			// 一天
			long period = 24L * 60 * 60 * 1000;
			// 每天的date时刻执行task, 仅执行一次
			timer.schedule(task, date, period);

		}
		
	}

	private Date getDate()
	{
		// 设置执行时间
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);// 每天

		// 每天的3点30开始执行
		int hour = 3;
		int minute = 30;
		String timeStr = SystemGlobals.getValue("database.job.time");
		String hourStr = "3";
		String minuteStr = "30";
		if (timeStr.contains(":"))
		{
			hourStr = timeStr.split(":")[0];
			minuteStr = timeStr.split(":")[1];
		}
		try
		{
			hour = Integer.parseInt(hourStr);
			if ((hour > 23) || (hour < 0))
			{
				hour = 3;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换执行时间(小时)异常，hourStr:" + hourStr);
		}
		try
		{
			minute = Integer.parseInt(minuteStr);
			if ((minute > 59) || (minute < 0))
			{
				minute = 30;
			}
		} catch (Exception e)
		{
			EmpExecutionContext.error(e, "转换执行时间(分钟)异常，minuteStr:" + minuteStr);
		}
		// 获取当前时间的小时和分钟
		int hour2 = calendar.get(Calendar.HOUR_OF_DAY);
		int minute2 = calendar.get(Calendar.MINUTE);

		// 定制每天的3:30:00执行，
		calendar.set(year, month, day, hour, minute, 00);

		// 判断今天是否过了3点30，如果是，则日期加一天
		if (hour2 > hour || hour2 == hour && minute2 >= minute)
		{
			calendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		Date date = calendar.getTime();
		return date;
	}
	
}
