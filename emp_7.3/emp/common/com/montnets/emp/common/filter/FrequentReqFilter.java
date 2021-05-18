package com.montnets.emp.common.filter;

import com.montnets.emp.common.constant.RequestData;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author zousy
 * @project emp_std_192.169.1.81
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2015/12/9
 * @description
 */
public class FrequentReqFilter implements Filter {
    private static final String SESSIONKEY = "requestDataKey";
    private static final int allowMaxTimes = 10;
    private static final long allowMinMillis = 1 * 1000L;

    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //请求
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //响应
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        String uri = request.getRequestURI();
        if (session == null) {
            //如果是带了异步请求的标示
            if("yes".equals(request.getParameter("isAsync")))
            {
                //返回已经退出登录
                response.getWriter().print("outOfLogin");
                return;
            }
            /*当用户未登录时，重新访问main.jsp页面时，由于此页面是框架页面，页面上有两次请求，所以会导致页面给出提示
            两次．所以再这儿判断一下，如果是top.jsp页面请求的则只返回，不给出提示．*/
            if(uri.contains("top.jsp"))
            {
                return;
            }
            else
            {
                //跳转到checkLogin.html
                response.sendRedirect(request.getContextPath()+"/common/logoutEmp.html");
                return;
            }
        }
        //请求方法名
        String methodName = request.getParameter("method");
        //页面为传method参数 则默认为find
        if(methodName == null)
        {
            methodName = "find";
        }
        //请求路径 uri+method
        String method = uri+"?method="+methodName;
        //请求数据对象
        RequestData requestData = (RequestData) session.getAttribute(SESSIONKEY);
        synchronized (requestData){
            //为空则重置对象
            if (requestData.isEmpty()) {
                requestData.reset(method);
            } else {
                //更新对象
                requestData.push(method);
                //相同方法连续请求次数超过最大上限
                if (requestData.isOverMax(allowMaxTimes)) {
                    //相同方法连续请求持续时间在允许最大时间内
                    if (requestData.isInTime(allowMinMillis)) {
                        //为非法频繁请求 销毁当前会话
                        session.invalidate();
                    } else {
                        //次数达到上限但不满足请求持续时间在允许最大时间内 则 重置对象重新开始统计
                        requestData.reset(method);
                    }
                }
            }
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}