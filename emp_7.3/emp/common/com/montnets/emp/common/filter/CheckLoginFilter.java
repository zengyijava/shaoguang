package com.montnets.emp.common.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.biz.SmsBiz;
import com.montnets.emp.common.biz.SysNoticeBiz;
import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.tools.JsonReturnUtil;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.gateway.AProInfo;
import com.montnets.emp.entity.monitoronline.LfMonOnlcfg;
import com.montnets.emp.entity.monitoronline.LfMonOnluser;
import com.montnets.emp.entity.notice.LfNotice;
import com.montnets.emp.entity.system.LfUser2skin;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfPrivilege;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.MD5;

public class CheckLoginFilter implements Filter {

	protected FilterConfig filterConfig = null;

	private String sessionKey = null;

    private BaseBiz baseBiz = new BaseBiz();

    private LoginBiz biz = new LoginBiz();




	/**
	 * 执行登录过滤
	 */
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		//请求
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		//响应
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		//路径
		String uri =request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
        if("/common/logoutEmp.html".equals(uri)){
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
		String noFilterStr = "";
		if (uri.indexOf("_") > uri.lastIndexOf("/"))
		{
			noFilterStr = uri.substring(uri.lastIndexOf("/")+1,uri.lastIndexOf("_"));
			if ("init".equals(noFilterStr))
			{
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
			String wxurl=uri.substring(0,uri.lastIndexOf("/")+1);
			if ("wx".equals(noFilterStr)&&"/file/wx/PAGE/".equals(wxurl))
			{
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}
		}
		//如果是单点登录的方式，则直接进入下次过滤
//		if(uri.contains("singleSign.htm")){
//				filterChain.doFilter(servletRequest, servletResponse);
//				return ;			
//		}
		//
		if(uri.contains("singleSign.htm")|| uri.contains("login_new.jsp")||uri.contains("case.jsp")||uri.contains("index_new.jsp")||uri.contains("contact.jsp")){
				filterChain.doFilter(servletRequest, servletResponse);
				return ;			
		}

        HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute(sessionKey) == null) {
			
			//如果是带了异步请求的标示
			if("yes".equals(request.getParameter("isAsync")))
			{
				//返回已经退出登录
				response.getWriter().print("outOfLogin");
				return;
			}
			//当用户未登录时，重新访问main.jsp页面时，由于此页面是框架页面，页面上有两次请求，所以会导致页面给出提示
			//两次．所以再这儿判断一下，如果是top.jsp页面请求的则只返回，不给出提示．
			if(uri.contains("top.jsp"))
			{				
				return;
			}
			else
			{
			    if(isAsyncRequest(request)){
                    JsonReturnUtil.fail(403,"user not login",request,response);
                    return;
                }
				//使用EMP服务器URL
				if(StaticValue.getUseServerUrlFlag() - 1 == 0)
				{
					//请求HOST
					String host = ","+request.getHeader("Host")+",";
					//EMP服务器URL
					String empServerUrl = StaticValue.getServerUrl();
					//未配置EMP服务器URL
					if(empServerUrl == null || empServerUrl.trim().length() < 1)
					{
						EmpExecutionContext.error("请求过滤，开启使用EMP服务器URL标识，但未设置服务器URL，HOST:"+host+"，empServerUrl:"+empServerUrl);
						return;
					}
					//请求HOST在配置的EMP服务器URL中不存在，直接返回
					if(empServerUrl.indexOf(host) < 0)
					{
						EmpExecutionContext.error("请求过滤，请求HOST在配置的服务器URL中不存在，HOST:"+host+"，empServerUrl:"+empServerUrl);
						return;
					}
				}
				// 登录页 修复会话标识未更新漏洞
				session = SysuserUtil.regenerateSessionId(request);
				//跳转到checkLogin.html
				response.sendRedirect(request.getContextPath()+"/common/logoutEmp.html");//common/logoutEmp.html
				return;
			}
		}

        if(uri.equals("/init.htm"))
        {
            initLoginParams(request,response,session);
            return;
        }

		//执行过滤
		filterChain.doFilter(servletRequest, servletResponse);
	}
	/**
	 * 摧毁方法
	 */
	public void destroy() {
		sessionKey = null;
	}

