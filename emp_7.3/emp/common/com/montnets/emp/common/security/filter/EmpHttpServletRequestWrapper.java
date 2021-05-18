package com.montnets.emp.common.security.filter;

import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.security.cache.SecurityCache;
import com.montnets.emp.common.security.constant.SecurityConst;
import com.montnets.emp.common.security.service.EmpSecurityService;
import com.montnets.emp.common.security.service.impl.EmpSecurityServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.List;
import java.util.Map;

/**
 * @author liangHuaGeng
 * @Title: EmpHttpServletRequestWrapper
 * @ProjectName emp_7.3
 * @Description: TODO
 * @date 2019/1/16 10:10
 */
public class EmpHttpServletRequestWrapper extends HttpServletRequestWrapper {
	public static EmpSecurityFilter empSec=new EmpSecurityFilter();

    /**
     * 原始 Request
     */
    protected HttpServletRequest oldRequest;
    protected EmpSecurityService securityService = new EmpSecurityServiceImpl();
    /**
     * 特殊字符过滤器 放权的url和method
     */
    protected Map<String, Map<String, List<String>>> urlMethodMapOfChar;
    /**
     * 是否字符转义
     */
    protected boolean charFlag = true;
    /**
     * 是否 sql 过滤
     */
    protected boolean sqlFlag;

    protected Map<String, List<String>> escapeParam;

    public EmpHttpServletRequestWrapper(HttpServletRequest request, boolean sqlFlag) {
        super(request);
        this.oldRequest = request;
        this.sqlFlag = sqlFlag;
        if (SecurityCache.INSTANCE.getUrlMethodMapOfChar().isEmpty()) {
            this.urlMethodMapOfChar = securityService.getUrlMethodMapOfChar("UrlMethodAllowedOfChar.properties");
            SecurityCache.INSTANCE.setUrlMethodOfCharCache(this.urlMethodMapOfChar);
        } else {
            this.urlMethodMapOfChar = SecurityCache.INSTANCE.getUrlMethodMapOfChar();
        }
        if(SecurityCache.INSTANCE.getEscapeParam().isEmpty()){
            this.escapeParam= securityService.getEscapeParam();
            SecurityCache.INSTANCE.setEscapeParam(this.escapeParam);
        }else{
            this.escapeParam=SecurityCache.INSTANCE.getEscapeParam();
        }
    }

    /**
     * 获取最原始的request的静态方法
     *
     * @return
     */
    public static HttpServletRequest getOldRequest(HttpServletRequest req) {
        if (req instanceof EmpHttpServletRequestWrapper) {
            return ((EmpHttpServletRequestWrapper) req).getOrgRequest();
        }
        return null;
    }

    /**
     * 覆盖getParameter方法，将参数名和参数值都做xss & sql过滤。<br/>
     * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
     * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
     */
    @Override
    public String getParameter(String name) {
        try {
            //获取URL
            String tempURL = oldRequest.getRequestURI();
            //判断是否是可以忽略走特殊字符转义的action
            String action = tempURL.substring(tempURL.lastIndexOf("/") + 1, tempURL.length());
            if (null != urlMethodMapOfChar) {
                if (urlMethodMapOfChar.get(SecurityConst.ASTERISK) != null
                        && (urlMethodMapOfChar.get(SecurityConst.ASTERISK).get(SecurityConst.ASTERISK).contains(SecurityConst.ASTERISK))) {
                    //如果配置了*=*&*，则全部不走特殊字符过滤
                    charFlag = false;
                } else if (urlMethodMapOfChar.get(action) != null) {
                    //获取map里的url
                    //获取map里的method
                    Map<String, List<String>> map = urlMethodMapOfChar.get(action);
                    //请求方法的method
                    String method = oldRequest.getParameter("method");
                    boolean condition = (map.get(SecurityConst.ASTERISK) != null && map.get(SecurityConst.ASTERISK).contains(SecurityConst.ASTERISK))
                            || (method != null && map.get(method) != null && map.get(method).contains(SecurityConst.ASTERISK))
                            || (method != null && map.get(method) != null && map.get(method).contains(name));
                    if (condition) {
                        charFlag = false;
                    }
                }
            }

            String value = super.getParameter(name);

            // 对urlEncode编码的参数进行urlEncode 解码
            //value = empSec.urlDecode(value);
            
            if (null != value) {
                //解决%和_不能单独发送的问题
                if(escapeParam.get(action)!=null&&escapeParam.get(action).contains(name)){
                    value = securityService.xssEncodeForSend(value, charFlag, sqlFlag);
                }else {
                    value = securityService.xssEncode(value, charFlag, sqlFlag);
                }
            }
            return value;
        } catch (Exception e) {
            EmpExecutionContext.error(e, "EmpHttpServletRequestWrapper.getParameter() 发现异常!");
        }
        return SecurityConst.EMPTY;
    }

    /**
     * 获取最原始的request
     *
     * @return
     */
    public HttpServletRequest getOrgRequest() {
        return oldRequest;
    }

}
