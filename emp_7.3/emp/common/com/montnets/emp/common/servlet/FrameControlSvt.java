package com.montnets.emp.common.servlet;

import com.montnets.emp.common.biz.BalanceLogBiz;
import com.montnets.emp.common.biz.BaseBiz;
import com.montnets.emp.common.biz.CommonBiz;
import com.montnets.emp.common.biz.FrameControlBiz;
import com.montnets.emp.common.biz.LoginBiz;
import com.montnets.emp.common.biz.SysuserBiz;
import com.montnets.emp.common.constant.LoginInfo;
import com.montnets.emp.common.constant.StaticValue;
import com.montnets.emp.common.constant.SystemGlobals;
import com.montnets.emp.common.constant.ViewParams;
import com.montnets.emp.common.context.EmpExecutionContext;
import com.montnets.emp.common.entity.LfChangeLog;
import com.montnets.emp.common.tools.SysuserUtil;
import com.montnets.emp.common.vo.LfSysuserVo;
import com.montnets.emp.entity.corp.LfCorp;
import com.montnets.emp.entity.corp.LfCorpConf;
import com.montnets.emp.entity.employee.LfEmployee;
import com.montnets.emp.entity.gateway.AProInfo;
import com.montnets.emp.entity.system.LfSkin;
import com.montnets.emp.entity.system.LfTheme;
import com.montnets.emp.entity.system.LfUser2skin;
import com.montnets.emp.entity.sysuser.LfSysuser;
import com.montnets.emp.i18n.util.MessageUtils;
import com.montnets.emp.reportform.cache.DepAndUserTreeInfoCache;
import com.montnets.emp.reportform.cache.InitDataCache;
import com.montnets.emp.security.wgmsgconfig.WgMsgConfigBiz;
import com.montnets.emp.util.MD5;
import com.montnets.emp.util.PhoneUtil;
import com.montnets.emp.util.SuperOpLog;
import com.montnets.emp.util.TxtFileUtil;
import org.apache.commons.beanutils.DynaBean;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 框架相关控制Servlet
 * 
 * @author Administrator
 */
@SuppressWarnings("serial")
public class FrameControlSvt extends HttpServlet
{

	String	empRoot	= "frame";

	BaseBiz	baseBiz	= new BaseBiz();

	FrameControlBiz frameBiz = new FrameControlBiz();
	
	private LoginBiz biz = new LoginBiz();

