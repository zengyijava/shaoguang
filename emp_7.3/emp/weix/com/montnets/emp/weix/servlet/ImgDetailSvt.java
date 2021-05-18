package com.montnets.emp.weix.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.servlet.BaseServlet;
import com.montnets.emp.entity.weix.LfWcRimg;

/**
 * @author chensj
 *
 */
public class ImgDetailSvt extends BaseServlet{
	
	
	public void find(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String lgcorpcode=request.getParameter("lgcorpcode");
		//模板ID
		String tempId = request.getParameter("tempId");
		//图文ID
		String rimgid=request.getParameter("rimgid");
		//设备类型fromTp['类型'或者'手机'],解决手机上"返回"链接不用显示
		String fromTp = request.getParameter("fromTp");
		BaseBiz baseBiz = new BaseBiz();
		LfWcRimg wcrimg=null;
		try {
			wcrimg = baseBiz.getById(LfWcRimg.class, rimgid);
			request.setAttribute("wcrimg", wcrimg);
			request.setAttribute("tempId", tempId);
			request.setAttribute("fromTp", fromTp);
		} catch (IOException e) {
			EmpExecutionContext.error(e,"模板预览-图文详情IOException异常！");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"模板预览-图文详情预览失败！");
		}finally{
			request.getRequestDispatcher("/weix/temple/ImgDetails.jsp").forward(request, response);
		}
		
	}
}
