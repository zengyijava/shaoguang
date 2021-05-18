/**
 * 
 */
package com.montnets.emp.common.constant;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.DataAccessDriver;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.system.LfSysParam;
import com.montnets.emp.entity.sysuser.LfSysuser;

public class SystemGlobals
{
	public static final String SYSTEM_GLOBALS_NAME = "SystemGlobals";

	private static Properties defaults = new Properties();
	
	private SystemGlobals(){}
	
	public static void setProperties(Properties properties){
		defaults = properties;
	}
	
	public static Properties getProperties(){
		return defaults;
	}
	
	
	public static String getValue(String key){
		return defaults.getProperty(key);
	}
	public static String getValue(String key,String defaultValue){
		String value = defaults.getProperty(key);
		if(value==null || "".equals(value)){
			return defaultValue;
		}
		return value;
	}
	/**
	 * 读取配置文件中的信息
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static int getIntValue(String key,int defaultValue){
		if(defaults.getProperty(key)==null){
			return defaultValue;
		}
		try{
			return Integer.parseInt(defaults.getProperty(key));
		}catch (Exception e) {
			EmpExecutionContext.error(e, "读取配置文件信息异常。");
			return defaultValue;
		}
	}
	/**
	 * 根据key值获取配置表中的信息
	 * @param key
	 * @return
	 */
	public static String getSysParam(String key){
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//key值
		conditionMap.put("paramItem", key);
		
		try {
			//是否开启审批提醒
			if("isRemind".equals(key)){
				return SysConfValue.getIsRemind();
			}

			//调度汇总是否执行使用
			if("isHdata".equals(key)){
				return SysConfValue.getIsHdata();
			}

			//EMP调度汇总是否执行使用
			if("isSumm".equals(key)){
				return SysConfValue.getIsSumm();
			}

			//SummTimeInterval 这个参数重启WEB才生效，这里无需设置

			//EMP汇总结束扫描网关执行状态时间点
			if("ENDHOUR".equals(key)){
				return SysConfValue.getENDHOUR();
			}

			//ORACLE DB2 是否支持小写账号
			if("SPUSER_ISLWR".equals(key)){
				return SysConfValue.getSpuserIslwr();
			}

			//登录页面是否需要验证码,true:需要;false:不需要
			if("loginYanZhengMa".equals(key)){
				return SysConfValue.getLoginYanZhengMa();
			}

			//系统下行记录导出的最大记录条数，默认100万
			if("cxtjMtExportLimit".equals(key)){
				return SysConfValue.getCxtjMtExportLimit();
			}



			List<LfSysParam> lfSysParams = new DataAccessDriver().getEmpDAO().findListByCondition(LfSysParam.class, conditionMap, null);
			
			if(lfSysParams !=  null && lfSysParams.size() > 0){
				//返回value
				return lfSysParams.get(0).getParamValue();
			}
			
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据key值获取配置表中的信息异常。");
		}
		return null;
		
	}
	
	
	
	/**
	 * 根据key值设置配置表中的信息
	 * @param key
	 * @return
	 */
	public static void setSysParam(String key,String value){
		//条件map
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//修改值set map
		LinkedHashMap<String, String> setMap = new LinkedHashMap<String, String>();
		//key值
		conditionMap.put("paramItem", key);
		setMap.put("paramValue", value);
		try {
				new DataAccessDriver().getEmpDAO().update(LfSysParam.class, setMap, conditionMap);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据key值设置配置表中的信息异常。");
		}
	}



	/**
	 * 根据企业编码和key获得对应企业配置的机构限制信息,返回map
	 * @param
	 * @param corpCode
	 * @return map
	 */
	public static LinkedHashMap<String, String> getSysParamLfcorpConf(String corpCode){
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try {
			// 机构级别(系统配置)
			conditionMap.put("dep.maxlevel", String.valueOf(SysConfValue.getDepMaxLevel()));
			//单个机构子机构数(系统配置)
			conditionMap.put("dep.maxchild", String.valueOf(SysConfValue.getDepMaxChild()));
			// 机构总数(系统配置)
			conditionMap.put("dep.maxdep", String.valueOf(SysConfValue.getDepMaxDep()));
			//企业机构最大充值级别
			conditionMap.put(StaticValue.MAX_CHARGE_DEP_LEVEL, String.valueOf(SysConfValue.getDepMaxChargeLevel()));
			//安全级别 没看到这个配置
			conditionMap.put("security.validation", "");
//				//安全级别
//				else if("security.validation".equals(lfCorpCon.getParamKey()))
//				{
//					conditionMap.put("security.validation", lfCorpCon.getParamValue());
//				}
			return conditionMap;
		} catch (Exception e) {
			EmpExecutionContext.error(e, "根据企业编码和key获得对应企业配置的机构限制信息异常。");
		}
		return null;

	}

	/**
	 * 获取最大的机构级别
	 * @param corpCode
	 * @return
	 */
	public static Integer getMaxChargeLevel(String corpCode){
		LinkedHashMap<String, String> corpConf = getSysParamLfcorpConf(corpCode);
		String depLevel = corpConf == null ? "0": corpConf.get(StaticValue.MAX_CHARGE_DEP_LEVEL);
		return Integer.parseInt(depLevel);
	}
	
	/**
	 * 系统是否计费，true为计费，false为不计费
	 * @param userId 操作员id
	 * @return
	 */
	public static boolean isDepBilling(Long userId){
		LfSysuser lfSysuser = null;
		try {
			//获取用户信息
			lfSysuser = new BaseBiz().getById(LfSysuser.class, userId);
		} catch (Exception e) {
			EmpExecutionContext.error(e,"判断计费状态时，获取操作员信息失败！");
		}
		//得到用户企业编码
		String corpCode = lfSysuser == null ? "" : lfSysuser.getCorpCode();
		
		return isDepBilling(corpCode);
		
	}
	
	/**
	 * 判断机构计费是否开启。true为计费，false为不计费
	 * @param corpCode
	 * @return
	 */
	public static boolean isDepBilling(String corpCode){
		//根据企业编码获取此企业是否开启计费功能
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", corpCode);
		List<LfCorp> returnLists = null;
		try {
			returnLists = new DataAccessDriver().getEmpDAO()
					.findListBySymbolsCondition(LfCorp.class,
							conditionMap, null);
		
			if (returnLists != null && returnLists.size() > 0)
			{
				LfCorp corp = (LfCorp) returnLists.get(0);
				Integer isBalance = corp.getIsBalance();//;//默认0不计费，1为计费。 
				if(isBalance == 1){
					return true;
				}else{
					return false;
				}
			} else
			{
				return false;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构是否计费异常。");
		}
		return false;
	}
	
	/**
	 * 系统是否计费，1为计费，0为不计费,-999则程序异常
	 * @param corpCode 企业编码
	 */
	public static int isDepCharging(String corpCode){
		//根据企业编码获取此企业是否开启计费功能
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		conditionMap.put("corpCode", corpCode);
		List<LfCorp> returnLists = null;
		try {
			returnLists = new DataAccessDriver().getEmpDAO()
					.findListBySymbolsCondition(LfCorp.class,
							conditionMap, null);
		
			if (returnLists != null && returnLists.size() > 0)
			{
				LfCorp corp = (LfCorp) returnLists.get(0);
				Integer isBalance = corp.getIsBalance();//;//默认0不计费，1为计费。 
				if(isBalance == 1){
					return 1;
				}else{
					return 0;
				}
			} else
			{
				return 0;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"获取机构是否计费异常。");
			return StaticValue.EXP_RETURN;
		}
	}
	
}
