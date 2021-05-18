package com.montnets.emp.common.servlet;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.montnets.emp.common.constant.EMPException;
import com.montnets.emp.common.constant.ErrorCodeInfo;
import com.montnets.emp.common.constant.IErrorCode;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.PageInfo;

/**
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-9 下午04:29:58
 */

public class BaseServlet extends HttpServlet{

	private static final long serialVersionUID = -4906192663471241287L;

	protected static final String ERROR = "error";

	/**
	 * doget方法
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * dopost方法
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{
		request.setCharacterEncoding(StaticValue.ENCODING);
		response.setCharacterEncoding(StaticValue.ENCODING);
        String method = request.getParameter("method");
		// 请求路径
		String requestUrl = request.getServletPath()
				+ (request.getPathInfo() == null ? "" : request.getPathInfo());
        if(method == null){
            method = "find";
        }
		try {
			if (requestUrl.indexOf("_") > 0 && requestUrl.indexOf(".htm") > 0) {
				request.setAttribute("rTitle", requestUrl.substring(requestUrl
						.indexOf("_") + 1, requestUrl.indexOf(".htm")));
			}
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取请求路径异常。url:"+requestUrl+",method:"+method);
            return;
		}
        //是否正确响应请求方法完成
        boolean isComplete = false;
		try {
            HttpSession session = request.getSession();
            String tkn = request.getParameter("tkn");
            if (tkn != null) {
                //当前tkn获取登录对象信息为空
                if(StaticValue.getLoginInfoMap().get(tkn) == null){
                    EmpExecutionContext.error("tkn获取登录对象信息为空或与当前请求会话不一致，url:"+requestUrl+",method:"+method);
                    return;
                }
                //tkn对应会话与当前请求会话不一致
                if (!StaticValue.getLoginInfoMap().get(tkn).checkSessionId(session.getId())) {
                    EmpExecutionContext.error("tkn对应会话与当前请求会话不一致，url:"+requestUrl+",method:"+method);
                    return;
                }
            }
            String checkResult = checkRequest(request);
            if(!"true".equals(checkResult)){
                EmpExecutionContext.error("请求方法参数不合法！"+checkResult);
                return;
            }
            //请求地址和方法名存在
            if(requestUrl != null && !"null".equals(requestUrl)&&requestUrl.trim().length() > 0 
            		&& method != null && !"null".equals(method) && method.trim().length() > 0)
            {
            	//记录短信预览、发送请求参数日志
            	reveiceParamsLog(request, requestUrl, method);
            }
            Method clazzMethod = getValidMethod(method);
            if(clazzMethod == null){
                EmpExecutionContext.error("请求方法名称不合法！ip:"+getIpAddr(request)+", url:"+requestUrl+",method:"+method);
                return;
            }
			Object[] Objparams = { request, response };
			clazzMethod.invoke(this, Objparams);
            isComplete = true;
		} catch (IllegalAccessException e) {
            EmpExecutionContext.error(e,"baseSvt方法转发异常，非法的方法访问权限！url:"+requestUrl+",method:"+method);
        } catch (Exception e) {
            EmpExecutionContext.error(e, "baseSvt方法转发异常。url:"+requestUrl+",method:"+method);
        }finally {
            if(!isComplete){
                response.sendRedirect(request.getContextPath() + "/common/logoutEmp.html");
            }
        }
    }

	/**
	 * 设置分页对象
	 * @param pageInfo 分页对象
	 * @param request 请求对象
	 * @return 是否第一次登录
	 */
	public boolean pageSet(PageInfo pageInfo ,HttpServletRequest request )
	{
		boolean isFirstEnter;
		String pageIndex = request.getParameter("pageIndex");
		String pageSize = request.getParameter("pageSize");
		String totalPage = request.getParameter("totalPage");
		String totalRec = request.getParameter("totalRec");
		String needNewData = request.getParameter("needNewData");

		//分页信息类
		isFirstEnter = pageIndex== null && pageSize == null;
		if(pageIndex != null && checkNumber(pageIndex))
		{
			pageInfo.setPageIndex(Integer.valueOf(pageIndex));
		}
		if(pageSize != null && checkNumber(pageSize))
		{
			pageInfo.setPageSize(Integer.valueOf(pageSize));
		}
		if(totalPage != null && checkNumber(totalPage))
		{
			pageInfo.setTotalPage(Integer.valueOf(totalPage));
		}
		if(totalRec != null && checkNumber(totalRec))
		{
			pageInfo.setTotalRec(Integer.valueOf(totalRec));
		}
		if(needNewData != null && checkNumber(needNewData))
		{
			pageInfo.setNeedNewData(Integer.parseInt(needNewData));
		}
		return isFirstEnter;
	}

