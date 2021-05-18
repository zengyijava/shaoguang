package com.montnets.emp.common.biz;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.entity.LfSumCtrl;
import com.montnets.emp.common.vo.LfSumCtrlVo;

/**
 * 
 * @project emp_std_192.169.1.81
 * @author huangzb
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2014-4-2 上午10:18:43
 * @description 调度biz
 */
public class JobBiz extends SuperBiz 
{
	/**
	 * 是否允许执行网优调度汇总
	 * @return 返回true则允许执行
	 */
	public boolean isExcWYProc(){
		try
		{
			//获取已加载的模块号码
			//String[] strMennNum = StaticValue.menu_num.toString().split(",");
			String[] strMennNum = StaticValue.getMenu_num().toString().split(",");
			for(int i=0;i<strMennNum.length;i++){
				//判断是否加载了网优模块
				if("17".equals(strMennNum[i])){
					//加载了模块，则返回允许执行
					return true;
				}
			}
			//未加载模块，返回不允许
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网优调度判断是否加载网优模块异常。");
			//异常也返回允许执行，避免漏执行
			return true;
		}
	}
	
	
	/**
	 * 汇总集群普通记录 插入or更新
	 * @description    
	 * @param sumtype
	 * 汇总类型  0 晚上  1白天
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-30 上午11:09:02
	 */
	public boolean UpdateSumCtrl(int sumtype)
	{
		try
		{
			//节点id
			String nodeId = StaticValue.getServerNumber();
			//判断节点编号是否正常
			if(nodeId == null || nodeId.trim().length() < 1)
			{
				EmpExecutionContext.error("汇总集群普通记录 插入or更新方法，获取不到节点ID。");
				return false;
			}
			//定义插入普通记录对象
			LfSumCtrl sumctrl = new LfSumCtrl();
			sumctrl.setNodeId(nodeId);
			//普通记录
			sumctrl.setIsMain(0);
			sumctrl.setSumType(sumtype);
			sumctrl.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			//查询普通记录
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("nodeId", nodeId);
			conditionMap.put("isMain", "0");
			conditionMap.put("sumType", sumtype+"");
			List<LfSumCtrl> sumctrlList = empDao.findListByCondition(LfSumCtrl.class, conditionMap, null);
			//有记录，则更新
			if(sumctrlList != null && sumctrlList.size() > 0)
			{
				return updateSumCtrl(sumctrl);
			}
			//没记录则插入
			else
			{
				return empDao.save(sumctrl);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "汇总集群普通记录 插入or更新方法，异常。");
			return false;
		}
	}
	
	
	/**
	 * 更新汇总节点普通数据记录
	 * @description    
	 * @param sumctrl 普通记录对象
	 * @return   
	 * 成功返回true    			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-30 下午01:44:52
	 */
	private boolean updateSumCtrl(LfSumCtrl sumctrl)
	{
		try
		{
			//定义更改map
			LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			objectMap.put("updateTime", sdf.format(new Date()));
			//定义条件map
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("nodeId", sumctrl.getNodeId());
			conditionMap.put("isMain", sumctrl.getIsMain()+"");
			conditionMap.put("sumType", sumctrl.getSumType()+"");
			
			return empDao.update(LfSumCtrl.class, objectMap, conditionMap);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "更新汇总节点普通数据记录，异常。");
			return false;
		}
	}
	
	
	/**
	 * 网关调度汇总是否执行完成
	 * 
	 * @description
	 * @return
	 *         0未完成 1已完成
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2015-12-28 下午02:27:17
	 */
	public String IsDataFilish()
	{
		try
		{
			String sql = "SELECT COUNT(*) icount FROM TRANS_LOG WHERE USETYPE='SMS' AND RUNFLAG=1 AND ";
			// 不同数据库时间查询函数不同大于当前日期
			switch (StaticValue.DBTYPE)
			{
				case 1:
					// oracle
					sql = sql + " CREATETIME>TO_DATE(TO_CHAR(SYSDATE,'YYYY-MM-DD'),'YYYY-MM-DD HH24:MI:SS ') ";
					break;
				case 2:
					// sqlserver2005
					sql = sql + " CREATETIME>CONVERT(VARCHAR(20),GETDATE(),23) ";
					break;
				case 3:
					// MYSQL
					sql = sql + " CREATETIME>CURDATE() ";
					break;
				case 4:
					// DB2
					sql = sql + " CREATETIME>TO_DATE(CHAR(CURRENT DATE),'yyyy-MM-dd') ";
					break;
				default:
					EmpExecutionContext.error("网关调度汇总是否执行完成，获取查询sql，未知数据库类型。DBTYPE=" + StaticValue.DBTYPE);
					return "0";
			}
			// 定义初始值
			String count = "0";
			List<DynaBean> dybcount = new SuperDAO().getListDynaBeanBySql(sql);
			if(dybcount != null && dybcount.size() > 0)
			{
				DynaBean bean = dybcount.get(0);
				if(bean.get("icount") != null)
				{
					count = bean.get("icount").toString();
				}
			}
			return count;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "网关调度汇总是否执行完成执行异常");
			return "0";
		}
	}
	
	/**
	 * 获取本地配置文件中节点编号，将节点编号与本地取出来的节点编号相同、汇总类型为晚上汇总且是主控记录的执行时间更新为当前时间，获取更新条数
	 * @description    
	 * @param sumtype
	 *        汇总类型 0 晚上  1白天
	 * @return   
	 *    有更新则返回true  无更新或更新失败返回false	 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-30 下午02:23:01
	 */
	public boolean UpdateMainRecord(int sumtype)
	{
		boolean result=false;
		try
		{
			//节点id
			String nodeId = StaticValue.getServerNumber();
			if(nodeId == null || nodeId.trim().length() < 1)
			{
				EmpExecutionContext.error("汇总控制记录，获取不到节点ID。");
				return result;
			}
			// 不同数据库兼容
			//当前时间
			String curentdatesql=getCurrentTimeSql();
			if(curentdatesql == null)
			{
				EmpExecutionContext.error("更新主控记录的更新时间，获取不到当前时间sql。");
				return false;
			}
			StringBuffer sql = new StringBuffer("UPDATE LF_SUMCTRL SET UPDATETIME=");
			sql.append(curentdatesql).append(" WHERE ISMAIN=1 AND SUMTYPE=").append(sumtype)
			.append(" AND NODEID='").append(nodeId).append("' ");
			
			//影响行数
			result=new SuperDAO().executeBySQL(sql.toString(), StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取本地配置文件中节点编号，将节点编号与本地取出来的节点编号相同、汇总类型为晚上汇总且是主控记录的执行时间更新为当前时间，获取更新条数失败");
			result=false;
		}
		return result;
	}
	

	
	/**
	 * 主备切换
	 * @description    
	 * @param sumtype
	 * 汇总类型  0晚上  1白天
	 * @param oldnodeid
	 * 旧的主控节点编号
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-31 上午10:11:21
	 */
	public boolean 	ChangeMainCtrl(int sumtype,String oldnodeid)
	{
		boolean result=false;
		try
		{
			//判断传入值的合法性
			if(oldnodeid==null||oldnodeid.trim().length() < 1){
				EmpExecutionContext.error("汇总主备切换，传入的旧主控记录编号为空。");
				return result;
			}
			
			//节点id
			String nodeId = StaticValue.getServerNumber();
			if(nodeId == null || nodeId.trim().length() < 1)
			{
				EmpExecutionContext.error("汇总主备切换，获取不到节点ID。");
				return result;
			}
			
			//判断是否是同一tomcat节点编号
			if(oldnodeid.equals(nodeId)){
				EmpExecutionContext.info("汇总主备切换，节点编号一致");
				return result;
			}
			
			// 不同数据库兼容
			//当前时间
			String curentdatesql=getCurrentTimeSql();
			if(curentdatesql == null)
			{
				EmpExecutionContext.error("执行完定时更新节点数据  如果是主则主辅同时更新时间，获取不到当前时间sql。");
				return false;
			}
			StringBuffer sql = new StringBuffer("UPDATE LF_SUMCTRL SET UPDATETIME=").append(curentdatesql).append(",NODEID='").append(nodeId)
			.append("' WHERE ISMAIN=1 AND SUMTYPE=").append(sumtype).append(" AND NODEID='").append(oldnodeid).append("' ");
			
			//影响行数
			result=new SuperDAO().executeBySQL(sql.toString(), StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "主备切换处理异常");
			result=false;
		}
		return result;
	}
	
	/**
	 * 执行完定时更新节点数据  如果是主则主辅同时更新时间
	 * @description    
	 * @param sumtype
	 *        汇总类型 0 晚上  1白天
	 * @return   
	 *    有更新则返回true  无更新或更新失败返回false	 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-30 下午02:23:01
	 */
	public boolean UpdateRecord(int sumtype)
	{
		boolean result=false;
		try
		{
			//节点id
			String nodeId = StaticValue.getServerNumber();
			if(nodeId == null || nodeId.trim().length() < 1)
			{
				EmpExecutionContext.error("汇总控制记录，获取不到节点ID。");
				return result;
			}
			// 不同数据库兼容
			//当前时间
			String curentdatesql=getCurrentTimeSql();
			if(curentdatesql == null)
			{
				EmpExecutionContext.error("执行完定时更新节点数据  如果是主则主辅同时更新时间，获取不到当前时间sql。");
				return false;
			}
			
			String sql = "UPDATE LF_SUMCTRL SET UPDATETIME="+curentdatesql+" WHERE SUMTYPE="+sumtype+" AND NODEID='"+nodeId+"' ";
			
			//影响行数
			result=new SuperDAO().executeBySQL(sql, StaticValue.EMP_POOLNAME);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "汇总执行完成  更新主辅记录失败");
			result=false;
		}
		return result;
	}

	
	/**
	 * 获得当前数据库时间
	 * @description    
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-30 下午04:28:48
	 */
	private String getCurrentTimeSql()
	{
		try
		{
			String curentdatesql = null;
			switch (StaticValue.DBTYPE)
			{
				case 1:
					// oracle
					curentdatesql = "SYSDATE";
					break;
				case 2:
					// sqlserver2005
					curentdatesql = "GETDATE()";
					break;
				case 3:
					// MYSQL
					curentdatesql = "NOW()";
					break;
				case 4:
					// DB2
					curentdatesql = "CURRENT TIMESTAMP";
					break;
				default:
					EmpExecutionContext.error("汇总更新主辅记录获得未知数据库类型。DBTYPE=" + StaticValue.DBTYPE);
					return null;
			}
			return curentdatesql;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "DAO获取当前时间sql，异常。");
			return null;
		}
	}
	
	
	/**
	 * 获取主控记录
	 * @description    
	 * @param sumtype
	 * 0晚上 1白天
	 * @return       			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-4-1 上午08:34:53
	 */
	public LfSumCtrlVo getLfSumCtrl(int sumtype){

		try
		{
			//当前时间
			String curentdatesql=getCurrentTimeSql();
			if(curentdatesql == null)
			{
				EmpExecutionContext.error("获取主控记录，获取不到当前时间sql。");
				return null;
			}
			StringBuffer sqlSb = new StringBuffer();
			sqlSb.append("SELECT SC.NODEID,SC.UPDATETIME,NB.SERLOCALURL,").append(curentdatesql)
			.append(" as DBCURRENTTIME from LF_SUMCTRL SC ").append(StaticValue.getWITHNOLOCK())
			.append(" INNER JOIN LF_NODEBASEINFO NB ").append(StaticValue.getWITHNOLOCK())
			.append(" ON NB.NODEID=SC.NODEID WHERE SC.ISMAIN=1 AND SC.SUMTYPE=").append(sumtype);
			List<DynaBean> sumctrlList = new SuperDAO().getListDynaBeanBySql(sqlSb.toString());
			if(sumctrlList!=null&& sumctrlList.size()> 0)
			{
				//获取第一条记录
				DynaBean dyn=sumctrlList.get(0);
				if(dyn!=null&&dyn.get("nodeid")!=null&&dyn.get("updatetime")!=null&&dyn.get("serlocalurl")!=null
						&&dyn.get("dbcurrenttime")!=null){
					LfSumCtrlVo sumctrl=new LfSumCtrlVo();
					sumctrl.setDbcurrenttime(Timestamp.valueOf(dyn.get("dbcurrenttime").toString()));
					sumctrl.setUpdateTime(Timestamp.valueOf(dyn.get("updatetime").toString()));
					sumctrl.setNodeId(dyn.get("nodeid").toString());
					sumctrl.setSerLocalURL(dyn.get("serlocalurl").toString());
					return sumctrl;
				}else{
					EmpExecutionContext.error("获取定时服务控制记录数据存在异常。");
					return null;
				}
			}else{
				EmpExecutionContext.error("获取定时服务控制记录，无记录。");
				return null;
			}

		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取汇总控制记录，异常。");
			return null;
		}
	}
	
	/**
	 * 是否超时
	 * @description    
	 * @param sumtype
	 * 0 晚上  1白天
	 * @param dbtime
	 *    数据库当前时间
	 * @param updatetime
	 *    汇总主数据执行时间
	 * @return      
	 *  true为超时  false未超时 			 
	 * @author liuxiangheng <xiaokanrensheng1012@126.com>
	 * @datetime 2016-3-30 下午05:26:48
	 */
	public boolean IsTimeOut(int sumtype,Timestamp dbtime,Timestamp updatetime)
	{
		try
		{
			//获取数据
			if(dbtime != null && updatetime != null)
			{
				long timebeatimes=dbtime.getTime()-updatetime.getTime();
				int interval = 12;
				if(sumtype==1){
					//获取系统配置时间间隔
					String summTimeInterval = SystemGlobals.getSysParam("SummTimeInterval");
					interval=3;
					//设置时间间隔为系统配置
					if(summTimeInterval != null && !"".equals(summTimeInterval))
					{
						interval = Integer.parseInt(summTimeInterval)/2;
					}
				}
				//获得最大间隔的毫秒数
				long bzbeatimes=(long)interval*60*60*1000;
				//如果超过最大间隔毫秒数
				if(timebeatimes-bzbeatimes>0){
					return true;
				}else{
					return false;
				}
			}
			//没记录则插入
			else
			{
				EmpExecutionContext.error("是否超时传入时间参数异常");
				return false;
			}
		
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "汇总控制记录，异常。");
			return false;
		}
	}
	
}
