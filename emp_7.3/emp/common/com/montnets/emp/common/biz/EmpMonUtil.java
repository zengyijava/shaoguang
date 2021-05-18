package com.montnets.emp.common.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;
import org.json.simple.JSONObject;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.IGenericDAO;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.gateway.LfSpFee;
import com.montnets.emp.entity.system.LfGlobalVariable;
import com.montnets.emp.util.TxtFileUtil;

public class EmpMonUtil extends SuperBiz
{
	//报文内容格式化时间
	SimpleDateFormat sdf_fileCont = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	//报文文件路径
	SimpleDateFormat sdf_path = new SimpleDateFormat("yyyyMMdd");
	//服务信息BIZ
	MonEmpProceBiz monEmpProce = new MonEmpProceBiz();
	//换行符
	String newline = System.getProperties().getProperty("line.separator");
	
	IGenericDAO genericDAO = new DataAccessDriver().getGenericDAO();
	
	SuperDAO superDAO = new SuperDAO();

	
	/**
	 * 设置EMP服务器监控报文信息
	 * @description    
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-12 下午07:22:51
	 */
	@SuppressWarnings("unchecked")
	public JSONObject setEmpServerMonInfo()
	{
		try
		{
			//报文内容
			JSONObject json = new JSONObject();
			//CPU使用率
			json.put("CPU", "0");
			//内存使用率
			long memUsage = monEmpProce.getProceMemUsage();
			//超过总内存阀值
			if(memUsage > (long)(StaticValue.getMemoryInfo()[0] * StaticValue.getMemoryPercentage()))
			{
				//第一次超过
				if(StaticValue.getMemoryInfo()[1] == 0)
				{
					//记录首次超过阀值
					StaticValue.getMemoryInfo()[1] = System.currentTimeMillis();
					//设置内存
					memUsage = (long)(StaticValue.getMemoryInfo()[0] * (StaticValue.getMemoryPercentage() - StaticValue.getMemoryPercentageValue()));
				}
				else
				{
					//当前内存占用大于总内存阀值小于5分钟
					if(System.currentTimeMillis() - StaticValue.getMemoryInfo()[1] <= StaticValue.getExceedMemoryTime())
					{
						//设置内存
						memUsage = (long)(StaticValue.getMemoryInfo()[0] * (StaticValue.getMemoryPercentage() - StaticValue.getMemoryPercentageValue()));
					}
				}
			}
			else
			{
				//初始化首次超过阀值时间为0
				StaticValue.getMemoryInfo()[1] = 0L;
			}
			json.put("MEM", String.valueOf(memUsage));
			//虚拟内存使用率
			json.put("VMEM", String.valueOf(monEmpProce.getProceVmemUsage()));
			//硬盘使用率
			json.put("DISKFREE", String.valueOf(monEmpProce.getProceDiskFree()));
			//状态，0正常；1警告；2严重
			json.put("STATUS", "0");
			//进程工作状态描述
			json.put("MSG", "");
			return json;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置EMP服务器监控周期信息报文信息异常！");
			return null;
		}
	}
	
	/**
	 * 设置告警报文信息
	 * @description    
	 * @param modcls
	 * @param who
	 * @param value
	 * @param msg
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-11 下午06:53:48
	 */
	@SuppressWarnings("unchecked")
	public JSONObject setAlarmInfo(String modcls, String who, String value, String msg)
	{
		try
		{
			//报文内容
			JSONObject json = new JSONObject();
			//告警点编号
			json.put("MODCLS", modcls);
			//事件抛出者
			json.put("WHO", "EMP");
			//具体的错误编码
			json.put("VALUE", value);
			//异常信息
			json.put("MSG", msg);
			return json;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置告警报文信息异常！");
			return null;
		}
	}
	
	/**
	 * 设置EMP服务器状态监控报文信息
	 * @description    
	 * @param status 程序启动状态，0成功，1失败
	 * @param info 事件描述
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-12 下午07:31:50
	 */
	@SuppressWarnings("unchecked")
	public JSONObject setEmpServerStatusMonInfo(String status, String info)
	{
		try
		{
			//报文内容
			JSONObject json = new JSONObject();
			//程序启动状态，0成功，1失败
			json.put("STATUS", status);
			//程序版本号
			json.put("VER", StaticValue.getEmpVersion());
			//程序路径
			json.put("PATH", new TxtFileUtil().getWebRoot());
			//事件描述
			json.put("INFO", info);
			return json;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置EMP服务器状态监控报文信息异常！");
			return null;
		}
	}
	
	/**
	 * 发送EMP服务器状态报文
	 * @description    
	 * @param status  程序启动状态，0成功，1失败
	 * @param info    事件描述
	 * @param type    类型，启动(19)关闭(10)    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-12 下午07:43:01
	 */
	public void sendEmpServerStatusMessage(String status, String info, String type)
	{
		//报文内容
		JSONObject json = setEmpServerStatusMonInfo(status, info);
		//报文时间
		String evtTm = sdf_fileCont.format(System.currentTimeMillis());
		
		//设置报文信息
		String message = setMessage(json, evtTm, "1300", type);
		if(message != null)
		{
			//写入文件
			writeMonFile(message, type);
		}
		else
		{
			EmpExecutionContext.error("设置EMP服务器状态监控报文信息失败，message为null");
		}
	}
	
