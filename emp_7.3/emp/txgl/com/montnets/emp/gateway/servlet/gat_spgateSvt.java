package com.montnets.emp.gateway.servlet;



import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.ParamsEncryptOrDecrypt;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.SuperDAO;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.gateway.AgwParamConf;
import com.montnets.emp.entity.gateway.AgwParamValue;
import com.montnets.emp.security.context.ErrorLoger;
import com.montnets.emp.util.PageInfo;


@SuppressWarnings("serial")
public class gat_spgateSvt extends BaseServlet {
	
	final ErrorLoger errorLoger = new ErrorLoger();
	final String empRoot="txgl";
	final String basePath="/gateway";
	
	/**
	 * 通道运行参数配置查询
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response)
	{
		PageInfo pageInfo = new PageInfo();
		BaseBiz baseBiz = new BaseBiz();
		try
		{
			String gwNo = request.getParameter("gwNo");
			pageSet(pageInfo, request);
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LinkedHashMap<String, String> orderbyMap = new LinkedHashMap<String, String>();
			// conditionMap.put("paramAttribute", "0");
			conditionMap.put("gwType", "3000");
			orderbyMap.put("paramItem", StaticValue.ASC);
			List<AgwParamConf> paramList = baseBiz.getByCondition(AgwParamConf.class, conditionMap, orderbyMap);
			
//			List<AgwAccount> accountList = baseBiz.getEntityList(AgwAccount.class);
//			
//			if(gwNo == null && accountList != null && accountList.size()>0)
//			{
//				gwNo = accountList.get(0).getGwNo().toString();
//			}else if(gwNo == null)
//			{
//				gwNo = "";
//			}
			
			//查询通道账号
			String sql="SELECT GC.GWNO GWNO,AG.PTACCID PTACCID,AG.PTACCNAME PTACCNAME FROM GW_CLUSPBIND GC INNER JOIN A_GWACCOUNT AG ON GC.PTACCUID=AG.PTACCUID ORDER BY AG.PTACCUID ASC,GC.GWNO ASC";
			List<DynaBean> accountBeanList=new SuperDAO().getListDynaBeanBySql(sql);
			if(gwNo == null && accountBeanList != null && accountBeanList.size()>0)
			{
				gwNo = accountBeanList.get(0).get("gwno").toString();
			}else if(gwNo == null)
			{
				gwNo = "";
			}
			
			conditionMap.clear();
			conditionMap.put("gwNo",gwNo);
			List<AgwParamValue> valueList = baseBiz.getByCondition(AgwParamValue.class, conditionMap, orderbyMap);
			LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
			if(valueList != null && valueList.size()>0)
			{
				for(AgwParamValue value : valueList)
				{
					valueMap.put(value.getParamItem(), value.getParamValue());
				}
			}
			request.setAttribute("gwNo", gwNo);
			//加密ID
			String keyId = "";
			//ID加密
			if(gwNo != null && gwNo.trim().length() > 0)
			{
				//加密对象
				ParamsEncryptOrDecrypt encryptOrDecrypt = getParamsEncryptOrDecrypt(request);
				//加密对象不为空
				if(encryptOrDecrypt != null)
				{
					keyId = encryptOrDecrypt.encrypt(gwNo);
					if(keyId == null)
					{
						EmpExecutionContext.error("通道运行参数配置信息列表，参数加密失败。");
						throw new Exception("通道运行参数配置信息列表，参数加密失败。");
					}
				}
				else
				{
					EmpExecutionContext.error("通道运行参数配置信息列表，从session中获取加密对象为空！");
					throw new Exception("通道运行参数配置信息列表，获取加密对象失败。");
				}
			}
			request.setAttribute("keyId", keyId);
			request.setAttribute("valueMap", valueMap);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("paramList", paramList);
			//request.setAttribute("accountList", accountList);
			request.setAttribute("accountList", accountBeanList);
			request.getRequestDispatcher(this.empRoot + this.basePath + "/gat_spgate.jsp")
				.forward(request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(errorLoger.getErrorLog(e,"通道运行参数配置查询异常"));
			request.setAttribute("findresult", "-1");
			request.setAttribute("pageInfo", pageInfo);
			try {
				request.getRequestDispatcher(this.empRoot + this.basePath +"/gat_spgate.jsp")
				.forward(request, response);
			} catch (ServletException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"通道运行参数配置servelt异常"));
			} catch (IOException e1) {				
				EmpExecutionContext.error(errorLoger.getErrorLog(e1,"通道运行参数配置查询servelt跳转异常"));
			}
		}
	}
	
	/**
	 * 获取加密对象
	 * @description    
	 * @param request
	 * @return       			 
	 * @author chentingsheng <cts314@163.com>
	 * @datetime 2016-10-26 下午07:29:28
	 */
	public ParamsEncryptOrDecrypt getParamsEncryptOrDecrypt(HttpServletRequest request)
	{
		try
		{
			ParamsEncryptOrDecrypt encryptOrDecrypt = null;
			//加密对象
			Object encrypOrDecrypttobject = request.getSession(false).getAttribute("decryptObj");
			//加密对象不为空
			if(encrypOrDecrypttobject != null)
			{
				//强转类型
				encryptOrDecrypt=(ParamsEncryptOrDecrypt)encrypOrDecrypttobject;
			}
			else
			{
				EmpExecutionContext.error("通道运行参数配置，从session获取加密对象为空。");
			}
			return encryptOrDecrypt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "通道运行参数配置，从session获取加密对象异常。");
			return null;
		}
	}
}
