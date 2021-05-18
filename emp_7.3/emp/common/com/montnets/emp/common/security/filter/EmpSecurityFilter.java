package com.montnets.emp.common.security.filter;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.security.cache.SecurityCache;
import com.montnets.emp.common.security.constant.SecurityConst;
import com.montnets.emp.common.security.service.EmpSecurityService;
import com.montnets.emp.common.security.service.impl.EmpSecurityServiceImpl;
import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author liangHuaGeng
 * @Title: SecurityFilter
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/16 10:09
 */
public class EmpSecurityFilter implements Filter {
    protected final EmpSecurityService securityService = new EmpSecurityServiceImpl();
    /**
     * 资源类
     */
    protected static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(SecurityConst.BUNDLE);
    /**
     * url的非法特殊字符
     */
    protected String[] illegalChars;
    /**
     * urlEncode解码次数
     */
    protected String count;
    /**
     * sql注入过滤器关键字 放权的url和method
     */
    protected Map<String, Map<String, String>> urlMethod;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        EmpExecutionContext.info("EMP安全检测开始初始化! currentTime = " + Calendar.getInstance(Locale.CHINA).getTime());
        this.illegalChars = filterConfig.getInitParameter("illegalChars").split(",");
        this.count = filterConfig.getInitParameter("count");
        if (SecurityCache.INSTANCE.getUrlMethodMap().isEmpty()) {
            this.urlMethod = securityService.getUrlMethod("UrlMethodAllowed.properties");
            SecurityCache.INSTANCE.setUrlMethodCache(this.urlMethod);
        } else {
            this.urlMethod = SecurityCache.INSTANCE.getUrlMethodMap();
        }
        if (SecurityCache.INSTANCE.getEscapeChar().isEmpty()) {
            List<String> escapeChar = securityService.getEscapeChar("Escape_character.properties");
            SecurityCache.INSTANCE.setEscapeChar(escapeChar);
        }

        if(SecurityCache.INSTANCE.getEscapeParam().isEmpty()){
            Map<String,List<String>> escapeParam=securityService.getEscapeParam();
            SecurityCache.INSTANCE.setEscapeParam(escapeParam);
        }
        EmpExecutionContext.info("EMP安全检测初始化完毕! currentTime = " + Calendar.getInstance(Locale.CHINA).getTime());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            // 必须手动指定编码格式
            request.setCharacterEncoding(SecurityConst.ENCODING_UTF8);

            // 修复跨站点请求伪造 - 恶意构造referer
            String referer = request.getHeader("referer");
            if (null != referer && !referer.contains(request.getServerName())) {
                return;
            }
            
            //BOGUS请求方式不合法，返回403
            if(removeDogusMethod(request, response)){
                return;
            }
            

            // 对请求输入HOST头信息进行信息安全性校验
            if (addHeader(request, response)) {
                return;
            }

            // 向所有会话cookie中添加“HttpOnly”等属性
            addHttpOnly(request, response);

            //获取URL
            String tempURL = request.getRequestURI();
            // 获取URL后面的参数列表
            String tempParam = request.getQueryString();


            //处理出现的非法的Content-Type
            if(doContentType(request, response)){
                return;
            }


            // 判断是否是可以忽略走sql注入的action
            boolean sqlFlag = isSqlFilter(request, tempURL);

            // 进行跨站脚本编制和sql注入的漏洞检测
            EmpHttpServletRequestWrapper xssRequest = new EmpHttpServletRequestWrapper(request, sqlFlag);

            // 增加sessionId
            addSessionId(response, xssRequest);

