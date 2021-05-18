package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.gateway.AProInfo;


@SuppressWarnings("serial")
public class FindAproInfoServlet extends BaseServlet{

	
	public void find(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BaseBiz baseBiz = new BaseBiz();
		
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = null;
		// 查找网关认证系信息
		List<AProInfo> proList;
		try {
			HttpSession session = request.getSession(false);
			out = response.getWriter();
			proList = baseBiz.getByCondition( AProInfo.class, null, null);

			if (proList != null && proList.size() > 0) {
				session.setAttribute("AProInfo", proList.get(0));
				// 发送速度
				out.print(proList.get(0).getSendSpeed());
			}
		} catch (Exception e) {
			if(out != null){
				out.print("error");
			}
			EmpExecutionContext.error(e, "网关认证系信息find方法异常。");
		}
	}

	public void findAll(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BaseBiz baseBiz = new BaseBiz();
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		// 查找网关认证系信息
		List<AProInfo> proList;
		try {
			proList = baseBiz.getByCondition( AProInfo.class, null, null);

			if (proList != null && proList.size() > 0) {
				AProInfo proInfo = proList.get(0);
				out.print(proInfo.getProStatus() + "&" + proInfo.getValidDays()
						+ "&" + proInfo.getStatusInfo() + "&"
						+ proInfo.getSendSpeed());
			}
		} catch (Exception e) {
			out.print("error");
			EmpExecutionContext.error(e, "网关认证系信息findAll方法异常。");
		}
	}
}