	/**
	 * doget方法
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * dopost方法
	 */
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String method = request.getParameter("method");
		String requestUrl = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
		requestUrl = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
		try
		{
			//如果是404的请求，则跳转到finda方法
			if("error404".equals(requestUrl))
			{
				method = "find";
			}
			else if(request.getParameter("method") != null)
			{
				method = request.getParameter("method");
			}
			else
			{
				method = "find";
			}

			// 登录匹配验证
			String tkn = request.getParameter("tkn");
			int ISCLUSTER = StaticValue.getISCLUSTER();
			if(request.getSession() != null)
			{
				if(tkn != null)
				{

					// 根据token获取登录对象，存在时判断session，集群时不做判断
					if(StaticValue.getLoginInfoMap().get(tkn) != null)
					{
						// 去除loginInfoMap中的sessionid跟当前的sessionId比较
						if(!StaticValue.getLoginInfoMap().get(tkn).checkSessionId(request.getSession().getId()))
						{
							response.sendRedirect(request.getContextPath() + "/common/logoutEmp.jsp");
							return;
						}
					}
					// 不存在则退出登录
					else
					{
						response.sendRedirect(request.getContextPath() + "/common/logoutEmp.jsp");
						return;
					}
				}
			}
			else
			{
				// 获取不到session对象时，退出登录
				response.sendRedirect(request.getContextPath() + "/common/logoutEmp.jsp");
				return;
			}
			//清除缓存
			DepAndUserTreeInfoCache.clear();
			InitDataCache.INSTANCE.clear();

			Class c = this.getClass();
			Class[] parameterTypes = {HttpServletRequest.class,HttpServletResponse.class};
			Method clazzMethod = c.getMethod(method, parameterTypes);
			Object[] Objparams = {request,response};
			clazzMethod.invoke(this, Objparams);
		}
		catch (SecurityException e)
		{
			EmpExecutionContext.error(e,"svt登录/退出异常。");
		}
		catch (NoSuchMethodException e)
		{
			EmpExecutionContext.error(e,"svt登录/退出，未找到对应方法异常。");
		}
		catch (IllegalArgumentException e)
		{
			EmpExecutionContext.error(e,"svt登录/退出异常。");
		}
		catch (IllegalAccessException e)
		{
			EmpExecutionContext.error(e,"svt登录/退出异常。");
		}
		catch (InvocationTargetException e)
		{
			EmpExecutionContext.error(e,"svt登录/退出异常。");
		}
	}

	/**
	 * 浏览器输入地址时判断跳转
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession();
		// 获取请求路径
		String requestUrl = request.getServletPath() + (request.getPathInfo() == null ? "" : request.getPathInfo());
		requestUrl = requestUrl.substring(requestUrl.lastIndexOf("/") + 1);
		// 退出登录操作
		if("logout".equals(requestUrl))
		{
			// LfSysuser lfSysuser = (LfSysuser)
			// session.getAttribute("loginSysuser");
			@SuppressWarnings("unchecked")
			java.util.Enumeration<String> e = session.getAttributeNames();
			String sessionName = null;
			// 循环系统中的session，并移除掉。
			while(e.hasMoreElements())
			{
				sessionName = e.nextElement();
				session.removeAttribute(sessionName);
			}
			session.invalidate();
			// 请求路径页面
			response.sendRedirect(request.getContextPath());

		}
		else
		{
			// 登录页 修复会话标识未更新漏洞
			final String loginUrl = "emp_tz.hts";
			if(loginUrl.equals(requestUrl)){
				session = SysuserUtil.regenerateSessionId(request);
			}

			// 获取是多企业还是单企业
			int corpType = StaticValue.getCORPTYPE();
			String isMulti = corpType == 1 ? "true" : "false";


			String tkn = session.getId() + String.valueOf(System.currentTimeMillis());

			// 判断是否需要验证码登录
			//String isYanzhengma = SystemGlobals.getValue(StaticValue.EMP_LOGIN_YANZHENGMA);
			//从缓存中获取是否需要验证码的配置 modify by tanglili 20210201
			String isYanzhengma = SystemGlobals.getSysParam("loginYanZhengMa");
			// 将验证码存入session中
			if(isYanzhengma != null)
			{
				session.setAttribute("yanzhengma", isYanzhengma);
			}
			else
			{
				session.setAttribute("yanzhengma", "true");
			}

			request.setAttribute("yanzhengma", isYanzhengma == null ? "true" : isYanzhengma);
			request.setAttribute("isMulti", isMulti);
			request.setAttribute("tkn", tkn);
			// 请求跳转页面
			request.getRequestDispatcher("/frame/frame3.0/login.jsp").forward(request, response);
		}
	}

	/**
	 * 托管版获取企业logo
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void picTz(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter writer = response.getWriter();
		String dirUrl = this.empRoot + "/" + SystemGlobals.getValue(StaticValue.EMP_WEB_FRAME) + StaticValue.LOGOPATH;
		String imgUrl = request.getParameter("imgUrl");
		//如果没有传值
		if(imgUrl != null && !"".equals(imgUrl))
		{
			//判断logo图片是否存在，不存在则使用默认的logo
			File dirFile = new File(new TxtFileUtil().getWebRoot() + dirUrl + imgUrl);
			if(!dirFile.exists())
			{
				imgUrl = "logo_1.png";
			}
		}
		else
		{
			imgUrl = "logo_1.png";
		}
		writer.print(request.getContextPath() + "/" + dirUrl + imgUrl);
	}

	/**
	 * 获取登录信息
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public void getLoginInfoMap(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		Map<String, LoginInfo> loginMap = StaticValue.getLoginInfoMap();
		Iterator<String> itr = loginMap.keySet().iterator();
		String key;

		// 表格html字符创
		StringBuffer sb = new StringBuffer("<table id='content'><thead><tr align='center'><th border=1>TOKEN</th>" +
				"<th border=1>SessionId</th>" +
				"<th border=1>UserId</th>" +
				"<th border=1>UserName</th>" +
				"<th border=1>GuId</th>" +
				"<th border=1>CorpCode</th></tr></thead>");
		// 循环遍历拼接
		while(itr.hasNext())
		{
			sb.append("<tr align='center'>");
			key = itr.next();
			LoginInfo logininfo = loginMap.get(key);
			sb.append("<td border=1>" + key + "</td>");
			sb.append("<td border=1>" + logininfo.getSessionId() + "</td>");
			sb.append("<td border=1>" + logininfo.getUserId() + "</td>");
			sb.append("<td border=1>" + logininfo.getUserName() + "</td>");
			sb.append("<td border=1>" + logininfo.getGuId() + "</td>");
			sb.append("<td border=1>" + logininfo.getCorpCode() + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");

		request.setAttribute("tableStr", sb.toString());
		request.getRequestDispatcher(this.empRoot + "/frame3.0/loginInfo.jsp?timee=" + System.currentTimeMillis()).forward(request, response);
	}

	/**
	 * 换肤操作
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void setSkin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//选择的皮肤
		String skin = request.getParameter("skin");
		//操作员id
		String userid = request.getParameter("userid");
		//皮肤所属主题
		String themeCode = request.getParameter("themeCode");
		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		conditionMap.put("userid", userid);

		BaseBiz baseBiz = new BaseBiz();
		//查找该操作员的换肤记录
		List<LfUser2skin> userskinList = baseBiz.getByCondition(LfUser2skin.class, conditionMap, null);
		if("frame2.5".equalsIgnoreCase(themeCode) || "frame3.0".equalsIgnoreCase(themeCode) || "frame4.0".equalsIgnoreCase(themeCode)) {
            if(userskinList != null && userskinList.size() > 0)
            {
                //找到记录则更新皮肤字段为新选择的皮肤
                LfUser2skin u2S = userskinList.get(0);
                u2S.setSkincode(skin);
                u2S.setThemecode(themeCode);
                baseBiz.updateObj(u2S);
            }
            else
            {
                //找不到记录则新增一条记录
                LfUser2skin u2S = new LfUser2skin();
                u2S.setSkincode("default");
                u2S.setUserid(Long.valueOf(userid));
                u2S.setThemecode(themeCode);
                u2S.setSid(1l);

                baseBiz.addObj(u2S);
            }
        }

		//设置框架
		request.getSession().setAttribute(StaticValue.EMP_WEB_FRAME, themeCode);
		request.getSession().setAttribute("stlyeSkin", request.getContextPath() + "/frame/" + themeCode + "/skin/" + skin);
	}

	/**
	 * 加载主题对应的皮肤
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getSkinList(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		String frame = (String) request.getSession().getAttribute(StaticValue.EMP_WEB_FRAME);
		//选择的主题
		String themecode = request.getParameter("themecode");
		//如果没有选择主题，则加载默认的主题
		if(themecode != null && !"".equals(themecode))
		{
			frame = themecode;
		}

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		conditionMap.put("themecode", frame);
		//获取主题对应的皮肤列表
		List<LfSkin> skinList = baseBiz.getByCondition(LfSkin.class, conditionMap, null);
		StringBuffer sb = new StringBuffer("<div class='themeSkin' framecode='" + frame + "' style='width:100%' >");
		if(skinList != null && skinList.size() > 0)
		{
			for (LfSkin sk : skinList)
			{
				String skcode = sk.getSkincode();
				String skinName = sk.getSkinname();
				if("极简蓝（默认）".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_1",request);
				}else if("新春贺岁".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_2",request);
				}else if("午后时光".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_3",request);
				}else if("化猫物语".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_4",request);
				}else if("炫动心情".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_5",request);
				}else if("清晨拂晓".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_6",request);
				}else if("清爽夏日".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_7",request);
				}else if("经典蓝（默认）".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_8",request);
				}else if("Wood风".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_9",request);
				}else if("经典绿".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_10",request);
				}else if("经典红".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_11",request);
				}else if("商务黑".equals(skinName)){
					skinName = MessageUtils.extractMessage("common","common_skinname_12",request);
				}
				if(!"经典蓝（默认）".equals(skinName)&&!"经典红".equals(skinName)) {
                //屏蔽经典蓝和经典红					
				sb.append("<div style='padding:5px;float:left'><div class='perSkin skin_").append(skcode).append("'  onclick='changeSkin2(\"").append(sk.getSkincode()).append("\",\"").append(frame).append("\")'>").append("<div class='skinPic' style='background:url(").append(request.getContextPath()).append("/frame/").append(frame).append("/skin/").append(skcode).append("/images/skinpreview.jpg) no-repeat;'></div>").append("<div class='uselb'>").append(skinName).append("</div></div></div>");
				}
			}
		}
		sb.append("</div>");
		writer.print(sb.toString());
		writer.flush();
		writer.close();
	}

	/**
	 * 获取主题列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getTheme(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		//获取主题集合
		List<LfTheme> themeList = baseBiz.getEntityList(LfTheme.class);
		String patch = request.getParameter("patch");
		StringBuffer sb = new StringBuffer();
		String userFrame = (String) request.getSession().getAttribute(StaticValue.EMP_WEB_FRAME);
		if(themeList != null && themeList.size() > 0)
		{
			int themeSize = themeList.size();
			int index = 0;
			for (LfTheme theme : themeList)
			{
				index++;
				String themecode = theme.getThemecode();
				sb.append("<span style='height:16px;float:left;line-height:16px;background:url(" + patch + "/img/icon_" + theme.getThemesrc() + (userFrame.equals(themecode) ? "_sel" : "") + ".png) left top no-repeat;text-indent:22px;cursor:pointer'" + " class='themeSpan " + (userFrame.equals(themecode) ? "fontsel" : "") + "' " + " themeCode='" + themecode + "' onclick='getThemeSkin(\"" + themecode + "\")'>" + theme.getThemename() + "</span>");
				if(index < themeSize)
				{
					sb.append("<span  style='height:14px;float:left;line-height:14px;border-left:0;border-top:0;border-bottom:0;width:10px;' class='div_bd'></span>");
					sb.append("<span  style='height:14px;float:left;width:10px;'></span>");
				}
			}
		}
		writer.print(sb.toString());
		writer.flush();
		writer.close();
	}

	/**
	 * 获取操作员信息
	 * 
	 * @param request
	 * @param response
	 */
	public void getSysUserInfo(HttpServletRequest request, HttpServletResponse response){
		PrintWriter out = null;
		String result = "error";
		try
		{
			response.setContentType("text/html");
			out = response.getWriter();
			// 登录用户id
			//String lguserid = request.getParameter("lguserid");
			//漏洞修复 session里获取操作员信息
			String lguserid = SysuserUtil.strLguserid(request);

			if(lguserid == null || "".equals(lguserid.trim())){
				return;
			}
			Long userId = Long.parseLong(lguserid);

			LfSysuserVo sysuserVo = new LfSysuserVo();
			sysuserVo = new SysuserBiz().getSysuserVoByUserId(userId);
			result = sysuserVo.getUserName() + "&" + sysuserVo.getName() + "&" + sysuserVo.getSex() + "&" + sysuserVo.getMobile() + "&" + sysuserVo.getOph() + "&" + sysuserVo.getEMail() + "&" + sysuserVo.getQq();
		}
		catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "svt获取操作员信息异常。");
		}finally {
			if(out != null){
				out.print(result);
			}
		}
	}

	/**
	 * 修改个人信息（密码）
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void update(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		String opModule = StaticValue.USERS_MANAGER;
		String oppType = "";
		String opContent = "";
		SuperOpLog spLog = new SuperOpLog();
		HttpSession session = request.getSession();
		// 用户名
		String username = request.getParameter("username");
		SysuserBiz sysuserBiz = new SysuserBiz();
		// 类型
		String type = request.getParameter("type");
		Long lgguid = Long.parseLong(request.getParameter("lgguid"));
		LfSysuser sysuser = null;
		try
		{
			// 获取用户信息
			sysuser = baseBiz.getByGuId(LfSysuser.class, lgguid);
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "修改个人信息，获取用户信息异常。");
			writer.print("false");
		}
		String opUser = sysuser != null? sysuser.getUserName() : null;
		// (LfSysuser) session.getAttribute("loginSysuser");
		boolean result = false;

		if(type.equals("phone"))
		{
			// 修改个人信息
			oppType = StaticValue.UPDATE;
			opContent = "修改操作员" + username + "个人信息";
			String userId = sysuser !=null? sysuser.getUserId().toString():"";
			// 手机
			String mobile = request.getParameter("mobile");
			// email
			String EMail = request.getParameter("EMail");
			// 座机
			String oph = request.getParameter("oph");
			// qq
			String qq = request.getParameter("qq");
			try
			{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				objectMap.put("mobile", mobile);
				objectMap.put("EMail", EMail);
				objectMap.put("oph", oph);
				objectMap.put("qq", qq);

				// 用户id
				conditionMap.put("userId", userId);

				LfEmployee employee = baseBiz.getByGuId(LfEmployee.class, sysuser!=null? sysuser.getGuId():null);
				if(employee != null)
				{
					employee.setEmail(EMail);
					employee.setQq(qq);
					employee.setMobile(mobile);
					employee.setOph(oph);
					baseBiz.updateObj(employee);
				}
				result = baseBiz.update(LfSysuser.class, objectMap, conditionMap);
				session.setAttribute("loginSysuser", sysuser);
				response.getWriter().print(result);
			}
			catch (Exception e)
			{
				// 异常处理
				EmpExecutionContext.error(e, "修改个人信息异常。");
				writer.print("false");
			}
			finally
			{
				if(result)
				{
					// 日志
					spLog.logSuccessString(opUser, opModule, oppType, opContent, sysuser == null ? "" :sysuser.getCorpCode());
				}
				else
				{
					spLog.logFailureString(opUser, opModule, oppType, opContent + StaticValue.OPSPER, null, sysuser !=null?sysuser.getCorpCode():null);
				}
				//补充操作日志
				if(result){
					Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
					if(loginSysuserObj!=null){
						LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
						EmpExecutionContext.info("模块名称：个人设置，企业："+loginSysuser.getCorpCode()+"，"
						+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
						+"个人信息修改(新的手机号码为："+mobile+")成功。");
					}
				}
			}
		}
		else
			if(type.equals("pass"))
			{
				// 修改密码
				oppType = StaticValue.UPDATE;
				opContent = "修改操作员" + username + "密码";
				String pass = request.getParameter("pass");
				String agoPass = request.getParameter("agoPass");
				if(sysuser==null){
					sysuser=new LfSysuser();
				}
				
				if(!MD5.getMD5Str(agoPass).equals(sysuser.getPassword()) && !MD5.getMD5Str(agoPass + sysuser.getUserName().toLowerCase()).equals(sysuser.getPassword()))
				{
					writer.print("mid");
				}
				else
				{
					try
					{	
						//在最后修改密码之前再次加上校验
						
						result = sysuserBiz.modifyPassword(sysuser, pass);
						sysuser.setPassword(MD5.getMD5Str(pass + sysuser.getUserName().toLowerCase()));
						session.setAttribute("loginSysuser", sysuser);
						session.removeAttribute("NameAndPWord");
						response.getWriter().print(result);
					}
					catch (Exception e)
					{
						// spLog.logFailureString(opUser, opModule, oppType,
						// opContent+opSper, e,getCorpCode());
						EmpExecutionContext.error(e, "svt修改密码异常。");
						writer.print("false");
					}
					finally
					{
						if(result)
						{
							spLog.logSuccessString(opUser, opModule, oppType, opContent, sysuser.getCorpCode());

						}
						else
						{
							spLog.logFailureString(opUser, opModule, oppType, opContent + StaticValue.OPSPER, null, sysuser.getCorpCode());
						}
						//补充操作日志
						if(result){
							Object loginSysuserObj=request.getSession().getAttribute("loginSysuser");
							if(loginSysuserObj!=null){
								LfSysuser loginSysuser=(LfSysuser)loginSysuserObj;
								EmpExecutionContext.info("模块名称：个人设置，企业："+loginSysuser.getCorpCode()+"，"
								+"操作员："+loginSysuser.getUserId()+"["+loginSysuser.getUserName()+"]，"
								+opContent+"成功。");
							}
						}
					}
				}
			}
	}

	/***
	 *  用于校验密码是否符合一个规则
	 *  位数要求、组合形式，初始化密码不受此条件约束，修改密码受此限制，当不满足条件时，提示：“密码设置不得少于n位，且至少包含数字、字母、符号”，保存失败
	 * @return
	 */
	public void  valid(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String result = "";
		PrintWriter writer = response.getWriter();
		try
		{
			LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
			LfCorp lfcorp=(LfCorp)request.getSession().getAttribute("loginCorp");
			conditionMap.put("corpCode", lfcorp.getCorpCode());
			String bit="0"; 
			String number="";
			String character ="";
			String sign="";
			String modiftime="";
			String remind="";
			String errorlimt="";
			List<LfCorpConf> lfCorpConfList= baseBiz.getByCondition(LfCorpConf.class, conditionMap, null);

			for (int i = 0; i < lfCorpConfList.size(); i++)
			{
				LfCorpConf conf = lfCorpConfList.get(i);
				// 位数

				if("pwd.count".equals(conf.getParamKey()))
				{
					bit = conf.getParamValue();
				}
				// 组合形式
				if("pwd.combtype".equals(conf.getParamKey()))
				{
					String cunt = conf.getParamValue();
					if(cunt != null)
					{
						String[] per = cunt.split(",");
						if(per.length == 1)
						{
							number = per[0];
						}
						else if(per.length == 2)
						{
							number = per[0];
							character = per[1];
						}
						else if(per.length == 3)
						{
							number = per[0];
							character = per[1];
							sign = per[2];
						}
					}
				}
				// 修改周期
				if("pwd.upcycle".equals(conf.getParamKey()))
				{
					modiftime = conf.getParamValue();
				}
				// 过期提醒
				if("pwd.pastalarm".equals(conf.getParamKey()))
				{
					remind = conf.getParamValue();
				}
				// 错误上限
				if("pwd.errlimit".equals(conf.getParamKey()))
				{
					errorlimt = conf.getParamValue();
				}
			}
			result=bit+"@"+number+"@"+character+"@"+sign+"@"+modiftime+"@"+remind+"@"+errorlimt;
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查询密码设置规则异常！");
		}
		finally
		{
			writer.print(result);
		}
	}
	
	
	
	/**
	 * 验证手机号码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkMobile(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		// 手机号码
		String mobile = request.getParameter("mobile");
		// 分隔符
		String separator = request.getParameter("separator");
		// 运营商类型
		String spisuncm = request.getParameter("spisuncm");

		WgMsgConfigBiz mtBiz = new WgMsgConfigBiz();

		int result = 0;
		try
		{
			Pattern pa = Pattern.compile("( {2,})|\n|\r");
			Matcher m = pa.matcher(mobile);
			mobile = m.replaceAll("");
			String[] numbers = mobile.split(separator);
			String[] haoduan = mtBiz.getHaoduan();
			if(spisuncm == null)
			{
				for (int i = 0; i < numbers.length; i++)
				{
					if(!numbers[i].equals(""))
					{
						result = mtBiz.checkMobile(numbers[i], haoduan);
						if(result == 0)
						{
							break;
						}
					}
				}
			}
			else
			{
				for (int i = 0; i < numbers.length; i++)
				{
					if(!numbers[i].equals(""))
					{
						result = mtBiz.checkMobile(numbers[i], Integer.valueOf(spisuncm), haoduan);
						if(result == 0)
						{
							break;
						}
					}
				}
			}
		}
		catch (Exception e)
		{
			result = 2;
			EmpExecutionContext.error(e, "svt验证手机号码异常。");
		}
		finally
		{
			writer.print(result);
		}
	}
	
	/**
	 * 个人设置验证手机号码
	 * @description    
	 * @param request
	 * @param response
	 * @throws Exception       			 
	 * @author zhangmin
	 * @datetime 2014-4-16 下午05:28:22
	 */
	public void checkMobileByInfo(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		// 手机号码
		String mobile = request.getParameter("mobile");

		WgMsgConfigBiz mtBiz = new WgMsgConfigBiz();
		
		PhoneUtil phoneUtil = new PhoneUtil();

		int result = 3;
		try
		{
			String[] haoduan = mtBiz.getHaoduan();
			result = phoneUtil.getPhoneType(mobile, haoduan);
			if(result == -1)
			{
				result=0;
			}
			else if(result==0)
			{
				result = 3;
			}else if(result==2){
				result = 4;
			}
		}
		catch (Exception e)
		{
			result = 2;
			EmpExecutionContext.error(e, "svt验证手机号码异常。");
		}
		finally
		{
			writer.print(result);
		}
	}

	/**
	 * 验证密码
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void checkPass(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		PrintWriter writer = response.getWriter();
		HttpSession session = request.getSession();
		// 老密码
		String agoPass = request.getParameter("agoPass");

		try
		{
			// 获取当前登录用户的信息
			LfSysuser sysuser = (LfSysuser) session.getAttribute("loginSysuser");

			// 检查密码
			if(MD5.getMD5Str(agoPass).equals(sysuser.getPassword()) || MD5.getMD5Str(agoPass + sysuser.getUserName().toLowerCase()).equals(sysuser.getPassword()))
			{

				writer.print("true");
			}
			else
			{
				writer.print("false");
			}

		}
		catch (Exception e)
		{
			// 异常日志
			EmpExecutionContext.error(e, "svt验证密码异常。");
		}

	}

	/**
	 * 获取当前发送余额
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getCt(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		BalanceLogBiz biz = new BalanceLogBiz();

		PrintWriter out = response.getWriter();
		Long maxcount = null;
		Long maxMmsConunt = null;
		// 返回结果
		String result = "";
		//String id = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String id = SysuserUtil.strLguserid(request);


		long userId;
		// 登录用户id
		if(id == null || "".equals(id) || "0".equals(id))
		{
			EmpExecutionContext.error("svt传入的操作员ID为空");
			userId = ((LfSysuser) request.getSession().getAttribute("loginSysuser")).getUserId();
		}
		else
		{
			userId = Long.parseLong(id);
		}
		LfSysuser syuser = null;
		try
		{
			syuser = baseBiz.getLfSysuserByUserId(userId);
			if(syuser == null){
				EmpExecutionContext.error("余额查询未获取到对应操作员信息。");
				return;
			}
			if(!biz.IsChargings(syuser.getUserId()))
			{
				out.print("nojifei");
			}
			else
			{
				maxcount = biz.getAllowSmsAmount(syuser);
				maxMmsConunt = biz.getAllowMmsAmount(syuser);
				if(maxcount == null && maxMmsConunt == null)
				{
					result = "^";
				}
                maxcount = (maxcount == null?0:maxcount);
                maxMmsConunt = (maxMmsConunt == null?0:maxMmsConunt);
				out.print(result + "ye" + maxcount + "," + maxMmsConunt);
			}
			out.flush();
			out.close();
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "svt获取当前发送余额异常。");
		}
	}

	/**
	 * frame2.5跳转到换肤界面
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void toChangeSkin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//String lguserid = request.getParameter("lguserid");
		//漏洞修复 session里获取操作员信息
		String lguserid = SysuserUtil.strLguserid(request);

		LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();

		conditionMap.put("userid", lguserid);
		//获取当前操作员的主题配置信息
		List<LfUser2skin> userskinList = baseBiz.getByCondition(LfUser2skin.class, conditionMap, null);
		LfUser2skin u2S = null;
		if((userskinList != null) && (userskinList.size() > 0))
		{
			u2S = (LfUser2skin) userskinList.get(0);
		}
		//获取主题集合
		List<LfTheme> themeList = baseBiz.getEntityList(LfTheme.class);
		//获取皮肤集合
		List<LfSkin> skinList = baseBiz.getEntityList(LfSkin.class);
		//主题皮肤对应集合Map<key-主题，value-皮肤集合>
		Map<String, List<LfSkin>> themeSkinMap = new HashMap<String, List<LfSkin>>();
		if(themeList != null && themeList.size() > 0)
		{
			for (LfTheme theme : themeList)
			{
				themeSkinMap.put(theme.getThemecode(), new ArrayList<LfSkin>());
			}
		}
		if(skinList != null && skinList.size() > 0)
		{
			for (LfSkin skin : skinList)
			{
				if(themeSkinMap.get(skin.getThemecode()) != null)
				{
					themeSkinMap.get(skin.getThemecode()).add(skin);
				}
			}
		}
		request.setAttribute("themeList", themeList);
		request.setAttribute("themeSkinMap", themeSkinMap);
		request.setAttribute("u2S", u2S);
		request.getRequestDispatcher("/frame/" + request.getSession().getAttribute(StaticValue.EMP_WEB_FRAME) + "/skin.jsp").forward(request, response);
	}

	/**
	 * 查找网关认证信息
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void showAbout(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AProInfo proInfo = null;
		Map<String,String[]> versions = new HashMap<String, String[]>();
        List<LfChangeLog> changeLogs = new ArrayList<LfChangeLog>();
		String dbVersion = null;
		try
		{
			// 查找网关认证系信息
			List<AProInfo> proList = baseBiz.getByCondition(AProInfo.class, null, null);
			if(proList != null && proList.size() > 0)
			{
				proInfo = proList.get(0);
			}
			//查询各版本信息
			//processid 1000:EMP-WEB,2000:E网关WBS,3000:网关SPGATE
//			String processIds = "1000,2000,3000";
			versions = frameBiz.getVersionInfos(null);

			//查询数据库版本信息
			dbVersion = frameBiz.getDbVersion();

            Map<String,String> changeLogMaps = getChangeLogs();
            List<DynaBean> changes = frameBiz.getVersionHis();
            LfChangeLog changeLog = null;
            if(changes != null)
            {
                String ver = null;
                String updatetime = null;
                String content = null;
                for (DynaBean change : changes) {
                    ver = change.get("version").toString();
                    updatetime = change.get("updatetime").toString();
//                    if(changeLogMaps.containsKey(ver))
//                    {
                        content = change.get("versioninfo").toString();//changeLogMaps.get(ver);
                        if(content!=null&&!"".equals(content.trim())){
	                        changeLog = new LfChangeLog();
	                        changeLog.setReleaseNote(content);
	                        changeLog.setReleasetime(updatetime.substring(0,10));
	                        changeLog.setMajorversion("V"+ver);
	                        changeLogs.add(changeLog);
                        }
//                    }
                }
            }
		}
		catch (Exception e)
		{
			EmpExecutionContext.error(e, "查找网关认证信息失败！");
		}
		finally
		{
			request.setAttribute("proInfo", proInfo);
			request.setAttribute("versions",versions);
			request.setAttribute("dbVersion",dbVersion);
			request.setAttribute("changeLogs",changeLogs);
			request.getRequestDispatcher("/common/aboutEmp.jsp").forward(request, response);
		}

	}

    public Map<String,String> getChangeLogs(){
        Map<String,String> changeLogs = new HashMap<String, String>();
        String webRoot = new TxtFileUtil().getWebRoot();
        //版本文件
        File file = new File(webRoot,"common/file/changelog.xml");
        SAXReader reader = new SAXReader();
        Document document = null;
        try
        {
            document = reader.read(new FileInputStream(file));
            // 得到根元素
            Element root = document.getRootElement();
            //得到列表
            List<Element> rootElementList = root.elements();
            String ver = null;
            StringBuffer stringBuffer = new StringBuffer();
            List<Element> lis = null;
            for (Element version : rootElementList)
            {
                ver = version.attributeValue("ver");
                lis = version.elements();
                for (int i = 0; i < lis.size(); i++) {
                    stringBuffer.append((i+1)+"、"+lis.get(i).getTextTrim());
                    stringBuffer.append("<br/>");
                }
                changeLogs.put(ver,stringBuffer.toString());
                stringBuffer.setLength(0);
            }
        }
        catch (Exception e)
        {
            EmpExecutionContext.error(e,"解析版本详情文件异常！");
        }

        return changeLogs;
    }

    /**
     *接收文件服务器通知 知会其他EMP节点下载
     * @param request
     * @param response
     * @throws IOException
     */
    public void notice(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String url = request.getParameter("url");
        String result = "error";
        if(url != null)
        {
            result = new CommonBiz().uploadFileToOtherNodes(url);
        }
        OutputStream os = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/octet-stream");
        response.setContentLength(result.getBytes().length);
        os.write(result.getBytes());
        os.close();
    }

    /**
     * 从指定路径下载文件
     * @param request
     * @param response
     */
    public void download(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = request.getParameter("url");
        String result = new CommonBiz().downloadFileFromFileCenter(url);
        OutputStream os = response.getOutputStream();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/octet-stream");
        response.setContentLength(result.getBytes().length);
        os.write(result.getBytes());
        os.close();
    }

    public void checkTkn(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
    	// 请求路径
    	String requestUrl = request.getServletPath();
		EmpExecutionContext.info("tkn验证通过"+"url:"+requestUrl);
		response.getWriter().print("true");
	}
    
    
    /**
     * 切换语言
     *@anthor qiyin<15112605627@163.com>
     *@param request
     *@param response
     *@throws Exception
     */
    public void refreshLanguage(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String temp="";
    	try
		{
//			LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
//			MessageUtils.extractMessage("comm", "", this.getp)
			temp = " <ul id=\"uChildren\" class=\"\">";
			List<String> langs = (List)request.getSession().getAttribute("multiLanguage");
			if(null != langs && !langs.isEmpty()){
				for (String str : langs) {
					String langName = "zh_HK".equals(str)?"English":"zh_TW".equals(str)?"繁體":"简体";
					temp += "<li><a href=\"#\" onclick=\"javascript:changeLanguage('"+str+"')\">"+langName+"</a></li>";
				}
			}
			temp += "</ul>";
		} catch (Exception e)
		{
			EmpExecutionContext.error(e,"切换语言异常");
		}finally{
			response.getWriter().print(temp);
		}
    }
    
    /**
     * 个人设置
     *@anthor qiyin<15112605627@163.com>
     *@param request
     *@param response
     *@throws Exception
     */
    public void refreshPersonSet(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String temp="";
    	try
    	{
    		LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
    		String common_personalSetting = MessageUtils.extractMessage("common", "common_personalSetting", request);
    		String common_helpManual = MessageUtils.extractMessage("common", "common_helpManual", request);
    		String common_aboutPlatform = MessageUtils.extractMessage("common", "common_aboutPlatform", request);
    		temp = " <ul class=\"uChildren\" id=\"\">";
    		temp+="<li><a href=\"#\" onclick=\"doUpdatePass()\">"+common_personalSetting+"</a></li>";
   			temp+="<li><a href=\"#\" onclick=\"parent.frames['topFrame'].window.upLoad()\">"+common_helpManual +"</a></li>";
   			temp+="<li><a href=\"#\" onclick=\"showAbout()\">"+common_aboutPlatform+"</a></li>";
   			temp+="</ul>";
   		
    	} catch (Exception e)
    	{
    		EmpExecutionContext.error(e,"切换菜单异常");
    	}finally{
    		response.getWriter().print(temp);
    	}
    }
    
    /**
     * 更新计费菜单
     *@anthor qiyin<15112605627@163.com>
     *@param request
     *@param response
     *@throws Exception
     */
    public void refreshBalance(HttpServletRequest request,HttpServletResponse response) throws Exception
    {
    	String temp="";
    	try
    	{
    		LfSysuser user = (LfSysuser) request.getSession().getAttribute("loginSysuser");
    		String common_frame2_main_8 = MessageUtils.extractMessage("common", "common_frame2_main_8", request);
    		String common_frame2_main_9 = MessageUtils.extractMessage("common", "common_frame2_main_9", request);
    		String common_frame2_main_10 = MessageUtils.extractMessage("common", "common_frame2_main_10", request);
    		
    		String corpcode=user.getCorpCode();
    		//是否启用计费机制
    		boolean isCharging = SystemGlobals.isDepBilling((corpcode == null || "".equals(corpcode.trim())) ? "100001": corpcode);
    		Map<String, String> btnMap = (Map<String, String>) request.getSession().getAttribute("btnMap");
			//是否显示运营商余额
			boolean isShowYe = btnMap.get(ViewParams.YECODE+"-0") == null ? false : true;
    		temp+="<ul class=\"uChildren\" id=\"\">";
      		if (isCharging) 
              {
      			temp+="	<li id=\"smsYe\"><a href=\"#\" >"+common_frame2_main_8 +"：<span id=\"ctSms\" style=\"color:red;\">0</span></a></li>";
      			temp+="<li id=\"mmsYe\"><a href=\"#\" >"+common_frame2_main_9 +"：<span id=\"ctMms\" style=\"color:red\">0</span></a></li>";
      		} 
      		if(isShowYe){ 
      			temp+="	<li><a href=\"#\" id=\"checkfee\" onclick=\"parent.frames['topFrame'].window.checkFee()\">"+common_frame2_main_10 +"</a></li>";
    		} 
      		temp+="</ul>";
    		
    	} catch (Exception e)
    	{
    		EmpExecutionContext.error(e,"切换菜单异常");
    	}finally{
    		response.getWriter().print(temp);
    	}
    }
    
    /**
	 * 换肤操作
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void setLangeuage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		//语言类型，1为繁体，2为英语，3为简体
		String lang = request.getParameter("lang");
		//操作员id
		String userid = request.getParameter("userid");
		String result="false";
		PrintWriter writer = response.getWriter();
		try
		{
			String value = StaticValue.ZH_CN;
			if (StaticValue.ZH_CN.equals(lang))
			{
				value =StaticValue.ZH_CN;
			} else if (StaticValue.ZH_TW.equals(lang))
			{
				value = StaticValue.ZH_TW;
			} else if (StaticValue.ZH_HK.equals(lang))
			{
				value = StaticValue.ZH_HK;
			}
			if(null==userid){
				Cookie cookie=new Cookie(StaticValue.LANG_KEY, value);
				cookie.setMaxAge(365*24*60*60);
				response.addCookie(cookie);
				request.getSession().setAttribute(StaticValue.LANG_KEY, value);
				result="true";
			}else{
				LinkedHashMap<String, String> objectMap = new LinkedHashMap<String, String>();
				objectMap.put("languageCode", value);
				LinkedHashMap<String, String> conditionMap = new LinkedHashMap<String, String>();
				conditionMap.put("userId", userid);
				BaseBiz baseBiz = new BaseBiz();
				Boolean flag = baseBiz.update(LfSysuser.class, objectMap, conditionMap);
				if (flag)
				{
					//设置session的languageMap
					HttpSession session= request.getSession();
//					LfSysuser user = (LfSysuser) session.getAttribute("loginSysuser");
//					user.setLanguageCode(value);
//					chooseLanguage(user);
					Cookie cookie=new Cookie(StaticValue.LANG_KEY, value);
					cookie.setMaxAge(365*24*60*60);
					response.addCookie(cookie);
					request.getSession().setAttribute(StaticValue.LANG_KEY, value);
					
					//更改菜单
					/*List<LfPrivilege> prList = biz.getMenuByPrivCodeAsc(userid,request);
					request.getSession().setAttribute("prList", prList);
					
					// 更改当前位置菜单显示
				    Map<String, String> titleMap = biz.getTitleMap(user.getUserId().toString(),request);
				    request.getSession().setAttribute("titleMap", titleMap);
				  
				    Map<String, String> btnMap = biz.getBtnFuntionMap(user.getUserId().toString(),request);
				    request.getSession().setAttribute("btnMap", btnMap);
				    Map<String, List<LfPrivilege>> priMap = new HashMap<String, List<LfPrivilege>>();
					for (LfPrivilege privi : prList)
					{
						// 该模块没有配置
						if (StaticValue.inniMenuMap.get(privi.getMenuSite()) == null)
						{
							continue;
						}
						String resourceId = privi.getResourceId().toString();
						if (priMap.get(resourceId) != null)
						{
							priMap.get(resourceId).add(privi);
						} else
						{
							List<LfPrivilege> pri = new ArrayList<LfPrivilege>();
							pri.add(privi);
							priMap.put(resourceId, pri);
						}
					}
					
					request.getSession().setAttribute("priMap", priMap);*/
					result="true";
				}
				//设置框架
				//		request.getSession().setAttribute(StaticValue.EMP_WEB_FRAME, themeCode);
				//		request.getSession().setAttribute("stlyeSkin", request.getContextPath() + "/frame/" + themeCode + "/skin/" + skin);
			}
		} catch (Exception e)
		{
			// 异常处理
			EmpExecutionContext.error(e, "设置语言异常");
		}finally{
			writer.print(result);
			writer.flush();
			writer.close();
		}
	}
}
