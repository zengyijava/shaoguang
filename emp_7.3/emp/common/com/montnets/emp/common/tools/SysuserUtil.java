package com.montnets.emp.common.tools;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class SysuserUtil {

	public static String strLguserid(HttpServletRequest request){
		//漏洞修复 session里获取操作员信息
		String lguserid;
		if(request.getSession(false) == null || request.getSession(false).getAttribute("loginSysuser") == null){
			lguserid =  request.getParameter("lguserid");
		}else{
			LfSysuser sysUser =(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			lguserid = String.valueOf(sysUser.getUserId());
		}
		return lguserid;
	}

	public static String getCorpcode(HttpServletRequest request){
		//漏洞修复 session里获取操作员信息
		String lgcorpcode;
		if(request.getSession(false) == null || request.getSession(false).getAttribute("loginSysuser") == null){
			lgcorpcode =  request.getParameter("lgcorpcode");
		}else{
			LfSysuser sysUser =(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
			lgcorpcode = sysUser.getCorpCode();
		}
		return lgcorpcode;
	}

	public static String getSysUserName(HttpServletRequest request){
		LfSysuser sysUser =(LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		return sysUser.getUserName();
	}

	public static Long longLguserid(HttpServletRequest request){
		//漏洞修复 session里获取操作员信息
		String lguserid = strLguserid(request);
		return lguserid == null ? null : Long.parseLong(lguserid);
	}

	/**
	 * 关闭输入流
	 */
	public static void closeStream(InputStream in){
		if(in != null){
			try {
				in.close();
			} catch (IOException e) {

			}
		}
	}

	/**
	 * 关闭输出流
	 */
	public static void closeStream(OutputStream out){
		if(out != null){
			try {
				out.close();
			} catch (IOException e) {
                EmpExecutionContext.error(e, "发现异常！");
			}
		}
	}

	/**
	 * 关闭字节输入流
	 */
	public static void closeStream(Reader reader){
		if(reader != null){
			try {
				reader.close();
			} catch (IOException e) {
                EmpExecutionContext.error(e, "发现异常！");
			}
		}
	}

	/**
	 * 关闭输出流
	 */
	public static void closeStream(Writer writer){
		if(writer != null){
			try {
				writer.close();
			} catch (IOException e) {
                EmpExecutionContext.error(e, "发现异常！");
			}
		}
	}

	/**
	 * 刷新session，修复会话标识未更新漏洞
	 * @param request
	 */
	public static HttpSession regenerateSessionId(HttpServletRequest request){

		HttpSession session = request.getSession();

		//首先将原session中的数据转移至一临时map中
		Map<String,Object> tempMap = new HashMap();
		Enumeration<String> sessionNames = session.getAttributeNames();
		while(sessionNames.hasMoreElements()){
			String sessionName = sessionNames.nextElement();
			tempMap.put(sessionName, session.getAttribute(sessionName));
		}

		//注销原session，为的是重置sessionId
		session.invalidate();

		//将临时map中的数据转移至新session
		session = request.getSession();
		for(Map.Entry<String, Object> entry : tempMap.entrySet()){
			session.setAttribute(entry.getKey(), entry.getValue());
		}
		if(request.getCookies()!=null){
			//获取cookie
			Cookie cookie = request.getCookies()[0];
			//让cookie过期
			cookie.setMaxAge(0);
		}
		return session;
	}


}
