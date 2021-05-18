package com.montnets.emp.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.constant.ErrorCodeParam;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.pasroute.LfMmsAccbind;
import com.montnets.emp.entity.pasroute.LfSpDepBind;
import com.montnets.emp.entity.sysuser.LfSysuser;

/**
 * @author Administrator
 *
 */
public class CheckUtil 
{
	private  BaseBiz baseBiz = new BaseBiz();
	/**
	 * 此方法在发送前检查操作员和发送账号是否是当前企业下的
	 * @param sysuser
	 * @param corpCode
	 * @param spUser
	 * @param errorCodeParam 为返回错误编码对象，如果不需要则传递null
	 * @return
	 */
	public boolean checkSysuserInCorp(LfSysuser sysuser,String corpCode,String spUser,ErrorCodeParam errorCodeParam)
	{
		String date = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
		if(sysuser==null || sysuser.getCorpCode()==null)
		{
			EmpExecutionContext.error(date+"checkSysuserInCorp检查sysuser为空或者sysuser.getCorpCode为空！");
			if(errorCodeParam!=null)
			{
				errorCodeParam.setErrorCode("nosysuser");
				errorCodeParam.setDescription("操作员参数为空");
			}
			return false;
		}
		if(corpCode==null || "".equals(corpCode.trim()))
		{
			EmpExecutionContext.error(date+"checkSysuserInCorp检查corpCode为空");
			if(errorCodeParam!=null)
			{
				errorCodeParam.setErrorCode("nocorpcode");
				errorCodeParam.setDescription("企业编码参数为空");
			}
			return false;
		}
		//判断操作员是否在这个企业内
		if (!sysuser.getCorpCode().equals(corpCode)) 
		{
			EmpExecutionContext.error(date+"checkSysuserInCorp检查sysuser.getCorpCode不等于corpCode");
			if(errorCodeParam!=null)
			{
				errorCodeParam.setErrorCode("sysusernotin");
				errorCodeParam.setDescription("操作员不在该企业下");
			}
			return false;
		}
		
		LinkedHashMap<String, String> conditionMap =  new LinkedHashMap<String, String>();
		conditionMap.put("spUser", spUser);
		conditionMap.put("corpCode", corpCode);
		//业务账户绑定类型  1-emp 2-DBServer，emp指的是EMP平台的账号，DBSERVER指的是DB接入的账号
		//modify by tanglili 20170627  账号类型   新增EMP接入账号和DBServer账号 解决托管版EMP接入账号充值
		conditionMap.put("platFormType&"+StaticValue.IN, "1,2,3");
		//1- validate 0-invalidate
		conditionMap.put("isValidate", "1");
		List<LfSpDepBind> spDepBinds = null;
		try {
			spDepBinds =baseBiz.getByCondition(LfSpDepBind.class, conditionMap, null);
			if (spDepBinds==null || spDepBinds.size()==0) 
			{
				EmpExecutionContext.error(date+"SP账号["+spUser+"]验证不通过，原因：SP账号未分配给企业["+corpCode+"]或者SP账号和企业的绑定不可用)。");
				if(errorCodeParam!=null)
				{
					errorCodeParam.setErrorCode("spusernotin");
					errorCodeParam.setDescription("发送账号不在该企业下");
				}
				return false;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"检查操作员和发送账号是否是当前企业下异常！ ");
			return false;
		}
		
		return true;
	}	
	
	
	/**
	 * 此方法在发送前检查操作员和彩信发送账号是否是当前企业下的
	 * @param sysuser
	 * @param corpCode
	 * @param spUser
	 * @param errorCodeParam 为返回错误编码对象，如果不需要则传递null
	 * @return
	 */
	public boolean checkMmsSysuserInCorp(LfSysuser sysuser,String corpCode,String spUser,ErrorCodeParam errorCodeParam)
	{
		String date = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
		if(sysuser==null || sysuser.getCorpCode()==null)
		{
			EmpExecutionContext.error(date+"checkMmsSysuserInCorp检查sysuser为空或者sysuser.getCorpCode为空！");
			if(errorCodeParam!=null)
			{
				errorCodeParam.setErrorCode("nosysuser");
				errorCodeParam.setDescription("操作员参数为空");
			}
			return false;
		}
		if(corpCode==null || "".equals(corpCode.trim()))
		{
			EmpExecutionContext.error(date+"checkMmsSysuserInCorp检查corpCode为空");
			if(errorCodeParam!=null)
			{
				errorCodeParam.setErrorCode("nocorpcode");
				errorCodeParam.setDescription("企业编码参数为空");
			}
			return false;
		}
		//判断操作员是否在这个企业内
		if (!sysuser.getCorpCode().equals(corpCode)) 
		{
			EmpExecutionContext.error(date+"checkMmsSysuserInCorp检查sysuser.getCorpCode不等于corpCode");
			if(errorCodeParam!=null)
			{
				errorCodeParam.setErrorCode("sysusernotin");
				errorCodeParam.setDescription("操作员不在该企业下");
			}
			return false;
		}
		
		LinkedHashMap<String, String> conditionMap =  new LinkedHashMap<String, String>();
		conditionMap.put("mmsUser", spUser);
		conditionMap.put("corpCode", corpCode);
		//1- validate 0-invalidate
		conditionMap.put("isValidate", "1");
		List<LfMmsAccbind> mmsAccbind = null;
		try {
			mmsAccbind =baseBiz.getByCondition(LfMmsAccbind.class, conditionMap, null);
			if (mmsAccbind==null || mmsAccbind.size()==0) 
			{
				EmpExecutionContext.error(date+"SP账号["+spUser+"]验证不通过，原因：SP账号未分配给企业["+corpCode+"]或者SP账号和企业的绑定不可用)。");
				if(errorCodeParam!=null)
				{
					errorCodeParam.setErrorCode("spusernotin");
					errorCodeParam.setDescription("发送账号不在该企业下");
				}
				return false;
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e,"检查操作员和发送账号是否是当前企业下异常！ ");
			return false;
		}
		
		return true;
	}	
}