	/**
	 * 初使化方法
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;

		//获取web.xml配置的sessionkey
		sessionKey = filterConfig.getInitParameter("sessionKey");
	}

    //加载登录相关信息
    public void initLoginParams(HttpServletRequest request,HttpServletResponse response,HttpSession session) throws IOException {
        response.setContentType("text/html");
        PrintWriter out =response.getWriter();
        String result = "false";
        try {
            String tkn = request.getParameter("tkn");
            String ip = session.getAttribute("loginIP").toString();
            LfSysuser sysuser = (LfSysuser) session.getAttribute(sessionKey);
            AuthenAtom authen = new AuthenAtom();
            String langName = (String)session.getAttribute(StaticValue.LANG_KEY);
            // 获得当前登录用户所有的按钮权限
            Map<String, String> btnMap = biz.getBtnFuntionMap(langName, sysuser.getUserId().toString());

            // 获得当前登录用户所用权限模块的位置
            Map<String, String> titleMap = biz.getTitleMap(sysuser.getUserId().toString());
            
            List<LfPrivilege> prList = biz.getMenuByPrivCodeAsc(sysuser.getUserId().toString());
            //从LF_SHORTTEMP表中获取数据，添加到LfPrivilege
            //企业编码
			String corpcode = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getCorpCode();
			//企业ID
			long userId = ((LfSysuser) request.getSession(false).getAttribute(StaticValue.SESSION_USER_KEY)).getUserId();
            List<LfPrivilege> addList = biz.getLfShortTempList(corpcode,userId);
            prList.addAll(addList);

            //判断当前是否开启了机构计费统计
            Boolean flag =  isOpenDepBilling();

            Map<String, List<LfPrivilege>> priMap = new HashMap<String, List<LfPrivilege>>();
            for (LfPrivilege privi : prList) {
                //该模块没有配置
            	String url = privi.getMenuSite();
            	if(url.contains("?")){
            		url = url.split("\\?")[0];
            	}
                if (StaticValue.getInniMenuMap().get(url) == null) {
                    continue;
                }

                if(StaticValue.getCORPTYPE() == 0 && !flag && "1600-1320".equals(privi.getMenuCode())){
                    continue;
                }
                String resourceId = privi.getResourceId().toString();
                if (priMap.get(resourceId) != null) {
                    priMap.get(resourceId).add(privi);
                } else {
                    List<LfPrivilege> pri = new ArrayList<LfPrivilege>();
                    pri.add(privi);
                    priMap.put(resourceId, pri);
                }
            }

            // 查找网关认证系信息
            List<AProInfo> proList = baseBiz.getEntityList(AProInfo.class);
            //网关认证信息
            AProInfo aProInfo = null;
            if(proList != null && proList.size() > 0)
            {
                aProInfo = proList.get(0);
            }

            // 加载系统公告
            LfNotice sysNotice = new SysNoticeBiz().getNotice("100000");

            // 企业编码
            String enpCode = sysuser.getCorpCode();

            // 获取当前登录用户的企业信息
            LinkedHashMap<String, String> conditionMap1 = new LinkedHashMap<String, String>();
            conditionMap1.put("corpCode", enpCode);
            List<LfCorp> returnLists = baseBiz.getByCondition(LfCorp.class, conditionMap1, null);

            //登录企业
            LfCorp corp = returnLists.get(0);

            try
            {
                // 是否加载监控模块，16：监控
                if(new SmsBiz().isWyModule("16"))
                {
                    LfMonOnluser lfMonOnluser = new LfMonOnluser();
                    // 会话ID
                    lfMonOnluser.setSesseionid(session.getId());
                    // 企业编号
                    lfMonOnluser.setCorpcode(sysuser.getUserCode());
                    // 登录账号
                    lfMonOnluser.setUsername(sysuser.getUserName());
                    // 操作员名称
                    lfMonOnluser.setName(sysuser.getName());
                    // 用户编号
                    lfMonOnluser.setUserid(sysuser.getUserId());
                    // 所属机构ID
                    lfMonOnluser.setDepid(sysuser.getDepId());
                    // 所属机构名称
                    lfMonOnluser.setDepName(null);
                    // 登录时间
                    lfMonOnluser.setLogintime(new Timestamp(System.currentTimeMillis()));
                    // 登录IP
                    lfMonOnluser.setLoginaddr(ip);
                    // 服务器编号
                    lfMonOnluser.setServernum(StaticValue.getServerNumber());
                    BaseBiz baseBiz = new BaseBiz();
                    LoginBiz loginBiz = new LoginBiz();
                    //设置用户详情
                    loginBiz.saveOrUpdate(lfMonOnluser, "SESSEION_ID");
                    // 设置在线用户详情
                    loginBiz.setOnlineUser();
                    //更新在线用户人数
                    loginBiz.setMonOnlcfg();
                    List<LfMonOnlcfg> lfMonOnlcfgList = baseBiz.getByCondition(LfMonOnlcfg.class, null, null);
                    if(lfMonOnlcfgList != null && lfMonOnlcfgList.size() > 0)
                    {
                        synchronized (StaticValue.getMonOnlinecfg())
                        {
                            // 在线用户数
                            StaticValue.getMonOnlinecfg().setOnlinenum(lfMonOnlcfgList.get(0).getOnlinenum());
                        }
                    }
                }
            }
            catch (Exception e)
            {
                EmpExecutionContext.error(e, "设置在线用户告警信息异常！");
            }

            // 过期提醒 may add 登陆成功之后再查看执行密码规则
            LinkedHashMap corpConf = getLfCorpConf(sysuser);
            String updatetime = (String) corpConf.get("result");
            List<LfCorpConf> lfCorpConfList = null;
            if(corpConf.get("lfCorpConfList") != null)
            {
                lfCorpConfList = (List<LfCorpConf>) corpConf.get("lfCorpConfList");
            }
            //密码是否超期
            String passOverdue = null;
            //密码提醒 超期剩余小时数
            Long remainHours = null;
            long passOverDate = 0;
            if(sysuser.getPwdupdatetime() != null)
            {
                if(!"".equals(updatetime) && updatetime != null)
                {
                    String[] array = updatetime.split("@");
                    if(array.length == 2)
                    {
                        //修改周期
                        String intervalStr = array[0];
                        //提醒天数 剩余多少天内提醒
                        String warnDayStr = array[1];
                        // 修改周期 提醒处理
                        if(StringUtils.isNumeric(intervalStr) && !"0".equals(intervalStr))
                        {
                            // 当前时间与修改时间的相差小时数
                            long hours = (System.currentTimeMillis()-sysuser.getPwdupdatetime().getTime())/ 1000 / 60 / 60;
                            long interval = Long.parseLong(intervalStr)*24;
                            if(hours >= interval){//超出修改周期 则登录需密码修改
                                passOverdue = "true";
                            }else{//周期范围内
                                passOverdue = "false";
                                long warnHours = 0L;
                                if(StringUtils.isNumeric(warnDayStr) && !"0".equals(warnDayStr)){
                                    warnHours = Long.parseLong(warnDayStr)*24;
                                }
                                //在提醒周期内
                                if(hours >= interval-warnHours){
                                    remainHours = interval - hours;
                                    passOverDate = (long)(Math.ceil(remainHours/24.0));
                                }
                            }
                        }
                    }
                }
            }
            // 为了处理给修改密码提示（在文本框中显示）
            valid(request, lfCorpConfList);

            // 读取皮肤cookie,获取主题路径
            String ftameUrl = getSkinConfig(request, response, sysuser);
            
            // 获取设置的语言选项，放入session
            getLanguageConfig(request);
            
            // 登录信息
            LoginInfo loginInfo = new LoginInfo();
            String username = sysuser.getUserName();
            String pass = sysuser.getPassword();
            tkn = MD5.getMD5Str(tkn + username + enpCode);
            setLoginInfoMap(loginInfo, sysuser, tkn, session.getId(), ip);
            String loginparams = loginInfo.toString() + "&" + tkn;
            String NameAndPWord = null;
            // 如果当前登录用户的用户名与密码相同．或者admin用户首次登录（这里首次登录使用了comment字段的特殊意义 为空 认为是首次）则弹出修改密码页面
            if(MD5.getMD5Str(username+username.toLowerCase()).equals(pass) || ("admin".equals(username)&&StringUtils.isBlank(sysuser.getComments())))
            {
                loginparams += "&NameAndPWordSame";
                NameAndPWord = "NameAndPWordSame";
            }
            else
            {
                loginparams += "&NameAndPWordNoSame";
            }
            loginparams +="&" + ftameUrl + "&" + passOverDate;

            String depName = null;
			if(StaticValue.getCORPTYPE() == 0)
			{
				// 如果是单企业的登录的页面上的企业名称取lf_dep表中为1的那条记录的企业名称.
				LinkedHashMap<String, String> conditionMapbydep = new LinkedHashMap<String, String>();
				conditionMapbydep.put("depId", "1");
				List<LfDep> depList = baseBiz.getByCondition(LfDep.class, conditionMapbydep, null);
				if(depList != null && depList.size() > 0)
				{
					LfDep dep = depList.get(0);
					depName = dep.getDepName();
				}
			}
            
            session.setAttribute("loginCorp", corp);
            session.setAttribute("depName", depName);
            session.setAttribute("AProInfo", aProInfo);
            session.setAttribute("prList", prList);
            session.setAttribute("priMap", priMap);
            session.setAttribute("btnMap", btnMap);
            session.setAttribute("titleMap", titleMap);
            session.setAttribute("sysNotice", sysNotice);
            session.setAttribute("NameAndPWord", NameAndPWord);
            session.setAttribute("passOverdue", passOverdue);
            session.setAttribute("Logindays", passOverDate+"");
            session.setAttribute("loginparams",loginparams);

            result = ftameUrl+"&"+tkn;
        } catch (Exception e) {
            EmpExecutionContext.error(e,"处理登录初始化信息异常！");
        }
        finally {
            out.print(result);
        }

    }

    private boolean isOpenDepBilling() {
	    Boolean flag = false;
	    try{
            List<LfCorp> lfCorps = baseBiz.getByCondition(LfCorp.class, null, null);
            flag = lfCorps.get(0).getIsBalance() == 1;
        }catch (Exception e){
	        EmpExecutionContext.error("查询企业表异常");
        }
	    return flag;
    }

    /***
     * 用于校验密码是否符合一个规则
     * 位数要求、组合形式，初始化密码不受此条件约束，修改密码受此限制，当不满足条件时，提示：“密码设置不得少于n位，且至少包含数字、字母、符号”，
     * 保存失败
     * @return
     */
    public boolean valid(HttpServletRequest request, List<LfCorpConf> lfCorpConfList)
    {
        boolean flag = false;
        try
        {
            String text = "";
            String bit = "0";
            String number = "";
            String character = "";
            String sign = "";

            for (LfCorpConf conf : lfCorpConfList) {
                // 位数
                if ("pwd.count".equals(conf.getParamKey())) {
                    bit = conf.getParamValue();
                }
                // 组合形式
                if ("pwd.combtype".equals(conf.getParamKey())) {
                    String cunt = conf.getParamValue();
                    if (cunt != null) {
                        String[] per = cunt.split(",");
                        if (per.length == 1) {
                            number = per[0];
                        } else if (per.length == 2) {
                            number = per[0];
                            character = per[1];
                        } else if (per.length == 3) {
                            number = per[0];
                            character = per[1];
                            sign = per[2];
                        }
                    }
                }
            }
            String temp = "";
            if(!"".equals(number) && number != null && number != "null")
            {
                temp = "数字、" + temp;
            }
            if(!"".equals(character) && character != null && character != "null")
            {
                temp = "字母、" + temp;
            }
            if(!"".equals(sign) && sign != null && sign != "null")
            {
                temp = "符号、" + temp;
            }

            if(!"".equals(bit) && !"0".equals(bit) && bit != null)
            {
                if(!"".equals(temp))
                {
                    text = "密码" + bit + "位以上且含";
                }
                else
                {
                    text = "密码" + bit + "位以上";
                }
            }
            else
            {
                if(!"".equals(temp))
                {
                    text = "密码包含";
                }
            }
            if(!"".equals(temp))
            {
                temp = temp.substring(0, temp.length() - 1);
                text = text + temp;
            }
            HttpSession session = request.getSession(false);
            if(session != null){
                session.setAttribute("passWordLimt", text);
                flag = true;
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询密码设置规则异常！");
        }
//        finally
//        {
//            return flag;
//        }
        return flag;

    }

    /***
     * 获得系统中配置信息
     *
     * @param sysuser
     * @return
     */
    public LinkedHashMap getLfCorpConf(LfSysuser sysuser)
    {

        String result = "";
        LinkedHashMap returnValue = new LinkedHashMap();

        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
        conditionMap.put("corpCode", sysuser.getCorpCode());
        List<LfCorpConf> lfCorpConfList = null;
        String modiftime = "0";
        String remind = "0";
        try
        {
            BaseBiz baseBiz = new BaseBiz();
            lfCorpConfList = baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);

            for (LfCorpConf conf : lfCorpConfList) {
                // 修改周期
                if ("pwd.upcycle".equals(conf.getParamKey())) {
                    if (!"".equals(conf.getParamValue())) {
                        modiftime = conf.getParamValue();
                    }
                }
                // 过期提醒
                if ("pwd.pastalarm".equals(conf.getParamKey())) {
                    if (!"".equals(conf.getParamValue())) {
                        remind = conf.getParamValue();
                    }
                }

            }
            result = modiftime + "@" + remind;
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "查询密码设置规则异常！");
        }
        returnValue.put("result", result);
        returnValue.put("lfCorpConfList", lfCorpConfList);
        return returnValue;
    }

    /**
     * 设置登录信息
     *
     * @param loginInfo
     * @param sysuser
     * @param tkn
     * @param sessionId
     */
    private void setLoginInfoMap(LoginInfo loginInfo, LfSysuser sysuser, String tkn, String sessionId, String loginAddr)
    {
        try
        {
            // 用户id
            loginInfo.setUserId(sysuser.getUserId());
            // 用户guid
            loginInfo.setGuId(sysuser.getGuId());
            // 用户名
            loginInfo.setUserName(sysuser.getUserName());
            // 用户编码
            loginInfo.setUserCode(sysuser.getUserCode());
            // 企业编码
            loginInfo.setCorpCode(sysuser.getCorpCode());
            // sessionid
            loginInfo.setSessionId(sessionId);
            //ip地址
            loginInfo.setLoginIP(loginAddr);

            // 判断是否已存在登陆对象
            Map<String, LoginInfo> loginMap = StaticValue.getLoginInfoMap();
            Iterator<String> itr = loginMap.keySet().iterator();
            String key;
            String removeKey = null;
            while(itr.hasNext())
            {
                key = itr.next();
                LoginInfo logininfo = loginMap.get(key);

                if(sessionId.equals(logininfo.getSessionId()))
                {
                    removeKey = key;
                    break;
                }
            }
            if(removeKey != null)
            {
                StaticValue.getLoginInfoMap().remove(removeKey);
            }
            StaticValue.getLoginInfoMap().put(tkn, loginInfo);


        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e, "设置登录操作员信息失败！");
        }
    }

    /**
     * 操作皮肤cookie
     *
     * @param request
     * @param response
     * @throws Exception
     */
    private String getSkinConfig(HttpServletRequest request, HttpServletResponse response, LfSysuser sysuser) throws Exception
    {

        String frameUrl = SystemGlobals.getValue("emp.web.frame");
        LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

        conditionMap.put("userid", sysuser.getUserId().toString());

        String skin;
        BaseBiz baseBiz = new BaseBiz();
        List<LfUser2skin> userskinList = baseBiz.getByCondition(LfUser2skin.class, conditionMap, null);
        LfUser2skin u2S;
        if((userskinList != null) && (userskinList.size() > 0))
        {
            u2S = userskinList.get(0);
            skin = u2S.getSkincode();
            frameUrl = u2S.getThemecode();
        }
        else
        {
            u2S = new LfUser2skin();
            u2S.setSkincode("default");
            u2S.setUserid(sysuser.getUserId());
            u2S.setSid(Long.valueOf(1L));
            u2S.setThemecode(frameUrl);

            baseBiz.addObj(u2S);
            skin = u2S.getSkincode();
        }
        String path = request.getContextPath();
        HttpSession session = request.getSession(false);
        session.setAttribute("stlyeSkin", path + "/frame/" + frameUrl + "/skin/" + skin);
        session.setAttribute("emp.web.frame", frameUrl);
        return frameUrl;
    }

    /**
     *  获取设置的语言选项，放入session
     * @param request
     */
    private void getLanguageConfig(HttpServletRequest request){
        List<String> langs = new ArrayList<String>();
        String multiLanguageEnable = SystemGlobals.getValue("multiLanguageEnable");
        if("Yes".equals(multiLanguageEnable)){
            langs = Arrays.asList(SystemGlobals.getValue("selectedLanguage").split(","));
        }else{
        	langs.add(SystemGlobals.getValue("defaultLanguage"));
        }
       /* if("No".equals(multiLanguageEnable)|| multiLanguageEnable == null || "".equals(multiLanguageEnable)){
            langs.add(SystemGlobals.getValue("defaultLanguage"));
        }*/
        HttpSession session = request.getSession(true);
        if (session != null) {
            session.setAttribute("multiLanguage", langs);
        }
    }

    /**
     *  判断是否异步请求
     * @return  boolean
     */
    public static boolean isAsyncRequest(HttpServletRequest request){
        //String requestType = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(request.getHeader("x-requested-with"))&&"true".equals(request.getHeader("meditor"));
    }
}
