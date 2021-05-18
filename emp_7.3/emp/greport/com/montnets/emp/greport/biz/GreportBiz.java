package com.montnets.emp.greport.biz;

import javax.servlet.http.HttpServletRequest;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;

public class GreportBiz {

	/**
	 * 获取企业编码
	 * @param request
	 * @return
	 */
	public String getCorpCode(HttpServletRequest request){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				if(loginSysuser!=null&&!"".equals(loginSysuser.getCorpCode())){
					return loginSysuser.getCorpCode();
				}else{
					return "-9999";
				}
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error("SESSION获取企业编码失败");
			return "-9999";
		}
		return "-9999";
	}
}
