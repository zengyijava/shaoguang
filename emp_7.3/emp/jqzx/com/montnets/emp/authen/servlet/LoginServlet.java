package com.montnets.emp.authen.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.montnets.emp.authen.atom.AuthenAtom;
import com.montnets.emp.common.biz.*;
import com.montnets.emp.common.constant.*;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.dao.impl.GenericEmpDAO;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfPrivilegeAndMenuVo;
import com.montnets.emp.datasource.servlet.systemGlobalsSvt;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.system.LfSpeUICfg;
import com.montnets.emp.entity.system.LfThiMenuControl;
import com.montnets.emp.entity.sysuser.LfDep;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;

/**
 * @project emp
 * @company ShenZhen Montnets Technology CO.,LTD.
 * @datetime 2011-3-5 上午10:42:28
 * @description
 */
@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet
{
	// 操作模块
	public static final String	auditModule	= "登录";

	// 操作用户
	String						opSper		= StaticValue.OPSPER;

	private SuperOpLog			spLog		= new SuperOpLog();

	private BaseBiz baseBiz = new BaseBiz();

    private LoginBiz biz = new LoginBiz();

	/**
	 * get请求方法
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.doPost(request, response);
	}

	/**
	 * 响应登录请求
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 操作类型
		String method = request.getParameter("method");
		Cookie[] cookies = request.getCookies();
		String langName = StaticValue.ZH_CN;
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (StaticValue.LANG_KEY.equals(cookie.getName())) {
					langName = cookie.getValue();
					break;
				}
			}
		}
		//判断验证内容
		if("checkDynamicWord".equals(method)){
			this.checkDynamicWord(request, response);
		}else{
		// 操作类型
		String opType = StaticValue.OTHER;
		// 操作内容
		String opContent = "";
		// 编码
		response.setContentType("text/html;charset=utf-8");

		PrintWriter out = response.getWriter();
		//获取客户端IP地址
		String ip = getIpAddr(request);

		//用户代理
		String userAgent = request.getHeader("User-Agent");

        //由于跳转到登录页时session已经存在或被创建
        HttpSession session = request.getSession(false);
        if(session== null || userAgent==null){
            out.print("illegal");
            EmpExecutionContext.error("非法操作！UA或会话为空,IP:"+ip);
            return;
        }
        //IP及浏览器类型
        String clientInfo = "IP:"+ip+",client:"+BrowseTool.checkBrowse(userAgent);
        //tkn合法性验证
        String tknStatus = checkToken(request);
        if("illegal".equals(tknStatus)){
            out.print(tknStatus);
            EmpExecutionContext.error("非法操作！tkn验证失败！"+clientInfo);
            return;
        }else if("expire".equals(tknStatus)){
            out.print(tknStatus);
            EmpExecutionContext.error("tkn已过期！"+clientInfo);
            return;
        }

		AuthenAtom authen = new AuthenAtom();
		// 判断是否需要验证码登录
		if("true".equals(session.getAttribute("yanzhengma")))
		{
			//用户输入验证码
			String yzm = request.getParameter("inyzm");
			//后台生成验证码内容
			String curYzm = (String) session.getAttribute("yzm");
			if(curYzm == null || !curYzm.equalsIgnoreCase(yzm))
			{
				out.print("yzfalse");
				EmpExecutionContext.error("登录失败，验证码错误，生成验证码：" +curYzm + "，传入验证码:" + yzm+","+clientInfo);
				return;
			}
		}

		// 企业编码
		String enpCode = request.getParameter("enpCode");
		// 企业编码是否存在sql注入
		if(!authen.holesProcessing(enpCode))
		{
			out.print("false");
			EmpExecutionContext.error("登录失败，企业编码请求参数被拒绝，enpCode：" + enpCode+","+clientInfo);
			return;
		}
		// 登录账号
		String username = request.getParameter("username");
		// 登录账号是否存在sql注入
		if(!authen.holesProcessing(username))
		{
			out.print("false");
			EmpExecutionContext.error("登录失败，登录账号请求参数被拒绝，username：" + username + "，enpCode：" + enpCode+","+clientInfo);
			return;
		}
		// 登录密码
		String pass = request.getParameter("pass");
		// 登录密码是否存在sql注入
		if(!authen.holesProcessing(pass))
		{
			out.print("false");
			EmpExecutionContext.error("登录失败，登录密码请求参数被拒绝，username：" + username + "，enpCode：" + enpCode+","+clientInfo);
			return;
		}

		username = authen.parseCode(username);
		// ********由于是加密所以需要解析
		pass = new String(authen.decode(pass));
		pass = authen.parseCode(pass);

		// 客户端传入ip地址
		String ipAddr = request.getParameter("ipAddr");
		// 客户端传入MAC地址
		String macAddr = request.getParameter("macAddr");
		// 手机动态口令
		String phoneword = request.getParameter("phoneword");
		// ****增加是否存在sql注入漏洞
		if(!authen.holesProcessing(phoneword))
		{
			out.print("false");
			EmpExecutionContext.error("登录失败,动态口令请求参数被拒绝，口令：" +phoneword + "，username：" + username + "，enpCode：" + enpCode+","+clientInfo);
			return;
		}
		//是否转入管理界面
		if(biz.isConfigAdmin(username,pass))
		{
            LfSysuser sysuser = new LfSysuser();
			ParamsEncryptOrDecrypt decryptObj = new ParamsEncryptOrDecrypt();
			request.getSession(false).setAttribute("decryptObj", decryptObj);
            session.setAttribute("loginIP",ip);
			session.setAttribute("loginSysuser", sysuser);
            session.setAttribute("requestDataKey", new RequestData());
			systemGlobalsSvt sg = new systemGlobalsSvt();
			sg.Init();
			session.setAttribute(StaticValue.LANG_KEY, langName);
			out.print("mananger");
			return;
		}

		//是否清除验证码
		boolean isClearYzm = true;
        LfSysuser sysuser = null;
        //返回结果
        String result = "false";
        String userName = null;
		try
		{
            sysuser =  biz.login(username, pass, enpCode);
			// 用户名或密码不对
			if(sysuser == null)
			{
				// 根据用户名及企业编码获取用户信息
				List<LfSysuser> userList = biz.getLfSysUserByUP(username.toUpperCase(), null, enpCode);
				if(userList.size() > 0)
				{
					LfSysuser errorUser = userList.get(0);
					biz.setErrorTimes(pass, errorUser);
					// 主要是用于提示密码可以输入多少次
					String limit = biz.getCorpConfByLimt(errorUser);
					if(!"".equals(limit) && !"0".equals(limit) && !"admin".equals(errorUser.getUserName()))
					{
						//当前连续错误次数
						int error = errorUser.getPwderrortimes();
						//允许的最大错误次数
						int intlimit = Integer.parseInt(limit);
						if(intlimit > error)
						{
							result = "[change];" + (intlimit - error);
						}
						else
						{
                            result = "lockuser";
						}
					}
					else
					{
                        result = "errpwd";
					}
                    EmpExecutionContext.info("登录用户密码错误！登录用户名:" + username + "，企业编码：" + enpCode + "," + clientInfo);
				}else{
                    EmpExecutionContext.error("登录用户名不存在！登录用户名:" + username + "，企业编码：" + enpCode + "," + clientInfo);
                }

				return;
			}

            Integer userState = sysuser.getUserState();
            userName = sysuser.getUserName();

			//查询到操作员 判断操作员状态
			if(userState - 3 == 0)
			{
				result = "lockuser";
				opContent = "操作员[" + userName + "]已被锁定，登录系统失败！";
				return;
			} else if(userState - 2 == 0)
			{
				// 用户已注销
                result = "deleteuser";
				opContent = "操作员[" + userName + "]已注销，登录系统失败！";
				return;
			}
			// 状态为启用状态 只要正确就清零
			else if(userState - 1 == 0)
			{
                if(!new Integer(0).equals(sysuser.getPwderrortimes()))
                {
                    sysuser.setPwderrortimes(0);
                    baseBiz.updateObj(sysuser);
                }
			}
			else if(userState - 0 == 0)
			{
				// 用户已禁用
                result = "mid";
				opContent = "操作员[" + userName + "]已禁用，登录系统失败！";
				return;
			}
			else if(sysuser.getName() == null)
			{
				opContent = "操作员[" + userName + "]操作员姓名为空，登录系统失败！";
				return;
			}

			String corpCode = sysuser.getCorpCode();
			LfCorp corp = biz.getCorp(corpCode);
			if(corp!=null && corp.getCorpState() == 0){
				// 用户已禁用
                result = "lockCorp";
				opContent = "企业[" + corpCode + "]已禁用，登录系统失败！";
				return;
			}
			
            opContent = "登录时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()) + "，ip地址：" + ip;

            //设置当前登录ip
            session.setAttribute("loginIP", ip);
            session.setAttribute("loginSysuser", sysuser);
            if(langName !=null &&!"".equals(langName)){
            	session.setAttribute(StaticValue.LANG_KEY, langName);
            	LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
    			objectMap.put("languageCode", langName);
    			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
    			conditionMap.put("userId", sysuser.getUserId()+"");
    			BaseBiz baseBiz = new BaseBiz();
    			baseBiz.update(LfSysuser.class, objectMap, conditionMap);
            }else{
            	session.setAttribute(StaticValue.LANG_KEY, sysuser.getLanguageCode());
            }
            session.setAttribute(pass + username.toLowerCase(),sysuser);
            session.setAttribute("requestDataKey", new RequestData());
            
            // 获取一级菜单
            getFirstMenu(request, response);
            // 获取一级菜单
            getSecondAndThirdMenu(request, response);
           SysuserUtil.regenerateSessionId(request);
            //安全验证 ip mac 
            result = biz.securityValidation(ipAddr, macAddr, phoneword, sysuser);
			if(!"0".equals(result)){
				if("nogetIP".equals(result)||"IPAddrerror".equals(result)
						||"nogetMacAddr".equals(result)||"MacAddrerror".equals(result)){
					opContent = "操作员[" + username + "]IP/MAC验证失败，登录系统失败！";
				}else if("nophoneword".equals(result)){
					isClearYzm  = false;
				}
				return ;
			}
            result = "true";
        }
		catch (Exception e)
		{
			result = "error";
            opContent = "登录异常！";
			EmpExecutionContext.error(e, "登录异常！username:" + username +"，enpCode:"+enpCode+","+clientInfo);
		}finally {
            out.print(result);
            // 此处session已失效，无需清除
			//验证码从session中清除
//			if(isClearYzm){
//				session.removeAttribute("yzm");
//			}
			if(!"nophoneword".equals(result)){
				if("true".equals(result))
				{
					EmpExecutionContext.info("登录成功，username：" + username + "，enpCode：" + enpCode + "，result：" + result + "," + clientInfo
							+ ",在线用户数：" + StaticValue.getLoginInfoMap().size() + ",服务节点：" + StaticValue.getServerNumber());
					spLog.logSuccessString(username, auditModule, opType, opContent, sysuser==null?"":sysuser.getCorpCode());
				}else if(sysuser != null)
				{
					EmpExecutionContext.error(opContent+"enpCode：" + enpCode+","+clientInfo);
					spLog.logFailureString(userName, auditModule, opType, opContent + opSper, null, sysuser.getCorpCode());
				}
			}

		}
		}
		try
		{
			//设置加密对象
			ParamsEncryptOrDecrypt decryptObj = new ParamsEncryptOrDecrypt();
			request.getSession(false).setAttribute("decryptObj", decryptObj);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "登录成功，设置加密对象失败!");
		}
	}

	/**
	 * 获取菜单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getFirstMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		orderbyMap.put("priOrder", StaticValue.ASC);
		String langName = (String)request.getSession().getAttribute(StaticValue.LANG_KEY);
		try {
			//if(StaticValue.menu_num!=null && !"".equals(StaticValue.menu_num.toString()))
			if(StaticValue.getMenu_num()!=null && !"".equals(StaticValue.getMenu_num().toString()))
			{
				
				//conditionMap.put("menuNum&in", StaticValue.menu_num.toString());
				conditionMap.put("menuNum&in", StaticValue.getMenu_num().toString());
			}
			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
			Map<Integer,String> thirdMenuMap = new LinkedHashMap<Integer,String>();
			Set<Integer>  numSet = getLoginUserMenuNums(request, response);
			
			if(thirdMenuList != null && thirdMenuList.size() > 0)
			{
				for(LfThiMenuControl menu : thirdMenuList)
				{
					//不拥有对应权限
					if(numSet != null&&!numSet.contains(menu.getMenuNum())){
						continue;
					}
					
					if (StaticValue.ZH_TW.equals(langName)) {
						thirdMenuMap.put(menu.getMenuNum(), menu.getZhTwTitle());
					} else if (StaticValue.ZH_HK.equals(langName)) {
						thirdMenuMap.put(menu.getMenuNum(), menu.getZhHkTitle());
					} else {
						thirdMenuMap.put(menu.getMenuNum(), menu.getTitle());
					}
					
				}
			}
			request.getSession(false).setAttribute("thirdMenuMap", thirdMenuMap);
			getCfg(request, response);
			
		}catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "svt获取菜单异常。");
		}
	}
	
	/**
	 * 获取菜单
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getSecondAndThirdMenu(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
		LinkedHashMap<String,String> orderbyMap = new LinkedHashMap<String,String>();
		orderbyMap.put("priOrder", StaticValue.ASC);
		HttpSession session = null;
		try {
			session = request.getSession(false);
			LfSysuser lfSysuser=((LfSysuser) session.getAttribute("loginSysuser"));
			LfDep lfdep=new BaseBiz().getById(LfDep.class, lfSysuser.getDepId());
			request.setAttribute("lfdep", lfdep);
		} catch (Exception e) {
			EmpExecutionContext.error(e, "获取菜单，获取操作员对象异常。");
		}
		
		try {
			List<LfThiMenuControl> thirdMenuList = new GenericEmpDAO().findListBySymbolsCondition(LfThiMenuControl.class, conditionMap, orderbyMap);
			LfThiMenuControl tt = new LfThiMenuControl();
			tt.setMenuNum(0);
			thirdMenuList.add(tt);
			request.getSession(false).setAttribute("thirdMenuList", thirdMenuList);
			
		}catch (Exception e)
		{
			//异常处理
			EmpExecutionContext.error(e, "获取菜单异常。");
		}
	}
	
	//登录用户拥有的大模块权限
		public Set<Integer> getLoginUserMenuNums(HttpServletRequest request, HttpServletResponse response){
			LoginBiz loginBiz=new LoginBiz();
			List<LfPrivilegeAndMenuVo> prList = null;
			try
			{
				LfSysuser sysuser = (LfSysuser) request.getSession(false).getAttribute("loginSysuser");
				if(sysuser == null){
					EmpExecutionContext.error("获取登录用户拥有的大模块权限，session获取失败！");
					return null;
				}
				Long lguserid = sysuser.getUserId();
				String lgcorpcode = sysuser.getCorpCode();
				prList = loginBiz.getAllMenuByUserId(lgcorpcode, lguserid.toString(),"1");
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e, "操作员权限查询失败！");
				return null;
			}
			//循环角色菜单列表，与配置文件里配置的模块匹配，没有配置的模块不显示  
			Set<Integer> numSet=new HashSet<Integer>();
			if(prList!=null && prList.size()>0)
			{
				String menuCode="";
				for(int i=0;i<prList.size();i++)
				{
					LfPrivilegeAndMenuVo priVo=prList.get(i);
					if(StaticValue.getInniMenuMap().get(priVo.getMenuSite())!=null || menuCode.equals(priVo.getMenuCode()))
					{
						menuCode=priVo.getMenuCode();
						numSet.add(priVo.getMenuNum());
					}
					//号码是否可见和运营商查看不是左边栏的模块，做特殊处理
					else if("1600-2000-0".equals(priVo.getPrivCode()) || "1600-1900-0".equals(priVo.getPrivCode()))
					{
						numSet.add(priVo.getMenuNum());
					}
				}
			}
			
			return numSet;
		}
		/**
		 * 处理个性化设置
		 * @param request
		 * @param response
		 * @throws ServletException
		 * @throws IOException
		 */
		public void getCfg(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
		{
			//查找当前登录操作员企业下个性化设置信息
			String corpCode = request.getParameter("corpcode");
			LinkedHashMap<String,String> conditionMap = new LinkedHashMap<String,String>();
			conditionMap.put("corpCode", corpCode);
			List<LfSpeUICfg> cfgs;
			try
			{
				cfgs = new GenericEmpDAO().findListByCondition(LfSpeUICfg.class, conditionMap, null);
				if(cfgs.size()>0){
					//个性化设置信息
					LfSpeUICfg cfg = cfgs.get(0);
					//多企业时将个性化信息缓存至cookie
				/*2014-05-13 取消多企业的登录页个性化
				 * 	if(StaticValue.CORPTYPE!=0){
						int maxAge=7*24*3600;
						addCookie(response, "displayType", String.valueOf(cfg.getDisplayType()), maxAge);
						addCookie(response, "loginLogo", cfg.getLoginLogo(), maxAge);
						addCookie(response, "bgImg", cfg.getBgImg(), maxAge);
						addCookie(response, "dispContent", cfg.getDispContent(), maxAge);
					}*/
					//内页个性化公司logo处理（图片是否存在 集群等）
					String url = cfg.getCompanyLogo();
					if(url!=null&&!"".equals(url.trim())){
						String servletPath=new TxtFileUtil().getWebRoot();
						File f = new File(servletPath+url);
						//文件本地不存在 且为集群时 则 从文件服务器上下载文件
						if(!f.exists()&& StaticValue.getISCLUSTER() ==1){
								CommonBiz comBiz = new CommonBiz();
								String downMsg = comBiz.downloadFileFromFileCenter(url);
								if("error".equals(downMsg)){
								EmpExecutionContext.error("从文件服务器下载文件"+url+"失败！");
								}
						}
						if(!f.exists()){
							cfg.setCompanyLogo(null);
						}
					}
					request.getSession(false).setAttribute("cfg", cfg);
				}
			}
			catch (Exception e)
			{
				EmpExecutionContext.error(e,"获取个性化设置失败！");
			}
		}	
	/**
	 * 验证token是否合法
	 * @param request 获取登录页生成tkn sessionID（tomcat非集群下生成默认长度32）+时间毫秒数（13）
	 * @return
	 */
	public String checkToken(HttpServletRequest request){
		String tkn = request.getParameter("tkn");
		int len;
		if(tkn == null || (len=tkn.length())<13){
			return "illegal";
		}
		//tkn生成时间
		String createStr = tkn.substring(len-13);
		//验证时间是否合法
		for(int i = 0; i < createStr.length(); ++i) {
			if(!Character.isDigit(createStr.charAt(i))) {
				return "illegal";
			}
		}
//		//验证时间是否在有效期内
//		long create =  Long.parseLong(createStr);
//		long cur = System.currentTimeMillis();
//		//是否在30分钟有效期内
//		if(create>cur || create+30*60*1000L<cur){
//			return false;
//		}
        HttpSession session = request.getSession(false);
		//验证session是否一致
		if(session == null || !tkn.substring(0, len - 13).equals(session.getId())){
			//满足格式验证 提示 过期
			return "expire";
		}
		return "true";
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
	 * 动态口令验证
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void checkDynamicWord(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// 操作类型
		String opType = StaticValue.OTHER;
		AuthenAtom authen = new AuthenAtom();
		// 企业编码
		String enpCode = request.getParameter("enpCode");
		// 登录账号
		String username = request.getParameter("username");
		// 客户端传入ip地址
		String ipAddr = request.getParameter("ipAddr");
		// 客户端传入MAC地址
		String macAddr = request.getParameter("macAddr");
		
		//用户代理
		String userAgent = request.getHeader("User-Agent");
		//获取客户端IP地址
		String ip = getIpAddr(request);
		// 由于跳转到登录页时session已经存在或被创建
		HttpSession session = request.getSession(false);
		if (session == null || userAgent == null) {
			response.getWriter().print("illegal");
			EmpExecutionContext.error("非法操作！UA或会话为空,IP:" + ip);
			return;
		}
		//IP及浏览器类型
		String clientInfo = "IP:"+ip+",client:"+BrowseTool.checkBrowse(userAgent);
		// 登录密码
		String pass = request.getParameter("pass");
		// 手机动态口令
		String phoneword = request.getParameter("phoneword");
		// ****增加是否存在sql注入漏洞
		if(!authen.holesProcessing(phoneword))
		{
			response.getWriter().print("false");
			EmpExecutionContext.error("登录失败,动态口令请求参数被拒绝，口令：" +phoneword + "，username：" + username + "，enpCode：" + enpCode+","+clientInfo);
			return;
		}
	    // 操作内容
	 	String opContent = "";
		//返回结果
	    String result = "false";
	    //是否清除验证码
	  	boolean isClearYzm = true;
	  	// ********由于是加密所以需要解析
	  	username = authen.parseCode(username);
	  	pass = new String(authen.decode(pass));
		pass = authen.parseCode(pass);
		
	  	LfSysuser sysuser =  biz.login(username, pass, enpCode);
	    //安全验证 ip mac 以及动态口令
	    try {
			result = biz.securityValidation(ipAddr, macAddr, phoneword, sysuser);
			if(!"0".equals(result)){
				if("nophoneword".equals(result)){
					isClearYzm  = false;
					opContent = "操作员[" + username + "]手机动态口令为空，登录系统失败！";
				}else if("overtimes".equals(result)){
					opContent = "操作员[" + username + "]手机动态口令已过期，登录系统失败！";
				}else if("phoneworderror".equals(result)){
					opContent = "操作员[" + username + "]手机动态口令错误，登录系统失败！";
				}else{
					opContent = "操作员[" + username + "]IP/MAC验证失败，登录系统失败！";
				}
				//验证失败移除session
				session.removeAttribute("loginIP");
				session.removeAttribute("loginSysuser");
				session.removeAttribute(pass + username.toLowerCase());
				session.removeAttribute("requestDataKey");
				return;
			}
			result = "true";
		} catch (Exception e) {
			 EmpExecutionContext.error("验证动态口令异常。口令：" +phoneword + "，username：" + username + "，enpCode：" + enpCode+","+clientInfo);
		}finally {
			response.getWriter().print(result);
			//验证码从session中清除
			if(isClearYzm){
				session.removeAttribute("yzm");
			}
	        if("true".equals(result))
	        {
	            EmpExecutionContext.info("登录成功，username：" + username + "，enpCode：" + enpCode + "，result：" + result + "," + clientInfo
	                    + ",在线用户数：" + StaticValue.getLoginInfoMap().size() + ",服务节点：" + StaticValue.getServerNumber());
	            spLog.logSuccessString(username, auditModule, opType, opContent, sysuser.getCorpCode());
	        }else if(sysuser != null)
	        {
	            EmpExecutionContext.error(opContent+"enpCode：" + enpCode+","+clientInfo);
	            spLog.logFailureString(username, auditModule, opType, opContent + opSper, null, sysuser.getCorpCode());
	        }
	
		}
		
	}
}
