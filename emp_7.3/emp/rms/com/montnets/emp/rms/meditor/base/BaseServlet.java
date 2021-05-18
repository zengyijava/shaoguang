package com.montnets.emp.rms.meditor.base;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.util.PageInfo;

/**
 * 前后分离富信模块servlet基础类
 */
public class BaseServlet extends HttpServlet {

    private static boolean cross = Boolean.valueOf(SystemGlobals.getValue("montnets.rms.editor.cross"));

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //是否正确响应请求方法完成
        boolean isComplete = false;

        request.setCharacterEncoding(StaticValue.ENCODING);
        response.setCharacterEncoding(StaticValue.ENCODING);

/*        //是否允许跨域
        if(cross){
             允许跨域的主机地址 
            response.setHeader("Access-Control-Allow-Origin", "*");
             允许跨域的请求方法GET, POST, HEAD 等 
            response.setHeader("Access-Control-Allow-Methods", "*");
             重新预检验跨域的缓存时间 (s) 
            response.setHeader("Access-Control-Max-Age", "9999999999");
             允许跨域的请求头 
            response.setHeader("Access-Control-Allow-Headers", "*");
             是否携带cookie 
            response.setHeader("Access-Control-Allow-Credentials", "true");
            
    		if (request.getMethod().equals("OPTIONS")) {
    			response.setStatus(HttpStatus.SC_OK);
            }
        }*/
        



        //以路由的最后一段作为方法名，例 /meditor/getUser  对应方法名为getUser(req,res)
        String requestUri = request.getRequestURI();
        if (requestUri.contains(".htm")){
            requestUri = requestUri.substring(requestUri.lastIndexOf("/")+1,requestUri.lastIndexOf("."));
        }
        String method = "";
        if (requestUri.contains(".meditorPage")){
            method = requestUri.substring(requestUri.lastIndexOf("/")+1,requestUri.lastIndexOf(".meditorPage"));
        }else {
            method = requestUri.substring(requestUri.lastIndexOf("/")+1,requestUri.length());
        }
        Method clazzMethod = getValidMethod(method);
        if(clazzMethod == null){
            EmpExecutionContext.error("请求方法名称不合法！ip:"+getIpAddr(request)+", url:"+requestUri+",method:"+method);
            return;
        }
        Object[] Objparams = { request, response };
        try {
            clazzMethod.invoke(this, Objparams);
            isComplete = true;
        } catch (IllegalAccessException e) {
            EmpExecutionContext.error(e,"baseSvt方法转发异常，非法的方法访问权限！url:"+requestUri+",method:"+method);
        } catch (InvocationTargetException e) {
            EmpExecutionContext.error(e, "baseSvt方法转发异常。url:"+requestUri+",method:"+method);
        }finally {
            if(!isComplete){
                response.sendRedirect(request.getContextPath() + "/common/logoutEmp.html");
            }
        }
    }

    public boolean pageSet(PageInfo pageInfo, HttpServletRequest request) {
        return false;
    }

    /**
     * 判断是否有效的方法名 数字、字母、下划线、$组成 并且不能以数字开头 且 当前请求servlet存在对应的方法
     * @param method
     * @return
     */
    private Method getValidMethod(String method){

        if(method == null){return null;}
        Pattern pattern = Pattern.compile("^[a-zA-Z$_][a-zA-Z$_0-9]*$");
        if(pattern.matcher(method).matches()){
            Class c = this.getClass();
            Method[] methods = c.getMethods();
            for (Method m : methods) {
                Class[] paraTypes = m.getParameterTypes();
                if(method.equals(m.getName()) && m.getReturnType().getClass().isInstance(Void.TYPE) //方法名相同 返回值void
                        && paraTypes != null && paraTypes.length==2 //2个参数
                        && paraTypes[0].getClass().isInstance(HttpServletRequest.class)&&paraTypes[1].getClass().isInstance(HttpServletResponse.class)){//两个参数分别为HttpServletRequest，HttpServletResponse
                    return m;
                }
            }
        }
        return null;
    }
    /**
     * 获取客户端IP地址
     * @param request
     * @return
     *
     */
    public String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