	/**
	 * 验证数字
	 * @param str 传入字符串
	 * @return
	 */
	private boolean checkNumber(String str)
	{
		for (int k = str.length(); --k >= 0;)
		{
			if (!Character.isDigit(str.charAt(k)))
			{
				return false;
			}
		}
		return true;
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
     */
    public String getIpAddr(HttpServletRequest request) {
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

    /**
     * 获取当前登录用户
     * @param request
     * @return
     * @throws EMPException
     */
    public LfSysuser getLoginUser(HttpServletRequest request) throws EMPException
    {
        HttpSession session = request.getSession(false);
        if(session == null)
        {
            EmpExecutionContext.error(ErrorCodeInfo.getInstance(StaticValue.ZH_CN).getErrorDes(IErrorCode.V10017));
            throw new EMPException(IErrorCode.V10017);
        }
        Object obj = session.getAttribute("loginSysuser");
        String langName = (String) request.getSession().getAttribute(StaticValue.LANG_KEY);
        if(obj == null)
        {
            EmpExecutionContext.error(ErrorCodeInfo.getInstance(langName).getErrorDes(IErrorCode.V10018));
            throw new EMPException(IErrorCode.V10018);
        }
        return (LfSysuser)obj;
    }

    /**
     * 判断是否为有效的请求 参数企业编码以及请求IP地址是否与登录时保持一致
     * @param request
     * @return
     * @throws Exception
     */
    public String checkRequest(HttpServletRequest request) throws EMPException
    {
        if(!request.getServletPath().endsWith(".htm")){
            return "true";
        }
        String result = null;
        HttpSession session = request.getSession(false);
        if(session == null){
            result = "会话已失效。";
            return result;
        }
        LfSysuser sysuser = getLoginUser(request);
//        //请求ip
//        String requestIP = getIpAddr(request);
//        //登录ip
//        String loginIP = (String) session.getAttribute("loginIP");
//        //登录ip与请求ip不一致 返回错误信息
//        if(loginIP != null && !loginIP.equals(requestIP)){
//            result = "请求ip（"+requestIP+"）与登录ip("+loginIP+")不一致。";
//            return result;
//        }
        //当前登录为100000用户 则不进行非法参数验证
        if("100000".equals(sysuser.getCorpCode()))
        {
            return "true";
        }
        String lgcorpcode = getCorpCode(request);
        //请求中无企业编码参数
        if(lgcorpcode == null)
        {
            return "true";
        }
        //只处理为六位数字时的比较
        if(lgcorpcode.length()==6 && StringUtils.isNumeric(lgcorpcode)){
            //若参数中存在企业编码且值与session中不一致 则 返回错误信息
            if(!sysuser.getCorpCode().equals(lgcorpcode)){
                result = "请求企业编码（"+lgcorpcode+"）与登录用户企业编码("+sysuser.getCorpCode()+")不一致。";
                return result;
            }
        }else{
            // 请求路径
            String requestUrl = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
            String method = (request.getParameter("method")==null?"find":request.getParameter("method"));
            return "请求企业编码不正确。"+"url:"+requestUrl+",method:"+method;
        }

        return "true";
    }

    /**
     * 获取请求参数中匹配的企业编码
     * @param request
     * @return
     */
    public String getCorpCode(HttpServletRequest request)
    {
        String result = null;
        //请求所有参数集合
        Map<String,String[]> map = request.getParameterMap();
        Iterator<String> its = map.keySet().iterator();
        String key = null;
        String _key = null;
        while (its.hasNext()){
            key = its.next();
            //参数名去除非单词字符 并且转化为小写
            _key = key.replaceAll("[^a-zA-Z]","").toLowerCase();
            if(_key.contains("corpcode")){
                result = map.get(key)[0];
                break;
            }
        }
        return result;
    }
    
    public void checkTkn(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
    	// 请求路径
    	String requestUrl = request.getServletPath();
		EmpExecutionContext.info("tkn验证通过"+"url:"+requestUrl);
		response.getWriter().print("true");
	}
    
    /**
     * 记录短信预览、发送请求日志
     * @description    
     * @param request
     * @param requestUrl
     * @param method       			 
     * @author chentingsheng <cts314@163.com>
     * @datetime 2016-8-30 下午02:30:31
     */
    public void reveiceParamsLog(HttpServletRequest request, String requestUrl, String method)
    {
    	try
		{
			//相同内容预览
			if(requestUrl.indexOf("ssm_previewSMS.htm") >= 0 && method.indexOf("preview") >= 0)
			{
				//任务ID
				String taskId = request.getParameter("taskId");
				//当前登录操作员id
				//String lguserid = request.getParameter("lguserid");
                //漏洞修复 session里获取操作员信息
                String lguserid = SysuserUtil.strLguserid(request);


                EmpExecutionContext.info("相同内容群发，预览BaseServlet接收参数，userid:"+lguserid+"，taskId:"+taskId);
			}
			//相同内容发送
			else if(requestUrl.indexOf("ssm_sendBatchSMS.htm") >= 0 && method.indexOf("add") >= 0)
			{
				//任务ID
				String taskId = request.getParameter("taskId");
				//当前登录操作员id
				//String lguserid = request.getParameter("lguserid");
                //漏洞修复 session里获取操作员信息
                String lguserid = SysuserUtil.strLguserid(request);

                //当前登录企业
				String lgcorpcode = request.getParameter("lgcorpcode");
				// 提交类型
				String bmtType = request.getParameter("bmtType");
				// 发送账号
				String spUser = request.getParameter("spUser");
				// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
				String sendType = request.getParameter("sendType");
				EmpExecutionContext.info("相同内容群发，发送BaseServlet接收参数，userid:"+lguserid
						+"，corpCode:"+lgcorpcode+"，taskId:"+taskId+"，sendType:"+sendType+"，bmtType:"+bmtType+"，spUser:"+spUser);
			}
			//不同内容预览
			else if(requestUrl.indexOf("dsm_previewSMS.htm") >= 0 && method.indexOf("preview") >= 0)
			{
				//任务ID
				String taskId = request.getParameter("taskId");
				//当前登录操作员id
				//String lguserid = request.getParameter("lguserid");
                //漏洞修复 session里获取操作员信息
                String lguserid = SysuserUtil.strLguserid(request);

                //是否过滤重号
				String checkrepeat = request.getParameter("checkrepeat");
				EmpExecutionContext.info("不同内容群发，预览BaseServlet接收参数，userid:"+lguserid+"，taskId:"+taskId+"，checkrepeat:"+checkrepeat);
			}
			//不同内容发送
			else if(requestUrl.indexOf("dsm_ssendDiffSMS.htm") >= 0 && method.indexOf("send") >= 0)
			{
				//任务ID
				String taskId = request.getParameter("taskId");
				//当前登录操作员id
				//String lguserid = request.getParameter("lguserid");
                //漏洞修复 session里获取操作员信息
                String lguserid = SysuserUtil.strLguserid(request);

                //当前登录企业
				String lgcorpcode = request.getParameter("lgcorpcode");
				// 提交类型
				String bmtType = request.getParameter("bmtType");
				// 发送账号
				String spUser = request.getParameter("spUser");
				// 发送类型：1-相同内容短信 2－动态模板短信；3－文件内容短信；4-客户群组短信
				String sendType = request.getParameter("sendType");
				EmpExecutionContext.info("不同内容群发，发送BaseServlet接收参数，userid:"+lguserid
						+"，corpCode:"+lgcorpcode+"，taskId:"+taskId+"，sendType:"+sendType+"，bmtType:"+bmtType+"，spUser:"+spUser);
			}
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "记录短信预览、发送请求日志失败，requestUrl:"+requestUrl+"，method:"+method);
		}
    }
}
