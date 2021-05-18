package com.montnets.emp.rms.meditor.servlet;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.rms.meditor.base.BaseServlet;
import com.montnets.emp.rms.meditor.config.LfTemplateConfig;
import com.montnets.emp.rms.meditor.tools.UserUtil;
import com.montnets.emp.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 页面跳转
 */
public class PageServlet extends BaseServlet {

    public void toTest(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher("/rms/meditor/test.jsp").forward(request, response);
        } catch (ServletException e) {
        	 EmpExecutionContext.error(e,"页面跳转异常");
        } catch (IOException e) {
        	 EmpExecutionContext.error(e,"页面跳转异常");
        }

    }
    public void toMyTplndex(HttpServletRequest request, HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/list/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void toPublicTpIndex(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/list/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void toCorpCustomTpIndex(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/list/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }

    public void toSiteIndex(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/site/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void textEditor(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/text-editor/index.html?dada=ada";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void mediaEditor(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/media/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void cardEditor(HttpServletRequest request,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/card/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void h5Editor(HttpServletRequest request,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/h5-editor/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void toTempChoose(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/meditor/meditor_tempChoose.jsp";
            LfSysuser lfSysuser = UserUtil.getUser(request);
            if ("100000".equals(lfSysuser.getCorpCode().toString())){
                request.setAttribute("isSysmanager",1);
            }else {
                request.setAttribute("isSysmanager",0);
            }
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void toPoplist(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/poplist/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void toPreviewIndex(HttpServletRequest request ,HttpServletResponse response){
        try {
            String url = "/rms/webapp/dist/preview/index.html";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }
    public void toShowTemp(HttpServletRequest request ,HttpServletResponse response){
        try {
            String id = request.getParameter("id");
            request.setAttribute("tmId",id);
            String url = "/rms/rmstask/rmsShowTempView.jsp";
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e,"页面跳转异常");
        }
    }


}
