package com.montnets.emp.monmanage.biz;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.monitor.LfMonSgtacinfo;
import com.montnets.emp.monmanage.biz.i.IGateAcctMonCfgBiz;
import com.montnets.emp.monmanage.dao.GateAcctMonCfgDAO;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.util.PhoneUtil;
/**
 * 通道账号监控管理biz
 * @description 
 * @project p_ptjk
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author zhangmin
 * @datetime 2013-12-03 下午04:57:00
 */
public class GateAcctMonCfgBiz extends SuperBiz implements IGateAcctMonCfgBiz
{

	/**
	 * 获取通道账号监控管理
	 * @description    
	 * @param pageInfo
	 * @param conditionMap
	 * @return       			 
	 * @author zhangmin <ljr0300@163.com>
	 * @datetime 2013-12-03 下午04:58:00
	 */
	public List<DynaBean> getGateAcctMonCfg(PageInfo pageInfo, LinkedHashMap< String, String> conditionMap)
	{
		
		return new GateAcctMonCfgDAO().getGateAcctMonCfg(pageInfo, conditionMap);
	}
	
	/**
	 * 设置通道账号阀值信息
	 * @description    
	 * @param objectMap 修改map
	 * @param spaccountid sp账号
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2013-11-27 下午03:08:36
	 */
	public boolean editGateAcct(LinkedHashMap<String, String> objectMap,String gateaccount)
	{
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("gateaccount", gateaccount);
		try {
			empDao.update(LfMonSgtacinfo.class, objectMap, map);
			return true;
		}
		catch (Exception e) {
			EmpExecutionContext.error(e, "设置通道账号阀值信息biz层异常！");
			return false;
		}
	}
	
	/**
	 * 检查告警手机号码是否合法
	 * @description    
	 * @param monPhone
	 * @return       			 
	 * @author zhangmin
	 * @datetime 2014-3-31 下午05:13:07
	 */
	public String checkPhone(String monPhone)
	{
		String result="error";
		try
		{
			String [] phone = monPhone.split(",");
			//获取运营商号码段
			String[] haoduan = new WgMsgConfigBiz().getHaoduan();
			PhoneUtil phoneUtil = new PhoneUtil();
			if(phone.length==0)
			{
				result = "phoneError";
				return result;
			}
			for(int i=0;i<phone.length;i++)
			{
				if(phoneUtil.getPhoneType(phone[i], haoduan)==-1)
				{
					result = "phoneError";
					return result;
				}
			}
			result="success";
		}
		catch (Exception e) {
			result="error";
			EmpExecutionContext.error(e,"验证告警手机号码异常！");
		}
		return result;
	}
	
	/**
	 * 验证邮箱是否合法
	 * @param monEmail
	 * @return
	 */
	public String checkEmail(String monEmail)
	{
		String result="error";
		try
		{
			String[] arr = { "ac", "com", "net", "org", "edu", "gov", "mil", "ac\\.cn",
				"com\\.cn", "net\\.cn", "org\\.cn", "edu\\.cn" };
			String temp_arr="";
			for(int i = 0 ; i<arr.length ; i++){
				temp_arr += arr[i]+"|";
			}

			// reg
			String reg_str = "^[0-9a-zA-Z](\\w|-)*@\\w+\\.(" + temp_arr + ")$";
			Pattern pattern = Pattern.compile(reg_str);
			Matcher matcher = pattern.matcher(monEmail);
			if (matcher.matches()) {
				result="success";
			}else {
				result="emailError";
			}			
		}
		catch (Exception e) {
			result="error";
			EmpExecutionContext.error(e,"验证告警邮箱异常！");
		}
		return result;
	}
}
