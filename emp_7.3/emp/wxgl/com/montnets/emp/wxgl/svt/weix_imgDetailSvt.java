package com.montnets.emp.wxgl.svt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.wxgl.LfWeiRimg;
import com.montnets.emp.ottbase.biz.BaseBiz;
import com.montnets.emp.ottbase.svt.BaseServlet;

/**
 * @author chensj
 *
 */
public class weix_imgDetailSvt extends BaseServlet{
    private final static String PATH    = "/wxgl/temple";
    
	//图文访问
	public void find(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		//图文ID
		String rimgid=request.getParameter("rimgid");
		//设备类型fromTp['类型'或者'手机'],解决手机上"返回"链接不用显示
		String fromTp = request.getParameter("fromTp");
		BaseBiz baseBiz = new BaseBiz();
		LfWeiRimg wcrimg=null;
		try {
			wcrimg = baseBiz.getById(LfWeiRimg.class, rimgid);
			if(wcrimg == null){
				request.getRequestDispatcher("/common/weixoverdue.jsp").forward(request, response);
			}	
			request.setAttribute("wcrimg", wcrimg);
			request.setAttribute("fromTp", fromTp);
		} catch (IOException e) {
			EmpExecutionContext.error(e,"模板预览-图文详情IOException异常！");
		} catch (Exception e) {
			EmpExecutionContext.error(e,"模板预览-图文详情预览失败！");
		}finally{
			request.getRequestDispatcher(PATH + "/weix_imgDetails.jsp").forward(request, response);
		}
		
	}
}
