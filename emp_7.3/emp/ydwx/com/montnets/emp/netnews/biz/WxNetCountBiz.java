package com.montnets.emp.netnews.biz;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.SuperBiz;
import com.montnets.emp.netnews.daoImpl.Wx_netCountDaoImpl;
public class WxNetCountBiz extends SuperBiz{
	
	
	/*
	 * 通过号码字符串获取一个号码和名称的map
	 */
	public Map<String, String> getEmpCliMobileNames(String phoneStr) throws Exception {
		Map<String, String> userMap=new HashMap<String, String>();
		if(phoneStr.length()==0){
			return userMap;
		}
		List<DynaBean> lfEmployees =new Wx_netCountDaoImpl().findLfEmployeesByMobiles(phoneStr);
		if(lfEmployees != null && lfEmployees.size()>0){
			for(DynaBean employee:lfEmployees){
				if(employee.get("mobile")!=null&& employee.get("name")!=null){
					userMap.put(employee.get("mobile").toString(), employee.get("name").toString());
				}
			}
		}
		List<DynaBean> lfclients =new Wx_netCountDaoImpl().findLfClientsByMobiles(phoneStr);
		if(lfclients != null && lfclients.size()>0){
			for(DynaBean lfclient:lfclients){
				if(lfclient.get("mobile")!=null&& lfclient.get("name")!=null){
					userMap.put(lfclient.get("mobile").toString(), lfclient.get("name").toString());
				}
			}
		}
		return userMap;
	}
	
	
	/*
	 * 通过号码字符串获取一个号码和名称的map
	 */
	public Map<String, String> getEmpCliMobileNames_V1(String phoneStr,String lgcorpcode) throws Exception {
		Map<String, String> userMap=new HashMap<String, String>();
		if(phoneStr.length()==0){
			return userMap;
		}
		List<DynaBean> lfEmployees =new Wx_netCountDaoImpl().findLfEmployeesByMobiles_V1(phoneStr, lgcorpcode);
		if(lfEmployees != null && lfEmployees.size()>0){
			for(DynaBean employee:lfEmployees){
				if(employee.get("mobile")!=null&& employee.get("name")!=null){
					userMap.put(employee.get("mobile").toString(), employee.get("name").toString());
				}
			}
		}
		List<DynaBean> lfclients =new Wx_netCountDaoImpl().findLfClientsByMobiles_V1(phoneStr, lgcorpcode);
		if(lfclients != null && lfclients.size()>0){
			for(DynaBean lfclient:lfclients){
				if(lfclient.get("mobile")!=null&& lfclient.get("name")!=null){
					userMap.put(lfclient.get("mobile").toString(), lfclient.get("name").toString());
				}
			}
		}
		return userMap;
	}
	
	
	
}