            filterChain.doFilter(xssRequest, response);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "EmpSecurityFilter.doFilter() 发现异常");
        }
    }

    /**
     * Sql 注入过滤
     *
     * @param request
     * @param tempURL
     * @return
     */
    private boolean isSqlFilter(HttpServletRequest request, String tempURL) {
        boolean sqlFlag = true;
        // 判断是否是可以忽略走sql注入的action
        String action = tempURL.substring(tempURL.lastIndexOf("/") + 1, tempURL.length());

        if (urlMethod.get(SecurityConst.ASTERISK) != null && (SecurityConst.NUM_ONE).equals(urlMethod.get(SecurityConst.ASTERISK).get(SecurityConst.ASTERISK))) {
            //如果配置了*=*，则全部不走Sql注入过滤
            sqlFlag = false;
        } else if (urlMethod.get(action) != null) {
            //获取map里的url
            //获取map里的method
            Map<String, String> map = urlMethod.get(action);
            //请求方法的method
            String method = request.getParameter("method");
            boolean condition = (SecurityConst.NUM_ONE).equals(map.get(SecurityConst.ASTERISK))
                    || (com.montnets.emp.util.StringUtils.isNotBlank(method) && (SecurityConst.NUM_ONE).equals(map.get(method)));
            if (condition) {
                sqlFlag = false;
            }
        }
        return sqlFlag;
    }

    /**
     * 增加sessionId
     *
     * @param servletResponse
     * @param xssRequest
     */
    private void addSessionId(HttpServletResponse servletResponse, EmpHttpServletRequestWrapper xssRequest) {
        // 不写sessionId值，webLogic启动时，IE浏览器无法登陆
        servletResponse.setHeader("SET-COOKIE", "Path=/;secure;HttpOnly");
        // String sessionId = xssRequest.getSession().getId();
        // servletResponse.setHeader("SET-COOKIE", "JSESSIONID=" + sessionId + ";Path=/;secure;HttpOnly");
    }

    private boolean hasIllegalUrl(String tempURL, String tempParam) {
        // true：非法  false；不非法
        boolean illegalStatus = false;

        //对URL进行判断
        for (String tmp : illegalChars) {
            boolean flag = (tempURL != null && tempURL.contains(tmp)) || (tempParam != null && tempParam.contains(tmp));
            if (flag) {
                //非法状态
                illegalStatus = true;
                break;
            }
        }
        //非法请求的URL，返回
        return illegalStatus;
    }

    /**
     * 对URL后面的参数进行urlDecode 解码
     *
     * @param tempParam
     * @return
     * @throws UnsupportedEncodingException
     */
    public String urlDecode(String tempParam) throws UnsupportedEncodingException {
        if (tempParam != null && tempParam.contains(SecurityConst.PER_CENT)) {
            //含有%的UrlEncode编码的参数，解码count次
            int num = count == null ? 5 : Integer.valueOf(count);
            for (int i = 0; i < num; i++) {
                //单独出现的%替换成转码后的字符就行了，也就是%25,避免解码出错
                tempParam = tempParam.replaceAll("%(?![0-9a-fA-F]{2})", "%25");
                tempParam = URLDecoder.decode(tempParam, SecurityConst.ENCODING_UTF8);
            }
        }
        return tempParam;
    }

    /**
     * 对请求输入HOST头信息进行信息安全性校验
     * 对请求输入HOST头信息进行信息安全性校验，防止HOST头信息被恶意篡改利用。
     * 使用一个HTTP头“X-Frame-Options”值为SAMEORIGIN-同源策略。
     * 设置X-XSS-Protection、X-Content-Type-Options、Content-Security-Policy头
     *
     * @param request
     * @param response
     * @return
     */
    private boolean addHeader(HttpServletRequest request, HttpServletResponse response) {
        String host = request.getHeader("Host");
        response.addHeader("x-frame-options", "SAMEORIGIN");
        response.addHeader("X-XSS-Protection", "1; mode=block");
        //此报文头使webLogic无法打开页面，注释
        response.addHeader("Content-Security-Policy", "frame-ancestors'self'");
        // HTTP host 头攻击漏洞
        if (StringUtils.isNotEmpty(host)) {
            if (!checkBlankList(host)) {
                EmpExecutionContext.error("请求过滤，请求HOST在配置的URL中不存在，HOST:" + host);
                return true;
            }
        }
        return false;
    }

    /**
     * 向所有会话cookie中添加“HttpOnly”等属性
     *
     * @param request
     * @param response
     */
    private void addHttpOnly(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                StringBuilder builder = new StringBuilder();
                builder.append("JSESSIONID=").append(cookie.getValue()).append("; ");
                builder.append("Secure; ");
                builder.append("HttpOnly; ");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR, 1);
                Date date = cal.getTime();
                Locale locale = Locale.CHINA;
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", locale);
                builder.append("Expires=").append(sdf.format(date));
                response.setHeader("Set-Cookie", builder.toString());
            }
        }
    }

    @Override
    public void destroy() {
        illegalChars = null;
        count = null;
    }

    /**
     * 校验当前host是否在白名单中
     *
     * @param host
     * @return
     */
    private boolean checkBlankList(String host) {
        String thisUrl = RESOURCE_BUNDLE.getString("montnets.thisUrl");
        String outerUrl = RESOURCE_BUNDLE.getString("montnets.outerUrl");
        return thisUrl.contains(host) || outerUrl.contains(host) || host.contains("127.0.0.1") || host.contains("localhost");
    }
    
    
    /**
     * 使用 HTTP 动词篡改的认证旁路
     * BOGUS是不合法,返回403
     * @return
     * @throws IOException 
     */
    private boolean removeDogusMethod(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	boolean flag=true;
        String method = request.getMethod();
        if("BOGUS".equals(method))
        {
            EmpExecutionContext.error("The request with Method["+method+"] was forbidden by server!");
        	response.setContentType("text/html;charset=UTF-8");
        	response.setCharacterEncoding("UTF-8");
        	response.setStatus(403);
        	response.getWriter().print("<font size=6 color=red>对不起，您的请求非法，系统拒绝响应!</font>");
        	flag=true;
        }else{
        	flag=false;
        }
        return flag;
    }


    /**
     * 处理contentType
     * contentType是不合法,返回403
     * @return
     * @throws IOException 
     */
    private boolean doContentType(HttpServletRequest request, HttpServletResponse response) throws IOException{
    	boolean flag=true;
        String contentType = request.getContentType();
        //存储的跨站点脚本编制 ,注入了下面的报文头
        //Content-Type: %{(#xx=#context['com.opensymphony.xwork2.dispatcher.HttpServletResponse']).(#xx.addHeader('AppScanHeader','AppScanValue/1.2-3'))}
        String type="%{(#xx=#context[";

        if(contentType!=null&&contentType.contains(type))
        {
        	System.out.println("报文头 Content-Type 包含["+type+"]是非法的!");
            EmpExecutionContext.error("报文头 Content-Type 包含["+type+"]是非法的!");
        	response.setContentType("text/html;charset=UTF-8");
        	response.setCharacterEncoding("UTF-8");
        	response.setStatus(403);
        	response.getWriter().print("<font size=6 color=red>对不起，您的请求非法，系统拒绝响应!</font>");
        	flag=true;
        }else{
        	flag=false;
        }
        return flag;
    }


}
