package com.montnets.emp.mondetails.biz;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonErr;
import com.montnets.emp.entity.monitor.LfMonErrhis;
import com.montnets.emp.mondetails.biz.i.IErrMonBiz;
import com.montnets.emp.mondetails.dao.ErrMonDAO;
import com.montnets.emp.monitor.biz.MonErrorInfo;
import com.montnets.emp.monitor.constant.MonitorStaticValue;
import com.montnets.emp.util.PageInfo;

/**
 * 实时告警监控详情biz
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-2 上午11:06:36
 */
public class ErrMonBiz extends SuperBiz implements IErrMonBiz
{

	private MonErrorInfo			monErrorInfo	= new MonErrorInfo();
	/**
	 * 实时告警监控详情查询
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-12-2 上午11:09:52
	 */
	public List<DynaBean> getErrMon(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		
		return new ErrMonDAO().getErrMon(pageInfo, conditionMap);
	}
	
	/**
	 * 告警处理
	 * @description    
	 * @param userid 用户名
	 * @param id 告警信息id
	 * @param dealflag 处理状态
	 * @param dealdesc 描述
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-1-12 下午04:46:10
	 */
	public boolean dealMon(String username,String ids,String dealflag, String dealdesc)
	{
		//获取数据库连接
		try {
			//状态为未处理，就不用移到历史表中
			if(!"3".equals(dealflag))
			{
				LfMonErr err = new LfMonErr();
				err.setDealflag(Integer.parseInt(dealflag));
				err.setDealdesc(dealdesc);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("id", ids);
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				objectMap.put("dealdesc", dealdesc);
				objectMap.put("dealflag", dealflag);
				objectMap.put("dealpeople", username);
				objectMap.put("crttime", df.format(new Date()));
				empDao.update(LfMonErr.class, objectMap, conditionMap);
				/*//同步处理缓存数据
				if(ids !=null && ids.length()>0)
				{
					if(ids.indexOf(",") > -1)
					{
						String[] idStr = ids.split(",");
						for(String id:idStr)
						{
							if(MonitorStaticValue.monError.containsKey(Long.valueOf(id)))
							{
								MonitorStaticValue.monError.get(Long.valueOf(id)).setDealflag(Integer.valueOf(dealflag));
							}
						}
					}
					else
					{
						MonitorStaticValue.monError.get(Long.valueOf(ids)).setDealflag(Integer.valueOf(dealflag));
					}
				}*/
				
				//修改告警阀值状态,-1为处理中,0为新事件
				int monStatus = 0;
				if("2".equals(dealflag))
				{
					monStatus = -1;
				}
				err = null;
				LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
				conMap.put("id&in", ids);
				List<LfMonErr> errList = empDao.findListBySymbolsCondition(LfMonErr.class, conMap, null);
				Iterator<LfMonErr> itr = errList.iterator();
				while(itr.hasNext())
				{
					err = itr.next();
					//处理阀值标示
					//主机告警
					if(err.getApptype()==3000)
					{
						monErrorInfo.setAlarmSmsFlag(1, err.getHostid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
						monErrorInfo.setAlarmSmsFlag(5, err.getHostid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
					}
					//emp程序或文件服务器
					else if(err.getApptype()==5000 || err.getApptype()==5800)
					{
						monErrorInfo.setAlarmSmsFlag(2, err.getProceid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
						monErrorInfo.setAlarmSmsFlag(6, err.getProceid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
					}
					//wbs程序
					else if(err.getApptype()==5200)
					{
						monErrorInfo.setAlarmSmsFlag(2, err.getProceid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
						monErrorInfo.setAlarmSmsFlag(6, err.getProceid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
					}
					//spgate程序
					else if(err.getApptype()==5300)
					{
						monErrorInfo.setAlarmSmsFlag(2, err.getProceid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
						monErrorInfo.setAlarmSmsFlag(6, err.getProceid(), String.valueOf(err.getMonthreshold()), monStatus, false, null);
					}
					//sp账号
					else if(err.getApptype()==5400)
					{
						monErrorInfo.setAlarmSmsFlag(3, null, String.valueOf(err.getMonthreshold()), monStatus, false, err.getSpaccountid());
						monErrorInfo.setAlarmSmsFlag(7, null, String.valueOf(err.getMonthreshold()), monStatus, false, err.getSpaccountid());
					}
					//通道账号
					else if(err.getApptype()==5500)
					{
						monErrorInfo.setAlarmSmsFlag(4, null, String.valueOf(err.getMonthreshold()), monStatus, false, err.getGateaccount());
						monErrorInfo.setAlarmSmsFlag(8, null, String.valueOf(err.getMonthreshold()), monStatus, false, err.getGateaccount());
					}
					//在线用户数
					else if(err.getApptype()==5700)
					{
						monErrorInfo.setOnlineCfg(monStatus);
					}
					//数据库监控
					else if(err.getApptype()==5100)
					{
						monErrorInfo.setAlarmFlag(10, err.getProcenode(), err.getApptype(), err.getWebnode(), String.valueOf(err.getMonthreshold()), monStatus);
					}
					//主机网络
					else if(err.getApptype()==3100)
					{
						monErrorInfo.setAlarmFlag(20, err.getProcenode(), err.getApptype(), err.getWebnode(), String.valueOf(err.getMonthreshold()), monStatus);
					}
				}
				return true;
			}
			else
			{
				
				Connection conn = empTransDao.getConnection();
				try
				{
					LinkedHashMap<String, String> conMap = new LinkedHashMap<String, String>();
					conMap.put("id&in", ids);
					List<LfMonErr> errs = empDao.findListBySymbolsCondition(LfMonErr.class, conMap, null);
					empTransDao.beginTransaction(conn);
					List<LfMonErrhis> errList = new ArrayList<LfMonErrhis>();
					for(int i=0;i<errs.size();i++)
					{
						LfMonErr err = errs.get(i);
						LfMonErrhis errhis = new LfMonErrhis();
						errhis.setApptype(err.getApptype());
						errhis.setCrttime(new Timestamp(System.currentTimeMillis()));
						errhis.setDealflag(3);
						errhis.setEvtid(err.getEvtid());
						errhis.setEvttime(err.getEvttime());
						errhis.setEvttype(err.getEvttype());
						errhis.setHostid(err.getHostid());
						errhis.setMonstatus(err.getMonstatus());
						errhis.setMsg(err.getMsg());
						errhis.setProceid(err.getProceid());
						errhis.setRcvtime(err.getRcvtime());
						errhis.setWho(err.getWho());
						errhis.setSpaccountid(err.getSpaccountid());
						errhis.setGateaccount(err.getGateaccount());
						errhis.setDealflag(Integer.parseInt(dealflag));
						errhis.setDealdesc(dealdesc);
						errhis.setDealpeople(username);
						errhis.setMontimer(err.getMontimer());
						errhis.setProcenode(err.getProcenode());
						errhis.setWebnode(err.getWebnode());
						errList.add(errhis);
						//处理阀值标示
						//主机告警
						if(err.getApptype()==3000)
						{
							monErrorInfo.setAlarmSmsFlag(1, err.getHostid(), String.valueOf(err.getMonthreshold()), 0, false, null);
							monErrorInfo.setAlarmSmsFlag(5, err.getHostid(), String.valueOf(err.getMonthreshold()), 0, false, null);
						}
						//emp程序
						else if(err.getApptype()==5000 || err.getApptype()==5800)
						{
							monErrorInfo.setAlarmSmsFlag(2, err.getProceid(), String.valueOf(err.getMonthreshold()), 0, false, null);
							monErrorInfo.setAlarmSmsFlag(6, err.getProceid(), String.valueOf(err.getMonthreshold()), 0, false, null);
						}
						//wbs程序
						else if(err.getApptype()==5200)
						{
							monErrorInfo.setAlarmSmsFlag(2, err.getProceid(), String.valueOf(err.getMonthreshold()), 0, false, null);
							monErrorInfo.setAlarmSmsFlag(6, err.getProceid(), String.valueOf(err.getMonthreshold()), 0, false, null);
						}
						//spgate程序
						else if(err.getApptype()==5300)
						{
							monErrorInfo.setAlarmSmsFlag(2, err.getProceid(), String.valueOf(err.getMonthreshold()), 0, false, null);
							monErrorInfo.setAlarmSmsFlag(6, err.getProceid(), String.valueOf(err.getMonthreshold()), 0, false, null);
						}
						//通道账号
						else if(err.getApptype()==5500)
						{
							monErrorInfo.setAlarmSmsFlag(4, null, String.valueOf(err.getMonthreshold()), 0, false, err.getGateaccount());
							monErrorInfo.setAlarmSmsFlag(8, null, String.valueOf(err.getMonthreshold()), 0, false, err.getGateaccount());
						}
						//sp账号
						else if(err.getApptype()==5400)
						{
							monErrorInfo.setAlarmSmsFlag(3, null, String.valueOf(err.getMonthreshold()), 0, false, err.getSpaccountid());
							monErrorInfo.setAlarmSmsFlag(7, null, String.valueOf(err.getMonthreshold()), 0, false, err.getSpaccountid());
						}
						//在线用户数
						else if(err.getApptype()==5700)
						{
							monErrorInfo.setOnlineCfg(0);
						}
						//数据库监控
						else if(err.getApptype()==5100)
						{
							monErrorInfo.setAlarmFlag(10, err.getProcenode(), err.getApptype(), err.getWebnode(), String.valueOf(err.getMonthreshold()), 0);
						}
						//主机网络
						else if(err.getApptype()==3100)
						{
							monErrorInfo.setAlarmFlag(20, err.getProcenode(), err.getApptype(), err.getWebnode(), String.valueOf(err.getMonthreshold()), 0);
						}
					}
					empTransDao.save(conn, errList, LfMonErrhis.class);
					empTransDao.delete(conn,LfMonErr.class, ids.indexOf(",")>0?ids.substring(0,ids.lastIndexOf(",")):ids);
					empTransDao.commitTransaction(conn);
					//同步处理缓存数据
					for(int i=0;i<errs.size();i++)
					{
						//同步删除缓存
						MonitorStaticValue.getMonError().remove(errs.get(i).getId());
					}
					return true;
				}
				catch (Exception e) {
					empTransDao.rollBackTransaction(conn);
					EmpExecutionContext.error(e, "实时告警处理biz层异常！");
					return false;
				}
				finally
				{
					empTransDao.closeConnection(conn);
				}
			}
		}
		catch (Exception e) {
			EmpExecutionContext.error(e,"实时告警处理biz层异常！");
			return false;
		}
	}
}
