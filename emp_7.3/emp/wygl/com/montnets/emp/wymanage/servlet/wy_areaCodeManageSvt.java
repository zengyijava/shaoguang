/**
 * @description
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午10:50:50
 */
package com.montnets.emp.wymanage.servlet;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.*;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.*;
import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.entity.wy.AAreacode;
import com.montnets.emp.util.PageInfo;
import com.montnets.emp.wymanage.biz.wy_areaCodeManageBiz;

/**
 * @description
 * @project p_wygl
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @author chentingsheng <cts314@163.com>
 * @datetime 2014-3-25 上午10:50:50
 */

public class wy_areaCodeManageSvt extends BaseServlet
{
	private final String						empRoot				= "wygl";

	private final String						base				= "/wymanage";

	private final AuthenAtom					authenAtom			= new AuthenAtom();

	private final wy_areaCodeManageBiz		areaCodeManageBiz	= new wy_areaCodeManageBiz();

	private final BaseBiz						baseBiz				= new BaseBiz();

	/**
	 * @description
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 上午10:52:05
	 */
	private static final long	serialVersionUID	= 1L;
	
	private final String opModule = "国家代码管理";

	/**
	 * @description
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 上午10:51:27
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		long stime = System.currentTimeMillis();
		PageInfo pageInfo = new PageInfo();
		try
		{
			List<DynaBean> areaCodeList = null;
			pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			// 国家
			String areaName = request.getParameter("queryareaname");
			// 国际区号
			String areaCode = request.getParameter("queryareacode");

			if(areaName != null && !"".equals(areaName) && authenAtom.holesProcessing(areaName))
			{
				conditionMap.put("areaname", areaName);
			}
			if(areaCode != null && !"".equals(areaCode) && authenAtom.holesProcessing(areaCode))
			{
				conditionMap.put("areacode", areaCode);
			}
			// 获取国家代码list
			areaCodeList = areaCodeManageBiz.getAreaCode(pageInfo, conditionMap);
			// 封装请求返回参数
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("areaCodeList", areaCodeList);
			request.getRequestDispatcher(this.empRoot + base + "/wy_areaCodeManage.jsp").forward(request, response);
			DateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String sDate = sdf.format(stime);
			setLog(request,"国家代码管理","("+sDate+")查询", StaticValue.GET);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "国家代码查询异常！");
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try
			{
				request.getRequestDispatcher(this.empRoot + base + "/wy_areaCodeManage.jsp").forward(request, response);
			}
			catch (Exception e1)
			{
				EmpExecutionContext.error(e1, "跳转国家代码查询界面异常！");
			}
		}
	}

	/**
	 * @description 新增国家代码
	 * @param request
	 * @param response
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-25 下午05:23:35
	 */
	public void toAdd(HttpServletRequest request, HttpServletResponse response)
	{
//		opModule = this.opModule;
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "ADD";
		String OpStatus = "失败";
		// 返回结果
		String result = "success";
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		// 国家
		String areaName = request.getParameter("areaname");
		// 国际区号
		String areaCode = request.getParameter("areacode");

		AAreacode areaCodeObj = new AAreacode();
		try
		{
			// 参数合法性校验
			if(areaName != null && !"".equals(areaName) && authenAtom.holesProcessing(areaName))
			{
				areaCodeObj.setAreaname(areaName);
			}
			else
			{
				result = "faild";
				EmpExecutionContext.error("新增国家代码参数异常，areaName：" + areaName);
				return;
			}
			if(areaCode != null && !"".equals(areaCode) && authenAtom.holesProcessing(areaCode))
			{
				conditionMap.put("areacode", areaCode);
				areaCodeObj.setAreacode(areaCode);
			}
			else
			{
				result = "faild";
				EmpExecutionContext.error("新增国家代码参数异常，areaCode：" + areaCode);
				return;
			}
			// 表中不存在相同的国际区号
			if(!areaCodeManageBiz.isExist(conditionMap))
			{
				areaCodeObj.setCreatetime(new Timestamp(System.currentTimeMillis()));
				if(!baseBiz.addObj(areaCodeObj))
				{
					result = "faild";
				}
			}
			else
			{
				result = "isExist";
				return;
			}
		}
		catch (Exception e)
		{
			result = "faild";
			EmpExecutionContext.error(e, "新增国家代码异常。");
		}
		finally
		{
			if ("success".equals(result))
			{
				OpStatus = "成功";
			}
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e1) {
				EmpExecutionContext.error(e1, "session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
				userId = lfSysuser.getUserId().toString();
				corpCode = lfSysuser.getCorpCode();
				userName = lfSysuser.getUserName();
			}
			
			opContent = "新增国家代码"+OpStatus+"。（国家/地区："+areaName+"，国际区号："+areaCode+")";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			try
			{
				response.getWriter().print(result);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "新增国家代码跳转界面异常！");
			}
		}
	}

	/**
	 * 删除国家代码
	 * @description    
	 * @param request
	 * @param response       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2014-3-26 上午11:28:11
	 */
	public void toDel(HttpServletRequest request, HttpServletResponse response)
	{
//		private String opModule = "国家代码管理";
		String corpCode = null;	
		String userId = null;	
		String userName = null;	
		String opContent = null;	
		String opType = "DELETE";
		String delStr = null;
		String result = "faild";
		String OpStatus = "失败";
		// ID编号
		String id = request.getParameter("id");
		// 条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		try
		{
		// 参数合法性校验
		if(id != null && !"".equals(id) && authenAtom.holesProcessing(id))
		{
			conditionMap.put("id&in", id);
		}
		else
		{
			result = "faild";
			EmpExecutionContext.error("删除国家代码参数异常，id：" + id);
			return;
		}
			delStr = areaCodeManageBiz.getAreacodeStrByid(id);
			int count = baseBiz.deleteByCondition(AAreacode.class, conditionMap);
			if(count < 1)
			{
				result = "faild";
				EmpExecutionContext.error("删除国家代码失败。");
			}
			else
			{
				OpStatus = "成功";
				result = String.valueOf(count);
			}
			//增加操作日志
		}
		catch (Exception e)
		{
			result = "faild";
			EmpExecutionContext.error(e, "删除国家代码异常。");
		}
		finally
		{
			//增加操作日志
			Object obj = null;
			try {
				obj = request.getSession(false).getAttribute("loginSysuser");
			} catch (Exception e1) {
				EmpExecutionContext.error(e1, "session为null");
			}
			LfSysuser lfSysuser = (LfSysuser)obj;
			if(lfSysuser!=null && lfSysuser.getUserId()!=null){
				userId = lfSysuser.getUserId().toString();
				corpCode = lfSysuser.getCorpCode();
				userName = lfSysuser.getUserName();
			}
			
			opContent = "删除国家代码"+OpStatus+"。（"+delStr+"）";
			EmpExecutionContext.info(opModule, corpCode, userId, userName, opContent, opType);
			try
			{
				response.getWriter().print(result);
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "删除国家代码跳转界面异常！");
			}
		}
	}


	private void setLog(HttpServletRequest request,String opModule,String opContent,String opType){
		try
		{
			Object loginSysuserObj=request.getSession(false).getAttribute("loginSysuser");
			if(loginSysuserObj!=null){
				LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
				EmpExecutionContext.info(opModule, loginSysuser.getCorpCode(), loginSysuser.getUserId()+"", loginSysuser.getUserName(), opContent, opType);
			}
		}catch(Exception e)
		{
			EmpExecutionContext.error(e,opModule+opType+opContent+"日志写入异常");
		}
	}
}
