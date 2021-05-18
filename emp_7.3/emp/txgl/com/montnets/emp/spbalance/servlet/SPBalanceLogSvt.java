package com.montnets.emp.spbalance.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.spbalance.biz.SpBalanceBiz;
import com.montnets.emp.util.CheckUtil;
import com.montnets.emp.util.PageInfo;

public class SPBalanceLogSvt extends BaseServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -992973330816027577L;
	private final String emproot = "txgl";
	private final String base = "/spbalance";
	
	
	/**
	 * 查看sp账号充值/回收管理界面的所调用的方法
	 * 
	 * @param request
	 * @param response
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) {
		//获取SpBalanceBiz对象
		SpBalanceBiz spBalanceBiz = new SpBalanceBiz();
		//SP账号
		String spUser = request.getParameter("spUser");
		//信息类型
		String msgType = request.getParameter("msgType");
		//操作类型
		String opType = request.getParameter("opType");
		//执行结果
		String result = request.getParameter("result");
		//操作员
		String opUser = request.getParameter("opUser");
		//操作的开始时间
		String beginTime = request.getParameter("beginTime");
		//操作的结束时间
		String endTime = request.getParameter("endTime");
		
		//从session中获取企业编码
		LfSysuser checkSysUser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
		String corpCode = checkSysUser.getCorpCode();
		// 查询条件
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
		//日志开始时间
		long begin_time = System.currentTimeMillis();
		try {
			PageInfo pageInfo = new PageInfo();
			pageSet(pageInfo, request);
			//设置查询的条件
			//企业编码
			if (corpCode != null && !"".equals(corpCode.trim()) ) {
				conditionMap.put("corpCode", corpCode.trim());
			}
			if (spUser != null && !"".equals(spUser.trim()) ) {
				conditionMap.put("spUser", spUser.trim());
			}
			if (msgType != null && !"".equals(msgType)) {
				conditionMap.put("msgType", msgType);
			}
			if (opType != null && !"".equals(opType)) {
				conditionMap.put("opType", opType);
			}
			if (result != null && !"".equals(result)) {
				conditionMap.put("result", result);
			}
			if (opUser != null && !"".equals(opUser.trim())) {
				conditionMap.put("opUser", opUser.trim());
			}
			if (beginTime != null && !"".equals(beginTime)) {
				conditionMap.put("beginTime", beginTime);
			}
			if (endTime != null && !"".equals(endTime)) {
				conditionMap.put("endTime", endTime);
			}
			//添加当前操作员,修改人moll
			if(checkSysUser.getUserId() != null && !"2".equals(checkSysUser.getUserId().toString())){
				conditionMap.put("userId",checkSysUser.getUserId().toString());
			}
			//用biz获取查询得到的动态bean
			List<DynaBean> beanList = spBalanceBiz.getSPBalanceBean(
					conditionMap, pageInfo);
			//将获取的结果设置返回给页面
			request.setAttribute("beanList", beanList);
			request.setAttribute("pageInfo", pageInfo);
			request.setAttribute("conditionMap", conditionMap);
			// 增加查询日志
			long end_time = System.currentTimeMillis();
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			if (pageInfo != null) {
				String opContent = "查询开始时间：" + format.format(begin_time)
						+ ",耗时:" + (end_time - begin_time) + "ms，数量："
						+ pageInfo.getTotalRec();
				EmpExecutionContext.info("sp账号充值日志查看:" + opContent);
			}
			request.getRequestDispatcher(
					this.emproot + this.base + "/sp_balanceLog.jsp").forward(
					request, response);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "sp账号充值日志查看出现异常！");
		}
		
	}
	
}