	/**
	 * 设置报文信息
	 * @description    
	 * @param json 报文内容
	 * @param evtTm 时间
	 * @param evtId 事件ID
	 * @param type  报文类型
	 * @return   报文信息    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-10 下午04:23:45
	 */
	public String setMessage(JSONObject json, String evtTm, String evtId, String type)
	{
		try
		{
			StringBuffer message = new StringBuffer();
			message.append("<EVENT>")
					.append("<EVTID>").append(evtId).append("</EVTID>")
					.append("<EVTTYPE>").append(type).append("</EVTTYPE>")
					.append("<EVTTM>").append(evtTm).append("</EVTTM>")
					.append("<EVTCONT>").append(json).append("</EVTCONT>")
					.append("</EVENT>");
			return message.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "设置EMP服务器监控报文信息异常！json:"+json.toString()+"，evtTm:"+json+"，type:"+json);
			return null;
		}
	}
	
	/**
	 * 写监控报文信息文件
	 * @description    
	 * @param message 监控报文
	 * @param type   报文类型    			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-10 下午04:31:27
	 */
	public void writeMonFile(String message, String type)
	{
		FileOutputStream out = null;
		try
		{
			//文件存放地址
			String filePath = new TxtFileUtil().getWebRoot()+"logger/mon_data/"+sdf_path.format(System.currentTimeMillis());
			File path = new File(filePath);
			if(!path.exists())
			{
				path.mkdirs();
			}
			//类型为19，写入10文件
			if("19".equals(type))
			{
				type = "10";
			}
			//文件名
			File file = new File(filePath+"/"+type+"_mon_log.txt");
			if(!file.exists())
			{
				boolean success = file.createNewFile();
				if(!success){
					EmpExecutionContext.error("创建文件失败");
				}
			}
			out = new FileOutputStream(file, true);
			out.write(message.getBytes());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "写监控报文信息文件异常，message:"+message+"，type:"+type);
		}finally
		{
			if(out != null)
			{
				try
				{
					out.close();
				}
				catch (Exception e)
				{
					EmpExecutionContext.error(e, "写监控报文信息文件关闭输出流异常！");
				}

			}
		}
	}
	
	/**
	 * 获取报文时间
	 * @description    
	 * @return  报文时间     			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-12 下午06:45:30
	 */
	public String getEvtTm()
	{
		try
		{
			//当前时间
			Calendar cal = Calendar.getInstance();
			int second = cal.get(Calendar.SECOND);
			//设置秒周期时间
			cal.set(Calendar.SECOND, second - (second % ScheduledEmpMonBiz.delay));
			return sdf_fileCont.format(cal.getTimeInMillis());
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP服务器周期信息监控报文时间异常！");
			return null;
		}
	}
	
	/**
	 * 获取失败账号列表
	 * @description    
	 * @param accounts
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 上午10:05:00
	 */
	public List<LfSpFee> getFailSpUserList(String accounts)
	{
		//对象列表
		List<LfSpFee> spFeeList = new ArrayList<LfSpFee>();
		try
		{
			String sql = "SELECT * FROM LF_SPFEE WHERE SP_USER IN ("+accounts+")";
			List<DynaBean> dynaBeanList = genericDAO.findDynaBeanBySql(sql);
			if(dynaBeanList == null || dynaBeanList.size() < 1)
			{
				EmpExecutionContext.error("根据账号获取运营商余额表记录为空，accounts:"+accounts);
				return null;
			}
			//余额对象
			LfSpFee spFee = null;
			for(DynaBean dynaBean:dynaBeanList)
			{
				spFee = new LfSpFee();
				if(dynaBean.get("sp_user") == null || dynaBean.get("sp_userpassword") == null)
				{
					EmpExecutionContext.error("运营商余额表记录账号或密码为空，spUser:"+dynaBean.get("sp_user")+"，pwd:"+dynaBean.get("sp_userpassword"));
					continue;
				}
				spFee.setSfId(Long.parseLong(dynaBean.get("sf_id").toString()));
				spFee.setSpUser(dynaBean.get("sp_user").toString());
				spFee.setSpUserpassword(dynaBean.get("sp_userpassword").toString());
				spFee.setAccountType(Integer.parseInt(dynaBean.get("accounttype").toString()));
				spFee.setSpFeeUrl(dynaBean.get("spfee_url").toString());
				spFeeList.add(spFee);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取运营商余额失败账号列表失败，accounts:"+accounts);
		}
		return spFeeList;
	}
	
	/**
	 * 更新运营商余额查询结果
	 * @description    
	 * @param spFee       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-1-12 下午12:24:10
	 */
	public boolean updateSpFee(LfSpFee spFee)
	{
		//更新数据库
		try
		{
			//更新失败，记录日志
			if(!empDao.update(spFee))
			{
				EmpExecutionContext.error("更新运营商余额查询结果失败，spuser:"+spFee.getSpUser());
				return false;
			}
			return true;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新运营商余额查询结果异常，spuser:"+spFee.getSpUser());
			return false;
		}
	}
	
	/**
	 * 获取EMP程序数据库连接状态
	 * 
	 * @description
	 * @return
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2015-11-24 上午09:50:18
	 */
	public Integer getDbConnectState()
	{
		// 数据库连接状态，0：开启；1：关闭
		int dbConnectState = 1;

		try
		{
			// 查询数据库
			List<LfGlobalVariable> lfGlobalVariable = empDao.findListByCondition(LfGlobalVariable.class, null, null);
			
			if(lfGlobalVariable != null && lfGlobalVariable.size() > 0)
			{
				// 设置数据库连接状态为0：开启
				dbConnectState = 0;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取EMP程序数据库连接异常！");
		}

		return dbConnectState;
	}
	
	/**
	 * 根据userId获取账号信息
	 * @description    
	 * @param userId
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2017-5-25 下午04:26:45
	 */
	public List<DynaBean> getUserDataInfo(String userId)
	{
		try
		{
			String sql = "SELECT * FROM USERDATA WHERE USERID='"+userId+"'";
			return superDAO.getListDynaBeanBySql(sql);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "根据userId获取账号信息异常，userId:"+userId);
			return null;
		}
	}
}
