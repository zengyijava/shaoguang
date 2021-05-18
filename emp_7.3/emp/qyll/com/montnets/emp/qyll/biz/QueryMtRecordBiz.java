package com.montnets.emp.qyll.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.accountpower.LfMtPri;
import com.montnets.emp.entity.pasgroup.Userdata;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfDomination;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.qyll.dao.QuertMtRecordRealDao;
import com.montnets.emp.qyll.dao.QueryMtRecordHisDAO;
import com.montnets.emp.qyll.dao.SysMtRecordDAO;
import com.montnets.emp.util.PageInfo;


/** 
* @ClassName: QueryMtRecordBiz 
* @Description: 短信 
* @author xuty  
* @date 2017-11-3 下午5:04:31 
*  
*/
public class QueryMtRecordBiz extends SuperBiz {
	/**
	 * 企业顶级机构map，查询到的企业顶级机构放这里，key为企业编码，value为企业的顶级机构对象。
	 */
	private static ConcurrentHashMap<String,LfDep> topDepMap = new ConcurrentHashMap<String,LfDep>();
	
	/**
	 * 获取下行记录集合
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行记录集合
	 */
	public List<DynaBean> getMtRecords(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			// 企业绑定的发送账户，多企业限制查询范围用到
			String spUsers = getCorpBindSpusers(conditionMap.get("lgcorpcode"));
			//sp账号条件
			conditionMap.put("spUsers", spUsers);
			
			//查询类型
			String recordType = conditionMap.get("recordType");
			//定义查询历史结果集
			List<DynaBean> mtTaskList;
			// 查询历史记录
			if("history".equals(recordType))
			{
				//设置查询条件
				setHisCondition(conditionMap);
				//下行记录dao
				QueryMtRecordHisDAO mtDao = new QueryMtRecordHisDAO();
				// 获取历史记录数据
				mtTaskList = mtDao.findMtTasksHis(conditionMap, pageInfo);
			}
			// 查询实时记录
			else if("realTime".equals(recordType))
			{
				//下行实时记录dao
				QuertMtRecordRealDao mtDao = new QuertMtRecordRealDao();
				//下行实时记录
				mtTaskList = mtDao.findMtTasksReal(conditionMap, pageInfo);
			}
			else
			{
				//类型传递错误
				mtTaskList = null;
			}
			
			return mtTaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取下行记录，异常。");
			return null;
		}
	}
	
	/**
	 * 获取下行记录集合
	 * @description    
	 * @param conditionMap  查询条件集合
	 * @param pageInfo 分页信息对象
	 * @param realDbpageInfo  实时库分页对象
	 * @return 返回下行记录集合
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-22 下午09:46:52
	 */
	public List<DynaBean> getMtRecords(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, PageInfo realDbpageInfo)
	{
		try
		{
			// 企业绑定的发送账户，多企业限制查询范围用到
			String spUsers = getCorpBindSpusers(conditionMap.get("lgcorpcode"));
			//sp账号条件
			conditionMap.put("spUsers", spUsers);
			
			//查询类型
			String recordType = conditionMap.get("recordType");
			//定义查询历史结果集
			List<DynaBean> mtTaskList;
			// 查询历史记录
			if("history".equals(recordType))
			{
				//设置查询条件
				setHisCondition(conditionMap);
				//下行记录dao
				 QueryMtRecordHisDAO mtDao = new QueryMtRecordHisDAO();
				// 获取历史记录数据
				mtTaskList = mtDao.findMtTasksHis(conditionMap, pageInfo, realDbpageInfo);
			}
			// 查询实时记录
			else if("realTime".equals(recordType))
			{
				//下行实时记录dao
				QuertMtRecordRealDao mtDao = new QuertMtRecordRealDao();
				//下行实时记录
				mtTaskList = mtDao.findMtTasksReal(conditionMap, pageInfo);
			}
			else
			{
				//类型传递错误
				mtTaskList = null;
			}
			
			return mtTaskList;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取下行记录，异常。");
			return null;
		}
	}
	
	/**
	 * 获取下行记录分页信息
	 * @param conditionMap 查询条件
	 * @param pageInfo 分页信息对象
	 * @return 返回下行记录集合
	 */
	public boolean getMtRecordsPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo)
	{
		try
		{
			//查询类型
			String recordType = conditionMap.get("recordType");
			// 查询历史记录
			if("history".equals(recordType))
			{
				QueryMtRecordHisDAO mtDao = new QueryMtRecordHisDAO();
				return mtDao.findMtTasksHisPageInfo(conditionMap, pageInfo);
			}
			// 查询实时记录
			else if("realTime".equals(recordType))
			{
				QuertMtRecordRealDao mtDao = new QuertMtRecordRealDao();
				return mtDao.findMtTasksRealPageInfo(conditionMap, pageInfo);
			}
			else
			{
				//未知类型
				return false;
			}
		}
		catch (Exception e)
		{
			//错误日志
			EmpExecutionContext.error(e, "系统下行记录，获取下行记录分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 获取下行记录分页信息
	 * @description    
	 * @param conditionMap  查询条件集合
	 * @param pageInfo 分页信息对象
	 * @param realDbpageInfo  实时库分页对象
	 * @return 返回下行记录集合
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-22 下午09:47:45
	 */
	public boolean getMtRecordsPageInfo(LinkedHashMap<String, String> conditionMap, PageInfo pageInfo, PageInfo realDbpageInfo)
	{
		try
		{
			//查询类型
			String recordType = conditionMap.get("recordType");
			// 查询历史记录
			if("history".equals(recordType))
			{
				QueryMtRecordHisDAO mtDao = new QueryMtRecordHisDAO();
				return mtDao.findMtTasksHisPageInfo(conditionMap, pageInfo, realDbpageInfo);
			}
			// 查询实时记录
			else if("realTime".equals(recordType))
			{
				QuertMtRecordRealDao mtDao = new QuertMtRecordRealDao();
				return mtDao.findMtTasksRealPageInfo(conditionMap, pageInfo);
			}
			else
			{
				//未知类型
				return false;
			}
		}
		catch (Exception e)
		{
			//错误日志
			EmpExecutionContext.error(e, "系统下行记录，获取下行记录分页信息，异常。");
			return false;
		}
	}
	
	/**
	 * 设置下行历史记录查询条件。
	 * @param conditionMap 查询条件map集合
	 */
	private void setHisCondition(LinkedHashMap<String, String> conditionMap)
	{
		try
		{
			//开始时间
			String sendtime = conditionMap.get("sendtime");
			//结束时间
			String recvtime = conditionMap.get("recvtime");
				
			//查询日期格式化：yyyy-MM-dd HH:mm:ss
			SimpleDateFormat sdfSeachTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			//查询时间段都为空
			if((sendtime == null || sendtime.length() == 0) && (recvtime == null || recvtime.length() == 0))
			{
				//开始时间设置为当前月1号0时0分0秒
				Calendar caStartTime = Calendar.getInstance();
				caStartTime.set(Calendar.DAY_OF_MONTH, 1);//设置天
				caStartTime.set(Calendar.HOUR_OF_DAY, 0);//设置小时
				caStartTime.set(Calendar.MINUTE, 0);//设值分钟
				caStartTime.set(Calendar.SECOND, 0);
				caStartTime.set(Calendar.MILLISECOND, 0);
				sendtime = sdfSeachTime.format(caStartTime.getTime());
				
				//结束时间设置为当前月最后一天
				Calendar caEndTime = Calendar.getInstance();
				caEndTime.set(Calendar.DAY_OF_MONTH, caEndTime.getActualMaximum(Calendar.DAY_OF_MONTH)); 
				caEndTime.set(Calendar.HOUR_OF_DAY, 23);
				caEndTime.set(Calendar.MINUTE, 59);
				caEndTime.set(Calendar.SECOND, 59);
				caEndTime.set(Calendar.MILLISECOND, 999);
				recvtime = sdfSeachTime.format(caEndTime.getTime());
			}
			//没开始时间
			else if(sendtime == null || sendtime.length() == 0)
			{
				Date dateEndTime = sdfSeachTime.parse(recvtime);
				//开始时间设置为结束时间月的1号0时0分0秒
				Calendar caStartTime = Calendar.getInstance();
				caStartTime.setTimeInMillis(dateEndTime.getTime());
				caStartTime.set(Calendar.DAY_OF_MONTH, 1);
				caStartTime.set(Calendar.HOUR_OF_DAY, 0);
				caStartTime.set(Calendar.MINUTE, 0);
				caStartTime.set(Calendar.SECOND, 0);
				caStartTime.set(Calendar.MILLISECOND, 0);
				sendtime = sdfSeachTime.format(caStartTime.getTime());
			}
			//没结束时间
			else if(recvtime == null || recvtime.length() == 0)
			{
				Date dateStartTime = sdfSeachTime.parse(sendtime);
				//结束时间设置为开始时间月的最后一天
				Calendar caEndTime = Calendar.getInstance();
				caEndTime.setTimeInMillis(dateStartTime.getTime());
				caEndTime.set(Calendar.DAY_OF_MONTH, caEndTime.getActualMaximum(Calendar.DAY_OF_MONTH)); 
				caEndTime.set(Calendar.HOUR_OF_DAY, 23);
				caEndTime.set(Calendar.MINUTE, 59);
				caEndTime.set(Calendar.SECOND, 59);
				caEndTime.set(Calendar.MILLISECOND, 999);
				recvtime = sdfSeachTime.format(caEndTime.getTime());
			}
			else
			{
				//开始时间和结束时间都有，不用再设置
				return;
			}
			//开始时间
			conditionMap.put("sendtime", sendtime);
			//结束时间
			conditionMap.put("recvtime", recvtime);
				
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，设置下行历史记录查询条件，异常。");
		}
	}
	
	/**
	 * 获取企业绑定发送账号。多企业时会去获取企业绑定发送账号，单企业则会返回null
	 * @param corpCode 企业编码
	 * @return 返回企业绑定的发送账号，格式如sp1,sp2,sp3...。单企业直接返回null
	 */
	public String getCorpBindSpusers(String corpCode)
	{
		//单企业则不需要拿绑定账号
		if(StaticValue.getCORPTYPE() != 1)
		{
			return null;
		}
		if(corpCode == null || corpCode.trim().length() < 1)
		{
			EmpExecutionContext.error("获取企业绑定发送账号，企业编码为空。");
			return null;
		}
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			//不是十万的企业。如果是100000则查询所有企业发送账号
			if(!"100000".equals(corpCode))
			{
				conditionMap.put("corpCode", corpCode);
			}
			
			// 短信账号
			List<LfSpDepBind> lfsp = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
			if(lfsp == null || lfsp.size() < 1)
			{
				EmpExecutionContext.info("下行记录查询，企业未绑定SP账号。corpcode="+corpCode);
				return null;
			}
			
			StringBuffer bufpusers = new StringBuffer();
			for (int i = 0; i < lfsp.size(); i++)
			{
				bufpusers.append("'").append(lfsp.get(i).getSpUser().toUpperCase()).append("'");
				
				if(i < lfsp.size() - 1)
				{
					bufpusers.append(",");
				}
			}
			return bufpusers.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取企业绑定发送账号，异常。corpCode="+corpCode);
			return null;
		}
	}

	/**
	 * 获取发送账号集合
	 * @param corpCode 企业编码
	 * @return 发送账号集合（大写）
	 */
	public List<String> getSpUserList(String corpCode)
	{
		if(corpCode == null || corpCode.trim().length() < 1)
		{
			EmpExecutionContext.error("获取企业发送账号，企业编码为空。");
			return null;
		}
		try
		{
			//发送账号（大写）
			List<String> spUserList = new ArrayList<String>();
			
			//多企业
			if(StaticValue.getCORPTYPE() == 1)
			{
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				//不是十万的企业。如果是100000则查询所有企业发送账号
				if(!"100000".equals(corpCode))
				{
					conditionMap.put("corpCode", corpCode);
				}
				
				List<LfSpDepBind> lfsp = empDao.findListByCondition(LfSpDepBind.class, conditionMap, null);
				if(lfsp == null || lfsp.size() < 1)
				{
					return null;
				}
				
				for (int i = 0; i < lfsp.size(); i++)
				{
					spUserList.add(lfsp.get(i).getSpUser().toUpperCase());
				}
				return spUserList;
			}
			//单企业
			else
			{
				//条件map
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String,String>();
				conditionMap.put("uid&>", "100001");
				//只查询短信发送账号
				conditionMap.put("accouttype", "1");
				conditionMap.put("userType", "0");
				
				//排序map
				LinkedHashMap<String, String> orderMap = new LinkedHashMap<String,String>();
				orderMap.put("userId",StaticValue.ASC);
				
				List<Userdata> usersp = empDao.findListBySymbolsCondition(Userdata.class, conditionMap, orderMap);
				if(usersp == null || usersp.size() < 1)
				{
					return null;
				}
				
				for (int i = 0; i < usersp.size(); i++)
				{
					spUserList.add(usersp.get(i).getUserId().toUpperCase());
				}
				return spUserList;
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "获取企业发送账号，异常。corpCode="+corpCode);
			return null;
		}
	}

	/**
	 * 获取通道号集合
	 * @param corpCode 企业编码
	 * @return 返回通道号动态bean集合
	 */
	public List<DynaBean> getSpgateList(String corpCode)
	{
		SysMtRecordDAO mtDao = new SysMtRecordDAO();
		return mtDao.findSpgateList(corpCode);
	}
	
	/**
	 * 
	 * @description 获取有权限看的操作员编码
	 * @param sysUser 操作员对象
	 * @return 返回有权限看的操作员编码，格式为code1,code2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午03:22:57
	 */
	public String getPermissionUserCode(LfSysuser sysUser)
	{
		if("100000".equals(sysUser.getCorpCode()))
		{
			return "";
		}
		//如果是admin管理员，则默认查全部
		else if("admin".equals(sysUser.getUserName()))
		{
			return "";
		}
		//如果是个人权限，则只能查自己的。权限类型 1：个人权限  2：机构权限
		else if(sysUser.getPermissionType() == 1)
		{
			//返回当前操作员的编码
			return "'" + sysUser.getUserCode() + "'";
		}
		
		//机构权限，则需要查询出当前操作员可管辖的所有操作员。
		try
		{
			//如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
			boolean domIsTopDep = checkDomIsTopDep(sysUser);
			if(domIsTopDep)
			{
				return "";
			}
			
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("userCode", StaticValue.ASC);
			//查询有权限看的操作员
			List<LfSysuser> userList = empDao.findListBySymbolsCondition(sysUser.getUserId(), LfSysuser.class, null, orderbyMap);
			//没找到其他操作员，那就只能看他自己的
			if(userList == null || userList.size() < 1)
			{
				//返回当前操作员的编码
				return "'" + sysUser.getUserCode() + "'"; 
			}
			//操作员编码超过1000，则不使用in方式拼接查询
			if(userList.size() > 1000)
			{
				return null;
			}
			
			StringBuffer sbUserCode = new StringBuffer();
			for(int i = 0;i < userList.size(); i++)
			{
				sbUserCode.append("'").append(userList.get(i).getUserCode()).append("'");
				if(i < userList.size() - 1)
				{
					sbUserCode.append(",");
				}
			}
			
			return sbUserCode.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取有权限看的操作员编码，异常" 
					+ "。userCode=" + sysUser.getUserCode()
					+ ",userId=" + sysUser.getUserId() 
					+ ",userName=" + sysUser.getUserName()
					+ ",corpCode=" + sysUser.getCorpCode()
					);
			//返回当前操作员的编码
			return null;
		}
	}
	
	
	
	/**
	 * 
	 * @description 获取有权限看的SP账号
	 * @param sysUser 操作员对象
	 * @return 返回有权限看的SP账号，格式为spuser1,spuser2；不需要考虑权限则返回空字符串，目前只有100000企业、admin和管辖范围是顶级机构不需要考虑权限；编码超过1000个、异常返回null
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午03:22:57
	 */
	public String getPermissionSpuserMtpri(LfSysuser sysUser)
	{
		if("100000".equals(sysUser.getCorpCode()))
		{
			return "";
		}
		//如果是admin管理员，则默认查全部
		else if("admin".equals(sysUser.getUserName()))
		{
			return "";
		}
		//机构权限，则需要查询出当前操作员可管辖的所有操作员。
		try
		{
			//如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
			boolean domIsTopDep = checkDomIsTopDep(sysUser);
			if(domIsTopDep)
			{
				return "";
			}
			LinkedHashMap<String, String> conditionmap = new LinkedHashMap<String, String>();
			conditionmap.put("userid", sysUser.getUserId()+"");
			conditionmap.put("corpcode", sysUser.getCorpCode());
			//查询有权限看的sp账号
			List<LfMtPri> mtpris = empDao.findListByCondition(LfMtPri.class, conditionmap, null);
			//没找到其他操作员，那就只能看他自己的
			if(mtpris == null || mtpris.size() < 1)
			{
				//返回当前操作员的编码
				return ""; 
			}
			//操作员编码超过1000，则不使用in方式拼接查询
			if(mtpris.size() > 1000)
			{
				return null;
			}
			
			StringBuffer spusers = new StringBuffer();
			for(int i = 0;i < mtpris.size(); i++)
			{
				spusers.append("'").append(mtpris.get(i).getSpuserid()).append("'");
				if(i < mtpris.size() - 1)
				{
					spusers.append(",");
				}
			}
			
			return spusers.toString();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取有权限看的SP账号，异常" 
					+ "。userCode=" + sysUser.getUserCode()
					+ ",userId=" + sysUser.getUserId() 
					+ ",userName=" + sysUser.getUserName()
					+ ",corpCode=" + sysUser.getCorpCode()
					);
			//返回当前操作员的编码
			return null;
		}
	}
	
	
	
	
	/**
	 * 
	 * @description 获取错误码说明map
	 * @param mtTaskList 下行记录对象集合
	 * @param corpCode 企业编码
	 * @return 错误码说明map，key为错误码，value为说明
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-19 上午09:09:11
	 */
	public Map<String,String> getErrCodeDisMap(List<DynaBean> mtTaskList, String corpCode)
	{
		if(mtTaskList == null || mtTaskList.size() < 1)
		{
			EmpExecutionContext.error("系统下行记录，获取错误码说明map，传入记录为空。");
			return null;
		}
		if(corpCode == null || corpCode.length() < 1)
		{
			EmpExecutionContext.error("系统下行记录，获取错误码说明map，传入企业编码为空。");
			return null;
		}
		try
		{
			//错误状态码map，key为错误状态码，value为错误状态码
			Map<String,String> errcodeMap = new HashMap<String,String>();
			
			//状态码
			String stateCode;
			for(DynaBean bean : mtTaskList)
			{
				if(bean.get("errorcode") == null)
				{
					continue;
				}
				stateCode = bean.get("errorcode").toString().trim();
				//成功的跳过
				if("DELIVRD".equals(stateCode) || "0".equals(stateCode))
				{
				    continue;
				}
				//把错误的状态码放入map中
				errcodeMap.put(stateCode, stateCode);
			}
			//没错误状态码
			if(errcodeMap.size() < 1)
			{
				return null;
			}
			
			StringBuffer sbErrCode = new StringBuffer();
			for(String key : errcodeMap.keySet())
			{
				sbErrCode.append("'").append(key).append("'").append(",");
			}
			//删除最后的,
			if(sbErrCode.length() > 0 && sbErrCode.lastIndexOf(",") == sbErrCode.length()-1)
			{
				sbErrCode.deleteCharAt(sbErrCode.length() - 1);
			}
			
			//错误码说明map，key为错误码，value为说明
			Map<String,String> errCodeDisMap = new HashMap<String,String>();
			
			SysMtRecordDAO mtDao = new SysMtRecordDAO();
			List<DynaBean> beanList = mtDao.getErrCodeDis(sbErrCode.toString(), corpCode);
			if(beanList == null || beanList.size() < 1)
			{
				return null;
			}
			
			for(DynaBean bean : beanList)
			{
				if(bean.get("state_code") == null || bean.get("state_des") == null)
				{
					continue;
				}
				errCodeDisMap.put(bean.get("state_code").toString(), bean.get("state_des").toString());
			}
			return errCodeDisMap;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取错误码说明map，异常。");
			return null;
		}
	}
	
	/**
	 * 
	 * @description 检查管辖机构是否就是企业顶级机构
	 * @param sysUser 操作员对象
	 * @return true：管辖的机构即为企业顶级机构；false：不是顶级机构，或者没能找到，或者异常。
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午02:56:42
	 */
	private boolean checkDomIsTopDep(LfSysuser sysUser)
	{
		//如果当前操作员的管辖机构即为企业最顶级的机构，则不需要拼操作员编码
		
		//获取企业最顶级机构
		LfDep topDep = getTopDep(sysUser.getCorpCode());
		//没能找到企业顶级机构
		if(topDep == null)
		{
			EmpExecutionContext.error("系统下行记录，检查管辖机构是否就是企业顶级机构，获取不到顶级机构对象。corpCode="+sysUser.getCorpCode());
			return false;
		}
		
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("userId", sysUser.getUserId().toString());
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("depId", StaticValue.ASC);
			List<LfDomination> domList = empDao.findListByCondition(LfDomination.class, conditionMap, orderbyMap);
			if(domList == null || domList.size() < 1)
			{
				return false;
			}
			
			for(LfDomination lfDom : domList)
			{
				//管辖的机构即为企业顶级机构
				if(lfDom.getDepId() == topDep.getDepId())
				{
					return true;
				}
			}
			return false;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，检查管辖机构是否就是企业顶级机构，异常。");
			return false;
		}
	}
	
	/**
	 * 
	 * @description 获取企业顶级机构
	 * @param corpCode 企业编码
	 * @return 企业顶级机构对象，没找到或者异常则返回null
	 * @author huangzb <huangzb@126.com>
	 * @datetime 2016-1-18 下午02:49:50
	 */
	private LfDep getTopDep(String corpCode)
	{
		try
		{
			//企业顶级机构先从内存中拿
			if(topDepMap.get(corpCode) != null)
			{
				return topDepMap.get(corpCode);
			}
			
			//获取企业最顶级机构
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			conditionMap.put("corpCode", corpCode);
			//顶级机构
			conditionMap.put("depLevel", "1");
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			orderbyMap.put("depId", StaticValue.ASC);
			List<LfDep> depList = empDao.findListByCondition(LfDep.class, conditionMap, orderbyMap);
			if(depList == null || depList.size() < 1)
			{
				EmpExecutionContext.error("系统下行记录，获取企业顶级机构，查询为空。corpCode="+corpCode);
				return null;
			}
			
			//企业顶级机构放到内存中
			topDepMap.put(corpCode, depList.get(0));
			
			return depList.get(0);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "系统下行记录，获取企业顶级机构，异常。");
			return null;
		}
	}
	

}
